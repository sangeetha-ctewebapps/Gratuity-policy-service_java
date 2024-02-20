package com.lic.epgs.gratuity.policy.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpConversionPropsDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpMergerPropsDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.service.ConversionService;

@CrossOrigin("*")
@RestController
@RequestMapping("api/conversion")
public class ConversionServiceController {

	@Autowired
	private ConversionService conversionService;
	
	@GetMapping("newconversionsearchfilter/{policyNumber}")
	public ApiResponseDto<PolicyDto> getConversionPolicyDetail(@PathVariable(value="policyNumber") String policyNumber){
		
		return conversionService.getConversionPolicyDetail(policyNumber);
		
	}
	
	@PostMapping("saveconversion")
	public ApiResponseDto<PmstTmpConversionPropsDto> SaveConversion(@RequestBody PmstTmpConversionPropsDto pmstTmpConversionPropsDto){
		
		return conversionService.saveConversionservice(pmstTmpConversionPropsDto);
		
	}
	
	@PutMapping("updateconversion/{id}")
public ApiResponseDto<PmstTmpConversionPropsDto> updateconversion(@PathVariable("id") Long id,@RequestBody PmstTmpConversionPropsDto pmstTmpConversionPropsDto){
		return conversionService.updateconversion(id,pmstTmpConversionPropsDto);
	}
	
	@PutMapping("conversionStatusUpdate")
	public ApiResponseDto<PmstTmpConversionPropsDto> conversionStatusUpdate(@RequestBody PmstTmpConversionPropsDto pmstTmpConversionPropsDto){
		return conversionService.conversionStatusUpdate(pmstTmpConversionPropsDto);
	}
	
	// add requestbody 
	@GetMapping("/inprogresssearchfilter/{policyNumber}")
	public ApiResponseDto<List<PmstTmpConversionPropsDto>> inprogresssearchfilter(@RequestBody PmstTmpConversionPropsDto pmstTmpConversionPropsDto) {
		
		return conversionService.inprogresssearchfilter(pmstTmpConversionPropsDto);
	}
	// add requestbody 
	@GetMapping("ExistingSearchFilter/{policyNumber}")
	public ApiResponseDto<List<PmstTmpConversionPropsDto>>getExistingSerchFilter(@RequestBody PmstTmpConversionPropsDto pmstTmpConversionPropsDto){
		return conversionService.getExistingSearchFilter(pmstTmpConversionPropsDto);
		}
	
	@GetMapping("getConversionDetailById/{id}")
	public ApiResponseDto<PmstTmpConversionPropsDto> getConversionDetailById(@PathVariable(value="id") Long id){
		
		return conversionService.getConversionDetailById(id);
		
	}
}
