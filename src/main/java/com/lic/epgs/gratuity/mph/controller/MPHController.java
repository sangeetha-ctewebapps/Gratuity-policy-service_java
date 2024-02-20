package com.lic.epgs.gratuity.mph.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.mph.dto.MPHEntityDto;
import com.lic.epgs.gratuity.mph.dto.TempMPHAddressDto;
import com.lic.epgs.gratuity.mph.dto.TempMPHBankDto;
import com.lic.epgs.gratuity.mph.dto.TemptMPHDto;
import com.lic.epgs.gratuity.mph.entity.TempMPHBankEntity;
import com.lic.epgs.gratuity.mph.service.MPHService;
import com.lic.epgs.gratuity.quotation.member.dto.MemberDto;



@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/mph" })
public class MPHController {
	
	@Autowired
	private MPHService mphService;
	
	protected final Logger logger = LogManager.getLogger(getClass());
	
	
	@GetMapping("{mphId}/{type}")
	public ApiResponseDto<MPHEntityDto> findById(@PathVariable("mphId") Long mphId,@PathVariable("type") String type ) {
		return mphService.findById(mphId,type);
	}
	
	@PostMapping("savetempmph/{endoresementId}")
	public ApiResponseDto<TemptMPHDto> tempMPHSave(@PathVariable ("endoresementId") Long endoresementId,@RequestBody TemptMPHDto temptMPHDto){
		
		
		return mphService.tempMPHSave( endoresementId,temptMPHDto );
	}
	@PostMapping("savetempmphAddress/{endoresementId}")
	public ApiResponseDto<TempMPHAddressDto> tempMPHAddressSave(@PathVariable ("endoresementId") Long endoresementId,@RequestBody TempMPHAddressDto tempMPHAddressDto){
		
		
		return mphService.tempMPHAddressSave( endoresementId,tempMPHAddressDto );
	}
	
	
	@PostMapping("savetmpmph/{temppolicyId}")
	public ApiResponseDto<TemptMPHDto> saveMphBank(@PathVariable("temppolicyId") Long temppolicyId,@RequestBody TempMPHBankDto tempMphBankdto){
		return mphService.saveMph(temppolicyId,tempMphBankdto);
		
	}
	@DeleteMapping("deletemphbank/{mphbankId}")
	public ApiResponseDto<TemptMPHDto> deleteMphBank(@PathVariable("mphbankId") Long mphbankId){
		return mphService.deleteMphBank(mphbankId);
		
	}
	
	@PutMapping("updateTmpMph/{mphId}")
	public ApiResponseDto<TemptMPHDto> updateMph(@PathVariable("mphId") Long mphId,@RequestBody TempMPHBankDto tempMPHBankDto) {
		
		return mphService.updateMph(mphId,tempMPHBankDto);
	}
	
	@GetMapping("viewTmpMph/{tempPolicyId}")
   public ApiResponseDto<TemptMPHDto> viewMph(@PathVariable("tempPolicyId") Long tempPolicyId) {
		
		return mphService.viewMph(tempPolicyId);
	}
	
	
}