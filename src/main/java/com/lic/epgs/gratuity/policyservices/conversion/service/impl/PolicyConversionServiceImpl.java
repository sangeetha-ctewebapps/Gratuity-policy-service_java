package com.lic.epgs.gratuity.policyservices.conversion.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.lic.epgs.gratuity.accountingservice.dto.GlTransactionModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.GstDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.JournalVoucherDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.RenewalContributionAdjustRequestModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.RenewalContrinAdjustDebitCreditRequestModelDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.GratutityIcodesEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.GratutityIcodesRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.service.CommonService;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.common.utils.NumericUtils;
import com.lic.epgs.gratuity.mph.entity.HistoryMPHEntity;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.repository.HistoryMPHRepository;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.dto.PayoutApproveRequestDto;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpConversionPropsEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.helper.PolicyGratuityBenefitHelper;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.HistoryGratutiyBenefitRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.integration.dto.CommonResponseDto;
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
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyContrySummaryRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyDepositRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContrySummaryRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyDepositRepository;
import com.lic.epgs.gratuity.policy.renewal.dto.RenewalPolicySearchDto;
import com.lic.epgs.gratuity.policy.renewal.repository.PolicyRenewalRemainderRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.QuotationRenewalDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalQuotationSearchDTo;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
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
import com.lic.epgs.gratuity.policyservices.common.enums.PolicyServiceName;
import com.lic.epgs.gratuity.policyservices.common.repository.PolicyServicingRepository;
import com.lic.epgs.gratuity.policyservices.conversion.constant.PolicyLevelConversionConstants;
import com.lic.epgs.gratuity.policyservices.conversion.dao.PolicyConversionDao;
import com.lic.epgs.gratuity.policyservices.conversion.dto.CheckActiveQuatation;
import com.lic.epgs.gratuity.policyservices.conversion.dto.ConvertPolicyRequestModel;
import com.lic.epgs.gratuity.policyservices.conversion.dto.GstDetailModel;
import com.lic.epgs.gratuity.policyservices.conversion.dto.PolicyConversionDto;
import com.lic.epgs.gratuity.policyservices.conversion.dto.PolicyLevelConversionDto;
import com.lic.epgs.gratuity.policyservices.conversion.dto.TrnRegisModel;
import com.lic.epgs.gratuity.policyservices.conversion.dto.datatable.GetPolicyConversionDetailsRequestDataTableDto;
import com.lic.epgs.gratuity.policyservices.conversion.dto.datatable.GetPolicyConversionDetailsResponseDataTableDto;
import com.lic.epgs.gratuity.policyservices.conversion.entity.PolicyLevelConversionTempEntity;
import com.lic.epgs.gratuity.policyservices.conversion.repository.PolicyLevelConversionTempRepository;
import com.lic.epgs.gratuity.policyservices.conversion.repository.PsPmstTmpConversionPropsRepository;
import com.lic.epgs.gratuity.policyservices.conversion.service.PolicyConversionService;
import com.lic.epgs.gratuity.policyservices.conversion.service.PsPolicyService;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.DataTable;
import com.lic.epgs.gratuity.quotation.helper.QuotationHelper;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;

@Service
public class PolicyConversionServiceImpl implements PolicyConversionService {

	public static final Long DRAFT = 11L;
	public static final Long PENDING_FOR_APPROVAL = 22L;
	public static final Long APPROVED = 33L;
	public static final Long REJECTED = 44L;
	public static final Long SEND_TO_MAKER = 55L;
	public static final Long POLICY_CONVERSION = 17L;
	public static final Long POLICY_CONVERTED=482L;

	private final Logger logger = LogManager.getLogger(PolicyConversionServiceImpl.class);

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CommonService commonService;

	@Autowired
	PolicyLevelConversionTempRepository policyLevelConversionTempRepository;

	@Autowired
	PolicyConversionDao policyConversionDao;

	@Autowired(required = true)
	PolicyServicingRepository policyServicingRepository;

	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;

	@Autowired
	private RenewalPolicyTMPCustomRepository renewalPolicyTMPCustomRepository;

	private static Long quotationStatusId = 16l;
	private static Long quotationSubStatusId = 21l;
	private static Long quotationTaggedStatusId = 78l;

	@Autowired
	private PolicyServiceRepository policyServiceRepository;

	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

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
	private PolicyMasterValuationRepository policyMasterValuationRepository;

	@Autowired
	PsPmstTmpConversionPropsRepository psPmstTmpConversionPropsRepository;

	@Autowired
	private MPHRepository mPHRepository;

	@Autowired
	PsPolicyService psPolicyService;

	@Autowired
	MemberCategoryModuleRepository memberCategoryModuleRepository;
	
	@Autowired
	MasterPolicyAdjustmentDetailRepository masterPolicyAdjustmentDetailRepository;
	
	@Autowired
	PolicyAdjustmentDetailRepository policyAdjustmentDetailRepository;

	// @Override
	public CommonResponseDto<PolicyLevelConversionDto> savePolicyConversionDetails_ORIGINAL(
			PolicyLevelConversionDto policyLevelConversionDto) {
		logger.info("PolicyLevelConversionServiceImpl------savePolicyConversionDetails-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		PolicyLevelConversionTempEntity policyLevelConversionTempEntity = null;
		try {
			if (policyLevelConversionDto.getConversionId() != null) {
				policyLevelConversionTempEntity = policyLevelConversionTempRepository
						.findByConversionIdAndIsActiveTrue(policyLevelConversionDto.getConversionId());
				policyLevelConversionTempEntity = modelMapper.map(policyLevelConversionDto,
						PolicyLevelConversionTempEntity.class);

				policyLevelConversionTempEntity.setNewpolicyIssueDate(policyLevelConversionDto.getNewpolicyIssueDate());
				policyLevelConversionTempEntity.setNewPolicyProduct(policyLevelConversionDto.getNewPolicyProduct());
				policyLevelConversionTempEntity.setNewPolicyVariant(policyLevelConversionDto.getNewPolicyVariant());
				policyLevelConversionTempEntity.setNoOfCatalogue(policyLevelConversionDto.getNoOfCatalogue());
				policyLevelConversionTempEntity.setPrevFundBalancde(policyLevelConversionDto.getPrevFundBalancde());
				policyLevelConversionTempEntity.setNewpolicyStatus(policyLevelConversionDto.getNewpolicyStatus());
				policyLevelConversionTempEntity
						.setNewPolicyAnnualRenewalDate(policyLevelConversionDto.getNewPolicyAnnualRenewalDate());
				policyLevelConversionTempEntity.setCreatedOn(DateUtils.sysDate());
				policyLevelConversionTempEntity.setConversionStatus(DRAFT.intValue());
				policyLevelConversionTempEntity.setIsActive(true);
				policyLevelConversionTempEntity = policyLevelConversionTempRepository
						.save(policyLevelConversionTempEntity);

				commonResponseDto.setResponseData(
						modelMapper.map(policyLevelConversionTempEntity, PolicyLevelConversionDto.class));
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SAVE);
			} else {
//				if (NumericUtils.convertLongToString(policyLevelConversionDto.getNewPolicyVariant())
//						.equals(PolicyLevelConversionConstants.POLICY_VARIANT_DB)
//						|| NumericUtils.convertLongToString(policyLevelConversionDto.getNewPolicyVariant())
//								.equals(PolicyLevelConversionConstants.POLICY_VARIANT_DC)) {
//					commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAIL);
//					commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.VARIANT);
//					return commonResponseDto;
//				}
				policyLevelConversionTempEntity = modelMapper.map(policyLevelConversionDto,
						PolicyLevelConversionTempEntity.class);
				// policyLevelConversionTempEntity.setCreatedOn(DateUtils.sysDate());
				policyLevelConversionTempEntity.setIsActive(true);
				policyLevelConversionTempRepository.save(policyLevelConversionTempEntity);

//				// Set Service Id into policy services
//				PolicyServiceMasterEntity policyServiceMasterEntity = new PolicyServiceMasterEntity();
//				policyServiceMasterEntity.setCreatedOn(new Date());
//				policyServiceMasterEntity.setIsActive(true);
//				policyServiceMasterEntity.setServiceStatus("Active");
//				policyServiceMasterEntity.setServiceType(PolicyServiceName.CONVERSION.getName());
//				policyServiceMasterEntity.setPolicyNumber(policyLevelConversionTempEntity.getPrevPolicyNo());
//
//				PolicyServiceMasterEntity servicesEntity = policyServicingRepository.save(policyServiceMasterEntity);

//				call for backend process
				QuotationRenewalDto quotationRenewalDto = new QuotationRenewalDto();
				quotationRenewalDto.setPolicyId(policyLevelConversionDto.getPrevPolicyId());
				quotationRenewalDto.setCreatedBy("maker");
				quotationRenewalDto.setServiceType(PolicyServiceName.CONVERSION.getName());
				quotationRenewalDto.setCanStartAnotherService(0l);

				ApiResponseDto<RenewalPolicyTMPDto> createPolicyLevelConversion = CreatePolicyLevelConversion(
						quotationRenewalDto);

				policyLevelConversionTempEntity
						.setServiceId(createPolicyLevelConversion.getData().getPolicyServiceId());
				policyLevelConversionTempRepository.save(policyLevelConversionTempEntity);
				PolicyLevelConversionDto policyLevelConversion = modelMapper.map(policyLevelConversionTempEntity,
						PolicyLevelConversionDto.class);

				policyLevelConversion.setRenewalPolicyTMPDto(createPolicyLevelConversion.getData());
				commonResponseDto.setResponseData(policyLevelConversion);
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SAVE);
			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- savePolicyConversionDetails --", ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ savePolicyConversionDetails----------- Ended");
		return commonResponseDto;
	}

