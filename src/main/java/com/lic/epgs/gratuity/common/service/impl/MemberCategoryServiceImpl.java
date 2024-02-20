package com.lic.epgs.gratuity.common.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.helper.CommonHelper;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.service.MemberCategoryService;

@Service
public class MemberCategoryServiceImpl implements MemberCategoryService {

	@Autowired
	private MemberCategoryRepository memberCategoryRepository;

	@Override
	public ApiResponseDto<List<MemberCategoryDto>> getAllMemberCategory( String entrytype, String type,Long id) {

		List<MemberCategoryEntity> memberCategoryList = new ArrayList<MemberCategoryEntity>();

		if (entrytype.equals("QUOTATION")) {

			if (type.equals("INPROGRESS")) {

				memberCategoryList = memberCategoryRepository.findByqstgQuoationId(id);

			} else {

				memberCategoryList = memberCategoryRepository.findByqmstQuotationId(id);

			}
		}
		if (entrytype.equals("POLICY")) {

			if (type.equals("INPROGRESS")) {

				memberCategoryList = memberCategoryRepository.findBypstgPolicyId(id);

			} else {

				memberCategoryList = memberCategoryRepository.findBypmstPolicyId(id);

			}

		}
		if (entrytype.equals("RENEWALS")) {

			if (type.equals("INPROGRESS")) {

				memberCategoryList = memberCategoryRepository.findBypmstTmpPolicy(id);

			} else {

				memberCategoryList = memberCategoryRepository.findBypmstTmpPolicy(id);

			}

		}

		return ApiResponseDto
				.success(memberCategoryList.stream().map(CommonHelper::entityToDto).collect(Collectors.toList()));
	}

}
