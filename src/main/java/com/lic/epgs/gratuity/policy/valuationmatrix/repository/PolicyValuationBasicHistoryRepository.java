package com.lic.epgs.gratuity.policy.valuationmatrix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationBasicHistoryEntity;

public interface PolicyValuationBasicHistoryRepository extends JpaRepository<PolicyValuationBasicHistoryEntity, Long> {
	
	Optional<PolicyValuationBasicHistoryEntity> findBypolicyId(Long id);

}
