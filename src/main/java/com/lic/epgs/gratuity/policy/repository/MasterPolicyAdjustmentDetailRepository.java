package com.lic.epgs.gratuity.policy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.entity.MasterPolicyAdjustmentDetailEntity;

public interface MasterPolicyAdjustmentDetailRepository extends JpaRepository<MasterPolicyAdjustmentDetailEntity, Long>{
	@Modifying
	@Query(value="UPDATE PMST_ADJUSTMENT_DETAIL SET IS_ACTIVE = 0 where CONTRIBUTION_DETAIL_ID IN (select CONTRIBUTION_DETAIL_ID from pmst_contribution_detail where master_policy_id =?1 AND ENTRY_TYPE = 'NB')",nativeQuery = true)
	void isactivefalse(Long id);

}
