package com.lic.epgs.gratuity.mph.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.mph.dto.MPHAddressDto;
import com.lic.epgs.gratuity.mph.dto.MPHBankDto;
import com.lic.epgs.gratuity.mph.dto.MPHEntityDto;
import com.lic.epgs.gratuity.mph.dto.MPHRepresentativeDto;
import com.lic.epgs.gratuity.mph.dto.TempMPHAddressDto;
import com.lic.epgs.gratuity.mph.dto.TempMPHBankDto;
import com.lic.epgs.gratuity.mph.dto.TemptMPHDto;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.MPHRepresentativesEntity;
import com.lic.epgs.gratuity.mph.entity.StagingMPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.StagingMPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.StagingMPHEntity;
import com.lic.epgs.gratuity.mph.entity.StagingMPHRepresentativesEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHRepresentativeEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.quotation.premium.dto.KeyValuePairDto;
import com.lic.epgs.gratuity.quotation.premium.entity.OyrgtavaluesEntity;

public class MPHHelper {

	public static MPHEntityDto entityToDto(MPHEntity entity) {
		return new ModelMapper().map(entity, MPHEntityDto.class);
	}

	public static MPHAddressDto addressEntityToDto(MPHAddressEntity entity) {
		return new ModelMapper().map(entity, MPHAddressDto.class);
	}

	public static MPHBankDto bankEntityToDto(MPHBankEntity entity) {
		return new ModelMapper().map(entity, MPHBankDto.class);
	}

	public static MPHRepresentativeDto repEntityToDto(MPHRepresentativesEntity entity) {
		return new ModelMapper().map(entity, MPHRepresentativeDto.class);
	}

	public static MPHAddressDto addressEntityToDto(StagingMPHAddressEntity entity) {
		return new ModelMapper().map(entity, MPHAddressDto.class);
	}

	public static MPHBankDto bankEntityToDto(StagingMPHBankEntity entity) {
		return new ModelMapper().map(entity, MPHBankDto.class);
	}

	public static MPHRepresentativeDto repEntityToDto(StagingMPHRepresentativesEntity entity) {
		return new ModelMapper().map(entity, MPHRepresentativeDto.class);
	}

