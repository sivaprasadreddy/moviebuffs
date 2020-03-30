package com.sivalabs.moviebuffs.importer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CastMemberRecord implements Serializable {

	@JsonProperty("cast_id")
	private String castId;

	private String character;

	@JsonProperty("credit_id")
	private String creditId;

	private String gender;

	private String id;

	private String name;

	private String order;

	@JsonProperty("profile_path")
	private String profilePath;

}
