package com.lic.epgs.gratuity.policy.member.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberSearchDto;
import com.lic.epgs.gratuity.policy.member.dto.TempMemberDto;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;

/**
 * @author Ismail Khan A
 *
 */
public interface PolicyMemberService {
	
	ApiResponseDto<List<PolicyMemberDto>> filter(PolicyMemberSearchDto policyMemberSearchDto, String type);
	ApiResponseDto<PolicyMemberDto> findById(Long memberId, String type);
	ApiResponseDto<TempMemberDto> saveTempMember(Long endorsementId, TempMemberDto tempMemberDto);
	ApiResponseDto<List<TempMemberDto>> getTempMember(Long endorsementId);
	ApiResponseDto<TempMemberDto> updateTempMember(Long id, TempMemberDto tempMemberDto);

}
