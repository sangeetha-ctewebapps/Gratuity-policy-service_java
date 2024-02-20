package com.lic.epgs.gratuity.quotation.notes.controller;

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
import com.lic.epgs.gratuity.quotation.notes.dto.NotesDto;
import com.lic.epgs.gratuity.quotation.notes.service.NotesService;

/**
 * @author Ismail Khan A
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/notes")
public class NotesController {
	
	@Autowired
	private NotesService notesService;
	
	@PostMapping
	public ApiResponseDto<List<NotesDto>> create(@RequestBody NotesDto notesDto) {
		return notesService.create(notesDto);
	}
	
	@GetMapping(value = "{quotationId}/{notesType}/{type}")
	public ApiResponseDto<List<NotesDto>> findByQuotationIdAndNotesType(@PathVariable("quotationId") Long quotationId, 
			@PathVariable("notesType") String notesType, @PathVariable("type") String type) {
		
		return notesService.findByQuotationIdAndNotesType(quotationId, notesType, type);
	}

}
