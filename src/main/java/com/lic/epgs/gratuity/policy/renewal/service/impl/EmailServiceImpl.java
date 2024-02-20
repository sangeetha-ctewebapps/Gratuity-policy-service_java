package com.lic.epgs.gratuity.policy.renewal.service.impl;

import java.io.File;

//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.policy.renewal.dto.Email;
import com.lic.epgs.gratuity.policy.renewal.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

//	@Autowired
//	private JavaMailSender javaMailSender;

//	@Value("${spring.mail.username}")
//	private String sender;

	@Override
	public String sendSimpleMail(Email details) {
		// Try block to check for exceptions
		try {

			// Creating a simple mail message
//			SimpleMailMessage mailMessage = new SimpleMailMessage();

			// Setting up necessary details
//			mailMessage.setFrom(sender);
//			mailMessage.setTo(details.getRecipient());
//			mailMessage.setText(details.getMsgBody());
//			mailMessage.setSubject(details.getSubject());

			// Sending the mail
//			javaMailSender.send(mailMessage);
			return "Mail Sent Successfully...";
		}

		// Catch block to handle the exceptions
		catch (Exception e) {
			return "Error while Sending Mail" + e;
		}

	}

	@Override
	public String sendMailWithAttachment(Email details) {

		// Creating a mime message
//		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//		MimeMessageHelper mimeMessageHelper;
//
//		try {
//
//			// Setting multipart as true for attachments to
//			// be send
//			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//			mimeMessageHelper.setFrom(sender);
//			mimeMessageHelper.setTo(details.getRecipient());
//			mimeMessageHelper.setText(details.getMsgBody());
//			mimeMessageHelper.setSubject(details.getSubject());
////			mimeMessageHelper.addAttachment(sender, null);
//			// Adding the attachment
//			FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
//
//			mimeMessageHelper.addAttachment(file.getFilename(), file);
//
//			FileSystemResource file1 = new FileSystemResource(new File(details.getAttachmentrenewal()));
//
//			mimeMessageHelper.addAttachment(file1.getFilename(), file1);
//
//			// Sending the mail
//			javaMailSender.send(mimeMessage);
			return "Mail sent Successfully";
		}

		// Catch block to handle MessagingException
//		catch (MessagingException e) {
//
//			// Display message when exception occurred
//			return "Error while sending mail!!!";
//		}
	

//}

	
	}



