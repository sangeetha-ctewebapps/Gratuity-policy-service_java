package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.UnlockDepositDetailDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.CommonMasterVariantEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterProductRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterVariantRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.PickListItemRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.service.CommonService;
import com.lic.epgs.gratuity.common.service.impl.CommonModuleServiceImpl;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.fund.service.FundService;
import com.lic.epgs.gratuity.mph.dto.TemptMPHDto;
import com.lic.epgs.gratuity.mph.entity.HistoryMPHEntity;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.helper.MPHHelper;
import com.lic.epgs.gratuity.mph.repository.HistoryMPHRepository;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.dto.GratuityCalculationsDto;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.claim.repository.TempPolicyClaimBeneficiaryRepository;
import com.lic.epgs.gratuity.policy.claim.repository.TempPolicyClaimPropsRepository;
import com.lic.epgs.gratuity.policy.claim.service.impl.PolicyClaimServiceImpl;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositLockDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.HistoryGratutiyBenefitRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.lifecover.entity.HistoryLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.repository.HistoryLifeCoverRepository;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.repository.HistoryMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberAppointeeRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContrySummaryEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyDepositRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyDepositRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.repository.RenewalLifeCoverTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.PolicyServiceRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.entity.RenewalSchemeruleTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.repository.RenewalSchemeruleTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.entity.RenewalValuationTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.repository.RenewalValuationTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationWithdrawalTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationBasicTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationMatrixTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationWithdrawalTMPRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyAdjustmentDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyAdjustmentDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyHistoryRepository;
import com.lic.epgs.gratuity.policy.repository.StagingPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.repository.custom.StagingPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeRuleHistoryEntity;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleHistoryRepository;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyMasterValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationHistoryEntity;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyMasterValuationRepository;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyValuationHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationBasicHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.repository.ContributionAdjustmentPropsRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.AdjustmentPropsDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.BeneficiaryPaymentDetailModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DepositAdjustementRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ExactMatchTransferSearchRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.MphDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.OmniDocumentResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.PolicyContriDetailDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.PreValidationRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RegularGstDetailModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RejectMemberTransferRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RemoveDocReq;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RetainMemberLicIdRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ReversalGstDetailModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.SaveMemberTransferDetailRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ServiceMemberTransferDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferDocUpdateDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferDocumentDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferMemberDrCrReqModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferMemberReqModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferMemberSearchWithFilterRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferNotesRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferRequisitionModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferSearchWithServiceResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TrnRegisModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.UpdateTransferMemberModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.UploadDocReq;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.ContriAdjustmentPropsEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.MemberCategoryEntityVersion;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.PolicyContriDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.PolicyDeposit;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferDocumentDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferMemberPolicyDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferNotesEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferPolicyDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferRequisitionEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.helper.ContriHelper;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.helper.PolicyDestinationVersionHelper;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferGratuityCalculationDTO;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferInCalculateDTO;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferRefundCalculationDTO;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.AdjustmentPropsRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.MasterPolicyCustomDestinationVersionRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.MasterPolicyTransferVerRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.PolicyAdjustmentDetailRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.PolicyContriDetailRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.PolicyContriRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.PolicyContrySummaryRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.PolicyDepositRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.SearchWithServiceNameAndFilterDao;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.StagingPolicyCustomDestinationVersionRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferDocumentRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferMemberPolicyDetailRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferNotesRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferPolicyDetailRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferRequisitionRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.impl.MemberCategoryRepositoryVersionOne;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.service.MemberTransferInOutService;
import com.lic.epgs.gratuity.policyservices.policymodification.repository.MPHBankRepositoryPS;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkStgEntity;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBatchRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBulkStgRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberRepository;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;
import com.lic.epgs.gratuity.quotation.premium.repository.impl.GratuityCalculationRepositoryImpl;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemberTransferInOutServiceImpl implements MemberTransferInOutService {

	private static Long onboardedId = 202l;
	private static Long OnboardCancelled = 203l;

	@Value("${app.policy.claim.ModeOfExitDeath}")
	private String modeOfExitDeath;

	@Autowired
	private TransferNotesRepo transferNotesRepo;

	@Autowired
	private TransferRequisitionRepo transferRequisitionRepo;

	@Autowired
	private TransferMemberPolicyDetailRepo transferMemberPolicyDetailRepo;

	@Autowired
	private TransferPolicyDetailRepo transferPolicyDetailRepo;

	@Autowired
	private MasterPolicyRepository masterPolicyRepository;

	@Autowired
	private PolicyMemberRepository policyMemberRepository;

	@Autowired
	private PolicyGratuityBenefitRepository policyGratuityBenefitRepository;

	@Autowired
	private PolicyClaimServiceImpl policyClaimServiceImpl;

	@Autowired
	private SearchWithServiceNameAndFilterDao searchWithServiceNameAndFilterDao;

	@Autowired
	private MemberCategoryRepositoryVersionOne memberCategoryRepositoryVersionOne;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private StagingPolicyCustomDestinationVersionRepository stagingPolicyCustomDestinationVersionRepository;

	@Autowired
	private MasterPolicyCustomDestinationVersionRepository masterPolicyCustomDestinationVersionRepository;

	@Autowired
	private CommonMasterProductRepository commonMasterProductRepository;

	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;

	@Autowired
	private StagingPolicyCustomRepository stagingPolicyCustomRepository;

	@Autowired
	private CommonModuleServiceImpl commonModuleServiceImpl;

	@Autowired
	private TempPolicyClaimPropsRepository tempPolicyClaimPropsRepository;

	@Autowired
	private PolicyHistoryRepository policyHistoryRepository;

	@Autowired
	private HistoryMemberRepository historyMemberRepository;

	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

	@Autowired
	private PolicyServiceRepository policyServiceRepository;

	@Autowired
	private PolicySchemeRuleHistoryRepository policySchemeRuleHistoryRepository;

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
	private RenewalSchemeruleTMPRepository renewalSchemeruleTMPRepository;

	@Autowired
	private PolicyLifeCoverRepository policyLifeCoverRepository;

	@Autowired
	private RenewalLifeCoverTMPRepository renewalLifeCoverTMPRepository;

	@Autowired
	private MemberCategoryRepository memberCategoryRepository;

	@Autowired
	private PolicyMasterValuationRepository policyMasterValuationRepository;

	@Autowired
	private RenewalPolicyTMPMemberRepository renewalPolicyTMPMemberRepository;

	@Autowired
	private MemberCategoryModuleRepository memberCategoryModuleRepository;

	@Autowired
	private HistoryLifeCoverRepository historyLifeCoverRepository;

	@Autowired
	private RenewalGratuityBenefitTMPRepository renewalGratuityBenefitTMPRepository;

	@Autowired
	private HistoryGratutiyBenefitRepository historyGratutiyBenefitRepository;

	@Autowired
	private PolicySchemeRuleRepository policySchemeRuleRepository;

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
	private PolicyValuationMatrixHistoryRepository policyValuationMatrixHistoryRepository;

	@Autowired
	private PolicyValuationWithdrawalRateHistoryRepository policyValuationWithdrawalRateHistoryRepository;

	@Autowired
	private PolicyValuationBasicHistoryRepository policyValuationBasicHistoryRepository;

	@Autowired
	private PolicyValuationHistoryRepository policyValuationHistoryRepository;

	@Autowired
	private TempPolicyClaimBeneficiaryRepository tempPolicyClaimBeneficiaryRepository;

	@Autowired
	private TempMemberAppointeeRepository tempMemberAppointeeRepository;

	@Autowired
	private CommonModuleService commonModuleService;

	@Autowired
	private PolicyValuationMatrixRepository policyValuationMatrixRepository;

	@Autowired
	private RenewalValuationMatrixTMPRepository renewalValuationMatrixTMPRepository;

	@Autowired
	private StagingPolicyRepository stagingPolicyRepository;

	@Autowired
	private TransferDocumentRepo transferDocumentRepo;

	@Autowired
	private MPHRepository mphRepository;

	@Autowired
	private GratuityCalculationRepository gratuityCalculationRepository;

	@Autowired
	private DocumentUploadServiceImpl documentUploadServiceImpl;

	@Autowired
	private PickListItemRepository pickListItemRepository;

	@Autowired
	private ContributionAdjustmentPropsRepository contributionAdjustmentPropsRepository;

	@Autowired
	private PolicyContributionDetailRepository policyContributionDetailRepository;

	@Autowired
	private PolicyDepositRepository policyDepositRepository;

	@Autowired
	private MasterPolicyDepositRepository masterPolicyDepositRepository;

	@Autowired
	private PolicyAdjustmentDetailRepository policyAdjustmentDetailRepository;

	@Autowired
	private MasterPolicyAdjustmentDetailRepository masterPolicyAdjustmentDetailRepository;

	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private CommonMasterStateRepository commonMasterStateRepository;

	@Autowired
	private PolicyContributionRepository policyContributionRepository;

	@Autowired
	private MasterPolicyContributionRepository masterPolicyContributionRepository;

	@Autowired
	private AccountingService accountingService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private MasterPolicyContributionDetailRepository masterPolicyContributionDetailRepository;

	@Autowired
	AdjustmentPropsRepository adjustmentPropsRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	PolicyContriDetailRepository policyContriDetailRepository;

	@Autowired
	PolicyDepositRepo policyDepositRepo;

	@Autowired
	private PolicyContriRepository policyContriRepository;

	@Autowired
	PolicyAdjustmentDetailRepo policyAdjustmentDetailRepo;

	@Autowired
	MasterPolicyTransferVerRepository masterPolicyTransferVerRepository;

	@Autowired
	private MPHBankRepositoryPS mphBankRepository;

	@Autowired
	CommonMasterVariantRepository commonMasterVariantRepository;

	@Autowired
	StandardCodeRepository standardCodeRepository;

	@Autowired
	MemberBatchRepository memberBatchRepository;

	@Autowired
	MemberBulkStgRepository memberBulkStgRepository;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	TaskProcessRepository taskProcessRepository;

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;

	@Value("${app.accountingServiceEndpoint}")
	private String accountingServiceEndpoint;

	@Value("${app.bulkServiceEndPoint}")
	private String bulkServiceEndPoint;

	@Autowired
	PolicyContrySummaryRepo policyContrySummaryRepo;

	@Autowired
	TaskAllocationRepository taskAllocationRepository;

	@Autowired
	private GratuityCalculationRepositoryImpl gratuityCalculationRepositoryImpl;

	@Value("${app.transfer.rejectedStatusId}")
	private String rejectedId;

	@Value("${app.fundEndpoint}")
	private String fundEndpoint;
	private static Long transfertatusId = 16l;

	public HttpHeaders restHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return headers;
	}

	@Override
	public Map<String, Object> saveTransferNotes(TransferNotesRequest transferNotesRequest) {
		log.info("Transfer Renewal Notes serviceImpl started ");
		Map<String, Object> response = new HashMap<>();

		try {
			TransferNotesEntity transferNotesEntity = getTransferNotesEntity(transferNotesRequest);
			transferNotesRepo.save(transferNotesEntity);

			response.put("responseMessage", "TRANSFER Notes Saved Successfully.");
			response.put("responseCode", "success");

			log.info("TRANSFER save Notes serviceImpl success");
			return response;
		} catch (Exception e) {
			System.out.println("Exception e is ..." + e);
			response.put("responseMessage", "TRANSFER Notes Failed to Save.");
			response.put("responseCode", "failed");
			log.info("TRANSFER save Notes serviceImpl failed");
		}
		log.info("TRANSFER save Notes serviceImpl ends");
		return response;
	}

	@NotNull
	private static TransferNotesEntity getTransferNotesEntity(TransferNotesRequest transferNotesRequest) {
		TransferNotesEntity transferNotesEntity = new TransferNotesEntity();

		transferNotesEntity.setTransferRequisitionId(transferNotesRequest.getTransferRequisitionId());
		transferNotesEntity.setNote(transferNotesRequest.getTransferNote());
		transferNotesEntity.setModifiedBy(transferNotesRequest.getModifiedBy());
		transferNotesEntity.setModifiedOn(new Date());
		transferNotesEntity.setCreatedOn(new Date());
		transferNotesEntity.setCreatedBy(transferNotesRequest.getCreatedBy());
		return transferNotesEntity;
	}

	@Override
	public ResponseEntity<?> calculategratuity(TransferGratuityCalculationDTO transferGratuityCalculationDTO) {
		Map<String, Object> response = new HashMap<>();
		boolean check;

		/** validate product variant **/
		PreValidationRequest preValidationRequest = new PreValidationRequest();
		preValidationRequest.setEventName("calculation");
		preValidationRequest.setProductVariantOut(transferGratuityCalculationDTO.getProductVariantOut());
		preValidationRequest.setProductIdOut(transferGratuityCalculationDTO.getProductIdOut());
		check = validatePolicyProductVariant(preValidationRequest);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Product variant");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		/** validate unit code **/
		PreValidationRequest preValidationRequest1 = new PreValidationRequest();
		preValidationRequest1.setEventName("calculation");
		preValidationRequest1.setPolicyNumberOut(transferGratuityCalculationDTO.getPolicyNumberOut());
		preValidationRequest1.setUnitCodeOut(transferGratuityCalculationDTO.getUnitOut());
		check = validatePolicyUnit(preValidationRequest1);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Unit");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		;

		PolicyMemberEntity policyMemberEntity = policyMemberRepository.findByPolicyIdandMemberId(
				transferGratuityCalculationDTO.getPolicyId(), transferGratuityCalculationDTO.getMemberId());
		if (policyMemberEntity != null) {
			if (transferGratuityCalculationDTO.getSalary() == null) {
				transferGratuityCalculationDTO.setSalary(policyMemberEntity.getBasicSalary());
			}
			Integer pastservice = 0;
			Date dateOfAppointmentdiff = null;
			String dateOfExitdiff;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dateOfAppointment = policyMemberEntity.getDateOfAppointment();
			Date str_DateofExit = transferGratuityCalculationDTO.getDateOfExit();
			String[] get = dateOfAppointment.toString().split(" ");
			log.info(get[0]);
			LocalDate startDate = LocalDate.parse(get[0]);
			dateOfExitdiff = sdf.format(str_DateofExit);
			LocalDate endDate = LocalDate.parse(dateOfExitdiff);
			Period period = Period.between(startDate, endDate);
			System.out.printf("The difference between %s and %s is %d years, %d months, and %d days.",
					dateOfAppointmentdiff, dateOfExitdiff, period.getYears(), period.getMonths(), period.getDays());

			if (period.getMonths() <= 6) {
				pastservice = period.getYears();
			}

			if (period.getMonths() == 6 && period.getDays() > 0) {
				pastservice = period.getYears() + 1;
			}

			if (period.getMonths() > 6) {
				pastservice = period.getYears() + 1;
			}
			double gratuityAmount = 0.0;
			Double calcgvalue = 0.0;
			List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntities = policyGratuityBenefitRepository
					.findBypolicyIdAndCategoryId(transferGratuityCalculationDTO.getPolicyId(),
							policyMemberEntity.getCategoryId());
			if (policyGratuityBenefitEntities != null && !policyGratuityBenefitEntities.isEmpty()) {
				for (PolicyGratuityBenefitEntity policyGratuityBenefitEntity : policyGratuityBenefitEntities) {
					if (policyGratuityBenefitEntity.getGratutiyBenefitTypeId() == 25
							|| policyGratuityBenefitEntity.getGratutiyBenefitTypeId() == 26) {
						for (PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity : policyGratuityBenefitEntity
								.getGratuityBenefits()) {
							if (policyGratuityBenefitPropsEntity.getIsActive()) {
								gratuityAmount = gratuityAmount
										+ ((policyGratuityBenefitPropsEntity.getNumberOfDaysWage().doubleValue()
												/ policyGratuityBenefitPropsEntity.getNumberOfWorkingDaysPerMonth()
														.doubleValue())
												* transferGratuityCalculationDTO.getSalary())
												* pastservice.doubleValue();
							}
						}
					}
					if (policyGratuityBenefitEntity.getGratutiyBenefitTypeId() == 27
							&& policyGratuityBenefitEntity.getGratutiySubBenefitId() == 185) {
						for (PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity : policyGratuityBenefitEntity
								.getGratuityBenefits()) {

							if (policyGratuityBenefitPropsEntity.getNumberOfYearsOfService() >= pastservice
									|| policyGratuityBenefitPropsEntity.getNumberOfYearsOfService() <= pastservice) {
								if (policyGratuityBenefitPropsEntity.getIsActive()) {
									calcgvalue = calcgvalue
											+ ((policyGratuityBenefitPropsEntity.getNumberOfDaysWage().doubleValue()
													/ policyGratuityBenefitPropsEntity.getNumberOfWorkingDaysPerMonth()
															.doubleValue())
													* transferGratuityCalculationDTO.getSalary())
													* policyGratuityBenefitPropsEntity.getNumberOfYearsOfService()
															.doubleValue();
									gratuityAmount = gratuityAmount + calcgvalue;
									break;
								}
							}
						}
					}
					if (policyGratuityBenefitEntity.getGratutiyBenefitTypeId() == 27
							&& policyGratuityBenefitEntity.getGratutiySubBenefitId() == 186) {
						for (PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity : policyGratuityBenefitEntity
								.getGratuityBenefits()) {
							if (policyGratuityBenefitPropsEntity.getIsActive()) {
								if (policyGratuityBenefitPropsEntity.getNumberOfYearsOfService() <= pastservice) {

									calcgvalue = calcgvalue
											+ ((policyGratuityBenefitPropsEntity.getNumberOfDaysWage().doubleValue()
													/ policyGratuityBenefitPropsEntity.getNumberOfWorkingDaysPerMonth()
															.doubleValue()
													* transferGratuityCalculationDTO.getSalary())
													* policyGratuityBenefitPropsEntity.getNumberOfYearsOfService()
															.doubleValue());
									gratuityAmount = gratuityAmount + calcgvalue;
								}
							}
						}
					}
				}
//                double gratuity = gratuityAmount;
//                double roundOff = Math.round(gratuity*100)/100;
//                log.info(roundOff);
				response.put("responseCode", "success");
				response.put("responseMessage", "Gratuity calculated successfully");
				response.put("Gratuityamount", Math.round(gratuityAmount * 100) / 100);
				response.put("Pastservice", pastservice);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				response.put("responseMessage", "No Gratuity Benefit Found by Policy ID and Category ID");
				response.put("responseCode", "failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
			}
		} else {
			response.put("responseMessage", "No member Found by Policy ID and Member ID");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
		}

	}

	@Override
	public ResponseEntity<?> RefundCalculation(TransferRefundCalculationDTO transferRefundCalculationDTO) {
		Map<String, Object> response = new HashMap<>();
		boolean check;

		/** validate product variant **/
		PreValidationRequest preValidationRequest = new PreValidationRequest();
		preValidationRequest.setEventName("calculation");
		preValidationRequest.setProductVariantOut(transferRefundCalculationDTO.getProductVariantOut());
		preValidationRequest.setProductIdOut(transferRefundCalculationDTO.getProductIdOut());
		check = validatePolicyProductVariant(preValidationRequest);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Product variant");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		/** validate unit code **/
		PreValidationRequest preValidationRequest1 = new PreValidationRequest();
		preValidationRequest1.setEventName("calculation");
		preValidationRequest1.setPolicyNumberOut(transferRefundCalculationDTO.getPolicyNumberOut());
		preValidationRequest1.setUnitCodeOut(transferRefundCalculationDTO.getUnitOut());
		check = validatePolicyUnit(preValidationRequest1);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Unit");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		;

		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository
				.findByIdAndIsActiveTrue(transferRefundCalculationDTO.getPolicyId());
		PolicyMemberEntity masterMemberEntity = policyMemberRepository.findByPolicyIdandMemberId(
				transferRefundCalculationDTO.getPolicyId(), transferRefundCalculationDTO.getMemberId());
		GratuityCalculationsDto getGratitutyCalculationDto = null;

		log.info(transferRefundCalculationDTO.getModeOfExit().equals(modeOfExitDeath));
		if (transferRefundCalculationDTO.getModeOfExit().equals(modeOfExitDeath)) {
			log.info("in death ");
			getGratitutyCalculationDto = policyClaimServiceImpl.futureForDeathRefundCalculation(
					masterPolicyEntity.getId(), masterMemberEntity.getId(),
					transferRefundCalculationDTO.getDateOfExit());
			double refundPremium = getGratitutyCalculationDto.getRefundPremium();
			double roundOff = Math.round(refundPremium * 100) / 100;
			log.info(roundOff);
			log.info("Refund GST" + getGratitutyCalculationDto.getRefundonGST());
			log.info("Redund premium" + roundOff);
		} else {
			getGratitutyCalculationDto = policyClaimServiceImpl.CurrentRefundCalculation(masterPolicyEntity.getId(),
					masterMemberEntity.getId(), transferRefundCalculationDTO.getDateOfExit());
			BigDecimal bd = BigDecimal.valueOf(getGratitutyCalculationDto.getRefundPremium());
			bd = bd.setScale(2, RoundingMode.HALF_UP);

			log.info(bd.toString());
			log.info("Refund GST" + getGratitutyCalculationDto.getRefundonGST());
			log.info("Redund premium" + bd);
		}
		BigDecimal bd = BigDecimal.valueOf(getGratitutyCalculationDto.getRefundPremium());
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		log.info(bd.toString());
		response.put("responseCode", "success");
		response.put("responseMessage", "Refund Calculated Successfully");
		response.put("refundGST", getGratitutyCalculationDto.getRefundonGST());
		response.put("refundPremium", bd.toString());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ResponseEntity<?> rejectMemberTransfer(RejectMemberTransferRequest rejectMemberTransferRequest) {
		log.info("Entering into MemberTransferInOutServiceImpl : rejectMemberTransfer");
		Map<String, Object> response = new HashMap<>();
		Long taskProcessId;
		try {
			Optional<TransferRequisitionEntity> transferRequisitionOpt = transferRequisitionRepo
					.findById(rejectMemberTransferRequest.getTransferRequisitionId());
			TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo
					.findByTransferRequisitionId(rejectMemberTransferRequest.getTransferRequisitionId());

			if (!transferRequisitionOpt.get().getTransferSubStatus().equalsIgnoreCase("Approved-Transfer OUT")
					&& transferRequisitionOpt.get().getTransferSubStatus() != null
					&& rejectMemberTransferRequest.getTransferType().equalsIgnoreCase("OUT")) {
				/* set transferStatus and transferSubStatus as rejected */
				transferRequisitionRepo.rejectMemberTransfer(rejectMemberTransferRequest.getTransferRequisitionId(),
						rejectMemberTransferRequest.getUserName(), "Rejected-Transfer Out");

				taskProcessId = taskProcessRepository.getTaskIdByProcessName("TRANSFER-OUT");
				TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
						.getByRequestId(String.valueOf(rejectMemberTransferRequest.getTransferRequestNumber()));

				if (taskAllocationEntity == null) {
					TaskAllocationEntity taskAllocationEntity1 = new TaskAllocationEntity();

					taskAllocationEntity1.setTaskStatus(rejectedId);
					taskAllocationEntity1
							.setRequestId(String.valueOf(transferRequisitionOpt.get().getTransferRequestNumber()));
					taskAllocationEntity1.setTaskProcessTaskPrId(taskProcessId);
					taskAllocationEntity1.setLocationType(transferPolicyDetailEntity.getUnitOut());
					taskAllocationEntity1.setCreatedBy(rejectMemberTransferRequest.getUserName());
					taskAllocationEntity1.setIsActive(true);
					taskAllocationEntity1.setModulePrimaryId(transferRequisitionOpt.get().getTransferRequisitionId());
					taskAllocationEntity1.setCreatedDate(new Date());

					taskAllocationRepository.save(taskAllocationEntity1);
				} else {
					TaskAllocationEntity taskAllocationEntityList = taskAllocationRepository
							.getByRequestId(String.valueOf(rejectMemberTransferRequest.getTransferRequestNumber()));
					taskAllocationEntityList.setTaskStatus(rejectedId);
					taskAllocationEntityList
							.setRequestId(String.valueOf(transferRequisitionOpt.get().getTransferRequestNumber()));
					taskAllocationEntityList.setTaskProcessTaskPrId(taskProcessId);
					taskAllocationEntityList.setLocationType(transferPolicyDetailEntity.getUnitOut());
					taskAllocationEntityList.setCreatedBy(rejectMemberTransferRequest.getUserName());
					taskAllocationEntityList.setIsActive(true);
					taskAllocationEntityList
							.setModulePrimaryId(transferRequisitionOpt.get().getTransferRequisitionId());
					taskAllocationEntityList.setCreatedDate(new Date());

					taskAllocationRepository.save(taskAllocationEntityList);
				}

				log.info(taskAllocationEntity);
				response.put("responseCode", "success");
				response.put("responseMessage", "Transfer request no. : "
						+ transferRequisitionOpt.get().getTransferRequestNumber() + " rejected successfully");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else if (!transferRequisitionOpt.get().getTransferSubStatus().equalsIgnoreCase("Approved-Transfer In")
					&& transferRequisitionOpt.get().getTransferSubStatus() != null
					&& rejectMemberTransferRequest.getTransferType().equalsIgnoreCase("IN")) {
				/* set transferStatus and transferSubStatus as rejected */
				transferRequisitionRepo.rejectMemberTransfer(rejectMemberTransferRequest.getTransferRequisitionId(),
						rejectMemberTransferRequest.getUserName(), "Rejected-Transfer In");

				taskProcessId = taskProcessRepository.getTaskIdByProcessName("TRANSFER-IN");
				TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
						.getByRequestId(String.valueOf(rejectMemberTransferRequest.getTransferRequestNumber()));

				if (taskAllocationEntity == null) {
					TaskAllocationEntity taskAllocationEntity1 = new TaskAllocationEntity();

					taskAllocationEntity1.setTaskStatus(rejectedId);
					taskAllocationEntity1
							.setRequestId(String.valueOf(transferRequisitionOpt.get().getTransferRequestNumber()));
					taskAllocationEntity1.setTaskProcessTaskPrId(taskProcessId);
					taskAllocationEntity1.setLocationType(transferPolicyDetailEntity.getUnitOut());
					taskAllocationEntity1.setCreatedBy(rejectMemberTransferRequest.getUserName());
					taskAllocationEntity1.setIsActive(true);
					taskAllocationEntity1.setModulePrimaryId(transferRequisitionOpt.get().getTransferRequisitionId());
					taskAllocationEntity1.setCreatedDate(new Date());

					taskAllocationRepository.save(taskAllocationEntity1);
				} else {
					TaskAllocationEntity taskAllocationEntityList = taskAllocationRepository
							.getByRequestId(String.valueOf(rejectMemberTransferRequest.getTransferRequestNumber()));
					taskAllocationEntityList.setTaskStatus(rejectedId);
					taskAllocationEntityList
							.setRequestId(String.valueOf(transferRequisitionOpt.get().getTransferRequestNumber()));
					taskAllocationEntityList.setTaskProcessTaskPrId(taskProcessId);
					taskAllocationEntityList.setLocationType(transferPolicyDetailEntity.getUnitOut());
					taskAllocationEntityList.setCreatedBy(rejectMemberTransferRequest.getUserName());
					taskAllocationEntityList.setIsActive(true);
					taskAllocationEntityList
							.setModulePrimaryId(transferRequisitionOpt.get().getTransferRequisitionId());
					taskAllocationEntityList.setCreatedDate(new Date());

					taskAllocationRepository.save(taskAllocationEntityList);
				}

				log.info(taskAllocationEntity);

				response.put("responseCode", "success");
				response.put("responseMessage", "Transfer request no. : "
						+ transferRequisitionOpt.get().getTransferRequestNumber() + " rejected successfully");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				response.put("responseCode", "failed");
				response.put("responseMessage", "Unable to reject, transfer request no. : "
						+ transferRequisitionOpt.get().getTransferRequestNumber() + " is already approved");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
		} catch (Exception e) {
			log.error("Transfer Request could not be rejected due to : " + e.getMessage());
			response.put("responseCode", "failed");
			response.put("responseMessage", "Transfer Request could not be rejected due to : " + e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> saveMemberTransfer(SaveMemberTransferDetailRequest saveRequest) {
		log.info("Entering into MemberTransferInOutServiceImpl : saveMemberTransfer");
		Map<String, Object> response = new HashMap<String, Object>();
		boolean check;

		try {

			/** To check member transfer process already initiated or completed **/
			if (StringUtils.equalsIgnoreCase(saveRequest.getIsBulk(), "N")) {
				String memberTransferStatus = transferRequisitionRepo
						.getMemberTransferStatus(saveRequest.getMemberId());
				if (!StringUtils.isEmpty(memberTransferStatus)
						&& !memberTransferStatus.equalsIgnoreCase("Rejected-Transfer Out")
						&& !memberTransferStatus.equalsIgnoreCase("Rejected-Transfer In")) {

					Long transferRequestNumber = transferRequisitionRepo
							.getTransferRequestNumber(saveRequest.getMemberId());
					log.info("Member transfer process is already initiated or completed, having transfer status as : "
							+ memberTransferStatus + " and Transfer Request No. : " + transferRequestNumber);
					response.put("responseMessage",
							"Member's transfer process is already initiated with Transfer Request No. : "
									+ transferRequestNumber);
					response.put("responseCode", "failed");
					return ResponseEntity.status(HttpStatus.OK).body(response);
				}
			}

			/** validate product variant **/
			PreValidationRequest preValidationRequest = new PreValidationRequest();
			preValidationRequest.setEventName("preSave");
			preValidationRequest.setProductIdOut(saveRequest.getProductIdOut());
			preValidationRequest.setProductIdIn(saveRequest.getProductIdIn());
			preValidationRequest.setProductVariantOut(saveRequest.getProductVariantOut());
			preValidationRequest.setProductVariantIn(saveRequest.getProductVariantIn());
			check = validatePolicyProductVariant(preValidationRequest);
			if (check == false) {
				response.put("responseMessage", "Policy doesn't exist in same Product variant");
				response.put("responseCode", "failed");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			;
			/** validate unit code **/
			PreValidationRequest preValidationRequest1 = new PreValidationRequest();
			preValidationRequest1.setEventName("preSave");
			preValidationRequest1.setUnitCodeOut(saveRequest.getUnitOut());
			preValidationRequest1.setUnitCodeIn(saveRequest.getUnitIn());
			preValidationRequest1.setPolicyNumberOut(saveRequest.getPolicyNumberOut());
			preValidationRequest1.setPolicyNumberIn(saveRequest.getPolicyNumberIn());
			check = validatePolicyUnit(preValidationRequest1);
			if (check == false) {
				response.put("responseMessage", "Policy doesn't exist in same Unit");
				response.put("responseCode", "failed");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			;

			/** Source and Destination Policy Should not be Same Validation **/

			if (saveRequest.getPolicyNumberOut() == saveRequest.getPolicyNumberIn()) {
				log.info(saveRequest.getPolicyNumberOut(), saveRequest.getPolicyNumberIn());
				response.put("responseMessage", "Policy Source Policy Number and Destination Policy Number's are Same");
				response.put("responseCode", "failed");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

			TransferRequisitionEntity transferRequisitionEntity = new TransferRequisitionEntity();

			transferRequisitionEntity.setTransferRequestNumber(transferRequisitionRepo.generateSeq());
			transferRequisitionEntity.setTransferRequestDate(new Date());
			transferRequisitionEntity.setLicId(saveRequest.getMemberLicId());
			transferRequisitionEntity.setTransferStatus("Active");
			transferRequisitionEntity.setTransferSubStatus("Active");
			transferRequisitionEntity.setRole(saveRequest.getRole());
			transferRequisitionEntity.setLocationType(saveRequest.getLocationType());
			transferRequisitionEntity.setIsBulk(saveRequest.getIsBulk());
			transferRequisitionEntity.setModifiedOn(new Date());
			transferRequisitionEntity.setCreatedOn(new Date());
			transferRequisitionEntity.setCreatedBy(saveRequest.getUserName());
			TransferRequisitionEntity savedRequistion = transferRequisitionRepo.save(transferRequisitionEntity);

			MasterPolicyEntity policyIdIn = masterPolicyRepository.getByPolicyNumber(saveRequest.getPolicyNumberIn());
			MasterPolicyEntity policyIdOut = masterPolicyRepository.getByPolicyNumber(saveRequest.getPolicyNumberOut());

			TransferPolicyDetailEntity transferPolicyDetailEntity = new TransferPolicyDetailEntity();
			transferPolicyDetailEntity.setTransferRequisitionId(savedRequistion.getTransferRequisitionId());
			transferPolicyDetailEntity.setPolicyNumberIn(Long.valueOf(saveRequest.getPolicyNumberIn()));
			transferPolicyDetailEntity.setPolicyNumberOut(Long.valueOf(saveRequest.getPolicyNumberOut()));
			transferPolicyDetailEntity.setMphNameIn(saveRequest.getMphNameIn());
			transferPolicyDetailEntity.setMphNameOut(saveRequest.getMphNameOut());
			transferPolicyDetailEntity.setProductNameIn(saveRequest.getProductNameIn());
			transferPolicyDetailEntity.setProductNameOut(saveRequest.getProductNameOut());
			transferPolicyDetailEntity.setProductVariantIn(saveRequest.getProductVariantIn());
			transferPolicyDetailEntity.setProductVariantOut(saveRequest.getProductVariantOut());
			transferPolicyDetailEntity.setAccruedInterestAmount(saveRequest.getTotalInterestAccrued());
			transferPolicyDetailEntity.setFrequency(saveRequest.getFrequencyIn());
			transferPolicyDetailEntity.setUnitIn(saveRequest.getUnitIn());
			transferPolicyDetailEntity.setUnitOut(saveRequest.getUnitOut());
			transferPolicyDetailEntity.setModifiedOn(new Date());
			transferPolicyDetailEntity.setCreatedOn(new Date());
			transferPolicyDetailEntity.setCreatedBy(saveRequest.getUserName());
			String productIdIn = saveRequest.getProductIdIn();
			Long productIdInNew = Long.valueOf(productIdIn);
			transferPolicyDetailEntity.setProductIdIn(productIdInNew);
			String productIdOut = saveRequest.getProductIdOut();
			Long productIdOutNew = Long.valueOf(productIdOut);
			transferPolicyDetailEntity.setProductIdOut(productIdOutNew);
			transferPolicyDetailEntity.setPolicyIdIn(policyIdIn.getId());
			transferPolicyDetailEntity.setPolicyIdOut(policyIdOut.getId());

			TransferPolicyDetailEntity savedPolicyDetail = transferPolicyDetailRepo.save(transferPolicyDetailEntity);

			TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = new TransferMemberPolicyDetailEntity();
			transferMemberPolicyDetailEntity.setTransferRequisitionId(savedRequistion.getTransferRequisitionId());
			transferMemberPolicyDetailEntity.setTransferPolicyDetailId(savedPolicyDetail.getTransferPolicyDetailId());
			transferMemberPolicyDetailEntity.setMemberName(saveRequest.getMemberName());
			transferMemberPolicyDetailEntity.setLicId(saveRequest.getMemberLicId());
			transferMemberPolicyDetailEntity.setCategoryIn(saveRequest.getCategoryIn());
			transferMemberPolicyDetailEntity.setCategoryOut(saveRequest.getCategoryOut());
			transferMemberPolicyDetailEntity.setPremiumAmount(saveRequest.getRefundPremiumOut());
			transferMemberPolicyDetailEntity.setMemberStatus(saveRequest.getMemberStatus());
			transferMemberPolicyDetailEntity.setGstOnPremium(saveRequest.getRefundGstOut());
			transferMemberPolicyDetailEntity.setIsAccruedGratuityModified(saveRequest.getIsAccruedGratuityModified());
			transferMemberPolicyDetailEntity.setAccruedGratuityExisting(saveRequest.getAccruedGratuityExisting());
			transferMemberPolicyDetailEntity.setAccruedGratuityNew(saveRequest.getAccruedGratuityNew());
			if (saveRequest.getDateOfBirth() != null) {
				Date fromDate2 = formatter2.parse(saveRequest.getDateOfBirth());
				transferMemberPolicyDetailEntity.setDateOfBirth(fromDate2);
			}
			transferMemberPolicyDetailEntity.setPanNumber(saveRequest.getPanNumber());
			transferMemberPolicyDetailEntity.setMemberId(saveRequest.getMemberId());
			transferMemberPolicyDetailEntity.setIsPremiumRefund(saveRequest.getIsPremiumRefund());
			transferMemberPolicyDetailEntity.setCompletedYearsOfService(saveRequest.getPastService());
			if (saveRequest.getDateOfJoining() != null) {
				Date fromDate1 = formatter1.parse(saveRequest.getDateOfJoining());
				transferMemberPolicyDetailEntity.setDateOfJoining(fromDate1);
			}
			if (saveRequest.getTransferEffectiveDate() != null) {
				Date fromDate = formatter.parse(saveRequest.getTransferEffectiveDate());
				transferMemberPolicyDetailEntity.setTransferOutEffectiveDate(fromDate);
			} else {
				response.put("responseMessage", "Transfer Out Effective Date is Mandatory");
				response.put("responseCode", "failed");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			transferMemberPolicyDetailEntity.setModifiedOn(new Date());
			transferMemberPolicyDetailEntity.setCreatedOn(new Date());
			transferMemberPolicyDetailEntity.setCreatedBy(saveRequest.getUserName());
			// transferMemberPolicyDetailEntity.setIsLicIdRetained(saveRequest.getIsLicIdRetained());
			transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);

			RetainMemberLicIdRequest retainMemberLicIdRequest = new RetainMemberLicIdRequest();
			retainMemberLicIdRequest.setTransferRequisitionId(transferRequisitionEntity.getTransferRequisitionId());
			retainMemberLicIdRequest.setIsLicIdRetainable(saveRequest.getRetainLicId());

			String isLicIdExist = "";

			transferMemberPolicyDetailEntity.setRetainLicId("N");

			if (saveRequest.getRetainLicId().equalsIgnoreCase("Y")) {
				MasterPolicyEntity masterPolicyEntity = masterPolicyRepository
						.findByPolicyNumber(transferPolicyDetailEntity.getPolicyNumberIn().toString());
				Long licIdCount = policyMemberRepository.getLicIdCountBylicIdAndpolicyId(
						transferMemberPolicyDetailEntity.getLicId().toString(), masterPolicyEntity.getId());

				if (licIdCount != null && licIdCount > 0) {
					isLicIdExist = "Y";
				} else {
					isLicIdExist = "N";
				}
				transferMemberPolicyDetailEntity.setRetainLicId("Y");
				transferMemberPolicyDetailEntity.setIsLicIdExist(isLicIdExist);
				transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);
			} else if (saveRequest.getRetainLicId().equalsIgnoreCase("N")) {
				transferMemberPolicyDetailEntity.setRetainLicId("N");
				transferMemberPolicyDetailEntity.setIsLicIdExist("");
				transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);

			}

			response.put("requisitionDetail", transferRequisitionEntity);
			response.put("policyDetail", transferPolicyDetailEntity);
			response.put("memberPolicyDetail", transferMemberPolicyDetailEntity);
			response.put("responseMessage", "Transfer Member details saved successfully.");
			response.put("responseCode", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);

		} catch (Exception e) {
			log.error("Save member transfer detailsa failed due to : " + e.getMessage());
			response.put("responseMessage", "member transfer Failed to Save.");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}

	public List<TransferSearchWithServiceResponse> searchWithServiceNumber(
			ExactMatchTransferSearchRequest exactMatchTransferSearchRequest) {
		Map<String, Object> quotDetails = new HashMap<String, Object>();
		List<TransferSearchWithServiceResponse> listResponse = new ArrayList<>();

		TransferSearchWithServiceResponse transferSearchWithServiceResponse = new TransferSearchWithServiceResponse();

		try {
			if ((exactMatchTransferSearchRequest.getTransferRequestNumber()) == null) {

				listResponse.add(transferSearchWithServiceResponse);
			} else {
				listResponse = searchWithServiceNameAndFilterDao
						.searchWithServiceNumber(exactMatchTransferSearchRequest);
			}
		} catch (Exception e) {
		}
		return listResponse;
	}

	public List<TransferSearchWithServiceResponse> getOverviewDetails(Long transferRequisitionId) {
		List<TransferSearchWithServiceResponse> listResponse = new ArrayList<>();
		try {
			listResponse = searchWithServiceNameAndFilterDao.getOverviewDetails(transferRequisitionId);

		} catch (Exception e) {
		}
		return listResponse;
	}

	public List<ServiceMemberTransferDetailsResponse> getSeviceDetails(Long transferRequisitionId) {
		List<ServiceMemberTransferDetailsResponse> listResponse = new ArrayList<>();
		try {
			listResponse = searchWithServiceNameAndFilterDao.getSeviceDetails(transferRequisitionId);
		} catch (Exception e) {
		}
		return listResponse;
	}

	@Override
	public ResponseEntity<Map<String, Object>> updateMemberTransfer(Long transferRequisitionId,
			UpdateTransferMemberModel updateRequest) {
		log.info("Entering into MemberTransferInOutServiceImpl : updateMemberTransfer");
		Map<String, Object> response = new HashMap<String, Object>();
		boolean check;

		try {
			/** validate product variant **/
			PreValidationRequest preValidationRequest = new PreValidationRequest();
			preValidationRequest.setEventName("preSave");
			preValidationRequest.setProductIdOut(updateRequest.getProductIdOut());
			preValidationRequest.setProductIdIn(updateRequest.getProductIdIn());
			preValidationRequest.setProductVariantOut(updateRequest.getProductVariantOut());
			preValidationRequest.setProductVariantIn(updateRequest.getProductVariantIn());
			check = validatePolicyProductVariant(preValidationRequest);
			if (check == false) {
				response.put("responseMessage", "Policy doesn't exist in same Product variant");
				response.put("responseCode", "failed");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			;

			/** validate unit code **/
			PreValidationRequest preValidationRequest1 = new PreValidationRequest();
			preValidationRequest1.setEventName("preSave");
			preValidationRequest1.setUnitCodeOut(updateRequest.getUnitOut());
			preValidationRequest1.setUnitCodeIn(updateRequest.getUnitIn());
			preValidationRequest1.setPolicyNumberOut(updateRequest.getPolicyNumberOut());
			preValidationRequest1.setPolicyNumberIn(updateRequest.getPolicyNumberIn());
			check = validatePolicyUnit(preValidationRequest1);
			if (check == false) {
				response.put("responseMessage", "Policy doesn't exist in same Unit");
				response.put("responseCode", "failed");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			;

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			TransferRequisitionEntity transferRequisitionEntity = transferRequisitionRepo
					.findById(transferRequisitionId).get();

			if (updateRequest.getMemberLicId() != null) {
				transferRequisitionEntity.setLicId(updateRequest.getMemberLicId());
			}

			transferRequisitionEntity.setTransferStatus("Active");
			if (updateRequest.getRole() != null) {
				transferRequisitionEntity.setRole(updateRequest.getRole());
			}
			if (updateRequest.getLocationType() != null) {
				transferRequisitionEntity.setLocationType(updateRequest.getLocationType());
			}
			if (updateRequest.getUserName() != null) {
				transferRequisitionEntity.setModifiedBy(updateRequest.getUserName());
			}
			transferRequisitionEntity.setModifiedOn(new Date());

			transferRequisitionRepo.save(transferRequisitionEntity);
			response.put("requisitionDetail", transferRequisitionEntity);

			MasterPolicyEntity policyIdIn = masterPolicyRepository.getByPolicyNumber(updateRequest.getPolicyNumberIn());
			MasterPolicyEntity policyIdOut = masterPolicyRepository
					.getByPolicyNumber(updateRequest.getPolicyNumberOut());

			TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo
					.findByTransferRequisitionId(transferRequisitionId);
			if (updateRequest.getPolicyNumberIn() != null) {
				transferPolicyDetailEntity.setPolicyNumberIn(Long.valueOf(updateRequest.getPolicyNumberIn()));
			}
			if (updateRequest.getPolicyNumberOut() != null) {
				transferPolicyDetailEntity.setPolicyNumberOut(Long.valueOf(updateRequest.getPolicyNumberOut()));
			}
			if (updateRequest.getPolicyIdIn() != null) {
				transferPolicyDetailEntity.setPolicyIdIn(Long.valueOf(policyIdIn.getId()));
			}
			if (updateRequest.getPolicyIdOut() != null) {
				transferPolicyDetailEntity.setPolicyIdOut(Long.valueOf(policyIdOut.getId()));
			}
			if (updateRequest.getMphNameIn() != null) {
				transferPolicyDetailEntity.setMphNameIn(updateRequest.getMphNameIn());
			}
			if (updateRequest.getMphNameOut() != null) {
				transferPolicyDetailEntity.setMphNameOut(updateRequest.getMphNameOut());
			}
			if (updateRequest.getProductNameIn() != null) {
				transferPolicyDetailEntity.setProductNameIn(updateRequest.getProductNameIn());

			}
			if (updateRequest.getProductNameOut() != null) {
				transferPolicyDetailEntity.setProductNameOut(updateRequest.getProductNameOut());
			}
			if (updateRequest.getProductVariantIn() != null) {
				transferPolicyDetailEntity.setProductVariantIn(updateRequest.getProductVariantIn());
			}
			if (updateRequest.getProductVariantOut() != null) {
				transferPolicyDetailEntity.setProductVariantOut(updateRequest.getProductVariantOut());
			}
			if (updateRequest.getTotalInterestAccrued() != null) {
				transferPolicyDetailEntity.setAccruedInterestAmount(updateRequest.getTotalInterestAccrued());

			}

			String productIdIn = updateRequest.getProductIdIn();
			Long productIdInNew = Long.valueOf(productIdIn);
			if (updateRequest.getProductIdIn() != null) {
				transferPolicyDetailEntity.setProductIdIn(productIdInNew);
			}
			String productIdOut = updateRequest.getProductIdOut();
			Long productIdOutNew = Long.valueOf(productIdOut);
			if (updateRequest.getProductIdOut() != null) {
				transferPolicyDetailEntity.setProductIdOut(productIdOutNew);
			}
			if (updateRequest.getFrequencyIn() != null) {
				transferPolicyDetailEntity.setFrequency(updateRequest.getFrequencyIn());
			}
			if (updateRequest.getUnitIn() != null) {
				transferPolicyDetailEntity.setUnitIn(updateRequest.getUnitIn());
			}
			if (updateRequest.getUnitOut() != null) {
				transferPolicyDetailEntity.setUnitOut(updateRequest.getUnitOut());
			}
			transferPolicyDetailEntity.setModifiedOn(new Date());
			if (updateRequest.getUserName() != null) {
				transferPolicyDetailEntity.setModifiedBy(updateRequest.getUserName());
			}
			transferPolicyDetailRepo.save(transferPolicyDetailEntity);
			response.put("policyDetail", transferPolicyDetailEntity);

			TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = transferMemberPolicyDetailRepo
					.findByTransferRequisitionId(transferRequisitionId);

			if (updateRequest.getMemberName() != null) {
				transferMemberPolicyDetailEntity.setMemberName(updateRequest.getMemberName());
			}
			if (updateRequest.getMemberLicId() != null) {
				transferMemberPolicyDetailEntity.setLicId(updateRequest.getMemberLicId());
			}
			if (updateRequest.getCategoryIn() != null) {
				transferMemberPolicyDetailEntity.setCategoryIn(updateRequest.getCategoryIn());
			}
			if (updateRequest.getCategoryOut() != null) {
				transferMemberPolicyDetailEntity.setCategoryOut(updateRequest.getCategoryOut());
			}
			if (updateRequest.getRefundPremiumOut() != null) {
				transferMemberPolicyDetailEntity.setPremiumAmount(updateRequest.getRefundPremiumOut());
			}
			if (updateRequest.getMemberStatus() != null) {
				transferMemberPolicyDetailEntity.setMemberStatus(updateRequest.getMemberStatus());
			}
			if (updateRequest.getRefundGstOut() != null) {
				transferMemberPolicyDetailEntity.setGstOnPremium(updateRequest.getRefundGstOut());
			}
			if (updateRequest.getIsAccruedGratuityModified() != null) {
				transferMemberPolicyDetailEntity
						.setIsAccruedGratuityModified(updateRequest.getIsAccruedGratuityModified());
			}
			if (updateRequest.getAccruedGratuityExisting() != null) {
				transferMemberPolicyDetailEntity.setAccruedGratuityExisting(updateRequest.getAccruedGratuityExisting());
			}
			if (updateRequest.getAccruedGratuityNew() != null) {
				transferMemberPolicyDetailEntity.setAccruedGratuityNew(updateRequest.getAccruedGratuityNew());
			}
			if (updateRequest.getDateOfBirth() != null) {
				Date fromDate = formatter.parse(updateRequest.getDateOfBirth());
				transferMemberPolicyDetailEntity.setDateOfBirth(fromDate);
			}
			if (updateRequest.getPanNumber() != null) {
				transferMemberPolicyDetailEntity.setPanNumber(updateRequest.getPanNumber());
			}
			if (updateRequest.getMemberId() != null) {
				transferMemberPolicyDetailEntity.setMemberId(updateRequest.getMemberId());
			}
			if (updateRequest.getDateOfJoining() != null) {
				Date fromDate1 = formatter.parse(updateRequest.getDateOfJoining());
				transferMemberPolicyDetailEntity.setDateOfJoining(fromDate1);
			}
			if (updateRequest.getPastSericve() != null) {
				transferMemberPolicyDetailEntity.setCompletedYearsOfService(updateRequest.getPastSericve());
			}
			if (updateRequest.getTransferEffectiveDate() != null) {
				Date fromDate2 = formatter.parse(updateRequest.getTransferEffectiveDate());
				transferMemberPolicyDetailEntity.setTransferOutEffectiveDate(fromDate2);
			}
			transferMemberPolicyDetailEntity.setModifiedOn(new Date());
			transferMemberPolicyDetailEntity.setModifiedBy(updateRequest.getUserName());
			transferMemberPolicyDetailEntity.setIsPremiumRefund(updateRequest.getIsPremiumRefund());
//            transferMemberPolicyDetailEntity.setIsLicIdRetained(updateRequest.getIsLicIdRetained());         
			transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);

			RetainMemberLicIdRequest retainMemberLicIdRequest = new RetainMemberLicIdRequest();
			retainMemberLicIdRequest.setTransferRequisitionId(transferRequisitionEntity.getTransferRequisitionId());
			retainMemberLicIdRequest.setIsLicIdRetainable(updateRequest.getRetainLicId());
			String isLicIdExist = "";

			if (updateRequest.getRetainLicId() != null && updateRequest.getRetainLicId().equalsIgnoreCase("Y")
					&& transferRequisitionEntity.getIsBulk().equalsIgnoreCase("N")) {
				MasterPolicyEntity masterPolicyEntity = masterPolicyRepository
						.findByPolicyNumber(transferPolicyDetailEntity.getPolicyNumberIn().toString());
				Long licIdCount = policyMemberRepository.getLicIdCountBylicIdAndpolicyId(
						transferMemberPolicyDetailEntity.getLicId().toString(), masterPolicyEntity.getId());

				if (licIdCount != null && licIdCount > 0) {
					isLicIdExist = "Y";
				} else {
					isLicIdExist = "N";
				}
				transferMemberPolicyDetailEntity.setRetainLicId("Y");
				transferMemberPolicyDetailEntity.setIsLicIdExist(isLicIdExist);
				transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);
			} else if (updateRequest.getRetainLicId() != null && updateRequest.getRetainLicId().equalsIgnoreCase("N")) {
				transferMemberPolicyDetailEntity.setRetainLicId("N");
				transferMemberPolicyDetailEntity.setIsLicIdExist("");
				transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);
			}

			response.put("memberPolicyDetail", transferMemberPolicyDetailEntity);
			response.put("requisitionDetail", transferRequisitionEntity);
			response.put("policyDetail", transferPolicyDetailEntity);
			response.put("memberPolicyDetail", transferMemberPolicyDetailEntity);
			response.put("responseMessage", "Transfer Member details Updated successfully.");
			response.put("responseCode", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			log.error("Update member transfer detailsa failed due to : " + e.getMessage());
			response.put("responseMessage", "member transfer Failed to Update.");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> getAllMemberCategoryForTransfer(String entrytype, String type, Long id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<MemberCategoryEntityVersion> memberCategoryList = new ArrayList<MemberCategoryEntityVersion>();
		if (entrytype.equals("QUOTATION")) {
			if (type.equals("INPROGRESS")) {
				memberCategoryList = memberCategoryRepositoryVersionOne.findByqstgQuoationId(id);
			} else {
				memberCategoryList = memberCategoryRepositoryVersionOne.findByqmstQuotationId(id);
			}
		}
		if (entrytype.equals("POLICY")) {
			if (type.equals("INPROGRESS")) {
				memberCategoryList = memberCategoryRepositoryVersionOne.findBypstgPolicyId(id);
			} else {
				memberCategoryList = memberCategoryRepositoryVersionOne.findBypmstPolicyId(id);
			}
		}
		if (entrytype.equals("RENEWALS")) {
			if (type.equals("INPROGRESS")) {
				memberCategoryList = memberCategoryRepositoryVersionOne.findBypmstTmpPolicy(id);
			} else {
				memberCategoryList = memberCategoryRepositoryVersionOne.findBypmstTmpPolicy(id);
			}
		}
		if (memberCategoryList != null && !memberCategoryList.isEmpty()) {
			response.put("memberCategoryDetails", memberCategoryList);
			response.put("responseCode", "success");
			response.put("responseMessage", "Success");
		} else {
			response.put("memberCategoryDetails", memberCategoryList);
			response.put("responseCode", "failed");
			response.put("responseMessage", "Member Category Data Not Found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public List<TransferSearchWithServiceResponse> getTransferMemberDetailsWithFilter(
			TransferMemberSearchWithFilterRequest transferMemberSearchWithFilterRequest) {
		List<TransferSearchWithServiceResponse> listResponse = new ArrayList<>();

		TransferSearchWithServiceResponse transferSearchWithServiceResponse = new TransferSearchWithServiceResponse();
		try {
			if ((transferMemberSearchWithFilterRequest.getLicId() == null)
					&& (transferMemberSearchWithFilterRequest.getPolicyNumberOut() == null)
					&& (transferMemberSearchWithFilterRequest.getCreatedFromDate() == null)
					&& (transferMemberSearchWithFilterRequest.getCreatedToDate() == null)) {
				listResponse.add(transferSearchWithServiceResponse);
			} else {
				listResponse = searchWithServiceNameAndFilterDao
						.getTransferMemberDetailsWithFilter(transferMemberSearchWithFilterRequest);
			}
		} catch (Exception e) {
		}
		return listResponse;
	}

	@Override
	public ApiResponseDto<List<PolicyDto>> searchSourcePolicy(PolicySearchDto policySearchDto) {
		if (policySearchDto.getTaggedStatusId().equals(138L)) {
			return this.inProgressSourceFilter(policySearchDto);
		} else {
			return this.existingSourceFilter(policySearchDto);
		}
	}

	@Override
	public ApiResponseDto<List<PolicyDto>> searchDestinationPolicy(PolicySearchDto policySearchDto) {
		if (policySearchDto.getTaggedStatusId().equals(138L)) {
			return this.inProgressDestinationFilter(policySearchDto);
		} else {
			return this.existingDestinationFilter(policySearchDto);
		}
	}

	// source search Policy - In Progress
	public ApiResponseDto<List<PolicyDto>> inProgressSourceFilter(PolicySearchDto policySearchDto) {
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
		if (StringUtils.isNotBlank(policySearchDto.getCustomerCode())) {
			predicates.add(criteriaBuilder.equal(root.get("customerCode"), policySearchDto.getCustomerCode()));
		}

		List variantVersionList = Arrays.asList(new String[] { "33", "45", "46" });
		predicates.add(root.get("productVariantId").in(variantVersionList));

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
			policyDtos.add(
					PolicyHelper.entityToDto(stagingPolicyCustomRepository.setTransientValues(stagingPolicyEntity)));
		}

		return ApiResponseDto.success(policyDtos);
	}

	// source search Policy - Existing
	public ApiResponseDto<List<PolicyDto>> existingSourceFilter(PolicySearchDto policySearchDto) {
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
			predicates.add(criteriaBuilder.equal(root.get("productVariant"), policySearchDto.getProductVariant()));
		}
		if (StringUtils.isNotBlank(policySearchDto.getCustomerCode())) {
			predicates.add(criteriaBuilder.equal(root.get("customerCode"), policySearchDto.getCustomerCode()));
		}

		List variantVersionList = Arrays.asList(new String[] { "33", "45", "46" });
		predicates.add(root.get("productVariantId").in(variantVersionList));

		if (policySearchDto.getUnitOffice() != null && policySearchDto.getUnitOffice() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("unitOffice"), policySearchDto.getUnitOffice()));
		}
//		if (policySearchDto.getPolicyStatus() != null && policySearchDto.getPolicyStatus() > 0) {
//			predicates.add(criteriaBuilder.equal(root.get("policyStatus"), policySearchDto.getPolicyStatus()));
//		}
//        if (policySearchDto.getPolicyStatusId() != null && policySearchDto.getPolicyStatusId() > 0) {
//            predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), policySearchDto.getPolicyStatusId()));
//        }
		List policyStatusIdList = Arrays.asList(new String[] { "123", "127" });
		predicates.add(root.get("policyStatusId").in(policyStatusIdList));

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

	// destination search Policy - In Progress
	public ApiResponseDto<List<PolicyDto>> inProgressDestinationFilter(PolicySearchDto policySearchDto) {
		List<Predicate> predicates = new ArrayList<>();

		String variantVersionSource = commonModuleServiceImpl.getVariantCode(policySearchDto.getProductVariantId());

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
//		if (policySearchDestinationVersionDto.getPolicyStatusId() != null && policySearchDestinationVersionDto.getPolicyStatusId() == 123) {
//			predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), policySearchDestinationVersionDto.getPolicyStatusId()));
//		}

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
		if (policySearchDto.getTransferOutEffectiveDate() != null) {
			Predicate date = criteriaBuilder.between(root.get("date"), policySearchDto.getPolicyStartDate(),
					policySearchDto.getPolicyEndDate());
			predicates.add(date);
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

		List<PolicyDto> policyDestinationVersionDto = new ArrayList<>();
		for (StagingPolicyEntity stagingPolicyEntity : entities) {
			policyDestinationVersionDto.add(PolicyDestinationVersionHelper.entityToVersionDto(
					stagingPolicyCustomDestinationVersionRepository.setTransientValues(stagingPolicyEntity)));

			String variantVersionDestination = commonModuleServiceImpl
					.getVariantCode(stagingPolicyEntity.getProductVariantId());

			if (!((variantVersionSource.equalsIgnoreCase("V1") && variantVersionDestination.equalsIgnoreCase("V1"))
					|| (variantVersionSource.equalsIgnoreCase("V2") && variantVersionDestination.equalsIgnoreCase("V2"))
					|| (variantVersionSource.equalsIgnoreCase("V3") && variantVersionDestination.equalsIgnoreCase("V3"))
					|| (variantVersionSource.equalsIgnoreCase("V1") && variantVersionDestination.equalsIgnoreCase("V3"))
					|| (variantVersionSource.equalsIgnoreCase("V2") && variantVersionDestination.equalsIgnoreCase("V3"))
					|| (variantVersionSource.equalsIgnoreCase("V3")
							&& variantVersionDestination.equalsIgnoreCase("V3")))) {
				return ApiResponseDto.errorMessage(null, null,
						"Source variant version '" + variantVersionSource + "' " + "& destiantion variant version '"
								+ variantVersionDestination + "' doesn't belong to permitted transfer variant version");
			}
			policyDestinationVersionDto.add(PolicyDestinationVersionHelper.entityToVersionDto(
					stagingPolicyCustomDestinationVersionRepository.setTransientValues(stagingPolicyEntity)));
		}

		return ApiResponseDto.success(policyDestinationVersionDto);
	}

	// destination search Policy - Existing
	public ApiResponseDto<List<PolicyDto>> existingDestinationFilter(PolicySearchDto policySearchDto) {
		List<Predicate> predicates = new ArrayList<>();
		String variantVersionSource = commonModuleServiceImpl.getVariantCode(policySearchDto.getProductVariantId());

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MasterPolicyEntity> createQuery = criteriaBuilder.createQuery(MasterPolicyEntity.class);
		/*
		 * CriteriaQuery<TransferMemberPolicyDetailEntity> createQuery1 =
		 * criteriaBuilder .createQuery(TransferMemberPolicyDetailEntity.class);
		 */

		Root<MasterPolicyEntity> root = createQuery.from(MasterPolicyEntity.class);
		/*
		 * Root<TransferMemberPolicyDetailEntity> root1 =
		 * createQuery.from(TransferMemberPolicyDetailEntity.class);
		 */

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

		List variantVersionList = Arrays.asList(new String[] { "33", "45", "46" });
		predicates.add(root.get("productVariantId").in(variantVersionList));

		if (policySearchDto.getUnitOffice() != null && policySearchDto.getUnitOffice() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("unitOffice"), policySearchDto.getUnitOffice()));
		}
//		if (policySearchDto.getPolicyStatus() != null && policySearchDto.getPolicyStatus() > 0) {
//			predicates.add(criteriaBuilder.equal(root.get("policyStatus"), policySearchDto.getPolicyStatus()));
//		}
		if (policySearchDto.getPolicyStatusId() != null && policySearchDto.getPolicyStatusId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), policySearchDto.getPolicyStatusId()));
		}
//		if (policySearchDestinationVersionDto.getPolicyStatusId() != null && policySearchDestinationVersionDto.getPolicyStatusId() ==123) {
//			predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), policySearchDestinationVersionDto.getPolicyStatusId()));
//		}
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
		if (policySearchDto.getTransferOutEffectiveDate() != null) {
			try {
				// predicates.add(criteriaBuilder.between(root1.get("date"),
				// policySearchDto.getPolicyStartDate(),
				predicates.add(
						criteriaBuilder.between(criteriaBuilder.literal(policySearchDto.getTransferOutEffectiveDate()),
								policySearchDto.getPolicyStartDate(), policySearchDto.getPolicyEndDate()));
			} catch (Exception e) {

			}
		}

//		if (policySearchDto.getTransferOutectiveDate()!= null) {
//			Predicate date =  criteriaBuilder.between(policySearchDto.getTransferOutectiveDate(), policySearchDto.getPolicyStartDate(), policySearchDto.getPolicyEndDate());
//			predicates.add(date);
//		}

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
			masterPolicyEntities
					.add(masterPolicyCustomDestinationVersionRepository.setTransientValues(masterPolicyEntity));

			String variantVersionDestination = commonModuleServiceImpl
					.getVariantCode(masterPolicyEntity.getProductVariantId());

			if (!((variantVersionSource.equalsIgnoreCase("V1") && variantVersionDestination.equalsIgnoreCase("V1"))
					|| (variantVersionSource.equalsIgnoreCase("V2") && variantVersionDestination.equalsIgnoreCase("V2"))
					|| (variantVersionSource.equalsIgnoreCase("V3") && variantVersionDestination.equalsIgnoreCase("V3"))
					|| (variantVersionSource.equalsIgnoreCase("V1") && variantVersionDestination.equalsIgnoreCase("V3"))
					|| (variantVersionSource.equalsIgnoreCase("V2") && variantVersionDestination.equalsIgnoreCase("V3"))
					|| (variantVersionSource.equalsIgnoreCase("V3")
							&& variantVersionDestination.equalsIgnoreCase("V3")))) {
				return ApiResponseDto.errorMessage(null, null,
						"Source variant version '" + variantVersionSource + "' " + "& destiantion variant version '"
								+ variantVersionDestination + "' doesn't belong to permitted transfer variant version");
			}

//            if (policySearchDto.getPolicyNumber() ==  policySearchDto.getPolicyNumberOut())       {
//
//                return ApiResponseDto.errorMessage(null, null, "Source Policy Number '" + policySearchDto.getPolicyNumber() + "' "
//                        + "& destiantion Policy Number '" + policySearchDto.getPolicyNumberOut() + "' Both Source and Destination Policy Numbers are same");
//            }
//            masterPolicyEntities
//                    .add(masterPolicyCustomDestinationVersionRepository.setTransientValues(masterPolicyEntity));
		}

		return ApiResponseDto.success(masterPolicyEntities.stream().map(PolicyDestinationVersionHelper::entityToDto1)
				.collect(Collectors.toList()));

	}

	@Override
	public boolean validatePolicyProductVariant(PreValidationRequest preValidationRequest) {

		String sourceProductVariantMst = "";
		String destinationProductVariantMst = "";
		String sourceProductVariant = "";
		String destinationProductVariant = "";

		/** Fetch Master source and destination product variant using Policy Number **/
		if (preValidationRequest.getProductIdOut() != null) {
			CommonMasterVariantEntity commonMasterVariantEntitySource = commonMasterVariantRepository
					.findByProductId(Long.valueOf(preValidationRequest.getProductIdOut()));
			sourceProductVariantMst = commonMasterVariantEntitySource.getVariantVersion();
		}

		if (preValidationRequest.getProductIdIn() != null) {
			CommonMasterVariantEntity commonMasterVariantEntityDest = commonMasterVariantRepository
					.findByProductId(Long.valueOf(preValidationRequest.getProductIdIn()));
			destinationProductVariantMst = commonMasterVariantEntityDest.getVariantVersion();
		}

		/**
		 * Fetch source and destination product variant using Transfer Requisition Id
		 **/
		if (preValidationRequest.getTransferRequisitionId() != null) {
			TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo
					.findByTransferRequisitionId(preValidationRequest.getTransferRequisitionId());
			sourceProductVariant = transferPolicyDetailEntity.getProductVariantOut();
			destinationProductVariant = transferPolicyDetailEntity.getProductVariantIn();

			CommonMasterVariantEntity commonMasterVariantEntitySource = commonMasterVariantRepository
					.findByProductId(transferPolicyDetailEntity.getProductIdOut());
			sourceProductVariantMst = commonMasterVariantEntitySource.getVariantVersion();

			CommonMasterVariantEntity commonMasterVariantEntityDest = commonMasterVariantRepository
					.findByProductId(transferPolicyDetailEntity.getProductIdIn());
			destinationProductVariantMst = commonMasterVariantEntityDest.getVariantVersion();

		}

		if (preValidationRequest.getEventName().equalsIgnoreCase("postSearch")) {

			if (preValidationRequest.getProductVariantOut() != sourceProductVariantMst) {
				return false;
			} else {
				return true;
			}

		} else if (preValidationRequest.getEventName().equalsIgnoreCase("calculation")) {

			if (!preValidationRequest.getProductVariantOut().equalsIgnoreCase(sourceProductVariantMst)) {
				return false;
			} else {
				return true;
			}

		} else if (preValidationRequest.getEventName().equalsIgnoreCase("preSave")) {

			if (!(preValidationRequest.getProductVariantOut().equalsIgnoreCase(sourceProductVariantMst))
					|| !(preValidationRequest.getProductVariantIn().equalsIgnoreCase(destinationProductVariantMst))) {
				return false;
			} else {
				return true;
			}

		} else if (preValidationRequest.getEventName().equalsIgnoreCase("preSendToChecker")) {

			if (!(sourceProductVariant.equalsIgnoreCase(sourceProductVariantMst))
					|| !(destinationProductVariant.equalsIgnoreCase(destinationProductVariantMst))) {
				return false;
			} else {
				return true;
			}

		} else if (preValidationRequest.getEventName().equalsIgnoreCase("checkerApprove")) {

			if (!(sourceProductVariant.equalsIgnoreCase(sourceProductVariantMst))
					|| !(destinationProductVariant.equalsIgnoreCase(destinationProductVariantMst))) {
				return false;
			} else {
				return true;
			}
		} else if (preValidationRequest.getEventName().equalsIgnoreCase("preSendBackToMaker")) {

			if (!(sourceProductVariant.equalsIgnoreCase(sourceProductVariantMst))
					|| !(destinationProductVariant.equalsIgnoreCase(destinationProductVariantMst))) {
				return false;
			} else {
				return true;
			}
		}
		return false;

	}

	@Override
	public boolean validatePolicyUnit(PreValidationRequest preValidationRequest) {

		String sourceUnitCodeMst = "";
		String destinationUnitCodeMst = "";
		String sourceUnitCode = "";
		String destinationUnitCode = "";

		/** Fetch Master source and destination Unit Code using Policy Number **/
		if (preValidationRequest.getPolicyNumberOut() != null) {
			MasterPolicyEntity masterPolicyEntitySource = masterPolicyRepository
					.findByPolicyNumber(preValidationRequest.getPolicyNumberOut());
			sourceUnitCodeMst = masterPolicyEntitySource.getUnitCode();
		}

		if (preValidationRequest.getPolicyNumberIn() != null) {
			MasterPolicyEntity masterPolicyEntityDest = masterPolicyRepository
					.findByPolicyNumber(preValidationRequest.getPolicyNumberIn());
			destinationUnitCodeMst = masterPolicyEntityDest.getUnitCode();
		}

		/**
		 * Fetch source and destination product variant using Transfer Requisition Id
		 **/
		if (preValidationRequest.getTransferRequisitionId() != null) {
			TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo
					.findByTransferRequisitionId(preValidationRequest.getTransferRequisitionId());
			sourceUnitCode = transferPolicyDetailEntity.getUnitOut();
			destinationUnitCode = transferPolicyDetailEntity.getUnitIn();

			MasterPolicyEntity masterPolicyEntitySource = masterPolicyRepository
					.findByPolicyNumber(transferPolicyDetailEntity.getPolicyNumberOut().toString());
			sourceUnitCodeMst = masterPolicyEntitySource.getUnitCode();

			MasterPolicyEntity masterPolicyEntityDest = masterPolicyRepository
					.findByPolicyNumber(transferPolicyDetailEntity.getPolicyNumberIn().toString());
			destinationUnitCodeMst = masterPolicyEntityDest.getUnitCode();
		}

		if (preValidationRequest.getEventName().equalsIgnoreCase("postSearch")) {

			if (preValidationRequest.getUnitCodeOut() != sourceUnitCodeMst) {
				return false;
			} else {
				return true;
			}

		} else if (preValidationRequest.getEventName().equalsIgnoreCase("calculation")) {

			if (!preValidationRequest.getUnitCodeOut().equalsIgnoreCase(sourceUnitCodeMst)) {
				return false;
			} else {
				return true;
			}

		} else if (preValidationRequest.getEventName().equalsIgnoreCase("preSave")) {

			if (!(preValidationRequest.getUnitCodeOut().equalsIgnoreCase(sourceUnitCodeMst))
					|| !(preValidationRequest.getUnitCodeIn().equalsIgnoreCase(destinationUnitCodeMst))) {
				return false;

			} else {
				return true;
			}

		} else if (preValidationRequest.getEventName().equalsIgnoreCase("preSendToChecker")) {

			if (!(sourceUnitCode.equalsIgnoreCase(sourceUnitCodeMst))
					|| !(destinationUnitCode.equalsIgnoreCase(destinationUnitCodeMst))) {
				return false;
			} else {
				return true;
			}

		} else if (preValidationRequest.getEventName().equalsIgnoreCase("checkerApprove")) {

			if (!(sourceUnitCode.equalsIgnoreCase(sourceUnitCodeMst))
					|| !(destinationUnitCode.equalsIgnoreCase(destinationUnitCodeMst))) {
				return false;
			} else {
				return true;
			}

		} else if (preValidationRequest.getEventName().equalsIgnoreCase("preSendBackToMaker")) {

			if (!(sourceUnitCode.equalsIgnoreCase(sourceUnitCodeMst))
					|| !(destinationUnitCode.equalsIgnoreCase(destinationUnitCodeMst))) {
				return false;
			} else {
				return true;
			}
		}
		return false;

	}

	@Override
	public ResponseEntity<?> TransferInCalculate(TransferInCalculateDTO transferInCalculateDTO) {

		Map<String, Object> historyResponse = new HashMap<>();
		Map<String, Object> masterResponse = new HashMap<>();
		Map<String, Object> response = new HashMap<>();
		TransferRequisitionEntity transferRequisitionEntity = transferRequisitionRepo
				.findById(transferInCalculateDTO.getTransferRequisitionId()).get();
		MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository
				.findById(transferInCalculateDTO.getPmstPolicyId());
		PolicyMemberEntity policyMemberEntity = policyMemberRepository
				.findByMemberId(transferInCalculateDTO.getPmstMemberId());
		if (policyMemberEntity == null) {

			response.put("responseMessage", "Member not exist in provided policy");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		List<PolicyHistoryEntity> getPolicyHistoryEntity = policyHistoryRepository
				.findBymasterPolicyId(transferInCalculateDTO.getPmstPolicyId());

		if (getPolicyHistoryEntity.size() > 0) {

			for (PolicyHistoryEntity policyHistoryEntities : getPolicyHistoryEntity) {
				HistoryMemberEntity historyMemberEntity = historyMemberRepository.findBypolicyIdandpmstMemberId(
						policyHistoryEntities.getId(), transferInCalculateDTO.getPmstMemberId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				new Timestamp(System.currentTimeMillis());
				Date dateOfExitwithdateform = null;
				Date dateEnd = null;
				Date dateStart = null;
				Date dateOfJoin = null;
				Date rcd = null;

				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

				// String dateOfExit =
				// transferRequisitionEntity.getTransferRequestDate().toString();
				String str_StartDate = policyHistoryEntities.getPolicyStartDate().toString();
				String str_EndDate = policyHistoryEntities.getPolicyEndDate().toString();
				String str_rcd = masterPolicyEntity.getRiskCommencementDate().toString();
				String str_dateOfJoin = policyMemberEntity.getDojToScheme().toString();
//                DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withLocale(Locale.US);
//                LocalDate dt = LocalDate.parse(dateOfExit, f);

				String str_AnnualRenewlDate = policyHistoryEntities.getAnnualRenewlDate().toString();
				String[] str_SplitARDDate = str_AnnualRenewlDate.split(" ");
				String[] str_SplitEndDate = str_EndDate.split(" ");
				String[] str_SplitStartDate = str_StartDate.split(" ");
				String[] str_SplitRcd = str_rcd.split(" ");
				String[] str_SplitDateOfJoin = str_dateOfJoin.split(" ");
				try {
					dateOfExitwithdateform = sdf.parse(sdf1.format(transferRequisitionEntity.getTransferRequestDate()));
					sdf.parse(str_SplitARDDate[0]);
					dateStart = sdf.parse(str_SplitStartDate[0]);
					dateEnd = sdf.parse(str_SplitEndDate[0]);
					dateOfJoin = sdf.parse(str_SplitDateOfJoin[0]);
					rcd = sdf.parse(str_SplitRcd[0]);

				} catch (Exception e) {
				}

				// if (dateOfExitwithdateform.compareTo(dateARD) < 0) {
				if (dateOfExitwithdateform.compareTo(dateStart) >= 0
						&& dateOfExitwithdateform.compareTo(dateEnd) <= 0) {

					if (dateOfExitwithdateform.compareTo(dateOfJoin) >= 0
							&& dateOfExitwithdateform.compareTo(dateEnd) <= 0) {

						/*
						 * if (transferInCalculateDTO.getModeOfExit() == 193) {
						 * 
						 * if (dateOfExitwithdateform.compareTo(rcd) >= 0 &&
						 * dateOfExitwithdateform.compareTo(dateEnd) <= 0) { } else { return
						 * ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.errorMessage(null,
						 * null,
						 * "Date Of Exit is not Between Risk Commencement Date & Policy End Date"));
						 * 
						 * }
						 * 
						 * }
						 */
						historyResponse = historyForIndividual(transferRequisitionEntity.getTransferRequisitionId(),
								policyHistoryEntities, masterPolicyEntity, policyMemberEntity, transferInCalculateDTO);

					}
//					else {
//						return ApiResponseDto.errorMessage(null, null,
//								"Date Of Exit is not Between Date of Joining to Scheme & Policy End Date");
//					}

				}

//				else {
//					return ApiResponseDto.errorMessage(null,
//							"Date Of Exit is not Between Policy Start Date & Policy End Date",
//							"Date Of Exit should be between Policy Start Date & Policy End Date");
//
//				}

			}

			response.put("calculatedDetails", historyResponse);
			response.put("responseMessage", "Calculation Completed");
			response.put("responseCode", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);

		} else {

			log.info("Policy History Details Not Found");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

			Date dateOfExitwithdateform = null;
			Date dateEnd = null;
			Date dateStart = null;
			Date dateOfJoin = null;
			Date rcd = null;

			// String dateOfExit =
			// transferRequisitionEntity.getTransferRequestDate().toString();
			String str_StartDate = masterPolicyEntity.getPolicyStartDate().toString();
			String str_EndDate = masterPolicyEntity.getPolicyEndDate().toString();
			String str_rcd = masterPolicyEntity.getRiskCommencementDate().toString();
			String str_dateOfJoin = policyMemberEntity.getDojToScheme().toString();
			System.out.println(str_StartDate);
			System.out.println(str_EndDate);
			System.out.println(str_rcd);
			System.out.println(str_dateOfJoin);

//
//			String[] str_SplitDateOfExit = dateOfExit.split(" ");
//            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withLocale(Locale.US);
//            LocalDate dt = LocalDate.parse(dateOfExit, f);

			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

			String str_AnnualRenewlDate = masterPolicyEntity.getAnnualRenewlDate().toString();
			String[] str_SplitARDDate = str_AnnualRenewlDate.split(" ");
			String[] str_SplitStartDate = str_StartDate.split(" ");
			String[] str_SplitEndDate = str_EndDate.split(" ");
			String[] str_SplitRcd = str_rcd.split(" ");
			String[] str_SplitDateOfJoin = str_dateOfJoin.split(" ");
			try {
				dateOfExitwithdateform = sdf.parse(sdf1.format(transferRequisitionEntity.getTransferRequestDate()));
				sdf.parse(str_SplitARDDate[0]);
				dateStart = sdf.parse(str_SplitStartDate[0]);
				dateEnd = sdf.parse(str_SplitEndDate[0]);
				dateOfJoin = sdf.parse(str_SplitDateOfJoin[0]);
				rcd = sdf.parse(str_SplitRcd[0]);

			} catch (Exception e) {
			}

			// if (dateOfExitwithdateform.compareTo(dateARD) < 0) {
			if (dateOfExitwithdateform.compareTo(dateStart) >= 0 && dateOfExitwithdateform.compareTo(dateEnd) <= 0) {

				if (dateOfExitwithdateform.compareTo(dateOfJoin) >= 0
						&& dateOfExitwithdateform.compareTo(dateEnd) <= 0) {

					// *if (transferInCalculateDTO.getModeOfExit() == 193) {

					if (dateOfExitwithdateform.compareTo(rcd) >= 0 && dateOfExitwithdateform.compareTo(dateEnd) <= 0) {
					} else {
						return ResponseEntity.status(HttpStatus.OK)
								.body(ApiResponseDto.errorMessage(null,
										"Date Of Exit is not between Risk Commencement Date & Policy End Date",
										"Date Of Exit should be between Risk Commencement Date & Policy End Date"));

					}

				} // *

				masterResponse = masterPolicyForIndividual(transferRequisitionEntity.getTransferRequisitionId(),
						masterPolicyEntity, policyMemberEntity, transferInCalculateDTO);

			} else {
				return ResponseEntity.status(HttpStatus.OK)
						.body(ApiResponseDto.errorMessage(null,
								"Date Of Exit is not Between Doj to scheme & Policy End Date",
								"Date Of Exit should be between DOJ to Scheme & Policy End Date"));

			}

			response.put("calculatedDetails", masterResponse);
			response.put("responseMessage", "Calculation Completed");
			response.put("responseCode", "success");

			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		/*
		 * if (masterPolicyEntity.getOnboardNumber() == null) {
		 * 
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Timestamp
		 * timeStamp = new Timestamp(System.currentTimeMillis());
		 * 
		 * Date dateOfExitwithdateform = null; Date dateEnd = null; Date dateStart =
		 * null; Date dateOfJoin = null; Date rcd = null;
		 * 
		 * String dateOfExit =
		 * transferRequisitionEntity.getTransferRequestDate().toString(); String
		 * str_StartDate = masterPolicyEntity.getPolicyStartDate().toString(); String
		 * str_EndDate = masterPolicyEntity.getPolicyEndDate().toString(); String
		 * str_rcd = masterPolicyEntity.getRiskCommencementDate().toString(); String
		 * str_dateOfJoin = policyMemberEntity.getDojToScheme().toString();
		 * System.out.println(str_StartDate); System.out.println(str_EndDate);
		 * System.out.println(str_rcd); System.out.println(str_dateOfJoin);
		 * 
		 * // // String[] str_SplitDateOfExit = dateOfExit.split(" "); DateTimeFormatter
		 * f =
		 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withLocale(Locale.US);
		 * LocalDate dt = LocalDate.parse(dateOfExit, f);
		 * 
		 * 
		 * String str_AnnualRenewlDate =
		 * masterPolicyEntity.getAnnualRenewlDate().toString(); String[]
		 * str_SplitARDDate = str_AnnualRenewlDate.split(" "); String[]
		 * str_SplitStartDate = str_StartDate.split(" "); String[] str_SplitEndDate =
		 * str_EndDate.split(" "); String[] str_SplitRcd = str_rcd.split(" "); String[]
		 * str_SplitDateOfJoin = str_dateOfJoin.split(" "); try { dateOfExitwithdateform
		 * = sdf.parse(dt.toString()); sdf.parse(str_SplitARDDate[0]); dateStart =
		 * sdf.parse(str_SplitStartDate[0]); dateEnd = sdf.parse(str_SplitEndDate[0]);
		 * dateOfJoin = sdf.parse(str_SplitDateOfJoin[0]); rcd =
		 * sdf.parse(str_SplitRcd[0]);
		 * 
		 * } catch (Exception e) { }
		 * 
		 * // if (dateOfExitwithdateform.compareTo(dateARD) < 0) { if
		 * (dateOfExitwithdateform.compareTo(dateStart) >= 0 &&
		 * dateOfExitwithdateform.compareTo(dateEnd) <= 0) {
		 * 
		 * if (dateOfExitwithdateform.compareTo(dateOfJoin) >= 0 &&
		 * dateOfExitwithdateform.compareTo(dateEnd) <= 0) {
		 * 
		 *//*
			 * if (transferInCalculateDTO.getModeOfExit() == 193) {
			 * 
			 * if (dateOfExitwithdateform.compareTo(rcd) >= 0 &&
			 * dateOfExitwithdateform.compareTo(dateEnd) <= 0) { } else { return
			 * ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.errorMessage(null,
			 * "Date Of Exit is not between Risk Commencement Date & Policy End Date",
			 * "Date Of Exit should be between Risk Commencement Date & Policy End Date"));
			 * 
			 * }
			 * 
			 * }
			 *//*
				 * 
				 * response = masterPolicyForCreateClaimforIndividual(masterPolicyEntity,
				 * policyMemberEntity, transferInCalculateDTO);
				 * 
				 * } else { return
				 * ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.errorMessage(null,
				 * "Date Of Exit is not Between Doj to scheme & Policy End Date",
				 * "Date Of Exit should be between DOJ to Scheme & Policy End Date"));
				 * 
				 * }
				 * 
				 * } else { return
				 * ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.errorMessage(null,
				 * "Date Of Exit is not Between Policy Start Date & Policy End Date",
				 * "Date Of Exit should be between Policy Start Date & Policy End Date"));
				 * 
				 * } }
				 */
	}

	private Map<String, Object> masterPolicyForIndividual(Long transferRequisitionId,
			MasterPolicyEntity masterPolicyEntity, PolicyMemberEntity policyMemberEntity,
			TransferInCalculateDTO transferInCalculateDTO) {
		Map<String, Object> response = new HashMap<>();
		try {
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
					.copytoTmpForClaim(masterPolicyEntity);
			renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
			/*
			 * PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
			 * 
			 * policyServiceEntitiy.setServiceType("Transfer");
			 * policyServiceEntitiy.setPolicyId(transferInCalculateDTO.getPmstPolicyId());
			 * policyServiceEntitiy.setCreatedBy(transferInCalculateDTO.getCreatedBy());
			 * policyServiceEntitiy.setCreatedDate(new Date());
			 * policyServiceEntitiy.setIsActive(true); policyServiceEntitiy =
			 * policyServiceRepository.save(policyServiceEntitiy);
			 * 
			 * renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
			 * 
			 * renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
			 */

			MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
			TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytomastertoTemp(renewalPolicyTMPEntity.getId(),
					mPHEntity);
			tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
			renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
			renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

			Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
					.findBypolicyId(transferInCalculateDTO.getPmstPolicyId());
			RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
					.copyToTmpSchemeforClaim(policySchemeEntity, renewalPolicyTMPEntity.getId());
			renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

			List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
					.findBypmstPolicyId(transferInCalculateDTO.getPmstPolicyId());
			List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
					.copyToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
			memberCategoryRepository.saveAll(getmemberCategoryEntity);
			List<MemberCategoryModuleEntity> addMemberCategoryModuleEntity = new ArrayList<MemberCategoryModuleEntity>();

			for (MemberCategoryEntity getmemberCategoryEntities : memberCategoryEntity) {
				MemberCategoryModuleEntity memberCategoryModuleEntity = new MemberCategoryModuleEntity();
				memberCategoryModuleEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
				memberCategoryModuleEntity.setMemberCategoryId(getmemberCategoryEntities.getId());
				memberCategoryModuleEntity.setPolicyId(getmemberCategoryEntities.getPmstPolicyId());
				memberCategoryModuleEntity.setCreatedBy(getmemberCategoryEntities.getCreatedBy());
				memberCategoryModuleEntity.setCreatedDate(new Date());
				memberCategoryModuleEntity.setIsActive(true);
				addMemberCategoryModuleEntity.add(memberCategoryModuleEntity);
			}
			memberCategoryModuleRepository.saveAll(addMemberCategoryModuleEntity);

			List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
					.findByPolicyId(transferInCalculateDTO.getPmstPolicyId());

			List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
					.copyToTmpLifeCoverforClaim(policyLifeCoverEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());

			renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
//		policyLifeCoverRepository.updateISActive(tempPolicyClaimPropsDto.getPmstPolicyId());

			List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
					.findBypolicyId(transferInCalculateDTO.getPmstPolicyId());

			List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
					.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());

			renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
//		policyGratuityBenefitRepository.updateIsActive(tempPolicyClaimPropsDto.getPmstPolicyId());

			Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
					.findByPolicyId(transferInCalculateDTO.getPmstPolicyId());
			RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
					.copyToTmpValuationforClaim(policyValuationEntity, renewalPolicyTMPEntity.getId());
			renewalValuationTMPRepository.save(renewalValuationTMPEntity);

			Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
					.findByPolicyId(transferInCalculateDTO.getPmstPolicyId());
			RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
					.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity, renewalPolicyTMPEntity.getId());
			renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

			Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
					.findByPolicyId(transferInCalculateDTO.getPmstPolicyId());
			RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
					.copyToTmpValuationBasicClaim(policyValutationBasicEntity, renewalPolicyTMPEntity.getId());
			renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

			List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
					.findByPolicyId(transferInCalculateDTO.getPmstPolicyId());
			List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
					.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity,
							renewalPolicyTMPEntity.getId());
			renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);

			RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
					.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);

			Double modfdPremRateCrdbiltyFctr = 0.0;

			if (renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr() != null
					&& renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr() != 0) {
				modfdPremRateCrdbiltyFctr = renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr();
			} else {
				modfdPremRateCrdbiltyFctr = renewalValuationBasicTMPEntity.getModfdPremRateCrdbiltyFctr();

			}
			List<GratuityCalculationEntity> gratuityCalculationEntities;
			if (modfdPremRateCrdbiltyFctr != null && modfdPremRateCrdbiltyFctr > 0) {
				gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationEntities = gratuityCalculationRepository
						.findAllByTmpPolicyIdForRwl(renewalPolicyTMPEntity.getId());
			} else {
				gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationEntities = gratuityCalculationRepository
						.findAllByTmpPolicyIdForNew(renewalPolicyTMPEntity.getId());

			}
			/*
			 * TempPolicyClaimPropsEntity getTempPolicyClaimPropsEntity = new
			 * TempPolicyClaimPropsEntity(); getTempPolicyClaimPropsEntity =
			 * PolicyClaimCommonHelper.copytotmpforclaim(renewalPolicyTMPMemberEntity.getId(
			 * ), renewalPolicyTMPEntity.getId(), transferInCalculateDTO);
			 * getTempPolicyClaimPropsEntity
			 * .setOnboardNumber(tempPolicyClaimPropsRepository.getSequence(HttpConstants.
			 * ONBORDING_MODULE));
			 * getTempPolicyClaimPropsEntity.setClaimStatusId(onboardedId);
			 * getTempPolicyClaimPropsEntity.setOnboardingDate(new Date());
			 * tempPolicyClaimPropsRepository.save(getTempPolicyClaimPropsEntity);
			 */
			/*
			 * GratuityCalculationEntity gratuityCalculationEntity =
			 * gratuityCalculationRepository.findByMemberId(renewalPolicyTMPMemberEntity.
			 * getPmstMemebrId());
			 */
			TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo
					.findByTransferRequisitionId(transferRequisitionId);
			if (transferPolicyDetailEntity != null) {
				transferPolicyDetailEntity.setTempPolicyId(renewalPolicyTMPMemberEntity.getTmpPolicyId());

				transferPolicyDetailRepo.save(transferPolicyDetailEntity);
			}
			log.info("LIC Premium" + gratuityCalculationEntities.get(0).getLcPremium());
			Optional<RenewalValuationMatrixTMPEntity> valuationMatrixEntity = renewalValuationMatrixTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			RenewalValuationMatrixTMPEntity newValuationMatrixEntity = null;
			if (valuationMatrixEntity.isPresent()) {
				newValuationMatrixEntity = valuationMatrixEntity.get();
				newValuationMatrixEntity.setModifiedBy(transferPolicyDetailEntity.getModifiedBy());
				newValuationMatrixEntity.setModifiedDate(new Date());
			} else {
				newValuationMatrixEntity = new RenewalValuationMatrixTMPEntity();
				newValuationMatrixEntity.setCreatedBy(transferPolicyDetailEntity.getModifiedBy());
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
			Double Lcpremium = 0.0;

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
				if (gratuityCalculationEntity.getLcPremium() != null)
					Lcpremium = Lcpremium + gratuityCalculationEntity.getLcPremium();
			}

			renewalValuationBasicTMPEntity.setAccruedGratuity(accruedGratuity);
			renewalValuationBasicTMPEntity.setPastServiceDeath(pastServiceDeath);
			renewalValuationBasicTMPEntity.setPastServiceWithdrawal(pastServiceWithdrawal);
			renewalValuationBasicTMPEntity.setPastServiceRetirement(pastServiceRetirement);
			renewalValuationBasicTMPEntity.setCurrentServiceDeath(currentServiceDeath);
			renewalValuationBasicTMPEntity.setCurrentServiceWithdrawal(currentServiceWithdrawal);
			renewalValuationBasicTMPEntity.setCurrentServiceRetirement(currentServiceRetirement);
			renewalValuationBasicTMPEntity.setTotalGratuity(totalGratuity);

			renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

			TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = transferMemberPolicyDetailRepo
					.findByTransferRequisitionId(transferRequisitionId);
			Double gstOnPremiumIn = null;
			double lcpremiumTwoDecimal = 0;
			double gstOnPremiumTwoDecimal = 0;
			double accruedGratuityWholeNumber = 0;

			if (transferMemberPolicyDetailEntity != null) {

				BigDecimal decimalNumber = new BigDecimal(accruedGratuity);
				accruedGratuityWholeNumber = decimalNumber.intValue();
				transferMemberPolicyDetailEntity.setAccruedGratuityIn(accruedGratuityWholeNumber);

				lcpremiumTwoDecimal = (double) Math.round(Lcpremium * 100) / 100;
				transferMemberPolicyDetailEntity.setLicPremiumIn(lcpremiumTwoDecimal);

				Optional<StandardCodeEntity> standardCodeEntity = standardCodeRepository.findById(5L);
				gstOnPremiumIn = (Lcpremium * Double.valueOf(standardCodeEntity.get().getValue())) / 100;

				gstOnPremiumTwoDecimal = (double) Math.round(gstOnPremiumIn * 100) / 100;

				transferMemberPolicyDetailEntity.setGstOnPremiumIn(gstOnPremiumTwoDecimal);
				transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);
			}

			response.put("AccruedGratuity", accruedGratuityWholeNumber);
			response.put("LicPremium", lcpremiumTwoDecimal);
			response.put("GstOnPremium", gstOnPremiumTwoDecimal);
			response.put("tempPolicyId", renewalPolicyTMPMemberEntity.getTmpPolicyId());
			response.put("status", "success");
			return response;
		} catch (Exception e) {
			log.info("Something went wrong while copy in tmp from master" + e.getMessage());
			response.put("status", "failed");
			response.put("message", "Something went wrong while copy in tmp from master" + e.getMessage());
			return response;
		}

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

	private Map<String, Object> historyForIndividual(Long transferRequisitionId,
			PolicyHistoryEntity policyHistoryEntities, MasterPolicyEntity masterPolicyEntity,
			PolicyMemberEntity policyMemberEntity, TransferInCalculateDTO transferInCalculateDTO) {
		Map<String, Object> response = new HashMap<>();
		try {
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
					.copytoHistoryForClaim(policyHistoryEntities);
			renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

			/*
			 * PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
			 * 
			 * policyServiceEntitiy.setServiceType("Transfer");
			 * policyServiceEntitiy.setPmstHisPolicyId(policyHistoryEntities.getId());
			 * policyServiceEntitiy.setCreatedBy(transferInCalculateDTO.getCreatedBy());
			 * policyServiceEntitiy.setCreatedDate(new Date());
			 * policyServiceEntitiy.setIsActive(true); policyServiceEntitiy =
			 * policyServiceRepository.save(policyServiceEntitiy);
			 * 
			 * renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
			 * renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
			 */
			HistoryMPHEntity historyMPHEntity = historyMPHRepository
					.findById(policyHistoryEntities.getMasterpolicyHolder()).get();
			TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytoHistoTemp(renewalPolicyTMPEntity.getId(),
					historyMPHEntity);
			tempMPHRepository.save(tempMPHEntity);

			renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
			renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
			Optional<PolicySchemeRuleHistoryEntity> PolicySchemeRuleHistoryEntity = policySchemeRuleHistoryRepository
					.findBypolicyId(policyHistoryEntities.getId());
			RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
					.copyToHistorySchemeforClaim(PolicySchemeRuleHistoryEntity, renewalPolicyTMPEntity.getId());
			renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

			List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
					.findBypmstHisPolicyId(policyHistoryEntities.getId());
			List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
					.copyhisToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
			memberCategoryRepository.saveAll(getmemberCategoryEntity);

			List<MemberCategoryModuleEntity> addMemberCategoryModuleEntity = new ArrayList<MemberCategoryModuleEntity>();

			for (MemberCategoryEntity getmemberCategoryEntities : memberCategoryEntity) {
				MemberCategoryModuleEntity memberCategoryModuleEntity = new MemberCategoryModuleEntity();
				memberCategoryModuleEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
				memberCategoryModuleEntity.setMemberCategoryId(getmemberCategoryEntities.getId());
//						memberCategoryModuleEntity.setPolicyId(getmemberCategoryEntities.getPmstPolicyId());
				memberCategoryModuleEntity.setCreatedBy(getmemberCategoryEntities.getCreatedBy());
				memberCategoryModuleEntity.setCreatedDate(new Date());
				memberCategoryModuleEntity.setIsActive(true);
				addMemberCategoryModuleEntity.add(memberCategoryModuleEntity);
			}
			memberCategoryModuleRepository.saveAll(addMemberCategoryModuleEntity);

			HistoryMemberEntity historyMemberEntity = historyMemberRepository.findBypolicyIdandpmstMemberId(
					policyHistoryEntities.getId(), transferInCalculateDTO.getPmstMemberId());
			RenewalPolicyTMPMemberEntity renewalMemberEntity = PolicyClaimCommonHelper.copyToHistoryMemberforClaim(
					historyMemberEntity, getmemberCategoryEntity, memberCategoryEntity, renewalPolicyTMPEntity.getId());
			renewalPolicyTMPMemberRepository.save(renewalMemberEntity);

			List<HistoryLifeCoverEntity> historyLifeCoverEntity = historyLifeCoverRepository
					.findBypolicyId(policyHistoryEntities.getId());
			List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryLifeCoverforClaim(historyLifeCoverEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);

			List<HistoryGratuityBenefitEntity> historyGratuityBenefitEntity = historyGratutiyBenefitRepository
					.findBypolicyId(policyHistoryEntities.getId());
			List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryGratuityforClaim(historyGratuityBenefitEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);

			Optional<PolicyValuationHistoryEntity> policyValuationHistoryEntity = policyValuationHistoryRepository
					.findBypolicyId(policyHistoryEntities.getId());
			RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryValuationforClaim(policyValuationHistoryEntity, renewalPolicyTMPEntity.getId());
			renewalValuationTMPRepository.save(renewalValuationTMPEntity);

			Optional<PolicyValuationMatrixHistoryEntity> policyValuationMatrixHistoryEntity = policyValuationMatrixHistoryRepository
					.findBypolicyId(policyHistoryEntities.getId());
			RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryValuationMatrixforClaim(policyValuationMatrixHistoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

			Optional<PolicyValuationBasicHistoryEntity> policyValuationBasicHistoryEntity = policyValuationBasicHistoryRepository
					.findBypolicyId(policyHistoryEntities.getId());
			RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryValuationBasicClaim(policyValuationBasicHistoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

			List<PolicyValuationWithdrawalRateHistoryEntity> policyValuationWithdrawalRateHistoryEntity = policyValuationWithdrawalRateHistoryRepository
					.findBypolicyId(policyHistoryEntities.getId());
			List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryValuationWithdrawlClaim(policyValuationWithdrawalRateHistoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);

			RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
					.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);

			Double modfdPremRateCrdbiltyFctr = 0.0;

			if (renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr() != null
					&& renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr() != 0) {
				modfdPremRateCrdbiltyFctr = renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr();
			} else {
				modfdPremRateCrdbiltyFctr = renewalValuationBasicTMPEntity.getModfdPremRateCrdbiltyFctr();
			}

			List<GratuityCalculationEntity> gratuityCalculationEntities;
			if (modfdPremRateCrdbiltyFctr != null && modfdPremRateCrdbiltyFctr > 0) {
				gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationEntities = gratuityCalculationRepository
						.findAllByTmpPolicyIdForRwl(renewalPolicyTMPEntity.getId());
			} else {
				gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationEntities = gratuityCalculationRepository
						.findAllByTmpPolicyIdForNew(renewalPolicyTMPEntity.getId());

			}
			/*
			 * TempPolicyClaimPropsEntity getTempPolicyClaimPropsEntity = new
			 * TempPolicyClaimPropsEntity(); getTempPolicyClaimPropsEntity =
			 * PolicyClaimCommonHelper.copytotmpforclaim(renewalMemberEntity.getId(),
			 * renewalPolicyTMPEntity.getId(), transferInCalculateDTO);
			 * getTempPolicyClaimPropsEntity
			 * .setOnboardNumber(tempPolicyClaimPropsRepository.getSequence(HttpConstants.
			 * ONBORDING_MODULE));
			 * getTempPolicyClaimPropsEntity.setClaimStatusId(onboardedId);
			 * getTempPolicyClaimPropsEntity.setOnboardingDate(new Date());
			 * tempPolicyClaimPropsRepository.save(getTempPolicyClaimPropsEntity);
			 */

			// return
			// ApiResponseDto.created(PolicyClaimHelper.claimpropstoDto(getTempPolicyClaimPropsEntity));
			/*
			 * GratuityCalculationEntity gratuityCalculationEntity =
			 * gratuityCalculationRepository.findByMemberId(renewalPolicyTMPMemberEntity.
			 * getPmstMemebrId());
			 */
			TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo
					.findByTransferRequisitionId(transferRequisitionId);
			if (transferPolicyDetailEntity != null) {
				transferPolicyDetailEntity.setTempPolicyId(renewalPolicyTMPMemberEntity.getTmpPolicyId());

				transferPolicyDetailRepo.save(transferPolicyDetailEntity);
			}
			log.info("LIC Premium" + gratuityCalculationEntities.get(0).getLcPremium());
			Optional<RenewalValuationMatrixTMPEntity> valuationMatrixEntity = renewalValuationMatrixTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			RenewalValuationMatrixTMPEntity newValuationMatrixEntity = null;
			if (valuationMatrixEntity.isPresent()) {
				newValuationMatrixEntity = valuationMatrixEntity.get();
				newValuationMatrixEntity.setModifiedBy(transferPolicyDetailEntity.getModifiedBy());
				newValuationMatrixEntity.setModifiedDate(new Date());
			} else {
				newValuationMatrixEntity = new RenewalValuationMatrixTMPEntity();
				newValuationMatrixEntity.setCreatedBy(transferPolicyDetailEntity.getModifiedBy());
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
			Double Lcpremium = 0.0;

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
				if (gratuityCalculationEntity.getLcPremium() != null)
					Lcpremium = Lcpremium + gratuityCalculationEntity.getLcPremium();
			}

			renewalValuationBasicTMPEntity.setAccruedGratuity(accruedGratuity);
			renewalValuationBasicTMPEntity.setPastServiceDeath(pastServiceDeath);
			renewalValuationBasicTMPEntity.setPastServiceWithdrawal(pastServiceWithdrawal);
			renewalValuationBasicTMPEntity.setPastServiceRetirement(pastServiceRetirement);
			renewalValuationBasicTMPEntity.setCurrentServiceDeath(currentServiceDeath);
			renewalValuationBasicTMPEntity.setCurrentServiceWithdrawal(currentServiceWithdrawal);
			renewalValuationBasicTMPEntity.setCurrentServiceRetirement(currentServiceRetirement);
			renewalValuationBasicTMPEntity.setTotalGratuity(totalGratuity);

			renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

			TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = transferMemberPolicyDetailRepo
					.findByTransferRequisitionId(transferRequisitionId);
			Double gstOnPremiumIn = null;
			if (transferMemberPolicyDetailEntity != null) {
				transferMemberPolicyDetailEntity.setAccruedGratuityIn(accruedGratuity);
				transferMemberPolicyDetailEntity.setLicPremiumIn(Lcpremium);

				Optional<StandardCodeEntity> standardCodeEntity = standardCodeRepository.findById(5L);
				gstOnPremiumIn = (Lcpremium * Double.valueOf(standardCodeEntity.get().getValue())) / 100;
				transferMemberPolicyDetailEntity.setGstOnPremiumIn(gstOnPremiumIn);
				transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);
			}
			double a = gstOnPremiumIn;
			double gstOnPremiumRoundOff = (double) Math.round(a * 100) / 100;

			double b = Lcpremium;
			double licPremiumRoundOff = (double) Math.round(b * 100) / 100;

			response.put("AccruedGratuity", Math.ceil(accruedGratuity));
			response.put("LicPremium", licPremiumRoundOff);
			response.put("GstOnPremium", gstOnPremiumRoundOff);
			response.put("tempPolicyId", renewalPolicyTMPMemberEntity.getTmpPolicyId());
			response.put("status", "success");
			return response;
		} catch (Exception e) {
			log.info("Something went wrong while copy in tmp from History" + e.getMessage());
			response.put("status", "failed");
			response.put("message", "Something went wrong while copy in tmp from History" + e.getMessage());
			return response;
		}
	}

	/**
	 * Bulk : Transfer-In Calculate
	 **/
	@Override
	public ResponseEntity<?> transferInCalculateForBulk(Long transferRequisitionId) {

		Map<String, Object> historyResponse = new HashMap<>();
		Map<String, Object> masterResponse = new HashMap<>();
		Map<String, Object> response = new HashMap<>();
		Long policyId;

		TransferRequisitionEntity transferRequisitionEntity = transferRequisitionRepo.findById(transferRequisitionId)
				.get();
		TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = transferMemberPolicyDetailRepo
				.findByTransferRequisitionId(transferRequisitionId);
		TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo
				.findByTransferRequisitionId(transferRequisitionId);
		policyId = transferPolicyDetailEntity.getPolicyIdIn();

		// Optional<MemberBatchEntity> memberBatchEntity =
		// memberBatchRepository.findById(transferRequisitionEntity.getBatchId());
		// List<MemberBulkStgEntity> memberBulkStgEntityList =
		// memberBulkStgRepository.findbyMemberBatchId(transferRequisitionEntity.getBatchId());

		/** PMST_POLICY **/
		MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(policyId);

		/** QSTG_MEMBER **/
		// List<MemberEntity> memberEntityList =
		// memberRepository.findByMemberBatchId(transferRequisitionEntity.getBatchId());

		/** QSTG_MEMBER_BULK_STG **/
		// List<MemberBulkStgEntity> memberBulkStgEntityList =
		// memberBulkStgRepository.findByMemberBatchIdAndRecordStatus(transferRequisitionEntity.getBatchId(),
		// "G");

		/** PMST_MEMBER **/
		List<PolicyMemberEntity> policyMemberEntityList = policyMemberRepository
				.findMemberByBtachIdAndRecordStatus(transferRequisitionEntity.getBatchId(), "G");
		// PolicyMemberEntity policyMemberEntity =
		// policyMemberRepository.findByMemberId(transferInCalculateDTO.getPmstMemberId());

//        for(MemberEntity memberEntity : memberEntityList) {
//        	
//        	PolicyMemberEntity policyMemberEntity = policyMemberRepository.findByMemberId(memberEntity.getId());        	
//        	if (policyMemberEntity == null) {	        	
//                response.put("responseMessage", "Member not exist in provided policy");
//                response.put("responseCode", "failed");
//                return ResponseEntity.status(HttpStatus.OK).body(response);            
//            }
//        }

		List<PolicyHistoryEntity> getPolicyHistoryEntity = policyHistoryRepository.findBymasterPolicyId(policyId);

		if (getPolicyHistoryEntity.size() > 0) {

			for (PolicyHistoryEntity policyHistoryEntities : getPolicyHistoryEntity) {
//                HistoryMemberEntity historyMemberEntity = historyMemberRepository.findBypolicyIdandpmstMemberId(
//                        policyHistoryEntities.getId(), transferInCalculateDTO.getPmstMemberId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				new Timestamp(System.currentTimeMillis());
				Date dateOfExitwithdateform = null;
				Date dateEnd = null;
				Date dateStart = null;
				Date dateOfJoin = null;
				Date rcd = null;

				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

				// String dateOfExit =
				// transferRequisitionEntity.getTransferRequestDate().toString();
				String str_StartDate = policyHistoryEntities.getPolicyStartDate().toString();
				String str_EndDate = policyHistoryEntities.getPolicyEndDate().toString();
				String str_rcd = masterPolicyEntity.getRiskCommencementDate().toString();
				// String str_dateOfJoin = policyMemberEntity.getDojToScheme().toString();

				String str_AnnualRenewlDate = policyHistoryEntities.getAnnualRenewlDate().toString();
				String[] str_SplitARDDate = str_AnnualRenewlDate.split(" ");
				String[] str_SplitEndDate = str_EndDate.split(" ");
				String[] str_SplitStartDate = str_StartDate.split(" ");
				String[] str_SplitRcd = str_rcd.split(" ");
				// String[] str_SplitDateOfJoin = str_dateOfJoin.split(" ");
				try {
					dateOfExitwithdateform = sdf.parse(sdf1.format(transferRequisitionEntity.getTransferRequestDate()));
					sdf.parse(str_SplitARDDate[0]);
					dateStart = sdf.parse(str_SplitStartDate[0]);
					dateEnd = sdf.parse(str_SplitEndDate[0]);
					// dateOfJoin = sdf.parse(str_SplitDateOfJoin[0]);
					dateOfJoin = sdf.parse(sdf1.format(transferMemberPolicyDetailEntity.getTransferOutEffectiveDate()));
					rcd = sdf.parse(str_SplitRcd[0]);

				} catch (Exception e) {
				}

				if (dateOfExitwithdateform.compareTo(dateStart) >= 0
						&& dateOfExitwithdateform.compareTo(dateEnd) <= 0) {

					if (dateOfExitwithdateform.compareTo(dateOfJoin) >= 0
							&& dateOfExitwithdateform.compareTo(dateEnd) <= 0) {

						historyResponse = historyForBulk(transferRequisitionId, policyHistoryEntities,
								masterPolicyEntity, policyMemberEntityList, policyId);
					}
				}
			}

			response.put("calculatedDetails", historyResponse);
			response.put("responseMessage", "Calculation Completed");
			response.put("responseCode", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);

		} else {

			log.info("Policy History Details Not Found");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

			Date dateOfExitwithdateform = null;
			Date dateEnd = null;
			Date dateStart = null;
			Date dateOfJoin = null;
			Date rcd = null;

			// String dateOfExit =
			// transferRequisitionEntity.getTransferRequestDate().toString();
			String str_StartDate = masterPolicyEntity.getPolicyStartDate().toString();
			String str_EndDate = masterPolicyEntity.getPolicyEndDate().toString();
			String str_rcd = masterPolicyEntity.getRiskCommencementDate().toString();
			// String str_dateOfJoin = policyMemberEntity.getDojToScheme().toString();
			// --need to check

			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

			String str_AnnualRenewlDate = masterPolicyEntity.getAnnualRenewlDate().toString();
			String[] str_SplitARDDate = str_AnnualRenewlDate.split(" ");
			String[] str_SplitStartDate = str_StartDate.split(" ");
			String[] str_SplitEndDate = str_EndDate.split(" ");
			String[] str_SplitRcd = str_rcd.split(" ");
			// String[] str_SplitDateOfJoin = str_dateOfJoin.split(" ");
			try {
				dateOfExitwithdateform = sdf.parse(sdf1.format(transferRequisitionEntity.getTransferRequestDate()));
				sdf.parse(str_SplitARDDate[0]);
				dateStart = sdf.parse(str_SplitStartDate[0]);
				dateEnd = sdf.parse(str_SplitEndDate[0]);
				// dateOfJoin = sdf.parse(str_SplitDateOfJoin[0]);
				dateOfJoin = sdf.parse(sdf1.format(transferMemberPolicyDetailEntity.getTransferOutEffectiveDate()));
				rcd = sdf.parse(str_SplitRcd[0]);

			} catch (Exception e) {
			}

			// if (dateOfExitwithdateform.compareTo(dateARD) < 0) {
			if (dateOfExitwithdateform.compareTo(dateStart) >= 0 && dateOfExitwithdateform.compareTo(dateEnd) <= 0) {

				if (dateOfExitwithdateform.compareTo(dateOfJoin) >= 0
						&& dateOfExitwithdateform.compareTo(dateEnd) <= 0) {

					// *if (transferInCalculateDTO.getModeOfExit() == 193) {

					if (dateOfExitwithdateform.compareTo(rcd) >= 0 && dateOfExitwithdateform.compareTo(dateEnd) <= 0) {
					} else {

						response.put("calculatedDetails", masterResponse);
						response.put("responseMessage",
								"Date Of Exit should be between Risk Commencement Date & Policy End Date");
						response.put("responseCode", "failed");
						return ResponseEntity.status(HttpStatus.OK).body(response);

//                        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.errorMessage(null,
//                                "Date Of Exit is not between Risk Commencement Date & Policy End Date",
//                                "Date Of Exit should be between Risk Commencement Date & Policy End Date"));
					}
				}

				masterResponse = masterPolicyForBulk(transferRequisitionId, masterPolicyEntity, policyMemberEntityList,
						policyId);

			} else {

				response.put("calculatedDetails", masterResponse);
				response.put("responseMessage", "Date Of Exit should be between DOJ to Scheme & Policy End Date");
				response.put("responseCode", "failed");
				return ResponseEntity.status(HttpStatus.OK).body(response);

//                return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.errorMessage(null,
//                        "Date Of Exit is not Between Doj to scheme & Policy End Date",
//                        "Date Of Exit should be between DOJ to Scheme & Policy End Date"));
			}

			response.put("calculatedDetails", masterResponse);
			response.put("responseMessage", "Calculation Completed");
			response.put("responseCode", "success");

			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

	}

	/**
	 * Bulk : masterPolicyForBulk
	 **/
	private Map<String, Object> masterPolicyForBulk(Long transferRequisitionId, MasterPolicyEntity masterPolicyEntity,
			List<PolicyMemberEntity> policyMemberEntityList, Long policyId) {
		Map<String, Object> response = new HashMap<>();
		try {
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
					.copytoTmpForClaim(masterPolicyEntity);
			renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
			/*
			 * PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
			 *
			 * policyServiceEntitiy.setServiceType("Transfer");
			 * policyServiceEntitiy.setPolicyId(transferInCalculateDTO.getPmstPolicyId());
			 * policyServiceEntitiy.setCreatedBy(transferInCalculateDTO.getCreatedBy());
			 * policyServiceEntitiy.setCreatedDate(new Date());
			 * policyServiceEntitiy.setIsActive(true); policyServiceEntitiy =
			 * policyServiceRepository.save(policyServiceEntitiy);
			 *
			 * renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
			 *
			 * renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
			 */

			MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
			TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytomastertoTemp(renewalPolicyTMPEntity.getId(),
					mPHEntity);
			tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
			renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
			renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

			Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository.findBypolicyId(policyId);
			RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
					.copyToTmpSchemeforClaim(policySchemeEntity, renewalPolicyTMPEntity.getId());
			renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

			List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository.findBypmstPolicyId(policyId);
			List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
					.copyToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
			memberCategoryRepository.saveAll(getmemberCategoryEntity);
			List<MemberCategoryModuleEntity> addMemberCategoryModuleEntity = new ArrayList<MemberCategoryModuleEntity>();

			for (MemberCategoryEntity getmemberCategoryEntities : memberCategoryEntity) {
				MemberCategoryModuleEntity memberCategoryModuleEntity = new MemberCategoryModuleEntity();
				memberCategoryModuleEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
				memberCategoryModuleEntity.setMemberCategoryId(getmemberCategoryEntities.getId());
				memberCategoryModuleEntity.setPolicyId(getmemberCategoryEntities.getPmstPolicyId());
				memberCategoryModuleEntity.setCreatedBy(getmemberCategoryEntities.getCreatedBy());
				memberCategoryModuleEntity.setCreatedDate(new Date());
				memberCategoryModuleEntity.setIsActive(true);
				addMemberCategoryModuleEntity.add(memberCategoryModuleEntity);
			}
			memberCategoryModuleRepository.saveAll(addMemberCategoryModuleEntity);

			List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository.findByPolicyId(policyId);

			List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
					.copyToTmpLifeCoverforClaim(policyLifeCoverEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());

			renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
//policyLifeCoverRepository.updateISActive(tempPolicyClaimPropsDto.getPmstPolicyId());

			List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
					.findBypolicyId(policyId);

			List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
					.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());

			renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
//policyGratuityBenefitRepository.updateIsActive(tempPolicyClaimPropsDto.getPmstPolicyId());

			Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
					.findByPolicyId(policyId);
			RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
					.copyToTmpValuationforClaim(policyValuationEntity, renewalPolicyTMPEntity.getId());
			renewalValuationTMPRepository.save(renewalValuationTMPEntity);

			Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
					.findByPolicyId(policyId);
			RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
					.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity, renewalPolicyTMPEntity.getId());
			renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

			Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
					.findByPolicyId(policyId);
			RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
					.copyToTmpValuationBasicClaim(policyValutationBasicEntity, renewalPolicyTMPEntity.getId());
			renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

			List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
					.findByPolicyId(policyId);
			List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
					.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity,
							renewalPolicyTMPEntity.getId());
			renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);

			List<RenewalPolicyTMPMemberEntity> renewalPolicyTMPMemberEntityList = PolicyClaimCommonHelper
					.copyToTmpBulkMember(policyMemberEntityList, getmemberCategoryEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalPolicyTMPMemberRepository.saveAll(renewalPolicyTMPMemberEntityList);

			Double modfdPremRateCrdbiltyFctr = 0.0;

			if (renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr() != null
					&& renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr() != 0) {
				modfdPremRateCrdbiltyFctr = renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr();
			} else {
				modfdPremRateCrdbiltyFctr = renewalValuationBasicTMPEntity.getModfdPremRateCrdbiltyFctr();
			}

			List<GratuityCalculationEntity> gratuityCalculationEntities;
			if (modfdPremRateCrdbiltyFctr != null && modfdPremRateCrdbiltyFctr > 0) {
				gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationEntities = gratuityCalculationRepository
						.findAllByTmpPolicyIdForRwl(renewalPolicyTMPEntity.getId());
			} else {
				gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationEntities = gratuityCalculationRepository
						.findAllByTmpPolicyIdForNew(renewalPolicyTMPEntity.getId());
			}
			/*
			 * TempPolicyClaimPropsEntity getTempPolicyClaimPropsEntity = new
			 * TempPolicyClaimPropsEntity(); getTempPolicyClaimPropsEntity =
			 * PolicyClaimCommonHelper.copytotmpforclaim(renewalPolicyTMPMemberEntity.getId(
			 * ), renewalPolicyTMPEntity.getId(), transferInCalculateDTO);
			 * getTempPolicyClaimPropsEntity
			 * .setOnboardNumber(tempPolicyClaimPropsRepository.getSequence(HttpConstants.
			 * ONBORDING_MODULE));
			 * getTempPolicyClaimPropsEntity.setClaimStatusId(onboardedId);
			 * getTempPolicyClaimPropsEntity.setOnboardingDate(new Date());
			 * tempPolicyClaimPropsRepository.save(getTempPolicyClaimPropsEntity);
			 */
			TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo
					.findByTransferRequisitionId(transferRequisitionId);

			if (transferPolicyDetailEntity != null) {
				transferPolicyDetailEntity.setTempPolicyId(renewalPolicyTMPMemberEntityList.get(0).getTmpPolicyId());
				transferPolicyDetailRepo.save(transferPolicyDetailEntity);
			}
			/*
			 * GratuityCalculationEntity gratuityCalculationEntity =
			 * gratuityCalculationRepositoryImpl.getGratCalculatedValues(
			 * renewalPolicyTMPMemberEntityList);
			 */

			Optional<RenewalValuationMatrixTMPEntity> valuationMatrixEntity = renewalValuationMatrixTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			RenewalValuationMatrixTMPEntity newValuationMatrixEntity = null;
			if (valuationMatrixEntity.isPresent()) {
				newValuationMatrixEntity = valuationMatrixEntity.get();
				newValuationMatrixEntity.setModifiedBy(transferPolicyDetailEntity.getModifiedBy());
				newValuationMatrixEntity.setModifiedDate(new Date());
			} else {
				newValuationMatrixEntity = new RenewalValuationMatrixTMPEntity();
				newValuationMatrixEntity.setCreatedBy(transferPolicyDetailEntity.getModifiedBy());
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
			Double Lcpremium = 0.0;
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
				if (gratuityCalculationEntity.getLcPremium() != null)
					Lcpremium = Lcpremium + gratuityCalculationEntity.getLcPremium();
			}

			renewalValuationBasicTMPEntity.setAccruedGratuity(accruedGratuity);
			renewalValuationBasicTMPEntity.setPastServiceDeath(pastServiceDeath);
			renewalValuationBasicTMPEntity.setPastServiceWithdrawal(pastServiceWithdrawal);
			renewalValuationBasicTMPEntity.setPastServiceRetirement(pastServiceRetirement);
			renewalValuationBasicTMPEntity.setCurrentServiceDeath(currentServiceDeath);
			renewalValuationBasicTMPEntity.setCurrentServiceWithdrawal(currentServiceWithdrawal);
			renewalValuationBasicTMPEntity.setCurrentServiceRetirement(currentServiceRetirement);
			renewalValuationBasicTMPEntity.setTotalGratuity(totalGratuity);

			renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

			TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = transferMemberPolicyDetailRepo
					.findByTransferRequisitionId(transferRequisitionId);
			BigDecimal acclcpremium = null;
			BigDecimal accGstOnPremiumIn = null;
			if (transferMemberPolicyDetailEntity != null) {

				transferMemberPolicyDetailEntity.setAccruedGratuityIn(Math.ceil(accruedGratuity));
				acclcpremium = BigDecimal.valueOf(Lcpremium).setScale(2, RoundingMode.HALF_UP);
				transferMemberPolicyDetailEntity.setLicPremiumIn(acclcpremium.doubleValue());

				Optional<StandardCodeEntity> standardCodeEntity = standardCodeRepository.findById(5L);
				accGstOnPremiumIn = acclcpremium.multiply(new BigDecimal(standardCodeEntity.get().getValue()))
						.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN);
				transferMemberPolicyDetailEntity.setGstOnPremiumIn(accGstOnPremiumIn.doubleValue());
				transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);
			}

			response.put("AccruedGratuity", Math.ceil(accruedGratuity));
			response.put("LicPremium", acclcpremium);
			response.put("GstOnPremium", accGstOnPremiumIn);
			response.put("tempPolicyId", renewalPolicyTMPEntity.getId());
			response.put("status", "success");
			return response;

		} catch (Exception e) {
			log.info("Something went wrong while copy in tmp from master" + e.getMessage());
			response.put("status", "failed");
			response.put("message", "Something went wrong while copy in tmp from master" + e.getMessage());
			return response;
		}

	}

	/**
	 * Bulk : historyForBulk
	 **/
	private Map<String, Object> historyForBulk(Long transferRequisitionId, PolicyHistoryEntity policyHistoryEntities,
			MasterPolicyEntity masterPolicyEntity, List<PolicyMemberEntity> policyMemberEntityList, Long policyId) {
		Map<String, Object> response = new HashMap<>();
		try {
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
					.copytoHistoryForClaim(policyHistoryEntities);
			renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

			/*
			 * PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
			 * 
			 * policyServiceEntitiy.setServiceType("Transfer");
			 * policyServiceEntitiy.setPmstHisPolicyId(policyHistoryEntities.getId());
			 * policyServiceEntitiy.setCreatedBy(transferInCalculateDTO.getCreatedBy());
			 * policyServiceEntitiy.setCreatedDate(new Date());
			 * policyServiceEntitiy.setIsActive(true); policyServiceEntitiy =
			 * policyServiceRepository.save(policyServiceEntitiy);
			 * 
			 * renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
			 * renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
			 */
			HistoryMPHEntity historyMPHEntity = historyMPHRepository
					.findById(policyHistoryEntities.getMasterpolicyHolder()).get();
			TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytoHistoTemp(renewalPolicyTMPEntity.getId(),
					historyMPHEntity);
			tempMPHRepository.save(tempMPHEntity);

			renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
			renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
			Optional<PolicySchemeRuleHistoryEntity> PolicySchemeRuleHistoryEntity = policySchemeRuleHistoryRepository
					.findBypolicyId(policyHistoryEntities.getId());
			RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
					.copyToHistorySchemeforClaim(PolicySchemeRuleHistoryEntity, renewalPolicyTMPEntity.getId());
			renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

			List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
					.findBypmstHisPolicyId(policyHistoryEntities.getId());
			List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
					.copyhisToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
			memberCategoryRepository.saveAll(getmemberCategoryEntity);

			List<MemberCategoryModuleEntity> addMemberCategoryModuleEntity = new ArrayList<MemberCategoryModuleEntity>();

			for (MemberCategoryEntity getmemberCategoryEntities : memberCategoryEntity) {
				MemberCategoryModuleEntity memberCategoryModuleEntity = new MemberCategoryModuleEntity();
				memberCategoryModuleEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
				memberCategoryModuleEntity.setMemberCategoryId(getmemberCategoryEntities.getId());
//						memberCategoryModuleEntity.setPolicyId(getmemberCategoryEntities.getPmstPolicyId());
				memberCategoryModuleEntity.setCreatedBy(getmemberCategoryEntities.getCreatedBy());
				memberCategoryModuleEntity.setCreatedDate(new Date());
				memberCategoryModuleEntity.setIsActive(true);
				addMemberCategoryModuleEntity.add(memberCategoryModuleEntity);
			}
			memberCategoryModuleRepository.saveAll(addMemberCategoryModuleEntity);

			RenewalPolicyTMPMemberEntity renewalMemberEntity = new RenewalPolicyTMPMemberEntity();
			for (PolicyMemberEntity policyMemberEntity : policyMemberEntityList) {

				HistoryMemberEntity historyMemberEntity = historyMemberRepository
						.findBypolicyIdandpmstMemberId(policyHistoryEntities.getId(), policyMemberEntity.getId());
				renewalMemberEntity = PolicyClaimCommonHelper.copyToHistoryMemberforClaim(historyMemberEntity,
						getmemberCategoryEntity, memberCategoryEntity, renewalPolicyTMPEntity.getId());
				renewalPolicyTMPMemberRepository.save(renewalMemberEntity);
			}

			List<HistoryLifeCoverEntity> historyLifeCoverEntity = historyLifeCoverRepository
					.findBypolicyId(policyHistoryEntities.getId());
			List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryLifeCoverforClaim(historyLifeCoverEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);

			List<HistoryGratuityBenefitEntity> historyGratuityBenefitEntity = historyGratutiyBenefitRepository
					.findBypolicyId(policyHistoryEntities.getId());
			List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryGratuityforClaim(historyGratuityBenefitEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);

			Optional<PolicyValuationHistoryEntity> policyValuationHistoryEntity = policyValuationHistoryRepository
					.findBypolicyId(policyHistoryEntities.getId());
			RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryValuationforClaim(policyValuationHistoryEntity, renewalPolicyTMPEntity.getId());
			renewalValuationTMPRepository.save(renewalValuationTMPEntity);

			Optional<PolicyValuationMatrixHistoryEntity> policyValuationMatrixHistoryEntity = policyValuationMatrixHistoryRepository
					.findBypolicyId(policyHistoryEntities.getId());
			RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryValuationMatrixforClaim(policyValuationMatrixHistoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

			Optional<PolicyValuationBasicHistoryEntity> policyValuationBasicHistoryEntity = policyValuationBasicHistoryRepository
					.findBypolicyId(policyHistoryEntities.getId());
			RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryValuationBasicClaim(policyValuationBasicHistoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

			List<PolicyValuationWithdrawalRateHistoryEntity> policyValuationWithdrawalRateHistoryEntity = policyValuationWithdrawalRateHistoryRepository
					.findBypolicyId(policyHistoryEntities.getId());
			List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
					.copyToHistoryValuationWithdrawlClaim(policyValuationWithdrawalRateHistoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);

			List<RenewalPolicyTMPMemberEntity> renewalPolicyTMPMemberEntityList = PolicyClaimCommonHelper
					.copyToTmpBulkMember(policyMemberEntityList, getmemberCategoryEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());

			renewalPolicyTMPMemberRepository.saveAll(renewalPolicyTMPMemberEntityList);

			Double modfdPremRateCrdbiltyFctr = 0.0;

			if (renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr() != null
					&& renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr() != 0) {
				modfdPremRateCrdbiltyFctr = renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr();
			} else {
				modfdPremRateCrdbiltyFctr = renewalValuationBasicTMPEntity.getModfdPremRateCrdbiltyFctr();
			}

			List<GratuityCalculationEntity> gratuityCalculationEntities;
			if (modfdPremRateCrdbiltyFctr != null && modfdPremRateCrdbiltyFctr > 0) {
				gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
						modfdPremRateCrdbiltyFctr.toString());
				gratuityCalculationEntities = gratuityCalculationRepository
						.findAllByTmpPolicyIdForRwl(renewalPolicyTMPEntity.getId());
			} else {
				gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						renewalValuationBasicTMPEntity.getRateTable());
				gratuityCalculationEntities = gratuityCalculationRepository
						.findAllByTmpPolicyIdForNew(renewalPolicyTMPEntity.getId());
			}
			/*
			 * TempPolicyClaimPropsEntity getTempPolicyClaimPropsEntity = new
			 * TempPolicyClaimPropsEntity(); getTempPolicyClaimPropsEntity =
			 * PolicyClaimCommonHelper.copytotmpforclaim(renewalMemberEntity.getId(),
			 * renewalPolicyTMPEntity.getId(), transferInCalculateDTO);
			 * getTempPolicyClaimPropsEntity
			 * .setOnboardNumber(tempPolicyClaimPropsRepository.getSequence(HttpConstants.
			 * ONBORDING_MODULE));
			 * getTempPolicyClaimPropsEntity.setClaimStatusId(onboardedId);
			 * getTempPolicyClaimPropsEntity.setOnboardingDate(new Date());
			 * tempPolicyClaimPropsRepository.save(getTempPolicyClaimPropsEntity);
			 */

			// return
			// ApiResponseDto.created(PolicyClaimHelper.claimpropstoDto(getTempPolicyClaimPropsEntity));

			TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo
					.findByTransferRequisitionId(transferRequisitionId);

			if (transferPolicyDetailEntity != null) {
				transferPolicyDetailEntity.setTempPolicyId(renewalPolicyTMPMemberEntityList.get(0).getTmpPolicyId());
				transferPolicyDetailRepo.save(transferPolicyDetailEntity);
			}
			/*
			 * GratuityCalculationEntity gratuityCalculationEntity =
			 * gratuityCalculationRepositoryImpl.getGratCalculatedValues(
			 * renewalPolicyTMPMemberEntityList);
			 */

			Optional<RenewalValuationMatrixTMPEntity> valuationMatrixEntity = renewalValuationMatrixTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			RenewalValuationMatrixTMPEntity newValuationMatrixEntity = null;
			if (valuationMatrixEntity.isPresent()) {
				newValuationMatrixEntity = valuationMatrixEntity.get();
				newValuationMatrixEntity.setModifiedBy(transferPolicyDetailEntity.getModifiedBy());
				newValuationMatrixEntity.setModifiedDate(new Date());
			} else {
				newValuationMatrixEntity = new RenewalValuationMatrixTMPEntity();
				newValuationMatrixEntity.setCreatedBy(transferPolicyDetailEntity.getModifiedBy());
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
			Double Lcpremium = 0.0;
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
				if (gratuityCalculationEntity.getLcPremium() != null)
					Lcpremium = Lcpremium + gratuityCalculationEntity.getLcPremium();
			}

			renewalValuationBasicTMPEntity.setAccruedGratuity(accruedGratuity);
			renewalValuationBasicTMPEntity.setPastServiceDeath(pastServiceDeath);
			renewalValuationBasicTMPEntity.setPastServiceWithdrawal(pastServiceWithdrawal);
			renewalValuationBasicTMPEntity.setPastServiceRetirement(pastServiceRetirement);
			renewalValuationBasicTMPEntity.setCurrentServiceDeath(currentServiceDeath);
			renewalValuationBasicTMPEntity.setCurrentServiceWithdrawal(currentServiceWithdrawal);
			renewalValuationBasicTMPEntity.setCurrentServiceRetirement(currentServiceRetirement);
			renewalValuationBasicTMPEntity.setTotalGratuity(totalGratuity);

			renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

			TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = transferMemberPolicyDetailRepo
					.findByTransferRequisitionId(transferRequisitionId);
			BigDecimal acclcpremium = null;
			BigDecimal accGstOnPremiumIn = null;
			if (transferMemberPolicyDetailEntity != null) {

				transferMemberPolicyDetailEntity.setAccruedGratuityIn(Math.ceil(accruedGratuity));
				acclcpremium = BigDecimal.valueOf(Lcpremium).setScale(2, RoundingMode.HALF_UP);
				transferMemberPolicyDetailEntity.setLicPremiumIn(acclcpremium.doubleValue());

				Optional<StandardCodeEntity> standardCodeEntity = standardCodeRepository.findById(5L);
				accGstOnPremiumIn = acclcpremium.multiply(new BigDecimal(standardCodeEntity.get().getValue()))
						.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN);
				transferMemberPolicyDetailEntity.setGstOnPremiumIn(accGstOnPremiumIn.doubleValue());
				transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);
			}

			response.put("AccruedGratuity", Math.ceil(accruedGratuity));
			response.put("LicPremium", acclcpremium);
			response.put("GstOnPremium", accGstOnPremiumIn);
			response.put("tempPolicyId", renewalPolicyTMPEntity.getId());
			response.put("status", "success");
			return response;

		} catch (Exception e) {
			log.info("Something went wrong while copy in tmp from History" + e.getMessage());
			response.put("status", "failed");
			response.put("message", "Something went wrong while copy in tmp from History" + e.getMessage());
			return response;
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> create(TransferDocumentDto transferDocumentDto) {
		log.info("Entering into MemberTransferInOutServiceImpl : saveTransferDocumnentDetails");
		Map<String, Object> response = new HashMap<String, Object>();
		boolean check;
		try {
			List<TransferDocumentDetailEntity> docDetailsList = new ArrayList<>();

			TransferDocumentDetailEntity transferDocumentDetailEntity = new TransferDocumentDetailEntity();
			for (DocumentRequest deposit : transferDocumentDto.getDocumentRequest()) {
				transferDocumentDetailEntity.setTransferRequisitionId(transferDocumentDto.getTransferRequisitionId());
				transferDocumentDetailEntity.setPicklistItemId(deposit.getDocumentIndex());
				transferDocumentDetailEntity.setIsReceived(deposit.getIsReceived());
				transferDocumentDetailEntity.setIsUploaded(deposit.getIsUploaded());
				transferDocumentDetailEntity.setRemarks(transferDocumentDto.getRemarks());
				transferDocumentDetailEntity.setDocumentIndex(deposit.getDocumentIndex());
				transferDocumentDetailEntity.setCreatedBy(transferDocumentDto.getCreatedBy());
				// transferDocumentDetailEntity.setModifiedBy(deposit.getModifiedBy());
				transferDocumentDetailEntity.setFolderIndexNo(deposit.getFolderIndexNo());
				transferDocumentDetailEntity.setModifiedOn(new Date());
				transferDocumentDetailEntity.setCreatedOn(new Date());
				transferDocumentRepo.saveAndFlush(transferDocumentDetailEntity);
				transferDocumentDetailEntity = new TransferDocumentDetailEntity();
			}

			response.put("transferDocumentsDetails", transferDocumentDto);
			response.put("responseMessage", "Transfer Document details saved successfully.");
			response.put("responseCode", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			log.error("Save Transfer Document Deatils failed due to : " + e.getMessage());
			response.put("responseMessage", "Transfer Document details Failed to Save.");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> edit(TransferDocUpdateDto transferDocUpdateDto) {
		log.info("Entering into MemberTransferInOutServiceImpl : editDocumentDeatilsTransfer");
		Map<String, Object> response = new HashMap<String, Object>();
		boolean check;

		try {
			TransferDocumentDetailEntity transferDocumentDetail = new TransferDocumentDetailEntity();
			for (DocumentRequest documentReq : transferDocUpdateDto.getDocumentRequest()) {

				TransferDocumentDetailEntity transferDocumentDetailEntity = new TransferDocumentDetailEntity();

				transferDocumentDetailEntity = transferDocumentRepo.findByTransferRequisitionIdAndPicklistItemId(
						transferDocUpdateDto.getTransferRequisitionId(), documentReq.getPicklistItemId());
				log.info(transferDocumentDetailEntity);
				if (transferDocumentDetailEntity == null) {

					transferDocumentDetail.setTransferRequisitionId(transferDocUpdateDto.getTransferRequisitionId());
					transferDocumentDetail.setPicklistItemId(documentReq.getPicklistItemId());
					transferDocumentDetail.setIsReceived(documentReq.getIsReceived());
					transferDocumentDetail.setIsUploaded(documentReq.getIsUploaded());
					transferDocumentDetail.setRemarks(transferDocUpdateDto.getRemarks());
					transferDocumentDetail.setDocumentIndex(documentReq.getDocumentIndex());
					transferDocumentDetail.setCreatedBy(transferDocUpdateDto.getCreatedBy());
					transferDocumentDetail.setFolderIndexNo(documentReq.getFolderIndexNo());
					transferDocumentDetail.setModifiedOn(new Date());
					transferDocumentDetail.setCreatedOn(new Date());
					transferDocumentRepo.saveAndFlush(transferDocumentDetail);
					transferDocumentDetail = new TransferDocumentDetailEntity();
				} else {
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

					TransferDocumentDetailEntity transferDocumentDetail1 = new TransferDocumentDetailEntity();

					transferDocumentDetail1 = transferDocumentRepo.findByTransferRequisitionIdAndPicklistItemId(
							transferDocUpdateDto.getTransferRequisitionId(), documentReq.getPicklistItemId());
					log.info(transferDocumentDetail1);

					transferDocumentDetail1.setTransferRequisitionId(transferDocUpdateDto.getTransferRequisitionId());
					transferDocumentDetail1.setPicklistItemId(documentReq.getPicklistItemId());
					transferDocumentDetail1.setIsReceived(documentReq.getIsReceived());
					transferDocumentDetail1.setIsUploaded(documentReq.getIsUploaded());
					transferDocumentDetail1.setRemarks(transferDocUpdateDto.getRemarks());
					transferDocumentDetail1.setDocumentIndex(documentReq.getDocumentIndex());
					transferDocumentDetail1.setFolderIndexNo(documentReq.getFolderIndexNo());
					transferDocumentDetail1.setModifiedOn(new Date());
					transferDocumentRepo.save(transferDocumentDetail1);
				}
			}
			response.put("responseMessage", "Document Details Updated successfully.");
			response.put("responseCode", "success");
		} catch (Exception e) {
			log.error("Update Document transfer detailsa failed due to : " + e.getMessage());
			response.put("responseMessage", "Document Details transfer Failed to Update.");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@Override
	public ResponseEntity<Map<String, Object>> getMphDetails(String policyNumber) {
		log.info("Entering into MemberTransferInOutServiceImpl : getMphDetailsById");
		Map<String, Object> response = new HashMap<String, Object>();

		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findByPolicyNumber(policyNumber);
		// masterpolicyHolder is mphId
		Optional<MPHEntity> mphEntity = mphRepository.findById(masterPolicyEntity.getMasterpolicyHolder());
		// TempMPHEntity tempMPHEntity = tempMPHRepository.findBytmpPolicyId(id).get();
		TemptMPHDto temptMPHDto = MPHHelper.mphEntityToDto(mphEntity.get());

		if (temptMPHDto != null) {
			MphDetailsResponse mphDetailsResponse = new MphDetailsResponse();
			mphDetailsResponse.setMphName(temptMPHDto.getMphName());
			mphDetailsResponse.setBankName(temptMPHDto.getMphBank().get(0).getBankName());
			mphDetailsResponse.setAccountNumber(temptMPHDto.getMphBank().get(0).getAccountNumber().toString());
			mphDetailsResponse.setIfscCode(temptMPHDto.getMphBank().get(0).getIfscCode());

			response.put("mphDetails", mphDetailsResponse);
			response.put("responseMessage", "Data Found");
			response.put("responseCode", "success");
		} else {

			response.put("responseMessage", "Data Not Found");
			response.put("responseCode", "failed");
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public Map<String, Object> uploadDocs(UploadDocReq uploadDocReq) throws JsonProcessingException {

		ResponseEntity<String> uploadStatus = documentUploadServiceImpl.uploadClaimDocs(uploadDocReq);

		Map<String, Object> respone = new HashMap<>();

		ObjectMapper mapper = new ObjectMapper();
		OmniDocumentResponse omniDocsResponseObj = mapper.readValue(uploadStatus.getBody().toString(),
				OmniDocumentResponse.class);

		if (omniDocsResponseObj.getStatus().equals("true")) {
// 			List<TransferDocumentDetailEntity> transferDocumentDetailEntity = transferDocumentRepo.
//                    findByTransferRequisitionId(uploadDocReq.getTransferRequisitionId());
			TransferDocumentDetailEntity transferDocumentDetailEntity = transferDocumentRepo
					.findByTransferRequisitionIdAndPicklistItemId(uploadDocReq.getTransferRequisitionId(),
							uploadDocReq.getPicklistItemId());

			log.info(transferDocumentDetailEntity);

			if (transferDocumentDetailEntity == null) {

				TransferDocumentDetailEntity transferDocumentDetailEntity1 = new TransferDocumentDetailEntity();
				transferDocumentDetailEntity1.setTransferRequisitionId(uploadDocReq.getTransferRequisitionId());
				transferDocumentDetailEntity1.setPicklistItemId(uploadDocReq.getPicklistItemId());
				transferDocumentDetailEntity1.setDocumentIndex(omniDocsResponseObj.getDocumentIndexNo());
				if (omniDocsResponseObj.getDocumentIndexNo() != null) {
					transferDocumentDetailEntity1.setIsUploaded("Y");
				} else {
					transferDocumentDetailEntity1.setIsUploaded("N");
				}
				transferDocumentDetailEntity1.setIsReceived("Y");
				transferDocumentDetailEntity1.setRemarks("Document Uploaded");
				transferDocumentDetailEntity1.setFolderIndexNo(omniDocsResponseObj.getFolderIndexNo());
				transferDocumentDetailEntity1.setCreatedBy(uploadDocReq.getCreatedBy());
				transferDocumentDetailEntity1.setCreatedOn(new Date());
				TransferDocumentDetailEntity savedDoc = transferDocumentRepo.save(transferDocumentDetailEntity1);

				respone.put("returnCode", 0);
				respone.put("responseMessage", "Document Uploaded Succesfully");
				respone.put("transferDocumentDetail", transferDocumentDetailEntity1);
				return respone;

			} else {
//       	 List<TransferDocumentDetailEntity> transferDocumentDetailEntity2 = transferDocumentRepo.
//       			findByTransferRequisitionId(uploadDocReq.getTransferRequisitionId());

				TransferDocumentDetailEntity transferDocumentDetailEntity2 = transferDocumentRepo
						.findByTransferRequisitionIdAndPicklistItemId(uploadDocReq.getTransferRequisitionId(),
								uploadDocReq.getPicklistItemId());

				transferDocumentDetailEntity2.setTransferRequisitionId(uploadDocReq.getTransferRequisitionId());
				transferDocumentDetailEntity2.setPicklistItemId(uploadDocReq.getPicklistItemId());
				transferDocumentDetailEntity2.setDocumentIndex(omniDocsResponseObj.getDocumentIndexNo());
				if (omniDocsResponseObj.getDocumentIndexNo() != null) {
					transferDocumentDetailEntity2.setIsUploaded("Y");
				} else {
					transferDocumentDetailEntity2.setIsUploaded("N");
				}
				transferDocumentDetailEntity2.setIsReceived("Y");
				transferDocumentDetailEntity2.setRemarks("Document Uploaded");
				transferDocumentDetailEntity2.setFolderIndexNo(omniDocsResponseObj.getFolderIndexNo());
				transferDocumentDetailEntity2.setCreatedBy(uploadDocReq.getCreatedBy());
				transferDocumentDetailEntity2.setCreatedOn(new Date());

				transferDocumentRepo.save(transferDocumentDetailEntity2);

				respone.put("returnCode", 0);
				respone.put("responseMessage", "Document Uploaded Succesfully");
				respone.put("transferDocumentDetail", transferDocumentDetailEntity2);
				return respone;
			}

		} else {
			respone.put("returnCode", 1);
			respone.put("responseMessage", "Document Upload Failed, Since " + omniDocsResponseObj.getMessage());
			return respone;
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> removeUploadedDocs(RemoveDocReq removeDocReq)
			throws JsonMappingException, JsonProcessingException {
		Map<String, Object> response = new HashMap<String, Object>();

		ObjectMapper mapper1 = new ObjectMapper();
		ResponseEntity<String> removeUploadResponse = documentUploadServiceImpl
				.removeUploadedDoc(removeDocReq.getFolderIndexNo(), removeDocReq.getDocumentIndex());
		OmniDocumentResponse omniDocsResponseObj = mapper1.readValue(removeUploadResponse.getBody().toString(),
				OmniDocumentResponse.class);

		if (omniDocsResponseObj.getStatus().equals("true")) {

			transferDocumentRepo.removeDocImageUsingDcoumentIndexNo(Long.parseLong(removeDocReq.getDocumentIndex()));
			response.put("responseMessage", "Document deleted Successfully");
			response.put("responseCode", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);

		}

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@Override
	public ResponseEntity<String> getUploadDocs(RemoveDocReq removeDocReq) {

		ResponseEntity<String> uploadStatus = documentUploadServiceImpl
				.getUploadedDocs(removeDocReq.getDocumentIndex());
		return uploadStatus;
	}

	public List<DocumentDetailsResponse> getDocumentDetails(Long transferRequisitionId) {
		List<DocumentDetailsResponse> listResponse = new ArrayList<>();
		try {
			listResponse = searchWithServiceNameAndFilterDao.getDocumentDetails(transferRequisitionId);
		} catch (Exception e) {
		}
		return listResponse;
	}

	@Override
	public ApiResponseDto<AdjustmentPropsDto> transferInSave(PolicyContriDetailDto policyContriDetailDto,
			Long pmstPolicyId) {

		log.info("Entering into MemberTransferInOutServiceImpl : transferInSave");
		/*
		 * int countExitingContriAdj = adjustmentPropsRepository
		 * .findpmstpolicyexitforcontriadjust(pmstPolicyId); if (countExitingContriAdj >
		 * 0) { return ApiResponseDto.errorMessage(null, null, "already existed"); }
		 */
		MasterPolicyEntity masterPolicyEntity = masterPolicyTransferVerRepository.findById(pmstPolicyId).get();
		log.info(masterPolicyEntity);
		TransferPolicyDetailEntity transferPolicyDetailEntity = new TransferPolicyDetailEntity();

		Optional<TransferPolicyDetailEntity> transferPolicyDetails = transferPolicyDetailRepo
				.findById(Long.valueOf(policyContriDetailDto.getTransferRequisitionId()));
		Long masterTmpPolicyId = transferPolicyDetails.get().getTempPolicyId();
		Double getBalance = null;
		Double adjustementAmount = 0.0;
		int year = 0;
		int month = 0;
		String financialYear = null;
		financialYear = DateUtils.getFinancialYear(new Date());

		TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = transferMemberPolicyDetailRepo
				.findByTransferRequisitionId(policyContriDetailDto.getTransferRequisitionId());

// 		PolicyContriDetailEntity policyContriDetailEntity = new ModelMapper()
//  				.map(policyContriDetailDto, PolicyContriDetailEntity.class);
		PolicyContriDetailEntity policyContriDetailEntity = new PolicyContriDetailEntity();
		policyContriDetailEntity.setTmpPolicyId(masterTmpPolicyId);
		policyContriDetailEntity.setCreatedBy(policyContriDetailDto.getCreatedBy());
		policyContriDetailEntity.setCreatedDate(new Date());
		policyContriDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
		policyContriDetailEntity.setEntryType("TR");
		policyContriDetailEntity.setActive(true);
		Double pastService = 0.0;
		Double currentService = 0.0;
		policyContriDetailEntity.setLifePremium(transferMemberPolicyDetailEntity.getLicPremiumIn());

		if (transferMemberPolicyDetailEntity.getIsAccruedGratuityModified() != "Y") {
			policyContriDetailEntity
					.setPastService(Double.valueOf(transferMemberPolicyDetailEntity.getAccruedGratuityExisting()));
		} else {
			policyContriDetailEntity
					.setPastService(Double.valueOf(transferMemberPolicyDetailEntity.getAccruedGratuityNew()));
		}
		policyContriDetailEntity.setGst(transferMemberPolicyDetailEntity.getGstOnPremiumIn());
		policyContriDetailEntity.setCurrentServices(currentService);
		policyContriDetailEntity.setLifePremium(transferMemberPolicyDetailEntity.getLicPremiumIn());
		policyContriDetailEntity.setPolicyId(pmstPolicyId);
		policyContriDetailEntity.setAdjustmentForDate(new Date());
		policyContriDetailEntity.setFinancialYear(financialYear);
		PolicyContriDetailEntity savedContributionDetail = policyContriDetailRepository.save(policyContriDetailEntity);

		masterTmpPolicyId = savedContributionDetail.getTmpPolicyId();
		TransferRequisitionEntity transferRequisitionEntity = transferRequisitionRepo
				.findById(policyContriDetailDto.getTransferRequisitionId()).get();

		if (policyContriDetailDto.getTransferRequisitionId() != null) {
			transferRequisitionEntity.setContributionAdjustmentId(savedContributionDetail.getId());
		}

		transferRequisitionEntity.setContributionAdjustmentId(savedContributionDetail.getId());

		adjustementAmount = (transferMemberPolicyDetailEntity.getLicPremiumIn() != null
				? transferMemberPolicyDetailEntity.getLicPremiumIn()
				: 0.0)
				+ (transferMemberPolicyDetailEntity.getGstOnPremiumIn() != null
						? transferMemberPolicyDetailEntity.getGstOnPremiumIn()
						: 0.0);

		List<ShowDepositLockDto> depositLockDto = new ArrayList<ShowDepositLockDto>();
		List<Date> collectionDate = new ArrayList<>();
		for (DepositAdjustementRequest depositAdjustementRequest : policyContriDetailDto
				.getDepositAdjustementRequest()) {
			if (adjustementAmount != 0) {

				PolicyDeposit policyDeposit = new PolicyDeposit();

				PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();

				if (adjustementAmount < depositAdjustementRequest.getAvailableAmount()) {

					getBalance = depositAdjustementRequest.getAvailableAmount() - adjustementAmount;
					Double T = getBalance * 100;
					Long T1 = Math.round(T);
					getBalance = T1.doubleValue() / 100;
					policyDeposit.setAvailableAmount(getBalance);
					policyDeposit.setAdjustmentAmount(adjustementAmount);
					adjustementAmount = 0.0;
				} else {

					getBalance = adjustementAmount - depositAdjustementRequest.getAvailableAmount();
					Double T = getBalance * 100;
					Long T1 = Math.round(T);
					getBalance = T1.doubleValue() / 100;
					policyDeposit.setAdjustmentAmount(depositAdjustementRequest.getAvailableAmount());
					policyDeposit.setAvailableAmount(0.0);
					adjustementAmount = getBalance;
				}
				policyDeposit.setActive(true);
				Random r = new Random(System.currentTimeMillis());
				int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
				policyDeposit.setChallanNo(policyContriDetailEntity.getChallanNo());
				policyDeposit.setCollectionDate(new Date());
				collectionDate.add(policyDeposit.getCollectionDate());
				policyDeposit.setZeroId(null);
				policyDeposit.setChequeRealistionDate(depositAdjustementRequest.getVoucherEffectiveDate());
				policyDeposit.setTransactionMode(depositAdjustementRequest.getCollectionMode());
				policyDeposit.setStatus(null);
				policyDeposit.setRegConId(null);
				policyDeposit.setTmpPolicyId(masterTmpPolicyId);
				policyDeposit.setContributionType("TR");
				policyDeposit.setContributionDetailId(policyContriDetailEntity.getId());
				;
				policyDeposit.setAdjConId(null);
				policyDeposit.setDepositAmount(depositAdjustementRequest.getCollectionAmount());
				policyDeposit.setCreatedBy(depositAdjustementRequest.getCreatedBy());
				policyDeposit.setCreateDate(new Date());
				policyDeposit.setIsDeposit(true);
				policyDeposit.setRemark(depositAdjustementRequest.getRemarks());
				policyDeposit.setCollectionNo(depositAdjustementRequest.getCollectionNumber());
				policyDeposit.setCollectionStatus(depositAdjustementRequest.getCollectionStatus());
				policyDeposit.setActive(true);
				policyDepositRepo.save(policyDeposit);

				ShowDepositLockDto showDepositLockDto = new ShowDepositLockDto();

				String prodAndVarientCodeSame = commonModuleService
						.getProductCode(transferPolicyDetails.get().getProductIdIn());

				showDepositLockDto.setChallanNo(Integer.valueOf(policyContriDetailEntity.getChallanNo()));
				showDepositLockDto.setRefNo(policyDeposit.getId().toString());
				showDepositLockDto.setCollectionNo(policyDeposit.getCollectionNo().toString());
				showDepositLockDto.setUserCode(policyContriDetailDto.getCreatedBy());
				showDepositLockDto.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
				showDepositLockDto.setProductCode(prodAndVarientCodeSame);
				showDepositLockDto.setVariantCode(prodAndVarientCodeSame);

				log.info("Lock Deposit Params: " + showDepositLockDto.toString());
				depositLockDto.add(showDepositLockDto);
				;
			}
		}

		year = Calendar.getInstance().get(Calendar.YEAR);

		month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		System.out.println("Financial month : " + month);
		if (month < 3) {
			financialYear = ((year - 1) + "-" + year);
		} else {
			financialYear = (year + "-" + (year + 1));
		}
		List<PolicyDeposit> policyDepositEntities = policyDepositRepo
				.findBycontributionDetailId(policyContriDetailEntity.getId());

		List<PolicyContributionEntity> policyContributionEntity = new ArrayList<PolicyContributionEntity>();
		List<PolicyAdjustmentDetailEntity> policyAdjustmentDetail = new ArrayList<PolicyAdjustmentDetailEntity>();
		Double depositAmount = 0.0;
		Long depositId = 0l;
		Double currentAmount = 0.0;

		Double premiumAdjustment = transferMemberPolicyDetailEntity.getLicPremiumIn();
		Double gstOnPremiumAdjusted = transferMemberPolicyDetailEntity.getGstOnPremiumIn();
		adjustementAmount = premiumAdjustment + gstOnPremiumAdjusted;
		policyContriDetailDto.setDepositAdjustementRequest(sort(policyContriDetailDto.getDepositAdjustementRequest()));
		Long versionNumber = policyContriRepository.getMaxVersion(financialYear, policyContriDetailEntity.getId());
		versionNumber = versionNumber == null ? 01 : versionNumber + 1;
		for (PolicyDeposit policyDeposit : policyDepositEntities) {
			if (adjustementAmount > 0) {
				Double currentAmountforContribution = 0.0;
				depositAmount = policyDeposit.getDepositAmount();
				depositId = policyDeposit.getId();

				// LIFE PREMIUM NO
				if (premiumAdjustment > 0 && depositAmount > 0) {
					if (premiumAdjustment >= depositAmount) {
						currentAmount = depositAmount;

						premiumAdjustment = premiumAdjustment - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(policyDeposit.getContributionDetailId(),
								depositId, currentAmount, "LifePremium", "LifePremium", "TR",
								policyDeposit.getChequeRealistionDate(), policyContriDetailDto.getCreatedBy()));
						depositAmount = 0.0;
					} else {
						currentAmount = premiumAdjustment;

						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(policyDeposit.getContributionDetailId(),
								depositId, currentAmount, "LifePremium", "LifePremium", "TR",
								policyDeposit.getChequeRealistionDate(), policyContriDetailDto.getCreatedBy()));
						depositAmount = depositAmount - premiumAdjustment;
						premiumAdjustment = 0.0;
					}
				}

				// GST AMOUNT NO

				if (premiumAdjustment == 0 && gstOnPremiumAdjusted > 0 && depositAmount > 0) {
					if (gstOnPremiumAdjusted >= depositAmount) {
						currentAmount = depositAmount;

						gstOnPremiumAdjusted = gstOnPremiumAdjusted - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(policyDeposit.getContributionDetailId(),
								depositId, currentAmount, "Gst", "Gst", "TR", policyDeposit.getChequeRealistionDate(),
								policyContriDetailDto.getCreatedBy()));
						depositAmount = 0.0;
					} else {
						currentAmount = gstOnPremiumAdjusted;

						adjustementAmount = adjustementAmount - currentAmount;
						policyAdjustmentDetail.add(this.saveAdjustmentdata(policyDeposit.getContributionDetailId(),
								depositId, currentAmount, "Gst", "Gst", "TR", policyDeposit.getChequeRealistionDate(),
								policyContriDetailDto.getCreatedBy()));
						depositAmount = depositAmount - gstOnPremiumAdjusted;
						gstOnPremiumAdjusted = 0.0;
					}
				}

				// SinglePremium

				// FirstPremium

				policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
						policyDeposit.getContributionDetailId(), "TR", currentAmountforContribution, financialYear,
						versionNumber, commonModuleService.getVariantCode(transferPolicyDetails.get().getProductIdIn()),
						policyDeposit.getChequeRealistionDate(), policyContriDetailDto.getCreatedBy(),
						policyDeposit.getCollectionDate(), policyDeposit.getCollectionNo(),
						policyDeposit.getChallanNo()));
				versionNumber = versionNumber + 1;
			}
		}
		policyContriRepository.saveAll(policyContributionEntity);
		policyAdjustmentDetailRepo.saveAll(policyAdjustmentDetail);

		if (isDevEnvironment == false) {

			accountingService.lockDeposits(depositLockDto, policyContriDetailDto.getCreatedBy());
		}

		Date risk = Collections.max(collectionDate);
//  		renewalPolicyTMPEntity.setRiskCommencementDate(risk);
//  		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		ContriAdjustmentPropsEntity contributionAdjustmentPropsEntity = null;
		try {
			contributionAdjustmentPropsEntity = new ContriAdjustmentPropsEntity();
			contributionAdjustmentPropsEntity.setAmtToBeAdjusted(
					policyContriDetailEntity.getCurrentServices() + policyContriDetailEntity.getPastService());
			contributionAdjustmentPropsEntity.setPstgContriDetailId(policyContriDetailEntity.getId());
			contributionAdjustmentPropsEntity.setTmpPolicyId(masterTmpPolicyId);
			contributionAdjustmentPropsEntity
					.setContriAdjNumber(commonService.getSequence(HttpConstants.CONTRIBUTION_ADJUSTMENT));
			contributionAdjustmentPropsEntity.setFirstPremiumPS(policyContriDetailEntity.getPastService());
			contributionAdjustmentPropsEntity
					.setSinglePremiumFirstYearCS(policyContriDetailEntity.getCurrentServices());
			contributionAdjustmentPropsEntity.setAdjustmentForDate(new Date());
			contributionAdjustmentPropsEntity.setIsActive(true);
			contributionAdjustmentPropsEntity.setCreatedBy(policyContriDetailDto.getCreatedBy());
			contributionAdjustmentPropsEntity.setCreatedDate(new Date());
			contributionAdjustmentPropsEntity.setId(null);
			contributionAdjustmentPropsEntity.setAdjustmentForDate(new Date());
			contributionAdjustmentPropsEntity = adjustmentPropsRepository.save(contributionAdjustmentPropsEntity);

		} catch (Exception e) {
			log.error("CAProps Error", e);
		}
		return ApiResponseDto.success(ContriHelper.entityToDto(contributionAdjustmentPropsEntity));

	}

	public static List<DepositAdjustementRequest> sort(List<DepositAdjustementRequest> list) {

		list.sort((o1, o2) -> o1.getVoucherEffectiveDate().compareTo(o2.getVoucherEffectiveDate()));

		return list;
	}

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
			Date effectiveDate, String userName, Date Contibutiondate, Long adjustmentNumber, String depositNumber) {
		PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();

		policyContributionEntity.setAdjConid(adjustmentNumber);
		policyContributionEntity.setOpeningBalance(0.0);
		policyContributionEntity.setTotalContribution(currentAmount);
		policyContributionEntity.setClosingBlance(null);
//			policyContributionEntity.setClosingBlance(
//					policyContributionEntity.getOpeningBalance() + policyContributionEntity.getTotalContribution());
		policyContributionEntity.setContReferenceNo(depositNumber);
//			policyContributionEntity.setContributionDate(policyDepositEntity.getCollectionDate());
		policyContributionEntity.setContributionDate(Contibutiondate);
		policyContributionEntity.setCreatedBy(userName);
		policyContributionEntity.setCreateDate(new Date());
		policyContributionEntity.setEmployeeContribution(0.0);
		policyContributionEntity.setEmployerContribution(currentAmount);

		policyContributionEntity.setFinancialYear(financialYear);

		policyContributionEntity.setActive(true);
		policyContributionEntity.setIsDeposit(true);
		policyContributionEntity.setOpeningBalance(0.0);
		if (contributionType.toLowerCase().equals("tr")) {
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
		if (variantCode != "V2") {
			policyContributionEntity.setQuarter(HttpConstants.newBusinessQuarter);
		} else {
			policyContributionEntity.setQuarter(DateUtils.getFinancialQuarterIdentifier(effectiveDate));
		}
		// PMST_POLICY ID
		// for now default Q0

		return policyContributionEntity;
	}

	@Transactional
	@Override
	public ResponseEntity<Map<String, Object>> transferInApprove(Long transferRequisitionId, String userName)
			throws JsonProcessingException {
		log.info("Entering into MemberTransferInOutServiceImpl : transferInApprove : transferRequisitionId : "
				+ transferRequisitionId);

		Map<String, Object> response = new HashMap<String, Object>();
		Long transferRequestNumber;
		Optional<TransferRequisitionEntity> transferRequisitionEntity = transferRequisitionRepo
				.findById(transferRequisitionId);
		TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo
				.findByTransferRequisitionId(transferRequisitionId);
		TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = transferMemberPolicyDetailRepo
				.findByTransferRequisitionId(transferRequisitionId);

		transferRequestNumber = transferRequisitionEntity.get().getTransferRequestNumber();
		Long contributionAdjustmentId = transferRequisitionEntity.get().getContributionAdjustmentId();
		// ContributionAdjustmentPropsEntity contributionAdjustmentPropsEntity
		// =contributionAdjustmentPropsRepository.findById(contributionAdjustmentId).get();

		/** PSTG_CONTRIBUTION_DETAIL **/
		PolicyContributionDetailEntity policyContributionDetailEntity = policyContributionDetailRepository
				.findById(contributionAdjustmentId).get();

		/** PMST_TMP_POLICY **/
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(policyContributionDetailEntity.getTmpPolicyId()).get();

		// Copy to MasterPolicycontributionDetails
		/** PMST_POLICY **/
		MasterPolicyEntity newmasterPolicyEntity = masterPolicyRepository
				.findById(renewalPolicyTMPEntity.getMasterPolicyId()).get();

		/** PMST_CONTRIBUTION_DETAIL **/
		MasterPolicyContributionDetails copyToMaster = PolicyHelper.entityToMaster(
				policyContributionDetailRepository
						.findByTmpPolicyIdandTypeTR(policyContributionDetailEntity.getTmpPolicyId()),
				transferPolicyDetailEntity.getTempPolicyId(), newmasterPolicyEntity.getId());

		copyToMaster = masterPolicyContributionDetailRepository.save(copyToMaster);
		log.info("transferInApprove : copied to MasterPolicycontributionDetails");

		List<PolicyDepositEntity> policyDepositEntites = policyDepositRepository
				.findBycontributionDetailId(policyContributionDetailEntity.getId());
		Date depositEffectiveDate = null;
		for (PolicyDepositEntity stagingDepositData : policyDepositEntites) {

			/** PMST_DEPOSIT **/
			MasterPolicyDepositEntity newmasterPolicyDepositEntity = new ModelMapper().map(stagingDepositData,
					MasterPolicyDepositEntity.class);
			newmasterPolicyDepositEntity.setId(null);
			newmasterPolicyDepositEntity.setMasterPolicyId(newmasterPolicyEntity.getId());
			newmasterPolicyDepositEntity.setActive(true);
			newmasterPolicyDepositEntity.setContributionDetailId(copyToMaster.getId());
			newmasterPolicyDepositEntity.setAvailableAmount(policyDepositEntites.get(0).getAvailableAmount());
			newmasterPolicyDepositEntity = masterPolicyDepositRepository.save(newmasterPolicyDepositEntity);

			/** PSTG_ADJUSTMENT_DETAIL **/
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
				depositEffectiveDate = masterPolicyAdjustmentDetailEntity.getDepositEffectiveDate();
			}

		}

		String financialYear = null;
		financialYear = DateUtils.getFinancialYear(new Date());

		Long versionNumber = masterPolicyContributionRepository.getMaxVersion(financialYear,
				newmasterPolicyEntity.getId());
		versionNumber = versionNumber == null ? 01 : versionNumber + 1;

		// copy Deposit to MasterPolicyContributionEntity
		List<MasterPolicyContributionEntity> masterPolicyContributionEntity = PolicyHelper.entityToPolicyContriMaster(
				versionNumber,
				policyContributionRepository.findByContributionDetailId(policyContributionDetailEntity.getId()),
				newmasterPolicyEntity.getId(), copyToMaster, userName, transferPolicyDetailEntity.getTempPolicyId());
		masterPolicyContributionRepository.saveAll(masterPolicyContributionEntity);
		log.info("transferInApprove : copied deposit to MasterPolicyContribution");

		String prodAndVarientCodeSame = commonModuleService.getProductCode(newmasterPolicyEntity.getProductId());
		String unitStateName = commonMasterUnitRepository.getStateNameByUnitCode(newmasterPolicyEntity.getUnitCode());
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
				.findByTmpPolicyIdandTypeTR(renewalPolicyTMPEntity.getId());
		HSNCodeDto hSNCodeDto = new HSNCodeDto();
		HSNCodeDto hSNCodeDto1;
		// if (isDevEnvironment == false) {
		if (isDevEnvironment == true) {
			log.info("transferInApprove : running environment: " + isDevEnvironment);
			hSNCodeDto.setCgstRate(4);
			hSNCodeDto.setIgstRate(5);
			hSNCodeDto.setSgstRate(7);
			hSNCodeDto.setUtgstRate(8);
			hSNCodeDto.setHsnCode("HSN_1002");
		} else {
			hSNCodeDto1 = accountingService.getHSNCode();
			hSNCodeDto = hSNCodeDto1;
		}

		Map<String, Double> gstComponents = accountingService.getGstComponents(unitStateType, mPHStateType, hSNCodeDto,
				masterPolicyContributiondetailEntity.getLifePremium() == null ? 0.0
						: masterPolicyContributiondetailEntity.getLifePremium());
		String toGSTIn = getMPHEntity.getGstIn() == null ? "" : getMPHEntity.getGstIn();
		ResponseDto responseDto = accountingService.commonmasterserviceAllUnitCode(newmasterPolicyEntity.getUnitCode());

		/** Beneficiary PaymentDetail Model **/
		BeneficiaryPaymentDetailModel beneficiaryPaymentDetailModel = prepareBeneficiaryPaymentDetailModelObject(
				getMPHEntity, transferRequestNumber, transferPolicyDetailEntity, transferMemberPolicyDetailEntity,
				prodAndVarientCodeSame);

		/** Transfer Member DrCr Request Model **/
		TransferMemberDrCrReqModel transferMemberDrCrReqModel = prepareTransferMemberDrCrReqModelObject(
				transferMemberPolicyDetailEntity);

		/** Trn Registration Model **/
		TrnRegisModel trnRegisModel = prepareTrnRegisModelObject(transferMemberPolicyDetailEntity,
				transferPolicyDetailEntity, prodAndVarientCodeSame);

		/** Regular Gst Detail Model **/
		RegularGstDetailModel regularGstDetailModel = prepareRegularGstDetailModelObject(newmasterPolicyEntity,
				masterPolicyContributiondetailEntity, unitStateType, mPHStateType, hSNCodeDto, gstComponents,
				unitStateCode, mphStateCode, toGSTIn, responseDto, mPHAddress, getMPHEntity, prodAndVarientCodeSame,
				policyContributionDetailEntity.getChallanNo());

		/** Reversal Gst Detail Model **/
		ReversalGstDetailModel reversalGstDetailModel = prepareReversalGstDetailModelObject(newmasterPolicyEntity,
				masterPolicyContributiondetailEntity, unitStateType, mPHStateType, hSNCodeDto, gstComponents,
				unitStateCode, mphStateCode, toGSTIn, responseDto, mPHAddress, getMPHEntity, prodAndVarientCodeSame,
				transferMemberPolicyDetailEntity, transferRequestNumber, transferPolicyDetailEntity);

		/** Prepare Accounting Adjustment Payload **/
		TransferMemberReqModel transferMemberReqModel = new TransferMemberReqModel();
		transferMemberReqModel.setRefNo(transferRequestNumber.toString());
		transferMemberReqModel.setMphCode(getMPHEntity.getMphCode());
		transferMemberReqModel.setPolicyNo(transferPolicyDetailEntity.getPolicyNumberIn().toString());
		transferMemberReqModel.setLineOfBusiness("Gratuity");
		transferMemberReqModel.setProduct(prodAndVarientCodeSame);
		transferMemberReqModel.setProductVariant(prodAndVarientCodeSame);
		transferMemberReqModel.setICodeForLob(5);
		transferMemberReqModel.setICodeForProductLine(11);
		transferMemberReqModel.setICodeForVariant("113"); // need to check
		transferMemberReqModel.setICodeForBusinessType(1);
		transferMemberReqModel.setICodeForParticipatingType(1);
		transferMemberReqModel.setICodeForBusinessSegment(1);
		transferMemberReqModel.setICodeForInvestmentPortfolio(1);
		transferMemberReqModel.setPayoutApplicable(true);
		transferMemberReqModel.setUnitCode(transferPolicyDetailEntity.getUnitIn());
		transferMemberReqModel.setUserCode("1");

//		
//		if(newmasterPolicyEntity.getGstApplicableId() == 1l)
//			 gstDetailModelDto.setGstApplicableType("Taxable");
//		else
//			 gstDetailModelDto.setGstApplicableType("Non-Taxable");
//		

		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository
				.findBymasterPolicyIdandEntryType(renewalPolicyTMPEntity.getId());
		// List<RenwalWithGIDebitCreditRequestModelDto>
		// getRenewalWithGiDebitCreditRequestModel =new
		// ArrayList<RenwalWithGIDebitCreditRequestModelDto>();

		/** Prepare request for Accounting API **/
		Map<String, Object> accountingPayload = prepareRequestForAccoutingAPI(transferRequestNumber,
				transferMemberReqModel, transferMemberDrCrReqModel, beneficiaryPaymentDetailModel,
				regularGstDetailModel, trnRegisModel, reversalGstDetailModel, transferPolicyDetailEntity);

		String accountingPayloadJson = new ObjectMapper().writeValueAsString(accountingPayload);
		log.info("Accounting Payload Json - Member Transfer : " + accountingPayloadJson);
		/** Call for Accounting Member Transfer - Adjusmtent API **/
		Map<String, Object> accountingResponse = callToAccountingAPIForAdjustment(accountingPayload);

//		prepareRequestForAccoutingAPI(transferRequestNumber, transferMemberReqModel, transferMemberDrCrReqModel, beneficiaryPaymentDetailModel, 
//				regularGstDetailModel, reversalGstDetailModel, trnRegisModel)

//		String prodAndVarientCodeSame=	commonModuleService.getProductCode(policyEntity.getProductId());
		List<UnlockDepositDetailDto> showDepositLockDto = new ArrayList<UnlockDepositDetailDto>();

		for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {

			UnlockDepositDetailDto depositLockDto = new UnlockDepositDetailDto();
			depositLockDto.setChallanNo(Integer.parseInt(policyDepositEntity.getChallanNo()));
			depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
			// depositLockDto.setUserCode(userName);
			depositLockDto.setUserCode("1");
			depositLockDto.setProductCode(prodAndVarientCodeSame);
			depositLockDto.setVariantCode(prodAndVarientCodeSame);
			showDepositLockDto.add(depositLockDto);
		}

		/** Unlock Deposit **/
		accountingService.unlockDeposits(showDepositLockDto, userName);

		/**
		 * TODO Activate member in destination policy and fetch LIC ID of Source, LIC of
		 * Destination and destination member ID for further api call
		 **/
		PolicyMemberEntity policyMemberEntityDestination = new PolicyMemberEntity();

		/** Individual Member Activation/Deactivation **/
		if (transferRequisitionEntity.get().getIsBulk().equalsIgnoreCase("N")) {
			/** Activate transfered member in destination policy **/
			PolicyMemberEntity policyMemberEntity = policyMemberRepository
					.findByMemberId(transferMemberPolicyDetailEntity.getMemberId());
			if (policyMemberEntity != null) {

				PolicyMemberEntity policyMemberEntitynew = new PolicyMemberEntity();
				// policyMemberEntitynew = new ModelMapper().map(policyMemberEntity,
				// PolicyMemberEntity.class);

				policyMemberEntitynew.setPolicyId(transferPolicyDetailEntity.getPolicyIdIn());
				policyMemberEntitynew
						.setProposalPolicyNumber(transferPolicyDetailEntity.getPolicyNumberIn().toString());
				policyMemberEntitynew.setIsActive(true);
				policyMemberEntitynew.setStatusId(146L);
				policyMemberEntitynew.setCreatedBy(userName);
				policyMemberEntitynew.setCreatedDate(new Date());
				policyMemberEntitynew.setModifiedBy(userName);
				policyMemberEntitynew.setModifiedDate(new Date());

				if (transferMemberPolicyDetailEntity.getRetainLicId() != null
						&& transferMemberPolicyDetailEntity.getIsLicIdExist() != null
						&& transferMemberPolicyDetailEntity.getRetainLicId().equalsIgnoreCase("Y")
						&& transferMemberPolicyDetailEntity.getIsLicIdExist().equalsIgnoreCase("N")) {
					policyMemberEntitynew.setLicId(policyMemberRepository
							.maxPlusOneLicId(transferPolicyDetailEntity.getPolicyNumberIn().toString()));
				} else if (transferMemberPolicyDetailEntity.getRetainLicId() != null
						&& transferMemberPolicyDetailEntity.getIsLicIdExist() != null
						&& transferMemberPolicyDetailEntity.getRetainLicId().equalsIgnoreCase("Y")
						&& transferMemberPolicyDetailEntity.getIsLicIdExist().equalsIgnoreCase("Y")) {
					policyMemberEntitynew.setLicId(policyMemberEntity.getLicId());
				} else {
					policyMemberEntitynew.setLicId(policyMemberEntity.getLicId());
				}

				policyMemberEntitynew.setGratCalculationId(policyMemberEntity.getGratCalculationId());
				policyMemberEntitynew.setEmployeeCode(policyMemberEntity.getEmployeeCode());
				policyMemberEntitynew.setFirstName(policyMemberEntity.getFirstName());
				policyMemberEntitynew.setMiddleName(policyMemberEntity.getMiddleName());
				policyMemberEntitynew.setLastName(policyMemberEntity.getLastName());
				policyMemberEntitynew.setDateOfBirth(policyMemberEntity.getDateOfBirth());
				policyMemberEntitynew.setGenderId(policyMemberEntity.getGenderId());
				policyMemberEntitynew.setPanNumber(policyMemberEntity.getPanNumber());
				policyMemberEntitynew.setAadharNumber(policyMemberEntity.getAadharNumber());
				policyMemberEntitynew.setLandlineNo(policyMemberEntity.getLandlineNo());
				policyMemberEntitynew.setMobileNo(policyMemberEntity.getMobileNo());
				policyMemberEntitynew.setEmailId(policyMemberEntity.getEmailId());
				policyMemberEntitynew.setDateOfAppointment(policyMemberEntity.getDateOfAppointment());
				policyMemberEntitynew.setCategoryId(policyMemberEntity.getCategoryId());
				policyMemberEntitynew.setDesignation(policyMemberEntity.getDesignation());
				policyMemberEntitynew.setBasicSalary(policyMemberEntity.getBasicSalary());
				policyMemberEntitynew.setFatherName(policyMemberEntity.getFatherName());
				policyMemberEntitynew.setSpouseName(policyMemberEntity.getSpouseName());
				policyMemberEntitynew.setLifeCoverPremium(policyMemberEntity.getLifeCoverPremium());
				policyMemberEntitynew.setLifeCoverSumAssured(policyMemberEntity.getLifeCoverSumAssured());
				policyMemberEntitynew.setAccruedGratuity(policyMemberEntity.getAccruedGratuity());
				policyMemberEntitynew.setTotalGratuity(policyMemberEntity.getTotalGratuity());
				policyMemberEntitynew.setValuationDate(policyMemberEntity.getValuationDate());
				policyMemberEntitynew.setCommunicationPreference(policyMemberEntity.getCommunicationPreference());
				policyMemberEntitynew.setLanguagePreference(policyMemberEntity.getLanguagePreference());
				policyMemberEntitynew.setDojToScheme(policyMemberEntity.getDojToScheme());
				policyMemberEntitynew.setSalaryFrequency(policyMemberEntity.getSalaryFrequency());
				policyMemberEntitynew.setRenewalUpdateType(policyMemberEntity.getRenewalUpdateType());
				policyMemberEntitynew.setLastPremiumCollectedDate(policyMemberEntity.getLastPremiumCollectedDate());
				policyMemberEntitynew.setNewLife(policyMemberEntity.getNewLife());
				policyMemberEntityDestination = policyMemberRepository.save(policyMemberEntitynew);

			}

			/** Deactivate member in source policy **/
			policyMemberRepository.deactivateTransferMember(transferMemberPolicyDetailEntity.getMemberId());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("stream", "Gratuity");
		map.put("module", "TRANSFER");
		map.put("sourceMemberId", transferMemberPolicyDetailEntity.getMemberId());
		map.put("destinationMemberId", policyMemberEntityDestination.getId());
		map.put("policyNumber", transferPolicyDetailEntity.getPolicyNumberIn());
		map.put("sourcePolicyId", transferPolicyDetailEntity.getPolicyIdIn());
		map.put("destinationPolicyId", transferPolicyDetailEntity.getPolicyIdOut());
		map.put("updateType", "TOUT");
		Double accruedGratuity = 0.0;
		if (StringUtils.equalsIgnoreCase(transferMemberPolicyDetailEntity.getIsAccruedGratuityModified(), "Y")) {
			accruedGratuity = Double.valueOf(transferMemberPolicyDetailEntity.getAccruedGratuityNew());
		} else {
			accruedGratuity = Double.valueOf(transferMemberPolicyDetailEntity.getAccruedGratuityExisting());
		}
		map.put("txnAmount", accruedGratuity);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		map.put("txnDate", sdf.format(transferRequisitionEntity.get().getTransferRequestDate()));
		map.put("username", userName);
		map.put("destinationLicId", policyMemberEntityDestination.getLicId());
		map.put("sourceLicId", transferMemberPolicyDetailEntity.getLicId());
		map.put("depositDate", sdf.format(depositEffectiveDate));
		map.put("contributionId", copyToMaster.getId());
		callToProcessTransferInOut(map);

		// response.put("adjustmentResponse", accountingResponse);
		response.put("responseMessage", "Approved & Member Transferred Successfully");
		response.put("responseCode", "success");
		response.put("accountingPayloadJson", accountingPayloadJson);
		response.put("accountingResponse", accountingResponse);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	private Map<String, Object> callToProcessTransferInOut(Map<String, Object> map) throws JsonProcessingException {
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, restHeader());
		String url = fundEndpoint + "/services/processMemberTransferOutIn";
		Map<String, Object> response = new HashMap<>();
		log.info("callToProcessTransferInOut : calling FundEndpoint Url : " + url);
		String payload = new ObjectMapper().writeValueAsString(map);
		log.info("callToProcessTransferInOut : " + payload);
		try {

			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

			log.info("Response from Accounting : " + resp);
			response.put("status", "success");
			response.put("data", resp.getBody());
			response.put("status-code", resp.getStatusCodeValue());
			response.put("message", "FundEndpoint Api Call successfully Executed");
			return response;
		} catch (HttpClientErrorException httpClientErrorException) {
			log.error("HTTPClientError Exception occurred while calling FundEndpoint API ", httpClientErrorException);
			response.put("status", "failed");
			response.put("status-code", httpClientErrorException.getStatusCode().value());
			response.put("message",
					"Exception Accounting Api Response : " + httpClientErrorException.getResponseBodyAsString());
			return response;
		} catch (RestClientException restClientException) {
			log.error("Rest Exception occurred while calling FundEndpoint API ", restClientException);
			response.put("status", "failed");
			response.put("status-code", 500);
			response.put("message",
					"Rest Exception occurred while calling AdjustDualPremium API " + restClientException.getMessage());
			return response;
		}
	}

	public Map<String, Object> callToAccountingAPIForAdjustment(Map<String, Object> map)
			throws JsonProcessingException {
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, restHeader());
		String url = accountingServiceEndpoint + "/accountsgratuityservice/ePGS/Accounts/Gratuity/TransferMember";
		Map<String, Object> response = new HashMap<>();
		log.info("Transfer-In Adjustment : calling Accounting Url : " + url);
		String payload = new ObjectMapper().writeValueAsString(map);
		log.info("Payload sent to Accounting : " + payload);
		try {

			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

			log.info("Response from Accounting : " + resp);
			response.put("status", "success");
			response.put("data", resp.getBody());
			response.put("status-code", resp.getStatusCodeValue());
			response.put("message", "Accounting Api Call successfull");
			return response;
		} catch (HttpClientErrorException httpClientErrorException) {
			log.error("HTTPClientError Exception occured while calling AdjustDualPremium API ",
					httpClientErrorException);
			response.put("status", "failed");
			response.put("status-code", httpClientErrorException.getStatusCode().value());
			response.put("message",
					"Exception Accounting Api Response : " + httpClientErrorException.getResponseBodyAsString());
			return response;
		} catch (RestClientException restClientException) {
			log.error("Rest Exception occured while calling AdjustDualPremium API ", restClientException);
			response.put("status", "failed");
			response.put("status-code", 500);
			response.put("message",
					"Rest Exception occurred while calling AdjustDualPremium API " + restClientException.getMessage());
			return response;
		}
	}

	private Map<String, Object> prepareRequestForAccoutingAPI(Long transferRequestNumber,
			TransferMemberReqModel transferMemberReqModel, TransferMemberDrCrReqModel transferMemberDrCrReqModel,
			BeneficiaryPaymentDetailModel beneficiaryPaymentDetailModel, RegularGstDetailModel regularGstDetailModel,
			TrnRegisModel trnRegisModel, ReversalGstDetailModel reversalGstDetailModel,
			TransferPolicyDetailEntity transferPolicyDetailEntity) {
		log.info("Entering into prepareRequestForAccoutingAPI");
		Map<String, Object> map = new HashMap<>();

		map.put("refNo", "TR" + transferRequestNumber.toString());
		map.put("mphCode", transferMemberReqModel.getMphCode());
		map.put("policyNo", transferMemberReqModel.getPolicyNo());
		map.put("lineOfBusiness", transferMemberReqModel.getLineOfBusiness());
		map.put("product", transferMemberReqModel.getProduct());
		map.put("productVariant", transferMemberReqModel.getProductVariant());
		map.put("iCodeForLob", transferMemberReqModel.getICodeForLob());
		map.put("iCodeForProductLine", transferMemberReqModel.getICodeForProductLine());
		map.put("iCodeForVariant", transferMemberReqModel.getICodeForVariant());
		map.put("iCodeForBusinessType", transferMemberReqModel.getICodeForBusinessType());
		map.put("iCodeForParticipatingType", transferMemberReqModel.getICodeForParticipatingType());
		map.put("iCodeForBusinessSegment", transferMemberReqModel.getICodeForBusinessSegment());
		map.put("iCodeForInvestmentPortfolio", transferMemberReqModel.getICodeForInvestmentPortfolio());
		map.put("payoutApplicable", transferMemberReqModel.getPayoutApplicable());
		// map.put("unitCode", transferMemberReqModel.getUnitCode());
		map.put("sourceUnitCode", transferPolicyDetailEntity.getUnitOut());
		map.put("targetUnitCode", transferPolicyDetailEntity.getUnitIn());
		map.put("userCode", transferMemberReqModel.getUserCode());
		map.put("userCode", transferMemberReqModel.getUserCode());
		map.put("isRegularGstApplicable", true);
		map.put("isReversalGstApplicable", true);

		map.put("transferMemberDrCrReqModel", transferMemberDrCrReqModel);
		map.put("beneficiaryPaymentDetailModel", beneficiaryPaymentDetailModel);
		map.put("regularGstDetailModel", regularGstDetailModel);
		map.put("reversalGstDetailModel", reversalGstDetailModel);
		map.put("trnRegisModel", trnRegisModel);

		return map;
	}

	private TransferMemberDrCrReqModel prepareTransferMemberDrCrReqModelObject(
			TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity) {
		log.info("Entering into prepareTransferMemberDrCrReqModelObject");
		TransferMemberDrCrReqModel transferMemberDrCrReqModel = new TransferMemberDrCrReqModel();

		if (transferMemberPolicyDetailEntity.getAccruedGratuityExisting() != null) {
			transferMemberDrCrReqModel
					.setDebitEquitableOutGo(transferMemberPolicyDetailEntity.getAccruedGratuityExisting());
		} else if (transferMemberPolicyDetailEntity.getAccruedGratuityNew() != null) {
			transferMemberDrCrReqModel.setDebitEquitableOutGo(transferMemberPolicyDetailEntity.getAccruedGratuityNew());
		}
		transferMemberDrCrReqModel
				.setDebitReFundFirstPremium(Double.valueOf(transferMemberPolicyDetailEntity.getPremiumAmount()));
		transferMemberDrCrReqModel.setDebitReFundOtherFirstPremium(0);
		transferMemberDrCrReqModel.setDebitReFundRenewalPremium(0);
		transferMemberDrCrReqModel.setDebitGstRefundPremium(transferMemberPolicyDetailEntity.getGstOnPremium());

		Double creditCoPaymentContra = transferMemberPolicyDetailEntity.getPremiumAmount()
				+ transferMemberPolicyDetailEntity.getGstOnPremium();
		BigDecimal premiumGstSum = new BigDecimal(creditCoPaymentContra).setScale(2, RoundingMode.HALF_UP);

		// Split the number into its integer and decimal parts.
		String[] parts = String.valueOf(premiumGstSum).split("\\.");
		double integerPart = Double.parseDouble(parts[0]);
		double decimalPart = Double.parseDouble(parts[1]);

		// Convert the decimal part to a double.
		Double decimalValue = decimalPart / 100;

		if (decimalValue <= 0.50) {
			transferMemberDrCrReqModel.setCreditCoPaymentContra(integerPart);
			BigDecimal creditSr = new BigDecimal(decimalValue).setScale(2, RoundingMode.HALF_UP);
			transferMemberDrCrReqModel.setCreditSr(Double.valueOf(creditSr.toString()));

			/** Pay-out **/
			if (transferMemberPolicyDetailEntity.getIsPremiumRefund().equalsIgnoreCase("Y")) {
				transferMemberDrCrReqModel.setDebitCoPaymentContra(integerPart);
				transferMemberDrCrReqModel.setCreditOsPaymentAtCpc(integerPart);
			} else if (transferMemberPolicyDetailEntity.getIsPremiumRefund().equalsIgnoreCase("N")) {
				transferMemberDrCrReqModel.setDebitCoPaymentContra(0);
				transferMemberDrCrReqModel.setCreditOsPaymentAtCpc(0);
			}

		} else {
			transferMemberDrCrReqModel.setCreditCoPaymentContra(integerPart + 1);
			BigDecimal debitSr = new BigDecimal((integerPart + 1) - creditCoPaymentContra).setScale(2,
					RoundingMode.HALF_UP);
			transferMemberDrCrReqModel.setDebitSr(Double.valueOf(debitSr.toString()));

			/** Pay-out **/
			if (transferMemberPolicyDetailEntity.getIsPremiumRefund().equalsIgnoreCase("Y")) {
				transferMemberDrCrReqModel.setDebitCoPaymentContra(integerPart + 1);
				transferMemberDrCrReqModel.setCreditOsPaymentAtCpc(integerPart + 1);
			} else if (transferMemberPolicyDetailEntity.getIsPremiumRefund().equalsIgnoreCase("N")) {
				transferMemberDrCrReqModel.setDebitCoPaymentContra(0);
				transferMemberDrCrReqModel.setCreditOsPaymentAtCpc(0);
			}
		}

		if (transferMemberPolicyDetailEntity.getAccruedGratuityExisting() != null) {
			transferMemberDrCrReqModel
					.setCreditEquitableContraTransferOut(transferMemberPolicyDetailEntity.getAccruedGratuityExisting());
		} else if (transferMemberPolicyDetailEntity.getAccruedGratuityNew() != null) {
			transferMemberDrCrReqModel
					.setCreditEquitableContraTransferOut(transferMemberPolicyDetailEntity.getAccruedGratuityNew());
		}
		transferMemberDrCrReqModel
				.setDebitEquitableContraTransferIn(transferMemberPolicyDetailEntity.getAccruedGratuityIn());
		transferMemberDrCrReqModel.setCreditEquitableIn(transferMemberPolicyDetailEntity.getAccruedGratuityIn()
				- (transferMemberPolicyDetailEntity.getLicPremiumIn()
						+ transferMemberPolicyDetailEntity.getGstOnPremiumIn()));
		transferMemberDrCrReqModel.setCreditLifePremiumRenewalPremium(0);
		transferMemberDrCrReqModel.setCreditOtherFirstLifePremium(transferMemberPolicyDetailEntity.getLicPremiumIn());
		transferMemberDrCrReqModel.setCreditGstPremium(transferMemberPolicyDetailEntity.getGstOnPremiumIn());

		return transferMemberDrCrReqModel;
	}

	private BeneficiaryPaymentDetailModel prepareBeneficiaryPaymentDetailModelObject(MPHEntity getMPHEntity,
			Long transferRequestNumber, TransferPolicyDetailEntity transferPolicyDetailEntity,
			TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity, String prodAndVarientCodeSame) {
		log.info("Entering into prepareBeneficiaryPaymentDetailModelObject");
		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository
				.findByPolicyNumber(transferPolicyDetailEntity.getPolicyNumberIn().toString());
		Optional<MPHEntity> mphEntity = mphRepository.findById(masterPolicyEntity.getMasterpolicyHolder());
		// MPHBankEntity mphBankEntity =
		// mphBankRepository.findByMasterMph(mphEntity.get().getId());

		String bankName = "";
		String ifscCode = "";
		String bankBranch = "";
		String accountType = "";
		String accountNo = "";
		for (MPHBankEntity mphBankEntity : mphEntity.get().getMphBank()) {
			bankName = mphBankEntity.getBankName();
			ifscCode = mphBankEntity.getIfscCode();
			bankBranch = mphBankEntity.getBankBranch();
			accountType = mphBankEntity.getAccountType();
			accountNo = mphBankEntity.getAccountNumber();
		}

		BeneficiaryPaymentDetailModel beneficiaryPaymentDetailModel = new BeneficiaryPaymentDetailModel();

		beneficiaryPaymentDetailModel.setPayoutSourceModule("Gratuity Tr");
		beneficiaryPaymentDetailModel.setPaymentSourceRefNumber(transferRequestNumber.toString());
		beneficiaryPaymentDetailModel.setBeneficiaryName(mphEntity.get().getMphName());// Need to check
		beneficiaryPaymentDetailModel.setBeneficiaryBankName(bankName);
		beneficiaryPaymentDetailModel.setBeneficiaryBankIfsc(ifscCode);
		beneficiaryPaymentDetailModel.setBeneficiaryBranchName(bankBranch);
		beneficiaryPaymentDetailModel.setBeneficiaryAccountNumber(accountNo);
		beneficiaryPaymentDetailModel.setBeneficiaryAccountType(accountType);
		// beneficiaryPaymentDetailModel.setPaymentAmount(transferMemberPolicyDetailEntity.getLicPremiumIn()
		// + transferMemberPolicyDetailEntity.getGstOnPremiumIn());
		beneficiaryPaymentDetailModel.setPaymentAmount(transferMemberPolicyDetailEntity.getPremiumAmount()
				+ transferMemberPolicyDetailEntity.getGstOnPremium());
		beneficiaryPaymentDetailModel.setEffectiveDateOfPayment(new Date());
		beneficiaryPaymentDetailModel.setPaymentMode("N"); // N-NEFT/ R-RTGS
		beneficiaryPaymentDetailModel.setUnitCode(masterPolicyEntity.getUnitCode());
		beneficiaryPaymentDetailModel.setPolicyNumber(transferPolicyDetailEntity.getPolicyNumberIn().toString());
		beneficiaryPaymentDetailModel.setMemberNumber(transferMemberPolicyDetailEntity.getMemberName());
		beneficiaryPaymentDetailModel.setPaymentCategory("PCM002"); // Need to check hardcoded
		beneficiaryPaymentDetailModel.setPaymentSubCategory("O"); // 0-> OTHERS / DC- Death Claim etc
		beneficiaryPaymentDetailModel.setRemarks("Gratuity Member Transfer Deposit Adjustment");
		beneficiaryPaymentDetailModel.setBeneficiaryPaymentId(transferRequestNumber.toString());
		beneficiaryPaymentDetailModel.setProductCode(prodAndVarientCodeSame);
		beneficiaryPaymentDetailModel.setVariantCode(prodAndVarientCodeSame);
		beneficiaryPaymentDetailModel.setOperatinUnitType("UO");
		return beneficiaryPaymentDetailModel;
	}

	private TrnRegisModel prepareTrnRegisModelObject(TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity,
			TransferPolicyDetailEntity transferPolicyDetailEntity, String prodAndVarientCodeSame) {
		log.info("Entering into prepareTrnRegisModelObject");
		TrnRegisModel trnRegisModel = new TrnRegisModel();

		trnRegisModel.setReferenceIdType("CRI");
		trnRegisModel.setVan("");
		trnRegisModel.setPolicyNo(transferPolicyDetailEntity.getProductNameIn());
		Double amount = transferMemberPolicyDetailEntity.getLicPremiumIn()
				+ transferMemberPolicyDetailEntity.getGstOnPremiumIn();
		trnRegisModel.setAmount(amount.toString());
		trnRegisModel.setAmountType("9");
		trnRegisModel.setProductCode(prodAndVarientCodeSame);
		trnRegisModel.setVariantCode(prodAndVarientCodeSame);
		trnRegisModel.setValidityUptoDate("2099-03-31");
		trnRegisModel.setStatus(2);
		trnRegisModel.setBankUniqueId("");
		trnRegisModel.setReason("Member Transfer");
		return trnRegisModel;
	}

	private RegularGstDetailModel prepareRegularGstDetailModelObject(MasterPolicyEntity newmasterPolicyEntity,
			PolicyContributionDetailEntity masterPolicyContributiondetailEntity, String unitStateType,
			String mPHStateType, HSNCodeDto hSNCodeDto, Map<String, Double> gstComponents, String unitStateCode,
			String mphStateCode, String toGSTIn, ResponseDto responseDto, String mPHAddress, MPHEntity getMPHEntity,
			String prodAndVarientCodeSame, String challanNo) {
		log.info("Entering into prepareRegularGstDetailModelObject");
		RegularGstDetailModel regularGstDetailModel = new RegularGstDetailModel();

		regularGstDetailModel.setTransactionDate(new Date());
		regularGstDetailModel.setGstRefNo(challanNo);
		regularGstDetailModel.setGstRefTransactionNo(newmasterPolicyEntity.getPolicyNumber());
		regularGstDetailModel.setGstTransactionType("DEBIT");
		regularGstDetailModel.setAmountWithTax((masterPolicyContributiondetailEntity.getLifePremium() == null ? 0.0
				: masterPolicyContributiondetailEntity.getLifePremium())
				+ (masterPolicyContributiondetailEntity.getGst() == null ? 0.0
						: masterPolicyContributiondetailEntity.getGst()));
		regularGstDetailModel.setAmountWithoutTax(masterPolicyContributiondetailEntity.getLifePremium() == null ? 0.0
				: masterPolicyContributiondetailEntity.getLifePremium());
		regularGstDetailModel.setCessAmount(0.0);
		regularGstDetailModel.setTotalGstAmount(masterPolicyContributiondetailEntity.getGst() == null ? 0.0
				: masterPolicyContributiondetailEntity.getGst().doubleValue());
		regularGstDetailModel.setGstRate(accountingService.getGSTRate(unitStateType, mPHStateType, hSNCodeDto) * 100);
		regularGstDetailModel.setCgstAmount(gstComponents.get("CGST"));
		regularGstDetailModel.setCgstRate(hSNCodeDto.getCgstRate());
		regularGstDetailModel.setIgstAmount(gstComponents.get("IGST"));
		regularGstDetailModel.setIgstRate(hSNCodeDto.getIgstRate());
		regularGstDetailModel.setSgstAmount(gstComponents.get("SGST"));
		regularGstDetailModel.setSgstRate(hSNCodeDto.getSgstRate());
		regularGstDetailModel.setUtgstAmount(gstComponents.get("UTGST"));
		regularGstDetailModel.setUtgstRate(hSNCodeDto.getUtgstRate());
		if (newmasterPolicyEntity.getGstApplicableId() == 1l)
			regularGstDetailModel.setGstApplicableType("Taxable");
		else
			regularGstDetailModel.setGstApplicableType("Non-Taxable");
		regularGstDetailModel.setGstType("GST");
		regularGstDetailModel.setFromStateCode(unitStateCode);
		regularGstDetailModel.setToStateCode(mphStateCode == null ? "" : mphStateCode);
		regularGstDetailModel.setUserCode("1");
		regularGstDetailModel.setTransactionType("C");
		regularGstDetailModel.setTransactionSubType("A");
		regularGstDetailModel.setGstInvoiceNo("");
		regularGstDetailModel.setEntryType(toGSTIn != null ? "B2B" : "B2C");
		regularGstDetailModel.setFromGstn(responseDto.getGstIn() == null ? "" : responseDto.getGstIn());
		regularGstDetailModel.setFromPan(responseDto.getPan());
		regularGstDetailModel.setHsnCode(hSNCodeDto.getHsnCode());
		regularGstDetailModel.setMphAddress(mPHAddress);
		regularGstDetailModel.setMphName(getMPHEntity.getMphName());
		// regularGstDetailModel.setNatureOfTransaction(hSNCodeDto.getDescription());
		regularGstDetailModel.setNatureOfTransaction("NEFT");
//        regularGstDetailModel.setOldInvoiceDate(new Date());
//        regularGstDetailModel.setOldInvoiceNo("IN20123QE");
		regularGstDetailModel.setProductCode(prodAndVarientCodeSame);
		regularGstDetailModel.setRemarks("Gratuity Member Transfer Deposit Adjustment");
		regularGstDetailModel.setToGstIn(getMPHEntity.getGstIn() == null ? "" : getMPHEntity.getGstIn());
		regularGstDetailModel.setToPan(getMPHEntity.getPan() == null ? "" : getMPHEntity.getPan());
		regularGstDetailModel.setVariantCode(prodAndVarientCodeSame);
		regularGstDetailModel.setYear(DateUtils.uniqueNoYYYY());
		regularGstDetailModel.setMonth(DateUtils.currentMonthName());

		return regularGstDetailModel;
	}

	private ReversalGstDetailModel prepareReversalGstDetailModelObject(MasterPolicyEntity newmasterPolicyEntity,
			PolicyContributionDetailEntity masterPolicyContributiondetailEntity, String unitStateType,
			String mPHStateType, HSNCodeDto hSNCodeDto, Map<String, Double> gstComponents, String unitStateCode,
			String mphStateCode, String toGSTIn, ResponseDto responseDto, String mPHAddress, MPHEntity getMPHEntity,
			String prodAndVarientCodeSame, TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity,
			Long transferRequestNumber, TransferPolicyDetailEntity transferPolicyDetailEntity) {
		log.info("Entering into prepareReversalGstDetailModelObject");
		ReversalGstDetailModel reversalGstDetailModel = new ReversalGstDetailModel();

		reversalGstDetailModel.setTransactionDate(new Date());
		reversalGstDetailModel.setGstRefNo(transferRequestNumber.toString());
		reversalGstDetailModel.setGstRefTransactionNo(transferPolicyDetailEntity.getPolicyNumberOut().toString());
		reversalGstDetailModel.setGstTransactionType("CREDIT");

//        reversalGstDetailModel.setAmountWithTax((masterPolicyContributiondetailEntity.getLifePremium() == null ? 0.0 : masterPolicyContributiondetailEntity.getLifePremium())
//                + (masterPolicyContributiondetailEntity.getGst() == null ? 0.0 : masterPolicyContributiondetailEntity.getGst()));

		reversalGstDetailModel.setAmountWithTax((transferMemberPolicyDetailEntity.getPremiumAmount() == null ? 0.0
				: transferMemberPolicyDetailEntity.getPremiumAmount())
				+ (transferMemberPolicyDetailEntity.getGstOnPremium() == null ? 0.0
						: transferMemberPolicyDetailEntity.getGstOnPremium()));

		// reversalGstDetailModel.setAmountWithoutTax(masterPolicyContributiondetailEntity.getLifePremium()
		// == null ? 0.0 : masterPolicyContributiondetailEntity.getLifePremium());
		reversalGstDetailModel.setAmountWithoutTax(transferMemberPolicyDetailEntity.getPremiumAmount() == null ? 0.0
				: transferMemberPolicyDetailEntity.getPremiumAmount());
		reversalGstDetailModel.setCessAmount(0.0);
		// reversalGstDetailModel.setTotalGstAmount(masterPolicyContributiondetailEntity.getGst()
		// == null ? 0.0 : masterPolicyContributiondetailEntity.getGst().doubleValue());
		reversalGstDetailModel.setTotalGstAmount(transferMemberPolicyDetailEntity.getGstOnPremium() == null ? 0.0
				: transferMemberPolicyDetailEntity.getGstOnPremium().doubleValue());

		reversalGstDetailModel.setGstRate(accountingService.getGSTRate(unitStateType, mPHStateType, hSNCodeDto) * 100);
		reversalGstDetailModel.setCgstAmount(gstComponents.get("CGST"));
		reversalGstDetailModel.setCgstRate(hSNCodeDto.getCgstRate());
		reversalGstDetailModel.setIgstAmount(gstComponents.get("IGST"));
		reversalGstDetailModel.setIgstRate(hSNCodeDto.getIgstRate());
		reversalGstDetailModel.setSgstAmount(gstComponents.get("SGST"));
		reversalGstDetailModel.setSgstRate(hSNCodeDto.getSgstRate());
		reversalGstDetailModel.setUtgstAmount(gstComponents.get("UTGST"));
		reversalGstDetailModel.setUtgstRate(hSNCodeDto.getUtgstRate());
		if (newmasterPolicyEntity.getGstApplicableId() == 1l)
			reversalGstDetailModel.setGstApplicableType("Taxable");
		else
			reversalGstDetailModel.setGstApplicableType("Non-Taxable");
		reversalGstDetailModel.setGstType("GST");
		reversalGstDetailModel.setFromStateCode(unitStateCode);
		reversalGstDetailModel.setToStateCode(mphStateCode == null ? "" : mphStateCode);
		// reversalGstDetailModel.setUserCode(userName);
		reversalGstDetailModel.setTransactionType("C");
		reversalGstDetailModel.setTransactionSubType("A");
		reversalGstDetailModel.setGstInvoiceNo("");
		reversalGstDetailModel.setEntryType(toGSTIn != null ? "B2B" : "B2C");
		reversalGstDetailModel.setFromGstn(responseDto.getGstIn() == null ? "" : responseDto.getGstIn());
		reversalGstDetailModel.setFromPan(responseDto.getPan());
		reversalGstDetailModel.setHsnCode(hSNCodeDto.getHsnCode());
		reversalGstDetailModel.setMphAddress(mPHAddress);
		reversalGstDetailModel.setMphName(getMPHEntity.getMphName());
		reversalGstDetailModel.setNatureOfTransaction(hSNCodeDto.getDescription());
		reversalGstDetailModel.setOldInvoiceDate(new Date());
		reversalGstDetailModel.setOldInvoiceNo("IN20123QE");
		reversalGstDetailModel.setProductCode(prodAndVarientCodeSame);
		reversalGstDetailModel.setRemarks("Gratuity Member Transfer Deposit Adjustment");
		reversalGstDetailModel.setToGstIn(getMPHEntity.getGstIn() == null ? "" : getMPHEntity.getGstIn());
		reversalGstDetailModel.setToPan(getMPHEntity.getPan() == null ? "" : getMPHEntity.getPan());
		reversalGstDetailModel.setVariantCode(prodAndVarientCodeSame);
		reversalGstDetailModel.setYear(DateUtils.uniqueNoYYYY());
		reversalGstDetailModel.setMonth(DateUtils.currentMonthName());

		return reversalGstDetailModel;
	}

	@Transactional
	@Override
	public ApiResponseDto<AdjustmentPropsDto> transferInUpdate(PolicyContriDetailDto policyContriDetailDto,
			Long tmpPolicyId) {
		log.info("Entering into MemberTransferInOutServiceImpl : transferInUpdate");
//			Long masterTmpPolicyId = null;
		Double getBalance = null;
		Double adjustementAmount = 0.0;
		Double stampDuty = 0.0;
		int year = 0;
		int month = 0;
		String financialYear = null;
		financialYear = DateUtils.getFinancialYear(new Date());

		// masterTmpPolicyId = tmpPolicyId;
		TransferPolicyDetailEntity transferPolicyDetails = transferPolicyDetailRepo
				.findByTransferRequisitionId(policyContriDetailDto.getTransferRequisitionId());

		Optional<TransferRequisitionEntity> transferRequisitionEntity = transferRequisitionRepo
				.findById(policyContriDetailDto.getTransferRequisitionId());

		TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = transferMemberPolicyDetailRepo
				.findByTransferRequisitionId(policyContriDetailDto.getTransferRequisitionId());
		List<PolicyDeposit> policyDepositEntities = policyDepositRepo
				.findBycontributionDetailId(transferRequisitionEntity.get().getContributionAdjustmentId());

		if (isDevEnvironment == false) {
			List<UnlockDepositDetailDto> showDepositLockDto = new ArrayList<UnlockDepositDetailDto>();

			for (PolicyDeposit policyDepositEntity : policyDepositEntities) {

				UnlockDepositDetailDto depositLockDto = new UnlockDepositDetailDto();
				depositLockDto.setChallanNo(Integer.parseInt(policyDepositEntity.getChallanNo()));
				depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				depositLockDto.setUserCode(policyContriDetailDto.getCreatedBy());
				String prodAndVarientCodeSame = commonModuleService
						.getProductCode(transferPolicyDetails.getProductIdIn());
				depositLockDto.setProductCode(prodAndVarientCodeSame);
				depositLockDto.setVariantCode(prodAndVarientCodeSame);
				depositLockDto.setVariantCode("V3");
				showDepositLockDto.add(depositLockDto);
			}

			accountingService.unlockDeposits(showDepositLockDto, policyContriDetailDto.getCreatedBy());
		}

		Optional<PolicyContriDetailEntity> policyContriDetailOpt = policyContriDetailRepository
				.findById(transferRequisitionEntity.get().getContributionAdjustmentId());
		if (policyContriDetailOpt.isPresent()) {
			PolicyContriDetailEntity policyContriDetailEntity = policyContriDetailOpt.get();
			policyContriDetailEntity.setTmpPolicyId(policyContriDetailDto.getTmpPolicyId());
			policyContriDetailEntity.setCreatedBy(policyContriDetailDto.getCreatedBy());
			policyContriDetailEntity.setCreatedDate(new Date());
			policyContriDetailEntity.setActive(true);
			// policyContriDetailEntity.setAdjustmentForDate(policyContributionDetailDto.getAdjustmentForDate());
			policyContriDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
			policyContriDetailEntity.setLifePremium(transferMemberPolicyDetailEntity.getLicPremiumIn());
			policyContriDetailEntity.setLifePremium(transferMemberPolicyDetailEntity.getLicPremiumIn());

			if (transferMemberPolicyDetailEntity.getIsAccruedGratuityModified() != "Y") {
				policyContriDetailEntity
						.setPastService(Double.valueOf(transferMemberPolicyDetailEntity.getAccruedGratuityExisting()));
			} else {
				policyContriDetailEntity
						.setPastService(Double.valueOf(transferMemberPolicyDetailEntity.getAccruedGratuityNew()));
			}
			policyContriDetailEntity.setCurrentServices(0.0);
			policyContriDetailEntity.setGst(transferMemberPolicyDetailEntity.getGstOnPremiumIn());
			policyContriDetailEntity.setModifiedBy(policyContriDetailDto.getModifiedBy());
			policyContriDetailEntity.setModifiedDate(new Date());
			policyContriDetailEntity.setEntryType("TR");
			policyContriDetailEntity.setAdjustmentForDate(new Date());
			policyContriDetailEntity.setFinancialYear(financialYear);
			policyContriDetailEntity = policyContriDetailRepository.save(policyContriDetailEntity);

			List<PolicyAdjustmentDetailEntity> PolicyAdjustmentDetail = policyAdjustmentDetailRepo
					.deleteBycontributionDetailId(policyContriDetailEntity.getId());

			List<PolicyDeposit> oldDepositList = policyDepositRepo
					.deleteByContributionDetailId(transferRequisitionEntity.get().getContributionAdjustmentId());
			List<PolicyContributionEntity> oldPolicyContribution = policyContriRepository
					.deleteByContributionDetailId(transferRequisitionEntity.get().getContributionAdjustmentId());
			List<PolicyContrySummaryEntity> oldPolicyContriSummary = policyContrySummaryRepo
					.deleteByContributionDetailId(transferRequisitionEntity.get().getContributionAdjustmentId());

			Double premiumAdjustment = transferMemberPolicyDetailEntity.getLicPremiumIn();
			Double gstOnPremiumAdjusted = transferMemberPolicyDetailEntity.getGstOnPremiumIn();
			adjustementAmount = premiumAdjustment + gstOnPremiumAdjusted;

			policyContriDetailEntity = policyContriDetailRepository.save(policyContriDetailEntity);

			List<ShowDepositLockDto> showDepositLockDto = new ArrayList<ShowDepositLockDto>();
			for (DepositAdjustementRequest depositAdjustementRequest : policyContriDetailDto
					.getDepositAdjustementRequest()) {
				if (adjustementAmount != 0) {

					PolicyDeposit policyDeposit = new PolicyDeposit();

					if (adjustementAmount < depositAdjustementRequest.getCollectionAmount()) {

						getBalance = depositAdjustementRequest.getCollectionAmount() - adjustementAmount;
						Double T = getBalance * 100;
						Long T1 = Math.round(T);
						getBalance = T1.doubleValue() / 100;
						policyDeposit.setAvailableAmount(getBalance);
						policyDeposit.setAdjustmentAmount(adjustementAmount);
						adjustementAmount = 0.0;
					} else {

						getBalance = adjustementAmount - depositAdjustementRequest.getCollectionAmount();
						Double T = getBalance * 100;
						Long T1 = Math.round(T);
						getBalance = T1.doubleValue() / 100;
						policyDeposit.setAdjustmentAmount(depositAdjustementRequest.getCollectionAmount());
						policyDeposit.setAvailableAmount(0.0);
						adjustementAmount = getBalance;
					}
					policyDeposit.setChequeRealistionDate(depositAdjustementRequest.getVoucherEffectiveDate());
					policyDeposit.setActive(true);
					Random r = new Random(System.currentTimeMillis());
					int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
					policyDeposit.setChallanNo(String.valueOf(getCollectionNumber));
					policyDeposit.setCollectionDate(new Date());
					policyDeposit.setCreateDate(new Date());
					policyDeposit.setZeroId(null);
					policyDeposit.setTransactionMode(depositAdjustementRequest.getCollectionMode());
					policyDeposit.setStatus(null);
					policyDeposit.setRegConId(null);
					policyDeposit.setTmpPolicyId(tmpPolicyId);
					policyDeposit.setContributionType("RE");
					policyDeposit.setAdjConId(null);
					policyDeposit.setDepositAmount(depositAdjustementRequest.getCollectionAmount());
					policyDeposit.setCreatedBy(depositAdjustementRequest.getCreatedBy());
					policyDeposit.setIsDeposit(true);
					policyDeposit.setActive(true);
					policyDeposit.setRemark(depositAdjustementRequest.getRemarks());
					policyDeposit.setCollectionNo(depositAdjustementRequest.getCollectionNumber());
					policyDeposit.setCollectionStatus(depositAdjustementRequest.getCollectionStatus());
					policyDeposit.setContributionDetailId(policyContriDetailEntity.getId());
					policyDepositRepo.save(policyDeposit);

					ShowDepositLockDto depositLockDto = new ShowDepositLockDto();

					String prodAndVarientCodeSame = commonModuleService
							.getProductCode(transferPolicyDetails.getProductIdOut());

					depositLockDto.setChallanNo(Integer.valueOf(policyContriDetailEntity.getChallanNo()));
					depositLockDto.setRefNo(policyDeposit.getId().toString());
					depositLockDto.setCollectionNo(policyDeposit.getCollectionNo().toString());
					depositLockDto.setUserCode(policyContriDetailDto.getCreatedBy());
					depositLockDto.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
					depositLockDto.setProductCode(prodAndVarientCodeSame);
					depositLockDto.setVariantCode(prodAndVarientCodeSame);

					log.info("Lock Deposit Params: " + depositLockDto.toString());

					showDepositLockDto.add(depositLockDto);
				}
			}

			year = Calendar.getInstance().get(Calendar.YEAR);

			month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			System.out.println("Financial month : " + month);

			if (month < 3) {
				financialYear = ((year - 1) + "-" + year);
			} else {
				financialYear = (year + "-" + (year + 1));
			}
			List<PolicyContributionEntity> policyContributionEntity = new ArrayList<PolicyContributionEntity>();

			List<PolicyDeposit> policyDepositEntitieList = policyDepositRepo
					.findBycontributionDetailId(transferRequisitionEntity.get().getContributionAdjustmentId());

			List<PolicyAdjustmentDetailEntity> policyAdjustmentDetail = new ArrayList<PolicyAdjustmentDetailEntity>();
			Double depositAmount = 0.0;
			Long depositId = 0l;
			Double currentAmount = 0.0;

//				Long premiumAdjustmentLong=Math.round(policyContributionDetailEntity.getLifePremium());
//				Double premiumAdjustment = premiumAdjustmentLong.doubleValue();
//				
//				Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();

			Long currentServiceAdjustedLong = Math.round(policyContriDetailEntity.getCurrentServices());

			Double currentServiceAdjusted = currentServiceAdjustedLong.doubleValue();

			Long pastServiceAdjustedLong = Math.round(policyContriDetailEntity.getPastService());
			Double pastServiceAdjusted = pastServiceAdjustedLong.doubleValue();
			adjustementAmount = currentServiceAdjusted + pastServiceAdjusted;

			policyContriDetailDto
					.setDepositAdjustementRequest(sort(policyContriDetailDto.getDepositAdjustementRequest()));

			Long versionNumber = policyContriRepository.getMaxVersion(financialYear, policyContriDetailEntity.getId());
			versionNumber = versionNumber == null ? 01 : versionNumber + 1;
			for (PolicyDeposit policyDepositEntity : policyDepositEntitieList) {
				if (adjustementAmount > 0) {
					Double currentAmountforContribution = 0.0;
					depositAmount = policyDepositEntity.getDepositAmount();
					depositId = policyDepositEntity.getId();

					// LIFE PREMIUM NO
					if (premiumAdjustment > 0 && depositAmount > 0) {
						if (premiumAdjustment >= depositAmount) {
							currentAmount = depositAmount;

							premiumAdjustment = premiumAdjustment - depositAmount;
							adjustementAmount = adjustementAmount - currentAmount;
							policyAdjustmentDetail.add(this.saveAdjustmentdata(
									policyDepositEntity.getContributionDetailId(), depositId, currentAmount,
									"LifePremium", "LifePremium", "TR", policyDepositEntity.getChequeRealistionDate(),
									policyContriDetailDto.getCreatedBy()));
							depositAmount = 0.0;
						} else {
							currentAmount = premiumAdjustment;

							adjustementAmount = adjustementAmount - currentAmount;
							policyAdjustmentDetail.add(this.saveAdjustmentdata(
									policyDepositEntity.getContributionDetailId(), depositId, currentAmount,
									"LifePremium", "LifePremium", "TR", policyDepositEntity.getChequeRealistionDate(),
									policyContriDetailDto.getCreatedBy()));
							depositAmount = depositAmount - premiumAdjustment;
							premiumAdjustment = 0.0;
						}
					}

					// GST AMOUNT NO

					if (premiumAdjustment == 0 && gstOnPremiumAdjusted > 0 && depositAmount > 0) {
						if (gstOnPremiumAdjusted >= depositAmount) {
							currentAmount = depositAmount;

							gstOnPremiumAdjusted = gstOnPremiumAdjusted - depositAmount;
							adjustementAmount = adjustementAmount - currentAmount;
							policyAdjustmentDetail.add(this.saveAdjustmentdata(
									policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst",
									"Gst", "TR", policyDepositEntity.getChequeRealistionDate(),
									policyContriDetailDto.getCreatedBy()));
							depositAmount = 0.0;
						} else {
							currentAmount = gstOnPremiumAdjusted;

							adjustementAmount = adjustementAmount - currentAmount;
							policyAdjustmentDetail.add(this.saveAdjustmentdata(
									policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst",
									"Gst", "TR", policyDepositEntity.getChequeRealistionDate(),
									policyContriDetailDto.getCreatedBy()));
							depositAmount = depositAmount - gstOnPremiumAdjusted;
							gstOnPremiumAdjusted = 0.0;
						}
					}
					// SinglePremium
					// FirstPremium

					policyContributionEntity
							.add(this.saveContributionDetail(tmpPolicyId, policyDepositEntity.getContributionDetailId(),
									"ca", currentAmountforContribution, financialYear, versionNumber,
									commonModuleService.getVariantCode(transferPolicyDetails.getProductIdOut()),
									policyDepositEntity.getChequeRealistionDate(), policyContriDetailDto.getCreatedBy(),
									policyDepositEntity.getCollectionDate(), policyDepositEntity.getCollectionNo(),
									policyDepositEntity.getChallanNo()));
					versionNumber = versionNumber + 1;
				}
			}
			policyContriRepository.saveAll(policyContributionEntity);
			policyAdjustmentDetailRepo.saveAll(policyAdjustmentDetail);
			if (isDevEnvironment == false) {
				accountingService.lockDeposits(showDepositLockDto, policyContriDetailDto.getCreatedBy());
			}

			ContriAdjustmentPropsEntity contributionAdjustmentPropsEntity = adjustmentPropsRepository
					.findByPstgContriDetailId(transferRequisitionEntity.get().getContributionAdjustmentId());
			try {

				contributionAdjustmentPropsEntity.setAmtToBeAdjusted(
						policyContriDetailEntity.getCurrentServices() + policyContriDetailEntity.getPastService());
				contributionAdjustmentPropsEntity.setPstgContriDetailId(policyContriDetailEntity.getId());
				contributionAdjustmentPropsEntity.setTmpPolicyId(tmpPolicyId);
				contributionAdjustmentPropsEntity.setFirstPremiumPS(policyContriDetailEntity.getPastService());
				contributionAdjustmentPropsEntity
						.setSinglePremiumFirstYearCS(policyContriDetailEntity.getCurrentServices());
				contributionAdjustmentPropsEntity.setAdjustmentForDate(new Date());
				contributionAdjustmentPropsEntity.setModifiedBy(policyContriDetailEntity.getModifiedBy());
				contributionAdjustmentPropsEntity.setModifiedDate(new Date());
				contributionAdjustmentPropsEntity = adjustmentPropsRepository.save(contributionAdjustmentPropsEntity);

			} catch (Exception e) {
				log.error("CAProps Error", e);
			}
			ApiResponseDto response = new ApiResponseDto();
			response.setMessage("Success");
			response.setStatus("200");
			// return
			// ApiResponseDto.success(ContriHelper.entityToDto(contributionAdjustmentPropsEntity));
			return response;
		} else {
			ApiResponseDto response = new ApiResponseDto();
			response.setMessage("Could not find Contribution Detail ID");
			response.setStatus("Failed");
			return response;
		}
	}

//		@Transactional
//		public Map<String, Object> sendEmail(Long transferRequisitionId)
//				throws IOException {
//			Map<String, Object> response = new HashMap<String, Object>();
//
//			
//			List<RQPolicyARDDetailsResponse> rqPolicyARDDetailsResponse = new ArrayList<>();
//			if (rqPolicyARDSearch != null) {
//
//				// rqPolicyARDDetailsResponse =
//				// renewalQuotationCreationDao.getPolicyIdForArdSixtyDaysLeft(rqPolicyARDSearch);
//				rqPolicyARDDetailsResponse = renewalQuotationCreationDao.getPolicyIdForArdSixtyDaysLeft();
//			}
//
//			Map<String, Object> result = new HashMap<String, Object>();
//
//			for (RQPolicyARDDetailsResponse sixtyDaysArd : rqPolicyARDDetailsResponse) {
//
//				String policyNumber = sixtyDaysArd.getPolicyNumber();
//				String lastFourPolicyNumber = null;
//
//				if (policyNumber.length() > 4) {
//					lastFourPolicyNumber = policyNumber.substring(policyNumber.length() - 4);
//				} else {
//					lastFourPolicyNumber = policyNumber;
//				}
//
//				DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//				Date date2 = new Date();
//				try {
//					date2 = inputFormat.parse(sixtyDaysArd.getAnnualRenewalDate());
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//				// Format date into output format
//				DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
//				String outputString = outputFormat.format(date2);
//				String FileName = lastFourPolicyNumber + "_" + dateFormated + "_";
//				/*
//				 * String str =
//				 * "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Document</title></meta></meta></meta></head><body style=\"margin:0px; font-family: arial;\"><div class=\"outer-wrapper\" style=\"background:#ffffff;border: 2px #000000 solid;padding: 5px 0px 10px 15px;\"><table style=\"width:95%;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px;\">Ref:P&amp;GS/GSLI/ver</td><td style=\"color:black;padding-bottom:2px; font-size: 15px;  text-align: right;\">Date:@date</td></table><span style=\"font-size:12px;color:black;padding-left: 365px;\"><strong><u>RENEWAL NOTICE</u></strong><br /></span><table style=\"width:95%;\"><tr><td style=\"color:black;padding-bottom:2px; font-size: 15px;\">Officer Incharge: @MPHName <br /> @mphAddress1 <br />@mphAddress2 <br />@mphAddress3 <br /> </td><br /><br /></tr></table><table style=\"width:95%;\"><tr><td style=\"color:black;padding-bottom:2px; font-size: 15px;\"> Dear Sir/Madam,</td><br /></tr></table><table style=\"width:95%;\"><tr><td style=\"color:black;padding-bottom:2px; font-size: 15px;\">&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; <b>Re :&nbsp; &nbsp; &nbsp; &nbsp; New Policy No. @MPNO </b><br /> &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<b> Old Policy No : @OLDMPNO </b><br /> &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <b> Annual Renewal Date : @ARDDATE </b><br /></td></tr></table><table style=\"width:95%;\"><tr style=\"line-height: 22px;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px;\"> Greetings from Life Insurance Corporation of India ! <br />This is to bring to your notice that the above scheme is due for renewal on @ARDDATE. In order to renew it in time and continue with the Insurance cover for the members, we request you to comply with the following: <br />&nbsp; &nbsp; &nbsp; &nbsp; <li style=\"padding-left: 50px;\"> Remittance of monthly /annual contributions towards Group Savings Linked Insurance scheme as mentioned in the scheme rules on or before annual renewal date</li><li style=\"padding-left: 50px;\">Please provide the renewal data as specified in the enclosed format (in soft copy)</li></td></tr></table><table style=\"width:95%;\"><tr style=\"line-height: 22px;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px; padding-left: 55px; \"><ul style=\"list-style-type:square;\"><li>Full data of existing members along with LICID allotted by us.</li><li>Full data of Transfer in / out members</li><li>Particulars of members who ceased to be in service by way of Resignation / Retirement/ Death during the last policy year </li></ul></td></tr></table><table style=\"width:95%;\"><tr style=\"line-height: 22px;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px;\"><li style=\"padding-left: 50px;\"><b>Please note that no new entrants are allowed under the Policy.</b></li><li style=\"padding-left: 50px;\">Please furnish the reconciliation statement attached herewith for reviewing the renewal premium.</li><li style=\"padding-left: 50px;\">In case of exit of the member, please send the duly completed claim form along with the reconciliation statement.</li><li style=\"padding-left: 50px;\">With reference to Policy conditions Schedule Part II, paragraph 4, please remit the premium for those employees who are on <u>loss of pay</u>. In case premium for such members is not paid continuously for more than six months, the said members will cease to be covered under the Scheme and will not be allowed to rejoin the schem </li>It may be noted that no grace period is allowed under the Policy. You are requested to remit the premium on or before due date. In case of late remittance, please note that the late fee latefeerate compounding half yearly will be charged from the due date to the date of receipt of premium. <br /><br /><b>Yours faithfully</b><br /><br />Manager (P &amp; GS) <br /></td></tr></table><span style=\"font-size:12px;color:black;padding-left: 210px;\"><strong>GSLI DATA TO BE SUBMITTED FOR RENEWAL</strong><br /></span></div></body></html>"
//				 * ; String str =
//				 * "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Document</title></meta></meta></meta></head><body style=\"margin:0px; font-family: arial;\"><div class=\"outer-wrapper\" style=\"background:#ffffff;border: 2px #000000 solid;padding: 5px 0px 10px 15px;\"><table style=\"width:95%;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px;\">Ref:P&amp;GS/GSLI/ver</td><td style=\"color:black;padding-bottom:2px; font-size: 15px;  text-align: right;\">Date:@date</td></table><span style=\"font-size:12px;color:black;padding-left: 365px;\"><strong><u>RENEWAL NOTICE</u></strong><br /></span><table style=\"width:95%;\"><tr><td style=\"color:black;padding-bottom:2px; font-size: 15px;\">Officer Incharge: @MPHName <br /> @mphAddress1 <br />@mphAddress2 <br />@mphAddress3 <br /> </td><br /><br /></tr></table><table style=\"width:95%;\"><tr><td style=\"color:black;padding-bottom:2px; font-size: 15px;\"> Dear Sir/Madam,</td><br /></tr></table><table style=\"width:95%;\"><tr><td style=\"color:black;padding-bottom:2px; font-size: 15px;\">&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; <b>Re :&nbsp; &nbsp; &nbsp; &nbsp; New Policy No. @MPNO </b><br /> &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<b> Old Policy No : @OLDMPNO </b><br /> &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <b> Annual Renewal Date : @ARDDATE </b><br /></td></tr></table><table style=\"width:95%;\"><tr style=\"line-height: 22px;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px;\"> Greetings from Life Insurance Corporation of India ! <br />This is to bring to your notice that the above scheme is due for renewal on @ARDDATE. In order to renew it in time and continue with the Insurance cover for the members, we request you to comply with the following: <br />&nbsp; &nbsp; &nbsp; &nbsp; <li style=\"padding-left: 50px;\"> Remittance of monthly /annual contributions towards Group Savings Linked Insurance scheme as mentioned in the scheme rules on or before annual renewal date</li><li style=\"padding-left: 50px;\">Please provide the renewal data as specified in the enclosed format (in soft copy)</li></td></tr></table><table style=\"width:95%;\"><tr style=\"line-height: 22px;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px; padding-left: 55px; \"><ul style=\"list-style-type:square;\"><li>Full data of existing members along with LICID allotted by us.</li><li>Full data of Transfer in / out members</li><li>Particulars of members who ceased to be in service by way of Resignation / Retirement/ Death during the last policy year </li></ul></td></tr></table><table style=\"width:95%;\"><tr style=\"line-height: 22px;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px;\"><li style=\"padding-left: 50px;\"><b>Please note that no new entrants are allowed under the Policy.</b></li><li style=\"padding-left: 50px;\">Please furnish the reconciliation statement attached herewith for reviewing the renewal premium.</li><li style=\"padding-left: 50px;\">In case of exit of the member, please send the duly completed claim form along with the reconciliation statement.</li><li style=\"padding-left: 50px;\">With reference to Policy conditions Schedule Part II, paragraph 4, please remit the premium for those employees who are on <u>loss of pay</u>. In case premium for such members is not paid continuously for more than six months, the said members will cease to be covered under the Scheme and will not be allowed to rejoin the schem </li>It may be noted that no grace period is allowed under the Policy. You are requested to remit the premium on or before due date. In case of late remittance, please note that the late fee latefeerate compounding half yearly will be charged from the due date to the date of receipt of premium. <br /><br /><b>Yours faithfully</b><br /><br />Manager (P &amp; GS) <br /></td></tr></table><span style=\"font-size:12px;color:black;padding-left: 210px;\"><strong>GSLI DATA TO BE SUBMITTED FOR RENEWAL</strong><br /></span></div></body></html>"
//				 * ;
//				 * 
//				 * // String emailBody1 = line; System.out.println(str);
//				 * 
//				 * String completehtmlContent1 = str.replaceAll("@date",
//				 * date).replaceAll("@mphAddress1", finalAddress) .replaceAll("@mphAddress2",
//				 * finalAddress2).replaceAll("@mphAddress3", finalAddress3) .replaceAll("@MPNO",
//				 * newpolicyNumber).replace("@ARDDATE", renewalNoticeEmailModel.getArdDate())
//				 * .replaceAll("@MPHName", mphName) .replaceAll("@OLDMPNO",
//				 * renewalNoticeEmailModel.getOldPolicyNumber());
//				 * 
//				 * String completehtmlContent1 = str.replaceAll("@date",dateFormated
//				 * ).replaceAll("@mphAddress1", sixtyDaysArd.getMphAddress1())
//				 * .replaceAll("@mphAddress2",
//				 * sixtyDaysArd.getMphAddress2()).replaceAll("@mphAddress3",
//				 * sixtyDaysArd.getMphAddress3()) .replaceAll("@MPNO",
//				 * sixtyDaysArd.getPolicyNumber()).replace("@ARDDATE",
//				 * sixtyDaysArd.getAnnualRenewalDate().toString()) .replaceAll("@MPHName",
//				 * sixtyDaysArd.getMphName()) .replaceAll("@OLDMPNO",
//				 * sixtyDaysArd.getOldPolicyNumber());
//				 **/
//				String str = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Document</title></meta></meta></meta></head><body style=\"margin:0px; font-family: arial;\"><div class=\"outer-wrapper\" style=\"background:#ffffff;border: 2px #000000 solid;padding: 5px 0px 10px 15px;\"><table style=\"width:95%;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px;\">Ref:P&amp;GS/GSLI/@version</td><td style=\"color:black;padding-bottom:2px; font-size: 15px;  text-align: right;\">Date:@date</td></table><span style=\"font-size:12px;color:black;padding-left: 365px;\"><strong><u>RENEWAL NOTICE</u></strong><br /></span><table style=\"width:95%;\"><tr><td style=\"color:black;padding-bottom:2px; font-size: 15px;\">The Office Incharge @MPHName, <br /> @mphAddress1, <br />@mphAddress2, <br />@mphAddress3. <br /> </td><br /><br /></tr></table><table style=\"width:95%;\"><tr><td style=\"color:black;padding-bottom:2px; font-size: 15px;\"> Dear Sir/Madam,</td><br /></tr></table><table style=\"width:95%;\"><tr><td style=\"color:black;padding-bottom:2px; font-size: 15px;\">&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; <b>Re :&nbsp; &nbsp; &nbsp; &nbsp; New Policy No. @MPNO </b><br /> &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<b> Old Policy No : @OLDMPNO </b><br /> &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <b> Annual Renewal Date : @ARDDATE </b><br /></td></tr></table><table style=\"width:95%;\"><tr style=\"line-height: 22px;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px;\"> Greetings from Life Insurance Corporation of India ! <br />This is to bring to your notice that the above scheme is due for renewal on @ARDDATE. In order to renew it in time and continue with the Insurance cover for the members, we request you to comply with the following: <br />&nbsp; &nbsp; &nbsp; &nbsp; <li style=\"padding-left: 50px;\"> Remittance of monthly /annual contributions towards Group Savings Linked Insurance scheme as mentioned in the scheme rules on or before annual renewal date</li><li style=\"padding-left: 50px;\">Please provide the renewal data as specified in the enclosed format (in soft copy)</li></td></tr></table><table style=\"width:95%;\"><tr style=\"line-height: 22px;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px; padding-left: 55px; \"><ul style=\"list-style-type:square;\"><li>Full data of existing members along with LICID allotted by us.</li><li>Full data of Transfer in / out members</li><li>Particulars of members who ceased to be in service by way of Resignation / Retirement/ Death during the last policy year </li></ul></td></tr></table><table style=\"width:95%;\"><tr style=\"line-height: 22px;\"><td style=\"color:black;padding-bottom:2px; font-size: 15px;\"><li style=\"padding-left: 50px;\"><b>Please note that no new entrants are allowed under the Policy.</b></li><li style=\"padding-left: 50px;\">Please furnish the reconciliation statement attached herewith for reviewing the renewal premium.</li><li style=\"padding-left: 50px;\">In case of exit of the member, please send the duly completed claim form along with the reconciliation statement.</li><li style=\"padding-left: 50px;\">With reference to Policy conditions Schedule Part II, paragraph 4, please remit the premium for those employees who are on <u>loss of pay</u>. In case premium for such members is not paid continuously for more than six months, the said members will cease to be covered under the Scheme and will not be allowed to rejoin the schem </li>It may be noted that no grace period is allowed under the Policy. You are requested to remit the premium on or before due date. In case of late remittance, please note that the late fee latefeerate compounding half yearly will be charged from the due date to the date of receipt of premium. <br /><br /><b>Yours faithfully</b><br /><br />Manager (P &amp; GS) <br /></td></tr></table><span style=\"font-size:12px;color:black;padding-left: 210px;\"><strong>GSLI DATA TO BE SUBMITTED FOR RENEWAL</strong><br /></span></div></body></html>";
//
////				String emailBody1 = line;
//				System.out.println(str);
//
//				String completehtmlContent1 = str.replaceAll("@date", dateFormated)
//						.replaceAll("@mphAddress1", sixtyDaysArd.getMphAddress1())
//						.replaceAll("@mphAddress2", sixtyDaysArd.getMphAddress2())
//						.replaceAll("@mphAddress3", sixtyDaysArd.getMphAddress3())
//						.replaceAll("@MPNO", sixtyDaysArd.getPolicyNumber()).replace("@ARDDATE", outputString)
//						.replaceAll("@MPHName", sixtyDaysArd.getMphName())
//						.replaceAll("@OLDMPNO", sixtyDaysArd.getOldPolicyNumber()).replaceAll("@version", referenceNumber);
//
//				logger.info("htmlContent - " + completehtmlContent1);
//
//				String completehtmlContent2 = completehtmlContent1.replace("\\", "").replaceAll("\t", "");
//
//				logger.info(completehtmlContent2);
//
//				logger.info("Email Process start");
//
//				EmailRequestModel emailRequest = new EmailRequestModel();
//				emailRequest.setTo(renewalNoticeEmailModel.getMphEmail());
//				emailRequest.setSubject("Quotation Renewal Before 60 Days of ARD!");
//				emailRequest.setEmailBody(completehtmlContent2);
//				emailRequest.setCc(renewalNoticeEmailModel.getCc());
//				// List<PdfFile> pdfMapList = new ArrayList<PdfFile>();
//
//				String transferFileName = "";
//
//				transferFileName = "Existing_And_Transfer_In_Members";
//				Map<String, Object> fileData = new HashMap<String, Object>();
//				StringBuilder sb = new StringBuilder();
//				String firstLine = "SR No,Name of Member,LIC ID,Employee Number,DOB,Date of Joining the Scheme,Change in Category,Present Category,Monthly premium (risk + savings),Dab Premium if any";
//				String firstLine5 = ",,,,,Year & Cat,Year & Cat,Year & Cat";
//
//				sb.append(firstLine).append("\n");
//				sb.append(firstLine5).append("\n");
//
//				StringBuilder sb1 = new StringBuilder();
//				String firstLine1 = "SR No,LIC ID,Name of Member Category,Date of Exit,Cause of Exit";
//				sb1.append(firstLine1).append("\n");
//
//				StringBuilder sb2 = new StringBuilder();
//				String firstLine2 = "SR No,Particulars,Cat 1, Cat 2, Cat3, Cat4";
//				String firstLine21 = "1,No. of members at the end of previous month";
//				String firstLine22 = "2,Add : transfer in members";
//				String firstLine23 = "3,Less: exited members";
//				String firstLine24 = "4,Change of category at the renewal date";
//				String firstLine25 = "5,No. Of members as on current due date ( 1+2-3+/-4=5)";
//				String firstLine26 = "6,Monthly contributions per member";
//				String firstLine27 = "7,Dab premium if any";
//				String firstLine28 = "8,Total monthly contribution (6+7 )";
//				String firstLine29 = "9,Total contribution (5 x8)";
//
//				sb2.append(firstLine2).append("\n");
//				sb2.append(firstLine21).append("\n");
//				sb2.append(firstLine22).append("\n");
//				sb2.append(firstLine23).append("\n");
//				sb2.append(firstLine24).append("\n");
//				sb2.append(firstLine25).append("\n");
//				sb2.append(firstLine26).append("\n");
//				sb2.append(firstLine27).append("\n");
//				sb2.append(firstLine28).append("\n");
//				sb2.append(firstLine29).append("\n");
//
//				// List<Mmember> getMemberData = new ArrayList<>();
//
//				// String mmemberFileName = "";
//
//				// getMemberData =
//				// mmemberRepository.getMmberDataForSixtyDaysBeforeArd(sixtyDaysArd.getPolicyId());
//
//				// mmemberFileName = "MemberDetails";
//				// Map<String, Object> fileData1 = new HashMap<String, Object>();
//				// StringBuilder sb3 = new StringBuilder();
//				// String firstLine3 = "FIRST_NAME,MIDDLE_NAME, LAST_NAME, GENDER, PAN_CARD_NO,
//				// DOB, DATE_OF_JOINING_SCHEME, MOBILE_NO, EMAIL, CATEGORY";
//				// sb3.append(firstLine3).append("\n");
//				// for (Mmember memberData : getMemberData) {
//				// String line = memberData.getFirstName() + "," + memberData.getMiddleName() +
//				// ","
//				// + memberData.getLastName() + "," + memberData.getGender() + "," +
//				// memberData.getPanCardNo()
//				// + "," + memberData.getDob() + "," + memberData.getDateOfJoining() + ","
//				// + memberData.getMobileNo() + "," + memberData.getEmail() + "," +
//				// memberData.getCategory();
//				// sb3.append(line).append("\n");
//				// }
//
//				StringBuilder sb3 = new StringBuilder();
//				String firstLine3 = "mber,Annual Renewal Date,LIC ID,Member Name,Gender,Date of Birth,PAN Card Number,Mobile No,Email-id,Address 1,Address 2,Address 3,PIN code,Village/ Town / City,Rural Or Urban,Employee Number,Date of Appointment,Date of Joining the scheme,Category,Life Cover Sum Assured,"
//						+ "Contribution - Frequency,Contribution Amount in INR,DAB Premium if any ,DAB_Sum_Assured,Mobile No, Email-id,Rural_Urban,Nominee Name,Nominee Relation,Nominee DOB,Nominee Share Percentage,Appointee Name,Appointee DOB,Nominee2 Name,Nominee2 Relation,Nominee2 DOB,"
//						+ "Nominee2 Share Percentage,Appointee2 Name,Appointee2 DOB,"
//						+ "Nominee3 Name,Nominee3 Relation,Nominee3 DOB,Nominee3 SharePercentage,Appointee3 Name,Appointee3 DOB,LIC Error Count,LIC Error Code / Description,LIC Remarks,Blank 1,Blank 2,Blank 3,Blank 4\r\n"
//						+ "";
//				sb1.append(firstLine3).append("\n");
//
//				byte[] fileBytes = sb.toString().getBytes();
//				byte[] fileBytes1 = sb1.toString().getBytes();
//				byte[] fileBytes2 = sb2.toString().getBytes();
//				byte[] fileBytes3 = sb3.toString().getBytes();
//
//				String encodedString = Base64.getEncoder().encodeToString(fileBytes);
//				List<PdfFile> pdfFile = new ArrayList<>(); //
//				PdfFile pdfFileObj = new PdfFile();
//				pdfFileObj.setFileName(FileName + "Existing_And_Transfer_In_Members");
//				pdfFileObj.setFileData(encodedString);
//				pdfFileObj.setFileType("csv");
//				pdfFile.add(pdfFileObj);
//
//				String encodedString1 = Base64.getEncoder().encodeToString(fileBytes1);
//				// List<PdfFile> pdfFile1 = new ArrayList<>(); //
//				PdfFile pdfFileObj1 = new PdfFile();
//				pdfFileObj1.setFileName(FileName + "Exit_Member_List");
//				pdfFileObj1.setFileData(encodedString1);
//				pdfFileObj1.setFileType("csv");
//				pdfFile.add(pdfFileObj1);
//
//				String encodedString2 = Base64.getEncoder().encodeToString(fileBytes2);
//				// List<PdfFile> pdfFile2 = new ArrayList<>(); //
//				PdfFile pdfFileObj2 = new PdfFile();
//				pdfFileObj2.setFileName(FileName + "Member_Reconciliation_Statement");
//				pdfFileObj2.setFileData(encodedString2);
//				pdfFileObj2.setFileType("csv");
//				pdfFile.add(pdfFileObj2);
//
//				String encodedString3 = Base64.getEncoder().encodeToString(fileBytes3);
//				// List<PdfFile> pdfFile3 = new ArrayList<>(); //
//				PdfFile pdfFileObj3 = new PdfFile();
//				pdfFileObj3.setFileName(FileName + "Member_Details");
//				pdfFileObj3.setFileData(encodedString3);
//				pdfFileObj3.setFileType("csv");
//				pdfFile.add(pdfFileObj3);
//
//				logger.info("Response from email API - " + pdfFile.size());
//
//				emailRequest.setPdfFile(pdfFile);
//				// emailRequest.setPdfFile(pdfFile1);
//				// emailRequest.setPdfFile(pdfFile2);
//
//				RestTemplate restTemplate = new RestTemplate();
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				HttpEntity entity = new HttpEntity(emailRequest, headers);
//
//				logger.info("Email Process Initiated " + entity);
//
//				result = restTemplate.postForObject(emailApi, entity, Map.class);
//
//			}
//
//			logger.info("Email Process Initiated " + emailApi);
//
//			logger.info("Emali Sent-" + result);
//
//			response.put(Constant.STATUS, 1);
//			response.put(Constant.MESSAGE, Constant.SUCCESS);
//			response.put("Data", result);
//
//			logger.info("Response from email API - " + result);
//
////				return result;
////			}
//			return response;
//
//		}

	@Override
	public ResponseEntity<Map<String, Object>> setMemberLicIdRetention(
			RetainMemberLicIdRequest retainMemberLicIdRequest) {
		log.info("Entering into MemberTransferInOutServiceImpl : setMemberLicIdRetention");
		Map<String, Object> response = new HashMap<String, Object>();
		String isLicIdExist = "";
		String message = "";

		// Optional<TransferRequisitionEntity> transferRequisitionEntity =
		// transferRequisitionRepo.findById(retainMemberLicIdRequest.getTransferRequisitionId());
		TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo
				.findByTransferRequisitionId(retainMemberLicIdRequest.getTransferRequisitionId());
		TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = transferMemberPolicyDetailRepo
				.findByTransferRequisitionId(retainMemberLicIdRequest.getTransferRequisitionId());

		if (retainMemberLicIdRequest.getIsLicIdRetainable().equalsIgnoreCase("Y")) {
			MasterPolicyEntity masterPolicyEntity = masterPolicyRepository
					.findByPolicyNumber(transferPolicyDetailEntity.getPolicyNumberIn().toString());
			Long licIdCount = policyMemberRepository.getLicIdCountBylicIdAndpolicyId(
					transferMemberPolicyDetailEntity.getLicId().toString(), masterPolicyEntity.getId());

			if (licIdCount != null && licIdCount > 0) {
				isLicIdExist = "Y";
				message = "Lic Id : " + transferMemberPolicyDetailEntity.getLicId() + " will be retained";
			} else {
				isLicIdExist = "N";
				message = "Lic Id : " + transferMemberPolicyDetailEntity.getLicId()
						+ " is already present, could not be retained";
			}
			transferMemberPolicyDetailEntity.setRetainLicId("Y");
			transferMemberPolicyDetailEntity.setIsLicIdExist(isLicIdExist);
			transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);
		} else if (retainMemberLicIdRequest.getIsLicIdRetainable().equalsIgnoreCase("N")) {
			transferMemberPolicyDetailEntity.setRetainLicId("N");
			transferMemberPolicyDetailRepo.save(transferMemberPolicyDetailEntity);
			message = "Lic Id : " + transferMemberPolicyDetailEntity.getLicId() + " will be not retained";
		}
		response.put("isLicIdRetained", retainMemberLicIdRequest.getIsLicIdRetainable());
		response.put("isLicIdExist", isLicIdExist);
		response.put("responseCode", "success");
		response.put("responseMessage", message);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> getCollectionDetails(Long transferRequisitionId) {
		log.info("Entering into MemberTransferInOutServiceImpl : transferRequisitionId");
		Map<String, Object> response = new HashMap<String, Object>();

		Optional<TransferRequisitionEntity> transferRequisitionEntity = transferRequisitionRepo
				.findById(transferRequisitionId);
		List<PolicyDepositEntity> policyDepositEntity = policyDepositRepository
				.findBycontributionDetailId(transferRequisitionEntity.get().getContributionAdjustmentId());

		if (policyDepositEntity != null && !policyDepositEntity.isEmpty()) {
			response.put("collectionDetails", policyDepositEntity);
			response.put("responseCode", "success");
			response.put("responseMessage", "Data Found");
		} else {
			response.put("collectionDetails", policyDepositEntity);
			response.put("responseCode", "failed");
			response.put("responseMessage", "Data Not Found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ApiResponseDto<MemberBulkResponseDto> uploadTransferMemberData(String username, Long transferId,
			Long masterPolicyId, MultipartFile file) {
		ApiResponseDto<MemberBulkResponseDto> response = new ApiResponseDto<MemberBulkResponseDto>();
		try {
			String sparkUrl = bulkServiceEndPoint + "/api/transfer/member/upload";
			response.setMessage("spark error");
			response.setStatus("fail");
			log.info("MemberTransferInOutServiceImpl : Bulk : uploadTransferMemberData-Start");
			log.info("Bulk Service url : " + sparkUrl);

			byte[] fileContent = null;

			try {
				fileContent = IOUtils.toByteArray(file.getInputStream());
			} catch (IOException e) {
				log.info("MemberTransferInOutServiceImpl : Bulk : uploadTransferMemberData-Error:", e);
				response.setMessage("Multipart file is empty!");
				response.setStatus("fail");
				return response;
			}

			String filename = file.getOriginalFilename();

			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(filename).build();

			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileEntity);
			body.add("transferId", transferId);
			body.add("username", username);
			body.add("masterPolicyId", masterPolicyId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			ResponseEntity<ApiResponseDto> responseEntity = restTemplate.postForEntity(sparkUrl, requestEntity,
					ApiResponseDto.class);

			if (responseEntity.getBody() != null) {
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			log.info("MemberTransferInOutServiceImpl : Bulk : uploadTransferMemberData-Error : ", e);
		}
		return response;
	}

	@Override
	public ResponseEntity<byte[]> downloadSampleFile() {
		log.info("MemberTransferInOutServiceImpl : Bulk : downloadSampleFile-Start");

		String url = bulkServiceEndPoint + "/api/transfer/member/downloads/sample";
		log.info("Bulk Service url : " + url);

		ResponseEntity<byte[]> response = null;
		HttpEntity<String> entity = new HttpEntity<String>(restHeader());
		try {
			response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
		} catch (RestClientException e) {
			log.error("MemberTransferInOutServiceImpl : Bulk : downloadSampleFile : error : " + e);
		}
		return response;
	}

	@Override
	public ResponseEntity<MemberBulkResponseDto> deleteBatch(Long batchId) {
		log.info("MemberTransferInOutServiceImpl : Bulk : deleteBatch-Start");

		String url = bulkServiceEndPoint + "/api/transfer/member/delete/" + batchId;
		log.info("Bulk Service url : " + url);

		ResponseEntity<MemberBulkResponseDto> response = null;
		HttpEntity<String> entity = new HttpEntity<String>(restHeader());
		try {
			response = restTemplate.exchange(url, HttpMethod.DELETE, entity, MemberBulkResponseDto.class);
		} catch (RestClientException e) {
			log.error("MemberTransferInOutServiceImpl : Bulk : deleteBatch : error : " + e);
		}
		return response;

	}

	@Override
	public ResponseEntity<byte[]> downloadErrorLog(Long transferRequisitionId) {
		log.info("MemberTransferInOutServiceImpl : Bulk : downloadErrorLog-Start");

		String url = bulkServiceEndPoint + "/api/transfer/member/downloads/errorlog/" + transferRequisitionId;
		log.info("Bulk Service url : " + url);

		ResponseEntity<byte[]> response = null;
		HttpEntity<String> entity = new HttpEntity<String>(restHeader());
		try {
			response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
		} catch (RestClientException e) {
			log.error("MemberTransferInOutServiceImpl : Bulk : downloadErrorLog : error : " + e);
		}
		return response;
	}

	@Override
	public ResponseEntity<?> getUploadSummary(Long transferRequisitionId) {
		log.info("MemberTransferInOutServiceImpl : Bulk : getUploadSummary-Start");

		String url = bulkServiceEndPoint + "/api/transfer/member/getUploadedData/" + transferRequisitionId + "/type";
		log.info("Bulk Service url : " + url);

		// ResponseEntity<MemberBulkResponseDto> response = null;
		ResponseEntity<String> response = null;
		HttpEntity<String> entity = new HttpEntity<String>(restHeader());
		try {
			// response = restTemplate.exchange(url, HttpMethod.GET, entity,
			// MemberBulkResponseDto.class);
			response = restTemplate.getForEntity(url, String.class);

		} catch (RestClientException e) {
			log.error("MemberTransferInOutServiceImpl : Bulk : getUploadSummary : error : " + e);
		}
		return response;
	}

//		 
//		 @Transactional
//			public TaskAllocationEntity saveToTaskAlloc(Long transferRequisitionId, String createdBy) {
//
//				TaskProcessEntity taskProcessEntity = taskProcessRepository.findByProcessName("TRANSFER-OUT");
//
//				TransferRequisitionEntity transferRequisitionEntity = transferRequisitionRepo.findByIdByTransferReqId(transferRequisitionId);
//				
//				TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();
//							
//				taskAllocationEntity.setTaskStatus(transfertatusId.toString());
//				taskAllocationEntity.setRequestId(String.valueOf(transferRequisitionEntity.getTransferRequestNumber()));
//				
//				taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
////				taskAllocationEntity.setLocationType(quotationRenewalDto.getUnitCode());
//				taskAllocationEntity.setCreatedBy(createdBy);
//				taskAllocationEntity.setCreatedDate(new Date());
//				taskAllocationEntity.setModulePrimaryId(transferRequisitionEntity.getTransferRequisitionId());
//				taskAllocationEntity.setIsActive(true);
//				taskAllocationRepository.save(taskAllocationEntity);
////				}
//
//				return taskAllocationEntity;
//
//			}

	@Override
	public ResponseEntity<?> generateTransferRequisition(TransferRequisitionModel transferRequisitionRequest) {
		log.info("Entering into MemberTransferInOutServiceImpl : generateTransferRequisition");

		Map<String, Object> response = new HashMap<>();

		try {
			Long draftRequisitionCount = transferRequisitionRepo.getTransferRequisitionCount();

			if (draftRequisitionCount != null && draftRequisitionCount == 0) {
				// create new draft transfer requisition
				TransferRequisitionEntity transferRequisitionEntity = new TransferRequisitionEntity();
				transferRequisitionEntity.setTransferRequestNumber(transferRequisitionRepo.generateSeq());
				transferRequisitionEntity.setTransferRequestDate(new Date());
				transferRequisitionEntity.setIsBulk("Y");
				transferRequisitionEntity.setRole(transferRequisitionRequest.getRole());
				transferRequisitionEntity.setLocationType(transferRequisitionRequest.getLocationType());
				transferRequisitionEntity.setTransferStatus("Draft");
				transferRequisitionEntity.setTransferSubStatus("Draft");
				transferRequisitionEntity.setCreatedOn(new Date());
				transferRequisitionEntity.setCreatedBy(transferRequisitionRequest.getUserName());
				TransferRequisitionEntity generatedRequisition = transferRequisitionRepo
						.save(transferRequisitionEntity);
				log.info(
						"MemberTransferInOutServiceImpl : generateTransferRequisition : saved new  draft transfer requisition");

				TransferRequisitionModel transferRequisitionResponse = new TransferRequisitionModel();
				transferRequisitionResponse.setTransferRequisitionId(generatedRequisition.getTransferRequisitionId());
				transferRequisitionResponse.setTransferRequestNumber(generatedRequisition.getTransferRequestNumber());
				transferRequisitionResponse.setTransferRequestDate(generatedRequisition.getTransferRequestDate());
				transferRequisitionResponse.setIsBulk(generatedRequisition.getIsBulk());
				transferRequisitionResponse.setRole(generatedRequisition.getRole());
				transferRequisitionResponse.setLocationType(generatedRequisition.getLocationType());
				transferRequisitionResponse.setTransferStatus(generatedRequisition.getTransferStatus());
				transferRequisitionResponse.setTransferSubStatus(generatedRequisition.getTransferSubStatus());
				transferRequisitionResponse.setCreatedOn(generatedRequisition.getCreatedOn());
				transferRequisitionResponse.setCreatedBy(generatedRequisition.getCreatedBy());
				transferRequisitionResponse.setUserName(generatedRequisition.getCreatedBy());

				response.put("transferRequisitionDetail", transferRequisitionResponse);
				response.put("responseMessage", "Draft Requisition generated with Transfer Request Number :  "
						+ generatedRequisition.getTransferRequestNumber());
				response.put("responseCode", "success");

			} else if (draftRequisitionCount != null && draftRequisitionCount == 1) {
				// fetch existing draft transfer requisition
				TransferRequisitionEntity transferRequisitionEntity = transferRequisitionRepo
						.getDraftTransferRequisition();

				TransferRequisitionModel transferRequisitionResponse = new TransferRequisitionModel();
				transferRequisitionResponse
						.setTransferRequisitionId(transferRequisitionEntity.getTransferRequisitionId());
				transferRequisitionResponse
						.setTransferRequestNumber(transferRequisitionEntity.getTransferRequestNumber());
				transferRequisitionResponse.setTransferRequestDate(transferRequisitionEntity.getTransferRequestDate());
				transferRequisitionResponse.setIsBulk(transferRequisitionEntity.getIsBulk());
				transferRequisitionResponse.setRole(transferRequisitionEntity.getRole());
				transferRequisitionResponse.setLocationType(transferRequisitionEntity.getLocationType());
				transferRequisitionResponse.setTransferStatus(transferRequisitionEntity.getTransferStatus());
				transferRequisitionResponse.setTransferSubStatus(transferRequisitionEntity.getTransferSubStatus());
				transferRequisitionResponse.setCreatedOn(transferRequisitionEntity.getCreatedOn());
				transferRequisitionResponse.setCreatedBy(transferRequisitionEntity.getCreatedBy());
				transferRequisitionResponse.setUserName(transferRequisitionEntity.getCreatedBy());

				response.put("transferRequisitionDetail", transferRequisitionResponse);
				response.put("responseMessage", "Exisitng Draft Requisition found, Transfer Request Number : "
						+ transferRequisitionEntity.getTransferRequestNumber() + " will be used");
				response.put("responseCode", "success");
			} else if (draftRequisitionCount != null && draftRequisitionCount > 1) {
				response.put("responseMessage", "More than one Draft Requisition found, cannot proceed");
				response.put("responseCode", "failed");
			}

		} catch (Exception ex) {
			response.put("responseMessage", "Draft Requisition generation failed due to : " + ex);
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ResponseEntity<?> RefundCalculationBulk(Long transferRequisitionId,
			TransferRefundCalculationDTO transferRefundCalculationDTO) {
		Map<String, Object> response = new HashMap<>();
		boolean check;

		/** validate product variant **/
		PreValidationRequest preValidationRequest = new PreValidationRequest();
		preValidationRequest.setEventName("calculation");
		preValidationRequest.setProductVariantOut(transferRefundCalculationDTO.getProductVariantOut());
		preValidationRequest.setProductIdOut(transferRefundCalculationDTO.getProductIdOut());
		check = validatePolicyProductVariant(preValidationRequest);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Product variant");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		/** validate unit code **/
		PreValidationRequest preValidationRequest1 = new PreValidationRequest();
		preValidationRequest1.setEventName("calculation");
		preValidationRequest1.setPolicyNumberOut(transferRefundCalculationDTO.getPolicyNumberOut());
		preValidationRequest1.setUnitCodeOut(transferRefundCalculationDTO.getUnitOut());
		check = validatePolicyUnit(preValidationRequest1);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Unit");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		;

		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository
				.findByIdAndIsActiveTrue(transferRefundCalculationDTO.getPolicyId());
		TransferRequisitionEntity transferRequisitionEntity = transferRequisitionRepo.findById(transferRequisitionId)
				.get();
		/** QSTG_MEMBER **/
//        List<MemberEntity> memberEntityList = memberRepository.findByMemberBatchId(transferRequisitionEntity.getBatchId());

		/** QSTG_MEMBER_BULK_STG **/
		List<MemberBulkStgEntity> memberBulkStgEntityList = memberBulkStgRepository
				.findByMemberBatchIdAndRecordStatus(transferRequisitionEntity.getBatchId(), "G");

		List<GratuityCalculationsDto> getGratitutyCalculationDto = new ArrayList<>();

		log.info(transferRefundCalculationDTO.getModeOfExit().equals(modeOfExitDeath));
		if (transferRefundCalculationDTO.getModeOfExit().equals(modeOfExitDeath)) {
			log.info("in death ");
			/*
			 * getGratitutyCalculationDto =
			 * policyClaimServiceImpl.futureForDeathRefundCalculation(
			 * masterPolicyEntity.getId(), masterMemberEntity.getId(),
			 * transferRefundCalculationDTO.getDateOfExit()); double refundPremium =
			 * getGratitutyCalculationDto.getRefundPremium(); double roundOff =
			 * Math.round(refundPremium*100)/100; log.info(roundOff); log.info("Refund GST"
			 * + getGratitutyCalculationDto.getRefundonGST()); log.info("Redund premium" +
			 * roundOff);
			 */
		} else {
			if (!memberBulkStgEntityList.isEmpty()) {
				for (MemberBulkStgEntity memberBulkStgEntity : memberBulkStgEntityList) {
					getGratitutyCalculationDto.add(policyClaimServiceImpl.CurrentRefundCalculation(
							masterPolicyEntity.getId(), memberBulkStgEntity.getPmstMemberId(),
							transferRefundCalculationDTO.getDateOfExit()));
				}
			}

		}
		double refundOnPremium = getGratitutyCalculationDto.stream()
				.flatMapToDouble(x -> DoubleStream.of(x.getRefundPremium())).sum();
		double gstOnpremium = getGratitutyCalculationDto.stream()
				.flatMapToDouble(x -> DoubleStream.of(x.getRefundonGST())).sum();
		BigDecimal decRefundOnPremium = new BigDecimal(refundOnPremium).setScale(2, RoundingMode.HALF_UP);
		BigDecimal decgstOnPremium = new BigDecimal(gstOnpremium).setScale(2, RoundingMode.HALF_UP);
		response.put("responseCode", "success");
		response.put("responseMessage", "Refund Calculated Successfully");
		response.put("refundGST", decgstOnPremium);
		response.put("refundPremium", decRefundOnPremium);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ResponseEntity<?> calculategratuityBulk(Long transferRequisitionId,
			TransferGratuityCalculationDTO transferGratuityCalculationDTO) {

		Map<String, Object> response = new HashMap<>();
		boolean check;

		/** validate product variant **/
		PreValidationRequest preValidationRequest = new PreValidationRequest();
		preValidationRequest.setEventName("calculation");
		preValidationRequest.setProductVariantOut(transferGratuityCalculationDTO.getProductVariantOut());
		preValidationRequest.setProductIdOut(transferGratuityCalculationDTO.getProductIdOut());
		check = validatePolicyProductVariant(preValidationRequest);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Product variant");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		/** validate unit code **/
		PreValidationRequest preValidationRequest1 = new PreValidationRequest();
		preValidationRequest1.setEventName("calculation");
		preValidationRequest1.setPolicyNumberOut(transferGratuityCalculationDTO.getPolicyNumberOut());
		preValidationRequest1.setUnitCodeOut(transferGratuityCalculationDTO.getUnitOut());
		check = validatePolicyUnit(preValidationRequest1);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Unit");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		List<Double> gratuityAmountList = new ArrayList<>();
		TransferRequisitionEntity transferRequisitionEntity = transferRequisitionRepo.findById(transferRequisitionId)
				.get();
		/** QSTG_MEMBER **/
		// List<MemberEntity> memberEntityList =
		// memberRepository.findByMemberBatchId(transferRequisitionEntity.getBatchId());

		/** QSTG_MEMBER_BULK_STG **/
		List<MemberBulkStgEntity> memberBulkStgEntityList = memberBulkStgRepository
				.findByMemberBatchIdAndRecordStatus(transferRequisitionEntity.getBatchId(), "G");

		if (!memberBulkStgEntityList.isEmpty()) {
			for (MemberBulkStgEntity memberBulkStgEntity : memberBulkStgEntityList) {

				PolicyMemberEntity policyMemberEntity = policyMemberRepository.findByPolicyIdandMemberId(
						transferGratuityCalculationDTO.getPolicyId(), memberBulkStgEntity.getPmstMemberId());
				if (policyMemberEntity != null) {
					// if (transferGratuityCalculationDTO.getSalary() == null) {
					transferGratuityCalculationDTO.setSalary(policyMemberEntity.getBasicSalary());
					// }
					Integer pastservice = 0;
					Date dateOfAppointmentdiff = null;
					String dateOfExitdiff;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date dateOfAppointment = policyMemberEntity.getDateOfAppointment();
					Date str_DateofExit = transferGratuityCalculationDTO.getDateOfExit();
					String[] get = dateOfAppointment.toString().split(" ");
					log.info(get[0]);
					LocalDate startDate = LocalDate.parse(get[0]);
					dateOfExitdiff = sdf.format(str_DateofExit);
					LocalDate endDate = LocalDate.parse(dateOfExitdiff);
					Period period = Period.between(startDate, endDate);
					System.out.printf("The difference between %s and %s is %d years, %d months, and %d days.",
							dateOfAppointmentdiff, dateOfExitdiff, period.getYears(), period.getMonths(),
							period.getDays());

					if (period.getMonths() <= 6) {
						pastservice = period.getYears();
					}

					if (period.getMonths() == 6 && period.getDays() > 0) {
						pastservice = period.getYears() + 1;
					}

					if (period.getMonths() > 6) {
						pastservice = period.getYears() + 1;
					}
					double gratuityAmount = 0.0;
					Double calcgvalue = 0.0;
					List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntities = policyGratuityBenefitRepository
							.findBypolicyIdAndCategoryId(transferGratuityCalculationDTO.getPolicyId(),
									policyMemberEntity.getCategoryId());
					if (policyGratuityBenefitEntities != null && !policyGratuityBenefitEntities.isEmpty()) {
						for (PolicyGratuityBenefitEntity policyGratuityBenefitEntity : policyGratuityBenefitEntities) {
							if (policyGratuityBenefitEntity.getGratutiyBenefitTypeId() == 25
									|| policyGratuityBenefitEntity.getGratutiyBenefitTypeId() == 26) {
								for (PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity : policyGratuityBenefitEntity
										.getGratuityBenefits()) {
									if (policyGratuityBenefitPropsEntity.getIsActive()) {
										gratuityAmount = gratuityAmount
												+ ((policyGratuityBenefitPropsEntity.getNumberOfDaysWage().doubleValue()
														/ policyGratuityBenefitPropsEntity
																.getNumberOfWorkingDaysPerMonth().doubleValue())
														* transferGratuityCalculationDTO.getSalary())
														* pastservice.doubleValue();
									}
								}
							}
							if (policyGratuityBenefitEntity.getGratutiyBenefitTypeId() == 27
									&& policyGratuityBenefitEntity.getGratutiySubBenefitId() == 185) {
								for (PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity : policyGratuityBenefitEntity
										.getGratuityBenefits()) {

									if (policyGratuityBenefitPropsEntity.getNumberOfYearsOfService() >= pastservice
											|| policyGratuityBenefitPropsEntity
													.getNumberOfYearsOfService() <= pastservice) {
										if (policyGratuityBenefitPropsEntity.getIsActive()) {
											calcgvalue = calcgvalue + ((policyGratuityBenefitPropsEntity
													.getNumberOfDaysWage().doubleValue()
													/ policyGratuityBenefitPropsEntity.getNumberOfWorkingDaysPerMonth()
															.doubleValue())
													* transferGratuityCalculationDTO.getSalary())
													* policyGratuityBenefitPropsEntity.getNumberOfYearsOfService()
															.doubleValue();
											gratuityAmount = gratuityAmount + calcgvalue;
											break;
										}
									}
								}
							}
							if (policyGratuityBenefitEntity.getGratutiyBenefitTypeId() == 27
									&& policyGratuityBenefitEntity.getGratutiySubBenefitId() == 186) {
								for (PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity : policyGratuityBenefitEntity
										.getGratuityBenefits()) {
									if (policyGratuityBenefitPropsEntity.getIsActive()) {
										if (policyGratuityBenefitPropsEntity
												.getNumberOfYearsOfService() <= pastservice) {

											calcgvalue = calcgvalue + ((policyGratuityBenefitPropsEntity
													.getNumberOfDaysWage().doubleValue()
													/ policyGratuityBenefitPropsEntity.getNumberOfWorkingDaysPerMonth()
															.doubleValue()
													* transferGratuityCalculationDTO.getSalary())
													* policyGratuityBenefitPropsEntity.getNumberOfYearsOfService()
															.doubleValue());
											gratuityAmount = gratuityAmount + calcgvalue;
										}
									}
								}
							}
						}
						gratuityAmountList.add(gratuityAmount);
					} else {
						response.put("responseMessage", "No Gratuity Benefit Found by Policy ID and Category ID");
						response.put("responseCode", "failed");
						return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
					}
				} else {
					response.put("responseMessage", "No member Found by Policy ID and Member ID");
					response.put("responseCode", "failed");
					return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
				}
			}
		} else {
			response.put("responseMessage", "No member uploaded to compute");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
		}
		Double totalgratuityAmount = gratuityAmountList.stream().mapToDouble(x -> x).sum();
		response.put("responseCode", "success");
		response.put("responseMessage", "Gratuity calculated successfully");
		response.put("Gratuityamount", Math.round(totalgratuityAmount * 100) / 100);
		// response.put("Pastservice", pastservice);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}