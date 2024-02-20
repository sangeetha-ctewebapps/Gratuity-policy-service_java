package com.lic.epgs.gratuity.policy.surrender.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SURR_EXIT_LOAD")
public class SurrenderExitLoadEntity {

	@Id
	@Column(name = "SURR_EXIT_LOAD_ID") 
	private Long surrenderExitLoadId;
	
	@Column(name = "VARIANT_VERSION") 
	private String variantVersion;
	
	@Column(name = "POLICY_DURATION") 
	private String policyDuration;
	
	@Column(name = "SURR_RANGE") 
	private String range;
	
	@Column(name = "PERCENTAGE_APPLIED") 
	private Double percentageApplied;
	
	@Column(name = "EXITLOAD_PERCENTAGE") 
	private Double exitLoadPercentage;

	public Long getSurrenderExitLoadId() {
		return surrenderExitLoadId;
	}

	public void setSurrenderExitLoadId(Long surrenderExitLoadId) {
		this.surrenderExitLoadId = surrenderExitLoadId;
	}

	public String getVariantVersion() {
		return variantVersion;
	}

	public void setVariantVersion(String variantVersion) {
		this.variantVersion = variantVersion;
	}

	public String getPolicyDuration() {
		return policyDuration;
	}

	public void setPolicyDuration(String policyDuration) {
		this.policyDuration = policyDuration;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public Double getPercentageApplied() {
		return percentageApplied;
	}

	public void setPercentageApplied(Double percentageApplied) {
		this.percentageApplied = percentageApplied;
	}

	public Double getExitLoadPercentage() {
		return exitLoadPercentage;
	}

	public void setExitLoadPercentage(Double exitLoadPercentage) {
		this.exitLoadPercentage = exitLoadPercentage;
	}

	
}
