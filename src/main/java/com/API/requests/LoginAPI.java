package com.API.requests;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.Assert;
import org.testng.Reporter;

import com.AB_API.TestEngine;
import com.AB_API.TestRunner;
import com.API.ServiceEnum.ServiceEndpoint;
import com.API.Utils.ExcelOperation;
import com.API.Utils.WebPage;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import net.minidev.json.JSONObject;

public class LoginAPI extends WebPage {

	public String hashCode;
	public String fetchAuthToken;
	public static boolean isHashGenerated;

	public String generateParentJson(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws JsonParseException, JsonMappingException, IOException {
		try {
			JSONObject parentJsonObject1 = new JSONObject();
			LinkedHashMap<String, String> jsonMap = excelOperation
					.getScenarioData(workbook, "DT_ExtServiceClientLogin", scenarioID).get(0);
			parentJsonObject1.put("username", jsonMap.get("username"));
			parentJsonObject1.put("password", jsonMap.get("password"));
			parentJsonObject1.put("appID", jsonMap.get("appID"));

			return parentJsonObject1.toString();
		} catch (Exception | AssertionError e) {
			throw e;
		}
	}

	public void LoginAPIStep(XSSFWorkbook workbook, String sheetName, ExcelOperation excelOperation, String scenarioID)
			throws Exception {

		String uri = ServiceEndpoint.LoginAPI.getUrl();
		TestEngine.APIurl = uri;
		String json = generateParentJson(workbook, sheetName, scenarioID, excelOperation);
		TestEngine.strRequest = json;
		Reporter.log("<b>Url--></b><Font Color =\"blue\">" + uri + "</Font>");
		Reporter.log("<b>Request is--></b>" + json);
		long start = 0L;
		long end = 0L;
		start = System.nanoTime();
		Response res1 = given().body("").when().contentType(ContentType.JSON).post(uri);
		System.out.println("Getting correct log time...");
		end = System.nanoTime();

		start = System.nanoTime();
		Response res = given().body(json).when().contentType(ContentType.JSON).post(uri);
		end = System.nanoTime();

		TestRunner.elapsed = reqResponseTimeCalc(end, start);
		Reporter.log("<b>Response time for Login is --></b>" + TestRunner.elapsed);

		Reporter.log("<b>Response is--></b>" + res.asString());
		String result = res.asString();
		TestEngine.strResponse = result;
		JsonParser jsonParser = new JsonParser();
		JsonObject NewJson = (JsonObject) jsonParser.parse(result);
		JsonObject childJson2 = NewJson.get("data").getAsJsonObject();
		String authToken = childJson2.get("authToken").toString();
		assertBlank(authToken, "authToken");
		if (super.er.equals("java.lang.AssertionError: expected [true] but found [false]"))
			Assert.assertEquals(false, true);

		authToken = authToken.substring(1);
		authToken = authToken.substring(0, authToken.length() - 1);
		Reporter.log("<b>Auth token is--></b>" + authToken);
		Reporter.log("<b>Login Successful !!!</b>");
		String Hashe = getHash(authToken);
		Reporter.log("<b>HASH generated from SHA252--></b>" + Hashe);
		assertBlank(Hashe, "Hash");
		if (!isHashGenerated) {
			getHashToken(authToken, Hashe);
			Reporter.log("<b>AuthToken generated --></b>" + fetchAuthToken);
			assertBlank(fetchAuthToken, "AuthHashToken");
			System.out.println(fetchAuthToken);
			System.out.println(Hashe);
			TestEngine.sessionID = fetchAuthToken;
		}
		if (super.er.equals("java.lang.AssertionError: expected [true] but found [false]"))
			Assert.assertEquals(false, true);

	}

	public String getHash(String authKey) throws InterruptedException {
		try {
			System.setProperty("webdriver.chrome.driver", "D:\\Grid\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless","--window-size=1920,1200");
			WebDriver driver = new ChromeDriver(options);
			//WebDriver driver = new ChromeDriver();
			driver.get("https://tools.keycdn.com/sha256-online-generator");
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(1000);
			Reporter.log("<b>Launched chrome --></b>");
			WebElement cookies = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div/button/span"));
			cookies.click();
			WebElement inputArea = driver.findElement(By.xpath("//*[@id='value']"));
			WebElement generate = driver.findElement(By.xpath("//*[@id='sha256Btn']"));

			String inputValue = "d159f9ff-1825-4aab-ae36-4affa24f4c73|" + authKey;
			Reporter.log("<b>inserted value in browser --></b>" + inputValue);
			inputArea.sendKeys(inputValue);
			generate.click();
			WebElement outputArea = driver.findElement(By.xpath("//*[@id='sha256Result']/pre/code"));
			// outputArea.click();
			hashCode = outputArea.getText();
			Reporter.log("<b>AuthToken generated successfully at browser--></b>" + hashCode);

			driver.close();
		} catch (Exception e) {
			isHashGenerated = true;
			e.printStackTrace();
			String SlackMsg = "Alert: External Services API: SHA256 Hash generation failed ";
			slackIntegration(SlackMsg);
		}
		return hashCode;

	}

	public String getHashToken(String AuthToken, String hash) {
		String uri = ServiceEndpoint.hashTokenLoink.getUrl();
		TestEngine.APIurl = uri;
		String json = "{" + "\"shortToken\"" + ":" + "\"" + AuthToken + "\"" + "," + "\"hash\"" + ":" + "\"" + hash
				+ "\"" + "}";
		TestEngine.strRequest = "{" + "shortToken:" + AuthToken + "," + "hash:" + hash + "}";
		Reporter.log("<b>Url--></b><Font Color =\"blue\">" + uri + "</Font>");
		Reporter.log("<b>Request is--></b>" + json);
		Response res = given().header("x-api-key", "751d55fb-6ba8-4c81-9b65-32698d79d448").body(json).when()
				.contentType(ContentType.JSON).post(uri);
		Reporter.log("<b>Response is--></b>" + res.asString());
		String result = res.asString();
		TestEngine.strResponse = result;
		JsonParser jsonParser = new JsonParser();
		JsonObject NewJson = (JsonObject) jsonParser.parse(result);
		JsonObject childJson2 = NewJson.get("data").getAsJsonObject();
		fetchAuthToken = childJson2.get("authToken").toString();
		fetchAuthToken = fetchAuthToken.substring(1);
		fetchAuthToken = fetchAuthToken.substring(0, fetchAuthToken.length() - 1);

		return fetchAuthToken;
	}
}
