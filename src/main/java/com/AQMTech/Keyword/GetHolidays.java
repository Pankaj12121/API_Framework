package com.AQMTech.Keyword;

import static com.jayway.restassured.RestAssured.given;

import org.mozilla.javascript.ast.CatchClause;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import net.minidev.json.JSONObject;

public class GetHolidays {
	public static String AmoDates = "";
	public static String getHolidayList() {
		try {
			JSONObject parentJson=new JSONObject();
			parentJson.put("name","StatementType");
			parentJson.put("value","Select");
			String req="["+parentJson.toString()+"]";
			String uri="http://172.31.16.193/APIService/AngelEyeNextService.svc/api/GetAMOConfigXML";
			Response res=given().
					body(req).
					when().
					contentType(ContentType.JSON).post(uri);
			String result=res.asString();
			String Addresult="{"+'"'+":"+'"'+":"+result+"}".toString();
			JsonParser jsonParser = new JsonParser();
			JsonObject NewJson=(JsonObject)jsonParser.parse(Addresult);
			JsonArray childJson1=NewJson.get(":").getAsJsonArray();
			int size=childJson1.size();
			for(int i=0;i<size;i++) {
				JsonElement Seg=childJson1.get(i);
				if(Seg.getAsJsonObject().get("ExchSeg").getAsString().equalsIgnoreCase("NSE")) {
					AmoDates=Seg.getAsJsonObject().get("AmoDates").getAsString().trim().replaceAll("\\s","");
					break;
				}	
			}
		}catch (Exception e) {
			System.out.println("Holidays List Not Recieved");
		}
		return AmoDates;

	}

}
