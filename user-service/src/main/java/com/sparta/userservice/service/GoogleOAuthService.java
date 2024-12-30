package com.sparta.userservice.service;

import com.sparta.userservice.dto.GoogleUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${GOOGLE_REDIRECT_URI}")
    private String redirectUri;

//    @Value("${GOOGLE_TOKEN_URI}")
//    private String tokenUri;
//
//    @Value("${GOOGLE_USER_INFO_URI}")
//    private String userInfoUri;

    private final RestTemplateService restTemplateService;

    // Google 로그인 URL 생성
    public String getGoogleAuthUrl() {
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "email profile")
                .build().toUriString();
    }

//    // Google에서 Access Token 가져오기
//    public String getAccessToken(String code) {
//        Map<String, String> params = new HashMap<>();
//        params.put("client_id", clientId);
//        params.put("client_secret", clientSecret);
//        params.put("redirect_uri", redirectUri);
//        params.put("grant_type", "authorization_code");
//        params.put("code", code);
//
//        Map<String, Object> response = restTemplateService.post(tokenUri, params, Map.class);
//        return (String) response.get("access_token");
//    }
//
//    // Google에서 사용자 정보 가져오기
//    public GoogleUserInfo getGoogleUserInfo(String code) {
//        String accessToken = getAccessToken(code);
//
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Authorization", "Bearer " + accessToken);
//
//        Map<String, Object> response = restTemplateService.get(userInfoUri, headers, Map.class);
//
//        return new GoogleUserInfo(
//                (String) response.get("email"),
//                (String) response.get("name")
//        );
//    }
}