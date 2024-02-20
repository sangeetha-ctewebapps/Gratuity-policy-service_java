package com.lic.epgs.gratuity.policyservices.conversion.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.helper.PolicyGratuityBenefitHelper;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.HistoryGratutiyBenefitRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.helper.PolicyLifeCoverHelper;
import com.lic.epgs.gratuity.policy.lifecover.repository.HistoryLifeCoverRepository;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.helper.PolicyMemberHelper;
import com.lic.epgs.gratuity.policy.member.repository.HistoryMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMCredibilityFactorDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMExpenseRateDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.entity.AOCMCredibilityFactorEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.entity.AOCMExpenseRateEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.helper.AOCMHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.repository.AOCMCredibilityFactorRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.repository.AOCMExpenseRateRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.service.AOCMService;
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
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.entity.RenewalSchemeruleTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.helper.RenewalSchemeruleTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.repository.RenewalSchemeruleTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.entity.RenewalValuationTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.helper.RenewalValuationTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.repository.RenewalValuationTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationBasicTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationDetailDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationWithdrawalTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.helper.RenewalValuationBasicTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.helper.RenewalValuationMatrixTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationBasicTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationMatrixTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationWithdrawalTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.ValuationBasicHistoryRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.service.RenewalValuationMatrixService;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyHistoryRepository;
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
import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;
import com.lic.epgs.gratuity.simulation.dto.DepositDto;
import com.lic.epgs.gratuity.simulation.entity.DepositEntity;
import com.lic.epgs.gratuity.simulation.repository.DepositRepository;

@Service
public class AOCMCalculationService {

	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

	@Autowired
	private ValuationBasicHistoryRepository valuationBasicHistoryRepository;

	@Autowired
	private AOCMCredibilityFactorRepository aocmCredibilityFactorRepository;

	@Autowired
	private AOCMExpenseRateRepository aocmExpenseRateRepository;

	@Autowired
	private RenewalValuationBasicTMPRepository renewalValuationBasicTMPRepository;

	@Autowired
	private MasterPolicyRepository masterPolicyRepository;
	
	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;

	@Autowired
	private DepositRepository depositRepository;

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
	private GratuityCalculationRepository gratuityCalculationRepository;
	
	@Autowired
	private StandardCodeRepository standardCodeRepository;
	
