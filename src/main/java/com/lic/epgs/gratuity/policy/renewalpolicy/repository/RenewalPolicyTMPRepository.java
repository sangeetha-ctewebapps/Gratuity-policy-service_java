package com.lic.epgs.gratuity.policy.renewalpolicy.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;

@Repository
public interface RenewalPolicyTMPRepository extends JpaRepository<RenewalPolicyTMPEntity, Long> {

	@Query(value="SELECT MAX(TO_NUMBER(QUOTATION_NUMBER)) AS MAXPOLICYNUMBER FROM PMST_TMP_POLICY", nativeQuery =true)
	Long maxQuotationNumber();

	@Query(value="SELECT * FROM PMST_TMP_POLICY ptp JOIN PMST_POLICY_SERVICE pps ON ptp.POLICY_SERVICE_ID =pps.POLICY_SERVICE_ID AND ptp.QUOTATION_TAGGED_STATUS_ID =?1 and ptp.QUOTATION_NUMBER=?2 " ,nativeQuery =true)
	List<RenewalPolicyTMPEntity> findByquotationTaggedStatusIdJpaRepo(Long quotationTaggedStatusId, String policyNumber);

	@Query(value="SELECT * FROM PMST_TMP_POLICY ptp JOIN PMST_POLICY_SERVICE pps ON ptp.POLICY_SERVICE_ID =pps.POLICY_SERVICE_ID AND PTP.POLICY_TAGGED_STATUS_ID = ?1",nativeQuery=true)
	List<RenewalPolicyTMPEntity> findBypolicytaggedStatusIdJpaRepo(Long policytaggedStatusId);	
	
	@Query(value = "Select *  FROM PMST_TMP_POLICY where POLICY_ID=?1",nativeQuery = true)
	List<Object[]> findBypolicyId1(Long tmpPolicyId);
	
	@Query(value = "SELECT * FROM PMST_TMP_POLICY WHERE POLICY_ID=?1 AND IS_ACTIVE=1", nativeQuery=true)
	RenewalPolicyTMPEntity findBymasterPolicyId(Long masterPolicyId);

	@Query(value="select count(*) AS  TOTAL_MEMBER, ROUND(avg(g.DOB_DER_AGE),2) AS AVERAGE_AGE, ROUND(avg(ptm.BASIC_SALARY),2) AS\r\n"
			+ "			AVG_MONTHLY_SALARY,ROUND(avg(g.PAST_SERVICE),2) AS AVG_PAST_SERVICE, sum(CEIL(g.TOTAL_GRAT)) AS\r\n"
			+ "			TOTAL_SERVICE_GRATUITY,sum(CEIL(g.ACCRUED_GRAT)) AS ACCURED_GRATUITY,CEIL(sum(g.LC_SUM_ASSURED)) AS LCSA,\r\n"
			+ "			CEIL(sum(g.LC_PREMIUM)) AS LC_PREMIUM, CEIL(sum(g.LC_PREMIUM)/100*18) AS GST , ROUND(sum(g.PAST_SERVICE_BENEFIT),2) AS PAST_SERVICE_BENEFIT, ROUND(sum(g.CURRENT_SERVICE_BENEFIT),2) AS CURRENT_SERVICE_BENEFIT from PMST_TMP_MEMBER ptm  inner join\r\n"
			+ "			gratcalculation g on ptm.GRATCALCULATION_ID = g.GRATCALCULATION_ID \r\n"
			+ "			INNER JOIN MEMBER_CATEGORY_MODULE  mc2 on mc2.TMP_POLICY_ID  = ptm.TMP_POLICY_ID \r\n"
			+ "			INNER JOIN MEMBER_CATEGORY mc on\r\n"
			+ "			mc.MEMBER_CATEGORY_ID = MC2.MEMBER_CATEGORY_ID AND ptm.CATEGORY_ID = mc.MEMBER_CATEGORY_ID where ptm.TMP_POLICY_ID =?1 ORDER BY TO_NUMBER(ptm.lic_id)",nativeQuery = true)
	String reportRenewalQuotationPDF(Long tmpPolicyId);

