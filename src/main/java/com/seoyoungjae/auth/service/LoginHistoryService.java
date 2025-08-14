package com.seoyoungjae.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.seoyoungjae.auth.domain.LoginHistory;
import com.seoyoungjae.auth.domain.User;
import com.seoyoungjae.auth.dto.LoginHistoryResponse;
import com.seoyoungjae.auth.repository.LoginHistoryRepository;
import com.seoyoungjae.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {

  private final LoginHistoryRepository loginHistoryRepository;
  private final UserRepository userRepository;

  public void recordLogin(Long userId, HttpServletRequest request) {
    String ip = request.getRemoteAddr();
    String agent = request.getHeader("User-Agent");

    LoginHistory history = LoginHistory.builder()
        .userId(userId)
        .loginAt(LocalDateTime.now())
        .ipAddress(ip)
        .userAgent(agent)
        .success(true)
        .build();

    loginHistoryRepository.save(history);
  }

  public LoginHistoryResponse getLastLogin(Long userId) {
    return loginHistoryRepository.findTopByUserIdOrderByLoginAtDesc(userId)
        .map(h -> {
          String email = userRepository.findById(userId)
              .map(User::getEmail)
              .orElse("알 수 없음");

          return LoginHistoryResponse.builder()
              .userId(userId)
              .email(email)
              .loginAt(h.getLoginAt())
              .ipAddress(h.getIpAddress())
              .userAgent(h.getUserAgent())
              .build();
        })
        .orElseThrow(() -> new RuntimeException("기록 없음"));
  }

  public List<LoginHistoryResponse> getAllHistories() {
    return loginHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "loginAt"))
        .stream()
        .map(history -> {
          String email = userRepository.findById(history.getUserId())
              .map(User::getEmail)
              .orElse("알 수 없음");

          return LoginHistoryResponse.builder()
              .userId(history.getUserId())
              .email(email)
              .loginAt(history.getLoginAt())
              .ipAddress(history.getIpAddress())
              .userAgent(history.getUserAgent())
              .build();
        })
        .collect(Collectors.toList());
  }
}
