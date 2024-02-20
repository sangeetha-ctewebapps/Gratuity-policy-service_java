package com.lic.epgs.gratuity.policyservices.premiumcollection.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.lic.epgs.gratuity.fund.service.FundService;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.dto.ContributionAdjustmentPropsDto;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.dto.ContributionAdjustmentSearchDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.LifeCoverPremiumCollectionPropsDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.PremiumCollectionDuesDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.PremiumCollectionSearchDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.service.PremiumCollectionService;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/premiumcollection" })
public class PremiumCollectionController {
	
	@Autowired
	private PremiumCollectionService premiumCollectionService;
	
	@Autowired
	private FundService fundService;
	
	
	
	
	@PostMapping("premiumfiltersearch")
	public ApiResponseDto<List<LifeCoverPremiumCollectionPropsDto>> contributionfiltersearch(@RequestBody PremiumCollectionSearchDto 	premiumCollectionSearchDto	) {
		
		
		return premiumCollectionService.contributionfiltersearch(premiumCollectionSearchDto);

	}	
	

	
	//sent to checker and send to maker
	@PutMapping("premiumAdjustStatusChange/{premiumpropsId}/{statusId}")
	public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> premiumAdjustStatusChange(@PathVariable("premiumpropsId") Long premiumpropsId,
			@PathVariable("statusId") Long StatusId, @RequestBody LifeCoverPremiumCollectionPropsDto lcPremiumCollectionPropsDto) {
		//DO like contribution adjustment
		return premiumCollectionService.premiumAdjustStatusChange(premiumpropsId, StatusId, lcPremiumCollectionPropsDto);
	}
	
	@GetMapping("/getpremiumcollectionprops/{premiumpropsId}")
	public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> premiumcollectionprops(@PathVariable("premiumpropsId") Long premiumpropsId){
		return premiumCollectionService.premiumcollectionprops(premiumpropsId);
		
	}
	@GetMapping("/getpremiumcollectionduesonprops/{premiumpropsId}")
	public ApiResponseDto<List<PremiumCollectionDuesDto>> premiumcollectionduesonprops(@PathVariable("premiumpropsId") Long premiumpropsId){
		return premiumCollectionService.premiumcollectionduesonprops(premiumpropsId);
	
		
	}
	
	
	@PostMapping("/premiumcollectionforReject/{premiumpropsId}/{username}")   //quotationforReject
	public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> premiumcollectionforReject (@PathVariable("premiumpropsId") Long premiumpropsId, @RequestBody LifeCoverPremiumCollectionPropsDto lifeCoverPremiumCollectionPropsDto,
			@PathVariable("username") String username ){
		
    return premiumCollectionService.premiumcollectionforReject(premiumpropsId,username,lifeCoverPremiumCollectionPropsDto);
	
	}
	
	@PostMapping("/duescalculation/{masterpolicyId}/{username}")
	public ApiResponseDto<List<PremiumCollectionDuesDto>> duescalculation(@PathVariable("masterpolicyId") Long masterpolicyId,@PathVariable("username") String username){
		
		return premiumCollectionService.duescalculation(masterpolicyId,username);	
	}
	
	
	@PostMapping("createpremiumcollectionAdjustment/{masterPolicyId}")
	public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> createcontributionadjustment(
			@RequestBody PolicyContributionDetailDto policyContributionDetailDto,@PathVariable (value="masterPolicyId")Long masterPolicyId) {
		return premiumCollectionService.createPremiumcollectionAdjustment(policyContributionDetailDto,masterPolicyId);
	}
	
	@PutMapping("updatepremiumcollectionAdjustment/{tmpPolicyId}")
  public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> updatecontribadjustment(
	 		@RequestBody PolicyContributionDetailDto policyContributionDetailDto,@PathVariable (value="tmpPolicyId")Long tmpPolicyId) {
		return premiumCollectionService.updatePremiumcollectionAdjustment(policyContributionDetailDto,tmpPolicyId);
	}
	
	
	//APPROVE
	@PostMapping("/approve/{premcontriadjustpropsid}/{username}")
	public ApiResponseDto<PolicyDto> approve (@PathVariable("premcontriadjustpropsid") Long premcontriadjustpropsid, @PathVariable("username") String username ){
		ApiResponseDto<PolicyDto> tt = premiumCollectionService.approve(premcontriadjustpropsid,username);
	
		return tt;
	}
	
	@GetMapping("get")
	public ApiResponseDto<?> getaal() throws ParseException{
		String fromDate = "01-APR-2023";
        String toDate = "31-MAR-2024";
        String nextDueDate="01-MAY-2023";
        String adjustdate="07-OCT-2023";
        int frequency=3;

        ArrayList<String> quarters = new ArrayList<String>();
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat dfYY = new SimpleDateFormat("yy");
        Calendar nextdue = Calendar.getInstance();
        nextdue.setTime(df.parse(nextDueDate));
        
        Calendar adjustment = Calendar.getInstance();
        adjustment.setTime(df.parse(adjustdate));
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(df.parse(fromDate));

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(df.parse(toDate));

        while (cal1.getTime().after(cal.getTime())) {
            int month = cal.get(Calendar.MONTH) + 1;
            System.out.println(month+"=11");
            int quarter = month % frequency == 0 ? (month / frequency) : (month / frequency) + 1;
           
            System.out.println(quarter+"=22");
            System.out.println(df.format(cal.getTime()));
            if(cal.getTime().compareTo(nextdue.getTime())>=0 && cal.getTime().compareTo(adjustment.getTime())<=0)
            quarters.add("Q" + quarter + "-" + cal.getTime());

            cal.add(Calendar.MONTH, frequency);
        }
        System.out.println(quarters);
		
		return ApiResponseDto.success(null);
	}
	@PostMapping(value="/escalatetozo/{premiumpropsId}/{username}")
	public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> escalateToZo(@PathVariable("premiumpropsId") Long premiumpropsId,@PathVariable("username") String username){
		return premiumCollectionService.escalateToZo(premiumpropsId, username);
	}
	
	 @DeleteMapping(value="discardservice/{propsId}/{moduleType}")
	  public Boolean discardservice(@PathVariable("propsId") Long propsId,@PathVariable("moduleType") String moduleType) {
		return premiumCollectionService.discardservice(propsId,moduleType);
	  }
	
	 @GetMapping(value="checkNextDueDate/{masterPolicyId}")
	 public ApiResponseDto<Boolean> checkNextDueDate (@PathVariable("masterPolicyId") Long masterPolicyId) {
		 return premiumCollectionService.checkNextDueDate(masterPolicyId);
	 }
	
}