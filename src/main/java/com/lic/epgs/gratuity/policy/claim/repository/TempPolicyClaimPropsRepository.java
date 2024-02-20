package com.lic.epgs.gratuity.policy.claim.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimBeneficiaryEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimPropsEntity;

public interface TempPolicyClaimPropsRepository extends JpaRepository<TempPolicyClaimPropsEntity, Long>{
//	@Query(value="SELECT MAX(ONBOARD_NUMBER) AS MAXPOLICYNUMBER FROM PMST_TMP_CLAIM_PROPS", nativeQuery =true)
//	Long maxOnboardedNumber();

//	@Query(value="SELECT * FROM PMST_TMP_CLAIM_PROPS WHERE  ")
	TempPolicyClaimPropsEntity findByonboardNumber(String onboardNumber);

	@Query(value="SELECT * FROM PMST_TMP_CLAIM_PROPS WHERE CLAIM_STATUS_ID IN :ids ORDER BY CLAIM_PROPS_ID ",nativeQuery = true)
	List<TempPolicyClaimPropsEntity> findByclaimstatusids(List<Long> ids);
	
//	@Query(value="SELECT MAX(INTIMATION_NUMBER) AS MAXPOLICYNUMBER FROM PMST_TMP_CLAIM_PROPS", nativeQuery =true)
//	Long genrateintimationno();
	
	

	
	  @Query(value="select ptm.mph_name,ptcp.intimation_number,ptcp.intimation_date, ptp.policy_number,\r\n"
	  		+ "ptp.annual_renewal_date, ptm2.date_of_birth, ptm2.DATE_OF_APPOINTMENT, ptcp.date_of_exit,\r\n"
	  		+ "ptcp.date_of_death, pli.PICK_LIST_ITEM_DESCRIPTION AS modeofexit,pli2.PICK_LIST_ITEM_DESCRIPTION AS causeofdeath,\r\n"
	  		+ "ptcp.REASON_FOR_DEATH_OTHER,ptcp.PLACE_OF_DEATH,ptm2.EMPLOYEE_CODE ,ptm2.LIC_ID,\r\n"
	  		+ "(ptm2.FIRST_NAME || ptm2.LAST_NAME ) AS membername, ptcp.SALARY_AS_ON_DATE_OF_EXIT, ptm2.BASIC_SALARY AS Salary_as_on_ard \r\n"
	  		+ ",ptcp.LC_SUM_ASSURED AS LCSA_payable, REFUND_PREMIUM_AMOUNT, REFUND_GST_AMOUNT, mc.NAME \r\n"
	  		+ "from PMST_TMP_CLAIM_PROPS ptcp \r\n"
	  		+ "JOIN PMST_TMP_POLICY ptp on ptcp.tmp_policy_id=ptp.policy_id \r\n"
	  		+ "join PMST_TMP_MPH ptm on ptm.TMP_POLICY_ID =ptp.POLICY_ID \r\n"
	  		+ "join PMST_TMP_MEMBER ptm2  on ptm2.tmp_policy_id=ptp.policy_id \r\n"
	  		+ "join PICK_LIST_ITEM pli ON pli.PICK_LIST_ITEM_ID =ptcp.Mode_of_exit \r\n"
	  		+ "JOIN MEMBER_CATEGORY mc ON mc.MEMBER_CATEGORY_ID = ptm2.CATEGORY_ID \r\n"
	  		+ "LEFT OUTER JOIN PICK_LIST_ITEM pli2 ON pli2.PICK_LIST_ITEM_ID =ptcp.REASON_FOR_DEATH_ID \r\n"
	  		+ "where ptcp.onboard_number=?1",nativeQuery =true)
	 List<Object> findClainCalculation(String onboardNumber);
	  
	  
	  

		@Query(value="SELECT ptcp.ONBOARD_NUMBER,ptp.POLICY_NUMBER,ptm.MPH_CODE,ptm2.LIC_ID ,ptm2.EMPLOYEE_CODE, ptcp.CLAIM_TYPE\r\n"
				+ ",pli.PICK_LIST_ITEM_NAME AS modeofexitname, pli1.PICK_LIST_ITEM_NAME  AS claim_status_name\r\n"
				+ "FROM PMST_TMP_CLAIM_PROPS ptcp \r\n"
				+ "JOIN pmst_tmp_policy ptp ON ptp.POLICY_ID =ptcp.TMP_POLICY_ID \r\n"
				+ "JOIN pmst_tmp_mph ptm ON ptm.TMP_POLICY_ID =ptcp.TMP_POLICY_ID \r\n"
				+ "JOIN pmst_tmp_member ptm2 ON ptm2.TMP_POLICY_ID=ptcp.TMP_POLICY_ID \r\n"
				+ "JOIN PICK_LIST_ITEM pli ON pli.PICK_LIST_ITEM_ID =ptcp.MODE_OF_EXIT\r\n"
				+ "JOIN PICK_LIST_ITEM pli1 ON pli1.PICK_LIST_ITEM_ID =ptcp.CLAIM_STATUS_ID  ORDER BY ONBOARD_NUMBER ASC", nativeQuery =true)
	List<Object> filterclaimpropsbasedontype();

