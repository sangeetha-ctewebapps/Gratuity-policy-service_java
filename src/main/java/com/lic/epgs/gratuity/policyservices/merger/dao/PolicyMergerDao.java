package com.lic.epgs.gratuity.policyservices.merger.dao;

import java.util.List;

import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalQuotationSearchDTo;
import com.lic.epgs.gratuity.policyservices.merger.dto.datatable.GetPolicyMergerDetailsRequestDataTableDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.datatable.GetPolicyMergerDetailsResponseDataTableDto;

public interface PolicyMergerDao {


	List<GetPolicyMergerDetailsResponseDataTableDto> getPolicyMergerDetailsDataTable(
			GetPolicyMergerDetailsRequestDataTableDto getPolicyMergerDetailsRequestDataTableDto);

	List<RenewalPolicyTMPDto> inprogressAndExistingMerge(RenewalQuotationSearchDTo renewalQuotationSearchDTo);

}
