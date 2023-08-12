package org.frank.springboot.small.management.controllers;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.frank.springboot.small.management.domains.Company;
import org.frank.springboot.small.management.domains.EchartsData;
import org.frank.springboot.small.management.services.company.CompanyService;
import org.frank.springboot.small.management.utils.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/company")
public class CompanyController {

    @Resource(name="companyService")
    private CompanyService companyService;

    @Autowired
    private UploadFileUtil uploadFileUtil;

    @RequestMapping(value = "/save",method =  RequestMethod.POST)
    @ResponseBody //这里必须要加@ResponseBody 如果不加就会有默认ModelandView 而且返回的路径默认是/company/save
    public void save(Company company){
        log.info("In the method now!!!");
        companyService.Save(company);
    }

    @GetMapping("/delete")
    @ResponseBody
    public void delete(@RequestParam String uuid){
        companyService.delete(uuid);
    }


    @PostMapping("/findAllSimplePage")
    @ResponseBody
    public Page<Company> findAllSimplePage(@RequestParam(name="page",required=false,defaultValue = "1") int page,
                                           @RequestParam(name="size",required=false,defaultValue = "2") int size){
        /**
         * 多条件排序及分页查询
         */
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"comname"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"comaddress"));

        return companyService.findAllSimplePage(PageRequest.of(page,size,Sort.by(orders)));
    }

    @PostMapping("/findAllSimplePageMap")
    @ResponseBody
    public String findAllSimplePageMap(@RequestBody(required = false) Map<String,Object> reqMap){
        /**
         * 简单分页查询
         */
        int page=this.getPage(reqMap);
        int size=this.getSize(reqMap);
        List<Sort.Order> orders = getSortRule(reqMap);

        //以下是代码的核心点
        Page<Company> pageInfo=companyService.findAllSimplePage(PageRequest.of(page,size,Sort.by(orders)));
        return getResult(pageInfo);
    }



    @GetMapping("/list-company")
    public String page(){
        return "/company/list-company";
    }


    @GetMapping("/add-companyPage")
    public String addCompanyHtml(){
        return "/company/add-company";
    }

    @GetMapping("/chart-html")
    public String chartHtml(){
        return "/company/company-chart";
    }



    @RequestMapping("validateEmail")
    @ResponseBody
    public String validateEmail(@RequestParam String contactorEmail){
        /**
         * 验证邮箱是否唯一，唯一:{"valid":true},不唯一：{"valid"：false}
         */
        boolean blStatus=companyService.validateEmail(contactorEmail);
        JSONObject result = new JSONObject();
        result.put("valid", blStatus);
        return result.toJSONString();
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam MultipartFile file){
        /**
         * 接收上传文件，反馈回服务器端的文件保存路径（全路径）
         */
        return uploadFileUtil.receiveFile(file,"myfile");
    }

    @PostMapping("/queryDynamic")
    @ResponseBody
    public String queryDynamic(@RequestBody(required = false) Map<String,Object> reqMap){
        int page=this.getPage(reqMap);
        int size=this.getSize(reqMap);
        List<Sort.Order> orders = getSortRule(reqMap);
        Page<Company> pageInfo=companyService.queryDynamic(reqMap,PageRequest.of(page,size,Sort.by(orders)));
        return getResult(pageInfo);
    }


    /**
     * 返回一个map
     * */
    @PostMapping("/chart")
    @ResponseBody
    public Map<String,List<EchartsData>> chart()
    {
        /**
         * 为Echarts图表展示，准备数据
         * 这里反馈回回聊员工数量、营业收入两个集合，并赋值给map集合
         */
        Map<String,List<EchartsData>> map = new HashMap<>();

        List<EchartsData> listEmployeenumber=new ArrayList<EchartsData>();//数据集合，员工数量
        List<EchartsData> listTotaloutput=new ArrayList<EchartsData>();//数据集合，营业收入
        List<Company> list=companyService.findAll();

        // 组装数据
        for (Company company:list)
        {
            listEmployeenumber.add(new EchartsData(company.getCompanyName(),company.getStaffAmount()));
            listTotaloutput.add(new EchartsData(company.getCompanyName(),company.getTotalOutput()));
        }

        //赋值给Map集合 KEY,VALUE
        map.put("listperson",listEmployeenumber);
        map.put("listoutput",listTotaloutput);

        return map;
    }


    private List<Sort.Order> getSortRule(Map<String,Object> reqMap){
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"companyName"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"companyAddress"));
        return orders;
    }

    private int getPage(Map<String,Object> reqMap){
        int page=0;
        if(reqMap!=null){
            if(reqMap.get("page").toString()!=null){
                page= Integer.parseInt(reqMap.get("page").toString());
            }
        }
        return page;
    }

    private int getSize(Map<String,Object> reqMap){
        int size=3;
        if(reqMap!=null){
            if(reqMap.get("size").toString()!=null){
                size= Integer.parseInt(reqMap.get("size").toString());
            }
        }
        return size;
    }

    private String getResult(Page<Company> pageInfo){
        List<Company> companies =pageInfo.getContent();
        JSONObject result = new JSONObject();//maven中配置alibaba的fastjson依赖
        //"rows"和"total"这两个属性是为前端列表插件"bootstrap-table"服务的
        result.put("rows", companies);
        result.put("total",pageInfo.getTotalElements());
        return result.toJSONString();
    }
}
