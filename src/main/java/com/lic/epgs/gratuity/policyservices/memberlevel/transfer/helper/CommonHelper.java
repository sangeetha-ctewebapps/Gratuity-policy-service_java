package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.dto.MemberWithdrawaldto;
import com.lic.epgs.gratuity.common.dto.TaskAllocationDto;
import com.lic.epgs.gratuity.common.dto.TaskProcessDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberWithdrawalEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.MemberCategoryVersionDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.MemberCategoryEntityVersion;

public class CommonHelper {



public static MemberCategoryVersionDto entityToDtoVersion(MemberCategoryEntityVersion entity) {
	  return new ModelMapper().map(entity, MemberCategoryVersionDto.class);
}


}
