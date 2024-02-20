package com.lic.epgs.gratuity.policy.endorsement.document.controller;

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
import com.lic.epgs.gratuity.policy.endorsement.document.dto.EndorsementDocumentDto;
import com.lic.epgs.gratuity.policy.endorsement.document.service.EndorsementDocumentService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy/endorsementdocument/")
public class EndorsementDocumentController {

	@Autowired
	private EndorsementDocumentService endorsementDocumentService;
	
	@PostMapping
	public ApiResponseDto<List<EndorsementDocumentDto>> create(@RequestBody EndorsementDocumentDto endorsementDocumentDto) {
		return endorsementDocumentService.create(endorsementDocumentDto);
	}
	@PutMapping(value = "{id}")
	public ApiResponseDto<List<EndorsementDocumentDto>> update(@PathVariable("id") Long id, @RequestBody EndorsementDocumentDto endorsementDocumentDto) {
		return endorsementDocumentService.update(id, endorsementDocumentDto);
	}
	
	@GetMapping(value = "{tmpPolicyId}")
	public ApiResponseDto<List<EndorsementDocumentDto>> findByTmpPolicyId(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return endorsementDocumentService.findByTmpPolicyId(tmpPolicyId);
	}
	@DeleteMapping(value = "delete/{id}")
	public ApiResponseDto<List<EndorsementDocumentDto>> delete(@PathVariable("id") Long id) {
		return endorsementDocumentService.delete(id); 
	}
}
