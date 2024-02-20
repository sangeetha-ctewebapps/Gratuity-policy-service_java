package com.lic.epgs.gratuity.policyservices.conversion.dto.datatable;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class GetPolicyConversionDetailsResponseDataTableDto {

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


	private String rejectionReasonCode;

	private String rejectionRemarks;
	private String statusDescription;
	
	private Long recordsTotal;

	private Long noOfPages;
}
