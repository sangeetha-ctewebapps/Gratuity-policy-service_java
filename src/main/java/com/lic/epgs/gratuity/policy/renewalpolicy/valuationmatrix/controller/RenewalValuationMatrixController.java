package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.dto.RenewalValuationTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationDetailDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationMatrixDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.service.RenewalValuationMatrixService;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationDetailDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;

@RestController
@CrossOrigin("*")
@RequestMapping("api/renewalvaluationmatrix")
public class RenewalValuationMatrixController {
	
	@Autowired
	private RenewalValuationMatrixService renewalValuationMatrixService;

	
	@PutMapping("renewalvaluation/update/{id}")
	public ApiResponseDto<RenewalValuationDetailDto> RenewalValuationUpdate(@PathVariable (value = "id")Long id ,@RequestBody RenewalValuationDetailDto renewalValuationDetailDto  ){
		return renewalValuationMatrixService.RenewalValuationUpdate(id,renewalValuationDetailDto);
	}
	
	@PostMapping("renewalvaluationcreate")
	public ApiResponseDto<RenewalValuationDetailDto> renewalvaluationcreate(@RequestBody RenewalValuationDetailDto renewalValuationDetailDto){
		return renewalValuationMatrixService.renewalvaluationcreate(renewalValuationDetailDto);
	}

	@PostMapping("/valuationmatrixsave")
	
	public ApiResponseDto<RenewalValuationMatrixDto> create(@RequestBody RenewalValuationMatrixDto renewalValuationMatrixDto){
		
		return renewalValuationMatrixService.create(renewalValuationMatrixDto);
	}
	
	
	@GetMapping(value = "/matrix/{tmpPolicyId}/{type}")
	public ApiResponseDto<RenewalValuationMatrixDto> findMatrixTemPolicyId(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("type") String type) {
		return renewalValuationMatrixService.findMatrixTemPolicyId(tmpPolicyId, type);
	
	}	
	
	@PutMapping(value = "/matrix/updatevalutionmatrix{tmpPolicyId}")
	public ApiResponseDto<RenewalValuationMatrixDto> updateRenewalvalutionMatrixTemPolicyId(@PathVariable("tmpPolicyId") Long tmpPolicyId,@RequestBody RenewalValuationMatrixDto renewalValuationMatrixDto) {
		return renewalValuationMatrixService.updateRenewalvalutionMatrixTemPolicyId(tmpPolicyId, renewalValuationMatrixDto);
	
	}	
	
	@GetMapping("getrenewalvaluation/{tmpPolicyId}/{type}")
	public ApiResponseDto<RenewalValuationDetailDto> GetrRenewalValuation(@PathVariable (value = "tmpPolicyId")Long tmpPolicyId ,@PathVariable (value = "type")String  type ){
		return renewalValuationMatrixService.findBypolicyId(tmpPolicyId,type);
	}
	
	
	
}
