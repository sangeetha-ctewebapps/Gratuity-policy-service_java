package com.lic.epgs.gratuity.quotation.schemerule.controller;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.quotation.schemerule.dto.NewSchemeRuleDto;
import com.lic.epgs.gratuity.quotation.schemerule.dto.SchemeRuleDto;
import com.lic.epgs.gratuity.quotation.schemerule.service.SchemeRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Gopi
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/schemerule")
public class SchemeRuleController {

	@Autowired
	private SchemeRuleService schemeRuleService;
	
	@PostMapping
	public ApiResponseDto<SchemeRuleDto> create(@RequestBody NewSchemeRuleDto newSchemeRuleDto){
		return schemeRuleService.create(newSchemeRuleDto);
	}
	
	@PutMapping(value = "{id}")
	public ApiResponseDto<SchemeRuleDto> update(@PathVariable("id") Long id, 
			@RequestBody SchemeRuleDto schemeRuleDto) {
		return schemeRuleService.update(id, schemeRuleDto);
	}
	
	@GetMapping(value = "{quotationId}/{type}")
	public ApiResponseDto<SchemeRuleDto> findByQuotationId(@PathVariable("quotationId") Long quotationId,
			@PathVariable("type") String type) {
		return schemeRuleService.findByQuotationId(quotationId, type);
	}
	
	@PostMapping("createlifecoverandgratuity")
	public ApiResponseDto<List<LifeCoverAndGratuityDto>> createLifeCoverandGratuity(@RequestBody LifeCoverAndGratuityDto lifeCoverAndGratuityDto){
		return schemeRuleService.createLifeCoverandGratuity(lifeCoverAndGratuityDto);
	}
	
	@PutMapping("updatelifecoverandgratuity")
	public ApiResponseDto<List<LifeCoverAndGratuityDto>> updateLifeCoverandGratuity(@RequestBody LifeCoverAndGratuityDto lifeCoverAndGratuityDto){
		return schemeRuleService.updateLifeCoverandGratuity(lifeCoverAndGratuityDto);
	}
	
	@GetMapping(value = "findall/{quotationId}/{type}")
	public ApiResponseDto<List<LifeCoverAndGratuityDto>> findAll(@PathVariable("quotationId") Long quotationId,
			@PathVariable("type") String type) {
		return schemeRuleService.findAll(quotationId, type);
	}
	
	@DeleteMapping(value = "/prop/{quotationId}/{id}")
	public ApiResponseDto<List<LifeCoverAndGratuityDto>> delete(@PathVariable("quotationId") Long quotationId, @PathVariable("id") Long id) {
		return schemeRuleService.delete(quotationId, id); 
	}
	
	@DeleteMapping(value = "deletelifeandgratuity/{quotationId}/{lifecoverID}/{gratuityId}")
	public ApiResponseDto<List<LifeCoverAndGratuityDto>> deleteLifeandGratuity(@PathVariable("quotationId") Long quotationId, @PathVariable("lifecoverID") Long lifecoverID,@PathVariable("gratuityId") Long gratuityId) {
		return schemeRuleService.deleteLifeandGratuity(quotationId, lifecoverID,gratuityId); 
	}
	
	
	
}
