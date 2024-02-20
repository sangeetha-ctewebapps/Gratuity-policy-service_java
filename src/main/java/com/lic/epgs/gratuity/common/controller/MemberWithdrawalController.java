package com.lic.epgs.gratuity.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberWithdrawaldto;
import com.lic.epgs.gratuity.common.dto.TaskAllocationDto;
import com.lic.epgs.gratuity.common.dto.TaskProcessDto;
import com.lic.epgs.gratuity.common.service.MemberWithdrawalService;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/memberwithdrawl")
public class MemberWithdrawalController {
	
	@Autowired
	private MemberWithdrawalService memberWithdrawalService;
	
	@GetMapping("getall")
	public ApiResponseDto<List<MemberWithdrawaldto>> findAllMemberWithdrawal() {
		
		return memberWithdrawalService.findAllMemberWithdrawal();
	}
	
	
	@GetMapping("getallpendingallocationtask")
	public ApiResponseDto<List<TaskAllocationDto>> getallpendingallocationtask() {
		
		return memberWithdrawalService.getallpendingallocationtask();
	}
	
	@GetMapping("getalltaskprocessmodule")
	public ApiResponseDto<List<TaskProcessDto>> getalltaskprocessmodule() {
		
		return memberWithdrawalService.getalltaskprocessmodule();
	}
	

}
