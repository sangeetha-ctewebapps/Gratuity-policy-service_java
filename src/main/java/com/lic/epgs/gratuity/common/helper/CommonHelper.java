package com.lic.epgs.gratuity.common.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.dto.MemberWithdrawaldto;
import com.lic.epgs.gratuity.common.dto.ProductFeatureDto;
import com.lic.epgs.gratuity.common.dto.TaskAllocationDto;
import com.lic.epgs.gratuity.common.dto.TaskProcessDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberWithdrawalEntity;
import com.lic.epgs.gratuity.common.entity.ProductFeatureEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;

public class CommonHelper {

	  public static MemberWithdrawaldto entityToDto(MemberWithdrawalEntity entity) {
		  return new ModelMapper().map(entity, MemberWithdrawaldto.class);
	}



public static MemberCategoryDto entityToDto(MemberCategoryEntity entity) {
	  return new ModelMapper().map(entity, MemberCategoryDto.class);
}

public static TaskAllocationDto enititytoDto(TaskAllocationEntity taskAllocationEntityGetAll) {
	return new ModelMapper().map(taskAllocationEntityGetAll, TaskAllocationDto.class);
}

public static TaskProcessDto enititytoDto(TaskProcessEntity taskProcessEntityGetAll) {
	return new ModelMapper().map(taskProcessEntityGetAll, TaskProcessDto.class);
}

	public static ProductFeatureDto enititytoDto(ProductFeatureEntity productFeatureEntity) {
		return new ModelMapper().map(productFeatureEntity, ProductFeatureDto.class);
	}



	public static Double convertTwoDecimalValue(Double amount) {
		Double amountD = amount * 100;
		Long amounttoLong = Math.round(amountD);
		amountD = amounttoLong.doubleValue() / 100;
		return amountD;
	}
	
	public static Double convertFourDecimalValue(Double amount) {
		Double amountD = amount * 10000;
		Long amounttoLong = Math.round(amountD);
		amountD = amounttoLong.doubleValue() / 10000;
		return amountD;
	}
}
