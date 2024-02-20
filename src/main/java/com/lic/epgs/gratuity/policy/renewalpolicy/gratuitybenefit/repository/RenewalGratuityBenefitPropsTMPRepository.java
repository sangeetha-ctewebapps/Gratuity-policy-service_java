package com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalsGratuityBenefitPropsTMPEntity;

public interface RenewalGratuityBenefitPropsTMPRepository extends JpaRepository<RenewalsGratuityBenefitPropsTMPEntity, Long> {

	@Query(value="SELECT * FROM PMST_TMP_GRATUITY_BENFIT_PROPS WHERE GRATUITY_BENEFIT_ID  IN \r\n"
			+ "(SELECT GRATUITY_BENEFIT_ID  FROM PMST_TMP_GRATUITY_BENEFIT WHERE TMP_POLICY_ID =:tmpPolicyId AND CATEGORY_ID IN\r\n"
			+ "( SELECT CATEGORY_ID  FROM PMST_TMP_MEMBER WHERE MEMBER_ID =:tmpMemberId))",nativeQuery = true)
	RenewalsGratuityBenefitPropsTMPEntity findgratuitysalary(Long tmpMemberId,Long tmpPolicyId);

}
