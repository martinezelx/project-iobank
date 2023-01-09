package io.builders.iobank.application.adapter.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.builders.iobank.application.adapter.rest.dto.UserDto;
import io.builders.iobank.application.adapter.rest.mapper.UserMapper;
import io.builders.iobank.domain.exceptions.UserNotFoundException;
import io.builders.iobank.domain.model.User;
import io.builders.iobank.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    void shouldCreateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFullName("John Smith");
        userDto.setUserName("jsmith");
        userDto.setEmail("john@smith.com");

        User user = new User();
        user.setFullName("John Smith");
        user.setUserName("jsmith");
        user.setEmail("john@smith.com");

        when(userMapper.toDomain(userDto)).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName", is("John Smith")))
                .andExpect(jsonPath("$.userName", is("jsmith")))
                .andExpect(jsonPath("$.email", is("john@smith.com")));

        verify(userService, times(1)).saveUser(user);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void shouldGetUsers() throws Exception {
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setFullName("John Smith");
        userDto1.setUserName("jsmith");
        userDto1.setEmail("john@smith.com");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setFullName("Jane Smith");
        userDto2.setUserName("janesmith");
        userDto2.setEmail("jane@smith.com");

        List<UserDto> userDtoList = Arrays.asList(userDto1, userDto2);

        User user1 = new User();
        user1.setId(1L);
        user1.setFullName("John Smith");
        user1.setUserName("jsmith");
        user1.setEmail("john@smith.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setFullName("Jane Smith");
        user2.setUserName("janesmith");
        user2.setEmail("jane@smith.com");

        List<User> userList = Arrays.asList(user1, user2);

        when(userService.getUsers()).thenReturn(userList);
        when(userMapper.listToDto(userList)).thenReturn(userDtoList);

        mockMvc.perform(get("/api/v1/users/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].fullName", is("John Smith")))
                .andExpect(jsonPath("$[0].userName", is("jsmith")))
                .andExpect(jsonPath("$[0].email", is("john@smith.com")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].fullName", is("Jane Smith")))
                .andExpect(jsonPath("$[1].userName", is("janesmith")))
                .andExpect(jsonPath("$[1].email", is("jane@smith.com")));

        verify(userService, times(1)).getUsers();
        verify(userMapper, times(1)).listToDto(userList);
    }

    @Test
    void shouldGetUserById() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFullName("John Smith");
        userDto.setUserName("jsmith");
        userDto.setEmail("john@smith.com");

        User user = new User();
        user.setId(1L);
        user.setFullName("John Smith");
        user.setUserName("jsmith");
        user.setEmail("john@smith.com");

        when(userService.getUser(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/v1/users/search/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("John Smith")))
                .andExpect(jsonPath("$.userName", is("jsmith")))
                .andExpect(jsonPath("$.email", is("john@smith.com")));

        verify(userService, times(1)).getUser(1L);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void shouldReturnUserNotFoundException() throws Exception {
        when(userService.getUser(1L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/search/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(1L);
    }
}