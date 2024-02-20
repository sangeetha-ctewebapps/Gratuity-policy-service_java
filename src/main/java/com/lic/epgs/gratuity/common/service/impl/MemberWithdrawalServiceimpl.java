package com.lic.epgs.gratuity.common.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberWithdrawaldto;
import com.lic.epgs.gratuity.common.dto.TaskAllocationDto;
import com.lic.epgs.gratuity.common.dto.TaskProcessDto;
import com.lic.epgs.gratuity.common.entity.MemberWithdrawalEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.common.helper.CommonHelper;
import com.lic.epgs.gratuity.common.repository.MemberWithdrawalRepository;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.service.MemberWithdrawalService;

@Service
public class MemberWithdrawalServiceimpl implements MemberWithdrawalService {

	
	@Autowired
	private MemberWithdrawalRepository memberWithdrawalRepository;
	
	@Autowired
	private TaskAllocationRepository taskAllocationRepository;
	
	@Autowired
	private TaskProcessRepository taskProcessRepository;
	
	

		@Override
		public ApiResponseDto<List<MemberWithdrawaldto>> findAllMemberWithdrawal() {
			List<MemberWithdrawalEntity> entities  =memberWithdrawalRepository.findAll();
			if(entities.isEmpty())
			{
				return ApiResponseDto.notFound(Collections.emptyList());
			}
			else
			{
				return ApiResponseDto.success(entities.stream().map(CommonHelper::entityToDto)
						.collect(Collectors.toList()));
			}
		    }


		@Override
		public ApiResponseDto<List<TaskAllocationDto>> getallpendingallocationtask() {
				
			List<TaskAllocationEntity> taskAllocationEntityGetAll=taskAllocationRepository.findAllocationtoMakerandCheckerStatus();
		
			List<TaskAllocationDto> getTaskAllocationDto =new ArrayList<TaskAllocationDto>();
			for(TaskAllocationEntity getTaskAllocationEntity:taskAllocationEntityGetAll) {
				TaskAllocationDto TaskAllocationDto=new ModelMapper().map(getTaskAllocationEntity, TaskAllocationDto.class);
				TaskAllocationDto.setTaskProcessTaskPrType(taskProcessRepository.getModulType(getTaskAllocationEntity.getTaskProcessTaskPrId()));
				getTaskAllocationDto.add(TaskAllocationDto);
				
				
			}
			
			return ApiResponseDto.created(getTaskAllocationDto);
		}

		@Override
		public ApiResponseDto<List<TaskProcessDto>> getalltaskprocessmodule() {
			
			List<TaskProcessEntity> taskProcessEntityGetAll=taskProcessRepository.findAll();
			
			return ApiResponseDto.created(taskProcessEntityGetAll.stream().map(CommonHelper::enititytoDto).collect(Collectors.toList()));

		}


		
		   
		
	
}
