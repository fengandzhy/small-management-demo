package org.frank.springboot.small.management.repositories.auth;

import org.frank.springboot.small.management.domain.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.List;


public interface AuthRepository extends JpaRepository<Auth, String>{

    //根据节点id查找权限信息
    Auth findById(int id);

    //根据节点的全称（不是树形节点名称）查询节点名称，主要用于验证对应的节点信息是否存在
    Auth findByFullName(String fullname);

    //根据父节点的id，获取其子节点的最大id
    @Query(value = "select max(id) from t_auth where pid=?1",nativeQuery = true)
    int findMaxId(int pid);

    //根据父节点的id，获取其全部的子节点
    @Query(value = "select * from t_auth where pid=?1",nativeQuery = true)
    List<Auth> findAllChildByPid(int pid);

    //根据节点的id删除节点及子节点
    @Modifying
    @Query(value = "delete from t_auth where id=?1",nativeQuery = true)
    String deleteByChild(int id);

    //根据name删除指定的节点及子节点
    @Modifying
    @Query(value = "delete from t_auth where full_name LIKE ?1",nativeQuery = true)
    void deleteByName(String name);

    //(用于删除关联表中的记录)根据name查找指定的节点及子节点
    @Query(value = "select * from t_auth where full_name LIKE ?1",nativeQuery = true)
    List<Auth> findAllByFullName(String name);

    //根据权限uuid，删除角色权限关联表中角色对应的记录
    @Modifying
    @Query(value = "delete from t_role_auths where auths_uuid=?1",nativeQuery = true)
    void deleteMaptabByUuid(String uuid);

}
