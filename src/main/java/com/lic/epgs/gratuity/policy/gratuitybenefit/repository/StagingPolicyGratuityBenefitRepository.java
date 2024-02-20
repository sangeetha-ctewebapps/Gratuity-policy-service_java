package com.lic.epgs.gratuity.policy.gratuitybenefit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.StagingPolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;

public interface StagingPolicyGratuityBenefitRepository extends JpaRepository<StagingPolicyGratuityBenefitEntity, Long>{

	@Query(value = "SELECT * FROM PSTG_GRATUITY_BENEFIT WHERE POLICY_ID=?1 and IS_ACTIVE=1 ORDER BY GRATUITY_BENEFIT_ID",nativeQuery = true)
	List<StagingPolicyGratuityBenefitEntity> findByPolicyId(Long policyId);

}