		@Query(value="SELECT * FROM PMST_TMP_CLAIM_PROPS WHERE BATCH_ID=:batchId ORDER BY CLAIM_PROPS_ID ",nativeQuery = true)
		List<TempPolicyClaimPropsEntity> findBybatchId(Long batchId);

		@Query(value="SELECT ptcp.ONBOARD_NUMBER,ptp.POLICY_NUMBER,ptm.MPH_CODE,ptm2.LIC_ID ,ptm2.EMPLOYEE_CODE, ptcp.CLAIM_TYPE\r\n"
				+ "	,pli.PICK_LIST_ITEM_NAME AS modeofexitname, pli1.PICK_LIST_ITEM_NAME AS claim_status_name \r\n"
				+ "	,ptcp.INTIMATION_NUMBER,ptcp.PAYOUT_NUMBER FROM PMST_TMP_CLAIM_PROPS ptcp \r\n"
				+ "	JOIN pmst_tmp_policy ptp ON ptp.POLICY_ID =ptcp.TMP_POLICY_ID \r\n"
				+ "	JOIN pmst_tmp_mph ptm ON ptm.TMP_POLICY_ID =ptcp.TMP_POLICY_ID \r\n"
				+ "	JOIN pmst_tmp_member ptm2 ON ptm2.TMP_POLICY_ID=ptcp.TMP_POLICY_ID \r\n"
				+ "	JOIN PICK_LIST_ITEM pli ON pli.PICK_LIST_ITEM_ID =ptcp.MODE_OF_EXIT \r\n"
				+ "	JOIN PICK_LIST_ITEM pli1 ON pli1.PICK_LIST_ITEM_ID =ptcp.CLAIM_STATUS_ID WHERE ptcp.ONBOARD_NUMBER=?1 ORDER BY ONBOARD_NUMBER desc",nativeQuery = true)
		List<Object[]> findByOnboardNumber(String onboardNumber);

		@Query(value="SELECT * FROM PMST_TMP_CLAIM_PROPS WHERE ONBOARD_NUMBER=?1",nativeQuery = true)
		TempPolicyClaimPropsEntity findByOnboardNumberisActive(String onboardNumber);

		@Query(value = "SELECT MODULE_SEQ_FUNC(?1) FROM DUAL", nativeQuery = true)
		String getSequence(String onbordingModule);


		@Query(value="SELECT ptp.POLICY_NUMBER,ptp.UNIT_CODE,\r\n"
				+ "ptm.MPH_NAME, \r\n"
				+ "pli.PICK_LIST_ITEM_DESCRIPTION, \r\n"
				+ "NVL(sum((CASE WHEN NVL(MODIFIED_GRATUITY_AMOUNT, 0)=0 THEN NVL(GRATUITY_AMT_ON_DATE_OF_EXIT , 0) ELSE  NVL(MODIFIED_GRATUITY_AMOUNT, 0) END)+ NVL(LC_SUM_ASSURED, 0)+NVL(REFUND_PREMIUM_AMOUNT,0) +\r\n"
				+ "NVL(REFUND_GST_AMOUNT,0) +NVL(PENAL_AMOUNT,0)+NVL(COURT_AWARD,0) ),0)\r\n"
				+ "as AMOUNT_OF_CLAIMS\r\n"
				+ "FROM PMST_TMP_CLAIM_PROPS ptcp\r\n"
				+ "JOIN PMST_TMP_POLICY ptp ON ptcp.TMP_POLICY_ID =ptp.POLICY_ID\r\n"
				+ "JOIN PMST_TMP_MPH ptm ON ptm.TMP_POLICY_ID =ptp.POLICY_ID \r\n"
				+ "JOIN PICK_LIST_ITEM pli ON pli.PICK_LIST_ITEM_ID =ptcp.MODE_OF_EXIT \r\n"
				+ "WHERE\r\n"
				+ "CLAIM_STATUS_ID =221 AND ptp.MASTER_POLICY_ID =?1\r\n"
				+ "GROUP BY ptp.POLICY_NUMBER,ptp.UNIT_CODE,\r\n"
				+ "ptm.MPH_NAME, \r\n"
				+ "pli.PICK_LIST_ITEM_DESCRIPTION",nativeQuery = true)
		List<Object[]> claimreport(Long masterpolicyId);

