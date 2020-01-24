package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.entity.Genre;
import com.sivalabs.moviebuffs.entity.Movie;
import com.sivalabs.moviebuffs.models.MovieDTO;
import com.sivalabs.moviebuffs.service.MovieService;
import com.sivalabs.moviebuffs.web.mappers.MovieDTOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class HomeController {
    private final MovieService movieService;
    private final MovieDTOMapper movieDTOMapper;

    public HomeController(MovieService movieService, MovieDTOMapper movieDTOMapper) {
        this.movieService = movieService;
        this.movieDTOMapper = movieDTOMapper;
    }

    @GetMapping({"/"})
    public String home(@RequestParam(required = false) Integer pageNo, Model model) {
        if(pageNo == null || pageNo < 1) pageNo = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        PageRequest pageRequest = PageRequest.of(pageNo-1, 24, sort);
        Page<MovieDTO> data = movieService.getMovies(pageRequest).map(movieDTOMapper::map);
        model.addAttribute("paginationPrefix", "/");
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("moviesData", data);
        return "home";
    }

    @GetMapping({"/movies/{id}"})
    public String viewBook(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id).orElse(null);
        model.addAttribute("movie", movieDTOMapper.map(movie));
        return "movie";
    }

    @GetMapping("/genre/{slug}")
    public String byGenre(@PathVariable String slug,
                          @RequestParam(required = false) Integer pageNo,
                          Model model) {
        if(pageNo == null || pageNo < 1) pageNo = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        PageRequest pageRequest = PageRequest.of(pageNo-1, 24, sort);
        Optional<Genre> byId = movieService.findGenreBySlug(slug);
        if(byId.isPresent()) {
            Page<MovieDTO> data = movieService.findMoviesByGenre(byId.get().getId(), pageRequest)
                    .map(movieDTOMapper::map);;
            model.addAttribute("header", "Movies by Genre \""+ byId.get().getName()+"\"");
            model.addAttribute("moviesData", data);
        }
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("paginationPrefix", "/genre/"+slug);
        return "home";
    }

    @ModelAttribute("genres")
    public List<Genre> allGenres() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        return movieService.findAllGenres(sort);
    }
}
