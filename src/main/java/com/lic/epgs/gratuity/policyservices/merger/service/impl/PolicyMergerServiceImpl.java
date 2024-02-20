package com.lic.epgs.gratuity.policyservices.merger.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.NonUniqueResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;
import com.lic.epgs.gratuity.common.entity.PickListItemEntity;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.PickListItemRepository;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.common.utils.NumericUtils;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpMergerPropsEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.renewal.repository.PolicyRenewalRemainderRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.QuotationRenewalDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalQuotationSearchDTo;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.helper.RenewalPolicyTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.repository.RenewalLifeCoverTMPRepository;
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
import com.lic.epgs.gratuity.policyservices.common.constants.CommonConstantsPS;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceStatusResponseDto;
import com.lic.epgs.gratuity.policyservices.common.enums.PolicyServiceName;
import com.lic.epgs.gratuity.policyservices.common.repository.PolicyServicingRepository;
import com.lic.epgs.gratuity.policyservices.common.service.impl.PolicyServicingCommonServiceImpl;
import com.lic.epgs.gratuity.policyservices.conversion.repository.PsPmstTmpMergerPropsRepository;
import com.lic.epgs.gratuity.policyservices.conversion.service.PsPolicyService;
import com.lic.epgs.gratuity.policyservices.merger.dao.PolicyMergerDao;
import com.lic.epgs.gratuity.policyservices.merger.dto.CommonResponseDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.CommonStatusDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.PolicyLevelMergerApiResponse;
import com.lic.epgs.gratuity.policyservices.merger.dto.PolicyLevelMergerDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.PolicyMergerDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.QuotationStatusDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.SearchSourcePolicyDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.datatable.GetPolicyMergerDetailsRequestDataTableDto;
import com.lic.epgs.gratuity.policyservices.merger.dto.datatable.GetPolicyMergerDetailsResponseDataTableDto;
import com.lic.epgs.gratuity.policyservices.merger.entity.PolicyLevelMergerTempEntity;
import com.lic.epgs.gratuity.policyservices.merger.repository.MPHRepositoryMerger;
import com.lic.epgs.gratuity.policyservices.merger.repository.MasterPolicyRepositoryMerger;
import com.lic.epgs.gratuity.policyservices.merger.repository.PolicyLevelMergerTempRepository;
import com.lic.epgs.gratuity.policyservices.merger.repository.PolicyMemberRepositoryMerger;
import com.lic.epgs.gratuity.policyservices.merger.service.PolicyMergerService;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.DataTable;
import com.lic.epgs.gratuity.quotation.member.repository.MemberRepository;
import com.lic.epgs.gratuity.quotation.repository.QuotationRepository;

@Service
public class PolicyMergerServiceImpl implements PolicyMergerService {

	@Autowired
	MasterPolicyRepositoryMerger masterPolicyRepository;

	@Autowired
	MPHRepositoryMerger mphRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PolicyLevelMergerTempRepository policyLevelMergerTempRepository;

	@Autowired
	PolicyMemberRepositoryMerger policyMemberRepository;

	@Autowired
	Environment env;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	PolicyServicingCommonServiceImpl policyServicingCommonServiceImpl;

	@Autowired(required = true)
	PolicyServicingRepository policyServicingRepository;

	private final RestTemplate restTemplate;

	public PolicyMergerServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Autowired
	PolicyMergerDao policyMergerDao;

	@Autowired
	PsPmstTmpMergerPropsRepository psPmstTmpMergerPropsRepository;
	
	@Autowired
	private QuotationRepository quotationRepository;
	
	@Autowired
	private PickListItemRepository pickListItemRepository;

//	@Autowired
//	PolicyServiceDocumentTempRepository policyServiceDocumentTempRepository;

	public static final Long DRAFT = 11L;
	public static final Long PENDING_FOR_APPROVAL = 22L;
	public static final Long APPROVED = 33L;
	public static final Long REJECTED = 44L;
	public static final Long SEND_TO_MAKER = 55L;
	public static final Long POLICY_MERGED = 17L;

	protected final Logger logger = LogManager.getLogger(getClass());

