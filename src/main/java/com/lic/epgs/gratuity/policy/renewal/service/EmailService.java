package com.lic.epgs.gratuity.policy.renewal.service;

import com.lic.epgs.gratuity.policy.renewal.dto.Email;

public interface EmailService {
	// Method
    // To send a simple email
    String sendSimpleMail(Email details);
 
    // Method
    // To send an email with attachment
    String sendMailWithAttachment(Email details);
	/*
	 * String sendSimpleMailPolicy(Email details, Long id);
	 */
}
