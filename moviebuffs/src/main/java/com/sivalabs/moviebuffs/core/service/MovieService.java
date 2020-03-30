package com.sivalabs.moviebuffs.core.service;

import com.sivalabs.moviebuffs.core.entity.CastMember;
import com.sivalabs.moviebuffs.core.entity.CrewMember;
import com.sivalabs.moviebuffs.core.entity.Genre;
import com.sivalabs.moviebuffs.core.entity.Movie;
import com.sivalabs.moviebuffs.core.repository.CastMemberRepository;
import com.sivalabs.moviebuffs.core.repository.CrewMemberRepository;
import com.sivalabs.moviebuffs.core.repository.GenreRepository;
import com.sivalabs.moviebuffs.core.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

	private final MovieRepository movieRepository;

	private final CastMemberRepository castMemberRepository;

	private final CrewMemberRepository crewMemberRepository;

	private final GenreRepository genreRepository;

	public void cleanupMovieData() {
		crewMemberRepository.deleteAll();
		castMemberRepository.deleteAll();
		movieRepository.deleteAll();
	}

	@Transactional(readOnly = true)
	public Optional<Movie> findMovieById(Long id) {
		return movieRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Page<Movie> findMovies(Pageable pageable) {
		return movieRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Page<Movie> findMoviesByGenre(Long genreId, String query, Pageable pageable) {
		String searchQuery = StringUtils.trimToEmpty(query).replaceAll("%", "");
		return movieRepository.findByGenre(genreId, "%" + searchQuery + "%", pageable);
	}

	@Transactional(readOnly = true)
	public Page<Movie> searchMovies(String query, Pageable pageable) {
		return movieRepository.findByTitleContainingIgnoreCase(query, pageable);
	}

	@Transactional(readOnly = true)
	public List<Genre> findAllGenres(Sort sort) {
		return genreRepository.findAll(sort);
	}

	@Transactional(readOnly = true)
	public Optional<Genre> findGenreBySlug(String slug) {
		return genreRepository.findBySlug(slug);
	}

	@Transactional(readOnly = true)
	public Optional<Movie> findByTmdbId(String tmdbId) {
		return movieRepository.findByTmdbId(tmdbId);
	}

	public Movie createMovie(Movie movie) {
		Set<Genre> genreList = saveGenres(movie.getGenres());
		movie.setGenres(genreList);
		return movieRepository.save(movie);
	}

	public List<Movie> createMovies(List<Movie> movies) {
		return movieRepository.saveAll(movies);
	}

	private Set<Genre> saveGenres(Set<Genre> genres) {
		Set<Genre> genreList = new HashSet<>();
		for (Genre genre : genres) {
			Optional<Genre> byId = genreRepository.findByName(genre.getName());
			if (byId.isPresent()) {
				genreList.add(byId.get());
			}
			else {
				genreList.add(genreRepository.save(genre));
			}
		}
		return genreList;
	}

	public List<CastMember> saveAllCastMembers(List<CastMember> castMembers) {
		return castMemberRepository.saveAll(castMembers);
	}

	public List<CrewMember> saveAllCrewMembers(List<CrewMember> crewMembers) {
		return crewMemberRepository.saveAll(crewMembers);
	}

	public Genre saveGenre(Genre genre) {
		return genreRepository.save(genre);
	}

}
