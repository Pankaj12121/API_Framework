package com.AQMTech.Keyword;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.API.Utils.ExcelOperation;
import com.API.requests.GenerateJWTAPI;
import com.API.requests.GenerateTokenAPI;
import com.API.requests.LoginAPI;
import com.API.requests.SensiBullCancelOrder;
import com.API.requests.SensiBullModifyOrder;
import com.API.requests.SensiBullOrderBook;
import com.API.requests.SensiBullPlaceOrder;
import com.API.requests.SensiBullPositions;
import com.API.requests.SensiBullSellOrder;
import com.API.requests.SubBrokerLoginAPI;
import com.API.requests.UserDetails;
import com.API.requests.VerifyToken;

public class KeywordHelper {

	public void extServicesUserDetailsStep(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws Exception {
		UserDetails extServicesUserDetails = new UserDetails();
		extServicesUserDetails.extServicesUserDetailsStep(workbook, sheetName, excelOperation, scenarioID);
	}

	public void extServicesVerifyTokenStep(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws Exception {
		VerifyToken extServicesVerifyToken = new VerifyToken();
		extServicesVerifyToken.extServicesVerifyTokenStep(workbook, sheetName, excelOperation, scenarioID);
	}

	public void extServicesGenerateTokenStep(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws Exception {
		GenerateTokenAPI extServicesGenerateTokenStep = new GenerateTokenAPI();
		extServicesGenerateTokenStep.extServicesGenerateTokenStep(workbook, sheetName, excelOperation, scenarioID);
	}

	public void extServicesGenerateJWTTokenStep(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws Exception {
		GenerateJWTAPI extServicesGenerateJWTTokenStep = new GenerateJWTAPI();
		extServicesGenerateJWTTokenStep.extServicesGenerateJWTAPIStep(workbook, sheetName, excelOperation, scenarioID);
	}

	public void extServicesSubBrokerLoginAPIStep(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws Exception {
		SubBrokerLoginAPI extServicesSubBrokerLoginAPIStep = new SubBrokerLoginAPI();
		extServicesSubBrokerLoginAPIStep.extServicesGenerateSubBrokerLoginAPIStep(workbook, sheetName, excelOperation,
				scenarioID);
	}

	public void loginAPIStep(XSSFWorkbook workbook, String sheetName, String scenarioID, ExcelOperation excelOperation)
			throws Exception {
		LoginAPI loginapi = new LoginAPI();
		loginapi.LoginAPIStep(workbook, sheetName, excelOperation, scenarioID);
	}

	public void sensibullCancelOrderHelper(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws Exception {
		SensiBullCancelOrder sensiBullCancelOrder = new SensiBullCancelOrder();
		sensiBullCancelOrder.sensibullCancelOrderStep(workbook, sheetName, excelOperation, scenarioID);
	}

	public void sensibullModifyOrderHelper(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws Exception {
		SensiBullModifyOrder sensiBullModifyOrder = new SensiBullModifyOrder();
		sensiBullModifyOrder.sensiBullModifyOrderStep(workbook, sheetName, excelOperation, scenarioID);
	}

	public void sensiBullOrderBookHelper(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws Exception {
		SensiBullOrderBook sensiBullOrderBook = new SensiBullOrderBook();
		sensiBullOrderBook.sensibullOrderBookStep(workbook, sheetName, excelOperation, scenarioID);
	}

	public void sensiBullPlaceOrderHelper(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws Exception {
		SensiBullPlaceOrder sensiBullPlaceOrder = new SensiBullPlaceOrder();
		sensiBullPlaceOrder.sensibullPlaceOrderStep(workbook, sheetName, excelOperation, scenarioID);
	}

	public void sensibullPositionsHelper(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws Exception {
		SensiBullPositions sensiBullPositions = new SensiBullPositions();
		sensiBullPositions.sensiBullPositionsStep(workbook, sheetName, excelOperation, scenarioID);
	}
	
	public void sensibullSellOrderHelper(XSSFWorkbook workbook, String sheetName, String scenarioID,
			ExcelOperation excelOperation) throws Exception {
		SensiBullSellOrder sensiBullsellOrder = new SensiBullSellOrder();
		sensiBullsellOrder.sensibullSellOrderStep(workbook, sheetName, excelOperation, scenarioID);
	}


}
