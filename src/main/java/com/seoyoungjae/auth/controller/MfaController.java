// com/seoyoungjae/auth/controller/MfaController.java
package com.seoyoungjae.auth.controller;

import com.seoyoungjae.auth.dto.MfaSetupResponse;
import com.seoyoungjae.auth.dto.TotpVerifyRequest;
import com.seoyoungjae.auth.service.TotpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mfa")
@RequiredArgsConstructor
public class MfaController {

  private final TotpService totpService;

  /** QR 발급 */
  @PostMapping("/setup")
  public ResponseEntity<MfaSetupResponse> setup(@AuthenticationPrincipal UserDetails user) throws Exception {
    var result = totpService.setup(user.getUsername(), "spring-auth-platform");
    return ResponseEntity.ok(new MfaSetupResponse(result.secret(), result.otpauthUrl(), result.qrImageBase64()));
  }

  /** 코드 검증 -> 활성화 */
  @PostMapping("/verify")
  public ResponseEntity<String> verify(@AuthenticationPrincipal UserDetails user,
      @RequestBody TotpVerifyRequest request) {
    totpService.enable(user.getUsername(), request.getCode());
    return ResponseEntity.ok("TOTP 활성화 완료");
  }
}
