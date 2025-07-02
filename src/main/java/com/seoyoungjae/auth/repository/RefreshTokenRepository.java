package com.seoyoungjae.auth.repository;

import com.seoyoungjae.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByEmail(String email);
  Optional<RefreshToken> findByToken(String token);
  void deleteByEmail(String email);
}
