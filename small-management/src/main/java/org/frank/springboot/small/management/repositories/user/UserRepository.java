package org.frank.springboot.small.management.repositories.user;

import org.frank.springboot.small.management.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor {

    //使用SpringDataJPA方法定义查询,根据用户名/邮件/手机号，查询用户信息
    User findByUsernameOrUseremailOrUsermobile(String username,String email,String mobile);

    //账号唯一性验证(如果已经存在，返回0，否则返回1)
    @Query(value = "select count(*) from t_user where username=?1",nativeQuery = true)
    int validateUsername(String username);

    //邮箱号唯一性验证(如果已经存在，返回0，否则返回1)
    @Query(value = "select count(*) from t_user where useremail=?1",nativeQuery = true)
    int validateEmail(String email);

    //手机号唯一性验证(如果已经存在，返回0，否则返回1)
    @Query(value = "select count(*) from t_user where usermobile=?1",nativeQuery = true)
    int validateMobile(String mobile);
}