	// @Override
	/*
	 * public PolicyLevelMergerApiResponse
	 * saveAndUpdatePolicyLevelMerger_OLD(PolicyLevelMergerDto policyLevelMergerDto)
	 * {
	 * 
	 * PolicyLevelMergerApiResponse policyLevelMergerApiResponse = new
	 * PolicyLevelMergerApiResponse();
	 * 
	 * MasterPolicyEntity mergerPolicyEntity = masterPolicyRepository
	 * .findByPolicyNumberAndPolicyStatusIdAndIsActiveTrue(policyLevelMergerDto.
	 * getMergingPolicy(), 123l); MasterPolicyEntity destinationPolicyEntity =
	 * masterPolicyRepository
	 * .findByPolicyNumberAndPolicyStatusIdAndIsActiveTrue(policyLevelMergerDto.
	 * getDestinationPolicy(), 123l);
	 * 
	 * if (mergerPolicyEntity == null && destinationPolicyEntity == null) {
	 * policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
	 * policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.
	 * INVALIDREQUEST); return policyLevelMergerApiResponse; } MPHEntity
	 * mphMasterEntity =
	 * mphRepository.findByIdAndIsActiveTrue(mergerPolicyEntity.getId()); //
	 * MphMasterEntity mphMasterEntitys =
	 * mphMasterRepository.findByMphIdAndIsActiveTrue(destinationPolicyEntity.
	 * getMphId());
	 * 
	 * try { logger.info(
	 * "PolicyLevelMergerServieImpl:saveAndUpdatePolicyLevelMerger:Start");
	 * 
	 * if (validPolicyNumberAndType(policyLevelMergerApiResponse,
	 * policyLevelMergerDto, mergerPolicyEntity, destinationPolicyEntity)) {
	 * PolicyLevelMergerTempEntity newPolicyLevelMergerTempEntity =
	 * modelMapper.map(policyLevelMergerDto, PolicyLevelMergerTempEntity.class);
	 * newPolicyLevelMergerTempEntity.setIsActive(true); if
	 * (policyLevelMergerDto.getMergeId() != null) { PolicyLevelMergerTempEntity
	 * policyLevelMergerTempEntity = policyLevelMergerTempRepository
	 * .findByMergeIdAndIsActiveTrue(policyLevelMergerDto.getMergeId());
	 * newPolicyLevelMergerTempEntity.setCreatedBy(policyLevelMergerTempEntity.
	 * getCreatedBy());
	 * newPolicyLevelMergerTempEntity.setCreatedOn(policyLevelMergerTempEntity.
	 * getCreatedOn());
	 * newPolicyLevelMergerTempEntity.setEffectiveDt(policyLevelMergerTempEntity.
	 * getEffectiveDt());
	 * newPolicyLevelMergerTempEntity.setModifiedBy(policyLevelMergerDto.
	 * getModifiedBy());
	 * newPolicyLevelMergerTempEntity.setModifiedOn(DateUtils.sysDate());
	 * newPolicyLevelMergerTempEntity.setServiceId(policyLevelMergerDto.getServiceId
	 * ()); if (mergerPolicyEntity != null) {
	 * newPolicyLevelMergerTempEntity.setSrcMPHCode(mphMasterEntity.getMphCode());
	 * newPolicyLevelMergerTempEntity.setSrcMPHName(mphMasterEntity.getMphName());
	 * 
	 * } newPolicyLevelMergerTempEntity.setCreatedOn(DateUtils.sysDate());
	 * newPolicyLevelMergerTempEntity.setMergeStatus(DRAFT.intValue());
	 * PolicyLevelMergerTempEntity policyLevelMergerTempEntity1 =
	 * policyLevelMergerTempRepository .save(newPolicyLevelMergerTempEntity);
	 * PolicyLevelMergerDto resPolicyLevelMergerDto =
	 * modelMapper.map(policyLevelMergerTempEntity1, PolicyLevelMergerDto.class);
	 * policyLevelMergerApiResponse.setResponseData(resPolicyLevelMergerDto);
	 * policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.SUCCESS);
	 * policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.
	 * SAVEMESSAGE);
	 * 
	 * } else { newPolicyLevelMergerTempEntity.setEffectiveDt(new Date());
	 * newPolicyLevelMergerTempEntity.setCreatedBy(policyLevelMergerDto.getCreatedBy
	 * ()); newPolicyLevelMergerTempEntity.setCreatedOn(DateUtils.sysDate());
	 * newPolicyLevelMergerTempEntity.setMergeId(null);
	 * 
	 * // Save service id
	 * 
	 * PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
	 * 
	 * policyServiceEntitiy.setServiceType(PolicyServiceName.MERGE.getName());
	 * policyServiceEntitiy.setPolicyId(policyLevelMergerDto.getPolicyId());
	 * policyServiceEntitiy.setCreatedBy(policyLevelMergerDto.getCreatedBy());
	 * policyServiceEntitiy.setCreatedDate(new Date());
	 * policyServiceEntitiy.setIsActive(true); policyServiceEntitiy =
	 * policyServiceRepository.save(policyServiceEntitiy);
	 * 
	 * newPolicyLevelMergerTempEntity.setServiceId(policyServiceEntitiy.getId());
	 * 
	 * // call for backend process for source policy
	 * 
	 * QuotationRenewalDto sourcePolicyMerge = new QuotationRenewalDto();
	 * 
	 * sourcePolicyMerge.setPolicyId(policyLevelMergerDto.getPolicyId());
	 * sourcePolicyMerge.setCreatedBy(policyLevelMergerDto.getCreatedBy());
	 * sourcePolicyMerge.setServiceType(PolicyServiceName.MERGE.getName());
	 * sourcePolicyMerge.setCanStartAnotherService(0l);
	 * 
	 * ApiResponseDto<RenewalPolicyTMPDto> sourcePolicyMergeResponse =
	 * CreatePolicyLevelConversion( sourcePolicyMerge,
	 * policyServiceEntitiy.getId());
	 * 
	 * // call for backend process for destination policy
	 * 
	 * QuotationRenewalDto destPolicyMerge = new QuotationRenewalDto();
	 * 
	 * destPolicyMerge.setPolicyId(policyLevelMergerDto.getDestinationPolicyId());
	 * destPolicyMerge.setCreatedBy(policyLevelMergerDto.getCreatedBy());
	 * destPolicyMerge.setServiceType(PolicyServiceName.MERGE.getName());
	 * destPolicyMerge.setCanStartAnotherService(0l);
	 * 
	 * ApiResponseDto<RenewalPolicyTMPDto> destPolicyMergeResponse =
	 * CreatePolicyLevelConversion( destPolicyMerge, policyServiceEntitiy.getId());
	 * 
	 * 
	 * 
	 * if (mergerPolicyEntity != null) {
	 * newPolicyLevelMergerTempEntity.setSrcMPHCode(mphMasterEntity.getMphCode());
	 * newPolicyLevelMergerTempEntity.setSrcMPHName(mphMasterEntity.getMphName());
	 * 
	 * } newPolicyLevelMergerTempEntity.setCreatedOn(DateUtils.sysDate()); //
	 * Set<PolicyServiceDocumentTempEntity> commonDocsTempEntityList = new
	 * HashSet<>(); // if (!newPolicyLevelMergerTempEntity.getDocs().isEmpty()) { //
	 * newPolicyLevelMergerTempEntity.getDocs().forEach(commonDocsTempEntity -> { //
	 * commonDocsTempEntity.setIsActive(true); //
	 * commonDocsTempEntityList.add(commonDocsTempEntity); // }); // } //
	 * newPolicyLevelMergerTempEntity.setDocs(commonDocsTempEntityList);
	 * newPolicyLevelMergerTempEntity.setMergeStatus(DRAFT.intValue());
	 * PolicyLevelMergerTempEntity policyLevelMergerTempEntity =
	 * policyLevelMergerTempRepository .save(newPolicyLevelMergerTempEntity);
	 * PolicyLevelMergerDto resPolicyLevelMergerDto =
	 * modelMapper.map(policyLevelMergerTempEntity, PolicyLevelMergerDto.class);
	 * 
	 * policyLevelMergerApiResponse.setSourcePolicyMerge(sourcePolicyMergeResponse.
	 * getData());
	 * policyLevelMergerApiResponse.setDestPolicyMerge(destPolicyMergeResponse.
	 * getData());
	 * policyLevelMergerApiResponse.setResponseData(resPolicyLevelMergerDto);
	 * policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.SUCCESS);
	 * policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.
	 * SAVEMESSAGE); } } } catch (Exception e) { e.printStackTrace(); logger.info(
	 * "PolicyLevelMergerServieImpl:saveAndUpdatePolicyLevelMerger:error");
	 * policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
	 * policyLevelMergerApiResponse.setTransactionMessage(e.getMessage()); }
	 * logger.info("PolicyLevelMergerServieImpl:saveAndUpdatePolicyLevelMerger:end")
	 * ; return policyLevelMergerApiResponse; }
	 * 
	 */
	@Override
	public PolicyLevelMergerApiResponse saveAndUpdatePolicyLevelMerger(PolicyMergerDto policyMergerDto) {

		PolicyLevelMergerApiResponse policyLevelMergerApiResponse = new PolicyLevelMergerApiResponse();

		MasterPolicyEntity mergerPolicyEntity = masterPolicyRepository
				.findByPolicyNumberAndPolicyStatusIdAndIsActiveTrue(policyMergerDto.getSourcePolicy(), 123l);
		MasterPolicyEntity destinationPolicyEntity = masterPolicyRepository
				.findByPolicyNumberAndPolicyStatusIdAndIsActiveTrue(policyMergerDto.getDestinationPolicy(), 123l);

		if (mergerPolicyEntity == null && destinationPolicyEntity == null) {
			policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
			policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.INVALIDREQUEST);
			return policyLevelMergerApiResponse;
		}
		MPHEntity mphMasterEntity = mphRepository.findByIdAndIsActiveTrue(mergerPolicyEntity.getId());

