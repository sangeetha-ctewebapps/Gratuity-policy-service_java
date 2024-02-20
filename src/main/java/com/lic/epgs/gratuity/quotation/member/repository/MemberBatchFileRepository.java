package com.lic.epgs.gratuity.quotation.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.member.entity.MemberBatchFileEntity;

/**
 * @author Gopi
 *
 */

@Repository
public interface MemberBatchFileRepository extends JpaRepository<MemberBatchFileEntity, Long> {
	@Query(value = "UPDATE QSTG_MEMBER_BATCH_FILE m SET m.IS_ACTIVE=0 WHERE m.BATCH_FILE_ID IN "
			+ "(SELECT BATCH_FILE_ID FROM QSTG_MEMBER_BATCH WHERE QUOTATION_ID=?1 AND BATCH_ID!=?2)", nativeQuery=true)
	void inactiveByQuotationId(Long quotationId, Long batchId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_MEMBER_BATCH_FILE mbf WHERE BATCH_FILE_ID =?1", nativeQuery=true)
	void deleteByBatchFileId(Long batchId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_MEMBER_BATCH_FILE WHERE BATCH_FILE_ID IN (SELECT BATCH_FILE_ID FROM QSTG_MEMBER_BATCH WHERE QUOTATION_ID "
			+ "IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION qq WHERE PROPOSAL_NUMBER=?1))", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
}
