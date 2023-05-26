package com.aptech.proj4.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
public class UserDto {
    private String id;
    private String email;
    private String username;
    private String password;
    private String role;
    private String bio;
    private String pic;
    private String create_at;

    private String res_message;
}
