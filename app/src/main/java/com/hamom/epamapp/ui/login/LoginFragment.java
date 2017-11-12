package com.hamom.epamapp.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.hamom.epamapp.R;
import com.hamom.epamapp.data.local.ProviderHelper;
import com.hamom.epamapp.data.models.User;
import com.hamom.epamapp.data.network.requests.SignInReq;
import com.hamom.epamapp.ui.main.MainActivity;


/**
 * Created by hamom on 02.11.17.
 */

public class LoginFragment extends Fragment {
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int LOGIN_MIN_LENGTH = 3;
    private EditText mLoginEt;
    private EditText mPasswordEt;
    private ProviderHelper mProviderHelper;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState == null) {
            mProviderHelper = new ProviderHelper(getActivity().getApplicationContext().getContentResolver());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginEt = view.findViewById(R.id.login_et);
        mPasswordEt = view.findViewById(R.id.password_et);
        Button loginBtn = view.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(v -> onLoginClick());
        return view;
    }


    private void onLoginClick() {
        if (isValidLogin() && isValidPassword()) {
            mProviderHelper.getUserByName(mLoginEt.getText().toString(), result -> checkUser(((User) result)));
//            SignInReq req =
//                    new SignInReq(mLoginEt.getText().toString(), mPasswordEt.getText().toString());
//            ((LoginActivity) getActivity()).signIn(req);
        }
    }

    private void checkUser(User user) {
        long id;
        if (user != null) {
            id = user.getId();
            startMainActivity(id);
        } else {
            mProviderHelper.saveUser(mLoginEt.getText().toString(), result -> {
                String stringId = ((Uri) result).getLastPathSegment();
                long userId = Long.parseLong(stringId);
                startMainActivity(userId);
            });
        }
    }

    private void startMainActivity(long id) {
       Intent intent = MainActivity.getNewIntent(getActivity(), id);
       startActivity(intent);
    }

    private boolean isValidPassword() {
        if (mPasswordEt.getText().length() < PASSWORD_MIN_LENGTH) {
            mPasswordEt.setError(getString(R.string.too_short, PASSWORD_MIN_LENGTH));
            return false;
        }
        return true;
    }

    private boolean isValidLogin() {
        if (mLoginEt.getText().length() < LOGIN_MIN_LENGTH) {
            mLoginEt.setError(getString(R.string.too_short, LOGIN_MIN_LENGTH));
            return false;
        }
        return true;
    }
}
