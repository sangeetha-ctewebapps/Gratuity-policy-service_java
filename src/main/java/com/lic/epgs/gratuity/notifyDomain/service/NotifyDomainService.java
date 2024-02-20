package com.lic.epgs.gratuity.notifyDomain.service;

import com.lic.epgs.gratuity.notifyDomain.dto.CommonResponseDto;
import com.lic.epgs.gratuity.notifyDomain.dto.NotifyDomainDto;

public interface NotifyDomainService {
	
	CommonResponseDto notifyDomain(NotifyDomainDto notifyDomainDto);

}
