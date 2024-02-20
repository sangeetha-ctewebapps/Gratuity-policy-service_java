package com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.controller;

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
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.dto.RenewalLifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.service.RenewalSchemeruleTMPService;
import com.lic.epgs.gratuity.policy.schemerule.dto.PolicyLifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverAndGratuityDto;



@RestController
@CrossOrigin("*")
@RequestMapping("api/policyrenewalschemerule")
public class RenewalPolicySchemeRuleController {
	
	@Autowired
	private RenewalSchemeruleTMPService renewalSchemeruleTMPService;

	@PostMapping("createlifecoverandgratuity")
	public ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> createLifeCoverandGratuity(@RequestBody RenewalLifeCoverAndGratuityDto renewalLifeCoverAndGratuityDto){
		return renewalSchemeruleTMPService .createLifeCoverandGratuity(renewalLifeCoverAndGratuityDto);
	}
	
	@PutMapping("updatelifecoverandgratuity")
	public ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> updateLifeCoverandGratuity(@RequestBody RenewalLifeCoverAndGratuityDto renewallifeCoverAndGratuityDto){
		return renewalSchemeruleTMPService.updateLifeCoverandGratuity(renewallifeCoverAndGratuityDto);
	}
	
	@DeleteMapping(value = "/prop/{policyId}/{id}")
	public ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> delete(@PathVariable("policyId") Long policyId, @PathVariable("id") Long id) {
		return renewalSchemeruleTMPService.delete(policyId, id); 
	}
	
	@DeleteMapping(value = "deletelifeandgratuity/{policyId}/{lifecoverID}/{gratuityId}")
	public ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> deleteLifeandGratuity(@PathVariable("policyId") Long policyId, @PathVariable("lifecoverID") Long lifecoverID,@PathVariable("gratuityId") Long gratuityId) {
		return renewalSchemeruleTMPService.deleteLifeandGratuity(policyId, lifecoverID,gratuityId); 
	}
	
	@GetMapping(value = "findall/{policyId}/{type}")
	public ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> findAll(@PathVariable("policyId") Long policyId,
			@PathVariable("type") String type) {
		return renewalSchemeruleTMPService.findAll(policyId, type);
	}
	
}
