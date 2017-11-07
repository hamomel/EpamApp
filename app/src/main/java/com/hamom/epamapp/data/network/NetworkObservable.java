package com.hamom.epamapp.data.network;

import retrofit2.Response;

/**
 * Created by hamom on 07.11.17.
 */

public class NetworkObservable<T> {
    private Response<T> mResponse;
    private Throwable mError;
    private NetworkObserver<T> mObserver;

    public void subscribe(NetworkObserver<T> observer) {
        mObserver = observer;
        notifyObserver();
    }

    public void unSubscribe() {
        mObserver = null;
    }

    private void notifyObserver() {
        if (mObserver == null) return;

        if (mResponse != null) {
            mObserver.onResponse(mResponse);
            mResponse = null;
        } else {
            if (mError != null) {
                mObserver.onError(mError);
                mError = null;
            }
        }
    }

    public void setResponse(Response<T> response) {
        mResponse = response;
        notifyObserver();
    }

    public void setError(Throwable error) {
        mError = error;
        notifyObserver();
    }
}
