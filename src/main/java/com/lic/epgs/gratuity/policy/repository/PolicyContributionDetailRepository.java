package com.lic.epgs.gratuity.policy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;

public interface PolicyContributionDetailRepository extends JpaRepository<PolicyContributionDetailEntity, Long>  {

	List<PolicyContributionDetailEntity> findBymasterQuotationId(Long id);
	
	@Query(value="SELECT * FROM PSTG_CONTRIBUTION_DETAIL WHERE MASTER_QUOTATION_ID=?1",nativeQuery = true)
	PolicyContributionDetailEntity findBymasterQuotationId1(Long masterQuotationId);

	@Query(value="SELECT * FROM PSTG_CONTRIBUTION_DETAIL WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	PolicyContributionDetailEntity findBymasterTmpPolicyId(Long masterTmpPolicyId);

	List<PolicyContributionDetailEntity> findBytmpPolicyId(Long tempPolicyId);

	PolicyContributionDetailEntity findBypolicyId(Long policyId);

	@Modifying
	@Query(value="Delete FROM PSTG_CONTRIBUTION_DETAIL WHERE  MASTER_QUOTATION_ID=:masterQuotationId",nativeQuery = true)
	void deleteByMasterId(@Param("masterQuotationId") Long masterQuotationId);

	
	@Query(value="Select * from PSTG_CONTRIBUTION_DETAIL where TMP_POLICY_ID = ?1 AND ENTRY_TYPE = 'RE'",nativeQuery = true)
	PolicyContributionDetailEntity findBymasterPolicyIdandType(Long id);
	
	@Query(value="Select * from PSTG_CONTRIBUTION_DETAIL where TMP_POLICY_ID = ?1 AND ENTRY_TYPE = 'MJ'",nativeQuery = true)
	PolicyContributionDetailEntity findBymasterPolicyIdandTypeMJ(Long id);

	@Query(value="Select * from PSTG_CONTRIBUTION_DETAIL where TMP_POLICY_ID = ?1 AND ENTRY_TYPE = 'CA'",nativeQuery = true)
	PolicyContributionDetailEntity findByTmpPolicyIdandType(Long id);

	@Query(value="Select * from PSTG_CONTRIBUTION_DETAIL where TMP_POLICY_ID = ?1 AND ENTRY_TYPE = ?2",nativeQuery = true)
	PolicyContributionDetailEntity findByTmpPolicyandType(Long id, String entryType);
	
	@Query(value="Select * from PSTG_CONTRIBUTION_DETAIL where TMP_POLICY_ID = ?1 AND ENTRY_TYPE = 'TR'",nativeQuery = true)
	PolicyContributionDetailEntity findByTmpPolicyIdandTypeTR(Long id);

	
}
