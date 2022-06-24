package com.AB_API;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.API.Reporter.ScenarioResultObject;
import com.API.Utils.ExcelOperation;
import com.API.Utils.FrameworkServices;

public class DailyReportSender {
	Properties configProp;
	FileInputStream configInput;

	String DailyReportPath;

	public void excelUtilityforDailyReport() {
		loadConfig();
		DailyReportPath = configProp.getProperty("DailyReportFolder") + configProp.getProperty("DailyReportFileName");
		if (getTimeinHours() == 9 && getTimeinMin() <= 10)
			moveReportExcelToArchive();
		//dailyReportToRepo();

	}

	public void moveReportExcelToArchive() {

		Calendar cal = new GregorianCalendar();
		Date date = cal.getTime();
		DateFormat sdf = new SimpleDateFormat("yyyy-MMMM-dd");
		String newDate = sdf.format(date);
		System.out.println("Date : " + sdf.format(date));
		String[] dateArray = newDate.split("-");
		String Year = dateArray[0];
		String Month = dateArray[1];
		String day = dateArray[2];

		String APIFolder;
		File APIResultsRootFolder = new File(configProp.getProperty("DailyReportFolder"));
		File srcfile = new File(APIResultsRootFolder + configProp.getProperty("DailyReportFileName"));
		File drc = new File(configProp.getProperty("DailyReportArchieveFolder") + Year + "\\" + Month + "\\" + day);
		File templateFile = new File(configProp.getProperty("DailyReportBackUpTemplateFolder")
				+ configProp.getProperty("DailyReportFileName"));
		if (!drc.exists()) {
			drc.mkdirs();
		}

		try {
			FileUtils.moveFileToDirectory(srcfile, drc, false);
			System.out.println("Successfully moved 'Daily Automation file' from location  " + srcfile
					+ " to backup folder " + srcfile);
			// FileUtils.deleteDirectory(srcfile);
			FileUtils.copyFileToDirectory(templateFile, APIResultsRootFolder);
		} catch (IOException e) {
			System.out.println("Failed to take back of " + templateFile + " of result folder " + srcfile);
			e.printStackTrace();
		}

	}
	
	public void moveWeeklyReportExcelToArchive() {
		loadConfig();
		LocalDate ld= LocalDate.now();
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
		if (day.equals("Sunday") && (hr == 9 && min <= 30)) {
			
			String APIFolder;
			File APIResultsRootFolder = new File(configProp.getProperty("TestDataFolder"));
			File srcfile = new File(APIResultsRootFolder+"//" + configProp.getProperty("TestReportFileLocation"));
			File drc = new File("D://WeeklyAPIReport_Archieve\\ExternalServices\\" + Year + "\\" + Month + "\\" +"_"+ld.toString());
			File templateFile = new File(configProp.getProperty("TestDataFolder")+"//ReportBackup//"
					+ configProp.getProperty("TestReportFileLocation"));
			if (!drc.exists()) {
				drc.mkdirs();
			}

			try {
				FileUtils.moveFileToDirectory(srcfile, drc, false);
				System.out.println("Successfully moved 'Daily Automation file' from location  " + srcfile
						+ " to backup folder " + srcfile);
				// FileUtils.deleteDirectory(srcfile);
				FileUtils.copyFileToDirectory(templateFile, APIResultsRootFolder);
			} catch (IOException e) {
				System.out.println("Failed to take back of " + templateFile + " of result folder " + srcfile);
				e.printStackTrace();
			}
		}
	}

