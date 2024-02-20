package com.lic.epgs.gratuity.policy.renewalpolicy.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.lic.epgs.gratuity.accountingservice.dto.GstDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.JournalVoucherDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.RenewalContributionAdjustRequestModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.RenewalContrinAdjustDebitCreditRequestModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.UnlockDepositDetailDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.mph.dto.TemptMPHDto;
import com.lic.epgs.gratuity.mph.entity.HistoryMPHEntity;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.helper.MPHHelper;
import com.lic.epgs.gratuity.mph.repository.HistoryMPHRepository;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimHelper;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.entity.QuotationChargeEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.helper.PolicyGratuityBenefitHelper;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.HistoryGratutiyBenefitRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.helper.PolicyLifeCoverHelper;
import com.lic.epgs.gratuity.policy.lifecover.repository.HistoryLifeCoverRepository;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.helper.PolicyMemberHelper;
import com.lic.epgs.gratuity.policy.member.repository.HistoryMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContrySummaryEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyContrySummaryRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyDepositRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContrySummaryRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyDepositRepository;
import com.lic.epgs.gratuity.policy.renewal.helper.RenewalHelper;
import com.lic.epgs.gratuity.policy.renewal.repository.PolicyRenewalRemainderRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.PolicySearchFilterDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.QuotationRenewalDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyNewSearchFilterDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalQuotationSearchDTo;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyRenewalRemainderSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicySearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalsGratuityBenefitPropsTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.helper.RenewalGratuityBenefitTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.helper.RenewalPolicyHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.helper.RenewalPolicyTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.repository.RenewalLifeCoverTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.helper.RenewalPolicyTMPMemberHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.PolicyServiceRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.custom.RenewalPolicyTMPCustomRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.entity.RenewalSchemeruleTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.helper.RenewalSchemeruleTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.repository.RenewalSchemeruleTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.service.RenewalPolicyService;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.entity.RenewalValuationTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.helper.RenewalValuationTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.repository.RenewalValuationTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationWithdrawalTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.helper.RenewalValuationBasicTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.helper.RenewalValuationMatrixTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationBasicTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationMatrixTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationWithdrawalTMPRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyHistoryRepository;
import com.lic.epgs.gratuity.policy.repository.QuotationChargeRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.helper.PolicySchemeRuleHelper;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleHistoryRepository;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyMasterValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.helper.PolicyValuationHelper;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyMasterValuationRepository;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyValuationHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.helper.PolicyValuationMatrixHelper;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.quotation.dto.BenefitValuationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationPDFGenerationDto;
import com.lic.epgs.gratuity.quotation.helper.QuotationHelper;
import com.lic.epgs.gratuity.quotation.member.helper.MemberErrorWorkbookHelper;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;

@Service
public class RenewalPolicyServiceImpl implements RenewalPolicyService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Value("${app.policy.approvedStatudId}")
	private Long approvedStatudId;

	@Value("${app.policy.approvedSubStatudId}")
	private Long approvedSubStatudId;

	@Value("${app.policy.existingTaggedStatusId}")
	private Long existingTaggedStatusId;

	@Value("${app.quotation.defaultStatudId}")
	private String defaultStatusId;

	@Value("${app.quotation.defaultTaggedStatusId}")
	private String defaultTaggedStatusId;

	@Value("${app.quotation.sentBackToMakerSubStatudId}")
	private String sentBackToMakerSubStatudId;

	@Value("${app.quotation.existingaggedStatusId}")
	private String existingaggedStatusId;

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;

	@Value("${app.SITurl}")
	private String SITurl;

	@Value("${app.isDevEnvironmentForRenewals}")
	private Boolean isDevEnvironmentForRenewals;

	private static Long quotationStatusId = 16l;
	private static Long quotationSubStatusId = 21l;
	private static Long quotationTaggedStatusId = 78l;
	@Autowired
	private MasterPolicyDepositRepository masterPolicyDepositRepository;

	@Autowired
	private MasterPolicyContributionRepository masterPolicyContributionRepository;

	@Autowired
	private MasterPolicyContrySummaryRepository masterPolicyContrySummaryRepository;

	@Autowired
	private PolicyDepositRepository policyDepositRepository;

	@Autowired
	private PolicyContributionRepository policyContributionRepository;

	@Autowired
	private PolicyContrySummaryRepository policyContrySummaryRepository;

	@Autowired
	private GratuityCalculationRepository gratuityCalculationRepository;
	@Autowired
	private RenewalPolicyHelper renewalPolicyHelper;

	@Autowired
	private PolicyServiceRepository policyServiceRepository;

	@Autowired
	private MasterPolicyRepository masterPolicyRepository;

	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;

	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

	@Autowired
	private RenewalPolicyTMPCustomRepository renewalPolicyTMPCustomRepository;

	@Autowired
	private TempMPHRepository tempMPHRepository;

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
	private RenewalGratuityBenefitTMPRepository renewalGratuityBenefitTMPRepository;

	@Autowired
	private PolicyValuationMatrixRepository policyValuationMatrixRepository;

	@Autowired
	private RenewalValuationMatrixTMPRepository renewalValuationMatrixTMPRepository;

	@Autowired
	private MemberCategoryRepository memberCategoryRepository;

	@Autowired
	private PolicyRenewalRemainderRepository policyRenewalRemainderRepository;

	@Autowired
	private PolicyHistoryRepository policyHistoryRepository;

	@Autowired
	private PolicyMasterValuationRepository policyMasterValuationRepository;

	@Autowired
	private PolicySchemeRuleHistoryRepository policySchemeRuleHistoryRepository;
	@Autowired
	private PolicyValuationHistoryRepository policyValuationHistoryRepository;
	@Autowired
	PolicyValuationWithdrawalRateHistoryRepository policyValuationWithdrawalRateHistoryRepository;
	@Autowired
	private PolicyValuationMatrixHistoryRepository policyValuationMatrixHistoryRepository;
	@Autowired
	private PolicyValuationBasicHistoryRepository policyValuationBasicHistoryRepository;
	@Autowired
	private HistoryLifeCoverRepository historyLifeCoverRepository;
	@Autowired
	private HistoryGratutiyBenefitRepository historyGratutiyBenefitRepository;

	@Autowired
	private PolicyMemberRepository policyMemberRepository;

	@Autowired
	private HistoryMemberRepository historyMemberRepository;
	@Autowired
	private RenewalPolicyTMPMemberRepository renewalPolicyTMPMemberRepository;

	@Autowired
	private StandardCodeRepository standardCodeRepository;

	@Autowired
	private QuotationChargeRepository quotationChargeRepository;

	@Autowired
	private HistoryMPHRepository historyMPHRepository;

	@Autowired
	private PolicyContributionDetailRepository policyContributionDetailRepository;

	@Autowired
	private MasterPolicyContributionDetailRepository masterPolicyContributionDetailRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CommonModuleService commonModuleService;

	@Autowired
	private AccountingService accountingService;

	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private CommonMasterStateRepository commonMasterStateRepository;
	
	@Autowired
	private MemberCategoryModuleRepository memberCategoryModuleRepository;
	
	@Autowired
	private TaskProcessRepository taskProcessRepository; 
	
	@Autowired
	private TaskAllocationRepository taskAllocationRepository;

	@Autowired
	private Environment environment;

	@Autowired
	@Qualifier(value = "restTemplateService")
	private RestTemplate restTemplateService;
	
	
	@Override
	public ApiResponseDto<List<RenewalPolicyTMPDto>> fetchPolicyForRenewalQuotation(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo) {
		return this.inPolicyTmpFilter(renewalQuotationSearchDTo);
	}

	private ApiResponseDto<List<RenewalPolicyTMPDto>> inPolicyTmpFilter(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo) {
		List<RenewalPolicyTMPEntity> getRenewalPolicyTMPEntity = null;

		if (renewalQuotationSearchDTo.getUserType() != null) {
			if (renewalQuotationSearchDTo.getUserType().equals("UO")) {
				getRenewalPolicyTMPEntity = renewalPolicyTMPCustomRepository.findByquotationTaggedStatusIdwithUnit(
						renewalQuotationSearchDTo.getQuotationTaggedStatusId(),
						renewalQuotationSearchDTo.getPolicyNumber(), renewalQuotationSearchDTo.getUnitCode());
			}
			if (renewalQuotationSearchDTo.getUserType().equals("ZO")) {
				String getUnitCode = renewalQuotationSearchDTo.getUnitCode().substring(0, 2);
				getRenewalPolicyTMPEntity = renewalPolicyTMPCustomRepository.findByquotationTaggedStatuswithgetUnitCode(
						renewalQuotationSearchDTo.getQuotationTaggedStatusId(),
						renewalQuotationSearchDTo.getPolicyNumber(), getUnitCode);
			} else {
				getRenewalPolicyTMPEntity = renewalPolicyTMPCustomRepository.findByquotationTaggedStatusId(
						renewalQuotationSearchDTo.getQuotationTaggedStatusId(),
						renewalQuotationSearchDTo.getPolicyNumber());
			}
		}

		return ApiResponseDto.success(
				getRenewalPolicyTMPEntity.stream().map(RenewalPolicyHelper::entityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<RenewalPolicyTMPDto>> processingfilter(

			RenewalQuotationSearchDTo renewalQuotationSearchDTo) {


		List<RenewalPolicyTMPEntity> renewalPolicTmpEntitiy = new ArrayList<>();
		List<RenewalPolicyTMPEntity> newRenewalPolicyTmpEntitiy = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<RenewalPolicyTMPEntity> criteriaQuery = criteriaBuilder
					.createQuery(RenewalPolicyTMPEntity.class);
			Root<RenewalPolicyTMPEntity> root = criteriaQuery.from(RenewalPolicyTMPEntity.class);

//			  Join<RenewalPolicyTMPEntity,PolicyServiceEntitiy> join = root.join("policyServiceId"); 
//		
//			  if(renewalQuotationSearchDTo.getServiceId()!=null) {
//				  join.on(criteriaBuilder.equal(join.get("policyServiceId"),
//			  renewalQuotationSearchDTo.getServiceId())); }

			if (StringUtils.isNotBlank(renewalQuotationSearchDTo.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
						renewalQuotationSearchDTo.getPolicyNumber().toLowerCase().trim()));
			}
			if (StringUtils.isNoneBlank(renewalQuotationSearchDTo.getCustomerName())) {
				predicates.add(
						criteriaBuilder.equal(root.get("customerName"), renewalQuotationSearchDTo.getCustomerName()));
			}
			if (StringUtils.isNoneBlank(renewalQuotationSearchDTo.getIntermediaryName())) {
				predicates.add(criteriaBuilder.equal(root.get("intermediaryName"),
						renewalQuotationSearchDTo.getIntermediaryName()));
			}
			if (renewalQuotationSearchDTo.getLineOfBusiness() != null) {
				predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"),
						renewalQuotationSearchDTo.getLineOfBusiness()));
			}
			if (renewalQuotationSearchDTo.getProductId() != null) {
				predicates.add(criteriaBuilder.equal(root.get("productId"), renewalQuotationSearchDTo.getProductId()));
			}
			if (renewalQuotationSearchDTo.getQuotationTaggedStatusId() != null) {
				predicates.add(criteriaBuilder.equal(root.get("quotationTaggedStatusId"),
						renewalQuotationSearchDTo.getQuotationTaggedStatusId()));
			}
			if (renewalQuotationSearchDTo.getQuotationStatusId() != null && renewalQuotationSearchDTo.getQuotationStatusId() >0) {
				predicates.add(criteriaBuilder.equal(root.get("quotationStatusId"),
						renewalQuotationSearchDTo.getQuotationStatusId()));
			}
			if (renewalQuotationSearchDTo.getQuotationSubStatusId() != null && renewalQuotationSearchDTo.getQuotationSubStatusId() >0) {
				predicates.add(criteriaBuilder.equal(root.get("quotationSubStatusId"),
						renewalQuotationSearchDTo.getQuotationSubStatusId()));
			}
			
			
			if (renewalQuotationSearchDTo.getPolicytaggedStatusId() != null) {
				predicates.add(criteriaBuilder.equal(root.get("policytaggedStatusId"),
						renewalQuotationSearchDTo.getPolicytaggedStatusId()));
			}
			
			if (renewalQuotationSearchDTo.getPolicyStatusId() != null && renewalQuotationSearchDTo.getPolicyStatusId() >0) {
				predicates.add(criteriaBuilder.equal(root.get("policyStatusId"),
						renewalQuotationSearchDTo.getPolicyStatusId()));
			}
			
			if (renewalQuotationSearchDTo.getPolicySubStatusId() != null && renewalQuotationSearchDTo.getPolicySubStatusId() >0) {
				predicates.add(criteriaBuilder.equal(root.get("policySubStatusId"),
						renewalQuotationSearchDTo.getPolicySubStatusId()));
			}

			if (renewalQuotationSearchDTo.getProductVariant() != null) {
				predicates.add(criteriaBuilder.equal(root.get("productVariant"),
						renewalQuotationSearchDTo.getProductVariant()));
			}
			if (StringUtils.isNoneBlank(renewalQuotationSearchDTo.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), renewalQuotationSearchDTo.getUnitCode()));
			}
