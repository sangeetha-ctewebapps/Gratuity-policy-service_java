package com.lic.epgs.gratuity.quotation.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.quotation.member.entity.MemberBatchEntity;

/**
 * @author Gopi
 *
 */

public interface MemberBatchRepository extends JpaRepository<MemberBatchEntity, Long> {

	@Query(value = "SELECT d FROM MemberBatchEntity d WHERE isActive=1 AND quotationId=:quotationId ORDER BY batchId DESC")
	List<MemberBatchEntity> findByQuotationId(Long quotationId);
	
	@Query(value = "UPDATE QSTG_MEMBER_BATCH m SET m.IS_ACTIVE=0 WHERE QUOTATION_ID=?1 AND BATCH_ID!=?2", nativeQuery=true)
	void inactiveByQuotationId(Long quotationId, Long batchId);
	
	@Query(value = "SELECT count(*) AS MemberCount FROM QSTG_MEMBER WHERE IS_ACTIVE=1 AND QUOTATION_ID=?1", nativeQuery=true)
	Long memberImportCount(Long quotationId);

	@Query(value = "SELECT count(*) AS MemberCount FROM PMST_TMP_MEMBER WHERE IS_ACTIVE=1 AND TMP_POLICY_ID=?1", nativeQuery=true)
	Long renewalMemberImportCount(Long tmpPolicyId);

	@Query(value = "SELECT d FROM MemberBatchEntity d WHERE isActive=1 AND tmpPolicyId=:tmpPolicyId ORDER BY batchId DESC")
	List<MemberBatchEntity> findByTmpPolicyId(Long tmpPolicyId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_MEMBER_BATCH WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
	@Query(value = "SELECT count(*) AS MemberCount FROM QMST_MEMBER WHERE IS_ACTIVE=1 AND QUOTATION_ID=?1", nativeQuery=true)
	Long memberImportCountmasterquotation(Long quotationId);
	
}
