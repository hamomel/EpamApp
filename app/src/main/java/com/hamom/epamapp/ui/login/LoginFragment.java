package com.hamom.epamapp.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.hamom.epamapp.R;
import com.hamom.epamapp.data.network.NetworkDataProvider;
import com.hamom.epamapp.data.network.requests.SignInReq;
import com.hamom.epamapp.data.network.responces.SignInRes;
import com.hamom.epamapp.ui.main.MainActivity;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hamom on 02.11.17.
 */

public class LoginFragment extends Fragment {
  public static final int PASSWORD_MIN_LENGTH = 6;
  public static final int LOGIN_MIN_LENGTH = 3;
  private EditText mLoginEt;
  private EditText mPasswordEt;
  private Button mLoginBtn;

  private NetworkDataProvider mNetworkDataProvider;

  public static LoginFragment newInstance() {
    return new LoginFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mNetworkDataProvider = NetworkDataProvider.get();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login, container, false);
    mLoginEt = view.findViewById(R.id.login_et);
    mPasswordEt = view.findViewById(R.id.password_et);
    mLoginBtn = view.findViewById(R.id.login_btn);
    mLoginBtn.setOnClickListener(v -> onLoginClick());
    return view;
  }

  private void onLoginClick() {
    if (isValidLogin() && isValidPassword()) {
      SignInReq req =
          new SignInReq(mLoginEt.getText().toString(), mPasswordEt.getText().toString());
      if (isSuccessCall()) {
        mNetworkDataProvider.signIn(req, getSignInCallback());
      } else {
        mNetworkDataProvider.signIn_401(req, getSignInCallback());
      }
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
        if (response.code() == 200) {
          Intent intent = new Intent(getActivity(), MainActivity.class);
          startActivity(intent);
        } else {
          showError(getString(R.string.wrong_login_or_password));
        }
      }

      @Override
      public void onFailure(Call<SignInRes> call, Throwable t) {
        showError(getString(R.string.something_went_wrong));
      }
    };
  }

  private void showError(String message) {
    Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
    toast.getView().setBackgroundColor(Color.RED);
    toast.show();
  }

  private boolean isValidPassword() {
    if (mPasswordEt.getText().length() < PASSWORD_MIN_LENGTH) {
      mPasswordEt.setError(getString(R.string.too_short, PASSWORD_MIN_LENGTH));
      return false;
    } else {
      return true;
    }
  }

  private boolean isValidLogin() {
    if (mLoginEt.getText().length() < LOGIN_MIN_LENGTH) {
      mLoginEt.setError(getString(R.string.too_short, LOGIN_MIN_LENGTH));
      return false;
    } else {
      return true;
    }
  }
}
