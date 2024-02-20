package com.lic.epgs.gratuity.policy.premiumadjustment.repositoty;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContributionEntity;

@Repository
public interface PolicyContributionRepository extends JpaRepository<PolicyContributionEntity, Long> {

	List<PolicyContributionEntity> findBypolicyId(Long id);

	List<PolicyContributionEntity> deleteBypolicyId(Long stagingPolicyID);

	@Query(value = "UPDATE PSTG_CONTRIBUTION SET IS_ACTIVE=0 WHERE TMP_POLICY_ID =?1",nativeQuery=true)
	List<PolicyContributionEntity> updateIsActiveFalsePolicyId(Long masterTmpPolicyId);



	List<PolicyContributionEntity> deleteBytmpPolicyId(Long masterTmpPolicyId);

	List<PolicyContributionEntity> findBytmpPolicyId(Long id);

	@Query(value="SELECT MAX(TO_NUMBER(VERSION_NO)) AS VERSIONNUMBER FROM pstg_contribution where FINANCIAL_YEAR=?1 and contribution_detail_id=?2",nativeQuery=true)
	Long getMaxVersion(String financialYear, Long contributionDetailId);

	List<PolicyContributionEntity> findByContributionDetailId(Long id);

	@Query(value = " Select * from (\r\n"
			+ "           SELECT Closing_balance FROM PMST_CONTRIBUTION where master_policy_id=?1 ORDER BY contribution_id desc)\r\n"
			+ "           where rownum = 1",nativeQuery=true)
	Double getLastClosingBalance(Long pmstPolicyId);
}
