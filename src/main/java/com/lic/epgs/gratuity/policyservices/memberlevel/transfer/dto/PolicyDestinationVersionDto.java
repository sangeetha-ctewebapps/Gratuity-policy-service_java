package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyDestinationVersionDto {
	
	private Long id;
	private Long masterpolicyHolder;
	private String policyNumber;
	private Long masterQuotationId;
	private Long contactPersonId;
	private Long bankAccountId;
	private Long masterPolicyId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date policyDispatchDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date policyRecievedDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date policyStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date policyEndDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date annualRenewlDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date lastPremiumPaid;
	private Float maximumStampFee;
	private Long totalMember;
	private String policyStatusId;
	private Long policySubStatusId;
	private Long policyTaggedStatusId;
	private Long productId;
	private String productName;
	private Long productVariantId;
	
	private String proposalNumber;
	private String productVariant;
	private String lineOfBusiness;
	private String unitId;
	private String unitCode;
	private String unitOffice;
	private String marketingOfficerCode;
	private String marketingOfficerName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date proposalSignDate;
//	
	private String rejectedReason;
	private String rejectedRemarks;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date riskCommencementDate;
	private String intermediaryCode;
	private String intermediaryName;
	private String advanceOrArrears;
	private Long contributtionFrequencyId;
	private Long lastLicId;
	private String customerCode;
	private String customerName;
	private String industryType;
	private Boolean isEscalatedToCo;
	private Boolean isEscalatedToZo;
	private Boolean isMinimumPaymentReceived;
	private Boolean isFullPaymentReceived;
	private Long memberCount;
	private String quotationNumber;
	private Boolean isAutoCoverStatus;
	private Long gstApplicableId;
	private Date modifiedDate;
	private String modifiedBy;
	private Date createdDate;
	private String createdBy;
	private Boolean isActive;
	 private  Long payto;
	  
}
