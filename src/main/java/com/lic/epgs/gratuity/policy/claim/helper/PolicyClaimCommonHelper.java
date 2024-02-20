package com.lic.epgs.gratuity.policy.claim.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.mph.entity.HistoryMPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.HistoryMPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.HistoryMPHEntity;
import com.lic.epgs.gratuity.mph.entity.HistoryMPHRepresentativesEntity;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.MPHRepresentativesEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHRepresentativeEntity;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimPropsDto;
import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimPropsEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.lifecover.entity.HistoryLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberBankAccount;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.renewal.repository.PolicyRenewalRemainderRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.helper.RenewalGratuityBenefitTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.helper.RenewalPolicyTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.helper.RenewalLifeCoveTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.repository.RenewalLifeCoverTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAddressEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.helper.RenewalPolicyTMPMemberHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberRepository;
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
import com.lic.epgs.gratuity.policy.repository.PolicyHistoryRepository;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeRuleHistoryEntity;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyMasterValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationHistoryEntity;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyMasterValuationRepository;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyValuationRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationBasicHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.helper.GratuityBenefitHelper;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.helper.LifeCoverFlatHelper;
import com.lic.epgs.gratuity.quotation.member.entity.MemberAddressEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberAppointeeEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBankAccount;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkStgEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberNomineeEntity;

@Component
public class PolicyClaimCommonHelper {
	private static Long renewalValuationTypeId = 58l;

	public static TempPolicyClaimPropsDto claimdtotoentity(TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity) {
		return new ModelMapper().map(tempPolicyClaimPropsEntity, TempPolicyClaimPropsDto.class);
				}
	public static TempPolicyClaimPropsEntity copytotmpforclaim(Long tempMemberId, Long tempPolicyId,
			TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {

		TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity = new ModelMapper().map(tempPolicyClaimPropsDto,
				TempPolicyClaimPropsEntity.class);
		tempPolicyClaimPropsEntity.setTmpPolicyId(tempPolicyId);
		tempPolicyClaimPropsEntity.setIsActive(true);
		tempPolicyClaimPropsEntity.setTmpMemberId(tempMemberId);
		tempPolicyClaimPropsEntity.setCreatedDate(new Date());
		tempPolicyClaimPropsEntity.setCreatedBy(tempPolicyClaimPropsDto.getCreatedBy());
		return tempPolicyClaimPropsEntity;
	}

	public static RenewalPolicyTMPEntity copytoTmpForClaim(MasterPolicyEntity masterPolicyEntity) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = RenewalPolicyTMPHelper.pmsttoTmp(masterPolicyEntity);
		renewalPolicyTMPEntity.setId(null);
		renewalPolicyTMPEntity.setPolicyStatusId(null);
		renewalPolicyTMPEntity.setPolicySubStatusId(null);
		renewalPolicyTMPEntity.setPolicytaggedStatusId(null);

		renewalPolicyTMPEntity.setIsActive(true);
		renewalPolicyTMPEntity.setMasterPolicyId(masterPolicyEntity.getId());
		renewalPolicyTMPEntity.setCreatedDate(new Date());
		return renewalPolicyTMPEntity;
	}

	public static RenewalSchemeruleTMPEntity copyToTmpSchemeforClaim(Optional<PolicySchemeEntity> policySchemeEntity,
			Long tempPolicyId) {

		RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = null;
		if (policySchemeEntity.isPresent()) {

			renewalSchemeruleTMPEntity = RenewalSchemeruleTMPHelper.pmsttoTmp(policySchemeEntity.get());
			renewalSchemeruleTMPEntity.setId(null);
			renewalSchemeruleTMPEntity.setTmpPolicyId(tempPolicyId);
			renewalSchemeruleTMPEntity.setPmstSchemeRuleId(policySchemeEntity.get().getId());

		}
		return renewalSchemeruleTMPEntity;

	}

	public static List<MemberCategoryEntity> copyToTmpMemberforClaim(List<MemberCategoryEntity> memberCategoryEntity,
			Long tempPolicyId) {
		List<MemberCategoryEntity> addMemberCategoryEntity = new ArrayList<MemberCategoryEntity>();

		for (MemberCategoryEntity getmemberCategoryEntity : memberCategoryEntity) {
//			getmemberCategoryEntity.setId(null);
			getmemberCategoryEntity.setPmstTmpPolicy(tempPolicyId);
			addMemberCategoryEntity.add(getmemberCategoryEntity);
		}
		return addMemberCategoryEntity;
	}

	public static List<RenewalLifeCoverTMPEntity> copyToTmpLifeCoverforClaim(
			List<PolicyLifeCoverEntity> policyLifeCoverEntity,
			List<MemberCategoryEntity> memberCategoryEntity, Long tempPolicyId) {
		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = new ArrayList<RenewalLifeCoverTMPEntity>();
	

			for (MemberCategoryEntity getOldMemberCategory : memberCategoryEntity) {

				for (PolicyLifeCoverEntity getLifeCoverEntity : policyLifeCoverEntity) {

					if (getOldMemberCategory.getId().equals(getLifeCoverEntity.getCategoryId())) {
						RenewalLifeCoverTMPEntity renewalsLifeCoverEntity = RenewalLifeCoveTMPHelper
								.pmsttoTmp(getLifeCoverEntity);
						renewalsLifeCoverEntity.setId(null);
						renewalsLifeCoverEntity.setTmpPolicyId(tempPolicyId);
						renewalsLifeCoverEntity.setPmstLifeCoverId(getLifeCoverEntity.getId());

						renewalsLifeCoverEntity.setCategoryId(getOldMemberCategory.getId());

						renewalLifeCoverTMPEntity.add(renewalsLifeCoverEntity);
//						getLifeCoverEntity.setIsActive(false);
//						break;
					}

				

			}

		}
		return renewalLifeCoverTMPEntity;
	}

	public static List<RenewalGratuityBenefitTMPEntity> copyToTmpGratuityforClaim(
			List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity,
			 List<MemberCategoryEntity> memberCategoryEntity,
			Long tempPolicyId) {
		List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = new ArrayList<RenewalGratuityBenefitTMPEntity>();

	
			for (MemberCategoryEntity getOldMemberCategory : memberCategoryEntity) {
				for (PolicyGratuityBenefitEntity getpolicyGratuityBenefitEntity : policyGratuityBenefitEntity) {

					if (getOldMemberCategory.getId().equals(getpolicyGratuityBenefitEntity.getCategoryId())) {
						renewalGratuityBenefitTMPEntity.add(RenewalGratuityBenefitTMPHelper
								.pmsttoTmp(getpolicyGratuityBenefitEntity, tempPolicyId, getOldMemberCategory.getId()));
//						getpolicyGratuityBenefitEntity.setIsActive(false);
//						break;
					}

				
			}

		}
		return renewalGratuityBenefitTMPEntity;
	}

	public static RenewalValuationTMPEntity copyToTmpValuationforClaim(
			Optional<PolicyMasterValuationEntity> policyValuationEntity, Long TmpPolicyId) {
		RenewalValuationTMPEntity renewalValuationTMPEntity = RenewalValuationTMPHelper
				.pmsttoTmp(policyValuationEntity.get());
		renewalValuationTMPEntity.setId(null);
		renewalValuationTMPEntity.setTmpPolicyId(TmpPolicyId);
		renewalValuationTMPEntity.setPmstValuationId(policyValuationEntity.get().getId());
		return renewalValuationTMPEntity;
	}

	public static RenewalValuationMatrixTMPEntity copyToTmpValuationMatrixforClaim(
			Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity, Long tempPolicyId) {
		RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = RenewalValuationMatrixTMPHelper
				.pmsttoTmp(policyValuationMatrixEntity.get());
		renewalValuationMatrixTMPEntity.setId(null);
		renewalValuationMatrixTMPEntity.setTmpPolicyId(tempPolicyId);
		renewalValuationMatrixTMPEntity.setPmstValuationMatrixId(policyValuationMatrixEntity.get().getId());
		return renewalValuationMatrixTMPEntity;
	}

