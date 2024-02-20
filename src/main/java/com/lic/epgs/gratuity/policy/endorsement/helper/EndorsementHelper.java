package com.lic.epgs.gratuity.policy.endorsement.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.endorsement.dto.EndorsementDto;
import com.lic.epgs.gratuity.policy.endorsement.entity.EndorsementEntity;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberDto;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;


public class EndorsementHelper {
	private static String endorsementStatusId  ="157";
	private static String endorsementSubStatusId ="158";

	public static EndorsementDto entityToDto(EndorsementEntity endorsementEntity) {
		// TODO Auto-generated method stub
		return new ModelMapper().map(endorsementEntity, EndorsementDto.class);
	}

	public static Long nextServiceNumber(Long lastServiceNumber) {
		lastServiceNumber = lastServiceNumber == null ? 1 : lastServiceNumber + 1;
		return lastServiceNumber;
	}

	public static List<EndorsementDto> entityToDto(List<EndorsementEntity> endorsementEntity) {
	List<EndorsementDto> newEndorsementEntity=new ArrayList<>();
	
	for(EndorsementEntity getEndorsementEntity :endorsementEntity ) {
		
		EndorsementDto changeEndorsementDto=new ModelMapper().map(getEndorsementEntity,EndorsementDto.class);
		if(changeEndorsementDto.getEndorsementStatusId() != Long.parseLong(endorsementStatusId) && changeEndorsementDto.getEndorsementStatusId() != Long.parseLong(endorsementSubStatusId)) {
			newEndorsementEntity.add(changeEndorsementDto);
		}
	
	}
		return  newEndorsementEntity;
	}

	public static PolicyMemberEntity updateTemptoPolicyMemberMaster(TempMemberEntity newTempMemberEntity,String username) {
		PolicyMemberEntity policyMemberEntity =new ModelMapper().map(newTempMemberEntity, PolicyMemberEntity.class);
		policyMemberEntity.setId(newTempMemberEntity.getPmstMemberId());
		policyMemberEntity.setModifiedBy(username);
		policyMemberEntity.setModifiedDate(new Date());
		policyMemberEntity.setIsActive(true);
		return policyMemberEntity;
	}




}
