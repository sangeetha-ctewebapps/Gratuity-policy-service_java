package com.lic.epgs.gratuity.policy.renewalpolicy.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.fund.service.FundService;
import com.lic.epgs.gratuity.mph.dto.TemptMPHDto;
import com.lic.epgs.gratuity.policy.dto.MasterPolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.endorsement.dto.EndorsementDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.PolicySearchFilterDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.QuotationRenewalDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalQuotationSearchDTo;
import com.lic.epgs.gratuity.policy.renewalpolicy.service.RenewalPolicyService;

@RestController
@CrossOrigin("*")
@RequestMapping("api/renewalpolicy")
public class RenewalPolicyController {
	
	@Autowired
	private RenewalPolicyService renewalPolicyService;	
	
	@Autowired
	private FundService fundService;
	
	//quotation tagged status
	
	@PostMapping("renewalSearch")
	public ApiResponseDto<List<RenewalPolicyTMPDto>> fetchPolicyForRenewalQuotation(@RequestBody RenewalQuotationSearchDTo renewalQuotationSearchDTo){
		return renewalPolicyService.fetchPolicyForRenewalQuotation(renewalQuotationSearchDTo);
	}
	
	@PostMapping("renewalInprogressSearch")
	public ApiResponseDto<List<PolicySearchFilterDto>> filter(@RequestBody PolicySearchFilterDto policyTmpServiceDto) {
		return renewalPolicyService.filter(policyTmpServiceDto);
	}
	
	//policyTaggedstatus
	@PostMapping("renewalProcessingSearch")
	public ApiResponseDto<List<RenewalPolicyTMPDto>> processingfilter(@RequestBody RenewalQuotationSearchDTo renewalQuotationSearchDTo){
		return renewalPolicyService.processingfilter(renewalQuotationSearchDTo);	
	}	
	@GetMapping("RenewalGet/{id}")
	public ApiResponseDto<RenewalPolicyTMPDto> findByTMPId(@PathVariable("id") Long id){
		return renewalPolicyService.findByTMPId(id);
	}
	
	@GetMapping("TempMPHDetailsBasedonTempId/{id}")
	public ApiResponseDto<TemptMPHDto> findByTempMPHIdonTempId(@PathVariable("id") Long id){
		return renewalPolicyService.findByTempMPHIdonTempId(id);
	}
	
	
	@GetMapping("downloads/candbsheet/{tmpPolicyId}")
	public ResponseEntity<byte[]> findByPolicyId(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		byte[] tt = null;
		if (tmpPolicyId != 0) {
			tt = renewalPolicyService.findByPolicyId(tmpPolicyId);
		} 

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=CandBsheet.xlsx");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}
	

	@PostMapping(value = "/renewalpolicyapprove/{tempid}/{username}")
	public ApiResponseDto<PolicyDto> renewalpolicyapprove(@PathVariable("tempid") Long tempid,
			@PathVariable("username") String username) {
		ApiResponseDto<PolicyDto> tt = renewalPolicyService.renewalpolicyapprove(tempid, username);
		fundService.setCreditEntries(tt.getData().getId(), new Date());
		return tt;
	}
	
	@PostMapping("renewalInprogressSearchFilters")
	public ApiResponseDto<List<RenewalPolicyTMPDto>> renewalSearchFilters(
			@RequestBody RenewalQuotationSearchDTo renewalQuotationSearchDTo) {
		return renewalPolicyService.renewalSearchFilters(renewalQuotationSearchDTo);
	}

}
