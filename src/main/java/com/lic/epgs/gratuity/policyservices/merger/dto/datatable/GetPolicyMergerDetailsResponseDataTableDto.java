package com.lic.epgs.gratuity.policyservices.merger.dto.datatable;

import java.util.Date;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class GetPolicyMergerDetailsResponseDataTableDto {



	private Long mergeId;
	private Long serviceId;
	private Long policyId;
	private Date effectiveDt;
	private Date reqRecvdDt;
	private Long mergerType;
	private Long destinationPolicyVersionType;
	private Long sourcePolicyVersionType;

	private Long destinationPolicyId;
	private String mergingPolicy;
	private String destinationPolicy;
	private Integer mergeStatus;
	private Integer workflowStatus;
	private Boolean isActive;
	private String unitCode;
	private String roleType;
	private String rejectionReasonCode;
	private String rejectionRemarks;
	private String policyNumber;
	private String srcMPHCode;
	private String srcMPHName;
	private String destMPHCode;
	private String destMPHName;
	private String srcPolicyStatus;
	private String destPolicyStatus;
	private String product;
	private String statusDescription;
	

	private Long recordsTotal;

	private Long noOfPages;


		

}
