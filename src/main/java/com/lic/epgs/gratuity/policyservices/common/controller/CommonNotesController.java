package com.lic.epgs.gratuity.policyservices.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.policyservices.common.entity.TempPolicyServiceNotes;
import com.lic.epgs.gratuity.policyservices.common.service.impl.NotesService;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/common/notes" })
public class CommonNotesController {

	@Autowired
	NotesService notesService;
	
	@PostMapping("save")
	public ResponseEntity<?> saveCommonNotes(@RequestBody TempPolicyServiceNotes tempPolicyServiceNotes) {

		return new ResponseEntity<>(notesService.saveCommonNotes(tempPolicyServiceNotes), HttpStatus.OK);
	}
	
	
	@GetMapping("getNotesList/{serviceNumber}")
	public ResponseEntity<?> getCommonNotesList(@PathVariable("serviceNumber") String serviceNumber) {

		return new ResponseEntity<>(notesService.getCommonNotesList(serviceNumber), HttpStatus.OK);

	}

	

	
}

