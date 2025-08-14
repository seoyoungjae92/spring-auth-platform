package com.seoyoungjae.auth.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seoyoungjae.auth.dto.LoginHistoryResponse;
import com.seoyoungjae.auth.service.LoginHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class LoginHistoryController {

  private final LoginHistoryService loginHistoryService;

  @GetMapping("/login-history")
  @PreAuthorize("hasRole('ADMIN')")
  public List<LoginHistoryResponse> getAllLoginHistories() {
    return loginHistoryService.getAllHistories();
  }
}
