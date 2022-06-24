package com.API.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.AB_API.DailyReportSender;
import com.AB_API.TestEngine;
import com.API.Reporter.ScenarioResultObject;

public class ExcelOperation {
	static Properties configProp;
	static FileInputStream configInput;
	public static boolean checkSMSTriggerInterval;

	public static Properties getConfigProperties() throws FileNotFoundException, IOException {
		if (configProp == null) {
			configProp = new Properties();
			configInput = new FileInputStream("config.properties");
			configProp.load(configInput);
		}
		return configProp;
	}

	public void setDataInExcelFile(XSSFWorkbook workbook, String sheetName, int rowNum, int colNum, String data)
			throws Exception {
		try {
			String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation") + "\\"
					+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
			XSSFSheet sheet = workbook.getSheet(sheetName);
			XSSFCell cell = sheet.getRow(rowNum).getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellValue(data);
			FileOutputStream fos = new FileOutputStream(excelFilePath);
			workbook.write(fos);
			fos.close();
		} catch (Exception e) {
			throw (e);
		}
	}

	public List<LinkedHashMap<String, String>> getScenarioData(XSSFWorkbook workbook, String sheetName,
			String scenario) {
		List<LinkedHashMap<String, String>> testDataList = new ArrayList<>();
		XSSFSheet sheet = workbook.getSheet(sheetName);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
			Row headerRow = sheet.getRow(0);
			Row row = sheet.getRow(i);
			if (row.getCell(0).getStringCellValue().equalsIgnoreCase(scenario)) {
				LinkedHashMap<String, String> testData = new LinkedHashMap<>();
				for (int j = 1; j < row.getLastCellNum(); j++) {
					if (row.getCell(j) != null) {
						testData.put(headerRow.getCell(j).getStringCellValue(), row.getCell(j).getStringCellValue());
					} else {
						testData.put(headerRow.getCell(j).getStringCellValue(), "");
					}
				}
				// System.out.println(testData);
				testDataList.add(testData);
			}
		}
		return testDataList;
	}

	public List<LinkedHashMap<Object, Object>> getScenarioDataObject(XSSFWorkbook workbook, String sheetName,
			String scenario) {
		List<LinkedHashMap<Object, Object>> testDataList = new ArrayList<>();
		XSSFSheet sheet = workbook.getSheet(sheetName);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
			Row headerRow = sheet.getRow(0);
			Row row = sheet.getRow(i);
			if (row.getCell(0).getStringCellValue().equalsIgnoreCase(scenario)) {
				LinkedHashMap<Object, Object> testData = new LinkedHashMap<>();
				for (int j = 1; j < row.getLastCellNum(); j++) {
					if (row.getCell(j) != null) {
						Cell cell = row.getCell(j);
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC:
							testData.put(headerRow.getCell(j).getStringCellValue(),
									(int)row.getCell(j).getNumericCellValue());
							break;
						case Cell.CELL_TYPE_BOOLEAN:
							testData.put(headerRow.getCell(j).getStringCellValue(),
									row.getCell(j).getBooleanCellValue());
							break;
						default:
							testData.put(headerRow.getCell(j).getStringCellValue(),
									row.getCell(j).getStringCellValue());
							break;

						}

					} else {
						testData.put(headerRow.getCell(j).getStringCellValue(), "");
					}
				}
				// System.out.println(testData);
				testDataList.add(testData);
			}
		}
		return testDataList;
	}

	public List<LinkedHashMap<String, String>> getScenarioID(XSSFWorkbook workbook, String sheetName, String scenario) {
		List<LinkedHashMap<String, String>> testDataList = new ArrayList<>();
		XSSFSheet sheet = workbook.getSheet(sheetName);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
			Row headerRow = sheet.getRow(0);
			Row row = sheet.getRow(i);

			if (row.getCell(0).getStringCellValue().equalsIgnoreCase(scenario)) {
				LinkedHashMap<String, String> testData = new LinkedHashMap<>();
				for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
					if (row.getCell(j) != null) {
						testData.put(headerRow.getCell(j).getStringCellValue(), row.getCell(j).getStringCellValue());
					} else {
						testData.put(headerRow.getCell(j).getStringCellValue(), "");
					}
				}
				// System.out.println(testData);
				testDataList.add(testData);
			}
		}
		return testDataList;

	}

	/*
	 * PSS for jgetting Scenario Rows in List
	 * 
	 */
	public List<Integer> getScenarioRowforSettingData(XSSFWorkbook workbook, String sheetName, String scenarioID) {
		List<Integer> rowPositionList = new ArrayList<>();
		XSSFSheet sheet = workbook.getSheet(sheetName);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
			int j = 0;
			Row headerRow = sheet.getRow(0);
			Row row = sheet.getRow(i);
			if (row.getCell(0).getStringCellValue().equalsIgnoreCase(scenarioID)) {
				j = row.getRowNum();
				rowPositionList.add(j);
			}
		}
		return rowPositionList;
	}

	public void setDataIntoExcel(XSSFWorkbook workbook, String sheetName, int rowNumber, String columnName,
			String testData) throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation") + "\\"
				+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		Row headerRow = sheet.getRow(0);

		LinkedHashMap<String, Integer> headerPosition = new LinkedHashMap<>();
		for (int columnNo = 1; columnNo < headerRow.getPhysicalNumberOfCells(); columnNo++) {
			headerPosition.put(headerRow.getCell(columnNo).getStringCellValue(), columnNo);
		}
		Row dataRow = sheet.getRow(rowNumber);
		XSSFCell cell = (XSSFCell) dataRow.getCell(headerPosition.get(columnName), Row.RETURN_BLANK_AS_NULL);
		cell.setCellValue(testData);
		FileOutputStream fos = new FileOutputStream(excelFilePath);
		workbook.write(fos);
		fos.close();
	}

	public void updateDataInExcel(XSSFWorkbook workbook, String sheetName, String scenarioId, int columnIndex,
			String data) throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation") + "\\"
				+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		Row headerRow = sheet.getRow(0);
		LinkedHashMap<String, Integer> headerPosition = new LinkedHashMap<>();
		for (int columnNo = 1; columnNo < headerRow.getPhysicalNumberOfCells(); columnNo++) {
			headerPosition.put(headerRow.getCell(columnNo).getStringCellValue(), columnNo);
		}
		// Row dataRow=sheet.getRow(rowNumber);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
			XSSFCell cell1 = sheet.getRow(i).getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			// XSSFCell cell=(XSSFCell)
			// headerRow.getCell(headerPosition.get(columnName),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			if (cell1.getStringCellValue().equalsIgnoreCase(scenarioId)) {
				XSSFCell cell2 = sheet.getRow(i).getCell(columnIndex);
				cell2.setCellValue(data);
				FileOutputStream fos = new FileOutputStream(excelFilePath);
				workbook.write(fos);
				fos.close();
				break;
			}

		}
	}

	public void updateDataForOrderID(XSSFWorkbook workbook, String sheetName, String scenarioId, String columnName,
			String data) throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation") + "\\"
				+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		Row headerRow = sheet.getRow(0);
		LinkedHashMap<String, Integer> headerPosition = new LinkedHashMap<>();
		for (int columnNo = 1; columnNo < headerRow.getPhysicalNumberOfCells(); columnNo++) {
			headerPosition.put(headerRow.getCell(columnNo).getStringCellValue(), columnNo);
		}
		// Row dataRow=sheet.getRow(rowNumber);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
			XSSFCell cell1 = sheet.getRow(i).getCell(0, Row.CREATE_NULL_AS_BLANK);
			XSSFCell cell = (XSSFCell) headerRow.getCell(headerPosition.get(columnName), Row.CREATE_NULL_AS_BLANK);

			if (cell1.getStringCellValue().equalsIgnoreCase(scenarioId)) {
				XSSFRow Row = sheet.getRow(i);
				XSSFCell cell2 = Row.createCell(1);
				cell2.setCellValue(data);
				FileOutputStream fos = new FileOutputStream(excelFilePath);
				workbook.write(fos);
				fos.close();
				break;
			}

		}
	}

	public void updateDataLTP(XSSFWorkbook workbook, String sheetName, String scenarioId, String columnName,
			String data) throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation") + "\\"
				+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		Row headerRow = sheet.getRow(0);
		LinkedHashMap<String, Integer> headerPosition = new LinkedHashMap<>();
		for (int columnNo = 1; columnNo < headerRow.getPhysicalNumberOfCells(); columnNo++) {
			headerPosition.put(headerRow.getCell(columnNo).getStringCellValue(), columnNo);
		}

		// Row dataRow=sheet.getRow(rowNumber);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {

			XSSFCell cell1 = sheet.getRow(i).getCell(0, Row.CREATE_NULL_AS_BLANK);
			XSSFCell cell = (XSSFCell) headerRow.getCell(headerPosition.get(columnName), Row.CREATE_NULL_AS_BLANK);

			if (cell1.getStringCellValue().equalsIgnoreCase(scenarioId)) {
				XSSFRow Row = sheet.getRow(i);
				if (sheetName.equalsIgnoreCase("DT_ModifyOrderData")) {
					XSSFCell cell2 = Row.createCell(16);
					cell2.setCellValue(data);
					FileOutputStream fos = new FileOutputStream(excelFilePath);
					workbook.write(fos);
					fos.close();
					break;
				} else {
					XSSFCell cell2 = Row.createCell(12);
					cell2.setCellValue(data);
					FileOutputStream fos = new FileOutputStream(excelFilePath);
					workbook.write(fos);
					fos.close();
					break;
				}

			}

		}
	}

	public void updateTriggerPriceForCoverOrder(XSSFWorkbook workbook, String sheetName, String scenarioId,
			String columnName, String data) throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation") + "\\"
				+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		Row headerRow = sheet.getRow(0);
		LinkedHashMap<String, Integer> headerPosition = new LinkedHashMap<>();
		for (int columnNo = 1; columnNo < headerRow.getPhysicalNumberOfCells(); columnNo++) {
			headerPosition.put(headerRow.getCell(columnNo).getStringCellValue(), columnNo);
		}
		// Row dataRow=sheet.getRow(rowNumber);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
			XSSFCell cell11 = sheet.getRow(i).getCell(0, Row.CREATE_NULL_AS_BLANK);
			XSSFCell cellData = (XSSFCell) headerRow.getCell(headerPosition.get(columnName), Row.CREATE_NULL_AS_BLANK);

			if (cell11.getStringCellValue().equalsIgnoreCase(scenarioId)) {
				XSSFRow Row = sheet.getRow(i);
				XSSFCell cell2 = Row.createCell(7);
				cell2.setCellValue(data);
				FileOutputStream fos = new FileOutputStream(excelFilePath);
				workbook.write(fos);
				fos.close();
				break;
			}

		}

	}

	public void updateDataForTriggerPrice(XSSFWorkbook workbook, String sheetName, String scenarioId, String columnName,
			String data) throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation") + "\\"
				+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		Row headerRow = sheet.getRow(0);
		LinkedHashMap<String, Integer> headerPosition = new LinkedHashMap<>();
		for (int columnNo = 1; columnNo < headerRow.getPhysicalNumberOfCells(); columnNo++) {
			headerPosition.put(headerRow.getCell(columnNo).getStringCellValue(), columnNo);
		}

		// Row dataRow=sheet.getRow(rowNumber);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {

			XSSFCell cell1 = sheet.getRow(i).getCell(0, Row.CREATE_NULL_AS_BLANK);
			XSSFCell cell = (XSSFCell) headerRow.getCell(headerPosition.get(columnName), Row.CREATE_NULL_AS_BLANK);

			if (cell1.getStringCellValue().equalsIgnoreCase(scenarioId)) {
				XSSFRow Row = sheet.getRow(i);
				if (sheetName.equalsIgnoreCase("DT_ModifyOrderData")) {
					XSSFCell cell2 = Row.createCell(15);
					cell2.setCellValue(data);
					FileOutputStream fos = new FileOutputStream(excelFilePath);
					workbook.write(fos);
					fos.close();
					break;
				} else {
					XSSFCell cell2 = Row.createCell(14);
					cell2.setCellValue(data);
					FileOutputStream fos = new FileOutputStream(excelFilePath);
					workbook.write(fos);
					fos.close();
					break;
				}

			}

		}
	}

	public void updateOrderIDForModifyOrder(XSSFWorkbook workbook, String sheetName, String scenarioId,
			String columnName, String data) throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation") + "\\"
				+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		Row headerRow = sheet.getRow(0);
		LinkedHashMap<String, Integer> headerPosition = new LinkedHashMap<>();
		for (int columnNo = 1; columnNo < headerRow.getPhysicalNumberOfCells(); columnNo++) {
			headerPosition.put(headerRow.getCell(columnNo).getStringCellValue(), columnNo);
		}
		// Row dataRow=sheet.getRow(rowNumber);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
			XSSFCell cell1 = sheet.getRow(i).getCell(0, Row.CREATE_NULL_AS_BLANK);
			XSSFCell cell = (XSSFCell) headerRow.getCell(headerPosition.get(columnName), Row.CREATE_NULL_AS_BLANK);

			if (cell1.getStringCellValue().equalsIgnoreCase(scenarioId)) {
				XSSFRow Row = sheet.getRow(i);
				XSSFCell cell2 = Row.createCell(4);
				cell2.setCellValue(data);
				FileOutputStream fos = new FileOutputStream(excelFilePath);
				workbook.write(fos);
				fos.close();
				break;
			}

		}
	}

	public void updateValidatePasswordDataInExcel(XSSFWorkbook workbook, String sheetName, String scenarioId,
			String columnName, String data) throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation") + "\\"
				+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		Row headerRow = sheet.getRow(0);
		LinkedHashMap<String, Integer> headerPosition = new LinkedHashMap<>();
		for (int columnNo = 1; columnNo < headerRow.getPhysicalNumberOfCells(); columnNo++) {
			headerPosition.put(headerRow.getCell(columnNo).getStringCellValue(), columnNo);
		}
		// Row dataRow=sheet.getRow(rowNumber);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
			XSSFCell cell1 = sheet.getRow(i).getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			XSSFCell cell = (XSSFCell) headerRow.getCell(headerPosition.get(columnName), Row.CREATE_NULL_AS_BLANK);

			if (cell1.getStringCellValue().equalsIgnoreCase(scenarioId)) {
				XSSFCell cell2 = sheet.getRow(i).getCell(4);
				cell2.setCellValue(data);
				FileOutputStream fos = new FileOutputStream(excelFilePath);
				workbook.write(fos);
				fos.close();
				break;
			}
		}
	}

	public void closeWorkbook(XSSFWorkbook workbook) throws IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation") + "\\"
				+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
		FileOutputStream fos = new FileOutputStream(excelFilePath);
		workbook.write(fos);
		fos.close();
	}

	public void createScenarioDataFromResponse(XSSFWorkbook workbook, String sheetName, String scenarioId,
			int rowsToBeAdded) throws IOException {

		XSSFSheet sheet = workbook.getSheet(sheetName);
		for (int i = 1; i <= rowsToBeAdded; i++) {
			int currentRow = sheet.getPhysicalNumberOfRows();
			sheet.createRow(currentRow);
			XSSFCell cell = sheet.getRow(currentRow).getCell(0, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(scenarioId);

		}
		closeWorkbook(workbook);
	}

	public void updateStatusInExcel(XSSFWorkbook workbook, String sheetName, String scenarioId, String data)
			throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFolder")
				+ FrameworkServices.getConfigProperties().getProperty("TestScenarioFileLocation");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
			XSSFCell cell1 = sheet.getRow(i).getCell(0, Row.CREATE_NULL_AS_BLANK);
			if (cell1.getStringCellValue().equalsIgnoreCase(scenarioId)) {
				XSSFCell cell = sheet.getRow(i).getCell(5);
				cell.setCellValue(data);
				FileOutputStream fos = new FileOutputStream(excelFilePath);
				workbook.write(fos);
				fos.close();
				break;
			}
		}
	}

	public void updateTotalTimeInExcel(XSSFWorkbook workbook, String sheetName, String scenarioId, long data)
			throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFolder")
				+ FrameworkServices.getConfigProperties().getProperty("TestScenarioFileLocation");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
			XSSFCell cell1 = sheet.getRow(i).getCell(0, Row.CREATE_NULL_AS_BLANK);
			if (cell1.getStringCellValue().equalsIgnoreCase(scenarioId)) {
				XSSFCell cell = sheet.getRow(i).getCell(8);
				cell.setCellValue(data);
				FileOutputStream fos = new FileOutputStream(excelFilePath);
				workbook.write(fos);
				fos.close();
				break;
			}
		}
	}

	public void updateReportInExcel(XSSFWorkbook workbook, String sheetName, String scenarioId, String data,
			int ColumnNumber, int RowNumber) throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFolder")
				+ FrameworkServices.getConfigProperties().getProperty("TestReportFileLocation");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		XSSFCell cell = sheet.getRow(RowNumber).createCell(ColumnNumber);
		cell = sheet.getRow(RowNumber).getCell(ColumnNumber);

		cell.setCellValue(data);
		FileOutputStream fos = new FileOutputStream(excelFilePath);
		workbook.write(fos);
		fos.close();
	}

	public void updateExecutionTimeInExcel(XSSFWorkbook workbook, String sheetName, String scenarioId, double data,
			int ColumnNumber, int RowNumber) throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFolder")
				+ FrameworkServices.getConfigProperties().getProperty("TestReportFileLocation");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		XSSFCell cell = sheet.getRow(RowNumber).createCell(ColumnNumber);
		cell = sheet.getRow(RowNumber).getCell(ColumnNumber);
		cell.setCellValue(data);
		FileOutputStream fos = new FileOutputStream(excelFilePath);
		workbook.write(fos);
		fos.close();
	}

	public String getCellData(XSSFWorkbook workbook, String sheetName, String scenarioId, int RowNum, int ColNum)
			throws Exception {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFileLocation")
				+ FrameworkServices.getConfigProperties().getProperty("TestDataWorkBook");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		XSSFCell Cell = sheet.getRow(RowNum).getCell(ColNum);
		Cell.setCellType(Cell.CELL_TYPE_STRING);
		String CellData = Cell.getStringCellValue();
		FileOutputStream fos = new FileOutputStream(excelFilePath);
		workbook.write(fos);
		fos.close();
		return CellData;
	}

	public static void isTestSuiteExcelCorrupted() throws IOException {
		String destFolder = getConfigProperties().getProperty("TestDataFolder") + "//TestSuite";
		String src = getConfigProperties().getProperty("TestDataFolder")
				+ getConfigProperties().getProperty("TestScenarioFileLocation");
		String drc = getConfigProperties().getProperty("TestDataFolder") + "//TestSuite" + "//BackUp//"
				+ "API_Testsuite.xlsx";
		File srcFile, destFile, destFolder1;

		srcFile = new File(src);
		destFile = new File(drc);
		destFolder1 = new File(destFolder);

		try {

			XSSFWorkbook workbook1 = new XSSFWorkbook(srcFile);
			workbook1.close();

		} catch (Exception e) {

			FileUtils.copyFileToDirectory(destFile, destFolder1, false);

		}

	}

	public static void isTestDataExcelCorrupted() throws IOException {
		String destFolder = getConfigProperties().getProperty("TestDataFileLocation");
		String src = getConfigProperties().getProperty("TestDataFileLocation") + "//"
				+ getConfigProperties().getProperty("TestDataWorkBook");
		String drc = getConfigProperties().getProperty("TestDataFileLocation") + "//BackUp//" + "ABTemplate_Data.xlsx";
		File srcFile, destFile, destFolder1;

		srcFile = new File(src);
		destFile = new File(drc);
		destFolder1 = new File(destFolder);

		try {

			XSSFWorkbook workbook1 = new XSSFWorkbook(srcFile);
			workbook1.close();

		} catch (Exception e) {

			FileUtils.copyFileToDirectory(destFile, destFolder1, false);

		}

	}

	public static boolean weeklyReportUpdater(List<ScenarioResultObject> sro)
			throws FileNotFoundException, IOException {
		boolean excelFlag = false;
		XSSFWorkbook workbook1 = null;
		FileOutputStream fos = null;
		ExcelOperation excelOperation = new ExcelOperation();
		FileInputStream fileInputStream1;
		DailyReportSender dru = new DailyReportSender();
		try {
			dru.moveWeeklyReportExcelToArchive();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			fileInputStream1 = new FileInputStream(
					new File(FrameworkServices.getConfigProperties().getProperty("TestDataFolder")
							+ FrameworkServices.getConfigProperties().getProperty("TestReportFileLocation")));
			try {
				workbook1 = new XSSFWorkbook(fileInputStream1);

			} catch (Exception e) {
				e.printStackTrace();
				isWeeklyReportExcelCorrupted();
				workbook1 = new XSSFWorkbook(fileInputStream1);
			}
			XSSFSheet sheet = workbook1.getSheet("Report");

			for (ScenarioResultObject s : sro) {
				int rowCount = sheet.getPhysicalNumberOfRows();
				XSSFRow row = sheet.createRow(rowCount);

				XSSFCell module = row.createCell(0);
				module.setCellValue(s.getModule());

				XSSFCell submodule = row.createCell(1);
				submodule.setCellValue(s.getSubModule());

				XSSFCell suiteName = row.createCell(2);
				suiteName.setCellValue(s.getSuiteName());

				XSSFCell date = row.createCell(3);
				date.setCellValue(s.getDate());

				XSSFCell time = row.createCell(4);
				time.setCellValue(s.getTime());

				XSSFCell test = row.createCell(5);
				test.setCellValue(s.getTest());

				XSSFCell pass = row.createCell(6);
				pass.setCellValue(s.getPass());

				XSSFCell fail = row.createCell(7);
				fail.setCellValue(s.getFail());

				XSSFCell TotalTime = row.createCell(8);
				TotalTime.setCellValue(s.getTotalTime());

				fos = new FileOutputStream(FrameworkServices.getConfigProperties().getProperty("TestDataFolder")
						+ FrameworkServices.getConfigProperties().getProperty("TestReportFileLocation"));
				workbook1.write(fos);

			}
			fos.close();
			moveDailyReportExcelToArchive();
			return excelFlag;
		} catch (IOException e) {
			excelFlag = true;
			e.printStackTrace();
			return excelFlag;
		}
	}

	public static void isWeeklyReportExcelCorrupted() throws FileNotFoundException, IOException {
		FileInputStream fileInputStream1;
		File weeklyReport = null;
		File srcFile;

		srcFile = new File(FrameworkServices.getConfigProperties().getProperty("TestDataFolder") + "//"
				+ "ReportBackup//IncrementalBackUp" + "//"
				+ FrameworkServices.getConfigProperties().getProperty("TestReportFileLocation"));
		File destDir = new File(FrameworkServices.getConfigProperties().getProperty("TestDataFolder"));
		File destDir1 = new File(FrameworkServices.getConfigProperties().getProperty("TestDataFolder") + "//"
				+ "ReportBackup//IncrementalBackUp");
		try {
			FileUtils.copyFileToDirectory(srcFile, destDir, false);

			/*
			 * weeklyReport = new
			 * File(FrameworkServices.getConfigProperties().getProperty("TestDataFolder") +
			 * FrameworkServices.getConfigProperties().getProperty("TestReportFileLocation")
			 * ); fileInputStream1 = new FileInputStream(weeklyReport); XSSFWorkbook
			 * workbook1 = new XSSFWorkbook(fileInputStream1);
			 * takeIncrementalBackup(weeklyReport, destDir1); fileInputStream1.close();
			 * workbook1.close();
			 */

		} catch (Exception e) {

			System.out.println("Failed to copy file " + srcFile);
		}

	}

	public boolean getfailedCounterInExcel(XSSFWorkbook workbook, String sheetName, String scenarioId, int[] data)
			throws FileNotFoundException, IOException {
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("TestDataFolder")
				+ FrameworkServices.getConfigProperties().getProperty("TestScenarioFileLocation");
		XSSFCell FC = null;
		XSSFCell US = null;
		int conterF = 0;
		String CounterFS = null;
		String failedCounter = null;

		XSSFSheet sheet = workbook.getSheet(sheetName);

		for (int i = 1; i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
			XSSFCell cell1 = sheet.getRow(i).getCell(0, Row.CREATE_NULL_AS_BLANK);
			if (cell1.getStringCellValue().trim().equalsIgnoreCase(scenarioId)) {
				US = sheet.getRow(i).getCell(8);
				FC = sheet.getRow(i).getCell(3);
				US.setCellValue("smsNotTriggered");
				failedCounter = FC.getStringCellValue();
				conterF = Integer.parseInt(failedCounter);
				conterF = conterF + 1;
				System.out.println("Failed Counter for scenario ID " + scenarioId + " is " + conterF);
				CounterFS = String.valueOf(conterF);
				FC.setCellValue(CounterFS);

				String failedsmsTriggerText = US.getStringCellValue();

				if (contains(data, conterF) && !failedsmsTriggerText.equalsIgnoreCase("smsTriggered")) {
					US.setCellValue("smsTriggered");
					checkSMSTriggerInterval = true;
					break;
				}
			}

		}
		FileOutputStream fos = new FileOutputStream(excelFilePath);
		workbook.write(fos);
		fos.close();
		// workbook.close();
		return checkSMSTriggerInterval;
	}

	public static boolean contains(final int[] array, final int v) {

		boolean result = false;

		for (int i : array) {
			if (i == v) {
				result = true;
				break;
			}
		}

		return result;
	}

	public static void takeIncrementalBackup(File src, File DestDir) {

		try {
			FileUtils.copyFileToDirectory(src, DestDir, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void weeklyPerformanceReportUpdater(List<ScenarioResultObject> sro) throws IOException {
		XSSFWorkbook workbook1 = null;
		XSSFSheet sheet = null;
		FileOutputStream fos = null;
		ExcelOperation excelOperation = new ExcelOperation();
		FileInputStream fileInputStream1;
		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("DailyReportFolder") + "//"
				+ FrameworkServices.getConfigProperties().getProperty("PerformanceReportFileName");
		fileInputStream1 = new FileInputStream(
				new File(FrameworkServices.getConfigProperties().getProperty("DailyReportFolder") + "//"
						+ FrameworkServices.getConfigProperties().getProperty("PerformanceReportFileName")));
		try {
			workbook1 = new XSSFWorkbook(fileInputStream1);
			sheet = workbook1.getSheet("Sheet1");
		} catch (Exception e) {
			checkExcelCorrupted();
			workbook1 = new XSSFWorkbook(fileInputStream1);
			sheet = workbook1.getSheet("Sheet1");
		}
		for (ScenarioResultObject s : sro) {

			for (int i = 1; i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
				XSSFCell cell1 = sheet.getRow(i).getCell(0, Row.CREATE_NULL_AS_BLANK);
				if (cell1.getStringCellValue().equalsIgnoreCase(s.getTest())) {

					String NewtimeStamp = String.valueOf(s.getTotalTime());
					String[] NewtimeStamp1 = NewtimeStamp.split("\\.");
					String NewtimeStamp2 = NewtimeStamp1[0];
					int NewtimeStamp3 = Integer.parseInt(NewtimeStamp2);
					if (NewtimeStamp3 > 1) {
						int threeM = (int) sheet.getRow(i).getCell(3).getNumericCellValue();
						sheet.getRow(i).getCell(3).setCellValue(threeM + 1);
					}

					double totalT = sheet.getRow(i).getCell(2).getNumericCellValue();
					if (totalT < s.getTotalTime())
						sheet.getRow(i).getCell(2).setCellValue(s.getTotalTime());
					if (TestEngine.Failedscenariolist.contains(s.getTest())) {
						int failed1C = (int) sheet.getRow(i).getCell(4).getNumericCellValue();
						sheet.getRow(i).getCell(4).setCellValue(failed1C + 1);
					}

					break;
				}

			}

		}
		fos = new FileOutputStream(excelFilePath);
		workbook1.write(fos);
		fos.close();
		workbook1.close();

	}

	public static void checkExcelCorrupted() throws FileNotFoundException, IOException {
		FileInputStream fileInputStream1;
		File dailyReport = null;
		File srcFile;

		srcFile = new File(FrameworkServices.getConfigProperties().getProperty("DailyReportFolder") + "//"
				+ "BackUpPerformanceSummaryReport//"
				+ FrameworkServices.getConfigProperties().getProperty("PerformanceReportFileName"));
		File destDir = new File(FrameworkServices.getConfigProperties().getProperty("DailyReportFolder"));
		File destDir1 = new File(FrameworkServices.getConfigProperties().getProperty("DailyReportFolder") + "//"
				+ "BackUpPerformanceSummaryReport//DailyIncrementalBackUp");

		try {
			FileUtils.copyFileToDirectory(srcFile, destDir, false);

			/*
			 * dailyReport = new
			 * File(FrameworkServices.getConfigProperties().getProperty("DailyReportFolder")
			 * + FrameworkServices.getConfigProperties().getProperty(
			 * "PerformanceReportFileName")); fileInputStream1 = new
			 * FileInputStream(dailyReport); XSSFWorkbook workbook1 = new
			 * XSSFWorkbook(fileInputStream1); takeIncrementalBackup(dailyReport, destDir1);
			 * fileInputStream1.close(); workbook1.close();
			 */

		} catch (IOException e) {

			System.out.println("Failed to copy " + srcFile);
		}

	}

	public static void moveDailyReportExcelToArchive() {
		LocalDate ld = LocalDate.now();
		Calendar cal = new GregorianCalendar();
		Date date = cal.getTime();
		DateFormat sdf = new SimpleDateFormat("yyyy-MMMM-EEEE");
		String newDate = sdf.format(date);
		System.out.println("Date : " + sdf.format(date));
		String[] dateArray = newDate.split("-");
		String Year = dateArray[0];
		String Month = dateArray[1];
		String day = dateArray[2];
		int hr = TestEngine.getTimeinHours();
		int min = TestEngine.getTimeinMin();
		if (hr == 23 && min <= 20) {

			String APIFolder;
			File APIResultsRootFolder = new File(configProp.getProperty("TestDataFolder"));
			File srcfile = new File(APIResultsRootFolder + "//" + configProp.getProperty("TestReportFileLocation"));
			File drc = new File(configProp.getProperty("TestDataFolder") + "//" + "DailReportBackup//" + Year + "\\"
					+ Month + "\\" + "_" + ld.toString());
			File templateFile = new File(configProp.getProperty("TestDataFolder") + "//ReportBackup//"
					+ configProp.getProperty("TestReportFileLocation"));
			if (!drc.exists()) {
				drc.mkdirs();
			}

			try {
				FileUtils.moveFileToDirectory(srcfile, drc, false);
				System.out.println("Successfully moved 'Daily log Automation file' from location  " + srcfile
						+ " to backup folder " + srcfile);
				// FileUtils.deleteDirectory(srcfile);
				FileUtils.copyFileToDirectory(templateFile, APIResultsRootFolder);
			} catch (IOException e) {
				System.out.println("Failed to take back of " + templateFile + " of result folder " + srcfile);
				e.printStackTrace();
			}
		}
	}
}
