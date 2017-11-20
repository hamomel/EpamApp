package com.hamom.epamapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.hamom.epamapp.R;
import com.hamom.epamapp.data.models.User;
import com.hamom.epamapp.ui.base.BaseFragment;
import com.hamom.epamapp.ui.todo_list.TodoListActivity;


/**
 * Created by hamom on 02.11.17.
 */

public class LoginFragment extends BaseFragment {
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int LOGIN_MIN_LENGTH = 3;
    private EditText mLoginEt;
    private EditText mPasswordEt;

    public static LoginFragment newInstance() {
        return new LoginFragment();
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
            mLocalService.getUserByName(mLoginEt.getText().toString(), this::checkUser);
        }
    }

    private void checkUser(User user) {
        long id;
        if (user != null) {
            id = user.getId();
            startTodoListActivity(id);
        } else {
            mLocalService.saveUser(mLoginEt.getText().toString(), result -> {
                String stringId = result.getLastPathSegment();
                long userId = Long.parseLong(stringId);
                startTodoListActivity(userId);
            });
        }
    }

    private void startTodoListActivity(long id) {
       Intent intent = TodoListActivity.getNewIntent(getActivity(), id);
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

    @Override
    protected boolean connectNetwork() {
        return true;
    }

    @Override
    protected boolean connectLocal() {
        return true;
    }

    @Override
    protected void onNetworkConnected() {

    }

    @Override
    protected void onLocalConnected() {

    }
}
