package com.lic.epgs.gratuity.policy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;

@Repository
public interface StagingPolicyRepository extends JpaRepository<StagingPolicyEntity, Long> {

Optional<StagingPolicyEntity> findByMasterQuotationId(Long masterQuotationId);
	
	Optional<StagingPolicyEntity> findByPolicyNumber(String policyNumber);
	
	@Query(value="SELECT MAX(TO_NUMBER(POLICY_NUMBER)) AS MAXPOLICYNUMBER FROM PSTG_POLICY", nativeQuery =true)
	Long maxPolicyNumber();

	StagingPolicyEntity findByIdAndIsActiveTrue(Long id);

	StagingPolicyEntity findBypolicyNumber(String policyNumber);

	@Query(value="SELECT * FROM PSTG_POLICY pp WHERE pp.PROPOSAL_NUMBER =?1",nativeQuery = true)
	List<StagingPolicyEntity> findinProposalnumberexitpolicynoJpaRepo(String proposalNumber);
	
	@Query(value = "SELECT PROPOSAL_NUMBER FROM PSTG_POLICY WHERE POLICY_NUMBER =?1",nativeQuery = true)
	String getProposaNumber(String policyNo);

	@Query(value = "SELECT COUNT(*) FROM  PSTG_POLICY  WHERE POLICY_SUB_STATUS_ID =?1",nativeQuery = true)
	Long getCountPolicySubStatus(Long policySubStatusId);
	
}
