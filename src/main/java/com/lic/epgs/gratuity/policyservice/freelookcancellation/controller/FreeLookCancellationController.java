package com.lic.epgs.gratuity.policyservice.freelookcancellation.controller;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.MasterPolicyContributionDetailsDto;
import com.lic.epgs.gratuity.policyservice.freelookcancellation.dto.FreeLookCancellationDto;
import com.lic.epgs.gratuity.policyservice.freelookcancellation.service.FreeLookCancellationService;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/freelookcancellation/")
public class FreeLookCancellationController {
	
	@Autowired
	private FreeLookCancellationService freeLookCancellationService;
	
	@PostMapping("/saveFreeLookCancellationDetails")
	public ApiResponseDto<FreeLookCancellationDto> saveFreeLookCancellationDeatils(@RequestBody FreeLookCancellationDto freeLookCancellationDto){
		return freeLookCancellationService.saveFreeLookCancellationDetails(freeLookCancellationDto);
	}
	
	@GetMapping("/getFreeLookCancellationDetailsByPolicyId/{policyId}")
	public ApiResponseDto<FreeLookCancellationDto> getFreeLookCancellationDetails(@PathVariable Long policyId){
		return freeLookCancellationService.getFreeLookCancellationDetails(policyId);
	}
	
	@PutMapping("/updateFreeLookCancellationDetails")
	public ApiResponseDto<FreeLookCancellationDto> updateFreeLookCancellationDetails(@RequestBody FreeLookCancellationDto freeLookCancellationDto){
		return freeLookCancellationService.updateFreeLookCancellationDetails(freeLookCancellationDto);
	}
	
	@GetMapping("/getFlcPremiumDetails/{masterPolicyId}")
	public ApiResponseDto<MasterPolicyContributionDetailsDto> getFlcPremiumFetails(@PathVariable Long masterPolicyId){
		return freeLookCancellationService.getFlcPremiumDetails(masterPolicyId);
	}
	
	@GetMapping("/inprogressFlcDetails/{policyNumber}")
	public ApiResponseDto<List<FreeLookCancellationDto>> inProgressFlcDetailsByPolicyNumber(@PathVariable String policyNumber){
		return freeLookCancellationService.inProgressDetailsByPolicyNumber(policyNumber);
	}

}
