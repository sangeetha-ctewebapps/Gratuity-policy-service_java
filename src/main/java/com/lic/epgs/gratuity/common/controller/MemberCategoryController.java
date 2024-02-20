package com.lic.epgs.gratuity.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.service.CommonService;
import com.lic.epgs.gratuity.common.service.MemberCategoryService;


@RestController
@CrossOrigin("*")
@RequestMapping("api/membercategory")
public class MemberCategoryController {
	
	
	
	@Autowired
	private MemberCategoryService memberCategoryService;

	
	
	@GetMapping(value ="{entrytype}/{type}/{id}")
	public   ApiResponseDto <List<MemberCategoryDto> > getAllMemberCategory( @PathVariable("entrytype") String entrytype,@PathVariable("type") String type,@PathVariable("id") Long id ){
		
		return memberCategoryService.getAllMemberCategory(entrytype,type,id);
		
	}
}


