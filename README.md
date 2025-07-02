# spring-auth-platform
Authentication &amp; Authorization demo with Spring Boot, JWT, OAuth2, Redis

### 🔐 OAuth2 설정 가이드

OAuth2 로그인(Google 등)을 사용하려면 민감 정보를 별도 설정 파일에 등록해야 합니다.

#### 📁 설정 파일 위치

```
src/main/resources/application-oauth.yml
```

> 이 파일은 Git에 커밋되지 않으며, `.gitignore`에 의해 무시됩니다.

#### 📝 예시: `application-oauth-example.yml`

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: your-google-client-id
            client-secret: your-google-client-secret
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope:
              - profile
              - email
        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/v2/auth
            token-uri: https://oauth2.googleapis.com/token
            user-info-uri: https://www.googleapis.com/oauth2/v3/userinfo
            user-name-attribute: sub
```

> 👉 위 예시를 참고하여 `application-oauth.yml` 파일을 생성하세요.

#### ⚙️ `application.yml` 설정

```yaml
spring:
  config:
    import: "optional:classpath:application-oauth.yml"
```

> 이 설정을 통해 `application-oauth.yml` 파일이 있으면 자동으로 불러오며, 없으면 무시됩니다.

---

### 🔒 보안 주의사항

- **절대 `application-oauth.yml`을 Git에 커밋하지 마세요!**
- GitHub는 민감정보(Push Protection)를 자동 탐지하여 push를 차단합니다.
- 팀원에게 공유할 경우에는 별도 안전한 방법(예: Slack DM, 비공개 gist 등)을 이용하세요.
