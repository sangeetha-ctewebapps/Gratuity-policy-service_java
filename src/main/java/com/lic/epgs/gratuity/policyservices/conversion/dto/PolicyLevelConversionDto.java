package com.lic.epgs.gratuity.policyservices.conversion.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:20-09-2022
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyLevelConversionDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long conversionId;

	private Long serviceId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date conversionDate;

	private Long prevPolicyId;

	private String prevPolicyNo;

	private String claimPending;

	private Long newPolicyId;

	private String newPolicyNo;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date newPolicyAnnualRenewalDate;

	private Long newpolicyStatus;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date newpolicyIssueDate;

	private String newPolicyMphCode;

	private String newPolicyMphName;

	private Long newPolicyProduct;

	private Long newPolicyVariant;

	private Long noOfCatalogue;

	private Long prevFundBalancde;

	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private String createdOn;

	private String modifiedBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;

	private Integer conversionStatus;

	private Integer conversionWorkflowStatus;

	private Boolean isActive;

	private String unitCode;

//	private Set<PolicyServiceDocumentDto> docs = new HashSet<>();

//	private Set<PolicyServiceMbrDto> member = new HashSet<>();
//
//	private Set<PolicyServiceNotesDto> notes = Collections.emptySet();

	private String rejectionReasonCode;

	private String rejectionRemarks;
	private String statusDescription;
	
	private RenewalPolicyTMPDto renewalPolicyTMPDto;
	
	// new fields for conversion pops
	private Long conversionRequestNumber;
	private Date conversionRequestDate;
	private Date policyConversionDate;
	private Double totalFundValue;
	private Long tmpPolicyID;
	private Long newMasterPolicyID;
	private Long statusID;
}