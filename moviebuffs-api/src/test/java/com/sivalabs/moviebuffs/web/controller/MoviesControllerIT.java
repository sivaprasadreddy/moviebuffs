package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static com.sivalabs.moviebuffs.utils.TestConstants.MOVIES_COLLECTION_BASE_PATH;
import static com.sivalabs.moviebuffs.utils.TestConstants.MOVIES_SINGLE_BASE_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MoviesControllerIT extends AbstractIntegrationTest {

    @Test
    void shouldFetchAllMovies() throws Exception {
        this.mockMvc.perform(get(MOVIES_COLLECTION_BASE_PATH))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFetchMoviesByGenre() throws Exception {
        this.mockMvc.perform(get(MOVIES_COLLECTION_BASE_PATH+"?genre=comedy"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFetchMovieById() throws Exception {
        this.mockMvc.perform(get(MOVIES_SINGLE_BASE_PATH, 1))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenMovieIdIsNotExists() throws Exception {
        this.mockMvc.perform(get(MOVIES_SINGLE_BASE_PATH, 999999))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFetchAllGenres() throws Exception {
        this.mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk());
    }
}
