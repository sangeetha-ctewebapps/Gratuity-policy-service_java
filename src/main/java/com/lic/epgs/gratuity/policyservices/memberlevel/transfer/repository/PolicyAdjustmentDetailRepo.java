package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.entity.PolicyAdjustmentDetailEntity;

public interface PolicyAdjustmentDetailRepo extends JpaRepository<PolicyAdjustmentDetailEntity, Long>{

	List<PolicyAdjustmentDetailEntity> deleteBycontributionDetailId(Long id);
	
	@Query(value="Select COUNT(*) from PSTG_ADJUSTMENT_DETAIL where CONTRIBUTION_DETAIL_ID=?1",nativeQuery = true)
	int findByContributionId(Long id);
	
	@Query(value="Select * from PSTG_ADJUSTMENT_DETAIL where contribution_detail_id in"
			+ "(Select contribution_detail_id from PSTG_CONTRIBUTION_DETAIL where tmp_Policy_Id = ?1 AND ENTRY_TYPE = 'RE')",nativeQuery = true)
	PolicyAdjustmentDetailEntity findBytmpPolicyId(Long id);

	List<PolicyAdjustmentDetailEntity> findBydepositId(Long id);





	

}