		try {
			logger.info("PolicyLevelMergerServieImpl:saveAndUpdatePolicyLevelMerger:Start");

			if (validPolicyNumberAndType(policyLevelMergerApiResponse, policyMergerDto, mergerPolicyEntity,
					destinationPolicyEntity)) {
				PmstTmpMergerPropsEntity newPmstTmpMergerPropsEntity = modelMapper.map(policyMergerDto,
						PmstTmpMergerPropsEntity.class);
				newPmstTmpMergerPropsEntity.setIsActive(true);
				if (policyMergerDto.getMergeId() != null) {
					PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity = psPmstTmpMergerPropsRepository
							.findByIdAndIsActiveTrue(policyMergerDto.getMergeId());

					pmstTmpMergerPropsEntity.setCreatedBy(pmstTmpMergerPropsEntity.getCreatedBy());
					pmstTmpMergerPropsEntity.setCreatedDate(pmstTmpMergerPropsEntity.getCreatedDate());
					pmstTmpMergerPropsEntity.setModifiedBy(policyMergerDto.getModifiedBy());
					pmstTmpMergerPropsEntity.setModifiedDate(DateUtils.sysDate());

					pmstTmpMergerPropsEntity.setMergerRequestNumber(policyMergerDto.getMergerRequestNumber());
					pmstTmpMergerPropsEntity.setMergerRequestDate(policyMergerDto.getMergerRequestDate());
					// pmstTmpMergerPropsEntity.setMergerType(policyLevelMergerDto.getMergerType().toString());

					pmstTmpMergerPropsEntity.setSourcePolicyID(policyMergerDto.getSourcePolicyID());
					pmstTmpMergerPropsEntity.setDestinationPolicyID(policyMergerDto.getDestinationPolicyID());

					pmstTmpMergerPropsEntity.setSourceTmpPolicyID(policyMergerDto.getSourceTmpPolicyID());
					pmstTmpMergerPropsEntity.setDestinationTmpPolicyID(policyMergerDto.getDestinationTmpPolicyID());
					pmstTmpMergerPropsEntity.setSourceAccruedInterest(policyMergerDto.getSourceAccruedInterest());
					pmstTmpMergerPropsEntity.setSourcePriorTotalFund(policyMergerDto.getSourcePriorTotalFund());
					pmstTmpMergerPropsEntity
							.setDestinationPriorFundValue(policyMergerDto.getDestinationPriorFundValue());
					pmstTmpMergerPropsEntity
							.setDestinationPriorTotalValue(policyMergerDto.getDestinationPriorFundValue());
					pmstTmpMergerPropsEntity.setDeductFrom(policyMergerDto.getDeductFrom());
					pmstTmpMergerPropsEntity.setStatusID(policyMergerDto.getStatusID());
					pmstTmpMergerPropsEntity.setIsActive(policyMergerDto.getIsActive());

					PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity1 = psPmstTmpMergerPropsRepository
							.save(pmstTmpMergerPropsEntity);

					// PolicyLevelMergerDto resPolicyLevelMergerDto =
					// modelMapper.map(pmstTmpMergerPropsEntity1,
					// PolicyLevelMergerDto.class);

					policyMergerDto.setMergeId(pmstTmpMergerPropsEntity1.getId());

					policyLevelMergerApiResponse.setResponseData(policyMergerDto);
					policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.SUCCESS);
					policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.SAVEMESSAGE);

				} else {

					PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();

					policyServiceEntitiy.setServiceType(PolicyServiceName.MERGER.getName());
					policyServiceEntitiy.setPolicyId(policyMergerDto.getSourceTmpPolicyID());
					policyServiceEntitiy.setCreatedBy(policyMergerDto.getCreatedBy());
					policyServiceEntitiy.setCreatedDate(new Date());
					policyServiceEntitiy.setIsActive(true);
					policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

					// newPmstTmpMergerPropsEntity.setServiceId(policyServiceEntitiy.getId());

					// call for backend process for source policy

					QuotationRenewalDto sourcePolicyMerge = new QuotationRenewalDto();

					sourcePolicyMerge.setPolicyId(policyMergerDto.getSourcePolicyID());
					sourcePolicyMerge.setCreatedBy(policyMergerDto.getCreatedBy());
					sourcePolicyMerge.setServiceType(PolicyServiceName.MERGER.getName());
					sourcePolicyMerge.setCanStartAnotherService(0l);

					ApiResponseDto<RenewalPolicyTMPDto> sourcePolicyMergeResponse = CreatePolicyLevelConversion(
							sourcePolicyMerge, policyServiceEntitiy.getId());

					newPmstTmpMergerPropsEntity.setSourceTmpPolicyID(sourcePolicyMergeResponse.getData().getId());

//					call for backend process  for destination policy

					QuotationRenewalDto destPolicyMerge = new QuotationRenewalDto();

					destPolicyMerge.setPolicyId(policyMergerDto.getDestinationPolicyID());
					destPolicyMerge.setCreatedBy(policyMergerDto.getCreatedBy());
					destPolicyMerge.setServiceType(PolicyServiceName.MERGER.getName());
					destPolicyMerge.setCanStartAnotherService(0l);

					ApiResponseDto<RenewalPolicyTMPDto> destPolicyMergeResponse = CreatePolicyLevelConversion(
							destPolicyMerge, policyServiceEntitiy.getId());

					newPmstTmpMergerPropsEntity.setDestinationTmpPolicyID(destPolicyMergeResponse.getData().getId());

					// newPolicyLevelMergerTempEntity.setEffectiveDt(new Date());
					newPmstTmpMergerPropsEntity.setCreatedBy(policyMergerDto.getCreatedBy());
					// newPolicyLevelMergerTempEntity.setMergeId(null);
					newPmstTmpMergerPropsEntity.setCreatedBy(policyMergerDto.getCreatedBy());
					newPmstTmpMergerPropsEntity.setModifiedBy(policyMergerDto.getModifiedBy());
					newPmstTmpMergerPropsEntity.setMergerRequestNumber(policyMergerDto.getMergerRequestNumber());
					newPmstTmpMergerPropsEntity.setMergerRequestDate(policyMergerDto.getMergerRequestDate());
					// pmstTmpMergerPropsEntity.setMergerType(policyLevelMergerDto.getMergerType().toString());
					// newPmstTmpMergerPropsEntity.setSourceTmpPolicyID(policyMergerDto.getSourceTmpPolicyID());
					// newPmstTmpMergerPropsEntity.setDestinationTmpPolicyID(policyMergerDto.getDestinationTmpPolicyID());
					newPmstTmpMergerPropsEntity.setSourceAccruedInterest(policyMergerDto.getSourceAccruedInterest());
					newPmstTmpMergerPropsEntity.setSourcePriorTotalFund(policyMergerDto.getSourcePriorTotalFund());
					newPmstTmpMergerPropsEntity
							.setDestinationPriorFundValue(policyMergerDto.getDestinationPriorFundValue());
					newPmstTmpMergerPropsEntity
							.setDestinationPriorTotalValue(policyMergerDto.getDestinationPriorFundValue());
					newPmstTmpMergerPropsEntity.setDeductFrom(policyMergerDto.getDeductFrom());
					newPmstTmpMergerPropsEntity.setStatusID(policyMergerDto.getStatusID());
					newPmstTmpMergerPropsEntity.setIsActive(policyMergerDto.getIsActive());

					newPmstTmpMergerPropsEntity.setModifiedDate(DateUtils.sysDate());
					newPmstTmpMergerPropsEntity.setCreatedDate(DateUtils.sysDate());

					PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity = psPmstTmpMergerPropsRepository
							.save(newPmstTmpMergerPropsEntity);

					policyMergerDto.setMergeId(pmstTmpMergerPropsEntity.getId());

					// PolicyLevelMergerDto resPolicyLevelMergerDto =
					// modelMapper.map(policyLevelMergerTempEntity,
					// PolicyLevelMergerDto.class);

					policyLevelMergerApiResponse.setSourcePolicyMerge(sourcePolicyMergeResponse.getData());
					policyLevelMergerApiResponse.setDestPolicyMerge(destPolicyMergeResponse.getData());
					policyLevelMergerApiResponse.setResponseData(policyMergerDto);
					policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.SUCCESS);
					policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.SAVEMESSAGE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("PolicyLevelMergerServieImpl:saveAndUpdatePolicyLevelMerger:error");
			policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
			policyLevelMergerApiResponse.setTransactionMessage(e.getMessage());
		}
		logger.info("PolicyLevelMergerServieImpl:saveAndUpdatePolicyLevelMerger:end");
		return policyLevelMergerApiResponse;
	}

	public boolean validPolicyNumberAndType(PolicyLevelMergerApiResponse policyLevelMergerApiResponse,
			PolicyMergerDto policyLevelMergerDto, MasterPolicyEntity mergerPolicyEntity,
			MasterPolicyEntity destinationPolicyEntity) {
		if (policyLevelMergerDto.getSourcePolicy().equals(policyLevelMergerDto.getDestinationPolicy())) {
			policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
			policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.VAlIDATE_POLICY);
			return false;
		}
