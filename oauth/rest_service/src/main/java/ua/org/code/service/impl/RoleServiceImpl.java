package ua.org.code.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.code.persistence.entity.Role;
import ua.org.code.persistence.repository.RoleRepository;
import ua.org.code.service.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {

        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name).orElseThrow();
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
