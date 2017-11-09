package com.hamom.epamapp.ui.main;

import android.support.v4.app.Fragment;

import com.hamom.epamapp.R;
import com.hamom.epamapp.data.network.responces.TodoRes;
import com.hamom.epamapp.ui.base.NetworkActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hamom on 03.11.17.
 */

public class MainActivity extends NetworkActivity {

    @Override
    protected Fragment getFragment() {
        return TodoListFragment.newInstance();
    }

    @Override
    protected void onNetworkServiceConnected() {
        mNetworkService.getAllTodos()
                .enqueue(new Callback<List<TodoRes>>() {
                    @Override
                    public void onResponse(Call<List<TodoRes>> call, Response<List<TodoRes>> response) {
                        if (response.isSuccessful()) {
                            showData(response.body());
                        } else {
                            showError(getString(R.string.something_went_wrong));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TodoRes>> call, Throwable t) {
                        showError(getString(R.string.something_went_wrong));
                    }
                });
    }

    private void showData(List<TodoRes> todos) {
        TodoListFragment fragment = ((TodoListFragment) getSupportFragmentManager().findFragmentById(R.id.main_frame));
        fragment.setData(todos);
    }
}
