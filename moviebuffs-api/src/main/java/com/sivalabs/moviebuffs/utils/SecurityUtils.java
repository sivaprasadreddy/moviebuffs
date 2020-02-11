package com.sivalabs.moviebuffs.utils;

import com.sivalabs.moviebuffs.config.security.SecurityUser;
import com.sivalabs.moviebuffs.entity.User;
import com.sivalabs.moviebuffs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    @Autowired
    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getPrincipal() == null){
            return null;
        }
        if (authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            return securityUser.getUser();
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        }
        return null;
    }

    public boolean isCurrentUserAdmin() {
        User loginUser = loginUser();
        if(loginUser != null) {
            return loginUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        }
        return false;
    }

}
