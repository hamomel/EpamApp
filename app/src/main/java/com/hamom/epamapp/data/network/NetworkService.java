package com.hamom.epamapp.data.network;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hamom.epamapp.data.network.requests.SignInReq;
import com.hamom.epamapp.data.network.responces.SignInRes;

import java.util.Random;

import retrofit2.Call;

/**
 * Created by hamom on 07.11.17.
 */

public class NetworkService extends Service {
    private UserService mUserService;

    public class NetworkBinder extends Binder {
        public NetworkService getService() {
            return NetworkService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mUserService = RestClient.getUserService();
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
    public Call<SignInRes> signIn(SignInReq req) {
        if (isSuccessCall()) {
            return signIn200(req);
        } else {
            return signIn401(req);
        }
    }

    private boolean isSuccessCall() {
        Random random = new Random();
        return random.nextBoolean();
    }

    private Call<SignInRes> signIn200(SignInReq req) {
        return mUserService.signIn(req);
    }

    private Call<SignInRes> signIn401(SignInReq req) {
        return mUserService.signIn401(req);
    }
    //endregion

}
