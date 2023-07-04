package com.aptech.proj4.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.enums.UserRole;
import com.aptech.proj4.service.UserService;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserDto userDto = userService.findUserByEmail(email);
            List<GrantedAuthority> authorities = getUserAuthority(userDto.getRole());

            // stop banned users
            if (authorities.stream().anyMatch(role -> role.getAuthority().equals(UserRole.BANNED.toString()))) {
                throw new UsernameNotFoundException("User with this email is banned.");
            }
            return buildUserForAuthentication(userDto, authorities);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User with email does not exist.");
        }
    }

    private List<GrantedAuthority> getUserAuthority(String userRole) {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(userRole));
        return new ArrayList<GrantedAuthority>(roles);
    }

    private UserDetails buildUserForAuthentication(UserDto user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
