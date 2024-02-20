package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.PolicyDeposit;


@Repository
public interface PolicyDepositRepo extends JpaRepository<PolicyDeposit, Long> {

	@Query(value="SELECT * FROM PSTG_DEPOSIT pd WHERE pd.POLICY_ID =:id ORDER  BY AVAILABLE_AMOUNT DESC",nativeQuery = true)
	List<PolicyDeposit> findBypolicyId(@Param("id") Long id);

	@Query(value = "SELECT * FROM  PSTG_DEPOSIT WHERE IS_ACTIVE = 1 AND TMP_POLICY_ID = ?1", nativeQuery= true)
	List<PolicyDeposit> findBytmpPolicyId(Long tmpPolicyId);

	List<PolicyDeposit> deleteBypolicyId(Long stagingPolicyID);

	@Query(value="UPDATE PSTG_DEPOSIT SET IS_ACTIVE=0 WHERE TMP_POLICY_ID =?1",nativeQuery = true)
	List<PolicyDeposit> updateIsActiveFalsePolicyId(Long masterTmpPolicyId);

	List<PolicyDeposit> deleteByContributionDetailId(Long contributionDetailId);

	@Query(value="Select * from pstg_deposit where contribution_detail_id in"
			+ "(Select contribution_detail_id from PSTG_CONTRIBUTION_DETAIL where tmp_Policy_Id = ?1 AND ENTRY_TYPE = 'RE')",nativeQuery = true)
	List<PolicyDeposit> findBymasterPolicyIdandType(Long id);
	
	@Query(value="Select * from pstg_deposit where contribution_detail_id in"
			+ "(Select contribution_detail_id from PSTG_CONTRIBUTION_DETAIL where tmp_Policy_Id = ?1 AND ENTRY_TYPE = 'CA')",nativeQuery = true)
	List<PolicyDeposit> findBymasterPolicyIdandTypeCA(Long id);
	
	@Query(value="Select * from pstg_deposit where contribution_detail_id in"
			+ "(Select contribution_detail_id from PSTG_CONTRIBUTION_DETAIL where tmp_Policy_Id = ?1 AND ENTRY_TYPE = 'MJ')",nativeQuery = true)
	List<PolicyDeposit> findBymasterPolicyIdandType1(Long id);

	List<PolicyDeposit> findBycontributionDetailId(Long contributionDetailId);
}
