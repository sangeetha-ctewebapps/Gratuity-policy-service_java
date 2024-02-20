package com.lic.epgs.gratuity.policyservices.policymodification.util;

import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHBankEntity;

@Service
public class Mapper {

	public MPHBankEntity convertTempMPHBankEntityToMPHBankEntity(TempMPHBankEntity tempMPHBankEntity) {
		
		MPHBankEntity bank = new MPHBankEntity();
		bank.setId(tempMPHBankEntity.getId());
		bank.setBankName(tempMPHBankEntity.getBankName());
		bank.setAccountNumber(tempMPHBankEntity.getAccountNumber());
		bank.setAccountType(tempMPHBankEntity.getAccountType());
		bank.setBankAddress(tempMPHBankEntity.getBankAddress());
		bank.setCityId(tempMPHBankEntity.getCityId());
		bank.setTownLocality(tempMPHBankEntity.getTownLocality());
		bank.setStateId(tempMPHBankEntity.getStateId());
		bank.setCountryId(tempMPHBankEntity.getCountryId());
		bank.setDistrictId(tempMPHBankEntity.getDistrictId());
		bank.setBankBranch(tempMPHBankEntity.getBankBranch());
		bank.setIfscCode(tempMPHBankEntity.getIfscCode());
		bank.setCountryCode(tempMPHBankEntity.getCountryCode());
		bank.setEmailId(tempMPHBankEntity.getEmailId());
		bank.setStdCode(tempMPHBankEntity.getStdCode());
		bank.setLandlineNumber(tempMPHBankEntity.getLandlineNumber());
		bank.setIsActive(tempMPHBankEntity.getIsActive());
		bank.setCreatedBy(tempMPHBankEntity.getCreatedBy());
		bank.setCreatedDate(tempMPHBankEntity.getCreatedDate());
		bank.setModifiedBy(tempMPHBankEntity.getModifiedBy());
		bank.setModifiedDate(tempMPHBankEntity.getModifiedDate());
		return bank;
		
	}
	
	
	
public TempMPHBankEntity convertMPHBankEntityToTempMPHBankEntity(MPHBankEntity mPHBankEntity) {
		
	TempMPHBankEntity bank = new TempMPHBankEntity();
		bank.setId(mPHBankEntity.getId());
		bank.setBankName(mPHBankEntity.getBankName());
		bank.setAccountNumber(mPHBankEntity.getAccountNumber());
		bank.setAccountType(mPHBankEntity.getAccountType());
		bank.setBankAddress(mPHBankEntity.getBankAddress());
		bank.setCityId(mPHBankEntity.getCityId());
		bank.setTownLocality(mPHBankEntity.getTownLocality());
		bank.setStateId(mPHBankEntity.getStateId());
		bank.setCountryId(mPHBankEntity.getCountryId());
		bank.setDistrictId(mPHBankEntity.getDistrictId());
		bank.setBankBranch(mPHBankEntity.getBankBranch());
		bank.setIfscCode(mPHBankEntity.getIfscCode());
		bank.setCountryCode(mPHBankEntity.getCountryCode());
		bank.setEmailId(mPHBankEntity.getEmailId());
		bank.setStdCode(mPHBankEntity.getStdCode());
		bank.setLandlineNumber(mPHBankEntity.getLandlineNumber());
		bank.setIsActive(mPHBankEntity.getIsActive());
		bank.setCreatedBy(mPHBankEntity.getCreatedBy());
		bank.setCreatedDate(mPHBankEntity.getCreatedDate());
		bank.setModifiedBy(mPHBankEntity.getModifiedBy());
		bank.setModifiedDate(mPHBankEntity.getModifiedDate());
		return bank;
		
	}







public  MPHAddressEntity  convertTempMPHAddressEntityToMPHAddressEntity(TempMPHAddressEntity tempMPHAddressEntity) {
	MPHAddressEntity address = new MPHAddressEntity();
	address.setId(tempMPHAddressEntity.getId());
	address.setAddressLine1(tempMPHAddressEntity.getAddressLine1());
	address.setAddressLine2(tempMPHAddressEntity.getAddressLine2());
	address.setAddressLine3(tempMPHAddressEntity.getAddressLine3());
	address.setAddressType(tempMPHAddressEntity.getAddressType());
	address.setCityId(tempMPHAddressEntity.getCityId());
	address.setCityLocality(tempMPHAddressEntity.getCityLocality());
	address.setStateId(tempMPHAddressEntity.getStateId());
	address.setStateName(tempMPHAddressEntity.getStateName());
	address.setDistrictId(tempMPHAddressEntity.getDistrictId());
	address.setDistrict(tempMPHAddressEntity.getDistrict());
	address.setPincode(tempMPHAddressEntity.getPincode());
	address.setCountryId(tempMPHAddressEntity.getCountryId());
	address.setIsActive(tempMPHAddressEntity.getIsActive());
	address.setCreatedBy(tempMPHAddressEntity.getCreatedBy());
	address.setCreatedDate(tempMPHAddressEntity.getCreatedDate());
	address.setModifiedBy(tempMPHAddressEntity.getModifiedBy());
	address.setModifiedDate(tempMPHAddressEntity.getModifiedDate());
	
	return address;
}

public TempMPHAddressEntity convertMPHAddressEntityToTempMPHAddressEntity(MPHAddressEntity mPHAddressEntity) {
	TempMPHAddressEntity address = new TempMPHAddressEntity();
	address.setId(mPHAddressEntity.getId());
	address.setAddressLine1(mPHAddressEntity.getAddressLine1());
	address.setAddressLine2(mPHAddressEntity.getAddressLine2());
	address.setAddressLine3(mPHAddressEntity.getAddressLine3());
	address.setAddressType(mPHAddressEntity.getAddressType());
	address.setCityId(mPHAddressEntity.getCityId());
	address.setCityLocality(mPHAddressEntity.getCityLocality());
	address.setStateId(mPHAddressEntity.getStateId());
	address.setStateName(mPHAddressEntity.getStateName());
	address.setDistrictId(mPHAddressEntity.getDistrictId());
	address.setDistrict(mPHAddressEntity.getDistrict());
	address.setPincode(mPHAddressEntity.getPincode());
	address.setCountryId(mPHAddressEntity.getCountryId());
	address.setIsActive(mPHAddressEntity.getIsActive());
	address.setCreatedBy(mPHAddressEntity.getCreatedBy());
	address.setCreatedDate(mPHAddressEntity.getCreatedDate());
	address.setModifiedBy(mPHAddressEntity.getModifiedBy());
	address.setModifiedDate(mPHAddressEntity.getModifiedDate());
	
	
	return address;
}
}
