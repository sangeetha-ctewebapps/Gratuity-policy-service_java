package com.lic.epgs.gratuity.policy.valuation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.valuation.entity.PolicyMasterValuationEntity;

public interface PolicyMasterValuationRepository extends JpaRepository<PolicyMasterValuationEntity, Long>{

	Optional<PolicyMasterValuationEntity> findByPolicyId(Long id);

	Optional<PolicyMasterValuationEntity> findBypolicyId(Long masterPolicyId);

	
	
	
}
