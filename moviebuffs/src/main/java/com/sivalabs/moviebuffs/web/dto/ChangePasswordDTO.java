package com.sivalabs.moviebuffs.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ChangePasswordDTO {

	@NotBlank(message = "Old password cannot be blank")
	String oldPassword;

	@NotBlank(message = "New password cannot be blank")
	String newPassword;

}
