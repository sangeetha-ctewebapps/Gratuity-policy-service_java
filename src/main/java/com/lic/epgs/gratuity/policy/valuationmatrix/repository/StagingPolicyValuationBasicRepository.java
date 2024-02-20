package com.lic.epgs.gratuity.policy.valuationmatrix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValutationBasicEntity;

public interface StagingPolicyValuationBasicRepository extends JpaRepository<StagingPolicyValutationBasicEntity, Long> {

	Optional<StagingPolicyValutationBasicEntity> findByPolicyId(Long id);

}