//			if (StringUtils.isNotBlank(
//					DateUtils.dateToStringFormatYyyyMmDdHhMmSsSAlash(renewalQuotationSearchDTo.getPolicyStartDate()))) {
//				predicates.add(criteriaBuilder.equal(root.get("policyStartDate"),
//						renewalQuotationSearchDTo.getPolicyStartDate()));
//			}
//			if (StringUtils.isNotBlank(
//					DateUtils.dateToStringFormatYyyyMmDdHhMmSsSAlash(renewalQuotationSearchDTo.getPolicyEndDate()))) {
//				predicates.add(
//						criteriaBuilder.equal(root.get("policyEndDate"), renewalQuotationSearchDTo.getPolicyEndDate()));
//			}
			
	
			if (renewalQuotationSearchDTo.getPolicyStartDate() != null && renewalQuotationSearchDTo.getPolicyEndDate() != null) {
				Date fromDate = renewalQuotationSearchDTo.getPolicyStartDate();
				Date toDate = renewalQuotationSearchDTo.getPolicyEndDate();
				
				fromDate= constructeStartDateTime(fromDate);
				toDate = constructeEndDateTime(toDate);
				Predicate onStart = criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), fromDate);
				Predicate onEnd = criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), toDate);
				predicates.add(onStart);
				predicates.add(onEnd);
			}
			
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			criteriaQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			renewalPolicTmpEntitiy = entityManager.createQuery(criteriaQuery).getResultList();

			// Get List Data
			List<RenewalPolicyTMPEntity> getRenewalPolicyTMPEntity = null;

			for (RenewalPolicyTMPEntity policyTMPEntity : renewalPolicTmpEntitiy) {

				if (renewalQuotationSearchDTo.getUserType() != null) {
					if (renewalQuotationSearchDTo.getUserType().equals("UO")) {
						getRenewalPolicyTMPEntity = renewalPolicyTMPCustomRepository.findBypolicytaggedStatusIdwithUnit(
								renewalQuotationSearchDTo.getPolicytaggedStatusId(), renewalQuotationSearchDTo.getPolicyNumber(),
								renewalQuotationSearchDTo.getUnitCode());
						if (getRenewalPolicyTMPEntity.size() > 0) {
							newRenewalPolicyTmpEntitiy.add(policyTMPEntity);
						}
					}
					if (renewalQuotationSearchDTo.getUserType().equals("ZO")) {
						String getUnitCode = policyTMPEntity.getUnitCode().substring(0, 2);
						getRenewalPolicyTMPEntity = renewalPolicyTMPCustomRepository.findBypolicytaggedStatusIdwithgetUnitCode(
								renewalQuotationSearchDTo.getPolicytaggedStatusId(), renewalQuotationSearchDTo.getPolicyNumber(),
								getUnitCode);
						if (getRenewalPolicyTMPEntity.size() > 0) {
							newRenewalPolicyTmpEntitiy.add(policyTMPEntity);
						}
					} else {
						getRenewalPolicyTMPEntity = renewalPolicyTMPCustomRepository.findBypolicytaggedStatusId(
								renewalQuotationSearchDTo.getPolicytaggedStatusId(), renewalQuotationSearchDTo.getPolicyNumber());
						if (getRenewalPolicyTMPEntity.size() > 0) {
							newRenewalPolicyTmpEntitiy.add(policyTMPEntity);
						}
					}
				}
			}

		} catch (IllegalArgumentException e) {
			e.getStackTrace();
		}
		return ApiResponseDto.created(
				renewalPolicTmpEntitiy.stream().map(RenewalPolicyHelper::entityToDto).collect(Collectors.toList()));
	
	}

//		return ApiResponseDto.success(getProcessingRenewalPolicyTMPEntity.stream().map(RenewalPolicyHelper::entityToDto)
//				.collect(Collectors.toList()));
//	}

//		List<Predicate> predicates = new ArrayList<>();
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<RenewalPolicyTMPEntity> createQuery = criteriaBuilder.createQuery(RenewalPolicyTMPEntity.class);
//
//		Root<RenewalPolicyTMPEntity> root = createQuery.from(RenewalPolicyTMPEntity.class);
//	
//		if (renewalQuotationSearchDTo.getQuotationTaggedStatusId() > 0) {
//			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("quotationTaggedStatusId")),
//					renewalQuotationSearchDTo.getQuotationTaggedStatusId()));
//		}
//		if (StringUtils.isNotBlank(renewalQuotationSearchDTo.getPolicyNumber())) {
//			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
//					renewalQuotationSearchDTo.getPolicyNumber().toLowerCase()));
//		}
//		if (StringUtils.isNotBlank(renewalQuotationSearchDTo.getCustomerName())) {
//			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("customerName")),
//					renewalQuotationSearchDTo.getCustomerName().toLowerCase()));
//		}
//		if (StringUtils.isNotBlank(renewalQuotationSearchDTo.getIntermediaryName())) {
//			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("intermediaryName")),
//					renewalQuotationSearchDTo.getIntermediaryName().toLowerCase()));
//		}
//		if (renewalQuotationSearchDTo.getLineOfBusiness() != null && renewalQuotationSearchDTo.getLineOfBusiness() > 0) {
//			predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"), renewalQuotationSearchDTo.getLineOfBusiness()));
//		}
//		if (renewalQuotationSearchDTo.getProductId() != null && renewalQuotationSearchDTo.getProductId() > 0) {
//			predicates.add(criteriaBuilder.equal(root.get("productId"), renewalQuotationSearchDTo.getProductId()));
//		}
//		if (renewalQuotationSearchDTo.getProductVariant() != null && renewalQuotationSearchDTo.getProductVariant() > 0) {
//			predicates.add(criteriaBuilder.equal(root.get("productVariant"), renewalQuotationSearchDTo.getProductVariant()));
//		}
//		if (renewalQuotationSearchDTo.getUnitOffice() != null && renewalQuotationSearchDTo.getUnitOffice() > 0) {
//			predicates.add(criteriaBuilder.equal(root.get("unitOffice"), renewalQuotationSearchDTo.getUnitOffice()));
//		}
////		if (policySearchDto.getPolicyStatus() != null && policySearchDto.getPolicyStatus() > 0) {
////			predicates.add(criteriaBuilder.equal(root.get("policyStatus"), policySearchDto.getPolicyStatus()));
////		}
//		if (renewalQuotationSearchDTo.getQuotationStatusId() != null && renewalQuotationSearchDTo.getQuotationStatusId() > 0) {
//			predicates.add(criteriaBuilder.equal(root.get("quotationStatusId"), renewalQuotationSearchDTo.getQuotationStatusId()));
//		}
//		if (renewalQuotationSearchDTo.getPolicyStartDate() != null) {
//			predicates.add(criteriaBuilder.equal(root.get("policyStartDate"), renewalQuotationSearchDTo.getPolicyStartDate()));
//		}
//		if (renewalQuotationSearchDTo.getPolicyStartDate() != null && renewalQuotationSearchDTo.getPolicyEndDate() != null) {
//			try {
//				predicates.add(criteriaBuilder.between(root.get("date"), renewalQuotationSearchDTo.getPolicyStartDate(),
//						renewalQuotationSearchDTo.getPolicyEndDate()));
//			} catch (Exception e) {
//
//			}
//		}
//
//		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
//		List<RenewalPolicyTMPEntity> entities = new ArrayList<RenewalPolicyTMPEntity>();
//		entities = entityManager.createQuery(createQuery).getResultList();

	@Transactional
	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> CreateRenewalforQuotation(QuotationRenewalDto quotationRenewalDto) {
		
		TaskProcessEntity taskProcessEntity =taskProcessRepository.findByProcessName("RENEWAL QUOTATION");
		TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();
		policyServiceRepository.updateifRenewalRemainderdoneforsamePolicy(quotationRenewalDto.getPolicyId(), quotationRenewalDto.getServiceType());
		
		if (policyServiceRepository
				.findByPolicyandType(quotationRenewalDto.getPolicyId(), quotationRenewalDto.getServiceType())
				.size() > 0 ) {
			return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
		} else {
			PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
			
			PolicyServiceEntitiy policyserviceentity = policyServiceRepository.findByPolicyandTypeandActive(quotationRenewalDto.getPolicyId(), quotationRenewalDto.getServiceType());
			
			if(policyserviceentity!=null) {
//				PolicyServiceEntitiy policyServiceEntities = policyServiceRepository.GetPolicyandTypeandActive(quotationRenewalDto.getPolicyId(), quotationRenewalDto.getServiceType());

				policyserviceentity.setServiceType(quotationRenewalDto.getServiceType());
				policyserviceentity.setPolicyId(quotationRenewalDto.getPolicyId());
				policyserviceentity.setCreatedBy(quotationRenewalDto.getCreatedBy());
				policyserviceentity.setCreatedDate(new Date());
				policyserviceentity.setIsActive(true);
				policyServiceEntitiy = policyServiceRepository.save(policyserviceentity);

			}else {
		

			policyServiceEntitiy.setServiceType(quotationRenewalDto.getServiceType());
			policyServiceEntitiy.setPolicyId(quotationRenewalDto.getPolicyId());
			policyServiceEntitiy.setCreatedBy(quotationRenewalDto.getCreatedBy());
			policyServiceEntitiy.setCreatedDate(new Date());
			policyServiceEntitiy.setIsActive(true);
			policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);
			}
			MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository
					.findById(quotationRenewalDto.getPolicyId());
			List<String> getListNumer = renewalPolicyTMPRepository.addQuotationNumber(masterPolicyEntity.getPolicyNumber());
			String getQuotationNumber = null;
			if (getListNumer.size() > 0) {
				getQuotationNumber = QuotationHelper.addQuotationNumber(masterPolicyEntity.getPolicyNumber(), getListNumer.get(0));
			} else {
				getQuotationNumber = masterPolicyEntity.getPolicyNumber() + "01";
			}
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = RenewalPolicyTMPHelper.pmsttoTmp(masterPolicyEntity);
			renewalPolicyTMPEntity.setId(null);
			renewalPolicyTMPEntity.setPolicyStatusId(null);
			renewalPolicyTMPEntity.setPolicySubStatusId(null);
			renewalPolicyTMPEntity.setPolicytaggedStatusId(null);
			renewalPolicyTMPEntity.setQuotationStatusId(quotationStatusId);
			renewalPolicyTMPEntity.setQuotationSubStatusId(quotationSubStatusId);
			renewalPolicyTMPEntity.setQuotationTaggedStatusId(quotationTaggedStatusId);
			renewalPolicyTMPEntity.setMasterPolicyId(quotationRenewalDto.getPolicyId());
			renewalPolicyTMPEntity.setQuotationDate(new Date());
			renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
			renewalPolicyTMPEntity.setIsActive(true);
			renewalPolicyTMPEntity.setCreatedBy(quotationRenewalDto.getCreatedBy());
			renewalPolicyTMPEntity.setCreatedDate(new Date());
			renewalPolicyTMPEntity.setQuotationNumber(getQuotationNumber);

			renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

			MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
			TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytomastertoTemp(renewalPolicyTMPEntity.getId(),
					mPHEntity);
			tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
			renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
			renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
			// renewalPolicyTMPEntity.getId()

			// scheme MastrePolicy to TMp copy

			Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
					.findBypolicyId(quotationRenewalDto.getPolicyId());

			if (policySchemeEntity.isPresent()) {

				RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = RenewalSchemeruleTMPHelper
						.pmsttoTmp(policySchemeEntity.get());
				renewalSchemeruleTMPEntity.setId(null);
				renewalSchemeruleTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
				renewalSchemeruleTMPEntity.setPmstSchemeRuleId(policySchemeEntity.get().getId());

				renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);
			}
			List<MemberCategoryModuleEntity> addMemberCategoryModuleEntity = new ArrayList<MemberCategoryModuleEntity>();
			List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
					.findBypmstPolicyId(quotationRenewalDto.getPolicyId());
			for(MemberCategoryEntity getmemberCategoryEntity : memberCategoryEntity) {
				MemberCategoryModuleEntity memberCategoryModuleEntity=new MemberCategoryModuleEntity();
				memberCategoryModuleEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
				memberCategoryModuleEntity.setMemberCategoryId(getmemberCategoryEntity.getId());
				memberCategoryModuleEntity.setPolicyId(getmemberCategoryEntity.getPmstPolicyId());
				memberCategoryModuleEntity.setCreatedBy(getmemberCategoryEntity.getCreatedBy());
				memberCategoryModuleEntity.setCreatedDate(new Date());
				memberCategoryModuleEntity.setIsActive(true);
				addMemberCategoryModuleEntity.add(memberCategoryModuleEntity);
			}
			memberCategoryModuleRepository.saveAll(addMemberCategoryModuleEntity);
			

			List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
					.findByPolicyId(quotationRenewalDto.getPolicyId());
			List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
					.copyToTmpLifeCoverforClaim(policyLifeCoverEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());

			renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
