package com.lic.epgs.gratuity.policy.valuationmatrix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValuationWithdrawalRateEntity;

public interface StagingValuationWithdrawaleRateRepository extends JpaRepository<StagingPolicyValuationWithdrawalRateEntity, Long> {

	List<StagingPolicyValuationWithdrawalRateEntity> findByPolicyId(Long id);

}
