package com.AB_API;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestCode {

	static String s = "{\"appId\":\"f46e80a1bb75c8daa049fc21cf3a345d\",\"authType\":\"TRADE\",\"emailId\":\"\",\"lastLoginTime\":\"1613048409000\",\"tradePlatform\":\"OMNE\",\"userId\":\"S507248\"}";

	public static void main(String[] args) {
		boolean flag = invalidSession();
		if (flag)
			System.out.println("Shoot slck msg");
		else
			System.out.println("dont shoot slack msg");
	}

	public static boolean invalidSession() {
		try {
			JsonParser jsonParser = new JsonParser();
			JsonObject NewJson = (JsonObject) jsonParser.parse(s);
			String childJson2 = NewJson.get("userId").getAsString();

			if (childJson2.contains("Invalid Session")) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
