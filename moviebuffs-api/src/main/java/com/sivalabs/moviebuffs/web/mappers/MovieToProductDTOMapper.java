package com.sivalabs.moviebuffs.web.mappers;

import com.sivalabs.moviebuffs.entity.Movie;
import com.sivalabs.moviebuffs.models.ProductDTO;
import org.springframework.stereotype.Component;

import static com.sivalabs.moviebuffs.utils.Constants.TMDB_IMAGE_PATH_PREFIX;

@Component
public class MovieToProductDTOMapper {

    public ProductDTO map(Movie movie) {
        if(movie == null) return null;
        return ProductDTO.builder()
                .id(movie.getId())
                .budget(movie.getBudget())
                .homepage(movie.getHomepage())
                .imdbId(movie.getImdbId())
                .originalLanguage(movie.getOriginalLanguage())
                .overview(movie.getOverview())
                .posterPath(TMDB_IMAGE_PATH_PREFIX + movie.getPosterPath())
                .releaseDate(movie.getReleaseDate())
                .revenue(movie.getRevenue())
                .tagline(movie.getTagline())
                .runtime(movie.getRuntime())
                .title(movie.getTitle())
                .price(movie.getPrice())
                .build();
    }
}