package com.hamom.epamapp.ui.login;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hamom.epamapp.R;
import com.hamom.epamapp.data.network.NetworkObservable;
import com.hamom.epamapp.data.network.NetworkObserver;
import com.hamom.epamapp.data.network.NetworkService;
import com.hamom.epamapp.data.network.errors.SignInError;
import com.hamom.epamapp.data.network.requests.SignInReq;
import com.hamom.epamapp.data.network.responces.SignInRes;
import com.hamom.epamapp.ui.main.MainActivity;

import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private boolean mBound;
    private NetworkService mNetworkService;
    private NetworkObservable<SignInRes> mSignInObservable;
    private ServiceConnection mServiceConnection = getServiceConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, LoginFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, NetworkService.class);
        startService(intent);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }

        if (mSignInObservable != null) {
            mSignInObservable.unSubscribe();
        }

        super.onStop();
    }

    @NonNull
    private ServiceConnection getServiceConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mBound = true;
                mNetworkService = ((NetworkService.NetworkBinder) iBinder).getService();
                subscribeOnSignInObs();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBound = false;
            }
        };
    }

    private void showError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.getView().setBackgroundColor(Color.RED);
        toast.show();
    }

    public void signIn(SignInReq req) {
        mNetworkService.signIn(req);
    }

    private void subscribeOnSignInObs() {
        mSignInObservable = mNetworkService.getSignInObservable();
        mSignInObservable.subscribe(new NetworkObserver<SignInRes>() {
            @Override
            public void onResponse(Response<SignInRes> response) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
//                finish();
            }

            @Override
            public void onError(Throwable error) {
                String message;
                if (error instanceof SignInError) {
                    switch (((SignInError) error).getErrorCode()) {
                        case 401:
                            message = getString(R.string.wrong_login_or_password);
                            break;
                        default:
                            message = getString(R.string.something_went_wrong);
                    }
                } else {
                    message = getString(R.string.something_went_wrong);
                }
                showError(message);
            }
        });
    }
}
