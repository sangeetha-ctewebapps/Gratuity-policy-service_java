package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.MemberWithdrawalEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.entity.RenewalValuationTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.repository.RenewalValuationTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationDetailDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationMatrixDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationWithdrawalTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationWithdrawalTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.helper.RenewalValuationMatrixTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.helper.RenewalValuationTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationBasicTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationMatrixTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationWithdrawalTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.service.RenewalValuationMatrixService;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.valuation.entity.ValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.helper.ValuationHelper;
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

@Service
public class RenewalValuationMatrixServiceImpl implements RenewalValuationMatrixService {

	@Autowired
	private RenewalValuationMatrixTMPRepository renewalValuationMatrixTMPRepository;

	@Autowired
	private RenewalValuationBasicTMPRepository renewalValuationBasicTMPRepository;

	@Autowired
	private RenewalValuationWithdrawalTMPRepository renewalValuationWithdrawalTMPRepository;

	@Autowired
	private RenewalPolicyTMPMemberRepository renewalPolicyTMPMemberRepository;
	
	@Autowired
	private RenewalValuationTMPRepository  renewalValuationTMPRepository;
	@Override
	public ApiResponseDto<RenewalValuationDetailDto> findBypolicyId(Long policyId, String type) {
		if (type.equals("INPROGRESS")) {
			Optional<RenewalValuationBasicTMPEntity> renewalValuationBasicTMPEntity = renewalValuationBasicTMPRepository
					.findBytmpPolicyId(policyId);
			Optional<RenewalValuationMatrixTMPEntity> renewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
					.findBytmpPolicyId(policyId);
			List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = renewalValuationWithdrawalTMPRepository
					.findBytmpPolicyId(policyId);
			if (renewalValuationBasicTMPEntity.isPresent()) {
				RenewalValuationDetailDto renewalValuationDetailDto = new RenewalValuationDetailDto();
				renewalValuationDetailDto.setRenewalValuationBasicTMPDto(RenewalValuationTMPHelper
						.renewalValuationBasicEntityToDto(renewalValuationBasicTMPEntity.get()));
				renewalValuationDetailDto.setRenewalValuationMatrixDto(RenewalValuationTMPHelper
						.renewalValuationMatrixEntityToDto(renewalValuationMatrixTMPEntity.get()));
				renewalValuationDetailDto.setRenewalValuationWithdrawalTMPDto(renewalValuationWithdrawalTMPEntity
						.stream().map(RenewalValuationTMPHelper::renewalValuationWithdrawalRateEntityToDto)
						.collect(Collectors.toList()));
				return ApiResponseDto.success(renewalValuationDetailDto);
			}
		} else {

			return ApiResponseDto.success(null);
		}

		return ApiResponseDto.notFound(new RenewalValuationDetailDto());
	}
	
	
	@Transactional
	@Override
	public ApiResponseDto<RenewalValuationDetailDto> RenewalValuationUpdate(Long policyId,
			RenewalValuationDetailDto renewalValuationDetailDto) {
		RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = renewalValuationBasicTMPRepository
				.findBytmpPolicyId(policyId).get();
		RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
				.findBytmpPolicyId(policyId).get();

		RenewalValuationBasicTMPEntity newRenewalValuationBasicTMPEntity = RenewalValuationTMPHelper
				.dtoToEntity1(renewalValuationDetailDto.getRenewalValuationBasicTMPDto());
		RenewalValuationMatrixTMPEntity newRenewalValuationMatrixTMPEntity = RenewalValuationTMPHelper
				.dtoToEntity2(renewalValuationDetailDto.getRenewalValuationMatrixDto());

		newRenewalValuationBasicTMPEntity.setId(renewalValuationBasicTMPEntity.getId());
		newRenewalValuationBasicTMPEntity.setTmpPolicyId(renewalValuationBasicTMPEntity.getTmpPolicyId());		
		newRenewalValuationBasicTMPEntity.setIsActive(renewalValuationBasicTMPEntity.getIsActive());
		newRenewalValuationBasicTMPEntity.setCreatedBy(renewalValuationBasicTMPEntity.getCreatedBy());
		newRenewalValuationBasicTMPEntity.setCreatedDate(renewalValuationBasicTMPEntity.getCreatedDate());
		newRenewalValuationBasicTMPEntity.setModifiedBy(renewalValuationDetailDto.getModifiedBy());
		newRenewalValuationBasicTMPEntity.setModifiedDate(new Date());
		newRenewalValuationBasicTMPEntity.setPolicyId(renewalValuationBasicTMPEntity.getPolicyId());
		newRenewalValuationBasicTMPEntity.setPmstValuationBasicId(renewalValuationBasicTMPEntity.getPmstValuationBasicId());

		newRenewalValuationMatrixTMPEntity.setId(renewalValuationMatrixTMPEntity.getId());
		newRenewalValuationMatrixTMPEntity.setTmpPolicyId(renewalValuationMatrixTMPEntity.getTmpPolicyId());
		newRenewalValuationMatrixTMPEntity.setIsActive(renewalValuationMatrixTMPEntity.getIsActive());
		newRenewalValuationMatrixTMPEntity.setCreatedBy(renewalValuationMatrixTMPEntity.getCreatedBy());
		newRenewalValuationMatrixTMPEntity.setCreatedDate(renewalValuationMatrixTMPEntity.getCreatedDate());
		newRenewalValuationMatrixTMPEntity.setModifiedBy(renewalValuationDetailDto.getModifiedBy());
		newRenewalValuationMatrixTMPEntity.setModifiedDate(new Date());
		newRenewalValuationMatrixTMPEntity.setPolicyId(renewalValuationMatrixTMPEntity.getPolicyId());
		newRenewalValuationMatrixTMPEntity.setPmstValuationMatrixId(renewalValuationMatrixTMPEntity.getPmstValuationMatrixId());
		
		List<RenewalValuationWithdrawalTMPEntity> oldList = renewalValuationWithdrawalTMPRepository
				.deleteBytmpPolicyId(policyId);
		List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = new ArrayList<RenewalValuationWithdrawalTMPEntity>();
		if (renewalValuationDetailDto.getRenewalValuationWithdrawalTMPDto() != null) {
			renewalValuationWithdrawalTMPEntity = renewalValuationDetailDto.getRenewalValuationWithdrawalTMPDto()
					.stream().map(this::valuationWithdrawalRateDtoToEntityForCreate).collect(Collectors.toList());
		}
		renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);
		renewalValuationBasicTMPRepository.save(newRenewalValuationBasicTMPEntity);
		renewalValuationMatrixTMPRepository.save(newRenewalValuationMatrixTMPEntity);
		return this.findBypolicyId(newRenewalValuationBasicTMPEntity.getTmpPolicyId(), "INPROGRESS");
	}

	
	

	private RenewalValuationWithdrawalTMPEntity valuationWithdrawalRateDtoToEntityForCreate(
			RenewalValuationWithdrawalTMPDto dto) {
		RenewalValuationWithdrawalTMPEntity renewalValuationWithdrawalTMPEntity = new ModelMapper().map(dto,
				RenewalValuationWithdrawalTMPEntity.class);

		renewalValuationWithdrawalTMPEntity.setIsActive(true);
		renewalValuationWithdrawalTMPEntity.setCreatedBy(dto.getCreatedBy());
		renewalValuationWithdrawalTMPEntity.setCreatedDate(new Date());

		return renewalValuationWithdrawalTMPEntity;
	}

	@Override
	public ApiResponseDto<RenewalValuationMatrixDto> create(RenewalValuationMatrixDto renewalValuationMatrixDto) {

		RenewalValuationMatrixTMPEntity dtoToEntity = RenewalValuationMatrixTMPHelper
				.dtoToEntity(renewalValuationMatrixDto);
         
		
		dtoToEntity.setTmpPolicyId(renewalValuationMatrixDto.getTmpPolicyId());
		dtoToEntity.setIsActive(true);
		dtoToEntity.setCreatedBy(renewalValuationMatrixDto.getCreatedBy());
		dtoToEntity.setCreatedDate(new Date());

		return ApiResponseDto.created(RenewalValuationMatrixTMPHelper
				.RenewalValuationMatrixTMPEntitytoDto(renewalValuationMatrixTMPRepository.save(dtoToEntity)));

	}

	@Override
	public ApiResponseDto<RenewalValuationMatrixDto> findMatrixTemPolicyId(Long tmpPolicyId, String type) {

		if (type.equals("INPROGRESS")) {

			 Optional<RenewalValuationMatrixTMPEntity> renewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository.findBytmpPolicyId(tmpPolicyId);
					

			if (renewalValuationMatrixTMPEntity.isPresent()) {
				return ApiResponseDto.success(RenewalValuationMatrixTMPHelper.RenewalValuationMatrixEntityToDto(renewalValuationMatrixTMPEntity.get()));
			} else {
				return ApiResponseDto.notFound(new RenewalValuationMatrixDto());
			}

		}
		return null;

	}

	
	@Override
	public ApiResponseDto<RenewalValuationMatrixDto> updateRenewalvalutionMatrixTemPolicyId(Long tmpPolicyId,
			RenewalValuationMatrixDto renewalValuationMatrixDto) {
	
		
		RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository.findBytmpPolicyId(tmpPolicyId).get();
		
		renewalValuationMatrixTMPEntity.setAmountPayable(renewalValuationMatrixDto.getAmountPayable());
		renewalValuationMatrixTMPEntity.setAmountReceived(renewalValuationMatrixDto.getAmountReceived());
		renewalValuationMatrixTMPEntity.setBalanceToBePaid(renewalValuationMatrixDto.getBalanceToBePaid());
		renewalValuationMatrixTMPEntity.setCreatedBy(renewalValuationMatrixDto.getCreatedBy());
		renewalValuationMatrixTMPEntity.setCreatedDate(renewalValuationMatrixDto.getCreatedDate());
		renewalValuationMatrixTMPEntity.setFutureServiceLiability(renewalValuationMatrixDto.getFutureServiceLiability());
		renewalValuationMatrixTMPEntity.setGst(renewalValuationMatrixDto.getGst());
		//renewalValuationMatrixTMPEntity.setIsActive(renewalValuationMatrixDto.getI);
		renewalValuationMatrixTMPEntity.setModifiedBy(renewalValuationMatrixDto.getModifiedBy());
		renewalValuationMatrixTMPEntity.setModifiedDate(renewalValuationMatrixDto.getModifiedDate());
		renewalValuationMatrixTMPEntity.setPastServiceLiability(renewalValuationMatrixDto.getPastServiceLiability());
		
		renewalValuationMatrixTMPEntity.setPmstValuationMatrixId(renewalValuationMatrixDto.getPmstValuationMatrixId());
		renewalValuationMatrixTMPEntity.setPremium(renewalValuationMatrixDto.getPremium());
		//renewalValuationMatrixTMPEntity.setTmpPolicyId(renewalValuationMatrixDto.g);
		renewalValuationMatrixTMPEntity.setTotalPremium(renewalValuationMatrixDto.getTotalPremium());
		renewalValuationMatrixTMPEntity.setTotalSumAssured(renewalValuationMatrixDto.getTotalSumAssured());
		renewalValuationMatrixTMPEntity.setValuationDate(renewalValuationMatrixDto.getValuationDate());
		renewalValuationMatrixTMPEntity.setValuationTypeId(renewalValuationMatrixDto.getValuationTypeId());
		
		
		renewalValuationMatrixTMPEntity.setTotalPremium(renewalValuationMatrixDto.getTotalPremium());
		renewalValuationMatrixTMPEntity.setCurrentServiceLiability(renewalValuationMatrixDto.getCurrentServiceLiability());
		
		
		
		
		renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);
		
		RenewalValuationMatrixDto renewalValuationMatrixEntityToDto = RenewalValuationMatrixTMPHelper.RenewalValuationMatrixEntityToDto(renewalValuationMatrixTMPEntity);
		
		return ApiResponseDto.success(renewalValuationMatrixEntityToDto);
	}

	@Transactional
	@Override
	public ApiResponseDto<RenewalValuationDetailDto> renewalvaluationcreate(
			RenewalValuationDetailDto renewalValuationDetailDto) {
		RenewalValuationBasicTMPEntity newRenewalValuationBasicTMPEntity = RenewalValuationTMPHelper
				.dtoToEntity1(renewalValuationDetailDto.getRenewalValuationBasicTMPDto());
		
		RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = new RenewalValuationMatrixTMPEntity();
		if (renewalValuationDetailDto.getRenewalValuationMatrixDto() != null) {
			renewalValuationMatrixTMPEntity =RenewalValuationTMPHelper
					.dtoToEntity2(renewalValuationDetailDto.getRenewalValuationMatrixDto());
		}
		
		List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalRateEntities = new ArrayList<RenewalValuationWithdrawalTMPEntity>();
		if (renewalValuationDetailDto.getRenewalValuationWithdrawalTMPDto() != null) {
			renewalValuationWithdrawalRateEntities = renewalValuationDetailDto.getRenewalValuationWithdrawalTMPDto()
				.stream().map(this::valuationWithdrawalRateDtoToEntityForCreate)
				.collect(Collectors.toList());
		}
		newRenewalValuationBasicTMPEntity.setNumberOfLives(renewalPolicyTMPMemberRepository.numberMemberCount(newRenewalValuationBasicTMPEntity.getTmpPolicyId()).intValue());
		newRenewalValuationBasicTMPEntity.setReferenceNumber(ValuationMatrixHelper
				.nextReferenceNumber(renewalValuationBasicTMPRepository.maxReferenceNumber()).toString());
		newRenewalValuationBasicTMPEntity.setIsActive(true);
		newRenewalValuationBasicTMPEntity.setCreatedBy(renewalValuationDetailDto.getCreatedBy());
		newRenewalValuationBasicTMPEntity.setCreatedDate(new Date());
		
		renewalValuationMatrixTMPEntity.setIsActive(true);
		renewalValuationMatrixTMPEntity.setCreatedBy(renewalValuationDetailDto.getCreatedBy());
		renewalValuationMatrixTMPEntity.setCreatedDate(new Date());
		
		renewalValuationBasicTMPRepository.save(newRenewalValuationBasicTMPEntity);
		renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);
		renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalRateEntities);
		//check deviation by quotation id
		RenewalValuationTMPEntity getValuationEntity = renewalValuationTMPRepository.findBytmpPolicyId(newRenewalValuationBasicTMPEntity.getTmpPolicyId());
//		StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(salaryEscalationId).get();
//		QuotationEntity quotationEntity = quotationRepository.findById(valuationBasicEntity.getQuotationId()).get();
//
//		List<MemberWithdrawalEntity> memberWithdrawalEntities  =memberWithdrawalRepository.findAll();
//		List<ValuationWithdrawalRateEntity> valuationWithdrawalRateEntity=valuationWithdrawalRateRepository.findByQuotationId(valuationBasicEntity.getQuotationId());
//		quotationEntity=ValuationHelper.checkStdValuationDeviated(getValuationEntity,standardCodeEntity,quotationEntity,memberWithdrawalEntities
//				,valuationWithdrawalRateEntity);
//		
//		quotationRepository.save(quotationEntity);
		return this.findBypolicyId(newRenewalValuationBasicTMPEntity.getTmpPolicyId(), "INPROGRESS");
	}

}
