package com.sivalabs.moviebuffs.importer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieCsvRecord {

	private String adult;

	private String belongsToCollection;

	private String budget;

	private String genres;

	private String homepage;

	private String id;

	private String imdbId;

	private String originalLanguage;

	private String originalTitle;

	private String overview;

	private String popularity;

	private String posterPath;

	private String productionCompanies;

	private String productionCountries;

	private String releaseDate;

	private String revenue;

	private String runtime;

	private String spokenLanguages;

	private String status;

	private String tagline;

	private String title;

	private String video;

	private String voteAverage;

	private String voteCount;

}