	public static RenewalValuationBasicTMPEntity copyToTmpValuationBasicClaim(
			Optional<PolicyValutationBasicEntity> policyValutationBasicEntity, Long tmpPolicyId) {
		RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = RenewalValuationBasicTMPHelper
				.pmsttoTmp(policyValutationBasicEntity.get());
		renewalValuationBasicTMPEntity.setId(null);
//		renewalValuationBasicTMPEntity.setValuationTypeId(renewalValuationTypeId);
		renewalValuationBasicTMPEntity.setPmstValuationBasicId(policyValutationBasicEntity.get().getId());
		renewalValuationBasicTMPEntity.setTmpPolicyId(tmpPolicyId);
		return renewalValuationBasicTMPEntity;
	}

	public static List<RenewalValuationWithdrawalTMPEntity> copyToTmpValuationWithdrawlClaim(
			List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity, Long tmpPolicyId) {

		List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = new ArrayList<RenewalValuationWithdrawalTMPEntity>();
		for (PolicyValuationWithdrawalRateEntity getpolicyValuationWithdrawalRateEntity : policyValuationWithdrawalRateEntity) {
			RenewalValuationWithdrawalTMPEntity newrenewalValuationWithdrawalTMPEntity = RenewalValuationTMPHelper
					.pmsttoTmp(getpolicyValuationWithdrawalRateEntity);
			newrenewalValuationWithdrawalTMPEntity.setId(null);
			newrenewalValuationWithdrawalTMPEntity.setTmpPolicyId(tmpPolicyId);
			newrenewalValuationWithdrawalTMPEntity
					.setPmstValWithDrawalId(getpolicyValuationWithdrawalRateEntity.getId());
			renewalValuationWithdrawalTMPEntity.add(newrenewalValuationWithdrawalTMPEntity);
		}
		return renewalValuationWithdrawalTMPEntity;
	}

	// history to temp

	public static RenewalPolicyTMPEntity copytoHistoryForClaim(PolicyHistoryEntity policyHistoryEntities) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = RenewalPolicyTMPHelper.histtoTmp(policyHistoryEntities);
		renewalPolicyTMPEntity.setId(null);
		renewalPolicyTMPEntity.setPolicyStatusId(null);
		renewalPolicyTMPEntity.setPolicySubStatusId(null);
		renewalPolicyTMPEntity.setPolicytaggedStatusId(null);

