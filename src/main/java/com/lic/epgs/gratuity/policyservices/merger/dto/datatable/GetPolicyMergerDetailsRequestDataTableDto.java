package com.lic.epgs.gratuity.policyservices.merger.dto.datatable;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
public class GetPolicyMergerDetailsRequestDataTableDto {


	private static final long serialVersionUID = 1L;
	private Long mergeId;
	private Long serviceId;
	private Long policyId;
//	private Set<PolicyServiceDocumentDto> docs = new HashSet<>();
//	private Set<PolicyServiceNotesDto> notes = new HashSet<>();
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date effectiveDt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date reqRecvdDt;
	private Long mergerType;
	private Long destinationPolicyVersionType;
	private Long sourcePolicyVersionType;
	private Long destinationPolicyId;
	private String mergingPolicy;
	private String destinationPolicy;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
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
	
	private Long start;

	private Long end;

	private Integer columnSort;
	
	private Long[] listOfStatus;
}
