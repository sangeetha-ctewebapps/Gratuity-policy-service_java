package com.lic.epgs.gratuity.renewalpolicy.notes.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;

import com.lic.epgs.gratuity.renewalpolicy.notes.dto.RenewalPolicyNotesDto;
import com.lic.epgs.gratuity.renewalpolicy.notes.entity.RenewalPolicyNotesEntity;
import com.lic.epgs.gratuity.renewalpolicy.notes.helper.RenewalPolicyNotesHelper;
import com.lic.epgs.gratuity.renewalpolicy.notes.repository.RenewalPolicyNotesRepository;
import com.lic.epgs.gratuity.renewalpolicy.notes.service.RenewalPolicyNotesService;

@Service
public class RenewalPolicyNotesImpl implements RenewalPolicyNotesService {

	@Autowired
	private RenewalPolicyNotesRepository renewalPolicyNotesRepository;

	@Override
	public ApiResponseDto<List<RenewalPolicyNotesDto>> create(RenewalPolicyNotesDto renewalPolicyNotesDto) {

		RenewalPolicyNotesEntity renewalPolicyNotesEntity = RenewalPolicyNotesHelper.dtoToEntity(renewalPolicyNotesDto);

		renewalPolicyNotesEntity.setIsActive(true);
		renewalPolicyNotesEntity.setCreatedBy(renewalPolicyNotesDto.getCreatedBy());
		renewalPolicyNotesEntity.setCreatedDate(new Date());

		renewalPolicyNotesRepository.save(renewalPolicyNotesEntity);

		return ApiResponseDto.created(renewalPolicyNotesRepository
				.findBytmpPolicyIdAndNotesType(renewalPolicyNotesDto.getTmpPolicyId(),
						renewalPolicyNotesDto.getNotesType())
				.stream().map(RenewalPolicyNotesHelper::entityToDto).collect(Collectors.toList()));

	}

	@Override
	public ApiResponseDto<List<RenewalPolicyNotesDto>> findByPolicyIdAndNotesType(Long tmppolicyId, String notesType,
			String type) {

		if (type.equals("INPROGRESS")) {
			List<RenewalPolicyNotesEntity> renewalPolicyNotesEntity = renewalPolicyNotesRepository
					.findBytmpPolicyIdAndNotesType(tmppolicyId, notesType);
			if (renewalPolicyNotesEntity.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto.success(renewalPolicyNotesEntity.stream()
						.map(RenewalPolicyNotesHelper::entityToDto).collect(Collectors.toList()));
			}
		} else {
			return ApiResponseDto.success(null);
		}

	}
}
