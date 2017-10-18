package com.lijie.demo.service;

import com.lijie.demo.bean.Const;
import com.lijie.demo.bean.VideoJob;
import com.lijie.demo.dao.TransCodingMapper;
import com.lijie.demo.httpclient.HttpclientUtil;
import com.lijie.demo.task.TransCodeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;


@Service
public class TransCodingService {
    @Autowired
    private HttpclientUtil httpclientUtil;

    @Autowired
    private TransCodingMapper transCodingMapper;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;


    private String uploadRootDir;
    private String inDir;
    private String outDir;



    //上传
    public void upload(MultipartFile oldfile) {

//        oldfile.getName()
        //文件上传
        String fileName = new Date().getTime()+oldfile.getOriginalFilename().substring(oldfile.getOriginalFilename().lastIndexOf("."));
        File uploadFile = new File(uploadRootDir+inDir+fileName);
        try {

            System.out.println(">>>>>filename>>"+fileName );
            //复制文件
            oldfile.transferTo(uploadFile);



            //封装数据
            VideoJob videoJob =new VideoJob(inDir+fileName,"0");


            //插入数据库记录
            transCodingMapper.insertJob(videoJob);

            System.out.println(">>>插入数据库成功 >>>>videoid: "+videoJob.getId());



            //转码
            taskExecutor.execute(new TransCodeTask(transCodingMapper,videoJob,httpclientUtil,outDir,uploadRootDir));




        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    @Value("${video.in.srcpath}")
    public void setInDir(String inDir) {
        this.inDir = inDir;
    }


    @Value("${video.out.despath}")
    public void setOutDir(String outDir) {
        this.outDir = outDir;
    }

    @Value("${video.upload.root}")
    public void setUploadRootDir(String uploadRootDir) {
        this.uploadRootDir = uploadRootDir;
    }
}
