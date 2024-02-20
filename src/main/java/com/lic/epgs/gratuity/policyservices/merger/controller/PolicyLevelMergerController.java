package com.lic.epgs.gratuity.policyservices.merger.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalQuotationSearchDTo;
import com.lic.epgs.gratuity.policyservices.merger.dto.CommonResponseDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.PolicyLevelMergerApiResponse;
import com.lic.epgs.gratuity.policyservices.merger.dto.PolicyLevelMergerDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.PolicyMergerDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.SearchSourcePolicyDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.datatable.GetPolicyMergerDetailsRequestDataTableDto;
import com.lic.epgs.gratuity.policyservices.merger.service.PolicyMergerService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policyLevelMerger")
public class PolicyLevelMergerController {

	protected final Logger logger = LogManager.getLogger(getClass());
	@Autowired
	private PolicyMergerService policyLevelMergerService;

	@PostMapping("/savePolicyLevelMerge")
	public PolicyLevelMergerApiResponse saveAndUpdatePolicyLevelMerger(
			@RequestBody PolicyMergerDto policyMergerDto) {
		return policyLevelMergerService.saveAndUpdatePolicyLevelMerger(policyMergerDto);
	}
	
	@PostMapping("/sendToChecker")
	public PolicyLevelMergerApiResponse sendToChecker(@RequestBody PolicyLevelMergerDto policyLevelMergerDto) {
		return policyLevelMergerService.sendToChecker(policyLevelMergerDto);
	}
	
	@PostMapping("/sendToMaker")
	public PolicyLevelMergerApiResponse sendToMaker(@RequestBody PolicyLevelMergerDto policyLevelMergerDto) {
		return policyLevelMergerService.sendToMaker(policyLevelMergerDto);
	}
	
	@PostMapping("/rejected")
	public PolicyLevelMergerApiResponse rejectedMerger(
			@RequestBody PolicyLevelMergerDto policyLevelMergerDto) {
		return policyLevelMergerService.rejectedMerger(policyLevelMergerDto);
	}
	
	@PostMapping("/approved")
	public PolicyLevelMergerApiResponse approvedPolicyLevelMerger(
			@RequestBody PolicyLevelMergerDto policyLevelMergerDto) {
		return policyLevelMergerService.approvedPolicyLevelMerger(policyLevelMergerDto);
	}
	
//	@PostMapping("/uploadDocument")
//	public PolicyLevelMergerApiResponse uploadDocument(@RequestBody PolicyServiceDocumentDto docsDto) {
//		return policyLevelMergerService.uploadDocument(docsDto);
//	}
	
	@RequestMapping(value = "/getPolicyMergerDetailsDataTable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPolicyMergerDetailsRequestDataTableDto(
			@RequestBody GetPolicyMergerDetailsRequestDataTableDto getPolicyMergerDetailsRequestDataTableDto) {
		return new ResponseEntity<>(policyLevelMergerService.getPolicyMergerDetailsDataTable(getPolicyMergerDetailsRequestDataTableDto),
				 HttpStatus.OK);
	}
	
	@GetMapping(value = "transactionEntries/{policyId}/{financialYear}")
	public ResponseEntity<Object> contributionTofund(@PathVariable ("policyId") Long policyId,@PathVariable ("financialYear") String financialYear ) {
		return new ResponseEntity<>(policyLevelMergerService.contributionTofund(policyId,financialYear),HttpStatus.OK);
	}
	
	//From Renewal  search policy With Filter Criteria
			@PostMapping("inExistPSMergeDetails")
			public  ApiResponseDto<List<RenewalPolicyTMPDto>> inprogressAndExistingMergeDetails(@RequestBody RenewalQuotationSearchDTo renewalQuotationSearchDTo){
			
				return policyLevelMergerService.inprogressAndExistingMergeDetails(renewalQuotationSearchDTo);
			}
	
	@GetMapping(value = "/fetchDetails/{policyNumber}")
	public PolicyLevelMergerApiResponse searchSourcePolicy(@PathVariable ("policyNumber") String policyNumber){
		return policyLevelMergerService.getAllSourcePolicyDetails(policyNumber);
	}
	
	
	@GetMapping("getFinancialYeartDetails")
	public CommonResponseDto getFinancialYeartDetails(@RequestParam String date) throws Exception {
		logger.info("CommonController -- saveSampleFile --started");
		CommonResponseDto commonDto = policyLevelMergerService.getFinancialYeartDetails(date);
		logger.info("CommonController -- saveSampleFile --ended");
		return commonDto;
	}
	
	@GetMapping(value = { "commonStatus" })
	public ResponseEntity<Object> commonStatus() {
		return ResponseEntity.ok().body(policyLevelMergerService.commonStatus());
	}
			
}