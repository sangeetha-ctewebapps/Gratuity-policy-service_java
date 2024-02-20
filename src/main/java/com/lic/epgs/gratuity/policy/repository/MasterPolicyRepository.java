package com.lic.epgs.gratuity.policy.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;

@Repository
public interface MasterPolicyRepository extends JpaRepository<MasterPolicyEntity, Long> {

	@Query(value = "SELECT pp.POLICY_NUMBER AS POLICYNUMBER,to_char(trunc(pp.ANNUAL_RENEWAL_DATE), 'DD/MM/YYYY') AS ANNUALRENEWALDATE,\r\n"
			+ "pp.TOTAL_MEMBER AS TOTALMEMBERS,qm.PROPOSAL_NUMBER AS PROPOSALNUMBER\r\n"
			+ "			,to_char(trunc(pv.VALUATION_EFFECTIVE_DATE ), 'DD/MM/YYYY')AS VALUATIONEFFECTIVEDATE,\r\n"
			+ "			to_char(trunc(pp.POLICY_START_DATE  ), 'DD/MM/YYYY') AS POLICYISSUANCEDATE,\r\n"
			+ "			NVL(pcd.LIFE_PREMIUM,0)+NVL(pcd.GST ,0)+NVL(pcd.PAST_SERVICE ,0)+NVL(pcd.CURRENT_SERVICE ,0) AS Adjustmentamount,\r\n"
			+ "			pv.TOTAL_GRATUITY ,pv.TOTAL_PREMIUM ,pv.TOTAL_SUM_ASSURED,pp.UNIT_CODE\r\n"
			+ "			FROM PMST_POLICY pp\r\n"
			+ "			JOIN PMST_VALUATIONBASIC pv ON pp.policy_id=pv.policy_id\r\n"
			+ "			JOIN PSTG_POLICY ps ON pp.policy_number=ps.policy_number\r\n"
			+ "			JOIN QMST_QUOTATION qm ON ps.MASTER_QUOTATION_ID=qm.QUOTATION_ID\r\n"
			+ "			JOIN PMST_CONTRIBUTION_DETAIL pcd ON pp.POLICY_ID =pcd.MASTER_POLICY_ID \r\n"
			+ "			where pp.POLICY_ID=?1",nativeQuery = true)
	String getBondDetail(Long policyId);

	@Query(value = "SELECT * FROM PMST_POLICY WHERE TRUNC(ANNUAL_RENEWAL_DATE) - TRUNC(CURRENT_DATE) = ?1", nativeQuery = true)
	List<MasterPolicyEntity> findNotInPolicyRenewalJpaRepo(String value);

	@Query(value = "SELECT * FROM PMST_POLICY WHERE TRUNC(ANNUAL_RENEWAL_DATE) - TRUNC(CURRENT_DATE) = ?1", nativeQuery = true)
	List<MasterPolicyEntity> findNotInPolicyRenewalRemainderJpaRepo(String value);

	@Query(value ="SELECT * FROM PMST_POLICY pp\r\n"
			+ "		JOIN  PMST_RENEWAL_REMINDERS prr ON pp.POLICY_ID = prr.POLICY_ID AND \r\n"
			+ "		PP.ANNUAL_RENEWAL_DATE = prr.ANNUAL_RENEWAL_DATE and prr.IS_ACTIVE =1",nativeQuery = true)
	 List<MasterPolicyEntity>fetchpolicyDetailsJpaRepo();

	@Query(value = "SELECT MODULE_SEQ_FUNC(?1) FROM DUAL", nativeQuery = true)
	String getSequence(String type);


	@Query(value = "SELECT REGEXP_SUBSTR(STANDARD_HASH(POLICY_NO,'MD5'), '([1-9])',1,1)||POLICY_NO NEW_POLICY_NO FROM (SELECT ?1 POLICY_NO FROM DUAL)", nativeQuery = true)
	String getPolicyNumber(@Param("policyNumber") String policyNumber);

	MasterPolicyEntity findByIdAndIsActiveTrue(Long policyId);

	@Query(value = "SELECT * FROM PMST_POLICY pp  JOIN  PMST_RENEWAL_REMINDERS prr ON pp.POLICY_ID = prr.POLICY_ID AND\r\n"
			+ "			PP.ANNUAL_RENEWAL_DATE = prr.ANNUAL_RENEWAL_DATE and prr.IS_ACTIVE =1 AND pp.POLICY_NUMBER =:policyNumber", nativeQuery = true)
	List<MasterPolicyEntity> findByPolicyNumberJpaRepo(String policyNumber);
	
