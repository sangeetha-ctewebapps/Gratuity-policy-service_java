package com.lic.epgs.gratuity.policyservices.dom.dto;



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
public class PmstMidleaverMemberPropsDto {  

	private Long id;
	private Long tmpMemberId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date leavingDate;
	private Long leavingReasonId;
	private String leavingReasonOther;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date lastPremiumCollectedDate;
	private Double lastPremiumCollectedAmount;
	private Double refundPremiumAmount;
	private Double refundGstAmount;
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	
}
