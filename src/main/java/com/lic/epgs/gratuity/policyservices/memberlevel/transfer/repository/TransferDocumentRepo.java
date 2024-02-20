package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferDocumentDetailEntity;
@Repository
public interface TransferDocumentRepo extends JpaRepository<TransferDocumentDetailEntity, Long> {
	
	//@Query(value = "SELECT d FROM TransferDocumentDetailEntity d WHERE isUploaded=Y AND transferRequisitionId=:transferRequisitionId")
	//List<TransferDocumentDetailEntity> findById(String transferRequisitionId);

	TransferDocumentDetailEntity findByTransferRequisitionIdAndPicklistItemId(Long transferRequisitionId,
			String picklistItemId);
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM TRANSFER_DOCUMENT_DETAIL WHERE DOCUMENT_INDEX = :documentIndex", nativeQuery = true)
	public int removeDocImageUsingDcoumentIndexNo(@RequestParam(value = "documentIndex") Long documentIndex);
	
	
	List<TransferDocumentDetailEntity> findByTransferRequisitionId(Long transferRequisitionId);
	//TransferDocumentDetailEntity findByTransferReqId(Long transferRequisitionId);
	
	
//	@Query(value = "SELECT * FROM TRANSFER_DOCUMENT_DETAIL WHERE TRANSFER_REQUISITION_ID = :transferRequisitionId", nativeQuery = true)
//	public TransferDocumentDetailEntity findByTransferRequisitionId(@RequestParam(value = "transferRequisitionId") Long transferRequisitionId);
//	
	
}
