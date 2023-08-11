package org.frank.springboot.small.management.domains;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="t_company")
@Data
public class Company {

    @Id
    @GeneratedValue(generator="myuuid")
    @GenericGenerator(name="myuuid",strategy = "uuid")//hibernate 主键生成策略
    @Column(length = 32)
    private String uuid;

    @Column(length = 60)
    private String companyName;

    @Column(length = 120)
    private String companyAddress;

    @Column(length = 30)
    private String companyUrl;

    @Column(length = 30)
    private String companyPhone;

    //定长型
    @Column(columnDefinition = "char(7)")
    private String establishDate;

    //整型
    @Column
    private int staffAmount;

    //float型
    @Column(columnDefinition = "float(20,4) default '0.00'")
    private float totalOutput;

    //文本型
    @Column(columnDefinition = "text")
    private String companyDesc;

    @Column(columnDefinition = "char(4)")
    private String companyStatus;

    @Column(length = 20)
    private String contactor;

    @Column(columnDefinition = "char(11)")
    private String contactorMobile;

    @Column(length = 30)
    private String contactorEmail;

    @Column(length = 500)
    private String upload;
}
