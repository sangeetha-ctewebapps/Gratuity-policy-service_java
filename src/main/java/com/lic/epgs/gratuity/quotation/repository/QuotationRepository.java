package com.lic.epgs.gratuity.quotation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;

/**
 * @author Gopi
 *
 */

@Repository
public interface QuotationRepository extends JpaRepository<QuotationEntity, Long>, JpaSpecificationExecutor<QuotationEntity>{

	@Query(value="SELECT MAX(TO_NUMBER(QUOTATION_NUMBER)) AS MAXQUOTATIONNUMBER FROM QSTG_QUOTATION", nativeQuery =true)
	Long maxQuotationNumber();
	
	@Query("SELECT q FROM QuotationEntity q WHERE q.proposalNumber=:proposalNumber AND ROWNUM <=1")
	Optional<QuotationEntity> findByProposalNumber(String proposalNumber);
	
	@Query(value = " SELECT MODULE_SEQ_FUNC(?1) FROM DUAL ", nativeQuery = true)
	String getSequence(String type);

	@Query("SELECT q.number  FROM QuotationEntity q WHERE q.proposalNumber=:proposalNumber ORDER BY q.createdDate  DESC")
	List<String> addQuotationNumber(String proposalNumber);

	@Query(value = " SELECT * FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1 and IS_ACTIVE=1 And rownum=1", nativeQuery = true)
	QuotationEntity findByProposalNumberisActive(String proposalNumber);
	
//	@Query(value = "DELETE FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER = ?1", nativeQuery = true)
	int deleteByProposalNumber(String proposalNumber);

	
	@Query(value = "SELECT COUNT(*) FROM  QSTG_QUOTATION WHERE SUB_STATUS_ID  =?1", nativeQuery = true)
	Long getCountQuotationSubStatus(Long subStatusId);

	@Query(value="SELECT COUNT(TO_NUMBER(QUOTATION_NUMBER))  FROM QSTG_QUOTATION qq WHERE PROPOSAL_NUMBER =?1 ", nativeQuery = true)
	int checkInProposalQuotationCreatedCount(String proposalNumber);

	@Query(value="SELECT MAX(TO_NUMBER(QUOTATION_NUMBER)) AS MAXQUOTATIONNUMBER  FROM QSTG_QUOTATION qq WHERE PROPOSAL_NUMBER =?1", nativeQuery = true)
	String getProposalQuotationCreatedNumber(String proposalNumber);

	
	@Query(value="	SELECT IN_DATE,\r\n"
			+ "	CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('01','02','03') THEN TO_CHAR(TO_NUMBER(TO_CHAR(IN_DATE,'YYYY')))-1||'-'||TO_CHAR(TO_NUMBER(TO_CHAR(IN_DATE,'YYYY')))\r\n"
			+ "	ELSE TO_CHAR(TO_NUMBER(TO_CHAR(IN_DATE,'YYYY')))||'-'||TO_CHAR(TO_NUMBER(TO_CHAR(IN_DATE,'YYYY'))+1) END FINANCIAL_YEAR,\r\n"
			+ "	CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('04','05','06') THEN 'Q1'\r\n"
			+ "	ELSE CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('07','08','09') THEN 'Q2'\r\n"
			+ "	ELSE CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('10','11','12') THEN 'Q3'\r\n"
			+ "	ELSE 'Q4' END END END QUARTER,\r\n"
			+ "	CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('04','05','06') THEN '1'\r\n"
			+ "	ELSE CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('07','08','09') THEN '2'\r\n"
			+ "	ELSE CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('10','11','12') THEN '3'\r\n"
			+ "	ELSE '4' END END END FREQUENCY\r\n"
			+ "	FROM\r\n"
			+ "	(SELECT TO_DATE(?1,'DD-MM-YYYY') IN_DATE FROM DUAL)",nativeQuery=true)
	Object findByObjectNew(@RequestParam String date);

	
}
