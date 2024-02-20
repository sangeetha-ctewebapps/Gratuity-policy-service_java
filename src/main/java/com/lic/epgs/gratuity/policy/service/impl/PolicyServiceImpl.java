package com.lic.epgs.gratuity.policy.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.accountingservice.dto.GstDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.IssuePolicyDto;
import com.lic.epgs.gratuity.accountingservice.dto.JournalVoucherDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.NewBusinessContriDebitCreditRequestModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.NewBusinessContributionAndLifeCoverAdjustmentDto;
import com.lic.epgs.gratuity.accountingservice.dto.UnlockDepositDetailDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.service.CommonService;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.StagingMPHEntity;
import com.lic.epgs.gratuity.mph.helper.MPHHelper;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.StagingMPHRepository;
import com.lic.epgs.gratuity.policy.dto.AgeValuationReportDto;
import com.lic.epgs.gratuity.policy.dto.ContributionRequestDto;
import com.lic.epgs.gratuity.policy.dto.ContributionResponseDto;
import com.lic.epgs.gratuity.policy.dto.DocumentStoragedto;
import com.lic.epgs.gratuity.policy.dto.PolicyBondDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.dto.PolicyStatementPeriod;
import com.lic.epgs.gratuity.policy.dto.QuotationChargeDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositLockDto;
import com.lic.epgs.gratuity.policy.entity.DocumentStorageEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicySearchEntity;
import com.lic.epgs.gratuity.policy.entity.MphSearchEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.entity.QuotationChargeEntity;
import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.StagingPolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.helper.PolicyGratuityBenefitHelper;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.StagingPolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.helper.DocumentStorageHelp;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.helper.QuotationChargeHelper;
import com.lic.epgs.gratuity.policy.integration.dto.AccountingIntegrationRequestDto;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.integration.dto.SuperAnnuationResponseModel;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.StagingPolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.helper.PolicyLifeCoverHelper;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.lifecover.repository.StagingPolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.helper.PolicyMemberHelper;
import com.lic.epgs.gratuity.policy.member.helper.PolicyMemberWorkbookHelper;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.StagingPolicyMemberRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.dto.PolicyContrySummarydto;
import com.lic.epgs.gratuity.policy.premiumadjustment.dto.PolicyDepositdto;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContrySummaryEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContrySummaryEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyContrySummaryRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyDepositRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContrySummaryRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyDepositRepository;
import com.lic.epgs.gratuity.policy.renewal.entity.PolicyRenewalRemainderEntity;
import com.lic.epgs.gratuity.policy.renewal.repository.PolicyRenewalRemainderRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.helper.RenewalPolicyTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.PolicyServiceRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationMatrixTMPRepository;
import com.lic.epgs.gratuity.policy.repository.DocumentStorageRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyHistoryRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyRepository;
import com.lic.epgs.gratuity.policy.repository.QuotationChargeRepository;
import com.lic.epgs.gratuity.policy.repository.StagingPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.repository.custom.StagingPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.StagingPolicySchemeRule;
import com.lic.epgs.gratuity.policy.schemerule.helper.PolicySchemeRuleHelper;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.schemerule.repository.StagingPolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.service.PolicyService;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyMasterValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.helper.PolicyValuationHelper;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyMasterValuationRepository;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyValuationRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValutationBasicEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.helper.PolicyValuationMatrixHelper;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.StagingPolicyValuationBasicRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.StagingPolicyValuationMatrixRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.StagingValuationWithdrawaleRateRepository;
import com.lic.epgs.gratuity.quotation.entity.MasterQuotationEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.MasterGratuityBenefitRepository;
import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.repository.MasterLifeCoverEntityRepository;
import com.lic.epgs.gratuity.quotation.member.helper.BulkMemberUploadHelper;
import com.lic.epgs.gratuity.quotation.member.helper.MemberErrorWorkbookHelper;
import com.lic.epgs.gratuity.quotation.member.repository.MasterMemberRepository;
import com.lic.epgs.gratuity.quotation.repository.MasterQuotationRepository;
import com.lic.epgs.gratuity.quotation.schemerule.entity.MasterSchemeRuleEntity;
import com.lic.epgs.gratuity.quotation.schemerule.repository.MasterSchemeRuleRepository;
import com.lic.epgs.gratuity.quotation.valuation.entity.MasterValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.repository.MasterValuationRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.MasterValuationBasicRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.MasterValuationMatrixRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.MasterValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.simulation.dto.DepositAdjustementDto;

/**
 * @author Vigneshwaran
 *
 */
@Service
public class PolicyServiceImpl implements PolicyService {

	protected final Logger logger = LogManager.getLogger(getClass());

	private Long standardcode = 10l;

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	@Value("${app.isDevEnvironmentForRenewals}")
	private Boolean isDevEnvironmentForRenewals;

	@Value("${app.policy.sentBackToMakerSubStatudId}")
	private String sentBackToMakerSubStatudId;

	@Value("${app.policy.submittedForApprovalStatudId}")
	private String submittedForApprovalStatudId;

	@Value("${app.policy.defaultStatusId}")
	private String defaultStatusId;

	@Value("${app.policy.defaultSubStatusId}")
	private String defaultSubStatusId;

	@Value("${app.policy.RenewalTaggedStatusId}")
	private String renewalTaggedStatusId;

	@Value("${app.policy.defaultTaggedStatusId}")
	private String defaultTaggedStatusId;

	@Value("${app.policy.approvedStatudId}")
	private String approvedStatudId;

	@Value("${app.policy.approvedSubStatudId}")
	private String approvedSubStatudId;

	@Value("${app.policy.existingTaggedStatusId}")
	private String existingTaggedStatusId;

	@Value("${app.policy.rejectedStatudId}")
	private String rejectedStatudId;

	@Value("${app.policy.rejectedSubStatudId}")
	private String rejectedSubStatudId;

	@Value("${app.commonModuleServiceEndpoint}")
	private String endPoint;

	@Value("${app.SITurl}")
	private String SITurl;

	@Autowired
	@Qualifier(value = "restTemplateService")
	private RestTemplate restTemplateService;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private PolicyServiceRepository policyServiceRepository;
	
	@Autowired
	private AccountingService accountingService;

	@Autowired
	private PolicyRepository policyRepository;

	@Autowired
	private QuotationChargeRepository quotationChargeRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private StagingPolicyRepository stagingPolicyRepository;

	@Autowired
	private MasterPolicyRepository masterPolicyEntityRepository;

	@Autowired
	private MasterQuotationRepository masterQuotationRepository;

	@Autowired
	private MasterMemberRepository masterMemberRepository;

	@Autowired
	private StagingPolicyMemberRepository stagingPolicyMemberRepository;

	@Autowired
	private MasterSchemeRuleRepository masterSchemeRuleRepository;

	@Autowired
	private StagingPolicySchemeRuleRepository stagingPolicySchemeRuleRepository;

	@Autowired
	private MasterLifeCoverEntityRepository masterLifeCoverEntityRepository;

	@Autowired
	private StagingPolicyLifeCoverRepository stagingPolicyLifeCoverRepository;

	@Autowired
	private MasterGratuityBenefitRepository MasterGratuityBenefitRepository;

	@Autowired
	private StagingPolicyGratuityBenefitRepository stagingPolicyGratuityBenefitRepository;

	@Autowired
	private MasterValuationMatrixRepository masterValuationMatrixRepository;

	@Autowired
	private StagingPolicyValuationMatrixRepository stagingPolicyValuationMatrixRepository;

	@Autowired
	private StagingMPHRepository stagingMPHRepository;

	@Autowired
	private PolicySchemeRuleRepository policySchemeRuleRepository;

	@Autowired
	private PolicyLifeCoverRepository policyLifeCoverRepository;

	@Autowired
	private PolicyGratuityBenefitRepository policyGratuityBenefitRepository;

	@Autowired
	private PolicyValuationMatrixRepository policyValuationMatrixRepository;

	@Autowired
	private MPHRepository mPHRepository;

	@Autowired
	private PolicyMemberRepository policyMemberRepository;

	@Autowired
	private BulkMemberUploadHelper memberHelper;

	@Autowired
	private MasterValuationRepository masterValuationRepository;

	@Autowired
	private PolicyValuationRepository policyValuationRepository;

	@Autowired
	private PolicyMasterValuationRepository policyMasterValuationRepository;

	@Autowired
	private MemberCategoryRepository memberCategoryRepository;

	@Autowired
	private MasterValuationBasicRepository masterValuationBasicRepository;

	@Autowired
	private StagingPolicyValuationBasicRepository stagingPolicyValuationBasicRepository;

	@Autowired
	private PolicyValuationBasicRepository policyValuationBasicRepository;

	@Autowired
	private PolicyContributionDetailRepository policyContributionDetailRepository;

	@Autowired
	private PolicyDepositRepository policyDepositRepository;

	@Autowired
	private PolicyContributionRepository policyContributionRepository;

	@Autowired
	private PolicyContrySummaryRepository policyContrySummaryRepository;
	@Autowired
	private StandardCodeRepository standardCodeRepository;

	@Autowired
	private MasterPolicyDepositRepository masterPolicyDepositRepository;

	@Autowired
	private MasterPolicyContributionRepository masterPolicyContributionRepository;

	@Autowired
	private MasterPolicyContrySummaryRepository masterPolicyContrySummaryRepository;

	@Autowired
	private MasterValuationWithdrawalRateRepository masterValuationWithdrawalRateRepository;

	@Autowired
	private StagingValuationWithdrawaleRateRepository stagingValuationWithdrawaleRateRepository;

	@Autowired
	private PolicyValuationWithdrawalRateRepository policyValuationWithdrawalRateRepository;

	@Autowired
	private DocumentStorageRepository documentStorageRepository;

	@Autowired
	private PolicyMemberWorkbookHelper policyMemberWorkbookHelper;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

	@Autowired
	private RenewalValuationMatrixTMPRepository renewalValuationMatrixTMPRepository;
	
	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;
	
	@Autowired
	private CommonModuleService commonModuleService;
	
	@Autowired
	private MasterPolicyContributionDetailRepository masterPolicyContributionDetailRepository; 
	
	@Autowired
	@Qualifier("jsonObjectMapper")
	private ObjectMapper objectMapper;
	
	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;
	
	@Autowired
	private CommonMasterStateRepository commonMasterStateRepository;
	
	@Autowired
	private PolicyHistoryRepository policyHistoryRepository;
	
	@Autowired
	private StagingPolicyCustomRepository stagingPolicyCustomRepository;
	
	@Autowired
	private PolicyRenewalRemainderRepository policyRenewalRemainderRepository;
	
	@Autowired
	private TaskProcessRepository taskProcessRepository;
	
	@Autowired
	private TaskAllocationRepository taskAllocationRepository;
	
	@Override
	public ApiResponseDto<PolicyDto> findById(Long id) {
		Optional<StagingPolicyEntity> policyEntity = stagingPolicyRepository.findById(id);
		if (policyEntity.isPresent()) {
			MasterQuotationEntity masterQuotationEntity = masterQuotationRepository
					.findById(policyEntity.get().getMasterQuotationId()).get();
			PolicyDto policyDto = PolicyHelper.entityToDto(policyEntity.get());
			policyDto.setProposalNumber(masterQuotationEntity.getProposalNumber());
			policyDto.setQuotationNumber(masterQuotationEntity.getNumber());
			return ApiResponseDto.success(policyDto);
		} else {
			return ApiResponseDto.notFound(null);
		}
	}

	@Override
	public ApiResponseDto<PolicyDto> findById(Long Id, String type) {
		if (type.equals("INPROGRESS")) {
			Optional<StagingPolicyEntity> entity = stagingPolicyRepository.findById(Id);
			if (entity.isPresent()) {
				PolicyDto policyDto = PolicyHelper.entityToDto(stagingPolicyCustomRepository.setTransientValues(entity.get()));
				policyDto.setMemberCount(stagingPolicyMemberRepository.totalCount(Id));
				return ApiResponseDto.success(policyDto);
			}
		} else {
			Optional<MasterPolicyEntity> entity = masterPolicyEntityRepository.findById(Id);
			if (entity.isPresent()) {
				PolicyDto policyDto = (PolicyHelper.entityToDto(masterPolicyCustomRepository.setTransientValues(masterPolicyCustomRepository.findById(Id))));
				policyDto.setMemberCount(policyMemberRepository.totalCount(Id));
				return ApiResponseDto.success(policyDto);
			}
		}
		return ApiResponseDto.notFound(new PolicyDto());
	}

	@Override
	public ApiResponseDto<List<PolicyDto>> filter(PolicySearchDto policySearchDto) {
		if (policySearchDto.getTaggedStatusId().equals(138L)) {
			return this.inprogressFilter(policySearchDto);
		} else {
			return this.existingFilter(policySearchDto);
		}
	}

	public ApiResponseDto<List<PolicyDto>> inprogressFilter(PolicySearchDto policySearchDto) {
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StagingPolicyEntity> createQuery = criteriaBuilder.createQuery(StagingPolicyEntity.class);

		Root<StagingPolicyEntity> root = createQuery.from(StagingPolicyEntity.class);

		if (policySearchDto.getTaggedStatusId() > 0) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyTaggedStatusId")),
					policySearchDto.getTaggedStatusId()));
		}
		if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
					policySearchDto.getPolicyNumber().toLowerCase().trim()));
		}
		if (StringUtils.isNotBlank(policySearchDto.getProposalNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("proposalNumber")),
					policySearchDto.getProposalNumber().toLowerCase().trim()));
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("isActive")), 1l));
		}
		if (StringUtils.isNotBlank(policySearchDto.getCustomerName())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("customerName")),
					policySearchDto.getCustomerName().toLowerCase()));
		}
		if (StringUtils.isNotBlank(policySearchDto.getIntermediaryName())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("intermediaryName")),
					policySearchDto.getIntermediaryName().toLowerCase()));
		}
		if (policySearchDto.getLineOfBusinessId() != null && policySearchDto.getLineOfBusinessId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("lineOfBusinessId"), policySearchDto.getLineOfBusinessId()));
		}
		if (policySearchDto.getProductId() != null && policySearchDto.getProductId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("productId"), policySearchDto.getProductId()));
		}
		if (policySearchDto.getProductVariant() != null && policySearchDto.getProductVariant() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("productVariant"), policySearchDto.getProductVariant()));
		}
		if (policySearchDto.getUnitOffice() != null && policySearchDto.getUnitOffice() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("unitOffice"), policySearchDto.getUnitOffice()));
		}
