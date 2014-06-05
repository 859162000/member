package com.xcesys.extras.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.xcesys.extras.core.util.StringUtil;

public class EmailUtil {

	/**
	 * 发送简单文本
	 * 
	 * @param from
	 * @param pwd
	 * @param host
	 * @param to
	 * @param subject
	 * @param text
	 * @throws Exception
	 */
	public static void sendSimpleText(String from, String pwd, String host,
			String to, String subject, String text) throws Exception{

		if (StringUtil.isNullOrBlank(from) || StringUtil.isNullOrBlank(pwd)
				|| StringUtil.isNullOrBlank(host)
				|| StringUtil.isNullOrBlank(to)
				|| StringUtil.isNullOrBlank(subject)
				|| StringUtil.isNullOrBlank(text)) {
			throw new NullPointerException("所有参数都不能为空");
		}

		class MyAuthenticator extends Authenticator {
			String u = null;
			String p = null;
			public MyAuthenticator(String u, String p) {
				this.u = u;
				this.p = p;
			}
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(u, p);
			}
		}
		Authenticator authenticator = new MyAuthenticator(from, pwd);

		// create some properties and get the default Session
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "25");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", Boolean.TRUE);

		Session session = Session.getDefaultInstance(props, authenticator);
		session.setDebug(Boolean.TRUE);

		try {
			// create a message
			MimeMessage msg = new MimeMessage(session);
			Address fromaddr = new InternetAddress(from);
			msg.setFrom(fromaddr);
			Address toaddr = new InternetAddress(to);
			msg.setRecipient(Message.RecipientType.TO, toaddr);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			// If the desired charset is known, you can use
			// setText(text, charset)
			msg.setText(text);
			Transport.send(msg);
		} catch (MessagingException mex) {
			System.out.println("\n--Exception handling in msgsendsample.java");

			mex.printStackTrace();
			System.out.println();
			Exception ex = mex;
			do {
				if (ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException) ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if (invalid != null) {
						System.out.println("    ** Invalid Addresses");
						for (int i = 0; i < invalid.length; i++)
							System.out.println("         " + invalid[i]);
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if (validUnsent != null) {
						System.out.println("    ** ValidUnsent Addresses");
						for (int i = 0; i < validUnsent.length; i++)
							System.out.println("         " + validUnsent[i]);
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if (validSent != null) {
						System.out.println("    ** ValidSent Addresses");
						for (int i = 0; i < validSent.length; i++)
							System.out.println("         " + validSent[i]);
					}
				}
				System.out.println();
				if (ex instanceof MessagingException)
					ex = ((MessagingException) ex).getNextException();
				else
					ex = null;
			} while (ex != null);
		}
	}

	/**
	 * 发送包含附件
	 * 
	 * @param from
	 * @param pwd
	 * @param host
	 * @param to
	 * @param subject
	 * @param text
	 * @param attachment
	 * @throws Exception
	 */
	public static void sendContainAttachment(String from, String pwd, String host,
			String to, String subject, String text,File attachment) throws Exception{

		if (StringUtil.isNullOrBlank(from) || StringUtil.isNullOrBlank(pwd)
				|| StringUtil.isNullOrBlank(host)
				|| StringUtil.isNullOrBlank(to)
				|| StringUtil.isNullOrBlank(subject)
				|| StringUtil.isNullOrBlank(text)
				|| attachment == null) {
			throw new NullPointerException("所有参数都不能为空");
		}

		class MyAuthenticator extends Authenticator {
			String u = null;
			String p = null;
			public MyAuthenticator(String u, String p) {
				this.u = u;
				this.p = p;
			}
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(u, p);
			}
		}
		Authenticator authenticator = new MyAuthenticator(from, pwd);

		// create some properties and get the default Session
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "25");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", Boolean.TRUE);

		Session session = Session.getDefaultInstance(props, authenticator);
		session.setDebug(Boolean.TRUE);
		StringBuilder errmsg = new StringBuilder();
		try {
			// create a message
			MimeMessage msg = new MimeMessage(session);
			Address fromaddr = new InternetAddress(from);
			msg.setFrom(fromaddr);
			Address toaddr = new InternetAddress(to);
			msg.setRecipient(Message.RecipientType.TO, toaddr);
			sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();  
			msg.setSubject("=?uft8?B?"+enc.encode(subject.getBytes("GBK"))+"?=");
//			msg.setSubject(subject);
			msg.setSentDate(new Date());
			
			// Attach the specified file.
			// We need a multipart message to hold the attachment.
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(text,"GBK");
			MimeBodyPart mbp2 = new MimeBodyPart();
			mbp2.attachFile(attachment);
			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);
			msg.setContent(mp);
			
			Transport.send(msg);
		} catch (MessagingException mex) {
			errmsg.append(mex.getLocalizedMessage());
			mex.printStackTrace();
			System.out.println();
			Exception ex = mex;
			do {
				if (ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException) ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if (invalid != null) {
						errmsg.append("    ** 无效地址");
						for (int i = 0; i < invalid.length; i++)
							errmsg.append("         " + invalid[i]);
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if (validUnsent != null) {
						errmsg.append("    ** 有效但未发送成功的地址");
						for (int i = 0; i < validUnsent.length; i++)
							errmsg.append("         " + validUnsent[i]);
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if (validSent != null) {
						errmsg.append("    ** ValidSent Addresses");
						for (int i = 0; i < validSent.length; i++)
							System.out.println("         " + validSent[i]);
					}
				}
				System.out.println();
				if (ex instanceof MessagingException){
					ex = ((MessagingException) ex).getNextException();
					errmsg.append(ex.getLocalizedMessage());
				}else
					ex = null;
			} while (ex != null);
			if(errmsg.length() > 0){
				throw new Exception(errmsg.toString());
			}
		}
	}
	public static void main(String[] args) {
		try {
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("C:/阿斯蒂芬.csv")),"GBK"));
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 3; j++) {
					fw.write(new String("测试java文件操作,"));
				}
				fw.write("\r\n");
				fw.flush();
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
