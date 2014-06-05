package com.wanda.member;

import com.google.gson.Gson;

public class ObjectType {
	Gson gson =new Gson();
	
	@Override
	public String toString() {
		return gson.toJson(this);
	}

}
