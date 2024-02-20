package com.lic.epgs.gratuity.policyservices.conversion.dao;

import java.util.List;

import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalQuotationSearchDTo;
import com.lic.epgs.gratuity.policyservices.conversion.dto.datatable.GetPolicyConversionDetailsRequestDataTableDto;
import com.lic.epgs.gratuity.policyservices.conversion.dto.datatable.GetPolicyConversionDetailsResponseDataTableDto;

public interface PolicyConversionDao {

	List<GetPolicyConversionDetailsResponseDataTableDto> getPolicyConversionDetailsDataTable(
			GetPolicyConversionDetailsRequestDataTableDto getPolicyConversionDetailsRequestDataTableDto);

	 List<RenewalPolicyTMPDto> inprogressAndExistingConversionDetails(RenewalQuotationSearchDTo renewalQuotationSearchDTo);

	List<RenewalPolicyTMPDto> inprogressAndExistingConversionProcessing(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo);

}
