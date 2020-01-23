package com.sivalabs.moviebuffs.service;

import com.sivalabs.moviebuffs.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

class MovieServiceTest {

    private MovieRepository movieRepository;

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieRepository = mock(MovieRepository.class);
        movieService = new MovieService(movieRepository);
    }

}
