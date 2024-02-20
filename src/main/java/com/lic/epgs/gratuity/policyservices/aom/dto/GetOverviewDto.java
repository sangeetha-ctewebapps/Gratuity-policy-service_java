package com.lic.epgs.gratuity.policyservices.aom.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class GetOverviewDto {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date quotationDate;

	private String quotationNumber;

	private String quotationStatus;

}
