package com.lic.epgs.gratuity.mph.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.mph.dto.MPHEntityDto;
import com.lic.epgs.gratuity.mph.dto.TempMPHAddressDto;
import com.lic.epgs.gratuity.mph.dto.TempMPHBankDto;
import com.lic.epgs.gratuity.mph.dto.TemptMPHDto;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.StagingMPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.helper.MPHHelper;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.StagingMPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHAddressRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHBankRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.mph.service.MPHService;
import com.lic.epgs.gratuity.policy.dto.MasterPolicyDto;
import com.lic.epgs.gratuity.policy.member.helper.PolicyMemberHelper;
import com.lic.epgs.gratuity.quotation.member.dto.MemberDto;
import com.lic.epgs.gratuity.quotation.member.entity.MemberEntity;


@Service
public class MPHServiceImpl implements MPHService {

	@Autowired
	private MPHRepository mphEntityRepository;
	
	@Autowired
	private StagingMPHRepository stagingMPHRepository;
	
	@Autowired
	private TempMPHRepository  tempMPHRepository;
	
	@Autowired
	private MPHRepository mphRepository;
	
	@Autowired
	private TempMPHBankRepository mphBankRepository;
	
	
	
	@Autowired
	private TempMPHAddressRepository TempMPHAddressRepository;
	
	@Override
	public ApiResponseDto<MPHEntityDto> findById(Long mphId,String type) {
		if(type.equals("EXISTING")) {
		MPHEntity entity = mphEntityRepository.findById(mphId).get();
		MPHEntityDto mdto =MPHHelper.entityToDto(entity);
		
		if (entity.getMphAddresses() != null && !entity.getMphAddresses().isEmpty()) {
			mdto.setMphAddresses(entity.getMphAddresses().stream().map(MPHHelper::addressEntityToDto)
			.collect(Collectors.toList()));
		}
		if (entity.getMphBank() != null && !entity.getMphBank().isEmpty()) {
			mdto.setMphBank(entity.getMphBank().stream().map(MPHHelper :: bankEntityToDto).collect(Collectors.toList()));
		}
		
		if (entity.getMphRepresentatives() != null && !entity.getMphRepresentatives().isEmpty()) {
			mdto.setMphRepresentatives(entity.getMphRepresentatives().stream().map(MPHHelper :: repEntityToDto).collect(Collectors.toList()));
		}
		
		return ApiResponseDto.success(mdto);
	}
		else {
			StagingMPHEntity entity = stagingMPHRepository.findById(mphId).get();
			MPHEntityDto mdto =MPHHelper.entityStagingToDto(entity);
			
			if (entity.getMphAddresses() != null && !entity.getMphAddresses().isEmpty()) {
				mdto.setMphAddresses(entity.getMphAddresses().stream().map(MPHHelper::addressEntityToDto)
				.collect(Collectors.toList()));
			}
			if (entity.getMphBank() != null && !entity.getMphBank().isEmpty()) {
				mdto.setMphBank(entity.getMphBank().stream().map(MPHHelper :: bankEntityToDto).collect(Collectors.toList()));
			}
			
			if (entity.getMphRepresentatives() != null && !entity.getMphRepresentatives().isEmpty()) {
				mdto.setMphRepresentatives(entity.getMphRepresentatives().stream().map(MPHHelper :: repEntityToDto).collect(Collectors.toList()));
			}
			
			return ApiResponseDto.success(mdto);
		}

	}

	@Override
	public ApiResponseDto<TemptMPHDto> tempMPHSave(Long endoresementId,TemptMPHDto temptMPHDto) {
		
	
		TempMPHEntity tempMPHEntity=MPHHelper.masterEntitytoTempEntity(mphRepository.findById(temptMPHDto.getPmstId()).get(),endoresementId);
		// TODO Auto-generated method stub
		tempMPHEntity.setPmstId(temptMPHDto.getPmstId());
		tempMPHEntity.setMobileNo(temptMPHDto.getMobileNo());
		tempMPHEntity.setEndrosementId(endoresementId);
		tempMPHEntity.setCreatedBy(temptMPHDto.getCreatedBy());
		tempMPHEntity.setCreatedDate(new Date());
		tempMPHEntity.setIsActive(true);
		
		return ApiResponseDto.created(MPHHelper.TempEntitytoDto(tempMPHRepository.save(tempMPHEntity)));
	}

