// com/seoyoungjae/auth/service/TotpService.java
package com.seoyoungjae.auth.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.seoyoungjae.auth.domain.User;
import com.seoyoungjae.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

@Service
@RequiredArgsConstructor
public class TotpService {

  private final UserRepository userRepository;
  private final Base32 base32 = new Base32();
  private final TimeBasedOneTimePasswordGenerator totp =
      new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30)); // 30초

  public String generateSecret() {
    byte[] buffer = new byte[20];
    new java.security.SecureRandom().nextBytes(buffer);
    return base32.encodeToString(buffer).replace("=", "");
  }

  public String buildOtpAuthUrl(String issuer, String email, String secret) {
    return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
        issuer, email, secret, issuer);
  }

  public String generateQrCodeBase64(String contents, int width, int height)
      throws WriterException, java.io.IOException {

    QRCodeWriter writer = new QRCodeWriter();
    BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, width, height);
    BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    javax.imageio.ImageIO.write(image, "png", baos);
    return Base64.getEncoder().encodeToString(baos.toByteArray());
  }

  public boolean verifyCode(String secret, int code) {
    try {
      byte[] keyBytes = base32.decode(secret);
      var key = new SecretKeySpec(keyBytes, "HmacSHA1");

      // 시간 윈도우 허용 (-1, 0, +1)
      Instant now = Instant.now();
      int current = totp.generateOneTimePassword(key, now);
      if (current == code) return true;

      Instant prev = now.minus(totp.getTimeStep());
      int previous = totp.generateOneTimePassword(key, prev);
      if (previous == code) return true;

      Instant next = now.plus(totp.getTimeStep());
      int nextCode = totp.generateOneTimePassword(key, next);
      return nextCode == code;

    } catch (GeneralSecurityException e) {
      return false;
    }
  }

  /** QR 발급(시크릿 생성 후 DB 저장, 단 totpEnabled=false) */
  public TotpSetupResult setup(String email, String issuer) throws Exception {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("user not found"));

    String secret = generateSecret();
    user.setTotpSecret(secret);
    user.setTotpEnabled(false);
    userRepository.save(user);

    String otpauthUrl = buildOtpAuthUrl(issuer, email, secret);
    String qrBase64 = generateQrCodeBase64(otpauthUrl, 200, 200);

    return new TotpSetupResult(secret, otpauthUrl, qrBase64);
  }

  /** 코드 검증 후 활성화 */
  public void enable(String email, int code) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("user not found"));

    if (user.getTotpSecret() == null) {
      throw new IllegalStateException("TOTP secret not issued. Call /setup first.");
    }

    if (!verifyCode(user.getTotpSecret(), code)) {
      throw new IllegalArgumentException("Invalid TOTP code");
    }

    user.setTotpEnabled(true);
    userRepository.save(user);
  }

  public static record TotpSetupResult(String secret, String otpauthUrl, String qrImageBase64) {}
}
