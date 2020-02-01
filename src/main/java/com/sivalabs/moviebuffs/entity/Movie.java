package com.sivalabs.moviebuffs.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="movies")
@Setter
@Getter
public class Movie implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "movie_id_generator", sequenceName = "movie_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "movie_id_generator")
	private Long id;

	private String title;

	@JsonProperty("imdb_id")
	@Column(name = "imdb_id")
	private String imdbId;

	private String overview;

	private String tagline;

	private String runtime;

	private String revenue;

	@JsonProperty("release_date")
	@Column(name = "release_date")
	private LocalDate releaseDate;

	@JsonProperty("poster_path")
	@Column(name = "poster_path")
	private String posterPath;

	private String budget;

	private String homepage;

	@JsonProperty("original_language")
	@Column(name = "original_language")
	private String originalLanguage;

	@JsonProperty("vote_average")
	@Column(name = "vote_average")
	private Double voteAverage;

	@JsonProperty("vote_count")
	@Column(name = "vote_count")
	private Double voteCount;

	@JsonProperty("created_at")
	@Column(updatable = false)
	protected LocalDateTime createdAt = LocalDateTime.now();

	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(
			name="movie_genre",
			joinColumns={@JoinColumn(name="MOVIE_ID", referencedColumnName="ID")},
			inverseJoinColumns={@JoinColumn(name="GENRE_ID", referencedColumnName="ID")})
	private List<Genre> genres = new ArrayList<>();

	@JsonProperty("updated_at")
	@Column(insertable = false)
	protected LocalDateTime updatedAt = LocalDateTime.now();

	@PrePersist
	public void onCreate() {
		createdAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
