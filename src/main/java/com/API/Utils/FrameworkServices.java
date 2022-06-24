package com.API.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.API.TestManagementPOJO.MasterScriptPOJO;
import com.API.TestManagementPOJO.TestScenariosPOJO;

public class FrameworkServices {
	static Properties configProp;
	static FileInputStream configInput;
	static Properties tableConfigProp;
	static FileInputStream tableConfigInput;
	static Properties serviceConfigProp;
	static FileInputStream serviceConfigInput;
	static Properties dataProp;
	static FileInputStream dataInput;
	//private XSSFWorkbook workbook;
	public static Properties getConfigProperties() throws FileNotFoundException, IOException {
		if (configProp==null) {
			configProp = new Properties();
			configInput = new FileInputStream("./config.properties");
			configProp.load(configInput);
		}
		return configProp;
	}
	
	public static Properties getDataProperties() throws FileNotFoundException, IOException {
		if (dataProp==null) {
			dataProp = new Properties();
			dataInput = new FileInputStream("./data.properties");
			dataProp.load(dataInput);
		}
		return dataProp;
	}
	public static Properties getTableConfigProperties() throws FileNotFoundException, IOException {
		if (tableConfigProp==null) {
			tableConfigProp = new Properties();
			tableConfigInput = new FileInputStream("./MasterStructure.properties");
			tableConfigProp.load(tableConfigInput);
		}
		return tableConfigProp;
	}
	public static Properties getServiceConfigProperties() throws FileNotFoundException, IOException {
		if (serviceConfigProp==null) {
			serviceConfigProp = new Properties();
			serviceConfigInput = new FileInputStream("./Service.properties");
			serviceConfigProp.load(serviceConfigInput);
		}
		return tableConfigProp;
	}

