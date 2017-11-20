package com.hamom.epamapp.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hamom.epamapp.R;
import com.hamom.epamapp.data.models.Todo;
import com.hamom.epamapp.ui.base.BaseFragment;

/**
 * Created by hamom on 20.11.17.
 */

public class TodoDetailFragment extends BaseFragment {
    private static final String ARG_TODO_ID = "todo_id";
    private long mTodoId;
    private Todo mTodo;
    private ConstraintSet mInitialSet = new ConstraintSet();

    private ConstraintLayout mConstraintLayout;
    private EditText mTitleET;
    private EditText mDescET;
    private Spinner mPrioritySpinner;
    private TextView mDateTV;
    private TextView mTimeTV;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private Button mOkTimeBtn;
    private Button mOkDateBtn;
    private Button mSaveBtn;

    public static Fragment newInstance(long todoId) {
        Bundle args = new Bundle();
        args.putLong(ARG_TODO_ID, todoId);
        Fragment fragment = new TodoDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTodoId = getArguments().getLong(ARG_TODO_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_todo, container, false);
        mConstraintLayout = view.findViewById(R.id.constraint_layout);
        mInitialSet.clone(mConstraintLayout);
        mTitleET = view.findViewById(R.id.title_et);
        mDescET = view.findViewById(R.id.description_et);
        mPrioritySpinner = view.findViewById(R.id.priority_spinner);
        mDateTV = view.findViewById(R.id.date_tv);
        mTimeTV = view.findViewById(R.id.time_tv);
        mDatePicker = view.findViewById(R.id.date_picker);
        mTimePicker = view.findViewById(R.id.time_picker);
        mOkTimeBtn = view.findViewById(R.id.ok_time_btn);
        mOkDateBtn = view.findViewById(R.id.ok_date_btn);
        mSaveBtn = view.findViewById(R.id.save_btn);
        return view;
    }

    @Override
    protected boolean connectNetwork() {
        return false;
    }

    @Override
    protected boolean connectLocal() {
        return true;
    }

    @Override
    protected void onNetworkConnected() {

    }

    @Override
    protected void onLocalConnected() {
        if (mTodoId >= 0) {
            mLocalService.getTodo(mTodoId, result -> {
                mTodo = result;
                initView();
            });
        }
    }

    private void initView() {

    }
}
