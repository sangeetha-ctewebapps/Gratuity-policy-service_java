package com.lic.epgs.gratuity.quotation.premium.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.PickListDto;
import com.lic.epgs.gratuity.common.entity.PickListItemEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.mph.helper.MPHHelper;
import com.lic.epgs.gratuity.quotation.entity.MasterQuotationEntity;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.member.dto.MemberDto;
import com.lic.epgs.gratuity.quotation.member.entity.MemberEntity;
import com.lic.epgs.gratuity.quotation.member.repository.MemberRepository;
import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;
import com.lic.epgs.gratuity.quotation.premium.dto.KeyValuePairDto;
import com.lic.epgs.gratuity.quotation.premium.dto.PremiumDto;
import com.lic.epgs.gratuity.quotation.premium.dto.RateTableSelectionDto;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;
import com.lic.epgs.gratuity.quotation.premium.entity.OyrgtavaluesEntity;
import com.lic.epgs.gratuity.quotation.premium.entity.PremiumEntity;
import com.lic.epgs.gratuity.quotation.premium.entity.RateTableSelectionEntity;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;
import com.lic.epgs.gratuity.quotation.premium.repository.OyrgtavaluesRepository;
import com.lic.epgs.gratuity.quotation.premium.repository.PremiumRepository;
import com.lic.epgs.gratuity.quotation.premium.repository.RateTableSelectionRepository;
import com.lic.epgs.gratuity.quotation.premium.service.PremiumService;
import com.lic.epgs.gratuity.quotation.repository.QuotationRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationBasicDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationDetailDto;
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

/**
 * @author Ismail Khan A
 *
 */

@Service
public class PremiumServiceImpl implements PremiumService {

	@Autowired
	private PremiumRepository premiumRepository;

	@Autowired
	private OyrgtavaluesRepository oyrgtavaluesRepository;

	@Autowired
	private GratuityCalculationRepository gratuityCalculationRepository;

	@Autowired
	ValuationMatrixRepository valuationMatrixRepository;

	@Autowired
	private ValuationBasicRepository valuationBasicRepository;

	@Autowired
	private com.lic.epgs.gratuity.common.repository.StandardCodeRepository standardCodeRepository;

	@Autowired
	private QuotationRepository quotationRepository;
	
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private MasterValuationBasicRepository masterValuationBasicRepository;

	@Autowired
	private MasterValuationMatrixRepository masterValuationMatrixRepository;

	@Autowired
	private ValuationWithdrawalRateRepository valuationWithdrawalRateRepository;

	@Autowired
	private MasterValuationWithdrawalRateRepository masterValuationWithdrawalRateRepository;

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private RateTableSelectionRepository rateTableSelectionRepository;

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	@Override
	public ApiResponseDto<List<PremiumDto>> findByQuotationId(Long quotationId) {
		List<PremiumEntity> entities = premiumRepository.findByQuotationId(quotationId);
		if (entities.isEmpty()) {
			return ApiResponseDto.notFound(Collections.emptyList());
		} else {
			return ApiResponseDto.success(entities.stream().map(this::entityToDto).collect(Collectors.toList()));
		}
	}

	@Override
	public ApiResponseDto<List<PremiumDto>> create(PremiumDto premiumDto) {

		PremiumEntity premiumEntity = dtoToEntity(premiumDto);

		premiumEntity.setIsActive(true);
		premiumEntity.setCreatedBy(premiumDto.getCreatedBy());
		premiumEntity.setCreatedDate(new Date());

		premiumRepository.save(premiumEntity);

		return ApiResponseDto.success(premiumRepository.findByQuotationId(premiumEntity.getQuotationId()).stream()
				.map(this::entityToDto).collect(Collectors.toList()));

	}

