package com.sivalabs.moviebuffs.service;

import com.sivalabs.moviebuffs.entity.Movie;
import com.sivalabs.moviebuffs.repository.CastMemberRepository;
import com.sivalabs.moviebuffs.repository.CrewMemberRepository;
import com.sivalabs.moviebuffs.repository.GenreRepository;
import com.sivalabs.moviebuffs.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private CastMemberRepository castMemberRepository;
    @Mock
    private CrewMemberRepository crewMemberRepository;
    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    void shouldGetAllMovies() {
        Pageable pageable = PageRequest.of(0,10);
        given(movieRepository.findMoviesWithCastAndCrew(pageable)).willReturn(new PageImpl<>(new ArrayList<>()));
        Page<Movie> movies = movieService.findMovies(pageable);
        assertThat(movies).isNotNull();
    }
}
