package com.hamom.epamapp.data.network.responces;

import com.google.gson.annotations.SerializedName;

public class SignInRes{

	@SerializedName("token")
	private String token;

	public String getToken(){
		return token;
	}
}