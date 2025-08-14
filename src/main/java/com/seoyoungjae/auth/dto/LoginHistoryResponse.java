package com.seoyoungjae.auth.dto;

import java.time.LocalDateTime;

import com.seoyoungjae.auth.domain.LoginHistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginHistoryResponse {

  private Long userId;
  private String email;
  private LocalDateTime loginAt;
  private String ipAddress;
  private String userAgent;

}
