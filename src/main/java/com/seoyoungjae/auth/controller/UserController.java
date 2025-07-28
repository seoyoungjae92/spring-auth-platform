package com.seoyoungjae.auth.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.seoyoungjae.auth.domain.LoginHistory;
import com.seoyoungjae.auth.domain.User;
import com.seoyoungjae.auth.dto.EmailRequest;
import com.seoyoungjae.auth.dto.LoginHistoryResponse;
import com.seoyoungjae.auth.dto.PasswordChangeRequest;
import com.seoyoungjae.auth.repository.LoginHistoryRepository;
import com.seoyoungjae.auth.repository.UserRepository;
import com.seoyoungjae.auth.service.LoginHistoryService;
import com.seoyoungjae.auth.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final LoginHistoryService loginHistoryService;
  private final LoginHistoryRepository loginHistoryRepository;
  private final UserRepository userRepository;

  @GetMapping("/me")
  public User getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
    return userService.findByEmail(userDetails.getUsername());
  }

  @DeleteMapping("/me")
  public ResponseEntity<String> deleteMyAccount(@AuthenticationPrincipal UserDetails userDetails) {
    userService.deleteByEmail(userDetails.getUsername());
    return ResponseEntity.ok("회원 탈퇴 완료");
  }

  @GetMapping("/me/last-login")
  public ResponseEntity<LoginHistoryResponse> getLastLoginHistory(@AuthenticationPrincipal UserDetails userDetails) {
    var user = userService.findByEmail(userDetails.getUsername());
    var latest = loginHistoryService.getLastLogin(user.getId());
    return ResponseEntity.ok(latest);
  }

  @PatchMapping("/password")
  public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetails userDetails,
      @RequestBody PasswordChangeRequest request) {
    userService.changePassword(userDetails.getUsername(),
        request.getCurrentPassword(),
        request.getNewPassword());
    return ResponseEntity.ok("비밀번호가 변경되었습니다.");
  }

  @PostMapping("/reset-password")
  public ResponseEntity<String> resetPassword(@RequestBody EmailRequest request) {
    try {
      userService.resetPassword(request.getEmail());
      return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/admin/login-history")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<LoginHistoryResponse>> getAllLoginHistory() {
    List<LoginHistory> all = loginHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "loginAt"));

    List<LoginHistoryResponse> responses = all.stream()
        .map(h -> {
          String email = userRepository.findById(h.getUserId())
              .map(User::getEmail)
              .orElse("알 수 없음");

          return LoginHistoryResponse.builder()
              .userId(h.getUserId())
              .email(email)
              .loginAt(h.getLoginAt())
              .ipAddress(h.getIpAddress())
              .userAgent(h.getUserAgent())
              .build();
        })
        .collect(Collectors.toList());

    return ResponseEntity.ok(responses);
  }
}
