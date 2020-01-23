package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.entity.Movie;
import com.sivalabs.moviebuffs.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class HomeController {
    private final MovieService movieService;

    public HomeController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping({"/"})
    public String home(@RequestParam(required = false) Integer pageNo, Model model) {
        if(pageNo == null || pageNo < 1) pageNo = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        PageRequest pageRequest = PageRequest.of(pageNo-1, 24, sort);
        Page<Movie> data = movieService.getMovies(pageRequest);
        model.addAttribute("paginationPrefix", "/");
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("moviesData", data);
        return "home";
    }

    @GetMapping({"/movies/{id}"})
    public String viewBook(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id).orElse(null);
        model.addAttribute("movie", movie);
        return "movie";
    }
}
