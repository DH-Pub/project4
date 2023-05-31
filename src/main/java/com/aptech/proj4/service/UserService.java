package com.aptech.proj4.service;

import java.util.List;

import com.aptech.proj4.dto.PasswordDto;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.model.User;

public interface UserService {
    UserDto createAdmin(UserDto user);

    boolean changeAdminRole(UserDto user);

    UserDto signup(UserDto user);

    UserDto getUser(String id);

    UserDto updateUser(UserDto user);

    boolean changePassword(UserDto user, PasswordDto passwordDto);

    boolean deleteUser(String id);

    UserDto findUserByEmail(String email);

    List<User> getAllUser();
}
