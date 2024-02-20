package com.lic.epgs.gratuity.fund.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.fund.entity.FundBatchSummaryEntity;

@Repository
public interface FundBatchSummaryRepository extends JpaRepository<FundBatchSummaryEntity, BigDecimal> {

	@Query(value = "select * from FUND_BATCH_SUMMARY where batch_no =:batchNo ", nativeQuery = true)
	List<FundBatchSummaryEntity> findBatchDetailsByBatchNo(String batchNo);

	FundBatchSummaryEntity findByBatchNo(String batchNo);

}