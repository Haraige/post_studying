package ua.org.code.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.org.code.exception.ValidationException;
import ua.org.code.exception.user.UserNotFoundException;
import ua.org.code.exception.user.UserRemoveException;
import ua.org.code.persistence.entity.User;
import ua.org.code.persistence.repository.UserRepository;
import ua.org.code.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User user) {
        isLoginAvailable(user.getLogin());
        isEmailAvailable(user.getEmail());
        isBirthdayAvailable(user.getBirthday());
        if (user.getPassword().isEmpty()) {
            throw new ValidationException("Enter the password", "password");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User foundedUser = findById(user.getId());
        if (!user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (!foundedUser.getEmail().equals(user.getEmail())) {
            isEmailAvailable(user.getEmail());
        }
        isBirthdayAvailable(user.getBirthday());
        foundedUser.setPassword(user.getPassword());
        foundedUser.setEmail(user.getEmail());
        foundedUser.setFirstName(user.getFirstName());
        foundedUser.setLastName(user.getLastName());
        foundedUser.setBirthday(user.getBirthday());
        foundedUser.setRole(user.getRole());
        userRepository.save(foundedUser);
        return foundedUser;
    }

    @Override
    public void remove(User user) {
        User foundedUser = findById(user.getId());
        if (foundedUser.getRole().getName().equals("SuperAdmin")) {
            throw new UserRemoveException("Cannot remove user with role SuperAdmin!");
        }
        userRepository.delete(foundedUser);
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No user with id " + id);
        } else {
            return user.get();
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByLogin(String login) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No user with login " + login);
        } else {
            return user.get();
        }
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No user with email " + email);
        } else {
            return user.get();
        }
    }

    private void isLoginAvailable(String login) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new ValidationException("Login " + login + " already present", "login");
        }
    }

    private void isEmailAvailable(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ValidationException("Email " + email + " already present", "email");
        }
    }

    private void isBirthdayAvailable(LocalDate birthday) {
        if (LocalDate.now().getYear() - birthday.getYear() < 5) {
            throw new ValidationException("You should be at least 5 years old", "birthday");
        }
    }
}