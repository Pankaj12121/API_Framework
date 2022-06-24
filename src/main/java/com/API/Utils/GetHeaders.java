package com.API.Utils;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetHeaders {
	public static HashMap<String, String> getHeaders(XSSFWorkbook workbook, String scenarioID,ExcelOperation excelOperation){
		return excelOperation.getScenarioData(workbook, "DT_Headers", scenarioID).get(0);
	}
}
