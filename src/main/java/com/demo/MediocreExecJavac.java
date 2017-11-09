package com.demo;
import java.io.*;
import java.lang.*;


/**
 * 安装 yasm
 * 安装 x264
 * 安装 ffmpeg
 * http://blog.csdn.net/season_hangzhou/article/details/24399371
 * @author jieli
 *
 */

public class MediocreExecJavac {

    public void runCmd(String infile,String outfile) {
        String command = "/usr/local/bin/ffmpeg -i  " + infile + " -vcodec libx264  -b:v 264k -vf fps=fps=25 -aspect 16:9  -f mp4 -y " + outfile;

        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
 
            while ( (line = br.readLine()) != null)
                System.out.println(">>>转码中>>>>>>>>>>"+line);

            int exitVal = proc.waitFor();
            System.out.println("Process exitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        System.out.println("转码完成: OK " );
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

            while ( (line = br.readLine()) != null)
                System.out.println("转码中:"+line);

            int exitVal = proc.waitFor();
            System.out.println("Success Process exitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        return true;
    }

//    public static String readFile(String fileName,int id) {
//        String dataStr = "";
//        FileInputStream fis = null;
//
//        try {
//            FileReader file = new FileReader(fileName);//建立FileReader对象，并实例化为fr
//            BufferedReader br=new BufferedReader(file);//建立BufferedReader对象，并实例化为br
//            String Line=br.readLine();//从文件读取一行字符串
//            dataStr=Line;
//            br.close();//关闭BufferedReader对象
//        } catch(Exception e) {
//
//        } finally {
//            try {
//                if(fis!=null)
//                    fis.close();
//            } catch(Exception e) {}
//        }
//        return dataStr;
//    }

//    public String readtime(String file) {
//        String str="/Users/jieli/Documents/workfile/videotest/info.txt";
//        String timelen = "";
//        String cmd = "timelen "+file;
//
////        runCmd(cmd);
////        timelen=readFile(str,1);
//
//        return timelen;
//    }

    public static void main(String args[]) {
        MediocreExecJavac me = new MediocreExecJavac();
        String infile = "/Users/jieli/Documents/workfile/videotest/in/test.avi";
        String outfile = "/Users/jieli/Documents/workfile/videotest/out/test.mp4";

//        String timelen = me.readtime(infile);
        me.runCmd(infile,outfile);
//        System.out.println("timelen is :" + timelen);

//        if(me.transfer(infile,outfile)) {
//            System.out.println("the transfer is ok!");
//        } else {
//            System.out.println("the transfer is error!");
//        }
    }
}