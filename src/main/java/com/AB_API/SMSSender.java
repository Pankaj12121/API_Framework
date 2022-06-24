package com.AB_API;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.API.Utils.FrameworkServices;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
//----------------------------------------------------------Heckyl---------------------------------------
public class SMSSender {
	public static void sendSMS(String smsSubject) throws FileNotFoundException, IOException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm"); 
		String strDate= formatter.format(TestEngine.date);
		
		String smsSub=smsSubject+strDate+" Kindly check your email for detailed report or tap on Link to view Report. "+Bitly.bitlyUrl+" - Regards, Automation Team.";
		String mobileNos=FrameworkServices.getConfigProperties().getProperty("MobileNumbers");
		String uri="http://mimansa.angelbackoffice.com/SMSGateway/SMSGateway.aspx?user=PDTeam&password=angelPD2018&SentFrom=PDTeam&ServiceName=TestingTeamSMS&MobileNo="+mobileNos+"&Message="+smsSub;
		
		Response res = given().
				when().
				contentType(ContentType.JSON).get(uri);
		
		System.out.println("SMS Sent");
	}
	
public static void sendSMSExcelCorrupt() throws FileNotFoundException, IOException {
		
		String smsSub=EmailSender.subject+" Automation Report Excel is corrupted. "+Bitly.bitlyUrl+" - Regards, Automation Team.";
		String mobileNos=FrameworkServices.getConfigProperties().getProperty("ExcelCorruptMobileNumbers");
		String uri="http://mimansa.angelbackoffice.com/SMSGateway/SMSGateway.aspx?user=PDTeam&password=angelPD2018&SentFrom=PDTeam&ServiceName=TestingTeamSMS&MobileNo="+mobileNos+"&Message="+smsSub;
		
		Response res = given().
				when().
				contentType(ContentType.JSON).get(uri);
		
		System.out.println("SMS Sent");
	}
}
