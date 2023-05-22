package com.aptech.proj4.service;

import java.util.List;

import com.aptech.proj4.dto.LoginRequest;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.model.User;

public interface UserService {
    UserDto signup(UserDto user);
    UserDto createAdmin(UserDto user);
    UserDto login(LoginRequest loginDto);

    UserDto findUserByEmail(String email);

    List<User> getAllUser();
}
