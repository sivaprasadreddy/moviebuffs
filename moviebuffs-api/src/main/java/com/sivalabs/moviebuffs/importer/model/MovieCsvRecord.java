package com.sivalabs.moviebuffs.importer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieCsvRecord {
    private String adult;
    private String belongs_to_collection;
    private String budget;
    private String genres;
    private String homepage;
    private String id;
    private String imdb_id;
    private String original_language;
    private String original_title;
    private String overview;
    private String popularity;
    private String poster_path;
    private String production_companies;
    private String production_countries;
    private String release_date;
    private String revenue;
    private String runtime;
    private String spoken_languages;
    private String status;
    private String tagline;
    private String title;
    private String video;
    private String vote_average;
    private String vote_count;
}
