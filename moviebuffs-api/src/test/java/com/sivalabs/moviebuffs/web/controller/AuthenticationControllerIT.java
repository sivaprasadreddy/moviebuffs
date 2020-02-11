package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.common.AbstractIntegrationTest;
import com.sivalabs.moviebuffs.config.ApplicationProperties;
import com.sivalabs.moviebuffs.config.security.TokenHelper;
import com.sivalabs.moviebuffs.models.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Test
    void shouldLoginSuccessfullyWithValidCredentials() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username("admin@gmail.com")
                .password("admin")
                .build();

        this.mockMvc.perform(post("/api/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk());

    }

    @Test
    void shouldNotLoginWithInvalidCredentials() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username("mymail@gmail.com")
                .password("secret")
                .build();

        this.mockMvc.perform(post("/api/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser("admin@gmail.com")
    void shouldGetRefreshedAuthToken() throws Exception {
        String token = tokenHelper.generateToken("admin@gmail.com");
        this.mockMvc.perform(post("/api/auth/refresh")
                .header(applicationProperties.getJwt().getHeader(),"Bearer "+token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailToGetRefreshedAuthTokenIfUnauthorized() throws Exception {
        this.mockMvc.perform(post("/api/auth/refresh"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldFailToGetRefreshedAuthTokenIfTokenIsInvalid() throws Exception {
        this.mockMvc.perform(post("/api/auth/refresh")
                .header(applicationProperties.getJwt().getHeader(),"Bearer invalid-token"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser("admin@gmail.com")
    void shouldGetLoginUserDetails() throws Exception {
        this.mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk());

    }

    @Test
    void shouldFailToGetLoginUserDetailsIfUnauthorized() throws Exception {
        this.mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isForbidden());

    }
}
