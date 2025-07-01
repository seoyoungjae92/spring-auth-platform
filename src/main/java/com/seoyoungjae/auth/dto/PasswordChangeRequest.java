package com.seoyoungjae.auth.dto;

import lombok.Getter;

@Getter
public class PasswordChangeRequest {
  private String currentPassword;
  private String newPassword;
}
