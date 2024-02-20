package com.lic.epgs.gratuity.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HSNCodeDto {

	private String hsnCode;
	private double cgstRate;
	private double sgstRate;
	private double igstRate;
	private double utgstRate;
	private String description;
	
	
}
