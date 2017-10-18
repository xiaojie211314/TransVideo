package com.lijie.demo.controller;/**
 * Created by Administrator on 2017/6/19 0019.
 */
import com.lijie.demo.bean.Const;
import com.lijie.demo.bean.VideoJob;
import com.lijie.demo.httpclient.HttpclientUtil;
import com.lijie.demo.service.TransCodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2017/6/19 0019
 * Time: 下午 8:45
 */
@Controller
@RequestMapping("video")
public class TransCodingController {
    @Autowired
    private HttpclientUtil httpclientUtil;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;

    @Autowired
    private TransCodingService transCodingService;




    @RequestMapping("upload")
    @ResponseBody
    public void upload(@RequestParam("file")MultipartFile oldfile) {
        transCodingService.upload(oldfile);
    }

    @RequestMapping("success")
    @ResponseBody
    public String success(){
        return "upload success";
    }
}
