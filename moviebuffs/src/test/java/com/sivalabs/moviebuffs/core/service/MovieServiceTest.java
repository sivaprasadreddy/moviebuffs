package com.sivalabs.moviebuffs.core.service;

import com.sivalabs.moviebuffs.core.entity.Movie;
import com.sivalabs.moviebuffs.core.repository.CastMemberRepository;
import com.sivalabs.moviebuffs.core.repository.CrewMemberRepository;
import com.sivalabs.moviebuffs.core.repository.GenreRepository;
import com.sivalabs.moviebuffs.core.repository.MovieRepository;
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

import static com.sivalabs.moviebuffs.datafactory.TestDataFactory.createMovie;
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
	void should_get_all_movies() {
		Pageable pageable = PageRequest.of(0, 10);
		given(movieRepository.findAll(pageable)).willReturn(new PageImpl<>(new ArrayList<>()));

		Page<Movie> movies = movieService.findMovies(pageable);

		assertThat(movies).isNotNull();
	}

	@Test
	void shout_save_new_movie() {
		Movie movie = createMovie("abcd", "genre-1", "genre-2");
		given(movieRepository.save(any(Movie.class))).willAnswer(answer -> answer.getArgument(0));

		Movie newMovie = movieService.createMovie(movie);

		assertThat(newMovie).isNotNull();
	}

}