//		policyLifeCoverRepository.updateISActive(quotationRenewalDto.getPolicyId());
			// member category

			// Gratuity

			List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
					.findBypolicyId(quotationRenewalDto.getPolicyId());
			List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
					.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());

			renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
//		policyGratuityBenefitRepository.updateIsActive(quotationRenewalDto.getPolicyId());

			// valuation
			Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
					.findByPolicyId(quotationRenewalDto.getPolicyId());
			if (policyValuationEntity.isPresent()) {
				RenewalValuationTMPEntity renewalValuationTMPEntity = RenewalValuationTMPHelper
						.pmsttoTmp(policyValuationEntity.get());
				renewalValuationTMPEntity.setId(null);
				renewalValuationTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
				renewalValuationTMPEntity.setPmstValuationId(policyValuationEntity.get().getId());
				renewalValuationTMPRepository.save(renewalValuationTMPEntity);
			}

			// valuation Matrix
			Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
					.findByPolicyId(quotationRenewalDto.getPolicyId());
			if (policyValuationMatrixEntity.isPresent()) {
				RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = RenewalValuationMatrixTMPHelper
						.pmsttoTmp(policyValuationMatrixEntity.get());
				renewalValuationMatrixTMPEntity.setId(null);
				renewalValuationMatrixTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
				renewalValuationMatrixTMPEntity.setPmstValuationMatrixId(policyValuationMatrixEntity.get().getId());
				renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

				if (renewalValuationMatrixTMPEntity.getValuationTypeId() == 1) {
					Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
							.findByPolicyId(quotationRenewalDto.getPolicyId());
					if (policyValutationBasicEntity.isPresent()) {
						RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = RenewalValuationBasicTMPHelper
								.pmsttoTmp(policyValutationBasicEntity.get());
						renewalValuationBasicTMPEntity.setId(null);
						renewalValuationBasicTMPEntity.setRateTable(null);
						//					renewalValuationBasicTMPEntity.setValuationTypeId(renewalValuationTypeId);
						renewalValuationBasicTMPEntity
								.setPmstValuationBasicId(policyValutationBasicEntity.get().getId());
						renewalValuationBasicTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
						renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

						// ValuationWithdrawalRate table
						List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
								.findByPolicyId(quotationRenewalDto.getPolicyId());
						if (policyValuationWithdrawalRateEntity.size() > 0) {
							List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = new ArrayList<RenewalValuationWithdrawalTMPEntity>();
							for (PolicyValuationWithdrawalRateEntity getpolicyValuationWithdrawalRateEntity : policyValuationWithdrawalRateEntity) {
								RenewalValuationWithdrawalTMPEntity newrenewalValuationWithdrawalTMPEntity = RenewalValuationTMPHelper
										.pmsttoTmp(getpolicyValuationWithdrawalRateEntity);
								newrenewalValuationWithdrawalTMPEntity.setId(null);
								newrenewalValuationWithdrawalTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
								newrenewalValuationWithdrawalTMPEntity
										.setPmstValWithDrawalId(getpolicyValuationWithdrawalRateEntity.getId());
								renewalValuationWithdrawalTMPEntity.add(newrenewalValuationWithdrawalTMPEntity);
							}
							renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);
						}
					}
				}
			}
			taskAllocationEntity.setTaskStatus(quotationStatusId.toString());
			taskAllocationEntity.setRequestId(getQuotationNumber);
			taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//			taskAllocationEntity.setLocationType(quotationRenewalDto.getUnitCode());
			taskAllocationEntity.setCreatedBy(quotationRenewalDto.getCreatedBy());
			taskAllocationEntity.setCreatedDate(new Date());
			taskAllocationEntity.setModulePrimaryId(renewalPolicyTMPEntity.getId());
			taskAllocationEntity.setIsActive(true);
			taskAllocationRepository.save(taskAllocationEntity);
			

			return ApiResponseDto.success(RenewalValuationTMPHelper.entityToDto(renewalPolicyTMPEntity));
		}
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> updaterenewalquotationbasic(Long id,
			QuotationRenewalDto quotationRenewalDto) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();

		renewalPolicyTMPEntity.setQuotationDate(quotationRenewalDto.getQuotationDate());
		renewalPolicyTMPEntity.setModifiedBy("maker");
		renewalPolicyTMPEntity.setModifiedDate(new Date());

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		return ApiResponseDto.success(RenewalPolicyTMPHelper.entityToDto(renewalPolicyTMPEntity));
	}

	public ApiResponseDto<RenewalPolicyTMPDto> findByTMPId(Long id) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();
		RenewalPolicyTMPDto renewalPolicyTMPDto = RenewalPolicyTMPHelper.entityToDto(renewalPolicyTMPEntity);
		renewalPolicyTMPDto.setTmpPolicyId(renewalPolicyTMPEntity.getTmpPolicyId());
		return ApiResponseDto.success(renewalPolicyTMPDto);
	}

	public ApiResponseDto<TemptMPHDto> findByTempMPHIdonTempId(Long id) {
//		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();
//		renewalPolicyTMPEntity.getMasterpolicyHolder();
		TempMPHEntity tempMPHEntity = tempMPHRepository.findBytmpPolicyId(id).get();
		TemptMPHDto temptMPHDto = MPHHelper.tmpentityToDto(tempMPHEntity);
		return ApiResponseDto.success(temptMPHDto);
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> sentForApproval(Long id, String username) {

		Long qutationStatusId1 = 17L;
		Long qutationSubStatusId1 = 21L;
		Long qutationTaggedStatuss = 78L;

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());
		renewalPolicyTMPEntity.setQuotationStatusId(qutationStatusId1);
		renewalPolicyTMPEntity.setQuotationSubStatusId(qutationSubStatusId1);
		renewalPolicyTMPEntity.setQuotationTaggedStatusId(qutationTaggedStatuss);

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		
//		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getQuotationNumber());
//		taskallocationentity.setTaskStatus(qutationSubStatusId1.toString());
//		taskAllocationRepository.save(taskallocationentity);


		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> sendBacktoMaker(Long id, String username) {

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());
		renewalPolicyTMPEntity.setQuotationStatusId(Long.parseLong(defaultStatusId));
		renewalPolicyTMPEntity.setQuotationSubStatusId(Long.parseLong(sentBackToMakerSubStatudId));
		renewalPolicyTMPEntity.setQuotationTaggedStatusId(Long.parseLong(defaultTaggedStatusId));

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		
		// TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getQuotationNumber());
		// taskallocationentity.setTaskStatus(sentBackToMakerSubStatudId.toString());
		// taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Transactional
	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> forApprove(Long tempPolicyId, String username) {

		Long qutationStatusId1 = 20L;
		Long qutationSubStatusId1 = 85L;

		Long taggedStatusId = 79L;

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tempPolicyId).get();
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());

		renewalPolicyTMPEntity.setQuotationStatusId(qutationStatusId1);
		renewalPolicyTMPEntity.setQuotationSubStatusId(qutationSubStatusId1);
		renewalPolicyTMPEntity.setQuotationTaggedStatusId(taggedStatusId);

		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		
//		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getQuotationNumber());
//		taskallocationentity.setTaskStatus(qutationSubStatusId1.toString());
//		taskAllocationRepository.save(taskallocationentity);

		
		renewalPolicyTMPRepository.deactiveotherpolicydetail(renewalPolicyTMPEntity.getPolicyNumber(),renewalPolicyTMPEntity.getId());
		// create payments to be received against master quotation
		Double oYRGTARenewalPremium = 0D;
		Double lateFee = 0D;
		Double lateFeeGSTAmount = 0D;
		Double currentServiceLiability = 0D;
		Double pastServiceLiabilty = 0D;
		// Not Way Correct need to run store Procedure
		List<GratuityCalculationEntity> gratuityCalculationEntities = gratuityCalculationRepository
				.findAllByTmpMemberId(tempPolicyId);
		for (GratuityCalculationEntity gratuityCalculationEntity : gratuityCalculationEntities) {
			oYRGTARenewalPremium = oYRGTARenewalPremium
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
			pastServiceLiabilty = pastServiceLiabilty
					+ +(gratuityCalculationEntity.getPastServiceBenefitDeath() != null
							? gratuityCalculationEntity.getPastServiceBenefitDeath()
							: 0.0D)
					+ (gratuityCalculationEntity.getPastServiceBenefitRet() != null
							? gratuityCalculationEntity.getPastServiceBenefitRet()
							: 0.0D)
					+ (gratuityCalculationEntity.getPastServiceBenefitWdl() != null
							? gratuityCalculationEntity.getPastServiceBenefitWdl()
							: 0.0D);

//					futureServiceLiability = futureServiceLiability
//							+ (gratuityCalculationEntity.getTerm() != null ? gratuityCalculationEntity.getTerm() : 0.0F);
		}

		Double gstAmount = 0.0D;
		if (renewalPolicyTMPEntity.getGstApplicableId().equals(1L)) {
			StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
			gstAmount = oYRGTARenewalPremium * (Float.parseFloat(standardCodeEntity.getValue()) / 100);
		}

		List<QuotationChargeEntity> quotationChargeEntities = new ArrayList<QuotationChargeEntity>();
		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
				.chargeTypeId(215L).amountCharged(oYRGTARenewalPremium).balanceAmount(0).isActive(true)
				.createdBy(username).createdDate(new Date()).build());
		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
				.chargeTypeId(216L).amountCharged(gstAmount).balanceAmount(0).isActive(true).createdBy(username)
				.createdDate(new Date()).build());

		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
				.chargeTypeId(220L).amountCharged(lateFee).balanceAmount(0).isActive(true).createdBy(username)
				.createdDate(new Date()).build());
		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
				.chargeTypeId(217L).amountCharged(lateFeeGSTAmount).balanceAmount(0).isActive(true).createdBy(username)
				.createdDate(new Date()).build());

		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
				.chargeTypeId(218L).amountCharged(pastServiceLiabilty).balanceAmount(0).isActive(true)
				.createdBy(username).createdDate(new Date()).build());
		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
				.chargeTypeId(219L).amountCharged(currentServiceLiability).balanceAmount(0).isActive(true)
				.createdBy(username).createdDate(new Date()).build());

