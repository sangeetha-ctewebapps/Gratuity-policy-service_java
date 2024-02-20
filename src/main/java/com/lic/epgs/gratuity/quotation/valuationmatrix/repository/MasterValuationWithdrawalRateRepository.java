package com.lic.epgs.gratuity.quotation.valuationmatrix.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationWithdrawalRateEntity;

public interface MasterValuationWithdrawalRateRepository extends JpaRepository<MasterValuationWithdrawalRateEntity, Long> {
	List<MasterValuationWithdrawalRateEntity> findByQuotationId(Long quotaitonId);

	@Query(value="SELECT min(RATE) || '% to ' || max(rate) || '%' FROM QMST_VALUATIONWITHDRAWALRATE pv WHERE QUOTATION_ID =?",nativeQuery = true)
	String findMinAndMax(Long quotationId);
}
