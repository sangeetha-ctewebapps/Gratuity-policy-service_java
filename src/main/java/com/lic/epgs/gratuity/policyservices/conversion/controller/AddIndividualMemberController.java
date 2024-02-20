package com.lic.epgs.gratuity.policyservices.conversion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.QuotationRenewalDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.service.RenewalPolicyService;
import com.lic.epgs.gratuity.policyservices.conversion.service.AddIndividualMemberService;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/ps/addIndividualMember" })
public class AddIndividualMemberController {

	@Autowired
	AddIndividualMemberService addIndividualMemberService;
	


	@PostMapping(value = "/saveTempMember")
	public TempMemberEntity saveTempMember(@RequestBody TempMemberEntity request, Long policyId, Long tempPolicyId) {
		return addIndividualMemberService.saveTempMember(request, policyId, tempPolicyId);
	}

	@PostMapping(value = "/saveTempMemberAddress")
	public TempMemberAddressEntity saveTempMemberAddress(@RequestBody TempMemberAddressEntity request, Long memberId,
			Long tempPolicyId) {
		return addIndividualMemberService.saveTempMemberAddress(request, memberId, tempPolicyId);
	}

	@PostMapping(value = "/saveTempMemberAppointee")
	public TempMemberAppointeeEntity saveTempMemberAppointee(@RequestBody TempMemberAppointeeEntity request,
			Long memberId, Long tempPolicyId) {
		return addIndividualMemberService.saveTempMemberAppointee(request, memberId, tempPolicyId);
	}

	@PostMapping(value = "/saveTempMemberBankAccount")
	public TempMemberBankAccountEntity saveTempMemberBankAccount(@RequestBody TempMemberBankAccountEntity request,
			Long memberId, Long tempPolicyId) {
		return addIndividualMemberService.saveTempMemberBankAccount(request, memberId, tempPolicyId);
	}

	@PostMapping(value = "/saveTempMemberNominee")
	public TempMemberNomineeEntity saveTempMemberNominee(@RequestBody TempMemberNomineeEntity request, Long memberId,
			Long tempPolicyId) {
		return addIndividualMemberService.saveTempMemberNominee(request, memberId, tempPolicyId);
	}

	@PostMapping("/saveAdditionOfMemberMasterToTemp")
	public ApiResponseDto<RenewalPolicyTMPDto> CreateRenewalforQuotation (@RequestBody QuotationRenewalDto quotationRenewalDto){
    return addIndividualMemberService.CreateRenewalforQuotation(quotationRenewalDto);
	
	}
	
}
