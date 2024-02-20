package com.lic.epgs.gratuity.policy.surrender.dto;

import java.math.BigDecimal;

public class SaveSurrenderPayoutDtlsReq {

	private Long surrenderId;
	private Long totalMembers;
	private BigDecimal totalAccumulatedFund;
	private BigDecimal totalInterestAmt;
	private BigDecimal surrenderCharges;
	private BigDecimal totalPayableAmt;
	private String modeOfPayment;
	private String paymentFrequency;
	private int noOfInstallments;
	private BigDecimal exitLoad;
	private String isMVAApplicable;
	private BigDecimal mvaCharges;
	private BigDecimal gstOnMVACharges;
	private BigDecimal gstOnSurrenderCharges;
	private BigDecimal gstOnExitLoad;
	private BigDecimal exitAmount;
	private Long percentageWithdrawn;
	private String isBulkExit;
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
	public BigDecimal getGstOnMVACharges() {
		return gstOnMVACharges;
	}
	public void setGstOnMVACharges(BigDecimal gstOnMVACharges) {
		this.gstOnMVACharges = gstOnMVACharges;
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
	public BigDecimal getExitLoad() {
		return exitLoad;
	}
	public void setExitLoad(BigDecimal exitLoad) {
		this.exitLoad = exitLoad;
	}
	public String getIsMVAApplicable() {
		return isMVAApplicable;
	}
	public void setIsMVAApplicable(String isMVAApplicable) {
		this.isMVAApplicable = isMVAApplicable;
	}
	public BigDecimal getMvaCharges() {
		return mvaCharges;
	}
	public void setMvaCharges(BigDecimal mvaCharges) {
		this.mvaCharges = mvaCharges;
	}
	
	public Long getSurrenderId() {
		return surrenderId;
	}
	public void setSurrenderId(Long surrenderId) {
		this.surrenderId = surrenderId;
	}
	
	public Long getTotalMembers() {
		return totalMembers;
	}
	public void setTotalMembers(Long totalMembers) {
		this.totalMembers = totalMembers;
	}
	
	public BigDecimal getTotalAccumulatedFund() {
		return totalAccumulatedFund;
	}
	public void setTotalAccumulatedFund(BigDecimal totalAccumulatedFund) {
		this.totalAccumulatedFund = totalAccumulatedFund;
	}
	
	public BigDecimal getTotalInterestAmt() {
		return totalInterestAmt;
	}
	public void setTotalInterestAmt(BigDecimal totalInterestAmt) {
		this.totalInterestAmt = totalInterestAmt;
	}
	
	public BigDecimal getSurrenderCharges() {
		return surrenderCharges;
	}
	public void setSurrenderCharges(BigDecimal surrenderCharges) {
		this.surrenderCharges = surrenderCharges;
	}
	
	public BigDecimal getTotalPayableAmt() {
		return totalPayableAmt;
	}
	public void setTotalPayableAmt(BigDecimal totalPayableAmt) {
		this.totalPayableAmt = totalPayableAmt;
	}
	
	public String getModeOfPayment() {
		return modeOfPayment;
	}
	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
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
	
	
}
