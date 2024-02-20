package com.lic.epgs.gratuity.quotation.lifecover.controller;

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
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverDto;
import com.lic.epgs.gratuity.quotation.lifecover.service.LifeCoverService;

/**
 * @author Vigneshwaran
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/lifecover")
public class LifeCoverController {
	@Autowired
	private LifeCoverService lifeCoverService;
	
	@PostMapping
	public ApiResponseDto<List<LifeCoverDto>> create(@RequestBody LifeCoverDto lifeCoverDto){
		return lifeCoverService.create(lifeCoverDto);
	}
	
	@PutMapping(value = "/{id}")
	public ApiResponseDto<List<LifeCoverDto>> update(@PathVariable("id") Long id, 
			@RequestBody LifeCoverDto lifeCoverDto) {
		return lifeCoverService.update(id, lifeCoverDto);
	}
	
	@GetMapping(value = "/{quotationId}/{type}")
	public ApiResponseDto<List<LifeCoverDto>> findByQuotationId(@PathVariable("quotationId") Long quotationId,
			@PathVariable("type") String type) {
		return lifeCoverService.findByQuotationId(quotationId, type);
	}
	
	@DeleteMapping(value = "/{id}")
	public ApiResponseDto<List<LifeCoverDto>> delete(@PathVariable("id") Long id) {
		return  lifeCoverService.delete(id);
	}
}
