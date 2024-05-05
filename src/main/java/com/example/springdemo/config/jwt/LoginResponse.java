package com.example.springdemo.config.jwt;

import com.example.springdemo.config.security.UserAuthority;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private String userId;

    private String email;

    private UserAuthority userAuthority;

    private boolean premium;

    private LocalDate trailExpiration;


}
