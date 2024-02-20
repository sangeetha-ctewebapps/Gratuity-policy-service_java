package com.lic.epgs.gratuity.policy.member.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberEntity;

import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface PolicyMemberRepository extends JpaRepository<PolicyMemberEntity, Long> {

	@Query(value = "SELECT count( * ) as totalCount  FROM PMST_MEMBER WHERE IS_ACTIVE =1 AND POLICY_ID =?", nativeQuery = true)
	Long totalCount(Long id);

	@Query(value = "SELECT * FROM PMST_MEMBER pm WHERE POLICY_ID =:id ORDER BY EMPLOYEE_CODE", nativeQuery = true)
	List<PolicyMemberEntity> findBypolicyId(Long id);

	@Query(value = "select pm.LIC_ID, pm.FIRST_NAME AS Name,pm.EMPLOYEE_CODE,pp.CUSTOMER_CODE,pp.CUSTOMER_NAME,pp.POLICY_NUMBER,to_char(trunc(pm.DATE_OF_BIRTH), 'DD/MM/YYYY') AS DATE_OF_BIRTH, to_char(trunc(pm.DATE_OF_APPOINTMENT), 'DD/MM/YYYY') AS DATE_OF_APPOINTMENT , pm.DOJ_TO_SCHEME, pm.BASIC_SALARY AS Salary, g.PAST_SERVICE,		\r\n"
			+ "g.TOTAL_SERVICE,ROUND(g.TOTAL_GRAT, 2) AS TOTAL_SERVICE_GRATUITY,ROUND(g.ACCRUED_GRAT, 2) AS PAST_SERVICE_GRATUITY,ROUND(g.LC_SUM_ASSURED, 2) AS LIFE_COVER,\r\n"
			+ "to_char(trunc(pp.ANNUAL_RENEWAL_DATE), 'DD/MM/YYYY') AS ARD, g.DOB_DER_AGE AS AGE,ROUND(g.LC_PREMIUM, 2) AS LIFE_COVER_PREMIUM, MC.NAME AS CATEGORY,\r\n"
			+ "g.PAST_SERVICE_BENEFIT AS PAST_SERVICE_BENEFIT,g.CURRENT_SERVICE_BENEFIT AS CURRENT_SERVICE_BENEFIT from PMST_MEMBER pm \r\n"
			+ "inner join GRATCALCULATION g on pm.GRATCALCULATION_ID = g.GRATCALCULATION_ID INNER JOIN MEMBER_CATEGORY mc on mc.PMST_POLICY_ID = pm.POLICY_ID AND  Pm.CATEGORY_ID = mc.MEMBER_CATEGORY_ID		\r\n"
			+ "INNER JOIN PMST_POLICY pp ON pm.POLICY_ID = pp.POLICY_ID where pm.POLICY_ID =:policyId ORDER BY (pm.EMPLOYEE_CODE)", nativeQuery = true)
	List<Object[]> findBypolicyId1(Long policyId);

	@Query(value = "SELECT * From PMST_MEMBER where POLICY_ID=?1 and MEMBER_ID=?2", nativeQuery = true)
	PolicyMemberEntity findByPolicyIdandMemberId(Long pmstPolicyId, Long pmstMemberId);

	@Query(value = "SELECT * From PMST_MEMBER where POLICY_ID=?1 and IS_ACTIVE=1", nativeQuery = true)
	List<PolicyMemberEntity> findByPolicyId(Long policyId);

	@Query(value = "select pm.LIC_ID, pm.FIRST_NAME AS Name,pm.EMPLOYEE_CODE,pp.CUSTOMER_CODE,pp.CUSTOMER_NAME,pp.POLICY_NUMBER,to_char(trunc(pm.DATE_OF_BIRTH), 'DD/MM/YYYY') AS DATE_OF_BIRTH, to_char(trunc(pm.DATE_OF_APPOINTMENT), 'DD/MM/YYYY') AS DATE_OF_APPOINTMENT , pm.DOJ_TO_SCHEME, pm.BASIC_SALARY AS Salary, g.PAST_SERVICE,\r\n"
			+ "g.TOTAL_SERVICE,ROUND(g.TOTAL_GRAT, 2) AS TOTAL_SERVICE_GRATUITY,ROUND(g.ACCRUED_GRAT, 2) AS PAST_SERVICE_GRATUITY,ROUND(g.LC_SUM_ASSURED, 2) AS LIFE_COVER,\r\n"
			+ "to_char(trunc(pp.ANNUAL_RENEWAL_DATE), 'DD/MM/YYYY') AS ARD, g.DOB_DER_AGE AS AGE,ROUND(g.LC_PREMIUM, 2) AS LIFE_COVER_PREMIUM, MC.NAME AS CATEGORY,\r\n"
			+ "g.PAST_SERVICE_BENEFIT AS PAST_SERVICE_BENEFIT,g.CURRENT_SERVICE_BENEFIT AS CURRENT_SERVICE_BENEFIT from PSTG_MEMBER pm \r\n"
			+ "inner join GRATCALCULATION g on pm.GRATCALCULATION_ID = g.GRATCALCULATION_ID INNER JOIN MEMBER_CATEGORY mc on mc.PSTG_POLICY_ID = pm.POLICY_ID AND  Pm.CATEGORY_ID = mc.MEMBER_CATEGORY_ID\r\n"
			+ "INNER JOIN PSTG_POLICY pp ON pm.POLICY_ID = pp.POLICY_ID where pm.POLICY_ID =:policyId ORDER BY (pm.EMPLOYEE_CODE)", nativeQuery = true)
	List<Object[]> findBypolicyId2(Long policyId);

	@Query(value = "SELECT MAX(TO_NUMBER(LIC_ID)) AS MAXLIC FROM PMST_MEMBER WHERE POLICY_ID =?1", nativeQuery = true)
	Integer maxLicNumber(Long masterPolicyId);

	@Query(value = "SELECT * From PMST_MEMBER where POLICY_ID=?1 and MEMBER_ID=?2", nativeQuery = true)
	PolicyMemberEntity findBymasterPolicyIDandmemberId(Long masterPolicyId, Long masterMemberId);

	@Query(value = "SELECT DATE_OF_APPOINTMENT From PMST_MEMBER where POLICY_ID=:policyid and MEMBER_ID=:memberid", nativeQuery = true)
	Date getByPolicyIdAndLicId(@Param("memberid") Long memberid, @Param("policyid") Long policyid);

	@Modifying
	@Query(value = "UPDATE PMST_MEMBER SET IS_ACTIVE =0  WHERE MEMBER_ID =(SELECT PMST_MEMBER_ID FROM PMST_TMP_MEMBER ptm WHERE TMP_POLICY_ID =?1 AND MEMBER_ID =?2) ", nativeQuery = true)
	void deactivateclaimmember(Long tmpPolicyId, Long tmpMemberId);

	@Modifying
	@Query(value = "UPDATE PMST_MEMBER SET IS_ACTIVE =1 WHERE POLICY_ID =?2 and MEMBER_ID in (SELECT PMST_MEMBER_ID FROM PMST_TMP_MEMBER ptm WHERE TMP_POLICY_ID =?1)", nativeQuery = true)
	void updaterenewalupdatememberinmasteractive(Long id, Long pmstPolicyId);

	@Modifying
	@Query(value = "UPDATE PMST_MEMBER SET IS_ACTIVE =0  WHERE POLICY_ID =?2 and MEMBER_ID not in"
			+ " (SELECT PMST_MEMBER_ID FROM PMST_TMP_MEMBER ptm WHERE TMP_POLICY_ID =?1)", nativeQuery = true)
	void updatemasternotprocessedrenewalmemberinactive(Long id, Long pmstPolicyId);

	@Query(value = "SELECT * From PMST_MEMBER where POLICY_ID=?1 and EMPLOYEE_CODE=?2 and IS_ACTIVE=1", nativeQuery = true)
	List<PolicyMemberEntity> findByEmployeeCode(Long policyId, String employeeCode);

	@Query(value = "SELECT * From PMST_MEMBER where POLICY_ID=?1 and PAN_NUMBER=?2 and IS_ACTIVE=1", nativeQuery = true)
	List<PolicyMemberEntity> findByEmployeePan(Long policyId, String employeePan);

	@Query(value = "SELECT * From PMST_MEMBER where POLICY_ID=?1 and AADHAR_NUMBER=?2 and IS_ACTIVE=1", nativeQuery = true)
	List<PolicyMemberEntity> findByEmployeeAadhar(Long policyId, String employeeAadhar);

	@Query(value = "SELECT * FROM PMST_MEMBER WHERE MEMBER_ID=:memberId", nativeQuery = true)
	PolicyMemberEntity findByMemberId(@Param("memberId") Long MemberId);

	@Query(value = "select count(1) from PMST_MEMBER where lic_id =:licId and policy_id =:policyId", nativeQuery = true)
	Long getLicIdCountBylicIdAndpolicyId(@Param("licId") String lidId, @Param("policyId") Long policyId);

	@Query(value = "select round(sum(lc_premium),2) from gratcalculation where gratcalculation_id in (select gratcalculation_id from pmst_member where policy_id=?1 and is_active=1)", nativeQuery = true)
	Double findByLcPremium(Long masterpolicyId);

	@Query(value = "Select TRUNC(?1)-TRUNC(?2) as diff from Dual", nativeQuery = true)
	int getDiffAdjustmentDueDate(Date adjustProcessingDate, Date dueDate);

	@Modifying
	@Query(value = "UPDATE PMST_MEMBER SET LAST_PREMIUM_COLLECTED_DATE =?2 WHERE POLICY_ID =?1", nativeQuery = true)
	void setAdjustmentDateforMasterPolicyId(Long id, Date adjustmentForDate);

	@Query(value = "select Monthly from modalfactor", nativeQuery = true)
	Double getMonthvalue();
	
	@Query(value = "select halferly from modalfactor", nativeQuery = true)
	Double getHalflyvalue();
	
	@Query(value = "select quarterly from modalfactor", nativeQuery = true)
	Double getQuaterlyvalue();

	@Query(value = "select * from pmst_member pm join qstg_member_bulk_stg  bm on  pm.member_id = bm.pmst_member_id where bm.member_batch_id =?1 and bm.record_status =?2 ", nativeQuery = true)
	List<PolicyMemberEntity> findMemberByBtachIdAndRecordStatus(Long batchId, String recordStatus);
	
	@Modifying
	@Query(value = "UPDATE PMST_MEMBER SET IS_ACTIVE =0, STATUS_ID = 490 WHERE MEMBER_ID =?1 ", nativeQuery = true)
	void deactivateTransferMember(Long memberId);
	
	@Query(value = "select max(member_id)+1 from pmst_member ", nativeQuery = true)
	Long maxPlusOneMemberId();
	
	@Query(value = "select max(Lic_id)+1 from pmst_member where proposal_policy_number =?1 ", nativeQuery = true)
	String maxPlusOneLicId(String destPolicyNumber);

	@Modifying
	@Query(value = "UPDATE PMST_MEMBER SET IS_ACTIVE =0 WHERE POLICY_ID =?1 ", nativeQuery = true)
	PolicyMemberEntity updateIsActiveFalseForSourcePolicy(Long sourcemasterPolicyId);

	@Modifying
	@Query(value="update PMST_MEMBER set GST_INVOICE_NUMBER=?2,GST_INVOICE_DATE=systimestamp where policy_id=?1",nativeQuery = true)
	void updateInvoiceNumberNo(Long masterId, String gstInvoiceNumber);

}
