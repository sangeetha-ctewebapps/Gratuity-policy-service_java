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
public class RenewalPolicyTMPDto {

	private Long id;
	private Long masterpolicyHolder;
	private String policyNumber;
	private Long policyServiceId;
	private Long quotationNumber;
	private Long masterQuotationId;
	private Long masterPolicyId;
	private Long tmpPolicyId;
	private Long gstApplicableId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date quotationDate;
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
	private Long policySubStatusId;
	private Long policytaggedStatusId;
	private Long productId;
	private String productName;
	private String productVariant;
	private Long productVariantId;
	private String lineOfBusiness;
	private Long contactPersonId;
	private Long bankAccountId;
	private Long isApprovalEscalatedToGo;
	private Long isApprovalEscalatedToZo;
	private Long isMinPaymentRecevied;
	private Long isFullPaymentReceived;
	private String unitId;
	private String unitCode;
	private String unitOffice;
	private String marketingOfficerCode;
	private String marketingOfficerName;
	private String intermediaryCode;
	private String intermediaryName;
	private String advanceOrArrears;
	private long contributionFrequencyId;
	private long memberUniqueIdendifierId;
	private Date riskCommencementDate;
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	private Long lastLicId;
	private String customerName;
	private String customerCode;
	private String industryType;
	private Boolean isStdValuesDeviated;
	private Long quotationStatusId;
	private Long quotationSubStatusId;
	private Long quotationTaggedStatusId;
	private String rejectedReason;
	private String rejectedRemarks;
    private String quotationStatus;
	private  Long payto;
	private String sourcePolicyId;
	private String destinationPolicyId;
	private String sourceTmpPolicyId;
	private String destinationTmpPolicyId;
	
	private String destinationPolicyNumber;

	private Long conversionId;
	private Date nextDueDate;
	private Date dateOfCommencement;
	
	
	


	
	
	



	
}
