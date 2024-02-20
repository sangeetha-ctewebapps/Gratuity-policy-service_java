package com.lic.epgs.gratuity.policy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;

@Repository
public interface MasterPolicyContributionDetailRepository extends JpaRepository<MasterPolicyContributionDetails, Long> {

	
	MasterPolicyContributionDetails findBymasterPolicyId(Long id);

	@Modifying
	@Query(value="UPDATE pmst_contribution_detail SET IS_ACTIVE = 0 where master_policy_id = ?1 AND ENTRY_TYPE = 'NB'",nativeQuery = true)
	void isacivefalse(Long id);

	@Query(value="Select * pmst_contribution_detail where master_policy_id = ?1 AND ENTRY_TYPE = 'RE'",nativeQuery = true)
	MasterPolicyContributionDetails findBymasterPolicyIdandType(Long id);
	
	@Query(value=" Select * from ( SELECT * FROM PMST_CONTRIBUTION_Detail where master_policy_id=?1 ORDER BY contribution_detail_id desc) where rownum = 1",nativeQuery = true)
	MasterPolicyContributionDetails findBygetlatestmasterPolicyIdrecord(Long masterPolicyId);

	@Query(value="Select * pmst_contribution_detail where master_policy_id = ?1 AND ENTRY_TYPE = ?2",nativeQuery = true)
	MasterPolicyContributionDetails findBymasterPolicyIdandType(Long sourcemasterPolicyId, String entryType);
	
	@Query(value="Select * from pmst_contribution_detail where master_policy_id = ?1 AND ENTRY_TYPE = 'NB'",nativeQuery = true)
	MasterPolicyContributionDetails findBymasterPolicyIDandType(Long masterPolicyId);
	
}
