package com.lic.epgs.gratuity.policy.surrender.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SURR_CONFIG")
public class SurrConfigEntity {

	@Id
	@Column(name = "SURR_CONFIG_ID") 
	private Long surrConfigId;
	
	@Column(name = "VARIANT_VERSION") 
	private String variantVersion;
	
	@Column(name = "NOTICE_PERIOD_IN_MONTHS") 
	private Long noticePeriodInMonths;
	
	@Column(name = "WAITING_PERIOD_IN_MONTHS") 
	private Long waitingPeriodInMonths;
	
	@Column(name = "IS_SURRENDER_CHARGES_APPLICABLE") 
	private String isSurrenderChargesApplicable;
	
	@Column(name = "SURRENDER_CHARGES") 
	private Double surrenderCharges;
	
	@Column(name = "IS_EXIT_LOAD_APPLICABLE") 
	private String isExitLoadApplicable;
	
	@Column(name = "IS_MVA_APPLICABLE") 
	private String isMVAApplicable;
	
	@Column(name = "MVA_CHARGES") 
	private Double mvaCharges;
	
	@Column(name = "MAX_SURRENDER_CHARGES") 
	private Double maxSurrenderCharges;

	public Double getMaxSurrenderCharges() {
		return maxSurrenderCharges;
	}

	public void setMaxSurrenderCharges(Double maxSurrenderCharges) {
		this.maxSurrenderCharges = maxSurrenderCharges;
	}

	public Long getSurrConfigId() {
		return surrConfigId;
	}

	public void setSurrConfigId(Long surrConfigId) {
		this.surrConfigId = surrConfigId;
	}

	public String getVariantVersion() {
		return variantVersion;
	}

	public void setVariantVersion(String variantVersion) {
		this.variantVersion = variantVersion;
	}

	public Long getNoticePeriodInMonths() {
		return noticePeriodInMonths;
	}

	public void setNoticePeriodInMonths(Long noticePeriodInMonths) {
		this.noticePeriodInMonths = noticePeriodInMonths;
	}

	public Long getWaitingPeriodInMonths() {
		return waitingPeriodInMonths;
	}

	public void setWaitingPeriodInMonths(Long waitingPeriodInMonths) {
		this.waitingPeriodInMonths = waitingPeriodInMonths;
	}

	public String getIsSurrenderChargesApplicable() {
		return isSurrenderChargesApplicable;
	}

	public void setIsSurrenderChargesApplicable(String isSurrenderChargesApplicable) {
		this.isSurrenderChargesApplicable = isSurrenderChargesApplicable;
	}

	public Double getSurrenderCharges() {
		return surrenderCharges;
	}

	public void setSurrenderCharges(Double surrenderCharges) {
		this.surrenderCharges = surrenderCharges;
	}

	public String getIsExitLoadApplicable() {
		return isExitLoadApplicable;
	}

	public void setIsExitLoadApplicable(String isExitLoadApplicable) {
		this.isExitLoadApplicable = isExitLoadApplicable;
	}

	public String getIsMVAApplicable() {
		return isMVAApplicable;
	}

	public void setIsMVAApplicable(String isMVAApplicable) {
		this.isMVAApplicable = isMVAApplicable;
	}

	public Double getMvaCharges() {
		return mvaCharges;
	}

	public void setMvaCharges(Double mvaCharges) {
		this.mvaCharges = mvaCharges;
	}
	

}


