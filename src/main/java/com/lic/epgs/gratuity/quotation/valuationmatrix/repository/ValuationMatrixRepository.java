package com.lic.epgs.gratuity.quotation.valuationmatrix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationMatrixEntity;

public interface ValuationMatrixRepository extends JpaRepository<ValuationMatrixEntity, Long> {
	
	Optional<ValuationMatrixEntity> findByQuotationId(Long quotaitonId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_VALUATIONMATRIX WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
}
