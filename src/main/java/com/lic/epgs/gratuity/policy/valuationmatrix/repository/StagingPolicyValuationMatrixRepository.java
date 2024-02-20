package com.lic.epgs.gratuity.policy.valuationmatrix.repository;

import java.util.Optional;

/**
 * @author Ismail Khan A
 *
 */

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValuationMatrixEntity;

public interface StagingPolicyValuationMatrixRepository extends JpaRepository<StagingPolicyValuationMatrixEntity, Long> {
	Optional<StagingPolicyValuationMatrixEntity> findByPolicyId(Long policyId);
}
