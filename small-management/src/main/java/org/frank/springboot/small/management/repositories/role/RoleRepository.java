package org.frank.springboot.small.management.repositories.role;

import org.frank.springboot.small.management.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface RoleRepository extends JpaRepository<Role, String> {

    //根据uuid查找角色信息
    Role findByUuid(String uuid);

    //账号唯一性验证(如果已经存在，返回0，否则返回1)
    @Query(value = "select count(*) from t_user where username=?1",nativeQuery = true)
    int validateUsername(String username);

    //根据uuid，删除角色
    @Modifying
    @Query(value = "delete from t_role where uuid=?1",nativeQuery = true)
    void deleteByUuid(String uuid);

    //根据角色uuid，删除角色权限关联表中角色对应的记录
    @Modifying
    @Query(value = "delete from t_role_auths where role_uuid=?1",nativeQuery = true)
    void deleteMaptabByUuid(String uuid);
}
