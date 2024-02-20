package com.lic.epgs.gratuity.quotation.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface MasterMemberRepository extends JpaRepository<MasterMemberEntity, Long> {
	@Query(value = "UPDATE QMST_MEMBER m SET m.IS_ACTIVE=0 WHERE m.QUOTATION_ID=:quotationId", nativeQuery=true)
	void inactiveByQuotationId(Long quotationId);
	
	@Query(value = "SELECT * from QMST_MEMBER WHERE IS_ACTIVE=1 AND QUOTATION_ID=:quotationId", nativeQuery=true)
	List<MasterMemberEntity> findAll(Long quotationId);
	List<MasterMemberEntity> findByQuotationId(Long id);

	@Query(value = "select count(*) AS  TOTAL_MEMBER, ROUND(avg(g.DOB_DER_AGE),2) AS AVERAGE_AGE, ROUND(avg(qm.BASIC_SALARY),2) AS\r\n"
			+ "AVG_MONTHLY_SALARY,ROUND(avg(g.PAST_SERVICE),2) AS AVG_PAST_SERVICE, sum(CEIL(g.TOTAL_GRAT)) AS\r\n"
			+ "TOTAL_SERVICE_GRATUITY,sum(CEIL(g.ACCRUED_GRAT)) AS ACCURED_GRATUITY,CEIL(sum(g.LC_SUM_ASSURED)) AS LCSA, \r\n"
			+ "CEIL(sum(g.LC_PREMIUM)) AS LC_PREMIUM, CEIL(sum(g.LC_PREMIUM)/100*18) AS GST , ROUND(sum(g.PAST_SERVICE_BENEFIT),2) AS PAST_SERVICE_BENEFIT, ROUND(sum(g.CURRENT_SERVICE_BENEFIT),2) AS CURRENT_SERVICE_BENEFIT from qmst_member qm inner join\r\n"
			+ "gratcalculation g on qm.GRATCALCULATION_ID = g.GRATCALCULATION_ID INNER JOIN MEMBER_CATEGORY mc on \r\n"
			+ "mc.QMST_QUOTATION_ID = qm.QUOTATION_ID AND qm.CATEGORY_ID = mc.MEMBER_CATEGORY_ID where qm.quotation_id =:quotationId ORDER BY TO_NUMBER(qm.lic_id)",nativeQuery = true)
	String generatePDFData(Long quotationId);

	
	@Query(value = "select qm.LIC_ID, qm.FIRST_NAME AS Name,qm.EMPLOYEE_CODE,qq.QUOTATION_NUMBER,to_char(trunc(qm.DATE_OF_BIRTH), 'DD/MM/YYYY') AS DATE_OF_BIRTH, to_char(trunc(qm.DATE_OF_APPOINTMENT), 'DD/MM/YYYY') AS DATE_OF_APPOINTMENT, QM.DOJ_TO_SCHEME, qm.BASIC_SALARY AS Salary, g.PAST_SERVICE,\r\n"
			+ "g.TOTAL_SERVICE,ROUND(g.TOTAL_GRAT, 2) AS TOTAL_SERVICE_GRATUITY,ROUND(g.ACCRUED_GRAT, 2) AS PAST_SERVICE_GRATUITY,ROUND(g.LC_SUM_ASSURED, 2) AS LIFE_COVER,\r\n"
			+ "to_char(trunc(qq.DATE_OF_COMMENCEMENT), 'DD/MM/YYYY') AS ARD, g.DOB_DER_AGE AS AGE,ROUND(g.LC_PREMIUM, 2) AS LIFE_COVER_PREMIUM, MC.NAME AS CATEGORY,\r\n"
			+ "g.PAST_SERVICE_BENEFIT AS PAST_SERVICE_BENEFIT,g.CURRENT_SERVICE_BENEFIT AS CURRENT_SERVICE_BENEFIT,qq.CUSTOMER_CODE AS customerCode,qq.PROPOSAL_NUMBER AS proposalNumber from qmst_member qm\r\n"
			+ "inner join gratcalculation g on qm.GRATCALCULATION_ID = g.GRATCALCULATION_ID INNER JOIN MEMBER_CATEGORY mc on mc.QMST_QUOTATION_ID = qm.QUOTATION_ID \r\n"
			+ "AND qm.CATEGORY_ID = mc.MEMBER_CATEGORY_ID  \r\n"
			+ "INNER JOIN QMST_QUOTATION qq ON qm.QUOTATION_ID = qq.QUOTATION_ID  where qm.quotation_id =:quotationId ORDER BY (qm.EMPLOYEE_CODE)",nativeQuery = true)
	List<Object[]> findByQuotationId1(Long quotationId);

	@Query(value = "select qm.LIC_ID, qm.FIRST_NAME AS Name,qm.EMPLOYEE_CODE,qq.QUOTATION_NUMBER,to_char(trunc(qm.DATE_OF_BIRTH), 'DD/MM/YYYY') AS DATE_OF_BIRTH, to_char(trunc(qm.DATE_OF_APPOINTMENT), 'DD/MM/YYYY') AS DATE_OF_APPOINTMENT, QM.DOJ_TO_SCHEME, qm.BASIC_SALARY AS Salary, g.PAST_SERVICE,\r\n"
			+ "g.TOTAL_SERVICE,ROUND(g.TOTAL_GRAT, 2) AS TOTAL_SERVICE_GRATUITY,ROUND(g.ACCRUED_GRAT, 2) AS PAST_SERVICE_GRATUITY,ROUND(g.LC_SUM_ASSURED, 2) AS LIFE_COVER,\r\n"
			+ "to_char(trunc(qq.DATE_OF_COMMENCEMENT), 'DD/MM/YYYY') AS ARD, g.DOB_DER_AGE AS AGE,ROUND(g.LC_PREMIUM, 2) AS LIFE_COVER_PREMIUM, MC.NAME AS CATEGORY,\r\n"
			+ "g.PAST_SERVICE_BENEFIT AS PAST_SERVICE_BENEFIT,g.CURRENT_SERVICE_BENEFIT AS CURRENT_SERVICE_BENEFIT,qq.CUSTOMER_CODE AS customerCode,qq.PROPOSAL_NUMBER AS proposalNumber from qstg_member qm\r\n"
			+ "inner join gratcalculation g on qm.GRATCALCULATION_ID = g.GRATCALCULATION_ID INNER JOIN MEMBER_CATEGORY mc on mc.QSTG_QUOTATION_ID = qm.QUOTATION_ID\r\n"
			+ "AND qm.CATEGORY_ID = mc.MEMBER_CATEGORY_ID  \r\n"
			+ "INNER JOIN QSTG_QUOTATION qq ON qm.QUOTATION_ID = qq.QUOTATION_ID  where qm.quotation_id =:quotationId ORDER BY (qm.EMPLOYEE_CODE)",nativeQuery = true)
	List<Object[]> findByQuotationId2(Long quotationId);

	@Query(value = "select count(*) AS  TOTAL_MEMBER,avg(g.DOB_DER_AGE) AS AVERAGE_AGE, avg(qm.BASIC_SALARY) AS\r\n"
			+ "AVG_MONTHLY_SALARY,avg(g.PAST_SERVICE) AS AVG_PAST_SERVICE, sum(g.TOTAL_GRAT) AS\r\n"
			+ "TOTAL_SERVICE_GRATUITY,sum(g.ACCRUED_GRAT) AS ACCURED_GRATUITY,sum(g.LC_SUM_ASSURED) AS LCSA,\r\n"
			+ "sum(g.LC_PREMIUM) AS LC_PREMIUM, (sum(g.LC_PREMIUM)/100*18) AS GST ,sum(g.PAST_SERVICE_BENEFIT) AS PAST_SERVICE_BENEFIT,sum(g.CURRENT_SERVICE_BENEFIT) AS CURRENT_SERVICE_BENEFIT from qstg_member qm inner JOIN\r\n"
			+ "gratcalculation g on qm.GRATCALCULATION_ID = g.GRATCALCULATION_ID INNER JOIN MEMBER_CATEGORY mc ON\r\n"
			+ "mc.QSTG_QUOTATION_ID = qm.QUOTATION_ID AND qm.CATEGORY_ID = mc.MEMBER_CATEGORY_ID where qm.quotation_id =:quotationId ORDER BY TO_NUMBER(qm.lic_id)",nativeQuery = true)
	String generatePDFDataforStaging(Long quotationId);
}