	//new
	@Query(value = "SELECT * FROM PMST_POLICY pp  JOIN  PMST_RENEWAL_REMINDERS prr ON pp.POLICY_ID = prr.POLICY_ID AND\r\n"
			+ "			PP.ANNUAL_RENEWAL_DATE = prr.ANNUAL_RENEWAL_DATE AND pp.POLICY_NUMBER =:policyNumber", nativeQuery = true)
	List<MasterPolicyEntity> findByPolicyNumberJpa(String policyNumber);

	@Query(value = "SELECT * FROM PMST_POLICY WHERE POLICY_NUMBER=?1", nativeQuery = true)
	MasterPolicyEntity findPolicyDetailJpaRepo(String policyNumber);
	
	@Query(value ="SELECT *  FROM PMST_POLICY pp WHERE pp.POLICY_NUMBER =?1 AND pp.POLICY_ID NOT IN (SELECT  pps.POLICY_ID FROM PMST_POLICY_SERVICE pps WHERE pps.POLICY_ID=pp.POLICY_ID AND pps.IS_ACTIVE =1)",nativeQuery = true)
	MasterPolicyEntity findBypolicyNumberJpaRepo(String policyNumber);
		
	@Query(value ="SELECT *  FROM PMST_POLICY pp WHERE pp.POLICY_NUMBER =?1 AND pp.POLICY_ID NOT IN (SELECT  pps.POLICY_ID FROM PMST_POLICY_SERVICE pps WHERE pps.IS_ACTIVE =1 )",nativeQuery = true)
	List<MasterPolicyEntity> findPolicyDetailSeerchJpaRepo(String policyNumber);
	
	@Query(value ="SELECT *  FROM PMST_POLICY pp WHERE pp.POLICY_NUMBER =?1 ",nativeQuery = true)
	MasterPolicyEntity findByPolicyNumberisactiveJpaRepo(String policyNumber);

	@Query(value = "SELECT * FROM PMST_POLICY pp WHERE pp.POLICY_NUMBER  =?1 AND pp.UNIT_CODE =?2",nativeQuery = true)
	List<MasterPolicyEntity> findByPolicyNumberwithUnitcodeJpaRepo(String policyNumber, String unitcode);

	@Query(value = "SELECT * FROM PMST_POLICY pp WHERE pp.POLICY_NUMBER  =?1 and IS_ACTIVE=1",nativeQuery = true)
	List<MasterPolicyEntity> findBypolicyNumberandActiveJpaRepo(String policyNumber);

	@Query(value="SELECT * FROM PMST_POLICY php WHERE php.POLICY_ID =?1 AND TO_DATE (?2,'dd/MM/yyyy')  >= php.POLICY_START_DATE AND TO_DATE (?2,'dd/MM/yyyy') <= php.POLICY_END_DATE",nativeQuery = true)
	MasterPolicyEntity findBymasterPolicyIdJpaRepo(Long masterPolicyId, String dateOfExit);
	
	@Query(value="SELECT * FROM PMST_POLICY php WHERE php.POLICY_ID =?1 AND TO_DATE (?2,'dd/MM/yyyy')  > php.POLICY_END_DATE",nativeQuery = true)
	MasterPolicyEntity findByGreaterStartDateandExitDateJpaRepo(Long masterPolicyId, String dateOfExit);
	
	@Query(value = "SELECT POLICY_ID FROM PMST_POLICY WHERE POLICY_NUMBER =?1",nativeQuery = true)
	Long getPolicyIdClaim(String policyNo);

	@Query(value= "SELECT * FROM PMST_CONTRIBUTION_DETAIL WHERE CONTRIBUTION_DETAIL_ID =:contriId AND ENTRY_TYPE = 'NB'",nativeQuery = true)
	MasterPolicyContributionDetails findByContriIdAndEntryType(@Param("contriId") Long contriId);
	
	@Query(value = "SELECT * FROM PMST_POLICY WHERE MPH_ID =?1",nativeQuery = true)
	MasterPolicyEntity getPolicyByMphId(Long mphId);

