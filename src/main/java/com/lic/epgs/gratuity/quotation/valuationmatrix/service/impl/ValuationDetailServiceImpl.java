package com.lic.epgs.gratuity.quotation.valuationmatrix.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.MemberWithdrawalEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.repository.MemberWithdrawalRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.quotation.entity.MasterQuotationEntity;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.member.repository.MemberRepository;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;
import com.lic.epgs.gratuity.quotation.repository.QuotationRepository;
import com.lic.epgs.gratuity.quotation.valuation.entity.ValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.helper.ValuationHelper;
import com.lic.epgs.gratuity.quotation.valuation.repository.ValuationRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationDetailDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationWithdrawalRateDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.helper.ValuationMatrixHelper;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.MasterValuationBasicRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.MasterValuationMatrixRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.MasterValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.ValuationBasicRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.ValuationMatrixRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.ValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.service.ValuationDetailService;

@Service
public class ValuationDetailServiceImpl implements ValuationDetailService {
	private Long salaryEscalationId = 2L;
	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private ValuationBasicRepository valuationBasicRepository;

	@Autowired
	private MasterValuationBasicRepository masterValuationBasicRepository;

	@Autowired
	private ValuationMatrixRepository valuationMatrixRepository;

	@Autowired
	private MasterValuationMatrixRepository masterValuationMatrixRepository;

	@Autowired
	private ValuationWithdrawalRateRepository valuationWithdrawalRateRepository;

	@Autowired
	private MasterValuationWithdrawalRateRepository masterValuationWithdrawalRateRepository;

	@Autowired
	private MemberWithdrawalRepository memberWithdrawalRepository;

	@Autowired
	private ValuationRepository valuationRepository;

	@Autowired
	private StandardCodeRepository standardCodeRepository;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Override
	public ApiResponseDto<ValuationDetailDto> findByQuotationId(Long quotationId, String type) {
		if (type.equals("INPROGRESS")) {
			Optional<ValuationBasicEntity> valuationBasicEntity = valuationBasicRepository
					.findByQuotationId(quotationId);
			Optional<ValuationMatrixEntity> valuationMatrixEntity = valuationMatrixRepository
					.findByQuotationId(quotationId);
			List<ValuationWithdrawalRateEntity> valuationWithdrawalRateEntities = valuationWithdrawalRateRepository
					.findByQuotationId(quotationId);
			if (valuationBasicEntity.isPresent()) {
				ValuationDetailDto valuationDetailDto = new ValuationDetailDto();
				valuationDetailDto.setValuatonBasicDto(
						ValuationMatrixHelper.valuationBasicEntityToDto(valuationBasicEntity.get()));
				valuationDetailDto.setValuationMatrixDto(
						ValuationMatrixHelper.valuationMatrixEntityToDto(valuationMatrixEntity.get()));
				valuationDetailDto.setValuationWithdrawalRatesDto(valuationWithdrawalRateEntities.stream()
						.map(ValuationMatrixHelper::valuationWithdrawalRateEntityToDto).collect(Collectors.toList()));
				return ApiResponseDto.success(valuationDetailDto);
			}
		} else {
			Optional<MasterValuationBasicEntity> valuationBasicEntity = masterValuationBasicRepository
					.findByQuotationId(quotationId);
			Optional<MasterValuationMatrixEntity> valuationMatrixEntity = masterValuationMatrixRepository
					.findByQuotationId(quotationId);
			List<MasterValuationWithdrawalRateEntity> valuationWithdrawalRateEntities = masterValuationWithdrawalRateRepository
					.findByQuotationId(quotationId);
			if (valuationBasicEntity.isPresent()) {
				ValuationDetailDto valuationDetailDto = new ValuationDetailDto();
				valuationDetailDto.setValuatonBasicDto(
						ValuationMatrixHelper.valuationBasicEntityToDto(valuationBasicEntity.get()));
				valuationDetailDto.setValuationMatrixDto(
						ValuationMatrixHelper.valuationMatrixEntityToDto(valuationMatrixEntity.get()));
				valuationDetailDto.setValuationWithdrawalRatesDto(valuationWithdrawalRateEntities.stream()
						.map(ValuationMatrixHelper::valuationWithdrawalRateEntityToDto).collect(Collectors.toList()));
				return ApiResponseDto.success(valuationDetailDto);
			}
		}
		return ApiResponseDto.notFound(new ValuationDetailDto());
	}

