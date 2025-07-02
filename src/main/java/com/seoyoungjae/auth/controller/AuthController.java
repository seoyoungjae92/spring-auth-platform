package com.seoyoungjae.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seoyoungjae.auth.domain.RefreshToken;
import com.seoyoungjae.auth.dto.LoginDto;
import com.seoyoungjae.auth.dto.TokenResponse;
import com.seoyoungjae.auth.dto.UserDto;
import com.seoyoungjae.auth.jwt.JwtProvider;
import com.seoyoungjae.auth.repository.RefreshTokenRepository;
import com.seoyoungjae.auth.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  private final RefreshTokenRepository refreshTokenRepository;

  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
    userService.register(userDto);
    return ResponseEntity.ok("회원가입 완료");
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

    try {
      Authentication authentication = authenticationManager.authenticate(token);
      String accessToken = jwtProvider.generateAccessToken(authentication.getName());
      String refreshToken = jwtProvider.generateRefreshToken(authentication.getName());

      // 저장
      userService.saveRefreshToken(authentication.getName(), refreshToken,
          System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7);

      return ResponseEntity.ok(TokenResponse.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .build());

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
    }
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@RequestBody String refreshToken) {
    if (!jwtProvider.validateToken(refreshToken) || jwtProvider.isTokenExpired(refreshToken)) {
      return ResponseEntity.status(401).body("리프레시 토큰이 유효하지 않음");
    }

    String email = jwtProvider.getEmailFromToken(refreshToken);
    RefreshToken savedToken = refreshTokenRepository.findByToken(refreshToken)
        .orElseThrow(() -> new RuntimeException("저장된 리프레시 토큰 없음"));

    if (!savedToken.getEmail().equals(email)) {
      return ResponseEntity.status(403).body("토큰의 소유자 불일치");
    }

    String newAccessToken = jwtProvider.generateAccessToken(email);
    return ResponseEntity.ok(TokenResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(refreshToken)
        .build());
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
    userService.logout(userDetails.getUsername());
    return ResponseEntity.ok("로그아웃 완료");
  }

  @GetMapping("/check-email")
  public ResponseEntity<String> checkEmail(@RequestParam String email) {
    if (userService.existsByEmail(email)) {
      return ResponseEntity.status(409).body("이미 사용 중인 이메일입니다.");
    }
    return ResponseEntity.ok("사용 가능한 이메일입니다.");
  }

}
