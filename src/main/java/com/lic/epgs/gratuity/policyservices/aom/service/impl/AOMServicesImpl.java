package com.lic.epgs.gratuity.policyservices.aom.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lic.epgs.gratuity.accountingservice.dto.GstDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.JournalVoucherDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.NewBusinessContriDebitCreditRequestModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.NewBusinessContributionAndLifeCoverAdjustmentDto;
import com.lic.epgs.gratuity.accountingservice.dto.UnlockDepositDetailDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;
import com.lic.epgs.gratuity.common.entity.PickListItemEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.PickListItemRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.service.CommonService;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.entity.TempMemberSearchEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempMphSearchEntity;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.claim.repository.TempMemberSearchRepository;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositLockDto;
import com.lic.epgs.gratuity.policy.endorsement.document.entity.EndorsementDocumentEntity;
import com.lic.epgs.gratuity.policy.endorsement.document.helper.EndorsementDocumentHelper;
import com.lic.epgs.gratuity.policy.endorsement.document.repository.EndorsementDocumentRepository;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicySearchEntity;
import com.lic.epgs.gratuity.policy.entity.MphSearchEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.entity.QuotationChargeEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.helper.PolicyMemberHelper;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyDepositRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContrySummaryRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyDepositRepository;
import com.lic.epgs.gratuity.policy.renewal.entity.EmailMessagesEntity;
import com.lic.epgs.gratuity.policy.renewal.repository.EmailMessagesRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.helper.RenewalPolicyTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.repository.RenewalLifeCoverTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAddressEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberAddressRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberAppointeeRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberBankRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberNomineeRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.PolicyServiceRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.entity.RenewalSchemeruleTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.helper.RenewalSchemeruleTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.repository.RenewalSchemeruleTMPRepository;
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
import com.lic.epgs.gratuity.policy.repository.MasterPolicyAdjustmentDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyAdjustmentDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyTmpSearchRepository;
import com.lic.epgs.gratuity.policy.repository.QuotationChargeRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyMasterValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyMasterValuationRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.policyservices.aom.dto.AOMSearchDto;
import com.lic.epgs.gratuity.policyservices.aom.dto.DocumentUploadDto;
import com.lic.epgs.gratuity.policyservices.aom.dto.GetOverviewDto;
import com.lic.epgs.gratuity.policyservices.aom.dto.MemberTmpDto;
import com.lic.epgs.gratuity.policyservices.aom.entity.AOMSearchEntity;
import com.lic.epgs.gratuity.policyservices.aom.entity.PolicyTmpAOMProps;
import com.lic.epgs.gratuity.policyservices.aom.model.EmailRequest;
import com.lic.epgs.gratuity.policyservices.aom.repository.PolicyTmpAOMPropsRepository;
import com.lic.epgs.gratuity.policyservices.aom.service.AOMService;
import com.lic.epgs.gratuity.policyservices.common.enums.PolicyServiceName;
import com.lic.epgs.gratuity.policyservices.dom.helper.MidLeaverHelper;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.OmniDocsRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.OmniDocumentResponse;
import com.lic.epgs.gratuity.quotation.helper.QuotationHelper;
import com.lic.epgs.gratuity.quotation.member.helper.BulkMemberUploadHelper;
import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;
import com.lic.epgs.gratuity.simulation.dto.DepositAdjustementDto;
import com.lic.epgs.gratuity.simulation.dto.DepositDto;
import com.lic.epgs.gratuity.simulation.dto.PolicyDepositDto;

@Service
public class AOMServicesImpl implements AOMService {

	private Long standardcode = 10l;

//	@Autowired
//	private PolicyTempSearchRepository policyTempSearchRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;

	@Autowired
	private MasterPolicyAdjustmentDetailRepository masterPolicyAdjustmentDetailRepository;

	@Autowired
	private PolicyServiceRepository policyServiceRepository;

	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

	@Autowired
	PolicyTmpAOMPropsRepository policyTmpAOMPropsRepository;

	@Autowired
	private RenewalPolicyTMPMemberBankRepository renewalPolicyTMPMemberBankRepository;

	@Autowired
	private RenewalPolicyTMPMemberAddressRepository renewalPolicyTMPMemberAddressRepository;

	@Autowired
	private RenewalPolicyTMPMemberAppointeeRepository renewalPolicyTMPMemberAppointeeRepository;

	@Autowired
	PickListItemRepository pickListItemRepository;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private QuotationChargeRepository quotationChargeRepository;

	@Autowired
	EndorsementDocumentRepository endorsementDocumentRepository;

	@Value("${app.policy.approvedStatudId}")
	private Long approvedStatudId;

	@Value("${app.policy.approvedSubStatudId}")
	private Long approvedSubStatudId;

	@Value("${app.policy.existingTaggedStatusId}")
	private Long existingTaggedStatusId;

	@Value("${app.quotation.defaultStatudId}")
	private String defaultStatusId;

	@Value("${app.policy.defaultStatusId}")
	private String policyDefaultStatusId;

	@Value("${app.policy.defaultSubStatusId}")
	private String policyDefaultSubStatusId;

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

	@Value("${app.policy.defaultSubStatusId}")
	private String defaultSubStatusId;

	private static Long quotationStatusId = 16l;
	private static Long quotationSubStatusId = 21l;
	private static Long quotationTaggedStatusId = 78l;
	@Autowired
	private RenewalPolicyTMPMemberNomineeRepository renewalPolicyTMPMemberNomineeRepository;

	@Autowired
	private MasterPolicyDepositRepository masterPolicyDepositRepository;

	@Autowired
	private MasterPolicyContributionRepository masterPolicyContributionRepository;

	@Autowired
	private PolicyDepositRepository policyDepositRepository;

	// private TempMemberSearchRepository tempMemberSearchRepository;

	@Autowired
	private PolicyContributionRepository policyContributionRepository;

	@Autowired
	private PolicyContrySummaryRepository policyContrySummaryRepository;

	@Autowired
	private GratuityCalculationRepository gratuityCalculationRepository;
	@Autowired
	private MasterPolicyRepository masterPolicyRepository;

	@Autowired
	private PolicyTmpSearchRepository policyTmpSearchRepository;

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
	private PolicyAdjustmentDetailRepository policyAdjustmentDetailRepository;

	@Autowired
	private RenewalValuationWithdrawalTMPRepository renewalValuationWithdrawalTMPRepository;

	@Autowired
	private TempMemberSearchRepository tempMemberSearchRepository;

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
	private CommonService commonService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private PolicyMasterValuationRepository policyMasterValuationRepository;

	@Autowired
	PolicyValuationWithdrawalRateHistoryRepository policyValuationWithdrawalRateHistoryRepository;
	@Autowired
	private PolicyMemberRepository policyMemberRepository;

	@Autowired
	private RenewalPolicyTMPMemberRepository renewalPolicyTMPMemberRepository;

	@Autowired
	private StandardCodeRepository standardCodeRepository;

	@Autowired
	private PolicyContributionDetailRepository policyContributionDetailRepository;

	@Autowired
	private MasterPolicyContributionDetailRepository masterPolicyContributionDetailRepository;

	@Autowired
	private MPHRepository mPHRepository;

	@Autowired
	private ModelMapper modelMapper;

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

	@Value("${temp.pdf.location}")
	private String fileLocation;

	@Autowired
	private EmailMessagesRepository emailMessagesRepository;

	@Override
	public ApiResponseDto<List<PolicyDto>> masterPolicySearch(PolicySearchDto policySearchDto) {

		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MasterPolicyEntity> createQuery = criteriaBuilder.createQuery(MasterPolicyEntity.class);

		Root<MasterPolicyEntity> root = createQuery.from(MasterPolicyEntity.class);
		Join<MasterPolicySearchEntity, MphSearchEntity> join = root.join("policyMPHTmp");

		if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())
				&& policySearchDto.getPolicyNumber().length() == 4) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("policyNumber")),
					"%" + StringUtils.right(policySearchDto.getPolicyNumber(), 4).toLowerCase()));
		} else {

			if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
						policySearchDto.getPolicyNumber().toLowerCase()));
			}
		}
		if (StringUtils.isNotBlank(policySearchDto.getCustomerName())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")),
					"%" + policySearchDto.getCustomerName().toLowerCase() + "%"));
		}
		if (policySearchDto.getCustomerCode() != null) {
			predicates.add(criteriaBuilder.equal(root.get("customerCode"), policySearchDto.getCustomerCode()));
		}
//		if (StringUtils.isNotBlank(policySearchDto.getCustomerName())) {
//			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("customerCode")),
//					"%" + policySearchDto.getCustomerCode().toLowerCase() + "%"));
//		}
		if (StringUtils.isNotBlank(policySearchDto.getIntermediaryName())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("intermediaryName")),
					"%" + policySearchDto.getIntermediaryName().toLowerCase() + "%"));
		}
		if (policySearchDto.getLineOfBusinessId() != null && policySearchDto.getLineOfBusinessId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"), policySearchDto.getLineOfBusinessId()));
		}
		if (policySearchDto.getProductId() != null && policySearchDto.getProductId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("productId"), policySearchDto.getProductId()));
		}
		if (policySearchDto.getProductVariant() != null && policySearchDto.getProductVariant() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("productVariantId"), policySearchDto.getProductVariant()));
		}
		if (policySearchDto.getUnitOffice() != null && policySearchDto.getUnitOffice() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("unitOffice"), policySearchDto.getUnitOffice()));
		}
