package com.seoyoungjae.auth.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base32;

public class TOTPUtil {

  public static String generateSecret() {
    byte[] buffer = new byte[20];
    new SecureRandom().nextBytes(buffer);
    return new Base32().encodeToString(buffer).replace("=", "");
  }

  public static String getOtpAuthURL(String issuer, String accountName, String secret) {
    return String.format(
        "otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
        URLEncoder.encode(issuer, StandardCharsets.UTF_8),
        URLEncoder.encode(accountName, StandardCharsets.UTF_8),
        URLEncoder.encode(secret, StandardCharsets.UTF_8),
        URLEncoder.encode(issuer, StandardCharsets.UTF_8)
    );
  }
}
