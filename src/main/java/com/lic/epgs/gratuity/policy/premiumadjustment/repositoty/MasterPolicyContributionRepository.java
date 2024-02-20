package com.lic.epgs.gratuity.policy.premiumadjustment.repositoty;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContributionEntity;
@Repository
public interface MasterPolicyContributionRepository extends JpaRepository<MasterPolicyContributionEntity,Long>{
		
	@Query(value = "WITH POLICIES AS"
			+ "						 ( SELECT"
			+ "						     PM.POLICY_ID,"
			+ "						     PM.POLICY_NUMBER,"
			+ "						     PM.PRODUCT_VARIANT_ID,"
			+ "						     PM.POLICY_STATUS_ID"
			+ "						     FROM PMST_POLICY PM"
			+ "						     WHERE PM.POLICY_ID = ?1"
			+ "						 ),"
			+ "						 POLICY_OB AS"
			+ "						 ( SELECT"
			+ "						 	TO_CHAR('') AS CONTRIBUTION_ID,"
			+ "						    TRUNC(PTE.TRANSACTION_DATE) AS CDATE,"
			+ "						     PTE.ENTRY_TYPE AS TYPE,"
			+ "						     0 AS EMPLOYEE_CONTRIBUTION,"
			+ "						     0 AS EMPLOYER_CONTRIBUTION,"
			+ "						     0 AS VOLUNTARY_CONTRIBUTION,"
			+ "						     DECODE(COALESCE(PTE.TOTAL_CONTRIBUTION,0),0,COALESCE(PTE.OPENING_BALANCE,0),COALESCE(PTE.TOTAL_CONTRIBUTION,0)) TOTAL_CONTRIBUTION"
			+ "						     FROM POLICY_TRANSACTION_ENTRIES PTE, POLICIES PC"
			+ "						     WHERE"
			+ "						     PTE.POLICY_ID = PC.POLICY_ID AND"
			+ "						     PTE.ENTRY_TYPE = 'OB' AND"
			+ "						     PTE.FINANCIAL_YEAR = ?2 AND"
			+ "						     PTE.STATEMENT_FREQUENCY = CASE WHEN PC.PRODUCT_VARIANT_ID = ?5 THEN ?4 ELSE 0 END"
			+ "						 ),"
			+ "						 POL_CONTRIBUTIONS AS"
			+ "						 ( SELECT"
			+ "						     CONTRIBUTION_ID,"
			+ "						     TRUNC(CDATE) AS CDATE,"
			+ "						     TYPE,"
			+ "						     SUM(EMPLOYEE_CONTRIBUTION) AS EMPLOYEE_CONTRIBUTION,"
			+ "						     SUM(EMPLOYER_CONTRIBUTION) AS EMPLOYER_CONTRIBUTION,"
			+ "						     SUM(VOLUNTARY_CONTRIBUTION) AS VOLUNTARY_CONTRIBUTION,"
			+ "						     SUM(TOTAL_CONTRIBUTION) AS TOTAL_CONTRIBUTION"
			+ "						     FROM"
			+ "						     ( SELECT"
			+ "						         POL.POLICY_ID,"
			+ "						         TO_CHAR(PC.CONTRIBUTION_ID) AS CONTRIBUTION_ID,"
			+ "						         PC.FINANCIAL_YEAR,"
			+ "						         PC.CONTRIBUTION_DATE AS CDATE,"
			+ "						         PC.CONTRIBUTION_TYPE AS TYPE,"
			+ "						         COALESCE(PC.EMPLOYEE_CONTRIBUTION,0) AS EMPLOYEE_CONTRIBUTION,"
			+ "						         COALESCE(PC.EMPLOYER_CONTRIBUTION,0) AS EMPLOYER_CONTRIBUTION,"
			+ "						         COALESCE(PC.VOLUNTARY_CONTRIBUTION,0) AS VOLUNTARY_CONTRIBUTION,"
			+ "						         COALESCE(PC.TOTAL_CONTRIBUTION,0) AS TOTAL_CONTRIBUTION"
			+ "						         FROM"
			+ "						         PMST_CONTRIBUTION PC, POLICIES POL"
			+ "						         WHERE"
			+ "						         POL.POLICY_ID = PC.POLICY_ID AND"
			+ "						         PC.FINANCIAL_YEAR = ?2"
			+ "						         AND PC.QUARTER = CASE WHEN POL.PRODUCT_VARIANT_ID = ?5 THEN ?3 ELSE 'Q0' END"
			+ "						         ORDER BY PC.CONTRIBUTION_ID"
			+ "						     )"
			+ "						     GROUP BY CONTRIBUTION_ID,TRUNC(CDATE),TYPE"
			+ "						 )"
			+ "						SELECT"
			+ "						     ROWNUM ROWNO,"
			+ "						     TC.*,"
			+ "						     SUM(TC.TOTAL_CONTRIBUTION) OVER (ORDER BY ROWNUM) AS CLOSINGBALANCE"
			+ "						     FROM"
			+ "						     ( SELECT * FROM POLICY_OB POB"
			+ "						         UNION"
			+ "						        SELECT * FROM POL_CONTRIBUTIONS PC"
			+ "						     ) TC", nativeQuery = true)
	List<Object[]> getPolicyContributionDetails(Long policyId, String financialyear, String quarter, Integer frequency,
			Long variandId);

	
	@Modifying
	@Query(value="UPDATE PMST_CONTRIBUTION SET IS_ACTIVE = 0 where CONTRIBUTION_DETAIL_ID IN (select CONTRIBUTION_DETAIL_ID from pmst_contribution_detail where master_policy_id = ?1 AND ENTRY_TYPE = 'NB')",nativeQuery = true)
	void isactivefalse(Long id);
	
	@Query(value="SELECT MAX(TO_NUMBER(VERSION_NO)) AS VERSIONNUMBER FROM PMST_CONTRIBUTION where FINANCIAL_YEAR=?1 and MASTER_POLICY_ID=?2",nativeQuery=true)
	Long getMaxVersion(String financialYear, Long masterPolicyId);
	
}
