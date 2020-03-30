package com.sivalabs.moviebuffs.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrewMemberDTO {

	private Long id;

	@JsonProperty("credit_id")
	private String creditId;

	private String department;

	private String gender;

	private String uid;

	private String job;

	private String name;

	@JsonProperty("profile_path")
	private String profilePath;

}