	@Autowired
	private RenewalValuationMatrixService renewalValuationMatrixService;
	
	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	
	@Transactional
	public Boolean getautocovercalculation(Long policyId,String autoCoverStatus,String userName, String conversionType) {	
		if (autoCoverStatus == "Y") {
			MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(policyId);

			RenewalPolicyTMPEntity renewalPolicyTMPEntity = RenewalPolicyTMPHelper.pmsttoTmp(masterPolicyEntity);
			renewalPolicyTMPEntity.setId(null);

			renewalPolicyTMPEntity.setMasterPolicyId(policyId);

			renewalPolicyTMPEntity.setIsActive(true);
//		renewalPolicyTMPEntity.setCreatedBy();
			renewalPolicyTMPEntity.setCreatedDate(new Date());
//		renewalPolicyTMPEntity.setQuotationNumber(
//				RenewalPolicyTMPHelper.nextQuotationNumber(renewalPolicyTMPRepository.maxQuotationNumber()).toString());

			renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

			// renewalPolicyTMPEntity.getId()

			// scheme MastrePolicy to TMp copy

			Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository.findBypolicyId(policyId);

			if (policySchemeEntity.isPresent()) {

				RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = RenewalSchemeruleTMPHelper
						.pmsttoTmp(policySchemeEntity.get());
				renewalSchemeruleTMPEntity.setId(null);
				renewalSchemeruleTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
				renewalSchemeruleTMPEntity.setPmstSchemeRuleId(policySchemeEntity.get().getId());

				renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);
			}
			List<MemberCategoryEntity> addMemberCategoryEntity = new ArrayList<MemberCategoryEntity>();
			List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository.findBypmstPolicyId(policyId);
			for (MemberCategoryEntity getmemberCategoryEntity : memberCategoryEntity) {
				getmemberCategoryEntity.setPmstTmpPolicy(renewalPolicyTMPEntity.getId());
				addMemberCategoryEntity.add(getmemberCategoryEntity);
			}
			memberCategoryRepository.saveAll(addMemberCategoryEntity);

			List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository.findByPolicyId(policyId);
			List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
					.copyToTmpLifeCoverforClaim(policyLifeCoverEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);

			// member category

			// Gratuity

			List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
					.findBypolicyId(policyId);
			List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
					.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
							renewalPolicyTMPEntity.getId());
			renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);

			// valuation
			Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
					.findByPolicyId(policyId);
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
					.findByPolicyId(policyId);
			if (policyValuationMatrixEntity.isPresent()) {
				RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = RenewalValuationMatrixTMPHelper
						.pmsttoTmp(policyValuationMatrixEntity.get());
				renewalValuationMatrixTMPEntity.setId(null);
				renewalValuationMatrixTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
				renewalValuationMatrixTMPEntity.setPmstValuationMatrixId(policyValuationMatrixEntity.get().getId());
				renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

				if (renewalValuationMatrixTMPEntity.getValuationTypeId() == 1) {
					Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
							.findByPolicyId(policyId);
					if (policyValutationBasicEntity.isPresent()) {
						RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = RenewalValuationBasicTMPHelper
								.pmsttoTmp(policyValutationBasicEntity.get());
						renewalValuationBasicTMPEntity.setId(null);

						renewalValuationBasicTMPEntity
								.setPmstValuationBasicId(policyValutationBasicEntity.get().getId());
						renewalValuationBasicTMPEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
						renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

						// ValuationWithdrawalRate table
						List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
								.findByPolicyId(policyId);
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

			List<RenewalPolicyTMPMemberEntity> addRenewalPolicyTMPMemberEntity = new ArrayList<RenewalPolicyTMPMemberEntity>();
			List<PolicyMemberEntity> listPolicyMemberEntity = policyMemberRepository.findByPolicyId(policyId);
			for (PolicyMemberEntity policyMemberEntity : listPolicyMemberEntity) {
				RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
						.copyToTmpIndividualMemberClaim(policyMemberEntity, addMemberCategoryEntity,
								memberCategoryEntity, renewalPolicyTMPEntity.getId());
				addRenewalPolicyTMPMemberEntity.add(renewalPolicyTMPMemberEntity);

			}
			renewalPolicyTMPMemberRepository.saveAll(addRenewalPolicyTMPMemberEntity);

			renewalPolicyTMPMemberRepository.updateSumAssureforPolicy(renewalPolicyTMPEntity.getId());

			// Start Grat Calculation
 
			RenewalValuationBasicTMPEntity valuationBasicEntity = renewalValuationBasicTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId()).get();
 
//			if (isDevEnvironment == false) {
 
			if (conversionType.equals("AOCM")) {
 
				Double modfdPremRateCrdbiltyFctr = 0.0;
 
				if (valuationBasicEntity.getStdPremRateCrdbiltyFctr() != null && valuationBasicEntity.getStdPremRateCrdbiltyFctr() !=0) {
					modfdPremRateCrdbiltyFctr = valuationBasicEntity.getStdPremRateCrdbiltyFctr();
				} else {
					modfdPremRateCrdbiltyFctr = valuationBasicEntity.getModfdPremRateCrdbiltyFctr();
				}
 
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
 
			} else if (conversionType.equals("SuggestiveUnderwriting")) {
 
				gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
 
			}
				
//			} else {
//
//				gratuityCalculationRepository.calculateGratuityRenewal1(renewalPolicyTMPEntity.getId());
//				gratuityCalculationRepository.calculateGratuityRenewal2(renewalPolicyTMPEntity.getId());
//				gratuityCalculationRepository.calculateGratuityRenewal3(renewalPolicyTMPEntity.getId(),
//						autoCoverStatus);
//
//			}
			List<GratuityCalculationEntity> gratuityCalculationEntities = gratuityCalculationRepository
					.findAllByTmpMemberId(renewalPolicyTMPEntity.getId());
			Optional<RenewalValuationMatrixTMPEntity> valuationMatrixEntity = renewalValuationMatrixTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
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
			RenewalValuationBasicTMPEntity newValuationBasicEntity = renewalValuationBasicTMPRepository
					.save(valuationBasicEntity);

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
			List<RenewalPolicyTMPMemberEntity> memberEntity = new ArrayList<RenewalPolicyTMPMemberEntity>();
			for (GratuityCalculationEntity gratuityCalculationEntity : gratuityCalculationEntities) {

				RenewalPolicyTMPMemberEntity getMemberEntity = renewalPolicyTMPMemberRepository
						.findById(gratuityCalculationEntity.getMemberId()).get();
				totalSumAssured = totalSumAssured + (gratuityCalculationEntity.getLcSumAssured() != null
						? gratuityCalculationEntity.getLcSumAssured()
						: 0.0D);
				premium = premium
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
				pastServiceLiability = pastServiceLiability
						+ (gratuityCalculationEntity.getPastServiceBenefitDeath() != null
								? gratuityCalculationEntity.getPastServiceBenefitDeath()
								: 0.0D)
						+ (gratuityCalculationEntity.getPastServiceBenefitRet() != null
								? gratuityCalculationEntity.getPastServiceBenefitRet()
								: 0.0D)
						+ (gratuityCalculationEntity.getPastServiceBenefitWdl() != null
								? gratuityCalculationEntity.getPastServiceBenefitWdl()
								: 0.0D);

				getMemberEntity.setLifeCoverPremium(gratuityCalculationEntity.getLcPremium());
				getMemberEntity.setLifeCoverSumAssured(gratuityCalculationEntity.getLcSumAssured());
				getMemberEntity.setAccruedGratuity(gratuityCalculationEntity.getAccruedGra());
				getMemberEntity.setTotalGratuity(gratuityCalculationEntity.getTotalGra());
				getMemberEntity.setGratCalculationId(gratuityCalculationEntity.getId());
				renewalPolicyTMPMemberRepository.save(getMemberEntity);
			}
			newValuationMatrixEntity.setValuationTypeId(1L);
			newValuationMatrixEntity.setValuationDate(valuationBasicEntity.getValuationEffectivDate());
			newValuationMatrixEntity.setCurrentServiceLiability(currentServiceLiability);
			newValuationMatrixEntity.setPastServiceLiability(pastServiceLiability);
			newValuationMatrixEntity.setFutureServiceLiability(futureServiceLiability);
			newValuationMatrixEntity.setPremium(premium);
			newValuationMatrixEntity.setGst(premium * gst);
			newValuationMatrixEntity.setTotalPremium(premium + (premium * gst));
			newValuationMatrixEntity.setAmountPayable(premium + (premium * gst) + currentServiceLiability
					+ pastServiceLiability + futureServiceLiability);
			newValuationMatrixEntity.setAmountReceived(0.0D);
			newValuationMatrixEntity.setBalanceToBePaid(
					newValuationMatrixEntity.getAmountPayable() - newValuationMatrixEntity.getAmountReceived());
			newValuationMatrixEntity.setTotalSumAssured(totalSumAssured);

			renewalValuationMatrixTMPRepository.save(newValuationMatrixEntity);

			// End Of Grat Cal Values
			MasterPolicyEntity getMasterPolicyEntity = policyMastertoPolicyHis(
					renewalPolicyTMPEntity.getMasterPolicyId(), userName);

			renewalPolicyTMPRepository.updateTmpPolicyInactive((renewalPolicyTMPEntity.getId()));

			return true;
		}
		
		else
		{
			// gratuityCalculationRepository.calculateGratuityRenewal(policyId,autoCoverStatus);
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(policyId).get();

			// Start Grat Calculation
 
			RenewalValuationBasicTMPEntity valuationBasicEntity = renewalValuationBasicTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId()).get();
 
//			if (isDevEnvironment == false) {
 
			if (conversionType.equals("AOCM")) {
 
				Double modfdPremRateCrdbiltyFctr = 0.0;
 
				if (valuationBasicEntity.getStdPremRateCrdbiltyFctr() != null && valuationBasicEntity.getStdPremRateCrdbiltyFctr() !=0) {
					modfdPremRateCrdbiltyFctr = valuationBasicEntity.getStdPremRateCrdbiltyFctr();
				} else {
					modfdPremRateCrdbiltyFctr = valuationBasicEntity.getModfdPremRateCrdbiltyFctr();
				}
 
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
 
			} else if (conversionType.equals("SuggestiveUnderwriting")) {
 
				gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
				gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
						valuationBasicEntity.getRateTable());
 
			}
