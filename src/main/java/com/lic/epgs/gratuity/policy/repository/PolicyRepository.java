package com.lic.epgs.gratuity.policy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.PolicyEntity;

/**
 * @author Gopi
 *
 */


@Repository
public interface PolicyRepository extends JpaRepository<PolicyEntity, Long>{

	Optional<PolicyEntity> findByMasterQuotationId(Long masterQuotationId);
	
	Optional<PolicyEntity> findByPolicyNumber(String policyNumber);
	
	@Query(value="SELECT MAX(TO_NUMBER(POLICY_NUMBER)) AS MAXPOLICYNUMBER FROM PSTG_POLICY", nativeQuery =true)
	Long maxPolicyNumber();
	
	@Query(value ="SELECT REGEXP_SUBSTR(STANDARD_HASH(POLICY_NO,'MD5'), '([1-9])',1,1)||POLICY_NO NEW_POLICY_NO FROM (SELECT ?1 POLICY_NO FROM DUAL)", nativeQuery = true) 
	String maxSipPolicyNumber();

	@Query(value="SELECT MC.NAME AS CATEGORY,g.DOB_DER_AGE as AGE,count(*) AS cnt, sum(g.PAST_SERVICE) AS PAST_SERVICE, \r\n"
			+ " sum(pm.BASIC_SALARY) AS SALARY, SUM(CEIL(g.ACCRUED_GRAT)) AS ACCRUED_GRATUITY, SUM(CEIL(g.TOTAL_GRAT)) AS TOTAL_SERVICE_GRATUITY,\r\n"
			+ " SUM(CEIL(g.LC_SUM_ASSURED)) AS LIFE_COVER,SUM(ROUND(g.LC_PREMIUM,2)) AS LIFE_COVER_PREMIUM\r\n"
			+ " from PMST_MEMBER pm\r\n"
			+ "inner join GRATCALCULATION g on pm.GRATCALCULATION_ID = g.GRATCALCULATION_ID \r\n"
			+ "INNER JOIN PMST_POLICY pp ON pm.POLICY_ID  = pp.POLICY_ID \r\n"
			+ "INNER JOIN MEMBER_CATEGORY mc on mc.PMST_POLICY_ID  = pm.POLICY_ID AND mc.MEMBER_CATEGORY_ID =pm.CATEGORY_ID \r\n"
			+ " where pp.POLICY_ID =:policyId \r\n"
			+ " GROUP BY MC.NAME,g.DOB_DER_AGE\r\n"
			+ " ORDER BY MC.NAME,g.DOB_DER_AGE\r\n"
			+ "",nativeQuery = true)
	List<Object[]> getAgeReport(Long policyId);

	@Query(value="SELECT pp.POLICY_ID AS master_policy_id, pp.POLICY_NUMBER,pp.UNIT_CODE,pp2.PROPOSAL_NUMBER ,\r\n"
			+ "pcd.CREATED_DATE, pcd.CHALLAN_NO ,\r\n"
			+ "PP.POLICY_START_DATE AS ARD ,PP.LAST_PREMIUM_PAID AS adjustmentdate,\r\n"
			+ "pp.ANNUAL_RENEWAL_DATE AS nextArd, pp.CONTRIBUTION_FREQUENCY_ID , pli.PICK_LIST_ITEM_DESCRIPTION AS PremiumMode,\r\n"
			+ "pm.MPH_NAME, \r\n"
			+ "pma.ADDRESS_LINE1 ,pma.ADDRESS_LINE2 ,pma.ADDRESS_LINE3,\r\n"
			+ "PD.COLLECTION_NO ,PD.COLLECTION_DATE ,PD.AVAILABLE_AMOUNT,\r\n"
			+ "PCD.LIFE_PREMIUM , PCD.GST ,pp.PRODUCT_ID,pcd.CURRENT_SERVICE,pcd.PAST_SERVICE\r\n"
			+ "FROM PMST_POLICY pp \r\n"
			+ "JOIN PMST_MPH pm ON pm.MPH_ID =pp.MPH_ID \r\n"
			+ "JOIN PMST_MPH_ADDRESS pma ON pma.MPH_ID =pm.MPH_ID \r\n"
			+ "JOIN PMST_DEPOSIT pd ON PD.MASTER_POLICY_ID =PP.POLICY_ID \r\n"
			+ "JOIN PSTG_POLICY pp2 ON PP2.POLICY_NUMBER =PP.POLICY_NUMBER \r\n"
			+ "JOIN PSTG_CONTRIBUTION_DETAIL pcd ON PP2.MASTER_QUOTATION_ID =PCD.MASTER_QUOTATION_ID \r\n"
			+ "JOIN PICK_LIST_ITEM pli ON pli.PICK_LIST_ITEM_ID =pp.CONTRIBUTION_FREQUENCY_ID\r\n"
			+ "WHERE pp.POLICY_ID =?1",nativeQuery = true)
	List<Object[]> findBypremiumreceiptdata(Long masterpolicyId);

	
	@Query(value="SELECT ptp.POLICY_NUMBER,ptp.UNIT_CODE,ptp.product_id,\r\n"
			+ "ptm.MPH_NAME,\r\n"
			+ "pcd.CONTRIBUTION_DETAIL_ID, pcd.CHALLAN_NO , pcd.CREATED_DATE AS challan_date\r\n"
			+ "FROM PMST_POLICY ptp \r\n"
			+ "JOIN PMST_MPH ptm ON ptm.MPH_ID = ptp.MPH_ID\r\n"
			+ "JOIN PMST_CONTRIBUTION_DETAIL pcd  ON pcd.MASTER_POLICY_ID =ptp.POLICY_ID \r\n"
			+ "WHERE ptp.POLICY_ID =?1",nativeQuery = true)
	List<Object[]> payoutvoucherReport(Long policyId);

	
	@Query(value="SELECT DEBIT_CODE ,DEBIT_CODE_DESCRIPTION ,DEBIT_AMOUNT ,CREDIT_CODE ,CREDIT_CODE_DESCRIPTION ,CREDIT_AMOUNT \r\n"
			+ "FROM ACCT_CR_DR_RESPONSE acdr WHERE ACCT_RESPONSE_ID \r\n"
			+ "IN (SELECT ID FROM ACCT_RESPONSE ar WHERE module='PREMIUM-ADJUST' AND REFERENCE_ID =:contributionDetailId AND ar.REFERENCE_VALUE='PMST-CONT-DET-ID')",nativeQuery = true)
	List<Object[]> paymentvoucher(Long contributionDetailId);

//	List<Object[]> getcbsheetpdf(Long policyId);
}
