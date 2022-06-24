package com.API.Utils;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.Reporter;

import com.AB_API.TestEngine;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import net.minidev.json.JSONObject;

public class WebPage {
	public String er = " ";

	public void assertBlank(String data, String name) {

		try {
			Assert.assertTrue(data.length() >= 1);
			Reporter.log("<B><Font Color = \"black\">" + name + " : </Font></B>" + data);
		} catch (AssertionError error) {
			Reporter.log("<B><Font Color = \"red\">" + name + ": Failed (Blank Data) </Font></B>");
			// Assert.assertEquals(false, true);
			TestEngine.blankData=name+": Failed (Blank Data)";
			er = error.toString();
		}
	}

	public String getSessionID(String response) {
		String sessionID = null;
		try {
			sessionID = response.split(Pattern.quote("|"))[3].split("=")[1];
			Reporter.log("Session ID:" + sessionID);
		} catch (Exception e) {
			sessionID = response.split(Pattern.quote("|"))[2].split("=")[1];
			Reporter.log("Session ID:" + sessionID);
		}
		return sessionID;
	}

	public String getGroupId(String response) {
		String GroupID = null;
		try {
			GroupID = response.split(Pattern.quote("|"))[5].split("=")[1];
			Reporter.log("Group ID:" + GroupID);
		} catch (Exception e) {
			Reporter.log("Group ID not recieved");
			TestEngine.blankData="Group ID not recieved";
		}
		return GroupID;
	}

	public String getLTP(String response) {
		String LTP = null;
		try {
			LTP = response.split(Pattern.quote("|"))[3];
			Reporter.log("LTP:" + LTP);
		} catch (Exception e) {
			Reporter.log("LTP not recieved");
			TestEngine.blankData="LTP not recieved";
		}
		return LTP;
	}

	public void assertBlankforTrade(String data, String apiName, String errorMsg) {
		String actData = data.trim();
		if (actData.equalsIgnoreCase("NoData") || actData.equalsIgnoreCase("1") || actData.length() < 1) {
			Reporter.log("<B><Font Color = \"red\"> " + errorMsg + " </Font></B>");
			TestEngine.blankData=errorMsg;
			Assert.assertEquals(true, false);
			// Reporter.log("<B><Font Color = \"black\">"+apiName+" Passed </Font></B>");
		}

		/*
		 * try { Assert.assertTrue(data.length()>=1);
		 * //Reporter.log("<B><Font Color = \"black\">"+apiName+" Passed </Font></B>");
		 * } catch(AssertionError error) {
		 * Reporter.log("<B><Font Color = \"red\"> "+errorMsg+" </Font></B>");
		 * //Assert.assertEquals(false, true); //er=error.toString(); throw error; }
		 */

	}

	public void responseTimeTrade(String time) {
		double elapsed = Double.parseDouble(time) / 1000;
		if (elapsed < 3)
			Reporter.log("<B><Font size=\"3\" Color = \"blue\">API Response Time: </Font>"
					+ "<Font size=\"4\" Color=\"green\">" + elapsed + " Seconds" + "</Font></B>");
		else
			Reporter.log("<B><Font size=\"3\" Color = \"blue\">API Response Time: </Font>"
					+ "<Font size=\"4\"  Color=\"red\">" + elapsed + " Seconds" + "</Font></B>");

	}

	public double reqResponseTimeCalc(long end, long start) {

		long elapsedTime = end - start;
		double seconds = (double) elapsedTime / 1000000000;
		DecimalFormat df = new DecimalFormat("#.##");
		String secondstr = df.format(seconds);
		seconds = Double.parseDouble(secondstr);
		return seconds;
	}
	public  void slackIntegration(String SlackMsg) {
		JSONObject parentJsonObject1 = new JSONObject();
		parentJsonObject1.put("text", SlackMsg);
		String uri = "https://hooks.slack.com/services/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";
		Response res = given().body(parentJsonObject1).when().contentType(ContentType.JSON).post(uri);
		parentJsonObject1.put("text", "-----------------End---------------------------");
		Response res2 = given().body(parentJsonObject1).when().contentType(ContentType.JSON).post(uri);

	}
	@SuppressWarnings("deprecation")
	public String getCellData(XSSFWorkbook workbook, String sheetName, String data, int RowNum, int ColNum)
			throws Exception {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation")
				+"\\"+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		XSSFCell Cell = sheet.getRow(RowNum).getCell(ColNum);
		Cell.setCellType(Cell.CELL_TYPE_STRING);
		Cell.setCellValue(data);;
		String CellData=Cell.getStringCellValue();
		FileOutputStream fos = new FileOutputStream(excelFilePath);
		workbook.write(fos);
		fos.close();
		return CellData;
	}
}