//		if (policySearchDto.getPolicyStatus() != null && policySearchDto.getPolicyStatus() > 0) {
//			predicates.add(criteriaBuilder.equal(root.get("policyStatus"), policySearchDto.getPolicyStatus()));
//		}

		if (StringUtils.isNotBlank(policySearchDto.getPan()) && policySearchDto.getPan() != null) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(join.get("pan"))),
					policySearchDto.getPan().toLowerCase()));
		}

		if (policySearchDto.getPolicyStatusId() != null && policySearchDto.getPolicyStatusId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), policySearchDto.getPolicyStatusId()));
		}
		if (policySearchDto.getPolicyStartDate() != null) {
			predicates.add(criteriaBuilder.equal(root.get("policyStartDate"), policySearchDto.getPolicyStartDate()));
		}
		if (policySearchDto.getPolicyStartDate() != null && policySearchDto.getPolicyEndDate() != null) {
			try {
				predicates.add(criteriaBuilder.between(root.get("date"), policySearchDto.getPolicyStartDate(),
						policySearchDto.getPolicyEndDate()));
			} catch (Exception e) {

			}
		}
		if (policySearchDto.getUserType() != null) {
			if (policySearchDto.getUserType().equals("UO")) {
				if (policySearchDto.getUnitCode() != null) {
					predicates.add(criteriaBuilder.equal(root.get("unitCode"), policySearchDto.getUnitCode()));
				}
			}
			if (policySearchDto.getUserType().equals("ZO")) {

				String get = policySearchDto.getUnitCode().substring(0, 2);
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("unitCode")),
						"%" + get.toLowerCase() + "%"));
			}
		}

		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<MasterPolicyEntity> entities = entityManager.createQuery(createQuery).getResultList();
		List<MasterPolicyEntity> masterPolicyEntities = new ArrayList<>();

		for (MasterPolicyEntity masterPolicyEntity : entities) {
			masterPolicyEntities.add(masterPolicyCustomRepository.setTransientValues(masterPolicyEntity));
		}
		return ApiResponseDto
				.success(masterPolicyEntities.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));

	}

	@Override
	public ApiResponseDto<?> saveTempMember(MemberTmpDto memberTmpDto, Long policyId, Long tempPolicyId) {

		RenewalPolicyTMPMemberEntity request = modelMapper.map(memberTmpDto, RenewalPolicyTMPMemberEntity.class);

		MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(policyId);

		renewalPolicyTMPRepository.deactiveOtherPolicyMemberdetailForMidJoiner(masterPolicyEntity.getPolicyNumber());

		if (request.getId() != 0) {
			Optional<RenewalPolicyTMPMemberEntity> optionalTeamMember = renewalPolicyTMPMemberRepository
					.findById(request.getId());
			RenewalPolicyTMPMemberEntity tempMemberEntity = optionalTeamMember.get();
			tempMemberEntity.setEmployeeCode(request.getEmployeeCode());
			tempMemberEntity.setFirstName(request.getFirstName());
			tempMemberEntity.setMiddleName(request.getMiddleName());
			tempMemberEntity.setLastName(request.getLastName());
			tempMemberEntity.setDateOfBirth(request.getDateOfBirth());
			tempMemberEntity.setDateOfAppointment(request.getDateOfAppointment());
			tempMemberEntity.setGenderId(request.getGenderId());
			tempMemberEntity.setFatherName(request.getFatherName());
			tempMemberEntity.setPanNumber(request.getPanNumber());
			tempMemberEntity.setAadharNumber(request.getAadharNumber());
			tempMemberEntity.setSpouseName(request.getSpouseName());
			tempMemberEntity.setCategoryId(request.getCategoryId());
			tempMemberEntity.setDesignation(request.getDesignation());
			tempMemberEntity.setCommunicationPreference(request.getCommunicationPreference());
			tempMemberEntity.setEmailId(request.getEmailId());
			tempMemberEntity.setMobileNo(request.getMobileNo());
			tempMemberEntity.setBasicSalary(request.getBasicSalary());
			tempMemberEntity.setSalaryFrequency(request.getSalaryFrequency());
			tempMemberEntity.setModifiedDate(new Date());
			tempMemberEntity.setModifiedBy(request.getModifiedBy());
			tempMemberEntity.setNewLife("Y");
			renewalPolicyTMPMemberRepository.save(tempMemberEntity);

			List<RenewalPolicyTMPMemberEntity> findBytmpPolicyId = renewalPolicyTMPMemberRepository
					.findBytmpPolicyId(tempPolicyId);

			List<MemberTmpDto> list = new ArrayList<>();
			for (RenewalPolicyTMPMemberEntity tempMemberEntity2 : findBytmpPolicyId) {
				MemberTmpDto conMemberTmpDto = modelMapper.map(tempMemberEntity2, MemberTmpDto.class);
				list.add(conMemberTmpDto);
			}

			return ApiResponseDto.success(list);
		} else {

			if (tempPolicyId == 0) {

				/* save master to temp */
				RenewalPolicyTMPEntity masterToTemp = masterToTemp(policyId, request.getCreatedBy());

				request.setPolicyId(masterToTemp.getMasterPolicyId());
				request.setTmpPolicyId(masterToTemp.getId());
				request.setCreatedDate(new Date());
				request.setModifiedDate(new Date());
				request.setStatusId(146L);
				request.setNewLife("Y");
				request.setIsActive(true);
				renewalPolicyTMPMemberRepository.save(request);
				try {
					List<RenewalPolicyTMPMemberEntity> findBytmpPolicyId = renewalPolicyTMPMemberRepository
							.findBytmpPolicyId(masterToTemp.getId());
					List<MemberTmpDto> list = new ArrayList<>();
					for (RenewalPolicyTMPMemberEntity tempMemberEntity2 : findBytmpPolicyId) {
						MemberTmpDto conMemberTmpDto = modelMapper.map(tempMemberEntity2, MemberTmpDto.class);
						list.add(conMemberTmpDto);
					}

					return ApiResponseDto.success(list);

				} catch (Exception e) {
					e.printStackTrace();

				}

			} else {

				request.setPolicyId(policyId);
				request.setTmpPolicyId(tempPolicyId);
				request.setCreatedDate(new Date());
				request.setModifiedDate(new Date());
				request.setStatusId(146L);
				request.setIsActive(true);
				renewalPolicyTMPMemberRepository.save(request);
				List<RenewalPolicyTMPMemberEntity> findBytmpPolicyId = renewalPolicyTMPMemberRepository
						.findBytmpPolicyId(tempPolicyId);

				List<MemberTmpDto> list = new ArrayList<>();
				for (RenewalPolicyTMPMemberEntity tempMemberEntity2 : findBytmpPolicyId) {
					MemberTmpDto conMemberTmpDto = modelMapper.map(tempMemberEntity2, MemberTmpDto.class);
					list.add(conMemberTmpDto);
				}

				return ApiResponseDto.success(list);
			}

		}
		return null;
	}

	@Transactional
	public RenewalPolicyTMPEntity masterToTemp(Long masterPolicyId, String createdBy) {

//		save service
//		PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();

		TaskProcessEntity taskProcessEntity = taskProcessRepository.getByTaskProcess("MID-JOINERS QUOTATION");

		TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();

//		save policy
		MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(masterPolicyId);

		List<String> getListNumer = renewalPolicyTMPRepository.addQuotationNumber(masterPolicyEntity.getPolicyNumber());
		String getQuotationNumber = null;
		if (getListNumer.size() > 0) {
			getQuotationNumber = QuotationHelper.addQuotationNumberForMJ(masterPolicyEntity.getPolicyNumber(),
					getListNumer.get(0));
		} else {
			getQuotationNumber = masterPolicyEntity.getPolicyNumber() + "01";
		}

		// RenewalPolicyTMPEntity renewalPolicyTMPEntityExist =
		// renewalPolicyTMPRepository
		// .findByServiceId(policyServiceEntitiy.getId());

//		RenewalPolicyTMPEntity renewalPolicyTMPEntityExist = renewalPolicyTMPRepository
//				.findBymasterPolicyId(masterPolicyId);
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = RenewalPolicyTMPHelper.pmsttoTmp(masterPolicyEntity);
//		if (renewalPolicyTMPEntityExist == null) {
		renewalPolicyTMPEntity.setId(null);
//		} else {
//			renewalPolicyTMPEntity.setId(renewalPolicyTMPEntityExist.getId());
//		}
		renewalPolicyTMPEntity.setPolicyStatusId(null);
		renewalPolicyTMPEntity.setPolicySubStatusId(null);
		renewalPolicyTMPEntity.setPolicytaggedStatusId(null);
		renewalPolicyTMPEntity.setQuotationStatusId(quotationStatusId);
		renewalPolicyTMPEntity.setQuotationSubStatusId(quotationSubStatusId);
		renewalPolicyTMPEntity.setQuotationTaggedStatusId(quotationTaggedStatusId);
		renewalPolicyTMPEntity.setMasterPolicyId(masterPolicyId);
		renewalPolicyTMPEntity.setQuotationDate(new Date());
		// renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
		renewalPolicyTMPEntity.setIsActive(true);
		renewalPolicyTMPEntity.setDateOfCommencement(masterPolicyEntity.getDateOfCommencement());
		renewalPolicyTMPEntity.setCreatedBy(createdBy);
		renewalPolicyTMPEntity.setCreatedDate(new Date());
		renewalPolicyTMPEntity.setQuotationNumber(getQuotationNumber);
		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

//		save AOMProps
		PolicyTmpAOMProps policyTmpAOMPropsExist = policyTmpAOMPropsRepository
				.findByTmpPolicyId(renewalPolicyTMPEntity.getId());
		if (policyTmpAOMPropsExist == null) {
			PolicyTmpAOMProps policyTmpAOMProps = new PolicyTmpAOMProps();
			// policyTmpAOMProps.setServiceNumber(policyServiceEntitiy.getId());
			policyTmpAOMProps.setTmpPolicyId(renewalPolicyTMPEntity.getId());
			policyTmpAOMProps.setAomStatusId(504L);
			policyTmpAOMProps.setCreatedBy(createdBy);
			policyTmpAOMProps.setCreatedDate(new Date());
			policyTmpAOMProps.setIsActive(true);
			policyTmpAOMPropsRepository.save(policyTmpAOMProps);
		}

//		if (renewalPolicyTMPEntityExist == null) {
		// save MPH
		MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
		TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytomastertoTemp(renewalPolicyTMPEntity.getId(),
				mPHEntity);
		tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
		renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		// scheme MastrePolicy to TMp copy

		Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository.findBypolicyId(masterPolicyId);

		if (policySchemeEntity.isPresent()) {

			RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = RenewalSchemeruleTMPHelper
					.pmsttoTmp(policySchemeEntity.get());
			renewalSchemeruleTMPEntity.setId(null);
			renewalSchemeruleTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
			renewalSchemeruleTMPEntity.setPmstSchemeRuleId(policySchemeEntity.get().getId());

			renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);
		}
		List<MemberCategoryModuleEntity> addMemberCategoryModuleEntity = new ArrayList<MemberCategoryModuleEntity>();
		List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository.findBypmstPolicyId(masterPolicyId);
		for (MemberCategoryEntity getmemberCategoryEntity : memberCategoryEntity) {
			MemberCategoryModuleEntity memberCategoryModuleEntity = new MemberCategoryModuleEntity();
			memberCategoryModuleEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
			memberCategoryModuleEntity.setMemberCategoryId(getmemberCategoryEntity.getId());
			memberCategoryModuleEntity.setPolicyId(getmemberCategoryEntity.getPmstPolicyId());
			memberCategoryModuleEntity.setCreatedBy(getmemberCategoryEntity.getCreatedBy());
			memberCategoryModuleEntity.setCreatedDate(new Date());
			memberCategoryModuleEntity.setIsActive(true);
			addMemberCategoryModuleEntity.add(memberCategoryModuleEntity);
		}
		memberCategoryModuleRepository.saveAll(addMemberCategoryModuleEntity);

		// save Life Cover
		List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository.findByPolicyId(masterPolicyId);
		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper.copyToTmpLifeCoverforClaim(
				policyLifeCoverEntity, memberCategoryEntity, renewalPolicyTMPEntity.getId());

		renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);

		// save GratuityBenefit
		List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
				.findBypolicyId(masterPolicyId);
		List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
				.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
						renewalPolicyTMPEntity.getId());

		renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);

		// valuation
		Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
				.findByPolicyId(masterPolicyId);
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
				.findByPolicyId(masterPolicyId);
		if (policyValuationMatrixEntity.isPresent()) {
			RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = RenewalValuationMatrixTMPHelper
					.pmsttoTmp(policyValuationMatrixEntity.get());
			renewalValuationMatrixTMPEntity.setId(null);
			renewalValuationMatrixTMPEntity.setGst(0.0);
			renewalValuationMatrixTMPEntity.setPremium(0.0);
			renewalValuationMatrixTMPEntity.setValuationDate(null);
			renewalValuationMatrixTMPEntity.setCurrentServiceLiability(0.0);
			renewalValuationMatrixTMPEntity.setPastServiceLiability(0.0);
			renewalValuationMatrixTMPEntity.setFutureServiceLiability(0.0);
			renewalValuationMatrixTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
			renewalValuationMatrixTMPEntity.setPmstValuationMatrixId(policyValuationMatrixEntity.get().getId());
			renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

			// valuation basic Entity
			if (renewalValuationMatrixTMPEntity.getValuationTypeId() == 1) {
				Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
						.findByPolicyId(masterPolicyId);
				if (policyValutationBasicEntity.isPresent()) {
					RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = RenewalValuationBasicTMPHelper
							.pmsttoTmp(policyValutationBasicEntity.get());
					renewalValuationBasicTMPEntity.setId(null);
					// renewalValuationBasicTMPEntity.setValuationTypeId(renewalValuationTypeId);
					renewalValuationBasicTMPEntity.setRateTable(policyValutationBasicEntity.get().getRateTable());
					renewalValuationBasicTMPEntity.setPmstValuationBasicId(policyValutationBasicEntity.get().getId());
					renewalValuationBasicTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
					renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

					// ValuationWithdrawalRate table
					List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
							.findByPolicyId(masterPolicyId);
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
//		taskAllocationEntity.setLocationType(quotationRenewalDto.getUnitCode());
		taskAllocationEntity.setCreatedBy(renewalPolicyTMPEntity.getCreatedBy());
		taskAllocationEntity.setCreatedDate(new Date());
		taskAllocationEntity.setModulePrimaryId(renewalPolicyTMPEntity.getId());
		taskAllocationEntity.setIsActive(true);
		taskAllocationRepository.save(taskAllocationEntity);
//		}

		return renewalPolicyTMPEntity;

	}

	// AOM
	@Autowired
	private BulkMemberUploadHelper bulkMemberUploadHelper;

	@Transactional
	@Override
	public ApiResponseDto<List<MemberTmpDto>> importMemberData(Long masterPolicyId, Long batchId, String username) {

		RenewalPolicyTMPEntity masterToTemp = masterToTemp(masterPolicyId, username); // request.getCreatedBy());
		bulkMemberUploadHelper.saveBulkMembersFromStg(masterToTemp.getId(), batchId, username);
		List<RenewalPolicyTMPMemberEntity> findBytmpPolicyId = renewalPolicyTMPMemberRepository
				.findByTmpPolicyId(masterToTemp.getId());
		List<MemberTmpDto> list = new ArrayList<>();
		for (RenewalPolicyTMPMemberEntity tempMemberEntity2 : findBytmpPolicyId) {
			MemberTmpDto conMemberTmpDto = modelMapper.map(tempMemberEntity2, MemberTmpDto.class);
			list.add(conMemberTmpDto);
		}
		return ApiResponseDto.success(list);
	}

	@Override
	public RenewalPolicyTMPMemberBankAccountEntity saveTempMemberBankAccount(
			RenewalPolicyTMPMemberBankAccountEntity request, Long memberId) {

		if (request.getId() != null) {
			Optional<RenewalPolicyTMPMemberBankAccountEntity> findById = renewalPolicyTMPMemberBankRepository
					.findById(request.getId());
			RenewalPolicyTMPMemberBankAccountEntity tempMemberBankAccountEntity = findById.get();

			tempMemberBankAccountEntity.setBankAccountNumber(request.getBankAccountNumber());
			tempMemberBankAccountEntity.setAccountType(request.getAccountType());
			tempMemberBankAccountEntity.setIfscCode(request.getIfscCode());
			tempMemberBankAccountEntity.setBankName(request.getBankName());
			tempMemberBankAccountEntity.setBankBranch(request.getBankBranch());
			tempMemberBankAccountEntity.setModifiedDate(new Date());
			tempMemberBankAccountEntity.setModifiedBy(request.getModifiedBy());
			return renewalPolicyTMPMemberBankRepository.save(tempMemberBankAccountEntity);
		} else {

			Optional<RenewalPolicyTMPMemberEntity> optionalTeamMember = renewalPolicyTMPMemberRepository
					.findById(memberId);

			RenewalPolicyTMPMemberEntity tempMemberEntity = null;
			if (optionalTeamMember.isPresent())
				tempMemberEntity = optionalTeamMember.get();

			request.setMember(tempMemberEntity);
			request.setCreatedDate(new Date());
			request.setModifiedDate(new Date());
			request.setIsActive(true);
			return renewalPolicyTMPMemberBankRepository.save(request);
		}

	}

	@Override
	public RenewalPolicyTMPMemberAddressEntity saveTempMemberAddress(RenewalPolicyTMPMemberAddressEntity request,
			Long memberId) {

		if (request.getId() != null) {
			Optional<RenewalPolicyTMPMemberAddressEntity> findById = renewalPolicyTMPMemberAddressRepository
					.findById(request.getId());
			RenewalPolicyTMPMemberAddressEntity tempMemberAddressEntity = findById.get();

			tempMemberAddressEntity.setAddressTypeId(request.getAddressTypeId());
			tempMemberAddressEntity.setCountry(request.getCountry());
			tempMemberAddressEntity.setPinCode(request.getPinCode());
			tempMemberAddressEntity.setDistrict(request.getDistrict());
			tempMemberAddressEntity.setCity(request.getCity());
			tempMemberAddressEntity.setAddress1(request.getAddress1());
			tempMemberAddressEntity.setAddress2(request.getAddress2());
			tempMemberAddressEntity.setAddress3(request.getAddress3());
			tempMemberAddressEntity.setModifiedDate(new Date());
			tempMemberAddressEntity.setModifiedBy(request.getModifiedBy());
			return renewalPolicyTMPMemberAddressRepository.save(tempMemberAddressEntity);
		} else {

			Optional<RenewalPolicyTMPMemberEntity> optionalTeamMember = renewalPolicyTMPMemberRepository
					.findById(memberId);

			RenewalPolicyTMPMemberEntity tempMemberEntity = null;
			if (optionalTeamMember.isPresent())
				tempMemberEntity = optionalTeamMember.get();

			request.setMember(tempMemberEntity);
			request.setCreatedDate(new Date());
			request.setModifiedDate(new Date());
			request.setIsActive(true);
			return renewalPolicyTMPMemberAddressRepository.save(request);
		}
	}

	@Override
	public RenewalPolicyTMPMemberNomineeEntity saveTempMemberNominee(RenewalPolicyTMPMemberNomineeEntity request,
			Long memberId) {
		if (request.getId() != null) {
			Optional<RenewalPolicyTMPMemberNomineeEntity> findById = renewalPolicyTMPMemberNomineeRepository
					.findById(request.getId());
			RenewalPolicyTMPMemberNomineeEntity tempMemberAddressEntity = findById.get();

			/* TODO: add editable fields */
			return renewalPolicyTMPMemberNomineeRepository.save(tempMemberAddressEntity);
		} else {

			Optional<RenewalPolicyTMPMemberEntity> optionalTeamMember = renewalPolicyTMPMemberRepository
					.findById(memberId);

			RenewalPolicyTMPMemberEntity tempMemberEntity = null;
			if (optionalTeamMember.isPresent())
				tempMemberEntity = optionalTeamMember.get();

			request.setMember(tempMemberEntity);
			request.setCreatedDate(new Date());
			request.setModifiedDate(new Date());
			request.setIsActive(true);
			return renewalPolicyTMPMemberNomineeRepository.save(request);
		}
	}

	@Override
	public RenewalPolicyTMPMemberAppointeeEntity saveTempMemberAppointee(RenewalPolicyTMPMemberAppointeeEntity request,
			Long memberId) {
		if (request.getId() != null) {
			Optional<RenewalPolicyTMPMemberAppointeeEntity> findById = renewalPolicyTMPMemberAppointeeRepository
					.findById(request.getId());
			RenewalPolicyTMPMemberAppointeeEntity tempMemberAddressEntity = findById.get();

			/* TODO: add editable fields */
			return renewalPolicyTMPMemberAppointeeRepository.save(tempMemberAddressEntity);
		} else {

			Optional<RenewalPolicyTMPMemberEntity> optionalTeamMember = renewalPolicyTMPMemberRepository
					.findById(memberId);

			RenewalPolicyTMPMemberEntity tempMemberEntity = null;
			if (optionalTeamMember.isPresent())
				tempMemberEntity = optionalTeamMember.get();

			request.setMember(tempMemberEntity);
			request.setCreatedDate(new Date());
			request.setModifiedDate(new Date());
			request.setIsActive(true);
			return renewalPolicyTMPMemberAppointeeRepository.save(request);
		}
	}

	@Override
	public ApiResponseDto<List<PolicyTmpSearchEntity>> quotationSearchPolicy(String policyNumber, String type) {

		Integer pickListItemId = null;
		// making String type case insensitive
		String type1 = "EXISTING";
		String type2 = "INPROGRESS";
		if (type.toUpperCase().equals(type1)) {
			pickListItemId = 79; // picklistitemid = taggedStatusId
		} else if (type.toUpperCase().equals(type2))
			pickListItemId = 78;

		List<PolicyTmpSearchEntity> policyDetails = policyTmpSearchRepository.findQuotationByPolicyId(pickListItemId,
				policyNumber);

		String unitCode = null;

		List<PolicyTmpSearchEntity> policyEnities = new ArrayList<>();

		for (PolicyTmpSearchEntity policyDetail : policyDetails) {
			unitCode = policyDetail.getUnitCode();
			String unitOf = this.getUnitOf(unitCode);
			JsonNode productCodeJsonNode = commonModuleService.getProductCodeJsonNode(policyDetail.getProductId());
			String textValue = productCodeJsonNode.path("productName").textValue();
			String variantCode = commonModuleService.getVariantCode(policyDetail.getProductVariantId());
			policyDetail.setProductName(textValue);
			policyDetail.setProductVariant(variantCode);
			policyDetail.setUnitOffice(unitOf);
			policyEnities.add(policyDetail);
		}
		return ApiResponseDto.success(policyEnities);

	}

	@Override
	public ApiResponseDto<List<PolicyTmpSearchEntity>> ServiceSerachPolicy(String policyNumber, String type) {

		Integer pickListItemId = null;

		String type1 = "EXISTING";
		String type2 = "INPROGRESS";
		String type3 = "NEW";

		List<PolicyTmpSearchEntity> policyDetails = null;

		List<Integer> statusIds = new ArrayList<Integer>();
		if (type.toUpperCase().equals(type1)) {
			statusIds.add(507);// policy status approved
			statusIds.add(506);// policy status rejected
			pickListItemId = 139;
			policyDetails = policyTmpSearchRepository.findServiceByPolicyId(pickListItemId, policyNumber, statusIds);
		} else if (type.toUpperCase().equals(type2)) {
			pickListItemId = 138;
			statusIds.add(505);// policy status send back to maker
			statusIds.add(504);// policy status waiting for approval)
			statusIds.add(513);// policy status Draft
			policyDetails = policyTmpSearchRepository.findServiceByPolicyId(pickListItemId, policyNumber, statusIds);
		} else if (type.toUpperCase().equals(type3)) {
			pickListItemId = 138;
			policyDetails = policyTmpSearchRepository.findNewServiceByPolicyId(pickListItemId, policyNumber);
		}

		String unitCode = null;

		List<PolicyTmpSearchEntity> policyEnities = new ArrayList<>();

		for (PolicyTmpSearchEntity policyDetail : policyDetails) {
			unitCode = policyDetail.getUnitCode();
			String unitOf = this.getUnitOf(unitCode);
			JsonNode productCodeJsonNode = commonModuleService.getProductCodeJsonNode(policyDetail.getProductId());
			String textValue = productCodeJsonNode.path("productName").textValue();
			String variantCode = commonModuleService.getVariantCode(policyDetail.getProductVariantId());
			policyDetail.setProductName(textValue);
			policyDetail.setProductVariant(variantCode);
			policyDetail.setUnitOffice(unitOf);
			policyEnities.add(policyDetail);
		}
		return ApiResponseDto.success(policyEnities);
	}

	@Transactional
	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> sentForApproval(Long tmpPolicyid, String username) {
		Long qutationStatusId1 = 17L;
		Long qutationSubStatusId1 = 21L;
		Long qutationTaggedStatuss = 78L;

		Long masterPolicyId = null;

		try {

			Optional<RenewalPolicyTMPEntity> findById = renewalPolicyTMPRepository.findById(tmpPolicyid);
			{
				masterPolicyId = findById.get().getMasterPolicyId();
			}
//			save service
			PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
			PolicyServiceEntitiy policyserviceentityExisiting = policyServiceRepository
					.findByPolicyandTypeandActive(masterPolicyId, PolicyServiceName.MEMBER_ADDITION.getName());

			if (policyserviceentityExisiting != null) {
				policyServiceEntitiy.setId(policyserviceentityExisiting.getId());
				policyServiceEntitiy.setCreatedBy(username);
				policyServiceEntitiy.setCreatedDate(new Date());
				policyServiceEntitiy.setIsActive(true);
			} else {
				policyServiceEntitiy.setServiceType(PolicyServiceName.MEMBER_ADDITION.getName());
				policyServiceEntitiy.setPolicyId(masterPolicyId);
				policyServiceEntitiy.setCreatedBy(username);
				policyServiceEntitiy.setCreatedDate(new Date());
				policyServiceEntitiy.setIsActive(true);
			}
			policyserviceentityExisiting = policyServiceRepository.save(policyServiceEntitiy);
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyid).get();

			renewalPolicyTMPEntity.setPolicyServiceId(policyserviceentityExisiting.getId());
			renewalPolicyTMPEntity.setModifiedBy(username);
			renewalPolicyTMPEntity.setModifiedDate(new Date());
			renewalPolicyTMPEntity.setQuotationStatusId(qutationStatusId1);
			renewalPolicyTMPEntity.setQuotationSubStatusId(qutationSubStatusId1);
			renewalPolicyTMPEntity.setQuotationTaggedStatusId(qutationTaggedStatuss);

			renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

			TaskAllocationEntity taskallocationentity = taskAllocationRepository
					.findTopByRequestId(renewalPolicyTMPEntity.getQuotationNumber());
			taskallocationentity.setTaskStatus(qutationSubStatusId1.toString());
			taskAllocationRepository.save(taskallocationentity);

			RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
					.entityToDtoTemPolicy(renewalPolicyTMPEntity);

			return ApiResponseDto.success(entityToDtoTemPolicy);
		} catch (Exception e) {
			logger.error(e.getStackTrace());
			return null;
		}
	}

	// checker will Reject
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

		TaskAllocationEntity taskallocationentity = taskAllocationRepository
				.findTopByRequestId(renewalPolicyTMPEntity.getQuotationNumber());
		taskallocationentity.setTaskStatus(qutationStatusId1.toString());
		taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);
		return ApiResponseDto.success(entityToDtoTemPolicy);

	}

	// checker will approve
	@Transactional
	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> forApprove(Long tempPolicyId, String username) {

		Long qutationStatusId1 = 20L;
		Long qutationSubStatusId1 = 85L;

		Long taggedStatusId = 79L;

		Long policyStaggedStatusId = 138L;
		Long policySubStaggedStatusId = 133L;

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tempPolicyId).get();
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());
		renewalPolicyTMPEntity.setQuotationStatusId(qutationStatusId1);
		renewalPolicyTMPEntity.setQuotationSubStatusId(qutationSubStatusId1);
		renewalPolicyTMPEntity.setQuotationTaggedStatusId(taggedStatusId);
		renewalPolicyTMPEntity.setPolicytaggedStatusId(policyStaggedStatusId);
		renewalPolicyTMPEntity.setPolicySubStatusId(policySubStaggedStatusId);
		renewalPolicyTMPEntity.setPolicyStatusId(null);
		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		TaskAllocationEntity taskallocationentity = taskAllocationRepository
				.findTopByRequestId(renewalPolicyTMPEntity.getQuotationNumber());
		taskallocationentity.setTaskStatus(qutationStatusId1.toString());
		taskAllocationRepository.save(taskallocationentity);

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
			/*
			 * currentServiceLiability = currentServiceLiability +
			 * (gratuityCalculationEntity.getCurrentServiceBenefitDeath() != null ?
			 * gratuityCalculationEntity.getCurrentServiceBenefitDeath() : 0.0D) +
			 * (gratuityCalculationEntity.getCurrentServiceBenefitRet() != null ?
			 * gratuityCalculationEntity.getCurrentServiceBenefitRet() : 0.0D) +
			 * (gratuityCalculationEntity.getCurrentServiceBenefitWdl() != null ?
			 * gratuityCalculationEntity.getCurrentServiceBenefitWdl() : 0.0D);
			 * pastServiceLiabilty = pastServiceLiabilty +
			 * +(gratuityCalculationEntity.getPastServiceBenefitDeath() != null ?
			 * gratuityCalculationEntity.getPastServiceBenefitDeath() : 0.0D) +
			 * (gratuityCalculationEntity.getPastServiceBenefitRet() != null ?
			 * gratuityCalculationEntity.getPastServiceBenefitRet() : 0.0D) +
			 * (gratuityCalculationEntity.getPastServiceBenefitWdl() != null ?
			 * gratuityCalculationEntity.getPastServiceBenefitWdl() : 0.0D);
			 */
