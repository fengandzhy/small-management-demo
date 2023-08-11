package org.frank.springboot.small.management.domains;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "t_auth")
@Data
public class Auth {

    @Id
    @GeneratedValue(generator = "myuuid")
    @GenericGenerator(name="myuuid",strategy = "uuid")
    @Column(length = 32)
    private String uuid;

    @Column(length = 200)
    private String fullName;//权限名称（唯一），如‘系统管理’,‘系统管理_部门成员_增加’

    @Column(length = 20)
    private String treeName;//树形节点名称，如‘增加’

    @Column
    private int id;//本身id

    @Column
    private int pid;//父id
}
