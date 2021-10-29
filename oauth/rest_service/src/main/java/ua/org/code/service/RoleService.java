package ua.org.code.service;

import ua.org.code.persistence.entity.Role;

import java.util.List;

public interface RoleService {
    Role findRoleByName(String name);
    List<Role> findAllRoles();
}
