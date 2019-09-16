package com.example.retrofittest.Service.impl;

import com.example.retrofittest.Service.UserLoginService;
import com.example.retrofittest.client.LoginClient;
import com.example.retrofittest.model.LoginInfo;
import com.example.retrofittest.model.LoginResult;
import com.example.retrofittest.model.RestResult;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
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
    public LoginResult login(LoginInfo loginInfo) {
        return toLogin(loginInfo);
    }

    @PostConstruct
    public void init(){
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setAccount("admin");
        loginInfo.setPassword("123456");
        LoginResult loginResult = login(loginInfo);
        System.out.println(loginResult);
    }
    /**
     * 去登录方法
     * @param loginInfo 登录信息
     * @return 登录结果
     */
    private LoginResult toLogin(LoginInfo loginInfo){
        LoginClient loginClient = retrofit.create(LoginClient.class);
        Map<String , Object> map = new HashMap<>();
        map.put("account" , "admin");
        map.put("password" , "123456");
        Call<RestResult<Map<String, Object>>> call =  loginClient.login("localhost:8090/api/test/login",map);
        Response<RestResult<Map<String,Object>>> response;
        try {
            response = call.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!response.isSuccessful()) {
           System.out.println("http call failed, response: " + response);
            return null;
        }

        RestResult<Map<String , Object>> rsl = response.body();
        if (rsl != null && RestResult.RESULT_SUCCESS.equals(rsl.getCode())) {
            Map<String , Object> maps =  rsl.getData();
            if (maps != null) {
                LoginResult lr = new LoginResult();
                lr.setName((String)maps.get("name"));
                lr.setSex((char)maps.get("sex"));
                lr.setAge((int)maps.get("age"));
                return lr;
            }
        }
        return null;
    }
}
