package com.AB_API;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.API.Reporter.CustomReporter;
import com.API.Reporter.ScenarioResultObject;
//import com.API.Reporter.GenerateCustomizationReports;
import com.API.TestManagementPOJO.MasterScriptPOJO;
import com.API.TestManagementPOJO.TestScenariosPOJO;
import com.API.Utils.ExcelOperation;
import com.API.Utils.FrameworkServices;
import com.API.Utils.ListenerClass;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.config.DecoderConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.config.SSLConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

//-------------------------------------------Heckyl API---------------------------------------------------------------------
public class TestEngine {

	public static String path;
	public static String SingleHtmlFilePath;
	public static String suiteName;
	public static Date date;
	public static List<ScenarioResultObject> scenarioResult;
	public static boolean isResponseReceieved;
	public static List<String> Failedscenariolist;
	static String strDate;
	public static String APIurl;
	public static String strRequest;
	public static String strResponse;
	public static String blankData;
	public static String sessionID;
	public static String JWTsessionID;
	public static boolean isLoginFailed;
	public static boolean isInvalidSession;
	public static void main(String[] args) throws FileNotFoundException, IOException {
		try {
			FrameworkServices frameworkServices = new FrameworkServices();
			scenarioResult = new ArrayList<ScenarioResultObject>();
			Failedscenariolist = new ArrayList<String>();
			TestNG testNG = new TestNG();
			CustomReporter frl = new CustomReporter();
			// testNG.addListener(frl);
			testNG.addListener((ITestNGListener) frl);
			// testNG.addListener(frl);
			date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
			strDate = formatter.format(date);
			path = frameworkServices.getConfigProperties().getProperty("Execution_Folder") + " " + strDate;
			SingleHtmlFilePath = path + "//"
					+ frameworkServices.getConfigProperties().getProperty("singleHtmlFileFolder") + " " + strDate;
			testNG.setOutputDirectory(path);
			XmlSuite suite = new XmlSuite();
			suiteName = "API_EXECUTION_Summary " + strDate;
			suite.setName(suiteName);
			suite.setParallel(XmlSuite.ParallelMode.FALSE);
			List<XmlSuite> xmlSuites = new ArrayList<XmlSuite>();
			xmlSuites.add(suite);
			TestRunner.takeTemplateTestSuiteFile();
			for (TestScenariosPOJO testScenariosPOJO : frameworkServices.getTestToBeExecuted()) {
				XmlTest test = new XmlTest(suite);
				test.setName(testScenariosPOJO.getReference());
				test.setVerbose(3);
				HashMap<String, String> parameter = new HashMap<String, String>();
				parameter.put("Description", testScenariosPOJO.getDescription());
				parameter.put("ScriptReference", testScenariosPOJO.getAutomationScriptReference());
				parameter.put("ScenarioReference", testScenariosPOJO.getReference());
				parameter.put("Owner", testScenariosPOJO.getOwner());
				parameter.put("Mobile", testScenariosPOJO.getContact());
				parameter.put("Email", testScenariosPOJO.getEmail());
				test.setParameters(parameter);
				List<XmlClass> classes = new ArrayList<XmlClass>();
				classes.add(new XmlClass("com.AB_API.TestRunner"));
				test.setXmlClasses(classes);
			}
			
			// xmlSuites.add(suite);
			testNG.setXmlSuites(xmlSuites);
			try {
				testNG.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			int hr = getTimeinHours();
			int mints = getTimeinMin();
			String filePath = TestEngine.path;
			try {
				integrationAnalysis();

				//ExcelOperation.weeklyPerformanceReportUpdater(scenarioResult);

			} catch (Exception e) {
				System.out.println("Weekly performance report not updated due to some exception");
			}
			boolean excelFlag = false;
			try {
				excelFlag = ExcelOperation.weeklyReportUpdater(scenarioResult);
			} catch (Exception e) {
				e.printStackTrace();
			}

			DailyReportSender DSR = new DailyReportSender();
			DSR.dailyReportUpdater(scenarioResult);
			//DSR.excelUtilityforDailyReport();

			/*if (excelFlag) {
				SMSSender.sendSMSExcelCorrupt();
			}
			int failCount = CustomReporter.totFail;
			if (failCount > 0) {
				if (FrameworkServices.getConfigProperties().getProperty("SendEmailandSMS").equalsIgnoreCase("Yes")) {
					try {
						// Bitly.bitlyLink(filePath);
						// if (ExcelOperation.checkSMSTriggerInterval) {
						// SMSSender.sendSMS("Heckyl API Automation Test FAILED - Scheduled @");
						EmailSender.sendEmailWithAttachments("ABMA.API.Automation@angelbroking.com", "angel@123",
								filePath, "Heckyl API Automation Test FAILED - Scheduled @");
						// }
					} catch (MessagingException e1) {
						e1.printStackTrace();
					}
				}
			}*/
		}
	}

	public static int getTimeinHours() {
		String time = LocalTime.now().toString();
		String hour = time.split(":")[0];
		int hours = Integer.parseInt(hour);
		return hours;
	}

	public static int getTimeinMin() {
		String time = LocalTime.now().toString();
		String minute = time.split(":")[1];
		int min = Integer.parseInt(minute);
		return min;
	}

	public static void integrationAnalysis() throws FileNotFoundException, IOException {
		if (FrameworkServices.getConfigProperties().getProperty("SlackMsg").equalsIgnoreCase("Yes")) {
			for (ScenarioResultObject sro : scenarioResult) {
				String module = sro.getModule();
				String submodule = sro.getSubModule();
				String automationID = sro.getTest();
				String exeDate = strDate;
				double resTime = sro.getTotalTime() * 1000;
				String isAPIFailed = sro.getFail();
				String APIStatus = sro.getPass();
				String failedresponse = sro.getErrorResponse();
				String apiurl = sro.getAPIURL();
				int DelayedResponse = sro.getDelayedResponse();
				String res1 = "{" + "\"" + module + "\"" + ":" + "[" + "{" + "\"subModule\"" + ":" + "\"" + submodule
						+ "\"" + "," + "\"scenarioName\"" + ":" + "\"" + automationID + "\"" + ","
						+ "\"DateOfExecution\"" + ":" + "\"" + exeDate + "\"" + "," + "\"ResponseTime\"" + ":" + "\""
						+ resTime + "\"" + "," + "\"isScenarioFailed\"" + ":" + isAPIFailed + "," + "\"APIStatus\""
						+ ":" + "\"" + APIStatus + "\"" + "," + "\"APIResponseIfFailed\"" + ":" + "\"" + failedresponse
						+ "\"" + "," + "\"APIRequestURL\"" + ":" + "\"" + apiurl + "\"" + "}" + "]" + "}";
				System.out.println(res1);
				String url1 = "https://internalstatus.angelbroking.com/ingest/api_metrics";
				Response res2 = given().headers("x-access-token", "r82089r0294ru024i2i248lhr84ir08234rn4r38r089ur094")
						.config(RestAssuredConfig.config()
								.decoderConfig(DecoderConfig.decoderConfig().defaultContentCharset("UTF-8")).and()
								.sslConfig(new SSLConfig().relaxedHTTPSValidation()))
						.contentType(ContentType.JSON).body(res1).when().post(url1).then().extract().response();
				System.out.println(res2.asString());

			}

		}
	}
}
