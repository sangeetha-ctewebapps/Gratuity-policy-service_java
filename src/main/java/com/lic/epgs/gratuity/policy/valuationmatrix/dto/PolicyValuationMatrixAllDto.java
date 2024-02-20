package com.lic.epgs.gratuity.policy.valuationmatrix.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationBasicDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;
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
public class PolicyValuationMatrixAllDto {
	private PolicyValuationMatrixDto policyValuationMatrixDto;
	private List<PolicyValuationWithdrawalRateDto> policyValuationWithdrawalRateDto;
	private PolicyValuationBasicDto policyValuationBasicDto ;
	
	private String createdBy;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;

}