	@Transactional
	@Override
	public ApiResponseDto<ValuationDetailDto> calculateGratuity(Long quotationId, String username) {
		//quotarionId/"Quotation"/''/'X04'/

		

		ValuationBasicEntity valuationBasicEntity = valuationBasicRepository.findByQuotationId(quotationId).get();
//		if(isDevEnvironment ==false) {
		
		
		gratuityCalculationRepository.calculateGratuity11(quotationId,"QUOTATION","TABULAR",valuationBasicEntity.getRateTable());
		gratuityCalculationRepository.calculateGratuity21(quotationId,"QUOTATION","TABULAR",valuationBasicEntity.getRateTable());
		gratuityCalculationRepository.calculateGratuity31(quotationId,"QUOTATION","TABULAR",valuationBasicEntity.getRateTable());
		gratuityCalculationRepository.calculateGratuity41(quotationId,"QUOTATION","TABULAR",valuationBasicEntity.getRateTable());
		gratuityCalculationRepository.calculateGratuity51(quotationId,"QUOTATION","TABULAR",valuationBasicEntity.getRateTable());
//		}else {
//			
//	
//		
//			gratuityCalculationRepository.calculateGratuity1(quotationId);
//			gratuityCalculationRepository.calculateGratuity2(quotationId);
//			gratuityCalculationRepository.calculateGratuity3(quotationId);
//			gratuityCalculationRepository.calculateGratuity4(quotationId);
//			gratuityCalculationRepository.calculateGratuity5(quotationId);
//		}
		List<GratuityCalculationEntity> gratuityCalculationEntities = gratuityCalculationRepository
				.findAllByQuotationId(quotationId);

		QuotationEntity quotationEntity = quotationRepository.findById(quotationId).get();
		
		
		Optional<ValuationMatrixEntity> valuationMatrixEntity = valuationMatrixRepository
				.findByQuotationId(quotationId);
		ValuationMatrixEntity newValuationMatrixEntity = null;
		if (valuationMatrixEntity.isPresent()) {
			newValuationMatrixEntity = valuationMatrixEntity.get();
			newValuationMatrixEntity.setModifiedBy(username);
			newValuationMatrixEntity.setModifiedDate(new Date());
		} else {
			newValuationMatrixEntity = new ValuationMatrixEntity();
			newValuationMatrixEntity.setCreatedBy(username);
			newValuationMatrixEntity.setCreatedDate(new Date());
		}
		// calculate Valuation basic
		Double accruedGratuity = 0.0D;
		Double pastServiceDeath = 0.0D;
		Double pastServiceWithdrawal = 0.0D;
		Double pastServiceRetirement = 0.0D;
		Double currentServiceDeath = 0.0D;
		Double currentServiceWithdrawal = 0.0D;
		Double currentServiceRetirement = 0.0D;
		Double totalGratuity = 0.0D;

		List<GratuityCalculationDto> getGratuityCalculationEntity = gratuityCalculationEntities.stream()
				.map(this::gratuityCalculationEntityToDto).collect(Collectors.toList());
		for (GratuityCalculationDto gratuityCalculationEntity : getGratuityCalculationEntity) {
			if (gratuityCalculationEntity.getAccruedGra() != null)
				accruedGratuity = accruedGratuity + gratuityCalculationEntity.getAccruedGra();
			if (gratuityCalculationEntity.getPastServiceBenefitDeath() != null)
				pastServiceDeath = pastServiceDeath + gratuityCalculationEntity.getPastServiceBenefitDeath();
			if (gratuityCalculationEntity.getPastServiceBenefitWdl() != null)
				pastServiceWithdrawal = pastServiceWithdrawal + gratuityCalculationEntity.getPastServiceBenefitWdl();
			if (gratuityCalculationEntity.getPastServiceBenefitRet() != null)
				pastServiceRetirement = pastServiceRetirement + gratuityCalculationEntity.getPastServiceBenefitRet();
			if (gratuityCalculationEntity.getCurrentServiceBenefitDeath() != null)
				currentServiceDeath = currentServiceDeath + gratuityCalculationEntity.getCurrentServiceBenefitDeath();
			if (gratuityCalculationEntity.getCurrentServiceBenefitWdl() != null)
				currentServiceWithdrawal = currentServiceWithdrawal
						+ gratuityCalculationEntity.getCurrentServiceBenefitWdl();
			if (gratuityCalculationEntity.getCurrentServiceBenefitRet() != null)
				currentServiceRetirement = currentServiceRetirement
						+ gratuityCalculationEntity.getCurrentServiceBenefitRet();
			if (gratuityCalculationEntity.getTotalGra() != null)
				totalGratuity = totalGratuity + gratuityCalculationEntity.getTotalGra();

		}
		valuationBasicEntity.setAccruedGratuity(accruedGratuity);
		valuationBasicEntity.setPastServiceDeath(pastServiceDeath);
		valuationBasicEntity.setPastServiceWithdrawal(pastServiceWithdrawal);
		valuationBasicEntity.setPastServiceRetirement(pastServiceRetirement);
		valuationBasicEntity.setCurrentServiceDeath(currentServiceDeath);
		valuationBasicEntity.setCurrentServiceWithdrawal(currentServiceWithdrawal);
		valuationBasicEntity.setCurrentServiceRetirement(currentServiceRetirement);
		valuationBasicEntity.setTotalGratuity(totalGratuity);
		ValuationBasicEntity newValuationBasicEntity = valuationBasicRepository.save(valuationBasicEntity);

		// end VALAUTION BASIC SET CALCULATED DATA

		Double currentServiceLiability = 0.0D;
		Double pastServiceLiability = 0.0D;
		Double futureServiceLiability = 0.0D;
		Double premium = 0.0D;
		Double totalSumAssured = 0.0D;
		Double gst = 0.0D;
		if (quotationEntity.getGstApplicableId().equals(1L)) {
			StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
			gst = Double.parseDouble(standardCodeEntity.getValue()) / 100;
		}
		List<MemberEntity> memberEntity = new ArrayList<MemberEntity>();
		for (GratuityCalculationEntity gratuityCalculationEntity : gratuityCalculationEntities) {
			MemberEntity getMemberEntity = memberRepository.findById(gratuityCalculationEntity.getMemberId())
					.get();
			totalSumAssured = totalSumAssured
					+ (gratuityCalculationEntity.getLcSumAssured() != null ? gratuityCalculationEntity.getLcSumAssured()
							: 0.0D);
			premium = premium
					+ (gratuityCalculationEntity.getLcPremium() != null ? gratuityCalculationEntity.getLcPremium()
							: 0.0D);
			currentServiceLiability = currentServiceLiability
					+ (gratuityCalculationEntity.getCurrentServiceBenefitDeath() != null
							? gratuityCalculationEntity.getCurrentServiceBenefitDeath()
							: 0.0D)
					+ (gratuityCalculationEntity.getCurrentServiceBenefitRet() != null
							? gratuityCalculationEntity.getCurrentServiceBenefitRet()
							: 0.0D)
					+ (gratuityCalculationEntity.getCurrentServiceBenefitWdl() != null
							? gratuityCalculationEntity.getCurrentServiceBenefitWdl()
							: 0.0D);

			pastServiceLiability = pastServiceLiability
					+ (gratuityCalculationEntity.getPastServiceBenefitDeath() != null
							? gratuityCalculationEntity.getPastServiceBenefitDeath()
							: 0.0D)
					+ (gratuityCalculationEntity.getPastServiceBenefitRet() != null
							? gratuityCalculationEntity.getPastServiceBenefitRet()
							: 0.0D)
					+ (gratuityCalculationEntity.getPastServiceBenefitWdl() != null
							? gratuityCalculationEntity.getPastServiceBenefitWdl()
							: 0.0D);

			getMemberEntity.setLifeCoverPremium(gratuityCalculationEntity.getLcPremium());
			getMemberEntity.setLifeCoverSumAssured(gratuityCalculationEntity.getLcSumAssured());
			getMemberEntity.setAccruedGratuity(gratuityCalculationEntity.getAccruedGra());
			getMemberEntity.setTotalGratuity(gratuityCalculationEntity.getTotalGra());
			getMemberEntity.setGratCalculationId(gratuityCalculationEntity.getId());
			memberRepository.save(getMemberEntity);
			;
		}

		newValuationMatrixEntity.setValuationTypeId(1L);
		newValuationMatrixEntity.setValuationDate(valuationBasicEntity.getValuationEffectivDate());
		newValuationMatrixEntity.setCurrentServiceLiability(currentServiceLiability);
		newValuationMatrixEntity.setPastServiceLiability(pastServiceLiability);
		newValuationMatrixEntity.setFutureServiceLiability(futureServiceLiability);
		newValuationMatrixEntity.setPremium(premium);
		newValuationMatrixEntity.setGst(premium * gst);
		newValuationMatrixEntity.setTotalPremium(premium.doubleValue() + (premium * gst));
		newValuationMatrixEntity.setAmountPayable(
				premium + (premium * gst) + currentServiceLiability + pastServiceLiability + futureServiceLiability);
		newValuationMatrixEntity.setAmountReceived(0.0D);
		newValuationMatrixEntity.setBalanceToBePaid(
				newValuationMatrixEntity.getAmountPayable() - newValuationMatrixEntity.getAmountReceived());
		newValuationMatrixEntity.setTotalSumAssured(totalSumAssured.doubleValue());

		valuationMatrixRepository.save(newValuationMatrixEntity);

		return this.findByQuotationId(valuationBasicEntity.getQuotationId(), "INPROGRESS");
	}

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
	public ApiResponseDto<List<GratuityCalculationDto>> findAll(Long quotationId) {
		List<GratuityCalculationEntity> tt = gratuityCalculationRepository.findAllByQuotationId(quotationId);
		return ApiResponseDto
				.success(tt.stream().map(this::gratuityCalculationEntityToDto).collect(Collectors.toList()));
	}

