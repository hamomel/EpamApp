package com.hamom.epamapp.data.network;

import retrofit2.Response;

/**
 * Created by hamom on 07.11.17.
 */

public interface NetworkObserver<T> {

    void onResponse(Response<T> response);
    void onError(Throwable error);
}
