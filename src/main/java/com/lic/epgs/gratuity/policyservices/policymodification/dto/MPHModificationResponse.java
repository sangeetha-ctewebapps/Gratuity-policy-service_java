package com.lic.epgs.gratuity.policyservices.policymodification.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;

import lombok.ToString;

@ToString
public class MPHModificationResponse {

	public MPHModificationResponse() {
	}

	public MPHModificationResponse(MasterPolicyEntity policyDetails, MPHEntity mphMaster) {

		this.policyDetails = policyDetails;
		this.mphMaster = mphMaster;

	}

	private MasterPolicyEntity policyDetails;
	
	@JsonIgnoreProperties({ "mphAddresses", "mphBank", "mphRepresentatives" })
	private MPHEntity mphMaster;
	
	@JsonIgnoreProperties({ "masterMph" })
	private List<MPHAddressEntity> mphAddress;
	
	@JsonIgnoreProperties({ "masterMph" })
	private List<MPHBankEntity> mphBank;

//	private TempPolicyServiceNotes notes;

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

//	public TempPolicyServiceNotes getNotes() {
//		return notes;
//	}
//
//	public void setNotes(TempPolicyServiceNotes notes) {
//		this.notes = notes;
//	}

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

	public List<MPHAddressEntity> getMphAddress() {
		return mphAddress;
	}

	public void setMphAddress(List<MPHAddressEntity> mphAddress) {
		this.mphAddress = mphAddress;
	}

	public List<MPHBankEntity> getMphBank() {
		return mphBank;
	}

	public void setMphBank(List<MPHBankEntity> mphBank) {
		this.mphBank = mphBank;
	}

	
}
