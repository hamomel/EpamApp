package com.hamom.epamapp.data.network.services;

import com.hamom.epamapp.data.network.requests.SignInReq;
import com.hamom.epamapp.data.network.responces.SignInRes;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by hamom on 02.11.17.
 */

public interface UserService {

  @POST("signIn")
  Call<SignInRes> signIn(@Body SignInReq req);

  @POST("signIn_401")
  Call<SignInRes> signIn401(@Body SignInReq req);
}
