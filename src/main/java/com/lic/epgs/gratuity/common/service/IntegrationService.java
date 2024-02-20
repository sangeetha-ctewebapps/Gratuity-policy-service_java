/**
 * 
 */
package com.lic.epgs.gratuity.common.service;

import org.springframework.web.multipart.MultipartFile;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;

/**
 * @author Uthayakumar Chelliah
 *
 */
public interface IntegrationService {

	ApiResponseDto<MemberBulkResponseDto> uploadMemberData(Long quotationId, String username, Long tmpPolicyId, MultipartFile file);

	ApiResponseDto<MemberBulkResponseDto> uploadMemberDataClaim(Long pmstpolicyId, String username, MultipartFile file);
	
	ApiResponseDto<MemberBulkResponseDto> uploadMemberDataAOMService(Long pmstpolicyId, String username,
			MultipartFile file);
	ApiResponseDto<MemberBulkResponseDto> uploadMemberDataDOMService(Long pmstpolicyId, String username, MultipartFile file);
}
