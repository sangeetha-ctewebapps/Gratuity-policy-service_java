package com.lic.epgs.gratuity.quotation.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.member.entity.MemberEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	
	List<MemberEntity> findByQuotationId(Long quotaitonId);
	
	@Query(value = "UPDATE QSTG_MEMBER m SET m.IS_ACTIVE=0 WHERE m.QUOTATION_ID=:quotationId", nativeQuery=true)
	void inactiveByQuotationId(Long quotationId);
	
	@Query(value = "SELECT * from QSTG_MEMBER WHERE IS_ACTIVE=1 AND QUOTATION_ID=:quotationId", nativeQuery=true)
	List<MemberEntity> findAll(Long quotationId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_MEMBER WHERE MEMBER_BATCH_ID=?1", nativeQuery=true)
	void deleteByBatchId(Long batchId);

	@Query(value = "SELECT MAX(TO_NUMBER(LIC_ID) ) AS maxnumber FROM QSTG_MEMBER   WHERE QUOTATION_ID  =?1",nativeQuery = true)
	Long MaxLicNumber(Long quotationId);

	@Query(value = "SELECT * FROM QSTG_MEMBER  WHERE QUOTATION_ID =?1 AND EMPLOYEE_CODE =?2",nativeQuery = true)
	Optional<MemberEntity> codeStatus(Long quotationId, String code);

	@Query(value = "SELECT * FROM QSTG_MEMBER  WHERE QUOTATION_ID =?1 AND EMPLOYEE_CODE =?2 AND  MEMBER_ID !=?3",nativeQuery = true)
	Optional<MemberEntity> codeStatuswithMember(Long quotationId, String code, Long id);

	@Query(value = "SELECT count(*) FROM QSTG_MEMBER WHERE IS_ACTIVE=1 AND QUOTATION_ID=?1", nativeQuery=true)
	int numberMemberCount(Long quotationId);
	
	@Query(value="SELECT SUM(LC_PREMIUM) AS LCPREMIUM  FROM PMST_MEMBER pm WHERE pm.POLICY_ID =?1",nativeQuery=true)
	Double findByPmstPolicyId(Long id);

	@Query(value="SELECT  *  FROM QSTG_MEMBER qm   WHERE QUOTATION_ID =?1 ORDER BY EMPLOYEE_CODE ",nativeQuery=true)
	List<MemberEntity> findbyquotation(Long quotationId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_MEMBER WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION qq WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);

	List<MemberEntity> findByMemberBatchId(Long batchId);

}
