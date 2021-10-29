package ua.org.code.view.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import ua.org.code.persistence.entity.User;
import ua.org.code.service.RoleService;
import ua.org.code.service.UserService;
import ua.org.code.view.dto.user.CreateEditUserDto;
import ua.org.code.view.dto.user.UserDto;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected RoleService roleService;

    @Autowired
    protected UserService userService;

    @Mappings({
            @Mapping(target = "role", expression = "java(user.getRole().getName())"),
            @Mapping(target = "birthday", dateFormat = "yyyy-MM-dd")
    })
    public abstract UserDto userToUserDto(User user);
    @Mappings({
            @Mapping(target = "role", expression = "java(roleService.findRoleByName(userDto.getRole()))")
    })
    public abstract User userDtoToUser(UserDto userDto);
    @Mappings({
            @Mapping(target = "role", expression = "java(roleService.findRoleByName(userDto.getRole()))")
    })
    public abstract User createEditUserDtoToUser(CreateEditUserDto userDto);
}
