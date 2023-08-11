package org.frank.springboot.small.management.services.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zhouhy.springboot.project1.domain.User;

import java.util.Map;

public interface UserService {
    //新添加账号
    void save(User sysUser);

    //根据账号/邮箱/手机号三者之一查询账号
    User findByUsernameOrUseremailOrUsermobile(String username,String email,String mobile);

    //带查询条件的分页查询
    Page<User> queryDynamic(Map<String,Object> reqMap, Pageable pageable);

    //账号名称唯一性验证(如果已经存在，返回false，否则返回true)
    boolean validateUsername(String username);

    //邮箱号唯一性验证(如果已经存在，返回false，否则返回true)
    boolean validateEmail(String email);

    //手机号唯一性验证(如果已经存在，返回false，否则返回true)
    boolean validateMobile(String mobile);
}
