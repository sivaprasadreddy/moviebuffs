package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.common.AbstractIntegrationTest;
import com.sivalabs.moviebuffs.models.ChangePasswordRequest;
import com.sivalabs.moviebuffs.models.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIT extends AbstractIntegrationTest {

    @Test
    void shouldFindUserById() throws Exception {
        Long userId = 1L;
        this.mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateNewUser() throws Exception {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email("myemail@gmail.com")
                .password("secret")
                .name("myname")
                .build();

        this.mockMvc.perform(post("/api/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser("siva@gmail.com")
    void shouldUpdatePasswordWhenUserIsAuthorized() throws Exception {
        ChangePasswordRequest changePasswordRequest= ChangePasswordRequest.builder()
                .oldPassword("siva")
                .newPassword("newpwd")
                .build();

        this.mockMvc.perform(post("/api/users/change-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isOk());

    }

    @Test
    void shouldFailToUpdatePasswordWhenUserIsNotAuthorized() throws Exception {
        ChangePasswordRequest changePasswordRequest= ChangePasswordRequest.builder()
                .oldPassword("admin")
                .newPassword("newpwd")
                .build();

        this.mockMvc.perform(post("/api/users/change-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isForbidden());

    }
}
