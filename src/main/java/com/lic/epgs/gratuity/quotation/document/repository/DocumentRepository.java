package com.lic.epgs.gratuity.quotation.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.document.entity.DocumentEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
	@Query(value = "SELECT * FROM QSTG_DOCUMENT qd WHERE qd.IS_DELETED =0 AND qd.QUOTATION_ID =:quotationId",nativeQuery = true)
	List<DocumentEntity> findByQuotationId(Long quotationId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_DOCUMENT WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
}


