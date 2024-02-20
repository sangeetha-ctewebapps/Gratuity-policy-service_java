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
public class MasterPolicyDto {
	
	private Long id;
	private Long masterpolicyHolder;
	private String policyNumber;
	private Date policyDispatchDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyRecievedDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyEndDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date annualRenewlDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date lastPremiumPaid;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")	
	private Float maximumStampFee;
	private Long totalMember;
	private Long policyStatusId;
	private Long policyTaggedStatusId;
	private Long productId;
	private String productName;
	private String productVariant;
	private String lineOfBusiness;
	private String unitId;
	private String unitCode;
	private String unitOffice;
	private String marketingOfficerCode;
	private String marketingOfficerName;
	private String intermediaryCode;
	private String intermediaryName;
	private String advanceOrArrears;
	private long contributtionFrequencyId;
//	private String rejectedReason;
//	private String rejectedRemarks;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long lastLicId;
	private String customerName;
	private String customerCode;
	private String industryType;
	private Long gstApplicableId;
	private Date proposalSignDate;
	private Date riskCommencementDate;
	
	private  Long payto;
	private Long productVariantId;
	private String proposalNumber;
	private Date nextDueDate;
	private Date dateOfCommencement;
	private Integer fclUptoAge;
	private Boolean midLeaverAllowed;
	private Boolean midJoinerAllowed;
	
}
