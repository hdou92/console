package com.zpk.console.web.controller;

import com.zpk.zcommon.util.ZDateUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Path;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
public class Test {

    private static final String LOG_BASE_PATH = "E:/usr/running-log/logistics/epshealth-logistics-front-server.";

    private static final String LOG_BASE_WRITE_PATH = "E:/app/epshealth-logistics-front-server.";

    private static final String POSTFIX = ".0.log";

    public static final String requestPath = "E:/app/epshealth-logistics-front-server.2018-12-19.哈.log";

    public static final String requestPaths = "E:/app/epshealth-logistics-front-server2019-01-25.txt";

    private static int connectionTimeOut = 25000;

    private static int socketTimeOut = 25000;

    private static int maxConnectionPerHost = 20;

    private static int maxTotalConnections = 20;


    @RequestMapping("/test")
    public static void main(String[] args) {
//        LocalDate localDate = LocalDate.now();
//        File file = new File(LOG_BASE_PATH + localDate.toString() + POSTFIX);
//
//        if(!file.exists()){
//            localDate = localDate.plus(-1, ChronoUnit.DAYS);
//            System.out.println("文件不存在！查看前一天日志" + LOG_BASE_PATH + localDate.toString() + POSTFIX);
//            file = new File(LOG_BASE_PATH + localDate.toString() + POSTFIX);
//        }
//
//        File fileWrite = new File(LOG_BASE_WRITE_PATH + localDate.now().toString() + POSTFIX);
//
//        long lo = file.length() - 2 * (1024 * 1024);
//
//        RandomAccessFile readFile = null;
//        RandomAccessFile writeFile = null;
//        try {
//            readFile= new RandomAccessFile(file,"r");
//            writeFile= new RandomAccessFile(fileWrite,"rw");
//
//            readFile.seek(lo);
//
//            String tmp = "";
//
//            while((tmp = readFile.readLine()) != null) {
//                String data = new String(tmp.getBytes("iso-8859-1"));
//                writeFile.write((data+"\r\n").getBytes());
//           //     lo = readFile.getFilePointer();
//            }
//            readFile.close();
//            writeFile.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            try {
//                if(readFile != null)
//                    readFile.close();
//                if(writeFile != null)
//                    writeFile.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }

        httpSend();

    }

    public static void httpSend(){
        MultiThreadedHttpConnectionManager connectionManager = null;
        HttpClient client;
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
        connectionManager.getParams().setSoTimeout(socketTimeOut);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
        client = new HttpClient(connectionManager);
        String response = "";
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod("http://localhost:8090/api/file/upload1");
          //  postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
           // postMethod.setRequestHeader("Content-Type", "multipart/form-data");
            //将表单的值放入postMethod中
            File file = new File(requestPath);
            postMethod.addParameter("file",String.valueOf(file));
            //执行postMethod
            int statusCode = client.executeMethod(postMethod);
            System.out.println(statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                response = postMethod.getResponseBodyAsString();
                System.out.println(response);
            } else {
                System.out.println("响应状态码 = " + postMethod.getStatusCode());
            }
        } catch (HttpException e) {
            System.out.println("发生致命的异常，可能是协议不对或者返回的内容有问题" + e);
            //   e.printStackTrace();
        } catch (IOException e) {
            System.out.println("发生网络异常" + e);
            //     e.printStackTrace();
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }
    }


}
