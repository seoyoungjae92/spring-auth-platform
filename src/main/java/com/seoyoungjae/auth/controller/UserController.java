package com.seoyoungjae.auth.controller;

import com.seoyoungjae.auth.domain.User;
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
}
