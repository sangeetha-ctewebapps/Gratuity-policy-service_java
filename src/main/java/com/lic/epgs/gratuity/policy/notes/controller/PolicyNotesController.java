package com.lic.epgs.gratuity.policy.notes.controller;

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
import com.lic.epgs.gratuity.policy.notes.dto.StagingPolicyNotesDto;
import com.lic.epgs.gratuity.policy.notes.service.PolicyNotesService;
import com.lic.epgs.gratuity.quotation.notes.dto.NotesDto;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/policynotes")
public class PolicyNotesController {
	
	@Autowired
	private PolicyNotesService policyNotesService;
	
	@PostMapping
	public ApiResponseDto<List<StagingPolicyNotesDto>> create(@RequestBody StagingPolicyNotesDto stagingPolicyNotesDto) {
		return policyNotesService.create(stagingPolicyNotesDto);
	}
	
	@GetMapping(value = "{policyId}/{notesType}/{type}")
	public ApiResponseDto<List<StagingPolicyNotesDto>> findByPolicyIdAndNotesType(@PathVariable("policyId") Long policyId, 
			@PathVariable("notesType") String notesType, @PathVariable("type") String type) {
		
		return policyNotesService.findByPolicyIdAndNotesType(policyId, notesType, type);
	}
	
	
	

}
