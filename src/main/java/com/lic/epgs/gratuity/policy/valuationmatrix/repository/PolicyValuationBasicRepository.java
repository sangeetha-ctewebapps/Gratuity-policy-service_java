package com.lic.epgs.gratuity.policy.valuationmatrix.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationMatrixEntity;

public interface PolicyValuationBasicRepository extends JpaRepository<PolicyValutationBasicEntity, Long> {

	Optional<PolicyValutationBasicEntity> findByPolicyId(Long policyId);

	Optional<PolicyValutationBasicEntity> findBypolicyId(Long masterPolicyId);

}
