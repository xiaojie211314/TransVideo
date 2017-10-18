package com.lijie.demo.task;

import com.lijie.demo.bean.Const;
import com.lijie.demo.bean.VideoJob;
import com.lijie.demo.dao.TransCodingMapper;
import com.lijie.demo.httpclient.HttpclientUtil;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class TransCodeTask implements Runnable {

    private TransCodingMapper transCodingMapper;
    private HttpclientUtil httpclientUtil;
    private String srcpath;
    private String despath;
    private VideoJob videoJob;



    public TransCodeTask( TransCodingMapper transCodingMapper,VideoJob videoJob, HttpclientUtil httpclientUtil,String outDir,String uploadRootDir) {

        String fileOutName = new Date().getTime()+".mp4";

        //设置转码地址
        videoJob.setDespath(outDir+fileOutName);

        this.videoJob = videoJob;
        this.transCodingMapper = transCodingMapper;
        this.srcpath = uploadRootDir+videoJob.getSrcpath();
        this.despath = uploadRootDir+videoJob.getDespath();
        this.httpclientUtil = httpclientUtil;
    }

    @Override
    public void run() {
        if(transfer(srcpath, despath)){
            //1 修改数据库成功状态 0 表示正在转码队列中, 1 表示转码成功  2 表示转码失败
            System.out.println("转码成功,修改数据库状态为>>>> 1 .... ");

            videoJob.setStatus("1");
            transCodingMapper.updateJob(videoJob);
            //像客户端发送消息,告知转码成功
            System.out.println("通知客户端,转码成功 .....");
             httpclientUtil.requestTest();
        } else{
            // 1 修改数据库转码失败状态
            System.out.println("转码失败,修改数据库状态为>>>> 2 .... ");
            videoJob.setStatus("2");
            transCodingMapper.updateJob(videoJob);
            // 2 告知数据库转码失败.

            System.out.println("通知客户端,转码失败 ...更新转码次数..");
            //通知失败如何处理
            httpclientUtil.requestTest();


        }

    }




    public boolean transfer(String infile,String outfile) {

        String transferMp4 = "/usr/local/bin/ffmpeg -i  " + infile + " -vcodec libx264  -b:v 264k -vf fps=fps=25 -aspect 16:9  -f mp4 -y " + outfile;
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(transferMp4);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ( (line = br.readLine()) != null) {
                System.out.println("转码中:" + line);
//                int exitVal = proc.waitFor();
//                System.out.println("Success Process exitValue: " + exitVal);
            }

            int exitVal = proc.waitFor();
            System.out.println("Success Process exitValue: " + exitVal);
            if(exitVal==0){
                return true;
            }else {
                return false;
            }

        } catch (Throwable t) {
            //判断转码次数是否大于三次  如果小于3次 重新转码
            System.out.println("转码失败,重新转码....");
//            transfer(srcpath, despath);
            return false;
        }
    }



}


