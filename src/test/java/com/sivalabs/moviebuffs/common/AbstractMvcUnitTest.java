package com.sivalabs.moviebuffs.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.moviebuffs.core.service.SecurityService;
import com.sivalabs.moviebuffs.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(Constants.PROFILE_TEST)
@Import({TestConfig.class})
public abstract class AbstractMvcUnitTest {

    @Autowired protected MockMvc mockMvc;

    @Autowired protected ObjectMapper objectMapper;

    @MockBean protected UserDetailsService userDetailsService;

    @MockBean protected SecurityService securityService;
}
