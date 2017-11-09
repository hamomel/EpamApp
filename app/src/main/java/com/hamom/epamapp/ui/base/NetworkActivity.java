package com.hamom.epamapp.ui.base;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.hamom.epamapp.R;
import com.hamom.epamapp.data.network.NetworkService;

/**
 * Created by hamom on 09.11.17.
 */

public abstract class NetworkActivity extends AppCompatActivity {
    protected boolean mBound;
    protected NetworkService mNetworkService;
    protected ServiceConnection mServiceConnection = getServiceConnection();

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
                onNetworkServiceConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBound = false;
            }
        };
    }

    protected void showError(String message) {
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

    protected abstract Fragment getFragment();

    protected abstract void onNetworkServiceConnected();
}
