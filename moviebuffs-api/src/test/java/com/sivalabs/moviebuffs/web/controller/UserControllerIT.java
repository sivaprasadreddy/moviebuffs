package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.common.AbstractIntegrationTest;
import com.sivalabs.moviebuffs.entity.User;
import com.sivalabs.moviebuffs.service.UserService;
import com.sivalabs.moviebuffs.web.dto.ChangePasswordDTO;
import com.sivalabs.moviebuffs.web.dto.CreateUserRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIT extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldFindUserById() throws Exception {
        Long userId = 1L;
        this.mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateNewUserWithValidData() throws Exception {
        CreateUserRequestDTO createUserRequestDTO = CreateUserRequestDTO.builder()
                .email("myemail@gmail.com")
                .password("secret")
                .name("myname")
                .build();

        this.mockMvc.perform(post("/api/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldFailToCreateNewUserWithExistingEmail() throws Exception {
        CreateUserRequestDTO createUserRequestDTO = CreateUserRequestDTO.builder()
                .email("admin@gmail.com")
                .password("secret")
                .name("myname")
                .build();

        this.mockMvc.perform(post("/api/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser("admin@gmail.com")
    void shouldUpdateOtherUserDetailsWhenUserIsAuthorizedAdmin() throws Exception {
        User savedUser = createUser("someuser1@gmail.com");

        savedUser.setName("New name");
        this.mockMvc.perform(put("/api/users/"+savedUser.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedUser)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser("siva1@gmail.com")
    void shouldUpdateOwnUserDetailsWhenUserIsAuthorized() throws Exception {
        User savedUser = createUser("siva1@gmail.com");

        savedUser.setName("New name");

        this.mockMvc.perform(put("/api/users/"+savedUser.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedUser)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser("siva@gmail.com")
    void shouldNotUpdateOtherUserDetailsWhenUserIsNotAdmin() throws Exception {
        User savedUser = createUser("siva2@gmail.com");
        savedUser.setName("New name");

        this.mockMvc.perform(put("/api/users/"+savedUser.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedUser)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser("admin@gmail.com")
    void shouldFailToDeleteNonExistingUser() throws Exception {
        this.mockMvc.perform(delete("/api/users/{id}", 9999))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser("admin@gmail.com")
    void shouldBeAbleToDeleteOtherUserIfUserIsAdmin() throws Exception {
        User savedUser = createUser("someuser@gmail.com");

        this.mockMvc.perform(delete("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser("siva@gmail.com")
    void shouldNotBeAbleToDeleteOtherUserIfNotAdmin() throws Exception {
        User savedUser = createUser("user123@gmail.com");
        this.mockMvc.perform(delete("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser("siva@gmail.com")
    void shouldUpdatePasswordWhenUserIsAuthorized() throws Exception {
        ChangePasswordDTO changePasswordDTO = ChangePasswordDTO.builder()
                .oldPassword("siva")
                .newPassword("newpwd")
                .build();

        this.mockMvc.perform(post("/api/users/change-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordDTO)))
                .andExpect(status().isOk());

    }

    @Test
    void shouldFailToUpdatePasswordWhenUserIsNotAuthorized() throws Exception {
        ChangePasswordDTO changePasswordDTO = ChangePasswordDTO.builder()
                .oldPassword("admin")
                .newPassword("newpwd")
                .build();

        this.mockMvc.perform(post("/api/users/change-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordDTO)))
                .andExpect(status().isForbidden());

    }

    private User createUser(String email) {
        User user = new User();
        user.setName("someuser");
        user.setEmail(email);
        user.setPassword("secret");
        return userService.createUser(user);
    }
}
