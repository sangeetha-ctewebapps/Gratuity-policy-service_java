package com.lic.epgs.gratuity.policy.integration.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProposalBasicDetailsDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String proposalId;
	private String leadId;
	private String proposalNumber;
	private String rejectionReasonCode;
	private String rejectionRemarks;
	private String remarks;
	private String proposalStatus;
	private String subStatus;
	private String workFlowStatus;
	private String createdBy;
	private String createdOn;
	private String modifiedBy;
	private String modifiedOn;
	private String proposalDate;
	private String subCustomerId;
	private String trustId;
	private String proposalSourse;
	private String lineOfBusiness;
	private String customerId;
	private String customerName;
	private String customerStatus;
	private String customerCode;
	private String leadNumber;
	private String proposerName;
	private String gstin;
	private String gstinApplicable;
	private String urbanOrRural;
    private String unitCode;
    private String tempProposalId;
	
	

	
}
