package com.hamom.epamapp.ui.todo_list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hamom.epamapp.R;
import com.hamom.epamapp.data.models.Todo;
import com.hamom.epamapp.ui.base.BaseFragment;
import com.hamom.epamapp.ui.detail.TodoDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hamom on 10.11.17.
 */

public class TodoListFragment extends BaseFragment {
    private static final String ARG_USER_ID = "user_id";
    private RecyclerView mTodoRecycler;
    private TodoAdapter mAdapter;
    private List<Todo> mTodos;
    private boolean isFirstStart;
    private long mUserId;

    public static Fragment newInstance(long userId) {
        if (userId < 0) {
            throw new IllegalStateException("User id must be set explicit. Now it = " + userId);
        }
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        Fragment fragment = new TodoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUserId = getArguments().getLong(ARG_USER_ID);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //check for each user
        isFirstStart = sharedPreferences.getBoolean(String.valueOf(mUserId), true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);
        mTodoRecycler = view.findViewById(R.id.todo_recycler);
        initView();
        return view;
    }

    private void initView() {
        mAdapter = new TodoAdapter(this::openDetailScreen);
        mTodoRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTodoRecycler.setAdapter(mAdapter);
    }

    private void openDetailScreen(long itemId) {
        Intent intent = TodoDetailActivity.getIntent(getActivity(), mUserId, itemId);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.todo_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                openDetailScreen(-1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean connectNetwork() {
        return isFirstStart;
    }

    @Override
    protected boolean connectLocal() {
        return true;
    }

    @Override
    protected void onNetworkConnected() {
       mNetworkService.getAllTodos().enqueue(new Callback<List<Todo>>() {
           @Override
           public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
               if (response.isSuccessful()) {
                   if (isFirstStart){
                       mTodos = response.body();
                       saveTodosInDb();
                   }
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

    private void saveTodosInDb() {
        if (mBoundLocal && mTodos != null) {
            for (Todo todo : mTodos) {
                mLocalService.saveTodo(todo, mUserId, result -> loadTodosFromDb());
            }
            saveFirstStart();
        }
    }

    private void saveFirstStart() {
        isFirstStart = false;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(String.valueOf(mUserId), false);
        editor.apply();
    }

    @Override
    protected void onLocalConnected() {
        if (isFirstStart) {
            saveTodosInDb();
        } else {
            loadTodosFromDb();
        }

    }

    private void loadTodosFromDb() {
        mLocalService.getAllUserTodos(mUserId, result -> {
            mTodos = result;
            mAdapter.setTodos(mTodos);
        });
    }
}
