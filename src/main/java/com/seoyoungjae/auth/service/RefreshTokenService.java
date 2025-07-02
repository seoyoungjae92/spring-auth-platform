package com.seoyoungjae.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.seoyoungjae.auth.domain.RefreshToken;
import com.seoyoungjae.auth.jwt.JwtProvider;
import com.seoyoungjae.auth.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final JwtProvider jwtProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  public void saveOrUpdateRefreshToken(String email, String token) {
    Optional<RefreshToken> existing = refreshTokenRepository.findByEmail(email);
    long expiration = System.currentTimeMillis() + jwtProvider.getRefreshTokenExpirationMs();

    if (existing.isPresent()) {
      RefreshToken refreshToken = existing.get();
      refreshToken.setToken(token);
      refreshToken.setExpiration(expiration);
      refreshTokenRepository.save(refreshToken);
    } else {
      RefreshToken refreshToken = RefreshToken.builder()
          .email(email)
          .token(token)
          .expiration(expiration)
          .build();
      refreshTokenRepository.save(refreshToken);
    }
  }

}