//		else if (!policyLevelMergerDto.getMergerType().equals(policyLevelMergerDto.getDestinationPolicyType())) {
//			policyLevelMergerApiResponse.setTransactionStatus(CommonConstants.FAIL);
//			policyLevelMergerApiResponse.setTransactionMessage(CommonConstants.VAlIDATE_POLICY_TYPE);
//			return false;
//		}
//		PolicyLevelMergerTempEntity mergeTemp = policyLevelMergerTempRepository
//				.findByMergingPolicyAndMergeStatusInAndIsActiveTrue(policyLevelMergerDto.getMergingPolicy(),
//						CommonConstants.validationMergerinprogressMaker());
//		if (mergeTemp != null && StringUtils.isNotBlank(mergeTemp.getMergeId().toString())
//				&& !mergeTemp.getMergeId().equals(policyLevelMergerDto.getMergeId())) {
//			policyLevelMergerApiResponse.setTransactionStatus(CommonConstants.FAIL);
//			policyLevelMergerApiResponse.setTransactionMessage(CommonConstants.ALREADY_USED_MERGING_POLICY);
//			return false;
//		}
//		PolicyLevelMergerTempEntity destinationTemp = policyLevelMergerTempRepository
//				.findByDestinationPolicyAndMergeStatusInAndIsActiveTrue(policyLevelMergerDto.getDestinationPolicy(),
//						CommonConstants.validationMergerinprogressMaker());
//		if (destinationTemp != null && StringUtils.isNotBlank(destinationTemp.getMergeId().toString())
//				&& mergeTemp != null && !mergeTemp.getMergeId().equals(policyLevelMergerDto.getMergeId())) {
//			policyLevelMergerApiResponse.setTransactionStatus(CommonConstants.FAIL);
//			policyLevelMergerApiResponse.setTransactionMessage(CommonConstants.ALREADY_USED_DESTINATION_POLICY);
//			return false;
//		}
//		if (mergerPolicyEntity.getPolicyType().equalsIgnoreCase(CommonConstants.DB)
//				&& destinationPolicyEntity.getPolicyType().equalsIgnoreCase(CommonConstants.DB)) {
//			if (mergerPolicyEntity.getNoOfCategory() <= destinationPolicyEntity.getNoOfCategory()) {
//				policyLevelMergerApiResponse.setTransactionStatus(CommonConstants.FAIL);
//				policyLevelMergerApiResponse.setTransactionMessage(CommonConstants.VAlIDATE_DB_TYPE);
//				return false;
//			}
//		} else if (mergerPolicyEntity.getPolicyType().equalsIgnoreCase(CommonConstants.DC)
//				&& destinationPolicyEntity.getPolicyType().equalsIgnoreCase(CommonConstants.DC)) {
//			if (
////		mergerPolicyEntity.getArd().compareTo(destinationPolicyEntity.getArd()) != 0
////					|| mergerPolicyEntity.getArd() == null || destinationPolicyEntity.getArd() == null ||
//				mergerPolicyEntity.getNoOfCategory() > destinationPolicyEntity.getNoOfCategory()) {
//				policyLevelMergerApiResponse.setTransactionStatus(CommonConstants.FAIL);
//				policyLevelMergerApiResponse.setTransactionMessage(CommonConstants.VAlIDATE_DC_TYPE);
//				return false;
//			}
//		}
		return true;
	}

	@Override
	public PolicyLevelMergerApiResponse sendToChecker(PolicyLevelMergerDto policyLevelMergerDto) {
		PolicyLevelMergerApiResponse policyLevelMergerApiResponse = new PolicyLevelMergerApiResponse();
		logger.info("PolicyLevelMergerServieImpl:sendToChecker:start");
		try {
			PolicyLevelMergerTempEntity policyLevelMergerTempEntity = policyLevelMergerTempRepository
					.findByMergeIdAndIsActiveTrue(policyLevelMergerDto.getMergeId());
			if (policyLevelMergerTempEntity != null) {
				policyLevelMergerTempEntity.setWorkflowStatus(policyLevelMergerDto.getWorkflowStatus());
				policyLevelMergerTempEntity.setMergeStatus((PENDING_FOR_APPROVAL.intValue()));
				policyLevelMergerTempEntity.setIsActive(true);
				policyLevelMergerTempEntity.setModifiedOn(DateUtils.sysDate());
				policyLevelMergerTempEntity = policyLevelMergerTempRepository.save(policyLevelMergerTempEntity);
				PolicyLevelMergerDto resPolicyLevelMergerDto = modelMapper.map(policyLevelMergerTempEntity,
						PolicyLevelMergerDto.class);
				resPolicyLevelMergerDto.setSrcMPHCode(policyLevelMergerTempEntity.getSrcMPHCode());
				resPolicyLevelMergerDto.setDestMPHCode(policyLevelMergerTempEntity.getDestMPHCode());
				resPolicyLevelMergerDto.setSrcMPHName(policyLevelMergerTempEntity.getPolicyNumber());
				resPolicyLevelMergerDto.setDestMPHName(policyLevelMergerTempEntity.getPolicyNumber());
				policyLevelMergerApiResponse.setResponseObject(resPolicyLevelMergerDto);
				policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.SUCCESS);
				policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.UPDATEMESSAGE);
				return policyLevelMergerApiResponse;
			} else {
				policyLevelMergerApiResponse.setResponseObject(policyLevelMergerApiResponse);
				policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
				policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.NO_RECORD_FOUND);
				return policyLevelMergerApiResponse;
			}

		} catch (IllegalArgumentException e) {
			logger.info("PolicyLevelMergerServieImpl:sendToChecker:error");
			policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
			policyLevelMergerApiResponse.setTransactionMessage(e.getMessage());
			logger.info("PolicyLevelMergerServieImpl:sendToChecker:end");
			return policyLevelMergerApiResponse;
		}
	}

	@Override
	public PolicyLevelMergerApiResponse sendToMaker(PolicyLevelMergerDto policyLevelMergerDto) {
		PolicyLevelMergerApiResponse policyLevelMergerApiResponse = new PolicyLevelMergerApiResponse();
		logger.info("PolicyLevelMergerServieImpl:sendToChecker:start");
		try {
			PolicyLevelMergerTempEntity policyLevelMergerTempEntity = policyLevelMergerTempRepository
					.findByMergeIdAndIsActiveTrue(policyLevelMergerDto.getMergeId());
			if (policyLevelMergerTempEntity != null) {
				policyLevelMergerTempEntity.setWorkflowStatus(policyLevelMergerDto.getWorkflowStatus());
				policyLevelMergerTempEntity.setMergeStatus((SEND_TO_MAKER.intValue()));
				policyLevelMergerTempEntity.setIsActive(true);
				policyLevelMergerTempEntity.setModifiedOn(DateUtils.sysDate());
				policyLevelMergerTempEntity = policyLevelMergerTempRepository.save(policyLevelMergerTempEntity);
				PolicyLevelMergerDto resPolicyLevelMergerDto = modelMapper.map(policyLevelMergerTempEntity,
						PolicyLevelMergerDto.class);
				resPolicyLevelMergerDto.setSrcMPHCode(policyLevelMergerTempEntity.getSrcMPHCode());
				resPolicyLevelMergerDto.setSrcMPHName(policyLevelMergerTempEntity.getPolicyNumber());
				policyLevelMergerApiResponse.setResponseObject(resPolicyLevelMergerDto);
				policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.SUCCESS);
				policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.UPDATEMESSAGE);
				return policyLevelMergerApiResponse;
			} else {
				policyLevelMergerApiResponse.setResponseObject(policyLevelMergerApiResponse);
				policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
				policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.NO_RECORD_FOUND);
				return policyLevelMergerApiResponse;
			}

		} catch (IllegalArgumentException e) {
			logger.info("PolicyLevelMergerServieImpl:sendToChecker:error");
			policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
			policyLevelMergerApiResponse.setTransactionMessage(e.getMessage());
			logger.info("PolicyLevelMergerServieImpl:sendToChecker:end");
			return policyLevelMergerApiResponse;
		}
	}

	@Override
	public PolicyLevelMergerApiResponse rejectedMerger(PolicyLevelMergerDto policyLevelMergerDto) {
		PolicyLevelMergerApiResponse policyLevelMergerApiResponse = new PolicyLevelMergerApiResponse();
		logger.info("PolicyLevelMergerServieImpl:sendToChecker:start");
		try {
			PolicyLevelMergerTempEntity policyLevelMergerTempEntity = policyLevelMergerTempRepository
					.findByMergeIdAndIsActiveTrue(policyLevelMergerDto.getMergeId());
			if (policyLevelMergerTempEntity != null) {
				policyLevelMergerTempEntity.setWorkflowStatus(policyLevelMergerDto.getWorkflowStatus());
				policyLevelMergerTempEntity.setMergeStatus((REJECTED.intValue()));

				policyLevelMergerTempEntity.setRejectionRemarks(policyLevelMergerDto.getRejectionRemarks());
				policyLevelMergerTempEntity.setRejectionReasonCode(policyLevelMergerDto.getRejectionReasonCode());

				policyLevelMergerTempEntity.setIsActive(true);
				policyLevelMergerTempEntity.setModifiedOn(DateUtils.sysDate());
				policyLevelMergerTempEntity = policyLevelMergerTempRepository.save(policyLevelMergerTempEntity);
				PolicyLevelMergerDto resPolicyLevelMergerDto = modelMapper.map(policyLevelMergerTempEntity,
						PolicyLevelMergerDto.class);
				resPolicyLevelMergerDto.setSrcMPHCode(policyLevelMergerTempEntity.getSrcMPHCode());
				resPolicyLevelMergerDto.setDestMPHCode(policyLevelMergerTempEntity.getDestMPHCode());

				resPolicyLevelMergerDto.setSrcMPHName(policyLevelMergerTempEntity.getPolicyNumber());
				policyLevelMergerApiResponse.setResponseObject(resPolicyLevelMergerDto);
				policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.SUCCESS);
				policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.UPDATEMESSAGE);
				return policyLevelMergerApiResponse;
			} else {
				policyLevelMergerApiResponse.setResponseObject(policyLevelMergerApiResponse);
				policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
				policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.NO_RECORD_FOUND);
				return policyLevelMergerApiResponse;
			}

		} catch (IllegalArgumentException e) {
			logger.info("PolicyLevelMergerServieImpl:sendToChecker:error");
			policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
			policyLevelMergerApiResponse.setTransactionMessage(e.getMessage());
			logger.info("PolicyLevelMergerServieImpl:sendToChecker:end");
			return policyLevelMergerApiResponse;
		}

	}

	@Override
	public PolicyLevelMergerApiResponse approvedPolicyLevelMerger(PolicyLevelMergerDto policyLevelMergerDto) {
		PolicyLevelMergerApiResponse policyLevelMergerApiResponse = new PolicyLevelMergerApiResponse();
		logger.info("PolicyLevelMergerServieImpl:approvedAndRejectPolicyLevelMerger:start");
		try {
			final PolicyLevelMergerTempEntity result = policyLevelMergerTempRepository
					.findByMergeIdAndIsActiveTrue(policyLevelMergerDto.getMergeId());
			if (result != null) {
				PolicyLevelMergerTempEntity policyLevelMergerTemp = result;
				policyLevelMergerTemp.setMergeStatus(APPROVED.intValue());
				policyLevelMergerTemp.setWorkflowStatus(policyLevelMergerDto.getWorkflowStatus());
				policyLevelMergerTemp.setModifiedOn(DateUtils.sysDate());
				policyLevelMergerTemp.setIsActive(true);
				policyLevelMergerTempRepository.save(policyLevelMergerTemp);

				// update status of source member policy as POLICY_MERGED

				MasterPolicyEntity mergerPolicy = masterPolicyRepository
						.findByPolicyNumberAndIsActiveTrue(result.getMergingPolicy());
				// merging policy status change
				mergerPolicy.setIsActive(true);
				mergerPolicy.setPolicyStatusId(POLICY_MERGED);
				masterPolicyRepository.save(mergerPolicy);
				List<PolicyMemberEntity> memberMasterEntity = policyMemberRepository
						.findByPolicyIdAndIsActiveTrue(mergerPolicy.getId());
				if (memberMasterEntity != null) {
					for (PolicyMemberEntity entit : memberMasterEntity) {
						if (entit.getId() != null) {
							PolicyMemberEntity masterEntity = modelMapper.map(entit, PolicyMemberEntity.class);
							masterEntity.setIsActive(false);
							masterEntity.setStatusId(POLICY_MERGED);
							policyMemberRepository.save(masterEntity);
						}
					}

				}

				// Add member into Destination policy

//				if (memberMasterEntity != null) {
//					for (PolicyMemberEntity entit : memberMasterEntity) {
//						if (entit.getId() != null) {
//							PolicyMemberEntity masterEntity = modelMapper.map(entit,
//									PolicyMemberEntity.class);
//							
////							Long getMaxLicNumber=memberRepository.MaxLicNumber(memberDto.getQuotationId()); 
////							getMaxLicNumber = getMaxLicNumber == null ? 1 : getMaxLicNumber + 1;
////							
////							masterEntity.setLicId(getMaxLicNumber.toString());
//							masterEntity.setCreatedDate(new Date());
//							masterEntity.setIsActive(true);
//							masterEntity.setId(null);
//							
//							policyMemberRepository.save(masterEntity);
//					//		list.add(modelMapper.map(masterEntity, MemberMasterDto.class));
//						}
//					}
//				
//				}

//				policyLevelMergerApiResponse
//						.setResponseData(modelMapper.map(policyLevelMergerEntity, PolicyLevelMergerDto.class));
				policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.SUCCESS);
				policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.SAVEMESSAGE);
			} else {
				policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
				policyLevelMergerApiResponse.setTransactionMessage(CommonConstantsPS.NO_RESULT);
				return policyLevelMergerApiResponse;
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyLevelMergerServieImpl:approveOrReject", e);
			policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
			policyLevelMergerApiResponse.setTransactionMessage(e.getMessage());
		}
		logger.info("PolicyLevelMergerServieImpl:approvedAndRejectPolicyLevelMerger:end");
		return policyLevelMergerApiResponse;

	}

	@Override
	public DataTable getPolicyMergerDetailsDataTable(
			GetPolicyMergerDetailsRequestDataTableDto getPolicyMergerDetailsRequestDataTableDto) {
		DataTable dataTable = new DataTable();
		try {
			List<GetPolicyMergerDetailsResponseDataTableDto> getPolicyMergerDetailsResponseDataTableDtoList = policyMergerDao
					.getPolicyMergerDetailsDataTable(getPolicyMergerDetailsRequestDataTableDto);

			dataTable.setData(getPolicyMergerDetailsResponseDataTableDtoList);

			if (getPolicyMergerDetailsResponseDataTableDtoList.size() > 0) {
				dataTable.setNoOfPages(getPolicyMergerDetailsResponseDataTableDtoList.get(0).getNoOfPages());
				dataTable.setRecordsTotal(getPolicyMergerDetailsResponseDataTableDtoList.get(0).getRecordsTotal());
			}

			return dataTable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataTable;
	}

	@Override
	public Object contributionTofund(Long policyId, String financialYear) {

		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			ResponseEntity<Object> restTemplateResponse = restTemplate
					.exchange("https://10.240.34.17:8443/gratuityintrestcalcservice/fund/api/transactionEntries/"
							+ policyId + "/" + financialYear, HttpMethod.GET, entity, Object.class);

			return restTemplateResponse.getBody();

		} catch (Exception e) {
			PolicyServiceStatusResponseDto responseDto = new PolicyServiceStatusResponseDto();
			responseDto.setMessage(e.getMessage());
			e.printStackTrace();
			return responseDto;
		}
	}