	private PremiumDto entityToDto(PremiumEntity entity) {
		return new ModelMapper().map(entity, PremiumDto.class);
	}

	private PremiumEntity dtoToEntity(PremiumDto dto) {
		return new ModelMapper().map(dto, PremiumEntity.class);
	}

	private GratuityCalculationDto gratuityCalculationEntityToDto(GratuityCalculationEntity entity) {
		GratuityCalculationDto dto = new GratuityCalculationDto();

		dto.setId(entity.getId());
		dto.setMemberId(entity.getMemberId());
		dto.setTerm(entity.getTerm());
		dto.setG2Der(entity.getG2Der());
		dto.setDobDerYear(entity.getDobDerYear());
		dto.setDobDerAge(entity.getDobDerAge());
		dto.setDojDerYear(entity.getDojDerYear());
		dto.setPastService(entity.getPastService());
		dto.setPastServiceDeath(entity.getPastServiceDeath());
		dto.setPastServiceWdl(entity.getPastServiceWdl());
		dto.setPastServiceRet(entity.getPastServiceRet());
		dto.setRetDerYear(entity.getRetDerYear());
		dto.setTotalService(entity.getTotalService());
		dto.setGratuityF1(entity.getGratuityF1());
		dto.setBenefitsDeath(entity.getBenefitsDeath());
		dto.setBenefitsWdl(entity.getBenefitsWdl());
		dto.setBenefitsRet(entity.getBenefitsRet());
		dto.setCurrentServiceDeath(entity.getCurrentServiceDeath());
		dto.setCurrentServiceWdl(entity.getCurrentServiceWdl());
		dto.setCurrentServiceRet(entity.getCurrentServiceRet());
		dto.setBeneCurrentServiceDeath(entity.getBeneCurrentServiceDeath());
		dto.setBeneCurrentServiceWith(entity.getBeneCurrentServiceWith());
		dto.setBeneCurrentServiceRet(entity.getBeneCurrentServiceRet());
		dto.setPastServiceBenefitDeath(entity.getPastServiceBenefitDeath());
		dto.setPastServiceBenefitWdl(entity.getPastServiceBenefitWdl());
		dto.setPastServiceBenefitRet(entity.getPastServiceBenefitRet());
		dto.setMvc(entity.getMvc());
		dto.setCurrentServiceBenefitDeath(entity.getCurrentServiceBenefitDeath());
		dto.setCurrentServiceBenefitWdl(entity.getCurrentServiceBenefitWdl());
		dto.setCurrentServiceBenefitRet(entity.getCurrentServiceBenefitRet());
		dto.setPastServiceBenefit(entity.getPastServiceBenefit());
		dto.setCurrentServiceBenefit(entity.getCurrentServiceBenefit());
		dto.setAccruedGra(entity.getAccruedGra());
		dto.setAccruedGrat(entity.getAccruedGrat());
		dto.setTotalGra(entity.getTotalGra());
		dto.setTotalGrat(entity.getTotalGrat());
		dto.setLcSumAssured(entity.getLcSumAssured());
		dto.setLcPremium(entity.getLcPremium());
		dto.setIsActive(entity.getIsActive());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setModifiedBy(entity.getModifiedBy());
		dto.setModifiedDate(entity.getModifiedDate());

		return dto;
	}

