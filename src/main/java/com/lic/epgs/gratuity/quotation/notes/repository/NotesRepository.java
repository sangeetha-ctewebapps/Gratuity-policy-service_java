package com.lic.epgs.gratuity.quotation.notes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.notes.entity.NotesEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface NotesRepository extends JpaRepository<NotesEntity, Long> {
	
	@Query(value ="SELECT n FROM NotesEntity n WHERE n.quotationId=:quotationId AND n.notesType=:notesType ORDER BY n.createdDate DESC")
	List<NotesEntity> findByQuotationIdAndNotesType(Long quotationId, String notesType);
	
	List<NotesEntity> findByQuotationId(Long quotationId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_NOTES WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);

}
