package com.lic.epgs.gratuity.policy.surrender.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "POLICY_SURRENDER")
public class PolicySurrender {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SURRENDER_ID_SEQ")
	@SequenceGenerator(name = "SURRENDER_ID_SEQ", sequenceName = "SURRENDER_ID_SEQ", allocationSize = 1)
	@Column(name = "SURR_ID") 
	private Long surrenderId;
	
	@Column(name = "SURRENDER_VALUE") 
	private BigDecimal surrenderValue;
	
	@Column(name = "SURRENDER_REASON") 
	private String surrenderReason;
	
	@Column(name = "REQUESTED_BY") 
	private String requestedBy;

	@Column(name = "SURRENDER_VALUE_PAYABLE_TO") 
	private String surrenderValuePayableTo;
	
	@Column(name = "SURRENDER_CHARGES") 
	private BigDecimal surrenderCharges;
	
	@Column(name = "ACCUMULATED_VALUE")
	private BigDecimal accumulatedValue;
	
	@Column(name = "APPLICABLE_INTEREST")
	private BigDecimal applicableInterest;
	
	@Column(name = "TOTAL_PAYABLE_AMOUNT")
	private BigDecimal totalPayableAmount;
	
	@Column(name = "STATUS") 
	private String surrenderStatus;
	
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SURRENDER_NUMBER_SEQ")
	@SequenceGenerator(name = "SURRENDER_NUMBER_SEQ", sequenceName = "SURRENDER_NUMBER_SEQ", allocationSize = 1)
	@Column(name = "SURRENDER_NUMBER") 
	private Long surrenderNumber;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "SURRENDER_EFFECTIVE_DATE")
	private Date surrenderEffectiveDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "REQUESTED_DATE")
	private Date surrenderRequestedDate;
	
	@Column(name = "SURR_STS_SURR_STS_ID") 
	private int surrenderStatusId;
	
	@Column(name = "MODIFIED_BY") 
	private String modifiedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFIED_ON") 
	private Date modifiedOn;

	@Column(name = "M_POLICY_ID") 
	private Long policyId;

	@Column(name = "PAYMENT_FREQUENCY") 
	private String paymentFrequency;
	
	@Column(name = "NO_OF_INSTALLMENTS") 
	private int noOfInstallments;
	
	@Column(name = "MODE_OF_PAYMENT") 
	private String modeOfPayment;
	
	@Column(name = "TOTAL_MEMBERS") 
	private Long totalMembers;
	
	@Column(name = "GST") 
	private BigDecimal gst;
	
	@Column(name = "USER_ID") 
	private Long userId;
	
	@Column(name = "UNIT_CODE") 
	private String unitCode;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "NOTICE_PERIOD_START_DATE") 
	private Date noticePeriodStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "NOTICE_PERIOD_END_DATE") 
	private Date noticePeriodEndDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "WAITING_PERIOD_START_DATE") 
	private Date waitingPeriodStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "WAITING_PERIOD_END_DATE") 
	private Date waitingPeriodEndDate;

	@Column(name = "WAIVE_WAITING_PERIOD") 
	private String waiveWaitingPeriod;
	
	@Column(name = "EXIT_LOAD")
	private BigDecimal exitLoad;
	
	@Column(name = "MVA_CHARGES")
	private BigDecimal mvaCharges;
	
	@Column(name = "IS_MVA_APPLICABLE") 
	private String isMVAApplicable;
	
	@Column(name = "SURRENDER_TYPE")
	private String surrenderType;
	
	@Column(name = "SURRENDER_PARTIAL_AMT")
	private Long surrenderPartialAmount;
	
	@Column(name = "PARTIAL_SURRENDER_EFFECTIVE_DATE")
	private Date partialSurrenderEffectiveDate;
	
	@Column(name = "GST_ON_SURRENDER_CHARGES")
	private BigDecimal gstOnSurrenderCharges;
	
	@Column(name = "GST_ON_EXITLOAD")
	private BigDecimal gstOnExitLoad;
	
	@Column(name = "EXIT_AMOUNT")
	private BigDecimal exitAmount;
	
	@Column(name = "PERCENTAGE_WITHDRAWN")
	private Long percentageWithdrawn;
	
	@Column(name = "IS_BULK_EXIT")
	private String isBulkExit;
	
	@Column(name = "MVF")
	private BigDecimal mvf;
	
	public BigDecimal getExitAmount() {
		return exitAmount;
	}

	public void setExitAmount(BigDecimal exitAmount) {
		this.exitAmount = exitAmount;
	}

	public Long getPercentageWithdrawn() {
		return percentageWithdrawn;
	}

	public void setPercentageWithdrawn(Long percentageWithdrawn) {
		this.percentageWithdrawn = percentageWithdrawn;
	}

	public String getIsBulkExit() {
		return isBulkExit;
	}

	public void setIsBulkExit(String isBulkExit) {
		this.isBulkExit = isBulkExit;
	}

	public BigDecimal getMvf() {
		return mvf;
	}

	public void setMvf(BigDecimal mvf) {
		this.mvf = mvf;
	}

	public BigDecimal getGstOnSurrenderCharges() {
		return gstOnSurrenderCharges;
	}

	public void setGstOnSurrenderCharges(BigDecimal gstOnSurrenderCharges) {
		this.gstOnSurrenderCharges = gstOnSurrenderCharges;
	}

	public BigDecimal getGstOnExitLoad() {
		return gstOnExitLoad;
	}

	public void setGstOnExitLoad(BigDecimal gstOnExitLoad) {
		this.gstOnExitLoad = gstOnExitLoad;
	}

	public BigDecimal getGstOnMVACharges() {
		return gstOnMVACharges;
	}

	public void setGstOnMVACharges(BigDecimal gstOnMVACharges) {
		this.gstOnMVACharges = gstOnMVACharges;
	}

	@Column(name = "GST_ON_MVA_CHARGES")
	private BigDecimal gstOnMVACharges;
	
	
	public String getSurrenderType() {
		return surrenderType;
	}

	public void setSurrenderType(String surrenderType) {
		this.surrenderType = surrenderType;
	}

	public Long getSurrenderPartialAmount() {
		return surrenderPartialAmount;
	}

	public void setSurrenderPartialAmount(Long surrenderPartialAmount) {
		this.surrenderPartialAmount = surrenderPartialAmount;
	}

	public Date getPartialSurrenderEffectiveDate() {
		return partialSurrenderEffectiveDate;
	}

	public void setPartialSurrenderEffectiveDate(Date partialSurrenderEffectiveDate) {
		this.partialSurrenderEffectiveDate = partialSurrenderEffectiveDate;
	}

	public BigDecimal getExitLoad() {
		return exitLoad;
	}

	public void setExitLoad(BigDecimal exitLoad) {
		this.exitLoad = exitLoad;
	}

	public BigDecimal getMvaCharges() {
		return mvaCharges;
	}

	public void setMvaCharges(BigDecimal mvaCharges) {
		this.mvaCharges = mvaCharges;
	}

	public String getIsMVAApplicable() {
		return isMVAApplicable;
	}

	public void setIsMVAApplicable(String isMVAApplicable) {
		this.isMVAApplicable = isMVAApplicable;
	}

	public String getWaiveWaitingPeriod() {
		return waiveWaitingPeriod;
	}

	public void setWaiveWaitingPeriod(String waiveWaitingPeriod) {
		this.waiveWaitingPeriod = waiveWaitingPeriod;
	}

	public Date getNoticePeriodStartDate() {
		return noticePeriodStartDate;
	}

	public void setNoticePeriodStartDate(Date noticePeriodStartDate) {
		this.noticePeriodStartDate = noticePeriodStartDate;
	}

	public Date getNoticePeriodEndDate() {
		return noticePeriodEndDate;
	}

	public void setNoticePeriodEndDate(Date noticePeriodEndDate) {
		this.noticePeriodEndDate = noticePeriodEndDate;
	}

	public Date getWaitingPeriodStartDate() {
		return waitingPeriodStartDate;
	}

	public void setWaitingPeriodStartDate(Date waitingPeriodStartDate) {
		this.waitingPeriodStartDate = waitingPeriodStartDate;
	}

	public Date getWaitingPeriodEndDate() {
		return waitingPeriodEndDate;
	}

	public void setWaitingPeriodEndDate(Date waitingPeriodEndDate) {
		this.waitingPeriodEndDate = waitingPeriodEndDate;
	}

	public Long getSurrenderId() {
		return surrenderId;
	}

	public void setSurrenderId(Long surrenderId) {
		this.surrenderId = surrenderId;
	}

	public BigDecimal getSurrenderValue() {
		return surrenderValue;
	}

	public void setSurrenderValue(BigDecimal surrenderValue) {
		this.surrenderValue = surrenderValue;
	}

	public String getSurrenderReason() {
		return surrenderReason;
	}

	public void setSurrenderReason(String surrenderReason) {
		this.surrenderReason = surrenderReason;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getSurrenderValuePayableTo() {
		return surrenderValuePayableTo;
	}

	public void setSurrenderValuePayableTo(String surrenderValuePayableTo) {
		this.surrenderValuePayableTo = surrenderValuePayableTo;
	}

	public BigDecimal getSurrenderCharges() {
		return surrenderCharges;
	}

	public void setSurrenderCharges(BigDecimal surrenderCharges) {
		this.surrenderCharges = surrenderCharges;
	}

	public BigDecimal getAccumulatedValue() {
		return accumulatedValue;
	}

	public void setAccumulatedValue(BigDecimal accumulatedValue) {
		this.accumulatedValue = accumulatedValue;
	}

	public BigDecimal getApplicableInterest() {
		return applicableInterest;
	}

	public void setApplicableInterest(BigDecimal applicableInterest) {
		this.applicableInterest = applicableInterest;
	}

	public BigDecimal getTotalPayableAmount() {
		return totalPayableAmount;
	}

	public void setTotalPayableAmount(BigDecimal totalPayableAmount) {
		this.totalPayableAmount = totalPayableAmount;
	}

	public String getSurrenderStatus() {
		return surrenderStatus;
	}

	public void setSurrenderStatus(String surrenderStatus) {
		this.surrenderStatus = surrenderStatus;
	}

	public Long getSurrenderNumber() {
		return surrenderNumber;
	}

	public void setSurrenderNumber(Long surrenderNumber) {
		this.surrenderNumber = surrenderNumber;
	}

	public Date getSurrenderEffectiveDate() {
		return surrenderEffectiveDate;
	}

	public void setSurrenderEffectiveDate(Date surrenderEffectiveDate) {
		this.surrenderEffectiveDate = surrenderEffectiveDate;
	}

	public Date getSurrenderRequestedDate() {
		return surrenderRequestedDate;
	}

	public void setSurrenderRequestedDate(Date surrenderRequestedDate) {
		this.surrenderRequestedDate = surrenderRequestedDate;
	}

	public int getSurrenderStatusId() {
		return surrenderStatusId;
	}

	public void setSurrenderStatusId(int surrenderStatusId) {
		this.surrenderStatusId = surrenderStatusId;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}

	public String getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(String paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	public int getNoOfInstallments() {
		return noOfInstallments;
	}

	public void setNoOfInstallments(int noOfInstallments) {
		this.noOfInstallments = noOfInstallments;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public Long getTotalMembers() {
		return totalMembers;
	}

	public void setTotalMembers(Long totalMembers) {
		this.totalMembers = totalMembers;
	}

	public BigDecimal getGst() {
		return gst;
	}

	public void setGst(BigDecimal gst) {
		this.gst = gst;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
}
