package com.lic.epgs.gratuity.policy.renewalpolicy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.QuotationRenewalDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyNewSearchFilterDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.service.RenewalPolicyService;
import com.lic.epgs.gratuity.quotation.dto.QuotationPDFGenerationDto;



@RestController
@CrossOrigin("*")
@RequestMapping("api/renewal")
public class RenewalQuotationController {
	
	
	@Autowired
	private RenewalPolicyService renewalPolicyService;
	
	
	@PostMapping("/createrenewalforquotation")
	public ApiResponseDto<RenewalPolicyTMPDto> CreateRenewalforQuotation (@RequestBody QuotationRenewalDto quotationRenewalDto){
    return renewalPolicyService.CreateRenewalforQuotation(quotationRenewalDto);
	
	}
	

	@PostMapping("/quotationforApprove/{id}/{username}")
	public ApiResponseDto<RenewalPolicyTMPDto> forApprove (@PathVariable("id") Long id,
			@PathVariable("username") String username){
    return renewalPolicyService.forApprove(id,username);
	
	}
	

	@PostMapping("/renewalprocessingsentForApproval/{id}/{username}") // //quotationsentForApproval
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyForApproval (@PathVariable("id") Long id,
		@PathVariable("username") String username ){
		
     return renewalPolicyService.sentPolicyForApproval(id,username );
	
	}
	
	@PostMapping("/renewalprocessingsendBacktoMaker/{id}/{username}")  //quotationsendBacktoMaker
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyBacktoMaker (@PathVariable("id") Long id,
			@PathVariable("username") String username ){
     return renewalPolicyService.sentPolicyBacktoMaker(id,username);
	
	}
	
	@PostMapping("/renewalprocessingforApprove/{tempid}/{username}")
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforApprove (@PathVariable("tempid") Long tempid,
			@PathVariable("username") String username ){
    return renewalPolicyService.sentPolicyforApprove(tempid,username );
	
	}
	@PostMapping("/renewalprocessingforReject/{id}/{username}")   //quotationforReject
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforReject (@PathVariable("id") Long id, @RequestBody RenewalPolicyTMPDto renewalPolicyTMPDto,
			@PathVariable("username") String username ){
    return renewalPolicyService.sentPolicyforReject(id,username,renewalPolicyTMPDto);
	
	}
	
	@PutMapping("updaterenewalquotationbasic/{id}")
	public ApiResponseDto<RenewalPolicyTMPDto> updaterenewalquotationbasic(@PathVariable (value="id") Long id,@RequestBody QuotationRenewalDto quotationRenewalDto){
		return renewalPolicyService.updaterenewalquotationbasic(id,quotationRenewalDto);
	}

	@GetMapping(value = "getpolicytmpdetail/{id}")
	public  ApiResponseDto<RenewalPolicyTMPDto> getpolicyTmpId(@PathVariable("id") Long id) {
		return renewalPolicyService.getpolicyTmpId(id);
	}
	
	@GetMapping(value = "generatepdf/{tmpPolicyId}")
	public ApiResponseDto<List<QuotationPDFGenerationDto>> ReportRenewalQuotationPDF(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return renewalPolicyService.generateReportRenewalQuotationPDF(tmpPolicyId);
	}
	
	
	@PostMapping(value="filterfornewsearchrenewal")
	public ApiResponseDto<List<RenewalPolicyNewSearchFilterDto>> filterForRenewal(@RequestBody RenewalPolicyNewSearchFilterDto renewalPolicyNewSearchFilterDto){
		return renewalPolicyService.filterForRenewal(renewalPolicyNewSearchFilterDto);
		
	}
	@PostMapping("/quotationsentForApproval/{id}/{username}")
	public ApiResponseDto<RenewalPolicyTMPDto> sentForApproval (@PathVariable("id") Long id,
			@PathVariable("username") String username ){
   return renewalPolicyService.sentForApproval(id,username);
	}
	@PostMapping("/quotationsendBacktoMaker/{id}/{username}")
	public ApiResponseDto<RenewalPolicyTMPDto> sendBacktoMaker (@PathVariable("id") Long id,
			@PathVariable("username") String username){
    return renewalPolicyService.sendBacktoMaker(id,username);
	
	}
  @PostMapping("/quotationforReject/{id}/{username}")
	public ApiResponseDto<RenewalPolicyTMPDto> forReject (@PathVariable("id") Long id,
			@PathVariable("username") String username){
    return renewalPolicyService.forReject(id,username);	
	}
  
  @PostMapping("/ARDstatus/{masterpolicyid}")
  public Boolean ARDStatus(@PathVariable("masterpolicyid") Long masterpolicyid) {
	return renewalPolicyService.ARDStatus(masterpolicyid);
	  
  }
  @GetMapping(value="getrenewalsubstatus/{quotationSubStatusId}")
  public Long getRenewalSubStatus(@PathVariable("quotationSubStatusId") Long quotationSubStatusId) {
	return renewalPolicyService.getRenewalSubStatus(quotationSubStatusId);
	  
  }
  
  @DeleteMapping(value="discardRenewal/{renewalTmpPolicyId}")
  public Boolean discardRenewal(@PathVariable("renewalTmpPolicyId") Long renewalTmpPolicyId) {
	return renewalPolicyService.discardRenewal(renewalTmpPolicyId);
  }
	
	
}
