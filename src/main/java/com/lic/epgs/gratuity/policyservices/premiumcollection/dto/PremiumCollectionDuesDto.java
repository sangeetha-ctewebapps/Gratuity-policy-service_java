package com.lic.epgs.gratuity.policyservices.premiumcollection.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PremiumCollectionDuesDto {
	
	private Long id;
	

	private Long lcPremCollPropsId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date dueDate;
	
	private Double lcPremiumAmount;

	private Double gstOnLCPremium;

	private Double lateFee;

	private Double gstOnLateFee;

	private Double totalDueAmt;
	

	private Boolean isActive;
	

	private String createdBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date createdDate;
	

	private String modifiedBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date modifiedDate;
}
