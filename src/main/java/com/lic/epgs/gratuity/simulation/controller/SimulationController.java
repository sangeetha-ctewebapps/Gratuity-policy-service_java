package com.lic.epgs.gratuity.simulation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositDto;
import com.lic.epgs.gratuity.simulation.dto.DepositAdjustementDto;
import com.lic.epgs.gratuity.simulation.dto.DepositDto;
import com.lic.epgs.gratuity.simulation.service.DepositService;

/**
 * @author Vigneshwaran
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/simulation")
public class SimulationController {
	
	@Autowired
	private DepositService depositService;
	
	@GetMapping(value = "/deposits/{proposalNumber}/{policyNumber}")
	public ApiResponseDto<List<DepositDto>> findAllDepositsForCustomer(@PathVariable ("proposalNumber") String proposalNumber,@PathVariable ("policyNumber") String policyNumber) {
		ApiResponseDto<List<DepositDto>> getDepositDto=null;
		if(proposalNumber.equals("null")) {
			getDepositDto = depositService.findAllByPolicyNumber(policyNumber);
		
		}else {
			getDepositDto=depositService.findAllByProposalNumber(proposalNumber);
		}
		return getDepositDto;
	}

	@GetMapping(value = "/adjustment/{Id}")
	public ApiResponseDto<List<DepositAdjustementDto>> GetDepositAdjustment(@PathVariable("Id") Long ld){
		
		return depositService.GetDepositAdjustment(ld);
	}
	
	@GetMapping(value = "/adjustmentbasedtmppolicy/{tmpPolicyId}")
	public ApiResponseDto<List<DepositAdjustementDto>> AdjustmentBasedTmppolicy(@PathVariable("tmpPolicyId") Long tmpPolicyId){
		
		return depositService.AdjustmentBasedTmppolicy(tmpPolicyId);
	}
}

