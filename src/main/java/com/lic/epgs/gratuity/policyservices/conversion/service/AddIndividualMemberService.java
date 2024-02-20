package com.lic.epgs.gratuity.policyservices.conversion.service;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.QuotationRenewalDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;

public interface AddIndividualMemberService {
	TempMemberEntity saveTempMember(TempMemberEntity tempMember, Long policyId, Long tempPolicyId);

	TempMemberAddressEntity saveTempMemberAddress(TempMemberAddressEntity memberAddress, Long memberId,
			Long tempPolicyId);

	TempMemberAppointeeEntity saveTempMemberAppointee(TempMemberAppointeeEntity tempMemberAppointee, Long memberId,
			Long tempPolicyId);

	TempMemberBankAccountEntity saveTempMemberBankAccount(TempMemberBankAccountEntity tempMemberBankAccount,
			Long memberId, Long tempPolicyId);

	TempMemberNomineeEntity saveTempMemberNominee(TempMemberNomineeEntity tempMemberNominee, Long memberId,
			Long tempPolicyId);

	ApiResponseDto<RenewalPolicyTMPDto> CreateRenewalforQuotation(QuotationRenewalDto quotationRenewalDto);


}
