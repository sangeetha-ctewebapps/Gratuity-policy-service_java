package com.lic.epgs.gratuity.fund.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FreelookCancellationReqDto {
	private Long id;
	private Long sourcePolicyId;
	private Long destinationPolicyId;
	private Long sourceMemberId;
	private Long destinationMemberId;
	private Long serviceId;
	private Date serviceDate;
	private String username;
	private String serviceType;
	private String serviceStatusType;
	private String[] licIds;
}
