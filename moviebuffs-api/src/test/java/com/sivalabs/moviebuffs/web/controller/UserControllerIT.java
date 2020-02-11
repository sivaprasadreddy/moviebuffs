package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.common.AbstractIntegrationTest;
import com.sivalabs.moviebuffs.web.dto.ChangePasswordDTO;
import com.sivalabs.moviebuffs.web.dto.CreateUserRequestDTO;
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
}
