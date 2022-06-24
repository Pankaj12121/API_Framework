package com.API.Reporter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.AB_API.TestEngine;
import com.AB_API.TestRunner;

public class CustomReporter implements IReporter {
	public int passCount,failCount,skipCount,totPass=0,totSkip=0;
	public static int totFail;
	private static final String emailableReportTemplateFile ="D:/AB_API_Automation/API/src/main/java/com/API/Reporter/customize-emailable-report-template.html";
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		//Iterating over each suite included in the test
		for (ISuite suite : suites) {
			// Following code gets the suite name
			String suiteName = suite.getName();
			// Getting the results for the said suite
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (ISuiteResult sr : suiteResults.values()) {
				ITestContext tc = sr.getTestContext();
				String testCaseString = tc.getCurrentXmlTest().toString();
				String[] testCaseAttributes = testCaseString.split(":");
				String[] testRunning = testCaseAttributes[1].split(" ");
				String aTest = testRunning[1];
				String TestCaseToMatch = aTest.replace("\"", "");

				passCount = tc.getPassedTests().getAllResults().size();
				if (passCount > 0)
					totPass++;
				failCount = tc.getFailedTests().getAllResults().size();
				if (failCount > 0) {
					TestEngine.Failedscenariolist.add(TestCaseToMatch);
					totFail++;
				}
				skipCount = tc.getSkippedTests().getAllResults().size();
				if (skipCount > 0)
					totSkip++;

			}
		}
	}
}