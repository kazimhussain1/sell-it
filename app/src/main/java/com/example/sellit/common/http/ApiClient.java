package com.example.sellit.common.http;


import com.example.sellit.R;
import com.example.sellit.common.ContextService;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    private static ApiClient instance;


    private final Retrofit baseRetrofit;

    private static OkHttpClient okHttpClientAuth;
    private final String apiKey;

    private ApiClient() {
        apiKey = ContextService.getInstance().getContext().getString(R.string.api_key);


        baseRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(getHttpClientAuth())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }


    private OkHttpClient getHttpClientAuth() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        if (okHttpClientAuth == null) {
            okHttpClientAuth = new OkHttpClient.Builder()
                    .addInterceptor(new StethoInterceptor())
                    .addInterceptor(chain -> {

                        Request original = chain.request();

                        HttpUrl url = original.url().newBuilder()
                                .addQueryParameter("key", apiKey)
                                .build();

                        Request request = original.newBuilder()
                                .url(url).build();
                        return chain.proceed(request);
                    })

                    .addInterceptor(interceptor)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClientAuth;
    }

    private Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    public <S> S createService(Class<S> serviceClass) {
        return baseRetrofit.create(serviceClass);
    }
}