		renewalPolicyTMPEntity.setIsActive(true);
		renewalPolicyTMPEntity.setPmstHisPolicyId(policyHistoryEntities.getId());
		renewalPolicyTMPEntity.setCreatedDate(new Date());
		return renewalPolicyTMPEntity;
	}

	public static RenewalSchemeruleTMPEntity copyToHistorySchemeforClaim(
			Optional<PolicySchemeRuleHistoryEntity> policySchemeRuleHistoryEntity, Long id) {

		RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = null;
		if (policySchemeRuleHistoryEntity.isPresent()) {

			renewalSchemeruleTMPEntity = RenewalSchemeruleTMPHelper.histtoTmp(policySchemeRuleHistoryEntity.get());
			renewalSchemeruleTMPEntity.setId(null);
			renewalSchemeruleTMPEntity.setTmpPolicyId(id);
			renewalSchemeruleTMPEntity.setPmstHisSchemeRuleId(policySchemeRuleHistoryEntity.get().getId());

		}
		return renewalSchemeruleTMPEntity;

	}

	public static RenewalValuationMatrixTMPEntity copyToHistoryValuationMatrixforClaim(
			Optional<PolicyValuationMatrixHistoryEntity> policyValuationMatrixHistoryEntity, Long tempPolicyId) {
		RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = RenewalValuationMatrixTMPHelper
				.histtoTmp(policyValuationMatrixHistoryEntity.get());
		renewalValuationMatrixTMPEntity.setId(null);
		renewalValuationMatrixTMPEntity.setTmpPolicyId(tempPolicyId);
		renewalValuationMatrixTMPEntity.setPmstHisValuationMatrixId(policyValuationMatrixHistoryEntity.get().getId());
		return renewalValuationMatrixTMPEntity;
	}

	public static List<RenewalValuationWithdrawalTMPEntity> copyToHistoryValuationWithdrawlClaim(
			List<PolicyValuationWithdrawalRateHistoryEntity> policyValuationWithdrawalRateHistoryEntity,
			Long tmpPolicyId) {

		List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = new ArrayList<RenewalValuationWithdrawalTMPEntity>();
		for (PolicyValuationWithdrawalRateHistoryEntity getPolicyValuationWithdrawalRateHistoryEntity : policyValuationWithdrawalRateHistoryEntity) {
			RenewalValuationWithdrawalTMPEntity newrenewalValuationWithdrawalTMPEntity = RenewalValuationTMPHelper
					.histtoTmp(getPolicyValuationWithdrawalRateHistoryEntity);
			newrenewalValuationWithdrawalTMPEntity.setId(null);
			newrenewalValuationWithdrawalTMPEntity.setTmpPolicyId(tmpPolicyId);
			newrenewalValuationWithdrawalTMPEntity
					.setPmstHisValWithDrawalId(getPolicyValuationWithdrawalRateHistoryEntity.getId());

		}
		return renewalValuationWithdrawalTMPEntity;
	}

	public static RenewalPolicyTMPMemberEntity copyToHistoryMemberforClaim(HistoryMemberEntity historyMemberEntity,
			List<MemberCategoryEntity> getmemberCategoryEntity, List<MemberCategoryEntity> oldmemberCategoryEntity,
			Long tempPolicyId) {

		RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = RenewalPolicyTMPMemberHelper
				.histtoTmp(historyMemberEntity);
		renewalPolicyTMPMemberEntity.setId(null);
		renewalPolicyTMPMemberEntity.setTmpPolicyId(tempPolicyId);
		renewalPolicyTMPMemberEntity.setPmstHistMemebrId(historyMemberEntity.getId());

		Set<RenewalPolicyTMPMemberAddressEntity> addTMPMemberAddressEntity = new HashSet<RenewalPolicyTMPMemberAddressEntity>();
		for (HistoryMemberAddressEntity policyMemberAddressEntity : historyMemberEntity.getAddresses()) {

			RenewalPolicyTMPMemberAddressEntity renewalPolicyTMPMemberAddressEntity = new ModelMapper()
					.map(policyMemberAddressEntity, RenewalPolicyTMPMemberAddressEntity.class);
			renewalPolicyTMPMemberAddressEntity.setId(null);
			renewalPolicyTMPMemberAddressEntity.setPmstHisMemberAddressId(policyMemberAddressEntity.getId());
			renewalPolicyTMPMemberAddressEntity.setMember(renewalPolicyTMPMemberEntity);
			addTMPMemberAddressEntity.add(renewalPolicyTMPMemberAddressEntity);
		}
		renewalPolicyTMPMemberEntity.setAddresses(addTMPMemberAddressEntity);
		// end
		// Bank Account
		Set<RenewalPolicyTMPMemberBankAccountEntity> addTMPMemberBankAccountEntity = new HashSet<RenewalPolicyTMPMemberBankAccountEntity>();
		for (HistoryMemberBankAccountEntity PolicyMemberBankAccount : historyMemberEntity.getBankAccounts()) {

			RenewalPolicyTMPMemberBankAccountEntity renewalPolicyTMPMemberBankAccountEntity = new ModelMapper()
					.map(PolicyMemberBankAccount, RenewalPolicyTMPMemberBankAccountEntity.class);
			renewalPolicyTMPMemberBankAccountEntity.setId(null);
			renewalPolicyTMPMemberBankAccountEntity.setMember(renewalPolicyTMPMemberEntity);
			renewalPolicyTMPMemberBankAccountEntity.setPmstHisMemBankAccoutId(PolicyMemberBankAccount.getId());
			addTMPMemberBankAccountEntity.add(renewalPolicyTMPMemberBankAccountEntity);
		}
		renewalPolicyTMPMemberEntity.setBankAccounts(addTMPMemberBankAccountEntity);
		// end

		// Member Appointee
		Set<RenewalPolicyTMPMemberAppointeeEntity> addRenewalPolicyTMPMemberAppointeeEntity = new HashSet<RenewalPolicyTMPMemberAppointeeEntity>();
		for (HistoryMemberAppointeeEntity hisMemberAppointeeEntity : historyMemberEntity.getAppointees()) {

			RenewalPolicyTMPMemberAppointeeEntity renewalPolicyTMPMemberAppointeeEntity = new ModelMapper()
					.map(hisMemberAppointeeEntity, RenewalPolicyTMPMemberAppointeeEntity.class);
			renewalPolicyTMPMemberAppointeeEntity.setId(null);
			renewalPolicyTMPMemberAppointeeEntity.setMember(renewalPolicyTMPMemberEntity);
			renewalPolicyTMPMemberAppointeeEntity.setPmstHisMemberAppointeeId(hisMemberAppointeeEntity.getId());
			addRenewalPolicyTMPMemberAppointeeEntity.add(renewalPolicyTMPMemberAppointeeEntity);

			HistoryMemberNomineeEntity newHisMemberNomineeEntity = hisMemberAppointeeEntity.getNominee();
			RenewalPolicyTMPMemberNomineeEntity renewalPolicyTMPMemberNomineeEntity = new ModelMapper()
					.map(newHisMemberNomineeEntity, RenewalPolicyTMPMemberNomineeEntity.class);
			renewalPolicyTMPMemberNomineeEntity.setId(null);
			renewalPolicyTMPMemberNomineeEntity.setMember(renewalPolicyTMPMemberEntity);
			renewalPolicyTMPMemberNomineeEntity.setPmstHisMemberNomineeId(newHisMemberNomineeEntity.getId());
			renewalPolicyTMPMemberAppointeeEntity.setNominee(renewalPolicyTMPMemberNomineeEntity);
			renewalPolicyTMPMemberEntity.getAppointees().add(renewalPolicyTMPMemberAppointeeEntity);

			for (Iterator<HistoryMemberNomineeEntity> iterator = historyMemberEntity.getNominees().iterator(); iterator
					.hasNext();) {
				HistoryMemberNomineeEntity s = iterator.next();
				if (s.getNomineeName() == renewalPolicyTMPMemberAppointeeEntity.getNominee().getNomineeName()
						&& s.getNomineeAadharNumber() == renewalPolicyTMPMemberAppointeeEntity.getNominee()
								.getNomineeAadharNumber()
						&& s.getDateOfBirth() == renewalPolicyTMPMemberAppointeeEntity.getNominee().getDateOfBirth()) {
					iterator.remove();
				}
			}

		}
		renewalPolicyTMPMemberEntity.setAppointees(addRenewalPolicyTMPMemberAppointeeEntity);
		// end

		// Member Nominee
		Set<RenewalPolicyTMPMemberNomineeEntity> addRenewalPolicyTMPMemberNomineeEntity = new HashSet<RenewalPolicyTMPMemberNomineeEntity>();
		for (HistoryMemberNomineeEntity hisMemberNomineeEntity : historyMemberEntity.getNominees()) {

			RenewalPolicyTMPMemberNomineeEntity renewalPolicyTMPMemberNomineeEntity = new ModelMapper()
					.map(hisMemberNomineeEntity, RenewalPolicyTMPMemberNomineeEntity.class);
			renewalPolicyTMPMemberNomineeEntity.setId(null);
			renewalPolicyTMPMemberNomineeEntity.setPmstHisMemberNomineeId(hisMemberNomineeEntity.getId());
			renewalPolicyTMPMemberNomineeEntity.setMember(renewalPolicyTMPMemberEntity);
			addRenewalPolicyTMPMemberNomineeEntity.add(renewalPolicyTMPMemberNomineeEntity);
		}
		renewalPolicyTMPMemberEntity.setNominees(addRenewalPolicyTMPMemberNomineeEntity);

		renewalPolicyTMPMemberEntity.setModifiedDate(new Date());
		renewalPolicyTMPMemberEntity.setIsActive(true);
		renewalPolicyTMPMemberEntity.setTmpPolicyId(tempPolicyId);
		// old category id = = policymember -category id

		for (MemberCategoryEntity memberCategoryEntity : oldmemberCategoryEntity) {

			for (MemberCategoryEntity getNew : getmemberCategoryEntity) {

				if (getNew.getName().equals(memberCategoryEntity.getName())) {

					if (historyMemberEntity.getCategoryId().equals(memberCategoryEntity.getId())) {

						renewalPolicyTMPMemberEntity.setCategoryId(getNew.getId());

					}
					;
				}
				break;
			}
		}
		return renewalPolicyTMPMemberEntity;

	}

	public static List<RenewalGratuityBenefitTMPEntity> copyToHistoryGratuityforClaim(
			List<HistoryGratuityBenefitEntity> historyGratuityBenefitEntity,
		List<MemberCategoryEntity> memberCategoryEntity,
			Long tempPolicyId) {
		List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = new ArrayList<RenewalGratuityBenefitTMPEntity>();


			for (MemberCategoryEntity getOldHistoryMember : memberCategoryEntity) {
				for (HistoryGratuityBenefitEntity gethistoryGratuityBenefitEntity : historyGratuityBenefitEntity) {

					if (getOldHistoryMember.getId().equals(gethistoryGratuityBenefitEntity.getCategoryId())) {
						renewalGratuityBenefitTMPEntity.add(RenewalGratuityBenefitTMPHelper
								.histtoTmp(gethistoryGratuityBenefitEntity, tempPolicyId, getOldHistoryMember.getId()));
						gethistoryGratuityBenefitEntity.setIsActive(false);						
						break;
					}

				}
			}

		
		return renewalGratuityBenefitTMPEntity;
	}

	public static List<RenewalLifeCoverTMPEntity> copyToHistoryLifeCoverforClaim(
			List<HistoryLifeCoverEntity> historyLifeCoverEntity,List<MemberCategoryEntity> oldMemberCategory, Long id) {

		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = new ArrayList<RenewalLifeCoverTMPEntity>();
		

		for (MemberCategoryEntity getOldMemberCategory : oldMemberCategory) {

			for (HistoryLifeCoverEntity getHistoryLifeCoverEntity : historyLifeCoverEntity) {

				if (getHistoryLifeCoverEntity.getCategoryId().equals(getOldMemberCategory.getId())) {
					RenewalLifeCoverTMPEntity renewalsLifeCoverEntity = RenewalLifeCoveTMPHelper
							.histtoTmp(getHistoryLifeCoverEntity);
					renewalsLifeCoverEntity.setId(null);
					renewalsLifeCoverEntity.setTmpPolicyId(id);
					renewalsLifeCoverEntity.setCategoryId(getOldMemberCategory.getId());
					renewalsLifeCoverEntity.setPmstHisLifeCoverId(getHistoryLifeCoverEntity.getId());
					renewalLifeCoverTMPEntity.add(renewalsLifeCoverEntity);

					break;
				}

			}

		}
		return renewalLifeCoverTMPEntity;

	}

	public static RenewalValuationBasicTMPEntity copyToHistoryValuationBasicClaim(
			Optional<PolicyValuationBasicHistoryEntity> policyValuationBasicHistoryEntity, Long tmpPolicyId) {
		RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = RenewalValuationBasicTMPHelper
				.histtoTmp(policyValuationBasicHistoryEntity.get());
		renewalValuationBasicTMPEntity.setId(null);
		renewalValuationBasicTMPEntity.setValuationTypeId(renewalValuationTypeId);
		renewalValuationBasicTMPEntity.setPmstHisValuationBasicId(policyValuationBasicHistoryEntity.get().getId());
		renewalValuationBasicTMPEntity.setTmpPolicyId(tmpPolicyId);
		return renewalValuationBasicTMPEntity;
	}

	public static RenewalValuationTMPEntity copyToHistoryValuationforClaim(
			Optional<PolicyValuationHistoryEntity> policyValuationHistoryEntity, Long TmpPolicyId) {
		RenewalValuationTMPEntity renewalValuationTMPEntity = RenewalValuationTMPHelper
				.histtoTmp(policyValuationHistoryEntity.get());
		renewalValuationTMPEntity.setId(null);
		renewalValuationTMPEntity.setTmpPolicyId(TmpPolicyId);
		renewalValuationTMPEntity.setPmstHisValuationId(policyValuationHistoryEntity.get().getId());
		return renewalValuationTMPEntity;
	}
	
	/** Individual : copyToTmpIndividualMemberClaim **/
	public static RenewalPolicyTMPMemberEntity copyToTmpIndividualMemberClaim(PolicyMemberEntity policyMemberEntity,
			List<MemberCategoryEntity> getmemberCategoryEntity, List<MemberCategoryEntity> oldmemberCategoryEntity,
			Long tempPolicyId) {

		RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = new ModelMapper().map(policyMemberEntity,
				RenewalPolicyTMPMemberEntity.class);

		renewalPolicyTMPMemberEntity.setId(null);
		renewalPolicyTMPMemberEntity.setPmstMemebrId(policyMemberEntity.getId());
		Set<RenewalPolicyTMPMemberAddressEntity> addTMPMemberAddressEntity = new HashSet<RenewalPolicyTMPMemberAddressEntity>();
		for (PolicyMemberAddressEntity policyMemberAddressEntity : policyMemberEntity.getAddresses()) {

			RenewalPolicyTMPMemberAddressEntity renewalPolicyTMPMemberAddressEntity = new ModelMapper()
					.map(policyMemberAddressEntity, RenewalPolicyTMPMemberAddressEntity.class);
			renewalPolicyTMPMemberAddressEntity.setId(null);
			renewalPolicyTMPMemberAddressEntity.setMember(renewalPolicyTMPMemberEntity);
			addTMPMemberAddressEntity.add(renewalPolicyTMPMemberAddressEntity);
		}
		renewalPolicyTMPMemberEntity.setAddresses(addTMPMemberAddressEntity);
		// end
		// Bank Account
		Set<RenewalPolicyTMPMemberBankAccountEntity> addTMPMemberBankAccountEntity = new HashSet<RenewalPolicyTMPMemberBankAccountEntity>();
		for (PolicyMemberBankAccount PolicyMemberBankAccount : policyMemberEntity.getBankAccounts()) {

			RenewalPolicyTMPMemberBankAccountEntity renewalPolicyTMPMemberBankAccountEntity = new ModelMapper()
					.map(PolicyMemberBankAccount, RenewalPolicyTMPMemberBankAccountEntity.class);
			renewalPolicyTMPMemberBankAccountEntity.setId(null);
			renewalPolicyTMPMemberBankAccountEntity.setMember(renewalPolicyTMPMemberEntity);
			renewalPolicyTMPMemberBankAccountEntity.setPmstMemberBankAccId(PolicyMemberBankAccount.getId());
			addTMPMemberBankAccountEntity.add(renewalPolicyTMPMemberBankAccountEntity);
		}
		renewalPolicyTMPMemberEntity.setBankAccounts(addTMPMemberBankAccountEntity);
		// end

		// Member Appointee
		Set<RenewalPolicyTMPMemberAppointeeEntity> addRenewalPolicyTMPMemberAppointeeEntity = new HashSet<RenewalPolicyTMPMemberAppointeeEntity>();
		for (PolicyMemberAppointeeEntity policyMemberAppointeeEntity : policyMemberEntity.getAppointees()) {

			RenewalPolicyTMPMemberAppointeeEntity renewalPolicyTMPMemberAppointeeEntity = new ModelMapper()
					.map(policyMemberAppointeeEntity, RenewalPolicyTMPMemberAppointeeEntity.class);
			renewalPolicyTMPMemberAppointeeEntity.setId(null);
			renewalPolicyTMPMemberAppointeeEntity.setMember(renewalPolicyTMPMemberEntity);
			renewalPolicyTMPMemberAppointeeEntity.setPmstMemberAppointeeId(policyMemberAppointeeEntity.getId());
			addRenewalPolicyTMPMemberAppointeeEntity.add(renewalPolicyTMPMemberAppointeeEntity);

			PolicyMemberNomineeEntity newPolicyMemberNomineeEntity = policyMemberAppointeeEntity.getNominee();
			RenewalPolicyTMPMemberNomineeEntity renewalPolicyTMPMemberNomineeEntity = new ModelMapper()
					.map(newPolicyMemberNomineeEntity, RenewalPolicyTMPMemberNomineeEntity.class);
			renewalPolicyTMPMemberNomineeEntity.setId(null);
			renewalPolicyTMPMemberNomineeEntity.setMember(renewalPolicyTMPMemberEntity);
			renewalPolicyTMPMemberNomineeEntity.setPmstMemberNomineeId(newPolicyMemberNomineeEntity.getId());
			renewalPolicyTMPMemberAppointeeEntity.setNominee(renewalPolicyTMPMemberNomineeEntity);
			renewalPolicyTMPMemberEntity.getAppointees().add(renewalPolicyTMPMemberAppointeeEntity);

			for (Iterator<PolicyMemberNomineeEntity> iterator = policyMemberEntity.getNominees().iterator(); iterator
					.hasNext();) {
				PolicyMemberNomineeEntity s = iterator.next();
				if (s.getNomineeName() == renewalPolicyTMPMemberAppointeeEntity.getNominee().getNomineeName()
						&& s.getNomineeAadharNumber() == renewalPolicyTMPMemberAppointeeEntity.getNominee()
								.getNomineeAadharNumber()
						&& s.getDateOfBirth() == renewalPolicyTMPMemberAppointeeEntity.getNominee().getDateOfBirth()) {
					iterator.remove();
				}
			}

		}
		renewalPolicyTMPMemberEntity.setAppointees(addRenewalPolicyTMPMemberAppointeeEntity);
		// end

		// Member Nominee
		Set<RenewalPolicyTMPMemberNomineeEntity> addRenewalPolicyTMPMemberNomineeEntity = new HashSet<RenewalPolicyTMPMemberNomineeEntity>();
		for (PolicyMemberNomineeEntity policyMemberNomineeEntity : policyMemberEntity.getNominees()) {

			RenewalPolicyTMPMemberNomineeEntity renewalPolicyTMPMemberNomineeEntity = new ModelMapper()
					.map(policyMemberNomineeEntity, RenewalPolicyTMPMemberNomineeEntity.class);
			renewalPolicyTMPMemberNomineeEntity.setId(null);
			renewalPolicyTMPMemberNomineeEntity.setPmstMemberNomineeId(policyMemberNomineeEntity.getId());
			renewalPolicyTMPMemberNomineeEntity.setMember(renewalPolicyTMPMemberEntity);
			addRenewalPolicyTMPMemberNomineeEntity.add(renewalPolicyTMPMemberNomineeEntity);
		}
		renewalPolicyTMPMemberEntity.setNominees(addRenewalPolicyTMPMemberNomineeEntity);

		renewalPolicyTMPMemberEntity.setModifiedDate(new Date());
		renewalPolicyTMPMemberEntity.setIsActive(true);
		renewalPolicyTMPMemberEntity.setTmpPolicyId(tempPolicyId);
		// old category id = = policymember -category id

		for (MemberCategoryEntity memberCategoryEntity : oldmemberCategoryEntity) {

			/*for (MemberCategoryEntity getNew : getmemberCategoryEntity) {*/

			/*	if (getNew.getName().equals(memberCategoryEntity.getName())) {

					if (policyMemberEntity.getCategoryId().equals(memberCategoryEntity.getId())) {*/

						renewalPolicyTMPMemberEntity.setCategoryId(memberCategoryEntity.getId());

				/*	}
					;
				}*/

		/*	}*/
			break;
		}
		return renewalPolicyTMPMemberEntity;
	}

	/** Bulk : copyToTmpBulkMember **/
	public static List<RenewalPolicyTMPMemberEntity> copyToTmpBulkMember(List<PolicyMemberEntity> policyMemberEntityList,
			List<MemberCategoryEntity> getmemberCategoryEntity, List<MemberCategoryEntity> oldmemberCategoryEntity,
			Long tempPolicyId) {

		/** PMST_TMP_MEMBER **/
		List<RenewalPolicyTMPMemberEntity> masterTempMemberEntityList = new ArrayList<>();

		for (PolicyMemberEntity policyMemberEntity : policyMemberEntityList) {

			RenewalPolicyTMPMemberEntity masterTempMemberEntity = new ModelMapper().map(policyMemberEntity, RenewalPolicyTMPMemberEntity.class);

			masterTempMemberEntity.setId(null);
			masterTempMemberEntity.setPmstMemebrId(policyMemberEntity.getId());
			Set<RenewalPolicyTMPMemberAddressEntity> addTMPMemberAddressEntity = new HashSet<RenewalPolicyTMPMemberAddressEntity>();
//			for (MemberAddressEntity memberAddressEntity : memberBulkStgEntity.getAddresses()) {
//
//				RenewalPolicyTMPMemberAddressEntity renewalPolicyTMPMemberAddressEntity = new ModelMapper()
//						.map(memberAddressEntity, RenewalPolicyTMPMemberAddressEntity.class);
//				renewalPolicyTMPMemberAddressEntity.setId(null);
//				renewalPolicyTMPMemberAddressEntity.setMember(masterTempMemberEntity);
//				addTMPMemberAddressEntity.add(renewalPolicyTMPMemberAddressEntity);
//			}
//			masterTempMemberEntity.setAddresses(addTMPMemberAddressEntity);
			// end
			// Bank Account
//			Set<RenewalPolicyTMPMemberBankAccountEntity> addTMPMemberBankAccountEntity = new HashSet<RenewalPolicyTMPMemberBankAccountEntity>();
//			for (MemberBankAccount memberBankAccount : memberBulkStgEntity.getBankAccounts()) {
//
//				RenewalPolicyTMPMemberBankAccountEntity renewalPolicyTMPMemberBankAccountEntity = new ModelMapper()
//						.map(memberBankAccount, RenewalPolicyTMPMemberBankAccountEntity.class);
//				renewalPolicyTMPMemberBankAccountEntity.setId(null);
//				renewalPolicyTMPMemberBankAccountEntity.setMember(masterTempMemberEntity);
//				renewalPolicyTMPMemberBankAccountEntity.setPmstMemberBankAccId(memberBankAccount.getId());
//				addTMPMemberBankAccountEntity.add(renewalPolicyTMPMemberBankAccountEntity);
//			}
//			masterTempMemberEntity.setBankAccounts(addTMPMemberBankAccountEntity);
			// end

			// Member Appointee
//			Set<RenewalPolicyTMPMemberAppointeeEntity> addRenewalPolicyTMPMemberAppointeeEntity = new HashSet<RenewalPolicyTMPMemberAppointeeEntity>();
//			for (MemberAppointeeEntity memberAppointeeEntity : memberEntity.getAppointees()) {
//
//				RenewalPolicyTMPMemberAppointeeEntity renewalPolicyTMPMemberAppointeeEntity = new ModelMapper()
//						.map(memberAppointeeEntity, RenewalPolicyTMPMemberAppointeeEntity.class);
//				renewalPolicyTMPMemberAppointeeEntity.setId(null);
//				renewalPolicyTMPMemberAppointeeEntity.setMember(masterTempMemberEntity);
//				renewalPolicyTMPMemberAppointeeEntity.setPmstMemberAppointeeId(memberAppointeeEntity.getId());
//				addRenewalPolicyTMPMemberAppointeeEntity.add(renewalPolicyTMPMemberAppointeeEntity);
//
//				MemberNomineeEntity newPolicyMemberNomineeEntity = memberAppointeeEntity.getNominee();
//				RenewalPolicyTMPMemberNomineeEntity renewalPolicyTMPMemberNomineeEntity = new ModelMapper()
//						.map(newPolicyMemberNomineeEntity, RenewalPolicyTMPMemberNomineeEntity.class);
//				renewalPolicyTMPMemberNomineeEntity.setId(null);
//				renewalPolicyTMPMemberNomineeEntity.setMember(masterTempMemberEntity);
//				renewalPolicyTMPMemberNomineeEntity.setPmstMemberNomineeId(newPolicyMemberNomineeEntity.getId());
//				renewalPolicyTMPMemberAppointeeEntity.setNominee(renewalPolicyTMPMemberNomineeEntity);
//				masterTempMemberEntity.getAppointees().add(renewalPolicyTMPMemberAppointeeEntity);
//
//				for (Iterator<MemberNomineeEntity> iterator = memberEntity.getNominees().iterator(); iterator
//						.hasNext();) {
//					MemberNomineeEntity s = iterator.next();
//					if (s.getName() == renewalPolicyTMPMemberAppointeeEntity.getNominee().getNomineeName()
//							&& s.getAadharNumber() == renewalPolicyTMPMemberAppointeeEntity.getNominee()
//									.getNomineeAadharNumber()
//							&& s.getDateOfBirth() == renewalPolicyTMPMemberAppointeeEntity.getNominee()
//									.getDateOfBirth()) {
//						iterator.remove();
//					}
//				}
//
//			}
//			masterTempMemberEntity.setAppointees(addRenewalPolicyTMPMemberAppointeeEntity);
			// end

			// Member Nominee
//			Set<RenewalPolicyTMPMemberNomineeEntity> addRenewalPolicyTMPMemberNomineeEntity = new HashSet<RenewalPolicyTMPMemberNomineeEntity>();
//			for (MemberNomineeEntity memberNomineeEntity : memberEntity.getNominees()) {
//
//				RenewalPolicyTMPMemberNomineeEntity renewalPolicyTMPMemberNomineeEntity = new ModelMapper()
//						.map(memberNomineeEntity, RenewalPolicyTMPMemberNomineeEntity.class);
//				renewalPolicyTMPMemberNomineeEntity.setId(null);
//				renewalPolicyTMPMemberNomineeEntity.setPmstMemberNomineeId(memberNomineeEntity.getId());
//				renewalPolicyTMPMemberNomineeEntity.setMember(masterTempMemberEntity);
//				addRenewalPolicyTMPMemberNomineeEntity.add(renewalPolicyTMPMemberNomineeEntity);
//			}
//			masterTempMemberEntity.setNominees(addRenewalPolicyTMPMemberNomineeEntity);

			masterTempMemberEntity.setModifiedDate(new Date());
			masterTempMemberEntity.setIsActive(true);
			masterTempMemberEntity.setTmpPolicyId(tempPolicyId);
			// old category id = = policymember -category id

			for (MemberCategoryEntity memberCategoryEntity : oldmemberCategoryEntity) {

			/*	for (MemberCategoryEntity getNew : getmemberCategoryEntity) {

					if (getNew.getName().equals(memberCategoryEntity.getName())) {

						if (policyMemberEntity.getCategoryId().equals(memberCategoryEntity.getId())) {*/

							masterTempMemberEntity.setCategoryId(memberCategoryEntity.getId());
					/*	}
						;
					}

				}*/
				break;
			}

			masterTempMemberEntityList.add(masterTempMemberEntity);
		}

		return masterTempMemberEntityList;
	}
	
	
	public static List<MemberCategoryEntity> copyhisToTmpMemberforClaim(List<MemberCategoryEntity> memberCategoryEntity,
			Long tempPolicyId) {
		List<MemberCategoryEntity> addMemberCategoryEntity = new ArrayList<MemberCategoryEntity>();

		for (MemberCategoryEntity getmemberCategoryEntity : memberCategoryEntity) {
//			getmemberCategoryEntity.setId(null);
			getmemberCategoryEntity.setPmstTmpPolicy(tempPolicyId);
			addMemberCategoryEntity.add(getmemberCategoryEntity);
		}
		return addMemberCategoryEntity;
	}

	public static TempMPHEntity copytoHistoTemp(Long tempPolicyid, HistoryMPHEntity historyMPHEntity) {

		TempMPHEntity tempMPHEntity = new ModelMapper().map(historyMPHEntity, TempMPHEntity.class);
		tempMPHEntity.setId(null);
		tempMPHEntity.setPmstHisMphId(historyMPHEntity.getId());
		tempMPHEntity.setTmpPolicyId(tempPolicyid);
		Set<TempMPHAddressEntity> addTempMPHAddressEntity = new HashSet<TempMPHAddressEntity>();
		for (HistoryMPHAddressEntity mphAddressEntity : historyMPHEntity.getMphAddresses()) {

			TempMPHAddressEntity tempMPHAddressEntity = new ModelMapper().map(mphAddressEntity,
					TempMPHAddressEntity.class);
			tempMPHAddressEntity.setId(null);
			tempMPHAddressEntity.setPmstHisMphAddressId(mphAddressEntity.getId());
			tempMPHAddressEntity.setMasterMph(tempMPHEntity);
			addTempMPHAddressEntity.add(tempMPHAddressEntity);
		}
		tempMPHEntity.setMphAddresses(addTempMPHAddressEntity);

		Set<TempMPHBankEntity> addTempMPHBankEntity = new HashSet<TempMPHBankEntity>();

		for (HistoryMPHBankEntity mphBankEntity : historyMPHEntity.getMphBank()) {

			TempMPHBankEntity tempMPHBankEntity = new ModelMapper().map(mphBankEntity, TempMPHBankEntity.class);
			tempMPHBankEntity.setId(null);
			tempMPHBankEntity.setPmstHisMphBankId(mphBankEntity.getId());
			tempMPHBankEntity.setMasterMph(tempMPHEntity);

			addTempMPHBankEntity.add(tempMPHBankEntity);
		}
		tempMPHEntity.setMphBank(addTempMPHBankEntity);

		Set<TempMPHRepresentativeEntity> addTempMPHRepresentativeEntity = new HashSet<TempMPHRepresentativeEntity>();

		for (HistoryMPHRepresentativesEntity mphRepresentivekEntity : historyMPHEntity.getMphRepresentatives()) {

			TempMPHRepresentativeEntity tempMPHRepresentativeEntity = new ModelMapper().map(mphRepresentivekEntity,
					TempMPHRepresentativeEntity.class);
			tempMPHRepresentativeEntity.setId(null);
			tempMPHRepresentativeEntity.setPmstHisMphRepId(mphRepresentivekEntity.getId());
			tempMPHRepresentativeEntity.setMasterMph(tempMPHEntity);
			addTempMPHRepresentativeEntity.add(tempMPHRepresentativeEntity);
		}
		tempMPHEntity.setMphRepresentatives(addTempMPHRepresentativeEntity);

		return tempMPHEntity;
	}

	public static TempMPHEntity copytomastertoTemp(Long tempPolicyid, MPHEntity mphEntity) {
		TempMPHEntity tempMPHEntity = new ModelMapper().map(mphEntity, TempMPHEntity.class);
		tempMPHEntity.setId(null);
		tempMPHEntity.setTmpPolicyId(tempPolicyid);
		tempMPHEntity.setPmstId(mphEntity.getId());
		Set<TempMPHAddressEntity> addTempMPHAddressEntity = new HashSet<TempMPHAddressEntity>();
		for (MPHAddressEntity mphAddressEntity : mphEntity.getMphAddresses()) {

			TempMPHAddressEntity tempMPHAddressEntity = new ModelMapper().map(mphAddressEntity,
					TempMPHAddressEntity.class);
			tempMPHAddressEntity.setId(null);
			tempMPHAddressEntity.setPmstId(mphAddressEntity.getId());
			tempMPHAddressEntity.setMasterMph(tempMPHEntity);
			addTempMPHAddressEntity.add(tempMPHAddressEntity);
		}
		tempMPHEntity.setMphAddresses(addTempMPHAddressEntity);

		Set<TempMPHBankEntity> addTempMPHBankEntity = new HashSet<TempMPHBankEntity>();

		for (MPHBankEntity mphBankEntity : mphEntity.getMphBank()) {

			TempMPHBankEntity tempMPHBankEntity = new ModelMapper().map(mphBankEntity, TempMPHBankEntity.class);
			tempMPHBankEntity.setId(null);
			tempMPHBankEntity.setPmstId(mphBankEntity.getId());
			tempMPHBankEntity.setMasterMph(tempMPHEntity);

			addTempMPHBankEntity.add(tempMPHBankEntity);
		}
		tempMPHEntity.setMphBank(addTempMPHBankEntity);

		Set<TempMPHRepresentativeEntity> addTempMPHRepresentativeEntity = new HashSet<TempMPHRepresentativeEntity>();

		for (MPHRepresentativesEntity mphRepresentivekEntity : mphEntity.getMphRepresentatives()) {

			TempMPHRepresentativeEntity tempMPHRepresentativeEntity = new ModelMapper().map(mphRepresentivekEntity,
					TempMPHRepresentativeEntity.class);
			tempMPHRepresentativeEntity.setId(null);
			tempMPHRepresentativeEntity.setPmstId(mphRepresentivekEntity.getId());
			tempMPHRepresentativeEntity.setMasterMph(tempMPHEntity);
			addTempMPHRepresentativeEntity.add(tempMPHRepresentativeEntity);
		}
		tempMPHEntity.setMphRepresentatives(addTempMPHRepresentativeEntity);

		return tempMPHEntity;
	}

	public static TempPolicyClaimPropsEntity copytotmpforclaim(Long tempMemberId, Long tempPolicyId, MemberBulkStgEntity temp) {
		TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity = new TempPolicyClaimPropsEntity();
		tempPolicyClaimPropsEntity.setBatchId(temp.getMemberBatchId());
		tempPolicyClaimPropsEntity.setDateOfDeath(temp.getDateOfDeath());
		tempPolicyClaimPropsEntity.setDateOfExit(temp.getDateOfExit());
		tempPolicyClaimPropsEntity.setPlaceOfDeath(temp.getPlaceOfDeath());
		tempPolicyClaimPropsEntity.setReasonForDeathOther(temp.getReasonForDeathOther());	
		tempPolicyClaimPropsEntity.setReasonForDeathId(temp.getReasonForDeathId());
		tempPolicyClaimPropsEntity.setModeOfExit(temp.getModeOfExit());
		tempPolicyClaimPropsEntity.setBatchId(temp.getMemberBatchId());
		tempPolicyClaimPropsEntity.setTmpPolicyId(tempPolicyId);
		tempPolicyClaimPropsEntity.setIsActive(true);
		tempPolicyClaimPropsEntity.setTmpMemberId(tempMemberId);
		tempPolicyClaimPropsEntity.setCreatedDate(new Date());
		tempPolicyClaimPropsEntity.setCreatedBy(temp.getCreatedBy());
		return tempPolicyClaimPropsEntity;
	}
	public static HistoryMPHEntity copytomastertohis(MPHEntity mPHEntity) {
		HistoryMPHEntity hisMPHEntity = new ModelMapper().map(mPHEntity, HistoryMPHEntity.class);
		hisMPHEntity.setId(null);
		
		Set<HistoryMPHAddressEntity> addTempMPHAddressEntity = new HashSet<HistoryMPHAddressEntity>();
		for (MPHAddressEntity mphAddressEntity : mPHEntity.getMphAddresses()) {

			HistoryMPHAddressEntity tempMPHAddressEntity = new ModelMapper().map(mphAddressEntity,
					HistoryMPHAddressEntity.class);
			tempMPHAddressEntity.setId(null);
			
			tempMPHAddressEntity.setMasterMph(hisMPHEntity);
			addTempMPHAddressEntity.add(tempMPHAddressEntity);
		}
		hisMPHEntity.setMphAddresses(addTempMPHAddressEntity);

		Set<HistoryMPHBankEntity> addTempMPHBankEntity = new HashSet<HistoryMPHBankEntity>();

		for (MPHBankEntity mphBankEntity : mPHEntity.getMphBank()) {

			HistoryMPHBankEntity tempMPHBankEntity = new ModelMapper().map(mphBankEntity, HistoryMPHBankEntity.class);
			tempMPHBankEntity.setId(null);
	
			tempMPHBankEntity.setMasterMph(hisMPHEntity);

			addTempMPHBankEntity.add(tempMPHBankEntity);
		}
		hisMPHEntity.setMphBank(addTempMPHBankEntity);

		Set<HistoryMPHRepresentativesEntity> addTempMPHRepresentativeEntity = new HashSet<HistoryMPHRepresentativesEntity>();

		for (MPHRepresentativesEntity mphRepresentivekEntity : mPHEntity.getMphRepresentatives()) {

			HistoryMPHRepresentativesEntity tempMPHRepresentativeEntity = new ModelMapper().map(mphRepresentivekEntity,
					HistoryMPHRepresentativesEntity.class);
			tempMPHRepresentativeEntity.setId(null);
			
			tempMPHRepresentativeEntity.setMasterMph(hisMPHEntity);
			addTempMPHRepresentativeEntity.add(tempMPHRepresentativeEntity);
		}
		hisMPHEntity.setMphRepresentatives(addTempMPHRepresentativeEntity);

		return hisMPHEntity;
	}
	public static MPHEntity copytotemptomaster(TempMPHEntity tempMPHentity) {
		MPHEntity mPHEntity = new ModelMapper().map(tempMPHentity, MPHEntity.class);
		mPHEntity.setId(tempMPHentity.getPmstId());
	
		Set<MPHAddressEntity> addTempMPHAddressEntity = new HashSet<MPHAddressEntity>();
		for (TempMPHAddressEntity mphAddressEntity : tempMPHentity.getMphAddresses()) {

			MPHAddressEntity tempMPHAddressEntity = new ModelMapper().map(mphAddressEntity,
					MPHAddressEntity.class);
			tempMPHAddressEntity.setId(mphAddressEntity.getPmstId());
			
			tempMPHAddressEntity.setMasterMph(mPHEntity);
			addTempMPHAddressEntity.add(tempMPHAddressEntity);
		}
		mPHEntity.setMphAddresses(addTempMPHAddressEntity);

		Set<MPHBankEntity> addTempMPHBankEntity = new HashSet<MPHBankEntity>();

		for (TempMPHBankEntity mphBankEntity : tempMPHentity.getMphBank()) {

			MPHBankEntity tempMPHBankEntity = new ModelMapper().map(mphBankEntity, MPHBankEntity.class);
			tempMPHBankEntity.setId(mphBankEntity.getPmstId());
			
			tempMPHBankEntity.setMasterMph(mPHEntity);

			addTempMPHBankEntity.add(tempMPHBankEntity);
		}
		mPHEntity.setMphBank(addTempMPHBankEntity);

		Set<MPHRepresentativesEntity> addTempMPHRepresentativeEntity = new HashSet<MPHRepresentativesEntity>();

		for (TempMPHRepresentativeEntity mphRepresentivekEntity : tempMPHentity.getMphRepresentatives()) {

			MPHRepresentativesEntity tempMPHRepresentativeEntity = new ModelMapper().map(mphRepresentivekEntity,
					MPHRepresentativesEntity.class);
			tempMPHRepresentativeEntity.setId(mphRepresentivekEntity.getPmstId());		
			tempMPHRepresentativeEntity.setMasterMph(mPHEntity);
			addTempMPHRepresentativeEntity.add(tempMPHRepresentativeEntity);
		}
		mPHEntity.setMphRepresentatives(addTempMPHRepresentativeEntity);

		return mPHEntity;
	}
	
	public static MPHEntity createtotemptomaster(TempMPHEntity tempMPHentity) {
		MPHEntity mPHEntity = new ModelMapper().map(tempMPHentity, MPHEntity.class);
		mPHEntity.setId(null);
	
		Set<MPHAddressEntity> addTempMPHAddressEntity = new HashSet<MPHAddressEntity>();
		for (TempMPHAddressEntity mphAddressEntity : tempMPHentity.getMphAddresses()) {

			MPHAddressEntity tempMPHAddressEntity = new ModelMapper().map(mphAddressEntity,
					MPHAddressEntity.class);
			tempMPHAddressEntity.setId(null);
			
			tempMPHAddressEntity.setMasterMph(mPHEntity);
			addTempMPHAddressEntity.add(tempMPHAddressEntity);
		}
		mPHEntity.setMphAddresses(addTempMPHAddressEntity);

		Set<MPHBankEntity> addTempMPHBankEntity = new HashSet<MPHBankEntity>();

		for (TempMPHBankEntity mphBankEntity : tempMPHentity.getMphBank()) {

			MPHBankEntity tempMPHBankEntity = new ModelMapper().map(mphBankEntity, MPHBankEntity.class);
			tempMPHBankEntity.setId(null);
			
			tempMPHBankEntity.setMasterMph(mPHEntity);

			addTempMPHBankEntity.add(tempMPHBankEntity);
		}
		mPHEntity.setMphBank(addTempMPHBankEntity);

		Set<MPHRepresentativesEntity> addTempMPHRepresentativeEntity = new HashSet<MPHRepresentativesEntity>();

		for (TempMPHRepresentativeEntity mphRepresentivekEntity : tempMPHentity.getMphRepresentatives()) {

			MPHRepresentativesEntity tempMPHRepresentativeEntity = new ModelMapper().map(mphRepresentivekEntity,
					MPHRepresentativesEntity.class);
			tempMPHRepresentativeEntity.setId(null);		
			tempMPHRepresentativeEntity.setMasterMph(mPHEntity);
			addTempMPHRepresentativeEntity.add(tempMPHRepresentativeEntity);
		}
		mPHEntity.setMphRepresentatives(addTempMPHRepresentativeEntity);

		return mPHEntity;
	}
	
	
	
	
	public static List<RenewalLifeCoverTMPEntity> copyToHistoryTmpLifeCoverforClaimbulk(
			List<HistoryLifeCoverEntity> historyLifeCoverEntity, List<MemberCategoryEntity> memberCategoryEntity,
			Long id) {
		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = new ArrayList<RenewalLifeCoverTMPEntity>();
		for (MemberCategoryEntity getNewMemberCategory : memberCategoryEntity) {


				for (HistoryLifeCoverEntity getHistoryLifeCoverEntity : historyLifeCoverEntity) {

					if (getHistoryLifeCoverEntity.getCategoryId().equals(getNewMemberCategory.getId())) {
						RenewalLifeCoverTMPEntity renewalsLifeCoverEntity = RenewalLifeCoveTMPHelper
								.histtoTmp(getHistoryLifeCoverEntity);
						renewalsLifeCoverEntity.setId(null);
						renewalsLifeCoverEntity.setTmpPolicyId(id);
						renewalsLifeCoverEntity.setCategoryId(getNewMemberCategory.getId());
						renewalsLifeCoverEntity.setPmstHisLifeCoverId(getHistoryLifeCoverEntity.getId());
						renewalLifeCoverTMPEntity.add(renewalsLifeCoverEntity);

						break;
					}

				}

			}

		
		return renewalLifeCoverTMPEntity;
	}
	
	
	
	
	public static List<RenewalLifeCoverTMPEntity> copyToTmpLifeCoverforClaimbulk(
			List<PolicyLifeCoverEntity> policyLifeCoverEntity, List<MemberCategoryEntity> getmemberCategoryEntity,
			Long tempPolicyId) {
		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = new ArrayList<RenewalLifeCoverTMPEntity>();
		for (MemberCategoryEntity getNewMemberCategory : getmemberCategoryEntity) {
			for(PolicyLifeCoverEntity getPolicyLifeCoverEntity :policyLifeCoverEntity) {
				if(getPolicyLifeCoverEntity.getCategoryId().equals(getNewMemberCategory.getId())) {
					RenewalLifeCoverTMPEntity renewalsLifeCoverEntity = RenewalLifeCoveTMPHelper
							.pmsttoTmp(getPolicyLifeCoverEntity);
					renewalsLifeCoverEntity.setId(null);
					renewalsLifeCoverEntity.setTmpPolicyId(tempPolicyId);
					renewalsLifeCoverEntity.setPmstLifeCoverId(getPolicyLifeCoverEntity.getId());

					renewalsLifeCoverEntity.setCategoryId(getNewMemberCategory.getId());

					renewalLifeCoverTMPEntity.add(renewalsLifeCoverEntity);
				}
			}
			
		}
		return renewalLifeCoverTMPEntity;
	}
	
	
	public static List<RenewalGratuityBenefitTMPEntity> copyToHistoryGratuityforClaimBulk(
			List<HistoryGratuityBenefitEntity> historyGratuityBenefitEntity,
			List<MemberCategoryEntity> memberCategoryEntity, Long tempPolicyId) {
		List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = new ArrayList<RenewalGratuityBenefitTMPEntity>();

		for (MemberCategoryEntity getNewMemberCategory : memberCategoryEntity) {
		
				for (HistoryGratuityBenefitEntity gethistoryGratuityBenefitEntity : historyGratuityBenefitEntity) {

					if (gethistoryGratuityBenefitEntity.getCategoryId().equals(getNewMemberCategory.getId())) {
						renewalGratuityBenefitTMPEntity.add(RenewalGratuityBenefitTMPHelper
								.histtoTmp(gethistoryGratuityBenefitEntity, tempPolicyId, getNewMemberCategory.getId()));
						gethistoryGratuityBenefitEntity.setIsActive(false);						
						break;
					}

				}
			

		}
		return renewalGratuityBenefitTMPEntity;
	}
	
	public static List<RenewalGratuityBenefitTMPEntity> copyToTmpGratuityforClaimBulk(
			List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity,
			List<MemberCategoryEntity> getmemberCategoryEntity, Long tempPolicyId) {
		List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = new ArrayList<RenewalGratuityBenefitTMPEntity>();

		for (MemberCategoryEntity getNewMemberCategory : getmemberCategoryEntity) {
			for (PolicyGratuityBenefitEntity getpolicyGratuityBenefitEntity : policyGratuityBenefitEntity) {

				if (getpolicyGratuityBenefitEntity.getCategoryId().equals(getNewMemberCategory.getId())) {
					renewalGratuityBenefitTMPEntity.add(RenewalGratuityBenefitTMPHelper
							.pmsttoTmp(getpolicyGratuityBenefitEntity, tempPolicyId, getNewMemberCategory.getId()));
					getpolicyGratuityBenefitEntity.setIsActive(false);
					break;
				}

			}
		}
		
			return renewalGratuityBenefitTMPEntity;
	}
	public static RenewalPolicyTMPMemberEntity copyToTmpIndividualMemberClaimBulk(PolicyMemberEntity policyMemberEntity,
			List<MemberCategoryEntity> memberCategoryEntity, Long tempPolicyId) {

		RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = new ModelMapper().map(policyMemberEntity,
				RenewalPolicyTMPMemberEntity.class);

		renewalPolicyTMPMemberEntity.setId(null);
		renewalPolicyTMPMemberEntity.setPmstMemebrId(policyMemberEntity.getId());
		Set<RenewalPolicyTMPMemberAddressEntity> addTMPMemberAddressEntity = new HashSet<RenewalPolicyTMPMemberAddressEntity>();
		for (PolicyMemberAddressEntity policyMemberAddressEntity : policyMemberEntity.getAddresses()) {

			RenewalPolicyTMPMemberAddressEntity renewalPolicyTMPMemberAddressEntity = new ModelMapper()
					.map(policyMemberAddressEntity, RenewalPolicyTMPMemberAddressEntity.class);
			renewalPolicyTMPMemberAddressEntity.setId(null);
			renewalPolicyTMPMemberAddressEntity.setMember(renewalPolicyTMPMemberEntity);
			addTMPMemberAddressEntity.add(renewalPolicyTMPMemberAddressEntity);
		}
		renewalPolicyTMPMemberEntity.setAddresses(addTMPMemberAddressEntity);
		// end
		// Bank Account
		Set<RenewalPolicyTMPMemberBankAccountEntity> addTMPMemberBankAccountEntity = new HashSet<RenewalPolicyTMPMemberBankAccountEntity>();
		for (PolicyMemberBankAccount PolicyMemberBankAccount : policyMemberEntity.getBankAccounts()) {

			RenewalPolicyTMPMemberBankAccountEntity renewalPolicyTMPMemberBankAccountEntity = new ModelMapper()
					.map(PolicyMemberBankAccount, RenewalPolicyTMPMemberBankAccountEntity.class);
			renewalPolicyTMPMemberBankAccountEntity.setId(null);
			renewalPolicyTMPMemberBankAccountEntity.setMember(renewalPolicyTMPMemberEntity);
			renewalPolicyTMPMemberBankAccountEntity.setPmstMemberBankAccId(PolicyMemberBankAccount.getId());
			addTMPMemberBankAccountEntity.add(renewalPolicyTMPMemberBankAccountEntity);
		}
		renewalPolicyTMPMemberEntity.setBankAccounts(addTMPMemberBankAccountEntity);
		// end

		// Member Appointee
		Set<RenewalPolicyTMPMemberAppointeeEntity> addRenewalPolicyTMPMemberAppointeeEntity = new HashSet<RenewalPolicyTMPMemberAppointeeEntity>();
		for (PolicyMemberAppointeeEntity policyMemberAppointeeEntity : policyMemberEntity.getAppointees()) {

			RenewalPolicyTMPMemberAppointeeEntity renewalPolicyTMPMemberAppointeeEntity = new ModelMapper()
					.map(policyMemberAppointeeEntity, RenewalPolicyTMPMemberAppointeeEntity.class);
			renewalPolicyTMPMemberAppointeeEntity.setId(null);
			renewalPolicyTMPMemberAppointeeEntity.setMember(renewalPolicyTMPMemberEntity);
			renewalPolicyTMPMemberAppointeeEntity.setPmstMemberAppointeeId(policyMemberAppointeeEntity.getId());
			addRenewalPolicyTMPMemberAppointeeEntity.add(renewalPolicyTMPMemberAppointeeEntity);

			PolicyMemberNomineeEntity newPolicyMemberNomineeEntity = policyMemberAppointeeEntity.getNominee();
			RenewalPolicyTMPMemberNomineeEntity renewalPolicyTMPMemberNomineeEntity = new ModelMapper()
					.map(newPolicyMemberNomineeEntity, RenewalPolicyTMPMemberNomineeEntity.class);
			renewalPolicyTMPMemberNomineeEntity.setId(null);
			renewalPolicyTMPMemberNomineeEntity.setMember(renewalPolicyTMPMemberEntity);
			renewalPolicyTMPMemberNomineeEntity.setPmstMemberNomineeId(newPolicyMemberNomineeEntity.getId());
			renewalPolicyTMPMemberAppointeeEntity.setNominee(renewalPolicyTMPMemberNomineeEntity);
			renewalPolicyTMPMemberEntity.getAppointees().add(renewalPolicyTMPMemberAppointeeEntity);

			for (Iterator<PolicyMemberNomineeEntity> iterator = policyMemberEntity.getNominees().iterator(); iterator
					.hasNext();) {
				PolicyMemberNomineeEntity s = iterator.next();
				if (s.getNomineeName() == renewalPolicyTMPMemberAppointeeEntity.getNominee().getNomineeName()
						&& s.getNomineeAadharNumber() == renewalPolicyTMPMemberAppointeeEntity.getNominee()
								.getNomineeAadharNumber()
						&& s.getDateOfBirth() == renewalPolicyTMPMemberAppointeeEntity.getNominee().getDateOfBirth()) {
					iterator.remove();
				}
			}

		}
		renewalPolicyTMPMemberEntity.setAppointees(addRenewalPolicyTMPMemberAppointeeEntity);
		// end

		// Member Nominee
		Set<RenewalPolicyTMPMemberNomineeEntity> addRenewalPolicyTMPMemberNomineeEntity = new HashSet<RenewalPolicyTMPMemberNomineeEntity>();
		for (PolicyMemberNomineeEntity policyMemberNomineeEntity : policyMemberEntity.getNominees()) {

			RenewalPolicyTMPMemberNomineeEntity renewalPolicyTMPMemberNomineeEntity = new ModelMapper()
					.map(policyMemberNomineeEntity, RenewalPolicyTMPMemberNomineeEntity.class);
			renewalPolicyTMPMemberNomineeEntity.setId(null);
			renewalPolicyTMPMemberNomineeEntity.setPmstMemberNomineeId(policyMemberNomineeEntity.getId());
			renewalPolicyTMPMemberNomineeEntity.setMember(renewalPolicyTMPMemberEntity);
			addRenewalPolicyTMPMemberNomineeEntity.add(renewalPolicyTMPMemberNomineeEntity);
		}
		renewalPolicyTMPMemberEntity.setNominees(addRenewalPolicyTMPMemberNomineeEntity);

		renewalPolicyTMPMemberEntity.setModifiedDate(new Date());
		renewalPolicyTMPMemberEntity.setIsActive(true);
		renewalPolicyTMPMemberEntity.setTmpPolicyId(tempPolicyId);
		// old category id = = policymember -category id

		for (MemberCategoryEntity getNew : memberCategoryEntity) {

		

			

					if (policyMemberEntity.getCategoryId().equals(getNew.getId())) {

						renewalPolicyTMPMemberEntity.setCategoryId(getNew.getId());
						break;
					}
				
				
			
		}
		return renewalPolicyTMPMemberEntity;

	}

}
