package com.lic.epgs.gratuity.policyservices.premiumcollection.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lic.epgs.gratuity.accountingservice.dto.GstDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.JournalVoucherDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.NewBusinessContributionAndLifeCoverAdjustmentDto;
import com.lic.epgs.gratuity.accountingservice.dto.RenwalWithGIDebitCreditRequestModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.UnlockDepositDetailDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.ProductFeatureDto;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.common.helper.CommonHelper;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.service.CommonService;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.fund.service.FundService;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.repository.HistoryMPHRepository;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.entity.PolicyTempSearchEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempMphSearchEntity;
import com.lic.epgs.gratuity.policy.claim.repository.TempPolicyClaimPropsRepository;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositLockDto;
import com.lic.epgs.gratuity.policy.endorsement.notes.repository.EndorsementNotesRepository;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyDepositRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContrySummaryRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyDepositRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.repository.RenewalLifeCoverTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.PolicyServiceRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.repository.RenewalSchemeruleTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.repository.RenewalValuationTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationBasicTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationMatrixTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationWithdrawalTMPRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyAdjustmentDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyAdjustmentDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyHistoryRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyMasterValuationRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.dto.ContributionAdjustmentPropsDto;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.entity.ContributionAdjustmentPropsEntity;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.entity.ContributionAdjustmentSearchPropsEntity;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.helper.ContriAdjustHelper;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.repository.ContributionAdjustmentPropsRepository;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.service.impl.ContributionAdjustmentServiceImpl;
import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.LifeCoverPremiumCollectionPropsDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.PremiumCollectionDuesDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.PremiumCollectionSearchDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.entity.LCPremiumCollectionDuesEntity;
import com.lic.epgs.gratuity.policyservices.premiumcollection.entity.LCPremiumCollectionSearchEntity;
import com.lic.epgs.gratuity.policyservices.premiumcollection.entity.LifeCoverPremiumCollectionPropsEntity;
import com.lic.epgs.gratuity.policyservices.premiumcollection.helper.PremiumCollectionHelper;
import com.lic.epgs.gratuity.policyservices.premiumcollection.repository.LifeCoverPremiumCollectionRepository;
import com.lic.epgs.gratuity.policyservices.premiumcollection.repository.PremiumCollectionDuesRepository;
import com.lic.epgs.gratuity.policyservices.premiumcollection.service.PremiumCollectionService;
import com.lic.epgs.gratuity.renewalpolicy.notes.repository.RenewalPolicyNotesRepository;
import com.lic.epgs.gratuity.simulation.dto.DepositAdjustementDto;

@Service
public class PremiumCollectionServiceImpl implements PremiumCollectionService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private StandardCodeRepository standardCodeRepository;
	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

	@Autowired
	private TaskProcessRepository taskProcessRepository;

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;

	private Long contributionDraftStatusId = 513l;
	private Long contributionsenttocheckerORPendingForApproveStatusId = 504l;
	private Long contributionSendtoBacktoMakerStatusId = 505L;
	private Long contributionRejectedStatusId = 506L;
	private Long contributionApproveStatusId = 507l;

	@Autowired
	private AccountingService accountingService;

	@Autowired
	private MasterPolicyAdjustmentDetailRepository masterPolicyAdjustmentDetailRepository;
	@Autowired
	private PolicyAdjustmentDetailRepository policyAdjustmentDetailRepository;

	@Autowired
	private PolicySchemeRuleRepository policySchemeRuleRepository;

	@Autowired
	private RenewalSchemeruleTMPRepository renewalSchemeruleTMPRepository;

	@Autowired
	private PolicyLifeCoverRepository policyLifeCoverRepository;

	@Autowired
	private RenewalLifeCoverTMPRepository renewalLifeCoverTMPRepository;

	@Autowired
	private RenewalValuationTMPRepository renewalValuationTMPRepository;

	@Autowired
	private PolicyValuationBasicRepository policyValuationBasicRepository;

	@Autowired
	private RenewalValuationBasicTMPRepository renewalValuationBasicTMPRepository;

	@Autowired
	private PolicyValuationWithdrawalRateRepository policyValuationWithdrawalRateRepository;

	@Autowired
	private RenewalValuationWithdrawalTMPRepository renewalValuationWithdrawalTMPRepository;

	@Autowired
	private PolicyGratuityBenefitRepository policyGratuityBenefitRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CommonModuleService commonModuleService;

	@Autowired
	private RenewalGratuityBenefitTMPRepository renewalGratuityBenefitTMPRepository;

	@Autowired
	private PolicyValuationMatrixRepository policyValuationMatrixRepository;

	@Autowired
	private RenewalValuationMatrixTMPRepository renewalValuationMatrixTMPRepository;

	@Autowired
	private MemberCategoryRepository memberCategoryRepository;

	@Autowired
	private PolicyMasterValuationRepository policyMasterValuationRepository;

	@Autowired
	private RenewalPolicyTMPMemberRepository renewalPolicyTMPMemberRepository;

	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;

	@Autowired
	private PolicyMemberRepository policyMemberRepository;

	@Autowired
	private PolicyServiceRepository policyServiceRepository;

	@Autowired
	private TempPolicyClaimPropsRepository tempPolicyClaimPropsRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	PolicyHistoryRepository policyHistoryRepository;

	@Autowired
	private HistoryMPHRepository historyMPHRepository;

	@Autowired
	private MPHRepository mPHRepository;

	@Autowired
	private TempMPHRepository tempMPHRepository;

	@Autowired
	private TempMemberRepository tempMemberRepository;

	@Autowired
	private FundService fundService;

	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private CommonMasterStateRepository commonMasterStateRepository;

	@Autowired
	private ContributionAdjustmentPropsRepository contributionAdjustmentPropsRepository;

	@Autowired
	private MasterPolicyRepository masterPolicyRepository;

	@Autowired
	private MemberCategoryModuleRepository memberCategoryModuleRepository;

	@Autowired
	private PolicyContributionDetailRepository policyContributionDetailRepository;

	@Autowired
	private PolicyDepositRepository policyDepositRepository;

	@Autowired
	private PolicyContributionRepository policyContributionRepository;

	@Autowired
	private PolicyContrySummaryRepository policyContrySummaryRepository;

	@Autowired
	private MasterPolicyDepositRepository masterPolicyDepositRepository;

	@Autowired
	private MasterPolicyContributionRepository masterPolicyContributionRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private RenewalPolicyNotesRepository renewalPolicyNotesRepository;

	@Autowired
	private MasterPolicyContributionDetailRepository masterPolicyContributionDetailRepository;

	@Autowired
	private LifeCoverPremiumCollectionRepository lifeCoverPremiumCollectionRepository;

	@Autowired
	private PremiumCollectionDuesRepository premiumCollectionDuesRepository;

	@Autowired
	private ContributionAdjustmentServiceImpl contributionAdjustmentServiceImpl;

	@Autowired
	private TaskAllocationRepository taskAllocationRepository;

	@Override
	public ApiResponseDto<List<LifeCoverPremiumCollectionPropsDto>> contributionfiltersearch(
			PremiumCollectionSearchDto premiumCollectionSearchDto) {

		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PolicyTempSearchEntity> createQuery = criteriaBuilder.createQuery(PolicyTempSearchEntity.class);

		Root<PolicyTempSearchEntity> root = createQuery.from(PolicyTempSearchEntity.class);

		Join<PolicyTempSearchEntity, LCPremiumCollectionSearchEntity> joinTmpPolicytoProps = root
				.join("tmpPremCollectPropsSearch");

		Join<PolicyTempSearchEntity, TempMphSearchEntity> joinTmpPolicytoTmpMPH = root.join("policyMPHTmp");

		// policyTMP start
		if (premiumCollectionSearchDto.getPolicyNumber() != null
				&& StringUtils.isNotBlank(premiumCollectionSearchDto.getPolicyNumber())) {
			predicates.add(criteriaBuilder.equal(root.get("policyNumber"),
					premiumCollectionSearchDto.getPolicyNumber().trim()));
		}
		if (premiumCollectionSearchDto.getCustomerCode() != null
				&& StringUtils.isNotBlank(premiumCollectionSearchDto.getCustomerCode())) {
			predicates.add(criteriaBuilder.equal(root.get("customerCode"),
					premiumCollectionSearchDto.getCustomerCode().trim()));
		}

		if (premiumCollectionSearchDto.getUserType() != null) {
			if (premiumCollectionSearchDto.getUserType().equals("UO")) {
				if (premiumCollectionSearchDto.getUnitCode() != null) {
					predicates
							.add(criteriaBuilder.equal(root.get("unitCode"), premiumCollectionSearchDto.getUnitCode()));
				}
			}
			if (premiumCollectionSearchDto.getUserType().equals("ZO")) {

				String get = premiumCollectionSearchDto.getUnitCode().substring(0, 2);
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("unitCode")),
						"%" + get.toLowerCase() + "%"));
			}
		}

		if ((premiumCollectionSearchDto.getPremAdjStatus() != null)
				&& StringUtils.isNotBlank(premiumCollectionSearchDto.getPremAdjStatus().toString())) {
			predicates.add(joinTmpPolicytoProps.get("premAdjStatus").in(premiumCollectionSearchDto.getPremAdjStatus()));
		}
		// End

		// MPHTMP Start
		if (premiumCollectionSearchDto.getMphName() != null
				&& StringUtils.isNotBlank(premiumCollectionSearchDto.getMphName())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicytoTmpMPH.get("mphName"))),
					premiumCollectionSearchDto.getMphName().toLowerCase()));
		}
		if (premiumCollectionSearchDto.getMphCode() != null
				&& StringUtils.isNotBlank(premiumCollectionSearchDto.getMphCode())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicytoTmpMPH.get("mphCode"))),
					premiumCollectionSearchDto.getMphCode().toLowerCase()));
		}
		if (premiumCollectionSearchDto.getPan() != null
				&& StringUtils.isNotBlank(premiumCollectionSearchDto.getPan())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicytoTmpMPH.get("pan"))),
					premiumCollectionSearchDto.getPan().toLowerCase()));
		}

		// ENd

		// MemberTMP Data start

		// END

