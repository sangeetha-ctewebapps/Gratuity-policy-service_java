package com.lic.epgs.gratuity.policy.valuation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationHistoryEntity;

public interface PolicyValuationHistoryRepository  extends JpaRepository<PolicyValuationHistoryEntity, Long>{
	Optional<PolicyValuationHistoryEntity> findBypolicyId(Long id);

}
