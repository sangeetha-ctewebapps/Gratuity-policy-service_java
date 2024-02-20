package com.lic.epgs.gratuity.quotation.premium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.premium.entity.PremiumEntity;
/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface PremiumRepository extends JpaRepository<PremiumEntity, Long> {
	
	List<PremiumEntity> findByQuotationId(Long quotaitonId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_PREMIUM WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
	
}
