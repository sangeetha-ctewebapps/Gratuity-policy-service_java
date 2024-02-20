package com.lic.epgs.gratuity.quotation.valuation.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.MemberWithdrawalEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.repository.MemberWithdrawalRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;
import com.lic.epgs.gratuity.quotation.repository.QuotationRepository;
import com.lic.epgs.gratuity.quotation.valuation.dto.ValuationDto;
import com.lic.epgs.gratuity.quotation.valuation.entity.MasterValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.entity.ValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.helper.ValuationHelper;
import com.lic.epgs.gratuity.quotation.valuation.repository.MasterValuationRepository;
import com.lic.epgs.gratuity.quotation.valuation.repository.ValuationRepository;
import com.lic.epgs.gratuity.quotation.valuation.service.ValuationService;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.ValuationWithdrawalRateRepository;

@Service
public class ValuationServiceImpl implements ValuationService {
	private Long salaryEscalationId = 2L;
	@Autowired
	private ValuationRepository valuationRepository;

	@Autowired
	private MasterValuationRepository masterValuationRepository;
	@Autowired
	private GratuityCalculationRepository gratuityCalculationRepository;
	
	@Autowired
	private StandardCodeRepository standardCodeRepository;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private MemberWithdrawalRepository memberWithdrawalRepository;

	@Autowired
	private ValuationWithdrawalRateRepository valuationWithdrawalRateRepository;

	@Override
	public ApiResponseDto<ValuationDto> findByQuotationId(Long quotationId, String type) {
		if (type.equals("INPROGRESS")) {
			Optional<ValuationEntity> entity = valuationRepository.findByQuotationId(quotationId);
			if (entity.isPresent()) {
				return ApiResponseDto.success(ValuationHelper.entityToDto(entity.get()));
			} else {
				return ApiResponseDto.notFound(new ValuationDto());
			}
		} else {
			Optional<MasterValuationEntity> entity = masterValuationRepository.findByQuotationId(quotationId);
			if (entity.isPresent()) {
				return ApiResponseDto.success(ValuationHelper.entityToDto(entity.get()));
			} else {
				return ApiResponseDto.notFound(new ValuationDto());
			}
		}
	}

	@Override
	public ApiResponseDto<ValuationDto> create(ValuationDto valuationDto) {
		ValuationEntity valuationEntity = ValuationHelper.dtoToEntity(valuationDto);

		valuationEntity.setIsActive(true);
		valuationEntity.setCreatedBy(valuationDto.getCreatedBy());
		valuationEntity.setCreatedDate(new Date());
		valuationEntity = valuationRepository.save(valuationEntity);


		ValuationEntity getValuationEntity = valuationRepository.findByQuotationId(valuationDto.getQuotationId()).get();
		StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(salaryEscalationId).get();
		QuotationEntity quotationEntity = quotationRepository.findById(valuationDto.getQuotationId()).get();

		List<MemberWithdrawalEntity> memberWithdrawalEntities = memberWithdrawalRepository.findAll();
		List<ValuationWithdrawalRateEntity> valuationWithdrawalRateEntity = valuationWithdrawalRateRepository
				.findByQuotationId(valuationDto.getQuotationId());
		quotationEntity = ValuationHelper.checkStdValuationDeviated(getValuationEntity, standardCodeEntity,
				quotationEntity, memberWithdrawalEntities, valuationWithdrawalRateEntity);

		quotationRepository.save(quotationEntity);

		return ApiResponseDto.success(ValuationHelper.entityToDto(valuationEntity));
	}
	
	
	@Transactional
	@Override
	public ApiResponseDto<ValuationDto> update(Long id, ValuationDto valuationDto) {
		ValuationEntity valuationEntity = valuationRepository.findById(id).get();
		ValuationEntity newValuationEntity = ValuationHelper.dtoToEntity(valuationDto);
		if(valuationEntity.getSalaryEscalation() !=newValuationEntity.getSalaryEscalation()) {
			gratuityCalculationRepository.updateDeActiveQuotationId(valuationEntity.getQuotationId());
		}
		newValuationEntity.setId(id);
		newValuationEntity.setIsActive(valuationEntity.getIsActive());
		newValuationEntity.setCreatedBy(valuationEntity.getCreatedBy());
		newValuationEntity.setCreatedDate(valuationEntity.getCreatedDate());
		newValuationEntity.setModifiedBy(valuationDto.getModifiedBy());
		newValuationEntity.setModifiedDate(new Date());
		newValuationEntity=valuationRepository.save(newValuationEntity);
		ValuationEntity getValuationEntity = valuationRepository.findByQuotationId(valuationDto.getQuotationId()).get();
		StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(salaryEscalationId).get();
		QuotationEntity quotationEntity = quotationRepository.findById(valuationDto.getQuotationId()).get();

		List<MemberWithdrawalEntity> memberWithdrawalEntities = memberWithdrawalRepository.findAll();
		List<ValuationWithdrawalRateEntity> valuationWithdrawalRateEntity = valuationWithdrawalRateRepository
				.findByQuotationId(valuationDto.getQuotationId());
		quotationEntity = ValuationHelper.checkStdValuationDeviated(getValuationEntity, standardCodeEntity,
				quotationEntity, memberWithdrawalEntities, valuationWithdrawalRateEntity);
		quotationRepository.save(quotationEntity);
		return ApiResponseDto.success(ValuationHelper.entityToDto(newValuationEntity));
	}

}
