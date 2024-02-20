package com.lic.epgs.gratuity.policyservices.common.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceStatusResponseDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PremiumGstRefundDto;
import com.lic.epgs.gratuity.policyservices.common.dto.LeavingMemberDto;

public interface PolicyServicingCommonService {
	
	public PolicyServiceStatusResponseDto savePolicyService(PolicyServiceDto policyServiceDto);
	
//	public PolicyServiceCommonResponseDto getServiceDetailsByServiceId(Long serviceId);

	public Object getActiveServiceDetailsByPolicy(String policyNumber, String currentService);
	
	public ApiResponseDto<List<PolicyServiceDto>> getActiveServices(Long masterPolicyId, Long tmpPolicyId);

	public PremiumGstRefundDto getRefundablePremiumAndGST(Long masterPolicyId, LeavingMemberDto LeavingMembersDto);

	/*

	public String getSequence(String type);

	public PolicyServiceStatusResponseDto policyServicingStatus();

	public PolicyServiceCommonResponseDto checkService(Long policyId, String serviceType);

	public PolicyServiceCommonResponseDto initiateService(PolicyServiceDto policyServiceDto, String serviceType);

	public PolicyServiceCommonResponseDto startService(PolicyServiceDto policyServiceDto, String serviceType);

	public PolicyServiceCommonResponseDto endService1(Long policyId, Long serviceId);


	public PolicyServiceCommonResponseDto getServiceDetailsByPolicyId(Long policyId);

	public PolicyServiceCommonResponseDto endService(PolicyServiceDto policyServiceDto, String serviceType);

//	public PolicyServiceCommonResponseDto generateServiceId(PolicyServiceDto policyServiceDto);

//	public PolicyServiceDto startServicecheck(PolicyServiceDto policyServiceDto,String serviceType);

	PolicyServiceCommonResponseDto generateServiceId(PolicyServiceDto policyLevelServiceDto);
*/
}
