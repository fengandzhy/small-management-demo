package org.frank.springboot.small.management.controllers;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zhouhy.springboot.project1.domain.Role;
import org.zhouhy.springboot.project1.domain.User;
import org.zhouhy.springboot.project1.domain.Ztree;
import org.zhouhy.springboot.project1.service.AuthService;
import org.zhouhy.springboot.project1.service.RoleService;
import org.zhouhy.springboot.project1.service.UserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/security")
public class SecurityController {

    @Resource(name="userService")
    private UserService userService;

    @Resource(name="roleService")
    private RoleService roleService;

    @Resource(name="authService")
    private AuthService authService;

    @RequestMapping("/security.html")
    public String findAllRole(Model model){
        /**
         * 查询全部的角色，然后跳转到页面，利用了Thymeleaf模板中的迭代器进行页面数据的输出
         */
        List<Role> list=roleService.findALL();
        model.addAttribute("sysRoles",list);
        return "/security/security.html";
    }

    @PostMapping("/user_list")
    @ResponseBody
    public String queryDynamic(@RequestBody Map<String,Object> reqMap){
        /**
         * 系统账号分页查询
         */

        //固定不变的两个分页参数
        int page=0;
        if(reqMap.get("page").toString()!=null){page= Integer.parseInt(reqMap.get("page").toString());}
        int size=3;
        if(reqMap.get("size").toString()!=null){size= Integer.parseInt(reqMap.get("size").toString());}

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"username"));

        Page<User> pageinfo=userService.queryDynamic(reqMap, PageRequest.of(page,size,Sort.by(orders)));
        List<User> sysUsers =pageinfo.getContent();
        JSONObject result = new JSONObject();
        result.put("rows", sysUsers);
        result.put("total",pageinfo.getTotalElements());
        return result.toJSONString();
    }



    @RequestMapping("/auth_list")
    @ResponseBody
    public List<Ztree> findALLToZtree(String roleid){
        /**
         * 返回全部权限（zTree结构形式的树形节点）
         */
        return authService.findALLToZtree(roleid);
    }

    //保存角色
    @PostMapping("/role_create")
    @ResponseBody

    public String save(Role sysRole){
        roleService.save(sysRole);
        return "OK";
    }

    //删除角色
    @PostMapping("/role_delete")
    @ResponseBody
    public String deleteRole(String uuid){
        roleService.deleteByUuid(uuid);
        roleService.deleteMaptabByUuid(uuid);
        return "OK";
    }

    //保存子节点（需要判断是否有重复，如果没有重复,保存信息,id是当前选定节点的id，name是需要新增加子节点的名称）
    @PostMapping("/child-auth_save")
    @ResponseBody
    public String saveChildAuth(@RequestParam int id, String name){
        return authService.saveChildAuth(id,name);
    }

    //根据节点的id删除节点及子节点
    @PostMapping("/auth_delete-by-child")
    @ResponseBody
    public String deleteByChild(@RequestParam int id){
        authService.deleteByChild(id);
        return "OK";
    }

    //保存角色对应的权限信息,其中‘authinfo’是以$分割的节点id字符串
    @PostMapping("/role_edit")
    @ResponseBody
    public String editRole(@RequestParam String uuid,String authinfo){
        authService.editRole(uuid,authinfo);
        return "OK";
    }

    //查询全部的角色(填充添加用户中的下拉列表)
    @RequestMapping("/role_all")
    @ResponseBody
    public List<Role> findAllRoles(){
        List<Role> list=roleService.findALL();
        return list;
    }

    //用户名唯一性验证(如果已经存在，返回false，否则返回true；返回json数据，格式为{"valid",true})
    @PostMapping("/username_validate")
    @ResponseBody
    public String validateUsername(@RequestParam String username){
        boolean blStatus=userService.validateUsername(username);
        JSONObject result = new JSONObject();
        result.put("valid", blStatus);
        return result.toJSONString();
    }

    //邮箱号唯一性验证(如果已经存在，返回false，否则返回true；返回json数据，格式为{"valid",true})
    @PostMapping("/email_validate")
    @ResponseBody
    public String validateEmail(@RequestParam String useremail){
        boolean blStatus=userService.validateEmail(useremail);
        JSONObject result = new JSONObject();
        result.put("valid", blStatus);
        return result.toJSONString();
    }

    //手机号唯一性验证(如果已经存在，返回false，否则返回true；返回json数据，格式为{"valid",true})
    @PostMapping("/mobile_validate")
    @ResponseBody
    public String validateMobile(@RequestParam String usermobile){
        boolean blStatus=userService.validateMobile(usermobile);
        JSONObject result = new JSONObject();
        result.put("valid", blStatus);
        return result.toJSONString();
    }

    //返回AddSysUser.html页面
    @RequestMapping("/user_create.html")
    public String ridirectAddSysUserHtml(){
        return "security/user_create.html";
    }

    //保存系统账户
    @RequestMapping("/user_create")
    @ResponseBody
    public String saveSysUser(User sysUser){
        userService.save(sysUser);
        return "OK";
    }
}