//							futureServiceLiability = futureServiceLiability
//									+ (gratuityCalculationEntity.getTerm() != null ? gratuityCalculationEntity.getTerm() : 0.0F);
		}

		Double gstAmount = 0.0D;
		if (renewalPolicyTMPEntity.getGstApplicableId().equals(1L)) {
			StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
			gstAmount = oYRGTARenewalPremium * (Double.parseDouble(standardCodeEntity.getValue()) / 100);
		}

		List<QuotationChargeEntity> quotationChargeEntities = new ArrayList<QuotationChargeEntity>();
		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
				.chargeTypeId(215L).amountCharged(oYRGTARenewalPremium).balanceAmount(0).isActive(true)
				.createdBy(username).createdDate(new Date()).build());
		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
				.chargeTypeId(216L).amountCharged(gstAmount).balanceAmount(0).isActive(true).createdBy(username)
				.createdDate(new Date()).build());

		quotationChargeRepository.saveAll(quotationChargeEntities);

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

		TaskAllocationEntity taskallocationentity = taskAllocationRepository
				.findByRequestId(renewalPolicyTMPEntity.getQuotationNumber());
		taskallocationentity.setTaskStatus(sentBackToMakerSubStatudId.toString());
		taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyForApproval(Long id, String username) {

		Long policyStatusId1 = 504L;
		Long policysubStatusId2 = 133L;

		Long policyTaggedStatuss = 138L;

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());
		renewalPolicyTMPEntity.setPolicyStatusId(policyStatusId1);
		renewalPolicyTMPEntity.setPolicySubStatusId(policysubStatusId2);
		renewalPolicyTMPEntity.setPolicytaggedStatusId(policyTaggedStatuss);

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		TaskAllocationEntity taskallocationentity = taskAllocationRepository
				.findByRequestId(renewalPolicyTMPEntity.getId().toString());
		taskallocationentity.setTaskStatus(policysubStatusId2.toString());
		taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyBacktoMaker(Long id, String username) {

		Long policyStatusId2 = 505L;
		Long policysubStatusId1 = 133L;
		Long policyTaggedStatuss = 138L;

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(id).get();
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());
		renewalPolicyTMPEntity.setPolicyStatusId(policyStatusId2);
		renewalPolicyTMPEntity.setPolicySubStatusId(policysubStatusId1);

		renewalPolicyTMPEntity.setPolicytaggedStatusId(policyTaggedStatuss);

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		TaskAllocationEntity taskallocationentity = taskAllocationRepository
				.findByRequestId(renewalPolicyTMPEntity.getId().toString());
		taskallocationentity.setTaskStatus(policysubStatusId1.toString());
		taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Transactional
	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforApprove(Long tempid, String username) throws Exception {

		Long policyStatusId2 = 507L;
		Long policysubStatusId1 = 136L;
		Long policyTaggedStatuss = 139L;

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tempid).get();
		renewalPolicyTMPEntity.setModifiedBy(username);
		renewalPolicyTMPEntity.setModifiedDate(new Date());
		renewalPolicyTMPEntity.setPolicyStatusId(policyStatusId2);
		renewalPolicyTMPEntity.setPolicySubStatusId(policysubStatusId1);
		renewalPolicyTMPEntity.setPolicytaggedStatusId(policyTaggedStatuss);

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		List<PolicyMemberEntity> tt = PolicyMemberHelper.tmpMemberToMasterEntity(
				renewalPolicyTMPMemberRepository.findBytmpPolicyId(tempid), renewalPolicyTMPEntity.getMasterPolicyId(),
				policyMemberRepository.maxLicNumber(renewalPolicyTMPEntity.getMasterPolicyId()));

		policyMemberRepository.saveAll(tt);
		this.copyTmpToMaster(tempid, username);

		TaskAllocationEntity taskallocationentity = taskAllocationRepository
				.findByRequestId(renewalPolicyTMPEntity.getId().toString());
		taskallocationentity.setTaskStatus(policysubStatusId1.toString());
		taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		PolicyTmpAOMProps policyTmpAOMProps = policyTmpAOMPropsRepository.findByTmpPolicyId(tempid);
		policyTmpAOMProps.setIsActive(false);
		policyTmpAOMPropsRepository.save(policyTmpAOMProps);

		PolicyServiceEntitiy policyServiceEntitiy = policyServiceRepository
				.findById(renewalPolicyTMPEntity.getPolicyServiceId()).get();
		policyServiceEntitiy.setIsActive(false);

		policyServiceRepository.save(policyServiceEntitiy);

		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository
				.findById(renewalPolicyTMPEntity.getMasterPolicyId()).get();
		if (isDevEnvironment == false) {

			String prodAndVarientCodeSame = commonModuleService.getProductCode(renewalPolicyTMPEntity.getProductId());
			String unitStateName = commonMasterUnitRepository.getStateNameByUnitCode(masterPolicyEntity.getUnitCode());
			String unitStateType = commonMasterStateRepository.getStateType(unitStateName);
			String unitStateCode = commonMasterStateRepository.getGSTStateCode(unitStateName);

			String mPHStateType = null;
			String mPHAddress = "";
			String mphStateCode = "";

			MPHEntity getMPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
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

			PolicyContributionDetailEntity policyContributionDetailEntity = policyContributionDetailRepository
					.findByTmpPolicyandType(renewalPolicyTMPEntity.getId(), "MJ");

//			MasterPolicyContributionDetails masterPolicyContributionDetails = masterPolicyContributionDetailRepository
//					.findBymasterPolicyId(masterPolicyEntity.getId());
			HSNCodeDto hSNCodeDto = accountingService.getHSNCode();
			Map<String, Double> gstComponents = accountingService.getGstComponents(unitStateType, mPHStateType,
					hSNCodeDto, policyContributionDetailEntity.getLifePremium());

			NewBusinessContributionAndLifeCoverAdjustmentDto newBusinessContributionAndLifeCoverAdjustmentDto = new NewBusinessContributionAndLifeCoverAdjustmentDto();
			newBusinessContributionAndLifeCoverAdjustmentDto.setAdjustmentAmount(
					policyContributionDetailEntity.getLifePremium() + policyContributionDetailEntity.getGst());
			newBusinessContributionAndLifeCoverAdjustmentDto
					.setAdjustmentNo(policyContributionDetailEntity.getId().intValue());
			newBusinessContributionAndLifeCoverAdjustmentDto
					.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
			newBusinessContributionAndLifeCoverAdjustmentDto.setIsGstApplicable(false);
			if (masterPolicyEntity.getGstApplicableId() == 1l)
				newBusinessContributionAndLifeCoverAdjustmentDto.setIsGstApplicable(true);
			newBusinessContributionAndLifeCoverAdjustmentDto.setMphCode(getMPHEntity.getMphCode());
			newBusinessContributionAndLifeCoverAdjustmentDto.setUnitCode(masterPolicyEntity.getUnitCode());
			newBusinessContributionAndLifeCoverAdjustmentDto.setUserCode(username);

			newBusinessContributionAndLifeCoverAdjustmentDto.setGlTransactionModel(accountingService
					.getGlTransactionModel(masterPolicyEntity.getProductId(), masterPolicyEntity.getProductVariantId(),
							masterPolicyEntity.getUnitCode(), "Mid Joiner Approval"));

			String toGSTIn = getMPHEntity.getGstIn() == null ? "" : getMPHEntity.getGstIn();
			ResponseDto responseDto = accountingService
					.commonmasterserviceAllUnitCode(masterPolicyEntity.getUnitCode());
			GstDetailModelDto gstDetailModelDto = new GstDetailModelDto();
			gstDetailModelDto.setAmountWithTax(
					policyContributionDetailEntity.getLifePremium() + policyContributionDetailEntity.getGst());
			gstDetailModelDto.setAmountWithoutTax(policyContributionDetailEntity.getLifePremium());
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

			if (masterPolicyEntity.getGstApplicableId() == 1l)
				gstDetailModelDto.setGstApplicableType("Taxable");
			else
				gstDetailModelDto.setGstApplicableType("Non-Taxable");

			gstDetailModelDto.setGstInvoiceNo(""); // From Docu
			gstDetailModelDto.setGstRate(accountingService.getGSTRate(unitStateType, mPHStateType, hSNCodeDto) * 100);
			gstDetailModelDto.setGstRefNo(renewalPolicyTMPEntity.getPolicyNumber());// From Docu
			gstDetailModelDto.setGstRefTransactionNo(renewalPolicyTMPEntity.getPolicyNumber());// From Docu
			gstDetailModelDto.setGstTransactionType("CREDIT");// From Docu
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
			gstDetailModelDto.setRemarks("Gratuity NB Deposit Adjustment");
			gstDetailModelDto.setSgstAmount(gstComponents.get("SGST"));
			gstDetailModelDto.setSgstRate(hSNCodeDto.getSgstRate());
			gstDetailModelDto.setToGstIn(getMPHEntity.getGstIn() == null ? "" : getMPHEntity.getGstIn()); // From Docu
																											// from get
																											// Common
																											// Module
			gstDetailModelDto.setToPan(getMPHEntity.getPan() == null ? "" : getMPHEntity.getPan());
			gstDetailModelDto.setToStateCode(mphStateCode == null ? "" : mphStateCode);
			gstDetailModelDto.setTotalGstAmount(policyContributionDetailEntity.getGst().doubleValue());
			gstDetailModelDto.setTransactionDate(new Date());
			gstDetailModelDto.setTransactionSubType("A"); // From Docu
			gstDetailModelDto.setTransactionType("C"); // From Docu
//			gstDetailModelDto.setUserCode(username);
			gstDetailModelDto.setUtgstAmount(gstComponents.get("UTGST"));

			gstDetailModelDto.setUtgstRate(hSNCodeDto.getUtgstRate());
			gstDetailModelDto.setVariantCode(prodAndVarientCodeSame);
			gstDetailModelDto.setYear(DateUtils.uniqueNoYYYY());
			gstDetailModelDto.setMonth(DateUtils.GSTInvoiceMonthCode());
			newBusinessContributionAndLifeCoverAdjustmentDto.setGstDetailModel(gstDetailModelDto);

			JournalVoucherDetailModelDto journalVoucherDetailModelDto = new JournalVoucherDetailModelDto();
			journalVoucherDetailModelDto.setLineOfBusiness(masterPolicyEntity.getLineOfBusiness());
			journalVoucherDetailModelDto.setProduct(prodAndVarientCodeSame);
			journalVoucherDetailModelDto.setProductVariant(prodAndVarientCodeSame);
			newBusinessContributionAndLifeCoverAdjustmentDto.setJournalVoucherDetailModel(journalVoucherDetailModelDto);

			List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			List<NewBusinessContriDebitCreditRequestModelDto> getNewBusinessContriDebitCreditRequestModelDto = new ArrayList<NewBusinessContriDebitCreditRequestModelDto>();
//			Long premiumAdjustmentLong = Math.round(policyContributionDetailEntity.getLifePremium());
//			Double premiumAdjustment = premiumAdjustmentLong.doubleValue();
			Double premiumAdjustment = policyContributionDetailEntity.getLifePremium();

			Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();

			for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {
				NewBusinessContriDebitCreditRequestModelDto newBusinessContriDebitCreditRequestModelDto = new NewBusinessContriDebitCreditRequestModelDto();
				newBusinessContriDebitCreditRequestModelDto
						.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				// Long availableLong = Math.round(policyDepositEntity.getAdjustmentAmount());
				// Double availableAmount = availableLong.doubleValue();

				Double availableAmount = policyDepositEntity.getAdjustmentAmount();
				// policyDepositEntity.setAvailableAmount(availableAmount);

				Double depositDebitAmount = 0.0;

				if (premiumAdjustment > 0 && availableAmount > 0) {

					if (policyContributionDetailEntity.getLifePremium() <= availableAmount) {

						newBusinessContriDebitCreditRequestModelDto
								.setOYRGTAFirstPremiumCreditAmount(premiumAdjustment);
						availableAmount -= premiumAdjustment;
						depositDebitAmount += premiumAdjustment;
					} else {

						newBusinessContriDebitCreditRequestModelDto
								.setOYRGTAFirstPremiumCreditAmount(policyDepositEntity.getAvailableAmount());

						premiumAdjustment -= policyDepositEntity.getAvailableAmount();
						availableAmount -= policyDepositEntity.getAvailableAmount();
						depositDebitAmount += policyDepositEntity.getAvailableAmount();
					}
				}

				if (gstOnPremiumAdjusted > 0 && availableAmount > 0) {

					if (policyContributionDetailEntity.getGst() <= policyDepositEntity.getAvailableAmount()) {
						newBusinessContriDebitCreditRequestModelDto
								.setGstOnPremiumCreditAmount(policyContributionDetailEntity.getGst());

						availableAmount -= policyContributionDetailEntity.getGst();
						depositDebitAmount += policyContributionDetailEntity.getGst();
					} else {
						newBusinessContriDebitCreditRequestModelDto
								.setGstOnPremiumCreditAmount(policyDepositEntity.getAvailableAmount());

						gstOnPremiumAdjusted -= policyDepositEntity.getAvailableAmount();
						availableAmount -= policyDepositEntity.getAvailableAmount();
						depositDebitAmount += policyDepositEntity.getAvailableAmount();
					}
				}

//				Long depositDebitAmountLong=Math.round(depositDebitAmount);
				newBusinessContriDebitCreditRequestModelDto.setDepositDebitAmount(depositDebitAmount);
				getNewBusinessContriDebitCreditRequestModelDto.add(newBusinessContriDebitCreditRequestModelDto);

			}

			newBusinessContributionAndLifeCoverAdjustmentDto
					.setNewBusinessContriDebitCreditRequestModel(getNewBusinessContriDebitCreditRequestModelDto);
			newBusinessContributionAndLifeCoverAdjustmentDto
					.setRefNo(policyContributionDetailEntity.getId().toString());
			Boolean consumeDeposits = accountingService
					.consumeDeposits(newBusinessContributionAndLifeCoverAdjustmentDto,masterPolicyEntity.getId());
			if (consumeDeposits == false) {
				throw new Exception("Accounting Failed: Consume Deposits Failed");

			}

//			String prodAndVarientCodeSame=	commonModuleService.getProductCode(policyEntity.getProductId());
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

			ResponseDto unlockDeposits = accountingService.unlockDeposits(showDepositLockDto, username);

			if (unlockDeposits.getStatus().equals("FALSE")) {
				throw new Exception("Accountung failed:unlock Deposist fAILED");
			}

		}

		this.sendMPHLetterInEmail(tempid);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforReject(Long id, String username,
			RenewalPolicyTMPDto renewalPolicyTMPDto) {

		// calling Unlock api Accounting APi
		Long policyStatusId2 = 506L;
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

		TaskAllocationEntity taskallocationentity = taskAllocationRepository
				.findByRequestId(renewalPolicyTMPEntity.getId().toString());
		taskallocationentity.setTaskStatus(policysubStatusId1.toString());
		taskAllocationRepository.save(taskallocationentity);

		RenewalPolicyTMPDto entityToDtoTemPolicy = RenewalValuationTMPHelper
				.entityToDtoTemPolicy(renewalPolicyTMPEntity);
		PolicyTmpAOMProps policyTmpAOMProps = policyTmpAOMPropsRepository.findByTmpPolicyId(id);
		policyTmpAOMProps.setIsActive(false);
		policyTmpAOMPropsRepository.save(policyTmpAOMProps);

		PolicyServiceEntitiy policyServiceEntitiy = policyServiceRepository
				.findById(renewalPolicyTMPEntity.getPolicyServiceId()).get();
		policyServiceEntitiy.setIsActive(false);

		policyServiceRepository.save(policyServiceEntitiy);

		return ApiResponseDto.success(entityToDtoTemPolicy);
	}

	@Transactional
	@Override
	public ApiResponseDto<ValuationMatrixDto> calculatePremeium(Long tempPolicyId, String userName) {
		// quotarionId/"Quotation"/''/'X04'/

		// if policy has been renewed
		// AOCM
		// else
		// RATE TABLE

		Double result = null;
		ValuationMatrixDto valuationMatrixDto = null;
		Optional<RenewalPolicyTMPEntity> findById = renewalPolicyTMPRepository.findById(tempPolicyId);

		if (findById.isPresent()) {
			Long masterPolicyId = findById.get().getMasterPolicyId();
			List<PolicyServiceEntitiy> findByPolicyId = policyServiceRepository.findByPolicyId(masterPolicyId);

			if (findByPolicyId.size() > 0) {
				RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tempPolicyId).get();
				RenewalValuationBasicTMPEntity valuationBasicEntity = renewalValuationBasicTMPRepository
						.findBytmpPolicyId(tempPolicyId).get();

				gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						valuationBasicEntity.getModfdPremRateCrdbiltyFctr().toString());
				gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						valuationBasicEntity.getModfdPremRateCrdbiltyFctr().toString());
				gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						valuationBasicEntity.getModfdPremRateCrdbiltyFctr().toString());
				gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						valuationBasicEntity.getModfdPremRateCrdbiltyFctr().toString());
				gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						valuationBasicEntity.getModfdPremRateCrdbiltyFctr().toString());

				List<GratuityCalculationEntity> gratuityCalculationEntities = gratuityCalculationRepository
						.findAllByTmpMemberId(tempPolicyId);

				Optional<RenewalValuationMatrixTMPEntity> valuationMatrixEntity = renewalValuationMatrixTMPRepository
						.findBytmpPolicyId(tempPolicyId);
				RenewalValuationMatrixTMPEntity newValuationMatrixEntity = null;
				if (valuationMatrixEntity.isPresent()) {
					newValuationMatrixEntity = valuationMatrixEntity.get();
					newValuationMatrixEntity.setModifiedBy(userName);
					newValuationMatrixEntity.setModifiedDate(new Date());
				} else {
					newValuationMatrixEntity = new RenewalValuationMatrixTMPEntity();
					newValuationMatrixEntity.setCreatedBy(userName);
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
						pastServiceWithdrawal = pastServiceWithdrawal
								+ gratuityCalculationEntity.getPastServiceBenefitWdl();
					if (gratuityCalculationEntity.getPastServiceBenefitRet() != null)
						pastServiceRetirement = pastServiceRetirement
								+ gratuityCalculationEntity.getPastServiceBenefitRet();
					if (gratuityCalculationEntity.getCurrentServiceBenefitDeath() != null)
						currentServiceDeath = currentServiceDeath
								+ gratuityCalculationEntity.getCurrentServiceBenefitDeath();
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

				// Renewal Policy Member data Update Grat calculation
				Double currentServiceLiability = 0.0D;
				Double pastServiceLiability = 0.0D;
				Double futureServiceLiability = 0.0D;
				Double premium = 0.0D;
				Double totalSumAssured = 0.0D;
				Double gst = 0.0D;
				if (renewalPolicyTMPEntity.getGstApplicableId().equals(1L)) {
					StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
					gst = Double.parseDouble(standardCodeEntity.getValue()) / 100;
				}
				new ArrayList<RenewalPolicyTMPMemberEntity>();
				for (GratuityCalculationEntity gratuityCalculationEntity : gratuityCalculationEntities) {
					RenewalPolicyTMPMemberEntity getMemberEntity = renewalPolicyTMPMemberRepository
							.findById(gratuityCalculationEntity.getMemberId()).get();

					// calculate the premium

					RenewalPolicyTMPEntity findByTmpPolicyId = renewalPolicyTMPRepository
							.findByTmpPolicyId(tempPolicyId);

					Long memberId = getMemberEntity.getId();
					Date dateOfJoining = getMemberEntity.getDojToScheme();

					Date policyEndDate = findByTmpPolicyId.getPolicyEndDate();
					gratuityCalculationEntity = gratuityCalculationRepository.findByMemberId(memberId);
					gratuityCalculationEntity.getId();
					Double lcPremium = gratuityCalculationEntity.getLcPremium();

					Calendar startDate = new GregorianCalendar();
					startDate.setTime(dateOfJoining);

					Calendar endDate = new GregorianCalendar();
					endDate.setTime(policyEndDate);

					int diffInYear = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
					int diffInMonths = diffInYear * 12
							+ (endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH) + 1);

					result = (lcPremium / 12) * diffInMonths;

					totalSumAssured = totalSumAssured + (gratuityCalculationEntity.getLcSumAssured() != null
							? gratuityCalculationEntity.getLcSumAssured()
							: 0.0D);
					premium = premium + (gratuityCalculationEntity.getLcPremium() != null ? result : 0.0D);
					/*
					 * currentServiceLiability = currentServiceLiability +
					 * (gratuityCalculationEntity.getCurrentServiceBenefitDeath() != null ?
					 * gratuityCalculationEntity.getCurrentServiceBenefitDeath() : 0.0D) +
					 * (gratuityCalculationEntity.getCurrentServiceBenefitRet() != null ?
					 * gratuityCalculationEntity.getCurrentServiceBenefitRet() : 0.0D) +
					 * (gratuityCalculationEntity.getCurrentServiceBenefitWdl() != null ?
					 * gratuityCalculationEntity.getCurrentServiceBenefitWdl() : 0.0D);
					 * pastServiceLiability = pastServiceLiability +
					 * (gratuityCalculationEntity.getPastServiceBenefitDeath() != null ?
					 * gratuityCalculationEntity.getPastServiceBenefitDeath() : 0.0D) +
					 * (gratuityCalculationEntity.getPastServiceBenefitRet() != null ?
					 * gratuityCalculationEntity.getPastServiceBenefitRet() : 0.0D) +
					 * (gratuityCalculationEntity.getPastServiceBenefitWdl() != null ?
					 * gratuityCalculationEntity.getPastServiceBenefitWdl() : 0.0D);
					 */

					getMemberEntity.setLifeCoverPremium(result);
					getMemberEntity.setLifeCoverSumAssured(gratuityCalculationEntity.getLcSumAssured());
					getMemberEntity.setAccruedGratuity(gratuityCalculationEntity.getAccruedGra());
					getMemberEntity.setTotalGratuity(gratuityCalculationEntity.getTotalGra());
					getMemberEntity.setGratCalculationId(gratuityCalculationEntity.getId());
					renewalPolicyTMPMemberRepository.save(getMemberEntity);
				}

				Double depositBalanceAmount = getDepositBalanceAmount(tempPolicyId, userName);

				newValuationMatrixEntity.setValuationTypeId(1L);
				newValuationMatrixEntity.setValuationDate(new Date());
				newValuationMatrixEntity.setCurrentServiceLiability(currentServiceLiability);
				newValuationMatrixEntity.setPastServiceLiability(pastServiceLiability);
				newValuationMatrixEntity.setFutureServiceLiability(futureServiceLiability);
				newValuationMatrixEntity.setPremium(premium);
				newValuationMatrixEntity.setGst(premium * gst);
				newValuationMatrixEntity.setTotalPremium(premium + (premium * gst));
				newValuationMatrixEntity.setAmountPayable(premium + (premium * gst) + currentServiceLiability
						+ pastServiceLiability + futureServiceLiability);
				newValuationMatrixEntity.setAmountReceived(depositBalanceAmount);
				newValuationMatrixEntity.setBalanceToBePaid(
						newValuationMatrixEntity.getAmountPayable() - newValuationMatrixEntity.getAmountReceived());
				newValuationMatrixEntity.setTotalSumAssured(totalSumAssured);

				RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
						.save(newValuationMatrixEntity);

				valuationMatrixDto = modelMapper.map(renewalValuationMatrixTMPEntity, ValuationMatrixDto.class);
				renewalPolicyTMPEntity.setIsActive(false);
				renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

				return ApiResponseDto.success(valuationMatrixDto);
			} else {
				RenewalValuationBasicTMPEntity valuationBasicEntity = renewalValuationBasicTMPRepository
						.findBytmpPolicyId(tempPolicyId).get();

				gratuityCalculationRepository.calculateGratuity11(tempPolicyId, "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity21(tempPolicyId, "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity31(tempPolicyId, "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity41(tempPolicyId, "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity51(tempPolicyId, "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());

				System.out.println(valuationBasicEntity.getRateTable());
				List<GratuityCalculationEntity> gratuityCalculationEntities = gratuityCalculationRepository
						.findAllByTmpMemberId(tempPolicyId);

				System.out.println(gratuityCalculationEntities.get(0).getLcPremium());
				Optional<RenewalValuationMatrixTMPEntity> valuationMatrixEntity = renewalValuationMatrixTMPRepository
						.findBytmpPolicyId(tempPolicyId);
				RenewalValuationMatrixTMPEntity newValuationMatrixEntity = null;
				if (valuationMatrixEntity.isPresent()) {
					newValuationMatrixEntity = valuationMatrixEntity.get();
					newValuationMatrixEntity.setModifiedBy(userName);
					newValuationMatrixEntity.setModifiedDate(new Date());
				} else {
					newValuationMatrixEntity = new RenewalValuationMatrixTMPEntity();
					newValuationMatrixEntity.setCreatedBy(userName);
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
						pastServiceWithdrawal = pastServiceWithdrawal
								+ gratuityCalculationEntity.getPastServiceBenefitWdl();
					if (gratuityCalculationEntity.getPastServiceBenefitRet() != null)
						pastServiceRetirement = pastServiceRetirement
								+ gratuityCalculationEntity.getPastServiceBenefitRet();
					if (gratuityCalculationEntity.getCurrentServiceBenefitDeath() != null)
						currentServiceDeath = currentServiceDeath
								+ gratuityCalculationEntity.getCurrentServiceBenefitDeath();
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

				renewalValuationBasicTMPRepository.save(valuationBasicEntity);

				// Renewal Policy Member data Update Grat calculation
				Double currentServiceLiability = 0.0D;
				Double pastServiceLiability = 0.0D;
				Double futureServiceLiability = 0.0D;
				Double premium = 0.0D;
				Double totalSumAssured = 0.0D;
				Double gst = 0.0D;
				RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tempPolicyId).get();

				if (renewalPolicyTMPEntity.getGstApplicableId().equals(1L)) {
					StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
					gst = Double.parseDouble(standardCodeEntity.getValue()) / 100;
				}

				for (GratuityCalculationEntity gratuityCalculationEntity : gratuityCalculationEntities) {

					RenewalPolicyTMPMemberEntity getMemberEntity = renewalPolicyTMPMemberRepository
							.findById(gratuityCalculationEntity.getMemberId()).get();
					// calculate the premium

					RenewalPolicyTMPEntity findByTmpPolicyId = renewalPolicyTMPRepository
							.findByTmpPolicyId(tempPolicyId);

					Long memberId = getMemberEntity.getId();
					Date dateOfJoining = getMemberEntity.getDojToScheme();

					Date policyEndDate = findByTmpPolicyId.getPolicyEndDate();

					gratuityCalculationEntity = gratuityCalculationRepository.findByMemberId(memberId);
					gratuityCalculationEntity.getId();
					Double lcPremium = gratuityCalculationEntity.getLcPremium();

					/*
					 * LocalDate dojToScheme = LocalDate.parse((CharSequence) dateOfJoining);
					 * LocalDate policyEnd = LocalDate.parse((CharSequence) policyEndDate);
					 * 
					 * int months = Period.between(dojToScheme, policyEnd).getMonths();
					 */

					Calendar startDate = new GregorianCalendar();
					startDate.setTime(dateOfJoining);

					Calendar endDate = new GregorianCalendar();
					endDate.setTime(policyEndDate);

					startDate.set(Calendar.DATE, endDate.get(Calendar.DATE));

					int diffInYear = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
					int diffInMonths = diffInYear * 12 + endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);

					result = (lcPremium / 12) * diffInMonths;

					totalSumAssured = totalSumAssured + (gratuityCalculationEntity.getLcSumAssured() != null
							? gratuityCalculationEntity.getLcSumAssured()
							: 0.0D);
					premium = premium + (gratuityCalculationEntity.getLcPremium() != null ? result : 0.0D);
					/*
					 * currentServiceLiability = currentServiceLiability +
					 * (gratuityCalculationEntity.getCurrentServiceBenefitDeath() != null ?
					 * gratuityCalculationEntity.getCurrentServiceBenefitDeath() : 0.0D) +
					 * (gratuityCalculationEntity.getCurrentServiceBenefitRet() != null ?
					 * gratuityCalculationEntity.getCurrentServiceBenefitRet() : 0.0D) +
					 * (gratuityCalculationEntity.getCurrentServiceBenefitWdl() != null ?
					 * gratuityCalculationEntity.getCurrentServiceBenefitWdl() : 0.0D);
					 * pastServiceLiability = pastServiceLiability +
					 * (gratuityCalculationEntity.getPastServiceBenefitDeath() != null ?
					 * gratuityCalculationEntity.getPastServiceBenefitDeath() : 0.0D) +
					 * (gratuityCalculationEntity.getPastServiceBenefitRet() != null ?
					 * gratuityCalculationEntity.getPastServiceBenefitRet() : 0.0D) +
					 * (gratuityCalculationEntity.getPastServiceBenefitWdl() != null ?
					 * gratuityCalculationEntity.getPastServiceBenefitWdl() : 0.0D);
					 */

					gratuityCalculationEntity.setLcPremium(result);
					gratuityCalculationEntity = gratuityCalculationRepository.save(gratuityCalculationEntity);

					getMemberEntity.setLifeCoverPremium(result);
					getMemberEntity.setLifeCoverSumAssured(gratuityCalculationEntity.getLcSumAssured());
					getMemberEntity.setAccruedGratuity(gratuityCalculationEntity.getAccruedGra());
					getMemberEntity.setTotalGratuity(gratuityCalculationEntity.getTotalGra());
					getMemberEntity.setGratCalculationId(gratuityCalculationEntity.getId());
					renewalPolicyTMPMemberRepository.save(getMemberEntity);
				}

				Double depositBalanceAmount = getDepositBalanceAmount(tempPolicyId, userName);
				newValuationMatrixEntity.setValuationTypeId(1L);
				newValuationMatrixEntity.setValuationDate(new Date());
				newValuationMatrixEntity.setCurrentServiceLiability(currentServiceLiability);
				newValuationMatrixEntity.setPastServiceLiability(pastServiceLiability);
				newValuationMatrixEntity.setFutureServiceLiability(futureServiceLiability);
				newValuationMatrixEntity.setPremium(premium);
				newValuationMatrixEntity.setGst(premium * gst);
				newValuationMatrixEntity.setTotalPremium(premium + (premium * gst));
				newValuationMatrixEntity.setAmountPayable(premium + (premium * gst) + currentServiceLiability
						+ pastServiceLiability + futureServiceLiability);
				newValuationMatrixEntity.setAmountReceived(depositBalanceAmount);
				newValuationMatrixEntity.setBalanceToBePaid(
						newValuationMatrixEntity.getAmountPayable() - newValuationMatrixEntity.getAmountReceived());
				newValuationMatrixEntity.setTotalSumAssured(totalSumAssured);

				RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
						.save(newValuationMatrixEntity);

				valuationMatrixDto = modelMapper.map(renewalValuationMatrixTMPEntity, ValuationMatrixDto.class);

//				renewalPolicyTMPEntity.setIsActive(false);
//				renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
				return ApiResponseDto.success(valuationMatrixDto);
			}
		}

		return null;
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

	@Override
	public ApiResponseDto<List<AOMSearchDto>> otherCriteiraQuotationSearch(AOMSearchDto aomSearchDto, String type) {

		Long quotationTaggedStatusId = 78L;// In progress
		Long quotationTaggedStatusId2 = 79L;// Existing
		Long quotationStatusIdDraft = 16L;
		Long quotationStatusIdApprovalPending = 17L;
		Long quotationStatusIdApproved = 20L;
		Long quotationStatusIdRejected = 18L;
		Long quotationStatusIdExpired = 19L;
		List<Predicate> predicates = new ArrayList<>();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AOMSearchEntity> createQuery = criteriaBuilder.createQuery(AOMSearchEntity.class);

		Root<AOMSearchEntity> root = createQuery.from(AOMSearchEntity.class);

		Join<AOMSearchEntity, TempMphSearchEntity> join = root.join("policyMPHTmp");
		// Join<AOMSearchEntity, PolicyServiceSearchEntity> join2 =
		// root.join("policyTmp");

		// predicates.add(criteriaBuilder.equal(join2.get("serviceType"),
		// "MEMBER_ADDITION"));
		// predicates.add(criteriaBuilder.equal(join2.get("isActive"), 1));

		if (aomSearchDto.getCustomerName() != null) {
			predicates.add(criteriaBuilder.equal(root.get("customerName"), aomSearchDto.getCustomerName()));
		}

		if (aomSearchDto.getPan() != null) {
			predicates.add(criteriaBuilder.equal(join.get("pan"), aomSearchDto.getPan()));
		}
		if (aomSearchDto.getCustomerCode() != null) {
			predicates.add(criteriaBuilder.equal(root.get("customerCode"), aomSearchDto.getCustomerCode()));
		}
		if (aomSearchDto.getPolicyServiceId() != null) {
			predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), aomSearchDto.getPolicyStatusId()));
		}

		if (type.equals("INPROGRESS")) {
			predicates.add(criteriaBuilder.equal(root.get("quotationTaggedStatusId"), quotationTaggedStatusId));
			predicates.add(root.get("quotationStatusId").in(quotationStatusIdDraft, quotationStatusIdApprovalPending));

		} else {
			predicates.add(criteriaBuilder.equal(root.get("quotationTaggedStatusId"), quotationTaggedStatusId2));
			predicates.add(root.get("quotationStatusId").in(quotationStatusIdApproved, quotationStatusIdRejected,
					quotationStatusIdExpired));
		}
		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<AOMSearchEntity> entites = entityManager.createQuery(createQuery).getResultList();
		String unitCode = null;

		List<AOMSearchEntity> policyEnities = new ArrayList<>();

		for (AOMSearchEntity policyDetail : entites) {
			unitCode = policyDetail.getUnitCode();
			String unitOf = this.getUnitOf(unitCode);
			JsonNode productCodeJsonNode = commonModuleService.getProductCodeJsonNode(policyDetail.getProductId());
			String textValue = productCodeJsonNode.path("productName").textValue();
			String variantCode = commonModuleService.getVariantCode(policyDetail.getProductVariantId());

			policyDetail.setProductName(textValue);
			policyDetail.setProductVariant(variantCode);
			policyDetail.setUnitOffice(unitOf);
			policyEnities.add(policyDetail);
		}

		List<AOMSearchDto> collect = policyEnities.stream().map(MidLeaverHelper::entityToDto)
				.collect(Collectors.toList());
		for (AOMSearchDto dto : collect) {

			dto.setTmpPolicyId(dto.getId());
		}

		return ApiResponseDto.success(collect);
	}

	@Override
	public ApiResponseDto<List<AOMSearchDto>> otherCriteiraPolicySearch(AOMSearchDto aomSearchDto, String type) {

		Long policyTaggedStatusId = 138L; // In progress
		Long policyTaggedStatusId2 = 139L; // Existing
		Long policyStatusIdDraft = 513L;
		Long policyStatusIdSendBacktoMaker = 505L;
		Long policyStatusIdApproved = 507L;
		Long policyStatusIdRejected = 506L;
		Long policyStatusIdPendingforApproval = 504L;

		List<Predicate> predicates = new ArrayList<>();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<AOMSearchEntity> createQuery = criteriaBuilder.createQuery(AOMSearchEntity.class);

		Root<AOMSearchEntity> root = createQuery.from(AOMSearchEntity.class);

		Join<AOMSearchEntity, TempMphSearchEntity> join = root.join("policyMPHTmp");
		Join<AOMSearchEntity, PolicyServiceSearchEntity> join2 = root.join("policyTmp");

		predicates.add(criteriaBuilder.equal(join2.get("serviceType"), "MEMBER_ADDITION"));
		// predicates.add(criteriaBuilder.equal(join2.get("isActive"), 1));

		if (aomSearchDto.getCustomerName() != null) {
			predicates.add(criteriaBuilder.equal(root.get("customerName"), aomSearchDto.getCustomerName()));
		}

		if (aomSearchDto.getPan() != null) {
			predicates.add(criteriaBuilder.equal(join.get("pan"), aomSearchDto.getPan()));
		}
		if (aomSearchDto.getCustomerCode() != null) {
			predicates.add(criteriaBuilder.equal(root.get("customerCode"), aomSearchDto.getCustomerCode()));
		}
		if (aomSearchDto.getPolicyServiceId() != null) {
			predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), aomSearchDto.getPolicyStatusId()));
		}
		if (aomSearchDto.getPolicyServiceId() != null) {
			predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), aomSearchDto.getPolicyStatusId()));
		}

		if (type.equals("INPROGRESS")) {

			predicates.add(criteriaBuilder.equal(root.get("policytaggedStatusId"), policyTaggedStatusId));

		} else if (type.equals("EXISTING")) {

			predicates.add(criteriaBuilder.equal(root.get("policytaggedStatusId"), policyTaggedStatusId2));

		} else if (type.equals("NEW")) {

			predicates.add(criteriaBuilder.equal(root.get("policytaggedStatusId"), policyTaggedStatusId));
		}
		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<AOMSearchEntity> entites = entityManager.createQuery(createQuery).getResultList();

		String unitCode = null;
		List<AOMSearchEntity> policyEnities = new ArrayList<>();

		for (AOMSearchEntity policyDetail : entites) {
			if (policyDetail.getPolicyStatusId() == null) {
				unitCode = policyDetail.getUnitCode();
				String unitOf = this.getUnitOf(unitCode);
				JsonNode productCodeJsonNode = commonModuleService.getProductCodeJsonNode(policyDetail.getProductId());
				String textValue = productCodeJsonNode.path("productName").textValue();
				String variantCode = commonModuleService.getVariantCode(policyDetail.getProductVariantId());
				policyDetail.setProductName(textValue);
				policyDetail.setProductVariant(variantCode);
				policyDetail.setUnitOffice(unitOf);
				policyEnities.add(policyDetail);
			} else if (policyDetail.getPolicyStatusId().equals(policyStatusIdApproved)
					|| policyDetail.getPolicyStatusId().equals(policyStatusIdRejected)) {
				unitCode = policyDetail.getUnitCode();
				String unitOf = this.getUnitOf(unitCode);
				JsonNode productCodeJsonNode = commonModuleService.getProductCodeJsonNode(policyDetail.getProductId());
				String textValue = productCodeJsonNode.path("productName").textValue();
				String variantCode = commonModuleService.getVariantCode(policyDetail.getProductVariantId());
				policyDetail.setProductName(textValue);
				policyDetail.setProductVariant(variantCode);
				policyDetail.setUnitOffice(unitOf);
				policyEnities.add(policyDetail);

			} else if (policyDetail.getPolicyStatusId().equals(policyStatusIdDraft)
					|| policyDetail.getPolicyStatusId().equals(policyStatusIdSendBacktoMaker)
					|| policyDetail.getPolicyStatusId().equals(policyStatusIdPendingforApproval)) {
				unitCode = policyDetail.getUnitCode();
				String unitOf = this.getUnitOf(unitCode);
				JsonNode productCodeJsonNode = commonModuleService.getProductCodeJsonNode(policyDetail.getProductId());
				String textValue = productCodeJsonNode.path("productName").textValue();
				String variantCode = commonModuleService.getVariantCode(policyDetail.getProductVariantId());
				policyDetail.setProductName(textValue);
				policyDetail.setProductVariant(variantCode);
				policyDetail.setUnitOffice(unitOf);
				policyEnities.add(policyDetail);

			}
		}
		List<AOMSearchDto> collect = policyEnities.stream().map(MidLeaverHelper::entityToDto)
				.collect(Collectors.toList());
		for (AOMSearchDto dto : collect) {

			dto.setTmpPolicyId(dto.getId());
		}

		return ApiResponseDto.success(collect);
	}

	@Transactional
	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> makePaymentAdjustmentforAOM(
			PolicyContributionDetailDto policyContributionDetailDto) {

		Long policystatusId = 513L;// draft
		Long policysubstatusId = 133L;// Approval Pending
		Long masterTmpPolicyId = null;
		Double getBalance = null;
		Double adjustementAmount = 0.0;
		String financialYear = null;
		financialYear = DateUtils.getFinancialYear(new Date());
		TaskProcessEntity taskProcessEntity = taskProcessRepository.findByProcessName("MID-JOINERS PROCESSING");
		TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();

		PolicyContributionDetailEntity policyContributionDetailEntity = new ModelMapper()
				.map(policyContributionDetailDto, PolicyContributionDetailEntity.class);
		policyContributionDetailEntity.setTmpPolicyId(policyContributionDetailDto.getTmpPolicyId());
		policyContributionDetailEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		// policyContributionDetailEntity.setCreatedBy("Harsha");
		policyContributionDetailEntity.setMasterQuotationId(null);
		policyContributionDetailEntity.setCreatedDate(new Date());
		policyContributionDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
		policyContributionDetailEntity.setEntryType("MJ");
		policyContributionDetailEntity.setActive(true);
		policyContributionDetailEntity.setFinancialYear(financialYear);
		policyContributionDetailEntity.setAdjustmentForDate(new Date());
		policyContributionDetailEntity = policyContributionDetailRepository.save(policyContributionDetailEntity);

		masterTmpPolicyId = policyContributionDetailEntity.getTmpPolicyId();
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(masterTmpPolicyId).get();

		renewalPolicyTMPEntity.setPolicyStatusId(policystatusId);
		renewalPolicyTMPEntity.setPolicySubStatusId(policysubstatusId);

		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		standardCodeRepository.findById(standardcode).get();
		PolicyContributionDetailEntity getGstSC = policyContributionDetailRepository
				.findBymasterTmpPolicyId(masterTmpPolicyId);

		adjustementAmount = getGstSC.getGst() + getGstSC.getLifePremium();
		List<ShowDepositLockDto> showDepositLockDto = new ArrayList<>();
		List<Date> collectionDate = new ArrayList<>();

		for (DepositAdjustementDto getDepositAdjustmentDto : policyContributionDetailDto.getDepositAdjustmentDto()) {

			if (adjustementAmount != null) {
				// masterTmpPolicyId = getDepositAdjustmentDto.getTmpPolicyId();

				getDepositAdjustmentDto.setDepositAmount(getDepositAdjustmentDto.getBalanceAmount());
				PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

				new PolicyContributionEntity();

				if (adjustementAmount < getDepositAdjustmentDto.getDepositAmount()) {
					getBalance = getDepositAdjustmentDto.getDepositAmount() - adjustementAmount;
					policyDepositEntity.setAvailableAmount(getBalance);
					policyDepositEntity.setAdjustmentAmount(adjustementAmount);
					adjustementAmount = 0.0;
				} else {
					getBalance = adjustementAmount - getDepositAdjustmentDto.getDepositAmount();
					policyDepositEntity.setAdjustmentAmount(getDepositAdjustmentDto.getDepositAmount());
					policyDepositEntity.setAvailableAmount(0.0);
					adjustementAmount = adjustementAmount - getDepositAdjustmentDto.getDepositAmount();
				}

				policyDepositEntity.setChequeRealistionDate(getDepositAdjustmentDto.getChequeRealisationDate());
				policyDepositEntity.setActive(true);
				Random r = new Random(System.currentTimeMillis());
				r.nextInt(2);
				r.nextInt(10000);
				policyDepositEntity.setChallanNo(policyContributionDetailEntity.getChallanNo());
				policyDepositEntity.setChequeRealistionDate(new Date());
				policyDepositEntity.setCollectionDate(new Date());
				collectionDate.add(policyDepositEntity.getCollectionDate());
				policyDepositEntity.setZeroId(null);
				policyDepositEntity.setTransactionMode(getDepositAdjustmentDto.getCollectionMode());
				policyDepositEntity.setStatus(null);
				policyDepositEntity.setRegConId(null);
				policyDepositEntity.setTmpPolicyId(masterTmpPolicyId);
				policyDepositEntity.setContributionType("MJ");
				policyDepositEntity.setContributionDetailId(policyContributionDetailEntity.getId());
				policyDepositEntity.setAdjConId(null);
				policyDepositEntity.setDepositAmount(getDepositAdjustmentDto.getDepositAmount());
				policyDepositEntity.setCreatedBy(getDepositAdjustmentDto.getCreatedBy());
				policyDepositEntity.setCreateDate(new Date());
				policyDepositEntity.setIsDeposit(true);
				policyDepositEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
				policyDepositEntity.setRemark(getDepositAdjustmentDto.getRemarks());
				policyDepositEntity.setCollectionNo(getDepositAdjustmentDto.getCollectionNumber());
				policyDepositEntity.setCollectionStatus(getDepositAdjustmentDto.getCollectionStatus());
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

			}
		}
		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository
				.findBymasterPolicyIdandTypeMJ(masterTmpPolicyId);

		List<PolicyContributionEntity> policyContributionEntity = new ArrayList<PolicyContributionEntity>();
		List<PolicyAdjustmentDetailEntity> policyAdjustmentDetail = new ArrayList<PolicyAdjustmentDetailEntity>();
		Double depositAmount = 0.0;
		Long depositId = 0l;
		Double currentAmount = 0.0;

		Long premiumAdjustmentLong = Math.round(policyContributionDetailEntity.getLifePremium());
		Double premiumAdjustment = premiumAdjustmentLong.doubleValue();

		Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();

		adjustementAmount = premiumAdjustment + gstOnPremiumAdjusted;

		policyContributionDetailDto
				.setDepositAdjustmentDto(sort(policyContributionDetailDto.getDepositAdjustmentDto()));

		Long versionNumber = policyContributionRepository.getMaxVersion(financialYear,
				policyContributionDetailEntity.getId());
		versionNumber = versionNumber == null ? 01 : versionNumber + 1;

		for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {
			if (adjustementAmount > 0) {
				Double currentAmountforContribution = 0.0;
				depositAmount = policyDepositEntity.getAdjustmentAmount();
				depositId = policyDepositEntity.getId();
				// LIFE PREMIUM
				if (premiumAdjustment > 0 && depositAmount > 0) {
					if (premiumAdjustment >= depositAmount) {
						currentAmount = depositAmount;

						premiumAdjustment = premiumAdjustment - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
								"LifePremium", "MJ", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = 0.0;
					} else {
						currentAmount = premiumAdjustment;

						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
								"LifePremium", "MJ", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - premiumAdjustment;
						premiumAdjustment = 0.0;
					}
				}

				// GST AMOUNT

				if (premiumAdjustment == 0 && gstOnPremiumAdjusted > 0 && depositAmount > 0) {
					if (gstOnPremiumAdjusted >= depositAmount) {
						currentAmount = depositAmount;

						gstOnPremiumAdjusted = gstOnPremiumAdjusted - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst",
								"MJ", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = 0.0;
					} else {
						currentAmount = gstOnPremiumAdjusted;

						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst",
								"MJ", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - gstOnPremiumAdjusted;
						gstOnPremiumAdjusted = 0.0;
					}
				}
				/*
				 * policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
				 * policyDepositEntity.getContributionDetailId(), "MJ",
				 * currentAmountforContribution, financialYear, versionNumber,
				 * commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId
				 * ()), policyDepositEntity.getChequeRealistionDate(),
				 * policyContributionDetailDto.getCreatedBy(),
				 * policyDepositEntity.getCollectionDate(), policyDepositEntity));
				 */

			}
		}

		// policyContributionRepository.saveAll(policyContributionEntity);
		policyAdjustmentDetailRepository.saveAll(policyAdjustmentDetail);

		if (isDevEnvironmentForRenewals == false) {
			accountingService.lockDeposits(showDepositLockDto, policyContributionDetailDto.getCreatedBy());
		}

		Date risk = Collections.max(collectionDate);
		renewalPolicyTMPEntity.setRiskCommencementDate(risk);
		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		taskAllocationEntity.setTaskStatus(defaultStatusId);
		taskAllocationEntity.setRequestId(renewalPolicyTMPEntity.getId().toString());
		taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
		// taskAllocationEntity.setLocationType(policyContributionDetailDto.getUnitCode());
		taskAllocationEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		taskAllocationEntity.setCreatedDate(new Date());
		taskAllocationEntity.setModulePrimaryId(renewalPolicyTMPEntity.getId());
		taskAllocationEntity.setIsActive(true);
		taskAllocationRepository.save(taskAllocationEntity);

		PolicyTmpAOMProps policyTmpAOMProps = policyTmpAOMPropsRepository.findByTmpPolicyId(masterTmpPolicyId);
		policyTmpAOMProps.setPstgContributionDetailId(policyContributionDetailEntity.getId());
		policyTmpAOMPropsRepository.save(policyTmpAOMProps);

		return ApiResponseDto.success(RenewalPolicyTMPHelper.entityToDto(renewalPolicyTMPEntity));
	}

	public static List<DepositAdjustementDto> sort(List<DepositAdjustementDto> list) {

		list.sort((o1, o2) -> o1.getVoucherEffectiveDate().compareTo(o2.getVoucherEffectiveDate()));

		return list;
	}

	// Save Adjustment Details
	private PolicyAdjustmentDetailEntity saveAdjustmentdata(Long contributionDetailId, Long depositId,
			Double currentAmount, String type, String subType, String entryType, Date date, String userName) {

		PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity = new PolicyAdjustmentDetailEntity();

		policyAdjustmentDetailEntity.setContributionDetailId(contributionDetailId);
		policyAdjustmentDetailEntity.setDepositId(depositId);
		policyAdjustmentDetailEntity.setAdjustedAmount(currentAmount);
		policyAdjustmentDetailEntity.setAdjustedFor(type);
		policyAdjustmentDetailEntity.setSubType(subType);
		policyAdjustmentDetailEntity.setEntryType(entryType);
		policyAdjustmentDetailEntity.setDepositEffectiveDate(date);
		policyAdjustmentDetailEntity.setIsActive(true);
		policyAdjustmentDetailEntity.setCreatedBy(userName);
		policyAdjustmentDetailEntity.setCreatedDate(new Date());

		return policyAdjustmentDetailEntity;

	}

	// Save Contribution Detail
	private PolicyContributionEntity saveContributionDetail(Long policyId, Long contributionDetailId,
			String contributionType, Double currentAmount, String financialYear, Long maxVersion, String variantCode,
			Date effectiveDate, String userName, Date date, PolicyDepositEntity policyDepositEntity) {
		PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();

		policyContributionEntity.setAdjConid(null);
		policyContributionEntity.setOpeningBalance(0.0);
		policyContributionEntity.setTotalContribution(currentAmount);
		policyContributionEntity.setClosingBlance(null);
//			policyContributionEntity.setClosingBlance(
//					policyContributionEntity.getOpeningBalance() + policyContributionEntity.getTotalContribution());
		policyContributionEntity.setContReferenceNo(Long.toString(policyDepositEntity.getCollectionNo()));
//			policyContributionEntity.setContributionDate(policyDepositEntity.getCollectionDate());
		policyContributionEntity.setContributionDate(date);
		policyContributionEntity.setCreatedBy(userName);
		policyContributionEntity.setCreateDate(new Date());
		policyContributionEntity.setEmployeeContribution(0.0);
		policyContributionEntity.setEmployerContribution(currentAmount);

		policyContributionEntity.setFinancialYear(financialYear);

		policyContributionEntity.setActive(true);
		policyContributionEntity.setIsDeposit(true);
		policyContributionEntity.setOpeningBalance(0.0);
		if (contributionType.toLowerCase().equals("mj")) {
			policyContributionEntity.setTmpPolicyId(policyId);
		} else {
			policyContributionEntity.setPolicyId(policyId);
		}
		policyContributionEntity.setRegConId(null);
		policyContributionEntity.setTxnEntryStatus(0l);
		policyContributionEntity.setContributionType(contributionType);
		policyContributionEntity.setVersionNo(maxVersion);
		policyContributionEntity.setEffectiveDate(effectiveDate);
		policyContributionEntity.setContributionDetailId(contributionDetailId);
		/*
		 * if (variantCode != "V2") {
		 * policyContributionEntity.setQuarter(HttpConstants.newBusinessQuarter); } else
		 * {
		 * policyContributionEntity.setQuarter(DateUtils.getFinancialQuarterIdentifier(
		 * effectiveDate)); }
		 */
		// for now default Q0

		return policyContributionEntity;
	}

	@Override
	public String generateMemberPDF(Long tmpPolicyId) {

		PolicyTmpSearchEntity policyTmpSearchEntity = policyTmpSearchRepository.findById(tmpPolicyId).get();
		String errorMessage = "";
		try {
			if (policyTmpSearchEntity != null) {
				List<TempMemberSearchEntity> policyMemberEntityList = tempMemberSearchRepository
						.findByTmpPolicyId(tmpPolicyId);

				StringBuffer sb = new StringBuffer();
				sb.append("<p>Following members are added to policy number: " + policyTmpSearchEntity.getPolicyNumber()
						+ "</p>");
				sb.append("<table><tr><td>Employee Code</td><td>Name</td><td>Date of Appointment</td></tr>");
				for (TempMemberSearchEntity tempMemberSearchEntity : policyMemberEntityList) {
					sb.append("<tr><td>" + tempMemberSearchEntity.getEmployeeCode() + "</td>");
					sb.append("<td>" + tempMemberSearchEntity.getFirstName() + "</td>");
					sb.append("<td>" + tempMemberSearchEntity.getDateOfAppointment() + "</td></tr>");
				}
				sb.append("</table>");

				String fileName = "/MemberData";
				String completehtmlContent = "<!DOCTYPE html>\n" + "<html>" + "<head>" + "<title>Page Title</title>"
						+ "</head>" + "<body>" + sb.toString() + "" + "</body>" + "</html>";

				String completehtmlContent2 = completehtmlContent.replace("\\", "").replaceAll("\t", "");
				String completehtmlContent3 = completehtmlContent2.replaceAll("&", "&amp;");
				String completehtmlContent4 = completehtmlContent3.replaceAll("&amp;nbsp;", "");

				String htmlFileLoc = fileLocation + fileName + "_" + tmpPolicyId + ".html";

				FileWriter fw = new FileWriter(htmlFileLoc);
				fw.write(completehtmlContent4);
				fw.close();

				FileOutputStream fileOutputStream = new FileOutputStream(
						fileLocation + fileName + "_" + tmpPolicyId + ".pdf");

				ITextRenderer renderer = new ITextRenderer();
				renderer.setDocument(new File(fileLocation + fileName + "_" + tmpPolicyId + ".html"));
				renderer.layout();
				renderer.createPDF(fileOutputStream, false);
				renderer.finishPDF();
				if (new File(htmlFileLoc).exists()) {

					new File(htmlFileLoc).delete();
				}
				File file = new File(fileLocation + fileName + "_" + tmpPolicyId + ".pdf");
				byte[] fileContent = Files.readAllBytes(file.toPath());
				String pdfBase64 = Base64.getEncoder().encodeToString(fileContent);
				return pdfBase64;
			}
		} catch (Exception e) {
			errorMessage = "Not Able to generate PDF";
			e.printStackTrace();
		}
		return errorMessage;
	}

	@Override
	public ApiResponseDto<GetOverviewDto> getOverView(Long tmpPolicyId) {

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findByTmpPolicyId(tmpPolicyId);

		Map<Long, String> map = new HashMap<>();
		map.put(16L, "Draft");
		map.put(17L, "Approval Pending");
		map.put(18L, "Rejected");
		map.put(19L, "Expired");
		map.put(20L, "Approved");

		String string = map.get(renewalPolicyTMPEntity.getQuotationStatusId());
		GetOverviewDto getOverviewDto = new GetOverviewDto();
		getOverviewDto.setQuotationNumber(renewalPolicyTMPEntity.getQuotationNumber());
		getOverviewDto.setQuotationDate(renewalPolicyTMPEntity.getCreatedDate());
		getOverviewDto.setQuotationStatus(string);
		return ApiResponseDto.success(getOverviewDto);
	}

	private String getUnitOf(String code) {

		CommonMasterUnitEntity commonMasterUnitEntity = commonMasterUnitRepository.findByCode(code);
		String description = commonMasterUnitEntity.getDescription();

		return description;

	}

	@Override
	public ApiResponseDto<String> sendMPHLetterInEmail(Long tmpPolicyId) {

		String pdfBase64 = generateMemberPDF(tmpPolicyId);
		String emailResponse = "";
		if (!pdfBase64.equalsIgnoreCase("Not Able to generate PDF")) {
			emailResponse = sendEMailToMph(pdfBase64, "/MemeberData", tmpPolicyId);
			if (!emailResponse.equalsIgnoreCase("Not able to email to MPH")) {
				return ApiResponseDto.success(null);
			} else {
				return ApiResponseDto.errorMessage(null, null, "PDF generated but failed to send email");
			}
		} else {
			return ApiResponseDto.errorMessage(null, null, "Unable to generate and email PDF");
		}
	}

	@Override
	public String sendEMailToMph(String pdfBase64, String fileName, Long policyId) {
		String message = "";
		try {
			PolicyTmpSearchEntity policyTmpSearchEntity = policyTmpSearchRepository.findById(policyId).get();
			TempMPHEntity tempMPHEntity = tempMPHRepository.findByTempPolicyId(policyTmpSearchEntity.getId());
			EmailMessagesEntity emailMessagesEntity = emailMessagesRepository.findById(3L).get();
			EmailRequest emailRequest = new EmailRequest();
			emailRequest.setTo(tempMPHEntity.getEmailId());
			emailRequest.setSubject(emailMessagesEntity.getSubject());
			emailRequest.setEmailBody(emailMessagesEntity.getContent());
			emailRequest.setBcc("");
			List<Map<String, Object>> pdfMapList = new ArrayList<Map<String, Object>>();
			Map<String, Object> pdfMap = new HashMap<String, Object>();
			pdfMap.put("fileName", fileName + "_" + policyId);
			pdfMap.put("fileData", pdfBase64);
			pdfMap.put("fileType", "pdf");
			pdfMapList.add(pdfMap);
			emailRequest.setPdfFile(pdfMapList);

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<EmailRequest> entity = new HttpEntity<EmailRequest>(emailRequest, headers);
			restTemplate.postForObject("https://email-api-uat.apps.nposepgs.licindia.com/api/email", entity, Map.class);
			message = "Email Triggered";
		} catch (Exception e) {
			message = "Not able to email to MPH";
			e.printStackTrace();
		}
		return message;
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> getInMakerBucket(Long masterPolicyId, Long tmpPolicyId) {
		Long mpid = masterPolicyId;

		if (mpid == 0)
			mpid = renewalPolicyTMPRepository.findById(tmpPolicyId).get().getMasterPolicyId();

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findInMakerBucket(mpid);

		PolicyContributionDetailEntity contributionDetailEntity = policyContributionDetailRepository
				.findBymasterTmpPolicyId(tmpPolicyId);

		if (contributionDetailEntity == null)
			return ApiResponseDto.success(null);
		else
			return ApiResponseDto.success(RenewalValuationTMPHelper.entityToDtoTemPolicy(renewalPolicyTMPEntity));
	}

	@Override
	public ApiResponseDto<Map<String, String>> getPremiumRate(Long tmpPolicyId) {

		Map<String, String> map = new HashMap<>();
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyId).get();
		List<PolicyServiceEntitiy> renewedPolicies = policyServiceRepository
				.findByPolicyId(renewalPolicyTMPEntity.getMasterPolicyId());
		RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = renewalValuationBasicTMPRepository
				.getByTmpPolicyId(tmpPolicyId);
		if (renewedPolicies.size() > 0) {
			map.put("AOCM", renewalValuationBasicTMPEntity.getModfdPremRateCrdbiltyFctr().toString());
			return ApiResponseDto.success(map);
		} else {
			map.put("RATETABLE", renewalValuationBasicTMPEntity.getRateTable());
			return ApiResponseDto.success(map);
		}
	}

	@Override
	public ApiResponseDto<List<PolicyDepositDto>> getAdjustedDeposit(Long tmpPolicyId) {

		List<PolicyDepositEntity> policyDepositEntites = policyDepositRepository.findBytmpPolicyId(tmpPolicyId);

		return ApiResponseDto
				.success(policyDepositEntites.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));
	}

	// AOM Premium Update
	@Transactional
	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> updatePaymentAdjustmentforAom(
			PolicyContributionDetailDto policyContributionDetailDto) {

		Long masterTmpPolicyId = null;
		Double getBalance = null;
		String financialYear = null;

		financialYear = DateUtils.getFinancialYear(new Date());
		Double adjustementAmount = 0.0;
		masterTmpPolicyId = policyContributionDetailDto.getTmpPolicyId();
		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository.findBytmpPolicyId(masterTmpPolicyId);

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(masterTmpPolicyId).get();

		if (isDevEnvironment == false) {
			List<UnlockDepositDetailDto> showDepositLockDto = new ArrayList<UnlockDepositDetailDto>();

			for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {

				UnlockDepositDetailDto depositLockDto = new UnlockDepositDetailDto();
				depositLockDto.setChallanNo(Integer.parseInt(policyDepositEntity.getChallanNo()));
				depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				depositLockDto.setUserCode(policyContributionDetailDto.getCreatedBy());
				depositLockDto
						.setProductCode(commonModuleService.getProductCode(renewalPolicyTMPEntity.getProductId()));
				depositLockDto.setVariantCode(
						commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()));
				depositLockDto.setVariantCode("V3");
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
		policyContributionDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
		policyContributionDetailEntity.setLifePremium(policyContributionDetailDto.getLifePremium());
		policyContributionDetailEntity.setPastService(policyContributionDetailDto.getPastService());
		policyContributionDetailEntity.setGst(policyContributionDetailDto.getGst());
		policyContributionDetailEntity.setCurrentServices(policyContributionDetailDto.getCurrentServices());
		policyContributionDetailEntity.setModifiedBy(policyContributionDetailDto.getModifiedBy());
		policyContributionDetailEntity.setModifiedDate(new Date());
		policyContributionDetailEntity.setEntryType("MJ");
		policyContributionDetailEntity.setFinancialYear(financialYear);
		policyContributionDetailEntity.setAdjustmentForDate(new Date());
		policyContributionDetailEntity = policyContributionDetailRepository.save(policyContributionDetailEntity);

		policyAdjustmentDetailRepository.deleteBycontributionDetailId(policyContributionDetailEntity.getId());
		policyDepositRepository.deleteBytmpPolicyId(masterTmpPolicyId);
		policyContributionRepository.deleteBytmpPolicyId(masterTmpPolicyId);
		policyContrySummaryRepository.deleteBytmpPolicyId(masterTmpPolicyId);

		standardCodeRepository.findById(standardcode).get();
		PolicyContributionDetailEntity getGstSC = policyContributionDetailRepository
				.findBymasterTmpPolicyId(masterTmpPolicyId);

		adjustementAmount = getGstSC.getGst() + getGstSC.getLifePremium();

		// RenewalValuationMatrixTMPEntity getRenewalValuationMatrixTMPEntity =
		// renewalValuationMatrixTMPRepository
		// .findBytmpPolicyId(masterTmpPolicyId).get();

		policyContributionDetailEntity = policyContributionDetailRepository.save(policyContributionDetailEntity);

		List<ShowDepositLockDto> showDepositLockDto = new ArrayList<ShowDepositLockDto>();
		for (DepositAdjustementDto getDepositAdjustementDto : policyContributionDetailDto.getDepositAdjustmentDto()) {
			if (adjustementAmount != 0) {
				// masterTmpPolicyId = getDepositAdjustementDto.getTmpPolicyId();

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
				Random r = new Random(System.currentTimeMillis());
				int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
				policyDepositEntity.setChallanNo(String.valueOf(getCollectionNumber));
				policyDepositEntity.setCollectionDate(new Date());
				policyDepositEntity.setCreateDate(new Date());
				policyDepositEntity.setZeroId(null);
				policyDepositEntity.setTransactionMode(getDepositAdjustementDto.getCollectionMode());
				policyDepositEntity.setStatus(null);
				policyDepositEntity.setRegConId(null);
				policyDepositEntity.setTmpPolicyId(masterTmpPolicyId);
				policyDepositEntity.setContributionType("MJ");
				policyDepositEntity.setAdjConId(null);
				policyDepositEntity.setDepositAmount(getDepositAdjustementDto.getDepositAmount());
				policyDepositEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
				policyDepositEntity.setIsDeposit(true);
				policyDepositEntity.setActive(true);
				policyDepositEntity.setRemark(getDepositAdjustementDto.getRemarks());
				policyDepositEntity.setCollectionNo(getDepositAdjustementDto.getCollectionNumber());
				policyDepositEntity.setCollectionStatus(getDepositAdjustementDto.getCollectionStatus());
				policyDepositEntity.setContributionDetailId(policyContributionDetailEntity.getId());
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
			}
		}

		List<PolicyContributionEntity> policyContributionEntity = new ArrayList<PolicyContributionEntity>();
		List<PolicyDepositEntity> policyDepositEntitieList = policyDepositRepository
				.findBymasterPolicyIdandTypeMJ(masterTmpPolicyId);

		List<PolicyAdjustmentDetailEntity> policyAdjustmentDetail = new ArrayList<PolicyAdjustmentDetailEntity>();
		Double depositAmount = 0.0;
		Long depositId = 0l;
		Double currentAmount = 0.0;

		Long premiumAdjustmentLong = Math.round(policyContributionDetailEntity.getLifePremium());
		Double premiumAdjustment = premiumAdjustmentLong.doubleValue();

		Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();

		adjustementAmount = premiumAdjustment + gstOnPremiumAdjusted;
		policyContributionDetailDto
				.setDepositAdjustmentDto(sort(policyContributionDetailDto.getDepositAdjustmentDto()));
		Long versionNumber = policyContributionRepository.getMaxVersion(financialYear,
				policyContributionDetailEntity.getId());
		versionNumber = versionNumber == null ? 01 : versionNumber + 1;
		for (PolicyDepositEntity policyDepositEntity : policyDepositEntitieList) {
			if (adjustementAmount > 0) {
				Double currentAmountforContribution = 0.0;
				depositAmount = policyDepositEntity.getAdjustmentAmount();
				depositId = policyDepositEntity.getId();
				// LIFE PREMIUM
				if (premiumAdjustment > 0 && depositAmount > 0) {
					if (premiumAdjustment >= depositAmount) {
						currentAmount = depositAmount;

						premiumAdjustment = premiumAdjustment - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
								"LifePremium", "MJ", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = 0.0;
					} else {
						currentAmount = premiumAdjustment;

						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
								"LifePremium", "MJ", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - premiumAdjustment;
						premiumAdjustment = 0.0;
					}
				}

				// GST AMOUNT

				if (premiumAdjustment == 0 && gstOnPremiumAdjusted > 0 && depositAmount > 0) {
					if (gstOnPremiumAdjusted >= depositAmount) {
						currentAmount = depositAmount;

						gstOnPremiumAdjusted = gstOnPremiumAdjusted - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst",
								"MJ", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = 0.0;
					} else {
						currentAmount = gstOnPremiumAdjusted;

						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst",
								"MJ", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - gstOnPremiumAdjusted;
						gstOnPremiumAdjusted = 0.0;
					}
				}

				/*
				 * policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
				 * policyDepositEntity.getContributionDetailId(), "MJ",
				 * currentAmountforContribution, financialYear, versionNumber,
				 * commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId
				 * ()), policyDepositEntity.getChequeRealistionDate(),
				 * policyContributionDetailDto.getCreatedBy(),
				 * policyDepositEntity.getCollectionDate(), policyDepositEntity)); versionNumber
				 * = versionNumber + 1;
				 */
			}
		}
		// policyContributionRepository.saveAll(policyContributionEntity);
		policyAdjustmentDetailRepository.saveAll(policyAdjustmentDetail);
		if (isDevEnvironmentForRenewals == false) {
			accountingService.lockDeposits(showDepositLockDto, policyContributionDetailDto.getCreatedBy());
		}

		return ApiResponseDto.success(RenewalPolicyTMPHelper.entityToDto(renewalPolicyTMPEntity));

	}

	private void copyTmpToMaster(Long tmpPolicyId, String username) {

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyId).get();
		Long masterPolicyId = renewalPolicyTMPEntity.getMasterPolicyId();
		MasterPolicyEntity newmasterPolicyEntity = null;

		newmasterPolicyEntity = PolicyHelper.updateTemptoPolicyMaster(renewalPolicyTMPEntity, username);
		System.out.println(newmasterPolicyEntity.getId());
		System.out.println(masterPolicyId);

		// Copy to MasterPolicycontributionDetails
		MasterPolicyContributionDetails copyToMaster = PolicyHelper.entityToMaster(
				policyContributionDetailRepository.findBymasterPolicyIdandTypeMJ(renewalPolicyTMPEntity.getId()), null,
				masterPolicyId);

		copyToMaster = masterPolicyContributionDetailRepository.save(copyToMaster);

		PolicyTmpAOMProps policyTmpAOMProps = policyTmpAOMPropsRepository
				.findByTmpPolicyId(renewalPolicyTMPEntity.getId());

		policyTmpAOMProps.setModifiedBy(username);
		policyTmpAOMProps.setModifiedDate(new Date());
		policyTmpAOMProps.setPmstContributionDetailId(copyToMaster.getId());
		policyTmpAOMProps.setIsActive(true);

		policyTmpAOMProps = policyTmpAOMPropsRepository.save(policyTmpAOMProps);

		masterPolicyDepositRepository.isactivefalse(newmasterPolicyEntity.getId());
		masterPolicyAdjustmentDetailRepository.isactivefalse(newmasterPolicyEntity.getId());

		new ArrayList<MasterPolicyDepositEntity>();

		// copy Deposit to Master data
		masterPolicyContributionRepository.isactivefalse(newmasterPolicyEntity.getId());
		// copy Deposit to MasterPolicyContributionEntity
		List<MasterPolicyContributionEntity> masterPolicyContributionEntity = PolicyHelper.entityToPolicyConMaster(
				policyContributionRepository.findBytmpPolicyId(renewalPolicyTMPEntity.getId()),
				newmasterPolicyEntity.getId(), copyToMaster, username);
		masterPolicyContributionRepository.saveAll(masterPolicyContributionEntity);

		// copy to masterPolicyDetail
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

			// copy to adjustment
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

				policyMemberRepository.setAdjustmentDateforMasterPolicyId(newmasterPolicyEntity.getId(),
						copyToMaster.getAdjustmentForDate());

			}

		}
	}

	@Override
	public Map<String, Object> uploadDocs(DocumentUploadDto documentUploadDto) throws JsonProcessingException {
		ResponseEntity<String> uploadStatus = this.uploadClaimDocs(documentUploadDto);

		Map<String, Object> respone = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		OmniDocumentResponse omniDocsResponseObj = mapper.readValue(uploadStatus.getBody().toString(),
				OmniDocumentResponse.class);

		if (omniDocsResponseObj.getStatus().equals("true")) {
			EndorsementDocumentEntity endorsementDocumentEntity = new EndorsementDocumentEntity();
			endorsementDocumentEntity.setPolicyId(documentUploadDto.getMasterPolicyId());
			endorsementDocumentEntity.setTmpPolicyId(documentUploadDto.getTmpPolicyId());
			endorsementDocumentEntity.setDocumentTypeId(documentUploadDto.getDocumentTypeId());
			endorsementDocumentEntity.setDocumentIndex(omniDocsResponseObj.getDocumentIndexNo());
			if (omniDocsResponseObj.getDocumentIndexNo() != null) {
				endorsementDocumentEntity.setDocumentName("Y");
			} else {
				endorsementDocumentEntity.setDocumentName("N");
			}
			endorsementDocumentEntity.setDocumentStatus(248L);
			endorsementDocumentEntity.setFolderIndexNo(Long.valueOf(omniDocsResponseObj.getFolderIndexNo()));
			endorsementDocumentEntity.setCreatedBy(documentUploadDto.getUsername());
			endorsementDocumentEntity.setCreatedDate(new Date());
			EndorsementDocumentEntity savedDoc = endorsementDocumentRepository.save(endorsementDocumentEntity);

			respone.put("returnCode", 0);
			respone.put("responseMessage", "Document Uploaded Succesfully");
			respone.put("documentDetail", savedDoc);
			return respone;

		} else {
			respone.put("returnCode", 1);
			respone.put("responseMessage", "Document Upload Failed, Since " + omniDocsResponseObj.getMessage());
			return respone;
		}
	}

	private ResponseEntity<String> uploadClaimDocs(DocumentUploadDto documentUploadDto) throws JsonProcessingException {
		String urlBalance = "https://uat-epgs.licindia.com/omnidocservice/LIC_ePGS/uploadDocwithBase64";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("infra", "epgs");

		ResponseEntity<String> uploadStatus = null;

		OmniDocsRequest omniDocRequest = new OmniDocsRequest();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

		Optional<PickListItemEntity> pickListItem = pickListItemRepository
				.findById(documentUploadDto.getDocumentTypeId());
		omniDocRequest.setDocumentType(pickListItem.get().getName());
		omniDocRequest.setIdentifier("Gratuity Mid Joiner");
		omniDocRequest.setOther("");
		omniDocRequest.setUserName(documentUploadDto.getUsername());
		omniDocRequest.setClaimOnBoardingNo("");
		omniDocRequest.setMasterPolicyNo(documentUploadDto.getMasterPolicyId().toString());

		String jsonBalance = ow.writeValueAsString(omniDocRequest);

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("omniDocRequest", jsonBalance);
		map.add("file", documentUploadDto.getFileBase64());

		logger.info("Json Request" + jsonBalance);

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);

		uploadStatus = restTemplate.exchange(urlBalance, HttpMethod.POST, entity, String.class);

		logger.info("Searching Member  " + uploadStatus.getBody().toString());

		return uploadStatus;

	}

	@Override
	public List<DocumentUploadDto> getDocumentDetails(Long tmpPolicyId) {
		List<EndorsementDocumentEntity> entities = endorsementDocumentRepository.findByTmpPolicyId(tmpPolicyId);
		if (entities.isEmpty()) {
			return Collections.emptyList();
		} else {
			return entities.stream().map(EndorsementDocumentHelper::entityToDocumentUploadDto)
					.collect(Collectors.toList());
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> removeUploadedDocs(Long documentId, String username)
			throws JsonMappingException, JsonProcessingException {
		Map<String, Object> response = new HashMap<String, Object>();

		EndorsementDocumentEntity endorsementDocumentEntity = endorsementDocumentRepository.findById(documentId).get();

		ObjectMapper mapper1 = new ObjectMapper();
		ResponseEntity<String> removeUploadResponse = removeUploadedDoc(
				endorsementDocumentEntity.getFolderIndexNo().toString(),
				endorsementDocumentEntity.getDocumentIndex().toString());
		OmniDocumentResponse omniDocsResponseObj = mapper1.readValue(removeUploadResponse.getBody().toString(),
				OmniDocumentResponse.class);

		if (omniDocsResponseObj.getStatus().equals("true")) {
			endorsementDocumentEntity.setIsActive(false);
			endorsementDocumentEntity.setModifiedBy(username);
			endorsementDocumentEntity.setModifiedDate(new Date());
			endorsementDocumentRepository.save(endorsementDocumentEntity);
			response.put("responseMessage", "Document deleted Successfully");
			response.put("responseCode", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	private ResponseEntity<String> removeUploadedDoc(String folderNo, String documentIndexNo) {

		String urlBalance = "https://D1STVRRPGCA01.licindia.com:8443/omnidocservice/LIC_ePGS/removeDocument/" + folderNo
				+ "/" + documentIndexNo;

		HttpHeaders headers = new HttpHeaders();
		headers.add("infra", "epgs");

		ResponseEntity<String> uploadStatus = null;

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		uploadStatus = restTemplate.exchange(urlBalance, HttpMethod.GET, entity, String.class);

		logger.info("Response From OmniDocs Service  " + uploadStatus.getBody().toString());

		return uploadStatus;

	}

	public Double getDepositBalanceAmount(Long tmpPolicyId, String userName) {

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = null;

		Optional<RenewalPolicyTMPEntity> findById = renewalPolicyTMPRepository.findById(tmpPolicyId);
		{
			renewalPolicyTMPEntity = findById.get();
		}
		String proposalNo = "0";
		String policyNo = renewalPolicyTMPEntity.getPolicyNumber();

		ApiResponseDto<List<DepositDto>> deposits = accountingService.getDeposits(proposalNo, policyNo, userName);
		Double getBalanceAmount = 0.0;

		List<DepositDto> data = deposits.getData();
		for (DepositDto getAvbAmt : data) {
			getBalanceAmount = getBalanceAmount + getAvbAmt.getBalanceAmount();
		}
		return getBalanceAmount;
	}

	@Override
	public ApiResponseDto<String> inActiveMemberInBulk(List<Long> memId) {
		List<RenewalPolicyTMPMemberEntity> members = renewalPolicyTMPMemberRepository.findByMember(memId);

		List<RenewalPolicyTMPMemberEntity> inActiveMem = new ArrayList<RenewalPolicyTMPMemberEntity>();
		for (RenewalPolicyTMPMemberEntity mem : members) {
			mem.setIsActive(false);
			inActiveMem.add(mem);
		}
		renewalPolicyTMPMemberRepository.saveAll(inActiveMem);

		String msg = "Deleted Succesfully";
		return ApiResponseDto.success(msg);
	}
}
