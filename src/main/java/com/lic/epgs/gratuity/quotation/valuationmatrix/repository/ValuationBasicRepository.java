package com.lic.epgs.gratuity.quotation.valuationmatrix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationBasicEntity;

public interface ValuationBasicRepository extends JpaRepository<ValuationBasicEntity, Long> {
	Optional<ValuationBasicEntity> findByQuotationId(Long quotaitonId);
	
	@Query(value="SELECT MAX(TO_NUMBER(REFERENCE_NUMBER)) AS MAXREFERENCENUMBER FROM QSTG_VALUATIONBASIC", nativeQuery =true)
	Long maxReferenceNumber();
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_VALUATIONBASIC WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
}
