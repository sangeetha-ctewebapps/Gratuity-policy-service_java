package com.lic.epgs.gratuity.policyservices.unit.transferr.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferDocumentDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferNotesEntity;
import com.lic.epgs.gratuity.policyservices.unit.transfer.entity.UnitTransferDocumentDetailEntity;
import com.lic.epgs.gratuity.policyservices.unit.transfer.entity.UnitTransferNotesEntity;
@Repository
public interface UnitTransferNotesRepo extends JpaRepository<UnitTransferNotesEntity, Long> {

	@Query(value = "SELECT UNIT_TRANSFER_NOTES_ID,UNIT_TRANSFER_REQUISITION_ID, UNIT_NOTE, MODIFIED_BY as CREATED_BY,\r\n"
			+ "			TO_CHAR(MODIFIED_ON,'dd/mm/yyyy HH24:MI:SS') as CREATE_DATE \r\n"
			+ "									FROM UNIT_TRANSFER_NOTES where UNIT_TRANSFER_REQUISITION_ID =:unitTransferRequisitionId \r\n"
			+ "									ORDER BY MODIFIED_ON DESC",nativeQuery = true)
	public List<Map<String,Object>> getTransferNotes(Long unitTransferRequisitionId);
	
	 List<UnitTransferNotesEntity> findByUnitTransferRequisitionId(Long unitTransferRequisitionId);

}
