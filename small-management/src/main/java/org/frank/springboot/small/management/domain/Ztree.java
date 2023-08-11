package org.frank.springboot.small.management.domain;

import lombok.Data;

@Data
public class Ztree {
    public int id;
    public int pId;
    public String name;
    public boolean open;
    public boolean checked;
}
