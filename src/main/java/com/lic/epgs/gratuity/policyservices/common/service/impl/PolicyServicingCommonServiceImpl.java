package com.lic.epgs.gratuity.policyservices.common.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.PolicyServiceRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyTmpSearchRepository;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceMatrixDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceStatusResponseDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PremiumGstRefundDto;
import com.lic.epgs.gratuity.policyservices.common.dto.LeavingMemberDto;
import com.lic.epgs.gratuity.policyservices.common.entity.PolicyServiceMasterEntity;
import com.lic.epgs.gratuity.policyservices.common.repository.PolicyServicingRepository;
import com.lic.epgs.gratuity.policyservices.common.service.PolicyServicingCommonService;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.StatusDto;

@Service
public class PolicyServicingCommonServiceImpl implements PolicyServicingCommonService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	ModelMapper modelMapper;

	@Autowired(required = true)
	PolicyServicingRepository policyServicingRepository;

	@Autowired
	PolicyServiceMatrixService policyServiceMatrixService;
	
	@Autowired
	PolicyServiceRepository policyServiceRepository;
	
	@Autowired
	private PolicyTmpSearchRepository policyTmpSearchRepository;
	
	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;
	
	@Autowired
	private PolicyMemberRepository policyMemberRepository;
	
	@Autowired
	private TempMemberRepository tempMemberRepository;
	
	@Autowired
	private StandardCodeRepository standardCodeRepository;
	
	@Autowired
	private MasterPolicyRepository masterPolicyRepository;

	@Override
	public PolicyServiceStatusResponseDto savePolicyService(PolicyServiceDto policyServiceDto) {
		PolicyServiceStatusResponseDto response = new PolicyServiceStatusResponseDto();
		PolicyServiceMasterEntity policyServiceEntity = modelMapper.map(policyServiceDto,
				PolicyServiceMasterEntity.class);

		if (policyServiceDto.getServiceId() != null) {
			Optional<PolicyServiceMasterEntity> policyService = policyServicingRepository
					.findById(policyServiceDto.getServiceId());
			if (policyService.isPresent()) {
				response.setMessage("Data Updated Successfully");
			} else {
				response.setMessage("Data Saved Successfully");
			}
		} else {
			response.setMessage("Data Saved Successfully");
		}

		PolicyServiceMasterEntity servicesEntity = policyServicingRepository.save(policyServiceEntity);
		servicesEntity.setServiceNumber(servicesEntity.getServiceId().toString());

		PolicyServiceMasterEntity servicesMaster = policyServicingRepository.save(servicesEntity);

		response.setResponseData(servicesMaster);
		response.setStatus("SUCCESS");
		return response;
	}