//		if (policySearchDto.getPolicyStatus() != null && policySearchDto.getPolicyStatus() > 0) {
//			predicates.add(criteriaBuilder.equal(root.get("policyStatus"), policySearchDto.getPolicyStatus()));
//		}
		if (policySearchDto.getPolicyStatusId() != null && policySearchDto.getPolicyStatusId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), policySearchDto.getPolicyStatusId()));
		}
		if (policySearchDto.getPolicySubStatusId() != null && policySearchDto.getPolicySubStatusId() > 0) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policySubStatusId")),
					policySearchDto.getPolicySubStatusId()));
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
		List<StagingPolicyEntity> entities = entityManager.createQuery(createQuery).getResultList();
		
		List<PolicyDto> policyDtos = new ArrayList<>();
		for (StagingPolicyEntity stagingPolicyEntity : entities) {
			policyDtos.add(PolicyHelper.entityToDto(stagingPolicyCustomRepository.setTransientValues(stagingPolicyEntity)));
		}

		return ApiResponseDto.success(policyDtos);
	}

	public ApiResponseDto<List<PolicyDto>> existingFilter(PolicySearchDto policySearchDto) {
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MasterPolicyEntity> createQuery = criteriaBuilder.createQuery(MasterPolicyEntity.class);

		Root<MasterPolicyEntity> root = createQuery.from(MasterPolicyEntity.class);

		if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
					policySearchDto.getPolicyNumber().toLowerCase().trim()));
		}
		if (StringUtils.isNotBlank(policySearchDto.getCustomerName())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")),
					"%" + policySearchDto.getCustomerName().toLowerCase() + "%"));
		}
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
		return ApiResponseDto.success(masterPolicyEntities.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));
	}

	//Policy Premium update
	@Transactional
	@Override
	public ApiResponseDto<PolicyContributionDetailDto> updatePaymentAdjustment(PolicyContributionDetailDto policyContributionDetailDto) {
		Long masterQuotationId = null;
		Double getBalance = null;
		Double adjustementAmount = 0.0;
		Long stagingPolicyID = null;
		int year = 0;
		int month = 0;

		masterQuotationId = policyContributionDetailDto.getMasterQuotationId();
		stagingPolicyID = policyContributionDetailDto.getDepositAdjustmentDto().get(0).getStagingPolicyId();

		// update proposalsignDate
		StagingPolicyEntity savedStagingPolicyEntity = stagingPolicyRepository.findById(stagingPolicyID).get();
		savedStagingPolicyEntity.setProposalSignDate(policyContributionDetailDto.getProposalSignDate());
		stagingPolicyRepository.save(savedStagingPolicyEntity);
		// end

		// Unlock API Start if Environment is False
		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository
				.findBypolicyId(stagingPolicyID);
		if (isDevEnvironment == false) {
			List<UnlockDepositDetailDto> showDepositLockDto = new ArrayList<UnlockDepositDetailDto>();

			for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {

				UnlockDepositDetailDto depositLockDto = new UnlockDepositDetailDto();
				depositLockDto.setChallanNo(Integer.parseInt(policyDepositEntity.getChallanNo()));
				depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				depositLockDto.setUserCode(policyContributionDetailDto.getCreatedBy());
				depositLockDto
						.setProductCode(commonModuleService.getProductCode(savedStagingPolicyEntity.getProductId()));
				depositLockDto.setVariantCode(
						commonModuleService.getVariantCode(savedStagingPolicyEntity.getProductVariantId()));
				depositLockDto.setVariantCode("V3");
				showDepositLockDto.add(depositLockDto);
			}

			accountingService.unlockDeposits(showDepositLockDto, policyContributionDetailDto.getCreatedBy());
		}
		// Unlock API END if Environment is False

		PolicyContributionDetailEntity policyContributionDetailEntity = policyContributionDetailRepository
				.findBypolicyId(stagingPolicyID	);
//		policyContributionDetailEntity.setMasterQuotationId(contributionDto.getMasterQuotationId());
//		policyContributionDetailEntity.setTmpPolicyId(policyContributionDetailDto.getTmpPolicyId());
		policyContributionDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
		policyContributionDetailEntity.setLifePremium(policyContributionDetailDto.getLifePremium());
		policyContributionDetailEntity.setPastService(policyContributionDetailDto.getPastService());
		policyContributionDetailEntity.setGst(policyContributionDetailDto.getGst());
		policyContributionDetailEntity.setCurrentServices(policyContributionDetailDto.getCurrentServices());
		policyContributionDetailEntity.setModifiedBy(policyContributionDetailDto.getModifiedBy());
		policyContributionDetailEntity.setModifiedDate(new Date());
		policyContributionDetailEntity=policyContributionDetailRepository.save(policyContributionDetailEntity);
		
		List<PolicyDepositEntity> oldDepositList = policyDepositRepository.deleteBypolicyId(stagingPolicyID);
		List<PolicyContributionEntity> oldPolicyContribution = policyContributionRepository
				.deleteBypolicyId(stagingPolicyID);
		List<PolicyContrySummaryEntity> oldPolicyContriSummary = policyContrySummaryRepository
				.deleteBypolicyId(stagingPolicyID);
		
		PolicyContrySummaryEntity policyContrySummaryEntity = new PolicyContrySummaryEntity();
		StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(standardcode).get();
		PolicyContributionDetailEntity getGstSC = policyContributionDetailRepository
				.findBymasterQuotationId1(masterQuotationId);
		adjustementAmount = getGstSC.getGst() + getGstSC.getLifePremium() + getGstSC.getCurrentServices()
				+ getGstSC.getPastService();

		List<ShowDepositLockDto> showDepositLockDto = new ArrayList<ShowDepositLockDto>();
		List<Date> collectionDate = new ArrayList<>();
		for (DepositAdjustementDto getDepositAdjustementDto : policyContributionDetailDto.getDepositAdjustmentDto()) {
			if (adjustementAmount != 0) {
				masterQuotationId = getDepositAdjustementDto.getMasterQuotationId();

				PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

				PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();
				ShowDepositLockDto depositLockDto = new ShowDepositLockDto();
				if (adjustementAmount < getDepositAdjustementDto.getDepositAmount()) {
					getBalance = getDepositAdjustementDto.getDepositAmount() - adjustementAmount;
					policyDepositEntity.setAvailableAmount(getBalance);
					policyDepositEntity.setAdjustmentAmount(adjustementAmount);
					adjustementAmount = 0.0;
				} else {
					getBalance = adjustementAmount - getDepositAdjustementDto.getDepositAmount();
					policyDepositEntity.setAdjustmentAmount(getDepositAdjustementDto.getDepositAmount());
					policyDepositEntity.setAvailableAmount(0.0);
					adjustementAmount = adjustementAmount - getDepositAdjustementDto.getDepositAmount();
				}
				policyDepositEntity.setContributionDetailId(policyContributionDetailEntity.getId());
				policyDepositEntity.setChequeRealistionDate(getDepositAdjustementDto.getChequeRealisationDate());
				policyDepositEntity.setActive(true);
				Random r = new Random(System.currentTimeMillis());
				int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
				policyDepositEntity.setChallanNo(policyContributionDetailEntity.getChallanNo().toString());
				policyDepositEntity.setChequeRealistionDate(new Date());
				policyDepositEntity.setCollectionDate(new Date());
				policyDepositEntity.setCreateDate(new Date());
				policyDepositEntity.setZeroId(null);
				policyDepositEntity.setTransactionMode(getDepositAdjustementDto.getCollectionMode());
				policyDepositEntity.setStatus(null);
				policyDepositEntity.setRegConId(null);
				policyDepositEntity.setPolicyId(savedStagingPolicyEntity.getId());
				policyDepositEntity.setContributionType("NB");
				policyDepositEntity.setAdjConId(null);
				policyDepositEntity.setDepositAmount(getDepositAdjustementDto.getDepositAmount());
				policyDepositEntity.setCreatedBy(getDepositAdjustementDto.getCreatedBy());
				policyDepositEntity.setCreateDate(new Date());
				policyDepositEntity.setIsDeposit(true);
				policyDepositEntity.setRemark(getDepositAdjustementDto.getRemarks());
				policyDepositEntity.setCollectionNo(getDepositAdjustementDto.getCollectionNumber());
				policyDepositEntity.setCollectionStatus(getDepositAdjustementDto.getCollectionStatus());
				policyDepositRepository.save(policyDepositEntity);

				policyContributionEntity.setAdjConid(null);
				policyContributionEntity.setOpeningBalance(0.0);
				policyContributionEntity
						.setTotalContribution(getGstSC.getCurrentServices() + getGstSC.getPastService());
				policyContributionEntity.setClosingBlance(
						policyContributionEntity.getOpeningBalance() + policyContributionEntity.getTotalContribution());
				policyContributionEntity.setContReferenceNo(Long.toString(policyDepositEntity.getCollectionNo()));
				policyContributionEntity.setContributionDate(policyDepositEntity.getCollectionDate());
				policyContributionEntity.setContributionType(null);
				policyContributionEntity.setCreatedBy(getDepositAdjustementDto.getCreatedBy());
				policyContributionEntity.setCreateDate(new Date());
				policyContributionEntity.setEmployeeContribution(0.0);
				policyContributionEntity
						.setEmployerContribution(getGstSC.getCurrentServices() + getGstSC.getPastService());
				year = Calendar.getInstance().get(Calendar.YEAR);

				month = Calendar.getInstance().get(Calendar.MONTH) + 1;
				System.out.println("Financial month : " + month);
				if (month < 3) {
					policyContributionEntity.setFinancialYear((year - 1) + "-" + year);
				} else {
					policyContributionEntity.setFinancialYear(year + "-" + (year + 1));
				}
				policyContributionEntity.setActive(true);
				policyContributionEntity.setIsDeposit(true);
				policyContributionEntity.setOpeningBalance(0.0);
				policyContributionEntity.setPolicyId(savedStagingPolicyEntity.getId());
				policyContributionEntity.setRegConId(null);
				policyContributionEntity.setTxnEntryStatus(0l);
				policyContributionEntity.setVersionNo(1l);
				policyContributionEntity.setVoluntaryContribution(0.0);
				policyContributionEntity.setZeroAccountEntries(0l);
				policyContributionEntity.setContributionDetailId(policyContributionDetailEntity.getId());
				policyContributionRepository.save(policyContributionEntity);

				depositLockDto.setChallanNo(Integer.valueOf(policyContributionDetailEntity.getChallanNo()) );
				depositLockDto.setRefNo(policyDepositEntity.getId().toString());
				depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				depositLockDto.setUserCode(policyContributionDetailDto.getCreatedBy());
				depositLockDto.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
				depositLockDto
						.setProductCode(commonModuleService.getProductCode(savedStagingPolicyEntity.getProductId()));
				depositLockDto.setVariantCode(
						commonModuleService.getVariantCode(savedStagingPolicyEntity.getProductVariantId()));
				depositLockDto.setVariantCode("V3");
				logger.info("Lock Deposit Params: " + depositLockDto.toString());
				showDepositLockDto.add(depositLockDto);
			}
		}
		if (isDevEnvironment == false) {
			accountingService.lockDeposits(showDepositLockDto, policyContributionDetailDto.getCreatedBy());
		}

		Double getCSPPSP = getGstSC.getCurrentServices() + getGstSC.getPastService();

		policyContrySummaryEntity.setClosingBalance(getCSPPSP);
		policyContrySummaryEntity.setTotalContribution(getCSPPSP);
		policyContrySummaryEntity.setTotalEmployerContribution(getCSPPSP);
		policyContrySummaryEntity.setActive(true);
		policyContrySummaryEntity.setCreatedDate(new Date());
		policyContrySummaryEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		policyContrySummaryEntity.setContributionDetailId(policyContributionDetailEntity.getId());
		System.out.println("Financial month : " + month);
		if (month < 3) {
			policyContrySummaryEntity.setFinancialYear((year - 1) + "-" + year);
		} else {
			policyContrySummaryEntity.setFinancialYear(year + "-" + (year + 1));
		}
		policyContrySummaryEntity.setOpeningBalance(0.0);
		policyContrySummaryEntity.setPolicyId(savedStagingPolicyEntity.getId());
		policyContrySummaryEntity.setTotalEmployeeContribution(0.0);
		policyContrySummaryEntity.setTotalVoluntaryContribution(0.0);

		Double getPaise = Double.parseDouble(standardCodeEntity.getValue()) / 100;
		StagingPolicyValuationMatrixEntity getStagingPolicyValuationMatrixEntity = stagingPolicyValuationMatrixRepository
				.findByPolicyId(savedStagingPolicyEntity.getId()).get();
		Double getCPLCSum = getGstSC.getCurrentServices() + getGstSC.getPastService()
				+ (getStagingPolicyValuationMatrixEntity.getTotalSumAssured().isNaN() ? 0L
						: getStagingPolicyValuationMatrixEntity.getTotalSumAssured().longValue());
		logger.info("Adding Total= CurrentService+PastService+totalsumAssured");
		if (getCPLCSum > 0) {
			Double valuesDividedByThousandQuotient = getCPLCSum / 1000;
			Double valuesDividedByThousandremainder = getCPLCSum % 1000;
			if (valuesDividedByThousandremainder == 0) {

				policyContrySummaryEntity
						.setTotalContribution(getCPLCSum + (valuesDividedByThousandQuotient * getPaise.longValue()));
			} else {

				policyContrySummaryEntity.setTotalContribution(
						getCPLCSum + ((valuesDividedByThousandQuotient + 1) * getPaise.longValue()));
			}
		}

		policyContrySummaryEntity
				.setStampDuty((policyContrySummaryEntity.getTotalContribution().doubleValue() / 1000) * getPaise);
		logger.info("StamDuty =(TotalContribution /1000) * getPaise" + policyContrySummaryEntity.getStampDuty());
		policyContrySummaryRepository.save(policyContrySummaryEntity);
		return ApiResponseDto.success(PolicyHelper.contridetentityTocontridetailDto(policyContributionDetailEntity));
	}

	// Start of Make Payment Adjustment
	
	
	//Policy Premium new
	@Transactional
	@Override
	public ApiResponseDto<PolicyContributionDetailDto> makePayment(PolicyContributionDetailDto policyContributionDetailDto) {

		Long masterQuotationId = null;
		Double getBalance = null;
		Double adjustementAmount = 0.0;
		int year = 0;
		int month = 0;
		masterQuotationId = policyContributionDetailDto.getMasterQuotationId();
		boolean isFullPaymentReceived = true;
		boolean isMinimumPaymentReceived = true;
		StagingPolicyEntity policyEntity = new StagingPolicyEntity();
		TaskProcessEntity taskProcessEntity =taskProcessRepository.findByProcessName("POLICY");
		TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();
//		if(isDevEnvironment == false) {
//			policyEntity.setPolicyNumber(PolicyHelper.nextPolicyNumber(policyRepository.maxPolicyNumber()).toString());
//			}
//		else { 
//			policyEntity.setPolicyNumber(policyRepository.maxSipPolicyNumber());
//		}
		policyEntity.setPolicyStatusId(Long.parseLong(defaultStatusId));
		policyEntity.setPolicySubStatusId(Long.parseLong(defaultSubStatusId));
		policyEntity.setPolicyTaggedStatusId(Long.parseLong(defaultTaggedStatusId));
		policyEntity.setIsActive(true);
		policyEntity.setIsEscalatedToCo(false);
		policyEntity.setIsEscalatedToZo(false);
		policyEntity.setIsFullPaymentReceived(isFullPaymentReceived);
		policyEntity.setIsMinimumPaymentReceived(isMinimumPaymentReceived);
		policyEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		policyEntity.setCreatedDate(new Date());
		policyEntity.setRiskCommencementDate(new Date());	
		policyEntity.setLastPremiumPaid(new Date());	
		policyEntity.setContributtionFrequencyId(HttpConstants.CONTRIBUTIONFREQUENCY_ANNUALID);
		logger.info("----- masterQuotationId -----");
		PolicyContrySummaryEntity policyContrySummaryEntity = new PolicyContrySummaryEntity();
		if (masterQuotationId != null) {
			MasterQuotationEntity masterQuotationEntity = masterQuotationRepository.findById(masterQuotationId).get();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(masterQuotationEntity.getDateOfCommencement());
			calendar.add(Calendar.YEAR, 1);
			Date newDate = calendar.getTime();
			
			calendar.setTime(newDate);
			calendar.add(Calendar.DATE, -1);
			Date newPolicyEndDate = calendar.getTime();
			
			StagingMPHEntity mphEntity = MPHHelper.getMph(masterQuotationEntity.getProposalNumber(),
					policyContributionDetailDto.getCreatedBy(), endPoint);
			StagingMPHEntity savedMphEntity = stagingMPHRepository.save(mphEntity);
			PolicyDto policyDto = PolicyHelper.getDetailFromProposalAPI(masterQuotationEntity.getProposalNumber(),
					policyContributionDetailDto.getCreatedBy(), endPoint);
			policyEntity.setGstApplicableId(masterQuotationEntity.getGstApplicableId());
			policyEntity.setMasterpolicyHolder(savedMphEntity.getId());
			policyEntity.setProductId(masterQuotationEntity.getProductId());
			policyEntity.setContactPersonId(masterQuotationEntity.getContactPersonId());
			policyEntity.setProductVariantId(masterQuotationEntity.getProductVariantId());
			policyEntity.setMasterQuotationId(masterQuotationId);
			policyEntity.setCustomerCode(masterQuotationEntity.getCustomerCode());
			policyEntity.setCustomerName(policyDto.getCustomerName());
			policyEntity.setLineOfBusiness(policyDto.getLineOfBusiness());
			policyEntity.setProductName(policyDto.getProductName());
			policyEntity.setProductVariant(policyDto.getProductVariant());
			policyEntity.setUnitId(policyDto.getUnitId());
			policyEntity.setUnitCode(policyDto.getUnitCode());
			policyEntity.setMarketingOfficerCode(policyDto.getMarketingOfficerCode());
			policyEntity.setMarketingOfficerName(policyDto.getMarketingOfficerName());
			policyEntity.setIntermediaryCode(policyDto.getIntermediaryCode());
			policyEntity.setIntermediaryName(policyDto.getIntermediaryName());
			policyEntity.setIndustryType(policyDto.getIndustryType());
			policyEntity.setAnnualRenewlDate(newDate);
			policyEntity.setTotalMember((long) masterMemberRepository.findAll(masterQuotationEntity.getId()).size());
			policyEntity.setProposalNumber(masterQuotationEntity.getProposalNumber());
			policyEntity.setPayto(masterQuotationEntity.getPayto());
			policyEntity.setPolicyStartDate(masterQuotationEntity.getDateOfCommencement());
			policyEntity.setPolicyEndDate(newPolicyEndDate);
			policyEntity.setProposalSignDate(policyContributionDetailDto.getProposalSignDate());
			
			// policyEntity.setDojofscheme(masterQuotationEntity.getDateOfCommencement());
		}

		StagingPolicyEntity savedStagingPolicyEntity = stagingPolicyRepository.save(policyEntity);

		StagingPolicyEntity stagingPolicyEntity = copyToStagingPolicy(savedStagingPolicyEntity.getId(),
				policyContributionDetailDto.getCreatedBy());

		List<StagingPolicyMemberEntity> tt = PolicyMemberHelper.entityToMasterEntity(
				masterMemberRepository.findByQuotationId(masterQuotationId), savedStagingPolicyEntity);
		stagingPolicyMemberRepository.saveAll(tt);
		logger.info("savingggggggg StagingPolicyEntity");
		logger.info(savedStagingPolicyEntity.getPolicyStatusId());
		logger.info(savedStagingPolicyEntity.getPolicySubStatusId());
		logger.info(savedStagingPolicyEntity.getPolicyTaggedStatusId());
		
		if(policyContributionDetailRepository.findBymasterQuotationId(masterQuotationId).size()> 0) {
			 policyContributionDetailRepository.deleteByMasterId(masterQuotationId);
			 }

		PolicyContributionDetailEntity policyContributionDetailEntity = new ModelMapper()
				.map(policyContributionDetailDto, PolicyContributionDetailEntity.class);
		policyContributionDetailEntity.setMasterQuotationId(masterQuotationId);
		policyContributionDetailEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		policyContributionDetailEntity.setCreatedDate(new Date());
		policyContributionDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
		policyContributionDetailEntity.setPolicyId(savedStagingPolicyEntity.getId());
		policyContributionDetailEntity.setEntryType("NB");				
		policyContributionDetailEntity=	policyContributionDetailRepository.save(policyContributionDetailEntity);

		StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(standardcode).get();
		PolicyContributionDetailEntity getGstSC = policyContributionDetailRepository
				.findBymasterQuotationId1(masterQuotationId);
		adjustementAmount = getGstSC.getGst() + getGstSC.getLifePremium() + getGstSC.getCurrentServices()
				+ getGstSC.getPastService();
		
		List<ShowDepositLockDto> showDepositLockDto =new ArrayList<ShowDepositLockDto>();
		List<Date> collectionDate = new ArrayList<>();
		for (DepositAdjustementDto getDepositAdjustementDto : policyContributionDetailDto.getDepositAdjustmentDto()) {
			if (adjustementAmount != 0) {
				masterQuotationId = getDepositAdjustementDto.getMasterQuotationId();

				PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();
				PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();
				ShowDepositLockDto depositLockDto = new ShowDepositLockDto();
				
				if (adjustementAmount < getDepositAdjustementDto.getDepositAmount()) {
				
					getBalance = getDepositAdjustementDto.getDepositAmount() - adjustementAmount;
					policyDepositEntity.setAvailableAmount(getBalance);
					policyDepositEntity.setAdjustmentAmount(adjustementAmount);
					adjustementAmount = 0.0;
				} else {
				
					getBalance = adjustementAmount - getDepositAdjustementDto.getDepositAmount();
					policyDepositEntity.setAdjustmentAmount(getDepositAdjustementDto.getDepositAmount());
					policyDepositEntity.setAvailableAmount(0.0);
					adjustementAmount = adjustementAmount - getDepositAdjustementDto.getDepositAmount();
				}
				policyDepositEntity.setChequeRealistionDate(getDepositAdjustementDto.getChequeRealisationDate());
				policyDepositEntity.setActive(true);
				Random r = new Random(System.currentTimeMillis());
				int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
				policyDepositEntity.setChallanNo(policyContributionDetailEntity.getChallanNo());		
				policyDepositEntity.setChequeRealistionDate(new Date());
				policyDepositEntity.setCollectionDate(getDepositAdjustementDto.getCollectionDate());
				collectionDate.add(policyDepositEntity.getCollectionDate());
				policyDepositEntity.setCreateDate(new Date());
				policyDepositEntity.setZeroId(null);
				policyDepositEntity.setTransactionMode(getDepositAdjustementDto.getTransactionMode());
				policyDepositEntity.setStatus(null);
				policyDepositEntity.setRegConId(null);
				policyDepositEntity.setPolicyId(savedStagingPolicyEntity.getId());
				policyDepositEntity.setContributionType("NB");
				policyDepositEntity.setAdjConId(null);
				policyDepositEntity.setDepositAmount(getDepositAdjustementDto.getDepositAmount());
				policyDepositEntity.setCreatedBy(getDepositAdjustementDto.getCreatedBy());
				policyDepositEntity.setCreateDate(new Date());
				policyDepositEntity.setIsDeposit(true);
				policyDepositEntity.setRemark(getDepositAdjustementDto.getRemarks());
				policyDepositEntity.setTransactionMode(getDepositAdjustementDto.getCollectionMode());
				policyDepositEntity.setCollectionNo(getDepositAdjustementDto.getCollectionNumber());
				policyDepositEntity.setCollectionStatus(getDepositAdjustementDto.getCollectionStatus());
				policyDepositEntity.setContributionDetailId(policyContributionDetailEntity.getId());		
				policyDepositEntity = policyDepositRepository.save(policyDepositEntity);

				policyContributionEntity.setAdjConid(null);
				policyContributionEntity.setOpeningBalance(0.0);
				policyContributionEntity
						.setTotalContribution(getGstSC.getCurrentServices() + getGstSC.getPastService());
				policyContributionEntity.setClosingBlance(
						policyContributionEntity.getOpeningBalance() + policyContributionEntity.getTotalContribution());
				policyContributionEntity.setContReferenceNo(Long.toString(policyDepositEntity.getCollectionNo()));
				policyContributionEntity.setContributionDate(policyDepositEntity.getCollectionDate());
				policyContributionEntity.setContributionType(null);
				policyContributionEntity.setCreatedBy(getDepositAdjustementDto.getCreatedBy());
				policyContributionEntity.setCreateDate(new Date());
				policyContributionEntity.setEmployeeContribution(0.0);
				policyContributionEntity
						.setEmployerContribution(getGstSC.getCurrentServices() + getGstSC.getPastService());
				year = Calendar.getInstance().get(Calendar.YEAR);

				month = Calendar.getInstance().get(Calendar.MONTH) + 1;
				System.out.println("Financial month : " + month);
				if (month < 3) {
					policyContributionEntity.setFinancialYear((year - 1) + "-" + year);
				} else {
					policyContributionEntity.setFinancialYear(year + "-" + (year + 1));
				}
				policyContributionEntity.setActive(true);
				policyContributionEntity.setIsDeposit(true);
				policyContributionEntity.setOpeningBalance(0.0);
				policyContributionEntity.setPolicyId(savedStagingPolicyEntity.getId());
				policyContributionEntity.setRegConId(null);
				policyContributionEntity.setTxnEntryStatus(0l);
				policyContributionEntity.setContributionType("NB");
				policyContributionEntity.setVersionNo(1l);
				policyContributionEntity.setVoluntaryContribution(0.0);
				policyContributionEntity.setZeroAccountEntries(0l);
				policyContributionEntity.setContributionDetailId(policyContributionDetailEntity.getId());
				policyContributionEntity.setQuarter(HttpConstants.newBusinessQuarter);//for now default Q0
				policyContributionRepository.save(policyContributionEntity);
			
				String prodAndVarientCodeSame=	commonModuleService.getProductCode(stagingPolicyEntity.getProductId());
			
					depositLockDto.setChallanNo(Integer.valueOf(policyContributionDetailEntity.getChallanNo()));
					depositLockDto.setRefNo(policyDepositEntity.getId().toString());
					depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
					depositLockDto.setUserCode(policyContributionDetailDto.getCreatedBy());
					depositLockDto.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
					depositLockDto.setProductCode(prodAndVarientCodeSame);
					depositLockDto.setVariantCode(prodAndVarientCodeSame);
		
					logger.info("Lock Deposit Params: "+depositLockDto.toString());
					showDepositLockDto.add(depositLockDto);
			}
		
		}
		
		if (isDevEnvironment == false) {
			accountingService.lockDeposits(showDepositLockDto,policyContributionDetailDto.getCreatedBy());
		}

             Date risk = Collections.max(collectionDate);
             savedStagingPolicyEntity.setRiskCommencementDate(risk);
				stagingPolicyRepository.save(savedStagingPolicyEntity);

		// update start
		Double getCSPPSP = getGstSC.getCurrentServices() + getGstSC.getPastService();

		policyContrySummaryEntity.setClosingBalance(getCSPPSP);
		policyContrySummaryEntity.setTotalContribution(getCSPPSP);
		policyContrySummaryEntity.setTotalEmployerContribution(getCSPPSP);
		policyContrySummaryEntity.setActive(true);
		policyContrySummaryEntity.setCreatedDate(new Date());
		policyContrySummaryEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		policyContrySummaryEntity.setContributionDetailId(policyContributionDetailEntity.getId());
		System.out.println("Financial month : " + month);
		if (month < 3) {
			policyContrySummaryEntity.setFinancialYear((year - 1) + "-" + year);
		} else {
			policyContrySummaryEntity.setFinancialYear(year + "-" + (year + 1));
		}
		policyContrySummaryEntity.setOpeningBalance(0.0);
		policyContrySummaryEntity.setPolicyId(savedStagingPolicyEntity.getId());
		policyContrySummaryEntity.setTotalEmployeeContribution(0.0);
		policyContrySummaryEntity.setTotalVoluntaryContribution(0.0);

		Double getPaise = Double.parseDouble(standardCodeEntity.getValue()) / 100;
		StagingPolicyValuationMatrixEntity getStagingPolicyValuationMatrixEntity = stagingPolicyValuationMatrixRepository
				.findByPolicyId(savedStagingPolicyEntity.getId()).get();
		Double getCPLCSum = getGstSC.getCurrentServices() + getGstSC.getPastService()
				+ (getStagingPolicyValuationMatrixEntity.getTotalSumAssured().isNaN() ? 0L
						: getStagingPolicyValuationMatrixEntity.getTotalSumAssured().longValue());
		logger.info("Adding Total= CurrentService+PastService+totalsumAssured");
		if (getCPLCSum > 0) {
			Double valuesDividedByThousandQuotient = getCPLCSum / 1000;
			Double valuesDividedByThousandremainder = getCPLCSum % 1000;
			if (valuesDividedByThousandremainder == 0) {

				policyContrySummaryEntity
						.setTotalContribution(getCPLCSum + (valuesDividedByThousandQuotient * getPaise.longValue()));
			} else {

				policyContrySummaryEntity.setTotalContribution(
						getCPLCSum + ((valuesDividedByThousandQuotient + 1) * getPaise.longValue()));
			}
		}

		policyContrySummaryEntity
				.setStampDuty((policyContrySummaryEntity.getTotalContribution().doubleValue() / 1000) * getPaise);
		logger.info("StamDuty =(TotalContribution /1000) * getPaise" + policyContrySummaryEntity.getStampDuty());
		policyContrySummaryRepository.save(policyContrySummaryEntity);
		
		taskAllocationEntity.setTaskStatus(defaultStatusId);
		taskAllocationEntity.setRequestId(stagingPolicyEntity.getProposalNumber());
		taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//		taskAllocationEntity.setLocationType(stagingPolicyEntity.getUnitCode());
		taskAllocationEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		taskAllocationEntity.setIsActive(true);
		taskAllocationEntity.setModulePrimaryId(stagingPolicyEntity.getId());
		taskAllocationEntity.setCreatedDate(new Date());
		
		taskAllocationRepository.save(taskAllocationEntity);
		// end
		return ApiResponseDto.success(PolicyHelper.contridetentityTocontridetailDto(policyContributionDetailEntity));
	}
	
