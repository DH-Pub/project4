package com.aptech.proj4.service;

import java.util.List;

import com.aptech.proj4.dto.LoginRequest;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.model.User;

public interface UserService {
    UserDto createAdmin(UserDto user);

    UserDto changeAdminRole(UserDto user);

    UserDto signup(UserDto user);

    UserDto login(LoginRequest loginDto);

    UserDto getUser(String id);

    UserDto updateUser(UserDto user);

    boolean changePassword(UserDto user);

    boolean deleteUser(String id);

    UserDto findUserByEmail(String email);

    List<User> getAllUser();
}