	@Override
	public CommonResponseDto<PolicyConversionDto> savePolicyConversionDetails(
			PolicyConversionDto policyLevelConversionDto) {
		logger.info("PolicyLevelConversionServiceImpl------savePolicyConversionDetails-------Started");
		CommonResponseDto<PolicyConversionDto> commonResponseDto = new CommonResponseDto<>();
		// PolicyLevelConversionTempEntity policyLevelConversionTempEntity = null;
		PmstTmpConversionPropsEntity pmstTmpConversionPropsEntity;
		try {
			if (policyLevelConversionDto.getConversionId() != null) {
				pmstTmpConversionPropsEntity = psPmstTmpConversionPropsRepository
						.findByIdAndIsActiveTrue(policyLevelConversionDto.getConversionId());

				pmstTmpConversionPropsEntity.setId(policyLevelConversionDto.getConversionId());
//				pmstTmpConversionPropsEntity
//						.setConversionRequestNumber(policyLevelConversionDto.getConversionRequestNumber());
//				pmstTmpConversionPropsEntity
//						.setConversionRequestDate(policyLevelConversionDto.getConversionRequestDate());
//				pmstTmpConversionPropsEntity
//						.setPolicyConversionDate(policyLevelConversionDto.getPolicyConversionDate());
//				pmstTmpConversionPropsEntity.setTotalFundValue(policyLevelConversionDto.getTotalFundValue());
//				pmstTmpConversionPropsEntity.setTmpPolicyID(policyLevelConversionDto.getTmpPolicyID());
//				pmstTmpConversionPropsEntity.setNewMasterPolicyID(policyLevelConversionDto.getNewMasterPolicyID());
//				pmstTmpConversionPropsEntity.setStatusID(policyLevelConversionDto.getStatusID());
//				pmstTmpConversionPropsEntity.setCreatedBy(policyLevelConversionDto.getCreatedBy());
				pmstTmpConversionPropsEntity.setModifiedBy(policyLevelConversionDto.getModifiedBy());
				pmstTmpConversionPropsEntity.setModifiedDate(new Date());
				pmstTmpConversionPropsEntity.setVariant(policyLevelConversionDto.getVariant());
				pmstTmpConversionPropsEntity.setPreviousFundBalance(policyLevelConversionDto.getPreviousFundBalance());// pmstTmpConversionPropsEntity.setCreatedDate(new
				pmstTmpConversionPropsEntity.setConversionProcessing(true);
				pmstTmpConversionPropsEntity.setConversionType(policyLevelConversionDto.getConversionType());
				//				pmstTmpConversionPropsEntity.setIsActive(true);
				pmstTmpConversionPropsEntity = psPmstTmpConversionPropsRepository.save(pmstTmpConversionPropsEntity);

				PolicyConversionDto	policyConversionDto = modelMapper.map(pmstTmpConversionPropsEntity,
						PolicyConversionDto.class);
				// commonResponseDto
				// .setResponseData(modelMapper.map(pmstTmpConversionPropsEntity,
				// PolicyLevelConversionDto.class));

				commonResponseDto.setResponseData(policyConversionDto);
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SAVE);
			} else {

				// call for backend process
				QuotationRenewalDto quotationRenewalDto = new QuotationRenewalDto();
				quotationRenewalDto.setPolicyId(policyLevelConversionDto.getPmstPolicyId());
				quotationRenewalDto.setCreatedBy("maker");
				quotationRenewalDto.setServiceType(PolicyServiceName.CONVERSION.getName());
				quotationRenewalDto.setCanStartAnotherService(0l);

				ApiResponseDto<RenewalPolicyTMPDto> createPolicyLevelConversion = CreatePolicyLevelConversion(
						quotationRenewalDto);

				pmstTmpConversionPropsEntity = modelMapper.map(policyLevelConversionDto,
						PmstTmpConversionPropsEntity.class);
				pmstTmpConversionPropsEntity.setIsActive(true);

				pmstTmpConversionPropsEntity
						.setConversionRequestNumber(policyLevelConversionDto.getConversionRequestNumber());
				pmstTmpConversionPropsEntity
						.setConversionRequestDate(policyLevelConversionDto.getConversionRequestDate());
				pmstTmpConversionPropsEntity
						.setPolicyConversionDate(policyLevelConversionDto.getPolicyConversionDate());
				pmstTmpConversionPropsEntity.setTotalFundValue(policyLevelConversionDto.getTotalFundValue());
				// pmstTmpConversionPropsEntity.setTmpPolicyID(policyLevelConversionDto.getTmpPolicyID());
				pmstTmpConversionPropsEntity.setTmpPolicyID(createPolicyLevelConversion.getData().getId());
				pmstTmpConversionPropsEntity.setNewMasterPolicyID(policyLevelConversionDto.getNewMasterPolicyID());
				pmstTmpConversionPropsEntity.setStatusID(policyLevelConversionDto.getStatusID());
				pmstTmpConversionPropsEntity.setPmstPolicyId(policyLevelConversionDto.getPmstPolicyId());
				pmstTmpConversionPropsEntity.setCreatedBy(policyLevelConversionDto.getCreatedBy());
				pmstTmpConversionPropsEntity.setCreatedDate(new Date());
				pmstTmpConversionPropsEntity.setModifiedBy(policyLevelConversionDto.getModifiedBy());
				pmstTmpConversionPropsEntity.setModifiedDate(new Date());
				pmstTmpConversionPropsEntity.setConversionProcessing(true);
				pmstTmpConversionPropsEntity.setConversionType(policyLevelConversionDto.getConversionType());

				pmstTmpConversionPropsEntity = psPmstTmpConversionPropsRepository.save(pmstTmpConversionPropsEntity);

				// pmstTmpConversionPropsEntity.setServiceId(createPolicyLevelConversion.getData().getPolicyServiceId());

				// psPmstTmpConversionPropsRepository.save(pmstTmpConversionPropsEntity);
//				PolicyLevelConversionDto policyLevelConversion = modelMapper.map(pmstTmpConversionPropsEntity,
//						PolicyLevelConversionDto.class);
//				
				policyLevelConversionDto.setConversionId(pmstTmpConversionPropsEntity.getId());

				policyLevelConversionDto.setRenewalPolicyTMPDto(createPolicyLevelConversion.getData());
				commonResponseDto.setResponseData(policyLevelConversionDto);
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SAVE);
			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- savePolicyConversionDetails --", ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ savePolicyConversionDetails----------- Ended");
		return commonResponseDto;
	}

