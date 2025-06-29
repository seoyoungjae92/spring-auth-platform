package com.seoyoungjae.auth.controller;

import com.seoyoungjae.auth.dto.UserDto;
import com.seoyoungjae.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
    userService.register(userDto);
    return ResponseEntity.ok("회원가입 완료");
  }
}
