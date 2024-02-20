package com.lic.epgs.gratuity.policy.renewalpolicy.repository.custom;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;

public interface RenewalPolicyTMPCustomRepository {

	List<RenewalPolicyTMPEntity> findByquotationTaggedStatusId(Long quotationTaggedStatusId, String policyNumber);
	List<RenewalPolicyTMPEntity> findBypolicytaggedStatusId(Long policytaggedStatusId);
	RenewalPolicyTMPEntity findBytempidandmasterPolicyId(Long id, Long masterPolicyId);
	List<RenewalPolicyTMPEntity> findByMasterPolicyId(Long masterPolicyId);
	List<RenewalPolicyTMPEntity> findByquotationTaggedStatusIdwithUnit(Long quotationTaggedStatusId,
			String policyNumber, String unitCode);
	List<RenewalPolicyTMPEntity> findByquotationTaggedStatuswithgetUnitCode(Long quotationTaggedStatusId,
			String policyNumber,@Param("getUnitCode") String getUnitCode);
	RenewalPolicyTMPEntity findBypolicyNumber(String policyNumber);
	List<RenewalPolicyTMPEntity> findBypolicytaggedStatusIdwithUnit(Long policytaggedStatusId, String policyNumber,
			String unitCode);
	List<RenewalPolicyTMPEntity> findBypolicytaggedStatusIdwithgetUnitCode(Long policytaggedStatusId,
			String policyNumber, String getUnitCode);
	List<RenewalPolicyTMPEntity> findBypolicytaggedStatusId(Long policytaggedStatusId, String policyNumber);
	RenewalPolicyTMPEntity findByMasterPolicyIdAndAnnualRenewalDate(Long masterPolicyId, Date annualRenewalDate);
	RenewalPolicyTMPEntity findByTmpPolicyId(@Param("tmpPolicyId") Long tmpPolicyId);
	RenewalPolicyTMPEntity setTransientValues(RenewalPolicyTMPEntity renewalPolicyTMPEntity);
	
}
