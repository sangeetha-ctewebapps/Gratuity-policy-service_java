package com.lic.epgs.gratuity.policy.schemerule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.schemerule.entity.StagingPolicySchemeRule;

@Repository
public interface StagingPolicySchemeRuleRepository extends JpaRepository<StagingPolicySchemeRule, Long>{

	Optional<StagingPolicySchemeRule> findBypolicyId(Long findBypolicyId);


}