package com.hamom.epamapp.ui.base;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.hamom.epamapp.data.local.LocalService;
import com.hamom.epamapp.data.network.NetworkService;
import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by hamom on 20.11.17.
 */

public abstract class BaseFragment extends Fragment {
    protected boolean mBoundNetwork;
    protected boolean mBoundLocal;
    private ServiceConnection mNetworkConnection = getNetworkConnection();
    private ServiceConnection mLocalConnection = getLocalConnection();
    protected NetworkService mNetworkService;
    protected LocalService mLocalService;

    protected abstract boolean connectNetwork();

    protected abstract boolean connectLocal();

    @Override
    public void onStart() {
        super.onStart();
        if (connectNetwork()) {
            Intent intent = new Intent(getActivity(), NetworkService.class);
            getActivity().startService(intent);
            getActivity().bindService(intent, mNetworkConnection, BIND_AUTO_CREATE);
        }

        if (connectLocal()) {
            Intent intent = new Intent(getActivity(), LocalService.class);
            getActivity().bindService(intent, mLocalConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onStop() {
        if (mBoundNetwork) {
            getActivity().unbindService(mNetworkConnection);
            mBoundNetwork = false;
        }

        if (mBoundLocal) {
            getActivity().unbindService(mLocalConnection);
            mBoundLocal = false;
        }
        super.onStop();
    }


    private ServiceConnection getNetworkConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mBoundNetwork = true;
                mNetworkService = ((NetworkService.NetworkBinder) iBinder).getService();
                onNetworkConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
    }

    protected abstract void onNetworkConnected();

    private ServiceConnection getLocalConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mBoundLocal = true;
                mLocalService = ((LocalService.LocalBinder) iBinder).getService();
                onLocalConnected();

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
    }

    protected abstract void onLocalConnected();

    protected void showError(String message) {
        float density = getDensity();
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundColor(Color.RED);
        int horizontalPadding = (int) (16 * density);
        int verticalPadding = view.getPaddingBottom();
        view.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        toast.show();
    }

    private float getDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    public boolean onBackPressed() {
        return false;
    }
}
