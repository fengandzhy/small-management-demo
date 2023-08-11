package org.frank.springboot.small.management.services.company;

import org.frank.springboot.small.management.domains.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface CompanyService {

    void Save(Company company);

    @Transactional
    void delete(String uuid);

    @Transactional
    void update(Company company);

    List<Company> findAll();

    List<Company> findByCompanyName(String companyName);

    //简单分页查询
    Page<Company> findAllSimplePage(Pageable pageable);

    //多条件动态查询
    Page<Company> queryDynamic(Map<String,Object> reqMap, Pageable pageable);

    //验证邮箱唯一性
    boolean validateEmail(String email);

}
