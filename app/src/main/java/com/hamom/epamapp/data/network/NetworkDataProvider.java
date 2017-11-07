package com.hamom.epamapp.data.network;

import com.hamom.epamapp.data.network.requests.SignInReq;
import com.hamom.epamapp.data.network.responces.SignInRes;
import com.hamom.epamapp.utils.AppConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hamom on 02.11.17.
 */

public class NetworkDataProvider {
  private static NetworkDataProvider INSTANCE;
  private RestService mRestService;

  private NetworkDataProvider(){
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(AppConfig.BASE_URL)
        .build();

    mRestService = retrofit.create(RestService.class);
  }

  public static NetworkDataProvider get() {
    if (INSTANCE == null) {
      INSTANCE = new NetworkDataProvider();
    }
    return INSTANCE;
  }

  public void signIn(SignInReq req, Callback<SignInRes> callback) {
    Call<SignInRes> call = mRestService.signIn(req);
    call.enqueue(callback);
  }

  public void signIn401(SignInReq req, Callback<SignInRes> callback) {
    Call<SignInRes> call = mRestService.signIn401(req);
    call.enqueue(callback);
  }
}
