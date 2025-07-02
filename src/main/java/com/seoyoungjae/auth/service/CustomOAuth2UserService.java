package com.seoyoungjae.auth.service;

import com.seoyoungjae.auth.domain.User;
import com.seoyoungjae.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oauth2User = super.loadUser(userRequest);

    String email = oauth2User.getAttribute("email");
    // DB에 유저가 없다면 회원가입 처리
    userRepository.findByEmail(email)
        .orElseGet(() -> userRepository.save(User.builder()
            .email(email)
            .password("") // 소셜 로그인은 비밀번호 필요 없음
            .isSocial(true)
            .role(User.Role.USER)
            .build()));

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
        oauth2User.getAttributes(),
        "email"
    );
  }
}
