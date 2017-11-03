package com.lijie.demo.httpclient;

import com.alibaba.fastjson.JSON;
import com.lijie.demo.bean.VideoJob;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * TODO 简单描述该类的实现功能（可选）.
 * <p>
 * Date:     2017/10/12
 *
 * @author likaihua
 */
@Component
public class HttpclientUtil {

    @Autowired
    private HttpClient httpClient;


    public String get(String url)
    {
        return doGet(url);
    }
    public String post(String url, VideoJob videoJob)
    {
        return doPost(url,videoJob);
    }

    private String doGet(String url) {
        try {
            HttpGet httpgets = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpgets);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instreams = entity.getContent();
                String str = convertStreamToString(instreams);
                return str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String doPost(String url,VideoJob videoJob) {
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(JSON.toJSONString(videoJob)));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instreams = entity.getContent();
                String str = convertStreamToString(instreams);
                return str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
