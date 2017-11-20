package com.hamom.epamapp.ui.login;


import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hamom.epamapp.R;
import com.hamom.epamapp.data.network.requests.SignInReq;
import com.hamom.epamapp.data.network.responces.SignInRes;
import com.hamom.epamapp.ui.base.OneFragmentActivity;
import com.hamom.epamapp.ui.todo_list.TodoListActivity;
import com.hamom.epamapp.utils.ConstantManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends OneFragmentActivity {
    private static String TAG = ConstantManager.TAG_PREFIX + "LoginActivity: ";

    @Override
    protected Fragment getFragment() {
        return LoginFragment.newInstance();
    }
}
