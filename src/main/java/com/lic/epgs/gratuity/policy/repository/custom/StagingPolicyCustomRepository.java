package com.lic.epgs.gratuity.policy.repository.custom;

import java.util.List;

import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;

public interface StagingPolicyCustomRepository {

	List<StagingPolicyEntity> findinProposalnumberexitpolicyno(String proposalNumber);
	StagingPolicyEntity setTransientValues(StagingPolicyEntity stagingPolicyEntity);
	RenewalPolicyTMPEntity setTransientValues(RenewalPolicyTMPEntity stagingPolicyEntity);

}
