//package com.capstoneproject.ElitesTracker.security.userDetailsService;
//
//import com.capstoneproject.ElitesTracker.models.EliteUser;
//import com.capstoneproject.ElitesTracker.security.model.SecureEliteUser;
//import com.capstoneproject.ElitesTracker.services.interfaces.UserService;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//@Component
//@AllArgsConstructor
//public class ElitesUserDetailsService implements UserDetailsService {
//    private final UserService userService;
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        EliteUser foundUser = userService.findUserByEmail(email);
//        return new SecureEliteUser(foundUser);
//    }
//}
