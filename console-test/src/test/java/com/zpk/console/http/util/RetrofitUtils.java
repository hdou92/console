package com.zpk.console.http.util;

import com.zpk.zcommon.util.ZJsonUtils;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RetrofitUtils {

    public static Retrofit getRetrofit(String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(60, TimeUnit.SECONDS).
                readTimeout(60, TimeUnit.SECONDS).
                writeTimeout(60, TimeUnit.SECONDS).build();
        return new Retrofit.Builder()
                .baseUrl(baseUrl) // 设置网络请求的Url地址
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create()) // 设置json数据解析器
                .addCallAdapterFactory(Java8CallAdapterFactory.create()) // 支持java8平台
                .build();
    }

    public static Retrofit getRetrofit(String baseUrl, retrofit2.Converter.Factory converterFactory) {
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(60, TimeUnit.SECONDS).
                readTimeout(60, TimeUnit.SECONDS).
                writeTimeout(60, TimeUnit.SECONDS).build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl) // 设置网络请求的Url地址
                .client(client)
                // 支持java8平台
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                // 设置json数据解析器
                .addConverterFactory(converterFactory);
        return builder.build();
    }

    public static Retrofit getRetrofit(String baseUrl, int timeOut) {
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(timeOut, TimeUnit.SECONDS).
                readTimeout(timeOut, TimeUnit.SECONDS).
                writeTimeout(timeOut, TimeUnit.SECONDS).build();
        return new Retrofit.Builder()
                .baseUrl(baseUrl) // 设置网络请求的Url地址
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create()) // 设置json数据解析器
                .addCallAdapterFactory(Java8CallAdapterFactory.create()) // 支持java8平台
                .build();
    }

    public static Retrofit getRetrofit(String baseUrl, int timeOut, retrofit2.Converter.Factory converterFactory) {
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(timeOut, TimeUnit.SECONDS).
                readTimeout(timeOut, TimeUnit.SECONDS).
                writeTimeout(timeOut, TimeUnit.SECONDS).build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl) // 设置网络请求的Url地址
                .client(client)
                // 支持java8平台
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                // 设置json数据解析器
                .addConverterFactory(converterFactory);
        return builder.build();
    }

    public static <T> T checkAndGetData(Call<T> call) {
        try {
            Response<T> response = call.execute();
            Assert.assertTrue(response.isSuccessful());
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getData(Call<T> call) {
        Response<T> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }

    public static <T> T getDataToJson(Call<String> call, Class<T> clazz) {
        Response<String> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ZJsonUtils.fromJsonEx(response.body(), clazz);
    }

}
