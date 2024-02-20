package com.lic.epgs.gratuity.policy.surrender.dto;

public class SurrenderWorkflowRequest {
	
	private Long surrenderNumber;
	private String action;
	private Long userId;
	private String modifiedBy;
	private String policyNumber;
	private String unitCode;
	private String locationType;
	private String variantVersion;
	
	public String getVariantVersion() {
		return variantVersion;
	}
	public void setVariantVersion(String variantVersion) {
		this.variantVersion = variantVersion;
	}
	public Long getSurrenderNumber() {
		return surrenderNumber;
	}
	public void setSurrenderNumber(Long surrenderNumber) {
		this.surrenderNumber = surrenderNumber;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

}
