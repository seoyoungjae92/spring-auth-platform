package com.seoyoungjae.auth.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender mailSender;

  public void sendPasswordResetMail(String to, String tempPassword) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("[spring-auth-platform] 임시 비밀번호 안내");
    message.setText("임시 비밀번호: " + tempPassword + "\n로그인 후 반드시 변경해주세요.");
    mailSender.send(message);
  }
}
