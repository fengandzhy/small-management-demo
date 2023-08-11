package org.frank.springboot.small.management.services.role.impl;

import org.frank.springboot.small.management.domain.Role;
import org.frank.springboot.small.management.repositories.role.RoleRepository;
import org.frank.springboot.small.management.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByUuid(String uuid) {
        return roleRepository.findByUuid(uuid);
    }

    @Override
    public List<Role> findALL() {
        return roleRepository.findAll();
    }

    @Override
    public void save(Role sysRole) {
        roleRepository.save(sysRole);
    }

    @Override
    @Transactional
    public void deleteByUuid(String uuid) {
        roleRepository.deleteByUuid(uuid);
    }

    @Override
    @Transactional
    public void deleteMaptabByUuid(String uuid) {
        roleRepository.deleteMaptabByUuid(uuid);
    }
}
