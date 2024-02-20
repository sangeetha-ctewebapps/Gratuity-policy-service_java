package com.lic.epgs.gratuity.quotation.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.member.entity.MemberNomineeEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface MemberNomineeRepository extends JpaRepository<MemberNomineeEntity, Long> {
	@Query(value = "UPDATE QSTG_MEMBER_NOMINEE m SET m.IS_ACTIVE=0 WHERE m.MEMBER_ID IN "
			+ "(SELECT MEMBER_ID FROM QSTG_MEMBER WHERE QUOTATION_ID=?1)", nativeQuery=true)
	void inactiveByQuotationId(Long quotationId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_MEMBER_NOMINEE ma WHERE MEMBER_ID IN "
		+ "(SELECT MEMBER_ID FROM QSTG_MEMBER WHERE MEMBER_BATCH_ID=?1)", nativeQuery=true)
	void deleteByBatchId(Long batchId);

	
	@Modifying
	@Query(value ="DELETE FROM QSTG_MEMBER_NOMINEE  WHERE MEMBER_NOMINEE_ID =?1",nativeQuery=true )
	void deleteMemberNomineeId(Long id);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_MEMBER_NOMINEE WHERE MEMBER_ID IN ("
			+ "SELECT MEMBER_ID FROM QSTG_MEMBER WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1))", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
}