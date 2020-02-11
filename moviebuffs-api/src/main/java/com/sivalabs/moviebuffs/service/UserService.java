package com.sivalabs.moviebuffs.service;

import com.sivalabs.moviebuffs.entity.Role;
import com.sivalabs.moviebuffs.entity.User;
import com.sivalabs.moviebuffs.exception.ApplicationException;
import com.sivalabs.moviebuffs.exception.UserNotFoundException;
import com.sivalabs.moviebuffs.models.ChangePasswordRequest;
import com.sivalabs.moviebuffs.models.UserDTO;
import com.sivalabs.moviebuffs.repository.RoleRepository;
import com.sivalabs.moviebuffs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO createUser(UserDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ApplicationException("Email "+user.getEmail()+" is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userEntity = user.toEntity();
        Optional<Role> role_user = roleRepository.findByName("ROLE_USER");
        userEntity.setRoles(Collections.singletonList(role_user.orElse(null)));
        return UserDTO.fromEntity(userRepository.save(userEntity));
    }

    public UserDTO updateUser(UserDTO user) {
        Optional<User> byId = userRepository.findById(user.getId());
        if(!byId.isPresent()) {
            throw new UserNotFoundException("User with id "+user.getId() + " not found");
        }
        User userEntity = user.toEntity();
        userEntity.setPassword(byId.get().getPassword());
        userEntity.setRoles(byId.get().getRoles());
        return UserDTO.fromEntity(userRepository.save(userEntity));

    }

    public void deleteUser(Long userId) {
        Optional<User> byId = userRepository.findById(userId);
        byId.ifPresent(userRepository::delete);
    }

    public void changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        Optional<User> userByEmail = this.getUserByEmail(email);
        if(!userByEmail.isPresent()) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        User user = userByEmail.get();
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new ApplicationException("Current password doesn't match");
        }
    }
}
