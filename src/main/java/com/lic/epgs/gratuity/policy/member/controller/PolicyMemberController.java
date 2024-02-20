package com.lic.epgs.gratuity.policy.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberSearchDto;
import com.lic.epgs.gratuity.policy.member.dto.TempMemberDto;
import com.lic.epgs.gratuity.policy.member.service.PolicyMemberService;

/**
 * @author Ismail Khan A
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/policy/member/" })
public class PolicyMemberController {
	
	@Autowired
	private PolicyMemberService policyMemberService;
	
	@PostMapping(value = "/filter/{type}")
    public ApiResponseDto<List<PolicyMemberDto>> filter(@RequestBody PolicyMemberSearchDto policyMemberSearchDto,
    		@PathVariable("type") String type) {
        return policyMemberService.filter(policyMemberSearchDto, type);
    }
	
	@GetMapping("{memberId}/{type}")
	public ApiResponseDto<PolicyMemberDto> findById(@PathVariable("memberId") Long memberId,
			@PathVariable("type") String type) {
		return policyMemberService.findById(memberId, type);
	}
	
	@PostMapping("saveTempMember/{endorsementId}")
	public ApiResponseDto<TempMemberDto> saveTempMember(@PathVariable ("endorsementId") Long endorsementId,@RequestBody TempMemberDto tempMemberDto){
		return policyMemberService.saveTempMember(endorsementId,tempMemberDto);
	}
	
	@GetMapping("getTempMember/{endorsementId}")
	public ApiResponseDto<List<TempMemberDto>> getTempMember(@PathVariable ("endorsementId") Long endorsementId){
		return policyMemberService.getTempMember(endorsementId);
	}
	
	@PutMapping("updateTempMember/{id}")
	public ApiResponseDto<TempMemberDto> updateTempMember(@PathVariable ("id") Long id,@RequestBody TempMemberDto tempMemberDto){
		return policyMemberService.updateTempMember(id,tempMemberDto);
	}
		
}
