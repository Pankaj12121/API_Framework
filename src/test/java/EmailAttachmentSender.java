import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailAttachmentSender {

	public static void sendEmailWithAttachments(String host, String port,
			final String userName, final String password, String toAddress,
			String subject, String message, String[] attachFiles)
					throws AddressException, MessagingException {

		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(properties, auth);
		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(userName));
		InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		if (attachFiles != null && attachFiles.length > 0) {
			for (String filePath1 : attachFiles) {
				MimeBodyPart attachPart = new MimeBodyPart();
				try {
					attachPart.attachFile(filePath1);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				multipart.addBodyPart(attachPart);
			}
		}
		msg.setContent(multipart);
		//java.net.URL classUrl = this.getClass().getResource("com.sun.mail.util.TraceInputStream");
		//System.out.println(classUrl.getFile());
		Transport.send(msg);
	}
	public static void main(String[] args) {

		String host = "smtp.gmail.com";
		String port = "465";
		String mailFrom = "ABMA.API.Automation@angelbroking.com";
		String password = "angel@123";
		Date date = new Date();  
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh.mm");  
		String strDate= formatter.format(date); 

		String mailTo = "rajesh.devakate@aqmtechnologies.com";
		String subject = "Test Mail: Kindly Ignore "+strDate;
		String message1 = "Hello Everyone,";
		String message2="Please find attached API Automation Test Result for Scheduled Run"; 
		String message3="Note: In order to view report, first download the same and then open the report.";  
		String message4="Kindly revert in case of any assistance required.";

		String message=message1+System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+message2+System.lineSeparator()+message3+System.lineSeparator()+message4;
				String[] attachFiles = new String[1];
		attachFiles[0] = "D:\\AB_API_Automation\\API\\test-output\\emailable-report.html";
		/* attachFiles[1] = "e:/Test/Music.mp3";
        attachFiles[2] = "e:/Test/Video.mp4";*/
		try {
			sendEmailWithAttachments(host, port, mailFrom, password, mailTo,
					subject, message, attachFiles);
			System.out.println("Email sent.");
		} catch (Exception ex) {
			System.out.println("Could not send email.");
			ex.printStackTrace();
		}
	}
	
	
}