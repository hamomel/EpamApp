package com.hamom.epamapp.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hamom.epamapp.BuildConfig;
import com.hamom.epamapp.R;
import com.hamom.epamapp.utils.ConstantManager;

/**
 * Created by hamom on 03.11.17.
 */

public class MainActivity extends AppCompatActivity {
    private static String TAG = ConstantManager.TAG_PREFIX + "MainActivity: ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) Log.d(TAG, "onStart: ");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) Log.d(TAG, "onDestroy: isFinishing " + isFinishing());


    }
}
