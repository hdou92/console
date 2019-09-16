package com.example.retrofittest.client;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import retrofit2.Retrofit;

import javax.xml.ws.Provider;
import java.util.concurrent.TimeUnit;

@Repository
public class LoginClientImpl implements Provider<LoginClient> {

   /* @Autowired
    private Retrofit retrofit;

    @Bean
    public Retrofit getRetrofit(){
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(60, TimeUnit.SECONDS).
                readTimeout(60, TimeUnit.SECONDS).
                writeTimeout(60, TimeUnit.SECONDS).build();
        return new Retrofit.Builder()
                .baseUrl("http://localhost:8090/api/test/login/") // 设置网络请求的Url地址
                .client(client)
                .build();
    }


    @Override
    public LoginClient invoke(LoginClient request) {
        return retrofit.create(LoginClient.class);
    }*/
   @Override
   public LoginClient invoke(LoginClient request) {
       return null;
   }
}
