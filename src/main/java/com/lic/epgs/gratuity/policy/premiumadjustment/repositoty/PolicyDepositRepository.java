package com.lic.epgs.gratuity.policy.premiumadjustment.repositoty;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;


@Repository
public interface PolicyDepositRepository extends JpaRepository<PolicyDepositEntity, Long> {

	@Query(value="SELECT * FROM PSTG_DEPOSIT pd WHERE pd.POLICY_ID =:id ORDER  BY AVAILABLE_AMOUNT DESC",nativeQuery = true)
	List<PolicyDepositEntity> findBypolicyId(@Param("id") Long id);

	@Query(value = "SELECT * FROM  PSTG_DEPOSIT WHERE IS_ACTIVE = 1 AND TMP_POLICY_ID = ?1", nativeQuery= true)
	List<PolicyDepositEntity> findBytmpPolicyId(Long tmpPolicyId);

	List<PolicyDepositEntity> deleteBypolicyId(Long stagingPolicyID);

	@Query(value="UPDATE PSTG_DEPOSIT SET IS_ACTIVE=0 WHERE TMP_POLICY_ID =?1",nativeQuery = true)
	List<PolicyDepositEntity> updateIsActiveFalsePolicyId(Long masterTmpPolicyId);

	List<PolicyDepositEntity> deleteBytmpPolicyId(Long masterTmpPolicyId);

	@Query(value="Select * from pstg_deposit where contribution_detail_id in"
			+ "(Select contribution_detail_id from PSTG_CONTRIBUTION_DETAIL where tmp_Policy_Id = ?1 AND ENTRY_TYPE = 'RE')",nativeQuery = true)
	List<PolicyDepositEntity> findBymasterPolicyIdandType(Long id);
	
	@Query(value="Select * from pstg_deposit where contribution_detail_id in"
			+ "(Select contribution_detail_id from PSTG_CONTRIBUTION_DETAIL where tmp_Policy_Id = ?1 AND ENTRY_TYPE = 'TR')",nativeQuery = true)
	List<PolicyDepositEntity> findBymasterPolicyIdandEntryType(Long id);
	
	@Query(value="Select * from pstg_deposit where contribution_detail_id in"
			+ "(Select contribution_detail_id from PSTG_CONTRIBUTION_DETAIL where tmp_Policy_Id = ?1 AND ENTRY_TYPE = ?2)",nativeQuery = true)
	List<PolicyDepositEntity> findBymasterPolicyIdandEntryType(Long id,String type);
	
	@Query(value="Select * from pstg_deposit where contribution_detail_id in"
			+ "(Select contribution_detail_id from PSTG_CONTRIBUTION_DETAIL where tmp_Policy_Id = ?1 AND ENTRY_TYPE = 'MJ')",nativeQuery = true)
	List<PolicyDepositEntity> findBymasterPolicyIdandTypeMJ(Long id);

	List<PolicyDepositEntity> findBycontributionDetailId(Long id);
	
	@Query(value="Select * from pstg_deposit where contribution_detail_id in"
			+ "(Select contribution_detail_id from PSTG_CONTRIBUTION_DETAIL where tmp_Policy_Id = ?1 AND ENTRY_TYPE = 'ME')",nativeQuery = true)
	List<PolicyDepositEntity> findBymasterPolicyIdandTypeME(Long masterTmpPolicyId);
}
