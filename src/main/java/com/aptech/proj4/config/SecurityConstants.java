package com.aptech.proj4.config;

public interface SecurityConstants {
    String SECRET = "Secret string for the security";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SIGN_UP_URL = "/users/sign-up";
    long TOKEN_EXPERITION_TIME = 300_000;
    long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000;
}
