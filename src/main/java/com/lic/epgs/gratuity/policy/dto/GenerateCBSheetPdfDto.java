package com.lic.epgs.gratuity.policy.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateCBSheetPdfDto {
	

	private Date date;
	private String customerCode;
	private String customerName;

	private String annualRenewlDate;
	private String policyNumber;
	private String quotationNumber;
	private String licId;
	private String employeeNo;
	private String name;
	private String category;

	private String dob;
	private String dojToScheme;
	private Double pastServiceBenefit;
	private Double currentServiceBenefit; 
	private String doa;
	private Long age;
	private Double salary;
	private Long pastService;
	private Long totalService;
	private Double totalServiceGratuity;
	private Double pastServiceGratuity;
	private Double lifeCover;
	private Double lifeCoverPremium;
	private String proposalNumber;

}
