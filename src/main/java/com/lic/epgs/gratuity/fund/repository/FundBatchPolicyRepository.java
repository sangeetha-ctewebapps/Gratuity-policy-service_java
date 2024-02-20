package com.lic.epgs.gratuity.fund.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.fund.entity.FundBatchPolicyEntity;

@Repository
public interface FundBatchPolicyRepository extends JpaRepository<FundBatchPolicyEntity, BigDecimal> {

	@Query(value = "SELECT * from FUND_BATCH_POLICY where batch_no =:batchNo ", nativeQuery = true)
	List<FundBatchPolicyEntity> searchByBatchNo(String batchNo);

	@Query(value = "SELECT * from FUND_BATCH_POLICY where batch_no =:batchNo and policy_id =:policyId ", nativeQuery = true)
	List<FundBatchPolicyEntity> searchByBatchNoPolicyNo(String batchNo, String policyId);

	FundBatchPolicyEntity findByBatchNo(String batchNo);

	FundBatchPolicyEntity findByPolicyIdAndBatchNo(String policyId, String generatedBatchNo);

}