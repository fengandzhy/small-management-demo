package org.frank.springboot.small.management.services.role;

import org.zhouhy.springboot.project1.domain.Role;

import java.util.List;

public interface RoleService {
    //根据uuid查找角色
    Role findByUuid(String uuid);

    //查询全部的权限
    List<Role> findALL();

    //保存角色
    void save(Role sysRole);

    //根据uuid，删除角色
    void deleteByUuid(String uuid);

    //根据角色uuid，删除角色权限关联表中角色对应的记录
    void deleteMaptabByUuid(String uuid);
}
