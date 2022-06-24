package com.AB_API;

import java.awt.Color;
import java.io.FileNotFoundException;
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

import com.API.Utils.FrameworkServices;

public class EmailSender {
	public static String[] emailList = {};
	public static String subject="";
	public static String Path;
	//public static String[] attachFiles;
	public static void sendEmailWithAttachments(final String userName, final String password, String filePath,String EmailSubject)
			throws AddressException, MessagingException, FileNotFoundException, IOException {
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");  
		String strDate= formatter.format(TestEngine.date); 

		
		subject = EmailSubject+strDate;
		String message =addColor("Hello Everyone,<p/>Greetings for the Day...!", Color.BLACK);
		message +=addColor("<p/>Please find attached <strong>API Automation Test Failed Report</strong> as per the scheduled execution.<br></br>In order to view the report, you must first download the same.<p/><strong>NOTE:</strong><p/>1. We have implemented<strong> \"API Response Time:\" </strong>Tag for each APIs in Response Section. The reason for implementing is because the time shown in the Result Grid increases when the CPU Usage or other JAVA factors result to more consumption of Memory.<p/>2. If \"<strong>API Response Time:</strong>\" is below 3 Seconds than it shall be displayed in ", Color.BLACK);
		message +=addColor( "\"<strong>GREEN</strong>\" " , Color.GREEN);
		message +=addColor("Font and for more than 3 seconds ", Color.BLACK);
		message +=addColor( "\"<strong>RED</strong>\" " , Color.RED);
		message +=addColor("Font.<br>", Color.BLACK);
		message +=addColor("<br>3. Implemented<strong> \"API Request Time\" </strong> for development team to find out the solution for the API failures by refering to logs for the specific time failure.<br>", Color.BLACK);
		message +=addColor("<br>4. As discussed in review meeting Physical Holdings APIs were failing due to unavailability of \"SCHEME NAME\" & \"DATA SOURCE\". We have currently skipped validating untill fixed hence it is passed.<br>", Color.RED);
		message +=addColor("<p/>Kindly revert at kunal.kotadia@angelbroking.com in case of any queries/concerns...",Color.BLACK);
		message	+=addColor("<p/><br></br><strong>Thanks & Regards,<br></br>Automation Team</strong>", Color.BLACK);
		message +=addColor("<p/><i>\"Innovative Testing Solutions for Quality Improvement & User Experience\"</i>", Color.red);
		Session session = Session.getInstance(properties, auth);
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(userName));
		
		emailList = getEmailIDs(FrameworkServices.getConfigProperties().getProperty("EmailToList"));
		InternetAddress[] toAddresses = new InternetAddress[emailList.length];
	    for (int i = 0; i < emailList.length; i++) {
	    	toAddresses[i] = new InternetAddress(emailList[i]);
	    }
	    msg.setRecipients(Message.RecipientType.TO, toAddresses);
	    
		emailList = getEmailIDs(FrameworkServices.getConfigProperties().getProperty("EmailCCList"));
		InternetAddress[] ccAddresses = new InternetAddress[emailList.length];
	    for (int i = 0; i < emailList.length; i++) {
	    	ccAddresses[i] = new InternetAddress(emailList[i]);
	    }
		
