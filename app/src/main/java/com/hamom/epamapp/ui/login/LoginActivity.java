package com.hamom.epamapp.ui.login;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.hamom.epamapp.R;
import com.hamom.epamapp.data.network.NetworkService;
import com.hamom.epamapp.data.network.requests.SignInReq;
import com.hamom.epamapp.data.network.responces.SignInRes;
import com.hamom.epamapp.ui.main.MainActivity;
import com.hamom.epamapp.utils.ConstantManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static String TAG = ConstantManager.TAG_PREFIX + "LoginActivity: ";
    private boolean mBound;
    private NetworkService mNetworkService;
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
        super.onStop();
    }

    @NonNull
    private ServiceConnection getServiceConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mBound = true;
                mNetworkService = ((NetworkService.NetworkBinder) iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBound = false;
            }
        };
    }

    public void signIn(SignInReq req) {
        mNetworkService.signIn(req)
                .enqueue(new Callback<SignInRes>() {
                    @Override
                    public void onResponse(Call<SignInRes> call, Response<SignInRes> response) {
                        switch (response.code()) {
                            case 200:
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 401:
                                showError(getString(R.string.wrong_login_or_password));
                                break;
                            default:
                                showError(getString(R.string.something_went_wrong));
                        }
                    }

                    @Override
                    public void onFailure(Call<SignInRes> call, Throwable t) {
                        showError(getString(R.string.something_went_wrong));
                    }
                });
    }

    private void showError(String message) {
        float density = getDensity();
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundColor(Color.RED);
        int horizontalPadding = (int) (16 * density);
        int verticalPadding = view.getPaddingBottom();
        view.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        toast.show();
    }

    private float getDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

}
