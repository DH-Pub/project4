package com.aptech.proj4.config;

public interface SecurityConstants {
    String SECRET = "Secret string for the security";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SIGN_UP_URL = "/users/sign-up";
    long TOKEN_EXPERITION_TIME = 300_000;
    long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000;

    String[] ENDPOINTS_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/auth/login",
            "/auth/signup",
            "/auth/refreshtoken",
            "auth/image/**",
    };

    String MAIN_EMAIL = "main@email.com";
    String MAIN_PASSWORD = "password";
}
