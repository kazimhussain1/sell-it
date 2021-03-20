package com.example.sellit.home.api;

import com.example.sellit.home.api.response.ClassifiedResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HomeApi {

    @GET("default/dynamodb-writer")
    Call<ClassifiedResponse> getClassified();

    @GET("default/dynamodb-writer")
    Call<ClassifiedResponse> getClassifiedPaginated(@Query(value = "page") int pageNo);
}
