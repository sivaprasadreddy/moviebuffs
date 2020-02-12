package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.common.AbstractMvcUnitTest;
import com.sivalabs.moviebuffs.entity.Movie;
import com.sivalabs.moviebuffs.service.MovieService;
import com.sivalabs.moviebuffs.web.mappers.MovieDTOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static com.sivalabs.moviebuffs.utils.TestConstants.MOVIES_COLLECTION_BASE_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MoviesController.class)
public class MoviesControllerTest extends AbstractMvcUnitTest {

    @MockBean
    private MovieService movieService;

    @MockBean
    private MovieDTOMapper movieDTOMapper;

    @Test
    void shouldReturnMovies() throws Exception {
        Page<Movie> page = new PageImpl<>(new ArrayList<>());
        given(movieService.findMovies(any(Pageable.class))).willReturn(page);

        this.mockMvc.perform(get(MOVIES_COLLECTION_BASE_PATH))
                .andExpect(status().isOk());
    }
}