	@Override
	public ApiResponseDto<TempMPHAddressDto> tempMPHAddressSave(Long endoresementId, TempMPHAddressDto tempMPHAddressDto) {
		
		TempMPHEntity tempMPHEntity = MPHHelper.masterEntitytoTempEntity(mphRepository.findById(tempMPHAddressDto.getPmstId()).get(),endoresementId);
		tempMPHEntity.setPmstId(tempMPHAddressDto.getPmstId());
		tempMPHEntity.setEndrosementId(endoresementId);
		tempMPHRepository.save(tempMPHEntity);
		
		
		for(TempMPHAddressEntity tempAddressEntity : tempMPHEntity.getMphAddresses()) {
			
			System.out.println(tempMPHEntity.getMphAddresses());
			
			if(tempAddressEntity.getPmstId() == tempMPHAddressDto.getPmstId()) {
				
				tempAddressEntity.setCityLocality(tempMPHAddressDto.getCityLocality());
				tempAddressEntity.setAddressLine1(tempMPHAddressDto.getAddressLine1());
				tempAddressEntity.setCityId(tempMPHAddressDto.getCityId());
				tempAddressEntity.setCountryId(tempMPHAddressDto.getCountryId());
				
				tempAddressEntity.setPmstId(tempAddressEntity.getPmstId());
				tempAddressEntity.setStateId(tempMPHAddressDto.getStateId());
				tempAddressEntity.setStateName(tempMPHAddressDto.getStateName());
				
		}
			
			return ApiResponseDto.created(MPHHelper.TempEntitytoDto(TempMPHAddressRepository.save(tempAddressEntity)));
			
		}
		
	return null;
	
		
	}

	@Transactional
	@Override
	public ApiResponseDto<TemptMPHDto> saveMph(Long temppolicyId, TempMPHBankDto tempMphBankdto) {

		TempMPHEntity tempmphentity = tempMPHRepository.findBytmpPolicyId(temppolicyId).get();
		Set<TempMPHBankEntity> tempBankEntity= new HashSet<TempMPHBankEntity>();
		TempMPHBankEntity tempmphBank = MPHHelper.tempBankDtoToEntity(tempMphBankdto);
		tempmphBank.setCreatedBy(tempMphBankdto.getCreatedBy());
		tempmphBank.setCreatedDate(new Date());
		tempmphBank.setIsActive(true);
		tempmphBank.setMasterMph(tempmphentity);
		
		tempBankEntity.add(tempmphBank);
		
		tempBankEntity.addAll(tempmphentity.getMphBank());
		tempmphentity.setMphBank(tempBankEntity);		
		tempMPHRepository.save(tempmphentity);
		
		return ApiResponseDto.created(MPHHelper.tmpentityToDto(tempmphentity));
	
		
		
	}

	@Override
	public ApiResponseDto<TemptMPHDto> deleteMphBank(Long mphbankId) {
		mphBankRepository.deleteMphBankId(mphbankId);
		return ApiResponseDto.success(null); 
	}

	@Transactional
	@Override
	public ApiResponseDto<TemptMPHDto> updateMph(Long mphId, TempMPHBankDto tempMPHBankDto) {
		
		 TempMPHBankEntity tmpMphBank = mphBankRepository.findById(mphId).get();
		 TempMPHBankEntity tempMphBank = MPHHelper.tempBankDtoToEntity(tempMPHBankDto);	
		 TempMPHEntity entity = tempMPHRepository.findById(tmpMphBank.getMasterMph().getId()).get();
		 tempMphBank.setId(mphId);
		 tempMphBank.setMasterMph(tmpMphBank.getMasterMph());
		 tempMphBank.setCreatedDate(tmpMphBank.getCreatedDate());
		 tempMphBank.setCreatedBy(tmpMphBank.getCreatedBy());
		 tempMphBank.setModifiedBy(tempMPHBankDto.getModifiedBy());
		 tempMphBank.setModifiedDate(new Date());
		 tempMphBank.setIsActive(true);
		 mphBankRepository.save(tempMphBank);
		           
		return ApiResponseDto.created(MPHHelper.tmpentityToDto(entity));
	}

	@Transactional
	@Override
	public ApiResponseDto<TemptMPHDto> viewMph(Long tempPolicyId) {
		
		TempMPHEntity entity = tempMPHRepository.findByTempPolicyId(tempPolicyId);
		Set<TempMPHBankEntity> tmpMphBank = entity.getMphBank();
		
		return ApiResponseDto.success(MPHHelper.tmpentityToDto(entity));
	}
	

	
}
	
	
	
	
		


