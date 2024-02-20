package com.lic.epgs.gratuity.quotation.notes.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.notes.dto.NotesDto;

/**
 * @author Ismail Khan A
 *
 */

public interface NotesService {

	ApiResponseDto<List<NotesDto>> findByQuotationIdAndNotesType(Long id, String notesType, String type);
	ApiResponseDto<List<NotesDto>> create(NotesDto notesDto);
}