	@Query(value = "UPDATE PMST_TMP_POLICY SET IS_ACTIVE =0 WHERE POLICY_ID  =:tmppolicyId",nativeQuery = true)
	void updateTmpPolicyInactive(Long tmppolicyId);

	@Query(value = "Select * from PMST_TMP_POLICY where POLICY_ID=?1 and MASTER_POLICY_ID=?2",nativeQuery = true)
	RenewalPolicyTMPEntity findBytempidandmasterPolicyIdJpaRepo(Long id, Long masterPolicyId);

	@Query(value = "Select * from PMST_TMP_POLICY where  MASTER_POLICY_ID=?1",nativeQuery = true)
	List<RenewalPolicyTMPEntity> findByMasterPolicyIdJpaRepo(Long masterPolicyId);
	
	@Query(value="SELECT * FROM PMST_TMP_POLICY ptp JOIN PMST_POLICY_SERVICE pps ON ptp.POLICY_SERVICE_ID =pps.POLICY_SERVICE_ID AND ptp.QUOTATION_TAGGED_STATUS_ID =?1 and ptp.QUOTATION_NUMBER=?2 and ptp.UNIT_CODE=?3" ,nativeQuery =true)
	List<RenewalPolicyTMPEntity> findByquotationTaggedStatusIdwithUnitJpaRepo(Long quotationTaggedStatusId,
			String policyNumber, String unitCode);

	@Query(value="SELECT * FROM PMST_TMP_POLICY ptp JOIN PMST_POLICY_SERVICE pps ON ptp.POLICY_SERVICE_ID =pps.POLICY_SERVICE_ID AND ptp.QUOTATION_TAGGED_STATUS_ID =:quotationTaggedStatusId and ptp.QUOTATION_NUMBER=:policyNumber and ptp.UNIT_CODE LIKE CONCAT(CONCAT('%',:getUnitCode),'%') ORDER BY  ptp.UNIT_CODE" ,nativeQuery =true)
	List<RenewalPolicyTMPEntity> findByquotationTaggedStatuswithgetUnitCodeJpaRepo(Long quotationTaggedStatusId,
			String policyNumber,@Param("getUnitCode") String getUnitCode);
	
	@Query(value="SELECT * FROM PMST_TMP_POLICY Where POLICY_NUMBER =?1 AND isActive =0",nativeQuery = true)
	RenewalPolicyTMPEntity findBypolicyNumberJpaRepo(String policyNumber);

	@Query(value="SELECT * FROM PMST_TMP_POLICY ptp JOIN PMST_POLICY_SERVICE pps ON ptp.POLICY_SERVICE_ID =pps.POLICY_SERVICE_ID AND ptp.POLICY_TAGGED_STATUS_ID =?1 and ptp.POLICY_NUMBER=?2 and ptp.UNIT_CODE=?3" ,nativeQuery =true)
	List<RenewalPolicyTMPEntity> findBypolicytaggedStatusIdwithUnitJpaRepo(Long policytaggedStatusId, String policyNumber,
			String unitCode);

	@Query(value="SELECT * FROM PMST_TMP_POLICY ptp JOIN PMST_POLICY_SERVICE pps ON ptp.POLICY_SERVICE_ID =pps.POLICY_SERVICE_ID AND ptp.POLICY_TAGGED_STATUS_ID =:policytaggedStatusId and ptp.POLICY_NUMBER=:policyNumber and ptp.UNIT_CODE LIKE CONCAT(CONCAT('%',:getUnitCode),'%') ORDER BY  ptp.UNIT_CODE" ,nativeQuery =true)
	List<RenewalPolicyTMPEntity> findBypolicytaggedStatusIdwithgetUnitCodeJpaRepo(Long policytaggedStatusId,
			String policyNumber, String getUnitCode);
	
