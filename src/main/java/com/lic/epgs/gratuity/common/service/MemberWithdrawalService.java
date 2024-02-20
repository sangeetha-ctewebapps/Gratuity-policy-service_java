package com.lic.epgs.gratuity.common.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberWithdrawaldto;
import com.lic.epgs.gratuity.common.dto.TaskAllocationDto;
import com.lic.epgs.gratuity.common.dto.TaskProcessDto;

public interface MemberWithdrawalService {


	ApiResponseDto<List<MemberWithdrawaldto>> findAllMemberWithdrawal();

	ApiResponseDto<List<TaskAllocationDto>> getallpendingallocationtask();

	ApiResponseDto<List<TaskProcessDto>> getalltaskprocessmodule();

	

}
