package com.lic.epgs.gratuity.policy.renewalpolicy.member.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;

public interface RenewalPolicyTMPMemberRepository extends JpaRepository<RenewalPolicyTMPMemberEntity, Long> {
	@Query(value = "SELECT * from PMST_TMP_MEMBER where TMP_POLICY_ID=?1 ORDER BY EMPLOYEE_CODE", nativeQuery = true)
	List<RenewalPolicyTMPMemberEntity> findBytmpPolicyId(Long tmpPolicyId);

	// renewal quotation C and b sheet
	@Query(value = "select ptm.LIC_ID, ptm.FIRST_NAME AS Name,ptm.EMPLOYEE_CODE, ptm.DATE_OF_BIRTH, ptm.DATE_OF_APPOINTMENT, ptm.DOJ_TO_SCHEME, ptm.BASIC_SALARY AS Salary, g.PAST_SERVICE,		g.TOTAL_SERVICE, CEIL(g.TOTAL_GRAT) AS TOTAL_SERVICE_GRATUITY, CEIL(g.ACCRUED_GRAT) AS PAST_SERVICE_GRATUITY, CEIL(g.LC_SUM_ASSURED) AS LIFE_COVER,\r\n"
			+ "			ptp.QUOTATION_DATE  AS ARD, g.DOB_DER_AGE AS AGE, ROUND(g.LC_PREMIUM,2) AS LIFE_COVER_PREMIUM, mc2.NAME AS CATEGORY,ptp.CUSTOMER_CODE,\r\n"
			+ "			CEIL(g.PAST_SERVICE_BENEFIT) AS PAST_SERVICE_BENEFIT, CEIL(g.CURRENT_SERVICE_BENEFIT) AS CURRENT_SERVICE_BENEFIT from PMST_TMP_MEMBER ptm\r\n"
			+ "			inner join gratcalculation g on ptm.GRATCALCULATION_ID = g.GRATCALCULATION_ID INNER JOIN MEMBER_CATEGORY mc on mc.PMST_TMP_POLICY_ID  = ptm.TMP_POLICY_ID\r\n"
			+ "			AND ptm.CATEGORY_ID = mc.MEMBER_CATEGORY_ID\r\n"
			+ "INNER JOIN MEMBER_CATEGORY mc2 ON mc.MEMBER_CATEGORY_ID = MC2.MEMBER_CATEGORY_ID AND  Ptm.CATEGORY_ID = mc2.MEMBER_CATEGORY_ID\r\n"
			+ "			INNER JOIN PMST_TMP_POLICY ptp ON ptm.TMP_POLICY_ID = ptp.POLICY_ID  where ptm.TMP_POLICY_ID =?1 ORDER BY (ptm.EMPLOYEE_CODE)\r\n"
			+ "", nativeQuery = true)
	List<Object[]> findByTempPolicyID(Long tmpPolicyId);

	@Query(value = "SELECT count(*) FROM PMST_TMP_MEMBER WHERE IS_ACTIVE=1 AND TMP_POLICY_ID=?1", nativeQuery = true)
	Long numberMemberCount(Long tmpPolicyId);

	@Query(value = "SELECT * from PMST_TMP_MEMBER where TMP_POLICY_ID=?1", nativeQuery = true)
	RenewalPolicyTMPMemberEntity findBytmpPolicyId1(Long tmppolicyid);

	@Query(value = "SELECT * from PMST_TMP_MEMBER where TMP_POLICY_ID=?1 and MEMBER_ID=?2", nativeQuery = true)
	RenewalPolicyTMPMemberEntity findByTmpMemberIdandTmpPolicyId(Long tmppolicyid, Long tmpMemberId);

	@Query(value = "SELECT * from PMST_TMP_MEMBER where TMP_POLICY_ID=?1 and PMST_MEMBER_ID=?2", nativeQuery = true)
	RenewalPolicyTMPMemberEntity findByTmpMemberIdandpmstPolicyId(Long id, Long masterMemberId);

	@Modifying
	@Query(value = "UPDATE PMST_TMP_MEMBER SET IS_ACTIVE=0 WHERE TMP_POLICY_ID=?1", nativeQuery = true)
	void updateisActivefalse(Long id);

	@Query(value = "SELECT MAX(TO_NUMBER(LIC_ID)) AS MAXLIC FROM PMST_TMP_MEMBER WHERE TMP_POLICY_ID =?1", nativeQuery = true)
	Integer maxLicNumber(Long id);

