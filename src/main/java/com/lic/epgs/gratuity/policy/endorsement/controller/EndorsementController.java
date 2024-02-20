package com.lic.epgs.gratuity.policy.endorsement.controller;

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
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.endorsement.dto.EndorsementDto;
import com.lic.epgs.gratuity.policy.endorsement.service.EndorsementService;
import com.lic.epgs.gratuity.quotation.dto.QuotationDto;



@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy/endorsement")
public class EndorsementController {

	
	@Autowired
	private EndorsementService endorsementService;
	
	@GetMapping(value = "/{policyId}")
	public ApiResponseDto<List<EndorsementDto>>  findById(@PathVariable("policyId") Long policyId) {
		
		return endorsementService.findBypolicyId(policyId);
	}
	
	@PostMapping(value = "/{policyId}")
	public ApiResponseDto<EndorsementDto> create(@PathVariable("policyId") long policyId,
			@RequestBody EndorsementDto endorsementDto){
		return endorsementService.create(policyId, endorsementDto);
	}
	
	@PutMapping(value = "/{id}")
	public ApiResponseDto<EndorsementDto> update(@PathVariable("id") long id,
			@RequestBody EndorsementDto endorsementDto){
		return endorsementService.update(id,endorsementDto);
	}
	
	@PostMapping(value = "/submitforapproval/{id}/{username}")
	public ApiResponseDto<EndorsementDto> submitForApproval(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return endorsementService.submitForApproval(id, username);
	}

	@PostMapping(value = "/sendbacktomaker/{id}/{username}")
	public ApiResponseDto<EndorsementDto> sendBackToMaker(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return endorsementService.sendBackToMaker(id, username);
	}
	
	@PostMapping(value = "/approve/{id}/{username}")
	public ApiResponseDto<EndorsementDto> approve(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return endorsementService.approve(id, username);
	}
	
	@PostMapping(value = "/reject/{id}/{username}")
	public ApiResponseDto<EndorsementDto> reject(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return endorsementService.reject(id, username);
	}
	

	
	
}
