package com.sivalabs.moviebuffs.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CreateUserRequestDTO {
    @NotBlank(message = "Name cannot be blank")
    String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email address")
    String email;

    @NotBlank(message = "Password cannot be blank")
    String password;
}
