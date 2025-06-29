package com.seoyoungjae.auth.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
  private String email;
  private String password;
}
