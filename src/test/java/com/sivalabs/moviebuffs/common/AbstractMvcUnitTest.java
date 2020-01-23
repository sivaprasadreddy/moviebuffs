package com.sivalabs.moviebuffs.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.moviebuffs.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(Constants.PROFILE_TEST)
public class AbstractMvcUnitTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUpBase() {
        /*objectMapper.registerModule(new ProblemModule());
        objectMapper.registerModule(new ConstraintViolationProblemModule());*/
    }
}
