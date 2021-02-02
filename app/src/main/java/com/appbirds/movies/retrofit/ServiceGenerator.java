package com.appbirds.movies.retrofit;

import com.appbirds.movies.utils.AppLog;
import com.appbirds.movies.utils.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    public static final String TAG = ServiceGenerator.class.getSimpleName();
    public static final int CONNECTION_TIMEOUT = 600000;
    private static Retrofit retrofit = null;
    private static ServiceGenerator mServeiceGenerator;


    public ServiceGenerator getInstance() {
        //if (mServeiceGenerator == null) {
        mServeiceGenerator = new ServiceGenerator();
        //}
        return mServeiceGenerator;
    }

    public ServiceGenerator() {
        generateRetrofit();
    }

    public static Retrofit generateRetrofit() {


        AppLog.d(TAG, "getRetrofitClient: " + Constants.bearerToken);

        // set your desired log level
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add the interceptor to OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(httpLoggingInterceptor);

        builder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

        if (Constants.bearerToken != null && !Constants.bearerToken.isEmpty()) {
            builder.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + Constants.bearerToken).build();
                    return chain.proceed(newRequest);
                }
            });
        }
        //
        builder.interceptors().add(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .build();
                return chain.proceed(newRequest);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        return retrofit;
    }


    public RestClient getRestClient() {
        return retrofit.create(RestClient.class);
    }


    public static RestClient getRetrofitClient() {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(700, TimeUnit.SECONDS).readTimeout(700, TimeUnit.SECONDS);

        AppLog.d("AAA", "getRetrofitClient: " + Constants.bearerToken);
        if (!Constants.bearerToken.isEmpty()) {
            okHttpBuilder.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + Constants.bearerToken).build();
                    return chain.proceed(newRequest);
                }
            });
        }
        retrofit = retrofitBuilder.client(okHttpBuilder.build()).
                build();
        return retrofit.create(RestClient.class);
    }
}
