package com.lic.epgs.gratuity.quotation.member.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAddressDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAppointeeDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBankAccountDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBatchDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberNomineeDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberSearchDto;

/**
 * @author Gopi
 *
 */
public interface MemberService {

	ApiResponseDto<MemberBulkResponseDto> getUploadedMemberData(Long quotationId, String type, Long tmpPolicyId);


	ApiResponseDto<MemberBulkResponseDto> importMemberData(Long quotationId, Long batchId, String username);

	ApiResponseDto<List<MemberDto>> findAll(Long quotationId);

	ApiResponseDto<List<MemberDto>> filter(MemberSearchDto memberSearchDto);

	ApiResponseDto<MemberDto> findById(Long memberId, Long taggedStatusId);

	byte[] findAllErrors(Long batchId);

	byte[] downloadSample();
	
	byte[] downloadMidLeaverSample();
	
	byte[] downloadMidLeaverMemberDetail(Long tmpPolicyId);

	ApiResponseDto<MemberBulkResponseDto> deleteBatch(Long batchId);

	ApiResponseDto<MemberDto> uploadMember(MemberDto memberDto);

	ApiResponseDto<MemberDto> updateMember(Long id, MemberDto memberDto);

	ApiResponseDto<List<MemberAddressDto>> uploadMemberAddress(MemberAddressDto memberAddressDto, Long memberId);

	ApiResponseDto<List<MemberAddressDto>> updateMemberAddress(Long id, MemberAddressDto memberAddressDto);

	ApiResponseDto<List<MemberBankAccountDto>> uploadMemberBank(MemberBankAccountDto memberBankAccountDto, Long memberId);

	ApiResponseDto<List<MemberBankAccountDto>> updateMemberBank(Long id, MemberBankAccountDto memberBankAccountDto);

	ApiResponseDto<List<MemberNomineeDto>> uploadMemberNominee(MemberNomineeDto memberNomineeDto, Long memberId);

	ApiResponseDto<List<MemberNomineeDto>> updateMemberNominee(MemberNomineeDto memberNomineeDto, Long id);

	ApiResponseDto<List<MemberAppointeeDto>> uploadMemberAppointee(MemberAppointeeDto memberAppointeeDto, Long nomineeId,
			Long memberId);

	ApiResponseDto<List<MemberAppointeeDto>> updateMemberAppointee(MemberAppointeeDto memberAppointeeDto, Long id);

	

	ApiResponseDto<MemberDto> memberCodeStatus(Long quotationId, String code, Long memberId);

	ApiResponseDto<List<MemberBankAccountDto>> deleteMemberBank(Long id);

	ApiResponseDto<List<MemberAddressDto>> deleteMemberAddress(Long id);

	ApiResponseDto<List<MemberNomineeDto>> deleteMemberNominee(Long id);

	ApiResponseDto<List<MemberAppointeeDto>> deleteMemberAppointee(Long id);

	ApiResponseDto<MemberBatchDto> memberTotalCount(Long quotationId);

	byte[] downloadSampleforCalim();

	ApiResponseDto <List<MemberDto>>delete(Long id);


	byte[] findforclaimsErrors(Long batchId);
	
	ApiResponseDto<Boolean> isEmployeeCodeExist(Long masterPolicyId, Long tmpPolicyId, Long tmpMemberId, String employeeCode);
	ApiResponseDto<Boolean> isEmployeePanExist(Long masterPolicyId, Long tmpPolicyId, Long tmpMemberId, String employeePan);
	ApiResponseDto<Boolean> isEmployeeAadharExist(Long masterPolicyId, Long tmpPolicyId, Long tmpMemberId, String employeeAadhar);


	byte[] findAlldomErrors(Long batchId);

}