	public void dailyReportToRepo() {

		try {

			if (getTimeinHours() == 10 && getTimeinMin() <= 15) {
				EmailSender.sendEmailWithAttachments("ABMA.API.Automation@angelbroking.com", "angel@123",
						configProp.getProperty("DailyReportFolder") + configProp.getProperty("DailyReportFileName"),
						"NonTrade API MarketHour Executions ResponseTime Analysis - Scheduled @","9:15 to 10:30");
			} else if (getTimeinHours() == 12 && getTimeinMin() <= 15) {
				EmailSender.sendEmailWithAttachments("ABMA.API.Automation@angelbroking.com",
						"angel@123",
						configProp.getProperty("DailyReportFolder") + configProp.getProperty("DailyReportFileName"),
						"NonTrade API MarketHour Executions ResponseTime Analysis - Scheduled @","10:31 to 12:00");

			} else if (getTimeinHours() == 14 && getTimeinMin() <= 15){
				EmailSender.sendEmailWithAttachments("ABMA.API.Automation@angelbroking.com",
						"angel@123",
						configProp.getProperty("DailyReportFolder") + configProp.getProperty("DailyReportFileName"),
						"NonTrade API MarketHour Executions ResponseTime Analysis - Scheduled @","12:01 to 2:00");

			} else if(getTimeinHours() == 16 && getTimeinMin() <= 15){
				EmailSender.sendEmailWithAttachments("ABMA.API.Automation@angelbroking.com",
						"angel@123",
						configProp.getProperty("DailyReportFolder") + configProp.getProperty("DailyReportFileName"),
						"NonTrade API MarketHour Executions ResponseTime Analysis - Scheduled @","2:01 to 3:50");

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e1) {
			e1.printStackTrace();
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

	public String getDay() {
		String day = LocalDate.now().getDayOfWeek().name();
		return day;
	}

	public Properties loadConfig() {
		try {
			if (configProp == null) {
				configProp = new Properties();
				configInput = new FileInputStream("./config.properties");
				configProp.load(configInput);
			}
			return configProp;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public boolean dailyReportUpdater(List<ScenarioResultObject> sro) throws FileNotFoundException, IOException {
		boolean excelFlag = false;
		XSSFWorkbook workbook1 = null;
		FileOutputStream fos = null;
		ExcelOperation excelOperation = new ExcelOperation();
		FileInputStream fileInputStream1;
		try {
			fileInputStream1 = new FileInputStream(
					new File(FrameworkServices.getConfigProperties().getProperty("DailyReportFolder")
							+ FrameworkServices.getConfigProperties().getProperty("DailyReportFileName")));
			try {
			workbook1 = new XSSFWorkbook(fileInputStream1);
			}catch(Exception e) {
				e.printStackTrace();
				checkExcelCorrupted();
				workbook1 = new XSSFWorkbook(fileInputStream1);
			}

			XSSFCellStyle headerStyle = workbook1.createCellStyle();
			XSSFFont headerLineFont = workbook1.createFont();
			headerStyle.setAlignment(HorizontalAlignment.LEFT);
			headerStyle.setAlignment(HorizontalAlignment.JUSTIFY);

			headerLineFont.setFontHeight(12);
			headerLineFont.setColor(IndexedColors.BLACK.getIndex());

			headerLineFont.setBold(true);
			headerStyle.setFont(headerLineFont);

			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// headerStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.index);;
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);

			headerStyle.setBorderBottom(BorderStyle.DOUBLE);
			headerStyle.setBorderTop(BorderStyle.DOUBLE);
			headerStyle.setBorderRight(BorderStyle.DOUBLE);
			headerStyle.setBorderLeft(BorderStyle.DOUBLE);

			XSSFSheet sheet = workbook1.getSheet("ExternalServices");
			XSSFRow Headerrow = sheet.getRow(0);
			/*int timeRange = Headerrow.getPhysicalNumberOfCells();
			if (getTimeinHours() == 9 && getTimeinMin() <= 15) {
				Cell TimeRangeCell = Headerrow.createCell(timeRange);
				TimeRangeCell.setCellStyle(headerStyle);
				TimeRangeCell.setCellValue("9:15 to 10:30");
			}else if (getTimeinHours() == 10 && getTimeinMin() <= 15) {
				Cell TimeRangeCell = Headerrow.createCell(timeRange);
				TimeRangeCell.setCellStyle(headerStyle);
				TimeRangeCell.setCellValue("10:31 to 12:00");
				
			}else if (getTimeinHours() == 12 && getTimeinMin() <= 15) {
				Cell TimeRangeCell = Headerrow.createCell(timeRange);
				TimeRangeCell.setCellStyle(headerStyle);
				TimeRangeCell.setCellValue("12:01 to 2:00");
				
			}else if (getTimeinHours() == 2 && getTimeinMin() <= 15) {
				Cell TimeRangeCell = Headerrow.createCell(timeRange);
				TimeRangeCell.setCellStyle(headerStyle);
				TimeRangeCell.setCellValue("2:01 to 3:50");
				
			}*/
			int timestampCell = Headerrow.getPhysicalNumberOfCells();
			Cell HeaderCell = Headerrow.createCell(timestampCell);
			HeaderCell.setCellStyle(headerStyle);
			HeaderCell.setCellValue(sro.get(0).getTime());

			for (ScenarioResultObject s : sro) {

				updateTimeStampInExcel(workbook1, "ExternalServices", s.getModule(), s.getSubModule(), s.getDate(),
						s.getTest(), s.getPass(), s.getTotalTime(), timestampCell);

			}

			System.out.println("Daily Analysis report updated successfully.");
			return excelFlag;
		} catch (IOException e) {
			System.out.println("Failed to update Daily Analysis report.");
			excelFlag = true;
			e.printStackTrace();
			return excelFlag;
		}
	}

	public static void checkExcelCorrupted() throws FileNotFoundException, IOException {
		FileInputStream fileInputStream1;
		File dailyReport = null;
		File srcFile;

		srcFile = new File(
				FrameworkServices.getConfigProperties().getProperty("DailyReportFolder") + "//" + "DailyReportBackup"
						+ "//" + FrameworkServices.getConfigProperties().getProperty("DailyReportFileName"));
		File destDir = new File(FrameworkServices.getConfigProperties().getProperty("DailyReportFolder"));
		File destDir1 = new File(FrameworkServices.getConfigProperties().getProperty("DailyReportFolder")+ "//" + "DailyReportBackup//DailyIncrementalBackUp");

		try {
			FileUtils.copyFileToDirectory(srcFile, destDir, false);
			/*
			 * dailyReport = new
			 * File(FrameworkServices.getConfigProperties().getProperty("DailyReportFolder")
			 * +
			 * FrameworkServices.getConfigProperties().getProperty("DailyReportFileName"));
			 * fileInputStream1 = new FileInputStream(dailyReport); XSSFWorkbook workbook1 =
			 * new XSSFWorkbook(fileInputStream1);
			 * takeIncrementalBackup(dailyReport,destDir1); fileInputStream1.close();
			 * workbook1.close();
			 */

		} catch (Exception e) {

			System.out.println("Failed to copy file "+srcFile);

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

	public void updateTimeStampInExcel(XSSFWorkbook workbook, String sheetName, String module, String subModule,
			String Date, String scenarioId, String Status, double data, int timeStamp)
			throws FileNotFoundException, IOException {
		
		XSSFCell timeStampcell;
		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFillForegroundColor(IndexedColors.RED.index);

		XSSFCellStyle headerStyle1 = workbook.createCellStyle();
		headerStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle1.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);

		String excelFilePath = FrameworkServices.getConfigProperties().getProperty("DailyReportFolder")
				+ FrameworkServices.getConfigProperties().getProperty("DailyReportFileName");
		XSSFSheet sheet = workbook.getSheet(sheetName);
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows()-1; i++) {
			XSSFCell cell1 = sheet.getRow(i).getCell(3, Row.CREATE_NULL_AS_BLANK);
			if (cell1.getStringCellValue().equalsIgnoreCase(scenarioId)) {
				String NewtimeStamp = String.valueOf(data);
				String[] NewtimeStamp1 = NewtimeStamp.split("\\.");
				String NewtimeStamp2 = NewtimeStamp1[0];
				int NewtimeStamp3 = Integer.parseInt(NewtimeStamp2);
				if (NewtimeStamp3 > 1) {
					timeStampcell= sheet.getRow(i).createCell(timeStamp);
					timeStampcell.setCellStyle(headerStyle1);

				} else {
					timeStampcell= sheet.getRow(i).createCell(timeStamp);
				}
				if (TestEngine.Failedscenariolist.contains(scenarioId))
					timeStampcell.setCellStyle(headerStyle);
				XSSFCell cell0 = sheet.getRow(i).createCell(0);
				XSSFCell cell2 = sheet.getRow(i).createCell(1);
				XSSFCell cell3 = sheet.getRow(i).createCell(2);
				// XSSFCell cell4 = sheet.getRow(i).createCell(5);
				cell0.setCellValue(module);
				cell2.setCellValue(subModule);
				cell3.setCellValue(Date);
				// cell4.setCellValue(Status);
				timeStampcell.setCellValue(data);
				
				updateAverageLogger(workbook, scenarioId, Status, data);
				FileOutputStream fos = new FileOutputStream(excelFilePath);
				workbook.write(fos);
				fos.close();
				break;
			}
		}
	}
	public void updateAverageLogger(XSSFWorkbook workbook, String scenarioId, String Status, double data) {

		XSSFSheet sheet = workbook.getSheet("AverageSummary");

		for (int i = 1; i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
			XSSFCell cell1 = sheet.getRow(i).getCell(1, Row.CREATE_NULL_AS_BLANK);
			if (cell1.getStringCellValue().equalsIgnoreCase(scenarioId)) {

				String resTime = sheet.getRow(i).getCell(2).getStringCellValue();
				double resTime3 = Double.valueOf(resTime);
				if (resTime3 < data) {
					String setValue3 = String.valueOf(data);
					sheet.getRow(i).getCell(2).setCellValue(setValue3);

				}

				String NewtimeStamp = String.valueOf(data);
				String[] NewtimeStamp1 = NewtimeStamp.split("\\.");
				String NewtimeStamp2 = NewtimeStamp1[0];
				int NewtimeStamp3 = Integer.parseInt(NewtimeStamp2);
				if (NewtimeStamp3 > 1) {
					String moreThanthree = sheet.getRow(i).getCell(3).getStringCellValue();
					int moreThan3 = Integer.valueOf(moreThanthree);
					int setValue = moreThan3 + 1;
					String setValue3 = String.valueOf(setValue);
					sheet.getRow(i).getCell(3).setCellValue(setValue3);

				} else {

				}
				if (TestEngine.Failedscenariolist.contains(scenarioId)) {
					String failedSec = sheet.getRow(i).getCell(4).getStringCellValue();
					int failedSce3 = Integer.valueOf(failedSec);
					int setValue = failedSce3 + 1;
					String setValue3 = String.valueOf(setValue);
					sheet.getRow(i).getCell(4).setCellValue(setValue3);

				}

			}
		}
	}
}
