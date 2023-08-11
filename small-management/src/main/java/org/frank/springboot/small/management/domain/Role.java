package org.frank.springboot.small.management.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="t_role")
@Data
public class Role {

    @Id
    @GeneratedValue(generator = "myuuid")
    @GenericGenerator(name="myuuid",strategy = "uuid")
    @Column(length = 32)
    private String uuid;

    @Column(length=30)
    private  String roleName;

    @Column(length=200)
    private  String roledesc;

    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    private List<Auth> Auths;


}
