package com.lic.epgs.gratuity.policy.valuationmatrix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixHistoryEntity;

public interface PolicyValuationMatrixHistoryRepository extends JpaRepository<PolicyValuationMatrixHistoryEntity, Long>{
	Optional<PolicyValuationMatrixHistoryEntity> findBypolicyId(Long id);

}