	public static StagingMPHEntity getMph(String proposalNumber, String userName, String endPoint) {

		StagingMPHEntity mphEntity = new StagingMPHEntity();
		try {
			URL url = new URL(
					endPoint + "/api/proposal/getProposalDetailsByProposalNumber?proposalNumber=" + proposalNumber);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);
			System.out.println(actualObj);

			JsonNode mphBasic = actualObj.path("responseData").path("mphDetails").path("mphBasicDetails");

			mphEntity.setMphCode(mphBasic.path("mphCode").textValue());
			mphEntity.setMphName(mphBasic.path("mphName").textValue());
			// TODO: MPHType is not available in API
			mphEntity.setMphType(null);
			// TODO: Following fields are not available in API mphContactDetails
			mphEntity.setCin(null);
			mphEntity.setPan(null);
			mphEntity.setAlternatePan(null);
			mphEntity.setCountryId(null);
			mphEntity.setEmailId(null);
			mphEntity.setMobileNo(0);
			mphEntity.setLandlineNo(0);
			mphEntity.setFax(0);
			mphEntity.setGstIn(mphBasic.path("gstin").textValue());
			mphEntity.setIsActive(true);
			mphEntity.setCreatedBy(userName);
			mphEntity.setCreatedDate(new Date());

			JsonNode mphBank = actualObj.path("responseData").path("mphDetails").path("mphBankAccountDetails");
			Set<StagingMPHBankEntity> mphBankEntities = new HashSet<StagingMPHBankEntity>();
			for (int i = 0; i < mphBank.size(); i++) {
				StagingMPHBankEntity mphBankEntity = new StagingMPHBankEntity();
				mphBankEntity.setStagingMph(mphEntity);
				mphBankEntity.setAccountNumber(mphBank.get(i).path("accountNumber").textValue());
				mphBankEntity.setAccountType(mphBank.get(i).path("accountType").textValue());
				mphBankEntity.setIfscCode(mphBank.get(i).path("ifscCode").textValue());
				mphBankEntity.setBankName(mphBank.get(i).path("bankName").textValue());
				mphBankEntity.setBankBranch(mphBank.get(i).path("bankBranch").textValue());
				mphBankEntity.setBankAddress(mphBank.get(i).path("bankAddressOne").textValue());
				// TODO: Not received from API
				mphBankEntity.setCityId(null);
				mphBankEntity.setTownLocality(null);
				mphBankEntity.setStateId(null);
				mphBankEntity.setDistrictId(null);
				// TODO: CountryCode is declared as Integer and API having +91
				mphBankEntity.setCountryCode(0L);
				mphBankEntity.setCountryId(null);
				mphBankEntity.setStdCode((Long) mphBank.get(i).path("stdCode").numberValue());
				mphBankEntity.setLandlineNumber((Long) mphBank.get(i).path("landlineNumber").numberValue());
				mphBankEntity.setEmailId(mphBank.get(i).path("emailID").textValue());
				mphBankEntity.setIsActive(true);
				mphBankEntity.setCreatedBy(userName);
				mphBankEntity.setCreatedDate(new Date());
				mphBankEntities.add(mphBankEntity);
			}
			mphEntity.setMphBank(mphBankEntities);

			JsonNode mphAdds = actualObj.path("responseData").path("mphDetails").path("mphAddressDetails");
			Set<StagingMPHAddressEntity> mphAddressEntities = new HashSet<StagingMPHAddressEntity>();

			for (int i = 0; i < mphAdds.size(); i++) {
				StagingMPHAddressEntity mphAddressEntity = new StagingMPHAddressEntity();
				mphAddressEntity.setStagingMph(mphEntity);
				mphAddressEntity.setAddressLine1(mphAdds.get(i).path("address1").textValue());
				mphAddressEntity.setAddressLine2(mphAdds.get(i).path("address2").textValue());
				mphAddressEntity.setAddressLine3(mphAdds.get(i).path("address3").textValue());
				mphAddressEntity.setAddressType(mphAdds.get(i).path("addressType").textValue());
				mphAddressEntity.setCityId(0L);
				mphAddressEntity.setCityLocality(mphAdds.get(i).path("city").textValue());
				mphAddressEntity.setStateId(0L);
				mphAddressEntity.setStateName(mphAdds.get(i).path("state").textValue());
				mphAddressEntity.setDistrictId(0L);
				mphAddressEntity.setDistrict(mphAdds.get(i).path("district").textValue());
				mphAddressEntity.setPincode(Long.parseLong(mphAdds.get(i).path("pinCode").textValue()));
				mphAddressEntity.setCountryId(0L);
				mphAddressEntity.setIsActive(true);
				mphAddressEntity.setCreatedBy(userName);
				mphAddressEntity.setCreatedDate(new Date());
				mphAddressEntities.add(mphAddressEntity);
			}
			mphEntity.setMphAddresses(mphAddressEntities);

			JsonNode mphRep = actualObj.path("responseData").path("mphDetails").path("mphContactDetails");
			Set<StagingMPHRepresentativesEntity> mphRepresentativesEntities = new HashSet<StagingMPHRepresentativesEntity>();
			for (int i = 0; i < mphRep.size(); i++) {
				StagingMPHRepresentativesEntity mphRepresentativesEntity = new StagingMPHRepresentativesEntity();
				mphRepresentativesEntity.setStagingMph(mphEntity);
				mphRepresentativesEntity.setAddressLine1(null);
				mphRepresentativesEntity.setAddressLine2(null);
				mphRepresentativesEntity.setAddressLine3(null);
				mphRepresentativesEntity.setAddressType(null);
				mphRepresentativesEntity.setCityLocality(mphRep.get(i).path("city").textValue());
				mphRepresentativesEntity.setStateName(null);
				mphRepresentativesEntity.setDistrict(null);
				mphRepresentativesEntity.setCountryCode(mphRep.get(i).path("countryCode").textValue());
				mphRepresentativesEntity.setEmailId(mphRep.get(i).path("emailID").textValue());
				mphRepresentativesEntity.setMobileNo(mphRep.get(i).path("mobileNumber").textValue());
				mphRepresentativesEntity.setLandlineNo(mphRep.get(i).path("landlineNumber").textValue());
				mphRepresentativesEntity.setPincode(null);
				mphRepresentativesEntity.setRepresentativeCode(null);
				mphRepresentativesEntity.setRepresentativeName(mphRep.get(i).path("firstName").textValue());
				mphRepresentativesEntity.setIsActive(true);
				mphRepresentativesEntity.setCreatedBy(userName);
				mphRepresentativesEntity.setCreatedDate(new Date());
				mphRepresentativesEntities.add(mphRepresentativesEntity);
			}
			mphEntity.setMphRepresentatives(mphRepresentativesEntities);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mphEntity;
	}

	public static MPHEntity entitytostagetomaster(StagingMPHEntity entities) {

		MPHEntity mphEntity = new ModelMapper().map(entities, MPHEntity.class);
		mphEntity.setId(null);
		Set<MPHBankEntity> mPHBankEntity = new HashSet<MPHBankEntity>();
		for (MPHBankEntity mPHBankEntities : mphEntity.getMphBank()) {
			mPHBankEntities.setId(null);
			mPHBankEntities.setMasterMph(mphEntity);
			mPHBankEntity.add(mPHBankEntities);
		}
		mphEntity.setMphBank(mPHBankEntity);

		Set<MPHAddressEntity> mPHAddressEntities = new HashSet<MPHAddressEntity>();
		for (MPHAddressEntity mPHAddressEntity : mphEntity.getMphAddresses()) {
			mPHAddressEntity.setId(null);
			mPHAddressEntity.setMasterMph(mphEntity);
			mPHAddressEntities.add(mPHAddressEntity);
		}

		mphEntity.setMphAddresses(mPHAddressEntities);

		Set<MPHRepresentativesEntity> mPHRepresentativesEntities = new HashSet<MPHRepresentativesEntity>();
		for (MPHRepresentativesEntity mPHRepresentativesEntity : mphEntity.getMphRepresentatives()) {
			mPHRepresentativesEntity.setId(null);
			mPHRepresentativesEntity.setMasterMph(mphEntity);
			mPHRepresentativesEntities.add(mPHRepresentativesEntity);
		}
		mphEntity.setMphRepresentatives(mPHRepresentativesEntities);

		return mphEntity;
	}

