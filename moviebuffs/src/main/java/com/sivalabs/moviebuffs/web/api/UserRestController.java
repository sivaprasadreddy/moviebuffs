package com.sivalabs.moviebuffs.web.api;

import com.sivalabs.moviebuffs.core.entity.User;
import com.sivalabs.moviebuffs.core.exception.BadRequestException;
import com.sivalabs.moviebuffs.core.exception.ResourceNotFoundException;
import com.sivalabs.moviebuffs.core.service.SecurityService;
import com.sivalabs.moviebuffs.core.service.UserService;
import com.sivalabs.moviebuffs.web.dto.ChangePasswordDTO;
import com.sivalabs.moviebuffs.web.dto.CreateUserRequestDTO;
import com.sivalabs.moviebuffs.web.dto.UserDTO;
import com.sivalabs.moviebuffs.web.mappers.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final UserDTOMapper userDTOMapper;
    private final SecurityService securityService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info("process=get_user, user_id={}", id);
        return userService.getUserById(id)
                .map(userDTOMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @ResponseStatus(CREATED)
    public UserDTO createUser(@RequestBody @Valid CreateUserRequestDTO createUserRequestDTO) {
        log.info("process=create_user, user_email={}", createUserRequestDTO.getEmail());
        UserDTO userDTO = new UserDTO(
                null,
                createUserRequestDTO.getName(),
                createUserRequestDTO.getEmail(),
                createUserRequestDTO.getPassword(),
                null
        );
        User user = userDTOMapper.toEntity(userDTO);
        User savedUser = userService.createUser(user);
        return userDTOMapper.toDTO(savedUser);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) {
        log.info("process=update_user, user_id="+id);
        if (!securityService.isCurrentUserHasPrivilege(id)) {
            throw new BadRequestException("You can't mess with other user details");
        } else {
            userDTO.setId(id);
            User user = userDTOMapper.toEntity(userDTO);
            User savedUser = userService.updateUser(user);
            return userDTOMapper.toDTO(savedUser);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("process=delete_user, user_id="+id);
        User deletedUser = userService.getUserById(id).map(u -> {
            if (!securityService.isCurrentUserHasPrivilege(u.getId())) {
                throw new BadRequestException("User not found with id=" + id);
            } else {
                userService.deleteUser(id);
                return u;
            }
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id=" + id));
        log.info("Deleted user with id :{}", deletedUser.getId());
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String email = currentUser.getName();
        log.info("process=change_password, email={}", email);
        userService.changePassword(email, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
    }
}
