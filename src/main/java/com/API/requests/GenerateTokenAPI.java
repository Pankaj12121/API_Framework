package com.API.requests;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.Reporter;

import com.AB_API.TestEngine;
import com.AB_API.TestRunner;
import com.API.ServiceEnum.ServiceEndpoint;
import com.API.Utils.ExcelOperation;
import com.API.Utils.WebPage;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import net.minidev.json.JSONObject;

public class GenerateTokenAPI extends WebPage {

	public String generateParentJson(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws JsonParseException, JsonMappingException, IOException {
		try {
			JSONObject parentJsonObject1 = new JSONObject();
			LinkedHashMap<String, String> jsonMap = excelOperation.getScenarioData(workbook, "DT_FundsAPI", scenarioID)
					.get(0);
			parentJsonObject1.put("segment", jsonMap.get("segment"));
			parentJsonObject1.put("exchange", jsonMap.get("exchange"));
			parentJsonObject1.put("product", jsonMap.get("product"));

			ObjectMapper mapper = new ObjectMapper();
			Object json1 = mapper.readValue(parentJsonObject1.toString(), Object.class);

			return parentJsonObject1.toString();
		} catch (Exception | AssertionError e) {
			throw e;
		}
	}

	public void extServicesGenerateTokenStep(XSSFWorkbook workbook, String sheetName, ExcelOperation excelOperation,
			String scenarioID) throws Exception {
		try {
			String sessionID = getSessionID();
			TestEngine.JWTsessionID = sessionID;
			String uri = ServiceEndpoint.generateToken.getUrl();
			TestEngine.APIurl = uri;
			// String json = generateParentJson(workbook, sheetName, scenarioID,
			// excelOperation);
			// TestEngine.strRequest = json;
			Reporter.log("<b>Url--></b><Font Color =\"blue\">" + uri + "</Font>");
			// Reporter.log("<b>Request is--></b>" + json);
			long start = 0L;
			long end = 0L;
			String nJson = "{" + "\"clientID\"" + ":" + "\"B159142\"" + "," + "\"sessionID\"" + ":" + sessionID + "}";
			Reporter.log("<b>Request is--></b>" + nJson);
			start = System.nanoTime();
			RestAssured.useRelaxedHTTPSValidation();
			Response res1 = RestAssured.given().when().body(nJson).contentType(ContentType.JSON).post(uri);

			end = System.nanoTime();
			TestRunner.elapsed = reqResponseTimeCalc(end, start);
			Reporter.log("<b>Response is--></b>" + res1.asString());
			String abc = res1.asString();
			TestEngine.strResponse = abc;
			// String Addresult="{"+'"'+":"+'"'+":"+abc+"}".toString();
			JsonParser jsonParser = new JsonParser();
			JsonObject NewJson = (JsonObject) jsonParser.parse(abc);
			JsonObject NewJson1 = NewJson.get("data").getAsJsonObject();
			String msg = NewJson1.get("authToken").getAsString();

			assertBlank(msg, "authToken");

			if (super.er.equals("java.lang.AssertionError: expected [true] but found [false]"))
				Assert.assertEquals(false, true);
		} catch (Exception e) {
			e.printStackTrace();
			// Reporter.log("<b>No Resposne Recieved / Null Values Recieved</b>");
			throw e;
		}
	}

	public String getSessionID() {
		String sessionID = "";
		try {
			System.setProperty("webdriver.chrome.driver", "D:\\Grid\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless","--window-size=1920,1200");
			WebDriver driver = new ChromeDriver(options);
			//WebDriver driver = new ChromeDriver();
			driver.get("http://calebb.net/");
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Reporter.log("<b>Launched chrome --></b>");
			WebElement inputArea = driver.findElement(By.xpath("//*[@id='inputBox']"));
			bringElementintoView(driver, inputArea);
			String inputValue = TestEngine.sessionID;
			Reporter.log("<b>inserted value in browser --></b>" + inputValue);

			inputArea.sendKeys(inputValue);
			WebElement outputArea = driver.findElement(By.xpath("//*[@id='decodedToken']/div[6]/span[4]"));

			sessionID = outputArea.getText();

			Reporter.log("<b>sessionID generated successfully at browser--></b>" + sessionID);
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
			String SlackMsg = "Alert : External Services API : generate session ID got failed.";
			slackIntegration(SlackMsg);
			System.exit(0);
		}
		return sessionID;

	}

	public void bringElementintoView(WebDriver driver, WebElement element) {

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

	}

	public void actionSendKeys(WebDriver driver, WebElement element, String data) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.click();
		actions.sendKeys(data);
		actions.build().perform();
	}
}
