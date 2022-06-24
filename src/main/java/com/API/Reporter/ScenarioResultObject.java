package com.API.Reporter;

public class ScenarioResultObject {
	
	private String Module;
	private String SubModule;
	private String SuiteName;
	private String Date;
	private String Time;
	private String Test;
	private String Pass;
	private String Fail;
	private double TotalTime;
	private String Analysis;
	private String errorResponse;
	private String APIURL;
	private int DelayedResponse;
	
	public String getModule() {
		return Module;
	}
	public void setModule(String module) {
		Module = module;
	}
	public String getSubModule() {
		return SubModule;
	}
	public void setSubModule(String subModule) {
		SubModule = subModule;
	}
	public String getSuiteName() {
		return SuiteName;
	}
	public void setSuiteName(String suiteName) {
		SuiteName = suiteName;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getTest() {
		return Test;
	}
	public void setTest(String test) {
		Test = test;
	}
	public String getPass() {
		return Pass;
	}
	public void setPass(String pass) {
		Pass = pass;
	}
	public String getFail() {
		return Fail;
	}
	public void setFail(String fail) {
		Fail = fail;
	}
	public double getTotalTime() {
		return TotalTime;
	}
	public void setTotalTime(double elapsed) {
		TotalTime = elapsed;
	}
	public String getAnalysis() {
		return Analysis;
	}
	public void setAnalysis(String analysis) {
		Analysis = analysis;
	}
	public String getErrorResponse() {
		return errorResponse;
	}
	public void setErrorResponse(String errorResponse) {
		this.errorResponse = errorResponse;
	}
	public String getAPIURL() {
		return APIURL;
	}
	public void setAPIURL(String aPIURL) {
		APIURL = aPIURL;
	}
	public int getDelayedResponse() {
		return DelayedResponse;
	}
	public void setDelayedResponse(int delayedResponse) {
		DelayedResponse = delayedResponse;
	}
	
	
	

}