	@Override
	public ApiResponseDto<ValuationMatrixDto> findMatrixByQuotationId(Long quotationId, String type) {
		if (type.equals("INPROGRESS")) {
			Optional<ValuationMatrixEntity> valuationMatrixEntity = valuationMatrixRepository
					.findByQuotationId(quotationId);
			if (valuationMatrixEntity.isPresent()) {
				return ApiResponseDto
						.success(ValuationMatrixHelper.valuationMatrixEntityToDto(valuationMatrixEntity.get()));
			} else {
				return ApiResponseDto.notFound(new ValuationMatrixDto());
			}
		} else {
			Optional<MasterValuationMatrixEntity> valuationMatrixEntity = masterValuationMatrixRepository
					.findByQuotationId(quotationId);
			if (valuationMatrixEntity.isPresent()) {
				return ApiResponseDto
						.success(ValuationMatrixHelper.valuationMatrixEntityToDto(valuationMatrixEntity.get()));
			} else {
				return ApiResponseDto.notFound(new ValuationMatrixDto());
			}
		}
	}

	@Transactional
	@Override
	public ApiResponseDto<ValuationDetailDto> create(ValuationDetailDto valuationDetailDto) {

		ValuationBasicEntity valuationBasicEntity = ValuationMatrixHelper
				.dtoToEntity1(valuationDetailDto.getValuatonBasicDto());
		ValuationMatrixEntity valuationMatrixEntity = new ValuationMatrixEntity();
		if (valuationDetailDto.getValuationMatrixDto() != null) {
			valuationMatrixEntity = ValuationMatrixHelper.dtoToEntity2(valuationDetailDto.getValuationMatrixDto());
		}
		List<ValuationWithdrawalRateEntity> valuationWithdrawalRateEntities = new ArrayList<ValuationWithdrawalRateEntity>();
		if (valuationDetailDto.getValuationWithdrawalRatesDto() != null) {
			valuationWithdrawalRateEntities = valuationDetailDto.getValuationWithdrawalRatesDto().stream()
					.map(this::valuationWithdrawalRateDtoToEntityForCreate).collect(Collectors.toList());
		}
		valuationBasicEntity
				.setNumberOfLives(memberRepository.numberMemberCount(valuationBasicEntity.getQuotationId()));
		valuationBasicEntity.setReferenceNumber(
				ValuationMatrixHelper.nextReferenceNumber(valuationBasicRepository.maxReferenceNumber()).toString());
		valuationBasicEntity.setIsActive(true);
		valuationBasicEntity.setCreatedBy(valuationDetailDto.getCreatedBy());
		valuationBasicEntity.setCreatedDate(new Date());

		valuationMatrixEntity.setIsActive(true);
		valuationMatrixEntity.setCreatedBy(valuationDetailDto.getCreatedBy());
		valuationMatrixEntity.setCreatedDate(new Date());

		valuationBasicRepository.save(valuationBasicEntity);
		valuationMatrixRepository.save(valuationMatrixEntity);
		valuationWithdrawalRateRepository.saveAll(valuationWithdrawalRateEntities);
		// check deviation by quotation id
		ValuationEntity getValuationEntity = valuationRepository
				.findByQuotationId(valuationBasicEntity.getQuotationId()).get();
		StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(salaryEscalationId).get();
		QuotationEntity quotationEntity = quotationRepository.findById(valuationBasicEntity.getQuotationId()).get();

		List<MemberWithdrawalEntity> memberWithdrawalEntities = memberWithdrawalRepository.findAll();
		List<ValuationWithdrawalRateEntity> valuationWithdrawalRateEntity = valuationWithdrawalRateRepository
				.findByQuotationId(valuationBasicEntity.getQuotationId());
		quotationEntity = ValuationHelper.checkStdValuationDeviated(getValuationEntity, standardCodeEntity,
				quotationEntity, memberWithdrawalEntities, valuationWithdrawalRateEntity);

		quotationRepository.save(quotationEntity);
		return this.findByQuotationId(valuationBasicEntity.getQuotationId(), "INPROGRESS");

	}

	@Override
	public ApiResponseDto<ValuationMatrixDto> createMatrix(ValuationMatrixDto valuationMatrixDto) {
		ValuationMatrixEntity valuationMatrixEntity = ValuationMatrixHelper.dtoToEntity2(valuationMatrixDto);

		valuationMatrixEntity.setIsActive(true);
		valuationMatrixEntity.setCreatedBy(valuationMatrixDto.getCreatedBy());
		valuationMatrixEntity.setCreatedDate(new Date());

		return ApiResponseDto.created(ValuationMatrixHelper
				.valuationMatrixEntityToDto(valuationMatrixRepository.save(valuationMatrixEntity)));
	}