	@Transactional
	@Override
	public ApiResponseDto<List<KeyValuePairDto>> findAllKeyValue() {

		OyrgtavaluesEntity oyrgtavaluesEntity = new OyrgtavaluesEntity();

		ArrayList<Object> tt = oyrgtavaluesRepository.findAllKeyValues();

		ArrayList<KeyValuePairDto> addkeyValuePairDto = new ArrayList<KeyValuePairDto>();
		for (Object get : tt) {
			KeyValuePairDto keyValuePairDto = new KeyValuePairDto();
			keyValuePairDto.setKey(get.toString());
			keyValuePairDto.setValue(get.toString());
			addkeyValuePairDto.add(keyValuePairDto);
		}

		return ApiResponseDto.success(
				addkeyValuePairDto.stream().map(MPHHelper::OyrgtavaluesEntityToDto).collect(Collectors.toList()));
	}
	
	
	@Transactional
	@Override
	public ApiResponseDto<KeyValuePairDto> findDefaultValues() {
		
		OyrgtavaluesEntity oyrgtavaluesEntity = new OyrgtavaluesEntity();
		ArrayList<Object> tt1 =oyrgtavaluesRepository.findDefaultValues();
		
		KeyValuePairDto keyValuePairDto = new KeyValuePairDto();
		keyValuePairDto.setKey(tt1.get(0).toString());
		keyValuePairDto.setValue(tt1.get(0).toString());
		return ApiResponseDto.success(keyValuePairDto);
		
	}
	

