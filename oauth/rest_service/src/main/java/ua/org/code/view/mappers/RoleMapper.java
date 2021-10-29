package ua.org.code.view.mappers;

import org.mapstruct.Mapper;
import ua.org.code.persistence.entity.Role;
import ua.org.code.view.dto.role.RoleDto;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto roleToRoleDto(Role role);
}

