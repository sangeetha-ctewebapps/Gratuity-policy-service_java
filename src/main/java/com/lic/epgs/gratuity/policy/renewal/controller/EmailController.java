package com.lic.epgs.gratuity.policy.renewal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.policy.renewal.dto.Email;
import com.lic.epgs.gratuity.policy.renewal.service.EmailService;

@RestController
@CrossOrigin("*")
@RequestMapping("api/email")
public class EmailController {

	@Autowired
	private EmailService emailService;

	// Sending a simple Email
	@PostMapping("/sendmail")
	public String sendMail(@RequestBody Email details) {
		String status = emailService.sendSimpleMail(details);

		return status;
	}
	
	// Sending email with attachment
	@PostMapping("/sendmailwithattachment")
	public String sendMailWithAttachment(@RequestBody Email details) {
		String status = emailService.sendMailWithAttachment(details);

		return status;
	}
	
}
