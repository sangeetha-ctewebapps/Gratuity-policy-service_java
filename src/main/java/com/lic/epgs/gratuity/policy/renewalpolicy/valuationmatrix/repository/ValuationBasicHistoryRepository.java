package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.ValuationBasicHistoryEntity;

public interface ValuationBasicHistoryRepository extends JpaRepository<ValuationBasicHistoryEntity, Long> {

	
//	@Query(value = "SELECT NO_OF_LIVES ,TOTAL_PREMIUM,TOTAL_SUM_ASSURED,ANNUAL_RENEWAL_DATE,AMOUNT_OF_CLAIMS,NO_OF_CLAIMS,flg \r\n"
//			+ "FROM (SELECT NO_OF_LIVES ,TOTAL_PREMIUM,TOTAL_SUM_ASSURED,phv.ANNUAL_RENEWAL_DATE,tdc.AMOUNT_OF_CLAIMS,tdc.NO_OF_CLAIMS,'p' AS flg  FROM PMST_HIS_VALUATIONBASIC phv JOIN\r\n"
//			+ "			TMP_DEATH_CLAIM tdc  ON phv.ANNUAL_RENEWAL_DATE =tdc.ANNUAL_RENEWAL_DATE \r\n"
//			+ "			JOIN PMST_HIS_POLICY php ON php.POLICY_ID=phv.POLICY_ID \r\n"
//			+ "			WHERE tdc.TMP_POLICY_ID =?2 AND php.MASTER_POLICY_ID =?1 UNION \r\n"
//			+ "			SELECT NO_OF_LIVES,TOTAL_PREMIUM,TOTAL_SUM_ASSURED ,pv.ANNUAL_RENEWAL_DATE,td.AMOUNT_OF_CLAIMS,td.NO_OF_CLAIMS,'c' AS flg \r\n"
//			+ "			FROM PMST_VALUATIONBASIC pv\r\n"
//			+ "			JOIN TMP_DEATH_CLAIM td  ON pv.ANNUAL_RENEWAL_DATE =td.ANNUAL_RENEWAL_DATE \r\n"
//			+ "			WHERE td.TMP_POLICY_ID =?2 AND pv.POLICY_ID =?1 ) a ORDER BY flg desc",nativeQuery = true)
//	List<Object[]> findBypolicyId(Long masterPolicyId, Long tmpPolicyId);
	
	
	@Query(value = "SELECT pv.NO_OF_LIVES,pv.TOTAL_PREMIUM,pv.TOTAL_SUM_ASSURED ,pv.ANNUAL_RENEWAL_DATE,td.AMOUNT_OF_CLAIMS,td.NO_OF_LIVES AS NO_OF_CLAIMS, \r\n"
			+ "0 AS PENDING_DEATH_CLAIM, 'p' AS flg \r\n"
			+ "FROM PMST_HIS_VALUATIONBASIC pv \r\n"
			+ "LEFT OUTER JOIN ( \r\n"
			+ "SELECT count(*) AS NO_OF_LIVES, \r\n"
			+ "NVL(sum((CASE WHEN NVL(MODIFIED_GRATUITY_AMOUNT, 0)=0 THEN NVL(GRATUITY_AMT_ON_DATE_OF_EXIT , 0) ELSE  NVL(MODIFIED_GRATUITY_AMOUNT, 0) END)+ NVL(LC_SUM_ASSURED, 0)+NVL(REFUND_PREMIUM_AMOUNT,0) + \r\n"
			+ "NVL(REFUND_GST_AMOUNT,0) +NVL(PENAL_AMOUNT,0)+NVL(COURT_AWARD,0) ),0) \r\n"
			+ "as AMOUNT_OF_CLAIMS,ptp.ANNUAL_RENEWAL_DATE \r\n"
			+ "FROM PMST_TMP_CLAIM_PROPS ptcp \r\n"
			+ "JOIN PMST_TMP_POLICY ptp ON ptcp.TMP_POLICY_ID =ptp.POLICY_ID \r\n"
			+ "JOIN PMST_HIS_VALUATIONBASIC pv ON ptp.MASTER_POLICY_ID =pv.POLICY_ID AND ptp.ANNUAL_RENEWAL_DATE =pv.ANNUAL_RENEWAL_DATE \r\n"
			+ "WHERE MODE_OF_EXIT =193 AND CLAIM_STATUS_ID =211 AND ptp.MASTER_POLICY_ID=?1  \r\n"
			+ "GROUP BY ptp.ANNUAL_RENEWAL_DATE \r\n"
			+ ") td ON pv.ANNUAL_RENEWAL_DATE =td.ANNUAL_RENEWAL_DATE \r\n"
			+ "WHERE pv.POLICY_ID =?1 \r\n"
			+ "UNION \r\n"
			+ "SELECT pv.NO_OF_LIVES,CEIL(pv.TOTAL_PREMIUM),pv.TOTAL_SUM_ASSURED ,pv.ANNUAL_RENEWAL_DATE,td.AMOUNT_OF_CLAIMS,td.NO_OF_LIVES AS NO_OF_CLAIMS, \r\n"
			+ "TD1.PENDING_DEATH_CLAIM, 'c' AS flg \r\n"
			+ "FROM PMST_VALUATIONBASIC pv \r\n"
			+ "LEFT OUTER JOIN ( \r\n"
			+ "SELECT count(*) AS NO_OF_LIVES, \r\n"
			+ "NVL(sum((CASE WHEN NVL(MODIFIED_GRATUITY_AMOUNT, 0)=0 THEN NVL(GRATUITY_AMT_ON_DATE_OF_EXIT , 0) ELSE  NVL(MODIFIED_GRATUITY_AMOUNT, 0) END)+ NVL(LC_SUM_ASSURED, 0)+NVL(REFUND_PREMIUM_AMOUNT,0) + \r\n"
			+ "NVL(REFUND_GST_AMOUNT,0) +NVL(PENAL_AMOUNT,0)+NVL(COURT_AWARD,0) ),0) \r\n"
			+ "as AMOUNT_OF_CLAIMS \r\n"
			+ ",ptp.ANNUAL_RENEWAL_DATE \r\n"
			+ "FROM PMST_TMP_CLAIM_PROPS ptcp \r\n"
			+ "JOIN PMST_TMP_POLICY ptp ON ptcp.TMP_POLICY_ID =ptp.POLICY_ID \r\n"
			+ "JOIN PMST_VALUATIONBASIC pv ON ptp.MASTER_POLICY_ID =pv.POLICY_ID AND ptp.ANNUAL_RENEWAL_DATE =pv.ANNUAL_RENEWAL_DATE \r\n"
			+ "WHERE \r\n"
			+ "MODE_OF_EXIT =193 \r\n"
			+ "AND \r\n"
			+ "CLAIM_STATUS_ID =211 AND ptp.MASTER_POLICY_ID=?1 \r\n"
			+ "GROUP BY ptp.ANNUAL_RENEWAL_DATE \r\n"
			+ ") td ON pv.ANNUAL_RENEWAL_DATE =td.ANNUAL_RENEWAL_DATE \r\n"
           + "LEFT OUTER JOIN ( \r\n"
			+ "SELECT count(*) AS NO_OF_LIVES, \r\n"
			+ "NVL(sum((CASE WHEN NVL(MODIFIED_GRATUITY_AMOUNT, 0)=0 THEN NVL(GRATUITY_AMT_ON_DATE_OF_EXIT , 0) ELSE  NVL(MODIFIED_GRATUITY_AMOUNT, 0) END)+ NVL(LC_SUM_ASSURED, 0)+NVL(REFUND_PREMIUM_AMOUNT,0) + \r\n"
			+ "NVL(REFUND_GST_AMOUNT,0) +NVL(PENAL_AMOUNT,0)+NVL(COURT_AWARD,0) ),0) \r\n"
			+ "as PENDING_DEATH_CLAIM \r\n"
			+ ",ptp.ANNUAL_RENEWAL_DATE \r\n"
			+ "FROM PMST_TMP_CLAIM_PROPS ptcp \r\n"
			+ "JOIN PMST_TMP_POLICY ptp ON ptcp.TMP_POLICY_ID =ptp.POLICY_ID \r\n"
			+ "JOIN PMST_VALUATIONBASIC pv ON ptp.MASTER_POLICY_ID =pv.POLICY_ID AND ptp.ANNUAL_RENEWAL_DATE =pv.ANNUAL_RENEWAL_DATE \r\n"
			+ "WHERE \r\n"
			+ "MODE_OF_EXIT =193 \r\n"
			+ "AND \r\n"
			+ "(CLAIM_STATUS_ID =206 OR CLAIM_STATUS_ID =208 OR CLAIM_STATUS_ID =210) AND ptp.MASTER_POLICY_ID=?1\r\n"
			+ "\r\n"
			+ "GROUP BY ptp.ANNUAL_RENEWAL_DATE \r\n"
			+ ") td1 ON pv.ANNUAL_RENEWAL_DATE =td1.ANNUAL_RENEWAL_DATE \r\n"
			+ "WHERE pv.POLICY_ID =?1",nativeQuery = true)
	List<Object[]> findBypolicyId(Long masterPolicyId, Long tmpPolicyId);
	

}
