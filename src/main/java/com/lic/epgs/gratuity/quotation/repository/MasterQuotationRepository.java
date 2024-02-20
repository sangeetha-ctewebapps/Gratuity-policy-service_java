package com.lic.epgs.gratuity.quotation.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.spark.sql.catalyst.expressions.CurrentDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.entity.MasterQuotationEntity;

/**
 * @author Gopi
 *
 */

@Repository
public interface MasterQuotationRepository extends JpaRepository<MasterQuotationEntity, Long>, JpaSpecificationExecutor<MasterQuotationEntity>{

	@Query(value="SELECT MAX(TO_NUMBER(QUOTATION_NUMBER)) AS MAXQUOTATIONNUMBER FROM MASTER_QUOTATION", nativeQuery =true)
	Long maxQuotationNumber();
	
	@Query("SELECT q FROM MasterQuotationEntity q WHERE q.proposalNumber=:proposalNumber AND ROWNUM <=1")
	Optional<MasterQuotationEntity> findByProposalNumber(String proposalNumber);
	
	@Query(value = " SELECT MODULE_SEQ_FUNC(?1) FROM DUAL ", nativeQuery = true)
	String getSequence(String type);
	
	@Modifying
	@Query(value = "UPDATE QMST_QUOTATION SET IS_ACTIVE=0 WHERE PROPOSAL_NUMBER=?1 AND QUOTATION_ID !=?2", nativeQuery = true)
	void deactivateOtherQuotations(String proposalNumber, Long masterQuotationId);

	@Modifying
	@Query(value = "UPDATE QMST_QUOTATION SET IS_ACTIVE=0 WHERE PROPOSAL_NUMBER=?1", nativeQuery = true)
	void deactivateAllQuotation(String proposalNumber);

	@Modifying
	@Query(value = "UPDATE QMST_QUOTATION SET IS_ACTIVE=1 WHERE PROPOSAL_NUMBER=?1 AND QUOTATION_ID =?2", nativeQuery = true)
	void activateCurrentQuotationId(String proposalNumber, Long masterQuotationId);

	@Query(value="SELECT *\r\n"
			+ "FROM QMST_QUOTATION qq\r\n"
			+ "LEFT OUTER JOIN PSTG_POLICY pp\r\n"
			+ "ON qq.QUOTATION_ID  = pp.MASTER_QUOTATION_ID\r\n"
			+ "WHERE PP.MASTER_QUOTATION_ID  IS NULL AND qq.IS_ACTIVE = 1\r\n"
			+ "ORDER BY qq.QUOTATION_ID",nativeQuery = true)
	List<MasterQuotationEntity> findNotInPolicy();
	
	@Query("SELECT q.number  FROM MasterQuotationEntity q WHERE q.proposalNumber=:proposalNumber ORDER BY q.createdDate  DESC")
	List<String> addQuotationNumber(String proposalNumber);

	@Query(value="SELECT sum(LC_SUM_ASSURED)  FROM qmst_member WHERE quotation_id in"
			+ "(SELECT QUOTATION_ID  FROM QMST_QUOTATION qq WHERE qq.PROPOSAL_NUMBER =?1 AND IS_ACTIVE =1)",nativeQuery = true)
	Long findByProposalNumberforPolicy(String proposalNumber);



//	@Query("select to_date(?1, 'yyyy-MM-dd') -to_date(?2, 'yyyy-MM-dd')  datediff from dual")
//	Long getNoDays(CurrentDate get, Date createdDate);	
}
