package com.lic.epgs.gratuity.quotation.premium.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;
import com.lic.epgs.gratuity.quotation.premium.dto.KeyValuePairDto;
import com.lic.epgs.gratuity.quotation.premium.dto.PremiumDto;
import com.lic.epgs.gratuity.quotation.premium.dto.RateTableSelectionDto;
import com.lic.epgs.gratuity.quotation.premium.service.PremiumService;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationDetailDto;

/**
 * @author Ismail Khan A
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/premium")
public class PremiumController {
	
	@Autowired
	private PremiumService premiumService;
	
	@GetMapping(value = "{quotationId}")
	public ApiResponseDto<List<PremiumDto>> findByQuotationId(@PathVariable("quotationId") Long quotationId) {
		
		return premiumService.findByQuotationId(quotationId);
	}
	
	@PostMapping
	public ApiResponseDto<List<PremiumDto>> create(@RequestBody PremiumDto premiumDto) {
		return premiumService.create(premiumDto);
	}
	@PostMapping(value ="calculate/gratuity/{quotationId}/{username}")
	public ApiResponseDto<ValuationDetailDto> calculateGratuity(@PathVariable("quotationId") Long quotationId,
			@PathVariable("username") String username) {
		return premiumService.calculateGratuity(quotationId, username);
	}
	@GetMapping(value ="calculate/gratuity/{quotationId}")
	public ApiResponseDto<List<GratuityCalculationDto>> findAll(@PathVariable("quotationId") Long quotationId) {
		return premiumService.findAll(quotationId);
	}
	@GetMapping(value ="/ratetable")
	public ApiResponseDto<List<KeyValuePairDto>> findAllKeyValue() {
		return premiumService.findAllKeyValue();
	}
	
	@GetMapping(value ="/default")
	public ApiResponseDto <KeyValuePairDto> findDefaultValues ()
	{
		return premiumService.findDefaultValues();
		
	}
	
	@PostMapping(value="/industrygroupfilter")
	public ApiResponseDto<List<RateTableSelectionDto>> industrygroupfilter(@RequestBody RateTableSelectionDto rateTableSelectionDto ) {
		return premiumService.industrygroup(rateTableSelectionDto);
	}
	
	@GetMapping(value="/getratetableinorder/{ratevalue}")
	public ApiResponseDto<List<RateTableSelectionDto>> getratetableinorder(@PathVariable ("ratevalue") String ratevalue ) {
		return premiumService.getratetableinorder(ratevalue);
	}
	

}
