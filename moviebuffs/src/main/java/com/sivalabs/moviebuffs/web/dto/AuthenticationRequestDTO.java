package com.sivalabs.moviebuffs.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class AuthenticationRequestDTO {

	@NotBlank(message = "UserName cannot be blank")
	private String username;

	@NotBlank(message = "Password cannot be blank")
	private String password;

}
