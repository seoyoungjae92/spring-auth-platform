// src/main/java/com/seoyoungjae/auth/dto/TotpVerifyRequest.java
package com.seoyoungjae.auth.dto;

import lombok.Data;

@Data
public class TotpVerifyRequest {
  private int code; // 설정(Setup) 완료를 위한 코드
}
