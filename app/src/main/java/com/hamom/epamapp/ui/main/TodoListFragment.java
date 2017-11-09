package com.hamom.epamapp.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hamom.epamapp.R;
import com.hamom.epamapp.data.network.responces.TodoRes;

import java.util.List;

/**
 * Created by hamom on 10.11.17.
 */

public class TodoListFragment extends Fragment {
    private RecyclerView mTodoRecycler;
    private TodoAdapter mAdapter;

    public static Fragment newInstance() {
        return new TodoListFragment();
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
        mAdapter = new TodoAdapter();
        mTodoRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTodoRecycler.setAdapter(mAdapter);
    }

    public void setData(List<TodoRes> todos) {
        mAdapter.setTodos(todos);
    }
}
