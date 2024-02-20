package com.lic.epgs.gratuity.renewalpolicy.notes.controller;

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

import com.lic.epgs.gratuity.renewalpolicy.notes.dto.RenewalPolicyNotesDto;
import com.lic.epgs.gratuity.renewalpolicy.notes.service.RenewalPolicyNotesService;



@RestController
@CrossOrigin("*")
@RequestMapping("/api/renewalnotes")
public class RenewalPolicyNotesController {
	
	@Autowired
	 private RenewalPolicyNotesService renewalPolicyNotesService;
	
	@PostMapping
	public ApiResponseDto<List<RenewalPolicyNotesDto>> create(@RequestBody RenewalPolicyNotesDto renewalPolicyNotesDto) {
		return renewalPolicyNotesService.create(renewalPolicyNotesDto);
	}
	
	@GetMapping(value = "{tmppolicyId}/{notesType}/{type}")
	public ApiResponseDto<List<RenewalPolicyNotesDto>> findByPolicyIdAndNotesType(@PathVariable("tmppolicyId") Long tmppolicyId, 
			@PathVariable("notesType") String notesType, @PathVariable("type") String type) {
		
		return renewalPolicyNotesService.findByPolicyIdAndNotesType(tmppolicyId, notesType, type);
	}
	

}
