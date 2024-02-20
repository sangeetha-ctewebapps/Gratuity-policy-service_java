package com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMCredibilityFactorDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMExpenseRateDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.service.AOCMService;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationBasicTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationDetailDto;

@RestController
@CrossOrigin("*")
@RequestMapping("api/aocmcalculation")
public class AOCMController {
	
	@Autowired
	private AOCMService aocmService;
	
	
	@GetMapping("getaocmcalculationdata/{policyId}")
	public ApiResponseDto<List<AOCMDto>> getAOCMCalculationData(@PathVariable(value = "policyId") Long policyId){
		return aocmService.getAOCMCalculationData(policyId);
		
	}
	
	@GetMapping("getAOCMcredibilityfactor/{variant}")
	public ApiResponseDto<List<AOCMCredibilityFactorDto>> GetAOCMCredibilityFactor(@PathVariable(value = "variant") String variant){
		return aocmService.GetAOCMCredibilityFactor(variant);
	}
	
	@GetMapping("getAOCMexpenserate/{variant}")
	public ApiResponseDto<List<AOCMExpenseRateDto>> GetAOCMExpenseRate(@PathVariable(value = "variant") String variant){
		return aocmService.GetAOCMExpenseRate(variant);
	}
	
	@PutMapping("updateaocmcalculation")
	public ApiResponseDto<RenewalValuationDetailDto> UpdateAOCMCalculation(@RequestBody RenewalValuationBasicTMPDto  renewalValuationBasicTMPDto){
		return aocmService.UpdateAOCMCalculation(renewalValuationBasicTMPDto);
	}
			
	@GetMapping("getAutoCoverDetails/{policyId}")
	public ApiResponseDto<PolicyDto> getAutoCoverDetails(@PathVariable(value = "policyId") Long policyId){
		return aocmService.getAutoCoverDetails(policyId);
	}
	
	@PostMapping("autocovercalculation/{policyid}/{autoCoverStatus}/{userName}")
	public  ApiResponseDto<AOCMDto> getautocovercalculation(@PathVariable (value = "policyid") Long policyid,
			@PathVariable (value = "autoCoverStatus") String autoCoverStatus,@PathVariable(value = "userName") String userName){
		 Boolean getCalculation=false;
		 if(policyid!=0) {
		 getCalculation=aocmService.getautocovercalculation(policyid,autoCoverStatus,userName);
		 }
		 AOCMDto aOCMDto=new AOCMDto();
		 aOCMDto.setFlag(getCalculation.toString());
		return ApiResponseDto.created(aOCMDto);
	}
	
	
	

}
