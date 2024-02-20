package com.lic.epgs.gratuity.policyservices.conversion.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CheckActiveQuatation {

	
	
	
	public CheckActiveQuatation(Boolean activeQuatationPresent, String quotationNumber, String message) {
		super();
		this.activeQuatationPresent = activeQuatationPresent;
		this.quotationNumber = quotationNumber;
		this.message = message;
	}
	private Boolean activeQuatationPresent;
	private String quotationNumber;
	private String message;

}
