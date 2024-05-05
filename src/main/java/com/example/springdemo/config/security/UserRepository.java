package com.example.springdemo.config.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Repository
public class UserRepository {
    @Autowired
    private PasswordEncoder passwordEncoder;// from security config
    private final Map<String, AppUser> idToUserMap = new HashMap<>();

    @PostConstruct
    private void init() {
        var adminUser = AppUser.getTestAdminUser();
        var normalUser = AppUser.getTestNormalUser();
        var guestUser = AppUser.getTestGuestUser();

        // encode password
        Stream.of(adminUser, normalUser, guestUser)
                .forEach(u -> {
                    var encodePwd = passwordEncoder.encode(u.getPassword());
                    u.setPassword(encodePwd);
                });

        idToUserMap.put(adminUser.getId(), adminUser);
        idToUserMap.put(normalUser.getId(), normalUser);
        idToUserMap.put(guestUser.getId(), guestUser);
    }

    public AppUser findById(String id) {
        return idToUserMap.get(id);
    }

    public AppUser findByEmail(String email) {
        return idToUserMap.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public List<AppUser> findAll() {
        return new ArrayList<>(idToUserMap.values());
    }

    public void insert(AppUser user) {
        var encodePwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePwd);
        idToUserMap.put(user.getId(), user);
    }
}
