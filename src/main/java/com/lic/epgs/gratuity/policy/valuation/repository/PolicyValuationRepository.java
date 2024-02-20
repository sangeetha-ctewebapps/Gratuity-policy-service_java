package com.lic.epgs.gratuity.policy.valuation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationEntity;

@Repository
public interface PolicyValuationRepository extends JpaRepository<PolicyValuationEntity, Long>{

	Optional<PolicyValuationEntity> findByPolicyId(Long id);

}
