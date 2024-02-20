package com.lic.epgs.gratuity.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.GratutityCeilingDto;
import com.lic.epgs.gratuity.common.dto.StandardCodeDto;
import com.lic.epgs.gratuity.common.service.StandardCodeService;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;

/**
 * @author Gopi
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/standardcode")
public class StandardCodeController {
	
	@Autowired
	private StandardCodeService standardCodeService;
	
	@Autowired
	private GratuityCalculationRepository gratuityCalculationRepository;
	
	@GetMapping
	public ApiResponseDto<List<StandardCodeDto>> findMultipleById() {
		return standardCodeService.findAll();
	}
	
	@GetMapping("getgratutity")
	public ApiResponseDto<List<GratutityCeilingDto>> GetGratutity() {
		return standardCodeService.findGetGratutity();
	}
}

