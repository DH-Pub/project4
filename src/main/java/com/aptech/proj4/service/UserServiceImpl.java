package com.aptech.proj4.service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aptech.proj4.dto.LoginRequest;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.model.User;
import com.aptech.proj4.model.UserRole;
import com.aptech.proj4.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto signup(UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (!user.isPresent()) {
            User newUser = new User()
                    // .setId(null)
                    .setEmail(userDto.getEmail())
                    .setUsername(userDto.getUsername())
                    .setPassword(passwordEncoder.encode(userDto.getPassword()))
                    .setRole(UserRole.USER)
                    .setBio(userDto.getBio())
                    // .setPic(null)
                    .setCreated_at(LocalTime.now().toString());
            userRepository.save(newUser);
            userDto.setPassword("");
        }
        return userDto;
    }

    @Override
    public UserDto login(LoginRequest loginDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public UserDto findUserByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findUserByEmail'");
    }

    @Override
    public List<User> getAllUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUser'");
    }

    @Override
    public UserDto createAdmin(UserDto user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAdmin'");
    }

}
