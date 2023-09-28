package com.capstoneproject.ElitesTracker.security;

import com.capstoneproject.ElitesTracker.security.filters.ElitesAuthenticationFilter;
import com.capstoneproject.ElitesTracker.security.filters.ElitesAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        final String[] publicEndPoints = {"/api/v1/user/register", "/api/v1/user/loginUser", "/api/v1/admin/addNative", "/swagger-ui.html", "api/v1/natives/takeAttendance"};

        return httpSecurity
                .addFilterAt(new ElitesAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ElitesAuthorizationFilter(), ElitesAuthenticationFilter.class)
                .sessionManagement(customizer-> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(c->c.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(c->c.requestMatchers(HttpMethod.POST,publicEndPoints).permitAll()
//                .authorizeHttpRequests(c -> c.anyRequest().hasAuthority(NATIVE.name()))
//                .authorizeHttpRequests(c -> c.anyRequest().hasAuthority(ADMIN.name()))
//                .authorizeHttpRequests(c -> c.anyRequest().hasAnyAuthority(NATIVE.name(), ADMIN.name())
                        .anyRequest().authenticated())
                .build();
    }

}
