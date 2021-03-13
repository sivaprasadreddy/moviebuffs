package com.sivalabs.moviebuffs.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.moviebuffs.common.AbstractIntegrationTest;
import com.sivalabs.moviebuffs.core.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MovieControllerIT extends AbstractIntegrationTest {

    @Autowired private MovieService movieService;

    @BeforeEach
    void setUp() {}

    @Test
    void shouldFetchAllCategories() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk());
    }
}