//	public CommonResponseDto<List<PolicyLevelConversionDto>> getInprogressPolicyConversionDetailsList(String role,
//			String unitCode) {
//		logger.info("PolicyLevelConversionServiceImpl------getInprogressPolicyConversionDetailsList-------Started");
//		CommonResponseDto<List<PolicyLevelConversionDto>> commonResponseDto = new CommonResponseDto<>();
//		List<PolicyLevelConversionTempEntity> policyLevelConversionTempEntityList = new ArrayList<>();
//		List<PolicyLevelConversionDto> policyLevelConversionDtoList = new ArrayList<>();
//		try {
//			if (role.equalsIgnoreCase(PolicyLevelConversionConstants.MAKER)) {
//				policyLevelConversionTempEntityList = policyLevelConversionTempRepository
//						.findAllByConversionStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
//								PolicyLevelConversionConstants.inProgressPolicyConversionMaker(), unitCode);
//			} else if (role.equalsIgnoreCase(PolicyLevelConversionConstants.CHECKER)) {
//				policyLevelConversionTempEntityList = policyLevelConversionTempRepository
//						.findAllByConversionStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
//								PolicyLevelConversionConstants.inProgressPolicyConversionChecker(), unitCode);
//			}
//			if (!policyLevelConversionTempEntityList.isEmpty()) {
//				policyLevelConversionTempEntityList.forEach(policyConversionDetails -> {
//					PolicyLevelConversionDto policyLevelConversionDto = modelMapper.map(policyConversionDetails,
//							PolicyLevelConversionDto.class);
//					policyLevelConversionDtoList.add(policyLevelConversionDto);
//				});
//				commonResponseDto.setResponseData(policyLevelConversionDtoList);
//				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
//				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
//			}
//		} catch (ConstraintViolationException ce) {
//			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getInprogressPolicyConversionDetailsList --",
//					ce);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
//		}
//		logger.info(
//				"PolicyLevelConversionServiceImpl ------ getInprogressPolicyConversionDetailsList----------- Ended");
//		return commonResponseDto;
//	}

	@Override
	public CommonResponseDto<PolicyLevelConversionDto> sendToChecker(String conversionId, String modifiedBy) {
		logger.info("PolicyLevelConversionServiceImpl------sendToChecker-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PolicyLevelConversionTempEntity policyLevelConversionTempEntity = policyLevelConversionTempRepository
					.findByConversionIdAndIsActiveTrue(NumericUtils.convertStringToLong(conversionId));

			if (policyLevelConversionTempEntity != null) {
				policyLevelConversionTempEntity.setConversionStatus(PENDING_FOR_APPROVAL.intValue());
				policyLevelConversionTempEntity.setConversionWorkflowStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.SEND_FOR_APPROVAL));
				policyLevelConversionTempEntity.setModifiedBy(modifiedBy);
				policyLevelConversionTempEntity.setModifiedOn(DateUtils.sysDate());
				policyLevelConversionTempRepository.save(policyLevelConversionTempEntity);
				commonResponseDto.setResponseData(
						modelMapper.map(policyLevelConversionTempEntity, PolicyLevelConversionDto.class));
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- sendToChecker --", ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		} catch (PersistenceException pe) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- sendToChecker --", pe);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ sendToChecker----------- Ended");
		return commonResponseDto;
	}

