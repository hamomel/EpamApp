package com.hamom.epamapp.data.network.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hamom on 02.11.17.
 */

public class SignInReq {

  @SerializedName("login")
  private String login;
  @SerializedName("user_name")
  private String userName;
  @SerializedName("password")
  private String password;

  public SignInReq(String login, String userName, String password) {
    this.login = login;
    this.userName = userName;
    this.password = password;
  }
}
