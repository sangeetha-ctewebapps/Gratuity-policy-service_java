package com.lic.epgs.gratuity.policy.repository.custom;

import java.util.List;

import com.lic.epgs.gratuity.policy.claim.entity.PolicyTempSearchEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicySearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicySearchEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.MasterPolicyDestinationVersionEntity;

public interface MasterPolicyCustomRepository {

	List<MasterPolicyEntity> findNotInPolicyRenewal(String value);
	List<MasterPolicyEntity> findNotInPolicyRenewalRemainder(String value);
	List<MasterPolicyEntity> fetchpolicyDetails();
	List<MasterPolicyEntity> findByPolicyNumber(String policyNumber);
	MasterPolicyEntity findPolicyDetail(String policyNumber);
	MasterPolicyEntity findBypolicyNumber(String policyNumber);
	List<MasterPolicyEntity> findPolicyDetailSeerch(String policyNumber);
	MasterPolicyEntity findByPolicyNumberisactive(String policyNumber);
	List<MasterPolicyEntity> findByPolicyNumberwithUnitcode(String policyNumber, String unitcode);
	List<MasterPolicyEntity> findBypolicyNumberandActive(String policyNumber);
	MasterPolicyEntity findBymasterPolicyId(Long masterPolicyId, String dateOfExit);
	MasterPolicyEntity findByGreaterStartDateandExitDate(Long masterPolicyId, String dateOfExit);
	MasterPolicyEntity findById(Long masterPolicyId);
	MasterPolicyEntity setTransientValues(MasterPolicyEntity masterPolicyEntity);
	List<MasterPolicyEntity> findByPolicyNumberValidation(String policyNumber);
	RenewalPolicySearchEntity setTransientValues(RenewalPolicySearchEntity masterPolicyEntity);
	PolicyTmpSearchEntity setTransientValue(PolicyTmpSearchEntity policySearch);
	MasterPolicySearchEntity setTransientValues(MasterPolicySearchEntity masterPolicyEntity);
	RenewalPolicyTMPDto setTransientValues(RenewalPolicyTMPDto renewalPolicyTMPDto);
	PolicyTempSearchEntity setTransientValue(PolicyTempSearchEntity policySearch);
	
	
}
