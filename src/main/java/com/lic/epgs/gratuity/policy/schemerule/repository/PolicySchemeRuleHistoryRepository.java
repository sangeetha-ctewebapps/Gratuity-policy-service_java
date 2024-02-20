package com.lic.epgs.gratuity.policy.schemerule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeRuleHistoryEntity;

public interface PolicySchemeRuleHistoryRepository extends JpaRepository<PolicySchemeRuleHistoryEntity, Long>{
	Optional<PolicySchemeRuleHistoryEntity> findBypolicyId(Long id);

}
