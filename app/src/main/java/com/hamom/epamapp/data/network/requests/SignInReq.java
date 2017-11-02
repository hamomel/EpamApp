package com.hamom.epamapp.data.network.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hamom on 02.11.17.
 */

public class SignInReq {

  @SerializedName("login")
  private String login;
  @SerializedName("password")
  private String password;

  public SignInReq(String login, String password) {
    this.login = login;
    this.password = password;
  }
}
