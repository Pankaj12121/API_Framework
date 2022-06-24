import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class Sample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String res="?xml version=1.0 encoding=UTF-8?PGRESPONSEPG UID=A81764 GID=HO PGNO=B01216000304 TRNO= AMT=100.00 STATUS=SUCCESS DESC= PG UID=A34430".trim();
		String[] id=res.split(Pattern.quote("="));
		//String[] id=res.split(Pattern.quote("$"));
		String UserCode=id[8];
		System.out.println(UserCode);
		String UserCode1=id[15].split("=")[1];
		System.out.println(UserCode1);
		String UserCode11=id[17].split("=")[1];
		System.out.println(UserCode11);
		String UserCode111=id[18].split("=")[1];
		System.out.println(UserCode111);
		String sessionID=res.split(Pattern.quote("|"))[3].split("=")[1];
		//System.out.println("Id:"+id[3]);
		//String actualId=id[3].split("=")[1];
		System.out.println("Session ID:"+sessionID);
		
		String a="<DocumentElement />";
		String b=a.replaceAll("<", "").replaceAll(">", "").replaceAll("/", "");
		System.out.println(b);
		System.out.println(buyValue("5.65"));
		System.out.println(sellValue("5.65"));
		
	}
	
	public static double buyValue(String ltp) {
		double lt=Double.parseDouble(ltp);	
		double per=lt*2/100;
		double buyPrice=(lt-per)*10;
		double roundOff= Math.round(buyPrice);
		double FinalBuyPrice=roundOff/10;
		return FinalBuyPrice;
		
	}
	
	public static double sellValue(String ltp) {
		double lt=Double.parseDouble(ltp);	
		double per=lt*2/100;
		double sellPrice=(lt+per)*10;
		double roundOff = Math.round(sellPrice);
		double FinalSellPrice=roundOff/10;
		return FinalSellPrice;
	}
	public static String enc_name() {
	try {

		String url = "<![CDATA[ <IMG SRC=\"  javascript:document.vulnerable=true;\"> ]]>";

		String encodedUrl = URLEncoder.encode(url, "UTF-8");

		System.out.println("Encoded URL " + encodedUrl);

		String decodedUrl = URLDecoder.decode(url, "UTF-8");

		System.out.println("Dncoded URL " + decodedUrl);

		} catch (UnsupportedEncodingException e) {

			System.err.println(e);

		}
	return null;
	}
}



