package com.lic.epgs.gratuity.policy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpPolTrfPropsDto;
import com.lic.epgs.gratuity.policy.service.PolicyTransferService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/tempMemberTrsProps/")
public class PolicyTransferController {
	
//	@Autowired
//	 private  TempMemberTrsPropsService  tempMemberTrsPropsService;
//	
//	
//	@GetMapping("/PmstTempMemTrfPropsInprogress/{policyNumber}")
//	public ApiResponseDto<List<PmstTmpPolTrfPropsDto>> Inprogresssearchfilter(@PathVariable String policyNumber) {
//		
//		return  tempMemberTrsPropsService.Inprogresssearchfilter(policyNumber);
//	}
//	
//	@GetMapping("PmstTempMemTrfPropsExistingSearchFilter/{policyNumber}")
//	public ApiResponseDto<List<PmstTmpPolTrfPropsDto>>GetExistingSerchFilter(@PathVariable(value="policyNumber") String policyNumber){
//		return  tempMemberTrsPropsService.GetExistingSearchFilter(policyNumber);
//		}
}
