package org.frank.springboot.small.management.services.company.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.zhouhy.springboot.project1.domain.Company;
import org.zhouhy.springboot.project1.repository.CompanyRepository;
import org.zhouhy.springboot.project1.service.CompanyService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public void Save(Company company) {
        companyRepository.save(company);
    }

    @Override
    public void delete(String uuid) {
        companyRepository.deleteById(uuid);
    }

    @Override
    @Transactional
    public void update(Company company) {
        companyRepository.save(company);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> findByCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName);
    }

    @Override
    public Page<Company> findAllSimplePage(Pageable pageable) {

        return companyRepository.findAll(pageable);
    }

    /**
     * 具有分页的复杂条件查询
     * */
    @Override
    public Page<Company> queryDynamic(Map<String, Object> reqMap, Pageable pageable) {
        Specification querySpecifi=new Specification<Company>(){
            @Override
            public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb){
                List<Predicate> predicates = new ArrayList<>();
                //条件设置部分，查询‘CriteriaBuilder’源码，查询的分类
                if(!reqMap.get("companyName").toString().equals(""))//公司名称，like 模糊查询
                {
                    predicates.add(cb.like(root.get("companyName"),"%"+reqMap.get("companyName").toString()+"%"));
                }
                if(!reqMap.get("companyStatus").toString().equals("全部"))//运营状态，精确查询
                {
                    predicates.add(cb.equal(root.get("companyStatus"),reqMap.get("companyStatus").toString()));
                }
                if(!reqMap.get("staffAmount").toString().equals(""))//大于员工人数
                {
                    predicates.add(cb.gt(root.get("staffAmount"),Integer.parseInt(reqMap.get("staffAmount").toString())));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return this.companyRepository.findAll(querySpecifi,pageable);//这里利用的是JpaSpecificationExecuto接口的分页查询方法
    }


    @Override
    public boolean validateEmail(String email) {
        int intNumber=companyRepository.validateEmail(email);
        if(intNumber==0){return true;}
        else{return false;}
    }
}
