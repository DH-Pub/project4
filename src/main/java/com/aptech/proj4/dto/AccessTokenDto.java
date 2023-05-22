package com.aptech.proj4.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenDto {
    private String token;
    private List<String> roles;
    private String refreshToken;
}
