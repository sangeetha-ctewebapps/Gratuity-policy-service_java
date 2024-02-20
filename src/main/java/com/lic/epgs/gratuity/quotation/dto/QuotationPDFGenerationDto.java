package com.lic.epgs.gratuity.quotation.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationPDFGenerationDto {
	
	private Long totalMember;
	private Long pastServiceBenefit;
	private Long currentServiceBenefit;
	private Float averageAge;
	private Float avgMonthlySalary;
	private Float avgPastService;
	private String totalServiceGratuity;
	private Long accuredGratuity;
	private Long lcas;
	private Long lcPremium;
	private Long gst;
	private String mphName;
	private String mphEmail;
	private String mphAddress1;
	private String mphAddress2;
	private String mphAddress3;
	private String address1;
	private String address2;
	private String address3;
	private String describtion;
	private String cityName;
	private String stateName;
	private String pinCode;
	private String mphAddressType;
	private Long mphMobileNo;
	private Float salaryEscalation;
	private Float discountRate;
	private String mortalityRate;
	private String minandmaxRate;
	private String email;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfApproval ;
	private String proposalNumber;
	private String quotationNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfCommencement;
	
	private List<BenefitValuationDto> benefitValuation;
	
	private CommonMasterUnitEntity commonMasterUnitEntity;
	
	
	
	
}
