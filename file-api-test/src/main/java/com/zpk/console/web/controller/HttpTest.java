package com.zpk.console.web.controller;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HttpTest {

    private static final String URL = "http://localhost:8090/api/file/upload";

    private static final File LOCAL_FILE = new File("E:/test/test中文乱码文件.log");

    private static MultiThreadedHttpConnectionManager connectionManager = null;

    private static HttpClient client = null;

    private static int connectionTimeOut = 25000;

    private static int socketTimeOut = 25000;

    private static int maxConnectionPerHost = 20;

    private static int maxTotalConnections = 20;

    private static HttpTest httpClientTest = new HttpTest();


    static {
        if (connectionManager == null) {
            connectionManager = new MultiThreadedHttpConnectionManager();
            connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
            connectionManager.getParams().setSoTimeout(socketTimeOut);
            connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
            connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
            client = new HttpClient(connectionManager);
        }
    }

    public static void main(String[] args) throws Exception {

        List<Integer> lists = Arrays.asList(1, 2, 3, 4);
        lists.forEach(httpClientTest::run);

    }

    private void run(Integer integer) {
        ((Runnable) () -> {
            httpClientTest.httpFileTest();
        }).run();
    }


    public void httpFileTest() {

        if (!LOCAL_FILE.exists()) {
            System.out.println("文件不存在");
        }
        PostMethod filePost = new PostMethod(URL);
        CustomFilePart fp = null;
        try {
            fp = new CustomFilePart("file", LOCAL_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fp.setCharSet("utf-8");
        // StringPart:普通的文本参数
        Part[] parts = {fp, new StringPart("name", "名称a11", "utf-8"),
                new StringPart("version", "版本a1.1", "utf-8")};
        // 对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
        MultipartRequestEntity mre = new MultipartRequestEntity(parts, filePost.getParams());
        filePost.setRequestEntity(mre);

        try {
            client.executeMethod(filePost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 释放连接
        filePost.releaseConnection();

    }

    public static void httpFileTest1() throws Exception {
        CloseableHttpClient client = HttpClients.custom().build();
        //登录
        HttpPost post = new HttpPost(URL);
        MultipartBodyBuilder entity = new MultipartBodyBuilder();
        entity.part("file", LOCAL_FILE);
        entity.part("name", "但是12d");
        entity.part("version", "的方式12d");
        CloseableHttpResponse execute = client.execute(post);
        //释放连接，以免超过服务器负荷
        System.out.println("============================");
        System.out.println(execute);
        System.out.println("============================");
        //释放连接，以免超过服务器负荷
        post.releaseConnection();
    }


}
