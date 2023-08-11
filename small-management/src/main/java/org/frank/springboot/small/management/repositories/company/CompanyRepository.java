package org.frank.springboot.small.management.repositories.company;

import org.frank.springboot.small.management.domains.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,String>, JpaSpecificationExecutor {

    //原生sql语句查询
    @Query(value="select * from t_company where company_name=?1",nativeQuery = true)
    List<Company> findByCompanyName(String companyName);

    @Query(value="select * from t_company where company_name like '%?1%'",nativeQuery = true)
    List<Company> findByCompanyNameLike(String companyName);

    //原生sql语句操作（涉及到数据变动的，如删除和更新，必须加注解@Modifying）
    @Modifying
    @Query(value = "update t_company set company_address =?1 where company_name=?2",nativeQuery = true)
    void updateByName(String companyAddress,String companyName);

    //邮箱号唯一性验证(如果已经存在，返回0，否则返回1)
    @Query(value = "select count(*) from t_company where contactor_email=?1",nativeQuery = true)
    int validateEmail(String email);


}
