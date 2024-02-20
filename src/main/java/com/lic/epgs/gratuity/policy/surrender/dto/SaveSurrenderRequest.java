package com.lic.epgs.gratuity.policy.surrender.dto;

import java.math.BigDecimal;

public class SaveSurrenderRequest {
	
	private Long surrenderId;
	private Long policyId;
	private String surrenderReason;
	private String surrenderRequestedBy;
	private String surrenderRequestedDate;
	private String surrenderPayableTo;
	private String surrenderEffectiveDate;
	private String modifiedBy;
	private String modifiedOn;
	private Long userId;
	private String unitCode;
	private String policyNumber;
	private String noticePeriodStartDate;
	private String noticePeriodEndDate;
	private String waitingPeriodStartDate;
	private String waitingPeriodEndDate;
	private String waiveWaitingPeriod;
	private String surrenderType;
	private Long partialSurrenderAmount;
	private String partialSurrenderEffectiveDate; 
	
	public String getPartialSurrenderEffectiveDate() {
		return partialSurrenderEffectiveDate;
	}
	public void setPartialSurrenderEffectiveDate(String partialSurrenderEffectiveDate) {
		this.partialSurrenderEffectiveDate = partialSurrenderEffectiveDate;
	}
	public String getSurrenderType() {
		return surrenderType;
	}
	public void setSurrenderType(String surrenderType) {
		this.surrenderType = surrenderType;
	}
	public Long getPartialSurrenderAmount() {
		return partialSurrenderAmount;
	}
	public void setPartialSurrenderAmount(Long partialSurrenderAmount) {
		this.partialSurrenderAmount = partialSurrenderAmount;
	}
	public String getWaiveWaitingPeriod() {
		return waiveWaitingPeriod;
	}
	public void setWaiveWaitingPeriod(String waiveWaitingPeriod) {
		this.waiveWaitingPeriod = waiveWaitingPeriod;
	}
	public String getNoticePeriodStartDate() {
		return noticePeriodStartDate;
	}
	public void setNoticePeriodStartDate(String noticePeriodStartDate) {
		this.noticePeriodStartDate = noticePeriodStartDate;
	}
	public String getNoticePeriodEndDate() {
		return noticePeriodEndDate;
	}
	public void setNoticePeriodEndDate(String noticePeriodEndDate) {
		this.noticePeriodEndDate = noticePeriodEndDate;
	}
	public String getWaitingPeriodStartDate() {
		return waitingPeriodStartDate;
	}
	public void setWaitingPeriodStartDate(String waitingPeriodStartDate) {
		this.waitingPeriodStartDate = waitingPeriodStartDate;
	}
	public String getWaitingPeriodEndDate() {
		return waitingPeriodEndDate;
	}
	public void setWaitingPeriodEndDate(String waitingPeriodEndDate) {
		this.waitingPeriodEndDate = waitingPeriodEndDate;
	}
	public Long getSurrenderId() {
		return surrenderId;
	}
	public void setSurrenderId(Long surrenderId) {
		this.surrenderId = surrenderId;
	}
	
	public Long getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}
	
	public String getSurrenderReason() {
		return surrenderReason;
	}
	public void setSurrenderReason(String surrenderReason) {
		this.surrenderReason = surrenderReason;
	}
	
	public String getSurrenderRequestedBy() {
		return surrenderRequestedBy;
	}
	public void setSurrenderRequestedBy(String surrenderRequestedBy) {
		this.surrenderRequestedBy = surrenderRequestedBy;
	}
	
	public String getSurrenderRequestedDate() {
		return surrenderRequestedDate;
	}
	public void setSurrenderRequestedDate(String surrenderRequestedDate) {
		this.surrenderRequestedDate = surrenderRequestedDate;
	}
	
	public String getSurrenderPayableTo() {
		return surrenderPayableTo;
	}
	public void setSurrenderPayableTo(String surrenderPayableTo) {
		this.surrenderPayableTo = surrenderPayableTo;
	}
	
	public String getSurrenderEffectiveDate() {
		return surrenderEffectiveDate;
	}
	public void setSurrenderEffectiveDate(String surrenderEffectiveDate) {
		this.surrenderEffectiveDate = surrenderEffectiveDate;
	}
	
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
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
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

}
