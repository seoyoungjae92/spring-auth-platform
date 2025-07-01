package com.seoyoungjae.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seoyoungjae.auth.domain.RefreshToken;
import com.seoyoungjae.auth.domain.User;
import com.seoyoungjae.auth.dto.UserDto;
import com.seoyoungjae.auth.repository.RefreshTokenRepository;
import com.seoyoungjae.auth.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;

  public void register(UserDto userDto) {
    if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
      throw new RuntimeException("이미 존재하는 사용자입니다.");
    }

    User user = User.builder()
        .email(userDto.getEmail())
        .password(passwordEncoder.encode(userDto.getPassword()))
        .role(User.Role.USER)
        .build();

    userRepository.save(user);
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
  }

  @Transactional
  public void logout(String email) {
    refreshTokenRepository.deleteByEmail(email);
  }

  @Transactional
  public void saveRefreshToken(String email, String refreshToken, long expiration) {
    refreshTokenRepository.deleteByEmail(email);
    refreshTokenRepository.save(RefreshToken.builder()
        .email(email)
        .token(refreshToken)
        .expiration(expiration)
        .build());
  }

  public boolean existsByEmail(String email) {
    return userRepository.findByEmail(email).isPresent();
  }

  @Transactional
  public void deleteByEmail(String email) {
    refreshTokenRepository.deleteByEmail(email);
    userRepository.deleteByEmail(email);
  }
}
