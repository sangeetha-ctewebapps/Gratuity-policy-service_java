package com.lic.epgs.gratuity.policy.surrender.repository;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.surrender.entity.PolicySurrender;

public interface SurrenderRepository extends JpaRepository<PolicySurrender, Long>{

	@Query(value="SELECT SURRENDER_NUMBER FROM POLICY_SURRENDER WHERE SURR_ID=:surrenderId",nativeQuery=true)
	public Long getSurrenderNumber(Long surrenderId);
	
	@Query(value="select SURRENDER_NUMBER_SEQ.nextval from dual",nativeQuery=true)
	public Long getSurrenderNumberSequence();
	
	@Transactional
	@Modifying(clearAutomatically=true,flushAutomatically = true)
	@Query(value="UPDATE POLICY_SURRENDER SET SURRENDER_REASON=:surrenderReason, REQUESTED_BY=:surrenderRequestedBy, "
			+ "REQUESTED_DATE=:reqDateAsTimestamp, SURRENDER_EFFECTIVE_DATE=:effDateAsTimestamp, MODIFIED_BY=:modifiedBy, "
			+ "MODIFIED_ON=:modifiedOn, USER_ID=:userId, UNIT_CODE=:unitCode "
			+ "WHERE SURR_ID=:surrenderId",nativeQuery=true)
	public int updateSurrenderDetails(Long surrenderId, String surrenderReason, String surrenderRequestedBy,
	 Date reqDateAsTimestamp, Date effDateAsTimestamp, String modifiedBy, Date modifiedOn, Long userId, String unitCode);
	
	@Query(value="SELECT * FROM POLICY_SURRENDER WHERE SURR_ID=:surrenderId",nativeQuery=true)
	public PolicySurrender getSurrenderDetailsUsingId(Long surrenderId);
	
	@Query(value="SELECT SURR_STS_ID FROM POLICY_SURRENDER_STS WHERE STATUS=:action",nativeQuery=true)
	public Long getSurrenderStatusId(String action);
	
	@Transactional
	@Modifying(clearAutomatically=true,flushAutomatically = true)
	@Query(value="UPDATE POLICY_SURRENDER SET STATUS=:action, SURR_STS_SURR_STS_ID=:surrenderStatusId, MODIFIED_BY=:modifiedBy, USER_ID=:userId\r\n" + 
			"WHERE SURRENDER_NUMBER=:surrenderNumber",nativeQuery=true)
	public int surrenderWorkflowAction(String action, Long surrenderStatusId, String modifiedBy, Long userId, Long surrenderNumber);
	
	@Transactional
	@Modifying(clearAutomatically=true,flushAutomatically = true)
	@Query(value="UPDATE POLICY_SURRENDER SET TOTAL_MEMBERS=:totalMembers, ACCUMULATED_VALUE=:totalAccumulatedFund, "
			+ "APPLICABLE_INTEREST=:totalInterestAmt, SURRENDER_CHARGES=:surrenderCharges, GST_ON_EXITLOAD=:gstOnExitLoad, TOTAL_PAYABLE_AMOUNT=:totalPayableAmt, "
			+ "MODE_OF_PAYMENT=:modeOfPayment, PAYMENT_FREQUENCY=:paymentFrequency, NO_OF_INSTALLMENTS=:noOfInstallments, EXIT_LOAD=:exitLoad, MVA_CHARGES=:mvaCharges ,IS_MVA_APPLICABLE=:isMVAApplicable,GST_ON_SURRENDER_CHARGES=:gstOnSurrenderCharges,GST_ON_MVA_CHARGES=:gstOnMVACharges,EXIT_AMOUNT=:exitAmount,PERCENTAGE_WITHDRAWN=:percentageWithdrawn,IS_BULK_EXIT=:isBulkExit,MVF=:mvf\r\n"
			+ "WHERE SURR_ID=:surrenderId",nativeQuery=true)
	public int saveSurrenderPaymentDetails(Long surrenderId, Long totalMembers, BigDecimal totalAccumulatedFund,
	 BigDecimal totalInterestAmt, BigDecimal surrenderCharges, BigDecimal gstOnExitLoad, BigDecimal totalPayableAmt, String modeOfPayment, 
	 String paymentFrequency, int noOfInstallments, BigDecimal exitLoad, BigDecimal mvaCharges, String isMVAApplicable, BigDecimal gstOnSurrenderCharges, BigDecimal gstOnMVACharges, BigDecimal exitAmount, Long percentageWithdrawn, String isBulkExit, BigDecimal mvf);
	
	@Transactional
	@Modifying(clearAutomatically=true,flushAutomatically = true)
	@Query(value="UPDATE POLICY_SURRENDER SET SURRENDER_PARTIAL_AMT=:partialSurrenderAmount, "
			+ "PARTIAL_SURRENDER_EFFECTIVE_DATE=:effDateAsTimestamp, MODIFIED_BY=:modifiedBy, "
			+ "MODIFIED_ON=:modifiedOn, UNIT_CODE=:unitCode "
			+ "WHERE SURR_ID=:surrenderId",nativeQuery=true)
	public int updatePartialSurrenderDetails(Long surrenderId, Long partialSurrenderAmount, Date effDateAsTimestamp, String modifiedBy, Date modifiedOn, String unitCode);
	
	@Query(value="SELECT * FROM POLICY_SURRENDER WHERE SURRENDER_NUMBER=:surrenderNumber",nativeQuery=true)
	public PolicySurrender getSurrenderDetailsUsingSurrenderNumber(Long surrenderNumber);
}