//		List<Long> list = Arrays.asList(premiumCollectionSearchDto.getContriAdjStatus());
//		if(list.get(0)==contributionApproveStatusId) {
//			predicates.add(criteriaBuilder.equal(joinTmpPolicytoProps.get("isActive"), Boolean.FALSE));
//
//		}else {
//		predicates.add(criteriaBuilder.equal(joinTmpPolicytoProps.get("isActive"), Boolean.TRUE));
//		}
		// Props end

		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<PolicyTempSearchEntity> entities = entityManager.createQuery(createQuery).getResultList();

		List<LifeCoverPremiumCollectionPropsDto> listContributionAdjustmentPropsDto = new ArrayList<>();
		for (PolicyTempSearchEntity policySearch : entities) {
			LifeCoverPremiumCollectionPropsDto newContributionAdjustmentPropsDto = new ModelMapper().map(
					masterPolicyCustomRepository.setTransientValue(policySearch),
					LifeCoverPremiumCollectionPropsDto.class);
			newContributionAdjustmentPropsDto.setTmpPolicyId(policySearch.getId());
			newContributionAdjustmentPropsDto.setMphCode(policySearch.getPolicyMPHTmp().getMphCode());
			newContributionAdjustmentPropsDto.setMphName(policySearch.getPolicyMPHTmp().getMphName());
			for (LCPremiumCollectionSearchEntity getContributionAdjustment : policySearch
					.getTmpPremCollectPropsSearch()) {
				newContributionAdjustmentPropsDto.setId(getContributionAdjustment.getId());
				newContributionAdjustmentPropsDto.setPremAdjStatus(getContributionAdjustment.getPremAdjStatus());
				newContributionAdjustmentPropsDto.setWaiveLateFee(getContributionAdjustment.getWaiveLateFee());
				newContributionAdjustmentPropsDto.setLcPremCollNumber(getContributionAdjustment.getLcPremCollNumber());
				newContributionAdjustmentPropsDto.setIsEscalatedToZo(getContributionAdjustment.getIsEscalatedToZo());
			}

			listContributionAdjustmentPropsDto.add(newContributionAdjustmentPropsDto);
		}

		return ApiResponseDto.success(listContributionAdjustmentPropsDto);
	}

	@Transactional
	@Override
	public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> premiumcollectionforReject(Long premiumpropsId,
			String username, LifeCoverPremiumCollectionPropsDto lifeCoverPremiumCollectionPropsDto) {

		LifeCoverPremiumCollectionPropsEntity lifeCoverPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
				.findById(premiumpropsId).get();
		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository
				.findBytmpPolicyId(lifeCoverPremiumCollectionPropsEntity.getTmpPolicyId());
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(lifeCoverPremiumCollectionPropsEntity.getTmpPolicyId()).get();
		
		policyServiceRepository.deactivateservicetypeusingServiceId(renewalPolicyTMPEntity.getPolicyServiceId());
		if (isDevEnvironment == false) {
			String prodAndVarientCodeSame = commonModuleService.getProductCode(renewalPolicyTMPEntity.getProductId());
			List<UnlockDepositDetailDto> showDepositLockDto = new ArrayList<UnlockDepositDetailDto>();

			for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {
				UnlockDepositDetailDto depositLockDto = new UnlockDepositDetailDto();
				depositLockDto.setChallanNo(Integer.parseInt(policyDepositEntity.getChallanNo()));
				depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				depositLockDto.setUserCode(username);
				depositLockDto.setProductCode(prodAndVarientCodeSame);
				depositLockDto.setVariantCode(prodAndVarientCodeSame);
				showDepositLockDto.add(depositLockDto);
			}

			accountingService.unlockDeposits(showDepositLockDto, username);
		}

		lifeCoverPremiumCollectionPropsEntity.setPremAdjStatus(contributionRejectedStatusId);
		lifeCoverPremiumCollectionPropsEntity.setRejectedReason(lifeCoverPremiumCollectionPropsDto.getRejectedReason());
		lifeCoverPremiumCollectionPropsEntity
				.setRejectedRemarks(lifeCoverPremiumCollectionPropsDto.getRejectedRemarks());
		lifeCoverPremiumCollectionPropsEntity.setModifiedBy(username);
		lifeCoverPremiumCollectionPropsEntity.setModifiedDate(new Date());
		lifeCoverPremiumCollectionPropsEntity.setIsActive(false);
		lifeCoverPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
				.save(lifeCoverPremiumCollectionPropsEntity);
	
		TaskAllocationEntity taskallocationentity = taskAllocationRepository
				.findByRequestId(lifeCoverPremiumCollectionPropsEntity.getLcPremCollNumber());
		if (taskallocationentity != null) {
			taskallocationentity.setTaskStatus(contributionRejectedStatusId.toString());
			taskAllocationRepository.save(taskallocationentity);
		}

		return ApiResponseDto.success(PremiumCollectionHelper.entityToDto(lifeCoverPremiumCollectionPropsEntity));
	}

	@Override
	public ApiResponseDto<List<PremiumCollectionDuesDto>> duescalculation(Long masterpolicyId, String username) {

		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findById(masterpolicyId).get();
		
		HSNCodeDto hSNCodeDto = accountingService.getHSNCode();

		Date policyStartDate = masterPolicyEntity.getPolicyStartDate();
		Date policyEndDate = masterPolicyEntity.getPolicyEndDate();
		Date nextDueDate = masterPolicyEntity.getNextDueDate();
		Date adjustProcessingDate = new Date();
		String frequency = masterPolicyRepository.getPremiumMode(masterPolicyEntity.getContributionFrequencyId());
		int getFrequency = 0;
		if (frequency.toLowerCase().equals("monthly")) {
			getFrequency = 1;
		}
		if (frequency.toLowerCase().equals("quarterly")) {
			getFrequency = 3;
		}
		if (frequency.toLowerCase().equals("half yearly")) {
			getFrequency = 6;
		}
		if (frequency.toLowerCase().equals("annual")) {
			getFrequency = 12;
		}
		
		
		List<Date> collectionDate = validDueDateBasedOnFrequency(policyStartDate, policyEndDate, nextDueDate,
				adjustProcessingDate, getFrequency);
		ApiResponseDto<ProductFeatureDto> productFeatureDto = commonService.getProductFeature(masterpolicyId, 0L);

		Double lateFeeRateofInterest = productFeatureDto.getData().getLcpremLateFeePerc();
		Double gracePeriod = new Double(productFeatureDto.getData().getLcpremGracePeriod());
		Double halfYear = 0.5108;

		Double quarterly = 0.2582;

		Double monthly = 0.0867;

		Double gstRate = hSNCodeDto.getIgstRate();
		Double lcPremium = policyMemberRepository.findByLcPremium(masterpolicyId); // need write query
		Double lcpremiumforDues = 0.0;
		Double gstOnLCPremium = 0.0;
		Double modelFactorCalculation = 0.0;
		Double lateFee = 0.0;
		Double latefeeGST = 0.0;
		List<PremiumCollectionDuesDto> listPremiumCollectionDuesDto = new ArrayList<PremiumCollectionDuesDto>();

		try {
			for (Date dueDate : collectionDate) {
				// LC Premium
				if (frequency.toLowerCase().equals("monthly")) {
					lcpremiumforDues = lcPremium * policyMemberRepository.getMonthvalue();
				}
				if (frequency.toLowerCase().equals("quarterly")) {
					lcpremiumforDues = lcPremium * policyMemberRepository.getQuaterlyvalue();
					;
				}
				if (frequency.toLowerCase().equals("half yearly")) {
					lcpremiumforDues = lcPremium * policyMemberRepository.getHalflyvalue();
				}
				if (frequency.toLowerCase().equals("annual")) {
					lcpremiumforDues = lcPremium;
				}

				// Model Factor
//			if(frequency.toLowerCase().equals("monthly")){
//				modelFactorCalculation=(monthly*100)*12;
//			}
//			if(frequency.toLowerCase().equals("quarterly")){
//				modelFactorCalculation=(quarterly*100)*4;
//			}
//			if(frequency.toLowerCase().equals("half yearly")){
//				modelFactorCalculation=(halfYear*100)*2;
//			}
				lcpremiumforDues = lcpremiumforDues;
				lcpremiumforDues = CommonHelper.convertTwoDecimalValue(lcpremiumforDues);// LCPremium for dues
				gstOnLCPremium = lcpremiumforDues * (gstRate / 100);
				gstOnLCPremium = CommonHelper.convertTwoDecimalValue(gstOnLCPremium);// GST for LCPremium
				long diff = adjustProcessingDate.getTime() - dueDate.getTime();
				System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
				Double numberOfDueDays = new Double((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) - 1);
				Double noofmonthdue = 0.0;
				Double B = 0.0;
				long C = 0l;
				double D = 0l;
				double E = 0.0;
				double F = 0;
				// calculat LATE FEEs
				if (numberOfDueDays < gracePeriod && numberOfDueDays < 15) {

					lateFee = lateFee;
					latefeeGST = latefeeGST;
				}
				if (numberOfDueDays <= gracePeriod && numberOfDueDays >= 15) {
					noofmonthdue = 1.0;
				}
				if (numberOfDueDays > gracePeriod) {
					B = CommonHelper.convertTwoDecimalValue((numberOfDueDays / gracePeriod));
					C = B.longValue();
					D = C * gracePeriod;
					E = numberOfDueDays - D;
					if (E <= 15) {
						F = 0.0;
					}
					if (E > 15) {
						F = 1.0;
					}
					noofmonthdue = C + F;
				}
				if (noofmonthdue > 0 && noofmonthdue <= 6.0) {
					lateFee = (lcpremiumforDues * (CommonHelper.convertFourDecimalValue(noofmonthdue / 12.0)))
							* (lateFeeRateofInterest / 100);
				}
				if (noofmonthdue > 6) {
					Double firstSixLateFee = CommonHelper.convertTwoDecimalValue(
							(lcpremiumforDues * CommonHelper.convertFourDecimalValue(6.0 / 12.0))
									* (lateFeeRateofInterest / 100));
					Double nextSixMonthDue = noofmonthdue - 6.0;
					CommonHelper.convertTwoDecimalValue(lcpremiumforDues + firstSixLateFee);
					CommonHelper.convertFourDecimalValue(nextSixMonthDue / 12.0);

					Double lastSixLateFee = CommonHelper.convertTwoDecimalValue(
							CommonHelper.convertTwoDecimalValue(lcpremiumforDues + firstSixLateFee)
									* CommonHelper.convertFourDecimalValue(nextSixMonthDue / 12.0)
									* (lateFeeRateofInterest / 100));
					lateFee = firstSixLateFee + lastSixLateFee;

				}
				lateFee = CommonHelper.convertTwoDecimalValue(lateFee);

				if (lateFee > 0) {
					latefeeGST = lateFee * (gstRate / 100);
				}
				latefeeGST = CommonHelper.convertTwoDecimalValue(latefeeGST);

				listPremiumCollectionDuesDto
						.add(premiumLatefeeDetail(lcpremiumforDues, gstOnLCPremium, lateFee, latefeeGST, dueDate));

			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return ApiResponseDto.success(listPremiumCollectionDuesDto);
	}

	private PremiumCollectionDuesDto premiumLatefeeDetail(Double lcpremiumforDues, Double gstOnLCPremium,
			Double lateFee, Double latefeeGST, Date dueDate) {
		PremiumCollectionDuesDto premiumCollectionDuesDto = new PremiumCollectionDuesDto();

		premiumCollectionDuesDto.setLcPremiumAmount(lcpremiumforDues);
		premiumCollectionDuesDto.setGstOnLCPremium(gstOnLCPremium);
		premiumCollectionDuesDto.setLateFee(lateFee);
		premiumCollectionDuesDto.setGstOnLateFee(latefeeGST);
		premiumCollectionDuesDto.setDueDate(dueDate);

		premiumCollectionDuesDto.setTotalDueAmt(
				CommonHelper.convertTwoDecimalValue(lcpremiumforDues + gstOnLCPremium + lateFee + latefeeGST));

		return premiumCollectionDuesDto;
	}

	private List<Date> validDueDateBasedOnFrequency(Date policyStartDate, Date policyEndDate, Date nextDueDate,
			Date adjustProcessingDate, int contributtionFrequencyId) {

		ArrayList<Date> quarters = new ArrayList<Date>();

		String processingDate = DateUtils.dateToStringDDMMYYYY(adjustProcessingDate);
		String nextdueDate = DateUtils.dateToStringDDMMYYYY(nextDueDate);
		String policyStartDate1 = DateUtils.dateToStringDDMMYYYY(policyStartDate);
		String policyEndDate2 = DateUtils.dateToStringDDMMYYYY(policyEndDate);
		Calendar nextdue = Calendar.getInstance();

		nextdue.setTime(DateUtils.convertStringToDate(nextdueDate));

		Calendar adjustment = Calendar.getInstance();
		adjustment.setTime(DateUtils.convertStringToDate(processingDate));

		Calendar startDate = Calendar.getInstance();
		startDate.setTime(DateUtils.convertStringToDate(policyStartDate1));

		Calendar endDate = Calendar.getInstance();
		endDate.setTime(DateUtils.convertStringToDate(policyEndDate2));
		int frequency = contributtionFrequencyId;

		while (endDate.getTime().after(startDate.getTime())) {
			int month = startDate.get(Calendar.MONTH) + 1;
			int quarter = month % frequency == 0 ? (month / frequency) : (month / frequency) + 1;

			System.out.println(startDate.getTime());
			if (startDate.getTime().compareTo(nextdue.getTime()) >= 0
					&& startDate.getTime().compareTo(adjustment.getTime()) <= 0)
				quarters.add(startDate.getTime());

			startDate.add(Calendar.MONTH, frequency);
		}

		return quarters;
	}

	@Transactional
	@Override
	public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> createPremiumcollectionAdjustment(
			PolicyContributionDetailDto policyContributionDetailDto, Long pmstPolicyId) {

		int countExitingContriAdj = contributionAdjustmentPropsRepository
				.findpmstpolicyexitforcontriadjust(pmstPolicyId);
		if (countExitingContriAdj > 0) {
			return ApiResponseDto.errorMessage(null, null, "already existed");
		}
		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findById(pmstPolicyId).get();

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = contributionAdjustmentServiceImpl
				.mastertoTmpforPolicyService(masterPolicyEntity);

		Long masterTmpPolicyId = renewalPolicyTMPEntity.getId();
		Double getBalance = null;
		Double adjustementAmount = 0.0;
		int year = 0;
		int month = 0;
		String financialYear = null;
		financialYear = DateUtils.getFinancialYear(new Date());

//		TaskProcessEntity taskProcessEntity =taskProcessRepository.findByProcessName("RENEWAL PROCESSING");

		PolicyContributionDetailEntity policyContributionDetailEntity = new ModelMapper()
				.map(policyContributionDetailDto, PolicyContributionDetailEntity.class);
		policyContributionDetailEntity.setTmpPolicyId(masterTmpPolicyId);
		policyContributionDetailEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		policyContributionDetailEntity.setCreatedDate(new Date());
		policyContributionDetailEntity.setAdjustmentForDate(new Date());
		policyContributionDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
		policyContributionDetailEntity.setEntryType("PC");
		policyContributionDetailEntity.setFinancialYear(financialYear);
		policyContributionDetailEntity.setActive(true);
		policyContributionDetailEntity = policyContributionDetailRepository.save(policyContributionDetailEntity);

		masterTmpPolicyId = policyContributionDetailEntity.getTmpPolicyId();

//		TaskAllocationEntity taskAllocationEntity = taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getPolicyNumber());;

		// master contributio detail

		adjustementAmount = policyContributionDetailEntity.getLifePremium() + policyContributionDetailEntity.getGst()
				+ policyContributionDetailEntity.getLateFee() + policyContributionDetailEntity.getGstOnLateFee();

		List<ShowDepositLockDto> showDepositLockDto = new ArrayList<ShowDepositLockDto>();
		List<Date> collectionDate = new ArrayList<>();
		for (DepositAdjustementDto getDepositAdjustementDto : policyContributionDetailDto.getDepositAdjustmentDto()) {
			if (adjustementAmount != 0) {
				getDepositAdjustementDto.setDepositAmount(getDepositAdjustementDto.getBalanceAmount());
				PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

				PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();

				if (adjustementAmount < getDepositAdjustementDto.getDepositAmount()) {

					getBalance = getDepositAdjustementDto.getDepositAmount() - adjustementAmount;
					Double T = getBalance * 100;
					Long T1 = Math.round(T);
					getBalance = T1.doubleValue() / 100;
					policyDepositEntity.setAvailableAmount(getBalance);
					policyDepositEntity.setAdjustmentAmount(adjustementAmount);
					adjustementAmount = 0.0;
				} else {

					getBalance = adjustementAmount - getDepositAdjustementDto.getDepositAmount();
					Double T = getBalance * 100;
					Long T1 = Math.round(T);
					getBalance = T1.doubleValue() / 100;
					policyDepositEntity.setAdjustmentAmount(getDepositAdjustementDto.getDepositAmount());
					policyDepositEntity.setAvailableAmount(0.0);
					adjustementAmount = getBalance;
				}
				policyDepositEntity.setActive(true);
				Random r = new Random(System.currentTimeMillis());
				int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
				policyDepositEntity.setChallanNo(policyContributionDetailEntity.getChallanNo());
				policyDepositEntity.setCollectionDate(new Date());
				collectionDate.add(policyDepositEntity.getCollectionDate());
				policyDepositEntity.setZeroId(null);
				policyDepositEntity.setChequeRealistionDate(getDepositAdjustementDto.getVoucherEffectiveDate());
				policyDepositEntity.setTransactionMode(getDepositAdjustementDto.getCollectionMode());
				policyDepositEntity.setStatus(null);
				policyDepositEntity.setRegConId(null);
				policyDepositEntity.setTmpPolicyId(masterTmpPolicyId);
				policyDepositEntity.setContributionType("PC");
				policyDepositEntity.setContributionDetailId(policyContributionDetailEntity.getId());
				;
				policyDepositEntity.setAdjConId(null);
				policyDepositEntity.setDepositAmount(getDepositAdjustementDto.getDepositAmount());
				policyDepositEntity.setCreatedBy(getDepositAdjustementDto.getCreatedBy());
				policyDepositEntity.setCreateDate(new Date());
				policyDepositEntity.setIsDeposit(true);
				policyDepositEntity.setRemark(getDepositAdjustementDto.getRemarks());
				policyDepositEntity.setCollectionNo(getDepositAdjustementDto.getCollectionNumber());
				policyDepositEntity.setCollectionStatus(getDepositAdjustementDto.getCollectionStatus());
				policyDepositEntity.setActive(true);
				policyDepositRepository.save(policyDepositEntity);

				ShowDepositLockDto depositLockDto = new ShowDepositLockDto();

				String prodAndVarientCodeSame = commonModuleService
						.getProductCode(renewalPolicyTMPEntity.getProductId());

				depositLockDto.setChallanNo(Integer.valueOf(policyContributionDetailEntity.getChallanNo()));
				depositLockDto.setRefNo(policyDepositEntity.getId().toString());
				depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				depositLockDto.setUserCode(policyContributionDetailDto.getCreatedBy());
				depositLockDto.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
				depositLockDto.setProductCode(prodAndVarientCodeSame);
				depositLockDto.setVariantCode(prodAndVarientCodeSame);

				logger.info("Lock Deposit Params: " + depositLockDto.toString());
				showDepositLockDto.add(depositLockDto);
				;
			}
		}

		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository
				.findBycontributionDetailId(policyContributionDetailEntity.getId());

		List<PolicyContributionEntity> policyContributionEntity = new ArrayList<PolicyContributionEntity>();
		List<PolicyAdjustmentDetailEntity> policyAdjustmentDetail = new ArrayList<PolicyAdjustmentDetailEntity>();
		Double depositAmount = 0.0;
		Long depositId = 0l;
		Double currentAmount = 0.0;

//		Long premiumAdjustmentLong = Math.round(policyContributionDetailEntity.getLifePremium());
//		Double premiumAdjustment = premiumAdjustmentLong.doubleValue();

//		Long latefeeLong = Math.round(policyContributionDetailEntity.getLateFee());
//		Double latefeeAdjusted = latefeeLong.doubleValue();

//		Long gstLateFeeAdjustedLong = Math.round(policyContributionDetailEntity.getGstOnLateFee());
//		Double gstLateFeeAdjusted = gstLateFeeAdjustedLong.doubleValue();

		Double premiumAdjustment = policyContributionDetailEntity.getLifePremium();
		Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();
		Double latefeeAdjusted = policyContributionDetailEntity.getLateFee();
		Double gstLateFeeAdjusted = policyContributionDetailEntity.getGstOnLateFee();

		adjustementAmount = premiumAdjustment + gstOnPremiumAdjusted + latefeeAdjusted + gstLateFeeAdjusted;
		policyContributionDetailDto
				.setDepositAdjustmentDto(sort(policyContributionDetailDto.getDepositAdjustmentDto()));

		Long versionNumber = policyContributionRepository.getMaxVersion(financialYear,
				policyContributionDetailEntity.getId());
		versionNumber = versionNumber == null ? 01 : versionNumber + 1;
		for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {
			if (adjustementAmount > 0) {
				Double currentAmountforContribution = 0.0;
				depositAmount = policyDepositEntity.getAdjustmentAmount(); // adjustment from given lifecover and gst
				depositId = policyDepositEntity.getId();
				// LIFE PREMIUM NO

				if (premiumAdjustment > 0 && depositAmount > 0) {
					if (premiumAdjustment >= depositAmount) {
						currentAmount = depositAmount;

						premiumAdjustment = premiumAdjustment - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
								"LifePremium", "PC", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						currentAmountforContribution += currentAmount;
//					policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
//							policyDepositEntity.getContributionDetailId(), "RE", currentAmount, financialYear,
//							versionNumber,
//							commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
//							policyDepositEntity.getChequeRealistionDate(),
//							policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate()));
//					versionNumber=versionNumber+1;
						depositAmount = 0.0;
					} else {
						currentAmount = premiumAdjustment;

						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
								"LifePremium", "PC", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - premiumAdjustment;
						currentAmountforContribution += currentAmount;
//					policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
//							policyDepositEntity.getContributionDetailId(), "RE", currentAmount, financialYear,
//							versionNumber,
//							commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
//							policyDepositEntity.getChequeRealistionDate(),
//							policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate()));
//					versionNumber=versionNumber+1;
						premiumAdjustment = 0.0;
					}
				}

				// GST AMOUNT NO

				if (premiumAdjustment == 0 && gstOnPremiumAdjusted > 0 && depositAmount > 0) {
					if (gstOnPremiumAdjusted >= depositAmount) {
						currentAmount = depositAmount;

						gstOnPremiumAdjusted = gstOnPremiumAdjusted - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst",
								"PC", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						currentAmountforContribution += currentAmount;
//					policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
//							policyDepositEntity.getContributionDetailId(), "RE", currentAmount, financialYear,
//							versionNumber,
//							commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
//							policyDepositEntity.getChequeRealistionDate(),
//							policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate()));
//					versionNumber=versionNumber+1;
						depositAmount = 0.0;
					} else {
						currentAmount = gstOnPremiumAdjusted;

						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst",
								"PC", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - gstOnPremiumAdjusted;
						currentAmountforContribution += currentAmount;
//					policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
//							policyDepositEntity.getContributionDetailId(), "RE", currentAmount, financialYear,
//							versionNumber,
//							commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
//							policyDepositEntity.getChequeRealistionDate(),
//							policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate()));
//					versionNumber=versionNumber+1;
						gstOnPremiumAdjusted = 0.0;
					}
				}

				if (premiumAdjustment == 0 && gstOnPremiumAdjusted == 0 && latefeeAdjusted > 0 && depositAmount > 0) {
					if (latefeeAdjusted >= depositAmount) {
						currentAmount = depositAmount;

						latefeeAdjusted = latefeeAdjusted - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LateFee",
								"LateFee", "PC", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						currentAmountforContribution += currentAmount;
//					policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
//							policyDepositEntity.getContributionDetailId(), "RE", currentAmount, financialYear,
//							versionNumber,
//							commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
//							policyDepositEntity.getChequeRealistionDate(),
//							policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate()));
//					versionNumber=versionNumber+1;
						depositAmount = 0.0;
					} else {
						currentAmount = latefeeAdjusted;

						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LateFee",
								"LateFee", "PC", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - gstOnPremiumAdjusted;
						currentAmountforContribution += currentAmount;
//					policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
//							policyDepositEntity.getContributionDetailId(), "RE", currentAmount, financialYear,
//							versionNumber,
//							commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
//							policyDepositEntity.getChequeRealistionDate(),
//							policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate()));
//					versionNumber=versionNumber+1;
						latefeeAdjusted = 0.0;
					}
				}

				if (premiumAdjustment == 0 && gstOnPremiumAdjusted == 0 && latefeeAdjusted == 0
						&& gstLateFeeAdjusted > 0 && depositAmount > 0) {
					if (latefeeAdjusted >= depositAmount) {
						currentAmount = depositAmount;

						gstLateFeeAdjusted = gstLateFeeAdjusted - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "GSTOnLateFee",
								"GSTOnLateFee", "PC", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						currentAmountforContribution += currentAmount;

						depositAmount = 0.0;
					} else {
						currentAmount = gstLateFeeAdjusted;

						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "GSTOnLateFee",
								"GSTOnLateFee", "PC", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - gstOnPremiumAdjusted;
						currentAmountforContribution += currentAmount;

						gstLateFeeAdjusted = 0.0;
					}
				}

			}
		}

		policyAdjustmentDetailRepository.saveAll(policyAdjustmentDetail);

		if (isDevEnvironment == false) {
			accountingService.lockDeposits(showDepositLockDto, policyContributionDetailDto.getCreatedBy());
		}

		Date risk = Collections.max(collectionDate);
		renewalPolicyTMPEntity.setRiskCommencementDate(risk);
		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		LifeCoverPremiumCollectionPropsEntity lifeCoverPremiumCollectionPropsEntity = null;
		try {
			lifeCoverPremiumCollectionPropsEntity = new LifeCoverPremiumCollectionPropsEntity();
			lifeCoverPremiumCollectionPropsEntity.setAmtDueToBeAdjusted(policyContributionDetailEntity.getLifePremium()
					+ policyContributionDetailEntity.getGst() + policyContributionDetailEntity.getLateFee()
					+ policyContributionDetailEntity.getGstOnLateFee());
			lifeCoverPremiumCollectionPropsEntity.setPstgContriDetailId(policyContributionDetailEntity.getId());
			lifeCoverPremiumCollectionPropsEntity.setTmpPolicyId(masterTmpPolicyId);
			lifeCoverPremiumCollectionPropsEntity
					.setLcPremCollNumber(commonService.getSequence(HttpConstants.PREM_COLLECTION));
			lifeCoverPremiumCollectionPropsEntity.setLcPremiumAmount(policyContributionDetailEntity.getLifePremium());
			lifeCoverPremiumCollectionPropsEntity.setGstOnLCPremium(policyContributionDetailEntity.getGst());
			lifeCoverPremiumCollectionPropsEntity.setLateFee(policyContributionDetailEntity.getLateFee());
			lifeCoverPremiumCollectionPropsEntity.setGstOnLateFee(policyContributionDetailEntity.getGstOnLateFee());
			lifeCoverPremiumCollectionPropsEntity.setPremAdjStatus(contributionDraftStatusId);
			lifeCoverPremiumCollectionPropsEntity.setAdjustmentForDate(new Date());
			lifeCoverPremiumCollectionPropsEntity.setIsActive(true);
			lifeCoverPremiumCollectionPropsEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
			lifeCoverPremiumCollectionPropsEntity.setCreatedDate(new Date());
			lifeCoverPremiumCollectionPropsEntity.setWaiveLateFee(policyContributionDetailDto.getWaiveLateFee());
			lifeCoverPremiumCollectionPropsEntity.setId(null);
			lifeCoverPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
					.save(lifeCoverPremiumCollectionPropsEntity);

		} catch (Exception e) {
			logger.error("PCProps Error", e);
		}
		List<LCPremiumCollectionDuesEntity> lcPremiumCollectionDuesEntity = new ArrayList<LCPremiumCollectionDuesEntity>();
		try {
			for (PremiumCollectionDuesDto premiumCollectionDuesDto : policyContributionDetailDto
					.getPremiumCollectionDuesDto()) {
				LCPremiumCollectionDuesEntity getLCPremiumCollectionDuesEntity = new ModelMapper()
						.map(premiumCollectionDuesDto, LCPremiumCollectionDuesEntity.class);
				getLCPremiumCollectionDuesEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
				getLCPremiumCollectionDuesEntity.setCreatedDate(new Date());
				getLCPremiumCollectionDuesEntity.setTotalDueAmt(getLCPremiumCollectionDuesEntity.getLcPremiumAmount()
						+ getLCPremiumCollectionDuesEntity.getGstOnLCPremium()
						+ getLCPremiumCollectionDuesEntity.getLateFee()
						+ getLCPremiumCollectionDuesEntity.getGstOnLateFee());
				getLCPremiumCollectionDuesEntity.setIsActive(true);
				getLCPremiumCollectionDuesEntity.setDueDate(premiumCollectionDuesDto.getDueDate());
				getLCPremiumCollectionDuesEntity.setLcPremCollPropsId(lifeCoverPremiumCollectionPropsEntity.getId());
				lcPremiumCollectionDuesEntity.add(getLCPremiumCollectionDuesEntity);
			}
			premiumCollectionDuesRepository.saveAll(lcPremiumCollectionDuesEntity);

		} catch (Exception e) {
			logger.error("PCProps Error", e);
		}
		TaskProcessEntity taskProcessEntity = taskProcessRepository.findByProcessName("LC-PREMIUM-ADJUSTMENT");
		TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
				.findByRequestId(lifeCoverPremiumCollectionPropsEntity.getLcPremCollNumber());

		if (taskAllocationEntity != null) {
			taskAllocationEntity.setRequestId(lifeCoverPremiumCollectionPropsEntity.getLcPremCollNumber());
			taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
			// taskAllocationEntity.setLocationType(policyContributionDetailDto.getUnitCode());
			taskAllocationEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
			taskAllocationEntity.setCreatedDate(new Date());
			taskAllocationEntity.setTaskStatus(contributionDraftStatusId.toString());
			taskAllocationEntity.setModulePrimaryId(lifeCoverPremiumCollectionPropsEntity.getId());
			taskAllocationEntity.setIsActive(true);

			taskAllocationRepository.save(taskAllocationEntity);
		} else {
			TaskAllocationEntity newTaskAllocationEntity = new TaskAllocationEntity();
			newTaskAllocationEntity.setRequestId(lifeCoverPremiumCollectionPropsEntity.getLcPremCollNumber());
			newTaskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
			// taskAllocationEntity.setLocationType(policyContributionDetailDto.getUnitCode());
			newTaskAllocationEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
			newTaskAllocationEntity.setCreatedDate(new Date());
			newTaskAllocationEntity.setTaskStatus(contributionDraftStatusId.toString());
			newTaskAllocationEntity.setModulePrimaryId(lifeCoverPremiumCollectionPropsEntity.getId());
			newTaskAllocationEntity.setIsActive(true);

			taskAllocationRepository.save(newTaskAllocationEntity);
		}
		return ApiResponseDto.success(PremiumCollectionHelper.entityToDto(lifeCoverPremiumCollectionPropsEntity));

	}

	public static List<DepositAdjustementDto> sort(List<DepositAdjustementDto> list) {

		list.sort((o1, o2) -> o1.getVoucherEffectiveDate().compareTo(o2.getVoucherEffectiveDate()));

		return list;
	}

	@Transactional
	@Override
	public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> updatePremiumcollectionAdjustment(
			PolicyContributionDetailDto policyContributionDetailDto, Long tmpPolicyId) {
		Long masterTmpPolicyId = null;
		Double getBalance = null;
		String financialYear = null;
		financialYear = DateUtils.getFinancialYear(new Date());
		Double adjustementAmount = 0.0;
		Double stampDuty = 0.0;
		int year = 0;
		int month = 0;
		masterTmpPolicyId = tmpPolicyId;
		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository.findBytmpPolicyId(masterTmpPolicyId);

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(masterTmpPolicyId).get();
		String prodAndVarientCodeSame = commonModuleService.getProductCode(renewalPolicyTMPEntity.getProductId());
		if (isDevEnvironment == false) {
			List<UnlockDepositDetailDto> showDepositLockDto = new ArrayList<UnlockDepositDetailDto>();

			for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {

				UnlockDepositDetailDto depositLockDto = new UnlockDepositDetailDto();
				depositLockDto.setChallanNo(Integer.parseInt(policyDepositEntity.getChallanNo()));
				depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				depositLockDto.setUserCode(policyContributionDetailDto.getCreatedBy());
				depositLockDto.setProductCode(prodAndVarientCodeSame);
				depositLockDto.setVariantCode(prodAndVarientCodeSame);
				showDepositLockDto.add(depositLockDto);
			}

			accountingService.unlockDeposits(showDepositLockDto, policyContributionDetailDto.getCreatedBy());
		}

		PolicyContributionDetailEntity policyContributionDetailEntity = policyContributionDetailRepository
				.findBymasterTmpPolicyId(policyContributionDetailDto.getTmpPolicyId());
		policyContributionDetailEntity.setTmpPolicyId(policyContributionDetailDto.getTmpPolicyId());
		policyContributionDetailEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		policyContributionDetailEntity.setCreatedDate(new Date());
		policyContributionDetailEntity.setActive(true);
		policyContributionDetailEntity.setAdjustmentForDate(new Date());
		policyContributionDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
		policyContributionDetailEntity.setLifePremium(policyContributionDetailDto.getLifePremium());
		policyContributionDetailEntity.setGst(policyContributionDetailDto.getGst());
		policyContributionDetailEntity.setLateFee(policyContributionDetailDto.getLateFee());
		policyContributionDetailEntity.setGstOnLateFee(policyContributionDetailDto.getGstOnLateFee());

		policyContributionDetailEntity.setModifiedBy(policyContributionDetailDto.getModifiedBy());
		policyContributionDetailEntity.setModifiedDate(new Date());
		policyContributionDetailEntity.setEntryType("PC");

		policyContributionDetailEntity.setFinancialYear(financialYear);
		policyContributionDetailEntity = policyContributionDetailRepository.save(policyContributionDetailEntity);
		LifeCoverPremiumCollectionPropsEntity lifeCoverPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
				.findBytmpPolicyId(masterTmpPolicyId); // PC PROPS ENTRY

		List<PolicyAdjustmentDetailEntity> PolicyAdjustmentDetail = policyAdjustmentDetailRepository
				.deleteBycontributionDetailId(policyContributionDetailEntity.getId());
		List<PolicyDepositEntity> oldDepositList = policyDepositRepository.deleteBytmpPolicyId(masterTmpPolicyId);
		List<PolicyContributionEntity> oldPolicyContribution = policyContributionRepository
				.deleteBytmpPolicyId(masterTmpPolicyId);
		List<LCPremiumCollectionDuesEntity> oldLcPremCollDues = premiumCollectionDuesRepository
				.deleteBylcPremCollPropsId(lifeCoverPremiumCollectionPropsEntity.getId());

		adjustementAmount = policyContributionDetailEntity.getLifePremium() + policyContributionDetailEntity.getGst()
				+ policyContributionDetailDto.getLateFee() + policyContributionDetailDto.getGstOnLateFee();

		policyContributionDetailEntity = policyContributionDetailRepository.save(policyContributionDetailEntity);

		List<ShowDepositLockDto> showDepositLockDto = new ArrayList<ShowDepositLockDto>();
		for (DepositAdjustementDto getDepositAdjustementDto : policyContributionDetailDto.getDepositAdjustmentDto()) {
			if (adjustementAmount != 0) {
				getDepositAdjustementDto.setDepositAmount(getDepositAdjustementDto.getBalanceAmount());
				PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

				if (adjustementAmount < getDepositAdjustementDto.getDepositAmount()) {

					getBalance = getDepositAdjustementDto.getDepositAmount() - adjustementAmount;
					Double T = getBalance * 100;
					Long T1 = Math.round(T);
					getBalance = T1.doubleValue() / 100;
					policyDepositEntity.setAvailableAmount(getBalance);
					policyDepositEntity.setAdjustmentAmount(adjustementAmount);
					adjustementAmount = 0.0;
				} else {

					getBalance = adjustementAmount - getDepositAdjustementDto.getDepositAmount();
					Double T = getBalance * 100;
					Long T1 = Math.round(T);
					getBalance = T1.doubleValue() / 100;
					policyDepositEntity.setAdjustmentAmount(getDepositAdjustementDto.getDepositAmount());
					policyDepositEntity.setAvailableAmount(0.0);
					adjustementAmount = getBalance;
				}
				policyDepositEntity.setChequeRealistionDate(getDepositAdjustementDto.getVoucherEffectiveDate());
				policyDepositEntity.setActive(true);
//					Random r = new Random(System.currentTimeMillis());
//					int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
				policyDepositEntity.setChallanNo(policyContributionDetailEntity.getChallanNo());
				policyDepositEntity.setCollectionDate(new Date());
				policyDepositEntity.setCreateDate(new Date());
				policyDepositEntity.setZeroId(null);
				policyDepositEntity.setTransactionMode(getDepositAdjustementDto.getCollectionMode());
				policyDepositEntity.setStatus(null);
				policyDepositEntity.setRegConId(null);
				policyDepositEntity.setTmpPolicyId(masterTmpPolicyId);
				policyDepositEntity.setContributionType("PC");
				policyDepositEntity.setAdjConId(null);
				policyDepositEntity.setDepositAmount(getDepositAdjustementDto.getDepositAmount());
				policyDepositEntity.setCreatedBy(getDepositAdjustementDto.getCreatedBy());
				policyDepositEntity.setIsDeposit(true);
				policyDepositEntity.setActive(true);
				policyDepositEntity.setRemark(getDepositAdjustementDto.getRemarks());
				policyDepositEntity.setCollectionNo(getDepositAdjustementDto.getCollectionNumber());
				policyDepositEntity.setCollectionStatus(getDepositAdjustementDto.getCollectionStatus());
				policyDepositEntity.setContributionDetailId(policyContributionDetailEntity.getId());
				policyDepositRepository.save(policyDepositEntity);

				ShowDepositLockDto depositLockDto = new ShowDepositLockDto();

				depositLockDto.setChallanNo(Integer.valueOf(policyContributionDetailEntity.getChallanNo()));
				depositLockDto.setRefNo(policyDepositEntity.getId().toString());
				depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				depositLockDto.setUserCode(policyContributionDetailDto.getCreatedBy());
				depositLockDto.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
				depositLockDto.setProductCode(prodAndVarientCodeSame);
				depositLockDto.setVariantCode(prodAndVarientCodeSame);

				logger.info("Lock Deposit Params: " + depositLockDto.toString());
				showDepositLockDto.add(depositLockDto);
			}
		}

		List<PolicyContributionEntity> policyContributionEntity = new ArrayList<PolicyContributionEntity>();

		List<PolicyDepositEntity> listPolicyDepositEntities = policyDepositRepository
				.findBycontributionDetailId(policyContributionDetailEntity.getId());

		List<PolicyAdjustmentDetailEntity> policyAdjustmentDetail = new ArrayList<PolicyAdjustmentDetailEntity>();
		Double depositAmount = 0.0;
		Long depositId = 0l;
		Double currentAmount = 0.0;

//			Long premiumAdjustmentLong = Math.round(policyContributionDetailEntity.getLifePremium());
//			Double premiumAdjustment = premiumAdjustmentLong.doubleValue();
		Double premiumAdjustment = policyContributionDetailEntity.getLifePremium();

		Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();

//			Long latefeeLong = Math.round(policyContributionDetailEntity.getLateFee());
//			Double latefeeAdjusted = latefeeLong.doubleValue();

		Double latefeeAdjusted = policyContributionDetailEntity.getLateFee();

//			Long gstLateFeeAdjustedLong = Math.round(policyContributionDetailEntity.getGstOnLateFee());
//			Double gstLateFeeAdjusted = gstLateFeeAdjustedLong.doubleValue();

		Double gstLateFeeAdjusted = policyContributionDetailEntity.getGstOnLateFee();

		adjustementAmount = premiumAdjustment + gstOnPremiumAdjusted + latefeeAdjusted + gstLateFeeAdjusted;
		policyContributionDetailDto
				.setDepositAdjustmentDto(sort(policyContributionDetailDto.getDepositAdjustmentDto()));

		Long versionNumber = policyContributionRepository.getMaxVersion(financialYear,
				policyContributionDetailEntity.getId());
		versionNumber = versionNumber == null ? 01 : versionNumber + 1;
		for (PolicyDepositEntity policyDepositEntity : listPolicyDepositEntities) {
			Double currentAmountforContribution = 0.0;
			depositAmount = policyDepositEntity.getAdjustmentAmount();
			depositId = policyDepositEntity.getId();
			// LIFE PREMIUM NO

			if (premiumAdjustment > 0 && depositAmount > 0) {
				if (premiumAdjustment >= depositAmount) {
					currentAmount = depositAmount;

					premiumAdjustment = premiumAdjustment - depositAmount;
					adjustementAmount = adjustementAmount - currentAmount;
					policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
							policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
							"LifePremium", "PC", policyDepositEntity.getChequeRealistionDate(),
							policyContributionDetailDto.getCreatedBy()));
					currentAmountforContribution += currentAmount;

					depositAmount = 0.0;
				} else {
					currentAmount = premiumAdjustment;

					adjustementAmount = adjustementAmount - currentAmount;
					policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
							policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
							"LifePremium", "PC", policyDepositEntity.getChequeRealistionDate(),
							policyContributionDetailDto.getCreatedBy()));
					depositAmount = depositAmount - premiumAdjustment;
					currentAmountforContribution += currentAmount;

					premiumAdjustment = 0.0;
				}
			}

			// GST AMOUNT NO

			if (premiumAdjustment == 0 && gstOnPremiumAdjusted > 0 && depositAmount > 0) {
				if (gstOnPremiumAdjusted >= depositAmount) {
					currentAmount = depositAmount;

					gstOnPremiumAdjusted = gstOnPremiumAdjusted - depositAmount;
					adjustementAmount = adjustementAmount - currentAmount;
					policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
							policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst", "PC",
							policyDepositEntity.getChequeRealistionDate(), policyContributionDetailDto.getCreatedBy()));
					currentAmountforContribution += currentAmount;

					depositAmount = 0.0;
				} else {
					currentAmount = gstOnPremiumAdjusted;

					adjustementAmount = adjustementAmount - currentAmount;
					policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
							policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst", "PC",
							policyDepositEntity.getChequeRealistionDate(), policyContributionDetailDto.getCreatedBy()));
					depositAmount = depositAmount - gstOnPremiumAdjusted;
					currentAmountforContribution += currentAmount;

					gstOnPremiumAdjusted = 0.0;
				}
			}

			if (premiumAdjustment == 0 && gstOnPremiumAdjusted == 0 && latefeeAdjusted > 0 && depositAmount > 0) {
				if (latefeeAdjusted >= depositAmount) {
					currentAmount = depositAmount;

					latefeeAdjusted = latefeeAdjusted - depositAmount;
					adjustementAmount = adjustementAmount - currentAmount;
					policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
							policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LateFee",
							"LateFee", "PC", policyDepositEntity.getChequeRealistionDate(),
							policyContributionDetailDto.getCreatedBy()));
					currentAmountforContribution += currentAmount;

					depositAmount = 0.0;
				} else {
					currentAmount = latefeeAdjusted;

					adjustementAmount = adjustementAmount - currentAmount;
					policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
							policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LateFee",
							"LateFee", "PC", policyDepositEntity.getChequeRealistionDate(),
							policyContributionDetailDto.getCreatedBy()));
					depositAmount = depositAmount - gstOnPremiumAdjusted;
					currentAmountforContribution += currentAmount;

					latefeeAdjusted = 0.0;
				}
			}

			if (premiumAdjustment == 0 && gstOnPremiumAdjusted == 0 && latefeeAdjusted == 0 && gstLateFeeAdjusted > 0
					&& depositAmount > 0) {
				if (latefeeAdjusted >= depositAmount) {
					currentAmount = depositAmount;

					gstLateFeeAdjusted = gstLateFeeAdjusted - depositAmount;
					adjustementAmount = adjustementAmount - currentAmount;
					policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
							policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "GSTOnLateFee",
							"GSTOnLateFee", "PC", policyDepositEntity.getChequeRealistionDate(),
							policyContributionDetailDto.getCreatedBy()));
					currentAmountforContribution += currentAmount;

					depositAmount = 0.0;
				} else {
					currentAmount = gstLateFeeAdjusted;

					adjustementAmount = adjustementAmount - currentAmount;
					policyAdjustmentDetail.add(contributionAdjustmentServiceImpl.saveAdjustmentdata(
							policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "GSTOnLateFee",
							"GSTOnLateFee", "PC", policyDepositEntity.getChequeRealistionDate(),
							policyContributionDetailDto.getCreatedBy()));
					depositAmount = depositAmount - gstOnPremiumAdjusted;
					currentAmountforContribution += currentAmount;

					gstLateFeeAdjusted = 0.0;
				}
			}

		}

		policyAdjustmentDetailRepository.saveAll(policyAdjustmentDetail);

		if (isDevEnvironment == false) {
			accountingService.lockDeposits(showDepositLockDto, policyContributionDetailDto.getCreatedBy());
		}
		// Update lifeCoverPremiumCollectionPropsEntity start
		try {

			lifeCoverPremiumCollectionPropsEntity.setAmtDueToBeAdjusted(policyContributionDetailEntity.getLifePremium()
					+ policyContributionDetailEntity.getGst() + policyContributionDetailEntity.getLateFee()
					+ policyContributionDetailEntity.getGstOnLateFee());
			lifeCoverPremiumCollectionPropsEntity.setTmpPolicyId(masterTmpPolicyId);
			lifeCoverPremiumCollectionPropsEntity.setLcPremiumAmount(policyContributionDetailEntity.getLifePremium());
			lifeCoverPremiumCollectionPropsEntity.setGstOnLCPremium(policyContributionDetailEntity.getGst());
			lifeCoverPremiumCollectionPropsEntity.setLateFee(policyContributionDetailEntity.getLateFee());
			lifeCoverPremiumCollectionPropsEntity.setGstOnLateFee(policyContributionDetailEntity.getGstOnLateFee());
			lifeCoverPremiumCollectionPropsEntity.setModifiedBy(policyContributionDetailEntity.getModifiedBy());
			lifeCoverPremiumCollectionPropsEntity.setModifiedDate(new Date());
			lifeCoverPremiumCollectionPropsEntity.setWaiveLateFee(policyContributionDetailDto.getWaiveLateFee());
			lifeCoverPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
					.save(lifeCoverPremiumCollectionPropsEntity);

		} catch (Exception e) {
			logger.error("CAProps Error", e);
		}

		List<LCPremiumCollectionDuesEntity> lcPremiumCollectionDuesEntity = new ArrayList<LCPremiumCollectionDuesEntity>();
		try {
			for (PremiumCollectionDuesDto premiumCollectionDuesDto : policyContributionDetailDto
					.getPremiumCollectionDuesDto()) {
				LCPremiumCollectionDuesEntity getLCPremiumCollectionDuesEntity = new ModelMapper()
						.map(premiumCollectionDuesDto, LCPremiumCollectionDuesEntity.class);
				getLCPremiumCollectionDuesEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
				getLCPremiumCollectionDuesEntity.setCreatedDate(new Date());
				getLCPremiumCollectionDuesEntity.setTotalDueAmt(getLCPremiumCollectionDuesEntity.getLcPremiumAmount()
						+ getLCPremiumCollectionDuesEntity.getGstOnLCPremium()
						+ getLCPremiumCollectionDuesEntity.getLateFee()
						+ getLCPremiumCollectionDuesEntity.getGstOnLateFee());
				getLCPremiumCollectionDuesEntity.setIsActive(true);
				getLCPremiumCollectionDuesEntity.setDueDate(premiumCollectionDuesDto.getDueDate());
				getLCPremiumCollectionDuesEntity.setLcPremCollPropsId(lifeCoverPremiumCollectionPropsEntity.getId());
				lcPremiumCollectionDuesEntity.add(getLCPremiumCollectionDuesEntity);
			}
			premiumCollectionDuesRepository.saveAll(lcPremiumCollectionDuesEntity);

		} catch (Exception e) {
			logger.error("PCProps Error", e);
		}

		return ApiResponseDto.success(PremiumCollectionHelper.entityToDto(lifeCoverPremiumCollectionPropsEntity));
	}

	@Override
	public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> premiumAdjustStatusChange(Long premiumpropsId,
			Long statusId, LifeCoverPremiumCollectionPropsDto lcPremiumCollectionPropsDto) {
		LifeCoverPremiumCollectionPropsEntity lcPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
				.findById(premiumpropsId).get();
		lcPremiumCollectionPropsEntity.setPremAdjStatus(statusId);

		lifeCoverPremiumCollectionRepository.save(lcPremiumCollectionPropsEntity);

		TaskAllocationEntity taskallocationentity = taskAllocationRepository
				.findByRequestId(lcPremiumCollectionPropsEntity.getLcPremCollNumber());
		if (taskallocationentity != null) {
			taskallocationentity.setTaskStatus(statusId.toString());
			taskAllocationRepository.save(taskallocationentity);
		}

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(lcPremiumCollectionPropsEntity.getTmpPolicyId()).get();
		if (contributionsenttocheckerORPendingForApproveStatusId.equals(statusId)) {

			PolicyServiceEntitiy policyserviceentity = policyServiceRepository.findByPolicyandTypeandActive(
					renewalPolicyTMPEntity.getMasterPolicyId(), lcPremiumCollectionPropsDto.getServiceType());

			if (policyserviceentity == null) {

				PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
				policyServiceEntitiy.setServiceType(lcPremiumCollectionPropsDto.getServiceType());
				policyServiceEntitiy.setPolicyId(renewalPolicyTMPEntity.getMasterPolicyId());
				policyServiceEntitiy.setCreatedBy(lcPremiumCollectionPropsEntity.getCreatedBy());
				policyServiceEntitiy.setCreatedDate(new Date());
				policyServiceEntitiy.setIsActive(true);
				policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);
				renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
			} else {
				renewalPolicyTMPEntity.setPolicyServiceId(policyserviceentity.getId());
			}
			renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		}

		return ApiResponseDto.success(PremiumCollectionHelper.entityToDto(lcPremiumCollectionPropsEntity));
	}

	@Override
	public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> premiumcollectionprops(Long premiumpropsId) {
		LifeCoverPremiumCollectionPropsEntity lifeCoverPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
				.findById(premiumpropsId).get();
		return ApiResponseDto.success(PremiumCollectionHelper.entityToDto(lifeCoverPremiumCollectionPropsEntity));
	}

	@Override
	public ApiResponseDto<List<PremiumCollectionDuesDto>> premiumcollectionduesonprops(Long premiumpropsId) {
		List<LCPremiumCollectionDuesEntity> lcPremiumCollectionDuesEntities = premiumCollectionDuesRepository
				.findBylcPremCollPropsId(premiumpropsId);
		return ApiResponseDto.success(lcPremiumCollectionDuesEntities.stream().map(PremiumCollectionHelper::entityToDto)
				.collect(Collectors.toList()));
	}

	@Transactional
	@Override
	public ApiResponseDto<PolicyDto> approve(Long premcontriadjustpropsid, String username) {
		LifeCoverPremiumCollectionPropsEntity lifeCoverPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
				.findById(premcontriadjustpropsid).get();

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(lifeCoverPremiumCollectionPropsEntity.getTmpPolicyId()).get();
		// Copy to MasterPolicycontributionDetails
		MasterPolicyEntity newmasterPolicyEntity = masterPolicyRepository
				.findById(renewalPolicyTMPEntity.getMasterPolicyId()).get();

		// calculate next due date

		Date policyStartDate = newmasterPolicyEntity.getPolicyStartDate();
		Date policyEndDate = newmasterPolicyEntity.getPolicyEndDate();
		Date nextDueDate = newmasterPolicyEntity.getNextDueDate();
		Date adjustProcessingDate = new Date();
		String frequency = masterPolicyRepository.getPremiumMode(newmasterPolicyEntity.getContributionFrequencyId());
		int getFrequency = 0;
		if (frequency.toLowerCase().equals("monthly")) {
			getFrequency = 1;
		}
		if (frequency.toLowerCase().equals("quarterly")) {
			getFrequency = 3;
		}
		if (frequency.toLowerCase().equals("half yearly")) {
			getFrequency = 2;
		}
		if (frequency.toLowerCase().equals("annual")) {
			getFrequency = 12;
		}

		List<Date> collectionDate = validDueDateBasedOnFrequencygetLatestDue(policyStartDate, policyEndDate,
				nextDueDate, adjustProcessingDate, getFrequency);
		if (collectionDate.size() > 0) {
			newmasterPolicyEntity.setNextDueDate(Collections.min(collectionDate));
		} else {
			newmasterPolicyEntity.setNextDueDate(null);
		}

		// end

		MasterPolicyContributionDetails copyToMaster = PolicyHelper.entityToMaster(
				policyContributionDetailRepository.findByTmpPolicyandType(renewalPolicyTMPEntity.getId(), "PC"), null,
				newmasterPolicyEntity.getId());
		copyToMaster = masterPolicyContributionDetailRepository.save(copyToMaster);

		List<PolicyDepositEntity> policyDepositEntites = policyDepositRepository
				.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
		for (PolicyDepositEntity stagingDepositData : policyDepositEntites) {

			MasterPolicyDepositEntity newmasterPolicyDepositEntity = new ModelMapper().map(stagingDepositData,
					MasterPolicyDepositEntity.class);
			newmasterPolicyDepositEntity.setId(null);
			newmasterPolicyDepositEntity.setMasterPolicyId(newmasterPolicyEntity.getId());
			newmasterPolicyDepositEntity.setActive(true);
			newmasterPolicyDepositEntity.setContributionDetailId(copyToMaster.getId());

			newmasterPolicyDepositEntity = masterPolicyDepositRepository.save(newmasterPolicyDepositEntity);

			List<PolicyAdjustmentDetailEntity> policyAdjustmentDetailEntity = policyAdjustmentDetailRepository
					.findBydepositId(stagingDepositData.getId());
			for (PolicyAdjustmentDetailEntity getStagingAdjEntries : policyAdjustmentDetailEntity) {
				MasterPolicyAdjustmentDetailEntity masterPolicyAdjustmentDetailEntity = new ModelMapper()
						.map(getStagingAdjEntries, MasterPolicyAdjustmentDetailEntity.class);
				masterPolicyAdjustmentDetailEntity.setActive(true);
				masterPolicyAdjustmentDetailEntity
						.setContributionDetailId(newmasterPolicyDepositEntity.getContributionDetailId());
				masterPolicyAdjustmentDetailEntity.setDepositId(newmasterPolicyDepositEntity.getId());
				masterPolicyAdjustmentDetailRepository.save(masterPolicyAdjustmentDetailEntity);

			}

		}
		// copy Deposit to MasterPolicyContributionEntity
		List<MasterPolicyContributionEntity> masterPolicyContributionEntity = PolicyHelper.entityToPolicyConMaster(
				policyContributionRepository.findBytmpPolicyId(renewalPolicyTMPEntity.getId()),
				newmasterPolicyEntity.getId(), copyToMaster, username);
		masterPolicyContributionRepository.saveAll(masterPolicyContributionEntity);

		if (isDevEnvironment == false) {
			Boolean isRenewalStatus = false;
			int policyServiceEntitiy = policyServiceRepository
					.checkforpolicyrenewalsuccessstatus(newmasterPolicyEntity.getId(), "renewals");
			if (policyServiceEntitiy > 0) {
				isRenewalStatus = true;
			}

			String prodAndVarientCodeSame = commonModuleService.getProductCode(newmasterPolicyEntity.getProductId());
			String unitStateName = commonMasterUnitRepository
					.getStateNameByUnitCode(newmasterPolicyEntity.getUnitCode());
			String unitStateType = commonMasterStateRepository.getStateType(unitStateName);
			String unitStateCode = commonMasterStateRepository.getGSTStateCode(unitStateName);

			String mPHStateType = null;
			String mPHAddress = "";
			String mphStateCode = "";

			MPHEntity getMPHEntity = mPHRepository.findById(newmasterPolicyEntity.getMasterpolicyHolder()).get();
			for (MPHAddressEntity getMPHAddressEntity : getMPHEntity.getMphAddresses()) {
				if (getMPHAddressEntity.getStateName() != null || getMPHAddressEntity.getAddressLine1() != null) {
					mPHAddress = getMPHAddressEntity.getAddressLine1();
					if (getMPHAddressEntity.getAddressLine2() != null)
						mPHAddress += ", " + getMPHAddressEntity.getAddressLine2();
					if (getMPHAddressEntity.getCityLocality() != null)
						mPHAddress += ", " + getMPHAddressEntity.getCityLocality();
					if (getMPHAddressEntity.getStateName() != null)
						mPHAddress += "," + getMPHAddressEntity.getStateName();
					mPHStateType = commonMasterStateRepository.getStateType(getMPHAddressEntity.getStateName());
					mphStateCode = commonMasterStateRepository
							.getGSTStatecodebyid(Long.valueOf(getMPHAddressEntity.getStateName()));
					break;
				}
			}

			PolicyContributionDetailEntity masterPolicyContributiondetailEntity = policyContributionDetailRepository
					.findByTmpPolicyandType(renewalPolicyTMPEntity.getId(), "PC");

			Double adjustmentAmt = (masterPolicyContributiondetailEntity.getLifePremium() == null ? 0.0
					: masterPolicyContributiondetailEntity.getLifePremium())
					+ (masterPolicyContributiondetailEntity.getGst() == null ? 0.0
							: masterPolicyContributiondetailEntity.getGst())
					+ (masterPolicyContributiondetailEntity.getGstOnLateFee() == null ? 0.0
							: masterPolicyContributiondetailEntity.getGstOnLateFee())
					+ (masterPolicyContributiondetailEntity.getLateFee() == null ? 0.0
							: masterPolicyContributiondetailEntity.getLateFee());
			Double premiumandlate = (masterPolicyContributiondetailEntity.getLifePremium() == null ? 0.0
					: masterPolicyContributiondetailEntity.getLifePremium())
					+ (masterPolicyContributiondetailEntity.getLateFee() == null ? 0.0
							: masterPolicyContributiondetailEntity.getLateFee());

			HSNCodeDto hSNCodeDto = accountingService.getHSNCode();
			Map<String, Double> gstComponents = accountingService.getGstComponents(unitStateType, mPHStateType,
					hSNCodeDto, premiumandlate);

			NewBusinessContributionAndLifeCoverAdjustmentDto newBusinessContributionAndLifeCoverAdjustmentDto = new NewBusinessContributionAndLifeCoverAdjustmentDto();
			newBusinessContributionAndLifeCoverAdjustmentDto.setAdjustmentAmount(adjustmentAmt);
			newBusinessContributionAndLifeCoverAdjustmentDto
					.setAdjustmentNo(masterPolicyContributiondetailEntity.getId().intValue() * 3);
			newBusinessContributionAndLifeCoverAdjustmentDto
					.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
			newBusinessContributionAndLifeCoverAdjustmentDto.setIsGstApplicable(false);
			if (newmasterPolicyEntity.getGstApplicableId() == 1l)
				newBusinessContributionAndLifeCoverAdjustmentDto.setIsGstApplicable(true);
			newBusinessContributionAndLifeCoverAdjustmentDto.setMphCode(getMPHEntity.getMphCode());
			newBusinessContributionAndLifeCoverAdjustmentDto.setUnitCode(newmasterPolicyEntity.getUnitCode());
			newBusinessContributionAndLifeCoverAdjustmentDto.setUserCode(username);

			newBusinessContributionAndLifeCoverAdjustmentDto
					.setGlTransactionModel(accountingService.getGlTransactionModel(newmasterPolicyEntity.getProductId(),
							newmasterPolicyEntity.getProductVariantId(), newmasterPolicyEntity.getUnitCode(),
							"GratuityPremCollPolicyApproval"));
			String toGSTIn = getMPHEntity.getGstIn() == null ? "" : getMPHEntity.getGstIn();
			ResponseDto responseDto = accountingService
					.commonmasterserviceAllUnitCode(newmasterPolicyEntity.getUnitCode());
			GstDetailModelDto gstDetailModelDto = new GstDetailModelDto();
			gstDetailModelDto.setAmountWithTax((masterPolicyContributiondetailEntity.getLifePremium() == null ? 0.0
					: masterPolicyContributiondetailEntity.getLifePremium())
					+ (masterPolicyContributiondetailEntity.getGst() == null ? 0.0
							: masterPolicyContributiondetailEntity.getGst())
					+ (masterPolicyContributiondetailEntity.getGstOnLateFee() == null ? 0.0
							: masterPolicyContributiondetailEntity.getGstOnLateFee())
					+ (masterPolicyContributiondetailEntity.getLateFee() == null ? 0.0
							: masterPolicyContributiondetailEntity.getLateFee()));
			gstDetailModelDto.setAmountWithoutTax((masterPolicyContributiondetailEntity.getLifePremium() == null ? 0.0
					: masterPolicyContributiondetailEntity.getLifePremium())
					+ (masterPolicyContributiondetailEntity.getLateFee() == null ? 0.0
							: masterPolicyContributiondetailEntity.getLateFee()));
			gstDetailModelDto.setCessAmount(0.0); // from Docu

			gstDetailModelDto.setCgstAmount(gstComponents.get("CGST"));
			gstDetailModelDto.setCgstRate(hSNCodeDto.getCgstRate());
			gstDetailModelDto.setCreatedBy(username);
			gstDetailModelDto.setCreatedDate(new Date());
			gstDetailModelDto.setEffectiveEndDate(""); // form docu
			gstDetailModelDto.setEffectiveStartDate(new Date());
			gstDetailModelDto.setEntryType(toGSTIn != null ? "B2B" : "B2C");
			gstDetailModelDto.setFromGstn(responseDto.getGstIn() == null ? "" : responseDto.getGstIn());
			gstDetailModelDto.setFromPan(responseDto.getPan());
			gstDetailModelDto.setFromStateCode(unitStateCode); // from MPH detail null

			if (newmasterPolicyEntity.getGstApplicableId() == 1l)
				gstDetailModelDto.setGstApplicableType("Taxable");
			else
				gstDetailModelDto.setGstApplicableType("Non-Taxable");

			gstDetailModelDto.setGstInvoiceNo(""); // From Docu
			gstDetailModelDto.setGstRate(accountingService.getGSTRate(unitStateType, mPHStateType, hSNCodeDto) * 100);
			gstDetailModelDto.setGstRefNo(newmasterPolicyEntity.getPolicyNumber()); // from Docu
			gstDetailModelDto.setGstRefTransactionNo(newmasterPolicyEntity.getPolicyNumber());// From Docu
			gstDetailModelDto.setGstTransactionType("DEBIT");// From Docu
			gstDetailModelDto.setGstType("GST");// From Docu
			gstDetailModelDto.setHsnCode(hSNCodeDto.getHsnCode());
			gstDetailModelDto.setIgstAmount(gstComponents.get("IGST"));
			gstDetailModelDto.setIgstRate(hSNCodeDto.getIgstRate());
			gstDetailModelDto.setModifiedBy(0L); // from docu
			gstDetailModelDto.setModifiedDate(new Date()); // from Docu
			gstDetailModelDto.setMphAddress(mPHAddress);
			gstDetailModelDto.setMphName(getMPHEntity.getMphName());
			gstDetailModelDto.setNatureOfTransaction(hSNCodeDto.getDescription());
			gstDetailModelDto.setOldInvoiceDate(new Date()); // From Docu
			gstDetailModelDto.setOldInvoiceNo("IN20123QE"); // From Docu
			gstDetailModelDto.setProductCode(prodAndVarientCodeSame);
			gstDetailModelDto.setRemarks("Gratuity PC Deposit Adjustment");
			gstDetailModelDto.setSgstAmount(gstComponents.get("SGST"));
			gstDetailModelDto.setSgstRate(hSNCodeDto.getSgstRate());
			gstDetailModelDto.setToGstIn(getMPHEntity.getGstIn() == null ? "" : getMPHEntity.getGstIn()); // From Docu
																											// from get
																											// Common
																											// Module
			gstDetailModelDto.setToPan(getMPHEntity.getPan() == null ? "" : getMPHEntity.getPan());
			gstDetailModelDto.setToStateCode(mphStateCode == null ? "" : mphStateCode);
			gstDetailModelDto.setTotalGstAmount((masterPolicyContributiondetailEntity.getGst() == null ? 0.0
					: masterPolicyContributiondetailEntity.getGst().doubleValue())
					+ (masterPolicyContributiondetailEntity.getGstOnLateFee() == null ? 0.0
							: masterPolicyContributiondetailEntity.getGstOnLateFee()));
			gstDetailModelDto.setTransactionDate(new Date());
			gstDetailModelDto.setTransactionSubType("A"); // From Docu
			gstDetailModelDto.setTransactionType("C"); // From Docu
//		gstDetailModelDto.setUserCode(username);
			gstDetailModelDto.setUtgstAmount(gstComponents.get("UTGST"));

			gstDetailModelDto.setUtgstRate(hSNCodeDto.getUtgstRate());
			gstDetailModelDto.setVariantCode(prodAndVarientCodeSame);
			gstDetailModelDto.setYear(DateUtils.uniqueNoYYYY());
			gstDetailModelDto.setMonth(DateUtils.currentMonthName());
			newBusinessContributionAndLifeCoverAdjustmentDto.setGstDetailModel(gstDetailModelDto);

			JournalVoucherDetailModelDto journalVoucherDetailModelDto = new JournalVoucherDetailModelDto();
			journalVoucherDetailModelDto.setLineOfBusiness(newmasterPolicyEntity.getLineOfBusiness());
			journalVoucherDetailModelDto.setProduct(prodAndVarientCodeSame);
			journalVoucherDetailModelDto.setProductVariant(prodAndVarientCodeSame);
			newBusinessContributionAndLifeCoverAdjustmentDto.setJournalVoucherDetailModel(journalVoucherDetailModelDto);

			List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository
					.findBymasterPolicyIdandEntryType(renewalPolicyTMPEntity.getId(), "PC");
			List<RenwalWithGIDebitCreditRequestModelDto> getRenewalWithGiDebitCreditRequestModel = new ArrayList<RenwalWithGIDebitCreditRequestModelDto>();
//		Long premiumAdjustmentLong=Math.round(masterPolicyContributiondetailEntity.getLifePremium());
//		Double premiumAdjustment = premiumAdjustmentLong.doubleValue();
//		
//		Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();

			for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {
				RenwalWithGIDebitCreditRequestModelDto renewalWithGiDebitCreditRequestModel = new RenwalWithGIDebitCreditRequestModelDto();
				renewalWithGiDebitCreditRequestModel.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				Double gstandgstforlatefee = 0.0;
				Double totalDeposit = 0.0;
				List<PolicyAdjustmentDetailEntity> policyAdjustmentDetailEntity = policyAdjustmentDetailRepository
						.findBydepositId(policyDepositEntity.getId());
				for (PolicyAdjustmentDetailEntity getStagingAdjEntries : policyAdjustmentDetailEntity) {

					if (getStagingAdjEntries.getSubType().toLowerCase().equals("gst")
							|| getStagingAdjEntries.getSubType().toLowerCase().equals("gstonlatefee")) {

						gstandgstforlatefee += getStagingAdjEntries.getAdjustedAmount();
						totalDeposit += getStagingAdjEntries.getAdjustedAmount();
					}
					if (getStagingAdjEntries.getSubType().toLowerCase().equals("latefee")) {
						renewalWithGiDebitCreditRequestModel
								.setLateFeeCreditAmount(getStagingAdjEntries.getAdjustedAmount());
						totalDeposit += getStagingAdjEntries.getAdjustedAmount();
					}

					if (isRenewalStatus) {

						if (getStagingAdjEntries.getSubType().toLowerCase().equals("lifepremium")) {
							renewalWithGiDebitCreditRequestModel.setLifePremiumOYRGTARenewalPremiumCreditAmount(
									getStagingAdjEntries.getAdjustedAmount());
							totalDeposit += getStagingAdjEntries.getAdjustedAmount();
						}

					} else {
						if (getStagingAdjEntries.getSubType().toLowerCase().equals("lifepremium")) {
							renewalWithGiDebitCreditRequestModel.setOtherFirstYearOYRGTALifePremiumCreditAmount(
									getStagingAdjEntries.getAdjustedAmount());
							totalDeposit += getStagingAdjEntries.getAdjustedAmount();
						}

					}

				}
				renewalWithGiDebitCreditRequestModel.setGstOnPremiumCreditAmount(gstandgstforlatefee);

//			Long depositDebitAmountLong=Math.round(depositDebitAmount);
				renewalWithGiDebitCreditRequestModel
						.setDepositDebitAmount(CommonHelper.convertTwoDecimalValue(totalDeposit));
				getRenewalWithGiDebitCreditRequestModel.add(renewalWithGiDebitCreditRequestModel);

			}

			newBusinessContributionAndLifeCoverAdjustmentDto
					.setRenewalWithGiDebitCreditRequestModel(getRenewalWithGiDebitCreditRequestModel);
			newBusinessContributionAndLifeCoverAdjustmentDto
					.setRefNo(masterPolicyContributiondetailEntity.getId().toString());
			// need to uncommand
			accountingService.consumeDepositsforSubsequence(newBusinessContributionAndLifeCoverAdjustmentDto,newmasterPolicyEntity.getId());

//		String prodAndVarientCodeSame=	commonModuleService.getProductCode(policyEntity.getProductId());
			List<UnlockDepositDetailDto> showDepositLockDto = new ArrayList<UnlockDepositDetailDto>();

			for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {

				UnlockDepositDetailDto depositLockDto = new UnlockDepositDetailDto();
				depositLockDto.setChallanNo(Integer.parseInt(policyDepositEntity.getChallanNo()));
				depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				depositLockDto.setUserCode(username);
				depositLockDto.setProductCode(prodAndVarientCodeSame);
				depositLockDto.setVariantCode(prodAndVarientCodeSame);
				showDepositLockDto.add(depositLockDto);
			}
//		need to uncommand
			accountingService.unlockDeposits(showDepositLockDto, username);
		}
		lifeCoverPremiumCollectionPropsEntity.setIsActive(false);
		lifeCoverPremiumCollectionPropsEntity.setPremAdjStatus(contributionApproveStatusId);
		lifeCoverPremiumCollectionPropsEntity.setPmstContriDetailId(copyToMaster.getId());
		lifeCoverPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
				.save(lifeCoverPremiumCollectionPropsEntity);

		PolicyServiceEntitiy policyserviceentity = policyServiceRepository
				.findByPolicyandTypeandActive(renewalPolicyTMPEntity.getMasterPolicyId(), "premiumcollection");
		policyserviceentity.setIsActive(false);
		policyServiceRepository.save(policyserviceentity);

		TaskAllocationEntity taskallocationentity = taskAllocationRepository
				.findByRequestId(lifeCoverPremiumCollectionPropsEntity.getLcPremCollNumber());
		if (taskallocationentity != null) {
			taskallocationentity.setTaskStatus(contributionApproveStatusId.toString());
			taskAllocationRepository.save(taskallocationentity);
		}

		newmasterPolicyEntity = masterPolicyRepository.save(newmasterPolicyEntity);
		return ApiResponseDto.success(PolicyHelper.entityToDto(newmasterPolicyEntity));

	}

	private List<Date> validDueDateBasedOnFrequencygetLatestDue(Date policyStartDate, Date policyEndDate,
			Date nextDueDate, Date adjustProcessingDate, int contributtionFrequencyId) {

		ArrayList<Date> quarters = new ArrayList<Date>();

		String processingDate = DateUtils.dateToStringDDMMYYYY(adjustProcessingDate);
		String nextdueDate = DateUtils.dateToStringDDMMYYYY(nextDueDate);
		String policyStartDate1 = DateUtils.dateToStringDDMMYYYY(policyStartDate);
		String policyEndDate2 = DateUtils.dateToStringDDMMYYYY(policyEndDate);
		Calendar nextdue = Calendar.getInstance();

		nextdue.setTime(DateUtils.convertStringToDate(nextdueDate));

		Calendar adjustment = Calendar.getInstance();
		adjustment.setTime(DateUtils.convertStringToDate(processingDate));

		Calendar startDate = Calendar.getInstance();
		startDate.setTime(DateUtils.convertStringToDate(policyStartDate1));

		Calendar endDate = Calendar.getInstance();
		endDate.setTime(DateUtils.convertStringToDate(policyEndDate2));
		int frequency = contributtionFrequencyId;

		while (endDate.getTime().after(startDate.getTime())) {
			int month = startDate.get(Calendar.MONTH) + 1;
			int quarter = month % frequency == 0 ? (month / frequency) : (month / frequency) + 1;

			System.out.println(startDate.getTime());
			if (startDate.getTime().compareTo(nextdue.getTime()) >= 0
					&& startDate.getTime().compareTo(adjustment.getTime()) >= 0)
				quarters.add(startDate.getTime());

			startDate.add(Calendar.MONTH, frequency);
		}

		return quarters;
	}

	@Override
	public ApiResponseDto<LifeCoverPremiumCollectionPropsDto> escalateToZo(Long premiumpropsId, String username) {
		Optional<LifeCoverPremiumCollectionPropsEntity> lifeCoverPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
				.findById(premiumpropsId);
		LifeCoverPremiumCollectionPropsDto lifeCoverPremiumCollectionPropsDto = new LifeCoverPremiumCollectionPropsDto();
		if (lifeCoverPremiumCollectionPropsEntity.isPresent()) {
			LifeCoverPremiumCollectionPropsEntity newlifeCoverPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
					.findById(premiumpropsId).get();
			newlifeCoverPremiumCollectionPropsEntity.setIsEscalatedToZo(true);
			newlifeCoverPremiumCollectionPropsEntity.setModifiedBy(username);
			newlifeCoverPremiumCollectionPropsEntity.setPremAdjStatus(contributionDraftStatusId);
			newlifeCoverPremiumCollectionPropsEntity.setModifiedDate(new Date());
			newlifeCoverPremiumCollectionPropsEntity = lifeCoverPremiumCollectionRepository
					.save(newlifeCoverPremiumCollectionPropsEntity);

			lifeCoverPremiumCollectionPropsDto = PremiumCollectionHelper
					.entityToDto(newlifeCoverPremiumCollectionPropsEntity);
		}
		return ApiResponseDto.success(lifeCoverPremiumCollectionPropsDto);

	}

	@Transactional
	@Override
	public Boolean discardservice(Long propsId, String moduleType) {
		Long tmpPolicyId = 0l;
		if (moduleType.toLowerCase().equals("premiumcollection")) {
			LifeCoverPremiumCollectionPropsEntity lifeCoverPreColleProps = lifeCoverPremiumCollectionRepository
					.findById(propsId).get();
			tmpPolicyId = lifeCoverPreColleProps.getTmpPolicyId();
			premiumCollectionDuesRepository.deleteBylcPremCollPropsId(propsId);
			lifeCoverPremiumCollectionRepository.deleteById(propsId);

		}
		if (tmpPolicyId != null) {

			RenewalPolicyTMPEntity oldrenewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyId).get();

			if (oldrenewalPolicyTMPEntity != null) {
				if (oldrenewalPolicyTMPEntity.getPolicyServiceId() != null)
					policyServiceRepository.deleteBypmstPolicyandServiceId(
							oldrenewalPolicyTMPEntity.getMasterPolicyId(),
							oldrenewalPolicyTMPEntity.getPolicyServiceId());

				renewalSchemeruleTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
				renewalLifeCoverTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
				renewalGratuityBenefitTMPRepository.deleteByGratuityBenefitId(oldrenewalPolicyTMPEntity.getId());
				renewalGratuityBenefitTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
				renewalValuationTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
				renewalValuationMatrixTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
				renewalValuationBasicTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
				renewalValuationWithdrawalTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());

				renewalPolicyTMPMemberRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
				renewalPolicyNotesRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());

				policyAdjustmentDetailRepository.deleteBycontributionDetailId(oldrenewalPolicyTMPEntity.getId());
				policyDepositRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
				policyContributionRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());

				renewalPolicyTMPRepository.deleteById(oldrenewalPolicyTMPEntity.getId());

			} else {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}

	@Override
	public ApiResponseDto<Boolean> checkNextDueDate(Long masterPolicyId) {
		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.getNextDue(masterPolicyId);
		if (masterPolicyEntity != null) {
			return ApiResponseDto.success(true);
		} else {
			return ApiResponseDto.success(false);
		}

	}

}