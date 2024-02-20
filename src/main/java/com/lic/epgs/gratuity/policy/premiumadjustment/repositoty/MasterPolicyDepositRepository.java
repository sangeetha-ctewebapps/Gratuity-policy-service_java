package com.lic.epgs.gratuity.policy.premiumadjustment.repositoty;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyDepositEntity;

public interface MasterPolicyDepositRepository extends JpaRepository<MasterPolicyDepositEntity, Long>{

	List<MasterPolicyDepositEntity> findBymasterPolicyId(Long policyId);

	@Query(value = "SELECT * FROM  PMST_DEPOSIT WHERE IS_ACTIVE = 1 AND TMP_POLICY_ID = ?1",nativeQuery=true)
	List<MasterPolicyDepositEntity> findBytmpPolicyId(Long tmpPolicyId);

	
	@Modifying
	@Query(value="UPDATE PMST_DEPOSIT SET IS_ACTIVE = 0 where CONTRIBUTION_DETAIL_ID IN (select CONTRIBUTION_DETAIL_ID from pmst_contribution_detail where master_policy_id =?1 AND ENTRY_TYPE = 'NB')",nativeQuery = true)
	void isactivefalse(Long id);
	
	@Query(value="Select * PMST_DEPOSIT where CONTRIBUTION_DETAIL_ID IN (select CONTRIBUTION_DETAIL_ID from pmst_contribution_detail where master_policy_id =?1 AND ENTRY_TYPE = 'RE')",nativeQuery = true)
	List<MasterPolicyDepositEntity> findBymasterPolicyIdandType(Long id);

}
