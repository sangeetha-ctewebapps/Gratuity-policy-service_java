package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationBasicDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationWithdrawalRateDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewalValuationMatrixDto {

	
	private Long id;
	private Long policyId;
	private Long valuationTypeId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date valuationDate;
	private Double currentServiceLiability;
	private Double pastServiceLiability;
	private Double futureServiceLiability;
	private Double premium;
	private Double gst;
	private Double totalPremium;
	private Double amountReceived;
	private Double amountPayable;
	private Double balanceToBePaid;
	private Double totalSumAssured;
	private String createdBy;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	private Long pmstValuationMatrixId;
	private Long tmpPolicyId;
}
