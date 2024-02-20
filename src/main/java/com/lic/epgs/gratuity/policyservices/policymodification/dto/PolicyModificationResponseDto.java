package com.lic.epgs.gratuity.policyservices.policymodification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policyservices.common.entity.TempPolicyServiceNotes;

import lombok.ToString;

@ToString
public class PolicyModificationResponseDto {

	public PolicyModificationResponseDto() {
	}

	public PolicyModificationResponseDto(MasterPolicyEntity masterPolicyEntity, MPHEntity mphEntity,
			MPHAddressEntity mphAddressEntity, MPHBankEntity mphBankEntity) {
		this.policyDetails = masterPolicyEntity;
		this.mphMaster = mphEntity;
		this.mphAddress = mphAddressEntity;
		this.mphBank = mphBankEntity;
	}

	private MasterPolicyEntity policyDetails;
	@JsonIgnoreProperties({ "mphAddresses", "mphBank", "mphRepresentatives" })
	private MPHEntity mphMaster;
	@JsonIgnoreProperties({ "masterMph" })
	private MPHAddressEntity mphAddress;
	@JsonIgnoreProperties({ "masterMph" })
	private MPHBankEntity mphBank;

	private TempPolicyServiceNotes notes;

	private String policyStatus;

	private Long existingpolicyId;
	
	private String serviceNumber;

	public MasterPolicyEntity getPolicyDetails() {
		return policyDetails;
	}

	public void setPolicyDetails(MasterPolicyEntity policyDetails) {
		this.policyDetails = policyDetails;
	}

	public MPHEntity getMphMaster() {
		return mphMaster;
	}

	public void setMphMaster(MPHEntity mphMaster) {
		this.mphMaster = mphMaster;
	}

	public MPHAddressEntity getMphAddress() {
		return mphAddress;
	}

	public void setMphAddress(MPHAddressEntity mphAddress) {
		this.mphAddress = mphAddress;
	}

	public MPHBankEntity getMphBank() {
		return mphBank;
	}

	public void setMphBank(MPHBankEntity mphBank) {
		this.mphBank = mphBank;
	}

	public TempPolicyServiceNotes getNotes() {
		return notes;
	}

	public void setNotes(TempPolicyServiceNotes notes) {
		this.notes = notes;
	}

	public String getPolicyStatus() {
		return StatusMaster.getStatusTranc(policyDetails.getPolicyStatusId());
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public Long getExistingpolicyId() {
		return existingpolicyId;
	}

	public void setExistingpolicyId(Long existingpolicyId) {
		this.existingpolicyId = existingpolicyId;
	}

	public String getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	

}
