package com.lic.epgs.gratuity.policy.schemerule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.quotation.schemerule.entity.SchemeRuleEntity;

@Repository
public interface PolicySchemeRuleRepository extends JpaRepository<PolicySchemeEntity, Long> {

	


	Optional<PolicySchemeEntity> findBypolicyId(Long policyId);

	@Query(value="SELECT * FROM PMST_SCHEMERULE WHERE POLICY_ID =:policyid",nativeQuery = true)
	PolicySchemeEntity findByPolicyId(@Param("policyid") Long policyid);

	

}
