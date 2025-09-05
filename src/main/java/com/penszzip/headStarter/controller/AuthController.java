package com.penszzip.headStarter.controller;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.penszzip.headStarter.dto.AuthResponse;
import com.penszzip.headStarter.dto.LoginRequest;
import com.penszzip.headStarter.dto.RegisterRequest;
import com.penszzip.headStarter.jwt.JwtUtil;
import com.penszzip.headStarter.model.User;
import com.penszzip.headStarter.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    
    /**
     * Login endpoint: authenticates user and returns JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {        
        try {
            // Try to authenticate
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            // Authentication failed
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
        }
        
        // Fetch user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        
        // Generate Token
        String jwt = jwtUtil.generateToken(userDetails);

        // Return token
        return new ResponseEntity<AuthResponse>(
            new AuthResponse(jwt, userDetails.getUsername()), 
            HttpStatus.OK
        );
    }
    
    /**
     * Registration endpoint: creates new user and returns JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<?> postMethodName(@RequestBody RegisterRequest registerRequest) {
        // Check if username already exists
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Username already exists");
        }

        // Create and save new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
        
        // Generate token for new user
        UserDetails userDetails = userDetailsService.loadUserByUsername(registerRequest.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);

        // Return token
        return ResponseEntity.ok(new AuthResponse(jwt, userDetails.getUsername()));
    }
    
    /**
     * Get current user details
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUser(Principal principal) {
        // Check if currently logged in
        if (principal == null) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Not logged in");
        }

        // Get user details if logged in
        Optional<User> user = userRepository.findByUsername(principal.getName());
        // If user not found
        if (user.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("User not found");
        }

        // If user found, return info without password
        return ResponseEntity.ok(Map.of(
            "id", user.get().getId(),
            "username", user.get().getUsername()
        ));
    }
    
}
