package com.lic.epgs.gratuity.policyservices.contributionadjustment.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.accountingservice.dto.GstDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.JournalVoucherDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.NewBusinessContributionAndLifeCoverAdjustmentDto;
import com.lic.epgs.gratuity.accountingservice.dto.RenwalWithGIDebitCreditRequestModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.UnlockDepositDetailDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;

import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.service.CommonService;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.fund.service.FundService;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.repository.HistoryMPHRepository;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.dto.ClaimPropSearchDetailDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimPropsDto;
import com.lic.epgs.gratuity.policy.claim.entity.PolicyTempSearchEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempMemberSearchEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempMphSearchEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimPropsSearchEntity;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimHelper;
import com.lic.epgs.gratuity.policy.claim.repository.TempPolicyClaimPropsRepository;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositLockDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContributionEntity;
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
import com.lic.epgs.gratuity.policy.renewal.helper.RenewalHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.helper.RenewalPolicyTMPHelper;
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
import com.lic.epgs.gratuity.policy.repository.MasterPolicyAdjustmentDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyAdjustmentDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyHistoryRepository;
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
import com.lic.epgs.gratuity.policyservices.contributionadjustment.dto.ContributionAdjustmentPropsDto;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.dto.ContributionAdjustmentSearchDto;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.entity.ContributionAdjustmentPropsEntity;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.entity.ContributionAdjustmentSearchPropsEntity;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.helper.ContriAdjustHelper;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.repository.ContributionAdjustmentPropsRepository;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.service.ContributionAdjustmentService;
import com.lic.epgs.gratuity.simulation.dto.DepositAdjustementDto;

@Service
public class ContributionAdjustmentServiceImpl implements ContributionAdjustmentService {
	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private StandardCodeRepository standardCodeRepository;
	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;
	
	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	private Long contributionDraftStatusId=513l;
	private Long contributionsenttocheckerORPendingForApproveStatusId=504l;
	private Long contributionSendtoBacktoMakerStatusId=505L;
	private Long contributionRejectedStatusId=506L;
	private Long contributionApproveStatusId=507l;
	
	
	@Autowired
	private AccountingService accountingService;
	
	@Autowired
	private MasterPolicyAdjustmentDetailRepository masterPolicyAdjustmentDetailRepository;
	@Autowired
	private PolicyAdjustmentDetailRepository  policyAdjustmentDetailRepository;

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
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CommonModuleService commonModuleService;

	@Autowired
	private RenewalGratuityBenefitTMPRepository renewalGratuityBenefitTMPRepository;

	@Autowired
	private PolicyValuationMatrixRepository policyValuationMatrixRepository;

	@Autowired
	private RenewalValuationMatrixTMPRepository renewalValuationMatrixTMPRepository;

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
	private PolicyServiceRepository policyServiceRepository;

	@Autowired
	private TempPolicyClaimPropsRepository tempPolicyClaimPropsRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	PolicyHistoryRepository policyHistoryRepository;

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
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private CommonMasterStateRepository commonMasterStateRepository;
	
	@Autowired
	private ContributionAdjustmentPropsRepository contributionAdjustmentPropsRepository;
	
	@Autowired
	private MasterPolicyRepository masterPolicyRepository;
	
	@Autowired
	private MemberCategoryModuleRepository memberCategoryModuleRepository;

	@Autowired
	private PolicyContributionDetailRepository policyContributionDetailRepository;

	@Autowired
	private PolicyDepositRepository policyDepositRepository;

	@Autowired
	private PolicyContributionRepository policyContributionRepository;

	@Autowired
	private PolicyContrySummaryRepository policyContrySummaryRepository;


	@Autowired
	private MasterPolicyDepositRepository masterPolicyDepositRepository;

	@Autowired
	private MasterPolicyContributionRepository masterPolicyContributionRepository;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MasterPolicyContributionDetailRepository masterPolicyContributionDetailRepository;
	
