package com.seoyoungjae.auth.controller;

import com.seoyoungjae.auth.dto.LoginDto;
import com.seoyoungjae.auth.dto.UserDto;
import com.seoyoungjae.auth.jwt.JwtUtil;
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

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
    userService.register(userDto);
    return ResponseEntity.ok("회원가입 완료");
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

    try {
      Authentication authentication = authenticationManager.authenticate(token);
      String jwt = jwtUtil.generateToken(authentication.getName());
      return ResponseEntity.ok(jwt);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
    }

  }

}
