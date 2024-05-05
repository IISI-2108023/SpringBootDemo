package com.example.springdemo.config.jwt;

import com.example.springdemo.config.security.AppUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    private Key secretKey;// 密鑰
    private JwtParser jwtParser;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @PostConstruct
    private void init() {
        String key = "MySecretKeyRRRRRRRRRisitlongenough";// 可能會放在keystore之類的地方
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
        jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    // LoginRequest > createToken: access token, refresh token > response
    public LoginResponse createToken(LoginRequest request) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authToken = authenticationProvider.authenticate(authToken);// 認證帳密
        AppUserDetails userDetails = (AppUserDetails) authToken.getPrincipal();

        String accessToken = createAccessToken(userDetails.getUsername());
        String refreshToken = createRefreshToken(userDetails.getUsername());

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserId(userDetails.getId());
        response.setEmail(userDetails.getUsername());
        response.setUserAuthority(userDetails.getUserAuthority());
        response.setPremium(userDetails.isPremium());
        response.setTrailExpiration(userDetails.getTrailExpiration());
        return response;
    }

    private String createAccessToken(String username) {
        long expirationMillis = Instant.now()
                .plusSeconds(90)
                .getEpochSecond() * 1000;
        // 設置內容
        Claims claims = Jwts.claims();
        claims.setSubject("Access Token");
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(expirationMillis));
        claims.put("username", username);

        // 產生token
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    private Map<String, Object> parseToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return new HashMap<>(claims);
    }

    private String createRefreshToken(String username) {
        long expirationMillis = Instant.now()
                .plusSeconds(600)
                .getEpochSecond() * 1000;
        Claims claims = Jwts.claims();
        claims.setSubject("Refresh token");
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(expirationMillis));
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    public String refreshAccessToken(String refreshToken) {
        Map<String, Object> payload = parseToken(refreshToken);
        String username = (String) payload.get("username");
        return createAccessToken(username);
    }
}
