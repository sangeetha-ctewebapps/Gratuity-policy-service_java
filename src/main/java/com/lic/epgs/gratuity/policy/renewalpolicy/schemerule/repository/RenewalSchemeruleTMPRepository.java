package com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.entity.RenewalSchemeruleTMPEntity;
import com.lic.epgs.gratuity.quotation.schemerule.entity.MasterSchemeRuleEntity;
import com.lic.epgs.gratuity.quotation.schemerule.entity.SchemeRuleEntity;

public interface RenewalSchemeruleTMPRepository extends JpaRepository<RenewalSchemeruleTMPEntity, Long> {
	Optional<SchemeRuleEntity> findByPolicyId(Long policyId);

	RenewalSchemeruleTMPEntity findBytmpPolicyId(Long tmpPolicyId);


	@Modifying
	@Query(value ="UPDATE PMST_TMP_SCHEMERULE SET IS_ACTIVE=0 WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void updateisActivefalse(Long id);
	@Modifying
	@Query(value ="Delete from PMST_TMP_SCHEMERULE WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void deleteBytmpPolicyId(Long id);
}
