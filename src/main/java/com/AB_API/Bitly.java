package com.AB_API;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.API.Utils.FrameworkServices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
//----------------------------------------------------------Heckyl-----------------------------------------
public class Bitly {
	public static String bitlyUrl;
	public static void bitlyLink(String filePath) throws FileNotFoundException, IOException {	
		String path=filePath+"\\emailable-report.html";;
		String splitPath=path.split(":")[1];
		String url=FrameworkServices.getConfigProperties().getProperty("ExecutionResultURL")+splitPath;
		String uri="https://api-ssl.bitly.com/v3/shorten?access_token=d380167c98d5cff765134761c27aec6bc2f328d5&longUrl="+url+"&format=json";
		Response res = given().
				when().
				contentType(ContentType.JSON).get(uri);
		JsonObject jsonObject=new Gson().fromJson(res.asString(), JsonObject.class);
		String result =  jsonObject.get("data").toString();
		result=result.replaceAll("\\\\", "").replaceAll("^\"|\"$", "");
		JsonParser jsonParser = new JsonParser();
		JsonObject childJson=(JsonObject)jsonParser.parse(result);
		bitlyUrl=childJson.get("url").getAsString();
		System.out.println(bitlyUrl);
	}
}