//	forShowDepositLock


	public HttpHeaders getAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
	// End of Make Payment Adustment

	//Renewal Premium new
	@Transactional
	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> makePaymentAdjustmentforRenewal(
			PolicyContributionDetailDto policyContributionDetailDto) {
		Long masterTmpPolicyId = null;
		Double getBalance = null;
		Double adjustementAmount = 0.0;
		int year = 0;
		int month = 0;
		
		TaskProcessEntity taskProcessEntity =taskProcessRepository.findByProcessName("RENEWAL PROCESSING");
		TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();
		
		PolicyContributionDetailEntity policyContributionDetailEntity = new ModelMapper().map(policyContributionDetailDto,
				PolicyContributionDetailEntity.class);
		policyContributionDetailEntity.setTmpPolicyId(policyContributionDetailDto.getTmpPolicyId());
		policyContributionDetailEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		policyContributionDetailEntity.setCreatedDate(new Date());
		policyContributionDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
		policyContributionDetailEntity.setEntryType("RE");
		policyContributionDetailEntity.setActive(true);
		policyContributionDetailEntity=policyContributionDetailRepository.save(policyContributionDetailEntity);
		
		masterTmpPolicyId = policyContributionDetailEntity.getTmpPolicyId();
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = null;
		if (masterTmpPolicyId != null) {
			renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(masterTmpPolicyId).get();

			renewalPolicyTMPEntity.setPolicyStatusId(Long.parseLong(defaultStatusId));
			renewalPolicyTMPEntity.setPolicySubStatusId(Long.parseLong(defaultSubStatusId));
			renewalPolicyTMPEntity.setPolicytaggedStatusId(Long.parseLong(defaultTaggedStatusId));

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(renewalPolicyTMPEntity.getAnnualRenewlDate());
			calendar.add(Calendar.YEAR, 1);
			Date newDate = calendar.getTime();
			renewalPolicyTMPEntity.setAnnualRenewlDate(newDate);
			renewalPolicyTMPEntity=	renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
			
		}
		PolicyContrySummaryEntity policyContrySummaryEntity = new PolicyContrySummaryEntity();
		StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(standardcode).get();
		PolicyContributionDetailEntity getGstSC = policyContributionDetailRepository
				.findBymasterTmpPolicyId(masterTmpPolicyId);
		
		adjustementAmount = getGstSC.getGst() + getGstSC.getLifePremium() + getGstSC.getCurrentServices()
				+ getGstSC.getPastService();
		List<ShowDepositLockDto> showDepositLockDto =new ArrayList<ShowDepositLockDto>();
		List<Date> collectionDate = new ArrayList<>();
//		for (DepositAdjustementDto getDepositAdjustementDto : policyContributionDetailDto.getDepositAdjustmentDto()){
//			if (adjustementAmount != 0) {
//				masterTmpPolicyId = getDepositAdjustementDto.getTmpPolicyId();
//
//				PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();
//
//				PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();
//				
//
//				if (adjustementAmount < getDepositAdjustementDto.getDepositAmount()) {
//					getBalance = getDepositAdjustementDto.getDepositAmount() - adjustementAmount;
//					policyDepositEntity.setAvailableAmount(getBalance);
//					policyDepositEntity.setAdjustmentAmount(adjustementAmount);
//					adjustementAmount = 0.0;
//				} else {
//					getBalance = adjustementAmount - getDepositAdjustementDto.getDepositAmount();
//					policyDepositEntity.setAdjustmentAmount(getDepositAdjustementDto.getDepositAmount());
//					policyDepositEntity.setAvailableAmount(0.0);
//					adjustementAmount = adjustementAmount - getDepositAdjustementDto.getDepositAmount();
//				}
//				policyDepositEntity.setChequeRealistionDate(getDepositAdjustementDto.getChequeRealisationDate());
//				policyDepositEntity.setActive(true);
//				Random r = new Random(System.currentTimeMillis());
//				int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
//				policyDepositEntity.setChallanNo(policyContributionDetailEntity.getChallanNo());
//				policyDepositEntity.setChequeRealistionDate(new Date());
//				policyDepositEntity.setCollectionDate(new Date());
//				collectionDate.add(policyDepositEntity.getCollectionDate());
//				policyDepositEntity.setZeroId(null);
//				policyDepositEntity.setTransactionMode(getDepositAdjustementDto.getCollectionMode());
//				policyDepositEntity.setStatus(null);
//				policyDepositEntity.setRegConId(null);
//				policyDepositEntity.setTmpPolicyId(masterTmpPolicyId);
//				policyDepositEntity.setContributionType("RE");
//				policyDepositEntity.setContributionDetailId(policyContributionDetailEntity.getId());;
//				policyDepositEntity.setAdjConId(null);
//				policyDepositEntity.setDepositAmount(getDepositAdjustementDto.getDepositAmount());
//				policyDepositEntity.setCreatedBy(getDepositAdjustementDto.getCreatedBy());
//				policyDepositEntity.setCreateDate(new Date());
//				policyDepositEntity.setIsDeposit(true);
//				policyDepositEntity.setRemark(getDepositAdjustementDto.getRemarks());
//				policyDepositEntity.setCollectionNo(getDepositAdjustementDto.getCollectionNumber());
//				policyDepositEntity.setCollectionStatus(getDepositAdjustementDto.getCollectionStatus());
//				policyDepositEntity.setActive(true);
//				policyDepositRepository.save(policyDepositEntity);
//
//				
//				
//				policyContributionEntity.setContributionDetailId(policyContributionDetailEntity.getId());;
//				policyContributionEntity.setAdjConid(null);
//				policyContributionEntity.setOpeningBalance(0.0);
//				policyContributionEntity
//						.setTotalContribution(getGstSC.getCurrentServices() + getGstSC.getPastService());
//				policyContributionEntity.setClosingBlance(
//						policyContributionEntity.getOpeningBalance() + policyContributionEntity.getTotalContribution());
//				policyContributionEntity.setContReferenceNo(Long.toString(policyDepositEntity.getCollectionNo()));
//				policyContributionEntity.setContributionDate(policyDepositEntity.getCollectionDate());
//				policyContributionEntity.setContributionType(null);
//				policyContributionEntity.setCreatedBy(getDepositAdjustementDto.getCreatedBy());
//				policyContributionEntity.setCreateDate(new Date());
//				policyContributionEntity.setEmployeeContribution(0.0);
//				policyContributionEntity
//						.setEmployerContribution(getGstSC.getCurrentServices() + getGstSC.getPastService());
//				year = Calendar.getInstance().get(Calendar.YEAR);
//
//				month = Calendar.getInstance().get(Calendar.MONTH) + 1;
//				System.out.println("Financial month : " + month);
//				if (month < 3) {
//					policyContributionEntity.setFinancialYear((year - 1) + "-" + year);
//				} else {
//					policyContributionEntity.setFinancialYear(year + "-" + (year + 1));
//				}
//				policyContributionEntity.setActive(true);
//				policyContributionEntity.setIsDeposit(true);
//				policyContributionEntity.setOpeningBalance(0.0);
////				policyContributionEntity.setPolicyId(savedStagingPolicyEntity.getId());
//				policyContributionEntity.setTmpPolicyId(masterTmpPolicyId);
//				policyContributionEntity.setRegConId(null);
//				policyContributionEntity.setTxnEntryStatus(0l);
//				policyContributionEntity.setVersionNo(1l);
//				policyContributionEntity.setVoluntaryContribution(0.0);
//				policyContributionEntity.setZeroAccountEntries(0l);
//				policyContributionRepository.save(policyContributionEntity);
//
//			
//			ShowDepositLockDto depositLockDto = new ShowDepositLockDto();
//			
//			String prodAndVarientCodeSame=	commonModuleService.getProductCode(renewalPolicyTMPEntity.getProductId());
//			
//			depositLockDto.setChallanNo(Integer.valueOf(policyContributionDetailEntity.getChallanNo()));
//			depositLockDto.setRefNo(policyDepositEntity.getId().toString());
//			depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
//			depositLockDto.setUserCode(policyContributionDetailDto.getCreatedBy());
//			depositLockDto.setDueMonth(DateUtils.currentMonth()+"/"+DateUtils.currentDay());
//			depositLockDto.setProductCode(prodAndVarientCodeSame);
//			depositLockDto.setVariantCode(prodAndVarientCodeSame);
//			
//			logger.info("Lock Deposit Params: "+depositLockDto.toString());
//			showDepositLockDto.add(depositLockDto);;
//			}
//		}
//		if (isDevEnvironmentForRenewals == false) {
//			accountingService.lockDeposits(showDepositLockDto,policyContributionDetailDto.getCreatedBy());
//		}

//           Date risk = Collections.max(collectionDate);
//             renewalPolicyTMPEntity.setRiskCommencementDate(risk);
//             renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		
		Double getCSPPSP = getGstSC.getCurrentServices() + getGstSC.getPastService();
		
	
		policyContrySummaryEntity.setContributionDetailId(policyContributionDetailEntity.getId());
		policyContrySummaryEntity.setClosingBalance(getCSPPSP);
		policyContrySummaryEntity.setTotalContribution(getCSPPSP);
		policyContrySummaryEntity.setTotalEmployerContribution(getCSPPSP);
		policyContrySummaryEntity.setActive(true);
		policyContrySummaryEntity.setCreatedDate(new Date());
		policyContrySummaryEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		System.out.println("Financial month : " + month);
		if (month < 3) {
			policyContrySummaryEntity.setFinancialYear((year - 1) + "-" + year);
		} else {
			policyContrySummaryEntity.setFinancialYear(year + "-" + (year + 1));
		}
		policyContrySummaryEntity.setOpeningBalance(0.0);
//		policyContrySummaryEntity.setPolicyId(savedStagingPolicyEntity.getId());
		policyContrySummaryEntity.setTmpPolicyId(masterTmpPolicyId);
		policyContrySummaryEntity.setTotalEmployeeContribution(0.0);
		policyContrySummaryEntity.setTotalVoluntaryContribution(0.0);
		Double getPaise = Double.parseDouble(standardCodeEntity.getValue()) / 100;

		policyContrySummaryEntity
				.setStampDuty((policyContrySummaryEntity.getTotalContribution().doubleValue() / 1000) * getPaise);
		logger.info("StamDuty =(TotalContribution /1000) * getPaise" + policyContrySummaryEntity.getStampDuty());
		policyContrySummaryRepository.save(policyContrySummaryEntity);
//		StagingPolicyValuationMatrixEntity getStagingPolicyValuationMatrixEntity=stagingPolicyValuationMatrixRepository.findByPolicyId(savedStagingPolicyEntity.getId()).get();

		RenewalValuationMatrixTMPEntity getRenewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
				.findBytmpPolicyId(masterTmpPolicyId).get();

		Double getCPLCSum = getGstSC.getCurrentServices() + getGstSC.getPastService()
				+ (getRenewalValuationMatrixTMPEntity.getTotalSumAssured().isNaN() ? 0L
						: getRenewalValuationMatrixTMPEntity.getTotalSumAssured().longValue());
		logger.info("Adding Total= CurrentService+PastService+totalsumAssured");
		if (getCPLCSum > 0) {
			Double valuesDividedByThousandQuotient = getCPLCSum / 1000;
			Double valuesDividedByThousandremainder = getCPLCSum % 1000;
			if (valuesDividedByThousandremainder == 0) {

				policyContrySummaryEntity
						.setTotalContribution(getCPLCSum + (valuesDividedByThousandQuotient * getPaise.longValue()));
			} else {

				policyContrySummaryEntity.setTotalContribution(
						getCPLCSum + ((valuesDividedByThousandQuotient + 1) * getPaise.longValue()));
			}
		}
		policyContrySummaryEntity.setStampDuty((policyContrySummaryEntity.getTotalContribution() / 1000) * getPaise);
		logger.info("StamDuty =(TotalContribution /1000) * getPaise" + policyContrySummaryEntity.getStampDuty());
		policyContrySummaryRepository.save(policyContrySummaryEntity);
		
		policyServiceRepository.deactivateservicetype(renewalPolicyTMPEntity.getMasterPolicyId());
		// PolicyRenewalRemainderEntity updateStatus = policyRenewalRemainderRepository
		// 		.findBypolicyId(renewalPolicyTMPEntity.getMasterPolicyId());
		// updateStatus.setIsActive(false);
		// policyRenewalRemainderRepository.save(updateStatus);
		
		taskAllocationEntity.setTaskStatus(defaultStatusId);
		taskAllocationEntity.setRequestId(renewalPolicyTMPEntity.getPolicyNumber());
		taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
	//	taskAllocationEntity.setLocationType(policyContributionDetailDto.getUnitCode());
		taskAllocationEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		taskAllocationEntity.setCreatedDate(new Date());
		taskAllocationEntity.setModulePrimaryId(renewalPolicyTMPEntity.getId());
		taskAllocationEntity.setIsActive(true);
		taskAllocationRepository.save(taskAllocationEntity);
		
		return ApiResponseDto.success(RenewalPolicyTMPHelper.entityToDto(renewalPolicyTMPEntity));
	}

