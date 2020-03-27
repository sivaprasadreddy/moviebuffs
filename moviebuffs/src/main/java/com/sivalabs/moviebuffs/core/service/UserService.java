package com.sivalabs.moviebuffs.core.service;

import com.sivalabs.moviebuffs.core.entity.Role;
import com.sivalabs.moviebuffs.core.entity.User;
import com.sivalabs.moviebuffs.core.exception.ApplicationException;
import com.sivalabs.moviebuffs.core.exception.UserNotFoundException;
import com.sivalabs.moviebuffs.core.repository.RoleRepository;
import com.sivalabs.moviebuffs.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.sivalabs.moviebuffs.core.utils.Constants.ROLE_USER;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ApplicationException("Email "+user.getEmail()+" is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> roleUser = roleRepository.findByName(ROLE_USER);
        user.setRoles(Collections.singletonList(roleUser.orElse(null)));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        Optional<User> byId = userRepository.findById(user.getId());
        if(byId.isEmpty()) {
            throw new UserNotFoundException("User with id "+user.getId() + " not found");
        }
        user.setRoles(byId.get().getRoles());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        Optional<User> byId = userRepository.findById(userId);
        byId.ifPresent(userRepository::delete);
    }

    public void changePassword(String email, String oldPwd, String newPwd) {
        Optional<User> userByEmail = this.getUserByEmail(email);
        if(userByEmail.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        User user = userByEmail.get();
        if (passwordEncoder.matches(oldPwd, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPwd));
            userRepository.save(user);
        } else {
            throw new ApplicationException("Current password doesn't match");
        }
    }

    public Optional<Role> findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
