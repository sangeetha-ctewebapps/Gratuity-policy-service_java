package com.lic.epgs.gratuity.policyservices.contributionadjustment.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.PolicyServiceRepository;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.dto.ContributionAdjustmentPropsDto;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.dto.ContributionAdjustmentSearchDto;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.service.ContributionAdjustmentService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/contributionadjustment")
public class ContributionAdjustmentController {
	@Autowired
	private PolicyServiceRepository policyServiceRepository;	
	
	@Autowired
	private ContributionAdjustmentService contributionAdjustmentService;
	
	@Autowired
	private FundService fundService;
	
	@PostMapping("createcontribadjustment/{masterPolicyId}")
	public ApiResponseDto<ContributionAdjustmentPropsDto> createcontributionadjustment(
			@RequestBody PolicyContributionDetailDto policyContributionDetailDto,@PathVariable (value="masterPolicyId")Long masterPolicyId) {
		return contributionAdjustmentService.createcontributionadjustment(policyContributionDetailDto,masterPolicyId);
	}
	
	@PutMapping("updatecontribadjustment/{tmpPolicyId}")
  public ApiResponseDto<ContributionAdjustmentPropsDto> updatecontribadjustment(
	 		@RequestBody PolicyContributionDetailDto policyContributionDetailDto,@PathVariable (value="tmpPolicyId")Long tmpPolicyId) {
		return contributionAdjustmentService.updatecontribadjustment(policyContributionDetailDto,tmpPolicyId);
	}
	
	@PostMapping("contributionfiltersearch")
	public ApiResponseDto<List<ContributionAdjustmentPropsDto>> contributionfiltersearch(
			@RequestBody ContributionAdjustmentSearchDto contributionAdjustmentPropsDto) {
		return contributionAdjustmentService.contributionfiltersearch(contributionAdjustmentPropsDto);

	}	
	
	@GetMapping(value = "/getInprogressforService/{masterPolicyId}/{tmpPolicyId}/{servicetype}")
	public ApiResponseDto<RenewalPolicyTMPDto> getInprogressforService(
			@PathVariable("masterPolicyId") Long masterPolicyId,
			@PathVariable("tmpPolicyId") Long tmpPolicyId,@PathVariable ("servicetype") String servicetype){
		return contributionAdjustmentService.getInprogressforService(masterPolicyId, tmpPolicyId,servicetype);
	}
	
	//sent to checker and send to maker
	@PutMapping("contriAdjustStatusChange/{contridjustpropsId}/{statusId}")
	public ApiResponseDto<ContributionAdjustmentPropsDto> contriAdjustStatusChange(@PathVariable("contridjustpropsId") Long contridjustpropsId,
			@PathVariable("statusId") Long StatusId, @RequestBody ContributionAdjustmentPropsDto tempPolicyClaimPropsDto) {
		return contributionAdjustmentService.contriAdjustStatusChange(contridjustpropsId, StatusId, tempPolicyClaimPropsDto);
	}
	
	
	@PostMapping("/contributionadjustforReject/{contridjustpropsId}/{username}")   //quotationforReject
	public ApiResponseDto<ContributionAdjustmentPropsDto> contributionadjustforReject (@PathVariable("contridjustpropsId") Long contridjustpropsId, @RequestBody ContributionAdjustmentPropsDto contributionAdjustmentPropsDto,
			@PathVariable("username") String username ){
    return contributionAdjustmentService.sentPolicyforReject(contridjustpropsId,username,contributionAdjustmentPropsDto);
	
	}
	
	//APPROVE
	@PostMapping("/approve/{contridjustpropsId}/{username}")
	public ApiResponseDto<PolicyDto> approve (@PathVariable("contridjustpropsId") Long contridjustpropsId, @PathVariable("username") String username ){
		ApiResponseDto<PolicyDto> tt = contributionAdjustmentService.approve(contridjustpropsId,username);
		fundService.setCreditEntries(tt.getData().getId(), new Date());
		return tt;
	}
	
	@GetMapping("getrenewalstatus/{masterPolicyId}")
	public Boolean getrenewalstatus (@PathVariable ("masterPolicyId") Long masterPolicyId) {
		Boolean isRenewalStatus=false;
		int policyServiceEntitiy = policyServiceRepository.checkforpolicyrenewalsuccessstatus(masterPolicyId,"renewals");	
		if(policyServiceEntitiy>0) {
			isRenewalStatus=true;
		}
		return isRenewalStatus;
	}
	

}
