package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContrySummaryEntity;

@Repository
public interface PolicyContrySummaryRepo extends JpaRepository<PolicyContrySummaryEntity, Long> {

	List<PolicyContrySummaryEntity> findBypolicyId(Long id);

	List<PolicyContrySummaryEntity> deleteBypolicyId(Long stagingPolicyID);

	@Query(value= "UPDATE PSTG_CONTRI_SUMMARY SET IS_ACTIVE=0 WHERE TMP_POLICY_ID =?1", nativeQuery=true)
	List<PolicyContrySummaryEntity> updateIsActiveFalsePolicyId(Long masterTmpPolicyId);

	List<PolicyContrySummaryEntity> findBytmpPolicyId(Long id);

	List<PolicyContrySummaryEntity> deleteByContributionDetailId(Long contributionDetailId);

}
