package com.seoyoungjae.auth.handler;

import com.seoyoungjae.auth.jwt.JwtProvider;
import com.seoyoungjae.auth.repository.UserRepository;
import com.seoyoungjae.auth.service.RefreshTokenService;
import com.seoyoungjae.auth.domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

  private final JwtProvider jwtProvider;
  private final RefreshTokenService refreshTokenService;
  private final UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
    String email = oauth2User.getAttribute("email");

    // 유저가 없으면 회원가입
    userRepository.findByEmail(email).orElseGet(() -> {
      return userRepository.save(User.builder()
          .email(email)
          .password("") // 소셜 로그인은 비밀번호 없음
          .isSocial(true)
          .role(User.Role.USER)
          .build());
    });

    String accessToken = jwtProvider.generateAccessToken(email);
    String refreshToken = jwtProvider.generateRefreshToken(email);

    refreshTokenService.saveOrUpdateRefreshToken(email, refreshToken);

    String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth-success")
        .queryParam("accessToken", accessToken)
        .queryParam("refreshToken", refreshToken)
        .build().toUriString();

    response.sendRedirect(redirectUrl);
  }
}
