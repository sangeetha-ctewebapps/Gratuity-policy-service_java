package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferRequisitionEntity;

public interface TransferRequisitionRepo extends JpaRepository<TransferRequisitionEntity, Long> {

	@Transactional
	@Modifying
	@Query(value = "update transfer_requisition set transfer_status=:status,transfer_sub_status=:workflowStatus, modified_on=SYSDATE , modified_by=:userName , "
			+ "role=:roleType, location_type=:locationType, approved_by=:userApprove, approved_on=:timeApprove "
			+ "where transfer_requisition_id=:transferRequisitionId and upper(transfer_status) in :existingStatus and upper(role)=upper(:existingRole) "
			+ "and upper(location_type)=upper(:existingLocation)", nativeQuery = true)
	void updateMemberTransferRequisitionApproval(
			@RequestParam(value = "transferRequisitionId") Long transferRequisitionId,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "workflowStatus") String workflowStatus,
			@RequestParam(value = "existingStatus") List<String> existingStatus,
			@RequestParam(value = "roleType") String roleType,
			@RequestParam(value = "locationType") String locationType,
			@RequestParam(value = "exisitngRole") String existingRole,
			@RequestParam(value = "existingLocation") String existingLocation,
			@RequestParam(value = "userName") String userName, @RequestParam(value = "userApprove") String userApprove,
			@RequestParam(value = "timeApprove") @Temporal Date timeApprove);

	@Transactional
	@Modifying
	@Query(value = "  UPDATE transfer_requisition SET transfer_status = 'Rejected', transfer_sub_status =:rejectStatus, modified_by =:userName, modified_on = sysdate "
			+ " WHERE transfer_requisition_id =:transferRequisitionId ", nativeQuery = true)
	public void rejectMemberTransfer(@Param(value = "transferRequisitionId") Long transferRequisitionId,
			@Param(value = "userName") String userName, @Param(value = "rejectStatus") String rejectStatus);

	@Query(value = "SELECT TRANSFER_REQUEST_NUMBER_SEQ.nextval S " + "  FROM dual", nativeQuery = true)
	Long generateSeq();

	@Query(value = "select count(1) from transfer_requisition where transfer_status = 'Draft' and transfer_sub_status = 'Draft' and is_bulk = 'Y' ", nativeQuery = true)
	Long getTransferRequisitionCount();
	
	@Query(value = "select * from transfer_requisition where transfer_status = 'Draft' and transfer_sub_status = 'Draft' and is_bulk = 'Y' ", nativeQuery = true)
	TransferRequisitionEntity getDraftTransferRequisition();

	@Query(value = "select tr.transfer_sub_status from transfer_requisition tr "
			+ "join  transfer_member_policy_detail tmpd on tr.transfer_requisition_id = tmpd.transfer_requisition_id "
			+ "where tmpd.member_id =?1 ", nativeQuery = true)
	String getMemberTransferStatus(Long memberId);
	
	@Query(value = "select tr.transfer_request_number from transfer_requisition tr "
			+ "join  transfer_member_policy_detail tmpd on tr.transfer_requisition_id = tmpd.transfer_requisition_id "
			+ "where tmpd.member_id =?1 ", nativeQuery = true)
	Long getTransferRequestNumber(Long memberId);
		
}
