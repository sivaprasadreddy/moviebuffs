package com.sivalabs.moviebuffs.web.api;

import com.sivalabs.moviebuffs.common.AbstractIntegrationTest;
import com.sivalabs.moviebuffs.config.security.SecurityConfigProperties;
import com.sivalabs.moviebuffs.config.security.TokenHelper;
import com.sivalabs.moviebuffs.core.entity.User;
import com.sivalabs.moviebuffs.core.service.UserService;
import com.sivalabs.moviebuffs.datafactory.TestDataFactory;
import com.sivalabs.moviebuffs.web.dto.AuthenticationRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
    "application.import-tmdb-data=false"
})
class AuthenticationRestControllerIT extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private SecurityConfigProperties securityConfigProperties;

    @Test
    void should_login_successfully_with_valid_credentials() throws Exception {
        User user = createUser();
        AuthenticationRequestDTO authenticationRequestDTO = AuthenticationRequestDTO.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();

        this.mockMvc.perform(post("/api/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void should_not_login_with_invalid_credentials() throws Exception {
        AuthenticationRequestDTO authenticationRequestDTO = AuthenticationRequestDTO.builder()
                .username("nonexisting@gmail.com")
                .password("secret")
                .build();

        this.mockMvc.perform(post("/api/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser("admin@gmail.com")
    void should_get_refreshed_authToken_if_authorized() throws Exception {
        String token = tokenHelper.generateToken("admin@gmail.com");
        this.mockMvc.perform(post("/api/auth/refresh")
                .header(securityConfigProperties.getJwt().getHeader(),"Bearer "+token))
                .andExpect(status().isOk());
    }

    @Test
    void should_fail_to_get_refreshed_authToken_if_unauthorized() throws Exception {
        this.mockMvc.perform(post("/api/auth/refresh"))
                .andExpect(status().isForbidden());
    }

    @Test
    void should_fail_to_get_refreshed_authToken_if_token_is_invalid() throws Exception {
        this.mockMvc.perform(post("/api/auth/refresh")
                .header(securityConfigProperties.getJwt().getHeader(),"Bearer invalid-token"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser("admin@gmail.com")
    void should_get_login_user_details() throws Exception {
        this.mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk());
    }

    @Test
    void should_fail_to_get_login_user_details_if_unauthorized() throws Exception {
        this.mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isForbidden());

    }

    private User createUser() {
        User user = TestDataFactory.createUser();
        String plainPwd = user.getPassword();
        userService.createUser(user);
        user.setPassword(plainPwd);
        return user;
    }
}
