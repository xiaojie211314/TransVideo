package com.lijie.demo.service;

import com.lijie.demo.bean.VideoJob;
import com.lijie.demo.dao.TransCodingMapper;
import com.lijie.demo.httpclient.HttpclientUtil;
import com.lijie.demo.task.TransCodeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 服务第一次启动初始化
 */
@Service
public class InitJobService {

    @Autowired
    private HttpclientUtil httpclientUtil;

    @Autowired
    private TransCodingMapper transCodingMapper;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;


    private String uploadRootDir;//根目录
    private String inDir;//上传目录
    private String outDir;//转码输出目录


    @PostConstruct
    public void init(){

        //创建目录
        creatUploadDir();

        System.out.print(">>>>>>>>>>查询数据库任务,写到内存....");

        //执行数据库写入内存
        List<VideoJob> videoJobList = transCodingMapper.findJob();

        if(null != videoJobList && !videoJobList.isEmpty())
            for (VideoJob videoJob: videoJobList){
                taskExecutor.execute(new TransCodeTask(transCodingMapper,videoJob,httpclientUtil,outDir,uploadRootDir));
            }
    }

    private void creatUploadDir(){
        File uploadFile = new File(uploadRootDir+inDir);
        if(!uploadFile.exists()){
            uploadFile.mkdirs();
        }

        File outFile = new File(uploadRootDir+outDir);
        if(!outFile.exists()){
            outFile.mkdirs();
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
