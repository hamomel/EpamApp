package com.hamom.epamapp.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.hamom.epamapp.data.network.NetworkDataProvider;
import com.hamom.epamapp.databinding.FragmentLoginBinding;

/**
 * Created by hamom on 02.11.17.
 */

public class LoginFragment extends Fragment {
  private FragmentLoginBinding mBinding;

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
    mBinding = FragmentLoginBinding.inflate(inflater, container, false);
    mBinding.setFragment(this);
    return mBinding.getRoot();
  }

  public void onLoginClick(){
    Toast.makeText(getActivity(), mBinding.loginET.getText(), Toast.LENGTH_LONG).show();
  }
}
