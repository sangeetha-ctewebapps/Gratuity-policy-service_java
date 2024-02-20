package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;

public interface RenewalValuationBasicTMPRepository extends JpaRepository<RenewalValuationBasicTMPEntity, Long> {

	
	
	RenewalValuationBasicTMPEntity findAllBytmpPolicyId(Long policyId);
	@Query(value="SELECT MAX(TO_NUMBER(REFERENCE_NUMBER)) AS MAXREFERENCENUMBER FROM PMST_TMP_VALUATIONBASIC", nativeQuery =true)
	Long maxReferenceNumber();

	@Modifying
	@Query(value ="UPDATE PMST_TMP_VALUATIONBASIC SET IS_ACTIVE=0 WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void updateisActivefalse(Long id);
	Optional<RenewalValuationBasicTMPEntity> findBytmpPolicyId(Long policyId);
	
	
	@Modifying
	@Query(value ="DELETE from PMST_TMP_VALUATIONBASIC WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void deleteBytmpPolicyId(Long id);
	
	@Query(value ="SELECT * FROM PMST_TMP_VALUATIONBASIC WHERE TMP_POLICY_ID=?1 AND IS_ACTIVE=1",nativeQuery = true)
	RenewalValuationBasicTMPEntity getByTmpPolicyId(Long tmpPolicyId);
	
}
