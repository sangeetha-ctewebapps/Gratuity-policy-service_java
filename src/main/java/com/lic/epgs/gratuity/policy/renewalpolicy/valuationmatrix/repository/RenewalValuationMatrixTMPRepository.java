package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;

public interface RenewalValuationMatrixTMPRepository extends JpaRepository<RenewalValuationMatrixTMPEntity, Long> {

	Optional<RenewalValuationMatrixTMPEntity> findBytmpPolicyId(Long policyId);
	
	RenewalValuationMatrixTMPEntity findAllBytmpPolicyId(Long policyId);

	@Modifying
	@Query(value ="UPDATE PMST_TMP_VALUATIONMATRIX SET IS_ACTIVE=0 WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void updateisActivefalse(Long id);

	@Modifying
	@Query(value ="DELETE FROM PMST_TMP_VALUATIONMATRIX WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void deleteBytmpPolicyId(Long id);

	
//	@Query(value = "SELECT * FROM PMST_TMP_VALUATIONMATRIX ptv WHERE ptv.TMP_POLICY_ID = ?1",nativeQuery = true)
//	Optional<RenewalValuationBasicTMPEntity> findBytmpPolicyId(Long policyId);

}