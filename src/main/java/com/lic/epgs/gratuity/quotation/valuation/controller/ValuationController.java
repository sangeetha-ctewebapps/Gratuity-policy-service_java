package com.lic.epgs.gratuity.quotation.valuation.controller;

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
import com.lic.epgs.gratuity.quotation.valuation.dto.ValuationDto;
import com.lic.epgs.gratuity.quotation.valuation.service.ValuationService;

/**
 * @author Ismail Khan
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/valuation")
public class ValuationController {
	
	@Autowired
	private ValuationService valuationService;
	
	@PostMapping(value = "create")
	public ApiResponseDto<ValuationDto> create( @RequestBody ValuationDto valuationDto){
		return valuationService.create(valuationDto);
	}
	
	@PutMapping(value = "{id}")
	public ApiResponseDto<ValuationDto> update(@PathVariable("id") Long id, 
			@RequestBody ValuationDto valuationDto) {
		return valuationService.update(id, valuationDto);
	}
	
	@GetMapping(value = "{id}/{type}")
	public ApiResponseDto<ValuationDto> findByQuotationId(@PathVariable("id") Long id,
			@PathVariable("type") String type) {
		return valuationService.findByQuotationId(id, type);
	}
	
	
	
	
}