//				quotationChargeEntities.add(QuotationChargeEntity.builder().masterQuotationId(renewalPolicyTMPEntity.getId())
//						.chargeTypeId(145L).amountCharged(futureServiceLiability).balanceAmount(0).isActive(true)
//						.createdBy(username).createdDate(new Date()).build());
		quotationChargeRepository.saveAll(quotationChargeEntities);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> forReject(Long id, String username) {

		Long qutationStatusId1 = 18L;
		Long qutationSubStatusId1 = 84L;
		Long taggedStatusId = 79L;

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());
		renewalPolicyTMPEntity.setQuotationStatusId(qutationStatusId1);
		renewalPolicyTMPEntity.setQuotationSubStatusId(qutationSubStatusId1);

		renewalPolicyTMPEntity.setQuotationTaggedStatusId(taggedStatusId);
		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		
		// TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getQuotationNumber());
		// taskallocationentity.setTaskStatus(sentBackToMakerSubStatudId.toString());
		// taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyForApproval(Long id, String username) {

		Long qutationStatusId1 = 143L;
		Long qutationStatusId2 = 133L;

		Long qutationTaggedStatuss = 138L;

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());
		renewalPolicyTMPEntity.setPolicyStatusId(qutationStatusId1);
		renewalPolicyTMPEntity.setPolicySubStatusId(qutationStatusId2);
		renewalPolicyTMPEntity.setPolicytaggedStatusId(qutationTaggedStatuss);

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		
//		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getPolicyNumber());
//		taskallocationentity.setTaskStatus(qutationStatusId2.toString());
//		taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyBacktoMaker(Long id, String username) {

		Long policyStatusId2 = 132L;
		Long policysubStatusId1 = 134L;
		Long policyTaggedStatuss = 138L;

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());
		renewalPolicyTMPEntity.setPolicyStatusId(policyStatusId2);
		renewalPolicyTMPEntity.setPolicySubStatusId(policysubStatusId1);

		renewalPolicyTMPEntity.setPolicytaggedStatusId(policyTaggedStatuss);

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		
//		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getPolicyNumber());
//		taskallocationentity.setTaskStatus(policysubStatusId1.toString());
//		taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforApprove(Long tempid, String username) {

		Long policyStatusId2 = 123L;
		Long policysubStatusId1 = 136L;
		Long policyTaggedStatuss = 139L;

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tempid).get();
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());
		renewalPolicyTMPEntity.setPolicyStatusId(policyStatusId2);
		renewalPolicyTMPEntity.setPolicySubStatusId(policysubStatusId1);

		renewalPolicyTMPEntity.setPolicytaggedStatusId(policyTaggedStatuss);

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		
//		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getPolicyNumber());
//		taskallocationentity.setTaskStatus(policysubStatusId1.toString());
//		taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforReject(Long id, String username,
			RenewalPolicyTMPDto renewalPolicyTMPDto) {

		Long policyStatusId2 = 144L;
		Long policysubStatusId1 = 135L;
		Long policyTaggedStatuss = 139L;

		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository.findBytmpPolicyId(id);
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();
		if (isDevEnvironmentForRenewals == false) {
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

		renewalPolicyTMPEntity.setPolicyStatusId(policyStatusId2);
		renewalPolicyTMPEntity.setPolicySubStatusId(policysubStatusId1);

		renewalPolicyTMPEntity.setPolicytaggedStatusId(policyTaggedStatuss);

		renewalPolicyTMPEntity.setRejectedReason(renewalPolicyTMPDto.getRejectedReason());
		renewalPolicyTMPEntity.setRejectedRemarks(renewalPolicyTMPDto.getRejectedRemarks());
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());
		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		
		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getPolicyNumber());
		taskallocationentity.setTaskStatus(policysubStatusId1.toString());
		taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Override
	public byte[] findByPolicyId(Long tmpPolicyId) {

		List<Object[]> policyMasterData = renewalPolicyHelper.getPolicyMasterDataForExcel(tmpPolicyId);

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		MemberErrorWorkbookHelper memberErrorWorkbookHelper = new MemberErrorWorkbookHelper();
		memberErrorWorkbookHelper.createHeaderForCandBSheet(workbook, sheet, false);
		memberErrorWorkbookHelper.createDetailRow(workbook, sheet, policyMasterData);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			workbook.write(out);
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> getpolicyTmpId(Long id) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();

		return ApiResponseDto.success(RenewalPolicyTMPHelper.entityToDto(renewalPolicyTMPEntity));
	}

	@Transactional
	@Override
	public ApiResponseDto<PolicyDto> renewalpolicyapprove(Long tempid, String username) {

	
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tempid).get();

		renewalPolicyTMPEntity.setPolicyStatusId(approvedStatudId);
		renewalPolicyTMPEntity.setPolicySubStatusId(approvedSubStatudId);

		renewalPolicyTMPEntity.setPolicytaggedStatusId(existingTaggedStatusId);
		
		
		

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		
		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getPolicyNumber());
		taskallocationentity.setTaskStatus(approvedSubStatudId.toString());
		taskAllocationRepository.save(taskallocationentity);

		MasterPolicyEntity getMasterPolicyEntity = policyMastertoPolicyHis(renewalPolicyTMPEntity, username);
		getMasterPolicyEntity.setPolicyStatusId(approvedStatudId);
		getMasterPolicyEntity.setPolicyTaggedStatusId(existingTaggedStatusId);
		masterPolicyRepository.save(getMasterPolicyEntity);
		// New

		if(isDevEnvironmentForRenewals==false) {
		String prodAndVarientCodeSame = commonModuleService.getProductCode(getMasterPolicyEntity.getProductId());
		String unitStateName = commonMasterUnitRepository.getStateNameByUnitCode(getMasterPolicyEntity.getUnitCode());
		String unitStateType = commonMasterStateRepository.getStateType(unitStateName);
		String unitStateCode = commonMasterStateRepository.getStateCode(unitStateName);

		String mPHStateType = null;
		String mPHAddress = "";
		String mphStateCode = "";

		MPHEntity mPHEntity = mPHRepository.findById(getMasterPolicyEntity.getMasterpolicyHolder()).get();
		for (MPHAddressEntity mphAddressEntity : mPHEntity.getMphAddresses()) {
			mPHStateType = commonMasterStateRepository.getStateType(mphAddressEntity.getStateName());
			mphStateCode = commonMasterStateRepository.getStateCode(mphAddressEntity.getStateName());
		}

			PolicyContributionDetailEntity masterPolicyContributionEntity = policyContributionDetailRepository
				.findBymasterPolicyIdandType(tempid);

		HSNCodeDto hSNCodeDto = accountingService.getHSNCode();
		Map<String, Double> gstComponents = accountingService.getGstComponents(unitStateType, mPHStateType, hSNCodeDto,
				masterPolicyContributionEntity.getLifePremium());

		RenewalContributionAdjustRequestModelDto renewalContributionAdjustRequestModelDto = new RenewalContributionAdjustRequestModelDto();
		renewalContributionAdjustRequestModelDto.setAdjustmentNo(masterPolicyContributionEntity.getId().intValue());
		renewalContributionAdjustRequestModelDto.setAdjustmentAmount((int) (masterPolicyContributionEntity.getLifePremium()
				+ masterPolicyContributionEntity.getGst() + masterPolicyContributionEntity.getCurrentServices()
				+ masterPolicyContributionEntity.getPastService()));
		renewalContributionAdjustRequestModelDto.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
		renewalContributionAdjustRequestModelDto.setMphCode(mPHEntity.getMphCode());
		renewalContributionAdjustRequestModelDto.setIsGstApplicable(false);
		renewalContributionAdjustRequestModelDto.setUnitCode(getMasterPolicyEntity.getUnitCode());
		renewalContributionAdjustRequestModelDto.setUserCode(username);
		
		

		renewalContributionAdjustRequestModelDto.setGlTransactionModel(accountingService.getGlTransactionModel(
				getMasterPolicyEntity.getProductId(), getMasterPolicyEntity.getProductVariantId(),
				getMasterPolicyEntity.getUnitCode(), "GratuityPolicyApproval"));

		ResponseDto responseDto = commonmasterserviceUnitByCode(getMasterPolicyEntity.getUnitCode());

		GstDetailModelDto gstDetailModel = new GstDetailModelDto();
		gstDetailModel.setAmountWithTax(masterPolicyContributionEntity.getLifePremium()
				+ masterPolicyContributionEntity.getGst() + masterPolicyContributionEntity.getCurrentServices()
				+ masterPolicyContributionEntity.getPastService().doubleValue());
		gstDetailModel.setAmountWithoutTax(masterPolicyContributionEntity.getLifePremium()
			    + masterPolicyContributionEntity.getCurrentServices() + masterPolicyContributionEntity.getPastService().doubleValue());
		gstDetailModel.setCessAmount(0.0); // from Docu
		gstDetailModel.setCgstAmount(gstComponents.get("CGST"));
		gstDetailModel.setCgstRate(hSNCodeDto.getCgstRate());
		gstDetailModel.setCreatedBy(username);
		gstDetailModel.setCreatedDate(new Date());
		gstDetailModel.setEffectiveEndDate(""); // form docu
		gstDetailModel.setEffectiveStartDate(new Date());
		gstDetailModel.setEntryType("L");
		gstDetailModel.setFromGstn(mPHEntity.getGstIn() == null ? "" : mPHEntity.getGstIn());
		gstDetailModel.setFromPan(mPHEntity.getPan() == null ? "" : mPHEntity.getPan());
		gstDetailModel.setFromStateCode(mphStateCode == null ? "" : mphStateCode); // from MPH detail null

		if (getMasterPolicyEntity.getGstApplicableId() == 1l)
			gstDetailModel.setGstApplicableType("Taxable");
		else
			gstDetailModel.setGstApplicableType("Non-Taxable");

		gstDetailModel.setGstInvoiceNo(""); // From Docu
		gstDetailModel.setGstRate((hSNCodeDto.getCgstRate() + hSNCodeDto.getSgstRate() + hSNCodeDto.getUtgstRate()
				+ hSNCodeDto.getIgstRate()));
		gstDetailModel.setGstRefNo("2301212");// From Docu
		gstDetailModel.setGstRefTransactionNo("9012371");// From Docu
		gstDetailModel.setGstTransactionType("K");// From Docu
		gstDetailModel.setGstType("K");// From Docu
		gstDetailModel.setHsnCode(hSNCodeDto.getHsnCode());
		gstDetailModel.setIgstAmount(gstComponents.get("IGST"));
		gstDetailModel.setIgstRate(hSNCodeDto.getIgstRate());
		gstDetailModel.setModifiedBy(0L); // from docu
		gstDetailModel.setModifiedDate(new Date()); // from Docu
		gstDetailModel.setMphAddress(mPHAddress);
		gstDetailModel.setMphName(mPHEntity.getMphName());
		gstDetailModel.setNatureOfTransaction("Gratuity RE");
		gstDetailModel.setOldInvoiceDate(new Date()); // From Docu
		gstDetailModel.setOldInvoiceNo("IN20123QE"); // From Docu
		gstDetailModel.setProductCode(prodAndVarientCodeSame);
		gstDetailModel.setRemarks("Gratuity RE Deposit Adjustment");
		gstDetailModel.setSgstAmount(gstComponents.get("SGST"));
		gstDetailModel.setSgstRate(hSNCodeDto.getSgstRate());
		gstDetailModel.setToGstIn(responseDto.getGstIn() == null ? "8347" : responseDto.getGstIn()); // From Docu
																										// from get
																										// Common Module
		gstDetailModel.setToPan(responseDto.getPan());
		gstDetailModel.setToStateCode(unitStateCode);
		gstDetailModel.setTotalGstAmount(masterPolicyContributionEntity.getGst().doubleValue());
		gstDetailModel.setTransactionDate(new Date());
		gstDetailModel.setTransactionSubType("A"); // From Docu
		gstDetailModel.setTransactionType("C"); // From Docu
//		gstDetailModel.setUserCode(username);
		gstDetailModel.setUtgstAmount(gstComponents.get("UTGST"));

		gstDetailModel.setUtgstRate(hSNCodeDto.getUtgstRate());
		gstDetailModel.setVariantCode(prodAndVarientCodeSame);
		gstDetailModel.setYear(DateUtils.uniqueNoYYYY());
		gstDetailModel.setMonth(DateUtils.currentMonthName());
		renewalContributionAdjustRequestModelDto.setGstDetailModel(gstDetailModel);

		JournalVoucherDetailModelDto journalVoucherDetailModelDto = new JournalVoucherDetailModelDto();
		journalVoucherDetailModelDto.setLineOfBusiness(getMasterPolicyEntity.getLineOfBusiness());
		journalVoucherDetailModelDto.setProduct(prodAndVarientCodeSame);
		journalVoucherDetailModelDto.setProductVariant(prodAndVarientCodeSame);
		renewalContributionAdjustRequestModelDto.setJournalVoucherDetailModel(journalVoucherDetailModelDto);
		
		
		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository.findBymasterPolicyIdandType(tempid);
		List<RenewalContrinAdjustDebitCreditRequestModelDto> getRenewalContrinAdjustDebitCreditRequestModelDto =new ArrayList<RenewalContrinAdjustDebitCreditRequestModelDto>();
		Double premiumAdjustment = masterPolicyContributionEntity.getLifePremium().doubleValue();
		Double gstOnPremiumAdjusted = masterPolicyContributionEntity.getGst().doubleValue();
		Double currentServiceAdjusted = masterPolicyContributionEntity.getCurrentServices().doubleValue();
		Double pastServiceAdjusted = masterPolicyContributionEntity.getPastService().doubleValue();
		
		for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {
			RenewalContrinAdjustDebitCreditRequestModelDto newRenewalContrinAdjustDebitCreditRequestModelDto=new RenewalContrinAdjustDebitCreditRequestModelDto();
			newRenewalContrinAdjustDebitCreditRequestModelDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
			Double availableAmount = policyDepositEntity.getAvailableAmount().doubleValue();
			Double depositDebitAmount = 0.0;
			
			if (premiumAdjustment > 0 && availableAmount > 0) {
				if (masterPolicyContributionEntity.getLifePremium() <= policyDepositEntity.getAvailableAmount()) {
					newRenewalContrinAdjustDebitCreditRequestModelDto.setOYRGTARenewalPremiumCreditAmount(masterPolicyContributionEntity.getLifePremium());
	availableAmount -= masterPolicyContributionEntity.getLifePremium();
					depositDebitAmount += masterPolicyContributionEntity.getLifePremium();
				} else {
					newRenewalContrinAdjustDebitCreditRequestModelDto.setOYRGTARenewalPremiumCreditAmount(policyDepositEntity.getAvailableAmount());

					premiumAdjustment -= policyDepositEntity.getAvailableAmount();
					availableAmount -= policyDepositEntity.getAvailableAmount();
					depositDebitAmount += policyDepositEntity.getAvailableAmount();
				}
			}
			
			if (gstOnPremiumAdjusted > 0 && availableAmount > 0) {
				
				
				if (masterPolicyContributionEntity.getGst() <= policyDepositEntity.getAvailableAmount()) {
					newRenewalContrinAdjustDebitCreditRequestModelDto.setGstOnPremiumCreditAmount(masterPolicyContributionEntity.getGst());
					
					availableAmount -= masterPolicyContributionEntity.getGst();
					depositDebitAmount += masterPolicyContributionEntity.getGst();
				} else {
					newRenewalContrinAdjustDebitCreditRequestModelDto.setGstOnPremiumCreditAmount(policyDepositEntity.getAvailableAmount());
				
					gstOnPremiumAdjusted -= policyDepositEntity.getAvailableAmount();
					availableAmount -= policyDepositEntity.getAvailableAmount();
					depositDebitAmount += policyDepositEntity.getAvailableAmount();
				}
			}
			
			if (currentServiceAdjusted > 0 && availableAmount > 0) {
				if (masterPolicyContributionEntity.getPastService() <= policyDepositEntity.getAvailableAmount()) {
					newRenewalContrinAdjustDebitCreditRequestModelDto.setRenewalPremiumOtherscreditAmount(masterPolicyContributionEntity.getPastService());
					availableAmount -= masterPolicyContributionEntity.getPastService();
					depositDebitAmount += masterPolicyContributionEntity.getPastService();
				} else {
					newRenewalContrinAdjustDebitCreditRequestModelDto.setRenewalPremiumOtherscreditAmount(policyDepositEntity.getAvailableAmount());
					currentServiceAdjusted -= policyDepositEntity.getAvailableAmount();
					availableAmount -= policyDepositEntity.getAvailableAmount();
					depositDebitAmount += policyDepositEntity.getAvailableAmount();
				}
			}
			
			if (pastServiceAdjusted > 0 && availableAmount > 0) {
				if (masterPolicyContributionEntity.getCurrentServices() <= policyDepositEntity.getAvailableAmount()) {
					newRenewalContrinAdjustDebitCreditRequestModelDto.setSinglePremiumSubsequentCreditAmount(masterPolicyContributionEntity.getCurrentServices());
					availableAmount -= masterPolicyContributionEntity.getCurrentServices();
					depositDebitAmount += masterPolicyContributionEntity.getCurrentServices();
				} else {
					newRenewalContrinAdjustDebitCreditRequestModelDto.setSinglePremiumSubsequentCreditAmount(policyDepositEntity.getAvailableAmount());
					pastServiceAdjusted -= policyDepositEntity.getAvailableAmount();
					availableAmount -= policyDepositEntity.getAvailableAmount();
					depositDebitAmount += policyDepositEntity.getAvailableAmount();
				}
			}
			
			newRenewalContrinAdjustDebitCreditRequestModelDto.setDepositDebitAmount(depositDebitAmount);
			getRenewalContrinAdjustDebitCreditRequestModelDto.add(newRenewalContrinAdjustDebitCreditRequestModelDto);
			
			}
		
		
		renewalContributionAdjustRequestModelDto.setRenewalDebitCreditRequestModel(getRenewalContrinAdjustDebitCreditRequestModelDto);
		renewalContributionAdjustRequestModelDto.setRefNo(masterPolicyContributionEntity.getId().toString());
		accountingService.consumeDeposit(renewalContributionAdjustRequestModelDto,getMasterPolicyEntity.getId());
		
		}

		return ApiResponseDto.success(PolicyHelper.entityToDto(getMasterPolicyEntity));
	}

	private MasterPolicyEntity policyMastertoPolicyHis(RenewalPolicyTMPEntity funcrenewalPolicyTMPEntity,
			String username) {
		MasterPolicyEntity newmasterPolicyEntity = null;
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = null;
		PolicyHistoryEntity policyHistoryEntity = null;
		// PolicyMaster to Copy History then Tmp Policy to Master Policy Update
		Optional<MasterPolicyEntity> masterPolicyEntity = masterPolicyRepository
				.findById(funcrenewalPolicyTMPEntity.getMasterPolicyId());

		if (masterPolicyEntity.isPresent()) {
			MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.get().getMasterpolicyHolder()).get();
			// MPH UPDATE from MAster to History update
			HistoryMPHEntity hisMPHEntity = PolicyClaimCommonHelper.copytomastertohis(mPHEntity);
			hisMPHEntity = historyMPHRepository.save(hisMPHEntity);
			// MPH UPDATE from MAster to History End
			renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(funcrenewalPolicyTMPEntity.getId()).get();
			policyHistoryEntity = policyHistoryRepository.save(
					PolicyHelper.policymasterToHisTransfer(masterPolicyEntity.get(), renewalPolicyTMPEntity, username));

			policyHistoryEntity.setMasterpolicyHolder(hisMPHEntity.getId());
			policyHistoryRepository.save(policyHistoryEntity);

			newmasterPolicyEntity = PolicyHelper.updateTemptoPolicyMaster(renewalPolicyTMPEntity, username);
			// MPH UPDATE from TEMP to MAster start
			TempMPHEntity tempMPHentity = tempMPHRepository.findById(renewalPolicyTMPEntity.getMasterpolicyHolder())
					.get();
			MPHEntity newmPHEntity = mPHRepository.save(PolicyClaimCommonHelper.copytotemptomaster(tempMPHentity));
			newmasterPolicyEntity.setMasterpolicyHolder(newmPHEntity.getId());
			// MPH UPDATE from TEMP to MAster
			newmasterPolicyEntity = masterPolicyRepository.save(newmasterPolicyEntity);
		}

		// PolicymasterSchemeRule to Copy History scheme rule then Update TmpScheme to
		// PolicyScheme rule
		Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
				.findBypolicyId(funcrenewalPolicyTMPEntity.getMasterPolicyId());
		if (policySchemeEntity.isPresent()) {

			policySchemeRuleHistoryRepository.save(PolicySchemeRuleHelper
					.policyMastertoHistransfer(policySchemeEntity.get(), policyHistoryEntity.getId(), username));

			RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = renewalSchemeruleTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			PolicySchemeEntity newPolicySchemeEntity = PolicySchemeRuleHelper
					.updateTempToSchemeMaster(renewalSchemeruleTMPEntity, username);
			policySchemeRuleRepository.save(newPolicySchemeEntity);
		}

		// PolicymasterLifeCoverRule to Copy HistoryLifeCOver then Update TmpLifeCover
		// to PolicymasterLifeCoverRule
		memberCategoryRepository.UpdateHistoryfromMasterPolicy(funcrenewalPolicyTMPEntity.getMasterPolicyId(),policyHistoryEntity.getId());
		
		List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
				.findByPolicyId(funcrenewalPolicyTMPEntity.getMasterPolicyId());
		List<PolicyLifeCoverEntity> policyLifeCoverEntities = new ArrayList<PolicyLifeCoverEntity>();
		for (PolicyLifeCoverEntity addPolicyLifeCoverEntity : policyLifeCoverEntity) {

			historyLifeCoverRepository.save(PolicyLifeCoverHelper.policyMastertoHistransfer(addPolicyLifeCoverEntity,
					policyHistoryEntity.getId(), username));
		}

		
		memberCategoryRepository.updateTmpCategorytoMasterPolicy(renewalPolicyTMPEntity.getId(),newmasterPolicyEntity.getId());
		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = renewalLifeCoverTMPRepository
				.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
		for (RenewalLifeCoverTMPEntity addrenewalLifeCoverTMPEntity : renewalLifeCoverTMPEntity) {
			PolicyLifeCoverEntity newPolicyLifeCoverEntity = PolicyLifeCoverHelper
					.updateTempToLifecoverMaster(addrenewalLifeCoverTMPEntity, username, newmasterPolicyEntity.getId());
			policyLifeCoverEntities.add(newPolicyLifeCoverEntity);
		}
		policyLifeCoverRepository.saveAll(policyLifeCoverEntities);

		List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
				.findBypolicyId(funcrenewalPolicyTMPEntity.getMasterPolicyId());
		List<PolicyGratuityBenefitEntity> PolicyGratuityBenefitEntities = new ArrayList<PolicyGratuityBenefitEntity>();

		if (policyGratuityBenefitEntity.size() > 0) {
			List<HistoryGratuityBenefitEntity> historyGratuityBenefitEntity = new ArrayList<HistoryGratuityBenefitEntity>();
			for (PolicyGratuityBenefitEntity getpolicyGratuityBenefitEntity : policyGratuityBenefitEntity) {
				historyGratuityBenefitEntity.add(PolicyGratuityBenefitHelper.policyMastertoHistransfer(
						getpolicyGratuityBenefitEntity, policyHistoryEntity.getId(), username));
			}
			historyGratutiyBenefitRepository.saveAll(historyGratuityBenefitEntity);

			List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = renewalGratuityBenefitTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			for (RenewalGratuityBenefitTMPEntity addrenewalGratuityBenefitTMPEntity : renewalGratuityBenefitTMPEntity) {
				PolicyGratuityBenefitEntity newPolicyGratuityBenefitEntity = RenewalGratuityBenefitTMPHelper
						.updateTempToPolicyGratuityBenefitMaster(addrenewalGratuityBenefitTMPEntity, username,
								newmasterPolicyEntity.getId());
				PolicyGratuityBenefitEntities.add(newPolicyGratuityBenefitEntity);
			}
			policyGratuityBenefitRepository.saveAll(PolicyGratuityBenefitEntities);

		}

		// PolicMasterValuation to copy ValuationHistory upodate RenewalvaluationTMP to
		// PolicMasterValuation
		Optional<PolicyMasterValuationEntity> policyMasterValuationEntity = policyMasterValuationRepository
				.findBypolicyId(funcrenewalPolicyTMPEntity.getMasterPolicyId());
		if (policyMasterValuationEntity.isPresent()) {
			policyValuationHistoryRepository.save(PolicyValuationHelper.policyMastertoHistransfer(
					policyMasterValuationEntity.get(), policyHistoryEntity.getId(), username));

			RenewalValuationTMPEntity renewalValuationTMPEntity = renewalValuationTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			PolicyMasterValuationEntity newPolicyMasterValuationEntity = PolicyValuationHelper
					.updateTempToValuationMaster(renewalValuationTMPEntity, username);
			newPolicyMasterValuationEntity.setPolicyId(newmasterPolicyEntity.getId());
			;
			policyMasterValuationRepository.save(newPolicyMasterValuationEntity);
		}

		//isactive false same masterpolicy for  NB
		masterPolicyContributionDetailRepository.isacivefalse(newmasterPolicyEntity.getId());
		
		// Copy to MasterPolicycontributionDetails
		MasterPolicyContributionDetails copyToMaster = PolicyHelper.entityToMaster(
				policyContributionDetailRepository.findBytmpPolicyId(renewalPolicyTMPEntity.getId()),
				newmasterPolicyEntity.getId(),username);

		copyToMaster = masterPolicyContributionDetailRepository.save(copyToMaster);

		
		masterPolicyDepositRepository.isactivefalse(newmasterPolicyEntity.getId());
		// copy Deposit to Master data
		List<MasterPolicyDepositEntity> masterPolicyDepositEntity = PolicyHelper.entityToPolicyMater(
				policyDepositRepository.findBytmpPolicyId(renewalPolicyTMPEntity.getId()),
				newmasterPolicyEntity.getId(), copyToMaster,username);
		masterPolicyDepositRepository.saveAll(masterPolicyDepositEntity);

		
		
		masterPolicyContributionRepository.isactivefalse(newmasterPolicyEntity.getId());
		// copy Deposit to MasterPolicyContributionEntity
		List<MasterPolicyContributionEntity> masterPolicyContributionEntity = PolicyHelper.entityToPolicyConMaster(
				policyContributionRepository.findBytmpPolicyId(renewalPolicyTMPEntity.getId()),
				newmasterPolicyEntity.getId(), copyToMaster,username);
		masterPolicyContributionRepository.saveAll(masterPolicyContributionEntity);

		
		masterPolicyContrySummaryRepository.isactivefalse(newmasterPolicyEntity.getId());
		// copy Deposit to masterPolicyContrySummaryEntity
		List<MasterPolicyContrySummaryEntity> masterPolicyContrySummaryEntity = PolicyHelper
				.entityToPolicySummaryMaster(
						policyContrySummaryRepository.findBytmpPolicyId(renewalPolicyTMPEntity.getId()),
						newmasterPolicyEntity.getId(), copyToMaster,username);
		masterPolicyContrySummaryRepository.saveAll(masterPolicyContrySummaryEntity);

		// Member Renewal Entity start
		try {
			List<RenewalPolicyTMPMemberEntity> tempMemberEntity = renewalPolicyTMPMemberRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			Integer licId = policyMemberRepository.maxLicNumber(renewalPolicyTMPEntity.getMasterPolicyId());
			List<PolicyMemberEntity> policyMemberEntityList =policyMemberRepository.findBypolicyId(renewalPolicyTMPEntity.getMasterPolicyId());
					for (PolicyMemberEntity newMemberEntity : policyMemberEntityList) {
						PolicyMemberEntity policyMemberEntity = null;
				policyMemberEntity = policyMemberRepository.findById(newMemberEntity.getId()).get();
				historyMemberRepository
						.save(PolicyMemberHelper.policymastertohistoryupdate(policyMemberEntity,policyHistoryEntity.getId(),username));
			
			}
			int LicInc = 1;
			for (RenewalPolicyTMPMemberEntity newTempMemberEntity : tempMemberEntity) {
				PolicyMemberEntity policyMemberEntity = null;
			
				if (newTempMemberEntity.getPmstMemebrId() == null) {
					licId = LicInc + licId.intValue();
					Long LicinLong = Long.valueOf(licId);
					newTempMemberEntity.setLicId(LicinLong.toString());
					newTempMemberEntity.setPolicyId(newmasterPolicyEntity.getId());
				}

				policyMemberEntity = RenewalPolicyTMPMemberHelper.updateTemptoPolicyMemberMaster(newTempMemberEntity,
						username);
				policyMemberRepository.save(policyMemberEntity);
			}
			
			 policyMemberRepository.updatemasternotprocessedrenewalmemberinactive(renewalPolicyTMPEntity.getId(),renewalPolicyTMPEntity.getMasterPolicyId());
			 policyMemberRepository.updaterenewalupdatememberinmasteractive(renewalPolicyTMPEntity.getId(),renewalPolicyTMPEntity.getMasterPolicyId());
			
		} catch (Exception e) {
			e.getStackTrace();
		}

		// PolicyValuationMastxic to copy valuationmatrixHistroy renewalvaluationmatric
		// to PolicyValuationMastxic
		Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
				.findByPolicyId(funcrenewalPolicyTMPEntity.getMasterPolicyId());
		if (policyValuationMatrixEntity.isPresent()) {
			policyValuationMatrixHistoryRepository.save(PolicyValuationMatrixHelper.policyMastertoHistransfer(
					policyValuationMatrixEntity.get(), policyHistoryEntity.getId(), username));

			RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
					.findAllBytmpPolicyId(renewalPolicyTMPEntity.getId());
			PolicyValuationMatrixEntity newPolicyValuationMatrixEntity = PolicyValuationMatrixHelper
					.updateTempToValuationMatrixMaster(renewalValuationMatrixTMPEntity, username);
			newPolicyValuationMatrixEntity.setPolicyId(newmasterPolicyEntity.getId());	
			policyValuationMatrixRepository.save(newPolicyValuationMatrixEntity);
		}

		// PolicValuationBasic to copy PolicValuationBasicHistory upodate
		// RenewalvaluationBasicTMP to PolicValuationBasic

		Optional<PolicyValutationBasicEntity> policyValuationBasicEntity = policyValuationBasicRepository
				.findBypolicyId(funcrenewalPolicyTMPEntity.getMasterPolicyId());
		if (policyValuationBasicEntity.isPresent()) {
			policyValuationBasicHistoryRepository.save(PolicyValuationMatrixHelper.policyMastertoHistransfer(
					policyValuationBasicEntity.get(), policyHistoryEntity.getId(), username));

			RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = renewalValuationBasicTMPRepository
					.findAllBytmpPolicyId(renewalPolicyTMPEntity.getId());
			PolicyValutationBasicEntity newPolicyValuationBasicEntity = PolicyValuationMatrixHelper
					.updateTempToValuationBasicMaster(renewalValuationBasicTMPEntity, username);
			newPolicyValuationBasicEntity.setAnnualRenewalDate(newmasterPolicyEntity.getAnnualRenewlDate());
			newPolicyValuationBasicEntity.setPolicyId(newmasterPolicyEntity.getId());
			policyValuationBasicRepository.save(newPolicyValuationBasicEntity);
		}

		// PolicyWithDraw copy to HistoryWidth renewalvaluationwidthdraw to
		// policywithdrwal
		List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
				.findByPolicyId(funcrenewalPolicyTMPEntity.getMasterPolicyId());
		List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntities = new ArrayList<PolicyValuationWithdrawalRateEntity>();
		for (PolicyValuationWithdrawalRateEntity addPolicyValuationWithdrawalRateEntity : policyValuationWithdrawalRateEntity) {

			policyValuationWithdrawalRateHistoryRepository.save(PolicyValuationMatrixHelper.policyMastertoHistransfer(
					addPolicyValuationWithdrawalRateEntity, policyHistoryEntity.getId(), username));
		}

		List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = renewalValuationWithdrawalTMPRepository
				.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
		for (RenewalValuationWithdrawalTMPEntity addrenewalValuationWithdrawalTMPEntity : renewalValuationWithdrawalTMPEntity) {
			
			
			List<PolicyValuationWithdrawalRateEntity> oldList = policyValuationWithdrawalRateRepository
					.deleteBypolicyId(newmasterPolicyEntity.getId());
			PolicyValuationWithdrawalRateEntity newPolicyValuationWithdrawalRateEntity = PolicyValuationMatrixHelper
					.updateTempToLifecoverMaster(addrenewalValuationWithdrawalTMPEntity, username);
			newPolicyValuationWithdrawalRateEntity.setPolicyId(newmasterPolicyEntity.getId());	
			policyValuationWithdrawalRateEntities.add(newPolicyValuationWithdrawalRateEntity);
		}
		policyValuationWithdrawalRateRepository.saveAll(policyValuationWithdrawalRateEntities);

		// End

		return newmasterPolicyEntity;

	}

	@Autowired
	private MPHRepository mPHRepository;

	@Override
	public ApiResponseDto<List<QuotationPDFGenerationDto>> generateReportRenewalQuotationPDF(Long tmpPolicyId) {
		String getPdfGeneration = renewalPolicyTMPRepository.reportRenewalQuotationPDF(tmpPolicyId);
		System.out.print(getPdfGeneration);
		String[] get = getPdfGeneration.toString().split(",");
		new ArrayList<BenefitValuationDto>();
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyId).get();
		
		String minandmaxValuationWithdrawl=null;
		minandmaxValuationWithdrawl=renewalValuationWithdrawalTMPRepository.findMinAndMax(tmpPolicyId);
		TempMPHEntity mphEntity = tempMPHRepository.findById(renewalPolicyTMPEntity.getMasterpolicyHolder()).get();
		List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository.findBypmstTmpPolicy(tmpPolicyId);
		List<RenewalLifeCoverTMPEntity> renewalLifeCoverEntity = renewalLifeCoverTMPRepository
				.findBytmpPolicyId(tmpPolicyId);
		List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitEntity = renewalGratuityBenefitTMPRepository
				.findBytmpPolicyId(tmpPolicyId);
		RenewalValuationBasicTMPEntity getRenewalValuationBasicEntity = renewalValuationBasicTMPRepository
				.findBytmpPolicyId(tmpPolicyId).get();
		RenewalValuationTMPEntity getRenewalValuation = renewalValuationTMPRepository.findBytmpPolicyId(tmpPolicyId);
