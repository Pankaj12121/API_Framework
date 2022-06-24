package com.AB_API;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.API.Reporter.ScenarioResultObject;
import com.API.TestManagementPOJO.MasterScriptPOJO;
import com.API.Utils.ExcelOperation;
import com.API.Utils.FrameworkServices;
import com.API.requests.LoginAPI;
import com.AQMTech.Keyword.FunctionalKeyword;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import net.minidev.json.JSONObject;

public class TestRunner extends FunctionalKeyword {
	public static double startTime;
	public static double stopTime;
	public static String APIReqDate;
	FileInputStream inputStream;
	public static boolean fileMoveFlag;
	public static String runningScenarioName;
	public static double elapsed;

	XSSFWorkbook workbook;

	@Parameters({ "Description", "ScriptReference", "ScenarioReference", "Owner", "Mobile", "Email" })

	@Test(testName = "Description")

	public void test(String description, String scriptReference, String scenarioReference, String owner, String mobile,
			String Email) throws Throwable {
		TestEngine.APIurl = " ";
		TestEngine.blankData = " ";
		TestEngine.strRequest = " ";
		TestEngine.strResponse = " ";
		runningScenarioName = scenarioReference;
		ExcelOperation excelOperation = new ExcelOperation();
		ScenarioResultObject sro = new ScenarioResultObject();
		boolean flag = false;
		try {
			Reporter.log("<b>API Description is--></b>" + scenarioReference);
			// Reporter.log("<b><font color='blue'>Owner Details: </font> Name:
			// </b>"+owner);
			FrameworkServices frameworkServices = new FrameworkServices();
			List<MasterScriptPOJO> scriptList = frameworkServices.getMasterScript(scriptReference);
			FileInputStream fileInputStream = new FileInputStream(
					new File(FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation") + "//"
							+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook")));
			ExcelOperation.isTestDataExcelCorrupted();
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			String sheetName = "";
			// startTime = System.currentTimeMillis();
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
			APIReqDate = format.format(date);
			for (MasterScriptPOJO masterScriptPOJO : scriptList) {
				executeTestStep(workbook, masterScriptPOJO, sheetName, scenarioReference, excelOperation);
			}
			flag = true;
			sro.setErrorResponse("");

		} catch (Exception | AssertionError e) {

			if (runningScenarioName.equals("ExternalServices_ClientLogin")) {

				Reporter.log("<b><font color='red'>LOGIN Failed::Null Values Recieved in Response</b></font>");
				sro.setErrorResponse("Null Values Recieved in Response");
				String slackMsg = "LOGIN FAILED for Omneysys ID B159142 ::" + runningScenarioName + "\n# API Owner::"
						+ owner + "(" + mobile + " , " + Email + ")" + "-No Response received." + "\n# API-URL::"
						+ TestEngine.APIurl + "" + "\n# REQUEST::" + TestEngine.strRequest + "\n# RESPONSE-API::"
						+ TestEngine.strResponse;
				if (!LoginAPI.isHashGenerated) {
					slackIntegration(slackMsg);
					slackCriticalAPIs(slackMsg);
				}
				TestEngine.isLoginFailed = true;
			} else {
				Reporter.log("<b><font color='red'>No response received</b></font>");
				sro.setErrorResponse("No response received");
				String slackMsg = "FAILED::" + runningScenarioName + "-No response received." + "\n# API Owner::"
						+ owner + "(" + mobile + " , " + Email + ")" + "\n# API-URL::" + TestEngine.APIurl + ""
						+ "\n# REQUEST::" + TestEngine.strRequest + "\n# RESPONSE-API::" + TestEngine.strResponse;
				slackIntegration(slackMsg);
			}

			Assert.assertEquals(false, true, "Failed due to exception " + e);
			sro.setErrorResponse("Failed due to exception " + e.toString());
		} finally {
			String status = null;
			Reporter.log("<br/><B><Font size=\"4\" Color = \"blue\">API Request Time: </Font>"
					+ "<Font size=\"4\" Color=\"green\">" + APIReqDate + "</Font></b>");
			// stopTime = System.currentTimeMillis();
			// elapsed = ((stopTime - startTime) / 1000);
			System.out.println("Total Execution Time:" + elapsed + " Seconds");
			if (elapsed < 3) {
				Reporter.log("<B><Font size=\"4\" Color = \"blue\">API Response Time: </Font>"
						+ "<Font size=\"4\" Color=\"green\">" + elapsed + " Seconds" + "</Font></B>");
				sro.setDelayedResponse(1);
			} else {
				Reporter.log("<B><Font size=\"4\" Color = \"blue\">API Response Time: </Font>"
						+ "<Font size=\"4\"  Color=\"red\">" + elapsed + " Seconds" + "</Font></B>");
				sro.setDelayedResponse(1);
			}
			FileInputStream fileInputStream = new FileInputStream(
					new File(FrameworkServices.getConfigProperties().getProperty("TestDataFolder")
							+ FrameworkServices.getConfigProperties().getProperty("TestScenarioFileLocation")));
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			if (flag) {
				status = "Passed";
				excelOperation.updateStatusInExcel(workbook, "TestScenarios", scenarioReference, "Passed");
			} else {
				status = "Failed";
				excelOperation.updateStatusInExcel(workbook, "TestScenarios", scenarioReference, "Failed");
				//returnsmsTriggerFlag(workbook, excelOperation, scenarioReference);
			}

			String Suite[] = TestEngine.suiteName.split(" ");
			String SuiteName = Suite[0];
			String Date = Suite[1];
			String Time = Suite[2];
			String module = description.split("-")[0];
			String subModule = description.split("-")[1];
			sro.setModule(module);
			sro.setSubModule(subModule);
			sro.setSuiteName(SuiteName);
			sro.setDate(Date);
			sro.setTime(Time);
			sro.setTest(scenarioReference);
			sro.setPass(status);
			sro.setPass(status);
			if (sro.getPass().equals("Passed"))
				sro.setFail("0");
			else
				sro.setFail("1");
			sro.setTotalTime(elapsed);
			sro.setAPIURL(TestEngine.APIurl);
			TestEngine.scenarioResult.add(sro);

		}
	}

	public synchronized static void returnsmsTriggerFlag(XSSFWorkbook workbook, ExcelOperation excelOperation,
			String scenarioReference) throws InterruptedException {

		int[] smsIntervalTrigger = { 2, 12, 24, 36, 48, 60, 72, 80 };
		try {
			excelOperation.getfailedCounterInExcel(workbook, "TestScenarios", scenarioReference, smsIntervalTrigger);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static void takeIncrementalBackup(File src, File DestDir) {

		try {
			FileUtils.copyFileToDirectory(src, DestDir, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void takeTemplateTestSuiteFile() {
		int hr = TestEngine.getTimeinHours();
		int mints = TestEngine.getTimeinMin();
		File dest = null;
		File src = null;

		try {
			dest = new File(FrameworkServices.getConfigProperties().getProperty("TestScenarioDestiFileLocation"));
			src = new File(FrameworkServices.getConfigProperties().getProperty("TestScenarioSourceFileLocation"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!fileMoveFlag) {
			if (hr == 9 && mints <= 50) {
				takeIncrementalBackup(src, dest);
				fileMoveFlag = true;
			}
		}
	}

	// @BeforeTest
	public void noResponseEmilShooter() {
		try {
			if (FrameworkServices.getConfigProperties().getProperty("SendEmailandSMS").equalsIgnoreCase("Yes")) {
				if (TestEngine.isResponseReceieved) {
					EmailSender.sendEmailForSingleScenario("ABMA.API.Automation@angelbroking.com", "angel@123",
							TestEngine.SingleHtmlFilePath + "//" + runningScenarioName + ".html",
							"Heckyl API- " + runningScenarioName + ":"
									+ " No Response Received hence Automation Test FAILED - Scheduled @");
					TestEngine.isResponseReceieved = false;
				}
			}
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void slackIntegration(String apiFailedMsg) throws FileNotFoundException, IOException {
		if (FrameworkServices.getConfigProperties().getProperty("SlackMsg").equalsIgnoreCase("Yes")) {
			if (!LoginAPI.isHashGenerated) {
				JSONObject parentJsonObject1 = new JSONObject();
				parentJsonObject1.put("text", apiFailedMsg);
				//parentJsonObject1.put("text", apiFailedMsg);
				String uri = "https://hooks.slack.com/services/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";
				Response res = given().body(parentJsonObject1).when().contentType(ContentType.JSON).post(uri);
				parentJsonObject1.put("text", "-----------------End---------------------------");
				Response res2 = given().body(parentJsonObject1).when().contentType(ContentType.JSON).post(uri);
			}
		}
	}

	public void slackCriticalAPIs(String apiFailedMsg) throws FileNotFoundException, IOException {
		if (FrameworkServices.getConfigProperties().getProperty("SlackMsg").equalsIgnoreCase("Yes")) {
			JSONObject parentJsonObject1 = new JSONObject();
			parentJsonObject1.put("text", apiFailedMsg);
			String uri = "https://hooks.slack.com/services/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";
			Response res = given().body(parentJsonObject1).when().contentType(ContentType.JSON).post(uri);
			parentJsonObject1.put("text", "-----------------End---------------------------");
			Response res2 = given().body(parentJsonObject1).when().contentType(ContentType.JSON).post(uri);
		}
	}

	public boolean invalidSession() {
		try {
			JsonParser jsonParser = new JsonParser();
			JsonObject NewJson = (JsonObject) jsonParser.parse(TestEngine.strResponse);
			String childJson2 = NewJson.get("message").getAsString();

			if (childJson2.contains("Invalid Session") || childJson2.contains("Unauthorized")) {
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
