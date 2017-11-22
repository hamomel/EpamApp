package com.hamom.epamapp.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hamom on 20.11.17.
 */

public class TodoDetailFragment extends BaseFragment {
    private static final String ARG_TODO_ID = "todo_id";
    private static final String ARG_USER_ID = "user_id";
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("dd MMM yy", Locale.getDefault());
    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private long mUserId;
    private long mTodoId;
    private Todo mTodo;
    private ConstraintSet mInitialSet = new ConstraintSet();
    private boolean isEditMode;

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
    private boolean isNormalState = true;

    public static Fragment newInstance(long todoId, long userId) {
        if (userId < 0) {
            throw new IllegalStateException("User id must be set explicit. Now it = " + userId);
        }
        Bundle args = new Bundle();
        args.putLong(ARG_TODO_ID, todoId);
        args.putLong(ARG_USER_ID, userId);
        Fragment fragment = new TodoDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTodoId = getArguments().getLong(ARG_TODO_ID);
        mUserId = getArguments().getLong(ARG_USER_ID);
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
        initView();
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
        if (mTodo != null) {
            mTitleET.setText(mTodo.getTitle());
            mDescET.setText(mTodo.getDescription());
            mPrioritySpinner.setSelection(mTodo.getPriority());
            Date date = new Date(mTodo.getTime());
            setDate(date);
            setTime(date);
            initPickers(date);
            setViewMode();
        } else {
            mPrioritySpinner.setSelection(Todo.PRIORITY_MED);
            Date date = new Date();
            setDate(date);
            setTime(date);
            initPickers(date);
            setEditMode();
        }
        mTimePicker.setIs24HourView(true);
        mDateTV.setOnClickListener(view -> setDatePickerState());
        mTimeTV.setOnClickListener(view -> setTimePickerState());
        mOkDateBtn.setOnClickListener(view -> {
            updateDate();
            setNormalState();
        });
        mOkTimeBtn.setOnClickListener(view -> {
            updateTime();
            setNormalState();
        });
        mSaveBtn.setOnClickListener(view -> {
            if (isEditMode) {
                saveTodo();
            } else {
                changeMode();
            }
        });
    }

    private void changeMode() {
        if (isEditMode) {
            setViewMode();
        } else {
            setEditMode();
        }
    }

    private void saveTodo() {
        if (!isValidTitle() || !isValidDescription()) return;
        
        String title = mTitleET.getText().toString();
        String description = mDescET.getText().toString();
        int priority = mPrioritySpinner.getSelectedItemPosition();
        Date date = getNewDate();

        if (mTodoId >= 0 && mTodo != null) {
            mTodo.setTitle(title);
            mTodo.setDescription(description);
            mTodo.setPriority(priority);
            mTodo.setTime(date.getTime());
            mLocalService.updateTodo(mTodo, mUserId, result -> returnToListScreen());
        } else {
            Todo todo = new Todo(mTodoId, title, description, date.getTime(), priority);
            mLocalService.saveTodo(todo, mUserId, result -> returnToListScreen());
        }
    }

    private boolean isValidTitle() {
        if (TextUtils.isEmpty(mTitleET.getText())) {
            mTitleET.setError(getString(R.string.field_cant_be_empty));
            return false;
        }
        return true;
    }

    private boolean isValidDescription() {
        if (TextUtils.isEmpty(mDescET.getText())) {
            mDescET.setError(getString(R.string.field_cant_be_empty));
            return false;
        }
        return true;
    }

    private void returnToListScreen() {
        getActivity().finish();
    }

    private Date getNewDate() {
        try {
            Date date = mDateFormat.parse(mDateTV.getText().toString());
            Calendar dateCal = Calendar.getInstance();
            dateCal.setTime(date);
            Date time = mTimeFormat.parse(mTimeTV.getText().toString());
            Calendar timeCal = Calendar.getInstance();
            timeCal.setTime(time);
            Calendar newCal = Calendar.getInstance();
            newCal.set(dateCal.get(Calendar.YEAR), dateCal.get(Calendar.MONTH),
                    dateCal.get(Calendar.DAY_OF_MONTH), timeCal.get(Calendar.HOUR_OF_DAY), timeCal.get(Calendar.MINUTE));
            return newCal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mTodo != null ? new Date(mTodo.getTime()) : new Date();
    }

    private void initPickers(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(date);
        mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), null);
        mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    private void updateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
        setTime(calendar.getTime());
    }

    private void updateDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
        setDate(calendar.getTime());
    }

    private void setDate(Date date) {
        mDateTV.setText(mDateFormat.format(date));
    }

    private void setTime(Date date) {
        mTimeTV.setText(mTimeFormat.format(date));
    }

    private void setViewMode() {
        mTitleET.setEnabled(false);
        mDescET.setEnabled(false);
        mPrioritySpinner.setEnabled(false);
        mDateTV.setEnabled(false);
        mTimeTV.setEnabled(false);
        mTitleET.setEnabled(false);
        mSaveBtn.setText(R.string.edit);
        isEditMode = false;
    }

    private void setEditMode() {
        mTitleET.setEnabled(true);
        mDescET.setEnabled(true);
        mPrioritySpinner.setEnabled(true);
        mDateTV.setEnabled(true);
        mTimeTV.setEnabled(true);
        mTitleET.setEnabled(true);
        mSaveBtn.setText(R.string.save);
        isEditMode = true;
    }


    private void setNormalState() {
        isNormalState = true;
        performTransition(mInitialSet);

    }

    private void setTimePickerState() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mInitialSet);
        set.setVisibility(R.id.title_et, ConstraintSet.GONE);
        set.setVisibility(R.id.description_et, ConstraintSet.GONE);
        set.setVisibility(R.id.priority_tv, ConstraintSet.GONE);
        set.setVisibility(R.id.priority_spinner, ConstraintSet.GONE);
        set.setVisibility(R.id.date_text_tv, ConstraintSet.GONE);
        set.setVisibility(R.id.date_tv, ConstraintSet.GONE);
        set.setVisibility(R.id.save_btn, ConstraintSet.GONE);
        set.setVisibility(R.id.time_picker, ConstraintSet.VISIBLE);
        set.setVisibility(R.id.ok_time_btn, ConstraintSet.VISIBLE);

        isNormalState = false;
        performTransition(set);
    }

    private void setDatePickerState() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mInitialSet);
        set.setVisibility(R.id.title_et, ConstraintSet.GONE);
        set.setVisibility(R.id.description_et, ConstraintSet.GONE);
        set.setVisibility(R.id.priority_tv, ConstraintSet.GONE);
        set.setVisibility(R.id.priority_spinner, ConstraintSet.GONE);
        set.setVisibility(R.id.time_text_tv, ConstraintSet.GONE);
        set.setVisibility(R.id.time_tv, ConstraintSet.GONE);
        set.setVisibility(R.id.save_btn, ConstraintSet.GONE);
        set.setVisibility(R.id.date_picker, ConstraintSet.VISIBLE);
        set.setVisibility(R.id.ok_date_btn, ConstraintSet.VISIBLE);

        isNormalState = false;
        performTransition(set);
    }

    private void performTransition(ConstraintSet set) {
        TransitionManager.beginDelayedTransition(mConstraintLayout);
        set.applyTo(mConstraintLayout);
    }

    @Override
    public boolean onBackPressed() {
        if (!isNormalState) {
            setNormalState();
            return true;
        }
        return super.onBackPressed();
    }
}