	@Transactional
	@Override
	public ApiResponseDto<ContributionAdjustmentPropsDto> createcontributionadjustment(
			PolicyContributionDetailDto policyContributionDetailDto, Long pmstPolicyId) {

		int countExitingContriAdj = contributionAdjustmentPropsRepository
				.findpmstpolicyexitforcontriadjust(pmstPolicyId);
		if (countExitingContriAdj > 0) {
			return ApiResponseDto.errorMessage(null, null, "already existed");
		}
		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findById(pmstPolicyId).get();

		RenewalPolicyTMPEntity renewalPolicyTMPEntity = mastertoTmpforPolicyService(masterPolicyEntity);

		Long masterTmpPolicyId = renewalPolicyTMPEntity.getId();
		Double getBalance = null;
		Double adjustementAmount = 0.0;
		int year = 0;
		int month = 0;
		String financialYear = null;
		financialYear=DateUtils.getFinancialYear(new Date());

//		TaskProcessEntity taskProcessEntity =taskProcessRepository.findByProcessName("RENEWAL PROCESSING");

		PolicyContributionDetailEntity policyContributionDetailEntity = new ModelMapper()
				.map(policyContributionDetailDto, PolicyContributionDetailEntity.class);
		policyContributionDetailEntity.setTmpPolicyId(masterTmpPolicyId);
		policyContributionDetailEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
		policyContributionDetailEntity.setCreatedDate(new Date());
		policyContributionDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
		policyContributionDetailEntity.setEntryType("CA");
		policyContributionDetailEntity.setAdjustmentForDate(new Date());
		policyContributionDetailEntity.setActive(true);
		policyContributionDetailEntity.setFinancialYear(financialYear);
		policyContributionDetailEntity.setPastServiceBalance(policyContributionDetailDto.getPastServiceBalance());					
		policyContributionDetailEntity.setCurrentServiceBalance(policyContributionDetailDto.getCurrentServiceBalance());
		policyContributionDetailEntity = policyContributionDetailRepository.save(policyContributionDetailEntity);

		masterTmpPolicyId = policyContributionDetailEntity.getTmpPolicyId();

//		TaskAllocationEntity taskAllocationEntity = taskAllocationRepository.findByRequestId(renewalPolicyTMPEntity.getPolicyNumber());;

		// master contributio detail

		adjustementAmount = policyContributionDetailEntity.getCurrentServices()
				+ policyContributionDetailEntity.getPastService();

		List<ShowDepositLockDto> showDepositLockDto = new ArrayList<ShowDepositLockDto>();
		List<Date> collectionDate = new ArrayList<>();
		for (DepositAdjustementDto getDepositAdjustementDto : policyContributionDetailDto.getDepositAdjustmentDto()) {
			if (adjustementAmount != 0) {
				getDepositAdjustementDto.setDepositAmount(getDepositAdjustementDto.getBalanceAmount());
				PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

				PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();

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
				policyDepositEntity.setActive(true);
				Random r = new Random(System.currentTimeMillis());
				int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
				policyDepositEntity.setChallanNo(policyContributionDetailEntity.getChallanNo());
				policyDepositEntity.setCollectionDate(new Date());
				collectionDate.add(policyDepositEntity.getCollectionDate());
				policyDepositEntity.setZeroId(null);
				policyDepositEntity.setChequeRealistionDate(getDepositAdjustementDto.getVoucherEffectiveDate());
				policyDepositEntity.setTransactionMode(getDepositAdjustementDto.getCollectionMode());
				policyDepositEntity.setStatus(null);
				policyDepositEntity.setRegConId(null);
				policyDepositEntity.setTmpPolicyId(masterTmpPolicyId);
				policyDepositEntity.setContributionType("CA");
				policyDepositEntity.setContributionDetailId(policyContributionDetailEntity.getId());
				;
				policyDepositEntity.setAdjConId(null);
				policyDepositEntity.setDepositAmount(getDepositAdjustementDto.getDepositAmount());
				policyDepositEntity.setCreatedBy(getDepositAdjustementDto.getCreatedBy());
				policyDepositEntity.setCreateDate(new Date());
				policyDepositEntity.setIsDeposit(true);
				policyDepositEntity.setRemark(getDepositAdjustementDto.getRemarks());
				policyDepositEntity.setCollectionNo(getDepositAdjustementDto.getCollectionNumber());
				policyDepositEntity.setCollectionStatus(getDepositAdjustementDto.getCollectionStatus());
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
				;
			}
		}

	
		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository
				.findBycontributionDetailId(policyContributionDetailEntity.getId());

		List<PolicyContributionEntity> policyContributionEntity = new ArrayList<PolicyContributionEntity>();
		List<PolicyAdjustmentDetailEntity> policyAdjustmentDetail = new ArrayList<PolicyAdjustmentDetailEntity>();
		Double depositAmount = 0.0;
		Long depositId = 0l;
		Double currentAmount = 0.0;
		Double closingBalance=0.0;
		Double openingBalance=policyContributionRepository.getLastClosingBalance(pmstPolicyId)!=null?policyContributionRepository.getLastClosingBalance(pmstPolicyId):0.0;
		
		
		
//		Long currentServiceAdjustedLong = Math.round(policyContributionDetailEntity.getCurrentServices());
//		Double currentServiceAdjusted = currentServiceAdjustedLong.doubleValue();
//		Long pastServiceAdjustedLong = Math.round(policyContributionDetailEntity.getPastService());
//		Double pastServiceAdjusted = pastServiceAdjustedLong.doubleValue();
		
		
		Double currentServiceAdjusted=policyContributionDetailEntity.getCurrentServices();


		Double pastServiceAdjusted = policyContributionDetailEntity.getPastService();
		adjustementAmount = currentServiceAdjusted + pastServiceAdjusted;
		policyContributionDetailDto
				.setDepositAdjustmentDto(sort(policyContributionDetailDto.getDepositAdjustmentDto()));

		Long versionNumber = masterPolicyContributionRepository.getMaxVersion(financialYear,
				pmstPolicyId);
		versionNumber = versionNumber == null ? 01 : versionNumber + 1;
		
		
		for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {
			if (adjustementAmount > 0) {
				Double currentAmountforContribution = 0.0;
				depositAmount = policyDepositEntity.getAdjustmentAmount(); 
				depositId = policyDepositEntity.getId();
				// LIFE PREMIUM NO
			

				// GST AMOUNT NO


				// SinglePremium

				if ( pastServiceAdjusted > 0
						&& depositAmount > 0) {
					if (pastServiceAdjusted >= depositAmount) {
						currentAmount = depositAmount;

						pastServiceAdjusted = pastServiceAdjusted - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
//						policyAdjustmentDetail.add(this.saveAdjustmentdata(
//								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//								"SinglePremium", "CA", policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy()));
						
						PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
								"SinglePremium", "CA", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
								
								
								currentAmountforContribution += currentAmount;
								closingBalance=openingBalance+currentAmountforContribution;
								policyContributionEntity.add(
										this.saveContributionDetail(masterTmpPolicyId, policyDepositEntity.getContributionDetailId(),
												"CA", currentAmount, financialYear, versionNumber,
												commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
												policyDepositEntity.getChequeRealistionDate(),
												policyContributionDetailDto.getCreatedBy(), policyDepositEntity.getCollectionDate(),Long.toString(policyDepositEntity.getCollectionNo()),
												policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
						versionNumber=versionNumber+1;
						openingBalance=+closingBalance;
						depositAmount = 0.0;
					} else {
						currentAmount = pastServiceAdjusted;

						adjustementAmount = adjustementAmount - currentAmount;
						PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
								"SinglePremium", "CA", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						
//						policyAdjustmentDetail.add(this.saveAdjustmentdata(
//								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//								"SinglePremium", "CA", policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - pastServiceAdjusted;
						currentAmountforContribution += currentAmount;
						closingBalance=openingBalance+currentAmountforContribution;
						policyContributionEntity.add(
								this.saveContributionDetail(masterTmpPolicyId, policyDepositEntity.getContributionDetailId(),
										"CA", currentAmount, financialYear, versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(), policyDepositEntity.getCollectionDate(),
										Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
				versionNumber=versionNumber+1;
				openingBalance=+closingBalance;
						pastServiceAdjusted = 0.0;
					}
				}

				// FirstPremium

				if (pastServiceAdjusted == 0
						&& currentServiceAdjusted > 0 && depositAmount > 0) {
					if (currentServiceAdjusted >= depositAmount) {
						currentAmount = depositAmount;

						currentServiceAdjusted = currentServiceAdjusted - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
								"FirstPremium", "CA", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
//						policyAdjustmentDetail.add(this.saveAdjustmentdata(
//								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//								"FirstPremium", "CA", policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy()));
						
						
						currentAmountforContribution += currentAmount;
						closingBalance=openingBalance+currentAmountforContribution;
					policyContributionEntity.add(
						this.saveContributionDetail(masterTmpPolicyId, policyDepositEntity.getContributionDetailId(),
								"CA", currentAmount, financialYear, versionNumber,
								commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
								policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy(), policyDepositEntity.getCollectionDate(),
								Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
					versionNumber=versionNumber+1;
					openingBalance=+closingBalance;
						depositAmount = 0.0;
					} else {
						currentAmount = currentServiceAdjusted;

						adjustementAmount = adjustementAmount - currentAmount;
//						policyAdjustmentDetail.add(this.saveAdjustmentdata(
//								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//								"FirstPremium", "CA", policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy()));
						
						PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
								"FirstPremium", "CA", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - currentServiceAdjusted;
						currentAmountforContribution += currentAmount;
						closingBalance=openingBalance+currentAmountforContribution;
						policyContributionEntity.add(
								this.saveContributionDetail(masterTmpPolicyId, policyDepositEntity.getContributionDetailId(),
										"CA", currentAmount, financialYear, versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(),
										policyDepositEntity.getCollectionDate(),Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
				versionNumber=versionNumber+1;
				openingBalance=+closingBalance;
						currentServiceAdjusted = 0.0;
					}
				}
//				policyContributionEntity.add(
//						this.saveContributionDetail(masterTmpPolicyId, policyDepositEntity.getContributionDetailId(),
//								"CA", currentAmountforContribution, financialYear, versionNumber,
//								commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
//								policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy(), policyDepositEntity.getCollectionDate(),Long.toString(policyDepositEntity.getCollectionNo()),savePolicyAdjustmentDetailEntity.getId())));
//				versionNumber = versionNumber + 1;
			}
		}
		policyContributionRepository.saveAll(policyContributionEntity);
//		policyAdjustmentDetailRepository.saveAll(policyAdjustmentDetail);

		if (isDevEnvironment == false) {
			accountingService.lockDeposits(showDepositLockDto, policyContributionDetailDto.getCreatedBy());
		}

		Date risk = Collections.max(collectionDate);
		renewalPolicyTMPEntity.setRiskCommencementDate(risk);
		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

//     		if(taskAllocationEntity!=null) {
//     		taskAllocationEntity.setRequestId(renewalPolicyTMPEntity.getPolicyNumber());
//     		taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//     	//	taskAllocationEntity.setLocationType(policyContributionDetailDto.getUnitCode());
//     		taskAllocationEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
//     		taskAllocationEntity.setCreatedDate(new Date());
//     		taskAllocationEntity.setTaskStatus(defaultStatusId);
//     		taskAllocationEntity.setModulePrimaryId(renewalPolicyTMPEntity.getId());
//     		taskAllocationEntity.setIsActive(true);
		//
//     		taskAllocationRepository.save(taskAllocationEntity);
//     		}else {
//     			TaskAllocationEntity newTaskAllocationEntity =new TaskAllocationEntity();
//     			newTaskAllocationEntity.setRequestId(renewalPolicyTMPEntity.getPolicyNumber());
//     			newTaskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//     		//	taskAllocationEntity.setLocationType(policyContributionDetailDto.getUnitCode());
//     			newTaskAllocationEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
//     			newTaskAllocationEntity.setCreatedDate(new Date());
//     			newTaskAllocationEntity.setTaskStatus(defaultStatusId);
//     			newTaskAllocationEntity.setModulePrimaryId(renewalPolicyTMPEntity.getId());
//     			newTaskAllocationEntity.setIsActive(true);
//     		
//     			taskAllocationRepository.save(newTaskAllocationEntity);
//     		}
		ContributionAdjustmentPropsEntity contributionAdjustmentPropsEntity =null;
		try {
			 contributionAdjustmentPropsEntity = new ContributionAdjustmentPropsEntity();
			contributionAdjustmentPropsEntity.setAmtToBeAdjusted(policyContributionDetailEntity.getCurrentServices()
					+ policyContributionDetailEntity.getPastService());
			contributionAdjustmentPropsEntity.setPstgContriDetailId(policyContributionDetailEntity.getId());
			contributionAdjustmentPropsEntity.setTmpPolicyId(masterTmpPolicyId);
			contributionAdjustmentPropsEntity.setContriAdjNumber(commonService.getSequence(HttpConstants.CONTRIBUTION_ADJUSTMENT));		
			contributionAdjustmentPropsEntity.setFirstPremiumPS(policyContributionDetailEntity.getPastService());
			contributionAdjustmentPropsEntity
					.setSinglePremiumFirstYearCS(policyContributionDetailEntity.getCurrentServices());
			contributionAdjustmentPropsEntity.setContriAdjStatus(contributionDraftStatusId);
			contributionAdjustmentPropsEntity.setAdjustmentForDate(new Date());
			contributionAdjustmentPropsEntity.setIsActive(true);
			contributionAdjustmentPropsEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
			contributionAdjustmentPropsEntity.setCreatedDate(new Date());
			contributionAdjustmentPropsEntity.setId(null);
			contributionAdjustmentPropsEntity = contributionAdjustmentPropsRepository
					.save(contributionAdjustmentPropsEntity);
			
		} catch (Exception e) {
			logger.error("CAProps Error", e);
		}
		return ApiResponseDto.success(ContriAdjustHelper.entityToDto(contributionAdjustmentPropsEntity));


	}

	//Save Contribution Detail
	  private PolicyContributionEntity saveContributionDetail(Long policyId, Long contributionDetailId, String contributionType,
			Double currentAmount, String financialYear, Long maxVersion, String variantCode, Date effectiveDate, String userName,
				Date date, String collectionnumber, Long conAdjId, Double openingBalance, Double closingBalance) {
			PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();

			policyContributionEntity.setAdjConid(conAdjId);

			policyContributionEntity.setTotalContribution(currentAmount);
			policyContributionEntity.setClosingBlance(closingBalance);
			policyContributionEntity.setOpeningBalance(openingBalance);
			policyContributionEntity.setContReferenceNo(collectionnumber);
			policyContributionEntity.setContributionDate(date);
			policyContributionEntity.setCreatedBy(userName);
			policyContributionEntity.setCreateDate(new Date());
			policyContributionEntity.setEmployeeContribution(0.0);
			policyContributionEntity.setEmployerContribution(currentAmount);

			policyContributionEntity.setFinancialYear(financialYear);

			policyContributionEntity.setActive(true);
			policyContributionEntity.setIsDeposit(true);

			policyContributionEntity.setTmpPolicyId(policyId);
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
			// for now default Q0

			return policyContributionEntity;
		}


	public static List<DepositAdjustementDto> sort(List<DepositAdjustementDto> list) {
		  
        list.sort((o1, o2)
                  -> o1.getVoucherEffectiveDate().compareTo(
                      o2.getVoucherEffectiveDate()));
        
      return  list;
    }
	
	public PolicyAdjustmentDetailEntity saveAdjustmentdata(Long contributionDetailId, Long depositId, Double currentAmount, String type,
			String subType, String entryType, Date date,String userName) {
	
	PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity=new PolicyAdjustmentDetailEntity();
	
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



	public RenewalPolicyTMPEntity mastertoTmpforPolicyService(MasterPolicyEntity masterPolicyEntity) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper.copytoTmpForClaim(masterPolicyEntity);
		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
//		PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
//
//		policyServiceEntitiy.setServiceType("Claim");
//		policyServiceEntitiy.setPolicyId(tempPolicyClaimPropsDto.getPmstPolicyId());
//		policyServiceEntitiy.setCreatedBy(tempPolicyClaimPropsDto.getCreatedBy());
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
				.findByPolicyId(masterPolicyEntity.getId());

		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper.copyToTmpLifeCoverforClaim(
				policyLifeCoverEntity, memberCategoryEntity, renewalPolicyTMPEntity.getId());

		renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
		//		policyLifeCoverRepository.updateISActive(tempPolicyClaimPropsDto.getPmstPolicyId());

		List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
				.findBypolicyId(masterPolicyEntity.getId());

		List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
				.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
						renewalPolicyTMPEntity.getId());

		renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
		//		policyGratuityBenefitRepository.updateIsActive(tempPolicyClaimPropsDto.getPmstPolicyId());

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

//		RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
//				.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity, memberCategoryEntity,
//						renewalPolicyTMPEntity.getId());
//		renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);
		return renewalPolicyTMPEntity;
	}



	@Override
	public ApiResponseDto<RenewalPolicyTMPDto> getInprogressforService(Long masterPolicyId, Long tmpPolicyId,
			String servicetype) {
		Long mpid = masterPolicyId;
		RenewalPolicyTMPEntity renewalPolicyTMPEntity=null;
		if (mpid == 0)
			mpid = renewalPolicyTMPRepository.findById(tmpPolicyId).get().getMasterPolicyId();
		if(servicetype.toLowerCase().equals("ca")) {
		 renewalPolicyTMPEntity = renewalPolicyTMPRepository.findInProgressServiceforcontribadjust(mpid);
		}
		if(servicetype.toLowerCase().equals("pc")) {
			renewalPolicyTMPEntity = renewalPolicyTMPRepository.findInProgressServiceforPremiumcollection(mpid);
		}
		if(renewalPolicyTMPEntity ==null)
			return ApiResponseDto.success(null);
		else
			return ApiResponseDto.success(RenewalValuationTMPHelper.entityToDtoTemPolicy(renewalPolicyTMPEntity));
	}

	@Transactional
	@Override
	public ApiResponseDto<ContributionAdjustmentPropsDto> updatecontribadjustment(
			PolicyContributionDetailDto policyContributionDetailDto, Long tmpPolicyId) {
		Long masterTmpPolicyId = null;
		Double getBalance = null;
		String financialYear =null;
		Double adjustementAmount = 0.0;
		Double stampDuty=0.0;
		
		financialYear=DateUtils.getFinancialYear(new Date());
		masterTmpPolicyId = tmpPolicyId;
		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository
				.findBytmpPolicyId(masterTmpPolicyId);
		
		RenewalPolicyTMPEntity renewalPolicyTMPEntity=renewalPolicyTMPRepository.findById(masterTmpPolicyId).get();	
		String prodAndVarientCodeSame=	commonModuleService.getProductCode(renewalPolicyTMPEntity.getProductId());
		if (isDevEnvironment == false) {
			List<UnlockDepositDetailDto> showDepositLockDto = new ArrayList<UnlockDepositDetailDto>();

			for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {

				UnlockDepositDetailDto depositLockDto = new UnlockDepositDetailDto();
				depositLockDto.setChallanNo(Integer.parseInt(policyDepositEntity.getChallanNo()));
				depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				depositLockDto.setUserCode(policyContributionDetailDto.getCreatedBy());
				depositLockDto
						.setProductCode(prodAndVarientCodeSame);
				depositLockDto.setVariantCode(prodAndVarientCodeSame);
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
		policyContributionDetailEntity.setAdjustmentForDate(new Date());
		policyContributionDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
		policyContributionDetailEntity.setLifePremium(policyContributionDetailDto.getLifePremium());
		policyContributionDetailEntity.setPastService(policyContributionDetailDto.getPastService());
		policyContributionDetailEntity.setGst(policyContributionDetailDto.getGst());
		policyContributionDetailEntity.setCurrentServices(policyContributionDetailDto.getCurrentServices());
		policyContributionDetailEntity.setModifiedBy(policyContributionDetailDto.getModifiedBy());
		policyContributionDetailEntity.setModifiedDate(new Date());
		policyContributionDetailEntity.setEntryType("CA");	
		policyContributionDetailEntity.setAdjustmentForDate(new Date());
		policyContributionDetailEntity.setFinancialYear(financialYear);
		policyContributionDetailEntity.setPastServiceBalance(policyContributionDetailDto.getPastServiceBalance());					
		policyContributionDetailEntity.setCurrentServiceBalance(policyContributionDetailDto.getCurrentServiceBalance());
		policyContributionDetailEntity=policyContributionDetailRepository.save(policyContributionDetailEntity);

		List<PolicyAdjustmentDetailEntity>PolicyAdjustmentDetail= policyAdjustmentDetailRepository
				.deleteBycontributionDetailId(policyContributionDetailEntity.getId());
		List<PolicyDepositEntity> oldDepositList = policyDepositRepository.deleteBytmpPolicyId(masterTmpPolicyId);
		List<PolicyContributionEntity> oldPolicyContribution = policyContributionRepository
				.deleteBytmpPolicyId(masterTmpPolicyId);
		List<PolicyContrySummaryEntity> oldPolicyContriSummary = policyContrySummaryRepository
				.deleteBytmpPolicyId(masterTmpPolicyId);
		




		adjustementAmount =  policyContributionDetailEntity.getCurrentServices()
				+ policyContributionDetailEntity.getPastService();
		
		policyContributionDetailEntity=policyContributionDetailRepository.save(policyContributionDetailEntity);

		

		List<ShowDepositLockDto> showDepositLockDto=new ArrayList<ShowDepositLockDto>();
		for (DepositAdjustementDto getDepositAdjustementDto : policyContributionDetailDto
				.getDepositAdjustmentDto()) {
			if (adjustementAmount != 0) {
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
				policyDepositEntity
						.setChequeRealistionDate(getDepositAdjustementDto.getVoucherEffectiveDate());
				policyDepositEntity.setActive(true);
//				Random r = new Random(System.currentTimeMillis());
//				int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
				policyDepositEntity.setChallanNo(policyContributionDetailEntity.getChallanNo());
				policyDepositEntity.setCollectionDate(new Date());
				policyDepositEntity.setCreateDate(new Date());
				policyDepositEntity.setZeroId(null);
				policyDepositEntity.setTransactionMode(getDepositAdjustementDto.getCollectionMode());
				policyDepositEntity.setStatus(null);
				policyDepositEntity.setRegConId(null);
				policyDepositEntity.setTmpPolicyId(masterTmpPolicyId);
				policyDepositEntity.setContributionType("CA");
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
				

				ShowDepositLockDto depositLockDto = new ShowDepositLockDto();
				
				
				
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

		List<PolicyContributionEntity> policyContributionEntity = new ArrayList<PolicyContributionEntity>();

		List<PolicyDepositEntity> policyDepositEntitieList = policyDepositRepository
				.findBycontributionDetailId(policyContributionDetailEntity.getId());
//		getGstSC.getGst() + getGstSC.getLifePremium() + getGstSC.getCurrentServices()
//		+ getGstSC.getPastService();
	List<PolicyAdjustmentDetailEntity> policyAdjustmentDetail =new ArrayList<PolicyAdjustmentDetailEntity>();
		Double depositAmount=0.0;
		Long depositId=0l;
		Double currentAmount=0.0;
		Double closingBalance=0.0;
		Double openingBalance=policyContributionRepository.getLastClosingBalance(renewalPolicyTMPEntity.getMasterPolicyId())!=null?policyContributionRepository.getLastClosingBalance(renewalPolicyTMPEntity.getMasterPolicyId()):0.0;
		
		
//		Long premiumAdjustmentLong=Math.round(policyContributionDetailEntity.getLifePremium());
//		Double premiumAdjustment = premiumAdjustmentLong.doubleValue();
//		
//		Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();
//		
//		Long currentServiceAdjustedLong=Math.round(policyContributionDetailEntity.getCurrentServices());
//		Long pastServiceAdjustedLong=Math.round(policyContributionDetailEntity.getPastService());
		
		Double currentServiceAdjusted = policyContributionDetailEntity.getCurrentServices();
		
	
		Double pastServiceAdjusted = policyContributionDetailEntity.getPastService();
		adjustementAmount = currentServiceAdjusted
				+ pastServiceAdjusted;
		policyContributionDetailDto.setDepositAdjustmentDto(sort(policyContributionDetailDto.getDepositAdjustmentDto()));
		Long versionNumber=masterPolicyContributionRepository.getMaxVersion(financialYear,
				renewalPolicyTMPEntity.getMasterPolicyId());
		versionNumber= versionNumber==null ?01:versionNumber+1;
		for (PolicyDepositEntity policyDepositEntity : policyDepositEntitieList) {
			if (adjustementAmount > 0) {
				Double currentAmountforContribution = 0.0;
				depositAmount = policyDepositEntity.getAdjustmentAmount(); 
				depositId = policyDepositEntity.getId();
				// LIFE PREMIUM NO
			

				// GST AMOUNT NO


				// SinglePremium

				if ( pastServiceAdjusted > 0
						&& depositAmount > 0) {
					if (pastServiceAdjusted >= depositAmount) {
						currentAmount = depositAmount;

						pastServiceAdjusted = pastServiceAdjusted - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
//						policyAdjustmentDetail.add(this.saveAdjustmentdata(
//								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//								"SinglePremium", "CA", policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy()));
						
						PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
								"SinglePremium", "CA", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
								
								
								currentAmountforContribution += currentAmount;
								closingBalance=openingBalance+currentAmountforContribution;
								policyContributionEntity.add(
										this.saveContributionDetail(masterTmpPolicyId, policyDepositEntity.getContributionDetailId(),
												"CA", currentAmount, financialYear, versionNumber,
												commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
												policyDepositEntity.getChequeRealistionDate(),
												policyContributionDetailDto.getCreatedBy(), policyDepositEntity.getCollectionDate(),Long.toString(policyDepositEntity.getCollectionNo()),
												policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
						versionNumber=versionNumber+1;
						openingBalance=+closingBalance;
						depositAmount = 0.0;
					} else {
						currentAmount = pastServiceAdjusted;

						adjustementAmount = adjustementAmount - currentAmount;
						PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
								"SinglePremium", "CA", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						
//						policyAdjustmentDetail.add(this.saveAdjustmentdata(
//								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//								"SinglePremium", "CA", policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - pastServiceAdjusted;
						currentAmountforContribution += currentAmount;
						closingBalance=openingBalance+currentAmountforContribution;
						policyContributionEntity.add(
								this.saveContributionDetail(masterTmpPolicyId, policyDepositEntity.getContributionDetailId(),
										"CA", currentAmount, financialYear, versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(), policyDepositEntity.getCollectionDate(),
										Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
				versionNumber=versionNumber+1;
				openingBalance=+closingBalance;
						pastServiceAdjusted = 0.0;
					}
				}

				// FirstPremium

				if (pastServiceAdjusted == 0
						&& currentServiceAdjusted > 0 && depositAmount > 0) {
					if (currentServiceAdjusted >= depositAmount) {
						currentAmount = depositAmount;

						currentServiceAdjusted = currentServiceAdjusted - depositAmount;
						adjustementAmount = adjustementAmount - currentAmount;
						PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
								"FirstPremium", "CA", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
//						policyAdjustmentDetail.add(this.saveAdjustmentdata(
//								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//								"FirstPremium", "CA", policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy()));
						
						
						currentAmountforContribution += currentAmount;
						closingBalance=openingBalance+currentAmountforContribution;
					policyContributionEntity.add(
						this.saveContributionDetail(masterTmpPolicyId, policyDepositEntity.getContributionDetailId(),
								"CA", currentAmount, financialYear, versionNumber,
								commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
								policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy(), policyDepositEntity.getCollectionDate(),
								Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
					versionNumber=versionNumber+1;
					openingBalance=+closingBalance;
						depositAmount = 0.0;
					} else {
						currentAmount = currentServiceAdjusted;

						adjustementAmount = adjustementAmount - currentAmount;
//						policyAdjustmentDetail.add(this.saveAdjustmentdata(
//								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//								"FirstPremium", "CA", policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy()));
						
						PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(saveAdjustmentdata(
								policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
								"FirstPremium", "CA", policyDepositEntity.getChequeRealistionDate(),
								policyContributionDetailDto.getCreatedBy()));
						depositAmount = depositAmount - currentServiceAdjusted;
						currentAmountforContribution += currentAmount;
						closingBalance=openingBalance+currentAmountforContribution;
						policyContributionEntity.add(
								this.saveContributionDetail(masterTmpPolicyId, policyDepositEntity.getContributionDetailId(),
										"CA", currentAmount, financialYear, versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(),
										policyDepositEntity.getCollectionDate(),Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
				versionNumber=versionNumber+1;
				openingBalance=+closingBalance;
						currentServiceAdjusted = 0.0;
					}
				}
//				policyContributionEntity.add(
//						this.saveContributionDetail(masterTmpPolicyId, policyDepositEntity.getContributionDetailId(),
//								"CA", currentAmountforContribution, financialYear, versionNumber,
//								commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
//								policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy(), policyDepositEntity.getCollectionDate(),Long.toString(policyDepositEntity.getCollectionNo()),savePolicyAdjustmentDetailEntity.getId())));
//				versionNumber = versionNumber + 1;
			}
		}
		policyContributionRepository.saveAll(policyContributionEntity);
//		policyAdjustmentDetailRepository.saveAll(policyAdjustmentDetail);
			if (isDevEnvironment == false) {
				accountingService.lockDeposits(showDepositLockDto,policyContributionDetailDto.getCreatedBy());
			}
			
			ContributionAdjustmentPropsEntity contributionAdjustmentPropsEntity =contributionAdjustmentPropsRepository.findBytmpPolicyId(masterTmpPolicyId);
			try {
				
				contributionAdjustmentPropsEntity.setAmtToBeAdjusted(policyContributionDetailEntity.getCurrentServices()
						+ policyContributionDetailEntity.getPastService());
				contributionAdjustmentPropsEntity.setPstgContriDetailId(policyContributionDetailEntity.getId());
				contributionAdjustmentPropsEntity.setTmpPolicyId(masterTmpPolicyId);
				contributionAdjustmentPropsEntity.setFirstPremiumPS(policyContributionDetailEntity.getPastService());
				contributionAdjustmentPropsEntity
						.setSinglePremiumFirstYearCS(policyContributionDetailEntity.getCurrentServices());
				contributionAdjustmentPropsEntity.setAdjustmentForDate(new Date());
				contributionAdjustmentPropsEntity.setModifiedBy(policyContributionDetailEntity.getModifiedBy());
				contributionAdjustmentPropsEntity.setModifiedDate(new Date());
				contributionAdjustmentPropsEntity = contributionAdjustmentPropsRepository
						.save(contributionAdjustmentPropsEntity);
				
			} catch (Exception e) {
				logger.error("CAProps Error", e);
			}

		return ApiResponseDto.success(ContriAdjustHelper.entityToDto(contributionAdjustmentPropsEntity));
	}
	
	
	
	@Override
	public ApiResponseDto<List<ContributionAdjustmentPropsDto>> contributionfiltersearch(
			ContributionAdjustmentSearchDto contributionAdjustmentPropsDto) {


		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PolicyTempSearchEntity> createQuery = criteriaBuilder.createQuery(PolicyTempSearchEntity.class);

		Root<PolicyTempSearchEntity> root = createQuery.from(PolicyTempSearchEntity.class);

		Join<PolicyTempSearchEntity, ContributionAdjustmentSearchPropsEntity> joinTmpPolicytoProps = root
				.join("tmpContriAdjustSearch");

		Join<PolicyTempSearchEntity, TempMphSearchEntity> joinTmpPolicytoTmpMPH = root.join("policyMPHTmp");

	
		// policyTMP start
		if (contributionAdjustmentPropsDto.getCustomerCode() != null
				&& StringUtils.isNotBlank(contributionAdjustmentPropsDto.getCustomerCode())) {
			predicates
					.add(criteriaBuilder.equal(criteriaBuilder.trim(criteriaBuilder.lower(criteriaBuilder.lower(root.get("customerCode")))), contributionAdjustmentPropsDto.getCustomerCode().trim().toLowerCase()));
		}
		
		if (contributionAdjustmentPropsDto.getCustomerName() != null
				&& StringUtils.isNotBlank(contributionAdjustmentPropsDto.getCustomerName())) {
//			criteriaBuilder.trim(criteriaBuilder.lower(criteriaBuilder.lower(root.get("customerName"))));	
			predicates
					.add(criteriaBuilder.equal(criteriaBuilder.trim(criteriaBuilder.lower(criteriaBuilder.lower(root.get("customerName")))), contributionAdjustmentPropsDto.getCustomerName().trim().toLowerCase()));
		}
		if (contributionAdjustmentPropsDto.getPolicyNumber() != null
				&& StringUtils.isNotBlank(contributionAdjustmentPropsDto.getPolicyNumber())) {
			predicates
					.add(criteriaBuilder.equal(root.get("policyNumber"), contributionAdjustmentPropsDto.getPolicyNumber().trim()));
		}
		if (contributionAdjustmentPropsDto.getUnitCode() != null
				&& StringUtils.isNotBlank(contributionAdjustmentPropsDto.getUnitCode())) {
			predicates
					.add(criteriaBuilder.equal(root.get("unitCode"), contributionAdjustmentPropsDto.getUnitCode().trim()));
		}

		if ((contributionAdjustmentPropsDto.getContriAdjStatus() != null)
				&& StringUtils.isNotBlank(contributionAdjustmentPropsDto.getContriAdjStatus().toString())) {
			predicates.add(joinTmpPolicytoProps.get("contriAdjStatus").in(contributionAdjustmentPropsDto.getContriAdjStatus()));
		}
		// End

		// MPHTMP Start
		if (contributionAdjustmentPropsDto.getMphName() != null
				&& StringUtils.isNotBlank(contributionAdjustmentPropsDto.getMphName())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicytoTmpMPH.get("mphName"))),
					contributionAdjustmentPropsDto.getMphName().toLowerCase()));
		}
		if (contributionAdjustmentPropsDto.getMphCode()!= null
				&& StringUtils.isNotBlank(contributionAdjustmentPropsDto.getMphCode())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicytoTmpMPH.get("mphCode"))),
					contributionAdjustmentPropsDto.getMphCode().toLowerCase()));
		}
		if (contributionAdjustmentPropsDto.getPan() != null
				&& StringUtils.isNotBlank(contributionAdjustmentPropsDto.getPan())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicytoTmpMPH.get("pan"))),
					contributionAdjustmentPropsDto.getPan().toLowerCase()));
		}

		// ENd

		// MemberTMP Data start


		// END

	
	
//		List<Long> list = Arrays.asList(contributionAdjustmentPropsDto.getContriAdjStatus());
//		if(list.get(0)==contributionApproveStatusId) {
//			predicates.add(criteriaBuilder.equal(joinTmpPolicytoProps.get("isActive"), Boolean.FALSE));
//
//		}else {
//		predicates.add(criteriaBuilder.equal(joinTmpPolicytoProps.get("isActive"), Boolean.TRUE));
//		}
		// Props end

		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<PolicyTempSearchEntity> entities = entityManager.createQuery(createQuery).getResultList();

		List<ContributionAdjustmentPropsDto> listContributionAdjustmentPropsDto = new ArrayList<>();
		for (PolicyTempSearchEntity policySearch : entities) {
			ContributionAdjustmentPropsDto newContributionAdjustmentPropsDto = new ModelMapper().map(
					masterPolicyCustomRepository.setTransientValue(policySearch), ContributionAdjustmentPropsDto.class);
			newContributionAdjustmentPropsDto.setTmpPolicyId(policySearch.getId());
			newContributionAdjustmentPropsDto.setMphCode(policySearch.getPolicyMPHTmp().getMphCode());
			newContributionAdjustmentPropsDto.setMphName(policySearch.getPolicyMPHTmp().getMphName());
			for (ContributionAdjustmentSearchPropsEntity getContributionAdjustment : policySearch
					.getTmpContriAdjustSearch()) {
				newContributionAdjustmentPropsDto.setId(getContributionAdjustment.getId());
				newContributionAdjustmentPropsDto.setContriAdjStatus(getContributionAdjustment.getContriAdjStatus());
				newContributionAdjustmentPropsDto.setContriAdjNumber(getContributionAdjustment.getContriAdjNumber());
				listContributionAdjustmentPropsDto.add(newContributionAdjustmentPropsDto);
		}
		}
			return ApiResponseDto.success(listContributionAdjustmentPropsDto);
			}
		
		
		
		@Override
	public ApiResponseDto<ContributionAdjustmentPropsDto> contriAdjustStatusChange(Long contridjustpropsId,
			Long statusId, ContributionAdjustmentPropsDto contributionAdjustmentPropsDto) {
		ContributionAdjustmentPropsEntity contributionAdjustmentPropsEntity =contributionAdjustmentPropsRepository.findById(contridjustpropsId).get();
		contributionAdjustmentPropsEntity.setContriAdjStatus(statusId);
		
		contributionAdjustmentPropsRepository.save(contributionAdjustmentPropsEntity);
		RenewalPolicyTMPEntity renewalPolicyTMPEntity=renewalPolicyTMPRepository.findById(contributionAdjustmentPropsEntity.getTmpPolicyId()).get();			
				if(contributionsenttocheckerORPendingForApproveStatusId.equals(statusId)) {
					
			
			PolicyServiceEntitiy policyserviceentity = policyServiceRepository.findByPolicyandTypeandActive(renewalPolicyTMPEntity.getMasterPolicyId(), contributionAdjustmentPropsDto.getServiceType());
			
			if(policyserviceentity==null) {
			
				PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
				policyServiceEntitiy.setServiceType(contributionAdjustmentPropsDto.getServiceType());
				policyServiceEntitiy.setPolicyId(renewalPolicyTMPEntity.getMasterPolicyId());
				policyServiceEntitiy.setCreatedBy(contributionAdjustmentPropsDto.getCreatedBy());
				policyServiceEntitiy.setCreatedDate(new Date());
				policyServiceEntitiy.setIsActive(true);
				policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);
				renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());	
				}
			else {
				renewalPolicyTMPEntity.setPolicyServiceId(policyserviceentity.getId());
			}
			
		}
				renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		
		return ApiResponseDto.success(ContriAdjustHelper.entityToDto(contributionAdjustmentPropsEntity));
	}

	@Override
	public ApiResponseDto<ContributionAdjustmentPropsDto> sentPolicyforReject(Long contridjustpropsId, String username,
			ContributionAdjustmentPropsDto contributionAdjustmentPropsDto) {
	
		ContributionAdjustmentPropsEntity contributionAdjustmentPropsEntity =contributionAdjustmentPropsRepository.findById(contridjustpropsId).get();
		List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository.findBytmpPolicyId(contributionAdjustmentPropsEntity.getTmpPolicyId());
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(contributionAdjustmentPropsEntity.getTmpPolicyId()).get();
		if (isDevEnvironment == false) {
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

	
		contributionAdjustmentPropsEntity.setContriAdjStatus(contributionRejectedStatusId);
		contributionAdjustmentPropsEntity.setRejectedReason(contributionAdjustmentPropsDto.getRejectedReason());
		contributionAdjustmentPropsEntity.setRejectedRemarks(contributionAdjustmentPropsDto.getRejectedRemarks());
		contributionAdjustmentPropsEntity.setModifiedBy(username);
		contributionAdjustmentPropsEntity.setModifiedDate(new Date());
		contributionAdjustmentPropsRepository.save(contributionAdjustmentPropsEntity);

		
		return ApiResponseDto.success(ContriAdjustHelper.entityToDto(contributionAdjustmentPropsEntity));
	}

	@Transactional
	@Override
	public ApiResponseDto<PolicyDto> approve(Long contadjustid, String username) {

		ContributionAdjustmentPropsEntity contributionAdjustmentPropsEntity =contributionAdjustmentPropsRepository.findById(contadjustid).get();
		
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(contributionAdjustmentPropsEntity.getTmpPolicyId()).get();
		// Copy to MasterPolicycontributionDetails
		MasterPolicyEntity newmasterPolicyEntity=masterPolicyRepository.findById(renewalPolicyTMPEntity.getMasterPolicyId()).get();
		MasterPolicyContributionDetails contributionMaster = PolicyHelper.entityToMaster(
				policyContributionDetailRepository.findByTmpPolicyIdandType(renewalPolicyTMPEntity.getId()),
			null,newmasterPolicyEntity.getId());
		contributionMaster=masterPolicyContributionDetailRepository.save(contributionMaster);
		
		List<PolicyDepositEntity> policyDepositEntites=policyDepositRepository.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
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
		
//		// copy Deposit to MasterPolicyContributionEntity
//		List<MasterPolicyContributionEntity> masterPolicyContributionEntity = PolicyHelper.entityToPolicyConMaster(
//				policyContributionRepository.findBytmpPolicyId(renewalPolicyTMPEntity.getId()),
//				newmasterPolicyEntity.getId(), contributionMaster,username);
//		masterPolicyContributionRepository.saveAll(masterPolicyContributionEntity);
		
		if (isDevEnvironment == false) {
			Boolean isRenewalStatus=false;
			int policyServiceEntitiy = policyServiceRepository.checkforpolicyrenewalsuccessstatus(newmasterPolicyEntity.getId(),"renewals");	
			if(policyServiceEntitiy>0) {
				isRenewalStatus=true;
			}

			String prodAndVarientCodeSame=	commonModuleService.getProductCode(newmasterPolicyEntity.getProductId());
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
					mphStateCode = commonMasterStateRepository.getGSTStatecodebyid(Long.valueOf(getMPHAddressEntity.getStateName()));
					break;
				}
			}

			PolicyContributionDetailEntity masterPolicyContributiondetailEntity = policyContributionDetailRepository
					.findByTmpPolicyIdandType(renewalPolicyTMPEntity.getId());	
			
		

			HSNCodeDto hSNCodeDto = accountingService.getHSNCode();
			Map<String, Double> gstComponents = accountingService.getGstComponents(unitStateType, mPHStateType, hSNCodeDto, masterPolicyContributiondetailEntity.getLifePremium()==null?0.0:masterPolicyContributiondetailEntity.getLifePremium());

			NewBusinessContributionAndLifeCoverAdjustmentDto newBusinessContributionAndLifeCoverAdjustmentDto = new NewBusinessContributionAndLifeCoverAdjustmentDto();
			newBusinessContributionAndLifeCoverAdjustmentDto.setAdjustmentAmount( masterPolicyContributiondetailEntity.getCurrentServices() 
					+ masterPolicyContributiondetailEntity.getPastService());
			newBusinessContributionAndLifeCoverAdjustmentDto.setAdjustmentNo(masterPolicyContributiondetailEntity.getId().intValue()*3);
			newBusinessContributionAndLifeCoverAdjustmentDto.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
			newBusinessContributionAndLifeCoverAdjustmentDto.setIsGstApplicable(false);
			if(newmasterPolicyEntity.getGstApplicableId() == 1l)
				 newBusinessContributionAndLifeCoverAdjustmentDto.setIsGstApplicable(true);	
			newBusinessContributionAndLifeCoverAdjustmentDto.setMphCode(getMPHEntity.getMphCode());
			newBusinessContributionAndLifeCoverAdjustmentDto.setUnitCode(newmasterPolicyEntity.getUnitCode());
			newBusinessContributionAndLifeCoverAdjustmentDto.setUserCode(username);
		
			newBusinessContributionAndLifeCoverAdjustmentDto.setGlTransactionModel(accountingService.getGlTransactionModel(newmasterPolicyEntity.getProductId(),newmasterPolicyEntity.getProductVariantId(),newmasterPolicyEntity.getUnitCode(), "GratuityContriAdjPolicyApproval"));
			String toGSTIn = getMPHEntity.getGstIn() == null ? "" : getMPHEntity.getGstIn();
			ResponseDto responseDto = accountingService
					.commonmasterserviceAllUnitCode(newmasterPolicyEntity.getUnitCode());
			GstDetailModelDto gstDetailModelDto=new GstDetailModelDto();
			gstDetailModelDto.setAmountWithTax((masterPolicyContributiondetailEntity.getLifePremium()==null?0.0: masterPolicyContributiondetailEntity.getLifePremium())
					+ (masterPolicyContributiondetailEntity.getGst()==null?0.0: masterPolicyContributiondetailEntity.getGst()));
			gstDetailModelDto.setAmountWithoutTax(masterPolicyContributiondetailEntity.getLifePremium()==null?0.0: masterPolicyContributiondetailEntity.getLifePremium());
			gstDetailModelDto.setCessAmount(0.0); //from Docu
			
			gstDetailModelDto.setCgstAmount(gstComponents.get("CGST"));
			gstDetailModelDto.setCgstRate(hSNCodeDto.getCgstRate());
			gstDetailModelDto.setCreatedBy(username);
			gstDetailModelDto.setCreatedDate(new Date());
			gstDetailModelDto.setEffectiveEndDate(""); //form docu
			gstDetailModelDto.setEffectiveStartDate(new Date());			
			gstDetailModelDto.setEntryType(toGSTIn != null ? "B2B" : "B2C");
			gstDetailModelDto.setFromGstn(responseDto.getGstIn()==null?"":responseDto.getGstIn());
			gstDetailModelDto.setFromPan(responseDto.getPan());
			gstDetailModelDto.setFromStateCode(unitStateCode); //from MPH detail null
			
			if(newmasterPolicyEntity.getGstApplicableId() == 1l)
				 gstDetailModelDto.setGstApplicableType("Taxable");
			else
				 gstDetailModelDto.setGstApplicableType("Non-Taxable");
			
			gstDetailModelDto.setGstInvoiceNo(""); //From Docu
			gstDetailModelDto.setGstRate(accountingService.getGSTRate(unitStateType, mPHStateType, hSNCodeDto)*100);
			gstDetailModelDto.setGstRefNo(newmasterPolicyEntity.getPolicyNumber()); //from Docu
			gstDetailModelDto.setGstRefTransactionNo(newmasterPolicyEntity.getPolicyNumber());//From Docu
			gstDetailModelDto.setGstTransactionType("DEBIT");//From Docu
			gstDetailModelDto.setGstType("GST");//From Docu
			gstDetailModelDto.setHsnCode(hSNCodeDto.getHsnCode());
			gstDetailModelDto.setIgstAmount(gstComponents.get("IGST"));
			gstDetailModelDto.setIgstRate(hSNCodeDto.getIgstRate());
			gstDetailModelDto.setModifiedBy(0L); //from docu
			gstDetailModelDto.setModifiedDate(new Date()); //from Docu
			gstDetailModelDto.setMphAddress(mPHAddress);
			gstDetailModelDto.setMphName(getMPHEntity.getMphName());
			gstDetailModelDto.setNatureOfTransaction(hSNCodeDto.getDescription());
			gstDetailModelDto.setOldInvoiceDate(new Date()); //From Docu
			gstDetailModelDto.setOldInvoiceNo("IN20123QE"); //From Docu
			gstDetailModelDto.setProductCode(prodAndVarientCodeSame);
			gstDetailModelDto.setRemarks("Gratuity CA Deposit Adjustment");
			gstDetailModelDto.setSgstAmount(gstComponents.get("SGST"));
			gstDetailModelDto.setSgstRate(hSNCodeDto.getSgstRate());
			gstDetailModelDto.setToGstIn(getMPHEntity.getGstIn() ==null?"":  getMPHEntity.getGstIn());  //From Docu from get Common Module
			gstDetailModelDto.setToPan(getMPHEntity.getPan()==null?"":getMPHEntity.getPan());
			gstDetailModelDto.setToStateCode(mphStateCode ==null?"":mphStateCode);
			gstDetailModelDto.setTotalGstAmount(masterPolicyContributiondetailEntity.getGst()==null?0.0:masterPolicyContributiondetailEntity.getGst().doubleValue());
			gstDetailModelDto.setTransactionDate(new Date());
			gstDetailModelDto.setTransactionSubType("A"); //From Docu
			gstDetailModelDto.setTransactionType("C"); //From Docu
//			gstDetailModelDto.setUserCode(username);
			gstDetailModelDto.setUtgstAmount(gstComponents.get("UTGST"));
		
			gstDetailModelDto.setUtgstRate(hSNCodeDto.getUtgstRate());
			gstDetailModelDto.setVariantCode(prodAndVarientCodeSame);
			gstDetailModelDto.setYear(DateUtils.uniqueNoYYYY());
			gstDetailModelDto.setMonth(DateUtils.currentMonthName());
			newBusinessContributionAndLifeCoverAdjustmentDto.setGstDetailModel(gstDetailModelDto);
			
			JournalVoucherDetailModelDto journalVoucherDetailModelDto =new JournalVoucherDetailModelDto();
			journalVoucherDetailModelDto.setLineOfBusiness(newmasterPolicyEntity.getLineOfBusiness());
			journalVoucherDetailModelDto.setProduct(prodAndVarientCodeSame);
			journalVoucherDetailModelDto.setProductVariant(prodAndVarientCodeSame);
			newBusinessContributionAndLifeCoverAdjustmentDto.setJournalVoucherDetailModel(journalVoucherDetailModelDto);
			
			List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository.findBymasterPolicyIdandEntryType(renewalPolicyTMPEntity.getId(),"CA");
			List<RenwalWithGIDebitCreditRequestModelDto> getRenewalWithGiDebitCreditRequestModel =new ArrayList<RenwalWithGIDebitCreditRequestModelDto>();
//			Long premiumAdjustmentLong=Math.round(masterPolicyContributiondetailEntity.getLifePremium());
//			Double premiumAdjustment = premiumAdjustmentLong.doubleValue();
//			
//			Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();
			
			Long currentServiceAdjustedLong=Math.round(masterPolicyContributiondetailEntity.getCurrentServices());
			Double currentServiceAdjusted = currentServiceAdjustedLong.doubleValue();
			
			Long pastServiceAdjustedLong=Math.round(masterPolicyContributiondetailEntity.getPastService());
			Double pastServiceAdjusted = pastServiceAdjustedLong.doubleValue();
			for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {
				RenwalWithGIDebitCreditRequestModelDto renewalWithGiDebitCreditRequestModel=new RenwalWithGIDebitCreditRequestModelDto();
				renewalWithGiDebitCreditRequestModel.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
				Long availableLong=Math.round(policyDepositEntity.getAdjustmentAmount());
				Double availableAmount = availableLong.doubleValue();
				policyDepositEntity.setAvailableAmount(availableAmount);
				
				Double depositDebitAmount = 0.0;
				
			if(isRenewalStatus) {
				if ( pastServiceAdjusted> 0 && availableAmount > 0) {
					if (masterPolicyContributiondetailEntity.getPastService() <= policyDepositEntity.getAvailableAmount()) {
						renewalWithGiDebitCreditRequestModel.setSinglePremiumSubsequentPastServiceCreditAmount(pastServiceAdjusted);
						availableAmount -= pastServiceAdjusted;
						depositDebitAmount += pastServiceAdjusted;
					} else {
						renewalWithGiDebitCreditRequestModel.setSinglePremiumSubsequentPastServiceCreditAmount(policyDepositEntity.getAvailableAmount());
						currentServiceAdjusted -= policyDepositEntity.getAvailableAmount();
						availableAmount -= policyDepositEntity.getAvailableAmount();
						depositDebitAmount += policyDepositEntity.getAvailableAmount();
					}
				}
				
				if (currentServiceAdjusted > 0 && availableAmount > 0) {
					if (masterPolicyContributiondetailEntity.getCurrentServices() <= policyDepositEntity.getAvailableAmount()) {
						renewalWithGiDebitCreditRequestModel.setRenewalPremiumOthersCurrentServiceCreditAmount(currentServiceAdjusted);
						availableAmount -= currentServiceAdjusted;
						depositDebitAmount += currentServiceAdjusted;
					} else {
						renewalWithGiDebitCreditRequestModel.setRenewalPremiumOthersCurrentServiceCreditAmount(policyDepositEntity.getAvailableAmount());
						pastServiceAdjusted -= policyDepositEntity.getAvailableAmount();
						availableAmount -= policyDepositEntity.getAvailableAmount();
						depositDebitAmount += policyDepositEntity.getAvailableAmount();
					}
				}
			}else {
				if ( pastServiceAdjusted> 0 && availableAmount > 0) {
					if (masterPolicyContributiondetailEntity.getPastService() <= policyDepositEntity.getAvailableAmount()) {
						renewalWithGiDebitCreditRequestModel.setSinglePremiumFirstYearPastServiceCreditAmount(pastServiceAdjusted);
						availableAmount -= pastServiceAdjusted;
						depositDebitAmount += pastServiceAdjusted;
					} else {
						renewalWithGiDebitCreditRequestModel.setSinglePremiumFirstYearPastServiceCreditAmount(policyDepositEntity.getAvailableAmount());
						currentServiceAdjusted -= policyDepositEntity.getAvailableAmount();
						availableAmount -= policyDepositEntity.getAvailableAmount();
						depositDebitAmount += policyDepositEntity.getAvailableAmount();
					}
				}
				
				if (currentServiceAdjusted > 0 && availableAmount > 0) {
					if (masterPolicyContributiondetailEntity.getCurrentServices() <= policyDepositEntity.getAvailableAmount()) {
						renewalWithGiDebitCreditRequestModel.setFirstPremiumOtherCurrentServiceCreditAmount(currentServiceAdjusted);
						availableAmount -= currentServiceAdjusted;
						depositDebitAmount += currentServiceAdjusted;
					} else {
						renewalWithGiDebitCreditRequestModel.setFirstPremiumOtherCurrentServiceCreditAmount(policyDepositEntity.getAvailableAmount());
						pastServiceAdjusted -= policyDepositEntity.getAvailableAmount();
						availableAmount -= policyDepositEntity.getAvailableAmount();
						depositDebitAmount += policyDepositEntity.getAvailableAmount();
					}
				}
			}
//				Long depositDebitAmountLong=Math.round(depositDebitAmount);
			renewalWithGiDebitCreditRequestModel.setDepositDebitAmount(depositDebitAmount);
			getRenewalWithGiDebitCreditRequestModel.add(renewalWithGiDebitCreditRequestModel);
				
				}
			
			newBusinessContributionAndLifeCoverAdjustmentDto.setRenewalWithGiDebitCreditRequestModel(getRenewalWithGiDebitCreditRequestModel);
			newBusinessContributionAndLifeCoverAdjustmentDto.setRefNo(masterPolicyContributiondetailEntity.getId().toString());
		//	need to uncommand
			accountingService.consumeDepositsforSubsequence(newBusinessContributionAndLifeCoverAdjustmentDto,newmasterPolicyEntity.getId());
			
			
//			String prodAndVarientCodeSame=	commonModuleService.getProductCode(policyEntity.getProductId());
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
//			need to uncommand
			accountingService.unlockDeposits(showDepositLockDto, username);
		}
		contributionAdjustmentPropsEntity.setIsActive(false);
		contributionAdjustmentPropsEntity.setContriAdjStatus(contributionApproveStatusId);
		contributionAdjustmentPropsEntity=contributionAdjustmentPropsRepository.save(contributionAdjustmentPropsEntity);
		
		PolicyServiceEntitiy policyserviceentity = policyServiceRepository.findByPolicyandTypeandActive(renewalPolicyTMPEntity.getMasterPolicyId(), "contribution");
		policyserviceentity.setIsActive(false);
		policyServiceRepository.save(policyserviceentity);
		return ApiResponseDto.success(PolicyHelper.entityToDto(newmasterPolicyEntity));	
	}
	
	
	

}
