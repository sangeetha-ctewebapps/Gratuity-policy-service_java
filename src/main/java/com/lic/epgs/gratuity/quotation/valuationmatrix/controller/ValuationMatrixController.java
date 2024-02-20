package com.lic.epgs.gratuity.quotation.valuationmatrix.controller;

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
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationDetailDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.service.ValuationDetailService;

/**
 * @author Ismail Khan
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/valuationdetail")
public class ValuationMatrixController {

	@Autowired
	private ValuationDetailService valuationDetailService;

	@GetMapping(value = "{quotationid}/{type}")
	public ApiResponseDto<ValuationDetailDto> findByQuotationId(@PathVariable("quotationid") Long quotationId,
			@PathVariable("type") String type) {
		return valuationDetailService.findByQuotationId(quotationId, type);
	}

	@GetMapping(value = "/matrix/{quotationid}/{type}")
	public ApiResponseDto<ValuationMatrixDto> findMatrixByQuotationId(@PathVariable("quotationid") Long quotationId,
			@PathVariable("type") String type) {
		return valuationDetailService.findMatrixByQuotationId(quotationId, type);
	}

	@PostMapping
	public ApiResponseDto<ValuationDetailDto> create(@RequestBody ValuationDetailDto valuationDetailDto) {
		return valuationDetailService.create(valuationDetailDto);
	}

	@PostMapping(value = "/matrix")
	public ApiResponseDto<ValuationMatrixDto> createMatrix(@RequestBody ValuationMatrixDto valuationMatrixDto) {
		return valuationDetailService.createMatrix(valuationMatrixDto);
	}

	@PutMapping(value = "{id}")
	public ApiResponseDto<ValuationDetailDto> update(@PathVariable("id") Long id,
			@RequestBody ValuationDetailDto valuationDetailDto) {
		return valuationDetailService.update(id, valuationDetailDto);
	}

	@PutMapping(value = "/matrix")
	public ApiResponseDto<ValuationMatrixDto> updateMatrix(@RequestBody ValuationMatrixDto valuationMatrixDto) {
		return valuationDetailService.updateMatrix(valuationMatrixDto);
	}

	@PostMapping(value = "/checkvaluationmatrixexit/{quotationid}")
	public ApiResponseDto<String> statusforvaluationMatrix(@PathVariable("quotationid") Long quotationId) {
		return valuationDetailService.statusforvaluationMatrix(quotationId);
	}

}
