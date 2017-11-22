package com.hamom.epamapp.ui.todo_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamom.epamapp.R;
import com.hamom.epamapp.data.models.Todo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hamom on 10.11.17.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<Todo> mTodos = new ArrayList<>();
    private TodoItemOnClickListener mOnClickListener;

    public TodoAdapter(TodoItemOnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    void setTodos(List<Todo> todos) {
        mTodos = todos;
        notifyDataSetChanged();
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        Todo todo = mTodos.get(position);
        holder.bind(todo, mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {

        private TextView mTimeTextView;
        private TextView mTitleTextView;
        private TextView mDescTextView;

        TodoViewHolder(View itemView) {
            super(itemView);
            mTimeTextView = itemView.findViewById(R.id.time_tv);
            mTitleTextView = itemView.findViewById(R.id.title_tv);
            mDescTextView = itemView.findViewById(R.id.description_tv);
        }

        void bind(Todo todo, TodoItemOnClickListener listener) {
            String time = getTimeString(todo.getTime());
            mTimeTextView.setText(time);
            mTitleTextView.setText(todo.getTitle());
            mDescTextView.setText(todo.getDescription());
            itemView.setOnClickListener(itemView -> {
                listener.onClick(todo.getId());
            });
        }

        private String getTimeString(long time) {
            Date date = new Date(time);
            SimpleDateFormat format = new SimpleDateFormat("dd MMM yy HH:mm");
            return format.format(date);
        }
    }

    public interface TodoItemOnClickListener {
        void onClick(long itemId);
    }
}
