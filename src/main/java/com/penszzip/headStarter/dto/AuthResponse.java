package com.penszzip.headStarter.dto;

import lombok.Getter;

@Getter
public class AuthResponse {
    private String token;
    private String username;

    public AuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
}
