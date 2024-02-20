package com.lic.epgs.gratuity.policyservices.aom.dto;

import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policyservices.aom.entity.PolicyTmpAOMProps;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@ToString
public class AOMPersonalInfoResponse {

	private TempMemberEntity tempMemberEntity;
	
	private RenewalPolicyTMPEntity renewalPolicyTMPEntity;
	
}
