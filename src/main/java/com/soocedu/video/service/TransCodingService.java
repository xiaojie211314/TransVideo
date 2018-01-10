package com.soocedu.video.service;

import com.alibaba.fastjson.JSON;
import com.soocedu.fastdfs.FastDFSClient;
import com.soocedu.httpclient.HttpclientUtil;
import com.soocedu.task.TransCodeTask;
import com.soocedu.util.Const;
import com.soocedu.util.Token;
import com.soocedu.util.Uuids;
import com.soocedu.video.bean.*;
import com.soocedu.video.dao.TransCodingMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Service
public class TransCodingService {
    private Logger log = LoggerFactory.getLogger(TransCodingService.class);
    @Autowired
    private HttpclientUtil httpclientUtil;

    @Autowired
    private TransCodingMapper transCodingMapper;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;

    @Autowired
    private VideoDir videoDir;//视频目录

    @Autowired
    private FastDFSClient fastDFSClient;




    //上传
    public VideoResult upload(MultipartFile file, @RequestParam("token") String token) {
        VideoJob videoJob = new VideoJob();
        VideoResult uploadResult = new VideoResult();
        try {


            Token tokenUtil = new Token(token, Const.SECRET_KEY);

            //验证签名
//            if(!tokenUtil.isValidCallback(Const.ACCESS_KEY)){
//                uploadResult.setCode(2);
//                uploadResult.setError("验证签名失败");
//                return uploadResult;
//            }


            //签名通过，上传文件， 解析 token数据

            UploadFile uploadFile = JSON.parseObject(tokenUtil.getPutPolicy(), UploadFile.class);

            uploadFile.setFile(file);


            //获取上传后缀名
            String infileName = Uuids.getUUID() + uploadFile.getFile().getOriginalFilename().substring(uploadFile.getFile().getOriginalFilename().lastIndexOf("."));


            //图片上传 名称
            if (StringUtils.isEmpty(uploadFile.getPersistentOps())) {

                infileName = videoDir.getImgInDir() + infileName;
            } else {
                //视频上传相对路径
                infileName = videoDir.getVideoInDir() + infileName;
            }


            File inFile = new File(videoDir.getUploadRootDir() + infileName);


            log.debug(">>>>>infilename>>" + inFile.getAbsolutePath());
//            //复制文件
            uploadFile.getFile().transferTo(inFile);

            //设置上传文件相对路径
            uploadResult.setKey(infileName);

            //上传文档图片成功
            if (StringUtils.isEmpty(uploadFile.getPersistentOps())) {
                uploadResult.setCode(0);
                uploadResult.setError("文件上传成功");
                log.debug(">>>>上传文档成功，数据返回： 【"+uploadResult.toString()+"】");

                return uploadResult;
            }


            //设置视频的唯一 key
            uploadResult.setPersistentId(Uuids.getUUID());

            //上传视频
            //视频参数
            videoJob.setVoptions(uploadFile.getVoptions());
            videoJob.setPersistentNotifyUrl(uploadFile.getPersistentNotifyUrl());
            videoJob.setFilename(uploadFile.getOutkey());
            videoJob.setSrcpath(inFile.getAbsolutePath());
            videoJob.setSrcurl(uploadResult.getKey());
            videoJob.setStatus(1);//等待转码
            videoJob.setCounts(1);
            videoJob.setMsg("等待转码");
            videoJob.setPersistentid(uploadResult.getPersistentId());
            //设置转码地址
            videoJob.setDespath(videoDir.getUploadRootDir() + videoDir.getOutDir() + uploadFile.getOutkey());
            videoJob.setDesurl(videoDir.getOutDir() + uploadFile.getOutkey());

            //设置转码命令
            videoJob = setCmd(videoJob);


//            //插入数据库记录
            transCodingMapper.insertJob(videoJob);
//
//
            log.debug(">>>插入数据库 ，插入任务成功【1】 >>>>video Persistentid: " + videoJob.getPersistentid());
//
//            //转码
            taskExecutor.execute(new TransCodeTask(transCodingMapper, videoJob, httpclientUtil,fastDFSClient));
//
//
//
////            transVideo();
//
        } catch (Exception e) {
            log.error(">>>上传异常 :"+e.getMessage());

            uploadResult.setCode(2);
            uploadResult.setError("上传失败");
            log.debug(">>>>上传失败，数据返回： 【"+uploadResult.toString()+"】");

            return uploadResult;
        }


        uploadResult.setCode(0);
        uploadResult.setError("上传文件成功");
        log.debug(">>>>上传视频文件成功，数据返回： 【"+uploadResult.toString()+"】");
        return uploadResult;

    }


    public List<VideoLook> findVideos() {
        return transCodingMapper.findVideos();
    }
    public List<VideoLook> findVideosByQueue() {
        return transCodingMapper.findVideosByQueue();
    }


    /**
     * 根据 persistentId 查询视频
     *
     * @param persistentId
     * @return
     */

    public VideoCall findVideoByPersistentId(String persistentId) {
        VideoJob videoJob = transCodingMapper.findVideoByPersistentId(persistentId);

        VideoCall videoCall = new VideoCall();
        if(null!=videoJob) {
            videoCall.setCode(videoJob.getStatus());
            videoCall.setError(videoJob.getError());
            videoCall.setId(videoJob.getPersistentid());
            videoCall.setDesc(videoJob.getMsg());
            List<VideoResult> listItems = new ArrayList<>();
            listItems.add(new VideoResult(videoJob.getDesurl(), videoJob.getFdsdomain(), videoJob.getFdsurl()));
            videoCall.setItems(listItems);
        }
        return videoCall;
    }


    private VideoJob setCmd(VideoJob videoJob) {
        //有转码命令 按照转码命令执行，没有转码命令生成转码命令
        StringBuilder sbCode = new StringBuilder("ffmpeg ");
        sbCode.append(" -i ").append(videoJob.getSrcpath()); //输入文件

        //分辨率
        if (!StringUtils.isEmpty(videoJob.getVoptions().getS())) {
            sbCode.append(" -s ").append(videoJob.getVoptions().getS());
        }

        //视频格式 x264
        if (!StringUtils.isEmpty(videoJob.getVoptions().getVcodec())) {
            sbCode.append(" -vcodec ").append(videoJob.getVoptions().getVcodec());
        }

        //设置音频采样率
        if (!StringUtils.isEmpty(videoJob.getVoptions().getAr())) {
            sbCode.append(" -ar ").append(videoJob.getVoptions().getAr());
        }

        //设置音频码率
        if (!StringUtils.isEmpty(videoJob.getVoptions().getAb())) {
            sbCode.append(" -ab ").append(videoJob.getVoptions().getAb());
        }

        //设置视频码率
        if (!StringUtils.isEmpty(videoJob.getVoptions().getVb())) {
            sbCode.append(" -vb ").append(videoJob.getVoptions().getVb());
        }

        //设置帧率
        if (!StringUtils.isEmpty(videoJob.getVoptions().getR())) {
            sbCode.append(" -r ").append(videoJob.getVoptions().getR());
        }

        sbCode.append(" -g ").append("10"); //关键帧控制
        sbCode.append(" -y ").append(videoJob.getDespath()); //输入文件

        videoJob.setCommand(sbCode.toString());

        return videoJob;
    }


}
