package com.lic.epgs.gratuity.policyservices.merger.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PolicyMergerDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long mergeId;
	private String mergerRequestNumber;

	private Date mergerRequestDate;

	private Integer mergerType;

	private Long sourceTmpPolicyID;

	private Long destinationTmpPolicyID;
	
	private Long sourcePolicyID;
	
	private Long destinationPolicyID;
	
	private String destinationPolicy;

	private String sourcePolicy;

	private Double sourceAccruedInterest;

	private Double sourcePriorTotalFund;

	private Double sourcePriorFundValue;

	private Double destinationPriorFundValue;

	private Double destinationPriorTotalValue;

	private Long deductFrom;

	private Long statusID;

	private Boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;

}