	@Transactional
	@Override
	public ApiResponseDto<ValuationDetailDto> update(Long quotationId, ValuationDetailDto valuationDetailDto) {
		ValuationBasicEntity valuationBasicEntity = valuationBasicRepository.findByQuotationId(quotationId).get();
		ValuationMatrixEntity valuationMatrixEntity = valuationMatrixRepository.findByQuotationId(quotationId).get();

		ValuationBasicEntity newValuationBasicEntity = ValuationMatrixHelper
				.dtoToEntity1(valuationDetailDto.getValuatonBasicDto());
		ValuationMatrixEntity newValuationMatrixEntity = ValuationMatrixHelper
				.dtoToEntity2(valuationDetailDto.getValuationMatrixDto());

		if ((valuationBasicEntity.getMinimumServiceForDeath() != newValuationBasicEntity.getMinimumServiceForDeath())
				&& (valuationBasicEntity.getMinimumServiceForWithdrawal() != newValuationBasicEntity
						.getMinimumServiceForWithdrawal())
				&& (valuationBasicEntity.getMinimumServiceForRetirement() != newValuationBasicEntity
						.getMinimumServiceForRetirement())
				&& (valuationBasicEntity.getRateTable() != newValuationBasicEntity.getRateTable())) {
			gratuityCalculationRepository.updateDeActiveQuotationId(quotationId);
		}

		newValuationBasicEntity.setId(valuationBasicEntity.getId());
		newValuationBasicEntity.setIsActive(valuationBasicEntity.getIsActive());
		newValuationBasicEntity.setCreatedBy(valuationBasicEntity.getCreatedBy());
		newValuationBasicEntity.setCreatedDate(valuationBasicEntity.getCreatedDate());
		newValuationBasicEntity.setModifiedBy(valuationDetailDto.getModifiedBy());
		newValuationBasicEntity.setModifiedDate(new Date());

		newValuationMatrixEntity.setId(valuationMatrixEntity.getId());
		newValuationMatrixEntity.setIsActive(valuationMatrixEntity.getIsActive());
		newValuationMatrixEntity.setCreatedBy(valuationMatrixEntity.getCreatedBy());
		newValuationMatrixEntity.setCreatedDate(valuationMatrixEntity.getCreatedDate());
		newValuationMatrixEntity.setModifiedBy(valuationDetailDto.getModifiedBy());
		newValuationMatrixEntity.setModifiedDate(new Date());
//for changes in old record inactve in grat
		List<ValuationWithdrawalRateEntity> oldValuationWithdrawal = valuationWithdrawalRateRepository.findByQuotationId(quotationId);	
		boolean tt = false;
		for(ValuationWithdrawalRateEntity getold:oldValuationWithdrawal) {
			for(ValuationWithdrawalRateDto getNewfromDto :valuationDetailDto.getValuationWithdrawalRatesDto()) {
				if(getold.getFromAgeBand() !=getNewfromDto.getFromAgeBand() && getold.getToAgeBand() != getNewfromDto.getToAgeBand()
						&& getold.getRate() != getNewfromDto.getRate() ) {
					gratuityCalculationRepository.updateDeActiveQuotationId(quotationId);
					tt = true;
				}
				if (tt) break;
			}
			if (tt) break;
		}
		//end
		List<ValuationWithdrawalRateEntity> oldList = valuationWithdrawalRateRepository
				.deleteByquotationId(quotationId);
		List<ValuationWithdrawalRateEntity> valuationWithdrawalRateEntities = new ArrayList<ValuationWithdrawalRateEntity>();
		if (valuationDetailDto.getValuationWithdrawalRatesDto() != null) {
			valuationWithdrawalRateEntities = valuationDetailDto.getValuationWithdrawalRatesDto().stream()
					.map(this::valuationWithdrawalRateDtoToEntityForCreate).collect(Collectors.toList());
		}
		valuationWithdrawalRateRepository.saveAll(valuationWithdrawalRateEntities);
		valuationBasicRepository.save(newValuationBasicEntity);
		valuationMatrixRepository.save(newValuationMatrixEntity);
		ValuationEntity getValuationEntity = valuationRepository
				.findByQuotationId(valuationBasicEntity.getQuotationId()).get();
		StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(salaryEscalationId).get();
		QuotationEntity quotationEntity = quotationRepository.findById(valuationBasicEntity.getQuotationId()).get();

		List<MemberWithdrawalEntity> memberWithdrawalEntities = memberWithdrawalRepository.findAll();
		List<ValuationWithdrawalRateEntity> valuationWithdrawalRateEntity = valuationWithdrawalRateRepository
				.findByQuotationId(valuationBasicEntity.getQuotationId());
		quotationEntity = ValuationHelper.checkStdValuationDeviated(getValuationEntity, standardCodeEntity,
				quotationEntity, memberWithdrawalEntities, valuationWithdrawalRateEntity);

		quotationRepository.save(quotationEntity);

		return this.findByQuotationId(newValuationBasicEntity.getQuotationId(), "INPROGRESS");
	}

