package com.capstoneproject.ElitesTracker.security.manager;

import com.capstoneproject.ElitesTracker.exceptions.AuthenticationNotSupportedException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.AUTHENTICATION_NOT_SUPPORTED_EXCEPTION;

@Component
@AllArgsConstructor
public class ElitesAuthenticationManager implements AuthenticationManager {
    private final AuthenticationProvider authenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authenticationProvider.supports(authentication.getClass())){
            return authenticationProvider.authenticate(authentication);
        }
        throw new AuthenticationNotSupportedException(AUTHENTICATION_NOT_SUPPORTED_EXCEPTION.getMessage());
    }
}
