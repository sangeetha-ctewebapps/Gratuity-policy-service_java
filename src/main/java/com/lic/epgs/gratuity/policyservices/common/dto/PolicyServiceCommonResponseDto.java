package com.lic.epgs.gratuity.policyservices.common.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyServiceCommonResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long serviceId;
	private Long policyId;
	private String serviceType;

	private String allow;
	private List<Object> allowList;

	private PolicyServiceDto policyServiceDto;
	private List<PolicyServiceDto> policyServiceDtos;
	private String transactionStatus;
	private String transactionMessage;

	private Boolean isUsing;

}
