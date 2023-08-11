package org.frank.springboot.small.management.services.user.impl;

import org.frank.springboot.small.management.domains.Role;
import org.frank.springboot.small.management.domains.User;
import org.frank.springboot.small.management.repositories.role.RoleRepository;
import org.frank.springboot.small.management.repositories.user.UserRepository;
import org.frank.springboot.small.management.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void save(User sysUser) {
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String hashPassword=passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(hashPassword);

        //根据账号对应的角色id（sysroleid），得到角色信息
        Role sysRole=roleRepository.findByUuid(sysUser.getSysroleid());
        sysUser.setSysrolename(sysRole.getRoleName());
        sysUser.setRole(sysRole);

        userRepository.save(sysUser);
    }

    @Override
    public User findByUsernameOrUseremailOrUsermobile(String username, String email, String mobile) {
        return userRepository.findByUsernameOrUseremailOrUsermobile(username,email,mobile);
    }

    @Override
    public Page<User> queryDynamic(Map<String, Object> reqMap, Pageable pageable) {
        Specification querySpecifi=new Specification<User>(){
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
                List<Predicate> predicates = new ArrayList<>();
                if(!(reqMap.get("username")==null||reqMap.get("username").toString().equals(""))){//账号名称，like 模糊查询
                    predicates.add(criteriaBuilder.like(root.get("username"),"%"+reqMap.get("username").toString()+"%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return this.userRepository.findAll(querySpecifi,pageable);
    }

    @Override
    public boolean validateUsername(String username) {
        int intCount=userRepository.validateUsername(username);
        if(intCount==0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean validateEmail(String email) {
        int intCount=userRepository.validateEmail(email);
        if(intCount==0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean validateMobile(String mobile) {
        int intCount=userRepository.validateMobile(mobile);
        if(intCount==0){
            return true;
        }else{
            return false;
        }
    }
}
