package com.lic.epgs.gratuity.policy.renewalpolicy.dto;

import java.util.Date;

import javax.persistence.Column;

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
public class RenewalQuotationSearchDTo {
	
	
	private String policyNumber;
	private Long serviceId;
	private String customerName;
	private String intermediaryName;
	private Long lineOfBusiness;
	private Long productId;
	private Long productVariant;
	private Long unitOffice;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyEndDate;
	private String quotationNumber;
	
	private Long policytaggedStatusId;
	private Long quotationStatusId;
	private Long quotationTaggedStatusId;
	private Long quotationSubStatusId;
	private String userName;
	private String unitCode;
	private String userType;
	private Long policyStatusId;
	private Long policySubStatusId;
	private Long[] statusIdList;
}
