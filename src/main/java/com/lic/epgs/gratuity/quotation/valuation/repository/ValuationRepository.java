package com.lic.epgs.gratuity.quotation.valuation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.valuation.entity.ValuationEntity;

/**
 * @author Ismail Khan
 *
 */

@Repository
public interface ValuationRepository extends JpaRepository<ValuationEntity, Long> {

	Optional<ValuationEntity> findByQuotationId(Long quotaitonId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_VALUATION WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
}