	public static MPHEntityDto entityStagingToDto(StagingMPHEntity entity) {
		return new ModelMapper().map(entity, MPHEntityDto.class);
	}

	public static TempMPHEntity masterEntitytoTempEntity(MPHEntity mphEntity, Long endoresementId) {
		TempMPHEntity tempMPHEntity = new ModelMapper().map(mphEntity, TempMPHEntity.class);
		tempMPHEntity.setId(null);

		Set<TempMPHAddressEntity> addTempMPHAddressEntity = new HashSet<TempMPHAddressEntity>();
		for (MPHAddressEntity mphAddressEntity : mphEntity.getMphAddresses()) {

			TempMPHAddressEntity tempMPHAddressEntity = new ModelMapper().map(mphAddressEntity,
					TempMPHAddressEntity.class);
			tempMPHAddressEntity.setId(null);
			tempMPHAddressEntity.setPmstId(mphAddressEntity.getId());
			tempMPHAddressEntity.setMasterMph(tempMPHEntity);
			addTempMPHAddressEntity.add(tempMPHAddressEntity);
		}
		tempMPHEntity.setMphAddresses(addTempMPHAddressEntity);

		Set<TempMPHBankEntity> addTempMPHBankEntity = new HashSet<TempMPHBankEntity>();

		for (MPHBankEntity mphBankEntity : mphEntity.getMphBank()) {

			TempMPHBankEntity tempMPHBankEntity = new ModelMapper().map(mphBankEntity, TempMPHBankEntity.class);
			tempMPHBankEntity.setId(null);
			tempMPHBankEntity.setPmstId(mphBankEntity.getId());
			tempMPHBankEntity.setMasterMph(tempMPHEntity);

			addTempMPHBankEntity.add(tempMPHBankEntity);
		}
		tempMPHEntity.setMphBank(addTempMPHBankEntity);

		Set<TempMPHRepresentativeEntity> addTempMPHRepresentativeEntity = new HashSet<TempMPHRepresentativeEntity>();

		for (MPHRepresentativesEntity mphRepresentivekEntity : mphEntity.getMphRepresentatives()) {

			TempMPHRepresentativeEntity tempMPHRepresentativeEntity = new ModelMapper().map(mphRepresentivekEntity,
					TempMPHRepresentativeEntity.class);
			tempMPHRepresentativeEntity.setId(null);
			tempMPHRepresentativeEntity.setPmstId(mphRepresentivekEntity.getId());
			tempMPHRepresentativeEntity.setMasterMph(tempMPHEntity);
			addTempMPHRepresentativeEntity.add(tempMPHRepresentativeEntity);
		}
		tempMPHEntity.setMphRepresentatives(addTempMPHRepresentativeEntity);

		return tempMPHEntity;
	}

	public static TemptMPHDto TempEntitytoDto(TempMPHEntity entity) {
		return new ModelMapper().map(entity, TemptMPHDto.class);
	}

	public static TempMPHAddressDto TempEntitytoDto(TempMPHAddressEntity entity) {
		return new ModelMapper().map(entity, TempMPHAddressDto.class);

	}

	public static KeyValuePairDto OyrgtavaluesEntityToDto(Object OyrgtavaluesEntity) {

		return new ModelMapper().map(OyrgtavaluesEntity, KeyValuePairDto.class);
	}

	public static TemptMPHDto tmpentityToDto(TempMPHEntity tempMPHEntity) {

		return new ModelMapper().map(tempMPHEntity, TemptMPHDto.class);
	}

	public static TemptMPHDto mphEntityToDto(MPHEntity mphEntity) {

		return new ModelMapper().map(mphEntity, TemptMPHDto.class);
	}

	public static TempMPHBankEntity tempBankDtoToEntity(TempMPHBankDto tempMPHBankDto) {
		return new ModelMapper().map(tempMPHBankDto, TempMPHBankEntity.class);
	}

	public static TempMPHBankDto tempBankEntityToDto(TempMPHBankEntity tempMPHBankEntity) {
		return new ModelMapper().map(tempMPHBankEntity, TempMPHBankDto.class);
	}

	public static TempMPHBankEntity stagingMPHBankEntityToTempMPHBankEntity(StagingMPHBankEntity stagingMPHBankEntity) {
		return new ModelMapper().map(stagingMPHBankEntity, TempMPHBankEntity.class);
	}

}
