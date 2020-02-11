package com.sivalabs.moviebuffs.web.mappers;

import com.sivalabs.moviebuffs.entity.Role;
import com.sivalabs.moviebuffs.entity.User;
import com.sivalabs.moviebuffs.web.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDTOMapper {

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
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
