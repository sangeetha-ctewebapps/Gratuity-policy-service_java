package com.lic.epgs.gratuity.policy.valuationmatrix.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.dto.PolicyValuationMatrixAllDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.dto.PolicyValuationMatrixDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.service.PolicyValuationMatrixService;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationDetailDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.helper.ValuationMatrixHelper;

/**
 * @author Ismail Khan A
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy/valuation")
public class PolicyValuationMatrixController {

	@Autowired
	private PolicyValuationMatrixService policyValuationMatrixService;
	
	@GetMapping(value = "/{policyId}/{type}")
	public ApiResponseDto<PolicyValuationMatrixDto> findByPolicyId(@PathVariable("policyId") Long policyId,
			@PathVariable("type") String type) {
	return policyValuationMatrixService.findByPolicyId(policyId, type);
		
	}
	
	@GetMapping(value = "/valuationmatrix/{policyId}/{type}")
	public ApiResponseDto<PolicyValuationMatrixAllDto> findAllValuation(@PathVariable("policyId") Long policyId,
			@PathVariable("type") String type) {
	return policyValuationMatrixService.findAllValuation(policyId, type);
		
	}
	
	
	
	
}
