// src/main/java/com/seoyoungjae/auth/dto/MfaVerifyRequest.java
package com.seoyoungjae.auth.dto;

import lombok.Data;

@Data
public class MfaVerifyRequest {
  private String mfaToken; // 로그인 1단계 후 받은 토큰(로그인용)
  private int code;        // 6자리 OTP
}
