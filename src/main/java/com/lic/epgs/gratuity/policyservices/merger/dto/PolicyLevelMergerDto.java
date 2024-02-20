package com.lic.epgs.gratuity.policyservices.merger.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lic.epgs.gratuity.policyservices.common.entity.PolicyServiceDocumentDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PolicyLevelMergerDto implements Serializable {
	
	private Long mergeId;
	private Long sourcePolicyId;
	private Long destinationPolicyId;
	private String mergingPolicy;
	private String destinationPolicy;
	private String createdBy;
	private String modifiedBy;
	
	private Long mergerRequestNumber;
	private Date mergerRequestDate;

	private Long sourceTmpPolicyID;
	private Long destinationTmpPolicyID;
	private Double sourceAccruedInterest;
	private Double sourcePriorTotalFund;
	private Double sourcePriorFundValue;
	private Double destinationPriorFundValue;
	private Double destinationPriorTotalValue;
	private Long deductFrom;
	private Long statusID;
	private Boolean isActive;
	
	//private Integer mergerType;
//	private static final long serialVersionUID = 1L;
	
	private Long serviceId;
	
//	private Set<PolicyServiceDocumentDto> docs = new HashSet<>();
////	private Set<PolicyServiceNotesDto> notes = new HashSet<>();
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date effectiveDt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date reqRecvdDt;
	private Long mergerType;
	private Long sourcePolicyVersionType;
	private Long destinationPolicyVersionType;
	
	private Date createdOn;
	
	private Date modifiedOn;
	private Integer mergeStatus;
	private Integer workflowStatus;
	private String unitCode;
	private String roleType;
	private String rejectionReasonCode;
	private String rejectionRemarks;
	private String srcPolicyNumber;
	private String srcMPHCode;
	private String srcMPHName;
	private String destMPHCode;
	private String destMPHName;
	private String srcPolicyStatus;
	private String destPolicyStatus;
	private String product;
	private String statusDescription;
	
	
}
