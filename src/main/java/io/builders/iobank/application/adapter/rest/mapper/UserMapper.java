package io.builders.iobank.application.adapter.rest.mapper;

import io.builders.iobank.application.adapter.rest.dto.UserDto;
import io.builders.iobank.domain.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto toDto(User user);

  List<UserDto> listToDto(List<User> users);

  User toDomain(UserDto userDto);
}