//		List<QuotationPDFGenerationDto> addQuotationPDFGenerationDto = RenewalPolicyTMPHelper
//				.generateRenewalQuotationReport(get, mphEntity, memberCategoryEntity, renewalLifeCoverEntity,
//						renewalGratuityBenefitEntity, getRenewalValuationBasicEntity, getRenewalValuation,
//						renewalPolicyTMPEntity);
//		
		JsonNode mphBasic = null;
		JsonNode mphAdds = null;
		JsonNode mphcont = null;
		
		// JsonNode actualobj = commonModuleService.getProposalNumber(renewalPolicyTMPEntity.getProposalNumber());
		// mphBasic =	actualobj.path("mphDetails").path("mphBasicDetails");
		// mphcont = actualobj.path("mphDetails").path("mphContactDetails");
		// mphAdds = actualobj.path("mphDetails").path("mphAddressDetails");        
 
		
		CommonMasterUnitEntity pdfClaimForward = null;
		if (isDevEnvironment) {
			pdfClaimForward = new CommonMasterUnitEntity();
		    pdfClaimForward.setDescription("Maker");
		    pdfClaimForward.setAddress1("LIC-ADDRESS 1");
		    pdfClaimForward.setAddress2("LIC-ADDRESS 2");
		    pdfClaimForward.setAddress3("LIC-ADDRESS 3");
		    pdfClaimForward.setCityName("KUMBAKONAM");
		    pdfClaimForward.setStateName("TAMILNADU");
		    pdfClaimForward.setPincode("600182");	  
		    } else {
			pdfClaimForward = commonMasterUnitRepository.findByUnitCode(renewalPolicyTMPEntity.getUnitCode());
		}
	
		pdfClaimForward = PolicyClaimHelper.claimforward(pdfClaimForward);
		List<QuotationPDFGenerationDto> addQuotationPDFGenerationDto = new ArrayList<QuotationPDFGenerationDto>();
		List<BenefitValuationDto> addbenefitValuationDto = new ArrayList<BenefitValuationDto>();
		QuotationPDFGenerationDto newQuotationPDFGenerationDto = new QuotationPDFGenerationDto();
		
		if(memberCategoryEntity.size()>0) {
		
		for (MemberCategoryEntity getMemberCategory : memberCategoryEntity) {
			for (RenewalLifeCoverTMPEntity getMasterLifeCoverEntity : renewalLifeCoverEntity) {
				for (RenewalGratuityBenefitTMPEntity getGratuityBenefitEntity : renewalGratuityBenefitEntity) {

					for (RenewalsGratuityBenefitPropsTMPEntity getGratuityBenefitProps : getGratuityBenefitEntity
							.getRenewalgratuityPropsBenefit()) {

						if (getGratuityBenefitEntity.getCategoryId().equals(getMemberCategory.getId())
								&& getMasterLifeCoverEntity.getCategoryId().equals(getMemberCategory.getId())) {

							newQuotationPDFGenerationDto.setTotalMember(Long.parseLong(get[0]));
							newQuotationPDFGenerationDto.setAverageAge(Float.parseFloat(get[1]));
							newQuotationPDFGenerationDto.setAvgMonthlySalary(Float.parseFloat(get[2]));
							newQuotationPDFGenerationDto.setAvgPastService(Float.parseFloat(get[3]));
							newQuotationPDFGenerationDto.setTotalServiceGratuity(get[4]);
							newQuotationPDFGenerationDto.setAccuredGratuity(Long.parseLong(get[5]));
							newQuotationPDFGenerationDto.setLcas(Long.parseLong(get[6]));
							newQuotationPDFGenerationDto.setLcPremium(Long.parseLong(get[7]));
							newQuotationPDFGenerationDto.setGst(Long.parseLong(get[8]));
							newQuotationPDFGenerationDto.setMinandmaxRate(minandmaxValuationWithdrawl);
							newQuotationPDFGenerationDto.setPastServiceBenefit(Long.parseLong(get[9]));
							newQuotationPDFGenerationDto.setCurrentServiceBenefit(Long.parseLong(get[10]));
							newQuotationPDFGenerationDto.setSalaryEscalation(getRenewalValuation.getSalaryEscalation() * 100);
							newQuotationPDFGenerationDto.setDiscountRate(getRenewalValuation.getDiscountRate()* 100);							newQuotationPDFGenerationDto.setMphName(mphEntity.getMphName());
							newQuotationPDFGenerationDto.setMphEmail(mphEntity.getEmailId());
							newQuotationPDFGenerationDto.setMphMobileNo(mphEntity.getMobileNo());
							for (TempMPHAddressEntity getMPHAddress : mphEntity.getMphAddresses()) {
								newQuotationPDFGenerationDto.setMphAddress1(
										getMPHAddress.getAddressLine1() != null ? getMPHAddress.getAddressLine1() : "");
								newQuotationPDFGenerationDto.setMphAddress2(
										getMPHAddress.getAddressLine2() != null ? getMPHAddress.getAddressLine2() : "");
								newQuotationPDFGenerationDto.setMphAddress3(
										getMPHAddress.getAddressLine3() != null ? getMPHAddress.getAddressLine3() : "");
								newQuotationPDFGenerationDto.setMphAddressType(
										getMPHAddress.getAddressType() != null ? getMPHAddress.getAddressType() : "");
							}
							newQuotationPDFGenerationDto.setDateOfApproval(renewalPolicyTMPEntity.getCreatedDate());
//						newQuotationPDFGenerationDto.setProposalNumber(renewalPolicyTMPEntity.getProposalNumber());
							newQuotationPDFGenerationDto
									.setQuotationNumber(renewalPolicyTMPEntity.getQuotationNumber());
							newQuotationPDFGenerationDto.setProposalNumber(renewalPolicyTMPEntity.getPolicyNumber());
						newQuotationPDFGenerationDto
								.setDateOfCommencement(renewalPolicyTMPEntity.getQuotationDate());

							BenefitValuationDto benefitValuationDto = new BenefitValuationDto();
							benefitValuationDto.setCategoryId(getMemberCategory.getId());
							benefitValuationDto.setCategoryName(getMemberCategory.getName());
							benefitValuationDto.setRetirementAge(getGratuityBenefitProps.getRetirementAge());
							benefitValuationDto.setRateTable(getRenewalValuationBasicEntity.getRateTable());
							benefitValuationDto.setLcas(getMasterLifeCoverEntity.getMaximumSumAssured());
							benefitValuationDto
									.setGratuityCellingAmount(getGratuityBenefitProps.getGratuityCellingAmount());
							benefitValuationDto.setNumberOfDaysWage(getGratuityBenefitProps.getNumberOfDaysWage());
							benefitValuationDto.setNumberOfWorkingDaysPerMonth(
									getGratuityBenefitProps.getNumberOfWorkingDaysPerMonth());
							addbenefitValuationDto.add(benefitValuationDto);
						}
					}
				}
			}
			newQuotationPDFGenerationDto.setCommonMasterUnitEntity(pdfClaimForward);		
			newQuotationPDFGenerationDto.setBenefitValuation(addbenefitValuationDto);
		}
		}
		addQuotationPDFGenerationDto.add(newQuotationPDFGenerationDto);

	
		

		return ApiResponseDto.success(addQuotationPDFGenerationDto);
	}

	@Override
	public ApiResponseDto<List<RenewalPolicyTMPDto>> renewalSearchFilters(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo) {

		List<RenewalPolicyTMPEntity> renewalPolicTmpEntitiy = new ArrayList<>();
		List<RenewalPolicyTMPEntity> newRenewalPolicyTmpEntitiy = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<RenewalPolicyTMPEntity> criteriaQuery = criteriaBuilder
					.createQuery(RenewalPolicyTMPEntity.class);
			Root<RenewalPolicyTMPEntity> root = criteriaQuery.from(RenewalPolicyTMPEntity.class);

//			  Join<RenewalPolicyTMPEntity,PolicyServiceEntitiy> join = root.join("policyServiceId"); 
//		
//			  if(renewalQuotationSearchDTo.getServiceId()!=null) {
//				  join.on(criteriaBuilder.equal(join.get("policyServiceId"),
//			  renewalQuotationSearchDTo.getServiceId())); }

			if (StringUtils.isNotBlank(renewalQuotationSearchDTo.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
						renewalQuotationSearchDTo.getPolicyNumber().toLowerCase().trim()));
			}
			if (StringUtils.isNoneBlank(renewalQuotationSearchDTo.getCustomerName())) {
				predicates.add(
						criteriaBuilder.equal(root.get("customerName"), renewalQuotationSearchDTo.getCustomerName()));
			}
			if (StringUtils.isNoneBlank(renewalQuotationSearchDTo.getIntermediaryName())) {
				predicates.add(criteriaBuilder.equal(root.get("intermediaryName"),
						renewalQuotationSearchDTo.getIntermediaryName()));
			}
			if (renewalQuotationSearchDTo.getLineOfBusiness() != null) {
				predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"),
						renewalQuotationSearchDTo.getLineOfBusiness()));
			}
			if (renewalQuotationSearchDTo.getProductId() != null) {
				predicates.add(criteriaBuilder.equal(root.get("productId"), renewalQuotationSearchDTo.getProductId()));
			}
			if (renewalQuotationSearchDTo.getQuotationTaggedStatusId() != null) {
				predicates.add(criteriaBuilder.equal(root.get("quotationTaggedStatusId"),
						renewalQuotationSearchDTo.getQuotationTaggedStatusId()));
			}
			if (renewalQuotationSearchDTo.getQuotationStatusId() != null && renewalQuotationSearchDTo.getQuotationStatusId() >0) {
				predicates.add(criteriaBuilder.equal(root.get("quotationStatusId"),
						renewalQuotationSearchDTo.getQuotationStatusId()));
			}
			if (renewalQuotationSearchDTo.getQuotationSubStatusId() != null && renewalQuotationSearchDTo.getQuotationSubStatusId() >0) {
				predicates.add(criteriaBuilder.equal(root.get("quotationSubStatusId"),
						renewalQuotationSearchDTo.getQuotationSubStatusId()));
			}
			
			
			if (renewalQuotationSearchDTo.getPolicytaggedStatusId() != null) {
				predicates.add(criteriaBuilder.equal(root.get("policytaggedStatusId"),
						renewalQuotationSearchDTo.getPolicytaggedStatusId()));
			}

			if (renewalQuotationSearchDTo.getProductVariant() != null) {
				predicates.add(criteriaBuilder.equal(root.get("productVariant"),
						renewalQuotationSearchDTo.getProductVariant()));
			}
			if (StringUtils.isNoneBlank(renewalQuotationSearchDTo.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), renewalQuotationSearchDTo.getUnitCode()));
			}
			
			if (StringUtils.isNoneBlank(renewalQuotationSearchDTo.getQuotationNumber())) {
				predicates.add(criteriaBuilder.equal(root.get("quotationNumber"), renewalQuotationSearchDTo.getQuotationNumber()));
			}
