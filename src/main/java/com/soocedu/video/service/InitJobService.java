package com.soocedu.video.service;

import com.soocedu.httpclient.HttpclientUtil;
import com.soocedu.task.TransCodeTask;
import com.soocedu.video.bean.VideoJob;
import com.soocedu.video.dao.TransCodingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 服务第一次启动初始化
 */
@Service
public class InitJobService {
    private Logger log = LoggerFactory.getLogger(InitJobService.class);

    @Autowired
    private HttpclientUtil httpclientUtil;

    @Autowired
    private TransCodingMapper transCodingMapper;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;


    private String uploadRootDir;//根目录
    private String imgInDir;//图片上传目录
    private String videoInDir;//视频上传目录
    private String outDir;//转码输出目录

    @PostConstruct
    public void init(){

        //创建目录
        creatUploadDir();

        log.info(">>>>>>>>>>查询数据库任务,写到内存....");

        //执行数据库写入内存
        List<VideoJob> videoJobList = transCodingMapper.findJobTask();

        if(null != videoJobList && !videoJobList.isEmpty())
            for (VideoJob videoJob: videoJobList){

                taskExecutor.execute(new TransCodeTask(transCodingMapper,videoJob,httpclientUtil));
            }
    }

    private void creatUploadDir(){
        //图片上传目录
        File file = new File(uploadRootDir+ imgInDir);
        if(!file.exists()){
            file.mkdirs();
        }

        //视频上传目录
        file = new File(uploadRootDir+videoInDir);
        if(!file.exists()){
            file.mkdirs();
        }


        //转码目录
        file = new File(uploadRootDir+outDir);
        if(!file.exists()){
            file.mkdirs();
        }
    }



    @Value("${img.in.srcpath}")
    public void setImgInDir(String imgInDir) {
        this.imgInDir = imgInDir;
    }

    @Value("${video.in.srcpath}")
    public void setVideoInDir(String videoInDir) {
        this.videoInDir = videoInDir;
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
