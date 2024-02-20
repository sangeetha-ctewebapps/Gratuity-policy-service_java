package com.lic.epgs.gratuity.policy.integration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.policy.integration.dto.AccountingIntegrationRequestDto;
import com.lic.epgs.gratuity.policy.integration.dto.SuperAnnuationResponseModel;
import com.lic.epgs.gratuity.policy.integration.service.AccountingIntegrationService;



/**
 * @author Logesh.D Date:16-09-2022
 *
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/external/accounting/")
public class AccountingIntegrationController {
	
	@Autowired
	AccountingIntegrationService accountingIntegrationService;
	
	
//	@PostMapping(value = { "/getMphAndIcodeDetail", "/getMphAndIcodeDetail" })
//	public ResponseEntity<Object> getMphAndIcodeDetail(@RequestBody AccountingIntegrationRequestDto accountingIntegrationRequestDto) {
//		return ResponseEntity.ok().body(accountingIntegrationService.getMphAndIcodeDetail(accountingIntegrationRequestDto));
//	}
	
	@PostMapping(value = { "/getMphAndIcodeDetail", "/getMphAndIcodeDetail" })
	public ResponseEntity<Object> getMphAndIcodeDetail(@RequestBody AccountingIntegrationRequestDto accountingIntegrationRequestDto) {
		return ResponseEntity.ok().body(accountingIntegrationService.getMphAndIcodeDetail(accountingIntegrationRequestDto));
	}
	
	@PostMapping(value = { "/getMphAndIcodeDetails"})
	public ResponseEntity<Object> getMphAndIcodeDetails(@RequestBody AccountingIntegrationRequestDto accountingIntegrationRequestDto) {
		return ResponseEntity.ok().body(accountingIntegrationService.getMphAndIcodeDetails(accountingIntegrationRequestDto));
	}
	


}
