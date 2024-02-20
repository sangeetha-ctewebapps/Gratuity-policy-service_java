package com.lic.epgs.gratuity.policy.renewalpolicy.member.controller;

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
import com.lic.epgs.gratuity.policy.document.dto.PolicyDocumentDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalMemberImportDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberAddressDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberAppointeeDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberBankAccountDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberNomineeDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.service.RenewalTMPMemberService;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBatchDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/renewalmember")
public class RenewalPolicyTMPMemberController {

	@Autowired
	private RenewalTMPMemberService renewalTMPMemberService;

	@GetMapping("/memberviewdetails/{tmpPolicyId}")
	public ApiResponseDto<List<RenewalPolicyTMPMemberDto>> memberViewDetail(
			@PathVariable(value = "tmpPolicyId") Long tmpPolicyId) {
		return renewalTMPMemberService.memberViewDetail(tmpPolicyId);
	}

	@GetMapping("/memberdetail/{memberId}")
	public ApiResponseDto<RenewalPolicyTMPMemberDto> MemberDetail(@PathVariable(value = "memberId") Long memberId) {
		return renewalTMPMemberService.MemberDetail(memberId);

	}

	@PostMapping("uploadmemberaddress/{id}")
	public ApiResponseDto<RenewalPolicyTMPMemberDto> uploadMemberAddress(
			@RequestBody RenewalPolicyTMPMemberAddressDto renewalPolicyTMPMemberAddressDto,
			@PathVariable(value = "id") Long id) {
		return renewalTMPMemberService.uploadMemberAddress(renewalPolicyTMPMemberAddressDto, id);
	}

	@PostMapping("uploadtempmemberbank/{memberId}")
	public ApiResponseDto<RenewalPolicyTMPMemberDto> uploadTempMemberBank(
			@RequestBody RenewalPolicyTMPMemberBankAccountDto renewalPolicyTMPMemberBankAccountDto,
			@PathVariable(value = "memberId") Long memberId) {
		return renewalTMPMemberService.uploaTempdMemberBank(renewalPolicyTMPMemberBankAccountDto, memberId);
	}

	@PostMapping("uploadtempmemberappointee/{nomineeId}/{memberId}")
	public ApiResponseDto<RenewalPolicyTMPMemberDto> uploadTempMemberAppointee(
			@RequestBody RenewalPolicyTMPMemberAppointeeDto renewalPolicyTMPMemberAppointeeDto,
			@PathVariable(value = "nomineeId") Long nomineeId, @PathVariable(value = "memberId") Long memberId) {
		return renewalTMPMemberService.uploadTempMemberAppointee(renewalPolicyTMPMemberAppointeeDto, nomineeId,
				memberId);
	}

	@PostMapping("renewalimport")
	public ApiResponseDto<MemberBulkResponseDto> importMemberData(@RequestBody RenewalMemberImportDto renewalMemberImportDto) {
		return renewalTMPMemberService.importMemberData(renewalMemberImportDto);
	}

	@GetMapping("tempmembercount/{tempPolicyId}")
	public ApiResponseDto<MemberBatchDto> memberTotalCount(@PathVariable(value = "tempPolicyId") Long tempPolicyId) {
		return renewalTMPMemberService.memberTotalCount(tempPolicyId);
	}

	@PostMapping("/memberdetailsnominee/{memberId}")
	public ApiResponseDto<RenewalPolicyTMPMemberDto> getMemberDetailNominee(
			@RequestBody RenewalPolicyTMPMemberNomineeDto renewalPolicyTMPMemberNomineeDto,
			@PathVariable(value = "memberId") Long memberId) {
		return renewalTMPMemberService.getMemberDetailNominee(renewalPolicyTMPMemberNomineeDto, memberId);
	}

	// Delete Member Details
	@DeleteMapping("/deleteMemberDetails/{id}")
	public ApiResponseDto<List<RenewalPolicyTMPMemberDto>> delete(@PathVariable(value = "id") Long id) {
		return renewalTMPMemberService.delete(id);
	}

	@DeleteMapping("deleteMemberAddress/{id}")
	public ApiResponseDto<List<RenewalPolicyTMPMemberAddressDto>> deleteMemberAddress(
			@PathVariable(value = "id") Long id) {
		return renewalTMPMemberService.deleteMemberAddress(id);
	}

	@DeleteMapping("deleteMemberAppointee/{id}")
	public ApiResponseDto<List<RenewalPolicyTMPMemberAppointeeDto>> deleteMemberAppointee(
			@PathVariable(value = "id") Long id) {
		return renewalTMPMemberService.deleteMemberAppointee(id);
	}

	@DeleteMapping("deleteMemberBankAccount/{id}")
	public ApiResponseDto<List<RenewalPolicyTMPMemberBankAccountDto>> deleteMemberBankAccount(
			@PathVariable(value = "id") Long id) {

		return renewalTMPMemberService.deleteMemberBankAccount(id);
	}

	@DeleteMapping("deleteMemberNominee/{id}")
	public ApiResponseDto<List<RenewalPolicyTMPMemberNomineeDto>> deleteMemberNominee(
			@PathVariable(value = "id") Long id) {

		return renewalTMPMemberService.deleteMemberNominee(id);
	}
	@PutMapping(value = "memberBankDetails/{memberBankId}")
	public ApiResponseDto<RenewalPolicyTMPMemberDto> memberBankDetailsUpdate(@PathVariable("memberBankId") Long id,
			@RequestBody RenewalPolicyTMPMemberBankAccountDto renewalPolicyTMPMemberBankAccountDto) {
		return renewalTMPMemberService.memberBankDetailsUpdate(id, renewalPolicyTMPMemberBankAccountDto);
	}
	@PutMapping("memberdetailsnomineeupdate/{id}")
	public ApiResponseDto<RenewalPolicyTMPMemberDto> updatememberdetailsnominee (@PathVariable("id") Long id,
			@RequestBody RenewalPolicyTMPMemberNomineeDto renewalPolicyTMPMemberNomineeDto){
				return renewalTMPMemberService.updatememberdetailsnominee(id,renewalPolicyTMPMemberNomineeDto);
	}
	
	@PutMapping("uploadtempmemberappointeeupdate/{id}")
	public ApiResponseDto<RenewalPolicyTMPMemberDto> uploadtempmemberappointeeupdate (@PathVariable("id") Long id,
				@RequestBody RenewalPolicyTMPMemberAppointeeDto renewalPolicyTMPMemberAppointeeDto){
				return renewalTMPMemberService.uploadtempmemberappointeeupdate(id,renewalPolicyTMPMemberAppointeeDto);
	}
	
}
