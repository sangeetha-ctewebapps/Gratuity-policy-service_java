package com.lic.epgs.gratuity.quotation.gratuitybenefit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitPropsDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.service.GratuityBenefitService;

/**
 * @author Ismail Khan A
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/gratuitybenefit")
public class GratuityBenefitController {

	@Autowired
	private GratuityBenefitService gratuityBenefitService;
	
	@PostMapping
	public ApiResponseDto<List<GratuityBenefitDto>> create(@RequestBody GratuityBenefitDto gratuityBenefitDto) {
		return gratuityBenefitService.create(gratuityBenefitDto);
	}
	
	@PutMapping
	public ApiResponseDto<List<GratuityBenefitDto>> update(@RequestBody GratuityBenefitDto gratuityBenefitDto) {
		return gratuityBenefitService.update(gratuityBenefitDto);
	}
	
	@PutMapping(value = "/prop/{quotationId}")
	public ApiResponseDto<List<GratuityBenefitDto>> update(@PathVariable("quotationId") Long quotationId,
			@RequestBody GratuityBenefitPropsDto gratuityBenefitPropsDto) {
		return gratuityBenefitService.update(quotationId, gratuityBenefitPropsDto);
	}
	
	@GetMapping(value = "/{quotationId}/{type}")
	public ApiResponseDto<List<GratuityBenefitDto>> findByQuotationId(@PathVariable("quotationId") Long quotationId,
			@PathVariable("type") String type) {
		return gratuityBenefitService.findByQuotationId(quotationId, type);
	}
	
	@DeleteMapping(value = "/prop/{quotationId}/{id}")
	public ApiResponseDto<List<GratuityBenefitDto>> delete(@PathVariable("quotationId") Long quotationId, @PathVariable("id") Long id) {
		return gratuityBenefitService.delete(quotationId, id); 
	}
	
	@DeleteMapping(value = "/{id}")
	public ApiResponseDto<List<GratuityBenefitDto>> delete(@PathVariable("id") Long id) {
		return gratuityBenefitService.delete(id);
	}
	
}
