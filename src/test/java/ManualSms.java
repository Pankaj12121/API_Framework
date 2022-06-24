import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class ManualSms {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String uri="http://mimansa.angelbackoffice.com/SMSGateway/SMSGateway.aspx?user=PDTeam&password=angelPD2018&SentFrom=PDTeam&ServiceName=TestingTeamSMS&MobileNo=9920850142|8976521802|9892510527|7039879735|9821432952|8898960667|9920056662|9930268046|9870732280|7219202117|8806965644|9833632647|9920498992|7387099909|9004349925|8097316111|7021361835|8080350555|8652165746&Message=Due to technical reasons related to Auto Execution, API Test was failed which resulted to failure for all API's.";
		Response res = given().
				when().
				contentType(ContentType.JSON).get(uri);
		
		System.out.println("SMS Sent");
	}

}
