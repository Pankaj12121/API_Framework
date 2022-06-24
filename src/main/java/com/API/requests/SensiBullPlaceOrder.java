package com.API.requests;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.Reporter;

import com.AB_API.TestEngine;
import com.AB_API.TestRunner;
import com.API.ServiceEnum.ServiceEndpoint;
import com.API.Utils.ExcelOperation;
import com.API.Utils.WebPage;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.DecoderConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.config.SSLConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import net.minidev.json.JSONObject;

public class SensiBullPlaceOrder extends WebPage {

	public String generateParentJson(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws JsonParseException, JsonMappingException, IOException {
		try {
			JSONObject parentJsonObject1 = new JSONObject();
			LinkedHashMap<Object, Object> jsonMap = excelOperation.getScenarioDataObject(workbook, "DT_PlaceOrder", scenarioID)
					.get(0);
			parentJsonObject1.put("segment", jsonMap.get("segment"));
			parentJsonObject1.put("ordDuration", jsonMap.get("ordDuration"));
			parentJsonObject1.put("product", jsonMap.get("product"));
			parentJsonObject1.put("orderType", jsonMap.get("orderType"));
			parentJsonObject1.put("trdSymbol", jsonMap.get("trdSymbol"));
			parentJsonObject1.put("transType", jsonMap.get("transType"));

			parentJsonObject1.put("strategyCode", jsonMap.get("strategyCode"));
			parentJsonObject1.put("token", jsonMap.get("token"));
			parentJsonObject1.put("price", jsonMap.get("price"));

			parentJsonObject1.put("triggerPrice", jsonMap.get("triggerPrice"));
			parentJsonObject1.put("quantity", jsonMap.get("quantity"));
			parentJsonObject1.put("discQuantity", jsonMap.get("discQuantity"));

			parentJsonObject1.put("ordValidityDate", jsonMap.get("ordValidityDate"));
			parentJsonObject1.put("clientLocalIP", jsonMap.get("clientLocalIP"));
			parentJsonObject1.put("clientPublicIP", jsonMap.get("clientPublicIP"));

			parentJsonObject1.put("MACaddress", jsonMap.get("MACaddress"));
			parentJsonObject1.put("appID", jsonMap.get("appID"));
			parentJsonObject1.put("strategyID", jsonMap.get("strategyID"));
			parentJsonObject1.put("isAMO", jsonMap.get("isAMO"));

		   
			ObjectMapper mapper = new ObjectMapper();
			Object json1 = mapper.readValue(parentJsonObject1.toString(), Object.class);

			return parentJsonObject1.toString();
		} catch (Exception | AssertionError e) {
			throw e;
		}
	}

	public void sensibullPlaceOrderStep(XSSFWorkbook workbook, String sheetName, ExcelOperation excelOperation,
			String scenarioID) throws Exception {
		try {
			String uri = ServiceEndpoint.sensibullPlaceOrder.getUrl();
			TestEngine.APIurl = uri;
			String json = generateParentJson(workbook, sheetName, scenarioID,
			excelOperation);
			TestEngine.strRequest = json;
			Reporter.log("<b>Url--></b><Font Color =\"blue\">" + uri + "</Font>");
			Reporter.log("<b>Request is--></b>" + json);
			long start = 0L;
			long end = 0L;
			
			start = System.nanoTime();
			RestAssured.useRelaxedHTTPSValidation();
			Response res1 = RestAssured.given().header("Authorization", TestEngine.sessionID).when().body(json)
					.contentType(ContentType.JSON).post(uri);

			
			end = System.nanoTime();
			
			TestRunner.elapsed = reqResponseTimeCalc(end, start);
			Reporter.log("<b>Response is--></b>" + res1.asString());
			String abc = res1.asString();
			TestEngine.strResponse = abc;
			// String Addresult="{"+'"'+":"+'"'+":"+abc+"}".toString();
			JsonParser jsonParser = new JsonParser();
			JsonObject NewJson = (JsonObject) jsonParser.parse(abc);
			String msg = NewJson.get("data").getAsString();
			getCellData(workbook, "DT_CancelOrder", msg, 1, 1);
			getCellData(workbook, "DT_ModifyOrder", msg, 1, 1);
			assertBlank(msg, "message");

			if (super.er.equals("java.lang.AssertionError: expected [true] but found [false]"))
				Assert.assertEquals(false, true);
		} catch (Exception e) {
			e.printStackTrace();
			// Reporter.log("<b>No Resposne Recieved / Null Values Recieved</b>");
			throw e;
		}
	}
}
