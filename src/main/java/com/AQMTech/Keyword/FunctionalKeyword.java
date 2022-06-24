package com.AQMTech.Keyword;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.AB_API.TestEngine;
import com.API.TestManagementPOJO.MasterScriptPOJO;
import com.API.Utils.ExcelOperation;
import com.API.Utils.FrameworkServices;

public class FunctionalKeyword extends KeywordHelper {
	public void executeTestStep(XSSFWorkbook workbook, MasterScriptPOJO masterScriptPOJO, String sheetName,
			String scenarioID, ExcelOperation excelOperation) throws Throwable {
		String executeStep = masterScriptPOJO.getStepkeyword();
		/*
		 * int hr = getTimeinHours(); int min = getTimeinMin(); String day =
		 * LocalDate.now().getDayOfWeek().name(); SimpleDateFormat formatter = new
		 * SimpleDateFormat("yyyy/MM/dd"); String curDate = formatter.format(new
		 * Date()); GetHolidays.getHolidayList(); String[] holidays =
		 * FrameworkServices.getDataProperties().getProperty("Holidays").split(",");
		 * boolean contains = Arrays.stream(holidays).anyMatch(curDate::equals);
		 * 
		 * if (!(day.equalsIgnoreCase("SATURDAY") || day.equalsIgnoreCase("SUNDAY") ||
		 * contains)) {
		 */
		if (!TestEngine.isLoginFailed) {
			switch (executeStep) {
			case "ExternalServicesClientLogin":
				loginAPIStep(workbook, sheetName, scenarioID, excelOperation);
				break;

			case "ExternalServicesUserDetails":
				extServicesUserDetailsStep(workbook, sheetName, scenarioID, excelOperation);
				break;

			case "ExternalServicesGenerateToken":
				extServicesGenerateTokenStep(workbook, sheetName, scenarioID, excelOperation);
				break;
			case "ExternalServicesVerifyToken":
				extServicesVerifyTokenStep(workbook, sheetName, scenarioID, excelOperation);
				break;

			case "ExternalServicesGenerateJWTtokenAPI":
				extServicesGenerateJWTTokenStep(workbook, sheetName, scenarioID, excelOperation);
				break;
			case "ExternalServicesSubBrokerLoginAPI":
				extServicesSubBrokerLoginAPIStep(workbook, sheetName, scenarioID, excelOperation);
				break;

			case "SensiBullgetOrderBook":
				sensiBullOrderBookHelper(workbook, sheetName, scenarioID, excelOperation);
				break;

			case "SensiBullgetPositions":
				sensibullPositionsHelper(workbook, sheetName, scenarioID, excelOperation);
				break;
			case "SensiBullPlaceOreder":
				sensiBullPlaceOrderHelper(workbook, sheetName, scenarioID, excelOperation);
				break;
			case "SensiBullModifyOrder":
				sensibullModifyOrderHelper(workbook, sheetName, scenarioID, excelOperation);
				break;
			case "SensiBullCancelOrder":
				sensibullCancelOrderHelper(workbook, sheetName, scenarioID, excelOperation);
				break;
			case "SensiBullSellOrder":
				sensibullSellOrderHelper(workbook, sheetName, scenarioID, excelOperation);
				break;

			default:
				break;
			}
		}
	}

	public boolean StartexeTime() {
		if (getTimeinHours() == 9) {
			if (getTimeinMinutes() >= 21)
				return true;
			else
				return false;
		} else {
			if (getTimeinHours() >= 10)
				return true;
			else
				return false;
		}
	}

	public int getTimeinHours() {
		String time = LocalTime.now().toString();
		String hour = time.split(":")[0];
		int hours = Integer.parseInt(hour);

		return hours;
	}

	public int getTimeinMin() {
		String time = LocalTime.now().toString();
		String minute = time.split(":")[1];
		int min = Integer.parseInt(minute);
		return min;
	}

	public int getTimeinMinutes() {
		String time = LocalTime.now().toString();
		String minutes = time.split(":")[1];
		int minute = Integer.parseInt(minutes);

		return minute;
	}

}
