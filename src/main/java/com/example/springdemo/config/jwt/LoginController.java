package com.example.springdemo.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/login")
public class LoginController {
    @Autowired
    private TokenService tokenService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse res = tokenService.createToken(request);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> request) {
        String accessToken = tokenService.refreshAccessToken(request.get("refreshToken"));
        Map<String, String> res = Map.of("accessToken", accessToken);
        return ResponseEntity.ok(res);
    }
}
