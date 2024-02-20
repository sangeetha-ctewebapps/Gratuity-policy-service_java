package com.lic.epgs.gratuity.policyservices.conversion.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.integration.dto.CommonResponseDto;
import com.lic.epgs.gratuity.policy.renewal.dto.RenewalPolicySearchDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.QuotationRenewalDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalQuotationSearchDTo;
import com.lic.epgs.gratuity.policyservices.conversion.dto.CheckActiveQuatation;
import com.lic.epgs.gratuity.policyservices.conversion.dto.PolicyConversionDto;
import com.lic.epgs.gratuity.policyservices.conversion.dto.PolicyLevelConversionDto;
import com.lic.epgs.gratuity.policyservices.conversion.dto.datatable.GetPolicyConversionDetailsRequestDataTableDto;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.DataTable;

public interface PolicyConversionService {

	CommonResponseDto<PolicyConversionDto> savePolicyConversionDetails(PolicyConversionDto policyConversionDto);

	CommonResponseDto<PolicyLevelConversionDto> sendToChecker(String conversionId, String modifiedBy);

//	CommonResponseDto<List<PolicyLevelConversionDto>> getInprogressPolicyConversionDetailsList(String role, String unitCode);

	//CommonResponseDto<List<PolicyLevelConversionDto>> getExistingPolicyConversionDetailsList(String role, String unitCode);

	CommonResponseDto<PolicyLevelConversionDto> approvedPolicyLevelConversion(PolicyLevelConversionDto policyLevelConversionDto);

	CommonResponseDto<PolicyLevelConversionDto> sendToMaker(String conversionId, String modifiedBy);

	CommonResponseDto<PolicyLevelConversionDto> rejectedConversion(PolicyLevelConversionDto policyLevelConversionDto);

	DataTable getPolicyConversionDetailsDataTable(
			GetPolicyConversionDetailsRequestDataTableDto getPolicyConversionDetailsRequestDataTableDto);

	ApiResponseDto<List<PolicyDto>> fetchPolicyForPS(RenewalPolicySearchDto renewalPolicySearchDto);

	ApiResponseDto<List<RenewalPolicyTMPDto>> fetchPolicyForPSSearch(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo);

	ApiResponseDto<RenewalPolicyTMPDto> CreatePolicyLevelConversion(QuotationRenewalDto quotationRenewalDto);

	
	ApiResponseDto<List<RenewalPolicyTMPDto>> inprogressAndExistingConversionDetails(RenewalQuotationSearchDTo renewalQuotationSearchDTo);

	Object updateValuationType(String valuationType, Long conversionId);

	Object getByConversionId(Long id);

	ApiResponseDto<List<RenewalPolicyTMPDto>> inprogressAndExistingConversionProcessing(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo);

	ApiResponseDto<PolicyDto> conversionProcessingApprove(Long tempid, String username);

	CheckActiveQuatation checkActiveQuatation(String policyNumber, Long policyId);

	CheckActiveQuatation quotationDetective(String policyNumber, Long policyId);

	
}
