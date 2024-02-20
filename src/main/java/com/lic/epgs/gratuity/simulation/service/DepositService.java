package com.lic.epgs.gratuity.simulation.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositDto;
import com.lic.epgs.gratuity.simulation.dto.DepositAdjustementDto;
import com.lic.epgs.gratuity.simulation.dto.DepositDto;

/**
 * @author Gopi
 *
 */

public interface DepositService {
	ApiResponseDto<List<DepositDto>> findAllByProposalNumber(String proposalNumber);

    ApiResponseDto<List<DepositAdjustementDto>> GetDepositAdjustment(Long ld);

	ApiResponseDto<List<DepositDto>> findAllByPolicyNumber(String policyNumber);

	ApiResponseDto<List<DepositAdjustementDto>> AdjustmentBasedTmppolicy(Long tmpPolicyId);
}
