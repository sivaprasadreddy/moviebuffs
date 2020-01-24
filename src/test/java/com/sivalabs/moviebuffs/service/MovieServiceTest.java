package com.sivalabs.moviebuffs.service;

import com.sivalabs.moviebuffs.repository.CastMemberRepository;
import com.sivalabs.moviebuffs.repository.CrewMemberRepository;
import com.sivalabs.moviebuffs.repository.GenreRepository;
import com.sivalabs.moviebuffs.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

class MovieServiceTest {

    private MovieRepository movieRepository;
    private CastMemberRepository castMemberRepository;
    private CrewMemberRepository crewMemberRepository;
    private GenreRepository genreRepository;

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieRepository = mock(MovieRepository.class);
        movieService = new MovieService(movieRepository, castMemberRepository, crewMemberRepository, genreRepository);
    }

}
