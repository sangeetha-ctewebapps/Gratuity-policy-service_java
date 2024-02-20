package com.lic.epgs.gratuity.policy.valuationmatrix.repository;

import java.util.Optional;

/**
 * @author Ismail Khan A
 *
 */

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;

public interface PolicyValuationMatrixRepository extends JpaRepository<PolicyValuationMatrixEntity, Long> {
	Optional<PolicyValuationMatrixEntity> findByPolicyId(Long policyId);
}
