package com.seoyoungjae.auth.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;

  private LocalDateTime loginAt;

  private String ipAddress;

  @Lob
  private String userAgent;

  private boolean success;
}
