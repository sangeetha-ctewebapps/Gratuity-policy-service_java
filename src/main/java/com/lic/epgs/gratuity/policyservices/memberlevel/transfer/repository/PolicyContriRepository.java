package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContributionEntity;

@Repository
public interface PolicyContriRepository extends JpaRepository<PolicyContributionEntity, Long> {

	List<PolicyContributionEntity> findBypolicyId(Long id);

	List<PolicyContributionEntity> deleteBypolicyId(Long stagingPolicyID);

	@Query(value = "UPDATE PSTG_CONTRIBUTION SET IS_ACTIVE=0 WHERE TMP_POLICY_ID =?1",nativeQuery=true)
	List<PolicyContributionEntity> updateIsActiveFalsePolicyId(Long masterTmpPolicyId);



	List<PolicyContributionEntity> deleteByContributionDetailId(Long contributionDetailId);

	List<PolicyContributionEntity> findBytmpPolicyId(Long id);

	@Query(value="SELECT MAX(TO_NUMBER(VERSION_NO)) AS VERSIONNUMBER FROM pstg_contribution where FINANCIAL_YEAR=?1 and contribution_detail_id=?2",nativeQuery=true)
	Long getMaxVersion(String financialYear, Long contributionDetailId);

}