	@Override
	public ApiResponseDto<ValuationMatrixDto> updateMatrix(ValuationMatrixDto valuationMatrixDto) {
		ValuationMatrixEntity valuationMatrixEntity = valuationMatrixRepository
				.findByQuotationId(valuationMatrixDto.getQuotationId()).get();
		ValuationMatrixEntity newValuationMatrixEntity = ValuationMatrixHelper.dtoToEntity2(valuationMatrixDto);

		newValuationMatrixEntity.setId(valuationMatrixEntity.getId());
		newValuationMatrixEntity.setIsActive(valuationMatrixEntity.getIsActive());
		newValuationMatrixEntity.setCreatedBy(valuationMatrixEntity.getCreatedBy());
		newValuationMatrixEntity.setCreatedDate(valuationMatrixEntity.getCreatedDate());
		newValuationMatrixEntity.setModifiedBy(valuationMatrixDto.getModifiedBy());
		newValuationMatrixEntity.setModifiedDate(new Date());

		return ApiResponseDto.success(ValuationMatrixHelper
				.valuationMatrixEntityToDto(valuationMatrixRepository.save(newValuationMatrixEntity)));
	}

	private ValuationWithdrawalRateEntity valuationWithdrawalRateDtoToEntityForCreate(ValuationWithdrawalRateDto dto) {
		ValuationWithdrawalRateEntity valuationWithdrawalRateEntity = new ModelMapper().map(dto,
				ValuationWithdrawalRateEntity.class);

		valuationWithdrawalRateEntity.setIsActive(true);
		valuationWithdrawalRateEntity.setCreatedBy(dto.getCreatedBy());
		valuationWithdrawalRateEntity.setCreatedDate(new Date());

		return valuationWithdrawalRateEntity;
	}

	private ValuationWithdrawalRateEntity updateValuationWithdrawalRate(ValuationWithdrawalRateDto dto) {
		ValuationWithdrawalRateEntity valuationWithdrawalRateEntity = new ModelMapper().map(dto,
				ValuationWithdrawalRateEntity.class);

		if (valuationWithdrawalRateEntity.getId() == null) {
			valuationWithdrawalRateEntity.setIsActive(true);
			valuationWithdrawalRateEntity.setCreatedBy(dto.getCreatedBy());
			valuationWithdrawalRateEntity.setCreatedDate(new Date());

			valuationWithdrawalRateRepository.save(valuationWithdrawalRateEntity);
		} else {
			Optional<ValuationWithdrawalRateEntity> oldValuationWithdrawalRateEntity = valuationWithdrawalRateRepository
					.findById(valuationWithdrawalRateEntity.getId());

			valuationWithdrawalRateEntity.setIsActive(oldValuationWithdrawalRateEntity.get().getIsActive());
			valuationWithdrawalRateEntity.setCreatedBy(oldValuationWithdrawalRateEntity.get().getCreatedBy());
			valuationWithdrawalRateEntity.setCreatedDate(oldValuationWithdrawalRateEntity.get().getCreatedDate());
			valuationWithdrawalRateEntity.setModifiedBy(dto.getModifiedBy());
			valuationWithdrawalRateEntity.setModifiedDate(new Date());

			valuationWithdrawalRateRepository.save(valuationWithdrawalRateEntity);
		}

		return valuationWithdrawalRateEntity;
	}

	
	//BY 
	//WHY 
	//INOUT
	//OUT
	//POST api :QUOTAION ID 
	
	@Autowired
	private GratuityCalculationRepository gratuityCalculationRepository;
	
	@Override
	public ApiResponseDto<String> statusforvaluationMatrix(Long quotationId) {
      String status = "";
		
     int get=gratuityCalculationRepository.findByQuotationId(quotationId);
		
		System.out.println(get);
		if(get >0) {
			status = "";
		}else {
			status="Please generate valuation";
		}
		return ApiResponseDto.success(status);    
	}

	
	
	
	
	
	
	
	
	
}
