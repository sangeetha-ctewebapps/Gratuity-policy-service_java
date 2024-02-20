package com.lic.epgs.gratuity.quotation.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.document.entity.MasterDocumentEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface MasterDocumentRepository extends JpaRepository<MasterDocumentEntity, Long> {
	@Query(value = "SELECT d FROM MasterDocumentEntity d WHERE isDeleted=false AND quotationId=:quotationId")
	List<MasterDocumentEntity> findByQuotationId(Long quotationId);
}


