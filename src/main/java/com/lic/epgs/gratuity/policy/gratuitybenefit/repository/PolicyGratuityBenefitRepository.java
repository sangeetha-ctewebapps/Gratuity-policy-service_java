package com.lic.epgs.gratuity.policy.gratuitybenefit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import org.springframework.web.bind.annotation.RequestParam;


public interface PolicyGratuityBenefitRepository extends JpaRepository<PolicyGratuityBenefitEntity, Long> {

    @Query(value = "SELECT * FROM PMST_GRATUITY_BENEFIT WHERE POLICY_ID=?1 and IS_ACTIVE=1 ORDER BY GRATUITY_BENEFIT_ID", nativeQuery = true)
    List<PolicyGratuityBenefitEntity> findBypolicyId(Long policyId);


    @Modifying
    @Query(value = "Update PMST_GRATUITY_BENEFIT set IS_ACTIVE=1 where POLICY_ID=?1", nativeQuery = true)
    void updateIsActive(Long pmstPolicyId);

    @Query(value = "SELECT * FROM PMST_GRATUITY_BENEFIT WHERE POLICY_ID=:policyId and CATEGORY_ID=:categoryId and IS_ACTIVE=1 ORDER BY GRATUITY_BENEFIT_ID", nativeQuery = true)
    List<PolicyGratuityBenefitEntity> findBypolicyIdAndCategoryId(@RequestParam("policyId") Long policyId, @RequestParam("categoryId") Long categoryId);

}
