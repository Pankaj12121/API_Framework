package com.API.Utils;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetParameters {
	public static HashMap<String, String> getParameters(XSSFWorkbook workbook, String scenarioID){
		ExcelOperation excelOperation=new ExcelOperation();
		return excelOperation.getScenarioData(workbook, "DT_Parameters", scenarioID).get(0);
	}

}