//	@Transactional
//	@Override
//	public ApiResponseDto<PolicyDto> makePaynent(List<PaymentDto> paymentDtos) {
//
//		// save all payments
//		boolean isFullPaymentReceived = true;
//		boolean isMinimumPaymentReceived = true;
//		Long masterQuotationId = null;
//		for (PaymentDto paymentDto : paymentDtos) {
//			logger.info("getQuotationChargeId");
//			logger.info(paymentDto.getQuotationChargeId());
//
//			QuotationChargeEntity quotationChargeEntity = quotationChargeRepository
//					.findById(paymentDto.getQuotationChargeId()).get();
//			logger.info("getMasterQuotationId");
//			logger.info(quotationChargeEntity.getMasterQuotationId());
//			masterQuotationId = quotationChargeEntity.getMasterQuotationId();
//			// masterQuotationId = 73L;
//			quotationChargeEntity.setBalanceAmount(paymentDto.getBalanceAmount());
//			quotationChargeEntity.setModifiedBy(paymentDto.getCreatedBy());
//			quotationChargeEntity.setModifiedDate(new Date());
//
//			PaymentEntity paymentEntity = PaymentHelper.dtoToEntity(paymentDto);
//			paymentEntity.setQuotationCharge(quotationChargeEntity);
//			paymentEntity.setIsActive(true);
//			paymentEntity.setCreatedDate(new Date());
//
//			quotationChargeRepository.save(quotationChargeEntity);
//			paymentRepository.save(paymentEntity);
//
//			isFullPaymentReceived = isFullPaymentReceived && paymentDto.getIsFullPaymentReceived();
//			isMinimumPaymentReceived = isMinimumPaymentReceived && paymentDto.getIsMinimumPaymentReceived();
//		}
//
//		// create new policy
//		StagingPolicyEntity policyEntity = new StagingPolicyEntity();
//		policyEntity.setPolicyNumber(PolicyHelper.nextPolicyNumber(policyRepository.maxPolicyNumber()).toString());
//		policyEntity.setPolicyStatusId(Long.parseLong(defaultStatusId));
//		policyEntity.setPolicySubStatusId(Long.parseLong(defaultSubStatusId));
//		policyEntity.setPolicyTaggedStatusId(Long.parseLong(defaultTaggedStatusId));
//		policyEntity.setIsActive(true);
//		policyEntity.setIsEscalatedToCo(false);
//		policyEntity.setIsEscalatedToZo(false);
//		policyEntity.setIsFullPaymentReceived(isFullPaymentReceived);
//		policyEntity.setIsMinimumPaymentReceived(isMinimumPaymentReceived);
//		policyEntity.setCreatedBy(paymentDtos.get(0).getCreatedBy());
//		policyEntity.setCreatedDate(new Date());
//		logger.info("----- masterQuotationId -----");
//		logger.info(masterQuotationId);
//		if (masterQuotationId != null) {
//			MasterQuotationEntity masterQuotationEntity = masterQuotationRepository.findById(masterQuotationId).get();
//			StagingMPHEntity mphEntity = MPHHelper.getMph(masterQuotationEntity.getProposalNumber(),
//					paymentDtos.get(0).getCreatedBy(), endPoint);
//			StagingMPHEntity savedMphEntity = stagingMPHRepository.save(mphEntity);
//			PolicyDto policyDto = PolicyHelper.getDetailFromProposalAPI(masterQuotationEntity.getProposalNumber(),
//					paymentDtos.get(0).getCreatedBy(), endPoint);
//			policyEntity.setGstApplicableId(masterQuotationEntity.getGstApplicableId());
//			policyEntity.setMasterpolicyHolder(savedMphEntity.getId());
//			policyEntity.setProductId(masterQuotationEntity.getProductId());
//			policyEntity.setProductVariantId(masterQuotationEntity.getProductVariantId());
//			policyEntity.setMasterQuotationId(masterQuotationId);
//			policyEntity.setCustomerCode(masterQuotationEntity.getCustomerCode());
//			policyEntity.setCustomerName(policyDto.getCustomerName());
//			policyEntity.setLineOfBusiness(policyDto.getLineOfBusiness());
//			policyEntity.setProductName(policyDto.getProductName());
//			policyEntity.setProductVariant(policyDto.getProductVariant());
//			policyEntity.setUnitId(policyDto.getUnitId());
//			policyEntity.setUnitCode(policyDto.getUnitCode());
//			policyEntity.setMarketingOfficerCode(policyDto.getMarketingOfficerCode());
//			policyEntity.setMarketingOfficerName(policyDto.getMarketingOfficerName());
//			policyEntity.setIntermediaryCode(policyDto.getIntermediaryCode());
//			policyEntity.setIntermediaryName(policyDto.getIntermediaryName());
//			policyEntity.setIndustryType(policyDto.getIndustryType());
//			policyEntity.setTotalMember((long) masterMemberRepository.findAll(masterQuotationEntity.getId()).size());
//			;
//
//		}
//
//		StagingPolicyEntity savedStagingPolicyEntity = stagingPolicyRepository.save(policyEntity);
//
//		StagingPolicyEntity stagingPolicyEntity = copyToStagingPolicy(savedStagingPolicyEntity.getId(),
//				paymentDtos.get(0).getCreatedBy());
//
//		List<StagingPolicyMemberEntity> tt = PolicyMemberHelper.entityToMasterEntity(
//				masterMemberRepository.findByQuotationId(masterQuotationId), savedStagingPolicyEntity);
//		stagingPolicyMemberRepository.saveAll(tt);
//		logger.info("savingggggggg StagingPolicyEntity");
//		logger.info(savedStagingPolicyEntity.getPolicyStatusId());
//		logger.info(savedStagingPolicyEntity.getPolicySubStatusId());
//		logger.info(savedStagingPolicyEntity.getPolicyTaggedStatusId());
//
//		return ApiResponseDto.success(PolicyHelper.entityToDto(savedStagingPolicyEntity));
//	}
//
	private StagingPolicyEntity copyToStagingPolicy(Long id, String username) {
		// Copy to Policy SchmeRule table
		StagingPolicyEntity stagingPolicyEntity = stagingPolicyRepository.findById(id).get();

		Optional<MasterSchemeRuleEntity> masterSchemeRuleEntity = Optional
				.of(masterSchemeRuleRepository.findByQuotationId(stagingPolicyEntity.getMasterQuotationId()));
		if (masterSchemeRuleEntity.isPresent()) {
			StagingPolicySchemeRule stagingPolicySchemeRule = PolicySchemeRuleHelper
					.masterQuotationentityToPolicyStagingEntity(masterSchemeRuleEntity.get());
			stagingPolicySchemeRule.setId(null);
			stagingPolicySchemeRule.setPolicyId(stagingPolicyEntity.getId());
			stagingPolicySchemeRuleRepository.save(stagingPolicySchemeRule);
		}

		List<MasterLifeCoverEntity> masterLifeCoverEntities = masterLifeCoverEntityRepository
				.findByQuotationId(stagingPolicyEntity.getMasterQuotationId());
		logger.info("masterLifeCoverEntities size");
		logger.info(masterLifeCoverEntities.size());
		if (masterLifeCoverEntities.size() > 0) {
			List<StagingPolicyLifeCoverEntity> stagingPolicyLifeCoverEntities = new ArrayList<StagingPolicyLifeCoverEntity>();
			for (MasterLifeCoverEntity masterLifeCoverEntity : masterLifeCoverEntities) {
				StagingPolicyLifeCoverEntity stagingPolicyLifeCoverEntity = PolicyLifeCoverHelper
						.masterQuotationentityToPolicyStagingEntity(masterLifeCoverEntity);
				stagingPolicyLifeCoverEntity.setId(null);
				stagingPolicyLifeCoverEntity.setPolicyId(stagingPolicyEntity.getId());
				stagingPolicyLifeCoverEntities.add(stagingPolicyLifeCoverEntity);
			}
			stagingPolicyLifeCoverRepository.saveAll(stagingPolicyLifeCoverEntities);

		}
		// MemberCategoryTest
		List<MemberCategoryEntity> addMemberCategoryEntity = new ArrayList<MemberCategoryEntity>();
		List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
				.findByqmstQuotationId(stagingPolicyEntity.getMasterQuotationId());
		for (MemberCategoryEntity getmemberCategoryEntity : memberCategoryEntity) {
			getmemberCategoryEntity.setPstgPolicyId(stagingPolicyEntity.getId());
			addMemberCategoryEntity.add(getmemberCategoryEntity);
		}
		memberCategoryRepository.saveAll(addMemberCategoryEntity);

		List<MasterGratuityBenefitEntity> masterGratuityBenefitEntities = MasterGratuityBenefitRepository
				.findByQuotationId(stagingPolicyEntity.getMasterQuotationId());
		if (masterGratuityBenefitEntities.size() > 0) {
			List<StagingPolicyGratuityBenefitEntity> stagingPolicyGratuityBenefitEntities = new ArrayList<StagingPolicyGratuityBenefitEntity>();
			for (MasterGratuityBenefitEntity masterGratuityBenefitEntity : masterGratuityBenefitEntities) {
				stagingPolicyGratuityBenefitEntities
						.add(PolicyGratuityBenefitHelper.masterQuotationentityToPolicyStagingEntity(
								masterGratuityBenefitEntity, stagingPolicyEntity.getId()));
			}
			stagingPolicyGratuityBenefitRepository.saveAll(stagingPolicyGratuityBenefitEntities);
		}

		Optional<MasterValuationMatrixEntity> masterValuationMatrixEntity = masterValuationMatrixRepository
				.findByQuotationId(stagingPolicyEntity.getMasterQuotationId());
		if (masterValuationMatrixEntity.isPresent()) {
			StagingPolicyValuationMatrixEntity stagingPolicyValuationMatrixEntity = PolicyValuationMatrixHelper
					.masterQuotationentityToPolicyStagingEntity(masterValuationMatrixEntity.get());
			stagingPolicyValuationMatrixEntity.setId(null);
			stagingPolicyValuationMatrixEntity.setPolicyId(stagingPolicyEntity.getId());
			stagingPolicyValuationMatrixRepository.save(stagingPolicyValuationMatrixEntity);

			if (stagingPolicyValuationMatrixEntity.getValuationTypeId() == 1) {
				Optional<MasterValuationBasicEntity> masterValuationBasic = masterValuationBasicRepository
						.findByQuotationId(stagingPolicyEntity.getMasterQuotationId());
				if (masterValuationBasic.isPresent()) {
					StagingPolicyValutationBasicEntity stagingPolicyValutationBasicEntity = PolicyValuationMatrixHelper
							.masterToStagingDto(masterValuationBasic.get());
					stagingPolicyValutationBasicEntity.setId(null);
					stagingPolicyValutationBasicEntity.setPolicyId(stagingPolicyEntity.getId());
					stagingPolicyValuationBasicRepository.save(stagingPolicyValutationBasicEntity);

					// Copy to Master ValuationWithdrawalRate table
					List<MasterValuationWithdrawalRateEntity> masterValuationWithdrawalRateEntity = masterValuationWithdrawalRateRepository
							.findByQuotationId(stagingPolicyEntity.getMasterQuotationId());
					if (masterValuationWithdrawalRateEntity.size() > 0) {
						List<StagingPolicyValuationWithdrawalRateEntity> stagingPolicyValuationWithdrawalRateEntity = new ArrayList<StagingPolicyValuationWithdrawalRateEntity>();
						for (MasterValuationWithdrawalRateEntity getMasterValuationWithdrawalRateEntity : masterValuationWithdrawalRateEntity) {
							StagingPolicyValuationWithdrawalRateEntity newStagingPolicyValuationWithdrawalRateEntity = PolicyValuationMatrixHelper
									.masterWntityToPolicyEntity(getMasterValuationWithdrawalRateEntity);
							newStagingPolicyValuationWithdrawalRateEntity.setId(null);
							newStagingPolicyValuationWithdrawalRateEntity.setPolicyId(stagingPolicyEntity.getId());
							stagingPolicyValuationWithdrawalRateEntity
									.add(newStagingPolicyValuationWithdrawalRateEntity);
						}
						stagingValuationWithdrawaleRateRepository.saveAll(stagingPolicyValuationWithdrawalRateEntity);
					}
				}
			}

		}

		Optional<MasterValuationEntity> masterValuationEntity = masterValuationRepository
				.findByQuotationId(stagingPolicyEntity.getMasterQuotationId());
		if (masterValuationEntity.isPresent()) {
			PolicyValuationEntity policyValuationEntity = PolicyValuationHelper
					.masterQuotationentityToPolicyStagingEntity(masterValuationEntity.get());
			policyValuationEntity.setId(null);
			policyValuationEntity.setPolicyId(stagingPolicyEntity.getId());
			policyValuationRepository.save(policyValuationEntity);
		}
		return stagingPolicyEntity;
	}

	@Override
	public ApiResponseDto<PolicyDto> submitForApproval(Long id, String username) {
		StagingPolicyEntity policyEntity = stagingPolicyRepository.findById(id).get();
		policyEntity.setPolicyStatusId(Long.parseLong(submittedForApprovalStatudId));
		policyEntity.setPolicySubStatusId(Long.parseLong(defaultSubStatusId));
		policyEntity.setModifiedBy(username);
		policyEntity.setModifiedDate(new Date());
		
		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(policyEntity.getProposalNumber());
		if(taskallocationentity !=null) {
		taskallocationentity.setTaskStatus(defaultSubStatusId);
		taskAllocationRepository.save(taskallocationentity);
		}
		return ApiResponseDto.success(PolicyHelper.entityToDto(stagingPolicyRepository.save(policyEntity)));
	}

	@Override
	public ApiResponseDto<PolicyDto> sendBackToMaker(Long id, String username) {
		StagingPolicyEntity policyEntity = stagingPolicyRepository.findById(id).get();
		policyEntity.setPolicySubStatusId(Long.parseLong(sentBackToMakerSubStatudId));
		policyEntity.setModifiedBy(username);
		policyEntity.setModifiedDate(new Date());
		
		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(policyEntity.getProposalNumber());
		if(taskallocationentity !=null) {
			taskallocationentity.setTaskStatus(defaultSubStatusId);
			taskAllocationRepository.save(taskallocationentity);
			}
		
		return ApiResponseDto.success(PolicyHelper.entityToDto(stagingPolicyRepository.save(policyEntity)));
	}

	@Transactional
	@Override
	public ApiResponseDto<PolicyDto> approve(Long id, String username) {
		StagingPolicyEntity policyEntity = stagingPolicyRepository.findById(id).get();
		policyEntity.setPolicyStatusId(Long.parseLong(approvedStatudId));
		policyEntity.setPolicySubStatusId(Long.parseLong(approvedSubStatudId));
		policyEntity.setPolicyTaggedStatusId(Long.parseLong(existingTaggedStatusId));
		policyEntity.setModifiedBy(username);
		policyEntity.setModifiedDate(new Date());
		
		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(policyEntity.getProposalNumber());
		if(taskallocationentity !=null) {
			taskallocationentity.setTaskStatus(defaultSubStatusId);
			taskAllocationRepository.save(taskallocationentity);
			}

		// GEnerated PDF
		// Change a file Multi or directed
		String policyNumber = null;
//		 policyNumber = masterPolicyEntityRepository.getPolicyNumber(getPolicySeq());
		if (isDevEnvironment == false) {
			policyNumber = masterPolicyEntityRepository.getPolicyNumber(getPolicySeq());
		} else {
			policyNumber = getPolicySeq();
		}
		
		MasterPolicyEntity masterPolicyEntity = copyToMasterPolicy(id, username, policyNumber);
		policyEntity.setPolicyNumber(policyNumber);
		policyEntity.setMasterPolicyId(masterPolicyEntity.getId());
		stagingPolicyRepository.save(policyEntity);// get Policy Bond detail
		String getPolicyDetail = masterPolicyEntityRepository.getBondDetail(masterPolicyEntity.getId());

		if (getPolicyDetail != null) {

			String[] get = getPolicyDetail.toString().split(",");

			JsonNode mphBasic = null;
			JsonNode mphAdds = null;
			JsonNode mphRep = null;
			//
			try {
				URL url = new URL(endPoint
						+ "/api/proposal/getProposalDetailsByProposalNumber?proposalNumber=" + get[3]);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output = br.readLine();
				conn.disconnect();

				ObjectMapper mapper = new ObjectMapper();
				JsonFactory factory = mapper.getFactory();
				JsonParser parser = factory.createParser(output);
				JsonNode actualObj = mapper.readTree(parser);
			
				String getStatus=actualObj.path("responseData").toString();
			
				if (!getStatus.equals("null")) {
					mphBasic = actualObj.path("responseData").path("mphDetails").path("mphBasicDetails");
					mphAdds = actualObj.path("responseData").path("mphDetails").path("mphAddressDetails");
					mphRep = actualObj.path("responseData").path("mphDetails").path("mphChannelDetails");
				}		
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			//
			PolicyBondDetailDto policyBondDetailDto = new PolicyBondDetailDto();

			PolicyValutationBasicEntity policyValutationBasicEntity = policyValuationBasicRepository
					.findByPolicyId(masterPolicyEntity.getId()).get();
			Long getLCSumAssure = masterQuotationRepository.findByProposalNumberforPolicy(get[3]);
			policyBondDetailDto.setPolicyNumber(get[0]);

			policyBondDetailDto.setAnnualRenewalDate(get[1]);
			policyBondDetailDto.setTotalMember(Long.parseLong(get[2]));
			policyBondDetailDto.setProposalNumber(get[3]);
			policyBondDetailDto.setValuationEffectiveDate(get[4]);
		
			;
			policyBondDetailDto.setTotalSumAssured(getLCSumAssure);
			policyBondDetailDto.setRetirementAge(policyLifeCoverRepository.maxRetirement(masterPolicyEntity.getId()));
			policyBondDetailDto.setPolicyIssuanceDate(get[5]);
			policyBondDetailDto.setPaymentReceived(null);
			policyBondDetailDto.setMphEmail(null);
			if (mphRep != null) {
			policyBondDetailDto.setIntermediaryContactNo(mphRep.path("interme7diaryName").asLong());
			policyBondDetailDto.setIntermediaryName(mphRep.path("intermediaryName").textValue());
			policyBondDetailDto.setIntermediaryCode(mphRep.path("intermediaryCode").asLong());
			}
			if (mphBasic != null) {
			policyBondDetailDto.setUnitCode(mphBasic.path("unitCode").textValue());
			policyBondDetailDto.setProposalDate(mphBasic.path("proposalDate").textValue());
			policyBondDetailDto.setMphName(mphBasic.path("mphName").textValue());
			}
			
			if (mphAdds != null) {
				policyBondDetailDto.setMphAddress1(mphAdds.get(0).path("address1").textValue());
				policyBondDetailDto.setMphAddress2(mphAdds.get(0).path("address2").textValue());
				policyBondDetailDto.setMphAddress3(mphAdds.get(0).path("address3").textValue());
				policyBondDetailDto.setMphAddress3(mphAdds.get(0).path("district").textValue());
				policyBondDetailDto.setMphAddress3(mphAdds.get(0).path("state").textValue());
			}
			try {
				byte[] pdfDocument = PolicyHelper.GetpolicyDocument(policyBondDetailDto);

				System.out.println("Blog Start" + pdfDocument);
				DocumentStorageEntity documentStorage = new DocumentStorageEntity();
				documentStorage.setCreatedBy(username);
				documentStorage.setCreatedDate(new Date());
				documentStorage.setDocBlob(pdfDocument);
				documentStorage.setActive(true);
				documentStorage.setPolicyid(masterPolicyEntity.getId());
				documentStorageRepository.save(documentStorage);
				System.out.println(documentStorage.getDocBlob());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (isDevEnvironment == false) {
				String prodAndVarientCodeSame=	commonModuleService.getProductCode(policyEntity.getProductId());
				String unitStateName = commonMasterUnitRepository.getStateNameByUnitCode(masterPolicyEntity.getUnitCode());
				String unitStateType = commonMasterStateRepository.getStateType(unitStateName);
				String unitStateCode = commonMasterStateRepository.getStateCode(unitStateName);
				
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
						mphStateCode = commonMasterStateRepository.getStateCode(getMPHAddressEntity.getStateName());
						break;
					}
				}

				PolicyContributionDetailEntity policyContributionDetailEntity = policyContributionDetailRepository
						.findBymasterQuotationId1(policyEntity.getMasterQuotationId());	
				
				MasterPolicyContributionDetails masterPolicyContributionDetails =masterPolicyContributionDetailRepository.findBymasterPolicyId(masterPolicyEntity.getId());


				HSNCodeDto hSNCodeDto = accountingService.getHSNCode();
				Map<String, Double> gstComponents = accountingService.getGstComponents(unitStateType, mPHStateType, hSNCodeDto, policyContributionDetailEntity.getLifePremium());

				NewBusinessContributionAndLifeCoverAdjustmentDto newBusinessContributionAndLifeCoverAdjustmentDto = new NewBusinessContributionAndLifeCoverAdjustmentDto();
				newBusinessContributionAndLifeCoverAdjustmentDto.setAdjustmentAmount(policyContributionDetailEntity.getLifePremium() 
						+ policyContributionDetailEntity.getGst() + policyContributionDetailEntity.getCurrentServices() 
						+ policyContributionDetailEntity.getPastService());
				newBusinessContributionAndLifeCoverAdjustmentDto.setAdjustmentNo(policyContributionDetailEntity.getId().intValue());
				newBusinessContributionAndLifeCoverAdjustmentDto.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
				newBusinessContributionAndLifeCoverAdjustmentDto.setIsGstApplicable(false);
				if(masterPolicyEntity.getGstApplicableId() == 1l)
					 newBusinessContributionAndLifeCoverAdjustmentDto.setIsGstApplicable(true);	
				newBusinessContributionAndLifeCoverAdjustmentDto.setMphCode(getMPHEntity.getMphCode());
				newBusinessContributionAndLifeCoverAdjustmentDto.setUnitCode(masterPolicyEntity.getUnitCode());
				newBusinessContributionAndLifeCoverAdjustmentDto.setUserCode(username);
			
				newBusinessContributionAndLifeCoverAdjustmentDto.setGlTransactionModel(accountingService.getGlTransactionModel(masterPolicyEntity.getProductId(),masterPolicyEntity.getProductVariantId(),masterPolicyEntity.getUnitCode(), "GratuityPolicyApproval"));
				
				
				ResponseDto responseDto = commonmasterserviceUnitByCode(masterPolicyEntity.getUnitCode());
				GstDetailModelDto gstDetailModelDto=new GstDetailModelDto();
				gstDetailModelDto.setAmountWithTax(policyContributionDetailEntity.getLifePremium() 
						+ policyContributionDetailEntity.getGst() + policyContributionDetailEntity.getCurrentServices() 
						+ policyContributionDetailEntity.getPastService().doubleValue());
				gstDetailModelDto.setAmountWithoutTax(policyContributionDetailEntity.getLifePremium() 
						+ policyContributionDetailEntity.getCurrentServices() + policyContributionDetailEntity.getPastService().doubleValue());
				gstDetailModelDto.setCessAmount(0.0); //from Docu
				gstDetailModelDto.setCgstAmount(gstComponents.get("CGST"));
				gstDetailModelDto.setCgstRate(hSNCodeDto.getCgstRate());
				gstDetailModelDto.setCreatedBy(username);
				gstDetailModelDto.setCreatedDate(new Date());
				gstDetailModelDto.setEffectiveEndDate(""); //form docu
				gstDetailModelDto.setEffectiveStartDate(new Date());			
				gstDetailModelDto.setEntryType("RE");
				gstDetailModelDto.setFromGstn(getMPHEntity.getGstIn() ==null?"":  getMPHEntity.getGstIn());
				gstDetailModelDto.setFromPan(getMPHEntity.getPan()==null?"":getMPHEntity.getPan());
				gstDetailModelDto.setFromStateCode(mphStateCode ==null?"":mphStateCode); //from MPH detail null
				
				if(masterPolicyEntity.getGstApplicableId() == 1l)
					 gstDetailModelDto.setGstApplicableType("Taxable");
				else
					 gstDetailModelDto.setGstApplicableType("Non-Taxable");
				
				gstDetailModelDto.setGstInvoiceNo(""); //From Docu
				gstDetailModelDto.setGstRate((hSNCodeDto.getCgstRate() + hSNCodeDto.getSgstRate() + hSNCodeDto.getUtgstRate() + hSNCodeDto.getIgstRate()));
				gstDetailModelDto.setGstRefNo("2301212");//From Docu
				gstDetailModelDto.setGstRefTransactionNo("9012371");//From Docu
				gstDetailModelDto.setGstTransactionType("K");//From Docu
				gstDetailModelDto.setGstType("K");//From Docu
				gstDetailModelDto.setHsnCode(hSNCodeDto.getHsnCode());
				gstDetailModelDto.setIgstAmount(gstComponents.get("IGST"));
				gstDetailModelDto.setIgstRate(hSNCodeDto.getIgstRate());
				gstDetailModelDto.setModifiedBy(0L); //from docu
				gstDetailModelDto.setModifiedDate(new Date()); //from Docu
				gstDetailModelDto.setMphAddress(mPHAddress);
				gstDetailModelDto.setMphName(getMPHEntity.getMphName());
				gstDetailModelDto.setNatureOfTransaction("Gratuity NB");
				gstDetailModelDto.setOldInvoiceDate(new Date()); //From Docu
				gstDetailModelDto.setOldInvoiceNo("IN20123QE"); //From Docu
				gstDetailModelDto.setProductCode(prodAndVarientCodeSame);
				gstDetailModelDto.setRemarks("Gratuity NB Deposit Adjustment");
				gstDetailModelDto.setSgstAmount(gstComponents.get("SGST"));
				gstDetailModelDto.setSgstRate(hSNCodeDto.getSgstRate());
				gstDetailModelDto.setToGstIn(responseDto.getGstIn()==null?"8347":responseDto.getGstIn());  //From Docu from get Common Module
				gstDetailModelDto.setToPan(responseDto.getPan());
				gstDetailModelDto.setToStateCode(unitStateCode);
				gstDetailModelDto.setTotalGstAmount(policyContributionDetailEntity.getGst().doubleValue());
				gstDetailModelDto.setTransactionDate(new Date());
				gstDetailModelDto.setTransactionSubType("A"); //From Docu
				gstDetailModelDto.setTransactionType("C"); //From Docu
//				gstDetailModelDto.setUserCode(username);
				gstDetailModelDto.setUtgstAmount(gstComponents.get("UTGST"));
			
				gstDetailModelDto.setUtgstRate(hSNCodeDto.getUtgstRate());
				gstDetailModelDto.setVariantCode(prodAndVarientCodeSame);
				gstDetailModelDto.setYear(DateUtils.uniqueNoYYYY());
				gstDetailModelDto.setMonth(DateUtils.currentMonthName());
				newBusinessContributionAndLifeCoverAdjustmentDto.setGstDetailModel(gstDetailModelDto);
				
				JournalVoucherDetailModelDto journalVoucherDetailModelDto =new JournalVoucherDetailModelDto();
				journalVoucherDetailModelDto.setLineOfBusiness(masterPolicyEntity.getLineOfBusiness());
				journalVoucherDetailModelDto.setProduct(prodAndVarientCodeSame);
				journalVoucherDetailModelDto.setProductVariant(prodAndVarientCodeSame);
				newBusinessContributionAndLifeCoverAdjustmentDto.setJournalVoucherDetailModel(journalVoucherDetailModelDto);
				
				List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository.findBypolicyId(policyEntity.getId());
				List<NewBusinessContriDebitCreditRequestModelDto> getNewBusinessContriDebitCreditRequestModelDto =new ArrayList<NewBusinessContriDebitCreditRequestModelDto>();
				Double premiumAdjustment = policyContributionDetailEntity.getLifePremium().doubleValue();
				Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();
				Double currentServiceAdjusted = policyContributionDetailEntity.getCurrentServices().doubleValue();
				Double pastServiceAdjusted = policyContributionDetailEntity.getPastService().doubleValue();
				for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {
					NewBusinessContriDebitCreditRequestModelDto newBusinessContriDebitCreditRequestModelDto=new NewBusinessContriDebitCreditRequestModelDto();
					newBusinessContriDebitCreditRequestModelDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
					Double availableAmount = policyDepositEntity.getAvailableAmount().doubleValue();
					Double depositDebitAmount = 0.0;
					
					if (premiumAdjustment > 0 && availableAmount > 0) {
						if (policyContributionDetailEntity.getLifePremium() <= policyDepositEntity.getAvailableAmount()) {
							newBusinessContriDebitCreditRequestModelDto.setOYRGTAFirstPremiumCreditAmount(policyContributionDetailEntity.getLifePremium());
			availableAmount -= policyContributionDetailEntity.getLifePremium();
							depositDebitAmount += policyContributionDetailEntity.getLifePremium();
						} else {
							newBusinessContriDebitCreditRequestModelDto.setOYRGTAFirstPremiumCreditAmount(policyDepositEntity.getAvailableAmount());

							premiumAdjustment -= policyDepositEntity.getAvailableAmount();
							availableAmount -= policyDepositEntity.getAvailableAmount();
							depositDebitAmount += policyDepositEntity.getAvailableAmount();
						}
					}
					
					if (gstOnPremiumAdjusted > 0 && availableAmount > 0) {
						
						
						if (policyContributionDetailEntity.getGst() <= policyDepositEntity.getAvailableAmount()) {
							newBusinessContriDebitCreditRequestModelDto.setGstOnPremiumCreditAmount(policyContributionDetailEntity.getGst());
							
							availableAmount -= policyContributionDetailEntity.getGst();
							depositDebitAmount += policyContributionDetailEntity.getGst();
						} else {
							newBusinessContriDebitCreditRequestModelDto.setGstOnPremiumCreditAmount(policyDepositEntity.getAvailableAmount());
						
							gstOnPremiumAdjusted -= policyDepositEntity.getAvailableAmount();
							availableAmount -= policyDepositEntity.getAvailableAmount();
							depositDebitAmount += policyDepositEntity.getAvailableAmount();
						}
					}
				
					if (currentServiceAdjusted > 0 && availableAmount > 0) {
						if (policyContributionDetailEntity.getPastService() <= policyDepositEntity.getAvailableAmount()) {
							newBusinessContriDebitCreditRequestModelDto.setFirstPremiumCreditAmount(policyContributionDetailEntity.getPastService());
							availableAmount -= policyContributionDetailEntity.getPastService();
							depositDebitAmount += policyContributionDetailEntity.getPastService();
						} else {
							newBusinessContriDebitCreditRequestModelDto.setFirstPremiumCreditAmount(policyDepositEntity.getAvailableAmount());
							currentServiceAdjusted -= policyDepositEntity.getAvailableAmount();
							availableAmount -= policyDepositEntity.getAvailableAmount();
							depositDebitAmount += policyDepositEntity.getAvailableAmount();
						}
					}
					
					if (pastServiceAdjusted > 0 && availableAmount > 0) {
						if (policyContributionDetailEntity.getCurrentServices() <= policyDepositEntity.getAvailableAmount()) {
							newBusinessContriDebitCreditRequestModelDto.setSinglePremiumFirstYearCreditAmount(policyContributionDetailEntity.getCurrentServices());
							availableAmount -= policyContributionDetailEntity.getCurrentServices();
							depositDebitAmount += policyContributionDetailEntity.getCurrentServices();
						} else {
							newBusinessContriDebitCreditRequestModelDto.setSinglePremiumFirstYearCreditAmount(policyDepositEntity.getAvailableAmount());
							pastServiceAdjusted -= policyDepositEntity.getAvailableAmount();
							availableAmount -= policyDepositEntity.getAvailableAmount();
							depositDebitAmount += policyDepositEntity.getAvailableAmount();
						}
					}
					
					newBusinessContriDebitCreditRequestModelDto.setDepositDebitAmount(depositDebitAmount);
					getNewBusinessContriDebitCreditRequestModelDto.add(newBusinessContriDebitCreditRequestModelDto);
					
					}
				
				newBusinessContributionAndLifeCoverAdjustmentDto.setNewBusinessContriDebitCreditRequestModel(getNewBusinessContriDebitCreditRequestModelDto);
				newBusinessContributionAndLifeCoverAdjustmentDto.setRefNo(masterPolicyContributionDetails.getId().toString());
				accountingService.consumeDeposits(newBusinessContributionAndLifeCoverAdjustmentDto,masterPolicyEntity.getId());

				IssuePolicyDto issuePolicyDto = new IssuePolicyDto();
				issuePolicyDto.setProposalNo(policyEntity.getProposalNumber());
				issuePolicyDto.setPolicyNo(masterPolicyEntity.getPolicyNumber());
				issuePolicyDto.setVariantCode(prodAndVarientCodeSame);
				issuePolicyDto.setProductCode(prodAndVarientCodeSame);
				accountingService.policyIssuance(issuePolicyDto);
			}
		}

		return ApiResponseDto.success(PolicyHelper.entityToDto(masterPolicyEntity));
	}
	
	public ResponseDto commonmasterserviceUnitByCode(String unitCode) {
		ResponseDto responseDto=null;
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

	public synchronized String getPolicySeq() {
		return commonService.getSequence(HttpConstants.POLICY_MODULE);
	}

	public synchronized String getSequence(String type) {
		logger.info("CommonSequenceService-getSequence-Start");
		try {
			return masterPolicyEntityRepository.getSequence(type);
		} catch (NonUniqueResultException e) {
			logger.error("CommonSequenceService-getSequence-Error:");
		}
		logger.info("CommonSequenceService-getSequence-End");
		return null;
	}

	private MasterPolicyEntity copyToMasterPolicy(Long id, String username, String policyNumber) {
		StagingPolicyEntity stagingPolicyEntity = stagingPolicyRepository.findById(id).get();
		MPHEntity mPHEntity = MPHHelper.entitytostagetomaster(
				stagingMPHRepository.findById(stagingPolicyEntity.getMasterpolicyHolder()).get());
		MPHEntity newMPHEntity = mPHRepository.save(mPHEntity);

		MasterPolicyEntity masterPolicyEntity = masterPolicyEntityRepository
				.save(PolicyHelper.stagingPolicyEntityToMasterPolicyEntity(stagingPolicyEntity, id, username,
						newMPHEntity.getId(), policyNumber));

		// Copy to Master Policy SchmeRule table
		Optional<StagingPolicySchemeRule> stagingPolicySchemeRule = stagingPolicySchemeRuleRepository
				.findBypolicyId(id);
		if (stagingPolicySchemeRule.isPresent()) {
			PolicySchemeEntity masterSchemeRuleEntity = PolicySchemeRuleHelper
					.stgPolicySchemeRuleToMasterEntity(stagingPolicySchemeRule.get());
			masterSchemeRuleEntity.setId(null);
			masterSchemeRuleEntity.setPolicyId(masterPolicyEntity.getId());
			policySchemeRuleRepository.save(masterSchemeRuleEntity);
		}

		// Copy to Master LifeCoverFlat table
		List<StagingPolicyLifeCoverEntity> lifeCoverEntities = stagingPolicyLifeCoverRepository.findByPolicyId(id);
		if (lifeCoverEntities.size() > 0) {
			List<PolicyLifeCoverEntity> policyLifeCoverEntities = new ArrayList<PolicyLifeCoverEntity>();
			for (StagingPolicyLifeCoverEntity stagingLifeCoverEntity : lifeCoverEntities) {
				PolicyLifeCoverEntity masterLifeCoverEntity = PolicyLifeCoverHelper
						.stgPolicyLifeCoverToMasterEntity(stagingLifeCoverEntity);
				masterLifeCoverEntity.setId(null);
				masterLifeCoverEntity.setPolicyId(masterPolicyEntity.getId());
				policyLifeCoverEntities.add(masterLifeCoverEntity);
			}
			policyLifeCoverRepository.saveAll(policyLifeCoverEntities);
		}

		// MemberCategoryTest
		List<MemberCategoryEntity> addMemberCategoryEntity = new ArrayList<MemberCategoryEntity>();
		List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository.findBypstgPolicyId(id);
		for (MemberCategoryEntity getmemberCategoryEntity : memberCategoryEntity) {
			getmemberCategoryEntity.setPmstPolicyId(masterPolicyEntity.getId());
			addMemberCategoryEntity.add(getmemberCategoryEntity);
		}
		memberCategoryRepository.saveAll(addMemberCategoryEntity);

		// Copy to MasterPolicycontributionDetails
				MasterPolicyContributionDetails copyToMaster = PolicyHelper.entityToMaster(
						policyContributionDetailRepository.findBymasterQuotationId1(stagingPolicyEntity.getMasterQuotationId()),
						stagingPolicyEntity.getId(), masterPolicyEntity.getId());

				copyToMaster=masterPolicyContributionDetailRepository.save(copyToMaster);

		// Copy to Master GratuityBenefit table
		List<StagingPolicyGratuityBenefitEntity> stagingPolicyGratuityBenefitEntities = stagingPolicyGratuityBenefitRepository
				.findByPolicyId(id);
		if (stagingPolicyGratuityBenefitEntities.size() > 0) {
			List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = new ArrayList<PolicyGratuityBenefitEntity>();
			for (StagingPolicyGratuityBenefitEntity stagingPolicyGratuityBenefitEntity : stagingPolicyGratuityBenefitEntities) {
				policyGratuityBenefitEntity.add(PolicyGratuityBenefitHelper.stgPolicyGratBenefToMasterEntity(
						stagingPolicyGratuityBenefitEntity, masterPolicyEntity.getId()));
			}
			policyGratuityBenefitRepository.saveAll(policyGratuityBenefitEntity);
		}

		// copy Deposit to Master data
		List<MasterPolicyDepositEntity> masterPolicyDepositEntity = PolicyHelper
				.entityToPolicyMater(policyDepositRepository.findBypolicyId(id), masterPolicyEntity.getId(),copyToMaster,username);
		masterPolicyDepositRepository.saveAll(masterPolicyDepositEntity);

		// copy Deposit to MasterPolicyContributionEntity
		List<MasterPolicyContributionEntity> masterPolicyContributionEntity = PolicyHelper
				.entityToPolicyConMaster(policyContributionRepository.findBypolicyId(id), masterPolicyEntity.getId(),copyToMaster,username);
		masterPolicyContributionRepository.saveAll(masterPolicyContributionEntity);

		// copy Deposit to masterPolicyContrySummaryEntity
		List<MasterPolicyContrySummaryEntity> masterPolicyContrySummaryEntity = PolicyHelper
				.entityToPolicySummaryMaster(policyContrySummaryRepository.findBypolicyId(id),
						masterPolicyEntity.getId(),copyToMaster,username);
		masterPolicyContrySummaryRepository.saveAll(masterPolicyContrySummaryEntity);

		// Copy to Master Member data
		List<PolicyMemberEntity> tt = PolicyMemberHelper
				.entityTopolicyMasterEntity(stagingPolicyMemberRepository.findByPolicyId(id), masterPolicyEntity);
		policyMemberRepository.saveAll(tt);

		// copy to master Valuation Entity
		Optional<StagingPolicyValuationMatrixEntity> masterValuationMatrixEntity = stagingPolicyValuationMatrixRepository
				.findByPolicyId(id);
		if (masterValuationMatrixEntity.isPresent()) {
			PolicyValuationMatrixEntity stagingPolicyValuationEntity = PolicyValuationMatrixHelper
					.policyValuationEntityToMasterEntity(masterValuationMatrixEntity.get());
			stagingPolicyValuationEntity.setId(null);
			stagingPolicyValuationEntity.setPolicyId(masterPolicyEntity.getId());
			policyValuationMatrixRepository.save(stagingPolicyValuationEntity);

			if (stagingPolicyValuationEntity.getValuationTypeId() == 1) {
				Optional<StagingPolicyValutationBasicEntity> stagingPolicyValuationBasicEntity = stagingPolicyValuationBasicRepository
						.findByPolicyId(id);
				if (stagingPolicyValuationBasicEntity.isPresent()) {
					PolicyValutationBasicEntity policyValutationBasicEntity = PolicyValuationMatrixHelper
							.policyValuationstagingToMasterDto(stagingPolicyValuationBasicEntity.get());
					
					policyValutationBasicEntity.setId(null);
					policyValutationBasicEntity.setPolicyId(masterPolicyEntity.getId());
					policyValutationBasicEntity.setAnnualRenewalDate(masterPolicyEntity.getAnnualRenewlDate());
					policyValutationBasicEntity.setTotalPremium(stagingPolicyValuationEntity.getTotalPremium());
					policyValutationBasicEntity.setTotalSumAssured(stagingPolicyValuationEntity.getTotalSumAssured());					policyValuationBasicRepository.save(policyValutationBasicEntity);
					// Copy to Master ValuationWithdrawalRate table
					List<StagingPolicyValuationWithdrawalRateEntity> stagingPolicyValuationWithdrawalRateEntity = stagingValuationWithdrawaleRateRepository
							.findByPolicyId(id);
					if (stagingPolicyValuationWithdrawalRateEntity.size() > 0) {
						List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = new ArrayList<PolicyValuationWithdrawalRateEntity>();
						for (StagingPolicyValuationWithdrawalRateEntity getStagingPolicyValuationWithdrawalRateEntity : stagingPolicyValuationWithdrawalRateEntity) {
							PolicyValuationWithdrawalRateEntity newPolicyValuationWithdrawalRateEntity = PolicyValuationMatrixHelper
									.stagingTomasterEntity(getStagingPolicyValuationWithdrawalRateEntity);
							newPolicyValuationWithdrawalRateEntity.setId(null);
							newPolicyValuationWithdrawalRateEntity.setPolicyId(masterPolicyEntity.getId());
							policyValuationWithdrawalRateEntity.add(newPolicyValuationWithdrawalRateEntity);
						}
						policyValuationWithdrawalRateRepository.saveAll(policyValuationWithdrawalRateEntity);
					}
				}
			}
		}

		Optional<PolicyValuationEntity> policyValuationEntity = policyValuationRepository.findByPolicyId(id);
		if (policyValuationEntity.isPresent()) {
			PolicyMasterValuationEntity policyMasterValuationEntity = PolicyValuationHelper
					.policyValuationEntityToMasterEntity(policyValuationEntity.get());
			policyMasterValuationEntity.setId(null);
			policyMasterValuationEntity.setPolicyId(masterPolicyEntity.getId());
			policyMasterValuationRepository.save(policyMasterValuationEntity);
		}
		return masterPolicyEntity;
	}
	
	//policy running statement
	
	@Override
	public ApiResponseDto<List<ContributionResponseDto>> getPolicyContributionDetails(
			ContributionRequestDto contributionRequestDto) {
		logger.info("PolicyServiceImpl:getPolicyContributionDetails---Started");

		List<ContributionResponseDto> contributionResponseDtoList = new ArrayList<>();
		try {
			MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(contributionRequestDto.getPolicyId());
			List<Object[]> contributionDetails = masterPolicyContributionRepository.getPolicyContributionDetails(
					contributionRequestDto.getPolicyId(), contributionRequestDto.getFinancialYear(),
					contributionRequestDto.getQuarter(),1, masterPolicyEntity.getProductVariantId());
			if (!contributionDetails.isEmpty()) {
				for (Object[] object : contributionDetails) {
					ContributionResponseDto newContributionResponseDto = new ContributionResponseDto();
					newContributionResponseDto.setSNo(String.valueOf(object[0]));
					newContributionResponseDto.setContributionId(object[1].toString());
					newContributionResponseDto
							.setDate(DateUtils.convertStringToDate2(String.valueOf(object[2])));
					newContributionResponseDto.setType(String.valueOf(object[3]));
					newContributionResponseDto.setEmployeeContribution(String.valueOf(object[4]));
					newContributionResponseDto.setEmployerContribution(String.valueOf(object[5]));
					newContributionResponseDto.setVoluntaryContribution(String.valueOf(object[6]));
					newContributionResponseDto.setTotalAmount(String.valueOf(object[7]));
					newContributionResponseDto.setClosingBalance(String.valueOf(object[8]));
					contributionResponseDtoList.add(newContributionResponseDto);
				}

			} else {
				return ApiResponseDto.errorMessage(null, HttpConstants.BAD_REQUEST, "No Record Found");
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- getPolicyContributionDetails-- getPolicyMember--", e);
			return ApiResponseDto.errorMessage(null, HttpConstants.SERVER_EXCEPTION, e.getMessage());
		}
		logger.info("PolicyServiceImpl:getPolicyContributionDetails--Ends");
		return ApiResponseDto.success(contributionResponseDtoList);
	}

	
	//End

	@Override
	public ApiResponseDto<PolicyDto> reject(Long id, String username,PolicyDto policyDto) {
		
	List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository.findBypolicyId(id);
	
	StagingPolicyEntity policyEntity = stagingPolicyRepository.findById(id).get();
	
		if (isDevEnvironment == false) {
			String prodAndVarientCodeSame=	commonModuleService.getProductCode(policyEntity.getProductId());
			List<UnlockDepositDetailDto> showDepositLockDto =new ArrayList<UnlockDepositDetailDto>();

			for(PolicyDepositEntity policyDepositEntity: policyDepositEntities) {
				
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
		policyEntity.setRejectedReason(policyDto.getRejectedReason());
		policyEntity.setRejectedRemarks(policyDto.getRejectedRemarks());	
		policyEntity.setPolicyStatusId(Long.parseLong(rejectedStatudId));
		policyEntity.setPolicySubStatusId(Long.parseLong(rejectedSubStatudId));
		policyEntity.setPolicyTaggedStatusId(Long.parseLong(existingTaggedStatusId));
		policyEntity.setModifiedBy(username);
		policyEntity.setModifiedDate(new Date());
		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(policyEntity.getProposalNumber());
		if(taskallocationentity !=null) {
			taskallocationentity.setTaskStatus(defaultSubStatusId);
			taskAllocationRepository.save(taskallocationentity);
			}
		return ApiResponseDto.success(PolicyHelper.entityToDto(stagingPolicyRepository.save(policyEntity)));
	}

	
	@Override
	public ApiResponseDto<PolicyDto> escalateToCo(Long id, String username) {
		StagingPolicyEntity policyEntity = stagingPolicyRepository.findById(id).get();
		policyEntity.setIsEscalatedToCo(true);
		policyEntity.setModifiedBy(username);
		policyEntity.setModifiedDate(new Date());
		return ApiResponseDto.success(PolicyHelper.entityToDto(stagingPolicyRepository.save(policyEntity)));
	}

	@Override
	public ApiResponseDto<PolicyDto> escalateToZo(Long id, String username) {
		StagingPolicyEntity policyEntity = stagingPolicyRepository.findById(id).get();
		policyEntity.setIsEscalatedToZo(true);
		policyEntity.setModifiedBy(username);
		policyEntity.setModifiedDate(new Date());
		return ApiResponseDto.success(PolicyHelper.entityToDto(stagingPolicyRepository.save(policyEntity)));
	}

	@Override
	public ApiResponseDto<List<QuotationChargeDto>> findCharges(Long id) {
		List<QuotationChargeEntity> quotationChargeEntities = quotationChargeRepository.findAllByMasterQuotationId(id);
		List<QuotationChargeEntity> getAllQuotationChargeEntity = new ArrayList<QuotationChargeEntity>();
		for (QuotationChargeEntity getQuotationChargeEntity : quotationChargeEntities) {
			Long getAmount = quotationChargeRepository.SumAmountCharged(getQuotationChargeEntity.getId());
			getQuotationChargeEntity.setAmountCharged(getAmount);
			getAllQuotationChargeEntity.add(getQuotationChargeEntity);
		}
		return ApiResponseDto.success(getAllQuotationChargeEntity.stream().map(QuotationChargeHelper::entityToDto)
				.collect(Collectors.toList()));
	}

	@Override
	public byte[] findByPolicyId(Long policyId,Long taggedStatusId) {
		List<Object[]> policyMasterData =null;
		if(taggedStatusId == 139 ) {
				policyMasterData= memberHelper.getPolicyMasterDataForExcel(policyId);
		}
		if(taggedStatusId == 138) {
			policyMasterData= memberHelper.getPolicyStagingDataForExcel(policyId);
		}
		
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
	public byte[] findByquotationId(Long quotationId,Long taggedStatusId) {
		List<Object[]> quotationMasterData = null;
		if(taggedStatusId == 79){
			quotationMasterData= memberHelper.getQuotationMasterDataForExcel(quotationId);
		}
		if(taggedStatusId == 78) {
			quotationMasterData= memberHelper.getQuotationStagingDataForExcel(quotationId);
		}
		
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		MemberErrorWorkbookHelper memberErrorWorkbookHelper = new MemberErrorWorkbookHelper();
		memberErrorWorkbookHelper.createHeaderForCandBSheetquotation(workbook, sheet, false);
		memberErrorWorkbookHelper.createDetailRow(workbook, sheet, quotationMasterData);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			workbook.write(out);
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

//	r
	@Override
	public ApiResponseDto<PolicyContributionDetailDto> createContributionid(Long masterQuotationId,
			PolicyContributionDetailDto contributionDto) {

		PolicyContributionDetailEntity policyContributionDetailEntity = new ModelMapper().map(contributionDto,
				PolicyContributionDetailEntity.class);
		policyContributionDetailEntity.setMasterQuotationId(masterQuotationId);
		policyContributionDetailEntity.setCreatedBy(contributionDto.getCreatedBy());
		policyContributionDetailEntity.setCreatedDate(new Date());

		policyContributionDetailRepository.save(policyContributionDetailEntity);

		return ApiResponseDto.created(PolicyHelper
				.contributentitytoDto(policyContributionDetailRepository.save(policyContributionDetailEntity)));
	}

	@Override
	public ApiResponseDto<PolicyContributionDetailDto> createContributionTmpId(Long tmpPolicyId,
			PolicyContributionDetailDto contributionDto) {

		PolicyContributionDetailEntity policyContributionDetailEntity = new ModelMapper().map(contributionDto,
				PolicyContributionDetailEntity.class);
		policyContributionDetailEntity.setTmpPolicyId(tmpPolicyId);
		policyContributionDetailEntity.setCreatedBy(contributionDto.getCreatedBy());
		policyContributionDetailEntity.setCreatedDate(new Date());

		policyContributionDetailRepository.save(policyContributionDetailEntity);

		return ApiResponseDto.created(PolicyHelper
				.contributentitytoDto(policyContributionDetailRepository.save(policyContributionDetailEntity)));
	}

	@Override
	public ApiResponseDto<List<PolicyContributionDetailDto>> findContributionId(Long id) {
		List<PolicyContributionDetailEntity> policyContributionDetailEntity = policyContributionDetailRepository
				.findBymasterQuotationId(id);
		if (policyContributionDetailEntity.size() > 0) {
			return ApiResponseDto.success(
					policyContributionDetailEntity.stream().map(PolicyHelper::EntityTodo).collect(Collectors.toList()));
		} else {
			return ApiResponseDto.notFound(null);
		}
	}

	@Override
	public ApiResponseDto<PolicyContributionDetailDto> updateContributionid(Long id,
			PolicyContributionDetailDto contributionDto) {
		PolicyContributionDetailEntity policyContributionDetailEntity = policyContributionDetailRepository.findById(id)
				.get();
//		policyContributionDetailEntity.setMasterQuotationId(contributionDto.getMasterQuotationId());
		policyContributionDetailEntity.setTmpPolicyId(contributionDto.getTmpPolicyId());
		policyContributionDetailEntity.setLifePremium(contributionDto.getLifePremium());
		policyContributionDetailEntity.setPastService(contributionDto.getPastService());
		policyContributionDetailEntity.setGst(contributionDto.getGst());
		policyContributionDetailEntity.setModifiedBy(contributionDto.getModifiedBy());
		policyContributionDetailEntity.setModifiedDate(new Date());
		policyContributionDetailRepository.save(policyContributionDetailEntity);

		return ApiResponseDto.created(PolicyHelper
				.contributentitytoDto(policyContributionDetailRepository.save(policyContributionDetailEntity)));

	}

	@Override
	public ApiResponseDto<List<PolicyDepositdto>> getDepositData(String type, Long policyId) {
		if (type.equals("INPROGRESS")) {
			List<PolicyDepositEntity> policyDepositEntity = policyDepositRepository.findBypolicyId(policyId);
			return ApiResponseDto.success(
					policyDepositEntity.stream().map(PolicyHelper::EntityTodepositdto).collect(Collectors.toList()));
		} else {
			List<MasterPolicyDepositEntity> masterPolicyDepositEntity = masterPolicyDepositRepository
					.findBymasterPolicyId(policyId);
			return ApiResponseDto.success(masterPolicyDepositEntity.stream().map(PolicyHelper::masterEntityTodepositdto)
					.collect(Collectors.toList()));

		}
	}

	@Override
	public ApiResponseDto<DocumentStoragedto> uploadDocumentStorage(MultipartFile file)
			throws IllegalStateException, IOException {
		DocumentStorageEntity build = DocumentStorageEntity.builder()
				.docBlob(DocumentStorageHelp.compressImage(file.getBytes())).build();

		DocumentStorageEntity documentStorageEntity = new DocumentStorageEntity();
		build.setCreatedBy("maker");
		build.setCreatedDate(new Date());
		build.setActive(true);
//		documentStorageEntity.setDocBlob(DocumentStorageUtil.compressImage(file.getBytes()));
		documentStorageEntity = documentStorageRepository.save(build);
		DocumentStoragedto documentStoragedto = new ModelMapper().map(documentStorageEntity, DocumentStoragedto.class);
		return ApiResponseDto.created(documentStoragedto);
	}

	@Override
	public ApiResponseDto<PolicyBondDetailDto> getPolicyBondDetails(Long policyId) {

		String getPolicyDetail = masterPolicyEntityRepository.getBondDetail(policyId);

		String[] get = getPolicyDetail.toString().split(",");

		JsonNode mphBasic = null;
		JsonNode mphAdds = null;
		JsonNode mphRep = null;
		JsonNode mphcont = null;
		//
		try {
			URL url = new URL(endPoint + "/api/proposal/getProposalDetailsByProposalNumber?proposalNumber="
					+ get[3]);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);
			System.out.println(actualObj);

			mphBasic = actualObj.path("responseData").path("mphDetails").path("mphBasicDetails");
			mphAdds = actualObj.path("responseData").path("mphDetails").path("mphAddressDetails");
			mphRep = actualObj.path("responseData").path("mphDetails").path("mphChannelDetails");
			mphcont = actualObj.path("responseData").path("mphDetails").path("mphChannelDetails");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//

		Long getLCSumAssure = masterQuotationRepository.findByProposalNumberforPolicy(get[3]);
		CommonMasterUnitEntity pdfClaimForwardLetter = null;
		PolicyBondDetailDto policyBondDetailDto = PolicyHelper.getPolicyBondDtoDetail(get, mphBasic, mphAdds, mphRep,
				pdfClaimForwardLetter,mphcont, policyLifeCoverRepository.maxRetirement(policyId), getLCSumAssure);

		try {
			byte[] pdfDocument = PolicyHelper.GetpolicyDocument(policyBondDetailDto);

			System.out.println("Blog Start" + pdfDocument);
			DocumentStorageEntity documentStorage = new DocumentStorageEntity();
			documentStorage.setCreatedBy("maker");
			documentStorage.setCreatedDate(new Date());
			documentStorage.setDocBlob(pdfDocument);
			documentStorage.setActive(true);
			documentStorage.setPolicyid(policyId);
			documentStorageRepository.save(documentStorage);
		} catch (Exception e) {
			System.out.println(e);
		}
		return ApiResponseDto.success(policyBondDetailDto);

	}

	@Override
	public byte[] policybondpdf(Long id) {

		DocumentStorageEntity dbImage = documentStorageRepository.findBypolicyid(id);

		return dbImage.getDocBlob();

	}

	@Override
	public byte[] getPolicyMemberDtls(Long policyId) {

//	List<PolicyMemberEntity> policyMemberDetails  = policyMemberRepository.findBypolicyId(policyId);
		List<Object[]> memberData = memberHelper.getmemberExcelData(policyId);

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("MemberDetails");
		XSSFRow row = null;
		int rownum = 1;
		policyMemberWorkbookHelper.createHeader(workbook, sheet, true);
		policyMemberWorkbookHelper.createDetailRow(workbook, sheet, memberData);

		ByteArrayOutputStream file;
		try {
			file = new ByteArrayOutputStream();
			workbook.write(file);
			workbook.close();
			return file.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] downloadSample() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("MemberDetails");
		policyMemberWorkbookHelper.createHeader(workbook, sheet, true);

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
	public byte[] findBytmpPolicyId(Long policyId) {
		List<Object[]> renewalQuotationMasterData = memberHelper.getRenewalQuotationDataForExcel(policyId);
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		MemberErrorWorkbookHelper memberErrorWorkbookHelper = new MemberErrorWorkbookHelper();
		memberErrorWorkbookHelper.createHeaderForCandBSheet(workbook, sheet, false);
		memberErrorWorkbookHelper.createDetailRow(workbook, sheet, renewalQuotationMasterData);

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
	public byte[] getpolicyIdactive(Long policyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponseDto<List<AgeValuationReportDto>> getAgeReport(Long policyId) {
		List<AgeValuationReportDto> ageValuationReportDto = new ArrayList<AgeValuationReportDto>();
		List<Object[]> getAgeReport = policyRepository.getAgeReport(policyId);

		ageValuationReportDto = (List<AgeValuationReportDto>) PolicyHelper.valuationObjtoDto(getAgeReport);
		
		System.out.println("" + ageValuationReportDto);

		return ApiResponseDto.success(ageValuationReportDto);
	}
	
	
	@Override
	public ApiResponseDto<List<QuotationChargeDto>> renewalChargeDetail(Long tmpPolicyId) {
		List<QuotationChargeEntity> quotationChargeEntities = quotationChargeRepository
				.findAllByTmpPolicyId(tmpPolicyId);
		List<QuotationChargeEntity> getAllQuotationChargeEntity = new ArrayList<QuotationChargeEntity>();
		for (QuotationChargeEntity getQuotationChargeEntity : quotationChargeEntities) {
			Long getAmount = quotationChargeRepository.SumAmountCharged(getQuotationChargeEntity.getId());
			getQuotationChargeEntity.setAmountCharged(getAmount);
			getAllQuotationChargeEntity.add(getQuotationChargeEntity);
		}
		return ApiResponseDto.success(getAllQuotationChargeEntity.stream().map(QuotationChargeHelper::entityToDto)
				.collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<PolicyDepositdto>> getDepositTmpPolicy(String type, Long tmpPolicyId) {
		if (type.equals("INPROGRESS")) {
			List<PolicyDepositEntity> policyDepositEntity = policyDepositRepository.findBytmpPolicyId(tmpPolicyId);
			return ApiResponseDto.success(
					policyDepositEntity.stream().map(PolicyHelper::EntityTodepositdto).collect(Collectors.toList()));
		} else {
			List<MasterPolicyDepositEntity> masterPolicyDepositEntity = masterPolicyDepositRepository
					.findBytmpPolicyId(tmpPolicyId);
			return ApiResponseDto.success(masterPolicyDepositEntity.stream().map(PolicyHelper::masterEntityTodepositdto)
					.collect(Collectors.toList()));

		}

	}

	@Override
	public ApiResponseDto<List<PolicyContributionDetailDto>> findContributionTmpPolicyId(Long tempPolicyId) {
		List<PolicyContributionDetailEntity> policyContributionDetailEntity = policyContributionDetailRepository
				.findBytmpPolicyId(tempPolicyId);
		if (policyContributionDetailEntity.size() > 0) {
			return ApiResponseDto.success(
					policyContributionDetailEntity.stream().map(PolicyHelper::EntityTodo).collect(Collectors.toList()));
		} else {
			return ApiResponseDto.notFound(null);
		}
	}

	@Override
	public ApiResponseDto<List<PolicyContrySummarydto>> getSummaryContriDetail(Long policyId) {

		List<PolicyContrySummaryEntity> policyContrySummaryEntity = policyContrySummaryRepository
				.findBypolicyId(policyId);

		if (policyContrySummaryEntity.size() < 0) {
			return ApiResponseDto.success(null);
		}
		return ApiResponseDto.success(
				policyContrySummaryEntity.stream().map(PolicyHelper::EntityTosummarydto).collect(Collectors.toList()));

//			List<MasterPolicyDepositEntity> masterPolicyDepositEntity = masterPolicyDepositRepository
//					.findBymasterPolicyId(policyId);
//			return ApiResponseDto.success(masterPolicyDepositEntity.stream().map(PolicyHelper::masterEntityTodepositdto)
//					.collect(Collectors.toList()));

//	return ApiResponseDto.success(null);
	}

	@Override
	public ApiResponseDto<PolicyContrySummarydto> getSummaryContriDetailfield(Long policyId,String taggedStatus) {

		PolicyContrySummarydto policyContrySummarydto = null;

	if (taggedStatus.equalsIgnoreCase("139")) {
			MasterPolicyContrySummaryEntity masterpolicyContrySummaryEntity = masterPolicyContrySummaryRepository
					.findBypolicyId(policyId).get(0);
			policyContrySummarydto = PolicyHelper.EntityTosummarydto(masterpolicyContrySummaryEntity);	
		} else {
			PolicyContrySummaryEntity policyContrySummaryEntity = policyContrySummaryRepository.findBypolicyId(policyId)
					.get(0);
			policyContrySummarydto = PolicyHelper.EntityTosummarydto(policyContrySummaryEntity);
		}

		return ApiResponseDto.success(policyContrySummarydto);
	}


	//Renewal Premium Update
				@Transactional
				@Override
				public ApiResponseDto<RenewalPolicyTMPDto> updatePaymentAdjustmentforRenewal(
						PolicyContributionDetailDto policyContributionDetailDto) {
					Long masterTmpPolicyId = null;
					Double getBalance = null;
					Double adjustementAmount = 0.0;
					int year = 0;
					int month = 0;
					masterTmpPolicyId = policyContributionDetailDto.getTmpPolicyId();
					List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository
							.findBytmpPolicyId(masterTmpPolicyId);
					
					RenewalPolicyTMPEntity renewalPolicyTMPEntity=renewalPolicyTMPRepository.findById(masterTmpPolicyId).get();	
					
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
					policyContributionDetailEntity.setEntryType("RE");				

					policyContributionDetailEntity=policyContributionDetailRepository.save(policyContributionDetailEntity);

				

		

					
					
					List<PolicyDepositEntity> oldDepositList = policyDepositRepository.deleteBytmpPolicyId(masterTmpPolicyId);
					List<PolicyContributionEntity> oldPolicyContribution = policyContributionRepository
							.deleteBytmpPolicyId(masterTmpPolicyId);
					List<PolicyContrySummaryEntity> oldPolicyContriSummary = policyContrySummaryRepository
							.deleteBytmpPolicyId(masterTmpPolicyId);
				
				
					PolicyContrySummaryEntity policyContrySummaryEntity = new PolicyContrySummaryEntity();
					StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(standardcode).get();
					PolicyContributionDetailEntity getGstSC = policyContributionDetailRepository
							.findBymasterTmpPolicyId(masterTmpPolicyId);

					adjustementAmount = getGstSC.getGst() + getGstSC.getLifePremium() + getGstSC.getCurrentServices()
							+ getGstSC.getPastService();
					List<ShowDepositLockDto> showDepositLockDto=new ArrayList<ShowDepositLockDto>();
					for (DepositAdjustementDto getDepositAdjustementDto : policyContributionDetailDto
							.getDepositAdjustmentDto()) {
						if (adjustementAmount != 0) {
							masterTmpPolicyId = getDepositAdjustementDto.getTmpPolicyId();

							PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

							PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();

							if (adjustementAmount < getDepositAdjustementDto.getDepositAmount()) {
								getBalance = getDepositAdjustementDto.getDepositAmount() - adjustementAmount;
								policyDepositEntity.setAvailableAmount(getBalance);
								policyDepositEntity.setAdjustmentAmount(adjustementAmount);
								adjustementAmount = 0.0;
							} else {
								getBalance = adjustementAmount - getDepositAdjustementDto.getDepositAmount();
								policyDepositEntity.setAdjustmentAmount(getDepositAdjustementDto.getDepositAmount());
								policyDepositEntity.setAvailableAmount(0.0);
								adjustementAmount = adjustementAmount - getDepositAdjustementDto.getDepositAmount();
							}
							policyDepositEntity
									.setChequeRealistionDate(getDepositAdjustementDto.getChequeRealisationDate());
							policyDepositEntity.setActive(true);
							Random r = new Random(System.currentTimeMillis());
							int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
							policyDepositEntity.setChallanNo(String.valueOf(getCollectionNumber));
							policyDepositEntity.setChequeRealistionDate(new Date());
							policyDepositEntity.setCollectionDate(new Date());
							policyDepositEntity.setCreateDate(new Date());
							policyDepositEntity.setZeroId(null);
							policyDepositEntity.setTransactionMode(getDepositAdjustementDto.getCollectionMode());
							policyDepositEntity.setStatus(null);
							policyDepositEntity.setRegConId(null);
							policyDepositEntity.setTmpPolicyId(masterTmpPolicyId);
							policyDepositEntity.setContributionType("RE");
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
							
							policyContributionEntity.setContributionDetailId(policyContributionDetailEntity.getId());
							policyContributionEntity.setAdjConid(null);
							policyContributionEntity.setOpeningBalance(0.0);
							policyContributionEntity
									.setTotalContribution(getGstSC.getCurrentServices() + getGstSC.getPastService());
							policyContributionEntity.setClosingBlance(policyContributionEntity.getOpeningBalance()
									+ policyContributionEntity.getTotalContribution());
							policyContributionEntity
									.setContReferenceNo(Long.toString(policyDepositEntity.getCollectionNo()));
							policyContributionEntity.setContributionDate(policyDepositEntity.getCollectionDate());
							policyContributionEntity.setContributionType(null);
							policyContributionEntity.setCreatedBy(getDepositAdjustementDto.getCreatedBy());
							policyContributionEntity.setCreateDate(new Date());
							policyContributionEntity.setEmployeeContribution(0.0);
							policyContributionEntity
									.setEmployerContribution(getGstSC.getCurrentServices() + getGstSC.getPastService());
							year = Calendar.getInstance().get(Calendar.YEAR);

							month = Calendar.getInstance().get(Calendar.MONTH) + 1;
							System.out.println("Financial month : " + month);
							if (month < 3) {
								policyContributionEntity.setFinancialYear((year - 1) + "-" + year);
							} else {
								policyContributionEntity.setFinancialYear(year + "-" + (year + 1));
							}
							policyContributionEntity.setActive(true);
							policyContributionEntity.setIsDeposit(true);
							policyContributionEntity.setOpeningBalance(0.0);
//							policyContributionEntity.setPolicyId(savedStagingPolicyEntity.getId());
							policyContributionEntity.setTmpPolicyId(masterTmpPolicyId);
							policyContributionEntity.setRegConId(null);
							policyContributionEntity.setTxnEntryStatus(0l);
							policyContributionEntity.setVersionNo(1l);
							policyContributionEntity.setVoluntaryContribution(0.0);
							policyContributionEntity.setZeroAccountEntries(0l);
							policyContributionRepository.save(policyContributionEntity);
							
							ShowDepositLockDto depositLockDto = new ShowDepositLockDto();
							
							String prodAndVarientCodeSame=	commonModuleService.getProductCode(renewalPolicyTMPEntity.getProductId());
							
							depositLockDto.setChallanNo(Integer.valueOf(policyContributionDetailEntity.getChallanNo()));
							depositLockDto.setRefNo(policyDepositEntity.getId().toString());
							depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
							depositLockDto.setUserCode(policyContributionDetailDto.getCreatedBy());
							depositLockDto.setDueMonth(DateUtils.currentMonth()+"/"+DateUtils.currentDay());
							depositLockDto.setProductCode(prodAndVarientCodeSame);
							depositLockDto.setVariantCode(prodAndVarientCodeSame);
							
							logger.info("Lock Deposit Params: "+depositLockDto.toString());
							showDepositLockDto.add(depositLockDto);
							}
						}
				
						if (isDevEnvironmentForRenewals == false) {
							accountingService.lockDeposits(showDepositLockDto,policyContributionDetailDto.getCreatedBy());
						}

					
					Double getCSPPSP = getGstSC.getCurrentServices() + getGstSC.getPastService();
					policyContrySummaryEntity.setContributionDetailId(policyContributionDetailEntity.getId());
					policyContrySummaryEntity.setClosingBalance(getCSPPSP);
					policyContrySummaryEntity.setTotalContribution(getCSPPSP);
					policyContrySummaryEntity.setTotalEmployerContribution(getCSPPSP);
					policyContrySummaryEntity.setActive(true);
					policyContrySummaryEntity.setCreatedDate(new Date());
					policyContrySummaryEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
					System.out.println("Financial month : " + month);
					if (month < 3) {
						policyContrySummaryEntity.setFinancialYear((year - 1) + "-" + year);
					} else {
						policyContrySummaryEntity.setFinancialYear(year + "-" + (year + 1));
					}
					policyContrySummaryEntity.setOpeningBalance(0.0);
//					policyContrySummaryEntity.setPolicyId(savedStagingPolicyEntity.getId());
					policyContrySummaryEntity.setTmpPolicyId(masterTmpPolicyId);
					policyContrySummaryEntity.setTotalEmployeeContribution(0.0);
					policyContrySummaryEntity.setTotalVoluntaryContribution(0.0);
					Double getPaise = Double.parseDouble(standardCodeEntity.getValue()) / 100;

					policyContrySummaryEntity.setStampDuty(
							(policyContrySummaryEntity.getTotalContribution().doubleValue() / 1000) * getPaise);
					logger.info("StamDuty =(TotalContribution /1000) * getPaise"
							+ policyContrySummaryEntity.getStampDuty());
					policyContrySummaryRepository.save(policyContrySummaryEntity);
//					StagingPolicyValuationMatrixEntity getStagingPolicyValuationMatrixEntity=stagingPolicyValuationMatrixRepository.findByPolicyId(savedStagingPolicyEntity.getId()).get();

					RenewalValuationMatrixTMPEntity getRenewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
							.findBytmpPolicyId(masterTmpPolicyId).get();

					Double getCPLCSum = getGstSC.getCurrentServices() + getGstSC.getPastService()
							+ (getRenewalValuationMatrixTMPEntity.getTotalSumAssured().isNaN() ? 0L
									: getRenewalValuationMatrixTMPEntity.getTotalSumAssured().longValue());
					logger.info("Adding Total= CurrentService+PastService+totalsumAssured");
					if (getCPLCSum > 0) {
						Double valuesDividedByThousandQuotient = getCPLCSum / 1000;
						Double valuesDividedByThousandremainder = getCPLCSum % 1000;
						if (valuesDividedByThousandremainder == 0) {

							policyContrySummaryEntity.setTotalContribution(
									getCPLCSum + (valuesDividedByThousandQuotient * getPaise.longValue()));
						} else {

							policyContrySummaryEntity.setTotalContribution(
									getCPLCSum + ((valuesDividedByThousandQuotient + 1) * getPaise.longValue()));
						}
					}
					policyContrySummaryEntity
							.setStampDuty((policyContrySummaryEntity.getTotalContribution() / 1000) * getPaise);
					logger.info("StamDuty =(TotalContribution /1000) * getPaise"
							+ policyContrySummaryEntity.getStampDuty());
					policyContrySummaryRepository.save(policyContrySummaryEntity);

					return ApiResponseDto.success(RenewalPolicyTMPHelper.entityToDto(renewalPolicyTMPEntity));
				}

				@Override
				public ApiResponseDto<PolicyDto> findByPolicyNumber(String policyNumber) {
		
				MasterPolicyEntity policyEntity = masterPolicyCustomRepository.findByPolicyNumberisactive(policyNumber);
						
							PolicyDto policyDto = PolicyHelper.entityToDto(policyEntity);
							
							return ApiResponseDto.success(policyDto);
						
					}

	@Override
	public ApiResponseDto<List<PolicyStatementPeriod>> getPolicyStatementPeriod(Long id) {
		String sdt = policyHistoryRepository.findPolicyMinStartDate(id);
		MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(id);
		if (sdt == null)
			sdt = DateUtils.dateToStringDDMMYYYY(masterPolicyEntity.getPolicyStartDate());
		
		Calendar startDate = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		startDate.setTime(DateUtils.convertStringToDate(sdt));
		today.setTime(new Date());
		
		List<PolicyStatementPeriod> policyStatementPeriods = new ArrayList<>();
		while (startDate.before(today)) {
			PolicyStatementPeriod policyStatementPeriod = new PolicyStatementPeriod();
			policyStatementPeriod.setFinancialYear(DateUtils.getFinancialYear(startDate.getTime()));
			
			if (masterPolicyEntity.getProductVariantId() == 46) {
				List<String> quarters = new ArrayList<>();
				Calendar tt = Calendar.getInstance();
				tt.setTime(startDate.getTime());
				while (policyStatementPeriod.getFinancialYear().equals(DateUtils.getFinancialYear(tt.getTime())) && tt.before(today)) {
					quarters.add(DateUtils.getFinancialQuarterIdentifier(tt.getTime()));
					tt.add(Calendar.MONTH, 3);
				}		
				policyStatementPeriod.setQuarters(quarters);
			}
			policyStatementPeriods.add(policyStatementPeriod);
			startDate.add(Calendar.YEAR, 1);
		}
		
		return ApiResponseDto.success(policyStatementPeriods);
	}

	@Override
	public ApiResponseDto<List<PolicyContrySummarydto>> getSummaryContriDetailForRenewal(Long tmppolicyId) {
		List<PolicyContrySummaryEntity> policyContrySummaryEntity = policyContrySummaryRepository
				.findBytmpPolicyId(tmppolicyId);

		if (policyContrySummaryEntity.size() < 0) {
			return ApiResponseDto.success(null);
		}
		return ApiResponseDto.success(
				policyContrySummaryEntity.stream().map(PolicyHelper::EntityTosummarydto).collect(Collectors.toList()));

	}

	@Override
	public ApiResponseDto<List<PolicyDto>> filterForClaim(PolicySearchDto policySearchDto) {
		
		List<Predicate> predicate = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MasterPolicySearchEntity> createQuery = criteriaBuilder
				.createQuery(MasterPolicySearchEntity.class);
		
		Root<MasterPolicySearchEntity> root = createQuery.from(MasterPolicySearchEntity.class);
		Join<MasterPolicySearchEntity, MphSearchEntity> join = root.join("policyMPHTmp");
		
		if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())
				&& policySearchDto.getPolicyNumber() != null) {
			predicate.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("policyNumber"))),
					policySearchDto.getPolicyNumber().toLowerCase()));
		}
		if (StringUtils.isNotBlank(policySearchDto.getCustomerName())
				&& policySearchDto.getCustomerName() != null) {
			predicate.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(root.get("customerName"))),
					policySearchDto.getCustomerName().toLowerCase()));
		}
		
		if (StringUtils.isNotBlank(policySearchDto.getPan())
				&& policySearchDto.getPan() !=null){
			predicate.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(join.get("pan"))),
					policySearchDto.getPan().toLowerCase()));
		}
		
		
		if ((policySearchDto.getTaggedStatusId() != null)
				&& StringUtils.isNotBlank(policySearchDto.getTaggedStatusId().toString())) {
			predicate.add(criteriaBuilder.equal(root.get("policyTaggedStatusId"),
					policySearchDto.getTaggedStatusId()));
		}
		
		if ((policySearchDto.getPolicyStatusId() != null && policySearchDto.getPolicyStatusId() >0)
				&& StringUtils.isNotBlank(policySearchDto.getPolicyStatusId().toString())) {
			predicate.add(criteriaBuilder.equal(root.get("policyStatusId"),
					policySearchDto.getPolicyStatusId()));
		}
		if (policySearchDto.getUnitOffice() != null && policySearchDto.getUnitOffice() > 0) {
			predicate.add(criteriaBuilder.equal(root.get("unitOffice"), policySearchDto.getUnitOffice()));
		}
		
		if (policySearchDto.getUserType() != null) {
			if (policySearchDto.getUserType().equals("UO")) {
				if (policySearchDto.getUnitCode() != null) {
					predicate.add(criteriaBuilder.equal(root.get("unitCode"), policySearchDto.getUnitCode()));
				}
			}
			if (policySearchDto.getUserType().equals("ZO")) {

				String get = policySearchDto.getUnitCode().substring(0, 2);
				predicate.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("unitCode")),
						"%" + get.toLowerCase() + "%"));
			}
		}



		predicate.add(criteriaBuilder.equal(join.get("isActive"), Boolean.TRUE));

		createQuery.select(root).where(predicate.toArray(new Predicate[] {}));
		List<MasterPolicySearchEntity> entities = new ArrayList<MasterPolicySearchEntity>();
		entities = entityManager.createQuery(createQuery).getResultList();

		List<MasterPolicySearchEntity> masterPolicyEntities = new ArrayList<>();

		for (MasterPolicySearchEntity masterPolicyEntity : entities) {
			masterPolicyEntities.add(masterPolicyCustomRepository.setTransientValues(masterPolicyEntity));
		}
		return ApiResponseDto
				.success(masterPolicyEntities.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));
	


		
	}

	@Override
	public Long getPolicySubStatus(Long policySubStatusId) {
		Long count=stagingPolicyRepository.getCountPolicySubStatus(policySubStatusId);
		return count;
	}

	@Override
	public ApiResponseDto<PolicyContributionDetailDto> getlatestcontributiondetail(Long masterPolicyId) {
		
		MasterPolicyContributionDetails 	masterPolicyContributionDetails	=masterPolicyContributionDetailRepository.findBygetlatestmasterPolicyIdrecord(masterPolicyId);
		
		
		return ApiResponseDto
				.success( new  ModelMapper().map(masterPolicyContributionDetails, PolicyContributionDetailDto.class));
						}
}
