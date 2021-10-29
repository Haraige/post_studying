package ua.org.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.org.code.service.RoleService;
import ua.org.code.view.dto.role.RoleDto;
import ua.org.code.view.mappers.RoleMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/roles")
public class RoleManageController {
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleManageController(RoleService roleService, RoleMapper roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    @GetMapping
    public List<RoleDto> getAllRoles() {
        return roleService.findAllRoles().stream().map(roleMapper::roleToRoleDto).collect(
                Collectors.toList());
    }
}
