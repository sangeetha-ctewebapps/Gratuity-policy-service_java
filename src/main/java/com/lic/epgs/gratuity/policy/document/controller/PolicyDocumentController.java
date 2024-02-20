package com.lic.epgs.gratuity.policy.document.controller;

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
import com.lic.epgs.gratuity.policy.document.dto.PolicyDocumentDto;
import com.lic.epgs.gratuity.policy.document.service.PolicyDocumentService;
import com.lic.epgs.gratuity.quotation.document.dto.DocumentDto;
import com.lic.epgs.gratuity.quotation.document.service.DocumentService;

@RestController
@CrossOrigin("*")
@RequestMapping("api/policydocument")
public class PolicyDocumentController {

	@Autowired
	private PolicyDocumentService policyDocumentService;

	@PostMapping
	public ApiResponseDto<List<PolicyDocumentDto>> create(@RequestBody PolicyDocumentDto policyDocumentDto) {
		return policyDocumentService.create(policyDocumentDto);
	}

	@PutMapping(value = "{id}")
	public ApiResponseDto<List<PolicyDocumentDto>> update(@PathVariable("id") Long id,
			@RequestBody PolicyDocumentDto policyDocumentDto) {
		return policyDocumentService.update(id, policyDocumentDto);
	}

	@GetMapping(value = "{policyId}/{type}")
	public ApiResponseDto<List<PolicyDocumentDto>> findByPolicyId(@PathVariable("policyId") Long policyId,
			@PathVariable("type") String type) {
		return policyDocumentService.findByPolicyId(policyId, type);
	}

	@DeleteMapping(value = "{id}")
	public ApiResponseDto<List<PolicyDocumentDto>> delete(@PathVariable("id") Long id) {
		return policyDocumentService.delete(id);
	}
}
