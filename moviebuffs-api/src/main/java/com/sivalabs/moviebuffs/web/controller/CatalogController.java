package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.entity.Genre;
import com.sivalabs.moviebuffs.models.ProductDTO;
import com.sivalabs.moviebuffs.models.ProductsResponse;
import com.sivalabs.moviebuffs.service.CatalogService;
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
public class CatalogController {
    private static final int DEFAULT_PAGE_SIZE = 25;

    private final CatalogService catalogService;

    @GetMapping("/products")
    public ProductsResponse getMovies(
            @RequestParam(name = "genre", required = false) String genreSlug,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            @SortDefault.SortDefaults({@SortDefault(sort = "title", direction = ASC)})
            Pageable pageable) {
        log.info("API Fetching movies for page {}", pageable.getPageNumber());
        Page<ProductDTO> moviesPage;
        if(StringUtils.isNotBlank(genreSlug)) {
            moviesPage = getMoviesByGenreSlug(genreSlug, pageable);
        } else {
            moviesPage = catalogService.getProducts(pageable);
        }
        log.info("Current page {}", moviesPage.getNumber());
        return new ProductsResponse(moviesPage);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getMovieById(@PathVariable Long id) {
        Optional<ProductDTO> movieById = catalogService.getProductById(id);
        return movieById.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return catalogService.findAllGenres(Sort.by("name"));
    }

    private Page<ProductDTO> getMoviesByGenreSlug(String genreSlug, Pageable pageable) {
        Optional<Genre> byId = catalogService.findGenreBySlug(genreSlug);
        return byId.map(genre -> catalogService.findProductsByGenre(genre.getId(), pageable))
                .orElseGet(() -> new PageImpl<>(new ArrayList<>()));
    }
}
