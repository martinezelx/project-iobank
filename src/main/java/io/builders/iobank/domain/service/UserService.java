package io.builders.iobank.domain.service;

import io.builders.iobank.domain.model.User;

import java.util.List;

public interface UserService {
    User getUser(Long id);

    List<User> getUsers();

    User saveUser(User user);
}
