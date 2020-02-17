package com.sivalabs.moviebuffs.service;

import com.sivalabs.moviebuffs.entity.Genre;
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
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void shoutSaveNewMovie() {
        Movie movie = new Movie();
        movie.setTitle("abcd");
        Set<Genre> genres = new HashSet<>();
        Genre genre1 = new Genre();
        genre1.setName("genre-1");
        genre1.setSlug("genre-1");
        genres.add(genre1);
        movie.setGenres(genres);

        given(movieRepository.save(any(Movie.class))).willAnswer(answer-> answer.getArgument(0));

        Movie newMovie = movieService.createMovie(movie);
        assertThat(newMovie).isNotNull();
    }
}
