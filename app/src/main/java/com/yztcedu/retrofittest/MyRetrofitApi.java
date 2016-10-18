package com.yztcedu.retrofittest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by QXL on 2016/10/17.
 */
public interface MyRetrofitApi {
    @GET
    Call<ResponseBody> gethttp(@Url()String url);

}