//			if (StringUtils.isNotBlank(
//					DateUtils.dateToStringFormatYyyyMmDdHhMmSsSAlash(renewalQuotationSearchDTo.getPolicyStartDate()))) {
//				predicates.add(criteriaBuilder.equal(root.get("policyStartDate"),
//						renewalQuotationSearchDTo.getPolicyStartDate()));
//			}
//			if (StringUtils.isNotBlank(
//					DateUtils.dateToStringFormatYyyyMmDdHhMmSsSAlash(renewalQuotationSearchDTo.getPolicyEndDate()))) {
//				predicates.add(
//						criteriaBuilder.equal(root.get("policyEndDate"), renewalQuotationSearchDTo.getPolicyEndDate()));
//			}
			
	
			if (renewalQuotationSearchDTo.getPolicyStartDate() != null && renewalQuotationSearchDTo.getPolicyEndDate() != null) {
				Date fromDate = renewalQuotationSearchDTo.getPolicyStartDate();
				Date toDate = renewalQuotationSearchDTo.getPolicyEndDate();
				
				fromDate= constructeStartDateTime(fromDate);
				toDate = constructeEndDateTime(toDate);
				Predicate onStart = criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), fromDate);
				Predicate onEnd = criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), toDate);
				predicates.add(onStart);
				predicates.add(onEnd);
			}
			
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			criteriaQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			renewalPolicTmpEntitiy = entityManager.createQuery(criteriaQuery).getResultList();

			// Get List Data
			List<RenewalPolicyTMPEntity> getRenewalPolicyTMPEntity = null;

			for (RenewalPolicyTMPEntity policyTMPEntity : renewalPolicTmpEntitiy) {

				if (renewalQuotationSearchDTo.getUserType() != null) {
					if (renewalQuotationSearchDTo.getUserType().equals("UO")) {
						getRenewalPolicyTMPEntity = renewalPolicyTMPCustomRepository
								.findByquotationTaggedStatusIdwithUnit(policyTMPEntity.getQuotationTaggedStatusId(),
										policyTMPEntity.getQuotationNumber(), policyTMPEntity.getUnitCode());
						if (getRenewalPolicyTMPEntity.size() > 0) {
							newRenewalPolicyTmpEntitiy.add(policyTMPEntity);
						}
					}
					if (renewalQuotationSearchDTo.getUserType().equals("ZO")) {
						String getUnitCode = policyTMPEntity.getUnitCode().substring(0, 2);
						getRenewalPolicyTMPEntity = renewalPolicyTMPCustomRepository
								.findByquotationTaggedStatuswithgetUnitCode(
										policyTMPEntity.getQuotationTaggedStatusId(), policyTMPEntity.getQuotationNumber(),
										getUnitCode);
						if (getRenewalPolicyTMPEntity.size() > 0) {
							newRenewalPolicyTmpEntitiy.add(policyTMPEntity);
						}
					} else {
						getRenewalPolicyTMPEntity = renewalPolicyTMPCustomRepository.findByquotationTaggedStatusId(
								policyTMPEntity.getQuotationTaggedStatusId(), policyTMPEntity.getQuotationNumber());
						if (getRenewalPolicyTMPEntity.size() > 0) {
							newRenewalPolicyTmpEntitiy.add(policyTMPEntity);
						}
					}
				}
			}

		} catch (IllegalArgumentException e) {
			e.getStackTrace();
		}
		return ApiResponseDto.created(
				renewalPolicTmpEntitiy.stream().map(RenewalPolicyHelper::entityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<RenewalPolicyNewSearchFilterDto>> filterForRenewal(
			RenewalPolicyNewSearchFilterDto renewalPolicyNewSearchFilterDto) {
		List<Predicate> predicate = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RenewalPolicySearchEntity> createQuery = criteriaBuilder
				.createQuery(RenewalPolicySearchEntity.class);

		Root<RenewalPolicySearchEntity> root = createQuery.from(RenewalPolicySearchEntity.class);
		Join<RenewalPolicySearchEntity, PolicyRenewalRemainderSearchEntity> join = root.join("policySearch");

		if (StringUtils.isNotBlank(renewalPolicyNewSearchFilterDto.getPolicyNumber())
				&& renewalPolicyNewSearchFilterDto.getPolicyNumber() != null) {
			predicate.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("policyNumber"))),
					renewalPolicyNewSearchFilterDto.getPolicyNumber().toLowerCase().trim()));
		}
		if (StringUtils.isNotBlank(renewalPolicyNewSearchFilterDto.getCustomerName())
				&& renewalPolicyNewSearchFilterDto.getCustomerName() != null) {
			predicate.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("customerName"))),
					renewalPolicyNewSearchFilterDto.getCustomerName().toLowerCase()));
		}

		if (StringUtils.isNotBlank(renewalPolicyNewSearchFilterDto.getIntermediaryName())
				&& renewalPolicyNewSearchFilterDto.getIntermediaryName() != null) {
			predicate.add(
					criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("intermediaryName"))),
							renewalPolicyNewSearchFilterDto.getIntermediaryName().toLowerCase()));
		}

		if (StringUtils.isNotBlank(renewalPolicyNewSearchFilterDto.getLineOfBusiness())
				&& renewalPolicyNewSearchFilterDto.getLineOfBusiness() != null) {
			predicate
					.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("lineOfBusiness"))),
							renewalPolicyNewSearchFilterDto.getLineOfBusiness().toLowerCase()));
		}
		if (StringUtils.isNotBlank(renewalPolicyNewSearchFilterDto.getProductName())
				&& renewalPolicyNewSearchFilterDto.getProductName() != null) {
			predicate.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("productName"))),
					renewalPolicyNewSearchFilterDto.getProductName().toLowerCase()));
		}

		if (StringUtils.isNotBlank(renewalPolicyNewSearchFilterDto.getProductVariant())
				&& renewalPolicyNewSearchFilterDto.getProductVariant() != null) {
			predicate
					.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("productVariant"))),
							renewalPolicyNewSearchFilterDto.getProductVariant().toLowerCase()));
		}

		if (StringUtils.isNotBlank(renewalPolicyNewSearchFilterDto.getUnitOffice())
				&& renewalPolicyNewSearchFilterDto.getUnitOffice() != null) {
			predicate.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("unitOffice"))),
					renewalPolicyNewSearchFilterDto.getUnitOffice().toLowerCase()));
		}
	
		if ((renewalPolicyNewSearchFilterDto.getPolicyTaggedStatusId() != null)
				&& StringUtils.isNotBlank(renewalPolicyNewSearchFilterDto.getPolicyTaggedStatusId().toString())) {
			predicate.add(criteriaBuilder.equal(root.get("policyTaggedStatusId"),
					renewalPolicyNewSearchFilterDto.getPolicyTaggedStatusId()));
		}
		
		if ((renewalPolicyNewSearchFilterDto.getPolicyStatusId() != null && renewalPolicyNewSearchFilterDto.getPolicyStatusId() >0)
				&& StringUtils.isNotBlank(renewalPolicyNewSearchFilterDto.getPolicyStatusId().toString())) {
			predicate.add(criteriaBuilder.equal(root.get("policyStatusId"),
					renewalPolicyNewSearchFilterDto.getPolicyStatusId()));
		}
		if (renewalPolicyNewSearchFilterDto.getPolicyEndDate() != null) {
			predicate.add(criteriaBuilder.equal(root.get("policyEndDate"),
					renewalPolicyNewSearchFilterDto.getPolicyEndDate()));
		}
		if (renewalPolicyNewSearchFilterDto.getPolicyEndDate() != null
				&& renewalPolicyNewSearchFilterDto.getPolicyEndDate() != null) {
			try {
				predicate.add(
						criteriaBuilder.between(root.get("date"), renewalPolicyNewSearchFilterDto.getPolicyEndDate(),
								renewalPolicyNewSearchFilterDto.getPolicyEndDate()));
			} catch (Exception e) {

			}
		}
		if (renewalPolicyNewSearchFilterDto.getPolicyStartDate() != null
				&& renewalPolicyNewSearchFilterDto.getPolicyStartDate() != null) {
			try {
				predicate.add(
						criteriaBuilder.between(root.get("date"), renewalPolicyNewSearchFilterDto.getPolicyStartDate(),
								renewalPolicyNewSearchFilterDto.getPolicyStartDate()));
			} catch (Exception e) {

			}
		}
		
