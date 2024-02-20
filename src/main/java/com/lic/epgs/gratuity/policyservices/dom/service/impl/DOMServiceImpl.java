package com.lic.epgs.gratuity.policyservices.dom.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.accountingservice.dto.DebitCreditMemberSurrenderModel;
import com.lic.epgs.gratuity.accountingservice.dto.GlTransactionModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.JournalVoucherDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.MidLeaverContributuionPremuimAndGstDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.common.helper.CommonHelper;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.PickListItemRepository;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.mph.dto.TempMPHBankDto;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.helper.MPHHelper;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHBankRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.entity.TempMphSearchEntity;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.claim.repository.TempPolicyClaimPropsRepository;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.policy.member.helper.PolicyMemberHelper;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.PolicyTmpSearchDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
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
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.helper.RenewalValuationTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.repository.RenewalValuationTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationWithdrawalTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationBasicTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationMatrixTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationWithdrawalTMPRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyHistoryRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyTmpSearchRepository;
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
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.policyservices.aom.dto.AOMSearchDto;
import com.lic.epgs.gratuity.policyservices.common.dto.LeavingMemberDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PremiumGstRefundDto;
import com.lic.epgs.gratuity.policyservices.common.enums.PolicyServiceName;
import com.lic.epgs.gratuity.policyservices.common.service.PolicyServicingCommonService;
import com.lic.epgs.gratuity.policyservices.conversion.dto.GstDetailModel;
import com.lic.epgs.gratuity.policyservices.dom.dto.DOMSearchDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.MemberLeaverDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.MidleaverBenficiaryDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.PmstMidleaverPropsDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.RefundDto;
import com.lic.epgs.gratuity.policyservices.dom.entity.DOMSearchEntity;
import com.lic.epgs.gratuity.policyservices.dom.entity.MidleaverBenficiaryEntity;
import com.lic.epgs.gratuity.policyservices.dom.entity.MidleaverPropsSearchEntity;
import com.lic.epgs.gratuity.policyservices.dom.entity.PmstMidleaverMemberPropsEntity;
import com.lic.epgs.gratuity.policyservices.dom.entity.PmstMidleaverPropsEntity;
import com.lic.epgs.gratuity.policyservices.dom.helper.MidLeaverHelper;
import com.lic.epgs.gratuity.policyservices.dom.repository.MidleaverBenficiaryRepository;
import com.lic.epgs.gratuity.policyservices.dom.repository.MidleaverMemberPropsRepository;
import com.lic.epgs.gratuity.policyservices.dom.repository.MidleaverPropsRepository;
import com.lic.epgs.gratuity.policyservices.dom.service.DOMService;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.BeneficiaryPaymentDetailModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.GlTransactionModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.JournalVoucherDetailModel;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkStgEntity;
import com.lic.epgs.gratuity.quotation.member.helper.BulkMemberUploadHelper;

@Service
public class DOMServiceImpl implements DOMService {

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;

	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

	@Autowired
	private PolicySchemeRuleRepository policySchemeRuleRepository;

	@Autowired
	private MasterPolicyRepository masterPolicyRepository;

	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private RenewalSchemeruleTMPRepository renewalSchemeruleTMPRepository;

	@Autowired
	private PolicyLifeCoverRepository policyLifeCoverRepository;

	@Autowired
	private RenewalLifeCoverTMPRepository renewalLifeCoverTMPRepository;

	@Autowired
	private AccountingService accountingService;

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
	private CommonModuleService commonModuleService;

	@Autowired
	private RenewalGratuityBenefitTMPRepository renewalGratuityBenefitTMPRepository;

	@Autowired
	private PolicyValuationMatrixRepository policyValuationMatrixRepository;

	@Autowired
	private RenewalValuationMatrixTMPRepository renewalValuationMatrixTMPRepository;

	@Autowired
	private PolicyTmpSearchRepository policyTmpSearchRepository;

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
	private MPHRepository mPHRepository;

	@Autowired
	private TempMPHRepository tempMPHRepository;

	@Autowired
	private MemberCategoryModuleRepository memberCategoryModuleRepository;

	@Autowired
	private EntityManager entityManager;

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MidleaverMemberPropsRepository midleaverMemberPropsRepository;

	@Autowired
	private MidleaverPropsRepository midleaverPropsRepository;

	@Autowired
	private PolicyServicingCommonService policyServicingCommonService;

	@Autowired
	private TempMemberRepository tempMemberRepository;

	@Autowired
	private TempPolicyClaimPropsRepository tempPolicyClaimPropsRepository;

	@Autowired
	private PickListItemRepository pickListItemRepository;

	@Autowired
	private TempMPHBankRepository tempMPHBankRepository;

	@Autowired
	private PolicyServiceRepository policyServiceRepository;

	@Autowired
	private TaskProcessRepository taskProcessRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MidleaverBenficiaryRepository midleaverBenficiaryRepository;

	@Autowired
	PolicyHistoryRepository policyHistoryRepository;

	@Autowired
	private BulkMemberUploadHelper bulkMemberUploadHelper;

	@Autowired
	private TaskAllocationRepository taskAllocationRepository;

	@Value("${app.commonModuleServiceEndpoint}")
	private String endPoint;

	@Autowired
	private CommonMasterStateRepository commonMasterStateRepository;

	@Value("${app.accountingServiceEndpoint}")
	private String accountingServiceEndpoint;

	@Autowired
	private RestTemplate restTemplate;