		@Query(value="SELECT * FROM PMST_TMP_CLAIM_PROPS WHERE TMP_MEMBER_ID =:memberid AND TMP_POLICY_ID  =:policyid",nativeQuery = true)
		TempPolicyClaimPropsEntity getByPolicyAndMemberId(@Param("memberid") Long memberid,@Param("policyid") Long policyid);
	  
		@Query(value="SELECT ptp.POLICY_NUMBER,ptcp.payout_number,ptp.unit_code,PTCP.PAYOUT_DATE,\r\n"
				+ "ptm.mph_name,\r\n"
				+ "ptma.ADDRESS_LINE1 ,ptma.ADDRESS_LINE2 , ptma.ADDRESS_LINE3,ptma.CITY_LOCALITY ,ptma.STATE_NAME AS state_name ,ptma.PINCODE,\r\n"
				+ "ptmb.BANK_NAME ,ptmb.ACCOUNT_NUMBER,ptmb.IFSC_CODE,\r\n"
				+ "ptm2.LIC_ID ,ptm2.EMPLOYEE_CODE ,CONCAT(ptm2.FIRST_NAME,ptm2.LAST_NAME) AS membername, \r\n"
				+ "ptcp.LC_SUM_ASSURED,\r\n"
				+ "(CASE WHEN NVL(ptcp.MODIFIED_GRATUITY_AMOUNT, 0)=0 THEN NVL(ptcp.GRATUITY_AMT_ON_DATE_OF_EXIT , 0) ELSE  NVL(ptcp.MODIFIED_GRATUITY_AMOUNT, 0) END) AS GRATUITY_AMOUNT,\r\n"
				+ "NVL(ptcp.REFUND_PREMIUM_AMOUNT , 0)+NVL(ptcp.REFUND_GST_AMOUNT , 0) AS refundamount \r\n"
				+ "FROM PMST_TMP_CLAIM_PROPS ptcp\r\n"
				+ "JOIN PMST_TMP_POLICY ptp ON ptcp.TMP_POLICY_ID =ptp.POLICY_ID\r\n"
				+ "JOIN PMST_TMP_MPH ptm ON ptm.TMP_POLICY_ID =ptp.POLICY_ID \r\n"
				+ "JOIN PMST_TMP_MPH_ADDRESS ptma ON ptm.MPH_ID =ptma.MPH_ID \r\n"
				+ "JOIN PMST_TMP_MPH_BANK ptmb ON ptmb.MPH_ID =ptm.MPH_ID \r\n"
				+ "JOIN PMST_TMP_MEMBER ptm2  ON ptm2.TMP_POLICY_ID = ptp.POLICY_ID \r\n"
				+ "WHERE\r\n"
				+ "ptcp.PAYOUT_NUMBER =?",nativeQuery = true)
		List<Object[]> claimForward(String payoutNumber);
		
		
		@Query(value="SELECT count(*)\r\n"
				+ "FROM PMST_TMP_POLICY ptp \r\n"
				+ "JOIN PMST_POLICY_SERVICE pps ON pps.POLICY_ID = ptp.MASTER_POLICY_ID AND ptp.POLICY_SERVICE_ID = pps.POLICY_SERVICE_ID  \r\n"
				+ "JOIN PMST_TMP_MEMBER ptm ON ptm.TMP_POLICY_ID  = ptp.POLICY_ID  \r\n"
				+ "WHERE pps.SERVICE_TYPE ='Claim' AND ptp.MASTER_POLICY_ID =?2 AND ptm.PMST_MEMBER_ID =?1",nativeQuery = true)
          int findByMemberIdandPolicyId(Long memberid, Long policyid);

