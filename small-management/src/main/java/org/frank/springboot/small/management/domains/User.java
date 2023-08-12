package org.frank.springboot.small.management.domains;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "t_user")
@Data
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid")
    @Column(length = 32)
    private String uuid;

    @Column(length = 100)
    private String username;

    @Column(length = 100)
    private String password;

    @Column(length = 100)
    private String userEmail;

    @Column(length = 100)
    private String userMobile;

    @Column(length = 30)
    private String sysRoleName;

    @Column(length = 32)
    private String sysRoleId;

    //referencedColumnName对应的是关联表对应的列
    @OneToOne
    @JoinColumn(name="role_id",referencedColumnName="uuid",insertable = false, updatable = false)
    private Role role;
}