//	@Override
//	public PolicyServiceCommonResponseDto getServiceDetailsByServiceId(Long serviceId) {
//		PolicyServiceCommonResponseDto policyServiceResponse = new PolicyServiceCommonResponseDto();
//		try {
//			logger.info("PolicyServicingCommonServiceImpl:getServiceDetailsByServiceId---Started");
//			Optional<PolicyServiceMasterEntity> policyService = policyServicingRepository.findById(serviceId);
//			if (policyService.isPresent()) {
//				PolicyServiceMasterEntity policy = policyService.get();
//				PolicyServiceDto policyserviceDto = modelMapper.map(policy, PolicyServiceDto.class);
//				policyServiceResponse.setPolicyServiceDto(policyserviceDto);
//				policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.SUCCESS);
//				policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.RECORD_FOUND);
//			} else {
//				policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.SUCCESS);
//				policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.NO_RECORD_FOUND);
//				logger.info("PolicyServicingCommonServiceImpl:getServiceDetailsByServiceId--NO data found");
//			}
//		} catch (IllegalArgumentException e) {
//			logger.info("PolicyServicingCommonServiceImpl:getServiceDetailsByServiceId--ERROR--", e);
//			policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.ERROR);
//			policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.FAIL);
//		} finally {
//			logger.info("PolicyServicingCommonServiceImpl:getServiceDetailsByServiceId--Ends");
//		}
//		return policyServiceResponse;
//	}

	public String getServiceNumberByPolicyId(Long id) {
		return policyServicingRepository.findByPolicyId(id);

	}

	@Override
	public Object getActiveServiceDetailsByPolicy(String policyNumber, String currentService) {

		String message = "";
		List<PolicyServiceMatrixDto> listPolicyServiceMatrixDto = new ArrayList<>();

		List<PolicyServiceMasterEntity> findByPolicyNumberAndServiceStatusIgnoreCase = policyServicingRepository
				.findByPolicyNumberAndServiceStatusIgnoreCase(policyNumber, "ACTIVE");
		
		if (findByPolicyNumberAndServiceStatusIgnoreCase.isEmpty()) {
			return new StatusDto("SUCCESS", message);
		}
		findByPolicyNumberAndServiceStatusIgnoreCase.forEach(policyDetails -> {
			PolicyServiceMatrixDto policyServiceMatrixDto = policyServiceMatrixService.getMatrix(currentService,
					policyDetails.getServiceType());
			listPolicyServiceMatrixDto.add(policyServiceMatrixDto);
		});
		int count = 0;
		for (PolicyServiceMatrixDto policyServiceMatrixDto : listPolicyServiceMatrixDto) {
			if (policyServiceMatrixDto.getIsAllowed().equalsIgnoreCase("No")) {
				count++;
				message = message+policyServiceMatrixDto.getOngoingService()+",";
			}
		}

		if (count > 0) {

			return new StatusDto("FAILED", message);
		} else {
			
			return new StatusDto("SUCCESS", message);
		}

	}

	@Override
	public ApiResponseDto<List<PolicyServiceDto>> getActiveServices(Long masterPolicyId, Long tmpPolicyId) {
		Long mpid = masterPolicyId;
		
		if (mpid == 0)
			mpid = policyTmpSearchRepository.findById(tmpPolicyId).get().getMasterPolicyId();
		
		List<PolicyServiceEntitiy> policyServiceEntitiies = policyServiceRepository.findByPolicyIdAndIsActive(mpid);
		
		return ApiResponseDto.success(policyServiceEntitiies.stream().map(this::entityToDto)
				.collect(Collectors.toList()));
		
	}
	
	private PolicyServiceDto entityToDto(PolicyServiceEntitiy entity) {
		return new ModelMapper().map(entity, PolicyServiceDto.class);
	}

	@Override
	public PremiumGstRefundDto getRefundablePremiumAndGST(Long masterPolicyId,
			LeavingMemberDto leavingMemberDto) {
		
		StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
		
		Date adjustmentDate = null;
		Double premiumCollected = 0D;
		Double gstCollected = 0D;
		Double premiumRefund = 0D;
		Double gstRefund = 0D;
		
		Long tmpPolicyId = getMemberEnroledTmpPolicyId(masterPolicyId, 
				leavingMemberDto.getMasterMemberId(), leavingMemberDto.getDateOfLeaving());
		
		if (tmpPolicyId == -1) {
			//TODO: look in history
		} else if (tmpPolicyId == 0) {
			MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findByIdAndIsActiveTrue(masterPolicyId);
			PolicyMemberEntity policyMemberEntity = policyMemberRepository.findById(leavingMemberDto.getMasterMemberId()).get();
			adjustmentDate = policyMemberEntity.getLastPremiumCollectedDate();
			
			premiumCollected += Double.valueOf(policyMemberEntity.getLifeCoverPremium());
			if (masterPolicyEntity.getGstApplicableId().equals(1L))
				gstCollected = 0D;
			else
				gstCollected += premiumCollected * (Float.parseFloat(standardCodeEntity.getValue()) / 100);
			premiumRefund = getRefundablePremium(leavingMemberDto.getDateOfLeaving(), masterPolicyEntity.getPolicyStartDate(), masterPolicyEntity.getPolicyEndDate(), policyMemberEntity.getLifeCoverPremium().floatValue());
			gstRefund += getRefundableGST(masterPolicyEntity.getGstApplicableId(), tmpPolicyId, premiumRefund, Float.parseFloat(standardCodeEntity.getValue()) / 100, adjustmentDate);
		} else {
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(tmpPolicyId).get();
			TempMemberEntity tempMemberEntity = tempMemberRepository.findByTmpPolicyIdAndMasterMemberId(
					renewalPolicyTMPEntity.getId(), leavingMemberDto.getMasterMemberId());
			adjustmentDate = tempMemberEntity.getLastPremiumCollectedDate();
			
			premiumCollected += Double.valueOf(tempMemberEntity.getLifeCoverPremium());
			if (renewalPolicyTMPEntity.getGstApplicableId().equals(1L))
				gstCollected = 0D;
			else
				gstCollected += premiumCollected * (Float.parseFloat(standardCodeEntity.getValue()) / 100);
			premiumRefund = getRefundablePremium(leavingMemberDto.getDateOfLeaving(), renewalPolicyTMPEntity.getPolicyStartDate(), renewalPolicyTMPEntity.getPolicyEndDate(), tempMemberEntity.getLifeCoverPremium());
			gstRefund += getRefundableGST(renewalPolicyTMPEntity.getGstApplicableId(), tmpPolicyId, premiumRefund, Float.parseFloat(standardCodeEntity.getValue()) / 100, adjustmentDate);
		}

		return PremiumGstRefundDto.builder()
			.premiumCollectedDate(adjustmentDate)
			.premiumCollected(premiumCollected)
			.gstCollected(gstCollected)
			.premiumRefunable(premiumRefund)
			.gstRefundable(gstRefund).build();
	}
	
	private Long getMemberEnroledTmpPolicyId(Long masterPolicyId, Long masterMemberId, Date dateOfLeaving) {
		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findByIdAndPeriod(masterPolicyId, dateOfLeaving);
		
		if (masterPolicyEntity == null) {
			return -1L;
		} else {
			List<PolicyServiceEntitiy> policyServiceEntities = policyServiceRepository.findAllInactiveServicesForPeriod(masterPolicyId, dateOfLeaving);
		
			if (policyServiceEntities.size() == 0) {
				//policy does not have any service - hence from new business
				return 0L;
			} else {
				for (PolicyServiceEntitiy policyServiceEntitiy : policyServiceEntities) {
					RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findByServiceId(policyServiceEntitiy.getId());
					TempMemberEntity tempMemberEntity = tempMemberRepository.findByTmpPolicyIdAndMasterMemberId(renewalPolicyTMPEntity.getId(), masterMemberId);
					
					if (tempMemberEntity != null) {
						return renewalPolicyTMPEntity.getId();
					}
				}
				//member was not added in any service - hence from new business
				return 0L;
			}
		}
	}
	
	private Double getRefundablePremium(Date dateOfLeaving, Date policyStartDate, Date policyEndDate, float premium) {
		// Policy start date
		Calendar policyStDate = new GregorianCalendar();
		policyStDate.setTime(policyStartDate);

		//Date of Leaving
		Calendar startDate = new GregorianCalendar();
		startDate.setTime(dateOfLeaving);
		startDate.set(Calendar.DATE, policyStDate.get(Calendar.DATE));
		
		// Policy end date
		Calendar endDate = new GregorianCalendar();
		endDate.setTime(policyEndDate);

		int diffInYear = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
		int diffInMonths = diffInYear * 12 - (startDate.get(Calendar.MONTH) - endDate.get(Calendar.MONTH));

		return Double.valueOf((premium / 12) * diffInMonths);
	}
	
	private Double getRefundableGST(Long GstApplicableId, Long tmpPolicyId, Double premium,
			Float gstRate, Date adjustmentDate) {
		if (GstApplicableId.equals(2L))
			return 0D;
		
		if (isInGSTCorrectionPeriod(adjustmentDate))
			return premium * gstRate;
		else
			return 0D;
	}
	
	private boolean isInGSTCorrectionPeriod(Date premiumCollectedDate) {
		Calendar premiumCollectedDay = Calendar.getInstance();
		premiumCollectedDay.setTime(premiumCollectedDate);
		
		Calendar previousFinancialStartDay = Calendar.getInstance();
		if (previousFinancialStartDay.get(Calendar.MONTH) < 3)
			previousFinancialStartDay.set(Calendar.YEAR, -1);
		previousFinancialStartDay.set(Calendar.MONTH, 3); //3 = April
		previousFinancialStartDay.set(Calendar.DATE, 1);
		
		Calendar correctionEndDate = Calendar.getInstance();
		correctionEndDate.set(Calendar.MONTH, 9); //9 = October - comparison is before
		correctionEndDate.set(Calendar.DATE, 1);
		
		Calendar today = Calendar.getInstance();
		
		if (premiumCollectedDay.after(previousFinancialStartDay)
				&& today.before(correctionEndDate)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * @Autowired private PolicyServicingCommonStatusRepository
	 * policyServicingCommonStatusRepository;
	 * 
	 * // @Autowired // private PolicyRepository policyRepository;
	 * 
	 * @Autowired private PolicyServicingMatRepository policyServicingMatRepository;
	 * 
	 * public synchronized String getSequence(String type) {
	 * logger.info("PolicyServicingCommonServiceImpl-getSequence-Start"); try {
	 * return policyServicingCommonRepository.getSequence(type); } catch
	 * (NonUniqueResultException e) {
	 * logger.error("PolicyServicingCommonServiceImpl-getSequence-Error:"); }
	 * logger.info("PolicyServicingCommonServiceImpl-getSequence-End"); return null;
	 * }
	 * 
	 * @Override public PolicyServiceStatusResponseDto policyServicingStatus() {
	 * PolicyServiceStatusResponseDto statusResponseDto = new
	 * PolicyServiceStatusResponseDto(); logger.
	 * info("PolicyServicingCommonServiceImpl --policyServicingStatus-- Start"); try
	 * { List<PolicyServiceStatusEntity> entity =
	 * policyServicingCommonStatusRepository.findAllByIsActiveTrue(); if
	 * (!entity.isEmpty()) { ArrayList<PolicyServiceStatusDto> commonStatusList =
	 * new ArrayList<>(); for (PolicyServiceStatusEntity commonStatus : entity) {
	 * PolicyServiceStatusDto statusDto = new PolicyServiceStatusDto();
	 * statusDto.setId(commonStatus.getId());
	 * statusDto.setCode(commonStatus.getCode());
	 * statusDto.setType(commonStatus.getType());
	 * statusDto.setDescription(commonStatus.getDescription());
	 * statusDto.setDescription1(commonStatus.getDescription1());
	 * statusDto.setName(commonStatus.getName());
	 * statusDto.setIsActive(commonStatus.getIsActive());
	 * commonStatusList.add(statusDto); }
	 * statusResponseDto.setResponseData(commonStatusList);
	 * statusResponseDto.setTransactionStatus(PolicyServicingCommonConstants.SUCCESS
	 * ); statusResponseDto.setTransactionMessage(PolicyServicingCommonConstants.
	 * RECORD_FOUND); } else {
	 * statusResponseDto.setTransactionStatus(PolicyServicingCommonConstants.ERROR);
	 * statusResponseDto.setTransactionMessage(PolicyServicingCommonConstants.
	 * NO_RECORD_FOUND); } } catch (NonUniqueResultException e) {
	 * logger.error("PolicyServicingCommonServiceImpl-policyServicingStatus-Error:")
	 * ;
	 * statusResponseDto.setTransactionStatus(PolicyServicingCommonConstants.ERROR);
	 * statusResponseDto.setTransactionMessage(PolicyServicingCommonConstants.
	 * INVALIDREQUEST); }
	 * logger.info("PolicyServicingCommonServiceImpl --policyServicingStatus-- End"
	 * ); return statusResponseDto; }
	 * 
	 * @Override public PolicyServiceCommonResponseDto
	 * getServiceDetailsByServiceId(Long serviceId) { PolicyServiceCommonResponseDto
	 * policyServiceResponse = new PolicyServiceCommonResponseDto(); try {
	 * logger.info(
	 * "PolicyServicingCommonServiceImpl:getServiceDetailsByServiceId---Started");
	 * Optional<PolicyServiceEntity> policyService =
	 * policyServicingCommonRepository.findById(serviceId); if
	 * (policyService.isPresent()) { PolicyServiceEntity policy =
	 * policyService.get(); PolicyServiceDto policyserviceDto =
	 * modelMapper.map(policy, PolicyServiceDto.class);
	 * policyServiceResponse.setPolicyServiceDto(policyserviceDto);
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * SUCCESS);
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * RECORD_FOUND); } else {
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * SUCCESS);
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * NO_RECORD_FOUND); logger.
	 * info("PolicyServicingCommonServiceImpl:getServiceDetailsByServiceId--NO data found"
	 * ); } } catch (IllegalArgumentException e) { logger.info(
	 * "PolicyServicingCommonServiceImpl:getServiceDetailsByServiceId--ERROR--", e);
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * ERROR);
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * FAIL); } finally { logger.info(
	 * "PolicyServicingCommonServiceImpl:getServiceDetailsByServiceId--Ends"); }
	 * return policyServiceResponse; }
	 * 
	 * @Override public PolicyServiceCommonResponseDto
	 * getServiceDetailsByPolicyId(Long policyId) { PolicyServiceCommonResponseDto
	 * policyServiceResponse = new PolicyServiceCommonResponseDto(); try {
	 * logger.info(
	 * "PolicyServicingCommonServiceImpl:getServiceDetailsByPolicyId---Started");
	 * Optional<PolicyServiceEntity> policyService =
	 * policyServicingCommonRepository.findByPolicyId(policyId); if
	 * (policyService.isPresent()) { PolicyServiceEntity policy =
	 * policyService.get(); PolicyServiceDto policyserviceDto =
	 * modelMapper.map(policy, PolicyServiceDto.class);
	 * policyServiceResponse.setPolicyServiceDto(policyserviceDto);
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * SUCCESS);
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * RECORD_FOUND); } else {
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * SUCCESS);
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * NO_RECORD_FOUND); logger.
	 * info("PolicyServicingCommonServiceImpl:getServiceDetailsByPolicyId--NO data found"
	 * ); } } catch (IllegalArgumentException e) { logger.info(
	 * "PolicyServicingCommonServiceImpl:getServiceDetailsByPolicyId--ERROR--", e);
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * ERROR);
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * FAIL);
	 * 
	 * } finally { logger.info(
	 * "PolicyServicingCommonServiceImpl:getServiceDetailsByPolicyId--Ends"); }
	 * return policyServiceResponse; }
	 * 
	 * @Override public PolicyServiceCommonResponseDto endService1(Long policyId,
	 * Long serviceId) { PolicyServiceCommonResponseDto policyServiceResponse = new
	 * PolicyServiceCommonResponseDto(); try { logger.info(
	 * "PolicyServicingCommonServiceImpl:getServiceDetailsByPolicyId---Started");
	 * Optional<PolicyServiceEntity> policyService = policyServicingCommonRepository
	 * .findByPolicyIdAndServiceId(policyId, serviceId); PolicyServiceEntity
	 * policyserviceEntity; if (policyService.isPresent()) { PolicyServiceEntity
	 * policy = policyService.get(); PolicyServiceEntity policyservicee =
	 * modelMapper.map(policy, PolicyServiceEntity.class);
	 * policyservicee.setIsUsing(false);
	 * policyservicee.setServiceStatus(PolicyServicingCommonConstants.
	 * SERVICE_INACTIVE_NO);
	 * 
	 * policyserviceEntity = policyServicingCommonRepository.save(policyservicee);
	 * 
	 * PolicyServiceDto policyserviceDto = modelMapper.map(policyserviceEntity,
	 * PolicyServiceDto.class);
	 * 
	 * policyServiceResponse.setPolicyServiceDto(policyserviceDto);
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * SUCCESS);
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * RECORD_FOUND); } else {
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * SUCCESS);
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * NO_RECORD_FOUND); logger.
	 * info("PolicyServicingCommonServiceImpl:getServiceDetailsByPolicyId--NO data found"
	 * ); } } catch (IllegalArgumentException e) { logger.info(
	 * "PolicyServicingCommonServiceImpl:getServiceDetailsByPolicyId--ERROR--", e);
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * ERROR);
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * FAIL);
	 * 
	 * } finally { logger.info(
	 * "PolicyServicingCommonServiceImpl:getServiceDetailsByPolicyId--Ends"); }
	 * return policyServiceResponse; }
	 * 
	 * @Override public PolicyServiceCommonResponseDto startService(PolicyServiceDto
	 * policyServiceDto, String serviceType) { PolicyServiceCommonResponseDto
	 * policyServiceResponse = new PolicyServiceCommonResponseDto(); try {
	 * PolicyServiceEntity policyServiceEntity = new PolicyServiceEntity();
	 * PolicyServiceEntity finalpolicyServiceEntity = new PolicyServiceEntity(); if
	 * (policyServiceDto.getPolicyId() == null) {
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * POLICY_NUMBER_EMPTY);
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * FAIL); } else { // Optional<PolicyEntity> policyEntity =
	 * policyRepository.findById(policyServiceDto.getPolicyId()); // if
	 * (policyEntity.isPresent()) { switch (serviceType) { case
	 * PolicyServicingCommonConstants.POLICY_CONVERSION_TYPE: policyServiceEntity
	 * .setServiceNumber(getSequence(PolicyServicingCommonConstants.
	 * POLICY_CONVERSION_TYPE)); policyServiceEntity.setIsUsing(true); break; case
	 * PolicyServicingCommonConstants.POLICY_MERGER_TYPE: policyServiceEntity
	 * .setServiceNumber(getSequence(PolicyServicingCommonConstants.
	 * POLICY_MERGER_TYPE)); policyServiceEntity.setIsUsing(true); break; case
	 * PolicyServicingCommonConstants.FREELOOK_CANCEL_TYPE: policyServiceEntity
	 * .setServiceNumber(getSequence(PolicyServicingCommonConstants.
	 * FREELOOK_CANCEL_TYPE)); policyServiceEntity.setIsUsing(true); break; case
	 * PolicyServicingCommonConstants.POLICY_DETAILS_CHANGE_TYPE:
	 * policyServiceEntity
	 * .setServiceNumber(getSequence(PolicyServicingCommonConstants.
	 * POLICY_DETAILS_CHANGE_TYPE)); policyServiceEntity.setIsUsing(true); break;
	 * case PolicyServicingCommonConstants.MEMBER_ADDITION_TYPE: policyServiceEntity
	 * .setServiceNumber(getSequence(PolicyServicingCommonConstants.
	 * MEMBER_ADDITION_TYPE)); policyServiceEntity.setIsUsing(true); break; case
	 * PolicyServicingCommonConstants.MEMBER_TRASFER_IN_OUT_TYPE:
	 * policyServiceEntity
	 * .setServiceNumber(getSequence(PolicyServicingCommonConstants.
	 * MEMBER_TRASFER_IN_OUT_TYPE)); policyServiceEntity.setIsUsing(true); break;
	 * default: policyServiceEntity.setServiceNumber(getSequence("SERVICE"));
	 * policyServiceEntity.setIsUsing(true); break; } PolicyServiceEntity
	 * policyServiceEntityy = modelMapper.map(policyServiceDto,
	 * PolicyServiceEntity.class);
	 * policyServiceEntityy.setServiceNumber(policyServiceEntity.getServiceNumber())
	 * ; policyServiceEntityy.setServiceStatus(PolicyServicingCommonConstants.
	 * SERVICE_ACTIVE_NO);
	 * policyServiceEntityy.setIsUsing(policyServiceEntity.getIsUsing());
	 * mapDtoToEntity(policyServiceEntity, policyServiceEntityy);
	 * finalpolicyServiceEntity =
	 * policyServicingCommonRepository.save(policyServiceEntity); // } else { //
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * POLICY_INVALID); //
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * FAIL); // } } PolicyServiceDto finalpolicyServiceDto =
	 * modelMapper.map(finalpolicyServiceEntity, PolicyServiceDto.class);
	 * policyServiceResponse.setPolicyServiceDto(finalpolicyServiceDto);
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * SUCCESS);
	 * policyServiceResponse.setTransactionMessage(PolicyServicingCommonConstants.
	 * FETCH_MESSAGE); } catch (IllegalArgumentException e) {
	 * policyServiceResponse.setTransactionStatus(PolicyServicingCommonConstants.
	 * FAIL); policyServiceResponse.setTransactionMessage(e.getMessage()); }
	 * logger.info("PolicyLevelMergerServieImpl:getServiceDetailsByServiceId:Start")
	 * ; return policyServiceResponse; }
	 * 
	 * public PolicyServiceEntity mapDtoToEntity(PolicyServiceEntity
	 * policyServiceEntity, PolicyServiceEntity policyServiceDto) {
	 * policyServiceEntity.setServiceId(policyServiceDto.getServiceId());
	 * policyServiceEntity.setServiceNumber(policyServiceDto.getServiceNumber());
	 * policyServiceEntity.setServiceType(policyServiceDto.getServiceType());
	 * policyServiceEntity.setServiceDoneBy(policyServiceDto.getServiceDoneBy());
	 * policyServiceEntity.setServiceEffectiveDate(policyServiceDto.
	 * getServiceEffectiveDate());
	 * policyServiceEntity.setRequestReceivedBy(policyServiceDto.
	 * getRequestReceivedBy());
	 * policyServiceEntity.setRequestReceivedDate(policyServiceDto.
	 * getRequestReceivedDate());
	 * policyServiceEntity.setServiceStatus(policyServiceDto.getServiceStatus());
	 * policyServiceEntity.setWorkflowStatus(policyServiceDto.getWorkflowStatus());
	 * policyServiceEntity.setPolicyId(policyServiceDto.getPolicyId());
	 * policyServiceEntity.setIsUsing(policyServiceDto.getIsUsing());
	 * policyServiceEntity.setIsActive(policyServiceDto.getIsActive());
	 * policyServiceEntity.setUnitCode(policyServiceDto.getUnitCode());
	 * policyServiceEntity.setCreatedBy(policyServiceDto.getCreatedBy());
	 * policyServiceEntity.setCreatedOn(policyServiceDto.getCreatedOn());
	 * policyServiceEntity.setModifiedBy(policyServiceDto.getModifiedBy());
	 * policyServiceEntity.setModifiedOn(policyServiceDto.getModifiedOn()); return
	 * policyServiceEntity; }
	 * 
	 * @Override public PolicyServiceCommonResponseDto checkService(Long policyId,
	 * String newServiceType) { PolicyServiceCommonResponseDto response = new
	 * PolicyServiceCommonResponseDto(); try { // Optional<PolicyServiceEntity>
	 * policyOpt =
	 * policyServicingCommonRepository.findByPolicyIdAndServiceStatusAndIsActiveTrue
	 * (policyId, "ACTIVE"); // if (policyOpt.isPresent()) { // PolicyServiceEntity
	 * polSerentity = policyOpt.get(); // response =
	 * checkServiceMap(polSerentity.getServiceType(),newServiceType); // }
	 * 
	 * List<PolicyServiceEntity> policyOpt = policyServicingCommonRepository
	 * .findByPolicyIdAndServiceStatusAndIsActiveTrueOrderByServiceIdAsc(policyId,
	 * "ACTIVE"); if (policyOpt != null) { for (PolicyServiceEntity
	 * policyServiceEntity : policyOpt) { response = checkServiceMap(policyId,
	 * policyServiceEntity.getServiceType(), newServiceType); } }
	 * 
	 * else { response.setAllowList(null); response.setAllow("yes");
	 * response.setTransactionMessage("Servicing can proceed");
	 * response.setTransactionStatus("SUCCESS"); }
	 * 
	 * } catch (Exception e) {
	 * response.setTransactionStatus(PolicyServicingCommonConstants.FAIL);
	 * response.setTransactionMessage(e.getMessage()); } return response; }
	 * 
	 * private PolicyServiceCommonResponseDto checkServiceMap(Long policyId, String
	 * serviceType, String newServiceType) { PolicyServiceCommonResponseDto response
	 * = new PolicyServiceCommonResponseDto();
	 * 
	 * String yes = "yes";
	 * 
	 * List<Object> check = policyServicingMatRepository.getServiceMap(policyId,
	 * yes); response.setAllowList(check);
	 * 
	 * // PolicyServiceMatrixEntity matrixList =
	 * policyServicingMatRepository.findByServiceTypeAndServiceStatusAndIsActiveTrue
	 * (serviceType, "ACTIVE"); // // if(matrixList != null) { // switch
	 * (serviceType) { // case
	 * PolicyServicingCommonConstants.POLICY_CONVERSION_TYPE: // if
	 * (Objects.equals(serviceType, matrixList.getServiceType())) { //
	 * response.setAllowList(matrixList); // response.setAllow("no"); // } // break;
	 * // case PolicyServicingCommonConstants.POLICY_MERGER_TYPE: // if
	 * (Objects.equals(serviceType, matrixList.getServiceType())) { //
	 * response.setAllowList(matrixList); // response.setAllow("no"); // } // break;
	 * // case PolicyServicingCommonConstants.FREELOOK_CANCEL_TYPE: // if
	 * (Objects.equals(serviceType, matrixList.getServiceType())) { //
	 * response.setAllowList(matrixList); // response.setAllow("no"); // } // break;
	 * // case PolicyServicingCommonConstants.POLICY_DETAILS_CHANGE_TYPE: // if
	 * (Objects.equals(serviceType, matrixList.getServiceType()) &&
	 * Objects.equals(newServiceType,
	 * PolicyServicingCommonConstants.MEMBER_ADDITION_TYPE)) { //
	 * response.setAllowList(matrixList); // response.setAllow("yes"); // } else {
	 * // response.setAllowList(matrixList); // response.setAllow("no"); // } //
	 * break; // case PolicyServicingCommonConstants.MEMBER_ADDITION_TYPE: // if
	 * (Objects.equals(serviceType, matrixList.getServiceType()) &&
	 * Objects.equals(newServiceType,
	 * PolicyServicingCommonConstants.POLICY_DETAILS_CHANGE_TYPE)) { //
	 * response.setAllowList(matrixList); // response.setAllow("yes"); // } else {
	 * // response.setAllowList(matrixList); // response.setAllow("no"); // } //
	 * break; // case PolicyServicingCommonConstants.MEMBER_TRASFER_IN_OUT_TYPE: //
	 * if (Objects.equals(serviceType, matrixList.getServiceType()) &&
	 * Objects.equals(newServiceType, PolicyServicingCommonConstants.CLAIM_TYPE) &&
	 * Objects.equals(newServiceType,
	 * PolicyServicingCommonConstants.MEMBER_ADDITION_TYPE)) { //
	 * response.setAllowList(matrixList); // response.setAllow("yes"); // } else {
	 * // response.setAllowList(matrixList); // response.setAllow("no"); // } //
	 * break; // case PolicyServicingCommonConstants.CLAIM_TYPE: // if
	 * (Objects.equals(serviceType, matrixList.getServiceType()) &&
	 * Objects.equals(newServiceType,
	 * PolicyServicingCommonConstants.MEMBER_ADDITION_TYPE) &&
	 * Objects.equals(newServiceType,
	 * PolicyServicingCommonConstants.MEMBER_TRASFER_IN_OUT_TYPE)) { //
	 * response.setAllowList(matrixList); // response.setAllow("yes"); // } else {
	 * // response.setAllowList(matrixList); // response.setAllow("no"); // } //
	 * break; // default: // if (Objects.equals(serviceType,
	 * matrixList.getServiceType())) { // response.setAllowList(matrixList); //
	 * response.setAllow("yes"); // } // break; // } // }else { //
	 * response.setAllow("yes"); //
	 * response.setTransactionMessage("Servicing can proceed"); //
	 * response.setTransactionStatus("SUCCESS"); // } // if
	 * ("yes".equals(response.getAllow())) { //
	 * response.setServiceType(serviceType); // response.setAllowList(matrixList);
	 * // response.setAllow("yes"); //
	 * response.setTransactionMessage("Servicing can proceed"); //
	 * response.setTransactionStatus("SUCCESS"); // return response; // } else if
	 * ("no".equals(response.getAllow())) { // response.setServiceType(serviceType);
	 * // response.setAllowList(matrixList); // response.setAllow("no"); //
	 * response.setTransactionMessage("Already Used in Another Servicing"); //
	 * response.setTransactionStatus("SUCCESS"); // return response; // } return
	 * response; }
	 * 
	 * @Override public PolicyServiceCommonResponseDto
	 * initiateService(PolicyServiceDto policyServiceDto, String serviceType) {
	 * PolicyServiceCommonResponseDto response = new
	 * PolicyServiceCommonResponseDto(); try {
	 * 
	 * PolicyServiceEntity entity = new PolicyServiceEntity();
	 * 
	 * entity.setServiceId(null); entity.setServiceNumber(getSequence(serviceType));
	 * entity.setServiceType(serviceType);
	 * entity.setPolicyId(policyServiceDto.getPolicyId());
	 * entity.setServiceStatus("ACTIVE");
	 * entity.setWorkflowStatus(policyServiceDto.getWorkflowStatus());
	 * entity.setIsActive(Boolean.TRUE);
	 * entity.setUnitCode(policyServiceDto.getUnitCode());
	 * entity.setServiceDoneBy(policyServiceDto.getServiceDoneBy());
	 * entity.setServiceEffectiveDate(new Date());
	 * entity.setRequestReceivedBy(policyServiceDto.getRequestReceivedBy());
	 * entity.setRequestReceivedDate(new Date());
	 * entity.setCreatedBy(policyServiceDto.getCreatedBy()); entity.setCreatedOn(new
	 * Date()); entity.setModifiedBy(policyServiceDto.getModifiedBy());
	 * entity.setModifiedOn(new Date());
	 * 
	 * policyServicingCommonRepository.save(entity);
	 * 
	 * PolicyServiceDto dto = modelMapper.map(entity, PolicyServiceDto.class);
	 * 
	 * response.setServiceId(dto.getServiceId());
	 * response.setServiceType(dto.getServiceType());
	 * response.setPolicyId(dto.getPolicyId()); response.setPolicyServiceDto(dto);
	 * response.setTransactionMessage("Service Initiated Successfully");
	 * response.setTransactionStatus("SUCCESS");
	 * 
	 * } catch (Exception e) {
	 * response.setTransactionStatus(PolicyServicingCommonConstants.FAIL);
	 * response.setTransactionMessage(e.getMessage()); } return response; }
	 * 
	 * @Override public PolicyServiceCommonResponseDto endService(PolicyServiceDto
	 * policyServiceDto, String serviceType) { PolicyServiceCommonResponseDto
	 * response = new PolicyServiceCommonResponseDto(); try { PolicyServiceEntity
	 * entity =
	 * policyServicingCommonRepository.findByPolicyIdAndServiceIdAndIsActiveTrue(
	 * policyServiceDto.getPolicyId(), policyServiceDto.getServiceId()); if (entity
	 * != null) { entity.setServiceStatus("INACTIVE");
	 * entity.setWorkflowStatus(policyServiceDto.getWorkflowStatus());
	 * entity.setIsActive(Boolean.FALSE);
	 * entity.setModifiedBy(policyServiceDto.getModifiedBy());
	 * entity.setModifiedOn(new Date());
	 * 
	 * policyServicingCommonRepository.save(entity);
	 * 
	 * PolicyServiceDto dto = modelMapper.map(entity, PolicyServiceDto.class);
	 * 
	 * response.setServiceId(dto.getServiceId());
	 * response.setServiceType(dto.getServiceType());
	 * response.setPolicyId(dto.getPolicyId()); response.setPolicyServiceDto(dto);
	 * response.setTransactionMessage("Service Initiated Successfully");
	 * response.setTransactionStatus("SUCCESS");
	 * 
	 * } else {
	 * 
	 * response.setServiceId(policyServiceDto.getServiceId());
	 * response.setServiceType(policyServiceDto.getServiceType());
	 * response.setPolicyId(policyServiceDto.getPolicyId());
	 * response.setTransactionMessage("No Service Found ");
	 * response.setTransactionStatus("FAIL"); } } catch (Exception e) {
	 * response.setTransactionStatus(PolicyServicingCommonConstants.FAIL);
	 * response.setTransactionMessage(e.getMessage()); } return null; }
	 * 
	 * @Override public PolicyServiceCommonResponseDto
	 * generateServiceId(PolicyServiceDto policyLevelServiceDto) {
	 * logger.info("PolicyLevelMergerServieImpl:getServiceDetailsByServiceId:Start")
	 * ; PolicyServiceCommonResponseDto policyLevelMergerApiResponse = new
	 * PolicyServiceCommonResponseDto(); try {
	 * 
	 * PolicyServiceEntity newpolicyLevelServiceEntity = new PolicyServiceEntity();
	 * newpolicyLevelServiceEntity.setRequestReceivedDate(policyLevelServiceDto.
	 * getRequestReceivedDate()); newpolicyLevelServiceEntity.setServiceNumber(
	 * policyServicingCommonRepository.getSequence(policyLevelServiceDto.
	 * getServiceType()));
	 * newpolicyLevelServiceEntity.setPolicyId(policyLevelServiceDto.getPolicyId());
	 * newpolicyLevelServiceEntity.setServiceEffectiveDate(policyLevelServiceDto.
	 * getServiceEffectiveDate());
	 * newpolicyLevelServiceEntity.setServiceType(policyLevelServiceDto.
	 * getServiceType());
	 * newpolicyLevelServiceEntity.setCreatedBy(policyLevelServiceDto.getCreatedBy()
	 * ); // newpolicyLevelServiceEntity.setCreatedOn(DateUtils.sysDate());
	 * newpolicyLevelServiceEntity.setIsActive(true);
	 * newpolicyLevelServiceEntity.setServiceDoneBy(policyLevelServiceDto.
	 * getServiceDoneBy());
	 * newpolicyLevelServiceEntity.setServiceStatus(PolicyServicingCommonConstants.
	 * SERVICE_ACTIVE);
	 * newpolicyLevelServiceEntity.setWorkflowStatus(PolicyServicingCommonConstants.
	 * SERVICE_ACTIVE);
	 * 
	 * newpolicyLevelServiceEntity =
	 * policyServicingCommonRepository.save(newpolicyLevelServiceEntity);
	 * PolicyServiceDto newPolicyServiceDto =
	 * modelMapper.map(newpolicyLevelServiceEntity, PolicyServiceDto.class);
	 * policyLevelMergerApiResponse.setPolicyServiceDto(newPolicyServiceDto);
	 * policyLevelMergerApiResponse.setTransactionStatus(
	 * PolicyServicingCommonConstants.SUCCESS);
	 * policyLevelMergerApiResponse.setTransactionMessage(
	 * PolicyServicingCommonConstants.SUCCESS_RETRIVE_MSG);
	 * 
	 * } catch (IllegalArgumentException e) {
	 * policyLevelMergerApiResponse.setTransactionStatus(
	 * PolicyServicingCommonConstants.FAIL);
	 * policyLevelMergerApiResponse.setTransactionMessage(e.getMessage()); }
	 * logger.info("PolicyLevelMergerServieImpl:getServiceDetailsByServiceId:Start")
	 * ; return policyLevelMergerApiResponse; }
	 */
}
