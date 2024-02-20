package com.lic.epgs.gratuity.quotation.document.controller;


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
import com.lic.epgs.gratuity.quotation.document.dto.DocumentDto;
import com.lic.epgs.gratuity.quotation.document.service.DocumentService;

/**
 * @author Ismail Khan A
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/document")
public class DocumentController {
	@Autowired
	private DocumentService documentService;
	
	@PostMapping
	public ApiResponseDto<List<DocumentDto>> create(@RequestBody DocumentDto documentDto) {
		return documentService.create(documentDto);
	}
	
	@PutMapping(value = "{id}")
	public ApiResponseDto<List<DocumentDto>> update(@PathVariable("id") Long id, @RequestBody DocumentDto documentDto) {
		return documentService.update(id, documentDto);
	}
	
	@GetMapping(value = "{quotationId}/{type}")
	public ApiResponseDto<List<DocumentDto>> findByQuotationId(@PathVariable("quotationId") Long quotationId,
			@PathVariable("type") String type) {
		return documentService.findByQuotationId(quotationId, type);
	}
	
	@DeleteMapping(value = "{id}")
	public ApiResponseDto<List<DocumentDto>> delete(@PathVariable("id") Long id) {
		return documentService.delete(id); 
	}
}
