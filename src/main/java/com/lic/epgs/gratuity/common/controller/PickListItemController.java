package com.lic.epgs.gratuity.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.PickListDto;
import com.lic.epgs.gratuity.common.dto.PickListItemDto;
import com.lic.epgs.gratuity.common.service.PickListItemService;

/**
 * @author Gopi
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/picklistitem")
public class PickListItemController {
	
	@Autowired
	private PickListItemService pickListItemService;
	
	@GetMapping(value = "{id}")
	public ApiResponseDto<List<PickListItemDto>> findByPickListId(@PathVariable("id") Long id) {
		return pickListItemService.findByPickListId(id);
	}
	
	@GetMapping
	public ApiResponseDto<List<PickListDto>> findAllByOrderByPickListIdOrderByDisplayOrder() {
		return pickListItemService.findAllByOrderByPickListIdOrderByDisplayOrder();
	}
}
