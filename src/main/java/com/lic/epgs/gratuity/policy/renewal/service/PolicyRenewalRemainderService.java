package com.lic.epgs.gratuity.policy.renewal.service;

import java.io.IOException;
import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.renewal.dto.EmailRequestDto;
import com.lic.epgs.gratuity.policy.renewal.dto.PolicyRenewalRemainderDto;

public interface PolicyRenewalRemainderService {

	ApiResponseDto<PolicyRenewalRemainderDto> createrenewal(PolicyRenewalRemainderDto policyRenewalRemainderDto);

	ApiResponseDto<?> sendMailForPolicy(Long policyId,Long quotationId) throws IOException;

	String sendMailForPolicyMail(EmailRequestDto emailRequestDto) throws IOException;
}