	@Query(value="SELECT count(*) FROM PMST_POLICY where TRUNC(ANNUAL_RENEWAL_DATE) <= TRUNC(CURRENT_DATE) and TRUNC(CURRENT_DATE) - TRUNC(ANNUAL_RENEWAL_DATE) <= 30 and policy_id=?",nativeQuery = true)
	int getARDStatusCount(Long masterpolicyid);

	MasterPolicyEntity findByPolicyNumber(String policyNumber);
	
	@Query(value = "SELECT * FROM PMST_POLICY WHERE POLICY_ID = ?1 " + 
			"AND ?2>=POLICY_START_DATE " + 
			"AND ?2<=POLICY_END_DATE ", nativeQuery=true)
	MasterPolicyEntity findByIdAndPeriod(Long policyId, Date eventDate);

	MasterPolicyEntity findByPolicyNumberAndIsActiveTrue(String policyNumber);

	@Query(value="select Pick_list_item_name from pick_list_item where pick_list_item_id=?1",nativeQuery = true)
	String getPremiumMode(long contributtionFrequencyId);
	
	MasterPolicyEntity getByPolicyNumber(String policyNumber);

	@Query(value="SELECT * FROM PMST_POLICY where policy_id=?1 and NEXT_DUE_DATE is not null",nativeQuery = true)
	MasterPolicyEntity getNextDue(Long masterPolicyId);

	@Query(value = " SELECT  "
			+ "    PM.POLICY_ID as policyId "
			+ "    ,PM.POLICY_NUMBER as policyNumber "
			+ "    ,CASE "
			+ "        WHEN PFSS.POLICY_ID IS NULL THEN 0 ELSE 1 "
			+ "    END AS isFundGenerated "
			+ "FROM "
			+ "    PMST_POLICY PM "
			+ "    LEFT OUTER JOIN POLICY_FUND_STATEMENT_SUMMARY PFSS "
			+ "    ON  "
			+ "    ( "
			+ "        PM.POLICY_ID = PFSS.POLICY_ID AND "
			+ "        PFSS.STATEMENT_TYPE = 'QUARTER_BATCH' AND "
			+ "        PFSS.FINANCIAL_YEAR =:finYear AND  "
			+ "        PFSS.STATEMENT_FREQUENCY =:frequency AND "
			+ "        PFSS.IS_ACTIVE = 1 "
			+ "    ) "
			+ "WHERE "
			+ "    PM.UNIT_CODE=:unitId AND "
			+ "    PM.POLICY_STATUS_ID IN (123) AND PRODUCT_VARIANT_ID=:variant", nativeQuery = true)
	List<Object[]> findByYearQtrVariant( String unitId, String finYear, String frequency, String variant);
	
	@Query(value = "SELECT "
			+ "POLICY_NUMBER,LIC_ID,FIRST_NAME, OPENING_BALANCE, TOTAL_CONTRIBUTION, OPENING_BALANCE_INT,TOTAL_INTEREST_AMOUNT, FMC, GST,CLOSING_BALANCE, TRAN_FROM_DATE, TRAN_TO_DATE, UNIT_CODE "
			+ "FROM "
			+ "(SELECT "
			+ "PM.POLICY_NUMBER,MM.LIC_ID,MM.FIRST_NAME,PFSS.OPENING_BALANCE,PFSS.TOTAL_CONTRIBUTION,PFSS.OPENING_BALANCE_INT, "
			+ "PFSS.TOTAL_INTEREST_AMOUNT,PFSS.FMC,PFSS.GST,PFSS.CLOSING_BALANCE,PFSS.TRAN_FROM_DATE,PFSS.TRAN_TO_DATE,PM.UNIT_CODE, "
			+ "ROW_NUMBER() OVER (PARTITION BY PFSS.POLICY_ID ORDER BY PFSS.CREATED_ON DESC) ROW_RANK, "
			+ "PFSS.IS_ACTIVE "
			+ "FROM POLICY_FUND_STATEMENT_SUMMARY PFSS "
			+ "JOIN PMST_POLICY PM ON PM.POLICY_ID = PFSS.POLICY_ID "
			+ "JOIN PMST_MEMBER MM ON MM.POLICY_ID = PFSS.POLICY_ID "
			+ "WHERE PFSS.POLICY_ID =:policyId AND STATEMENT_TYPE = 'QUARTER_BATCH' "
			+ ") "
			+ "WHERE ROW_RANK=1 "
			+ "ORDER BY POLICY_NUMBER ", nativeQuery = true)
	List<Object[]> downloadPolicyFundByPolicyId(String policyId);
}