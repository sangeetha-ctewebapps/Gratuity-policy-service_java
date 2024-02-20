package com.lic.epgs.gratuity.policy.surrender.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import com.lic.epgs.gratuity.policy.surrender.entity.SurrenderDocumentDetailEntity;

public interface SurrenderDocumentRepository extends JpaRepository<SurrenderDocumentDetailEntity, Long> {
	
	@Query(value="SELECT * FROM SURRENDER_DOCUMENT_DETAIL WHERE SURRENDER_ID =?1 and PICKLIST_ITEM_ID =?2",nativeQuery = true)
	SurrenderDocumentDetailEntity getDocBySurrenderIdAndPicklistItemId(Long surrenderId, String picklistItemId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM SURRENDER_DOCUMENT_DETAIL WHERE DOCUMENT_INDEX = :documentIndex", nativeQuery = true)
	public int removeDocImageUsingDcoumentIndexNo(@RequestParam(value = "documentIndex") Long documentIndex);
	
	@Query(value="SELECT * FROM SURRENDER_DOCUMENT_DETAIL WHERE SURRENDER_ID =?1",nativeQuery = true)
	List<SurrenderDocumentDetailEntity> getDocumentDetailsBySurrenderId(Long surrenderId);

}
