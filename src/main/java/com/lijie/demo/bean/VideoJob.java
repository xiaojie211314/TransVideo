package com.lijie.demo.bean;

import lombok.Data;

import java.util.Date;

/**
 * TODO 简单描述该类的实现功能（可选）.
 * <p>
 * Date:     2017/10/12
 *
 * @author lj
 */
@Data
public class VideoJob {
    private int id;//主键
    private String filename="";//文件名
    private String command="";
    private String srcpath="";//源视频地址(绝对路径)
    private String despath="";//转码视频地址(绝对路径)
    private String srcurl="";//视频源上传地址(相对路径)

    private String desurl="";//视频转码成功后地址(相对路径)


    private int status;//	是	int	视频状态 0:等待转码中 1:转码进行中 2:转码成功 3:转码失败 4:转码成功回调失败
    private int counts;//	是	int	转码次数
    private String msg="";//	否	varchar	转码错误信息，如果有错误信息就写入
    private String callmsg="";//	否	varchar	回调错误信息，返回接收到回调接口的信息
    private String videokey="";//	是	varchar	视频唯一值,uuid 格式,用来转码成功后告知服务器更新哪个视频
    private long inputtime = new Date().getTime();//	是	date	视频插入时间
    private long updatetime = new Date().getTime();//	是	date	视频更新时间（开始转码，转码成功或转码失败）
    private String callUrl;//回调地址

    //附加
    private String sign;//接口安全通信钥匙
    private String width = "1080";//分辨率宽度 默认640
    private String height = "720";// 分辨率高度,默认480


    public VideoJob() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSrcpath() {
        return srcpath;
    }

    public void setSrcpath(String srcpath) {
        this.srcpath = srcpath;
    }

    public String getDespath() {
        return despath;
    }

    public void setDespath(String despath) {
        this.despath = despath;
    }

    public String getSrcurl() {
        return srcurl;
    }

    public void setSrcurl(String srcurl) {
        this.srcurl = srcurl;
    }

    public String getDesurl() {
        return desurl;
    }

    public void setDesurl(String desurl) {
        this.desurl = desurl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCallmsg() {
        return callmsg;
    }

    public void setCallmsg(String callmsg) {
        this.callmsg = callmsg;
    }

    public String getVideokey() {
        return videokey;
    }

    public void setVideokey(String videokey) {
        this.videokey = videokey;
    }

    public long getInputtime() {
        return inputtime;
    }

    public void setInputtime(long inputtime) {
        this.inputtime = inputtime;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public String getCallUrl() {
        return callUrl;
    }

    public void setCallUrl(String callUrl) {
        this.callUrl = callUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
