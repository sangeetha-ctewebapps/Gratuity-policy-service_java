package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferNotesEntity;

public interface TransferNotesRepo extends JpaRepository<TransferNotesEntity, Long> {

	@Query(value = "SELECT TRANSFER_NOTES_ID,TRANSFER_REQUISITION_ID, NOTE, MODIFIED_BY as CREATED_BY,\r\n"
			+ "TO_CHAR(MODIFIED_ON,'dd/mm/yyyy HH24:MI:SS') as CREATE_DATE \r\n"
			+ "						FROM TRANSFER_NOTES where TRANSFER_REQUISITION_ID =:transferRequisitionId \r\n"
			+ "						ORDER BY MODIFIED_ON DESC",nativeQuery = true)
	public List<Map<String,Object>> getTransferNotes(Long transferRequisitionId);
	
	 Optional<List<TransferNotesEntity>> findByTransferRequisitionId(Long transferRequisitionId);
	
}
