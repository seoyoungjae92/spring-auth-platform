package com.seoyoungjae.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {
  private String accessToken;
  private String refreshToken;
}
