package com.hamom.epamapp.ui.login;


import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hamom.epamapp.R;
import com.hamom.epamapp.data.network.requests.SignInReq;
import com.hamom.epamapp.data.network.responces.SignInRes;
import com.hamom.epamapp.ui.base.NetworkActivity;
import com.hamom.epamapp.ui.main.MainActivity;
import com.hamom.epamapp.utils.ConstantManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends NetworkActivity {
    private static String TAG = ConstantManager.TAG_PREFIX + "LoginActivity: ";

    @Override
    protected Fragment getFragment() {
        return LoginFragment.newInstance();
    }

    @Override
    protected void onNetworkServiceConnected() {

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
}
