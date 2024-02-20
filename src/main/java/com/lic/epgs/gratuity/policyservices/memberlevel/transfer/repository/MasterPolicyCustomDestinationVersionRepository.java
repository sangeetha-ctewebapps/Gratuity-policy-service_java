package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.List;

import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicySearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicySearchEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.MasterPolicyDestinationVersionEntity;

public interface MasterPolicyCustomDestinationVersionRepository {


	MasterPolicyEntity setTransientValues(MasterPolicyEntity masterPolicyEntity);
	
	
}