//	@Override
//	public PolicyLevelMergerApiResponse uploadDocument(PolicyServiceDocumentDto docsDto) {
//		PolicyLevelMergerApiResponse policyLevelMergerApiResponse = new PolicyLevelMergerApiResponse();
//		try {
//			PolicyServiceDocumentTempEntity commonDocsTempEntity = modelMapper.map(docsDto, PolicyServiceDocumentTempEntity.class);
//			commonDocsTempEntity.setIsActive(true);
//			commonDocsTempEntity.setDocumentId(null);
//			PolicyServiceDocumentTempEntity saveDocsTempEntity = policyServiceDocumentTempRepository.save(commonDocsTempEntity);
//			PolicyServiceDocumentDto commonDocsDto = modelMapper.map(saveDocsTempEntity, PolicyServiceDocumentDto.class);
//			commonDocsDto.setDocumentId(saveDocsTempEntity.getDocumentId());
//			policyLevelMergerApiResponse.setPolicyServiceDocumentDto(commonDocsDto);
//			policyLevelMergerApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
//			policyLevelMergerApiResponse.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicyLevelMergerServieImpl-- uploadDocument --", e);
//			policyLevelMergerApiResponse.setTransactionMessage(CommonConstants.FAIL);
//			policyLevelMergerApiResponse.setTransactionStatus(CommonConstants.ERROR);
//		}
//		return policyLevelMergerApiResponse;
//	}

	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;

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
	private MPHRepository mPHRepository;

	@Autowired
	PsPolicyService psPolicyService;

	@Autowired
	MemberCategoryModuleRepository memberCategoryModuleRepository;

