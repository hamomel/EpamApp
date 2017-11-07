package com.hamom.epamapp.data.network;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hamom.epamapp.data.network.errors.SignInError;
import com.hamom.epamapp.data.network.requests.SignInReq;
import com.hamom.epamapp.data.network.responces.SignInRes;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hamom on 07.11.17.
 */

public class NetworkService extends Service {
    private NetworkDataProvider mNetworkDataProvider;
    private NetworkObservable<SignInRes> mSignInObservable;

    public class NetworkBinder extends Binder {

        public NetworkService getService() {
            return NetworkService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNetworkDataProvider = NetworkDataProvider.get();
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

    public NetworkObservable<SignInRes> getSignInObservable(SignInReq req) {
        if (mSignInObservable == null) {
            mSignInObservable = new NetworkObservable<>();
        }

        signIn(req);
        return mSignInObservable;
    }

    private void signIn(SignInReq req) {
        if (isSuccessCall()) {
            mNetworkDataProvider.signIn(req, getSignInCallback());
        } else {
            mNetworkDataProvider.signIn_401(req, getSignInCallback());
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
                    mSignInObservable.setError(new SignInError(String.valueOf(response.code())));
                }
            }

            @Override
            public void onFailure(Call<SignInRes> call, Throwable t) {
                mSignInObservable.setError(t);
            }
        };
    }

}
