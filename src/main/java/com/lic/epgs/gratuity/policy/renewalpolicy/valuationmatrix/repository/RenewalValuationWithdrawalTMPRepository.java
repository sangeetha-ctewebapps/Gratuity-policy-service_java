package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationWithdrawalTMPEntity;

public interface RenewalValuationWithdrawalTMPRepository extends JpaRepository<RenewalValuationWithdrawalTMPEntity, Long> {


	

	List<RenewalValuationWithdrawalTMPEntity> deleteBytmpPolicyId(Long policyId);

	@Query(value = "SELECT * FROM PMST_TMP_VALUATWITHDRAWALRATE  WHERE TMP_POLICY_ID=?1 ORDER BY FROM_AGE_BAND  ASC ",nativeQuery = true)
	List<RenewalValuationWithdrawalTMPEntity> findBytmpPolicyId(Long policyId);
	@Modifying
	@Query(value ="UPDATE PMST_TMP_VALUATWITHDRAWALRATE SET IS_ACTIVE=0 WHERE POLICY_ID=?1",nativeQuery = true)
	void updateisActivefalse(Long id);

	
	@Query(value="SELECT min(RATE) || '% to ' || max(rate) || '%'  FROM PMST_TMP_VALUATWITHDRAWALRATE ptv WHERE TMP_POLICY_ID  =?",nativeQuery = true)

	String findMinAndMax(Long tmpPolicyId);

	
}
