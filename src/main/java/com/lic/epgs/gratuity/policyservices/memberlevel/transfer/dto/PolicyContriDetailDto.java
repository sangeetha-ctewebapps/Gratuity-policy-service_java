package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.simulation.dto.DepositAdjustementDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyContriDetailDto {
	private Long transferRequisitionId;
	private String createdBy;
	private Long tmpPolicyId;
	private String ModifiedBy;

	private List<DepositAdjustementRequest> depositAdjustementRequest;

}
