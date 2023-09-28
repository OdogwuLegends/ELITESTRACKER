package com.capstoneproject.ElitesTracker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "ELites Tracker",
				version = "v1",
				description = "This app provides REST APIs for capstone project",
				contact = @Contact(
						name = "Odogwu Legends",
						email = ""
				)
		),
		servers = {
//				@Server(
//						url = "http://localhost:8092",
//						description = "DEV Server"
//				),
				@Server(
						url = "https://elitestracker-production.up.railway.app",
						description = "PROD server"
				)
		}
)
public class ElitesTrackerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ElitesTrackerApplication.class, args);
	}


	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
