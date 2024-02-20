package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;

public interface StagingPolicyCustomDestinationVersionRepository {

	//List<StagingPolicyDestinationVersionEntity> findinProposalnumberexitpolicyno(String proposalNumber);
	StagingPolicyEntity setTransientValues(StagingPolicyEntity stagingPolicyEntity);
	//RenewalPolicyTMPEntity setTransientValues(RenewalPolicyTMPEntity stagingPolicyEntity);
		
}
