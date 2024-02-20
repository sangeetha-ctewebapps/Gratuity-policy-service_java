package com.lic.epgs.gratuity.policyservices.conversion.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberAddressRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberAppointeeRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberBankAccountRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.policy.renewal.repository.PolicyRenewalRemainderRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.QuotationRenewalDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
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
import com.lic.epgs.gratuity.policyservices.common.enums.PolicyServiceName;
import com.lic.epgs.gratuity.policyservices.conversion.repository.PsTempMemberNomineeEntityRepository;
import com.lic.epgs.gratuity.policyservices.conversion.service.AddIndividualMemberService;

@Service
public class AddIndividualMemberServiceImpl implements AddIndividualMemberService {

	private static Long quotationStatusId = 16l;
	private static Long quotationSubStatusId = 21l;
	private static Long quotationTaggedStatusId = 78l;

	@Autowired
	private TempMemberRepository tempMemberRepository;
	@Autowired
	private TempMemberAddressRepository tempMemberAddressRepository;
	@Autowired
	private TempMemberAppointeeRepository tempMemberAppointeeRepository;
	@Autowired
	private TempMemberBankAccountRepository tempMemberBankAccountRepository;
	@Autowired
	private PsTempMemberNomineeEntityRepository psTempMemberNomineeEntityRepository;

	@Autowired
	private PolicyServiceRepository policyServiceRepository;

	@Autowired
	private PolicyRenewalRemainderRepository policyRenewalRemainderRepository;

	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;

	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

	@Autowired
	private TempMPHRepository tempMPHRepository;

	@Autowired
	private PolicySchemeRuleRepository policySchemeRuleRepository;

	@Autowired
	private RenewalSchemeruleTMPRepository renewalSchemeruleTMPRepository;

	@Autowired
	private MemberCategoryRepository memberCategoryRepository;

	@Autowired
	private PolicyLifeCoverRepository policyLifeCoverRepository;

	@Autowired
	private RenewalLifeCoverTMPRepository renewalLifeCoverTMPRepository;

	@Autowired
	private RenewalGratuityBenefitTMPRepository renewalGratuityBenefitTMPRepository;

	@Autowired
	private RenewalValuationTMPRepository renewalValuationTMPRepository;

	@Autowired
	private PolicyValuationMatrixRepository policyValuationMatrixRepository;

	@Autowired
	private PolicyGratuityBenefitRepository policyGratuityBenefitRepository;

	@Autowired
	private PolicyMasterValuationRepository policyMasterValuationRepository;

	@Autowired
	private RenewalValuationMatrixTMPRepository renewalValuationMatrixTMPRepository;

	@Autowired
	private PolicyValuationBasicRepository policyValuationBasicRepository;

	@Autowired
	private RenewalValuationBasicTMPRepository renewalValuationBasicTMPRepository;

	@Autowired
	private PolicyValuationWithdrawalRateRepository policyValuationWithdrawalRateRepository;

	@Autowired
	private RenewalValuationWithdrawalTMPRepository renewalValuationWithdrawalTMPRepository;

	@Autowired
	private MPHRepository mPHRepository;

	@Autowired
	MemberCategoryModuleRepository memberCategoryModuleRepository;

	@Override
	public TempMemberEntity saveTempMember(TempMemberEntity tempMember, Long policyId, Long tempPolicyId) {

		if (tempMember.getId() != null) {
			Optional<TempMemberEntity> optionalTeamMember = tempMemberRepository.findById(tempPolicyId);
			TempMemberEntity tempMemberEntity = optionalTeamMember.get();
			return tempMemberRepository.save(tempMemberEntity);
		} else {

			tempMember.setPolicyId(policyId);
			tempMember.setTmpPolicyId(tempPolicyId);
			tempMember.setCreatedDate(new Date());
			tempMember.setModifiedDate(new Date());
			tempMember.setStatusId(146L);
			tempMember.setIsActive(true);
			return tempMemberRepository.save(tempMember);
		}

	}

