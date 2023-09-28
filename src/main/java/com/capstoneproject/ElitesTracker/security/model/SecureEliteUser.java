package com.capstoneproject.ElitesTracker.security.model;

import com.capstoneproject.ElitesTracker.enums.Role;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class SecureEliteUser implements UserDetails {
    private final EliteUser eliteUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = eliteUser.getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority(role.name());
        authorities.add(userAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return eliteUser.getPassword();
    }

    @Override
    public String getUsername() {
        return eliteUser.getSemicolonEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
