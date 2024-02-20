package com.lic.epgs.gratuity.policy.renewal.service;

import java.io.IOException;
import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.MasterPolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.renewal.dto.PmstPolicyDto;
import com.lic.epgs.gratuity.policy.renewal.dto.RenewalPolicySearchDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberDto;
import com.lic.epgs.gratuity.policy.schemerule.dto.PolicySchemeRuleDto;

public interface RenewalService {

	byte[] downloadSampleRenewal(Long id) ;

	byte[] downloadSampleDynamic(Long id) ;



	ApiResponseDto<List<PolicyDto>> fetchPolicyForNewRenewalQuotation(RenewalPolicySearchDto renewalPolicySearchDto);

	String getcandbsheetpdf(Long tmpPolicyId) throws IOException ;

	byte[] findBytmpPolicyId(Long tmpPolicyId);

	String renewalNoticepdf(Long id)  throws IOException ;

	String renewalRemainderNoticepdf(Long id) throws IOException;

	ApiResponseDto<List<RenewalPolicyTMPMemberDto>> filter(RenewalPolicyTMPMemberDto renewalPolicyTMPMemberDto,
			String type);


}
