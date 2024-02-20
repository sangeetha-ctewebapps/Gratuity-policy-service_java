package com.lic.epgs.gratuity.policyservices.merger.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.claim.dto.GratuityCalculationsDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalQuotationSearchDTo;
import com.lic.epgs.gratuity.policyservices.merger.dto.CommonResponseDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.PolicyLevelMergerApiResponse;
import com.lic.epgs.gratuity.policyservices.merger.dto.PolicyLevelMergerDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.PolicyMergerDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.datatable.GetPolicyMergerDetailsRequestDataTableDto;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.DataTable;

public interface PolicyMergerService {

	PolicyLevelMergerApiResponse saveAndUpdatePolicyLevelMerger(PolicyMergerDto policyMergerDto);

	PolicyLevelMergerApiResponse sendToChecker(PolicyLevelMergerDto policyLevelMergerDto);

	PolicyLevelMergerApiResponse sendToMaker(PolicyLevelMergerDto policyLevelMergerDto);

	PolicyLevelMergerApiResponse rejectedMerger(PolicyLevelMergerDto policyLevelMergerDto);

	PolicyLevelMergerApiResponse approvedPolicyLevelMerger(PolicyLevelMergerDto policyLevelMergerDto);

	DataTable getPolicyMergerDetailsDataTable(
			GetPolicyMergerDetailsRequestDataTableDto getPolicyMergerDetailsRequestDataTableDto);

	Object contributionTofund(Long policyId, String financialYear);

	ApiResponseDto<List<RenewalPolicyTMPDto>> inprogressAndExistingMergeDetails(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo);

	PolicyLevelMergerApiResponse getAllSourcePolicyDetails(String policyNumber);

	CommonResponseDto getFinancialYeartDetails(String date);
	
	public CommonResponseDto commonStatus();

	

//	PolicyLevelMergerApiResponse uploadDocument(PolicyServiceDocumentDto docsDto);

}