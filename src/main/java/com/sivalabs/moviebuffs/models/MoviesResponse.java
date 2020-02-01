package com.sivalabs.moviebuffs.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class MoviesResponse {
    private List<MovieDTO> movies;
    private long totalElements;
    private int pageNumber;
    private int totalPages;
    @JsonProperty("isFirst")
    boolean isFirst;
    @JsonProperty("isLast")
    boolean isLast;
    @JsonProperty("hasNext")
    boolean hasNext;
    @JsonProperty("hasPrevious")
    boolean hasPrevious;

    public MoviesResponse(Page<MovieDTO> moviesPage) {
        this.setMovies(moviesPage.getContent());
        this.setTotalElements(moviesPage.getTotalElements());
        this.setPageNumber(moviesPage.getNumber()+1); // 1 - based page numbering
        this.setTotalPages(moviesPage.getTotalPages());
        this.setFirst(moviesPage.isFirst());
        this.setHasNext(moviesPage.hasNext());
        this.setHasPrevious(moviesPage.hasPrevious());
    }
}