	@Query(value = "SELECT MAX(TO_NUMBER(LIC_ID)) AS MAXLIC FROM PMST_TMP_MEMBER WHERE POLICY_ID =?1", nativeQuery = true)
	Long maximumLicNumber(Long id);

	@Modifying
	@Query(value = "UPDATE PMST_TMP_MEMBER SET LC_SUM_ASSURED=5000 WHERE POLICY_ID=?1", nativeQuery = true)
	void updateSumAssureforPolicy(Long policyId);

	@Query(value = "select pm.LIC_ID, pm.FIRST_NAME AS Name,pm.EMPLOYEE_CODE,pp.QUOTATION_NUMBER,\r\n"
			+ "to_char(trunc(pm.DATE_OF_BIRTH), 'DD/MM/YYYY') AS DATE_OF_BIRTH, \r\n"
			+ "to_char(trunc(pm.DATE_OF_APPOINTMENT), 'DD/MM/YYYY') AS DATE_OF_APPOINTMENT,pp.CUSTOMER_NAME,\r\n"
			+ "pm.BASIC_SALARY AS Salary, g.PAST_SERVICE,\r\n"
			+ "g.TOTAL_SERVICE, CEIL(g.TOTAL_GRAT) AS TOTAL_SERVICE_GRATUITY, CEIL(g.ACCRUED_GRAT) AS PAST_SERVICE_GRATUITY, \r\n"
			+ "CEIL(g.LC_SUM_ASSURED) AS LIFE_COVER, \r\n"
			+ " pp.ANNUAL_RENEWAL_DATE,g.DOB_DER_AGE AS AGE,ROUND(g.LC_PREMIUM,2) AS LIFE_COVER_PREMIUM,mc2.NAME AS CATEGORY,pp.CUSTOMER_CODE\r\n"
			+ "from PMST_TMP_MEMBER pm \r\n"
			+ "inner join GRATCALCULATION g on pm.GRATCALCULATION_ID = g.GRATCALCULATION_ID \r\n"
			+ "INNER JOIN MEMBER_CATEGORY_MODULE  mc on mc.TMP_POLICY_ID  = pm.TMP_POLICY_ID \r\n"
			+ "INNER JOIN MEMBER_CATEGORY mc2 ON mc.MEMBER_CATEGORY_ID = MC2.MEMBER_CATEGORY_ID AND  Pm.CATEGORY_ID = mc2.MEMBER_CATEGORY_ID        \r\n"
			+ "INNER JOIN PMST_TMP_POLICY pp ON pm.TMP_POLICY_ID = pp.POLICY_ID \r\n"
			+ "where pm.TMP_POLICY_ID =:tmpPolicyId ORDER BY (pm.EMPLOYEE_CODE)", nativeQuery = true)
	List<Object[]> findByTmpPolicyID(Long tmpPolicyId);

	@Query(value = "SELECT * FROM PMST_TMP_MEMBER WHERE MEMBER_ID =:memberId", nativeQuery = true)
	RenewalPolicyTMPMemberEntity findByMember(@Param("memberId") String memberId);

	@Modifying
	@Query(value = "DELETE from PMST_TMP_MEMBER WHERE TMP_POLICY_ID=?1", nativeQuery = true)
	void deleteBytmpPolicyId(Long id);

	List<RenewalPolicyTMPMemberEntity> findByTmpPolicyId(Long id);

	@Query(value = "SELECT * from PMST_TMP_MEMBER where POLICY_ID=?1 ORDER BY EMPLOYEE_CODE", nativeQuery = true)
	List<RenewalPolicyTMPMemberEntity> findByPolicyId(Long tmpPolicyId);

	@Query(value = "SELECT * FROM PMST_TMP_MEMBER WHERE MEMBER_ID IN :memIds", nativeQuery = true)
	List<RenewalPolicyTMPMemberEntity> findByMember(List<Long> memIds);

	@Modifying
	@Query(value = "UPDATE PMST_TMP_MEMBER SET TMP_POLICY_ID=?2 WHERE POLICY_ID=?1", nativeQuery = true)
	void updateSourceTmpPolicyIdAsDesti(Long destiMasterPolicyId, Long tmpPolicyId);

//	RenewalPolicyTMPMemberEntity findBytmpPolicyId1(Long id, Long masterMemberId);
}
