package com.lic.epgs.gratuity.policy.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MasterPolicyContributionDetailsDto {
	private Long id;
	private Long policyId;
	private Long masterPolicyId;
	private String challanNo;
	private Double lifePremium;
	private Double gst;
	private Double pastService;
	private Double currentServices;
	private boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	private String entryType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date adjustmentForDate;
	private String financialYear;
	private Double pastServiceBalance;
	private Double currentServiceBalance;
	private Double stampDuty;
	
}
