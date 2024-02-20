package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.PolicyContriDetailEntity;

public interface PolicyContriDetailRepository extends JpaRepository<PolicyContriDetailEntity, Long>  {

	List<PolicyContriDetailEntity> findBymasterQuotationId(Long id);
	
	@Query(value="SELECT * FROM PSTG_CONTRIBUTION_DETAIL WHERE MASTER_QUOTATION_ID=?1",nativeQuery = true)
	PolicyContriDetailEntity findBymasterQuotationId1(Long masterQuotationId);

	@Query(value="SELECT * FROM PSTG_CONTRIBUTION_DETAIL WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	PolicyContriDetailEntity findBymasterTmpPolicyId(Long masterTmpPolicyId);

	List<PolicyContriDetailEntity> findBytmpPolicyId(Long tempPolicyId);

	PolicyContriDetailEntity findBypolicyId(Long policyId);

	@Modifying
	@Query(value="Delete FROM PSTG_CONTRIBUTION_DETAIL WHERE  MASTER_QUOTATION_ID=:masterQuotationId",nativeQuery = true)
	void deleteByMasterId(@Param("masterQuotationId") Long masterQuotationId);

	
	@Query(value="Select * from PSTG_CONTRIBUTION_DETAIL where TMP_POLICY_ID = ?1 AND ENTRY_TYPE = 'RE'",nativeQuery = true)
	PolicyContriDetailEntity findBymasterPolicyIdandType(Long id);

	@Query(value="Select * from PSTG_CONTRIBUTION_DETAIL where TMP_POLICY_ID = ?1 AND ENTRY_TYPE = 'CA'",nativeQuery = true)
	PolicyContriDetailEntity findByTmpPolicyIdandType(Long id);


	
}
