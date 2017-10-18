package com.lijie.demo.bean;

import lombok.Data;

/**
 * TODO 简单描述该类的实现功能（可选）.
 * <p>
 * Date:     2017/10/12
 *
 * @author lj
 */
@Data
public class VideoJob {
    private int id;
    private String srcpath;
    private String despath;
    private String status;

    public VideoJob() {
    }

    public VideoJob(String srcpath, String status) {
        this.srcpath = srcpath;
        this.status = status;
    }

    public VideoJob(int id, String despath, String status) {
        this.id = id;
        this.despath = despath;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
