// com/seoyoungjae/auth/dto/MfaSetupResponse.java
package com.seoyoungjae.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MfaSetupResponse {
  private String secret;
  private String otpauthUrl;
  private String qrImageBase64;
}