	@Query(value="SELECT * FROM PMST_TMP_POLICY ptp JOIN PMST_POLICY_SERVICE pps ON ptp.POLICY_SERVICE_ID =pps.POLICY_SERVICE_ID AND ptp.POLICY_TAGGED_STATUS_ID =?1 and ptp.POLICY_NUMBER=?2 " ,nativeQuery =true)
	List<RenewalPolicyTMPEntity> findBypolicytaggedStatusIdJpaRepo(Long policytaggedStatusId, String policyNumber);

	@Query(value="select pm.LIC_ID, pm.FIRST_NAME AS Name,pm.EMPLOYEE_CODE,pp.QUOTATION_NUMBER,\r\n"
			+ "to_char(trunc(pm.DATE_OF_BIRTH), 'DD/MM/YYYY') AS DATE_OF_BIRTH, \r\n"
			+ "to_char(trunc(pm.DATE_OF_APPOINTMENT), 'DD/MM/YYYY') AS DATE_OF_APPOINTMENT,pp.CUSTOMER_NAME,\r\n"
			+ "pm.BASIC_SALARY AS Salary, g.PAST_SERVICE,\r\n"
			+ "g.TOTAL_SERVICE, CEIL(g.TOTAL_GRAT) AS TOTAL_SERVICE_GRATUITY, CEIL(g.ACCRUED_GRAT) AS PAST_SERVICE_GRATUITY, \r\n"
			+ "CEIL(g.LC_SUM_ASSURED) AS LIFE_COVER, \r\n"
			+ " pp.ANNUAL_RENEWAL_DATE,g.DOB_DER_AGE AS AGE,ROUND(g.LC_PREMIUM,2) AS LIFE_COVER_PREMIUM,MC.NAME AS CATEGORY,pp.CUSTOMER_CODE\r\n"
			+ "from PMST_TMP_MEMBER pm \r\n"
			+ "inner join GRATCALCULATION g on pm.GRATCALCULATION_ID = g.GRATCALCULATION_ID \r\n"
			+ "INNER JOIN MEMBER_CATEGORY_MODULE  mc2 on mc2.TMP_POLICY_ID  = pm.TMP_POLICY_ID \r\n"
			+ "INNER JOIN MEMBER_CATEGORY mc on\r\n"
			+ "			mc.MEMBER_CATEGORY_ID = MC2.MEMBER_CATEGORY_ID AND pm.CATEGORY_ID = mc.MEMBER_CATEGORY_ID     \r\n"
			+ "INNER JOIN PMST_TMP_POLICY pp ON pm.TMP_POLICY_ID = pp.POLICY_ID \r\n"
			+ "where pm.TMP_POLICY_ID =?1 ORDER BY (pm.EMPLOYEE_CODE)\r\n"
			+ " ",nativeQuery = true)
	List<Object[]> findBytmpPolicyId(Long tmpPolicyId);

	@Query(value="SELECT * FROM PMST_TMP_POLICY php WHERE php.MASTER_POLICY_ID =?1 AND TO_DATE (?2)  >= php.POLICY_START_DATE AND TO_DATE(?2) <= php.POLICY_END_DATE",nativeQuery = true)
	RenewalPolicyTMPEntity findByMasterPolicyIdAndAnnualRenewalDateJpaRepo(Long masterPolicyId, Date annualRenewalDate);

	@Query(value="SELECT * FROM PMST_TMP_POLICY WHERE POLICY_ID =:tmpPolicyId",nativeQuery = true)
	RenewalPolicyTMPEntity findByTmpPolicyId(@Param("tmpPolicyId") Long tmpPolicyId);
	
	@Query(value="SELECT * FROM PMST_TMP_POLICY ptp WHERE POLICY_ID =:tmpPolicyId",nativeQuery = true)
	RenewalPolicyTMPEntity getByPolicyId(@Param("tmpPolicyId") Long tmpPolicyId);
	
	@Query(value="SELECT * FROM PMST_TMP_POLICY ptp WHERE POLICY_NUMBER =?1 and POLICY_STATUS_ID =?2 ",nativeQuery = true)
	RenewalPolicyTMPEntity getByPolicyNubmber(String policyNumber, Long statusId);