	@Override
	public TempMemberAddressEntity saveTempMemberAddress(TempMemberAddressEntity memberAddress, Long memberId,
			Long tempPolicyId) {

		Optional<TempMemberEntity> optionalTeamMember = tempMemberRepository.findById(memberId);

		TempMemberEntity tempMemberEntity = null;
		if (optionalTeamMember.isPresent())
			tempMemberEntity = optionalTeamMember.get();

		memberAddress.setTempMember(tempMemberEntity);
		memberAddress.setModifiedDate(new Date());
		if (memberAddress.getId() == null)
			memberAddress.setCreatedDate(new Date());
		return tempMemberAddressRepository.save(memberAddress);
	}

	@Override
	public TempMemberAppointeeEntity saveTempMemberAppointee(TempMemberAppointeeEntity tempMemberAppointee,
			Long memberId, Long tempPolicyId) {
		Optional<TempMemberEntity> optionalTeamMember = tempMemberRepository.findById(memberId);
		try {
			TempMemberEntity tempMemberEntity = null;
			if (optionalTeamMember.isPresent())
				tempMemberEntity = optionalTeamMember.get();

			tempMemberAppointee.setTempMember(tempMemberEntity);
			tempMemberAppointee.setModifiedDate(new Date());
			if (tempMemberAppointee.getId() == null)
				tempMemberAppointee.setCreatedDate(new Date());

			TempMemberAppointeeEntity tempMemberAppointeeEntity = tempMemberAppointeeRepository
					.save(tempMemberAppointee);
			System.out.println(tempMemberAppointeeEntity);
			return tempMemberAppointeeEntity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TempMemberBankAccountEntity saveTempMemberBankAccount(TempMemberBankAccountEntity tempMemberBankAccount,
			Long memberId, Long tempPolicyId) {
		Optional<TempMemberEntity> optionalTeamMember = tempMemberRepository.findById(memberId);
		try {
			TempMemberEntity tempMemberEntity = null;
			if (optionalTeamMember.isPresent())
				tempMemberEntity = optionalTeamMember.get();

			tempMemberBankAccount.setTempMember(tempMemberEntity);
			tempMemberBankAccount.setModifiedDate(new Date());
			if (tempMemberBankAccount.getId() == null)
				tempMemberBankAccount.setCreatedDate(new Date());

			return tempMemberBankAccountRepository.save(tempMemberBankAccount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TempMemberNomineeEntity saveTempMemberNominee(TempMemberNomineeEntity tempMemberNominee, Long memberId,
			Long tempPolicyId) {
		Optional<TempMemberEntity> optionalTeamMember = tempMemberRepository.findById(memberId);
		try {
			TempMemberEntity tempMemberEntity = null;
			if (optionalTeamMember.isPresent())
				tempMemberEntity = optionalTeamMember.get();

			tempMemberNominee.setTempMember(tempMemberEntity);
			tempMemberNominee.setModifiedDate(new Date());
			if (tempMemberNominee.getId() == null)
				tempMemberNominee.setCreatedDate(new Date());
			return psTempMemberNomineeEntityRepository.save(tempMemberNominee);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	@Override
//	public ApiResponseDto<RenewalPolicyTMPDto> CreateRenewalforQuotation(QuotationRenewalDto quotationRenewalDto) {
//
//		if (policyServiceRepository
//				.findByPolicyandType(quotationRenewalDto.getPolicyId(), PolicyServiceName.MEMBER_ADDITION.getName())
//				.size() > 0) {
//			return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
//		}else {
//	
////			PolicyRenewalRemainderEntity updateStatus= policyRenewalRemainderRepository.findBypolicyId(quotationRenewalDto.getPolicyId());
////	updateStatus.setIsActive(false);
////	policyRenewalRemainderRepository.save(updateStatus);
//		PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
//
//		policyServiceEntitiy.setServiceType(PolicyServiceName.MEMBER_ADDITION.getName());
//		policyServiceEntitiy.setPolicyId(quotationRenewalDto.getPolicyId());
//		policyServiceEntitiy.setCreatedBy(quotationRenewalDto.getCreatedBy());
//		policyServiceEntitiy.setCreatedDate(new Date());
//		policyServiceEntitiy.setIsActive(true);
//		policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);
//
//		MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(quotationRenewalDto.getPolicyId());
//	
//		RenewalPolicyTMPEntity renewalPolicyTMPEntity = RenewalPolicyTMPHelper.pmsttoTmp(masterPolicyEntity);
//		renewalPolicyTMPEntity.setId(null);
//		renewalPolicyTMPEntity.setPolicyStatusId(null);	
//		renewalPolicyTMPEntity.setPolicySubStatusId(null);
//		renewalPolicyTMPEntity.setPolicytaggedStatusId(null);
//		renewalPolicyTMPEntity.setQuotationStatusId(quotationStatusId);
//		renewalPolicyTMPEntity.setQuotationSubStatusId(quotationSubStatusId);
//		renewalPolicyTMPEntity.setQuotationTaggedStatusId(quotationTaggedStatusId);
//		renewalPolicyTMPEntity.setMasterPolicyId(quotationRenewalDto.getPolicyId());
//		renewalPolicyTMPEntity.setQuotationDate(quotationRenewalDto.getQuotationDate());
//		renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
//		renewalPolicyTMPEntity.setIsActive(true);
//		renewalPolicyTMPEntity.setCreatedBy(quotationRenewalDto.getCreatedBy());
//		renewalPolicyTMPEntity.setCreatedDate(new Date());
//		renewalPolicyTMPEntity.setQuotationNumber(
//				RenewalPolicyTMPHelper.nextQuotationNumber(renewalPolicyTMPRepository.maxQuotationNumber()).toString());
//
//		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
//		
//		MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
//		TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytomastertoTemp(renewalPolicyTMPEntity.getId(),
//				mPHEntity);
//		tempMPHEntity=	tempMPHRepository.save(tempMPHEntity);
//		renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
//		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
//		// renewalPolicyTMPEntity.getId()
//
//		// scheme MastrePolicy to TMp copy
//
//		Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
//				.findBypolicyId(quotationRenewalDto.getPolicyId());
//
//		if (policySchemeEntity.isPresent()) {
//
//			RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = RenewalSchemeruleTMPHelper
//					.pmsttoTmp(policySchemeEntity.get());
//			renewalSchemeruleTMPEntity.setId(null);
//			renewalSchemeruleTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
//			renewalSchemeruleTMPEntity.setPmstSchemeRuleId(policySchemeEntity.get().getId());
//
//			renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);
//		}
//		List<MemberCategoryEntity> addMemberCategoryEntity = new ArrayList<MemberCategoryEntity>();
//		List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
//				.findBypmstPolicyId(quotationRenewalDto.getPolicyId());
//		for (MemberCategoryEntity getmemberCategoryEntity : memberCategoryEntity) {
//			getmemberCategoryEntity.setPmstTmpPolicy(renewalPolicyTMPEntity.getId());
//			addMemberCategoryEntity.add(getmemberCategoryEntity);
//		}
//		memberCategoryRepository.saveAll(addMemberCategoryEntity);
//
//		
//		
//		List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
//				.findByPolicyId(quotationRenewalDto.getPolicyId());
//		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity =PolicyClaimCommonHelper.copyToTmpLifeCoverforClaim(policyLifeCoverEntity,memberCategoryEntity,renewalPolicyTMPEntity.getId());
//		
//		renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
////		policyLifeCoverRepository.updateISActive(quotationRenewalDto.getPolicyId());
//		// member category
//
//	
//
//		// Gratuity
//	
//		List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
//				.findBypolicyId(quotationRenewalDto.getPolicyId());
//		List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper.copyToTmpGratuityforClaim(policyGratuityBenefitEntity,memberCategoryEntity,renewalPolicyTMPEntity.getId());
//	
//		renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
////		policyGratuityBenefitRepository.updateIsActive(quotationRenewalDto.getPolicyId());
//
//		// valuation
//		Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
//				.findByPolicyId(quotationRenewalDto.getPolicyId());
//		if (policyValuationEntity.isPresent()) {
//			RenewalValuationTMPEntity renewalValuationTMPEntity = RenewalValuationTMPHelper
//					.pmsttoTmp(policyValuationEntity.get());
//			renewalValuationTMPEntity.setId(null);
//			renewalValuationTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
//			renewalValuationTMPEntity.setPmstValuationId(policyValuationEntity.get().getId());
//			renewalValuationTMPRepository.save(renewalValuationTMPEntity);
//		}
//
//		// valuation Matrix
//		Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
//				.findByPolicyId(quotationRenewalDto.getPolicyId());
//		if (policyValuationMatrixEntity.isPresent()) {
//			RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = RenewalValuationMatrixTMPHelper
//					.pmsttoTmp(policyValuationMatrixEntity.get());
//			renewalValuationMatrixTMPEntity.setId(null);
//			renewalValuationMatrixTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
//			renewalValuationMatrixTMPEntity.setPmstValuationMatrixId(policyValuationMatrixEntity.get().getId());
//			renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);
//
//			if (renewalValuationMatrixTMPEntity.getValuationTypeId() == 1) {
//				Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
//						.findByPolicyId(quotationRenewalDto.getPolicyId());
//				if (policyValutationBasicEntity.isPresent()) {
//					RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = RenewalValuationBasicTMPHelper
//							.pmsttoTmp(policyValutationBasicEntity.get());
//					renewalValuationBasicTMPEntity.setId(null);
////					renewalValuationBasicTMPEntity.setValuationTypeId(renewalValuationTypeId);
//					renewalValuationBasicTMPEntity.setPmstValuationBasicId(policyValutationBasicEntity.get().getId());
//					renewalValuationBasicTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
//					renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);
//
//					// ValuationWithdrawalRate table
//					List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
//							.findByPolicyId(quotationRenewalDto.getPolicyId());
//					if (policyValuationWithdrawalRateEntity.size() > 0) {
//						List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = new ArrayList<RenewalValuationWithdrawalTMPEntity>();
//						for (PolicyValuationWithdrawalRateEntity getpolicyValuationWithdrawalRateEntity : policyValuationWithdrawalRateEntity) {
//							RenewalValuationWithdrawalTMPEntity newrenewalValuationWithdrawalTMPEntity = RenewalValuationTMPHelper
//									.pmsttoTmp(getpolicyValuationWithdrawalRateEntity);
//							newrenewalValuationWithdrawalTMPEntity.setId(null);
//							newrenewalValuationWithdrawalTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
//							newrenewalValuationWithdrawalTMPEntity
//									.setPmstValWithDrawalId(getpolicyValuationWithdrawalRateEntity.getId());
//							renewalValuationWithdrawalTMPEntity.add(newrenewalValuationWithdrawalTMPEntity);
//						}
//						renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);
//					}
//				}
//			}
//		}
//		
//		
//		return ApiResponseDto.success(RenewalValuationTMPHelper.entityToDto(renewalPolicyTMPEntity));
//		}
//	}

	@Transactional
	public ApiResponseDto<RenewalPolicyTMPDto> CreateRenewalforQuotation(QuotationRenewalDto quotationRenewalDto) {

		if (policyServiceRepository
				.findByPolicyandType(quotationRenewalDto.getPolicyId(), PolicyServiceName.MEMBER_ADDITION.getName())
				.size() > 0) {
			return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
		} else {

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
			renewalPolicyTMPEntity.setQuotationNumber(RenewalPolicyTMPHelper
					.nextQuotationNumber(renewalPolicyTMPRepository.maxQuotationNumber()).toString());

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

}
