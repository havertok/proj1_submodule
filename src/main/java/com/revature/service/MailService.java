package com.revature.service;

import java.util.Date;
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

public class MailService {

	public void sendPlainTextEmail(String host, String port, String username, String password, String toAddress,
			String subject, String message) throws AddressException, MessagingException{
		// Despite being named the same, these are from the javax.mail package.
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};

		Session session = Session.getInstance(properties, auth);
		//System.out.println("from: "+username+" to:"+toAddress+"subject "+subject+"\nmessage:"+message);
        Message msg = new MimeMessage(session);
        
        msg.setFrom(new InternetAddress(username));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(message);
 
        // sends the e-mail
        Transport.send(msg);
	}
	
//    public static void main(String[] args) {
//        // SMTP server information
//        String host = "smtp.gmail.com";
//        String port = "587";
//        String mailFrom = "edward.rob.owen";
//        String password = "Banhammer732!";
// 
//        // outgoing message information
//        String mailTo = "e.owen@zoho.com";
//        String subject = "Hello my friend";
//        String message = "Hi guy, Hope you are doing well. Duke.";
//        
//        MailService mailer = new MailService();
// 
//        try {
//            mailer.sendPlainTextEmail(host, port, mailFrom, password, mailTo,
//                    subject, message);
//            System.out.println("Email sent.");
//        } catch (Exception ex) {
//            System.out.println("Failed to sent email.");
//            ex.printStackTrace();
//        }
//    }

}