	@Query(value="SELECT COUNT(*) FROM PMST_TMP_POLICY  WHERE QUOTATION_SUB_STATUS_ID =?1 ",nativeQuery = true)
	Long getCountRenewalSubStatus(Long quotationSubStatusId);

	@Query("SELECT q.quotationNumber  FROM RenewalPolicyTMPEntity q WHERE q.policyNumber=:policyNumber and q.quotationNumber is not null  ORDER BY q.createdDate  DESC")
	List<String> addQuotationNumber(String policyNumber);
	
	@Modifying
	@Query(value = "UPDATE PMST_TMP_POLICY SET IS_ACTIVE=0 WHERE POLICY_NUMBER=?1 AND POLICY_ID != ?2", nativeQuery = true)
	void deactiveotherpolicydetail(String policyNumber, Long tmpPolicyId);

	@Query(value="SELECT ptp.POLICY_NUMBER FROM PMST_TMP_POLICY ptp WHERE POLICY_ID =?1 ",nativeQuery = true)
	String getByPolicyNubmberbyid(String destinationTmpPolicyId);
	
	@Query(value="SELECT * FROM PMST_TMP_POLICY WHERE POLICY_SERVICE_ID=?1 AND IS_ACTIVE=1", nativeQuery=true)
	RenewalPolicyTMPEntity findByServiceId(Long serviceId);
	
	@Query( value = "SELECT * FROM PMST_TMP_POLICY A " + 
			"JOIN PMST_TMP_AOM_PROPS B ON B.TMP_POLICY_ID=A.POLICY_ID " + 
			"WHERE A.MASTER_POLICY_ID=?1 AND B.IS_ACTIVE=1", nativeQuery=true)
	RenewalPolicyTMPEntity findInMakerBucket(Long masterPolicyId);
	
	@Query( value = "SELECT * FROM PMST_TMP_POLICY A " + 
			"JOIN PMST_MIDLEAVER_PROPS B ON B.TMP_POLICY_ID=A.POLICY_ID " + 
			"WHERE A.MASTER_POLICY_ID=?1 AND B.IS_ACTIVE=1", nativeQuery=true)
	RenewalPolicyTMPEntity findMidLeaversInMakerBucket(Long masterPolicyId);

	Optional<RenewalPolicyTMPEntity> findByPolicyServiceId(Long id);
	
	@Query( value = "SELECT * FROM PMST_TMP_POLICY A " + 
			"JOIN pmst_contri_adj_props B ON B.TEMP_POLICY_ID=A.POLICY_ID " + 
			"WHERE A.MASTER_POLICY_ID=?1 AND B.IS_ACTIVE=1", nativeQuery=true)
	RenewalPolicyTMPEntity findInProgressServiceforcontribadjust(Long mpid);
	
	@Query(value = "SELECT CREATED_DATE FROM PMST_CONTRIBUTION_DETAIL A " + 
			"JOIN PMST_TMP_POLICY B ON B.POLICY_ID=A.TEMP_POLICY_ID " + 
			"WHERE B.POLICY_ID=?1", nativeQuery=true)
	Date getAdjustmentDate(Long tmpPolicyId);
	
	@Query( value = "SELECT * FROM PMST_TMP_POLICY A " + 
			"JOIN PMST_LC_PREM_COLL_PROPS B ON B.TEMP_POLICY_ID=A.POLICY_ID " + 
			"WHERE A.MASTER_POLICY_ID=?1 AND B.IS_ACTIVE=1", nativeQuery=true)
	RenewalPolicyTMPEntity findInProgressServiceforPremiumcollection(Long mpid);

	@Query(value="SELECT * FROM PMST_TMP_POLICY WHERE POLICY_NUMBER=?1", nativeQuery=true)
	RenewalPolicyTMPEntity findByPolicyNumberAndPolicyStatusId(String policyNumber);
	
	@Modifying
	@Query(value = "update pmst_tmp_aom_props ptam set is_active =0 where tmp_policy_id in( Select policy_id from  pmst_tmp_policy ptp where ptp.policy_number=?1)", nativeQuery = true)
	void deactiveOtherPolicyMemberdetailForMidJoiner(String policyNumber);

}
