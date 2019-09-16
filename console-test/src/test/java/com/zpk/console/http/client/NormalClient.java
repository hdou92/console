package com.zpk.console.http.client;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface NormalClient {

    @GET
    Call<String> getString(@Url String url);

    @GET
    Call<Object> get(@Url String url, @QueryMap Map<String, Object> query);

    @POST
    @Headers("content-type: application/json;charset=UTF-8")
    Call<String> postString(@Url String url, @Body String data);

    @POST
    Call<Object> post(@Url String url, @Body Map<String, Object> data);

    @POST
    Call<Object> postWithQuery(@Url String url, @QueryMap Map<String, Object> query,
                               @Body Map<String, Object> data);

    @PUT
    Call<Object> put(@Url String url, @Body Map<String, Object> data);

    @PUT
    Call<Object> putWithQuery(@Url String url, @QueryMap Map<String, Object> query,
                              @Body Map<String, Object> data);

    @DELETE
    Call<Object> delete(@Url String url, @QueryMap Map<String, Object> query);

    @POST
    @Multipart
//    @Headers("content-type: multipart/form-data")
    Call<ResponseBody> fileUpload(@Url String url, @PartMap Map<String, RequestBody> args, @Part MultipartBody.Part file);

    @GET
    Call<ResponseBody> fileDownload(@Url String url);


}