	public HttpHeaders restHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return headers;
	}

	@Transactional
	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> masterPolicyForCreateServiceforIndividual(
			PmstMidleaverPropsDto pmstMidleaverPropsDto) {

		MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository
				.findById(pmstMidleaverPropsDto.getPmstPolicyId());
		PolicyMemberEntity policyMemberEntity = policyMemberRepository.findByPolicyIdandMemberId(
				pmstMidleaverPropsDto.getPmstPolicyId(), pmstMidleaverPropsDto.getPmstMemberId());

		PmstMidleaverPropsEntity getPmstMidleaverPropsEntity = mastertotemp(masterPolicyEntity, policyMemberEntity,
				pmstMidleaverPropsDto);

		PmstMidleaverPropsDto midleaverPropsDto = MidLeaverHelper.entitytodto(getPmstMidleaverPropsEntity);

		return ApiResponseDto.success(setRefundDto(midleaverPropsDto, getPmstMidleaverPropsEntity.getTmpPolicyId()));
	}

	private PmstMidleaverPropsDto setRefundDto(PmstMidleaverPropsDto midleaverPropsDto, Long tmpPolicyId) {
		midleaverPropsDto.getRefundDto().setPremiumCollected(0D);
		midleaverPropsDto.getRefundDto().setRefundableGst(0D);
		midleaverPropsDto.getRefundDto().setRefundablePremium(0D);

		List<PmstMidleaverMemberPropsEntity> pmstMidleaverMemberPropsEntities = midleaverMemberPropsRepository
				.findByTmpPolicyId(tmpPolicyId);
		for (PmstMidleaverMemberPropsEntity pmstMidleaverMemberPropsEntity : pmstMidleaverMemberPropsEntities) {
			midleaverPropsDto.getRefundDto().setPremiumCollected(midleaverPropsDto.getRefundDto().getPremiumCollected()
					+ pmstMidleaverMemberPropsEntity.getLastPremiumCollectedAmount());
			midleaverPropsDto.getRefundDto().setRefundableGst(midleaverPropsDto.getRefundDto().getRefundableGst()
					+ pmstMidleaverMemberPropsEntity.getRefundGstAmount());
			midleaverPropsDto.getRefundDto()
					.setRefundablePremium(midleaverPropsDto.getRefundDto().getRefundablePremium()
							+ pmstMidleaverMemberPropsEntity.getRefundPremiumAmount());
		}

		return midleaverPropsDto;
	}

	private RefundDto getTotalRefund(Long tmpPolicyId) {
		RefundDto refundDto = new RefundDto();

		refundDto.setPremiumCollected(0D);
		refundDto.setRefundableGst(0D);
		refundDto.setRefundablePremium(0D);
		// refundDto.getPremiumCollected() +
		// pmstMidleaverMemberPropsEntity.getLastPremiumCollectedAmount()

		List<PmstMidleaverMemberPropsEntity> pmstMidleaverMemberPropsEntities = midleaverMemberPropsRepository
				.findByTmpPolicyId(tmpPolicyId);
		for (PmstMidleaverMemberPropsEntity pmstMidleaverMemberPropsEntity : pmstMidleaverMemberPropsEntities) {
			refundDto.setPremiumCollected(CommonHelper.convertTwoDecimalValue(
					refundDto.getPremiumCollected() + pmstMidleaverMemberPropsEntity.getLastPremiumCollectedAmount()));
			refundDto.setRefundableGst(CommonHelper.convertTwoDecimalValue(
					refundDto.getRefundableGst() + pmstMidleaverMemberPropsEntity.getRefundGstAmount()));
			refundDto.setRefundablePremium(CommonHelper.convertTwoDecimalValue(
					refundDto.getRefundablePremium() + pmstMidleaverMemberPropsEntity.getRefundPremiumAmount()));
		}
		return refundDto;
	}

	private PmstMidleaverPropsEntity mastertotemp(MasterPolicyEntity masterPolicyEntity,
			PolicyMemberEntity policyMemberEntity, PmstMidleaverPropsDto pmstMidleaverPropsDto) {

		TaskProcessEntity taskProcessEntity = taskProcessRepository.findByProcessName("MID LEAVER");

		TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper.copytoTmpForClaim(masterPolicyEntity);
		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
		TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytomastertoTemp(renewalPolicyTMPEntity.getId(),
				mPHEntity);
		tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
		renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
				.findBypolicyId(pmstMidleaverPropsDto.getPmstPolicyId());
		RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
				.copyToTmpSchemeforClaim(policySchemeEntity, renewalPolicyTMPEntity.getId());
		renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

		List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
				.findBypmstPolicyId(pmstMidleaverPropsDto.getPmstPolicyId());
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
				.findByPolicyId(pmstMidleaverPropsDto.getPmstPolicyId());

		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper.copyToTmpLifeCoverforClaim(
				policyLifeCoverEntity, memberCategoryEntity, renewalPolicyTMPEntity.getId());

		renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
		policyLifeCoverRepository.updateISActive(pmstMidleaverPropsDto.getPmstPolicyId());

		List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
				.findBypolicyId(pmstMidleaverPropsDto.getPmstPolicyId());

		List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
				.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
						renewalPolicyTMPEntity.getId());

		renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
		policyGratuityBenefitRepository.updateIsActive(pmstMidleaverPropsDto.getPmstPolicyId());

		Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
				.findByPolicyId(pmstMidleaverPropsDto.getPmstPolicyId());
		RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationforClaim(policyValuationEntity, renewalPolicyTMPEntity.getId());
		renewalValuationTMPRepository.save(renewalValuationTMPEntity);

		Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
				.findByPolicyId(pmstMidleaverPropsDto.getPmstPolicyId());
		RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity, renewalPolicyTMPEntity.getId());
		renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

		Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
				.findByPolicyId(pmstMidleaverPropsDto.getPmstPolicyId());
		RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationBasicClaim(policyValutationBasicEntity, renewalPolicyTMPEntity.getId());
		renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

		List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
				.findByPolicyId(pmstMidleaverPropsDto.getPmstPolicyId());
		List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity, renewalPolicyTMPEntity.getId());
		renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);

		RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
				.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity, memberCategoryEntity,
						renewalPolicyTMPEntity.getId());
		renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);
		String variantCode = commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId());

		LeavingMemberDto leavingMemberDto = new LeavingMemberDto();
		leavingMemberDto.setMasterMemberId(pmstMidleaverPropsDto.getPmstMemberId());
		leavingMemberDto.setDateOfLeaving(pmstMidleaverPropsDto.getLeavingDate());
		PremiumGstRefundDto premiumGstRefundDto = policyServicingCommonService
				.getRefundablePremiumAndGST(masterPolicyEntity.getId(), leavingMemberDto);

		PmstMidleaverMemberPropsEntity pmstMidleaverMemberPropsEntity = new PmstMidleaverMemberPropsEntity();
		pmstMidleaverMemberPropsEntity.setLeavingDate(pmstMidleaverPropsDto.getLeavingDate());
		pmstMidleaverMemberPropsEntity.setLeavingReasonId(pmstMidleaverPropsDto.getLeavingReasonId());
		pmstMidleaverMemberPropsEntity.setLeavingReasonOther(pmstMidleaverPropsDto.getLeavingReasonOther());
		pmstMidleaverMemberPropsEntity.setTmpMemberId(renewalPolicyTMPMemberEntity.getId());

		if (variantCode.equalsIgnoreCase("V2")) {
			pmstMidleaverMemberPropsEntity.setLastPremiumCollectedDate(null);
			pmstMidleaverMemberPropsEntity.setLastPremiumCollectedAmount(0.0);
			pmstMidleaverMemberPropsEntity.setRefundPremiumAmount(0.0);
			pmstMidleaverMemberPropsEntity.setRefundGstAmount(0.0);

		} else {

			pmstMidleaverMemberPropsEntity.setLastPremiumCollectedDate(premiumGstRefundDto.getPremiumCollectedDate());
			pmstMidleaverMemberPropsEntity.setLastPremiumCollectedAmount(premiumGstRefundDto.getPremiumCollected());
			pmstMidleaverMemberPropsEntity.setRefundPremiumAmount(premiumGstRefundDto.getPremiumRefunable());
			pmstMidleaverMemberPropsEntity.setRefundGstAmount(premiumGstRefundDto.getGstRefundable());
		}
		pmstMidleaverMemberPropsEntity.setIsActive(true);
		pmstMidleaverMemberPropsEntity.setCreatedBy(pmstMidleaverPropsDto.getUserName());
		pmstMidleaverMemberPropsEntity.setCreatedDate(new Date());
		midleaverMemberPropsRepository.save(pmstMidleaverMemberPropsEntity);

		String sequence = tempPolicyClaimPropsRepository.getSequence(HttpConstants.MID_LEAVER_SERVICE_NO);
		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = new PmstMidleaverPropsEntity();
		pmstMidleaverPropsEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
		pmstMidleaverPropsEntity.setBulkOrIndividal(pmstMidleaverPropsDto.getBulkOrIndividal());
		pmstMidleaverPropsEntity.setServiceNumber(sequence);
		pmstMidleaverPropsEntity.setStatusId(514L);
		pmstMidleaverPropsEntity.setIsActive(true);
		pmstMidleaverPropsEntity.setUserName(pmstMidleaverPropsDto.getUserName());
		pmstMidleaverPropsEntity.setCreatedDate(new Date());

		Long statusId = 514L;
		taskAllocationEntity.setTaskStatus(statusId.toString());
		taskAllocationEntity.setRequestId(pmstMidleaverPropsEntity.getServiceNumber());
		taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//		taskAllocationEntity.setLocationType(quotationRenewalDto.getUnitCode());
		taskAllocationEntity.setCreatedBy(renewalPolicyTMPEntity.getCreatedBy());
		taskAllocationEntity.setCreatedDate(new Date());
		taskAllocationEntity.setModulePrimaryId(renewalPolicyTMPEntity.getId());
		taskAllocationEntity.setIsActive(true);
		taskAllocationRepository.save(taskAllocationEntity);

		return midleaverPropsRepository.save(pmstMidleaverPropsEntity);
	}

	private List<MemberBulkStgEntity> getQuotationMembers(Long batchId) {
		return jdbcTemplate.query(
				"SELECT * FROM QSTG_MEMBER_BULK_STG WHERE RECORD_STATUS='G' AND IS_ACTIVE=1 AND MEMBER_BATCH_ID=?",
				BeanPropertyRowMapper.newInstance(MemberBulkStgEntity.class), batchId);
	}

	@Override
	public RenewalPolicyTMPEntity masterToTempPolicyDetail(MasterPolicyEntity masterPolicyEntity) {

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper.copytoTmpForClaim(masterPolicyEntity);
		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
//		PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
//
//		policyServiceEntitiy.setServiceType("MID LEAVER");
//		policyServiceEntitiy.setPolicyId(masterPolicyEntity.getId());
//		policyServiceEntitiy.setCreatedBy(masterPolicyEntity.getCreatedBy());
//		policyServiceEntitiy.setCreatedDate(new Date());
//		policyServiceEntitiy.setIsActive(true);
//		policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

//		renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
		TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytomastertoTemp(renewalPolicyTMPEntity.getId(),
				mPHEntity);
		tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
		renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
				.findBypolicyId(masterPolicyEntity.getId());
		RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
				.copyToTmpSchemeforClaim(policySchemeEntity, renewalPolicyTMPEntity.getId());
		renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

		List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
				.findBypmstPolicyId(masterPolicyEntity.getId());
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
				.findByPolicyId(masterPolicyEntity.getId());

		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
				.copyToTmpLifeCoverforClaimbulk(policyLifeCoverEntity, memberCategoryEntity,
						renewalPolicyTMPEntity.getId());

		renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);

		List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
				.findBypolicyId(masterPolicyEntity.getId());

		List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
				.copyToTmpGratuityforClaimBulk(policyGratuityBenefitEntity, memberCategoryEntity,
						renewalPolicyTMPEntity.getId());

		renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
		policyGratuityBenefitRepository.updateIsActive(masterPolicyEntity.getId());

		Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
				.findByPolicyId(masterPolicyEntity.getId());
		RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationforClaim(policyValuationEntity, renewalPolicyTMPEntity.getId());
		renewalValuationTMPRepository.save(renewalValuationTMPEntity);

		Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
				.findByPolicyId(masterPolicyEntity.getId());
		RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity, renewalPolicyTMPEntity.getId());
		renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

		Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
				.findByPolicyId(masterPolicyEntity.getId());
		RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationBasicClaim(policyValutationBasicEntity, renewalPolicyTMPEntity.getId());
		renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

		List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
				.findByPolicyId(masterPolicyEntity.getId());
		List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity, renewalPolicyTMPEntity.getId());
		renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);

		return renewalPolicyTMPEntity;

	}

	@Transactional
	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> importMemberData(Long pmstPolicyId, Long batchId, String userName) {

		TaskProcessEntity taskProcessEntity = taskProcessRepository.findByProcessName("MID LEAVER");

		TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();

		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findById(pmstPolicyId).get();

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = masterToTempPolicyDetail(masterPolicyEntity); // need to copy
																										// master to
																										// policy level
																										// table
		// return type is RenewalPolicyTMPEntity
		List<MemberBulkStgEntity> temps = getQuotationMembers(batchId);

//PMSTMEMBER BULK DETAIL - 
		for (MemberBulkStgEntity temp : temps) {

			PolicyMemberEntity policyMemberEntity = policyMemberRepository.findByPolicyIdandMemberId(pmstPolicyId,
					temp.getPmstMemberId());

			List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
					.findBypmstPolicyId(masterPolicyEntity.getId());

			RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
					.copyToTmpIndividualMemberClaimBulk(policyMemberEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);

			String variantCode = commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId());

			LeavingMemberDto leavingMemberDto = new LeavingMemberDto();
			leavingMemberDto.setMasterMemberId(temp.getPmstMemberId());
			leavingMemberDto.setDateOfLeaving(temp.getDateOfExit()); // getLeavingDate();
			PremiumGstRefundDto premiumGstRefundDto = policyServicingCommonService
					.getRefundablePremiumAndGST(masterPolicyEntity.getId(), leavingMemberDto);

			PmstMidleaverMemberPropsEntity pmstMidleaverMemberPropsEntity = new PmstMidleaverMemberPropsEntity();
			pmstMidleaverMemberPropsEntity.setLeavingDate(temp.getDateOfExit()); // getLeavingDate
			pmstMidleaverMemberPropsEntity.setLeavingReasonId(temp.getReasonForDeathId()); // getLeavingReasonId
			pmstMidleaverMemberPropsEntity.setLeavingReasonOther(temp.getReasonForDeathOther()); // getLeavingReasonOther
			pmstMidleaverMemberPropsEntity.setTmpMemberId(renewalPolicyTMPMemberEntity.getId());

			if (variantCode.equalsIgnoreCase("V2")) {

				System.out.println("================================>V2 Policy" + variantCode);
				pmstMidleaverMemberPropsEntity.setLastPremiumCollectedDate(null);
				pmstMidleaverMemberPropsEntity.setLastPremiumCollectedAmount(0.0);
				pmstMidleaverMemberPropsEntity.setRefundPremiumAmount(0.0);
				pmstMidleaverMemberPropsEntity.setRefundGstAmount(0.0);

			} else {

				pmstMidleaverMemberPropsEntity
						.setLastPremiumCollectedDate(premiumGstRefundDto.getPremiumCollectedDate());
				pmstMidleaverMemberPropsEntity.setLastPremiumCollectedAmount(premiumGstRefundDto.getPremiumCollected());
				pmstMidleaverMemberPropsEntity.setRefundPremiumAmount(premiumGstRefundDto.getPremiumRefunable());
				pmstMidleaverMemberPropsEntity.setRefundGstAmount(premiumGstRefundDto.getGstRefundable());
			}
			pmstMidleaverMemberPropsEntity.setIsActive(true);
			pmstMidleaverMemberPropsEntity.setCreatedBy(userName); // temp.getUserName()
			pmstMidleaverMemberPropsEntity.setCreatedDate(new Date());
			midleaverMemberPropsRepository.save(pmstMidleaverMemberPropsEntity);

		}

		String sequence = tempPolicyClaimPropsRepository.getSequence(HttpConstants.MID_LEAVER_SERVICE_NO);
		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = new PmstMidleaverPropsEntity();
		pmstMidleaverPropsEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
		pmstMidleaverPropsEntity.setBulkOrIndividal("B"); // pmstMidleaverPropsDto.getBulkOrIndividal()
		pmstMidleaverPropsEntity.setServiceNumber(sequence);
		pmstMidleaverPropsEntity.setStatusId(514L);
		pmstMidleaverPropsEntity.setIsActive(true);
		pmstMidleaverPropsEntity.setUserName(userName); // pmstMidleaverPropsDto.getUserName()
		pmstMidleaverPropsEntity.setCreatedDate(new Date());
		midleaverPropsRepository.save(pmstMidleaverPropsEntity);

		PmstMidleaverPropsDto midleaverPropsDto = MidLeaverHelper.entitytodto(pmstMidleaverPropsEntity);

		Long statusId = 514L;
		taskAllocationEntity.setTaskStatus(statusId.toString());
		taskAllocationEntity.setRequestId(pmstMidleaverPropsEntity.getServiceNumber());
		taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//		taskAllocationEntity.setLocationType(quotationRenewalDto.getUnitCode());
		taskAllocationEntity.setCreatedBy(renewalPolicyTMPEntity.getCreatedBy());
		taskAllocationEntity.setCreatedDate(new Date());
		taskAllocationEntity.setModulePrimaryId(renewalPolicyTMPEntity.getId());
		taskAllocationEntity.setIsActive(true);
		taskAllocationRepository.save(taskAllocationEntity);

		return ApiResponseDto.success(midleaverPropsDto);
	}

	@Override
	public ApiResponseDto<List<PolicyTmpSearchDto>> searchByPolicyNumber(String type, String policyNumber) {
		// type will be INPROGRESS (514 & 516), EXISTING (517 & 518), PAYOUT-NEW (518),
		// PAYOUT-INPROGRESS (520), PAYOUT-EXISTING (521 & 522)
		// return PolicyTmpSearchDto [PolicyTmpSearchEntity] ===> pick_list_id=89

		List<Long> statusId = new ArrayList<>();

		List<PolicyTmpSearchEntity> policyTmpEntites = null;

		if (type.equals("INPROGRESS")) {
			statusId.add(514L);// draft
			statusId.add(515L);// Sent for Approve
			statusId.add(516L);// send back to maker

			policyTmpEntites = policyTmpSearchRepository.getDOMbyPolicyNumber(statusId, policyNumber);
		} else if (type.equals("EXISTING")) {
			statusId.add(517L);// Rejected
			statusId.add(518L);// Approved
			statusId.add(523L);// InPayout
			statusId.add(524L);// NEFTRejected
			statusId.add(525L);// PaidOut

			policyTmpEntites = policyTmpSearchRepository.getDOMbyPolicyNumber(statusId, policyNumber);
		} else if (type.equals("PAYOUT-NEW")) {
			statusId.add(518L);
			statusId.add(519L);
			policyTmpEntites = policyTmpSearchRepository.getDOMbyPolicyNumber1(statusId, policyNumber);
		} else if (type.equals("PAYOUT-INPROGRESS")) {
			statusId.add(520L);// Pay out – Sent back to Maker
			statusId.add(519L);// Pay out – Sent for Aprpoval

			policyTmpEntites = policyTmpSearchRepository.getDOMbyPolicyNumber1(statusId, policyNumber);
		} else if (type.equals("PAYOUT-EXISTING")) {
			statusId.add(521L);// Pay out - Approved
			statusId.add(522L);// Pay out-Rejected

			policyTmpEntites = policyTmpSearchRepository.getDOMbyPolicyNumber1(statusId, policyNumber);
		}

		String unitCode = null;
		List<PolicyTmpSearchEntity> entites = new ArrayList<>();

		for (PolicyTmpSearchEntity policyTmpEntity : policyTmpEntites) {
			unitCode = policyTmpEntity.getUnitCode();
			String unitOf = this.getUnitOf(unitCode);
			JsonNode productCodeJsonNode = commonModuleService.getProductCodeJsonNode(policyTmpEntity.getProductId());
			String textValue = productCodeJsonNode.path("productName").textValue();
			String variantCode = commonModuleService.getVariantCode(policyTmpEntity.getProductVariantId());
			policyTmpEntity.setProductName(textValue);
			policyTmpEntity.setProductVariant(variantCode);
			policyTmpEntity.setUnitOffice(unitOf);
			entites.add(policyTmpEntity);
		}

		List<PolicyTmpSearchDto> collect = entites.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList());

		for (PolicyTmpSearchDto dto : collect) {
			PmstMidleaverPropsEntity propsEntity = midleaverPropsRepository.findByTmpPolicyId(dto.getTmpPolicyId());

			dto.setServiceStatusId(propsEntity.getStatusId());
		}
		return ApiResponseDto.success(collect);
	}

	private String getUnitOf(String code) {

		CommonMasterUnitEntity commonMasterUnitEntity = commonMasterUnitRepository.findByCode(code);
		String description = commonMasterUnitEntity.getDescription();

		return description;

	}

	@Override
	public ApiResponseDto<List<AOMSearchDto>> otherSearchByPolicyNumber(String type, DOMSearchDto domSearchDto) {
		Long policyStatusDraft = 514L;
		Long sentForApprove = 515L;
		Long sendBacktoMaker = 516L;
		Long rejected = 517L;
		Long approved = 518L;
		Long payOutSentForApproval = 519L;
		Long payoutApproved = 521L;
		Long payoutRjected = 522L;
		Long payoutSentBacktoMaker = 520L;
		Long inPayOut = 523L;
		Long NEFTReject = 524L;

		List<Predicate> predicates = new ArrayList<>();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<DOMSearchEntity> createQuery = criteriaBuilder.createQuery(DOMSearchEntity.class);

		Root<DOMSearchEntity> root = createQuery.from(DOMSearchEntity.class);

		Join<DOMSearchEntity, TempMphSearchEntity> join = root.join("policyMPHTmp");
		Join<DOMSearchEntity, MidleaverPropsSearchEntity> join2 = root.join("propsSearchEntity");

		if (StringUtils.isNotBlank(domSearchDto.getCustomerName())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")),
					"%" + domSearchDto.getCustomerName().toLowerCase() + "%"));
		}

		if (domSearchDto.getPan() != null) {
			predicates.add(criteriaBuilder.equal(join.get("pan"), domSearchDto.getPan()));
		}

		if (type.equals("INPROGRESS")) {

			predicates.add(join2.get("statusId").in(policyStatusDraft, sendBacktoMaker, sentForApprove));

		} else if (type.equals("EXISTING")) {

			predicates.add(join2.get("statusId").in(rejected, approved, inPayOut, NEFTReject));

		} else if (type.equals("PAYOUT-NEW")) {

			predicates.add(join2.get("statusId").in(approved));

		} else if (type.equals("PAYOUT-INPROGRESS")) {

			predicates.add(join2.get("statusId").in(payoutSentBacktoMaker, payOutSentForApproval));

		} else if (type.equals("PAYOUT-EXISTING")) {
			predicates.add(join2.get("statusId").in(payoutApproved, payoutRjected));

		}
		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<DOMSearchEntity> entites = entityManager.createQuery(createQuery).getResultList();

		String unitCode = null;
		List<DOMSearchEntity> policyEnities = new ArrayList<>();

		for (DOMSearchEntity policyDetail : entites) {
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
			PmstMidleaverPropsEntity propsEntity = midleaverPropsRepository.findByTmpPolicyId(dto.getId());

			dto.setTmpPolicyId(dto.getId());
			dto.setServiceStatusId(propsEntity.getStatusId());
		}
		return ApiResponseDto.success(collect);
	}

	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> getOverView(Long tmpPolicyId) {

		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		PmstMidleaverPropsDto pmstMidleaverPropsDto = modelMapper.map(pmstMidleaverPropsEntity,
				PmstMidleaverPropsDto.class);
		return ApiResponseDto.success(setRefundDto(pmstMidleaverPropsDto, tmpPolicyId));
	}

	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> saveRefund(Long tmpPolicyId, Boolean isPremiumRefund,
			String userName) {
		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		pmstMidleaverPropsEntity.setIsPremiumRefund(isPremiumRefund);
		pmstMidleaverPropsEntity.setModifiedBy(userName);
		pmstMidleaverPropsEntity.setModifiedDate(new Date());
		pmstMidleaverPropsEntity = midleaverPropsRepository.save(pmstMidleaverPropsEntity);

		PmstMidleaverPropsDto pmstMidleaverPropsDto = modelMapper.map(pmstMidleaverPropsEntity,
				PmstMidleaverPropsDto.class);

		return ApiResponseDto.success(setRefundDto(pmstMidleaverPropsDto, tmpPolicyId));
	}

	@Override
	public ApiResponseDto<List<MemberLeaverDto>> getMemberList(Long tmpPolicyId) {
		List<MemberLeaverDto> memberLeaversDto = new ArrayList<>();

		List<TempMemberEntity> tempMemberEntities = tempMemberRepository.findByTmpPolicyId(tmpPolicyId);
		for (TempMemberEntity tempMemberEntity : tempMemberEntities) {
			MemberLeaverDto memberLeaverDto = PolicyMemberHelper.tmpMemberLeaver(tempMemberEntity);
			PmstMidleaverMemberPropsEntity pmstMidleaverMemberPropsEntity = midleaverMemberPropsRepository
					.findByTmpMemberId(tempMemberEntity.getId());
			memberLeaverDto = PolicyMemberHelper.addMemberLeaverProps(memberLeaverDto, pmstMidleaverMemberPropsEntity);
			memberLeaverDto.setStatus(pickListItemRepository.findById(memberLeaverDto.getStatusId()).get().getName());
			memberLeaverDto.setDateOfJoining(tempMemberEntity.getDojToScheme());

			List<PmstMidleaverMemberPropsEntity> memberPropsEntities = midleaverMemberPropsRepository
					.findByTmpPolicyId(tmpPolicyId);
			for (PmstMidleaverMemberPropsEntity entity : memberPropsEntities) {
				memberLeaverDto.setRefundGstAmount(entity.getRefundGstAmount());
				memberLeaverDto.setRefundPremiumAmount(entity.getRefundPremiumAmount());
				memberLeaverDto.setLastPremiumCollectedAmount(entity.getLastPremiumCollectedAmount());

			}

			memberLeaversDto.add(memberLeaverDto);
		}

		return ApiResponseDto.success(memberLeaversDto);
	}

	@Transactional
	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> midleaverSubmitForApproval(Long tmpPolicyId, String userName) {

		// submit for approval statusid

		Long senForApproval = 515L;

		Long masterPolicyId = null;

		Optional<RenewalPolicyTMPEntity> findById = renewalPolicyTMPRepository.findById(tmpPolicyId);
		{
			masterPolicyId = findById.get().getMasterPolicyId();
		}
//			save service
		PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
		PolicyServiceEntitiy policyserviceentityExisiting = policyServiceRepository
				.findByPolicyandTypeandActive(masterPolicyId, PolicyServiceName.MID_LEAVER.getName());

		if (policyserviceentityExisiting != null) {

			policyServiceEntitiy.setId(policyserviceentityExisiting.getId());
			policyServiceEntitiy.setServiceType(PolicyServiceName.MID_LEAVER.getName());
			policyServiceEntitiy.setPolicyId(masterPolicyId);
			policyServiceEntitiy.setModifiedBy(userName);
			policyServiceEntitiy.setModifiedDate(new Date());
			policyServiceEntitiy.setIsActive(true);
		} else {
			policyServiceEntitiy.setServiceType(PolicyServiceName.MID_LEAVER.getName());
			policyServiceEntitiy.setPolicyId(masterPolicyId);
			policyServiceEntitiy.setCreatedBy(userName);
			policyServiceEntitiy.setCreatedDate(new Date());
			policyServiceEntitiy.setIsActive(true);
		}
		policyserviceentityExisiting = policyServiceRepository.save(policyServiceEntitiy);

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = findById.get();

		renewalPolicyTMPEntity.setPolicyServiceId(policyserviceentityExisiting.getId());
		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);

		pmstMidleaverPropsEntity.setStatusId(senForApproval);
		pmstMidleaverPropsEntity.setModifiedBy(userName);
		pmstMidleaverPropsEntity.setModifiedDate(new Date());
		pmstMidleaverPropsEntity = midleaverPropsRepository.save(pmstMidleaverPropsEntity);

		PmstMidleaverPropsDto pmstMidleaverPropsDto = modelMapper.map(pmstMidleaverPropsEntity,
				PmstMidleaverPropsDto.class);

		TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
				.findByRequestId(pmstMidleaverPropsEntity.getServiceNumber());
		taskAllocationEntity.setTaskStatus(senForApproval.toString());
		taskAllocationRepository.save(taskAllocationEntity);

		return ApiResponseDto.success(pmstMidleaverPropsDto);
	}

	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> midleaverSendBackToMaker(Long tmpPolicyId, String userName) {

		Long sendBackMaker = 516L;

		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		pmstMidleaverPropsEntity.setStatusId(sendBackMaker);
		pmstMidleaverPropsEntity.setModifiedBy(userName);
		pmstMidleaverPropsEntity.setModifiedDate(new Date());
		pmstMidleaverPropsEntity = midleaverPropsRepository.save(pmstMidleaverPropsEntity);
		PmstMidleaverPropsDto pmstMidleaverPropsDto = modelMapper.map(pmstMidleaverPropsEntity,
				PmstMidleaverPropsDto.class);

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyId).get();

		TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
				.findByRequestId(pmstMidleaverPropsEntity.getServiceNumber());
		taskAllocationEntity.setTaskStatus(sendBackMaker.toString());
		taskAllocationRepository.save(taskAllocationEntity);

		return ApiResponseDto.success(pmstMidleaverPropsDto);

	}

	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> midleaverReject(Long tmpPolicyId, String userName) {

		Long rejected = 517L;
		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		pmstMidleaverPropsEntity.setStatusId(rejected);
		pmstMidleaverPropsEntity.setModifiedBy(userName);
		pmstMidleaverPropsEntity.setModifiedDate(new Date());
		pmstMidleaverPropsEntity = midleaverPropsRepository.save(pmstMidleaverPropsEntity);
		PmstMidleaverPropsDto pmstMidleaverPropsDto = modelMapper.map(pmstMidleaverPropsEntity,
				PmstMidleaverPropsDto.class);

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyId).get();

		TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
				.findByRequestId(pmstMidleaverPropsEntity.getServiceNumber());
		taskAllocationEntity.setTaskStatus(rejected.toString());
		taskAllocationRepository.save(taskAllocationEntity);

		return ApiResponseDto.success(pmstMidleaverPropsDto);

	}

	@Transactional
	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> midleaverApprove(Long tmpPolicyId, String userName) {

		Map<String, Object> response = new HashMap<String, Object>();
		Long inPayout = 523L;
		Long approved = 518L;
		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);

		if (pmstMidleaverPropsEntity.getIsPremiumRefund() == true) {
			pmstMidleaverPropsEntity.setStatusId(inPayout);
		} else {
			pmstMidleaverPropsEntity.setStatusId(approved);

		}
		pmstMidleaverPropsEntity.setModifiedBy(userName);
		pmstMidleaverPropsEntity.setIsActive(false);
		pmstMidleaverPropsEntity.setModifiedDate(new Date());
		pmstMidleaverPropsEntity = midleaverPropsRepository.save(pmstMidleaverPropsEntity);
		PmstMidleaverPropsDto pmstMidleaverPropsDto = modelMapper.map(pmstMidleaverPropsEntity,
				PmstMidleaverPropsDto.class);

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyId).get();

		Long tmpPolicyIdCount = midleaverMemberPropsRepository.findByTmpPolicyIdCount(tmpPolicyId);

		/** PMST_POLICY **/
		MasterPolicyEntity newmasterPolicyEntity = masterPolicyRepository
				.findById(renewalPolicyTMPEntity.getMasterPolicyId()).get();
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

			Set<MPHBankEntity> mphBank = getMPHEntity.getMphBank();

			String bankName = "";
			String ifscCode = "";
			String bankBranch = "";
			String accountType = "";
			String accountNo = "";

			for (MPHBankEntity bank : mphBank) {
				bankName = bank.getBankName();
				ifscCode = bank.getIfscCode();
				bankBranch = bank.getBankBranch();
				accountType = bank.getAccountType();
				accountNo = bank.getAccountNumber();
			}

			RefundDto totalRefund = getTotalRefund(renewalPolicyTMPEntity.getId());
			HSNCodeDto hSNCodeDto = accountingService.getHSNCode();
			Map<String, Double> gstComponents = accountingService.getGstComponents(unitStateType, mPHStateType,
					hSNCodeDto, totalRefund.getRefundablePremium());
			String toGSTIn = getMPHEntity.getGstIn() == null ? "" : getMPHEntity.getGstIn();
			ResponseDto responseDto = accountingService
					.commonmasterserviceAllUnitCode(newmasterPolicyEntity.getUnitCode());

			MidLeaverContributuionPremuimAndGstDto midLeaverContributuionPremuimAndGstDto = new MidLeaverContributuionPremuimAndGstDto();

			BeneficiaryPaymentDetailModel beneficiaryPaymentDetailModel = new BeneficiaryPaymentDetailModel();

			beneficiaryPaymentDetailModel.setPayoutSourceModule("Gratuity Tr");
			beneficiaryPaymentDetailModel.setPaymentSourceRefNumber(pmstMidleaverPropsEntity.getServiceNumber());
			beneficiaryPaymentDetailModel.setBeneficiaryName(getMPHEntity.getMphName());// Need to check
			beneficiaryPaymentDetailModel.setBeneficiaryBankName(bankName);
			beneficiaryPaymentDetailModel.setBeneficiaryBankIfsc(ifscCode);
			beneficiaryPaymentDetailModel.setBeneficiaryBranchName(bankBranch);
			beneficiaryPaymentDetailModel.setBeneficiaryAccountNumber(accountNo);
			beneficiaryPaymentDetailModel.setBeneficiaryAccountType(accountType);
			beneficiaryPaymentDetailModel
					.setPaymentAmount(totalRefund.getRefundableGst() + totalRefund.getRefundablePremium());
			beneficiaryPaymentDetailModel.setEffectiveDateOfPayment(new Date());
			beneficiaryPaymentDetailModel.setPaymentMode("N"); // N-NEFT/ R-RTGS
			beneficiaryPaymentDetailModel.setUnitCode(newmasterPolicyEntity.getUnitCode());
			beneficiaryPaymentDetailModel.setPolicyNumber(renewalPolicyTMPEntity.getPolicyNumber());
			beneficiaryPaymentDetailModel.setMemberNumber(tmpPolicyIdCount.toString());
			beneficiaryPaymentDetailModel.setPaymentCategory("PCM002"); // Need to check hardcoded
			beneficiaryPaymentDetailModel.setPaymentSubCategory("O"); // 0-> OTHERS / DC- Death Claim etc
			beneficiaryPaymentDetailModel.setRemarks("Gratuity Member Transfer Deposit Adjustment");
			beneficiaryPaymentDetailModel.setBeneficiaryPaymentId(pmstMidleaverPropsEntity.getServiceNumber());
			beneficiaryPaymentDetailModel.setProductCode(prodAndVarientCodeSame);
			beneficiaryPaymentDetailModel.setVariantCode(prodAndVarientCodeSame);
			beneficiaryPaymentDetailModel.setOperatinUnitType("UO");

			midLeaverContributuionPremuimAndGstDto.setBeneficiaryPaymentDetailModel(beneficiaryPaymentDetailModel);

			DebitCreditMemberSurrenderModel debitCreditMemberSurrenderModel = new DebitCreditMemberSurrenderModel();

			// needs to be clarified

			debitCreditMemberSurrenderModel.setDebitReFundOfOtherFirstYearPremiumAmount(0.0);

			debitCreditMemberSurrenderModel.setDebitReFundOfRenewalPremiumAmount(0.0);
			if (isRenewalStatus == true) {

				debitCreditMemberSurrenderModel
						.setDebitReFundOfRenewalPremiumAmount(totalRefund.getRefundablePremium());
			} else {
				debitCreditMemberSurrenderModel.setDebitReFundOfFirstPremiumAmount(totalRefund.getRefundablePremium());

			}
			debitCreditMemberSurrenderModel.setDebitGstLiabilityAmount(totalRefund.getRefundableGst());
			debitCreditMemberSurrenderModel
					.setCreditOsPaymentAmount(totalRefund.getRefundableGst() + totalRefund.getRefundablePremium());
			debitCreditMemberSurrenderModel.setCreditCoPaymentContraAmount(
					totalRefund.getRefundableGst() + totalRefund.getRefundablePremium());
			debitCreditMemberSurrenderModel
					.setDebitCoPaymentContraAmount(totalRefund.getRefundableGst() + totalRefund.getRefundablePremium());

			midLeaverContributuionPremuimAndGstDto.setDebitCreditMemberSurrenderModel(debitCreditMemberSurrenderModel);

			GlTransactionModelDto glTransactionModel = accountingService.getGlTransactionModel(
					newmasterPolicyEntity.getProductId(), newmasterPolicyEntity.getProductVariantId(),
					newmasterPolicyEntity.getUnitCode(), "Mid Leaver Approval");
			midLeaverContributuionPremuimAndGstDto.setGlTransactionModel(glTransactionModel);

			GstDetailModel gstDetailModelDto = new GstDetailModel();

			gstDetailModelDto.setAmountWithTax(totalRefund.getRefundableGst() + totalRefund.getRefundablePremium());
			gstDetailModelDto.setAmountWithoutTax(totalRefund.getRefundablePremium());
			gstDetailModelDto.setCessAmount(0.0); // from Docu

			gstDetailModelDto.setCgstAmount(gstComponents.get("CGST"));
			gstDetailModelDto.setCgstRate(hSNCodeDto.getCgstRate());
			gstDetailModelDto.setCreatedBy(userName);
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
			gstDetailModelDto.setGstRefNo(renewalPolicyTMPEntity.getPolicyNumber());// From Docu
			gstDetailModelDto.setGstRefTransactionNo(renewalPolicyTMPEntity.getPolicyNumber());// From Docu
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
			gstDetailModelDto.setRemarks("Gratuity Mid Leaver");
			gstDetailModelDto.setSgstAmount(gstComponents.get("SGST"));
			gstDetailModelDto.setSgstRate(hSNCodeDto.getSgstRate());
			gstDetailModelDto.setToGstIn(getMPHEntity.getGstIn() == null ? "" : getMPHEntity.getGstIn());
			gstDetailModelDto.setToPan(getMPHEntity.getPan() == null ? "" : getMPHEntity.getPan());
			gstDetailModelDto.setToStateCode(mphStateCode == null ? "" : mphStateCode);
			gstDetailModelDto.setTotalGstAmount(totalRefund.getRefundableGst());
			gstDetailModelDto.setTransactionDate(new Date());
			gstDetailModelDto.setTransactionSubType("A"); // From Docu
			gstDetailModelDto.setTransactionType("C"); // From Docu
			gstDetailModelDto.setUtgstAmount(gstComponents.get("UTGST"));
			gstDetailModelDto.setUtgstRate(hSNCodeDto.getUtgstRate());
			gstDetailModelDto.setVariantCode(prodAndVarientCodeSame);
			gstDetailModelDto.setYear(DateUtils.uniqueNoYYYY());
			gstDetailModelDto.setMonth(DateUtils.GSTInvoiceMonthCode());

			midLeaverContributuionPremuimAndGstDto.setGstDetailModel(gstDetailModelDto);

			JournalVoucherDetailModel journalVoucherDetailModel = new JournalVoucherDetailModel();
			journalVoucherDetailModel.setLineOfBusiness(newmasterPolicyEntity.getLineOfBusiness());
			journalVoucherDetailModel.setProduct(prodAndVarientCodeSame);
			journalVoucherDetailModel.setProductVariant(prodAndVarientCodeSame);

			midLeaverContributuionPremuimAndGstDto.setJournalVoucherDetailModel(journalVoucherDetailModel);
			midLeaverContributuionPremuimAndGstDto.setRefNo(pmstMidleaverPropsEntity.getServiceNumber());
			midLeaverContributuionPremuimAndGstDto.setMphCode(getMPHEntity.getMphCode());
			midLeaverContributuionPremuimAndGstDto.setUnitCode(newmasterPolicyEntity.getUnitCode());
			midLeaverContributuionPremuimAndGstDto.setUserCode(userName);
			midLeaverContributuionPremuimAndGstDto.setRefNo(pmstMidleaverPropsEntity.getId().toString());
			midLeaverContributuionPremuimAndGstDto.setAdjustmentNo(pmstMidleaverPropsEntity.getId().toString());
			midLeaverContributuionPremuimAndGstDto.setIsLegacy("N");
			midLeaverContributuionPremuimAndGstDto.setPolicyNo(renewalPolicyTMPEntity.getPolicyNumber());
			midLeaverContributuionPremuimAndGstDto.setProductCode(prodAndVarientCodeSame);
			midLeaverContributuionPremuimAndGstDto.setVariantCode(prodAndVarientCodeSame);

			logger.info("PayLoad Sending to Accouniting APi::" + midLeaverContributuionPremuimAndGstDto);
			accountingService.domAccounting(midLeaverContributuionPremuimAndGstDto);

			List<TempMemberEntity> findByTmpPolicyId = tempMemberRepository.findByTmpPolicyId(tmpPolicyId);

			List<TempMemberEntity> listTmpMem = new ArrayList<>();

			for (TempMemberEntity member : findByTmpPolicyId) {
				member.setIsActive(false);
				member.setStatusId(490L);
				listTmpMem.add(member);
			}
			tempMemberRepository.saveAll(listTmpMem);

			List<PmstMidleaverMemberPropsEntity> midleaverMemberPropsEntities = midleaverMemberPropsRepository

					.findByTmpPolicyId(tmpPolicyId);

			List<PmstMidleaverMemberPropsEntity> entites = new ArrayList<>();

			for (PmstMidleaverMemberPropsEntity midleaverMemberPropsEntity : midleaverMemberPropsEntities) {

				midleaverMemberPropsEntity.setIsActive(false);

				entites.add(midleaverMemberPropsEntity);
			}
			midleaverMemberPropsRepository.saveAll(entites);

			List<PolicyMemberEntity> list = new ArrayList<>();
			List<RenewalPolicyTMPMemberEntity> findByTmpPolicyId2 = renewalPolicyTMPMemberRepository
					.findByTmpPolicyId(tmpPolicyId);
			for (RenewalPolicyTMPMemberEntity tmpMem : findByTmpPolicyId2) {
				PolicyMemberEntity policyMemberEntity = modelMapper.map(tmpMem, PolicyMemberEntity.class);

				policyMemberEntity.setId(tmpMem.getPmstMemebrId());
				policyMemberEntity.setStatusId(tmpMem.getStatusId());
				policyMemberEntity.setIsActive(tmpMem.getIsActive());
				policyMemberEntity.setModifiedBy(userName);
				policyMemberEntity.setModifiedDate(new Date());
				list.add(policyMemberEntity);
			}
			policyMemberRepository.saveAll(list);

			TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
					.findByRequestId(pmstMidleaverPropsEntity.getServiceNumber());
			taskAllocationEntity.setTaskStatus(pmstMidleaverPropsEntity.getStatusId().toString());
			taskAllocationRepository.save(taskAllocationEntity);

		}
		return ApiResponseDto.success(pmstMidleaverPropsDto);
	}

	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> payoutSendForApproval(Long tmpPolicyId, String userName) {

		Long payoutSentForApproval = 519L;
		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		pmstMidleaverPropsEntity.setStatusId(payoutSentForApproval);
		pmstMidleaverPropsEntity.setModifiedBy(userName);
		pmstMidleaverPropsEntity.setModifiedDate(new Date());
		pmstMidleaverPropsEntity = midleaverPropsRepository.save(pmstMidleaverPropsEntity);
		PmstMidleaverPropsDto pmstMidleaverPropsDto = modelMapper.map(pmstMidleaverPropsEntity,
				PmstMidleaverPropsDto.class);

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyId).get();
		TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
				.findByRequestId(pmstMidleaverPropsEntity.getServiceNumber());
		taskAllocationEntity.setTaskStatus(payoutSentForApproval.toString());
		taskAllocationRepository.save(taskAllocationEntity);

		return ApiResponseDto.success(pmstMidleaverPropsDto);
	}

	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> payoutSendBackToMaker(Long tmpPolicyId, String userName) {
		Long payoutSentToMaker = 520L;
		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		pmstMidleaverPropsEntity.setStatusId(payoutSentToMaker);
		pmstMidleaverPropsEntity.setModifiedBy(userName);
		pmstMidleaverPropsEntity.setModifiedDate(new Date());
		pmstMidleaverPropsEntity = midleaverPropsRepository.save(pmstMidleaverPropsEntity);
		PmstMidleaverPropsDto pmstMidleaverPropsDto = modelMapper.map(pmstMidleaverPropsEntity,
				PmstMidleaverPropsDto.class);

		TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
				.findByRequestId(pmstMidleaverPropsEntity.getServiceNumber());
		taskAllocationEntity.setTaskStatus(payoutSentToMaker.toString());
		taskAllocationRepository.save(taskAllocationEntity);

		return ApiResponseDto.success(pmstMidleaverPropsDto);
	}

	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> payoutReject(Long tmpPolicyId, String userName) {
		Long payoutSentReject = 521L;
		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		pmstMidleaverPropsEntity.setStatusId(payoutSentReject);
		pmstMidleaverPropsEntity.setModifiedBy(userName);
		pmstMidleaverPropsEntity.setModifiedDate(new Date());
		pmstMidleaverPropsEntity = midleaverPropsRepository.save(pmstMidleaverPropsEntity);
		PmstMidleaverPropsDto pmstMidleaverPropsDto = modelMapper.map(pmstMidleaverPropsEntity,
				PmstMidleaverPropsDto.class);

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyId).get();

		TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
				.findByRequestId(pmstMidleaverPropsEntity.getServiceNumber());
		taskAllocationEntity.setTaskStatus(payoutSentReject.toString());
		taskAllocationRepository.save(taskAllocationEntity);

		return ApiResponseDto.success(pmstMidleaverPropsDto);

	}

	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> payoutApprove(Long tmpPolicyId, String userName) {
		Long payoutSentApprove = 522L;
		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		pmstMidleaverPropsEntity.setStatusId(payoutSentApprove);
		pmstMidleaverPropsEntity.setModifiedBy(userName);
		pmstMidleaverPropsEntity.setModifiedDate(new Date());
		pmstMidleaverPropsEntity = midleaverPropsRepository.save(pmstMidleaverPropsEntity);
		PmstMidleaverPropsDto pmstMidleaverPropsDto = modelMapper.map(pmstMidleaverPropsEntity,
				PmstMidleaverPropsDto.class);

		TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
				.findByRequestId(pmstMidleaverPropsEntity.getServiceNumber());
		taskAllocationEntity.setTaskStatus(payoutSentApprove.toString());
		taskAllocationRepository.save(taskAllocationEntity);

		return ApiResponseDto.success(pmstMidleaverPropsDto);
	}

	@Transactional
	@Override
	public ApiResponseDto<List<TempMPHBankDto>> getBeneficiaries(Long tmpPolicyId, String userName) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyId).get();

		if (renewalPolicyTMPEntity.getProposalNumber() == null
				|| renewalPolicyTMPEntity.getProposalNumber().length() == 0) {
			List<TempMPHBankEntity> tempMPHBankEntities = tempMPHBankRepository
					.findByMphId(renewalPolicyTMPEntity.getMasterpolicyHolder());
			return ApiResponseDto.success(
					tempMPHBankEntities.stream().map(MPHHelper::tempBankEntityToDto).collect(Collectors.toList()));
		} else {

			List<TempMPHBankEntity> checkAccountNoAndBankNameAndMphId = null;
			List<TempMPHBankDto> bankDetails = getBankDetails(renewalPolicyTMPEntity.getProposalNumber());

			for (TempMPHBankDto detail : bankDetails) {
				Long accountNumber = detail.getAccountNumber();
				String bankName = detail.getBankName();
				checkAccountNoAndBankNameAndMphId = tempMPHBankRepository.checkAccountNoAndBankNameAndMphId(bankName,
						accountNumber, renewalPolicyTMPEntity.getMasterpolicyHolder());

			}

			if (checkAccountNoAndBankNameAndMphId.size() == 0) {
				TempMPHEntity tempMPHEntity = tempMPHRepository.findById(renewalPolicyTMPEntity.getMasterpolicyHolder())
						.get();

				List<TempMPHBankEntity> collect = bankDetails.stream().map(MPHHelper::tempBankDtoToEntity)
						.collect(Collectors.toList());

				for (TempMPHBankEntity c : collect) {
					c.setMasterMph(tempMPHEntity);
					c.setIsActive(true);
					c.setCreatedBy(userName);
					c.setCreatedDate(new Date());
				}

				tempMPHBankRepository.saveAll(collect);
				tempMPHBankRepository.flush();

			}

			List<TempMPHBankEntity> tempMPHBankEntities = tempMPHBankRepository
					.findByMphId1(renewalPolicyTMPEntity.getMasterpolicyHolder());
			return ApiResponseDto.success(
					tempMPHBankEntities.stream().map(MPHHelper::tempBankEntityToDto).collect(Collectors.toList()));
		}
	}

	public List<TempMPHBankDto> getBankDetails(String proposalNumber) {

		JsonNode mphBankDetails = null;

		List<TempMPHBankDto> tempMPHBankDtoList = new ArrayList<TempMPHBankDto>();

		try {
			URL url = new URL(
					endPoint + "/api/proposal/getProposalDetailsByProposalNumber?proposalNumber=" + proposalNumber);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("FAILED : HTTP ERROR CODE" + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String outPut = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(outPut);
			JsonNode actualObj = mapper.readTree(parser);
			System.out.println(actualObj);

			mphBankDetails = actualObj.path("responseData").path("mphDetails").path("mphBankAccountDetails");
			System.out.println(mphBankDetails);

			TempMPHBankDto tempMPHBankDto = new TempMPHBankDto();

			for (JsonNode mphBankDetail : mphBankDetails) {
				tempMPHBankDto.setAccountNumber(mphBankDetail.path("accountNumber").asLong());
				tempMPHBankDto.setAccountType(mphBankDetail.path("accountType").asText());
				tempMPHBankDto.setBankName(mphBankDetail.path("bankName").asText());
				tempMPHBankDto.setBankBranch(mphBankDetail.path("bankBranch").asText());
				tempMPHBankDto.setBankAddress(mphBankDetail.path("bankAddressOne").asText());
				tempMPHBankDto.setCountryCode(mphBankDetail.path("countryCode").asLong());
				tempMPHBankDto.setIfscCode(mphBankDetail.path("ifscCode").asText());
				tempMPHBankDto.setStdCode(mphBankDetail.path("stdCode").asLong());
				tempMPHBankDto.setLandLineNumber(mphBankDetail.path("landlineNumber").asLong());
				tempMPHBankDto.setMphId(null);
				tempMPHBankDtoList.add(tempMPHBankDto);
				break;
			}

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return tempMPHBankDtoList;
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> findMidLeaversInMakerBucket(Long masterPolicyId, Long tmpPolicyId) {
		Long mpid = masterPolicyId;

		if (mpid == 0)
			mpid = renewalPolicyTMPRepository.findById(tmpPolicyId).get().getMasterPolicyId();

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findMidLeaversInMakerBucket(mpid);

		if (renewalPolicyTMPEntity == null)
			return ApiResponseDto.success(null);
		else
			return ApiResponseDto.success(RenewalValuationTMPHelper.entityToDtoTemPolicy(renewalPolicyTMPEntity));
	}

	@Override
	public ApiResponseDto<MidleaverBenficiaryDto> saveMidleaverBenficiary(Long tmpPolicyId, Long mphTmpBankId,
			String userName) {

		MidleaverBenficiaryEntity midleaverBenficiaryEntity = new MidleaverBenficiaryEntity();

		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		System.out.println(pmstMidleaverPropsEntity.getId());

		midleaverBenficiaryEntity.setTmpMlPropsId(pmstMidleaverPropsEntity.getId());
		midleaverBenficiaryEntity.setCreatedBy(userName);
		midleaverBenficiaryEntity.setCreatedDate(new Date());
		midleaverBenficiaryEntity.setIsActive(true);
		midleaverBenficiaryEntity.setMphTmpBankId(mphTmpBankId);

		midleaverBenficiaryEntity = midleaverBenficiaryRepository.save(midleaverBenficiaryEntity);

		MidleaverBenficiaryDto midleaverBenficiaryDto = modelMapper.map(midleaverBenficiaryEntity,
				MidleaverBenficiaryDto.class);
		return ApiResponseDto.success(midleaverBenficiaryDto);

	}

	@Override
	public ApiResponseDto<MidleaverBenficiaryDto> updateMidLeaverBenficiary(Long tmpPolicyId, Long mphTmpBankId,
			String userName) {

		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		Long midLeaverPropsId = pmstMidleaverPropsEntity.getId();

		MidleaverBenficiaryEntity midleaverBenficiaryEntity = midleaverBenficiaryRepository
				.findByTmpMlPropsId(midLeaverPropsId);

		midleaverBenficiaryEntity.setMphTmpBankId(mphTmpBankId);
		midleaverBenficiaryEntity.setModifiedBy(userName);
		midleaverBenficiaryEntity.setModifiedDate(new Date());
		midleaverBenficiaryEntity = midleaverBenficiaryRepository.save(midleaverBenficiaryEntity);

		MidleaverBenficiaryDto midleaverBenficiaryDto = modelMapper.map(midleaverBenficiaryEntity,
				MidleaverBenficiaryDto.class);
		return ApiResponseDto.success(midleaverBenficiaryDto);
	}

	@Override
	public ApiResponseDto<TempMPHBankDto> getMidLeaverBenficiary(Long tmpPolicyId) {

		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		MidleaverBenficiaryEntity midleaverBenficiaryEntity = midleaverBenficiaryRepository
				.findByTmpMlPropsId(pmstMidleaverPropsEntity.getId());

		TempMPHBankEntity tempMPHBankEntity = tempMPHBankRepository
				.findById(midleaverBenficiaryEntity.getMphTmpBankId()).get();

		// TempMPHBankEntity tempMPHBankEntity =
		// tempMPHBankRepository.getMphBankDetaild(midleaverBenficiaryEntity.getMphTmpBankId());

		TempMPHBankDto tempMPHBankDto = modelMapper.map(tempMPHBankEntity, TempMPHBankDto.class);

		return ApiResponseDto.success(tempMPHBankDto);
	}

	@Override
	public ApiResponseDto<PmstMidleaverPropsDto> discard(Long tmpPolicyId, String userName) {

		Long draft = 514L;

		PmstMidleaverPropsEntity pmstMidleaverPropsEntity = midleaverPropsRepository.findByTmpPolicyId(tmpPolicyId);
		pmstMidleaverPropsEntity.setStatusId(draft);
		pmstMidleaverPropsEntity.setModifiedBy(userName);
		pmstMidleaverPropsEntity.setModifiedDate(new Date());
		pmstMidleaverPropsEntity = midleaverPropsRepository.save(pmstMidleaverPropsEntity);
		PmstMidleaverPropsDto pmstMidleaverPropsDto = modelMapper.map(pmstMidleaverPropsEntity,
				PmstMidleaverPropsDto.class);

		return ApiResponseDto.success(pmstMidleaverPropsDto);
	}
}