		msg.setRecipients(Message.RecipientType.CC, ccAddresses);
		
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html; charset=utf-8");
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		Path=filePath+"\\emailable-report.html";
		String[] attachFiles = new String[1];
		attachFiles[0] = filePath+"\\emailable-report.html";	
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
		Transport.send(msg);
		System.out.println("Email Sent");
	}
	public static void sendEmailWithAttachments(final String userName, final String password, String filePath,String EmailSubject,String TimeRange)
			throws AddressException, MessagingException, FileNotFoundException, IOException {
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");  
		String strDate= formatter.format(TestEngine.date); 


		subject = EmailSubject + strDate + " && TimeRange " + TimeRange;
		String message = addColor("Hello Everyone,<p/>Greetings for the Day...!", Color.BLACK);
		message += addColor(
				"<p/>Please find attached <strong>'Daily_Automation_Report.xlsx' </strong> excel for the time range::  "+TimeRange,
				Color.BLACK);

		message += addColor("<p/>Please follow following instructions to analyze the report",
				Color.BLACK);

		message += addColor("Font.<br> 1. Download excel and open it", Color.BLACK);
		message += addColor(
				"<br>2. Click on 'Heckyl API' sheet.<br>",Color.BLACK);
		message += addColor(
				"<br>3. Header Row of the sheet have timeStamps of testsuite run.<br>",Color.BLACK);
		message += addColor(
				"<br>4. Before timeStamp, a time range mentioned which will give idea on log collected duration.<br>",Color.BLACK);
		message += addColor(
				"<br>5. Failed test case will be highlighted with red color.<br>",Color.BLACK);
		
		message += addColor(
				"<br>6. Report will be generated and distributed within market hours only.<br>",Color.BLACK);
		
		message += addColor(
				"<br>7. Report distribution timeslot will be 10:30, 12, 2 and 3:50 pm .<br>",Color.BLACK);
		
		message += addColor(
				"<br>8. Report will be refreshed everyday at 9 am and previous days report will be archieved.<br>",Color.BLACK);
		
		message += addColor("<p/>Kindly revert at kunal.kotadia@angelbroking.com in case of any queries/concerns...",
				Color.BLACK);
		message += addColor("<p/><br></br><strong>Thanks & Regards,<br></br>Automation Team</strong>", Color.BLACK);
		message += addColor("<p/><i>\"Innovative Testing Solutions for Quality Improvement & User Experience\"</i>",
				Color.red);
		Session session = Session.getInstance(properties, auth);
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(userName));

		emailList = getEmailIDs(FrameworkServices.getConfigProperties().getProperty("AnalysisEmailToList"));
		InternetAddress[] toAddresses = new InternetAddress[emailList.length];
		for (int i = 0; i < emailList.length; i++) {
			toAddresses[i] = new InternetAddress(emailList[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, toAddresses);

		emailList = getEmailIDs(FrameworkServices.getConfigProperties().getProperty("AnalysisEmailCCList"));
		InternetAddress[] ccAddresses = new InternetAddress[emailList.length];
		for (int i = 0; i < emailList.length; i++) {
			ccAddresses[i] = new InternetAddress(emailList[i]);
		}

		msg.setRecipients(Message.RecipientType.CC, ccAddresses);

		msg.setSubject(subject);
		msg.setSentDate(new Date());
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html; charset=utf-8");
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		Path=filePath;
		String[] attachFiles = new String[1];
		attachFiles[0] = filePath;	
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
		Transport.send(msg);
		System.out.println("Email Sent");
	}
	public static String addColor(String msg, Color color) {
		String hexColor = String.format("#%06X",  (0xFFFFFF & color.getRGB()));
		String colorMsg = "<FONT COLOR=\"#" + hexColor + "\">" + msg + "</FONT>";
		return colorMsg;
	}

	public static String[] getEmailIDs(String email) {
		String[] emailList = email.split(",");
		for(int i=0; i < emailList.length; i++) {
			emailList[i] = emailList[i].trim();
		}		
		return emailList;
	}
	
	public static void sendEmailForSingleScenario(final String userName, final String password, String filePath, String EmailSubject)
			throws AddressException, MessagingException, FileNotFoundException, IOException {
		// sets SMTP server properties
		if(TestRunner.runningScenarioName!=null) {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");  
		String strDate= formatter.format(TestEngine.date); 

		
		subject = EmailSubject+strDate;
		String message =addColor("Hello Everyone,<p/>Greetings for the Day...!", Color.BLACK);
		message +=addColor("<p/>Please find attached <strong>API Automation Test Failed Report</strong> as per the scheduled execution.<br></br>In order to view the report, you must first download the same.<p/><strong>NOTE:</strong><p/>", Color.BLACK);
		
		message +=addColor("<br> This email is triggered as API with valid inputs but have not received any response,  please log incident for this test " +TestRunner.runningScenarioName+" if further inputs required please revert back to us.<br>", Color.RED);
		message +=addColor("<p/>Kindly revert at kunal.kotadia@angelbroking.com in case of any queries/concerns...",Color.BLACK);
		message	+=addColor("<p/><br></br><strong>Thanks & Regards,<br></br>Automation Team</strong>", Color.BLACK);
		message +=addColor("<p/><i>\"Innovative Testing Solutions for Quality Improvement & User Experience\"</i>", Color.red);
		Session session = Session.getInstance(properties, auth);
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(userName));
		
		emailList = getEmailIDs(FrameworkServices.getConfigProperties().getProperty("EmailToNTList"));
		InternetAddress[] toAddresses = new InternetAddress[emailList.length];
	    for (int i = 0; i < emailList.length; i++) {
	    	toAddresses[i] = new InternetAddress(emailList[i]);
	    }
	    msg.setRecipients(Message.RecipientType.TO, toAddresses);
	    
		emailList = getEmailIDs(FrameworkServices.getConfigProperties().getProperty("EmailCCNTList"));
		InternetAddress[] ccAddresses = new InternetAddress[emailList.length];
	    for (int i = 0; i < emailList.length; i++) {
	    	ccAddresses[i] = new InternetAddress(emailList[i]);
	    }
		
		msg.setRecipients(Message.RecipientType.CC, ccAddresses);
		
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html; charset=utf-8");
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		Path=filePath;
		String[] attachFiles = new String[1];
		attachFiles[0] = filePath;	
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
		Transport.send(msg);
		System.out.println("Single 'No response Received' failed scenario Email Sent");
		}
	}


}