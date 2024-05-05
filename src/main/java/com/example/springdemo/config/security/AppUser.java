package com.example.springdemo.config.security;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AppUser {
    
    private String id;
    private String email;
    private String password;
    private UserAuthority authority;
    private boolean enabled = true;
    private boolean premium = false;
    private LocalDate trailExpiration;

    // test data
    public static AppUser getTestAdminUser() {
        var user = new AppUser();
        user.id = "100";
        user.email = "admin";
        user.password = "admin";
        user.authority = UserAuthority.ADMIN;

        return user;
    }

    public static AppUser getTestNormalUser() {
        var user = new AppUser();
        user.id = "101";
        user.email = "normal";
        user.password = "normal";
        user.authority = UserAuthority.NORMAL;

        return user;
    }

    public static AppUser getTestGuestUser() {
        var user = new AppUser();
        user.id = "000";
        user.email = "guest";
        user.password = "guest";
        user.authority = UserAuthority.GUEST;
        user.setEnabled(false);
        user.setTrailExpiration(LocalDate.of(2023, 12, 31));
        return user;
    }

}
