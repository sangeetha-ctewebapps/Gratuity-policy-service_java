package com.lic.epgs.gratuity.policyservices.conversion.controller;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.fund.service.FundService;
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
import com.lic.epgs.gratuity.policyservices.conversion.service.PolicyConversionService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/policyLevelConversion/")
public class PolicyLevelConversionController {

	
	@Autowired
	PolicyConversionService policyLevelConversionService;
	
	@Autowired
	FundService fundService;
	
	
	
	@PostMapping(value = "/savePolicyConversionDetails")
	ResponseEntity<Object> savePolicyConversionDetails(@RequestBody PolicyConversionDto policyLevelConversionDto) {
		return ResponseEntity.ok(policyLevelConversionService.savePolicyConversionDetails(policyLevelConversionDto));
	}
	
	@PostMapping(value = "/sendToChecker")
	ResponseEntity<Object> sendToChecker(@RequestParam String conversionId,@RequestParam String modifiedBy) {
		return ResponseEntity.ok(policyLevelConversionService.sendToChecker(conversionId, modifiedBy));
	}
	
//	@GetMapping(value = "/getInprogressPolicyConversionDetailsList")
//	ResponseEntity<Object> getInprogressPolicyConversionDetailsList(@RequestParam String role,String unitCode) {
//		return ResponseEntity.ok(policyLevelConversionService.getInprogressPolicyConversionDetailsList(role, unitCode));
//	}
	
	@PostMapping("/sendToMaker")
	ResponseEntity<Object> sendToMaker(@RequestParam String conversionId,@RequestParam String modifiedBy) {
		return ResponseEntity.ok(policyLevelConversionService.sendToMaker(conversionId, modifiedBy));
	}
	
	@PostMapping("/approved")
	public CommonResponseDto<PolicyLevelConversionDto> approvedPolicyLevelConversion(
			@RequestBody PolicyLevelConversionDto policyLevelConversionDto) {
		return policyLevelConversionService.approvedPolicyLevelConversion(policyLevelConversionDto);
	}
	
	
	@PostMapping("/rejected")
	public CommonResponseDto<PolicyLevelConversionDto> rejectedConversion(
			@RequestBody PolicyLevelConversionDto policyLevelConversionDto) {
		return policyLevelConversionService.rejectedConversion(policyLevelConversionDto);
	}
	
//	@GetMapping(value = "/getExistingPolicyConversionDetailsList")
//	ResponseEntity<Object> getExistingPolicyConversionDetailsList(@RequestParam String role,String unitCode) {
//		return ResponseEntity.ok(policyLevelConversionService.getExistingPolicyConversionDetailsList(role, unitCode));
//	}
	
	
	@RequestMapping(value = "/getPolicyConversionDetailsDataTable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPolicyConversionDetailsRequestDataTableDto(
			@RequestBody GetPolicyConversionDetailsRequestDataTableDto getPolicyConversionDetailsRequestDataTableDto) {
		return new ResponseEntity<>(policyLevelConversionService.getPolicyConversionDetailsDataTable(getPolicyConversionDetailsRequestDataTableDto),
				 HttpStatus.OK);
	}

	//From Renewal  search policy
	@PostMapping("/newPSDetails")
	public ApiResponseDto<List<PolicyDto>> newPSDetails(@RequestBody RenewalPolicySearchDto renewalPolicySearchDto) {

		return policyLevelConversionService.fetchPolicyForPS(renewalPolicySearchDto);
	}
	
	//From Renewal  search policy With Filter Criteria
	@PostMapping("inExistPSDetails")
	public ApiResponseDto<List<RenewalPolicyTMPDto>> inExistPSDetails(@RequestBody RenewalQuotationSearchDTo renewalQuotationSearchDTo){
	
		return policyLevelConversionService.fetchPolicyForPSSearch(renewalQuotationSearchDTo);
	}
	
	
	//From Renewal  search policy With Filter Criteria
		@PostMapping("inExistPSConversionDetails")
		public  ApiResponseDto<List<RenewalPolicyTMPDto>> inprogressAndExistingConversionDetails(@RequestBody RenewalQuotationSearchDTo renewalQuotationSearchDTo){
		
			return policyLevelConversionService.inprogressAndExistingConversionDetails(renewalQuotationSearchDTo);
		}
		
		//From Renewal  search policy With Filter Criteria
				@PostMapping("inExistPSConversionProcessing")
				public  ApiResponseDto<List<RenewalPolicyTMPDto>> inprogressAndExistingConversionProcessing(@RequestBody RenewalQuotationSearchDTo renewalQuotationSearchDTo){
				
					return policyLevelConversionService.inprogressAndExistingConversionProcessing(renewalQuotationSearchDTo);
				}
	
//	new save API
	@PostMapping("/createrenewalforquotation")
	public ApiResponseDto<RenewalPolicyTMPDto> CreatePolicyLevelConversion (@RequestBody QuotationRenewalDto quotationRenewalDto){
    return policyLevelConversionService.CreatePolicyLevelConversion(quotationRenewalDto);
	
	}
	
	@PutMapping(value = "/saveValuationType/{valuationType}/{conversionId}")
	ResponseEntity<Object> updateValuationType(@PathVariable String valuationType,@PathVariable Long conversionId) {
		return ResponseEntity.ok(policyLevelConversionService.updateValuationType(valuationType,conversionId));
	}
	
	@GetMapping(value = "/getByConversionId/{id}")
	ResponseEntity<Object> getByConversionId(@PathVariable Long id) {
		return ResponseEntity.ok(policyLevelConversionService.getByConversionId(id));
	}

	@PostMapping(value = "/conversionProcessingApprove/{tempid}/{username}")
	public ApiResponseDto<PolicyDto> conversionProcessingApprove(@PathVariable("tempid") Long tempid,
			@PathVariable("username") String username) {
		ApiResponseDto<PolicyDto> tt = policyLevelConversionService.conversionProcessingApprove(tempid, username);
		fundService.setCreditEntries(tt.getData().getId(), new Date());
		return tt;
	}
	
	@GetMapping(value = "/checkActiveQuatation/{policyNumber}/{policyId}")
	ResponseEntity<CheckActiveQuatation> checkActiveQuatation(@PathVariable String policyNumber,@PathVariable Long policyId) {
		return ResponseEntity.ok(policyLevelConversionService.checkActiveQuatation(policyNumber, policyId));
	}

	@GetMapping(value = "/quotationDetective/{policyNumber}/{policyId}")
	ResponseEntity<CheckActiveQuatation> quotationDetective(@PathVariable String policyNumber,@PathVariable Long policyId) {
		return ResponseEntity.ok(policyLevelConversionService.quotationDetective(policyNumber, policyId));
	}

}
