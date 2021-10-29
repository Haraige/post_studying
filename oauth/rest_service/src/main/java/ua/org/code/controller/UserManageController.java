package ua.org.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ua.org.code.persistence.entity.User;
import ua.org.code.service.UserService;
import ua.org.code.view.dto.user.CreateEditUserDto;
import ua.org.code.view.dto.user.UserDto;
import ua.org.code.view.mappers.UserMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/users")
@CrossOrigin(origins = "http://127.0.0.1:9100")
public class UserManageController {
    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserManageController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserDto> findAllUsers() {
        List<User> users = userService.findAll();
        return users.stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }

    @GetMapping("{userId}")
    public UserDto findUserById(@PathVariable("userId") Long id) {
        User user = userService.findById(id);
        return userMapper.userToUserDto(user);
    }

    @GetMapping("login/{userLogin}")
    public UserDto findUserByLogin(@PathVariable("userLogin") String login) {
        User user = userService.findByLogin(login);
        return userMapper.userToUserDto(user);
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Valid CreateEditUserDto createEditUserDto) {
        return userMapper.userToUserDto(userService.create(userMapper.createEditUserDtoToUser(createEditUserDto)));
    }

    @PutMapping("{userId}")
    public UserDto updateUser(@PathVariable("userId")Long id,
            @RequestBody @Valid CreateEditUserDto createEditUserDto) {
        User user = userMapper.createEditUserDtoToUser(
                createEditUserDto);
        user.setId(id);
        return userMapper.userToUserDto(userService.update(user));
    }

    @PutMapping("login/{userLogin}")
    public UserDto updateUser(@PathVariable("userLogin")String login,
            @RequestBody @Valid CreateEditUserDto createEditUserDto) {
        User user = userMapper.createEditUserDtoToUser(
                createEditUserDto);
        user.setId(userService.findByLogin(login).getId());
        return userMapper.userToUserDto(userService.update(user));
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") Long id) {
        userService.remove(userService.findById(id));
    }

    @DeleteMapping("login/{userLogin}")
    public void deleteUserByLogin(@PathVariable("userLogin") String login) {
        userService.remove(userService.findByLogin(login));
    }
}
