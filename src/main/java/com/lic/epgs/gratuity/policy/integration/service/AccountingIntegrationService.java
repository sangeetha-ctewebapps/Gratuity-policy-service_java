package com.lic.epgs.gratuity.policy.integration.service;


import com.lic.epgs.gratuity.policy.integration.dto.AccountingIntegrationRequestDto;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.integration.dto.SuperAnnuationResponseModel;

public interface AccountingIntegrationService {
	
	SuperAnnuationResponseModel getMphAndIcodeDetail(AccountingIntegrationRequestDto accountingIntegrationRequestDto);
	
	ResponseDto commonmasterserviceUnitByCode(String unitCode);

	SuperAnnuationResponseModel getMphAndIcodeDetails(AccountingIntegrationRequestDto accountingIntegrationRequestDto);

}
