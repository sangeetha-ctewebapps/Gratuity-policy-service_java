package com.lic.epgs.gratuity.policyservices.policymodification.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDto {

	public StatusDto(String Status) {
		this.Status = Status;
	}
	
	public StatusDto(String Status, String message) {
		this.Status = Status;
		this.message = message;
	}

	private String Status;
	private String message;
	private Date date = new Date();
}
