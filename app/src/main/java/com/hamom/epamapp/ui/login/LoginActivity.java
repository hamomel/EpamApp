package com.hamom.epamapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.hamom.epamapp.R;
import com.hamom.epamapp.ui.login.LoginFragment;

public class LoginActivity extends AppCompatActivity {

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
}
