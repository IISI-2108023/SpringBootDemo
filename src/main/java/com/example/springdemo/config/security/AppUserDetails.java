package com.example.springdemo.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class AppUserDetails implements UserDetails {

    private final AppUser appUser;

    public AppUserDetails(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(appUser.getAuthority());
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        if (appUser.getTrailExpiration() == null) {
            return true;
        }
        return LocalDate.now().isBefore(appUser.getTrailExpiration());
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
        return appUser.isEnabled();
    }

    // custom method
    public String getId() {
        return appUser.getId();
    }

    public UserAuthority getUserAuthority() {
        return appUser.getAuthority();
    }

    public boolean isPremium() {
        return appUser.isPremium();
    }

    public LocalDate getTrailExpiration() {
        return appUser.getTrailExpiration();
    }
}