	public List<TestScenariosPOJO> getTestToBeExecuted() throws FileNotFoundException, IOException {
		List<TestScenariosPOJO> scenariosToBeExecuted=new ArrayList<TestScenariosPOJO>();
		FileInputStream fileInputStream=new FileInputStream(new File(getConfigProperties().getProperty("TestDataFolder")+ getConfigProperties().getProperty("TestScenarioFileLocation")));
		ExcelOperation.isTestSuiteExcelCorrupted();
		XSSFWorkbook workbook = new  XSSFWorkbook (fileInputStream);
		XSSFSheet testScenarioSheet=workbook.getSheet("TestScenarios");
		//	Iterator<Row> testScenarioIterator = testScenarioSheet.iterator();
		getTableConfigProperties();
		int rowCount=testScenarioSheet.getPhysicalNumberOfRows()-1;
		/*
		 * Column number
		 * 
		 */
		int Reference=Integer.parseInt(tableConfigProp.getProperty("Reference"));
		int Description= Integer.parseInt(tableConfigProp.getProperty("Description"));
		int AutomationScriptReference= Integer.parseInt(tableConfigProp.getProperty("AutomationScriptReferenceForTestScenarios"));
		int SerialNumber= Integer.parseInt(tableConfigProp.getProperty("SerialNumber"));
		int ExecuteTestScenario=Integer.parseInt(tableConfigProp.getProperty("ExecuteTestScenario"));
		int ExecutionStatus= Integer.parseInt(tableConfigProp.getProperty("ExecutionStatus"));
		int AutomationScripterName= Integer.parseInt(tableConfigProp.getProperty("AutomationScripterName"));
		int Owner= Integer.parseInt(tableConfigProp.getProperty("Owner"));
		int Email= Integer.parseInt(tableConfigProp.getProperty("Email"));
		int Contact= Integer.parseInt(tableConfigProp.getProperty("Contact"));
		int Remarks= Integer.parseInt(tableConfigProp.getProperty("Remarks"));
		for(int i=1;i<=rowCount;i++) {
			TestScenariosPOJO scenariosPOJO=new TestScenariosPOJO();
			Row rowData=testScenarioSheet.getRow(i);
			if(rowData.getCell(ExecuteTestScenario).getStringCellValue().equalsIgnoreCase("Yes")) {
				scenariosPOJO.setReference(rowData.getCell(Reference).getStringCellValue());
				//scenariosPOJO.setPrerequisiteTestSuiteReference(rowData.getCell(PrerequisiteTestScenarioReference).getStringCellValue());
				//scenariosPOJO.setPrerequisiteTestScenarioReference(rowData.getCell(PrerequisiteTestScenarioReference).getStringCellValue());
				scenariosPOJO.setDescription(rowData.getCell(Description).getStringCellValue());	
				scenariosPOJO.setAutomationScriptReference(rowData.getCell(AutomationScriptReference).getStringCellValue());
				scenariosPOJO.setSerialNumber(rowData.getCell(SerialNumber).getStringCellValue());
				scenariosPOJO.setExecuteTestScenario(rowData.getCell(ExecuteTestScenario).getStringCellValue());
				scenariosPOJO.setAutomationScripterName(rowData.getCell(AutomationScripterName).getStringCellValue());
				scenariosPOJO.setOwner(rowData.getCell(Owner).getStringCellValue());
				scenariosPOJO.setEmail(rowData.getCell(Email).getStringCellValue());
				scenariosPOJO.setContact(rowData.getCell(Contact).getStringCellValue());
				scenariosToBeExecuted.add(scenariosPOJO);
			}
		}
		System.out.println(scenariosToBeExecuted);
		return scenariosToBeExecuted;

	}
	public List<MasterScriptPOJO> getMasterScript(String ScriptReference) throws FileNotFoundException, IOException{
		List<MasterScriptPOJO> masterScriptStepList=new ArrayList<MasterScriptPOJO>();
		FileInputStream fileInputStream=new FileInputStream(new File(getConfigProperties().getProperty("TestDataFolder")+"\\TestSuite\\API_Testsuite.xlsx"));
		XSSFWorkbook workbook = new  XSSFWorkbook (fileInputStream);
		XSSFSheet masterScriptSheet=workbook.getSheet("MasterScripts");
		//	Iterator<Row> testScenarioIterator = testScenarioSheet.iterator();
		getTableConfigProperties();
		int rowCount=masterScriptSheet.getPhysicalNumberOfRows()-1;
		/*
		 * Column number
		 * 
		 */
		int Reference=Integer.parseInt(tableConfigProp.getProperty("Reference"));
		int AutomationScriptReference =Integer.parseInt(tableConfigProp.getProperty("AutomationScriptReference"));
		int StepKeyword =Integer.parseInt(tableConfigProp.getProperty("StepKeyword"));
		int ConfigData =Integer.parseInt(tableConfigProp.getProperty("ConfigData"));
		int StepGroup =Integer.parseInt(tableConfigProp.getProperty("StepGroup"));
		int SerialNumber =Integer.parseInt(tableConfigProp.getProperty("SerialNumber"));
		int ExecutionSequence =Integer.parseInt(tableConfigProp.getProperty("ExecutionSequence"));
		int SkipStep =Integer.parseInt(tableConfigProp.getProperty("SkipStep"));
		int ToBeExecutedByMachine =Integer.parseInt(tableConfigProp.getProperty("ToBeExecutedByMachine"));
		for(int i=1;i<=rowCount;i++) {
			MasterScriptPOJO masterScriptStep=new MasterScriptPOJO();
			Row rowData=masterScriptSheet.getRow(i);
			if(rowData.getCell(AutomationScriptReference).getStringCellValue().equalsIgnoreCase(ScriptReference)) {
				masterScriptStep.setReference(rowData.getCell(Reference).getStringCellValue());
				masterScriptStep.setAutomationscriptreference(rowData.getCell(AutomationScriptReference).getStringCellValue());
				masterScriptStep.setStepkeyword(rowData.getCell(StepKeyword).getStringCellValue());
				masterScriptStep.setConfigdata(rowData.getCell(ConfigData).getStringCellValue());
				masterScriptStep.setStepgroup(rowData.getCell(StepGroup).getStringCellValue());
				masterScriptStep.setSerialnumber(rowData.getCell(SerialNumber).getStringCellValue());
				masterScriptStep.setExecutionsequence(rowData.getCell(ExecutionSequence).getStringCellValue());
				masterScriptStep.setSkipstep(rowData.getCell(SkipStep).getStringCellValue());
				masterScriptStep.setTobeexecutedbymachine(rowData.getCell(ToBeExecutedByMachine).getStringCellValue());
				
				masterScriptStepList.add(masterScriptStep);
			}
		}
		System.out.println(masterScriptStepList);
		return masterScriptStepList;

	}
	public static void main(String[] args) throws FileNotFoundException, IOException {
		FrameworkServices frameworkServices=new FrameworkServices();
		frameworkServices.getTestToBeExecuted();
	}
}
