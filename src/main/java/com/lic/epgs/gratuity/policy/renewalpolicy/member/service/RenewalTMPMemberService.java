package com.lic.epgs.gratuity.policy.renewalpolicy.member.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalMemberImportDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberAddressDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberAppointeeDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberBankAccountDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberNomineeDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBatchDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;

public interface RenewalTMPMemberService {

	ApiResponseDto<List<RenewalPolicyTMPMemberDto>> memberViewDetail(Long tmpPolicyId);

	ApiResponseDto<RenewalPolicyTMPMemberDto> MemberDetail(Long tmpPolicyId);


	ApiResponseDto<MemberBatchDto> memberTotalCount(Long tempPolicyId);

	ApiResponseDto<RenewalPolicyTMPMemberDto> getMemberDetailNominee(
			RenewalPolicyTMPMemberNomineeDto renewalPolicyTMPMemberNomineeDto, Long memberId);

	
    ApiResponseDto<RenewalPolicyTMPMemberDto> uploaTempdMemberBank(RenewalPolicyTMPMemberBankAccountDto renewalPolicyTMPMemberBankAccountDto, Long id);
	
	
	ApiResponseDto<RenewalPolicyTMPMemberDto> uploadTempMemberAppointee(RenewalPolicyTMPMemberAppointeeDto renewalPolicyTMPMemberAppointeeDto, Long nomineeId,Long memberId );

	
	ApiResponseDto<RenewalPolicyTMPMemberDto> uploadMemberAddress(RenewalPolicyTMPMemberAddressDto renewalPolicyTMPMemberAddressDto, Long id);

	ApiResponseDto<List<RenewalPolicyTMPMemberDto>> delete(Long id);

	ApiResponseDto<List<RenewalPolicyTMPMemberAddressDto>> deleteMemberAddress(Long id);

	ApiResponseDto<List<RenewalPolicyTMPMemberAppointeeDto>> deleteMemberAppointee(Long id);

	ApiResponseDto<List<RenewalPolicyTMPMemberBankAccountDto>> deleteMemberBankAccount(Long id);

	ApiResponseDto<List<RenewalPolicyTMPMemberNomineeDto>> deleteMemberNominee(Long id);

	ApiResponseDto<RenewalPolicyTMPMemberDto> updatememberdetailsnominee(Long id,
			RenewalPolicyTMPMemberNomineeDto renewalPolicyTMPMemberNomineeDto);

	ApiResponseDto<RenewalPolicyTMPMemberDto> uploadtempmemberappointeeupdate(Long id,
			RenewalPolicyTMPMemberAppointeeDto renewalPolicyTMPMemberAppointeeDto);
	
	ApiResponseDto<RenewalPolicyTMPMemberDto> memberBankDetailsUpdate(Long id,
			RenewalPolicyTMPMemberBankAccountDto renewalPolicyTMPMemberBankAccountDto);

	ApiResponseDto<MemberBulkResponseDto> importMemberData(RenewalMemberImportDto renewalMemberImportDto);
}
