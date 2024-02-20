package com.lic.epgs.gratuity.policy.endorsement.notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.endorsement.notes.dto.EndorsementNotesDto;
import com.lic.epgs.gratuity.policy.endorsement.notes.service.EndorsementNotesService;
import com.lic.epgs.gratuity.policy.notes.dto.StagingPolicyNotesDto;
import com.lic.epgs.gratuity.policy.notes.service.PolicyNotesService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/endorsementnotes")
public class EndorsementNotesController {
	@Autowired
	private EndorsementNotesService endorsementNotesService;
	
	@PostMapping
	public ApiResponseDto<List<EndorsementNotesDto>> create(@RequestBody EndorsementNotesDto endorsementNotesDto) {
		return endorsementNotesService.create(endorsementNotesDto);
	}
	
	@GetMapping(value = "{endorsementId}/{noteType}/{type}")
	public ApiResponseDto<List<EndorsementNotesDto>> findByEndorsementIdAndNotesType(@PathVariable("endorsementId") Long endorsementId,
			@PathVariable("noteType") String noteType,@PathVariable("type") String type){
				return endorsementNotesService.findByEndorsementIdAndNotesType(endorsementId,noteType,type);
		
	}
}
