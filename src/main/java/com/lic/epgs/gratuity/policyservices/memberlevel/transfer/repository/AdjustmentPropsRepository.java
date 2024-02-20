package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContrySummaryEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.ContriAdjustmentPropsEntity;

public interface AdjustmentPropsRepository extends JpaRepository<ContriAdjustmentPropsEntity, Long> {

	
	@Query(value="select count(*) from  pmst_contri_adj_props tt1  join pmst_tmp_policy ptp on ptp.policy_id = tt1.temp_policy_id where ptp.master_policy_id=?1 and tt1.is_active=1",nativeQuery = true)
	int findpmstpolicyexitforcontriadjust(Long pmstPolicyId);

	ContriAdjustmentPropsEntity findBytmpPolicyId(Long masterTmpPolicyId);
	
	ContriAdjustmentPropsEntity findByPstgContriDetailId(Long pstgContriDetailId);

	
}
