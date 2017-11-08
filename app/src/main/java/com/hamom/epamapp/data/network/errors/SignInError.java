package com.hamom.epamapp.data.network.errors;

/**
 * Created by hamom on 07.11.17.
 */

public class SignInError extends Throwable {
    private final int mErrorCode;

    public SignInError(int errorCode) {
        super();
        this.mErrorCode = errorCode;
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
