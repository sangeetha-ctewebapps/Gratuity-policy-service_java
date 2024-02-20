package com.lic.epgs.gratuity.policy.renewal.dto;

//Importing required classes
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Email {

	// Class data members
	private String recipient;
	private String msgBody;
	private String subject;
	private String attachment;
	private String attachmentrenewal;
	
	
     
}
