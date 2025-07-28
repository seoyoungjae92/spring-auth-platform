package com.seoyoungjae.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotpSetupResponse {
  private String secret;
  private String qrUrl;
}
