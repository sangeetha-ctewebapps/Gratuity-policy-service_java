package com.lic.epgs.gratuity.policyservices.conversion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMDto;
import com.lic.epgs.gratuity.policyservices.conversion.service.impl.AOCMCalculationService;

@RestController
@CrossOrigin("*")
@RequestMapping("api/convercalculation")
public class AOCMCalculation {

	@Autowired
	AOCMCalculationService aOCMCalculationService;

	@PostMapping("autocovercalculation/{policyid}/{autoCoverStatus}/{userName}/{conversionType}")
	public ApiResponseDto<AOCMDto> getautocovercalculation(@PathVariable(value = "policyid") Long policyid,
			@PathVariable(value = "autoCoverStatus") String autoCoverStatus,
			@PathVariable(value = "userName") String userName,
			@PathVariable(value = "conversionType") String conversionType) {
		Boolean getCalculation = false;
		if (policyid != 0) {
			getCalculation = aOCMCalculationService.getautocovercalculation(policyid, autoCoverStatus, userName,conversionType);
		}
		AOCMDto aOCMDto = new AOCMDto();
		aOCMDto.setFlag(getCalculation.toString());
		return ApiResponseDto.created(aOCMDto);
	}

}
