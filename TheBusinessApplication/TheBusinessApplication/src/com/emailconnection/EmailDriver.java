package com.emailconnection;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailDriver {

	public static void sendEmail(String recepient, String subjectOfEmail, String textOfEmail)
	{
		System.out.println("Preparing to send email to admin...");
		Properties properties = new Properties();
		
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		
		String email = "thebusinessapp2021@gmail.com";
		String password = "theapp1234";
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});
		
		Message message = prepareMessage(session, email, recepient, subjectOfEmail, textOfEmail);
	
		try {
			Transport.send(message);
			System.out.println("Email sent successfully");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static Message prepareMessage(Session session, String email, String recepient, String subjectOfEmail, String textOfEmail) {	
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject(subjectOfEmail);
			message.setText(textOfEmail);
			
			return message;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
