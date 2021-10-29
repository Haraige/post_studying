package ua.org.code.service;

import ua.org.code.persistence.entity.User;

import java.util.List;

public interface UserService {
    User create(User entity);
    User update(User entity);
    void remove(User entity);
    User findById(Long id);
    List<User> findAll();
    User findByLogin(String login);
    User findByEmail(String email);
}
