package com.zpk.console.http;

import com.zpk.console.http.client.NormalClient;
import com.zpk.console.http.util.RetrofitUtils;
import com.zpk.zcommon.util.ZFileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTests {

    //    private static final String BASE_URL = "http://192.168.37.128:8090/";
    private static final String BASE_URL = "http://127.0.0.1:8090/";

    @Test
    public void uploadFile() {
        Retrofit apis = RetrofitUtils.getRetrofit(BASE_URL, ScalarsConverterFactory.create());
        NormalClient client = apis.create(NormalClient.class);

//        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), "这里是模拟文件的内容123");

//        File resourceFile = ZFileUtils.getResourceFile("./package/JVoIP.zip");
        File resourceFile = new File("E:\\work\\java\\wcode\\logistics\\lora-base\\console\\file-api-test\\target\\file-api-test-1.0.jar");
        System.out.println(resourceFile.getAbsolutePath());
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), resourceFile);

        Map<String, RequestBody> data = new HashMap<>();
        MediaType textType = MediaType.parse("text/plain");
        data.put("name", RequestBody.create(textType, "test"));
        data.put("version", RequestBody.create(textType, "1.1.1"));

        // @Part
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", resourceFile.getName(), file);

        Call<ResponseBody> call = client.fileUpload("api/file/upload", data, filePart);
        ResponseBody rsl = RetrofitUtils.checkAndGetData(call);
        try {
            System.out.println(rsl.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFiles() {
        Retrofit apis = RetrofitUtils.getRetrofit(BASE_URL);
        NormalClient client = apis.create(NormalClient.class);
        Map<String, Object> data = new HashMap<>();
        data.put("name", "test");
        data.put("version", "1.1.1");
        Call<Object> rsl = client.get("api/file/files", data);
        System.out.println(RetrofitUtils.checkAndGetData(rsl));

    }

    @Test
    public void downloadFile() {
        Retrofit apis = RetrofitUtils.getRetrofit(BASE_URL, ScalarsConverterFactory.create());
        NormalClient client = apis.create(NormalClient.class);
//        String fileName = "JVoIP.zip";
        String fileName = "file-api-test-1.0.jar";
        Call<ResponseBody> call = client.fileDownload("api/file/download/test/1.1.1/" + fileName);
        ResponseBody rsl = RetrofitUtils.checkAndGetData(call);
        try {
            ZFileUtils.writeAll("E:/test/data/download/" + fileName, rsl.byteStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
