package io.builders.iobank.domain.service.impl;

import io.builders.iobank.domain.exceptions.UserNotFoundException;
import io.builders.iobank.domain.model.User;
import io.builders.iobank.domain.port.repository.database.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldGetUserSuccess() {
        User user = new User();
        user.setId(1L);
        user.setFullName("John Smith");
        user.setUserName("jsmith");
        user.setEmail("john@smith.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUser(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Smith", result.getFullName());
        assertEquals("jsmith", result.getUserName());
        assertEquals("john@smith.com", result.getEmail());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldGetUsersSuccess() {
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

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("John Smith", result.get(0).getFullName());
        assertEquals("jsmith", result.get(0).getUserName());
        assertEquals("john@smith.com", result.get(0).getEmail());

        assertEquals(2L, result.get(1).getId());
        assertEquals("Jane Smith", result.get(1).getFullName());
        assertEquals("janesmith", result.get(1).getUserName());
        assertEquals("jane@smith.com", result.get(1).getEmail());
    }

    @Test
    void shouldSaveUserSuccess() {
        User user = new User();
        user.setId(1L);
        user.setFullName("John Smith");
        user.setUserName("jsmith");
        user.setEmail("john@smith.com");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Smith", result.getFullName());
        assertEquals("jsmith", result.getUserName());
        assertEquals("john@smith.com", result.getEmail());

        verify(userRepository, times(1)).save(user);
    }
}
