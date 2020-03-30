package com.sivalabs.moviebuffs.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long id;

	@NotBlank(message = "Name cannot be blank")
	private String name;

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Invalid email address")
	private String email;

	@NotBlank(message = "Password cannot be blank")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private List<String> roles;

}
