package com.sivalabs.moviebuffs.importer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CrewMemberRecord implements Serializable {

	@JsonProperty("credit_id")
	private String creditId;

	private String department;

	private String gender;

	private String id;

	private String job;

	private String name;

	@JsonProperty("profile_path")
	private String profilePath;

}
