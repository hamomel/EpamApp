package com.hamom.epamapp.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.hamom.epamapp.R;

/**
 * Created by hamom on 09.11.17.
 */

public abstract class OneFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, getFragment())
                    .commit();
        }
    }

    protected abstract Fragment getFragment();

    @Override
    public void onBackPressed() {
        BaseFragment fragment = ((BaseFragment) getSupportFragmentManager().findFragmentById(R.id.main_frame));
        if (!fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
