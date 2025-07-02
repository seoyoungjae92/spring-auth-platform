package com.seoyoungjae.auth.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seoyoungjae.auth.domain.RefreshToken;
import com.seoyoungjae.auth.domain.User;
import com.seoyoungjae.auth.dto.UserDto;
import com.seoyoungjae.auth.jwt.JwtProvider;
import com.seoyoungjae.auth.repository.RefreshTokenRepository;
import com.seoyoungjae.auth.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final MailService mailService;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  public void register(UserDto userDto) {
    if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
      throw new RuntimeException("이미 존재하는 사용자입니다.");
    }

    User user = User.builder()
        .email(userDto.getEmail())
        .password(passwordEncoder.encode(userDto.getPassword()))
        .role(User.Role.USER)
        .isSocial(false)
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

  public void changePassword(String email, String currentPassword, String newPassword) {
    User user = findByEmail(email);

    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

  public void resetPassword(String email) {
    User user = findByEmail(email); // 존재하지 않으면 예외
    String tempPassword = UUID.randomUUID().toString().substring(0, 8);
    user.setPassword(passwordEncoder.encode(tempPassword));
    userRepository.save(user);

    mailService.sendPasswordResetMail(user.getEmail(), tempPassword);
  }
}
