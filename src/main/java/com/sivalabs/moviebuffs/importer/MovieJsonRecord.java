package com.sivalabs.moviebuffs.importer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class MovieJsonRecord {

    private Boolean adult;

    private Double budget;

    private List<Genre> genres;

    private String homepage;

    private Long id;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;

    private String overview;

    private Double popularity;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("release_date")
    private String releaseDate;

    private Double revenue;

    private Integer runtime;

    private String status;

    private String tagline;

    private String title;

    private Boolean video;

    @JsonProperty("vote_average")
    private Double voteAverage;

    @JsonProperty("vote_count")
    private Long voteCount;

    @Data
    public static class Genre {
        private Integer id;
        private String name;
    }
}