//	public CommonResponseDto<List<PolicyLevelConversionDto>> getExistingPolicyConversionDetailsList(String role,
//			String unitCode) {
//		logger.info("PolicyLevelConversionServiceImpl------getExistingPolicyConversionDetailsList-------Started");
//		CommonResponseDto<List<PolicyLevelConversionDto>> commonResponseDto = new CommonResponseDto<>();
//		List<PolicyLevelConversionTempEntity> policyLevelConversionEntityList = new ArrayList<>();
//		List<PolicyLevelConversionDto> policyLevelConversionDtoList = new ArrayList<>();
//		try {
//			if (role.equalsIgnoreCase(PolicyLevelConversionConstants.MAKER)
//					|| role.equalsIgnoreCase(PolicyLevelConversionConstants.CHECKER)) {
//				policyLevelConversionEntityList = policyLevelConversionTempRepository
//						.findAllByConversionStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
//								PolicyLevelConversionConstants.existingDetails(), unitCode);
//			}
//			if (!policyLevelConversionEntityList.isEmpty()) {
//				policyLevelConversionEntityList.forEach(policyConversionDetails -> {
//					PolicyLevelConversionDto policyLevelConversionDto = modelMapper.map(policyConversionDetails,
//							PolicyLevelConversionDto.class);
//					policyLevelConversionDtoList.add(policyLevelConversionDto);
//				});
//				commonResponseDto.setResponseData(policyLevelConversionDtoList);
//				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
//				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
//			}
//		} catch (ConstraintViolationException ce) {
//			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getExistingPolicyConversionDetailsList --",
//					ce);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
//		}
//		logger.info("PolicyLevelConversionServiceImpl ------ getExistingPolicyConversionDetailsList----------- Ended");
//		return commonResponseDto;
//	}

	@Override
	public CommonResponseDto<PolicyLevelConversionDto> approvedPolicyLevelConversion(
			PolicyLevelConversionDto policyLevelConversionDto) {
		logger.info("PolicyLevelConversionServiceImpl------approvedPolicyLevelConversion-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PolicyLevelConversionTempEntity policyLevelConversionTempEntity = policyLevelConversionTempRepository
					.findByConversionIdAndIsActiveTrue(policyLevelConversionDto.getConversionId());

			if (policyLevelConversionTempEntity != null) {
				policyLevelConversionTempEntity.setConversionStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.SEND_FOR_APPROVAL));
				policyLevelConversionTempEntity.setConversionStatus(APPROVED.intValue());
				policyLevelConversionTempEntity.setConversionWorkflowStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.SEND_FOR_APPROVAL));
				policyLevelConversionTempEntity.setModifiedOn(DateUtils.sysDate());
				;
				policyLevelConversionTempRepository.save(policyLevelConversionTempEntity);
				commonResponseDto.setResponseData(
						modelMapper.map(policyLevelConversionTempEntity, PolicyLevelConversionDto.class));
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- sendToChecker --", ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		} catch (PersistenceException pe) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- sendToChecker --", pe);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ sendToChecker----------- Ended");
		return commonResponseDto;
	}

	@Override
	public CommonResponseDto<PolicyLevelConversionDto> sendToMaker(String conversionId, String modifiedBy) {
		logger.info("PolicyLevelConversionServiceImpl------sendToMaker-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PolicyLevelConversionTempEntity policyLevelConversionTempEntity = policyLevelConversionTempRepository
					.findByConversionIdAndIsActiveTrue(NumericUtils.convertStringToLong(conversionId));

			if (policyLevelConversionTempEntity != null) {
				policyLevelConversionTempEntity.setConversionStatus(SEND_TO_MAKER.intValue());
				policyLevelConversionTempEntity.setConversionWorkflowStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.SEND_TO_MAKER));
				policyLevelConversionTempEntity.setModifiedBy(modifiedBy);
				policyLevelConversionTempEntity.setModifiedOn(DateUtils.sysDate());
				policyLevelConversionTempRepository.save(policyLevelConversionTempEntity);
				commonResponseDto.setResponseData(
						modelMapper.map(policyLevelConversionTempEntity, PolicyLevelConversionDto.class));
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- approved --", ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		} catch (PersistenceException pe) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- approved --", pe);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ approved----------- Ended");
		return commonResponseDto;
	}

	@Override
	public CommonResponseDto<PolicyLevelConversionDto> rejectedConversion(
			PolicyLevelConversionDto policyLevelConversionDto) {
		logger.info("PolicyLevelConversionServiceImpl------rejection-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PolicyLevelConversionTempEntity policyLevelConversionTempEntity = policyLevelConversionTempRepository
					.findByConversionIdAndIsActiveTrue(policyLevelConversionDto.getConversionId());

			if (policyLevelConversionTempEntity != null) {
				policyLevelConversionTempEntity.setConversionStatus(REJECTED.intValue());

				policyLevelConversionTempEntity.setRejectionRemarks(policyLevelConversionDto.getRejectionRemarks());
				policyLevelConversionTempEntity
						.setRejectionReasonCode(policyLevelConversionDto.getRejectionReasonCode());
				policyLevelConversionTempEntity.setModifiedOn(DateUtils.sysDate());
				policyLevelConversionTempRepository.save(policyLevelConversionTempEntity);
				commonResponseDto.setResponseData(
						modelMapper.map(policyLevelConversionTempEntity, PolicyLevelConversionDto.class));
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- rejection --", ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		} catch (PersistenceException pe) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- rejection--", pe);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ rejection----------- Ended");
		return commonResponseDto;
	}

	@Override
	public DataTable getPolicyConversionDetailsDataTable(
			GetPolicyConversionDetailsRequestDataTableDto getPolicyConversionDetailsRequestDataTableDto) {

		DataTable dataTable = new DataTable();
		try {
			List<GetPolicyConversionDetailsResponseDataTableDto> getPolicyConversionDetailsResponseDataTableDtoList = policyConversionDao
					.getPolicyConversionDetailsDataTable(getPolicyConversionDetailsRequestDataTableDto);

			dataTable.setData(getPolicyConversionDetailsResponseDataTableDtoList);

			if (getPolicyConversionDetailsResponseDataTableDtoList.size() > 0) {
				dataTable.setNoOfPages(getPolicyConversionDetailsResponseDataTableDtoList.get(0).getNoOfPages());
				dataTable.setRecordsTotal(getPolicyConversionDetailsResponseDataTableDtoList.get(0).getRecordsTotal());
			}

			return dataTable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataTable;

	}

	@Override
	public ApiResponseDto<List<PolicyDto>> fetchPolicyForPS(RenewalPolicySearchDto renewalPolicySearchDto) {
		if (renewalPolicySearchDto.getPolicyNumber().isEmpty()) {
			List<MasterPolicyEntity> fetchpolicyDetails = masterPolicyCustomRepository.fetchpolicyDetails();

			return ApiResponseDto
					.success(fetchpolicyDetails.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));
		} else {
			List<MasterPolicyEntity> checkPolicyNoIsAlreadyProcessorNot = masterPolicyCustomRepository
					.findByPolicyNumberValidation(renewalPolicySearchDto.getPolicyNumber());

			if (checkPolicyNoIsAlreadyProcessorNot.isEmpty()) {
				return ApiResponseDto.errorMessage(null, null, "Renewals Not eliglible");
			}

			List<MasterPolicyEntity> fetchpolicyDetails = masterPolicyCustomRepository
					.findByPolicyNumber(renewalPolicySearchDto.getPolicyNumber());
			if (fetchpolicyDetails.size() > 0) {
				return ApiResponseDto.success(
						fetchpolicyDetails.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));
			} else {
				return ApiResponseDto.errorMsg(null, null, "Renewals Already Processed for this Policy");
			}

		}

	}

	@Override
	public ApiResponseDto<List<RenewalPolicyTMPDto>> fetchPolicyForPSSearch(
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

//	save master data into temp
//	@Override
//	public ApiResponseDto<RenewalPolicyTMPDto> CreatePolicyLevelConversion(QuotationRenewalDto quotationRenewalDto) {
//
//		if (policyServiceRepository
//				.findByPolicyandType(quotationRenewalDto.getPolicyId(), quotationRenewalDto.getServiceType())
//				.size() > 0) {
//			return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
//		} else {
//
////			PolicyRenewalRemainderEntity updateStatus= policyRenewalRemainderRepository.findBypolicyId(quotationRenewalDto.getPolicyId());
////	updateStatus.setIsActive(false);
////	policyRenewalRemainderRepository.save(updateStatus);
//			PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
//
//			policyServiceEntitiy.setServiceType(quotationRenewalDto.getServiceType());
//			policyServiceEntitiy.setPolicyId(quotationRenewalDto.getPolicyId());
//			policyServiceEntitiy.setCreatedBy(quotationRenewalDto.getCreatedBy());
//			policyServiceEntitiy.setCreatedDate(new Date());
//			policyServiceEntitiy.setIsActive(true);
//			policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);
//
//			MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository
//					.findById(quotationRenewalDto.getPolicyId());
//
//			RenewalPolicyTMPEntity renewalPolicyTMPEntity = RenewalPolicyTMPHelper.pmsttoTmp(masterPolicyEntity);
//			renewalPolicyTMPEntity.setId(null);
//			renewalPolicyTMPEntity.setPolicyStatusId(null);
//			renewalPolicyTMPEntity.setPolicySubStatusId(null);
//			renewalPolicyTMPEntity.setPolicytaggedStatusId(null);
//			renewalPolicyTMPEntity.setQuotationStatusId(quotationStatusId);
//			renewalPolicyTMPEntity.setQuotationSubStatusId(quotationSubStatusId);
//			renewalPolicyTMPEntity.setQuotationTaggedStatusId(quotationTaggedStatusId);
//			renewalPolicyTMPEntity.setMasterPolicyId(quotationRenewalDto.getPolicyId());
//			renewalPolicyTMPEntity.setQuotationDate(quotationRenewalDto.getQuotationDate());
//			renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
//			renewalPolicyTMPEntity.setIsActive(true);
//			renewalPolicyTMPEntity.setCreatedBy(quotationRenewalDto.getCreatedBy());
//			renewalPolicyTMPEntity.setCreatedDate(new Date());
//			renewalPolicyTMPEntity.setQuotationNumber(RenewalPolicyTMPHelper
//					.nextQuotationNumber(renewalPolicyTMPRepository.maxQuotationNumber()).toString());
//
//			renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
//
////		master policy id 
//
//			// PASS POLICY ID AND TEMP POLICY ID
//			psPolicyService.savePolicyDataMasterToTemp(masterPolicyEntity.getId(), renewalPolicyTMPEntity.getId());
//
//			MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
//			TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytomastertoTemp(renewalPolicyTMPEntity.getId(),
//					mPHEntity);
//			tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
//			renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
//			renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
//			// renewalPolicyTMPEntity.getId()
//
//			// scheme MastrePolicy to TMp copy
//
//			Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
//					.findBypolicyId(quotationRenewalDto.getPolicyId());
//
//			if (policySchemeEntity.isPresent()) {
//
//				RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = RenewalSchemeruleTMPHelper
//						.pmsttoTmp(policySchemeEntity.get());
//				renewalSchemeruleTMPEntity.setId(null);
//				renewalSchemeruleTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
//				renewalSchemeruleTMPEntity.setPmstSchemeRuleId(policySchemeEntity.get().getId());
//
//				renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);
//			}
//			List<MemberCategoryEntity> addMemberCategoryEntity = new ArrayList<MemberCategoryEntity>();
//			List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
//					.findBypmstPolicyId(quotationRenewalDto.getPolicyId());
//			for (MemberCategoryEntity getmemberCategoryEntity : memberCategoryEntity) {
//				getmemberCategoryEntity.setPmstTmpPolicy(renewalPolicyTMPEntity.getId());
//				addMemberCategoryEntity.add(getmemberCategoryEntity);
//			}
//			memberCategoryRepository.saveAll(addMemberCategoryEntity);
//
//			List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
//					.findByPolicyId(quotationRenewalDto.getPolicyId());
//			List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
//					.copyToTmpLifeCoverforClaim(policyLifeCoverEntity, memberCategoryEntity,
//							renewalPolicyTMPEntity.getId());
//
//			renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
////		policyLifeCoverRepository.updateISActive(quotationRenewalDto.getPolicyId());
//			// member category
//
//			// Gratuity
//
//			List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
//					.findBypolicyId(quotationRenewalDto.getPolicyId());
//			List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
//					.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
//							renewalPolicyTMPEntity.getId());
//
//			renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
////		policyGratuityBenefitRepository.updateIsActive(quotationRenewalDto.getPolicyId());
//
//			// valuation
//			Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
//					.findByPolicyId(quotationRenewalDto.getPolicyId());
//			if (policyValuationEntity.isPresent()) {
//				RenewalValuationTMPEntity renewalValuationTMPEntity = RenewalValuationTMPHelper
//						.pmsttoTmp(policyValuationEntity.get());
//				renewalValuationTMPEntity.setId(null);
//				renewalValuationTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
//				renewalValuationTMPEntity.setPmstValuationId(policyValuationEntity.get().getId());
//				renewalValuationTMPRepository.save(renewalValuationTMPEntity);
//			}
//
//			// valuation Matrix
//			Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
//					.findByPolicyId(quotationRenewalDto.getPolicyId());
//			if (policyValuationMatrixEntity.isPresent()) {
//				RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = RenewalValuationMatrixTMPHelper
//						.pmsttoTmp(policyValuationMatrixEntity.get());
//				renewalValuationMatrixTMPEntity.setId(null);
//				renewalValuationMatrixTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
//				renewalValuationMatrixTMPEntity.setPmstValuationMatrixId(policyValuationMatrixEntity.get().getId());
//				renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);
//
//				if (renewalValuationMatrixTMPEntity.getValuationTypeId() == 1) {
//					Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
//							.findByPolicyId(quotationRenewalDto.getPolicyId());
//					if (policyValutationBasicEntity.isPresent()) {
//						RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = RenewalValuationBasicTMPHelper
//								.pmsttoTmp(policyValutationBasicEntity.get());
//						renewalValuationBasicTMPEntity.setId(null);
////					renewalValuationBasicTMPEntity.setValuationTypeId(renewalValuationTypeId);
//						renewalValuationBasicTMPEntity
//								.setPmstValuationBasicId(policyValutationBasicEntity.get().getId());
//						renewalValuationBasicTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
//						renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);
//
//						// ValuationWithdrawalRate table
//						List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
//								.findByPolicyId(quotationRenewalDto.getPolicyId());
//						if (policyValuationWithdrawalRateEntity.size() > 0) {
//							List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = new ArrayList<RenewalValuationWithdrawalTMPEntity>();
//							for (PolicyValuationWithdrawalRateEntity getpolicyValuationWithdrawalRateEntity : policyValuationWithdrawalRateEntity) {
//								RenewalValuationWithdrawalTMPEntity newrenewalValuationWithdrawalTMPEntity = RenewalValuationTMPHelper
//										.pmsttoTmp(getpolicyValuationWithdrawalRateEntity);
//								newrenewalValuationWithdrawalTMPEntity.setId(null);
//								newrenewalValuationWithdrawalTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
//								newrenewalValuationWithdrawalTMPEntity
//										.setPmstValWithDrawalId(getpolicyValuationWithdrawalRateEntity.getId());
//								renewalValuationWithdrawalTMPEntity.add(newrenewalValuationWithdrawalTMPEntity);
//							}
//							renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);
//						}
//					}
//				}
//			}
//
//			return ApiResponseDto.success(RenewalValuationTMPHelper.entityToDto(renewalPolicyTMPEntity));
//		}
//
//	}

	@Transactional
	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> CreatePolicyLevelConversion(QuotationRenewalDto quotationRenewalDto) {

//		if (policyServiceRepository
//				.findByPolicyandType(quotationRenewalDto.getPolicyId(), PolicyServiceName.CONVERSION.getName())
//				.size() > 0) {
//			return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
//		} else {

//			PolicyRenewalRemainderEntity updateStatus = policyRenewalRemainderRepository
//					.findBypolicyId(quotationRenewalDto.getPolicyId());
//			updateStatus.setIsActive(false);
//			policyRenewalRemainderRepository.save(updateStatus);
		PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();

		policyServiceEntitiy.setServiceType(quotationRenewalDto.getServiceType());
		policyServiceEntitiy.setPolicyId(quotationRenewalDto.getPolicyId());
		policyServiceEntitiy.setCreatedBy(quotationRenewalDto.getCreatedBy());
		policyServiceEntitiy.setCreatedDate(new Date());
		policyServiceEntitiy.setIsActive(true);
		policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

		MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository
				.findById(quotationRenewalDto.getPolicyId());

		List<String> getListNumer = renewalPolicyTMPRepository.addQuotationNumber(masterPolicyEntity.getPolicyNumber());
		String getQuotationNumber = null;
		if (getListNumer.size() > 0) {
			getQuotationNumber = QuotationHelper.addQuotationNumber(masterPolicyEntity.getPolicyNumber(),
					getListNumer.get(0));
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
		renewalPolicyTMPEntity.setQuotationDate(quotationRenewalDto.getQuotationDate());
		renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
		renewalPolicyTMPEntity.setIsActive(true);
		renewalPolicyTMPEntity.setCreatedBy(quotationRenewalDto.getCreatedBy());
		renewalPolicyTMPEntity.setCreatedDate(new Date());
		renewalPolicyTMPEntity.setQuotationNumber(getQuotationNumber);
		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		// PASS POLICY ID AND TEMP POLICY ID
		psPolicyService.savePolicyDataMasterToTemp(masterPolicyEntity.getId(), renewalPolicyTMPEntity.getId());

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

		List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
				.findByPolicyId(quotationRenewalDto.getPolicyId());
		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper.copyToTmpLifeCoverforClaim(
				policyLifeCoverEntity, memberCategoryEntity, renewalPolicyTMPEntity.getId());

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
			renewalValuationMatrixTMPEntity.setAmountPayable(null);	
			renewalValuationMatrixTMPEntity.setAmountReceived(null);
			renewalValuationMatrixTMPEntity.setBalanceToBePaid(null);
			renewalValuationMatrixTMPEntity.setCurrentServiceLiability(null);
			renewalValuationMatrixTMPEntity.setPastServiceLiability(null);
			renewalValuationMatrixTMPEntity.setFutureServiceLiability(null);
			renewalValuationMatrixTMPEntity.setGst(null);
			renewalValuationMatrixTMPEntity.setPremium(null);
			renewalValuationMatrixTMPEntity.setTotalPremium(null);
			renewalValuationMatrixTMPEntity.setTotalSumAssured(null);	
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
//					renewalValuationBasicTMPEntity.setValuationTypeId(renewalValuationTypeId);

					renewalValuationBasicTMPEntity.setPastServiceDeath(null);
					renewalValuationBasicTMPEntity.setPastServiceRetirement(null);
					renewalValuationBasicTMPEntity.setPastServiceWithdrawal(null);		
					renewalValuationBasicTMPEntity.setCurrentServiceDeath(null);
					renewalValuationBasicTMPEntity.setCurrentServiceRetirement(null);	
					renewalValuationBasicTMPEntity.setCurrentServiceWithdrawal(null);
					renewalValuationBasicTMPEntity.setAccruedGratuity(null);
					renewalValuationBasicTMPEntity.setTotalGratuity(null);	
					renewalValuationBasicTMPEntity.setPmstValuationBasicId(policyValutationBasicEntity.get().getId());
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

		return ApiResponseDto.success(RenewalValuationTMPHelper.entityToDto(renewalPolicyTMPEntity));

	}

	@Override
	public ApiResponseDto<List<RenewalPolicyTMPDto>> inprogressAndExistingConversionDetails(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo) {
//		List<RenewalPolicyTMPDto> newgetRenewalPolicyTMPEntity = new ArrayList<>(); 

		List<RenewalPolicyTMPDto> getRenewalPolicyTMPEntity = policyConversionDao
				.inprogressAndExistingConversionDetails(renewalQuotationSearchDTo);
//for (RenewalPolicyTMPDto renewalPolicyTMPDto : getRenewalPolicyTMPEntity) {
//	String s = renewalPolicyTMPRepository.getByPolicyNubmberbyid(renewalPolicyTMPDto.getDestinationTmpPolicyId());
//	renewalPolicyTMPDto.setDestinationPolicyNumber(s);
//	newgetRenewalPolicyTMPEntity.add(renewalPolicyTMPDto);
//}

		List<RenewalPolicyTMPDto> getRenewalPolicyTMPEntityupdate = new ArrayList<>();

		for (RenewalPolicyTMPDto renewalPolicyTMPDto : getRenewalPolicyTMPEntity) {
			getRenewalPolicyTMPEntityupdate.add(masterPolicyCustomRepository.setTransientValues(renewalPolicyTMPDto));
		}
		return ApiResponseDto.success(getRenewalPolicyTMPEntityupdate);

	}

	@Override
	public ApiResponseDto<List<RenewalPolicyTMPDto>> inprogressAndExistingConversionProcessing(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo) {
		List<RenewalPolicyTMPDto> getRenewalPolicyTMPEntity = policyConversionDao
				.inprogressAndExistingConversionProcessing(renewalQuotationSearchDTo);

		List<RenewalPolicyTMPDto> getRenewalPolicyTMPEntityupdate = new ArrayList<>();

		for (RenewalPolicyTMPDto renewalPolicyTMPDto : getRenewalPolicyTMPEntity) {
			getRenewalPolicyTMPEntityupdate.add(masterPolicyCustomRepository.setTransientValues(renewalPolicyTMPDto));
		}
		return ApiResponseDto.success(getRenewalPolicyTMPEntityupdate);
	}

	@Override
	public Object updateValuationType(String valuationType, Long conversionId) {
		CommonResponseDto<PolicyConversionDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PmstTmpConversionPropsEntity pmstTmpConversionPropsEntity = psPmstTmpConversionPropsRepository
					.findByIdAndIsActiveTrue(conversionId);
			pmstTmpConversionPropsEntity.setValuationType(valuationType);
			pmstTmpConversionPropsEntity = psPmstTmpConversionPropsRepository.save(pmstTmpConversionPropsEntity);
			return pmstTmpConversionPropsEntity;
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- savePolicyConversionDetails --", ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
			return commonResponseDto;
		}
	}

	@Override
	public Object getByConversionId(Long id) {
		CommonResponseDto<PolicyConversionDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PmstTmpConversionPropsEntity pmstTmpConversionPropsEntity = psPmstTmpConversionPropsRepository
					.findByIdAndIsActiveTrue(id);
			return pmstTmpConversionPropsEntity;
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- savePolicyConversionDetails --", ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
			return commonResponseDto;
		}
	}

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
	private MasterPolicyRepository masterPolicyRepository;

	@Autowired
	private PolicyHistoryRepository policyHistoryRepository;

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
	private TaskProcessRepository taskProcessRepository;

	@Autowired
	private TaskAllocationRepository taskAllocationRepository;

	@Autowired
	private Environment environment;
	
	@Autowired
	private GratutityIcodesRepository gratutityIcodesRepository;
		

	@Autowired
	@Qualifier(value = "restTemplateService")
	private RestTemplate restTemplateService;

