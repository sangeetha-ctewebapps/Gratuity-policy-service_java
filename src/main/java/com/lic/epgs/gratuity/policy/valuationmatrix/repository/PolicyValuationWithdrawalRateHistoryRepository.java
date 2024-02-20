package com.lic.epgs.gratuity.policy.valuationmatrix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateHistoryEntity;
public interface PolicyValuationWithdrawalRateHistoryRepository extends JpaRepository<PolicyValuationWithdrawalRateHistoryEntity, Long>{
	
	List<PolicyValuationWithdrawalRateHistoryEntity> findBypolicyId(Long id);

}
