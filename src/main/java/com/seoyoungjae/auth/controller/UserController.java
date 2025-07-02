package com.seoyoungjae.auth.controller;

import com.seoyoungjae.auth.domain.User;
import com.seoyoungjae.auth.dto.EmailRequest;
import com.seoyoungjae.auth.dto.PasswordChangeRequest;
import com.seoyoungjae.auth.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public User getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
    return userService.findByEmail(userDetails.getUsername());
  }

  @DeleteMapping("/me")
  public ResponseEntity<String> deleteMyAccount(@AuthenticationPrincipal UserDetails userDetails) {
    userService.deleteByEmail(userDetails.getUsername());
    return ResponseEntity.ok("회원 탈퇴 완료");
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
}
