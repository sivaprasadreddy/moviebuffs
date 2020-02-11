package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.config.ApplicationProperties;
import com.sivalabs.moviebuffs.config.security.CustomUserDetailsService;
import com.sivalabs.moviebuffs.config.security.SecurityUser;
import com.sivalabs.moviebuffs.config.security.TokenHelper;
import com.sivalabs.moviebuffs.entity.User;
import com.sivalabs.moviebuffs.models.AuthenticationRequest;
import com.sivalabs.moviebuffs.models.AuthenticationResponse;
import com.sivalabs.moviebuffs.models.UserDTO;
import com.sivalabs.moviebuffs.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final TokenHelper tokenHelper;
    private final SecurityUtils securityUtils;
    private final ApplicationProperties applicationProperties;


    @PostMapping(value = "/auth/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        String jws = tokenHelper.generateToken(user.getUsername());
        return new AuthenticationResponse(jws, applicationProperties.getJwt().getExpiresIn());
    }

    @PostMapping(value = "/auth/refresh")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<AuthenticationResponse> refreshAuthenticationToken(HttpServletRequest request) {
        String authToken = tokenHelper.getToken(request);
        if (authToken != null) {
            String email = tokenHelper.getUsernameFromToken(authToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            Boolean validToken = tokenHelper.validateToken(authToken, userDetails);
            if (validToken) {
                String refreshedToken = tokenHelper.refreshToken(authToken);
                return ResponseEntity.ok(
                        new AuthenticationResponse(
                                refreshedToken,
                                applicationProperties.getJwt().getExpiresIn()
                        )
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/auth/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDTO> me() {
        User loginUser = securityUtils.loginUser();
        if(loginUser != null) {
            return ResponseEntity.ok(UserDTO.fromEntity(loginUser));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
