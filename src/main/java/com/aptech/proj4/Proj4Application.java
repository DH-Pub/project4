package com.aptech.proj4;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aptech.proj4.config.SecurityConstants;
import com.aptech.proj4.model.User;
import com.aptech.proj4.model.UserRole;
import com.aptech.proj4.repository.UserRepository;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "springdoc-openapi", version = "1.0.0"), security = {@SecurityRequirement(name = "bearer-key")})
public class Proj4Application {

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Proj4Application.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository){
		return args ->{
			Optional<User> admin = userRepository.findByEmail(SecurityConstants.MAIN_EMAIL);
			if (!admin.isPresent()){
				User user = new User()
				.setId("MainAdmin")
				.setEmail(SecurityConstants.MAIN_EMAIL)
				.setUsername("Main Admin")
				.setPassword(passwordEncoder.encode(SecurityConstants.MAIN_PASSWORD))
				.setRole(UserRole.MAIN)
				.setBio("This is the Main Admin")
				.setPic(null);
				userRepository.save(user);
			}
		};
	}
}
