package com.lic.epgs.gratuity.renewalpolicy.document.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.renewalpolicy.document.dto.RenewalPolicyDocumentDto;
import com.lic.epgs.gratuity.renewalpolicy.document.service.RenewalPolicyDocumentService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/renewalpolicydocument")
public class RenewalPolicyDocumentController {

	@Autowired
	private RenewalPolicyDocumentService renewalPolicyDocumentService;

	@PostMapping
	public ApiResponseDto<List<RenewalPolicyDocumentDto>> create(
			@RequestBody RenewalPolicyDocumentDto renewalPolicyDocumentDto) {
		return renewalPolicyDocumentService.create(renewalPolicyDocumentDto);
	}

	@GetMapping(value = "{tmpPolicyId}/{moduleType}")
	public ApiResponseDto<List<RenewalPolicyDocumentDto>> findByPolicyId(@PathVariable("tmpPolicyId") Long tmpPolicyId,@PathVariable("moduleType") String moduleType) {
		return renewalPolicyDocumentService.findByPolicyIdandModuleType(tmpPolicyId,moduleType);
	}

	@DeleteMapping(value = "{id}")
	public ApiResponseDto<List<RenewalPolicyDocumentDto>> delete(@PathVariable("id") Long id) {
		return renewalPolicyDocumentService.delete(id);
	}

}