	@Query(value="SELECT count(*)\r\n"
			+ "FROM PMST_TMP_POLICY ptp \r\n"
			+ "JOIN PMST_POLICY_SERVICE pps ON pps.POLICY_ID = ptp.MASTER_POLICY_ID AND ptp.POLICY_SERVICE_ID = pps.POLICY_SERVICE_ID  \r\n"
			+ "JOIN PMST_TMP_MEMBER ptm ON ptm.TMP_POLICY_ID  = ptp.POLICY_ID  \r\n"
			+ "WHERE pps.SERVICE_TYPE ='Transfer' AND ptp.MASTER_POLICY_ID =?2 AND ptm.PMST_MEMBER_ID =?1",nativeQuery = true)
	int findByMemberIdandPolicyIdForTransfer(Long memberid, Long policyid);

		TempPolicyClaimPropsEntity findByinitimationNumber(String intimationNumber);

		
		@Query(value="SELECT TMP_MEMBER_ID FROM PMST_TMP_CLAIM_PROPS ptcp WHERE CLAIM_PROPS_ID=:propsId",nativeQuery = true)
		Long findByClaimId(@Param("propsId") Long propsId);


		@Query(value="SELECT ptcp.CLAIM_PROPS_ID,ptp.POLICY_NUMBER,ptp.UNIT_CODE,ptp.product_variant_id,\r\n"
				+ "ptm.MPH_NAME, \r\n"
				+ "pli.PICK_LIST_ITEM_DESCRIPTION, \r\n"
				+ "NVL((CASE WHEN NVL(MODIFIED_GRATUITY_AMOUNT, 0)=0 THEN NVL(GRATUITY_AMT_ON_DATE_OF_EXIT , 0) ELSE  NVL(MODIFIED_GRATUITY_AMOUNT, 0) END)+ NVL(LC_SUM_ASSURED, 0)+NVL(REFUND_PREMIUM_AMOUNT,0) +\r\n"
				+ "NVL(REFUND_GST_AMOUNT,0) +NVL(PENAL_AMOUNT,0)+NVL(COURT_AWARD,0) ,0)\r\n"
				+ "as AMOUNT_OF_CLAIMS,ptcp.PAYOUT_DATE,ptp.PRODUCT_ID \r\n"
				+ "FROM PMST_TMP_CLAIM_PROPS ptcp\r\n"
				+ "JOIN PMST_TMP_POLICY ptp ON ptcp.TMP_POLICY_ID =ptp.POLICY_ID\r\n"
				+ "JOIN PMST_TMP_MPH ptm ON ptm.TMP_POLICY_ID =ptp.POLICY_ID \r\n"
				+ "JOIN PICK_LIST_ITEM pli ON pli.PICK_LIST_ITEM_ID =ptcp.MODE_OF_EXIT \r\n"
				+ "WHERE\r\n"
				+ "CLAIM_STATUS_ID  in (211,214) AND ptcp.payout_number=?",nativeQuery = true)
		List<Object[]> claimpayoutreport(String payoutNumber);

		
		@Query(value="SELECT DEBIT_CODE ,DEBIT_CODE_DESCRIPTION ,DEBIT_AMOUNT ,CREDIT_CODE ,CREDIT_CODE_DESCRIPTION ,CREDIT_AMOUNT \r\n"
				+ "FROM ACCT_CR_DR_RESPONSE acdr WHERE ACCT_RESPONSE_ID \r\n"
				+ "IN (SELECT ID FROM ACCT_RESPONSE ar WHERE module='CLAIM-INTIMATION' AND REFERENCE_ID =:claimPropsId AND ar.REFERENCE_VALUE='CLAIM-PROPS-ID') order by DEBIT_CODE",nativeQuery = true)
		List<Object[]> claimvoucher(Long claimPropsId);
		
		@Query(value="SELECT PICK_LIST_ITEM_NAME AS modeofexitname FROM PICK_LIST_ITEM pli WHERE  pli.PICK_LIST_ITEM_ID =?1",nativeQuery = true)
		String getModeofExitName(Long modeOfExit);
		
		@Query(value="SELECT PICK_LIST_ITEM_NAME  AS claim_status_name  FROM PICK_LIST_ITEM pli WHERE  pli.PICK_LIST_ITEM_ID =?1",nativeQuery = true)
		String getClaimStatusName(Long claimStatusId);

		@Query(value="Call CLAIM_PAYOUT_APPROVE(?1,?2,?3)",nativeQuery = true)
		void callPayoutApproveProcedure(String payoutNo, String productCode, String variantCode);

		@Query(value="SELECT count(*) FROM PMST_TMP_CLAIM_PROPS ptcp  WHERE CLAIM_STATUS_ID =?1",nativeQuery = true)
		Long getCountClaimSubStatus(Long claimStatusId);
		
}
