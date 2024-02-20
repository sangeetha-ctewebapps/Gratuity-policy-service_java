package com.lic.epgs.gratuity.quotation.valuationmatrix.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationWithdrawalRateEntity;

public interface ValuationWithdrawalRateRepository extends JpaRepository<ValuationWithdrawalRateEntity, Long> {
	
	@Query(value="SELECT * FROM QSTG_VALUATIONWITHDRAWALRATE WHERE QUOTATION_ID =?1 ORDER BY VALUATIONWITHDRAWALRATE_ID",nativeQuery = true)
	List<ValuationWithdrawalRateEntity> findByQuotationId(Long quotaitonId);

	List<ValuationWithdrawalRateEntity> deleteByquotationId(Long quotationId);

	@Query(value="SELECT min(RATE) || '% to ' || max(rate) || '%' FROM QSTG_VALUATIONWITHDRAWALRATE pv WHERE QUOTATION_ID =?",nativeQuery = true)
	String findMinAndMax(Long quotationId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_VALUATIONWITHDRAWALRATE WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
}
