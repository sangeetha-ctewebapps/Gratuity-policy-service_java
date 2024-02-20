package com.lic.epgs.gratuity.policy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;

public interface PolicyHistoryRepository extends JpaRepository<PolicyHistoryEntity, Long>{


	List<PolicyHistoryEntity> findBymasterPolicyId(Long pmstPolicyId);

	@Query(value="SELECT TO_CHAR(MIN(POLICY_START_DATE),'DD/MM/YYYY') FROM PMST_HIS_POLICY WHERE MASTER_POLICY_ID = ?1", nativeQuery = true)
	String findPolicyMinStartDate(Long masterPolicyId);

	@Query(value="SELECT * FROM PMST_HIS_POLICY php WHERE php.MASTER_POLICY_ID =?1 AND TO_DATE (?2,'dd/MM/yyyy')  >= php.POLICY_START_DATE AND TO_DATE(?2,'dd/MM/yyyy') <= php.POLICY_END_DATE",nativeQuery = true)
	PolicyHistoryEntity findBymasterPolicyId(Long pmstPolicyId, String dateOfExit);



	@Query(value="SELECT * FROM PMST_HIS_POLICY php WHERE php.MASTER_POLICY_ID =?1 AND TO_DATE (?2,'dd/MM/yyyy')  > php.POLICY_END_DATE ",nativeQuery = true)
	List<PolicyHistoryEntity> findBymasterPolicyIdandDateofExit(Long masterPolicyId, String date);
}
