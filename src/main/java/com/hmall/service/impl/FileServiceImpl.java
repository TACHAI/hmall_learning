package com.hmall.service.impl;

import com.google.common.collect.Lists;
import com.hmall.service.IFileService;
import com.hmall.util.FTPUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by @Author tachai on 2017/8/29.
 *
 * @Email 1206966083@qq.com
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private org.slf4j.Logger logger=LoggerFactory.getLogger(FileServiceImpl.class);
    public String upload(MultipartFile file,String path){
    String fileName=file.getOriginalFilename();
    //扩展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
logger.info("开始上传文件，上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);
        File fileDir=new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile=new File(path,uploadFileName);
        try {
            file.transferTo(targetFile);
            //文件上传成功
            //将targetFile上传到我们的FTP服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //上传完之后，删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
           logger.error("上传文件异常",e);
        }
        return targetFile.getName();
    }
}
