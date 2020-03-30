package com.sivalabs.moviebuffs.web.api;

import com.sivalabs.moviebuffs.core.entity.Genre;
import com.sivalabs.moviebuffs.core.exception.ResourceNotFoundException;
import com.sivalabs.moviebuffs.core.service.MovieService;
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

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MoviesRestController {

	private static final int DEFAULT_PAGE_SIZE = 24;

	private final MovieService movieService;

	private final MovieDTOMapper movieDTOMapper;

	@GetMapping("/movies")
	public MoviesResponseDTO getMovies(@RequestParam(name = "genre", required = false) String genreSlug,
			@RequestParam(name = "query", required = false) String query,
			@PageableDefault(size = DEFAULT_PAGE_SIZE) @SortDefault.SortDefaults({
					@SortDefault(sort = "releaseDate", direction = DESC) }) Pageable pageable) {
		log.info("API Fetching movies for page : {}, query: {}, genre: {}", pageable.getPageNumber(), query, genreSlug);
		Page<MovieDTO> moviesPage;
		if (StringUtils.isBlank(genreSlug) && StringUtils.isNotBlank(query)) {
			moviesPage = movieService.searchMovies(query, pageable).map(movieDTOMapper::map);
		}
		else if (StringUtils.isNotBlank(genreSlug)) {
			moviesPage = getMoviesByGenreSlug(genreSlug, query, pageable);
		}
		else {
			moviesPage = movieService.findMovies(pageable).map(movieDTOMapper::map);
		}
		return new MoviesResponseDTO(moviesPage);
	}

	@GetMapping("/movies/{id}")
	public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
		Optional<MovieDTO> movieById = movieService.findMovieById(id).map(movieDTOMapper::map);
		return movieById.map(ResponseEntity::ok)
				.orElseThrow(() -> new ResourceNotFoundException("Movie not found with id=" + id));
	}

	@GetMapping("/genres")
	public List<Genre> getGenres() {
		return movieService.findAllGenres(Sort.by("name"));
	}

	private Page<MovieDTO> getMoviesByGenreSlug(String genreSlug, String query, Pageable pageable) {
		Optional<Genre> byId = movieService.findGenreBySlug(genreSlug);
		return byId
				.map(genre -> movieService.findMoviesByGenre(genre.getId(), query, pageable).map(movieDTOMapper::map))
				.orElseGet(() -> new PageImpl<>(new ArrayList<>()));
	}

}
