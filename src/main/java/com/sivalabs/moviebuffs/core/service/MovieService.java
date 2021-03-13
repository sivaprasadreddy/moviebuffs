package com.sivalabs.moviebuffs.core.service;

import com.sivalabs.moviebuffs.config.Loggable;
import com.sivalabs.moviebuffs.core.entity.Genre;
import com.sivalabs.moviebuffs.core.entity.Movie;
import com.sivalabs.moviebuffs.core.repository.GenreRepository;
import com.sivalabs.moviebuffs.core.repository.MovieRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@Loggable
public class MovieService {

    private final MovieRepository movieRepository;

    private final GenreRepository genreRepository;

    public void cleanupMovieData() {
        movieRepository.deleteAllInBatch();
    }

    @Transactional(readOnly = true)
    public Optional<Movie> findMovieById(Long id) {
        return movieRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Movie> findMovieByTmdbId(Long tmdbId) {
        return movieRepository.findByTmdbId(tmdbId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "movies")
    public Page<Movie> findMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Movie> findMoviesByGenre(Long genreId, Pageable pageable) {
        return movieRepository.findByGenre(genreId, pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "movies-by-search")
    public Page<Movie> searchMovies(String query, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(query, pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "genres")
    public List<Genre> findAllGenres(Sort sort) {
        return genreRepository.findAll(sort);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "genre-by-slug")
    public Optional<Genre> findGenreBySlug(String slug) {
        return genreRepository.findBySlug(slug);
    }

    public Movie createMovie(Movie movie) {
        // Set<Genre> genreList = saveGenres(movie.getGenres());
        // movie.setGenres(genreList);
        return movieRepository.save(movie);
    }

    @CacheEvict(
            value = {"movies", "movies-by-search"},
            allEntries = true)
    public List<Movie> createMovies(List<Movie> movies) {
        return movieRepository.saveAll(movies);
    }

    /*private Set<Genre> saveGenres(Set<Genre> genres) {
        Set<Genre> genreList = new HashSet<>();
        for (Genre genre : genres) {
            Optional<Genre> byId = genreRepository.findByName(genre.getName());
            if (byId.isPresent()) {
                genreList.add(byId.get());
            } else {
                genreList.add(genreRepository.save(genre));
            }
        }
        return genreList;
    }*/

    @CacheEvict(
            value = {"genres", "genre-by-slug"},
            allEntries = true)
    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }
}
