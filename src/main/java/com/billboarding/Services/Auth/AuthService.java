package com.billboarding.Services.Auth;

import com.billboarding.DTO.AuthResponse;
import com.billboarding.DTO.LoginRequest;
import com.billboarding.Entity.User;
import com.billboarding.Repository.UserRepository;
import com.billboarding.Services.JWT.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * AuthService: Handles login and token generation
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {

        // STEP 1 → authenticate (email + password)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // STEP 2 → load user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // STEP 3 → Check if user is blocked
        if (user.isBlocked()) {
            throw new RuntimeException("Your account is blocked by admin");
        }

        /**
         * ❌ KYC CHECK REMOVED (Option A)
         * Owners & Advertisers can login even if KYC is pending.
         */

        // STEP 4 → Generate JWT token
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        // STEP 5 → Return response DTO
        return new AuthResponse(
                token,
                user.getRole().name(),
                user.getId(),
                "Login successful"
        );
    }
}
