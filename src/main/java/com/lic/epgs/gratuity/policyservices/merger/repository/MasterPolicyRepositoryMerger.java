package com.lic.epgs.gratuity.policyservices.merger.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;

public interface MasterPolicyRepositoryMerger extends JpaRepository<MasterPolicyEntity, Long> {

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
			+ "			where pp.POLICY_ID=?1", nativeQuery = true)
	String getBondDetail(Long policyId);

	@Query(value = "SELECT * FROM PMST_POLICY WHERE TRUNC(ANNUAL_RENEWAL_DATE) - TRUNC(CURRENT_DATE) = ?1", nativeQuery = true)
	List<MasterPolicyEntity> findNotInPolicyRenewalJpaRepo(String value);

	@Query(value = "SELECT * FROM PMST_POLICY WHERE TRUNC(ANNUAL_RENEWAL_DATE) - TRUNC(CURRENT_DATE) = ?1", nativeQuery = true)
	List<MasterPolicyEntity> findNotInPolicyRenewalRemainderJpaRepo(String value);

	@Query(value = "SELECT * FROM PMST_POLICY pp\r\n"
			+ "		JOIN  PMST_RENEWAL_REMINDERS prr ON pp.POLICY_ID = prr.POLICY_ID AND \r\n"
			+ "		PP.ANNUAL_RENEWAL_DATE = prr.ANNUAL_RENEWAL_DATE and prr.IS_ACTIVE =1", nativeQuery = true)
	List<MasterPolicyEntity> fetchpolicyDetailsJpaRepo();

	@Query(value = "SELECT MODULE_SEQ_FUNC(?1) FROM DUAL", nativeQuery = true)
	String getSequence(String type);

	@Query(value = "SELECT REGEXP_SUBSTR(STANDARD_HASH(POLICY_NO,'MD5'), '([1-9])',1,1)||POLICY_NO NEW_POLICY_NO FROM (SELECT ?1 POLICY_NO FROM DUAL)", nativeQuery = true)
	String getPolicyNumber(@Param("policyNumber") String policyNumber);

	MasterPolicyEntity findByIdAndIsActiveTrue(Long policyId);

	@Query(value = "SELECT * FROM PMST_POLICY pp  JOIN  PMST_RENEWAL_REMINDERS prr ON pp.POLICY_ID = prr.POLICY_ID AND\r\n"
			+ "			PP.ANNUAL_RENEWAL_DATE = prr.ANNUAL_RENEWAL_DATE and prr.IS_ACTIVE =1 AND pp.POLICY_NUMBER =:policyNumber", nativeQuery = true)
	List<MasterPolicyEntity> findByPolicyNumberJpaRepo(String policyNumber);

	@Query(value = "SELECT * FROM PMST_POLICY WHERE POLICY_NUMBER=?1", nativeQuery = true)
	MasterPolicyEntity findPolicyDetailJpaRepo(String policyNumber);

	@Query(value = "SELECT *  FROM PMST_POLICY pp WHERE pp.POLICY_NUMBER =?1 AND pp.POLICY_ID NOT IN (SELECT  pps.POLICY_ID FROM PMST_POLICY_SERVICE pps WHERE pps.IS_ACTIVE =1)", nativeQuery = true)
	MasterPolicyEntity findBypolicyNumberJpaRepo(String policyNumber);

	@Query(value = "SELECT *  FROM PMST_POLICY pp WHERE pp.POLICY_NUMBER =?1 AND pp.POLICY_ID NOT IN (SELECT  pps.POLICY_ID FROM PMST_POLICY_SERVICE pps WHERE pps.IS_ACTIVE =1 )", nativeQuery = true)
	List<MasterPolicyEntity> findPolicyDetailSeerchJpaRepo(String policyNumber);

	@Query(value = "SELECT *  FROM PMST_POLICY pp WHERE pp.POLICY_NUMBER =?1 ", nativeQuery = true)
	MasterPolicyEntity findByPolicyNumberisactiveJpaRepo(String policyNumber);

	@Query(value = "SELECT * FROM PMST_POLICY pp WHERE pp.POLICY_NUMBER  =?1 AND pp.UNIT_CODE =?2", nativeQuery = true)
	List<MasterPolicyEntity> findByPolicyNumberwithUnitcodeJpaRepo(String policyNumber, String unitcode);

	@Query(value = "SELECT * FROM PMST_POLICY pp WHERE pp.POLICY_NUMBER  =?1 and IS_ACTIVE=1", nativeQuery = true)
	List<MasterPolicyEntity> findBypolicyNumberandActiveJpaRepo(String policyNumber);

	@Query(value = "SELECT * FROM PMST_POLICY php WHERE php.POLICY_ID =?1 AND TO_DATE (?2,'dd/MM/yyyy')  >= php.POLICY_START_DATE AND TO_DATE (?2,'dd/MM/yyyy') <= php.POLICY_END_DATE", nativeQuery = true)
	MasterPolicyEntity findBymasterPolicyIdJpaRepo(Long masterPolicyId, String dateOfExit);

	@Query(value = "SELECT * FROM PMST_POLICY php WHERE php.POLICY_ID =?1 AND TO_DATE (?2,'dd/MM/yyyy')  > php.POLICY_END_DATE", nativeQuery = true)
	MasterPolicyEntity findByGreaterStartDateandExitDateJpaRepo(Long masterPolicyId, String dateOfExit);

	@Query(value = "SELECT POLICY_ID FROM PMST_POLICY WHERE POLICY_NUMBER =?1", nativeQuery = true)
	Long getPolicyIdClaim(String policyNo);

	@Query(value = "SELECT * FROM PMST_CONTRIBUTION_DETAIL WHERE CONTRIBUTION_DETAIL_ID =:contriId AND ENTRY_TYPE = 'NB'", nativeQuery = true)
	MasterPolicyContributionDetails findByContriIdAndEntryType(@Param("contriId") Long contriId);

	@Query(value = "SELECT * FROM PMST_POLICY WHERE MPH_ID =?1", nativeQuery = true)
	MasterPolicyEntity getPolicyByMphId(Long mphId);

	@Query(value = "SELECT POLICY_STATUS_ID FROM PMST_POLICY WHERE POLICY_NUMBER =?1", nativeQuery = true)
	Long getStatusByPolicy(String policyNumber);

	MasterPolicyEntity findByPolicyNumberAndPolicyStatusIdAndIsActiveTrue(String mergingPolicy, Long commonApproved);

	MasterPolicyEntity findByPolicyNumberAndIsActiveTrue(String mergingPolicy);

	@Query(value = "SELECT " + "pp.POLICY_NUMBER, " // 0
			+ "pp.POLICY_ID, " // 1
			+ "pli.pick_list_item_name AS master_policy_status, " // 2
			+ "pp.POLICY_STATUS_ID, " // 3
			+ "pp.ANNUAL_RENEWAL_DATE, " // 4
			+ "pc.EMPLOYEE_CONTRIBUTION, " // 5
			+ "pc.EMPLOYER_CONTRIBUTION, " // 6
			+ "pc.VOLUNTARY_CONTRIBUTION, " // 7
			+ "pc.TOTAL_CONTRIBUTION, " // 8
			+ "pv.NO_OF_LIVES, " // 9
			+ "pm.MPH_NAME, " // 10 (Added a comma)
			+ "pp.PREMIUM_MODE AS frequency " // 11
			+ "FROM PMST_POLICY pp \r\n" + "JOIN PMST_CONTRIBUTION pc ON pp.POLICY_ID = pc.MASTER_POLICY_ID \r\n"
			+ "JOIN PMST_VALUATIONBASIC pv ON pp.POLICY_ID = pv.POLICY_ID \r\n"
			+ "JOIN PMST_MPH pm ON pp.MPH_ID = pm.MPH_ID\r\n"
			+ "JOIN PICK_LIST_ITEM pli ON pli.PICK_LIST_ITEM_ID = pp.PREMIUM_MODE\r\n"
			+ "WHERE pp.policy_number= :policyNumber AND pp.IS_ACTIVE = 1 FETCH FIRST 1 ROW ONLY", nativeQuery = true)
	Object[] findAllDetailsByPolicyNumber(String policyNumber);
}
