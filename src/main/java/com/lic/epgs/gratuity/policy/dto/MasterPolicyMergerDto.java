package com.lic.epgs.gratuity.policy.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class MasterPolicyMergerDto {
	
	private Long id;
    private MphSearchDto policyMPHTmp;
    private Long masterpolicyHolder;
    private String policyNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
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
    private Float maximumStampFee;
    private Long totalMember;
    private Long policyStatusId;
    private Long policyTaggedStatusId;
    private Long productId;
    private String productName;
    private String productVariant;
    private String lineOfBusiness;
    private Long contactPersonId;
    private Long bankAccountId;
    private String unitId;
    private String unitCode;
    private String unitOffice;
    private String marketingOfficerCode;
    private String marketingOfficerName;
    private String intermediaryCode;
    private String intermediaryName;
    private String advanceOrArrears;
    private long contributtionFrequencyId;
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
    private Long gstApplicableId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
    private Date riskCommencementDate;
    private Long payto;
    private Long productVariantId;
    private String proposalNumber;
    private Long oldPolicyNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
    private Date dateOfCommence;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
    private Date nextDueDate;

}
