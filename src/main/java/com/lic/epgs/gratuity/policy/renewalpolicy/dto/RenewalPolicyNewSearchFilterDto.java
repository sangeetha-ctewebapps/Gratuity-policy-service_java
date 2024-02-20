package com.lic.epgs.gratuity.policy.renewalpolicy.dto;

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
public class RenewalPolicyNewSearchFilterDto {
	
	private Long id;
	private String policyNumber;
	private String customerName;
	private String intermediaryName;
	private String lineOfBusiness;
	private String productName;
	private String productVariant;
	private String unitOffice;
	private Long policyTaggedStatusId;
	private Long policyStatusId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyEndDate;
	private Boolean isActive;
	
	
	

}
