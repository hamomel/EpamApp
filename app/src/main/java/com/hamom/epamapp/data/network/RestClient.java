package com.hamom.epamapp.data.network;

import android.support.annotation.NonNull;

import com.hamom.epamapp.data.network.services.TodoService;
import com.hamom.epamapp.data.network.services.UserService;
import com.hamom.epamapp.utils.AppConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hamom on 09.11.17.
 */

public class RestClient {

    static UserService getUserService() {
        return createRetrofit().create(UserService.class);
    }

    static TodoService getTodoService() {
        return createRetrofit().create(TodoService.class);
    }

    @NonNull
    private static Retrofit createRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(AppConfig.BASE_URL)
                .build();
    }
}
