package com.sivalabs.moviebuffs.web.mappers;

import com.sivalabs.moviebuffs.core.entity.Role;
import com.sivalabs.moviebuffs.core.entity.User;
import com.sivalabs.moviebuffs.core.service.UserService;
import com.sivalabs.moviebuffs.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDTOMapper {

	private final UserService userService;

	public User toEntity(UserDTO userDTO) {
		User user = new User();
		user.setId(userDTO.getId());
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
		user.setPassword(userDTO.getPassword());
		if (userDTO.getRoles() != null) {
			user.setRoles(userDTO.getRoles().stream().map(r -> userService.findRoleByName(r).orElse(null))
					.collect(Collectors.toList()));
		}
		return user;
	}

	public UserDTO toDTO(User user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setPassword(user.getPassword());
		dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
		return dto;
	}

}
