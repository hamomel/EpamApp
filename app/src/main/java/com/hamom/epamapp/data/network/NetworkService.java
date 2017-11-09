package com.hamom.epamapp.data.network;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hamom.epamapp.data.network.errors.SignInError;
import com.hamom.epamapp.data.network.requests.SignInReq;
import com.hamom.epamapp.data.network.responces.SignInRes;
import com.hamom.epamapp.utils.AppConfig;

import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hamom on 07.11.17.
 */

public class NetworkService extends Service {
    private RestService mRestService;
    private NetworkObservable<SignInRes> mSignInObservable;

    public class NetworkBinder extends Binder {
        public NetworkService getService() {
            return NetworkService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new NetworkBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    //region===================== SignIn ==========================
    public NetworkObservable<SignInRes> getSignInObservable() {
        if (mSignInObservable == null) {
            mSignInObservable = new NetworkObservable<>();
        }
        return mSignInObservable;
    }

    public void signIn(SignInReq req) {
        if (isSuccessCall()) {
            signIn(req, getSignInCallback());
        } else {
            signIn401(req, getSignInCallback());
        }
    }

    private boolean isSuccessCall() {
        Random random = new Random();
        return random.nextBoolean();
    }

    private Callback<SignInRes> getSignInCallback() {
        return new Callback<SignInRes>() {

            @Override
            public void onResponse(Call<SignInRes> call, Response<SignInRes> response) {
                if (response.isSuccessful()) {
                    mSignInObservable.setResponse(response);
                } else {
                    mSignInObservable.setError(new SignInError(response.code()));
                }
            }

            @Override
            public void onFailure(Call<SignInRes> call, Throwable t) {
                mSignInObservable.setError(t);
            }
        };
    }


    private void signIn(SignInReq req, Callback<SignInRes> callback) {
        Call<SignInRes> call = mRestService.signIn(req);
        call.enqueue(callback);
    }

    private void signIn401(SignInReq req, Callback<SignInRes> callback) {
        Call<SignInRes> call = mRestService.signIn401(req);
        call.enqueue(callback);
    }
    //endregion

}
