package com.hamom.epamapp.data.network.services;

import com.hamom.epamapp.data.models.Todo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by hamom on 09.11.17.
 */

public interface TodoService {

    @GET("todo")
    Call<List<Todo>> getAllTodos();
}