// copy master to his & temp to master
	@Transactional
	@Override
	public ApiResponseDto<PolicyDto> conversionProcessingApprove(Long tempid, String username) {

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tempid).get();

		renewalPolicyTMPEntity.setPolicyStatusId(approvedStatudId);
		renewalPolicyTMPEntity.setPolicySubStatusId(approvedSubStatudId);

		renewalPolicyTMPEntity.setPolicytaggedStatusId(existingTaggedStatusId);

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

//		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getPolicyNumber());
//		taskallocationentity.setTaskStatus(approvedSubStatudId.toString());
//		taskAllocationRepository.save(taskallocationentity);

//		call for master--->his  & temp----->master
		MasterPolicyEntity getMasterPolicyEntity = policyMastertoPolicyHis(renewalPolicyTMPEntity, username);
		getMasterPolicyEntity.setPolicyStatusId(approvedStatudId);
		getMasterPolicyEntity.setPolicyTaggedStatusId(existingTaggedStatusId);
		
		
		
		masterPolicyRepository.save(getMasterPolicyEntity);

		
		
		
		
		
		
//		accounting API call
		if (isDevEnvironmentForRenewals == false) {
			String prodAndVarientCodeSame = commonModuleService.getProductCode(getMasterPolicyEntity.getProductId());
			String unitStateName = commonMasterUnitRepository
					.getStateNameByUnitCode(getMasterPolicyEntity.getUnitCode());
			String unitStateType = commonMasterStateRepository.getStateType(unitStateName);
			String unitStateCode = commonMasterStateRepository.getGSTStateCode(unitStateName);

			String mPHStateType = null;
			String mPHAddress = "";
			String mphStateCode = "";

			MPHEntity mPHEntity = mPHRepository.findById(getMasterPolicyEntity.getMasterpolicyHolder()).get();
			for (MPHAddressEntity getMPHAddressEntity : mPHEntity.getMphAddresses()) {
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

			PolicyContributionDetailEntity masterPolicyContributionEntity = policyContributionDetailRepository
					.findBymasterPolicyIdandType(tempid);
			String toGSTIn = mPHEntity.getGstIn() == null ? "" : mPHEntity.getGstIn();
			HSNCodeDto hSNCodeDto = accountingService.getHSNCode();
			Map<String, Double> gstComponents = accountingService.getGstComponents(unitStateType, mPHStateType,
					hSNCodeDto, masterPolicyContributionEntity.getLifePremium());

//			1
			ConvertPolicyRequestModel convertPolicyRequestModel = new ConvertPolicyRequestModel();
			GratutityIcodesEntity gratutityIcodesEntity = gratutityIcodesRepository.findByProductIdVariantId(getMasterPolicyEntity.getProductId(), getMasterPolicyEntity.getProductVariantId());	
//			String unitStateName = commonMasterUnitRepository.getStateNameByUnitCode(unitCode);
//			String unitStateType = commonMasterStateRepository.getStateType(unitStateName);
			
					
			convertPolicyRequestModel.setSourcePolicyNo(getMasterPolicyEntity.getPolicyNumber());  // policy number search
			convertPolicyRequestModel.setTargetPolicyNo(null);   // ?
			convertPolicyRequestModel.setSourceVersion(null);// source version 
			convertPolicyRequestModel.setTargetVersion("v3");  // v3
			convertPolicyRequestModel.setMphCode(mPHEntity.getMphCode()); // we will geted from the policy id
			convertPolicyRequestModel.setDebitConversionOutGoAmount(null); 
			       
            convertPolicyRequestModel.setIsLegacy("no");
            convertPolicyRequestModel.setAdjustmentNo(masterPolicyContributionEntity.getId().intValue());
            convertPolicyRequestModel.setICodeForLob(gratutityIcodesEntity.getIcodeBusinessLine().intValue());
            convertPolicyRequestModel.setICodeForProductLine(gratutityIcodesEntity.getIcodeProductLine().intValue());
            convertPolicyRequestModel.setICodeForVariant(gratutityIcodesEntity.getIcodeVarient().toString());
            convertPolicyRequestModel.setICodeForBusinessType(gratutityIcodesEntity.getIcodeBuinessType().intValue());
            convertPolicyRequestModel.setICodeForParticipatingType(gratutityIcodesEntity.getIcodeParticipatingType().intValue());
            convertPolicyRequestModel.setICodeForBusinessSegment(gratutityIcodesEntity.getIcodeBusinessSegment().intValue());
            convertPolicyRequestModel.setICodeForInvestmentPortfolio(0);
            convertPolicyRequestModel.setProductCode(prodAndVarientCodeSame);
            convertPolicyRequestModel.setVariantCode(prodAndVarientCodeSame);
            convertPolicyRequestModel.setOperatingUnitType("UO");
            convertPolicyRequestModel.setUnitCode(getMasterPolicyEntity.getUnitCode());
            convertPolicyRequestModel.setUserCode(username);
           
			ResponseDto responseDto = accountingService
					.commonmasterserviceAllUnitCode(getMasterPolicyEntity.getUnitCode());

//			2
			GstDetailModel gstDetailModel = new GstDetailModel();
			
			gstDetailModel.setAmountWithTax(masterPolicyContributionEntity.getLifePremium()
					+ masterPolicyContributionEntity.getGst() + masterPolicyContributionEntity.getCurrentServices()
					+ masterPolicyContributionEntity.getPastService().doubleValue());
			gstDetailModel.setAmountWithoutTax(masterPolicyContributionEntity.getLifePremium()
					+ masterPolicyContributionEntity.getCurrentServices()
					+ masterPolicyContributionEntity.getPastService().doubleValue());
			gstDetailModel.setCessAmount(0.0); // from Docu

			gstDetailModel.setCgstAmount(gstComponents.get("CGST"));
			gstDetailModel.setCgstRate(hSNCodeDto.getCgstRate());
			gstDetailModel.setCreatedBy(username);
			gstDetailModel.setCreatedDate(new Date());
			gstDetailModel.setEffectiveEndDate(""); // form docu
			gstDetailModel.setEffectiveStartDate(new Date());
			gstDetailModel.setEntryType(toGSTIn != null ? "B2B" : "B2C");
			gstDetailModel.setFromGstn(responseDto.getGstIn() == null ? "" : responseDto.getGstIn());
			gstDetailModel.setFromPan(responseDto.getPan());
			gstDetailModel.setFromStateCode(unitStateCode); // from MPH detail null

			if (renewalPolicyTMPEntity.getGstApplicableId() == 1l)
				gstDetailModel.setGstApplicableType("Taxable");
			else
				gstDetailModel.setGstApplicableType("Non-Taxable");

			gstDetailModel.setGstInvoiceNo(""); // From Docu
			gstDetailModel.setGstRate(accountingService.getGSTRate(unitStateType, mPHStateType, hSNCodeDto));
			gstDetailModel.setGstRefNo(renewalPolicyTMPEntity.getPolicyNumber());// From Docu
			gstDetailModel.setGstRefTransactionNo(renewalPolicyTMPEntity.getPolicyNumber());// From Docu
			gstDetailModel.setGstTransactionType("DEBIT");// From Docu
			gstDetailModel.setGstType("GST");// From Docu
			gstDetailModel.setHsnCode(hSNCodeDto.getHsnCode());
			gstDetailModel.setIgstAmount(gstComponents.get("IGST"));
			gstDetailModel.setIgstRate(hSNCodeDto.getIgstRate());
			gstDetailModel.setModifiedBy(0L); // from docu
			gstDetailModel.setModifiedDate(new Date()); // from Docu
			gstDetailModel.setMphAddress(mPHAddress);
			gstDetailModel.setMphName(mPHEntity.getMphName());
			gstDetailModel.setNatureOfTransaction(hSNCodeDto.getDescription());
			gstDetailModel.setOldInvoiceDate(new Date()); // From Docu
			gstDetailModel.setOldInvoiceNo("IN20123QE"); // From Docu
			gstDetailModel.setProductCode(prodAndVarientCodeSame);
			gstDetailModel.setRemarks("Gratuity RE Deposit Adjustment");
			gstDetailModel.setSgstAmount(gstComponents.get("SGST"));
			gstDetailModel.setSgstRate(hSNCodeDto.getSgstRate());
			gstDetailModel.setToGstIn(mPHEntity.getGstIn() == null ? "" : mPHEntity.getGstIn()); // From Docu from get
																									// Common Module
			gstDetailModel.setToPan(mPHEntity.getPan() == null ? "" : mPHEntity.getPan());
			gstDetailModel.setToStateCode(mphStateCode == null ? "" : mphStateCode);
			gstDetailModel.setTotalGstAmount(masterPolicyContributionEntity.getGst().doubleValue());
			gstDetailModel.setTransactionDate(new Date());
			gstDetailModel.setTransactionSubType("A"); // From Docu
			gstDetailModel.setTransactionType("C"); // From Docu
//			gstDetailModel.setUserCode(username);
			gstDetailModel.setUtgstAmount(gstComponents.get("UTGST"));
			gstDetailModel.setUtgstRate(hSNCodeDto.getUtgstRate());
			gstDetailModel.setVariantCode(prodAndVarientCodeSame);
			gstDetailModel.setYear(DateUtils.uniqueNoYYYY());
			gstDetailModel.setMonth(DateUtils.currentMonthName());
			convertPolicyRequestModel.setGstDetailModel(gstDetailModel);
			
			
			
			

//			3
			TrnRegisModel trnRegisModel = new TrnRegisModel();

			convertPolicyRequestModel.setTrnRegisModel(trnRegisModel);
			trnRegisModel.setReferenceIdType(null);
			trnRegisModel.setVan(null);
			trnRegisModel.setChallanNo(new BigInteger(masterPolicyContributionEntity.getChallanNo())); //pmst_deposite
			trnRegisModel.setPolicyNo(getMasterPolicyEntity.getPolicyNumber());
			trnRegisModel.setProposalNo(getMasterPolicyEntity.getProposalNumber());
			trnRegisModel.setReferenceDate(null);
			trnRegisModel.setAmount(null);
			trnRegisModel.setAmountType(null);
			trnRegisModel.setProductCode(prodAndVarientCodeSame);
			trnRegisModel.setVariantCode(prodAndVarientCodeSame);
			trnRegisModel.setValidityUptoDate(null);
			trnRegisModel.setStatus(0L);
			trnRegisModel.setCreatedBy(username);
			trnRegisModel.setPoolAccountCode(null);
			trnRegisModel.setBankUniqueId(null);
			trnRegisModel.setReason(null);
			

			
//			last call
			//accountingService.conversionConsumeDeposit(convertPolicyRequestModel);

		}

		return ApiResponseDto.success(PolicyHelper.entityToDto(getMasterPolicyEntity));
	}

	private MasterPolicyEntity policyMastertoPolicyHis(RenewalPolicyTMPEntity funcrenewalPolicyTMPEntity,
			String username) {
		MasterPolicyEntity newmasterPolicyEntity = null;
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = null;
		PolicyHistoryEntity policyHistoryEntity = null;
		// PolicyMaster to Copy History then Tmp Policy to Master Policy Update
	MasterPolicyEntity masterPolicyEntity = masterPolicyRepository
				.findById(funcrenewalPolicyTMPEntity.getMasterPolicyId()).get();
	masterPolicyEntity.setPolicyStatusId(POLICY_CONVERTED);
		if (masterPolicyEntity!=null) {
			MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();

			// MPH UPDATE from MAster to History update
			HistoryMPHEntity hisMPHEntity = PolicyClaimCommonHelper.copytomastertohis(mPHEntity);
			hisMPHEntity = historyMPHRepository.save(hisMPHEntity);

			// MPH UPDATE from MAster to History End
			renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(funcrenewalPolicyTMPEntity.getId()).get();

			policyHistoryEntity = policyHistoryRepository.save(
					PolicyHelper.policymasterToHisTransfer(masterPolicyEntity, renewalPolicyTMPEntity, username));

			policyHistoryEntity.setMasterpolicyHolder(hisMPHEntity.getId());
			policyHistoryRepository.save(policyHistoryEntity);

			newmasterPolicyEntity = PolicyHelper.createTemptoPolicyMaster(renewalPolicyTMPEntity, username);
		String 	policyNumber = masterPolicyRepository.getPolicyNumber(getPolicySeq());
		newmasterPolicyEntity.setPolicyNumber(policyNumber);	
		newmasterPolicyEntity.setOldPolicyNumber(masterPolicyEntity.getPolicyNumber());	
		if(masterPolicyEntity.getPolicyStatusId()!=123) {
				newmasterPolicyEntity.setDateOfCommencement(masterPolicyEntity.getAnnualRenewlDate());
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(newmasterPolicyEntity.getDateOfCommencement());
				calendar.add(Calendar.YEAR, 1);
				Date newDateARD = calendar.getTime();
				
				calendar.setTime(newDateARD);
				calendar.add(Calendar.DATE, -1);
				Date newPolicyEndDate = calendar.getTime();
				
				newmasterPolicyEntity.setAnnualRenewlDate(newDateARD);
			
				newmasterPolicyEntity.setPolicyStartDate(newmasterPolicyEntity.getDateOfCommencement());
				newmasterPolicyEntity.setPolicyEndDate(newPolicyEndDate);
				
				}else {
					
					if(masterPolicyEntity.getPolicyStatusId()!=127) {
						newmasterPolicyEntity.setDateOfCommencement(new Date());
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(newmasterPolicyEntity.getDateOfCommencement());
						calendar.add(Calendar.YEAR, 1);
						Date newDateARD = calendar.getTime();
						
						calendar.setTime(newDateARD);
						calendar.add(Calendar.DATE, -1);
						Date newPolicyEndDate = calendar.getTime();
						
						newmasterPolicyEntity.setAnnualRenewlDate(newDateARD);
				
						newmasterPolicyEntity.setPolicyStartDate(newmasterPolicyEntity.getDateOfCommencement());
						newmasterPolicyEntity.setPolicyEndDate(newPolicyEndDate);
					}
					
				}
			TempMPHEntity tempMPHentity = tempMPHRepository.findById(renewalPolicyTMPEntity.getMasterpolicyHolder())
					.get();

			MPHEntity newmPHEntity = mPHRepository.save(PolicyClaimCommonHelper.createtotemptomaster(tempMPHentity));
			
			
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
					.createTempToSchemeMaster(renewalSchemeruleTMPEntity, username,newmasterPolicyEntity.getId());
			policySchemeRuleRepository.save(newPolicySchemeEntity);
		}

		// PolicymasterLifeCoverRule to Copy HistoryLifeCOver then Update TmpLifeCover
		// to PolicymasterLifeCoverRule
		memberCategoryRepository.UpdateHistoryfromMasterPolicy(funcrenewalPolicyTMPEntity.getMasterPolicyId(),
				policyHistoryEntity.getId());
		List<MemberCategoryEntity> addMemberCategoryEntity = new ArrayList<MemberCategoryEntity>();
		
		List<MemberCategoryEntity> oldmemberCategoryEntity = memberCategoryRepository.getTempMembertoCreateMaster(renewalPolicyTMPEntity.getId());
		for (MemberCategoryEntity convertmemberCategoryEntity : oldmemberCategoryEntity) {
			MemberCategoryEntity getmemberCategoryEntity =new MemberCategoryEntity();
			getmemberCategoryEntity.setId(null);		
			getmemberCategoryEntity.setPmstPolicyId(newmasterPolicyEntity.getId());
			getmemberCategoryEntity.setPstgPolicyId(null);
			getmemberCategoryEntity.setQmstQuotationId(null);
			getmemberCategoryEntity.setQstgQuoationId(null);
			getmemberCategoryEntity.setPmstHisPolicyId(policyHistoryEntity.getId());
			getmemberCategoryEntity.setName(convertmemberCategoryEntity.getName());
			addMemberCategoryEntity.add(getmemberCategoryEntity);
		}
		addMemberCategoryEntity = memberCategoryRepository.saveAll(addMemberCategoryEntity);

		List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
				.findByPolicyId(funcrenewalPolicyTMPEntity.getMasterPolicyId());
		List<PolicyLifeCoverEntity> policyLifeCoverEntities = new ArrayList<PolicyLifeCoverEntity>();
		for (PolicyLifeCoverEntity addPolicyLifeCoverEntity : policyLifeCoverEntity) {

			historyLifeCoverRepository.save(PolicyLifeCoverHelper.policyMastertoHistransfer(addPolicyLifeCoverEntity,
					policyHistoryEntity.getId(), username));
		}

		
		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = renewalLifeCoverTMPRepository
				.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
		 policyLifeCoverEntities = PolicyLifeCoverHelper
					.updateTempToCreateLifecoverMaster(renewalLifeCoverTMPEntity, username, newmasterPolicyEntity.getId(),addMemberCategoryEntity,oldmemberCategoryEntity);
		
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
				PolicyGratuityBenefitEntities = RenewalGratuityBenefitTMPHelper
						.updateTempTocreatePolicyGratuityBenefitMaster(renewalGratuityBenefitTMPEntity, username,
								newmasterPolicyEntity.getId(),addMemberCategoryEntity,oldmemberCategoryEntity);
			
			policyGratuityBenefitRepository.saveAll(PolicyGratuityBenefitEntities);

		}

	

		// isactive false same masterpolicy for NB
		masterPolicyContributionDetailRepository.isacivefalse(newmasterPolicyEntity.getId());
		masterPolicyDepositRepository.isactivefalse(newmasterPolicyEntity.getId());
		masterPolicyAdjustmentDetailRepository.isactivefalse(newmasterPolicyEntity.getId());
		masterPolicyContributionRepository.isactivefalse(newmasterPolicyEntity.getId());

		// Copy to MasterPolicycontributionDetails
		MasterPolicyContributionDetails contributionMaster = PolicyHelper.createtmpentityToMaster(
				policyContributionDetailRepository.findBymasterTmpPolicyId(renewalPolicyTMPEntity.getId()),
				newmasterPolicyEntity.getId(), username);

		contributionMaster = masterPolicyContributionDetailRepository.save(contributionMaster);

	
		// copy Deposit to Master data
		List<MasterPolicyDepositEntity> newMasterPolicyDepositEntity = new ArrayList<MasterPolicyDepositEntity>();

		List<PolicyDepositEntity> policyDepositEntites = policyDepositRepository
				.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
		for (PolicyDepositEntity stagingDepositData : policyDepositEntites) {
			MasterPolicyDepositEntity newmasterPolicyDepositEntity = new ModelMapper().map(stagingDepositData,
					MasterPolicyDepositEntity.class);
			newmasterPolicyDepositEntity.setId(null);
			newmasterPolicyDepositEntity.setMasterPolicyId(newmasterPolicyEntity.getId());
			newmasterPolicyDepositEntity.setActive(true);
			newmasterPolicyDepositEntity.setContributionDetailId(contributionMaster.getId());

			newmasterPolicyDepositEntity = masterPolicyDepositRepository.save(newmasterPolicyDepositEntity);

			List<PolicyAdjustmentDetailEntity> policyAdjustmentDetailEntity = policyAdjustmentDetailRepository
					.findBydepositId(stagingDepositData.getId());
			for (PolicyAdjustmentDetailEntity getStagingAdjEntries : policyAdjustmentDetailEntity) {
				MasterPolicyAdjustmentDetailEntity masterPolicyAdjustmentDetailEntity = new ModelMapper()
						.map(getStagingAdjEntries, MasterPolicyAdjustmentDetailEntity.class);
				masterPolicyAdjustmentDetailEntity.setActive(true);
				masterPolicyAdjustmentDetailEntity
						.setContributionDetailId(contributionMaster.getId());
				masterPolicyAdjustmentDetailEntity.setDepositId(newmasterPolicyDepositEntity.getId());
				masterPolicyAdjustmentDetailEntity = masterPolicyAdjustmentDetailRepository
						.save(masterPolicyAdjustmentDetailEntity);

				List<PolicyContributionEntity> listPolicyContribution = policyContributionRepository
						.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
				for (PolicyContributionEntity getPolicyContributionEntity : listPolicyContribution) {
					if (getPolicyContributionEntity.getAdjConid().equals(getStagingAdjEntries.getId())) {
						MasterPolicyContributionEntity masterPolicyContributionEntity = new ModelMapper()
								.map(getPolicyContributionEntity, MasterPolicyContributionEntity.class);
						masterPolicyContributionEntity.setId(null);
						masterPolicyContributionEntity.setMasterPolicyId(newmasterPolicyEntity.getId());
						masterPolicyContributionEntity.setAdjConid(masterPolicyAdjustmentDetailEntity.getId());
						masterPolicyContributionEntity.setActive(true);
						masterPolicyContributionEntity.setCreatedBy(username);
						masterPolicyContributionEntity.setContributionDetailId(contributionMaster.getId());
						masterPolicyContributionRepository.save(masterPolicyContributionEntity);
					}
				}
			}

		}


		// Member Renewal Entity start
		try {
			List<RenewalPolicyTMPMemberEntity> tempMemberEntity = renewalPolicyTMPMemberRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			Integer licId = policyMemberRepository.maxLicNumber(renewalPolicyTMPEntity.getMasterPolicyId());
			List<PolicyMemberEntity> policyMemberEntityList = policyMemberRepository
					.findBypolicyId(renewalPolicyTMPEntity.getMasterPolicyId());
			for (PolicyMemberEntity newMemberEntity : policyMemberEntityList) {
				PolicyMemberEntity policyMemberEntity = null;
				policyMemberEntity = policyMemberRepository.findById(newMemberEntity.getId()).get();
				historyMemberRepository.save(PolicyMemberHelper.policymastertohistoryupdate(policyMemberEntity,
						policyHistoryEntity.getId(), username));

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

				policyMemberEntity = RenewalPolicyTMPMemberHelper.createTemptoPolicyMemberMaster(newTempMemberEntity,
						username,addMemberCategoryEntity,oldmemberCategoryEntity);
				policyMemberRepository.save(policyMemberEntity);
			}
			policyMemberRepository.updateIsActiveFalseForSourcePolicy(renewalPolicyTMPEntity.getMasterPolicyId());
//			policyMemberRepository.updatemasternotprocessedrenewalmemberinactive(renewalPolicyTMPEntity.getId(),
//					renewalPolicyTMPEntity.getMasterPolicyId());
//			policyMemberRepository.updaterenewalupdatememberinmasteractive(renewalPolicyTMPEntity.getId(),
//					renewalPolicyTMPEntity.getMasterPolicyId());

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
					.createTempToValuationMatrixMaster(renewalValuationMatrixTMPEntity, username);
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
					.createTempToValuationBasicMaster(renewalValuationBasicTMPEntity, username);
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

//			List<PolicyValuationWithdrawalRateEntity> oldList = policyValuationWithdrawalRateRepository
//					.deleteBypolicyId(newmasterPolicyEntity.getId());
			PolicyValuationWithdrawalRateEntity newPolicyValuationWithdrawalRateEntity = PolicyValuationMatrixHelper
					.createTempToValuationWithdrawalMaster(addrenewalValuationWithdrawalTMPEntity, username);
			newPolicyValuationWithdrawalRateEntity.setPolicyId(newmasterPolicyEntity.getId());
			policyValuationWithdrawalRateEntities.add(newPolicyValuationWithdrawalRateEntity);
		}
		policyValuationWithdrawalRateRepository.saveAll(policyValuationWithdrawalRateEntities);

		// End

		return newmasterPolicyEntity;

	}
	
	public synchronized String getPolicySeq() {
		return commonService.getSequence(HttpConstants.POLICY_MODULE);
	}


	@Override
	public CheckActiveQuatation checkActiveQuatation(String policyNumber, Long policyId) {
		
		Optional<PolicyServiceEntitiy> findByPolicyIdAndServiceTypeAndIsActive = policyServiceRepository.findByPolicyIdAndServiceTypeAndIsActive(policyId,PolicyServiceName.CONVERSION.getName(),true);
		if (findByPolicyIdAndServiceTypeAndIsActive.isPresent()) {
			PolicyServiceEntitiy policyServiceEntitiy = findByPolicyIdAndServiceTypeAndIsActive.get();
			Optional<RenewalPolicyTMPEntity> findByPolicyServiceId = renewalPolicyTMPRepository.findByPolicyServiceId(policyServiceEntitiy.getId());
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = findByPolicyServiceId.get();
			return new CheckActiveQuatation( true, renewalPolicyTMPEntity.getQuotationNumber(), "Quotation already exists");
		}else {
			
			
			return new CheckActiveQuatation( false,"",  "Quotation not found.");
		}
		
	}

	@Override
	public CheckActiveQuatation quotationDetective(String policyNumber, Long policyId) {
		Optional<PolicyServiceEntitiy> findByPolicyIdAndServiceTypeAndIsActive = policyServiceRepository.findByPolicyIdAndServiceTypeAndIsActive(policyId,PolicyServiceName.CONVERSION.getName(),true);
		if (findByPolicyIdAndServiceTypeAndIsActive.isPresent()) {
		
		
		PolicyServiceEntitiy policyServiceEntitiy = findByPolicyIdAndServiceTypeAndIsActive.get();
		policyServiceEntitiy.setIsActive(false);		
		policyServiceRepository.save(policyServiceEntitiy);
		
		Optional<RenewalPolicyTMPEntity> findByPolicyServiceId = renewalPolicyTMPRepository.findByPolicyServiceId(policyServiceEntitiy.getId());
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = findByPolicyServiceId.get();
		Optional<PmstTmpConversionPropsEntity> findById = psPmstTmpConversionPropsRepository.findByTmpPolicyID(renewalPolicyTMPEntity.getId());
		PmstTmpConversionPropsEntity pmstTmpConversionPropsEntity = findById.get();	
		pmstTmpConversionPropsEntity.setIsActive(false);
		psPmstTmpConversionPropsRepository.save(pmstTmpConversionPropsEntity);
		return new CheckActiveQuatation(true, renewalPolicyTMPEntity.getQuotationNumber(), "Deactivate Quotation");
		}else {
			return new CheckActiveQuatation( false,"",  "Quotation not found.");
		}
	}

}
