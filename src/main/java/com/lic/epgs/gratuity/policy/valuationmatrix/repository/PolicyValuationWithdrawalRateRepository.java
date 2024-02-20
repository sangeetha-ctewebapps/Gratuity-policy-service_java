package com.lic.epgs.gratuity.policy.valuationmatrix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateHistoryEntity;
@Repository
public interface PolicyValuationWithdrawalRateRepository extends JpaRepository<PolicyValuationWithdrawalRateEntity, Long>{

	List<PolicyValuationWithdrawalRateEntity> findByPolicyId(Long policyId);


	List<PolicyValuationWithdrawalRateEntity> deleteBypolicyId(Long id);
	
}
