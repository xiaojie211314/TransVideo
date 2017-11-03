package com.lijie.demo.controller;/**
 * Created by Administrator on 2017/6/19 0019.
 */

import com.lijie.demo.bean.*;
import com.lijie.demo.httpclient.HttpclientUtil;
import com.lijie.demo.service.TransCodingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2017/6/19 0019
 * Time: 下午 8:45
 */
@CrossOrigin
@Controller
@RequestMapping("video")
public class TransCodingController {
    private Logger log = LoggerFactory.getLogger(TransCodingController.class);
    @Autowired
    private HttpclientUtil httpclientUtil;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;

    @Autowired
    private TransCodingService transCodingService;




    @RequestMapping("upload")
    @ResponseBody
    public ResResult upload(MultipartFile file, String callUrl, @RequestParam("sign") String signkey, String width, String height ) {
        VideoUpload videoUpload = new VideoUpload();
        videoUpload.setFile(file);
        videoUpload.setCallUrl(callUrl);
        videoUpload.setSign(signkey);
        videoUpload.setVideokey(Utils.getUUID());
        if(!StringUtils.isEmpty(width)) {
            videoUpload.setWidth(width);
        }
        if(!StringUtils.isEmpty(height)) {
            videoUpload.setHeight(height);
        }
        log.info(">>>>upload file : "+videoUpload.getFile().getOriginalFilename());
        ResResult resResult = new ResResult();

        String sign =transCodingService.validateSign(videoUpload);
        //验证签名
        if(null == sign){
            resResult.setStatus(Const.status2);//验证签名失败
            resResult.setStatusCode("验证签名失败!");

            return resResult;
        }




        //验证通过 上传

        Boolean  flag = transCodingService.upload(videoUpload);


        //上传失败
        if(!flag){
            resResult.setStatus(Const.status2);//上传失败
            resResult.setStatusCode("上传失败!");
            return resResult;
        }

        //上传成功
        resResult.setSign(sign);
        resResult.setVideokey(videoUpload.getVideokey());

        return resResult;

    }

    @RequestMapping("find")
    @ResponseBody
    public ModelAndView success(){

        List<VideoJob>  videoJobList = transCodingService.findListVideos();
        ModelAndView modelAndView = new ModelAndView("success");

        //将数据放到request中
        modelAndView.addObject("dataList", videoJobList);

        return modelAndView;
    }
}
