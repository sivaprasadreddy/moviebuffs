package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.config.ApplicationProperties;
import com.sivalabs.moviebuffs.config.security.CustomUserDetailsService;
import com.sivalabs.moviebuffs.config.security.SecurityUser;
import com.sivalabs.moviebuffs.config.security.TokenHelper;
import com.sivalabs.moviebuffs.entity.User;
import com.sivalabs.moviebuffs.service.SecurityService;
import com.sivalabs.moviebuffs.web.dto.AuthenticationRequestDTO;
import com.sivalabs.moviebuffs.web.dto.AuthenticationResponseDTO;
import com.sivalabs.moviebuffs.web.dto.UserDTO;
import com.sivalabs.moviebuffs.web.mappers.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final TokenHelper tokenHelper;
    private final UserDTOMapper userDTOMapper;
    private final SecurityService securityService;
    private final ApplicationProperties applicationProperties;


    @PostMapping(value = "/login")
    public AuthenticationResponseDTO createAuthenticationToken(@RequestBody AuthenticationRequestDTO credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        String jws = tokenHelper.generateToken(user.getUsername());
        return new AuthenticationResponseDTO(jws, applicationProperties.getJwt().getExpiresIn());
    }

    @PostMapping(value = "/refresh")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<AuthenticationResponseDTO> refreshAuthenticationToken(HttpServletRequest request) {
        String authToken = tokenHelper.getToken(request);
        String refreshedToken = tokenHelper.refreshToken(authToken);
        return ResponseEntity.ok(
                new AuthenticationResponseDTO(
                        refreshedToken,
                        applicationProperties.getJwt().getExpiresIn()
                )
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDTO> me() {
        User loginUser = securityService.loginUser();
        return ResponseEntity.ok(userDTOMapper.toDTO(loginUser));
    }
}
