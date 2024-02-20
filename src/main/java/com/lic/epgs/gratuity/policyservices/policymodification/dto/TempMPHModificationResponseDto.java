package com.lic.epgs.gratuity.policyservices.policymodification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TempMPHModificationResponseDto {

	public TempMPHModificationResponseDto() {
	}

	public TempMPHModificationResponseDto(RenewalPolicyTMPEntity policyDetails, TempMPHEntity mphMaster) {

		this.policyDetails = policyDetails;
		this.mphMaster = mphMaster;

	}

	private RenewalPolicyTMPEntity policyDetails;

	@JsonIgnoreProperties({ "mphAddresses", "mphBank", "mphRepresentatives" })
	private TempMPHEntity mphMaster;

}