//			} else {
//
//				gratuityCalculationRepository.calculateGratuityRenewal1(renewalPolicyTMPEntity.getId());
//				gratuityCalculationRepository.calculateGratuityRenewal2(renewalPolicyTMPEntity.getId());
//				gratuityCalculationRepository.calculateGratuityRenewal3(renewalPolicyTMPEntity.getId(),
//						autoCoverStatus);
//
//			}
			List<GratuityCalculationEntity> gratuityCalculationEntities = gratuityCalculationRepository
					.findAllByTmpMemberId(policyId);

			Optional<RenewalValuationMatrixTMPEntity> valuationMatrixEntity = renewalValuationMatrixTMPRepository
					.findBytmpPolicyId(policyId);
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
			RenewalValuationBasicTMPEntity newValuationBasicEntity = renewalValuationBasicTMPRepository
					.save(valuationBasicEntity);

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
			List<RenewalPolicyTMPMemberEntity> memberEntity = new ArrayList<RenewalPolicyTMPMemberEntity>();
			for (GratuityCalculationEntity gratuityCalculationEntity : gratuityCalculationEntities) {

				RenewalPolicyTMPMemberEntity getMemberEntity = renewalPolicyTMPMemberRepository
						.findById(gratuityCalculationEntity.getMemberId()).get();
				totalSumAssured = totalSumAssured + (gratuityCalculationEntity.getLcSumAssured() != null
						? gratuityCalculationEntity.getLcSumAssured()
						: 0.0D);
				premium = premium
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
				pastServiceLiability = pastServiceLiability
						+ (gratuityCalculationEntity.getPastServiceBenefitDeath() != null
								? gratuityCalculationEntity.getPastServiceBenefitDeath()
								: 0.0D)
						+ (gratuityCalculationEntity.getPastServiceBenefitRet() != null
								? gratuityCalculationEntity.getPastServiceBenefitRet()
								: 0.0D)
						+ (gratuityCalculationEntity.getPastServiceBenefitWdl() != null
								? gratuityCalculationEntity.getPastServiceBenefitWdl()
								: 0.0D);

				getMemberEntity.setLifeCoverPremium(gratuityCalculationEntity.getLcPremium());
				getMemberEntity.setLifeCoverSumAssured(gratuityCalculationEntity.getLcSumAssured());
				getMemberEntity.setAccruedGratuity(gratuityCalculationEntity.getAccruedGra());
				getMemberEntity.setTotalGratuity(gratuityCalculationEntity.getTotalGra());
				getMemberEntity.setGratCalculationId(gratuityCalculationEntity.getId());
				renewalPolicyTMPMemberRepository.save(getMemberEntity);
			}
			newValuationMatrixEntity.setValuationTypeId(1L);
			newValuationMatrixEntity.setValuationDate(valuationBasicEntity.getValuationEffectivDate());
			newValuationMatrixEntity.setCurrentServiceLiability(currentServiceLiability);
			newValuationMatrixEntity.setPastServiceLiability(pastServiceLiability);
			newValuationMatrixEntity.setFutureServiceLiability(futureServiceLiability);
			newValuationMatrixEntity.setPremium(premium);
			newValuationMatrixEntity.setGst(premium * gst);
			newValuationMatrixEntity.setTotalPremium(premium + (premium * gst));
			newValuationMatrixEntity.setAmountPayable(premium + (premium * gst) + currentServiceLiability
					+ pastServiceLiability + futureServiceLiability);
			newValuationMatrixEntity.setAmountReceived(0.0D);
			newValuationMatrixEntity.setBalanceToBePaid(
					newValuationMatrixEntity.getAmountPayable() - newValuationMatrixEntity.getAmountReceived());
			newValuationMatrixEntity.setTotalSumAssured(totalSumAssured);

			renewalValuationMatrixTMPRepository.save(newValuationMatrixEntity);

			// End Of Grat Cal Values

			return true;
		}		
		
}

	private MasterPolicyEntity policyMastertoPolicyHis(Long masterPolicyId, String username) {
		MasterPolicyEntity newmasterPolicyEntity = null;
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = null;
		PolicyHistoryEntity policyHistoryEntity = null;
		// PolicyMaster to Copy History then Tmp Policy to Master Policy Update
		Optional<MasterPolicyEntity> masterPolicyEntity = masterPolicyRepository.findById(masterPolicyId);
		if (masterPolicyEntity.isPresent()) {
			renewalPolicyTMPEntity = renewalPolicyTMPRepository.findBymasterPolicyId(masterPolicyId);
			policyHistoryEntity = policyHistoryRepository.save(
					PolicyHelper.policymasterToHisTransfer(masterPolicyEntity.get(), renewalPolicyTMPEntity, username));

			newmasterPolicyEntity = PolicyHelper.updateTemptoPolicyMaster(renewalPolicyTMPEntity, username);

			masterPolicyRepository.save(newmasterPolicyEntity);
		}

		// PolicymasterSchemeRule to Copy History scheme rule then Update TmpScheme to
		// PolicyScheme rule
		Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository.findBypolicyId(masterPolicyId);
		if (policySchemeEntity.isPresent()) {

			policySchemeRuleHistoryRepository
					.save(PolicySchemeRuleHelper.policyMastertoHistransfer(policySchemeEntity.get(),
							policyHistoryEntity.getId(), username));

			RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = renewalSchemeruleTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getTmpPolicyId());
			PolicySchemeEntity newPolicySchemeEntity = PolicySchemeRuleHelper
					.updateTempToSchemeMaster(renewalSchemeruleTMPEntity, username);
			policySchemeRuleRepository.save(newPolicySchemeEntity);
		}

		// PolicymasterLifeCoverRule to Copy HistoryLifeCOver then Update TmpLifeCover
		// to PolicymasterLifeCoverRule

		List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository.findByPolicyId(masterPolicyId);
		List<PolicyLifeCoverEntity> policyLifeCoverEntities = new ArrayList<PolicyLifeCoverEntity>();
		for (PolicyLifeCoverEntity addPolicyLifeCoverEntity : policyLifeCoverEntity) {

			historyLifeCoverRepository.save(PolicyLifeCoverHelper
					.policyMastertoHistransfer(addPolicyLifeCoverEntity, policyHistoryEntity.getId(), username));
		}

		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = renewalLifeCoverTMPRepository
				.findBytmpPolicyId(renewalPolicyTMPEntity.getTmpPolicyId());
		for (RenewalLifeCoverTMPEntity addrenewalLifeCoverTMPEntity : renewalLifeCoverTMPEntity) {
			PolicyLifeCoverEntity newPolicyLifeCoverEntity = PolicyLifeCoverHelper
					.updateTempToLifecoverMaster(addrenewalLifeCoverTMPEntity, username,masterPolicyId);
			policyLifeCoverEntities.add(newPolicyLifeCoverEntity);
		}
		policyLifeCoverRepository.saveAll(policyLifeCoverEntities);

		// PolicMasterValuation to copy ValuationHistory upodate RenewalvaluationTMP to
		// PolicMasterValuation
		Optional<PolicyMasterValuationEntity> policyMasterValuationEntity = policyMasterValuationRepository
				.findBypolicyId(masterPolicyId);
		if (policyMasterValuationEntity.isPresent()) {
			policyValuationHistoryRepository
					.save(PolicyValuationHelper.policyMastertoHistransfer(policyMasterValuationEntity.get(),
							policyHistoryEntity.getId(), username));

			RenewalValuationTMPEntity renewalValuationTMPEntity = renewalValuationTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getTmpPolicyId());
			PolicyMasterValuationEntity newPolicyMasterValuationEntity = PolicyValuationHelper
					.updateTempToValuationMaster(renewalValuationTMPEntity, username);
			policyMasterValuationRepository.save(newPolicyMasterValuationEntity);
		}

		// PolicyValuationMastxic to copy valuationmatrixHistroy renewalvaluationmatric
		// to PolicyValuationMastxic
		Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
				.findByPolicyId(masterPolicyId);
		if (policyValuationMatrixEntity.isPresent()) {
			policyValuationMatrixHistoryRepository
					.save(PolicyValuationMatrixHelper.policyMastertoHistransfer(policyValuationMatrixEntity.get(),
							policyHistoryEntity.getId(), username));

			RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
					.findAllBytmpPolicyId(renewalPolicyTMPEntity.getTmpPolicyId());
			PolicyValuationMatrixEntity newPolicyValuationMatrixEntity = PolicyValuationMatrixHelper
					.updateTempToValuationMatrixMaster(renewalValuationMatrixTMPEntity, username);
			policyValuationMatrixRepository.save(newPolicyValuationMatrixEntity);
		}

		// PolicValuationBasic to copy PolicValuationBasicHistory upodate
		// RenewalvaluationBasicTMP to PolicValuationBasic

		Optional<PolicyValutationBasicEntity> policyValuationBasicEntity = policyValuationBasicRepository
				.findBypolicyId(masterPolicyId);
		if (policyValuationBasicEntity.isPresent()) {
			policyValuationBasicHistoryRepository
					.save(PolicyValuationMatrixHelper.policyMastertoHistransfer(policyValuationBasicEntity.get(),
							policyHistoryEntity.getId(), username));

			RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = renewalValuationBasicTMPRepository
					.findAllBytmpPolicyId(renewalPolicyTMPEntity.getTmpPolicyId());
			PolicyValutationBasicEntity newPolicyValuationBasicEntity = PolicyValuationMatrixHelper
					.updateTempToValuationBasicMaster(renewalValuationBasicTMPEntity, username);
			policyValuationBasicRepository.save(newPolicyValuationBasicEntity);
		}

		// PolicyWithDraw copy to HistoryWidth renewalvaluationwidthdraw to
		// policywithdrwal
		List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
				.findByPolicyId(masterPolicyId);
		List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntities = new ArrayList<PolicyValuationWithdrawalRateEntity>();
		for (PolicyValuationWithdrawalRateEntity addPolicyValuationWithdrawalRateEntity : policyValuationWithdrawalRateEntity) {

			policyValuationWithdrawalRateHistoryRepository
					.save(PolicyValuationMatrixHelper.policyMastertoHistransfer(addPolicyValuationWithdrawalRateEntity,
							policyHistoryEntity.getId(), username));
		}

		List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = renewalValuationWithdrawalTMPRepository
				.findBytmpPolicyId(renewalPolicyTMPEntity.getTmpPolicyId());
		for (RenewalValuationWithdrawalTMPEntity addrenewalValuationWithdrawalTMPEntity : renewalValuationWithdrawalTMPEntity) {
			PolicyValuationWithdrawalRateEntity newPolicyValuationWithdrawalRateEntity = PolicyValuationMatrixHelper
					.updateTempToLifecoverMaster(addrenewalValuationWithdrawalTMPEntity, username);
			policyValuationWithdrawalRateEntities.add(newPolicyValuationWithdrawalRateEntity);
		}
		policyValuationWithdrawalRateRepository.saveAll(policyValuationWithdrawalRateEntities);

		List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
				.findBypolicyId(masterPolicyId);
		List<PolicyGratuityBenefitEntity> PolicyGratuityBenefitEntities = new ArrayList<PolicyGratuityBenefitEntity>();

		if (policyGratuityBenefitEntity.size() > 0) {
			List<HistoryGratuityBenefitEntity> historyGratuityBenefitEntity = new ArrayList<HistoryGratuityBenefitEntity>();
			for (PolicyGratuityBenefitEntity getpolicyGratuityBenefitEntity : policyGratuityBenefitEntity) {
				historyGratuityBenefitEntity.add(PolicyGratuityBenefitHelper.policyMastertoHistransfer(
						getpolicyGratuityBenefitEntity, policyHistoryEntity.getId(), username));
			}
			historyGratutiyBenefitRepository.saveAll(historyGratuityBenefitEntity);

			List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = renewalGratuityBenefitTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getTmpPolicyId());
			for (RenewalGratuityBenefitTMPEntity addrenewalGratuityBenefitTMPEntity : renewalGratuityBenefitTMPEntity) {
				PolicyGratuityBenefitEntity newPolicyGratuityBenefitEntity = RenewalGratuityBenefitTMPHelper
						.updateTempToPolicyGratuityBenefitMaster(addrenewalGratuityBenefitTMPEntity, username,masterPolicyId);
				PolicyGratuityBenefitEntities.add(newPolicyGratuityBenefitEntity);
			}
			policyGratuityBenefitRepository.saveAll(PolicyGratuityBenefitEntities);

		}
		// Member Renewal Entity start
		List<RenewalPolicyTMPMemberEntity> tempMemberEntity = renewalPolicyTMPMemberRepository
				.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
		for (RenewalPolicyTMPMemberEntity newTempMemberEntity : tempMemberEntity) {
			PolicyMemberEntity policyMemberEntity = policyMemberRepository
					.findById(newTempMemberEntity.getPmstMemebrId()).get();
			historyMemberRepository
					.save(PolicyMemberHelper.policymastertohistoryupdate(policyMemberEntity,policyHistoryEntity.getId(), username));

			policyMemberEntity = RenewalPolicyTMPMemberHelper.updateTemptoPolicyMemberMaster(newTempMemberEntity,
					username);
			policyMemberRepository.save(policyMemberEntity);
		}

		// End

		return newmasterPolicyEntity;

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

}
