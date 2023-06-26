package com.aptech.proj4.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aptech.proj4.config.SecurityConstants;
import com.aptech.proj4.dto.PasswordDto;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.enums.UserRole;
import com.aptech.proj4.model.User;
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
                    .setId(Long.toString(System.currentTimeMillis()))
                    .setEmail(userDto.getEmail())
                    .setUsername(userDto.getUsername())
                    .setPassword(passwordEncoder.encode(userDto.getPassword()))
                    .setRole(UserRole.USER)
                    .setBio(userDto.getBio());
            String pic = userDto.getPic() == null ? null : newUser.getId() + ".jpg";
            newUser.setPic(pic);
            userRepository.save(newUser);

            userDto.setPassword("");
            userDto.setPic(pic);
            return userDto;
        } else {
            throw new RuntimeException("email already exists.");
        }
    }

    @Override
    public UserDto findUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email).get());
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDto.class);
        }
        return null;
    }

    @Override
    public List<User> getAllUser() {
        List<User> users = (List<User>) userRepository.findAll();
        users.stream().forEach(u -> u.setPassword(null));
        return users;
    }

    @Override
    public UserDto createAdmin(UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (!user.isPresent()) {
            User newUser = new User()
                    .setId(Long.toString(System.currentTimeMillis()))
                    .setEmail(userDto.getEmail())
                    .setUsername(userDto.getUsername())
                    .setPassword(passwordEncoder.encode(userDto.getPassword()))
                    .setRole(UserRole.ADMIN)
                    .setBio(userDto.getBio());
            String pic = userDto.getPic() == null ? null : newUser.getId() + ".jpg";
            newUser.setPic(pic);
            userRepository.save(newUser);

            userDto.setPassword(null);
            userDto.setPic(pic);
            return userDto;
        } else {
            throw new RuntimeException("email already exists.");
        }
    }

    @Override
    public boolean changeAdminRole(UserDto userDto) {
        if (userDto.getEmail() == SecurityConstants.MAIN_EMAIL) {
            return false;
        }
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Email not found."));
        user.setRole(UserRole.valueOf(userDto.getRole()));
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDto getUser(String id) {
        Optional<User> user = Optional.ofNullable(userRepository.findById(id).get());
        if (user.isPresent()) {
            user.get().setPassword(null);
            return modelMapper.map(user.get(), UserDto.class);
        }
        throw new RuntimeException("User does not exist.");
    }

    @Override
    public UserDto updateUser(UserDto user) {
        User updatedUser = modelMapper.map(user, User.class);
        userRepository.save(updatedUser);
        return user;
    }

    @Override
    public boolean changePassword(UserDto userDto, PasswordDto passwordDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new NoSuchElementException("Email not found."));
        if (passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
            userRepository.save(user);
        }else{
            throw new RuntimeException("Wrong password.");
        }
        return true;
    }

    @Override
    public boolean deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("User id not found."));
        if(user.getRole() == UserRole.MAIN){
            return false;
        }
        userRepository.delete(user);
        return true;
    }

}
