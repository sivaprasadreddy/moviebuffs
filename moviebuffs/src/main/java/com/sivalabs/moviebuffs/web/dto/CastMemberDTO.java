package com.sivalabs.moviebuffs.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CastMemberDTO {

	private Long id;

	@JsonProperty("cast_id")
	private String castId;

	private String character;

	@JsonProperty("credit_id")
	private String creditId;

	private String gender;

	private String uid;

	private String name;

	private String order;

	@JsonProperty("profile_path")
	private String profilePath;

}
