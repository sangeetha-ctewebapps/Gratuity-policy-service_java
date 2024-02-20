package com.lic.epgs.gratuity.quotation.notes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.notes.entity.MasterNotesEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface MasterNotesRepository extends JpaRepository<MasterNotesEntity, Long> {
	
	@Query(value ="SELECT n FROM MasterNotesEntity n WHERE n.quotationId=:quotationId AND n.notesType=:notesType ORDER BY n.createdDate DESC")
	List<MasterNotesEntity> findByQuotationIdAndNotesType(Long quotationId, String notesType);

}
