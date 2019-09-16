package com.example.retrofittest.client;

import com.example.retrofittest.model.RestResult;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import java.util.Map;

public interface LoginClient {

    @POST
    Call<RestResult<Map<String, Object>>> login(@Url String url , @QueryMap Map<String ,Object> map);
}
