package com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;

public interface RenewalGratuityBenefitTMPRepository extends JpaRepository<RenewalGratuityBenefitTMPEntity, Long> {
	
//	@Query(value="SELECT * FROM PMST_TMP_LIFE_COVER  WHERE POLICY_ID =?1 ORDER BY LIFE_COVER_ID",nativeQuery = true)
//	List<RenewalGratuityBenefitTMPEntity> findByPolicyId(Long policyId);

	@Query(value="SELECT * FROM PMST_TMP_GRATUITY_BENEFIT  WHERE TMP_POLICY_ID =?1 and IS_ACTIVE=1 ORDER BY GRATUITY_BENEFIT_ID",nativeQuery = true)
	List<RenewalGratuityBenefitTMPEntity> findBytmpPolicyId(Long policyId);


	@Modifying
	@Query(value ="UPDATE PMST_TMP_GRATUITY_BENEFIT SET IS_ACTIVE=0 WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void updateisActivefalse(Long id);

	@Query(value="SELECT * FROM PMST_TMP_GRATUITY_BENEFIT  WHERE TMP_POLICY_ID =?1 AND CATEGORY_ID =?2 and IS_ACTIVE=1 ORDER BY GRATUITY_BENEFIT_ID",nativeQuery = true)
	List<RenewalGratuityBenefitTMPEntity> findBytmpPolicyIdAndCategoryId(Long tmppolicyid, Long categoryId);


	@Query(value = "SELECT POLICY_ID FROM PMST_TMP_GRATUITY_BENEFIT WHERE TMP_POLICY_ID =:policyId",nativeQuery = true)
	List<Long> findBytmpPolicy(@Param("policyId") Long policyId);


	@Modifying
	@Query(value ="DELETE FROM PMST_TMP_GRATUITY_BENEFIT  WHERE GRATUITY_BENEFIT_ID in( Select GRATUITY_BENEFIT_ID  FROM PMST_TMP_GRATUITY_BENEFIT  WHERE TMP_POLICY_ID=?1)",nativeQuery = true)
	void deleteBytmpPolicyId(Long id);

	@Modifying
	@Query(value = "DELETE FROM PMST_TMP_GRATUITY_BENFIT_PROPS pgbp WHERE GRATUITY_BENEFIT_ID in(Select GRATUITY_BENEFIT_ID  FROM PMST_TMP_GRATUITY_BENEFIT  WHERE TMP_POLICY_ID=?1)", nativeQuery = true)
	void deleteByGratuityBenefitId(Long id);

}





