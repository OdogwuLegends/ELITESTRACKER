//package com.capstoneproject.ElitesTracker.security.provider;
//
//import com.capstoneproject.ElitesTracker.exceptions.IncorrectDetailsException;
//import lombok.AllArgsConstructor;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//
//import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.INVALID_CREDENTIALS_EXCEPTION;
//
//@Component
//@AllArgsConstructor
//public class ElitesAuthenticationProvider implements AuthenticationProvider {
//    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String email = authentication.getPrincipal().toString();
//        UserDetails foundUser = userDetailsService.loadUserByUsername(email);
//        String password = authentication.getCredentials().toString();
//        boolean isValidPassword = passwordEncoder.matches(password, foundUser.getPassword());
//
//        if(isValidPassword){
//            Collection<? extends GrantedAuthority> authorities = foundUser.getAuthorities();
//            return new UsernamePasswordAuthenticationToken(email, password, authorities);
//        }
//        throw new IncorrectDetailsException(INVALID_CREDENTIALS_EXCEPTION.getMessage());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}
