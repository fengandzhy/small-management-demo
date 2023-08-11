package org.frank.springboot.small.management.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Component("uploadFileUtil")
public class UploadFileUtil {

    public String receiveFile(MultipartFile file, String fileprefix){

        JSONObject result = new JSONObject();

        if (file.isEmpty()){
            result.put("code",0);
            result.put("msg","文件为空");
            return result.toJSONString();
        }

        //获取文件的后缀名，不能是exe/dll/com/bat
        String fileName = file.getOriginalFilename(); // 获取文件名
        String suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();// 获取文件的后缀名,并转化为小写
        if(suffixName.equals(".exe")||suffixName.equals(".dll")
                ||suffixName.equals(".com")||suffixName.equals(".bat")){
            result.put("code",0);
            result.put("msg","文件格式错误");
            return result.toJSONString();
        }

        //获取当前的年月（作为文件夹名）
        SimpleDateFormat formater_YM=new SimpleDateFormat("yyyyMM");
        String strFolderPath=formater_YM.format(new Date());

        //判断文件夹是否存在，不存在则创建
        if(isOSLinux()){strFolderPath="/home/uploadfile/"+strFolderPath+"/";}//linux环境
        else{strFolderPath="D:\\uploadfile\\"+strFolderPath+"\\";}//Windows环境
        File dir=new File(strFolderPath);
        if(!dir.exists()){dir.mkdirs();}

        //获取当前的时间（到毫秒，作为保存文件的名称）
        SimpleDateFormat formater_time=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String strSaveFileName=formater_time.format(new Date())+suffixName;

        //保存文件
        try{
            String strFileFullPath=strFolderPath+fileprefix+strSaveFileName;
            File dest=new File(strFileFullPath);
            file.transferTo(dest);
            result.put("code",1);
            result.put("msg",strFileFullPath);
            return result.toJSONString();
        }
        catch (Exception e){e.printStackTrace();}

        result.put("code",0);
        result.put("msg","上传失败");
        return result.toJSONString();
    }

    private boolean isOSLinux(){
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().indexOf("linux") > -1){return true;}
        else {return false;}
    }
}
