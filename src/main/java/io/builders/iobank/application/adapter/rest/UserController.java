package io.builders.iobank.application.adapter.rest;

import io.builders.iobank.application.adapter.rest.dto.UserDto;
import io.builders.iobank.domain.service.UserService;
import io.builders.iobank.application.adapter.rest.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/create")
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto) {
      return new ResponseEntity<>(userMapper.toDto(userService.saveUser(
              userMapper.toDomain(userDto))),
              HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> getUsers() {
      return new ResponseEntity<>(
              userMapper.listToDto(userService.getUsers()),
              HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
      return new ResponseEntity<>(
              userMapper.toDto(userService.getUser(id)),
              HttpStatus.OK);
    }
}