//		predicate.add(criteriaBuilder.equal(join.get("annualrenewaldate"), Boolean.TRUE));

		predicate.add(criteriaBuilder.equal(join.get("isActive"), Boolean.TRUE));

		createQuery.select(root).where(predicate.toArray(new Predicate[] {}));
		List<RenewalPolicySearchEntity> entities = new ArrayList<RenewalPolicySearchEntity>();
		entities = entityManager.createQuery(createQuery).getResultList();

		List<RenewalPolicySearchEntity> masterPolicyEntities = new ArrayList<>();

		for (RenewalPolicySearchEntity masterPolicyEntity : entities) {
			masterPolicyEntities.add(masterPolicyCustomRepository.setTransientValues(masterPolicyEntity));
		}
		return ApiResponseDto
				.success(masterPolicyEntities.stream().map(RenewalHelper::entityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<PolicySearchFilterDto>> filter(PolicySearchFilterDto policyTmpServiceDto) {
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PolicyTmpSearchEntity> createQuery = criteriaBuilder.createQuery(PolicyTmpSearchEntity.class);

		Root<PolicyTmpSearchEntity> root = createQuery.from(PolicyTmpSearchEntity.class);

		Join<PolicyTmpSearchEntity, PolicyServiceSearchEntity> join = root.join("policyTmp");

		if ((policyTmpServiceDto.getCustomerName() != null)) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("customerName"))),
					policyTmpServiceDto.getCustomerName().toLowerCase()));
		}

		if ((policyTmpServiceDto.getIntermediaryName() != null)) {
			predicates.add(
					criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("intermediaryName"))),
							policyTmpServiceDto.getIntermediaryName().toLowerCase()));
		}

		if ((policyTmpServiceDto.getLineOfBusiness() != null)) {
			predicates
					.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("lineOfBusiness"))),
							policyTmpServiceDto.getLineOfBusiness().toLowerCase()));
		}

		if ((policyTmpServiceDto.getProductName() != null)) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("productName"))),
					policyTmpServiceDto.getProductName().toLowerCase()));
		}

		if ((policyTmpServiceDto.getProductVariant() != null)) {
			predicates
					.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("productVariant"))),
							policyTmpServiceDto.getProductVariant().toLowerCase()));
		}

		if ((policyTmpServiceDto.getUnitOffice() != null)) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("unitOffice"))),
					policyTmpServiceDto.getUnitOffice().toLowerCase()));
		}

		if ((policyTmpServiceDto.getPolicyStatusId() != null)) {

			predicates
					.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("policyStatusId"))),
							policyTmpServiceDto.getPolicyStatusId()));
		}

		if (policyTmpServiceDto.getPolicyStartDate() != null
				&& StringUtils.isNotBlank(policyTmpServiceDto.getPolicyStartDate().toString())) {
			predicates
					.add(criteriaBuilder.equal(root.get("policyStartDate"), policyTmpServiceDto.getPolicyStartDate()));
		}

		if (policyTmpServiceDto.getPolicyEndDate() != null
				&& StringUtils.isNotBlank(policyTmpServiceDto.getPolicyEndDate().toString())) {
			predicates.add(criteriaBuilder.equal(root.get("policyEndDate"), policyTmpServiceDto.getPolicyEndDate()));
		}

		predicates.add(criteriaBuilder.equal(join.get("isActive"), Boolean.TRUE));
		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<PolicyTmpSearchEntity> entities = new ArrayList<PolicyTmpSearchEntity>();
		entities = entityManager.createQuery(createQuery).getResultList();

		List<PolicyTmpSearchEntity> policyTempSearch = new ArrayList<>();
		for (PolicyTmpSearchEntity policySearch : entities) {
			policyTempSearch.add(masterPolicyCustomRepository.setTransientValue(policySearch));
		}

		return ApiResponseDto
				.success(policyTempSearch.stream().map(RenewalHelper::tmpEntityToDto).collect(Collectors.toList()));
	}

	public ResponseDto commonmasterserviceUnitByCode(String unitCode) {
		ResponseDto responseDto = null;
		logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-Start");

		try {
			String url = environment.getProperty("COMMON_MASTER_UNITBY_CODE");

			if (StringUtils.isNotBlank(url)) {

				responseDto = restTemplateService.exchange(url + unitCode, HttpMethod.GET, null, ResponseDto.class)
						.getBody();
				if (responseDto == null) {
					responseDto = new ResponseDto();
				}
			}
		} catch (HttpStatusCodeException e) {

			logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-Error:");
		}
		logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-End");

		return responseDto;
	}

	@Override
	public Boolean ARDStatus(Long masterpolicyid) {
	int masterpolicyentity=masterPolicyRepository.getARDStatusCount(masterpolicyid);
		if(masterpolicyentity>0) {
			return true;
		}
		else {
			return false;
		}
	
	}
	
	public static Date constructeStartDateTime(Date start) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.set(Calendar.MILLISECOND, 00);
		return cal.getTime();
	}

	public static Date constructeEndDateTime(Date end) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(end);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 59);
		return cal.getTime();
	}

	public static Date convertStringToDate(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("dd/MM/yyyy");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				/***
 * ParseException
 */
			}
		}
		return null;

}

	@Override
	public Long getRenewalSubStatus(Long quotationSubStatusId) {
		
		Long count=renewalPolicyTMPRepository.getCountRenewalSubStatus(quotationSubStatusId);
		return count;
	}

	//discard renewal
	@Transactional
	@Override
	public Boolean discardRenewal(Long renewalTmpPolicyId) {
		
		RenewalPolicyTMPEntity oldrenewalPolicyTMPEntity=renewalPolicyTMPRepository.findById(renewalTmpPolicyId).get();
		
		
		policyServiceRepository.deleteBypmstPolicyandServiceId(oldrenewalPolicyTMPEntity.getMasterPolicyId(),oldrenewalPolicyTMPEntity.getPolicyServiceId());
		
		renewalSchemeruleTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
		renewalLifeCoverTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
		renewalGratuityBenefitTMPRepository.deleteByGratuityBenefitId(oldrenewalPolicyTMPEntity.getId());
		renewalGratuityBenefitTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
		renewalValuationTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
		renewalValuationMatrixTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
		renewalValuationBasicTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
		renewalValuationWithdrawalTMPRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
		renewalPolicyTMPMemberRepository.deleteBytmpPolicyId(oldrenewalPolicyTMPEntity.getId());
		
		renewalPolicyTMPRepository.deleteById(oldrenewalPolicyTMPEntity.getId());
		return true;
	}
}
