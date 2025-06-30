package com.seoyoungjae.auth.controller;

import com.seoyoungjae.auth.domain.RefreshToken;
import com.seoyoungjae.auth.dto.LoginDto;
import com.seoyoungjae.auth.dto.TokenResponse;
import com.seoyoungjae.auth.dto.UserDto;
import com.seoyoungjae.auth.jwt.JwtUtil;
import com.seoyoungjae.auth.repository.RefreshTokenRepository;
import com.seoyoungjae.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  private final RefreshTokenRepository refreshTokenRepository;

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

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
      String accessToken = jwtUtil.generateToken(authentication.getName());
      String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());

      // 저장
      refreshTokenRepository.deleteByEmail(authentication.getName()); // 기존 토큰 제거
      refreshTokenRepository.save(RefreshToken.builder()
          .email(authentication.getName())
          .token(refreshToken)
          .expiration(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)
          .build());

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
    if (!jwtUtil.validateToken(refreshToken) || jwtUtil.isTokenExpired(refreshToken)) {
      return ResponseEntity.status(401).body("리프레시 토큰이 유효하지 않음");
    }

    String email = jwtUtil.extractUsername(refreshToken);
    RefreshToken savedToken = refreshTokenRepository.findByToken(refreshToken)
        .orElseThrow(() -> new RuntimeException("저장된 리프레시 토큰 없음"));

    if (!savedToken.getEmail().equals(email)) {
      return ResponseEntity.status(403).body("토큰의 소유자 불일치");
    }

    String newAccessToken = jwtUtil.generateToken(email);
    return ResponseEntity.ok(TokenResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(refreshToken)
        .build());
  }



}
