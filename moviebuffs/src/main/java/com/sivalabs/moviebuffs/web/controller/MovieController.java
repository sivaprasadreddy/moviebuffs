package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.core.entity.Genre;
import com.sivalabs.moviebuffs.core.service.MovieService;
import com.sivalabs.moviebuffs.web.dto.MovieDTO;
import com.sivalabs.moviebuffs.web.mappers.MovieDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MovieController {

	private final MovieService movieService;

	private final MovieDTOMapper movieDTOMapper;

	@GetMapping("/login")
	public String loginForm() {
		return "login";
	}

	@GetMapping({ "/" })
	public String home(@RequestParam(name = "query", required = false) String query,
			@PageableDefault(size = 24) @SortDefault.SortDefaults({
					@SortDefault(sort = "releaseDate", direction = DESC) }) Pageable pageable,
			Model model) {
		Page<MovieDTO> data;
		if (StringUtils.isEmpty(query)) {
			log.info("Fetching movies with page: {}", pageable.getPageNumber());
			data = movieService.findMovies(pageable).map(movieDTOMapper::map);
			model.addAttribute("paginationPrefix", "/?");
		}
		else {
			log.info("Searching movies for {} with page: {}", query, pageable.getPageNumber());
			model.addAttribute("header", "Search results for \"" + query + "\"");
			model.addAttribute("paginationPrefix", "/?query=" + query);
			data = movieService.searchMovies(query, pageable).map(movieDTOMapper::map);
		}

		model.addAttribute("page", data.getNumber() + 1);
		model.addAttribute("moviesData", data);
		return "home";
	}

	@GetMapping({ "/movies/{id}" })
	public String viewBook(@PathVariable Long id, Model model) {
		MovieDTO movie = movieService.findMovieById(id).map(movieDTOMapper::map).orElse(null);
		model.addAttribute("movie", movie);
		return "movie";
	}

	@GetMapping("/genre/{slug}")
	public String byGenre(@PathVariable String slug, @PageableDefault(size = 24) @SortDefault.SortDefaults({
			@SortDefault(sort = "releaseDate", direction = DESC) }) Pageable pageable, Model model) {
		Optional<Genre> byId = movieService.findGenreBySlug(slug);
		if (byId.isPresent()) {
			Page<MovieDTO> data = movieService.findMoviesByGenre(byId.get().getId(), null, pageable)
					.map(movieDTOMapper::map);
			model.addAttribute("header", "Movies by Genre \"" + byId.get().getName() + "\"");
			model.addAttribute("moviesData", data);
			model.addAttribute("page", data.getNumber() + 1);
		}
		else {
			model.addAttribute("header", "Movies by Genre \"" + slug + "\"");
			model.addAttribute("moviesData", new ArrayList<>(0));
			model.addAttribute("page", 1);
		}
		model.addAttribute("paginationPrefix", "/genre/" + slug + "?");
		return "home";
	}

	@ModelAttribute("genres")
	public List<Genre> allGenres() {
		Sort sort = Sort.by(ASC, "name");
		return movieService.findAllGenres(sort);
	}

}
