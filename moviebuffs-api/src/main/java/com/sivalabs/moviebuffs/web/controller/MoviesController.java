package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.config.Loggable;
import com.sivalabs.moviebuffs.entity.Genre;
import com.sivalabs.moviebuffs.exception.ResourceNotFoundException;
import com.sivalabs.moviebuffs.service.MovieService;
import com.sivalabs.moviebuffs.web.dto.MovieDTO;
import com.sivalabs.moviebuffs.web.dto.MoviesResponseDTO;
import com.sivalabs.moviebuffs.web.mappers.MovieDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Loggable
public class MoviesController {
    private static final int DEFAULT_PAGE_SIZE = 24;

    private final MovieService movieService;
    private final MovieDTOMapper movieDTOMapper;

    @GetMapping("/movies")
    public MoviesResponseDTO getMovies(
            @RequestParam(name = "genre", required = false) String genreSlug,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            @SortDefault.SortDefaults({@SortDefault(sort = "title", direction = ASC)})
            Pageable pageable) {
        log.info("API Fetching movies for page {}", pageable.getPageNumber());
        Page<MovieDTO> moviesPage;
        if(StringUtils.isNotBlank(genreSlug)) {
            moviesPage = getMoviesByGenreSlug(genreSlug, pageable);
        } else {
            moviesPage = movieService.findMovies(pageable).map(movieDTOMapper::map);
        }
        log.info("Current page {}", moviesPage.getNumber());
        return new MoviesResponseDTO(moviesPage);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        Optional<MovieDTO> movieById = movieService.findMovieById(id).map(movieDTOMapper::map);
        return movieById.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id="+id));
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return movieService.findAllGenres(Sort.by("name"));
    }

    private Page<MovieDTO> getMoviesByGenreSlug(String genreSlug, Pageable pageable) {
        Optional<Genre> byId = movieService.findGenreBySlug(genreSlug);
        return byId.map(genre -> movieService.findMoviesByGenre(genre.getId(), pageable).map(movieDTOMapper::map))
                .orElseGet(() -> new PageImpl<>(new ArrayList<>()));
    }
}
