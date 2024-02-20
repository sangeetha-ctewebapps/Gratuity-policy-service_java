package com.lic.epgs.gratuity.policyservices.contributionadjustment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policyservices.contributionadjustment.entity.ContributionAdjustmentPropsEntity;

@Repository
public interface ContributionAdjustmentPropsRepository extends JpaRepository<ContributionAdjustmentPropsEntity, Long> {

	@Query(value="select count(*) from  pmst_contri_adj_props tt1  join pmst_tmp_policy ptp on ptp.policy_id = tt1.temp_policy_id where ptp.master_policy_id=?1 and tt1.is_active=1",nativeQuery = true)
	int findpmstpolicyexitforcontriadjust(Long pmstPolicyId);

	ContributionAdjustmentPropsEntity findBytmpPolicyId(Long masterTmpPolicyId);

}