//	save master data into temp

//	public ApiResponseDto<RenewalPolicyTMPDto> CreatePolicyLevelConversion(QuotationRenewalDto quotationRenewalDto,
//			Long policyServiceId) {
//
////		if (policyServiceRepository
////				.findByPolicyandType(quotationRenewalDto.getPolicyId(), quotationRenewalDto.getServiceType())
////				.size() > 0) {
////			return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
////		}
////		else
//		{
//
////			PolicyRenewalRemainderEntity updateStatus= policyRenewalRemainderRepository.findBypolicyId(quotationRenewalDto.getPolicyId());
////	updateStatus.setIsActive(false);
////	policyRenewalRemainderRepository.save(updateStatus);
////		PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
////
////		policyServiceEntitiy.setServiceType(quotationRenewalDto.getServiceType());
////		policyServiceEntitiy.setPolicyId(quotationRenewalDto.getPolicyId());
////		policyServiceEntitiy.setCreatedBy(quotationRenewalDto.getCreatedBy());
////		policyServiceEntitiy.setCreatedDate(new Date());
////		policyServiceEntitiy.setIsActive(true);
////		policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);
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
//			renewalPolicyTMPEntity.setPolicyServiceId(policyServiceId);
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

//	@Transactional	
	public ApiResponseDto<RenewalPolicyTMPDto> CreatePolicyLevelConversion(QuotationRenewalDto quotationRenewalDto,
			Long policyServiceId) {
//		
//		if (policyServiceRepository
//				.findByPolicyandType(quotationRenewalDto.getPolicyId(), PolicyServiceName.CONVERSION.getName())
//				.size() > 0) {
//			return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
//		} else 
		{

//			PolicyRenewalRemainderEntity updateStatus = policyRenewalRemainderRepository
//					.findBypolicyId(quotationRenewalDto.getPolicyId());
//			updateStatus.setIsActive(false);
//			policyRenewalRemainderRepository.save(updateStatus);
//			PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
//
//			policyServiceEntitiy.setServiceType(quotationRenewalDto.getServiceType());
//			policyServiceEntitiy.setPolicyId(quotationRenewalDto.getPolicyId());
//			policyServiceEntitiy.setCreatedBy(quotationRenewalDto.getCreatedBy());
//			policyServiceEntitiy.setCreatedDate(new Date());
//			policyServiceEntitiy.setIsActive(true);
//			policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

			MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository
					.findById(quotationRenewalDto.getPolicyId());

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
			renewalPolicyTMPEntity.setPolicyServiceId(policyServiceId);
			renewalPolicyTMPEntity.setIsActive(true);
			renewalPolicyTMPEntity.setCreatedBy(quotationRenewalDto.getCreatedBy());
			renewalPolicyTMPEntity.setCreatedDate(new Date());
			renewalPolicyTMPEntity.setQuotationNumber(RenewalPolicyTMPHelper
					.nextQuotationNumber(renewalPolicyTMPRepository.maxQuotationNumber()).toString());

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

			return ApiResponseDto.success(RenewalValuationTMPHelper.entityToDto(renewalPolicyTMPEntity));
		}
	}

	@Override
	public ApiResponseDto<List<RenewalPolicyTMPDto>> inprogressAndExistingMergeDetails(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo) {
		List<RenewalPolicyTMPDto> newgetRenewalPolicyTMPEntity = new ArrayList<>();

		List<RenewalPolicyTMPDto> getRenewalPolicyTMPEntity = policyMergerDao
				.inprogressAndExistingMerge(renewalQuotationSearchDTo);
		for (RenewalPolicyTMPDto renewalPolicyTMPDto : getRenewalPolicyTMPEntity) {
			String s = renewalPolicyTMPRepository
					.getByPolicyNubmberbyid(renewalPolicyTMPDto.getDestinationTmpPolicyId());
			renewalPolicyTMPDto.setDestinationPolicyNumber(s);
			newgetRenewalPolicyTMPEntity.add(renewalPolicyTMPDto);
		}
		return ApiResponseDto.success(newgetRenewalPolicyTMPEntity);

	}

	@Override
	public PolicyLevelMergerApiResponse getAllSourcePolicyDetails(String policyNumber) {
		PolicyLevelMergerApiResponse policyLevelMergerApiResponse = new PolicyLevelMergerApiResponse();
		logger.info("-----------------PolicyMergerServiceImpl::getAllSourcePolicyDetails::start-------------------");
		try {
			Object[] allDetails = masterPolicyRepository.findAllDetailsByPolicyNumber(policyNumber);
			if (allDetails != null && allDetails.length > 0) {
				for (Object object : allDetails) {
					Object[] ob = (Object[]) object;
					SearchSourcePolicyDto sourcePolicyDto = new SearchSourcePolicyDto();
					sourcePolicyDto.setPolicyNumber(String.valueOf(ob[0]));
					sourcePolicyDto.setMphName(String.valueOf(ob[10]));
					sourcePolicyDto.setMasterPolicyStatus(String.valueOf(ob[2]));
					sourcePolicyDto.setAnnualRenewalDate(DateUtils.convertObjectToDate(ob[4]));
					sourcePolicyDto.setEmployeeContribution(Double.parseDouble(ob[5].toString()));
					sourcePolicyDto.setEmployerContribution(Double.parseDouble(ob[6].toString()));
					sourcePolicyDto.setVoluntaryContribution(Double.parseDouble(ob[7].toString()));
					sourcePolicyDto.setTotalContribution(Double.parseDouble(ob[8].toString()));
					sourcePolicyDto.setNumberOfLives(NumericUtils.convertObjectToInteger(ob[9]));
					sourcePolicyDto.setFrequency(String.valueOf(ob[11]));
					policyLevelMergerApiResponse.setSearchSourcePolicyDto(sourcePolicyDto);
				}
			}
		} catch (Exception e) {
			logger.error("-----------Error::PolicyMergerServiceImpl::getAllSourcePolicyDetails::{}-------------------");
			policyLevelMergerApiResponse.setTransactionStatus(CommonConstantsPS.FAIL);
			policyLevelMergerApiResponse.setTransactionMessage(e.getMessage());
		}
		return policyLevelMergerApiResponse;
	}

	@Override
	public CommonResponseDto getFinancialYeartDetails(String date) {
		CommonResponseDto response = new CommonResponseDto();
		Object object = quotationRepository.findByObjectNew(date);
		if (object != null) {
			Object[] ob = (Object[]) object;
			response.setData(ob[0].toString());
			response.setFinancialYear(ob[1].toString());
			response.setQuarter(ob[2].toString());
			response.setFrequency(ob[3].toString());
			response.setTransactionStatus(CommonConstantsPS.SUCCESS);
			response.setTransactionMessage(CommonConstantsPS.FETCH_MESSAGE);
			return response;
		} else {
			response.setTransactionStatus(CommonConstantsPS.FAIL);
			return response;
		}
	}

	@Override
	public CommonResponseDto commonStatus() {
		CommonResponseDto statusResponseDto=new CommonResponseDto();
		logger.info("CommonServiceImpl --commonStatus-- Start");
		try {
			List<PickListItemEntity> entity=pickListItemRepository.findQuotationStatusDetails();
			if(!entity.isEmpty()) {
				ArrayList<QuotationStatusDto> quotationStatusList=new ArrayList<>();
				for(PickListItemEntity quotationStatus:entity) {
					QuotationStatusDto statusDto=new QuotationStatusDto();
					statusDto.setId(quotationStatus.getId());
					statusDto.setPickListId(quotationStatus.getPickListId());
					statusDto.setName(quotationStatus.getName());
					statusDto.setDescription(quotationStatus.getDescription());
					statusDto.setDisplayOrder(quotationStatus.getDisplayOrder());
					statusDto.setParentId(quotationStatus.getParentId());
					statusDto.setIsActive(quotationStatus.getIsActive());
					statusDto.setCreatedBy(quotationStatus.getCreatedBy());
					statusDto.setCreatedDate(quotationStatus.getCreatedDate());
					statusDto.setModifiedBy(quotationStatus.getModifiedBy());
					statusDto.setModifiedDate(quotationStatus.getModifiedDate());
					quotationStatusList.add(statusDto);
					;
				}
				statusResponseDto.setResponseData(quotationStatusList);
				statusResponseDto.setTransactionStatus(CommonConstantsPS.SUCCESS);
				statusResponseDto.setTransactionMessage(CommonConstantsPS.FETCH_MESSAGE);
			}else {
				statusResponseDto.setTransactionStatus(CommonConstantsPS.ERROR);
				statusResponseDto.setTransactionMessage(CommonConstantsPS.NO_RESULT);
			}
			
		}catch(NonUniqueResultException e) {
			statusResponseDto.setTransactionStatus(CommonConstantsPS.ERROR);
			statusResponseDto.setTransactionMessage(CommonConstantsPS.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --commonStatus-- End");
		return statusResponseDto;
	}
}
