package com.aptech.proj4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "springdoc-openapi", version = "1.0.0"), security = {@SecurityRequirement(name = "bearer-key")})
public class Proj4Application {

	public static void main(String[] args) {
		SpringApplication.run(Proj4Application.class, args);
	}

}