	private KeyValuePairDto OyrgtavaluesEntityToDto(OyrgtavaluesEntity OyrgtavaluesEntity) {

		return new ModelMapper().map(OyrgtavaluesEntity, KeyValuePairDto.class);
	}

	@Override
	public ApiResponseDto<List<RateTableSelectionDto>> industrygroup(RateTableSelectionDto rateTableSelectionDto) {
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RateTableSelectionEntity> createQuery = criteriaBuilder.createQuery(RateTableSelectionEntity.class);

		Root<RateTableSelectionEntity> root = createQuery.from(RateTableSelectionEntity.class);

		if (StringUtils.isNotBlank(rateTableSelectionDto.getRiskGroup())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("riskGroup")),
					rateTableSelectionDto.getRiskGroup().toLowerCase().trim()));
		}
		if (StringUtils.isNotBlank(rateTableSelectionDto.getNonSkilledDesc())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("nonSkilledDesc")),
					rateTableSelectionDto.getNonSkilledDesc().toLowerCase().trim()));
		}
		
		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<RateTableSelectionEntity> entities = new ArrayList<RateTableSelectionEntity>();
		entities = entityManager.createQuery(createQuery).getResultList();

		return ApiResponseDto.success(entities.stream().map(this::entitytoDto).collect(Collectors.toList()));
	}
	
	private RateTableSelectionDto entitytoDto(RateTableSelectionEntity rateTableSelectionEntity) {

		return new ModelMapper().map(rateTableSelectionEntity, RateTableSelectionDto.class);
	}

	@Override
	public ApiResponseDto<List<RateTableSelectionDto>> getratetableinorder(String ratevalue) {
		// retwyure(0,4)
		String getratevalue = ratevalue.substring(ratevalue.length() - 2);
		List<String> getRateTableInorder = rateTableSelectionRepository.getratetable(getratevalue);

		List<RateTableSelectionDto> rateTableSelectionDto = new ArrayList<RateTableSelectionDto>();
		for (String setRateValueinList : getRateTableInorder) {
			RateTableSelectionDto newRateTableSelectionDto = new RateTableSelectionDto();

			newRateTableSelectionDto.setRateTable(setRateValueinList);
			rateTableSelectionDto.add(newRateTableSelectionDto);

		}

		return ApiResponseDto.success(rateTableSelectionDto);

	}
	
	
}
