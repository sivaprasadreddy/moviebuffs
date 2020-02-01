package com.sivalabs.moviebuffs.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

@Data
@Builder
public class MovieDTO {
    private Long id;

    private String title;

    @JsonProperty("imdb_id")
    private String imdbId;

    private String overview;

    private String tagline;

    private String runtime;

    private String revenue;

    @JsonProperty("release_date")
    private LocalDate releaseDate;

    @JsonProperty("poster_path")
    private String posterPath;

    private String budget;

    private String homepage;

    @JsonProperty("original_language")
    private String originalLanguage;

    public String getTrimmedTitle() {
        return StringUtils.abbreviate(title, 30);
    }

    public String getTrimmedOverview() {
        return StringUtils.abbreviate(overview, 100);
    }

    public String getTrimmedTagline() {
        return StringUtils.abbreviate(tagline, 100);
    }
}
