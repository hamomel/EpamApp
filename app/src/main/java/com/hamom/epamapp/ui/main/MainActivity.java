package com.hamom.epamapp.ui.main;

import android.support.v4.app.Fragment;

import com.hamom.epamapp.R;
import com.hamom.epamapp.data.models.Todo;
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
                .enqueue(new Callback<List<Todo>>() {
                    @Override
                    public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                        if (response.isSuccessful()) {
                            showData(response.body());
                        } else {
                            showError(getString(R.string.something_went_wrong));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Todo>> call, Throwable t) {
                        showError(getString(R.string.something_went_wrong));
                    }
                });
    }

    private void showData(List<Todo> todos) {
        TodoListFragment fragment = ((TodoListFragment) getSupportFragmentManager().findFragmentById(R.id.main_frame));
        fragment.setData(todos);
    }
}
