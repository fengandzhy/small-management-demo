package org.frank.springboot.small.management.services.auth.impl;

import com.alibaba.fastjson.JSON;
import org.frank.springboot.small.management.domain.Auth;
import org.frank.springboot.small.management.domain.Role;
import org.frank.springboot.small.management.domain.Ztree;
import org.frank.springboot.small.management.repositories.auth.AuthRepository;
import org.frank.springboot.small.management.repositories.role.RoleRepository;
import org.frank.springboot.small.management.services.auth.AuthService;
import org.frank.springboot.small.management.services.auth.impl.mapper.AuthServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthServiceMapper authServiceMapper;

    @Override
    public List<Auth> findAll() {
        return authRepository.findAll();
    }

    @Override
    public List<Ztree> findALLToZtree(String roleid) {
        List<Ztree> listztree=new ArrayList<Ztree>();

        //根据角色uuid获取角色对应的权限
        Role sysRole=roleRepository.findByUuid(roleid);

        //查询全部的权限
        List<Auth> sysAuths=findAll();

        //以全部权限为基准进行循环，并和角色对应的权限进行比较，用来设定权限的open和checked属性
        for(Auth sa:sysAuths){
            //遍历角色对应的权限，比较是否在全部权限之中（在其中时，则勾选属性为true）
            boolean blChecked=false;
            if(!roleid.equals("nouuid")){
                String strRoleAuthName=sa.getFullName();
                for(Auth roleAuth:sysRole.getAuths()){
                    if(roleAuth.getFullName().equals(strRoleAuthName)){
                        blChecked=true;
                        break;
                    }
                }
            }
            Ztree z=new Ztree();
            z.id=sa.getId();
            z.pId=sa.getPid();
            z.name=sa.getTreeName();
            z.open=true;
            z.checked=blChecked;
            listztree.add(z);
        }

        return listztree;
    }

    @Override
    public String saveChildAuth(int id, String childname) {
        if(id==0){//一级权限
            Auth sysAuth_Child=authRepository.findByFullName(childname);
            return this.saveResult(sysAuth_Child,id,childname);
        }else{//非一级权限
            Auth sysAuth_Parent=authRepository.findById(id);
            String strChildName=sysAuth_Parent.getFullName()+"_"+childname;
            Auth sysAuth_Child=authRepository.findByFullName(strChildName);
            return this.saveResult(sysAuth_Child,id,childname);
        }
    }

    @Override
    public Auth findById(int id) {
        return authRepository.findById(id);
    }

    @Override
    public Auth findByFullname(String fullname) {
        return authRepository.findByFullName(fullname);
    }

    @Override
    public int findMaxId(int pid) {
        List<Auth> sysAuths=authRepository.findAllChildByPid(pid);
        if(sysAuths.size()==0){//没有子节点
            int intNewId=pid*10+1;
            return intNewId;
        }else{//有子节点
            return authRepository.findMaxId(pid)+1;
        }
    }

    @Override
    public String deleteByChild(int id) {
        Auth sysAuth=authRepository.findById(id);
        //先批量删除角色权限中间表对应的记录
        List<Auth> list=authRepository.findAllByFullName(sysAuth.getFullName()+"%");
        for (Auth sa:list){
            authRepository.deleteMaptabByUuid(sa.getUuid());
        }

        //批量删除本身节点及子节点
        authRepository.deleteByName(sysAuth.getFullName()+"%");
        return JSON.toJSONString(sysAuth);//这里有返回值，主要是为后面的基于AOP的日志捕捉服务的
    }

    @Override
    public void deleteByName(String name) {
        authRepository.deleteByName(name+"%");
    }

    @Override
    public void editRole(String uuid, String authsinfo) {
        /**
         * 处理前端传过来的字符串形式组装的多权限信息，建立角色和权限的映射关系
         */
        //得到角色信息
        Role sysRole=roleRepository.findByUuid(uuid);

        //根据勾选的权限节点id，得到对应的权限对象，并给角色中的权限集合属性赋值
        List<Auth> list=new ArrayList<Auth>();
        String[] arrAuthid=authsinfo.split("\\$");
        for(int i=0,num=arrAuthid.length;i<num;i++){
            Auth sysAuth=authRepository.findById(Integer.parseInt(arrAuthid[i]));
            list.add(sysAuth);
        }
        sysRole.setAuths(list);

        //保存或者更新角色信息
        roleRepository.save(sysRole);
    }

    private String saveResult(Auth sysAuth_Child,int id, String childname){
        if(sysAuth_Child!=null){
            return authServiceMapper.createResult("exist");
        }else{
            int maxId = this.findMaxId(id);
            Auth newAuth = authServiceMapper.createNewAuth(id,childname,maxId);
            authRepository.save(newAuth);
            return authServiceMapper.createResult("ok");
        }
    }
}
