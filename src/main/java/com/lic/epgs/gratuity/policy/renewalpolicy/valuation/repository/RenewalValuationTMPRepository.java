package com.lic.epgs.gratuity.policy.renewalpolicy.valuation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.entity.RenewalValuationTMPEntity;

public interface RenewalValuationTMPRepository extends JpaRepository<RenewalValuationTMPEntity, Long> {

	RenewalValuationTMPEntity findBytmpPolicyId(Long tmpPolicyId);

	@Modifying
	@Query(value ="UPDATE PMST_TMP_VALUATION SET IS_ACTIVE=0 WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void updateisActivefalse(Long id);

	@Modifying
	@Query(value ="DELETE FROM PMST_TMP_VALUATION WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void deleteBytmpPolicyId(Long id);

}
