package com.lic.epgs.gratuity.policy.claim.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.lic.epgs.gratuity.accountingservice.dto.ClaimLiabilityBookingReqDto;
import com.lic.epgs.gratuity.accountingservice.dto.ClaimReverseLiabilityReqDto;
import com.lic.epgs.gratuity.accountingservice.dto.PayoutApproveResponse;
import com.lic.epgs.gratuity.accountingservice.entity.PayoutSpResponseEntity;
import com.lic.epgs.gratuity.accountingservice.repository.PayoutSpResponseRepository;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.ApiValidationResponse;
import com.lic.epgs.gratuity.common.entity.GratutityIcodesEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.common.repository.GratutityIcodesRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.fund.dto.CalculateResDto;
import com.lic.epgs.gratuity.fund.dto.CalculationReqDto;
import com.lic.epgs.gratuity.fund.dto.ClaimReqDto;
import com.lic.epgs.gratuity.fund.service.FundService;
import com.lic.epgs.gratuity.mph.entity.HistoryMPHEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.repository.HistoryMPHRepository;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.dto.ClaimPropSearchDetailDto;
import com.lic.epgs.gratuity.policy.claim.dto.GratuityCalculationsDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimBeneficiaryDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimPropsDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimPropsSearchDto;
import com.lic.epgs.gratuity.policy.claim.entity.PolicyTempSearchEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempMemberSearchEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempMphSearchEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimBeneficiaryEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimPropsEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimPropsSearchEntity;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimHelper;
import com.lic.epgs.gratuity.policy.claim.repository.TempPolicyClaimBeneficiaryRepository;
import com.lic.epgs.gratuity.policy.claim.repository.TempPolicyClaimPropsRepository;
import com.lic.epgs.gratuity.policy.claim.service.PolicyClaimService;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.HistoryGratutiyBenefitRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.lifecover.entity.HistoryLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.repository.HistoryLifeCoverRepository;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.repository.HistoryMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberAppointeeRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberBankAccountRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyContrySummaryRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalsGratuityBenefitPropsTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.repository.RenewalLifeCoverTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAddressEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.helper.RenewalPolicyTMPMemberHelper;
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
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyHistoryRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
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
import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkStgEntity;


@Service
public class PolicyClaimServiceImpl implements PolicyClaimService {

	protected final Logger logger = LogManager.getLogger(getClass());

	private static Long onboardedId = 202l;
	private static Long OnboardCancelled = 203l;
	@Value("${app.policy.claim.ModeOfExitDeath}")
	private String modeOfExitDeath;

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;

	@Value("${app.commonModuleServiceEndpoint}")
	private String endPoint;
	
	@Autowired
	private PayoutSpResponseRepository  payoutSpResponseRepository;

	@Autowired
	private StandardCodeRepository standardCodeRepository;
	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

	@Autowired
	private MasterPolicyContrySummaryRepository masterPolicyContrySummaryRepository;

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
	private MasterPolicyRepository masterPolicyRepository;
	
	@Autowired
	Environment environment;

	@Autowired
	PolicySchemeRuleHistoryRepository policySchemeRuleHistoryRepository;
	@Autowired
	PolicyValuationMatrixHistoryRepository policyValuationMatrixHistoryRepository;
	@Autowired
	PolicyValuationWithdrawalRateHistoryRepository policyValuationWithdrawalRateHistoryRepository;
	@Autowired
	PolicyValuationBasicHistoryRepository policyValuationBasicHistoryRepository;
	@Autowired
	PolicyValuationHistoryRepository policyValuationHistoryRepository;
	@Autowired
	HistoryMemberRepository historyMemberRepository;
	@Autowired
	HistoryGratutiyBenefitRepository historyGratutiyBenefitRepository;
	@Autowired
	HistoryLifeCoverRepository historyLifeCoverRepository;

	@Autowired
	private TempPolicyClaimBeneficiaryRepository tempPolicyClaimBeneficiaryRepository;

	@Autowired
	private TempMemberAppointeeRepository tempMemberAppointeeRepository;

	@Autowired
	private TempMemberBankAccountRepository tempMemberBankAccountRepository;

	@Autowired
	private AccountingService accountingService;
	
	@Autowired
	private GratutityIcodesRepository gratutityIcodesRepository;
	
	@Autowired
	private MemberCategoryModuleRepository memberCategoryModuleRepository;
	
	@Autowired
	private TaskProcessRepository taskProcessRepository;
	
	@Autowired
	private TaskAllocationRepository taskAllocationRepository;


	@Transactional
	@Override
	public ApiResponseDto<TempPolicyClaimPropsDto> createClaimforindividual(
			TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {
				
		int entity = tempPolicyClaimPropsRepository.findByMemberIdandPolicyId(tempPolicyClaimPropsDto.getPmstMemberId(), tempPolicyClaimPropsDto.getPmstPolicyId());
		if (entity > 0) {
			return ApiResponseDto.errorMessage(null, null, "already existed");
		}
		MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository
				.findById(tempPolicyClaimPropsDto.getPmstPolicyId());
		PolicyMemberEntity policyMemberEntity = policyMemberRepository.findByPolicyIdandMemberId(
				tempPolicyClaimPropsDto.getPmstPolicyId(), tempPolicyClaimPropsDto.getPmstMemberId());

		List<PolicyHistoryEntity> getPolicyHistoryEntity = policyHistoryRepository
				.findBymasterPolicyId(tempPolicyClaimPropsDto.getPmstPolicyId());
		TempPolicyClaimPropsEntity getTempPolicyClaimPropsEntity = new TempPolicyClaimPropsEntity();

		if (getPolicyHistoryEntity.size() > 0) {
			
		
			
			for (PolicyHistoryEntity policyHistoryEntities : getPolicyHistoryEntity) {
				HistoryMemberEntity historyMemberEntity = historyMemberRepository.findBypolicyIdandpmstMemberId(
						policyHistoryEntities.getId(), tempPolicyClaimPropsDto.getPmstMemberId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				new Timestamp(System.currentTimeMillis());
				Date dateOfExitwithdateform = null;
				Date dateEnd = null;
				Date dateStart = null;
				Date dateOfJoin = null;
				Date rcd = null;

				String dateOfExit = tempPolicyClaimPropsDto.getDateOfExit().toString();
				String str_StartDate = policyHistoryEntities.getPolicyStartDate().toString();
				String str_EndDate = policyHistoryEntities.getPolicyEndDate().toString();
				String str_rcd = masterPolicyEntity.getRiskCommencementDate().toString();
				String str_dateOfJoin = policyMemberEntity.getDojToScheme().toString();
				DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
				ZonedDateTime zdt = ZonedDateTime.parse(dateOfExit, f);

				LocalDate ldoe = zdt.toLocalDate();
				String str_AnnualRenewlDate = policyHistoryEntities.getAnnualRenewlDate().toString();
				String[] str_SplitARDDate = str_AnnualRenewlDate.split(" ");
				String[] str_SplitEndDate = str_EndDate.split(" ");
				String[] str_SplitStartDate = str_StartDate.split(" ");
				String[] str_SplitRcd = str_rcd.split(" ");
				String[] str_SplitDateOfJoin = str_dateOfJoin.split(" ");
				try {
					dateOfExitwithdateform = sdf.parse(ldoe.toString());
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

						if (tempPolicyClaimPropsDto.getModeOfExit() == 193) {

							if (dateOfExitwithdateform.compareTo(rcd) >= 0
									&& dateOfExitwithdateform.compareTo(dateEnd) <= 0) {
							} else {
								return ApiResponseDto.errorMessage(null, null,
										"Date Of Exit is not Between Risk Commencement Date & Policy End Date");

							}

						}
						getTempPolicyClaimPropsEntity=historyForCreateClaimforIndividual(policyHistoryEntities,masterPolicyEntity,policyMemberEntity,tempPolicyClaimPropsDto);


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
		} 
		
		if(getTempPolicyClaimPropsEntity.getOnboardNumber() == null) {
	
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

			Date dateOfExitwithdateform = null;
			Date dateEnd = null;
			Date dateStart = null;
			Date dateOfJoin = null;
			Date rcd = null;

			String dateOfExit = tempPolicyClaimPropsDto.getDateOfExit().toString();
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
			DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
			ZonedDateTime zdt = ZonedDateTime.parse(dateOfExit, f);

			LocalDate ld = zdt.toLocalDate();
			String str_AnnualRenewlDate = masterPolicyEntity.getAnnualRenewlDate().toString();
			String[] str_SplitARDDate = str_AnnualRenewlDate.split(" ");
			String[] str_SplitStartDate = str_StartDate.split(" ");
			String[] str_SplitEndDate = str_EndDate.split(" ");
			String[] str_SplitRcd = str_rcd.split(" ");
			String[] str_SplitDateOfJoin = str_dateOfJoin.split(" ");
			try {
				dateOfExitwithdateform = sdf.parse(ld.toString());
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

					if (tempPolicyClaimPropsDto.getModeOfExit() == 193) {

						if (dateOfExitwithdateform.compareTo(rcd) >= 0
								&& dateOfExitwithdateform.compareTo(dateEnd) <= 0) {
						} else {
							return ApiResponseDto.errorMessage(null,
									"Date Of Exit is not between Risk Commencement Date & Policy End Date",
									"Date Of Exit should be between Risk Commencement Date & Policy End Date");

						}

					}
					
					
					getTempPolicyClaimPropsEntity=masterPolicyForCreateClaimforIndividual(masterPolicyEntity,policyMemberEntity,tempPolicyClaimPropsDto);

				} else {
					return ApiResponseDto.errorMessage(null,
							"Date Of Exit is not Between Doj to scheme & Policy End Date",
							"Date Of Exit should be between DOJ to Scheme & Policy End Date");

				}

			} else {
				return ApiResponseDto.errorMessage(null,
						"Date Of Exit is not Between Policy Start Date & Policy End Date",
						"Date Of Exit should be between Policy Start Date & Policy End Date");

			}
		}
		
		policyMemberRepository.deactivateclaimmember(getTempPolicyClaimPropsEntity.getTmpPolicyId(),getTempPolicyClaimPropsEntity.getTmpMemberId());
		
	
		
		return ApiResponseDto.success(PolicyClaimHelper.claimpropstoDto(getTempPolicyClaimPropsEntity));
	}

	
	
	
	
	private TempPolicyClaimPropsEntity masterPolicyForCreateClaimforIndividual(MasterPolicyEntity masterPolicyEntity,
			PolicyMemberEntity policyMemberEntity, TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
				.copytoTmpForClaim(masterPolicyEntity);
		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
		PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();

		policyServiceEntitiy.setServiceType("Claim");
		policyServiceEntitiy.setPolicyId(tempPolicyClaimPropsDto.getPmstPolicyId());
		policyServiceEntitiy.setCreatedBy(tempPolicyClaimPropsDto.getCreatedBy());
		policyServiceEntitiy.setCreatedDate(new Date());
		policyServiceEntitiy.setIsActive(true);
		policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

		renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());

		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
		TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper
				.copytomastertoTemp(renewalPolicyTMPEntity.getId(), mPHEntity);
		tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
		renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
		renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
				.findBypolicyId(tempPolicyClaimPropsDto.getPmstPolicyId());
		RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
				.copyToTmpSchemeforClaim(policySchemeEntity, renewalPolicyTMPEntity.getId());
		renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

		List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
				.findBypmstPolicyId(tempPolicyClaimPropsDto.getPmstPolicyId());
		List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
				.copyToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
		memberCategoryRepository.saveAll(getmemberCategoryEntity);
		List<MemberCategoryModuleEntity> addMemberCategoryModuleEntity = new ArrayList<MemberCategoryModuleEntity>();

		for(MemberCategoryEntity getmemberCategoryEntities : memberCategoryEntity) {
			MemberCategoryModuleEntity memberCategoryModuleEntity=new MemberCategoryModuleEntity();
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
				.findByPolicyId(tempPolicyClaimPropsDto.getPmstPolicyId());

		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
				.copyToTmpLifeCoverforClaim(policyLifeCoverEntity,
						memberCategoryEntity, renewalPolicyTMPEntity.getId());

		renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
//		policyLifeCoverRepository.updateISActive(tempPolicyClaimPropsDto.getPmstPolicyId());

		List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
				.findBypolicyId(tempPolicyClaimPropsDto.getPmstPolicyId());

		List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
				.copyToTmpGratuityforClaim(policyGratuityBenefitEntity,
						memberCategoryEntity, renewalPolicyTMPEntity.getId());

		renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
//		policyGratuityBenefitRepository.updateIsActive(tempPolicyClaimPropsDto.getPmstPolicyId());

		Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
				.findByPolicyId(tempPolicyClaimPropsDto.getPmstPolicyId());
		RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationforClaim(policyValuationEntity, renewalPolicyTMPEntity.getId());
		renewalValuationTMPRepository.save(renewalValuationTMPEntity);

		Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
				.findByPolicyId(tempPolicyClaimPropsDto.getPmstPolicyId());
		RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity,
						renewalPolicyTMPEntity.getId());
		renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

		Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
				.findByPolicyId(tempPolicyClaimPropsDto.getPmstPolicyId());
		RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationBasicClaim(policyValutationBasicEntity, renewalPolicyTMPEntity.getId());
		renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

		List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
				.findByPolicyId(tempPolicyClaimPropsDto.getPmstPolicyId());
		List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
				.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity,
						renewalPolicyTMPEntity.getId());
		renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);

		RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
				.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity,
						memberCategoryEntity, renewalPolicyTMPEntity.getId());
		renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);
		TempPolicyClaimPropsEntity getTempPolicyClaimPropsEntity =new TempPolicyClaimPropsEntity();
		getTempPolicyClaimPropsEntity = PolicyClaimCommonHelper.copytotmpforclaim(
				renewalPolicyTMPMemberEntity.getId(), renewalPolicyTMPEntity.getId(),
				tempPolicyClaimPropsDto);
		getTempPolicyClaimPropsEntity.setOnboardNumber(
				tempPolicyClaimPropsRepository.getSequence(HttpConstants.ONBORDING_MODULE));
		getTempPolicyClaimPropsEntity.setClaimStatusId(onboardedId);
		getTempPolicyClaimPropsEntity.setOnboardingDate(new Date());
		tempPolicyClaimPropsRepository.save(getTempPolicyClaimPropsEntity);
		return getTempPolicyClaimPropsEntity;
	}





	private TempPolicyClaimPropsEntity historyForCreateClaimforIndividual(
			PolicyHistoryEntity policyHistoryEntities, MasterPolicyEntity masterPolicyEntity, PolicyMemberEntity policyMemberEntity, TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {

					RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
							.copytoHistoryForClaim(policyHistoryEntities);
					renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

					PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();

					policyServiceEntitiy.setServiceType("Claim");
					policyServiceEntitiy.setPmstHisPolicyId(policyHistoryEntities.getId());
					policyServiceEntitiy.setCreatedBy(tempPolicyClaimPropsDto.getCreatedBy());
					policyServiceEntitiy.setCreatedDate(new Date());
					policyServiceEntitiy.setIsActive(true);
					policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

					renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
					renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
					HistoryMPHEntity historyMPHEntity = historyMPHRepository
							.findById(policyHistoryEntities.getMasterpolicyHolder()).get();
					TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper
							.copytoHistoTemp(renewalPolicyTMPEntity.getId(), historyMPHEntity);
					tempMPHRepository.save(tempMPHEntity);

					renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
					renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
					Optional<PolicySchemeRuleHistoryEntity> PolicySchemeRuleHistoryEntity = policySchemeRuleHistoryRepository
							.findBypolicyId(policyHistoryEntities.getId());
					RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
							.copyToHistorySchemeforClaim(PolicySchemeRuleHistoryEntity,
									renewalPolicyTMPEntity.getId());
					renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

					List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
							.findBypmstHisPolicyId(policyHistoryEntities.getId());
					List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
							.copyhisToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
					memberCategoryRepository.saveAll(getmemberCategoryEntity);
					
					List<MemberCategoryModuleEntity> addMemberCategoryModuleEntity = new ArrayList<MemberCategoryModuleEntity>();

					for(MemberCategoryEntity getmemberCategoryEntities : memberCategoryEntity) {
						MemberCategoryModuleEntity memberCategoryModuleEntity=new MemberCategoryModuleEntity();
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
							policyHistoryEntities.getId(), tempPolicyClaimPropsDto.getPmstMemberId());
					RenewalPolicyTMPMemberEntity renewalMemberEntity = PolicyClaimCommonHelper
							.copyToHistoryMemberforClaim(historyMemberEntity, getmemberCategoryEntity,
									memberCategoryEntity, renewalPolicyTMPEntity.getId());
					renewalPolicyTMPMemberRepository.save(renewalMemberEntity);

					List<HistoryLifeCoverEntity> historyLifeCoverEntity = historyLifeCoverRepository
							.findBypolicyId(policyHistoryEntities.getId());
					List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
							.copyToHistoryLifeCoverforClaim(historyLifeCoverEntity,
									memberCategoryEntity, renewalPolicyTMPEntity.getId());
					renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);

					List<HistoryGratuityBenefitEntity> historyGratuityBenefitEntity = historyGratutiyBenefitRepository
							.findBypolicyId(policyHistoryEntities.getId());
					List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
							.copyToHistoryGratuityforClaim(historyGratuityBenefitEntity,
									memberCategoryEntity, renewalPolicyTMPEntity.getId());
					renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);

					Optional<PolicyValuationHistoryEntity> policyValuationHistoryEntity = policyValuationHistoryRepository
							.findBypolicyId(policyHistoryEntities.getId());
					RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
							.copyToHistoryValuationforClaim(policyValuationHistoryEntity,
									renewalPolicyTMPEntity.getId());
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
					TempPolicyClaimPropsEntity getTempPolicyClaimPropsEntity =new TempPolicyClaimPropsEntity();
					getTempPolicyClaimPropsEntity = PolicyClaimCommonHelper.copytotmpforclaim(
							renewalMemberEntity.getId(), renewalPolicyTMPEntity.getId(), tempPolicyClaimPropsDto);
					getTempPolicyClaimPropsEntity.setOnboardNumber(
							tempPolicyClaimPropsRepository.getSequence(HttpConstants.ONBORDING_MODULE));
					getTempPolicyClaimPropsEntity.setClaimStatusId(onboardedId);
					getTempPolicyClaimPropsEntity.setOnboardingDate(new Date());
					tempPolicyClaimPropsRepository.save(getTempPolicyClaimPropsEntity);

					// return
					// ApiResponseDto.created(PolicyClaimHelper.claimpropstoDto(getTempPolicyClaimPropsEntity));

			

			
		return getTempPolicyClaimPropsEntity;
	}


	
	@Override
	public ApiResponseDto<TempPolicyClaimPropsDto> getIndividualOnboardingno(String onboardNumber) {

		TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity = tempPolicyClaimPropsRepository
				.findByonboardNumber(onboardNumber);

		return ApiResponseDto.created(PolicyClaimHelper.claimpropstoDto(tempPolicyClaimPropsEntity));
	}

	@Override
	public ApiResponseDto<TempPolicyClaimPropsDto> onboardingClaimCancel(String onboardNumber) {
		TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity = tempPolicyClaimPropsRepository
				.findByonboardNumber(onboardNumber);
		tempPolicyClaimPropsEntity.setClaimStatusId(OnboardCancelled);
		tempPolicyClaimPropsRepository.save(tempPolicyClaimPropsEntity);
		return ApiResponseDto.created(PolicyClaimHelper.claimpropstoDto(tempPolicyClaimPropsEntity));
	}

	@Override
	public ApiResponseDto<List<TempPolicyClaimPropsDto>> filter(
			TempPolicyClaimPropsSearchDto policyClaimPropsSearchDto) {
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TempMemberSearchEntity> createQuery = criteriaBuilder.createQuery(TempMemberSearchEntity.class);

		Root<TempMemberSearchEntity> root = createQuery.from(TempMemberSearchEntity.class);
//		List<Long> ids;
		System.out.print(policyClaimPropsSearchDto.getClaimStatusId() != null);

		Join<TempMemberSearchEntity, TempPolicyClaimPropsSearchEntity> join = root.join("memberSearch");

//		data from child Table(claimProps)
//		predicates.add(join.get("claimStatusId").in(202L,203L));
		if (StringUtils.isNotBlank(policyClaimPropsSearchDto.getOnboardNumber())
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getOnboardNumber())) {
			predicates
					.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(join.get("onboardNumber"))),
							policyClaimPropsSearchDto.getOnboardNumber().toLowerCase()));
		}
		if (StringUtils.isNotBlank(policyClaimPropsSearchDto.getPayoutNo())
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getPayoutNo())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(join.get("payoutNo"))),
					policyClaimPropsSearchDto.getPayoutNo().toLowerCase()));
		}
		if ((policyClaimPropsSearchDto.getModeOfExit() != null)) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(criteriaBuilder.lower(join.get("modeOfExit"))),
					policyClaimPropsSearchDto.getModeOfExit()));
		}
		if (StringUtils.isNotBlank(policyClaimPropsSearchDto.getInitimationNumber())
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getInitimationNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(join.get("initimationNumber")),
					policyClaimPropsSearchDto.getInitimationNumber().toLowerCase()));
		}
		if ((policyClaimPropsSearchDto.getClaimStatusId() != null)
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getClaimStatusId().toString())) {
			predicates.add(join.get("claimStatusId").in(policyClaimPropsSearchDto.getClaimStatusId()));
		}

		if (policyClaimPropsSearchDto.getLicId() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getLicId())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("licId")),
					policyClaimPropsSearchDto.getLicId().toLowerCase()));
		}
		if (policyClaimPropsSearchDto.getPanNumber() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getPanNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("panNumber")),
					policyClaimPropsSearchDto.getPanNumber().toLowerCase()));
		}
		if (policyClaimPropsSearchDto.getAadharNumber() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getAadharNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("aadharNumber")),
					policyClaimPropsSearchDto.getAadharNumber().toLowerCase()));
		}
		if (policyClaimPropsSearchDto.getEmployeeCode() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getEmployeeCode())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("employeeCode")),
					policyClaimPropsSearchDto.getEmployeeCode().toLowerCase()));
		}

//		old 
		if (StringUtils.isNotBlank(policyClaimPropsSearchDto.getClaimType())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("claimType")),
					policyClaimPropsSearchDto.getClaimType().toLowerCase()));
		}
		if (policyClaimPropsSearchDto.getTmpPolicyId() != null && policyClaimPropsSearchDto.getTmpPolicyId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("tmpPolicyId"), policyClaimPropsSearchDto.getTmpPolicyId()));
		}
		if (policyClaimPropsSearchDto.getTmpMemberId() != null && policyClaimPropsSearchDto.getTmpMemberId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("tmpMemberId"), policyClaimPropsSearchDto.getTmpMemberId()));
		}
		if (policyClaimPropsSearchDto.getDateOfDeath() != null) {
			predicates.add(criteriaBuilder.equal(root.get("dateOfDeath"), policyClaimPropsSearchDto.getDateOfDeath()));
		}
		if (policyClaimPropsSearchDto.getDateOfExit() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getDateOfExit().toString())) {
			predicates.add(criteriaBuilder.equal(root.get("dateOfExit"), policyClaimPropsSearchDto.getDateOfExit()));
		}

		predicates.add(criteriaBuilder.equal(join.get("isActive"), Boolean.TRUE));
		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<TempMemberSearchEntity> entities = new ArrayList<TempMemberSearchEntity>();
		entities = entityManager.createQuery(createQuery).getResultList();

		// claim detail get using Query and Onboard Number start
		List<TempPolicyClaimPropsDto> getListClaimPropsDto = new ArrayList<TempPolicyClaimPropsDto>();
		new ArrayList<ClaimPropSearchDetailDto>();
		for (TempMemberSearchEntity singleTempPolicyClaimPropsEntity : entities) {

			TempPolicyClaimPropsDto get = PolicyClaimHelper.memberentityToDtoFilter(singleTempPolicyClaimPropsEntity);

			for (TempPolicyClaimPropsSearchEntity getTempPolicyClaimPropsSearchEntity : singleTempPolicyClaimPropsEntity
					.getMemberSearch()) {
//				ClaimPropSearchDetailDto tempPolicyClaimPropsSearchDto=PolicyClaimHelper.entityToDtoFilter(getTempPolicyClaimPropsSearchEntity);
				List<Object[]> getTempPolicySearchList = tempPolicyClaimPropsRepository
						.findByOnboardNumber(getTempPolicyClaimPropsSearchEntity.getOnboardNumber());

				if (getTempPolicySearchList.isEmpty()) {
					get.setClaimPropSearchDetailDto(null);
				} else {
					get.setClaimPropSearchDetailDto(PolicyClaimHelper.getObjectToDto(getTempPolicySearchList));
				}

			}

			getListClaimPropsDto.add(get);

		}
//		 end
//		return null;
		return ApiResponseDto.success(getListClaimPropsDto);

	}

	@Override
	public ApiResponseDto<List<TempPolicyClaimPropsDto>> getClaimBasedonStatusIds(Long[] claimstatusids) {

		List<Long> ids = Arrays.asList(claimstatusids);
		List<TempPolicyClaimPropsEntity> tempPolicyClaimPropsEntity = tempPolicyClaimPropsRepository
				.findByclaimstatusids(ids);
		return ApiResponseDto.success(
				tempPolicyClaimPropsEntity.stream().map(PolicyClaimHelper::entityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<TempPolicyClaimPropsDto> updateIntimationDocumentsDetails(Long id,
			TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {
		TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity = tempPolicyClaimPropsRepository.findById(id).get();
		tempPolicyClaimPropsEntity.setIsClaimFromReceived(tempPolicyClaimPropsDto.getIsClaimFromReceived());
		tempPolicyClaimPropsEntity
				.setIsDeathCertificateReceived(tempPolicyClaimPropsDto.getIsDeathCertificateReceived());

		return ApiResponseDto.success(
				PolicyClaimHelper.entityToDto(tempPolicyClaimPropsRepository.save(tempPolicyClaimPropsEntity)));

	}

	@Transactional
	@Override
	public ApiResponseDto<TempPolicyClaimPropsDto> intimationClaimUpdate(String onboardNumber,
			TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {
		
		TaskProcessEntity taskProcessEntity =taskProcessRepository.findByProcessName("CLAIM INTIMATION");
		TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();
		
		TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity = tempPolicyClaimPropsRepository
				.findByonboardNumber(onboardNumber);
		tempPolicyClaimPropsEntity.setIsClaimFromReceived(tempPolicyClaimPropsDto.getIsClaimFromReceived());
		tempPolicyClaimPropsEntity
				.setIsDeathCertificateReceived(tempPolicyClaimPropsDto.getIsDeathCertificateReceived());
		tempPolicyClaimPropsEntity.setSalaryAsOnDateOfExit(tempPolicyClaimPropsDto.getSalaryAsOnDateOfExit());
		tempPolicyClaimPropsEntity.setGratuityAmtOnDateExit(tempPolicyClaimPropsDto.getGratuityAmtOnDateExit());
		tempPolicyClaimPropsEntity.setModifiedGratuityAmount(tempPolicyClaimPropsDto.getModifiedGratuityAmount());
		tempPolicyClaimPropsEntity.setLcSumAssured(tempPolicyClaimPropsDto.getLcSumAssured());
		tempPolicyClaimPropsEntity.setIsPremiumRefund(tempPolicyClaimPropsDto.getIsPremiumRefund());
		tempPolicyClaimPropsEntity.setRefundPremiumAmount(tempPolicyClaimPropsDto.getRefundPremiumAmount());
		tempPolicyClaimPropsEntity.setPenalAmount(tempPolicyClaimPropsDto.getPenalAmount());
		tempPolicyClaimPropsEntity.setCourtAward(tempPolicyClaimPropsDto.getCourtAward());
		tempPolicyClaimPropsEntity.setClaimStatusId(tempPolicyClaimPropsDto.getClaimStatusId());
		tempPolicyClaimPropsEntity.setModifiedBy(tempPolicyClaimPropsDto.getModifiedBy());
		tempPolicyClaimPropsEntity.setRefundGSTAmount(tempPolicyClaimPropsDto.getRefundGSTAmount());
		// Deleted From Here
		
		taskAllocationEntity.setTaskStatus(tempPolicyClaimPropsEntity.getClaimStatusId().toString());
		taskAllocationEntity.setRequestId(tempPolicyClaimPropsEntity.getOnboardNumber());
		taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//		taskAllocationEntity.setLocationType(quotationRenewalDto.getUnitCode());
		taskAllocationEntity.setCreatedBy(tempPolicyClaimPropsDto.getModifiedBy());
		taskAllocationEntity.setCreatedDate(new Date());
	
		taskAllocationEntity.setModulePrimaryId(tempPolicyClaimPropsEntity.getId());
		taskAllocationEntity.setIsActive(true);
		taskAllocationRepository.save(taskAllocationEntity);
		
		return ApiResponseDto.success(
				PolicyClaimHelper.entityToDto(tempPolicyClaimPropsRepository.save(tempPolicyClaimPropsEntity)));

	}

	@Transactional
	@Override
	public ApiResponseDto<TempPolicyClaimPropsDto> claimstatuschange(Long id, Long statusId,
			TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {
		TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity = tempPolicyClaimPropsRepository.findById(id).get();

		// Added here
		if (tempPolicyClaimPropsDto.getClaimStatusId() == 206) {
			if (tempPolicyClaimPropsDto.getInitimationNumber() == null) {
				tempPolicyClaimPropsEntity.setIntimationDate(new Date());
				
				TaskProcessEntity taskProcessEntity =taskProcessRepository.findByProcessName("CLAIM INTIMATION");
				TaskAllocationEntity taskAllocationEntity =taskAllocationRepository.findBymodulePrimaryId(tempPolicyClaimPropsEntity.getId());
				
				
				
				tempPolicyClaimPropsEntity
						.setInitimationNumber(tempPolicyClaimPropsRepository.getSequence(HttpConstants.CLAIMINTI));
				if(taskAllocationEntity!=null) {
				taskAllocationEntity.setTaskStatus(tempPolicyClaimPropsEntity.getClaimStatusId().toString());
				taskAllocationEntity.setRequestId(tempPolicyClaimPropsEntity.getInitimationNumber());
				taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//				taskAllocationEntity.setLocationType(quotationRenewalDto.getUnitCode());
				taskAllocationEntity.setModulePrimaryId(tempPolicyClaimPropsEntity.getId());
				taskAllocationEntity.setIsActive(true);
				taskAllocationRepository.save(taskAllocationEntity);
				}

			}
		}
		
		tempPolicyClaimPropsEntity.setClaimStatusId(tempPolicyClaimPropsDto.getClaimStatusId());
		TempPolicyClaimPropsEntity newTempPolicyClaimPropsEntity = tempPolicyClaimPropsRepository
				.save(tempPolicyClaimPropsEntity);
		
		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findBymodulePrimaryId(tempPolicyClaimPropsEntity.getId());
		if(taskallocationentity!=null) {
		taskallocationentity.setTaskStatus(newTempPolicyClaimPropsEntity.getClaimStatusId().toString());
		taskAllocationRepository.save(taskallocationentity);
		}
		

		if (newTempPolicyClaimPropsEntity.getClaimStatusId() == 203
				|| newTempPolicyClaimPropsEntity.getClaimStatusId() == 207
				|| newTempPolicyClaimPropsEntity.getClaimStatusId() == 212) {
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
					.findByTmpPolicyId(newTempPolicyClaimPropsEntity.getTmpPolicyId());
			PolicyServiceEntitiy policyServiceEntitiy = policyServiceRepository
					.findByPolicyServiceId(renewalPolicyTMPEntity.getPolicyServiceId());
			policyServiceEntitiy.setIsActive(false);
			policyServiceRepository.save(policyServiceEntitiy);
		}

		// 206 - Intimation Approved
		if (isDevEnvironment == false && newTempPolicyClaimPropsEntity.getClaimStatusId() == 206) {
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
					.findById(newTempPolicyClaimPropsEntity.getTmpPolicyId()).get();
			ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto = getClaimLiabilityBookingReqDto(
					newTempPolicyClaimPropsEntity, renewalPolicyTMPEntity);

			claimLiabilityBookingReqDto.setGlTransactionVO(accountingService.getGlTransactionModel(
					renewalPolicyTMPEntity.getProductId(), renewalPolicyTMPEntity.getProductVariantId(),
					renewalPolicyTMPEntity.getUnitCode(), "Gratuity Claim Intimation Booking"));
			claimLiabilityBookingReqDto.setJournalVoucherDetailModel(
					accountingService.getJournalVoucherDetailModel(renewalPolicyTMPEntity.getLineOfBusiness(),
							renewalPolicyTMPEntity.getProductId(), renewalPolicyTMPEntity.getProductVariantId()));

			if (newTempPolicyClaimPropsEntity.getModeOfExit() == 193) {
				if (newTempPolicyClaimPropsEntity.getClaimType().equals("Individual")
						|| newTempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
					accountingService.intimateDeathClaimforNonCourt(claimLiabilityBookingReqDto, "SYSTEM",
							"CLAIM-INTIMATION", newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");
				} else {
					claimLiabilityBookingReqDto
							.setDebitConsumerForumAward(tempPolicyClaimPropsEntity.getCourtAward() == null ? 0.0
									: tempPolicyClaimPropsEntity.getCourtAward());
					accountingService.intimateDeathClaim(claimLiabilityBookingReqDto, "SYSTEM", "CLAIM-INTIMATION",
							newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");
				}
			} else if (newTempPolicyClaimPropsEntity.getModeOfExit() == 194) {
				if (newTempPolicyClaimPropsEntity.getClaimType().equals("Individual")
						|| newTempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
					accountingService.intimateMaturityClaimforNonCourt(claimLiabilityBookingReqDto, "SYSTEM",
							"CLAIM-INTIMATION", newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");
				} else {

					accountingService.intimateMaturityClaim(claimLiabilityBookingReqDto, "SYSTEM", "CLAIM-INTIMATION",
							newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");
				}
			} else if (newTempPolicyClaimPropsEntity.getModeOfExit() == 195) {

				if (newTempPolicyClaimPropsEntity.getClaimType().equals("Individual")
						|| newTempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
					accountingService.intimateWithdrawalClaimforNonClaim(claimLiabilityBookingReqDto, "SYSTEM",
							"CLAIM-INTIMATION", newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");

				} else {

					accountingService.intimateWithdrawalClaim(claimLiabilityBookingReqDto, "SYSTEM", "CLAIM-INTIMATION",
							newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");
				}
			}
		}

		// 212 Payout Rejected
		if (isDevEnvironment == false && newTempPolicyClaimPropsEntity.getClaimStatusId() == 212) {
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
					.findById(newTempPolicyClaimPropsEntity.getTmpPolicyId()).get();
			ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto = getClaimReverseLiabilityReqDto(
					newTempPolicyClaimPropsEntity, renewalPolicyTMPEntity);
			claimReverseLiabilityReqDto.setGlTransactionVO(accountingService.getGlTransactionModel(
					renewalPolicyTMPEntity.getProductId(), renewalPolicyTMPEntity.getProductVariantId(),
					renewalPolicyTMPEntity.getUnitCode(), "Gratuity Claim Intimation Booking"));
			claimReverseLiabilityReqDto.setJournalVoucherDetailModel(
					accountingService.getJournalVoucherDetailModel(renewalPolicyTMPEntity.getLineOfBusiness(),
							renewalPolicyTMPEntity.getProductId(), renewalPolicyTMPEntity.getProductVariantId()));

			if (newTempPolicyClaimPropsEntity.getModeOfExit() == 193) {

				if (newTempPolicyClaimPropsEntity.getClaimType().equals("Individual")
						|| newTempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
					accountingService.reverseDeathClaimIntimationNonCourtCase(claimReverseLiabilityReqDto, "SYSTEM",
							"CLAIM-PAYOUT-REJ", newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");
				} else {

					accountingService.reverseDeathClaimIntimation(claimReverseLiabilityReqDto, "SYSTEM",
							"CLAIM-PAYOUT-REJ", newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");
				}

			} else if (newTempPolicyClaimPropsEntity.getModeOfExit() == 194) {

				if (newTempPolicyClaimPropsEntity.getClaimType().equals("Individual")
						|| newTempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
					accountingService.reverseMaturityClaimIntimationforNonCourt(claimReverseLiabilityReqDto, "SYSTEM",
							"CLAIM-PAYOUT-REJ", newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");
				} else {
					accountingService.reverseMaturityClaimIntimation(claimReverseLiabilityReqDto, "SYSTEM",
							"CLAIM-PAYOUT-REJ", newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");

				}
			} else if (newTempPolicyClaimPropsEntity.getModeOfExit() == 195) {

				if (newTempPolicyClaimPropsEntity.getClaimType().equals("Individual")
						|| newTempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
					accountingService.reverseWithdrawalClaimIntimationforNonCourt(claimReverseLiabilityReqDto, "SYSTEM",
							"CLAIM-PAYOUT-REJ", newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");
				} else {

					accountingService.reverseWithdrawalClaimIntimation(claimReverseLiabilityReqDto, "SYSTEM",
							"CLAIM-PAYOUT-REJ", newTempPolicyClaimPropsEntity.getId(), "CLAIM-PROPS-ID");
				}
			}
		}

		return ApiResponseDto.success(PolicyClaimHelper.entityToDto(newTempPolicyClaimPropsEntity));
	}

	private ClaimLiabilityBookingReqDto getClaimLiabilityBookingReqDto(
			TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity, RenewalPolicyTMPEntity renewalPolicyTMPEntity) {
		TempMPHEntity tempMPHEntity = tempMPHRepository.findBytmpPolicyId(tempPolicyClaimPropsEntity.getTmpPolicyId())
				.get();
//		Date date= tempPolicyClaimPropsEntity.getDateOfExit();	
//		 LocalDate dateOfExit = date.toInstant().atZone(ZonedDateTime.now().getZone()).toLocalDate();
//		 int year = dateOfExit.getYear();
//		 int financialYr = year+1;
//		 String financialYear = year+"-"+financialYr;
//		 
//		 RenewalPolicyTMPEntity tmp =renewalPolicyTMPRepository.getByPolicyId(tempPolicyClaimPropsEntity.getTmpPolicyId());
//		 Long mastrPolicyId=tmp.getMasterPolicyId();		 
//		 MasterPolicyContributionEntity cont= masterPolicyContrySummaryRepository.findByPolicyIdAndFinancial(mastrPolicyId,financialYear);
//		 Long contriId=cont.getContributionDetailId();
//		 MasterPolicyContributionDetails masterContriDetails = masterPolicyRepository.findByContriIdAndEntryType(contriId);

		Double totalPayable = 0.0;
		Double gratuityPayable = 0.0;
		if (tempPolicyClaimPropsEntity.getModifiedGratuityAmount() != null
				&& tempPolicyClaimPropsEntity.getModifiedGratuityAmount() > 0) {
			totalPayable += tempPolicyClaimPropsEntity.getModifiedGratuityAmount();
			gratuityPayable = tempPolicyClaimPropsEntity.getModifiedGratuityAmount();
		} else {
			totalPayable += tempPolicyClaimPropsEntity.getGratuityAmtOnDateExit();
			gratuityPayable = tempPolicyClaimPropsEntity.getGratuityAmtOnDateExit();
		}
		if (tempPolicyClaimPropsEntity.getModeOfExit() == 193) {
			if (tempPolicyClaimPropsEntity.getLcSumAssured() != null
					&& tempPolicyClaimPropsEntity.getLcSumAssured() > 0)
				totalPayable += tempPolicyClaimPropsEntity.getLcSumAssured();
		}
		if (tempPolicyClaimPropsEntity.getCourtAward() != null && tempPolicyClaimPropsEntity.getCourtAward() > 0)
			totalPayable += tempPolicyClaimPropsEntity.getCourtAward();
		if (tempPolicyClaimPropsEntity.getPenalAmount() != null && tempPolicyClaimPropsEntity.getPenalAmount() > 0)
			totalPayable += tempPolicyClaimPropsEntity.getPenalAmount();
		if (tempPolicyClaimPropsEntity.getRefundPremiumAmount() != null
				&& tempPolicyClaimPropsEntity.getRefundPremiumAmount() > 0)
			totalPayable += tempPolicyClaimPropsEntity.getRefundPremiumAmount();
//		if (tempPolicyClaimPropsEntity.getRefundGSTAmount() != null && tempPolicyClaimPropsEntity.getRefundGSTAmount() > 0)
//			totalPayable += tempPolicyClaimPropsEntity.getRefundGSTAmount();

		ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto = new ClaimLiabilityBookingReqDto();
		claimLiabilityBookingReqDto.setMphCode(tempMPHEntity.getMphCode());
		claimLiabilityBookingReqDto.setPolicyNo(renewalPolicyTMPEntity.getPolicyNumber());
		claimLiabilityBookingReqDto.setRefNo(tempPolicyClaimPropsEntity.getInitimationNumber());
		claimLiabilityBookingReqDto.setUnitCode(renewalPolicyTMPEntity.getUnitCode());
		claimLiabilityBookingReqDto.setUserCode(tempPolicyClaimPropsEntity.getCreatedBy());

		claimLiabilityBookingReqDto.setDebitFundAmount(gratuityPayable == null ? 0.0 : gratuityPayable);

		claimLiabilityBookingReqDto.setDebitPenalInterest(tempPolicyClaimPropsEntity.getPenalAmount() == null ? 0.0
				: tempPolicyClaimPropsEntity.getPenalAmount());

		if (tempPolicyClaimPropsEntity.getModeOfExit() == 193) {
			claimLiabilityBookingReqDto
					.setDebitLifeCoverSumAssuredPayable(tempPolicyClaimPropsEntity.getLcSumAssured() == null ? 0.0
							: tempPolicyClaimPropsEntity.getLcSumAssured());
			if (!tempPolicyClaimPropsEntity.getClaimType().equals("Individual")
					|| !tempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
				claimLiabilityBookingReqDto
						.setDebitConsumerForumAward(tempPolicyClaimPropsEntity.getCourtAward() == null ? 0.0
								: tempPolicyClaimPropsEntity.getCourtAward());
			}
		}

		if (tempPolicyClaimPropsEntity.getModeOfExit() == 195) {

			if (!tempPolicyClaimPropsEntity.getClaimType().equals("Individual")
					|| !tempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
				claimLiabilityBookingReqDto
						.setDebitConsumerForumAwardExpenses(tempPolicyClaimPropsEntity.getCourtAward() == null ? 0.0
								: tempPolicyClaimPropsEntity.getCourtAward());
			}
		}

		if (tempPolicyClaimPropsEntity.getModeOfExit() == 194) {
			if ((totalPayable.longValue() - totalPayable) > 0.0) {
				claimLiabilityBookingReqDto.setDebitSRAmount(totalPayable.longValue() - totalPayable);
				claimLiabilityBookingReqDto.setCreditSRAmount(0.0);
			} else {
				claimLiabilityBookingReqDto.setDebitSRAmount(totalPayable - totalPayable.longValue());
				claimLiabilityBookingReqDto.setCreditSRAmount(0.0);
			}
			if (tempPolicyClaimPropsEntity.getClaimType().equals("Individual")
					|| tempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
			claimLiabilityBookingReqDto
					.setDebitReFundFirstPremiumOYRGTA(tempPolicyClaimPropsEntity.getRefundPremiumAmount() == null ? 0.0
							: tempPolicyClaimPropsEntity.getRefundPremiumAmount());
			claimLiabilityBookingReqDto.setDebitReFundOtherFirstPremiumOYRGTA(0.0);

			claimLiabilityBookingReqDto.setDebitReFundRenewalPremiumOYRGTA(0.0);
		
			
			claimLiabilityBookingReqDto.setCreditOutStandingAmount(claimLiabilityBookingReqDto.getDebitFundAmount()
					+ claimLiabilityBookingReqDto.getDebitLifeCoverSumAssuredPayable()
					+ claimLiabilityBookingReqDto.getDebitPenalInterest()
					+ claimLiabilityBookingReqDto.getDebitReFundFirstPremiumOYRGTA()
					+ claimLiabilityBookingReqDto.getDebitReFundOtherFirstPremiumOYRGTA()
					+ claimLiabilityBookingReqDto.getDebitReFundRenewalPremiumOYRGTA()
					+ claimLiabilityBookingReqDto.getDebitSRAmount()
					+ claimLiabilityBookingReqDto.getDebitConsumerForumAward()
					+ claimLiabilityBookingReqDto.getDebitConsumerForumAwardExpenses());
			
			}else {
				claimLiabilityBookingReqDto
				.setDebitReFundFirstPremiumOYRGTA(tempPolicyClaimPropsEntity.getRefundPremiumAmount() == null ? 0.0
						: tempPolicyClaimPropsEntity.getRefundPremiumAmount());
		claimLiabilityBookingReqDto.setDebitReFundOtherFirstPremiumOYRGTA(0.0);

				claimLiabilityBookingReqDto.setDebitReFundOfRenewalPremiumOYRGTA(0.0);
				claimLiabilityBookingReqDto.setDebitReFundRenewalPremiumOYRGTA(null);
				claimLiabilityBookingReqDto.setCreditOutStandingAmount(claimLiabilityBookingReqDto.getDebitFundAmount()
						+ claimLiabilityBookingReqDto.getDebitLifeCoverSumAssuredPayable()
						+ claimLiabilityBookingReqDto.getDebitPenalInterest()
						+ claimLiabilityBookingReqDto.getDebitReFundFirstPremiumOYRGTA()
						+ claimLiabilityBookingReqDto.getDebitReFundOtherFirstPremiumOYRGTA()
						+ claimLiabilityBookingReqDto.getDebitReFundOfRenewalPremiumOYRGTA()
						+ claimLiabilityBookingReqDto.getDebitSRAmount()
						+ claimLiabilityBookingReqDto.getDebitConsumerForumAward()
						+ claimLiabilityBookingReqDto.getDebitConsumerForumAwardExpenses());

			}
		} else {

			claimLiabilityBookingReqDto.setDebitReFundOfFirstPremiumOYRGTA(
					tempPolicyClaimPropsEntity.getRefundPremiumAmount() == null ? 0.0
							: tempPolicyClaimPropsEntity.getRefundPremiumAmount());
			claimLiabilityBookingReqDto.setDebitReFundOfOtherFirstPremiumOYRGTA(0.0);
			claimLiabilityBookingReqDto.setDebitReFundOfOtherFirstYearPremiumOYRGTA(0.0);
			claimLiabilityBookingReqDto.setDebitReFundOfRenewalPremiumOYRGTA(0.0);
			if ((totalPayable.longValue() - totalPayable) > 0.0) {
				claimLiabilityBookingReqDto.setDebitSrAccount(totalPayable.longValue() - totalPayable);
				claimLiabilityBookingReqDto.setCreditSrAccount(0.0);
			} else {
				claimLiabilityBookingReqDto.setDebitSrAccount(totalPayable - totalPayable.longValue());
				claimLiabilityBookingReqDto.setCreditSrAccount(0.0);
			}
			claimLiabilityBookingReqDto.setCreditOutstandingAccount(claimLiabilityBookingReqDto.getDebitFundAmount()
					+ claimLiabilityBookingReqDto.getDebitLifeCoverSumAssuredPayable()
					+ claimLiabilityBookingReqDto.getDebitPenalInterest()
					+ claimLiabilityBookingReqDto.getDebitReFundOfFirstPremiumOYRGTA()
					+ claimLiabilityBookingReqDto.getDebitReFundOfOtherFirstPremiumOYRGTA()
					+ claimLiabilityBookingReqDto.getDebitReFundOfRenewalPremiumOYRGTA()
					+ claimLiabilityBookingReqDto.getDebitSrAccount()
					+ claimLiabilityBookingReqDto.getDebitConsumerForumAward()
					+ claimLiabilityBookingReqDto.getDebitConsumerForumAwardExpenses());
		}

		return claimLiabilityBookingReqDto;
	}

	private ClaimReverseLiabilityReqDto getClaimReverseLiabilityReqDto(
			TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity, RenewalPolicyTMPEntity renewalPolicyTMPEntity) {
		TempMPHEntity tempMPHEntity = tempMPHRepository.findBytmpPolicyId(tempPolicyClaimPropsEntity.getTmpPolicyId())
				.get();

		Double totalPayable = 0.0;
		Double gratuityPayable = 0.0;
		if (tempPolicyClaimPropsEntity.getModifiedGratuityAmount() != null
				&& tempPolicyClaimPropsEntity.getModifiedGratuityAmount() > 0) {
			totalPayable += tempPolicyClaimPropsEntity.getModifiedGratuityAmount();
			gratuityPayable = tempPolicyClaimPropsEntity.getModifiedGratuityAmount();
		} else {
			totalPayable += tempPolicyClaimPropsEntity.getGratuityAmtOnDateExit();
			gratuityPayable = tempPolicyClaimPropsEntity.getGratuityAmtOnDateExit();
		}

		if (tempPolicyClaimPropsEntity.getModeOfExit() == 193) {
			if (tempPolicyClaimPropsEntity.getLcSumAssured() != null
					&& tempPolicyClaimPropsEntity.getLcSumAssured() > 0)
				totalPayable += tempPolicyClaimPropsEntity.getLcSumAssured();
		}
		if (tempPolicyClaimPropsEntity.getCourtAward() != null && tempPolicyClaimPropsEntity.getCourtAward() > 0)
			totalPayable += tempPolicyClaimPropsEntity.getCourtAward();
		if (tempPolicyClaimPropsEntity.getPenalAmount() != null && tempPolicyClaimPropsEntity.getPenalAmount() > 0)
			totalPayable += tempPolicyClaimPropsEntity.getPenalAmount();
		if (tempPolicyClaimPropsEntity.getRefundPremiumAmount() != null
				&& tempPolicyClaimPropsEntity.getRefundPremiumAmount() > 0)
			totalPayable += tempPolicyClaimPropsEntity.getRefundPremiumAmount();
//		if (tempPolicyClaimPropsEntity.getRefundGSTAmount() != null && tempPolicyClaimPropsEntity.getRefundGSTAmount() > 0)
//			totalPayable += tempPolicyClaimPropsEntity.getRefundGSTAmount();

		ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto = new ClaimReverseLiabilityReqDto();
		claimReverseLiabilityReqDto.setMphCode(tempMPHEntity.getMphCode());
		claimReverseLiabilityReqDto.setPolicyNo(renewalPolicyTMPEntity.getPolicyNumber());
		claimReverseLiabilityReqDto.setRefNo(tempPolicyClaimPropsEntity.getInitimationNumber());
		claimReverseLiabilityReqDto.setUnitCode(renewalPolicyTMPEntity.getUnitCode());
		claimReverseLiabilityReqDto.setUserCode(tempPolicyClaimPropsEntity.getCreatedBy());
		claimReverseLiabilityReqDto.setCreditFundAmount(gratuityPayable == null ? 0.0 : gratuityPayable);
		claimReverseLiabilityReqDto.setCreditPenalInterest(tempPolicyClaimPropsEntity.getPenalAmount() == null ? 0.0
				: tempPolicyClaimPropsEntity.getPenalAmount());


		if (tempPolicyClaimPropsEntity.getModeOfExit() == 193) {
			claimReverseLiabilityReqDto
					.setCreditLifeCoverSumAssuredPayable(tempPolicyClaimPropsEntity.getLcSumAssured() == null ? 0.0
							: tempPolicyClaimPropsEntity.getLcSumAssured());

			if (!tempPolicyClaimPropsEntity.getClaimType().equals("Individual")
					|| !tempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
				claimReverseLiabilityReqDto
						.setCreditConsumerForumAward(tempPolicyClaimPropsEntity.getCourtAward() == null ? 0.0
								: tempPolicyClaimPropsEntity.getCourtAward());
			}
		}
		if (tempPolicyClaimPropsEntity.getModeOfExit() == 195) {
		

			if (!tempPolicyClaimPropsEntity.getClaimType().equals("Individual")
					|| !tempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
				claimReverseLiabilityReqDto
						.setCreditConsumerForumAwardExpenses(tempPolicyClaimPropsEntity.getCourtAward() == null ? 0.0
								: tempPolicyClaimPropsEntity.getCourtAward());
			}
		}
		
		if (tempPolicyClaimPropsEntity.getModeOfExit() == 194) {
			if ((totalPayable.longValue() - totalPayable) > 0.0) {
				claimReverseLiabilityReqDto.setCreditSRAmount(totalPayable.longValue() - totalPayable);
				claimReverseLiabilityReqDto.setDebitSRAmount(0.0);
			} else {
				claimReverseLiabilityReqDto.setCreditSRAmount(totalPayable - totalPayable.longValue());
				claimReverseLiabilityReqDto.setDebitSRAmount(0.0);
			}
			if (tempPolicyClaimPropsEntity.getClaimType().equals("Individual")
					|| tempPolicyClaimPropsEntity.getClaimType().equals("BulkUpload")) {
			claimReverseLiabilityReqDto.setCreditReFundRenewalPremiumOYRGTA(0.0);
			claimReverseLiabilityReqDto
					.setCreditReFundFirstPremiumOYRGTA(tempPolicyClaimPropsEntity.getRefundPremiumAmount() == null ? 0.0
							: tempPolicyClaimPropsEntity.getRefundPremiumAmount());
			claimReverseLiabilityReqDto.setCreditReFundOtherFirstPremiumOYRGTA(0.0);
			
			
			claimReverseLiabilityReqDto.setDebitOutStandingAmount(claimReverseLiabilityReqDto.getCreditFundAmount()
					+ claimReverseLiabilityReqDto.getCreditLifeCoverSumAssuredPayable()
					+ claimReverseLiabilityReqDto.getCreditPenalInterest()
					+ claimReverseLiabilityReqDto.getCreditReFundFirstPremiumOYRGTA()
					+ claimReverseLiabilityReqDto.getCreditReFundOtherFirstPremiumOYRGTA()
					+ claimReverseLiabilityReqDto.getCreditReFundRenewalPremiumOYRGTA()
					+ claimReverseLiabilityReqDto.getCreditSRAmount()
					+ claimReverseLiabilityReqDto.getCreditConsumerForumAward()
					+ claimReverseLiabilityReqDto.getCreditConsumerForumAwardExpenses());
			
			}else {
				claimReverseLiabilityReqDto
				.setCreditReFundFirstPremiumOYRGTA(tempPolicyClaimPropsEntity.getRefundPremiumAmount() == null ? 0.0
						: tempPolicyClaimPropsEntity.getRefundPremiumAmount());
		claimReverseLiabilityReqDto.setCreditReFundOtherFirstPremiumOYRGTA(0.0);
				
			
				claimReverseLiabilityReqDto.setCreditReFundOfRenewalPremiumOYRGTA(0.0);
				claimReverseLiabilityReqDto.setCreditReFundRenewalPremiumOYRGTA(null);
				claimReverseLiabilityReqDto.setDebitOutStandingAmount(claimReverseLiabilityReqDto.getCreditFundAmount()
						+ claimReverseLiabilityReqDto.getCreditLifeCoverSumAssuredPayable()
						+ claimReverseLiabilityReqDto.getCreditPenalInterest()
						+ claimReverseLiabilityReqDto.getCreditReFundFirstPremiumOYRGTA()
						+ claimReverseLiabilityReqDto.getCreditReFundOtherFirstPremiumOYRGTA()
						+ claimReverseLiabilityReqDto.getCreditReFundOfRenewalPremiumOYRGTA()
						+ claimReverseLiabilityReqDto.getCreditSRAmount()
						+ claimReverseLiabilityReqDto.getCreditConsumerForumAward()
						+ claimReverseLiabilityReqDto.getCreditConsumerForumAwardExpenses());
				
			}
	
		}
		else {

			claimReverseLiabilityReqDto.setCreditReFundOfFirstPremiumOYRGTA(
					tempPolicyClaimPropsEntity.getRefundPremiumAmount() == null ? 0.0
							: tempPolicyClaimPropsEntity.getRefundPremiumAmount());
			claimReverseLiabilityReqDto.setCreditReFundOfOtherFirstPremiumOYRGTA(0.0);
			claimReverseLiabilityReqDto.setCreditReFundOfOtherFirstYearPremiumOYRGTA(0.0);
			claimReverseLiabilityReqDto.setCreditReFundOfRenewalPremiumOYRGTA(0.0);
			if ((totalPayable.longValue() - totalPayable) > 0.0) {
				claimReverseLiabilityReqDto.setCreditSrAccount(totalPayable.longValue() - totalPayable);
				claimReverseLiabilityReqDto.setDebitSrAccount(0.0);
			} else {
				claimReverseLiabilityReqDto.setCreditSrAccount(totalPayable - totalPayable.longValue());
				claimReverseLiabilityReqDto.setDebitSrAccount(0.0);
			}
			claimReverseLiabilityReqDto.setDebitOutstandingAccount(claimReverseLiabilityReqDto.getCreditFundAmount()
					+ claimReverseLiabilityReqDto.getCreditLifeCoverSumAssuredPayable()
					+ claimReverseLiabilityReqDto.getCreditPenalInterest()
					+ claimReverseLiabilityReqDto.getCreditReFundOfFirstPremiumOYRGTA()
					+ claimReverseLiabilityReqDto.getCreditReFundOfOtherFirstPremiumOYRGTA()
					+ claimReverseLiabilityReqDto.getCreditReFundOfRenewalPremiumOYRGTA()
					+ claimReverseLiabilityReqDto.getCreditSrAccount()
					+ claimReverseLiabilityReqDto.getCreditConsumerForumAward()
					+ claimReverseLiabilityReqDto.getCreditConsumerForumAwardExpenses());
		}
		return claimReverseLiabilityReqDto;
	}

	@Transactional
	@Override
	public ApiResponseDto<List<TempPolicyClaimBeneficiaryDto>> beneficiaryDetailsSave(
			TempPolicyClaimBeneficiaryDto tempPolicyClaimBeneficiaryDto) {

		// if (tempPolicyClaimBeneficiaryRepository
		// .existsByclaimPropsId(tempPolicyClaimBeneficiaryDto.getClaimPropsId())) {

		// tempPolicyClaimBeneficiaryRepository.deleteClaimProsID(tempPolicyClaimBeneficiaryDto.getClaimPropsId());
		// }

		TempPolicyClaimBeneficiaryEntity tempPolicyClaimBeneficiaryEntity = PolicyClaimHelper
				.dtotoentity(tempPolicyClaimBeneficiaryDto);

		tempPolicyClaimBeneficiaryEntity.setBeneficiaryType(tempPolicyClaimBeneficiaryDto.getBeneficiaryType());

		tempPolicyClaimBeneficiaryEntity.setClaimPropsId(tempPolicyClaimBeneficiaryDto.getClaimPropsId());
		tempPolicyClaimBeneficiaryEntity.setCreatedBy(tempPolicyClaimBeneficiaryDto.getCreatedBy());
		tempPolicyClaimBeneficiaryEntity.setCreatedDate(new Date());
		tempPolicyClaimBeneficiaryEntity.setMemberTmpBankId(tempPolicyClaimBeneficiaryDto.getMemberTmpBankId());
		tempPolicyClaimBeneficiaryEntity.setNomineeTmpBankId(tempPolicyClaimBeneficiaryDto.getNomineeTmpBankId());
		tempPolicyClaimBeneficiaryEntity.setAppointeeTmpBankId(tempPolicyClaimBeneficiaryDto.getAppointeeTmpBankId());
		tempPolicyClaimBeneficiaryEntity.setParentId(tempPolicyClaimBeneficiaryDto.getParentId());
		tempPolicyClaimBeneficiaryRepository.save(tempPolicyClaimBeneficiaryEntity);
		List<TempPolicyClaimBeneficiaryEntity> policyClaimBeneficiaryEntity = tempPolicyClaimBeneficiaryRepository
				.findByclaimPropsId(tempPolicyClaimBeneficiaryEntity.getClaimPropsId());
		return ApiResponseDto.success(policyClaimBeneficiaryEntity.stream()
				.map(PolicyClaimHelper::beneficiaryEntityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<TempPolicyClaimBeneficiaryDto> beneficiaryDetailsUpdate(
			TempPolicyClaimBeneficiaryDto tempPolicyClaimBeneficiaryDto, Long id) {

		TempPolicyClaimBeneficiaryEntity tempPolicyClaimBeneficiaryEntity = tempPolicyClaimBeneficiaryRepository
				.findById(id).get();

		tempPolicyClaimBeneficiaryEntity.setBeneficiaryType(tempPolicyClaimBeneficiaryDto.getBeneficiaryType());
		tempPolicyClaimBeneficiaryEntity.setClaimPropsId(tempPolicyClaimBeneficiaryDto.getClaimPropsId());
		tempPolicyClaimBeneficiaryEntity.setCreatedBy(tempPolicyClaimBeneficiaryDto.getCreatedBy());
		tempPolicyClaimBeneficiaryEntity.setCreatedDate(tempPolicyClaimBeneficiaryDto.getCreatedDate());

		tempPolicyClaimBeneficiaryEntity.setMemberTmpBankId(tempPolicyClaimBeneficiaryDto.getMemberTmpBankId());
		tempPolicyClaimBeneficiaryEntity.setModifiedBy(tempPolicyClaimBeneficiaryDto.getModifiedBy());
		tempPolicyClaimBeneficiaryEntity.setModifiedDate(new Date());
		tempPolicyClaimBeneficiaryEntity.setMphTmpBankId(tempPolicyClaimBeneficiaryDto.getMphTmpBankId());
		tempPolicyClaimBeneficiaryEntity.setNomineeTmpBankId(tempPolicyClaimBeneficiaryDto.getNomineeTmpBankId());
		tempPolicyClaimBeneficiaryEntity.setAppointeeTmpBankId(tempPolicyClaimBeneficiaryDto.getAppointeeTmpBankId());
		tempPolicyClaimBeneficiaryEntity.setParentId(tempPolicyClaimBeneficiaryDto.getParentId());
		return ApiResponseDto.success(PolicyClaimHelper
				.beneficiaryEntityToDto(tempPolicyClaimBeneficiaryRepository.save(tempPolicyClaimBeneficiaryEntity)));
	}

	@Override
	public ApiResponseDto<List<TempPolicyClaimBeneficiaryDto>> beneficiaryDetailsget(Long id) {
		List<TempPolicyClaimBeneficiaryEntity> tempPolicyClaimBeneficiaryEntity = tempPolicyClaimBeneficiaryRepository
				.findByclaimPropsId(id);
		return ApiResponseDto.success(tempPolicyClaimBeneficiaryEntity.stream()
				.map(PolicyClaimHelper::beneficiaryEntityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<GratuityCalculationsDto> calculategratuity(GratuityCalculationsDto gratitutyCalculationDto) {

		RenewalPolicyTMPMemberEntity getRenewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository
				.findBytmpPolicyId1(gratitutyCalculationDto.getTmppolicyid());
		if (gratitutyCalculationDto.getSalary() == null) {
			gratitutyCalculationDto.setSalary(getRenewalPolicyTMPMemberEntity.getBasicSalary());
		}
		Integer pastservice = 0;
		Date dateOfAppointmentdiff = null;
		String dateOfExitdiff = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateOfAppointment = getRenewalPolicyTMPMemberEntity.getDateOfAppointment();
		Date str_DateofExit = gratitutyCalculationDto.getDateOfExist();

//		String str_DateofExit = gratitutyCalculationDto.getDateOfExist().toString();
//		DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss").withLocale(Locale.US);
//		ZonedDateTime zdt = ZonedDateTime.parse(str_DateofExit, f);
		String[] get = dateOfAppointment.toString().split(" ");
		System.out.println(get[0]);
		LocalDate startDate = LocalDate.parse(get[0]);

//		LocalDate ld = zdt.toLocalDate();
		try {
//			dateOfAppointmentdiff = sdf.parse(dateOfAppointment.toString());
			dateOfExitdiff = sdf.format(str_DateofExit);
//			dateOfExitdiff = ld;

		} catch (Exception e) {

		}

//		String[] getstr_DateofExit=str_DateofExit.toString().split(" ");
//		System.out.println(getstr_DateofExit[0]);

		LocalDate endDate = LocalDate.parse(dateOfExitdiff);
		Period period = Period.between(startDate, endDate);
//		String str_DateofAppoint = dateOfAppointmentdiff.toString();
//		ZonedDateTime doa = ZonedDateTime.parse(str_DateofAppoint, f);
//
//		LocalDate ldDateofappoint = doa.toLocalDate();

//		String dateofExit = dateOfExitdiff.toString();
//		ZonedDateTime doe = ZonedDateTime.parse(dateofExit, f);
//
//		LocalDate ldDateofExit = doe.toLocalDate();

		// Calculate the difference between the two dates
//		Period period = Period.between(startDate, endDate);

		// Output the result
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

		List<RenewalGratuityBenefitTMPEntity> getRenewalGratuityBenefitTMPEntities = renewalGratuityBenefitTMPRepository
				.findBytmpPolicyIdAndCategoryId(gratitutyCalculationDto.getTmppolicyid(),
						getRenewalPolicyTMPMemberEntity.getCategoryId());
		for (RenewalGratuityBenefitTMPEntity renewalGratuityBenefitTMPEntity : getRenewalGratuityBenefitTMPEntities) {
			renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId();
			if (renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 25
					|| renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 26) {
				for (RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity : renewalGratuityBenefitTMPEntity
						.getRenewalgratuityPropsBenefit()) {
					if (renewalsGratuityBenefitPropsTMPEntity.getIsActive() == true) {
						gratuityAmount = gratuityAmount + ((renewalsGratuityBenefitPropsTMPEntity.getNumberOfDaysWage()
								.doubleValue()
								/ renewalsGratuityBenefitPropsTMPEntity.getNumberOfWorkingDaysPerMonth().doubleValue())
								* gratitutyCalculationDto.getSalary()) * pastservice.doubleValue();
					}
				}
			}
			if (renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 27
					&& renewalGratuityBenefitTMPEntity.getGratutiySubBenefitId() == 185) {
				for (RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity : renewalGratuityBenefitTMPEntity
						.getRenewalgratuityPropsBenefit()) {

					if (renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService() >= pastservice
							|| renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService() <= pastservice) {
						if (renewalsGratuityBenefitPropsTMPEntity.getIsActive() == true) {
						calcgvalue = calcgvalue + ((renewalsGratuityBenefitPropsTMPEntity.getNumberOfDaysWage()
								.doubleValue()
								/ renewalsGratuityBenefitPropsTMPEntity.getNumberOfWorkingDaysPerMonth().doubleValue())
								* gratitutyCalculationDto.getSalary())
								* renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService().doubleValue();
						gratuityAmount = gratuityAmount + calcgvalue;
						break;
						}
					}
				}
			}
			if (renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 27
					&& renewalGratuityBenefitTMPEntity.getGratutiySubBenefitId() == 186) {
				for (RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity : renewalGratuityBenefitTMPEntity
						.getRenewalgratuityPropsBenefit()) {
					if (renewalsGratuityBenefitPropsTMPEntity.getIsActive() == true) {
					if (renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService() <= pastservice) {

						calcgvalue = calcgvalue
								+ ((renewalsGratuityBenefitPropsTMPEntity.getNumberOfDaysWage().doubleValue()
										/ renewalsGratuityBenefitPropsTMPEntity.getNumberOfWorkingDaysPerMonth()
												.doubleValue()
										* gratitutyCalculationDto.getSalary())
										* renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService()
												.doubleValue());
						gratuityAmount = gratuityAmount + calcgvalue;
					}
					}
				}
			}
		}
		GratuityCalculationsDto gratutityCalculationDto = new GratuityCalculationsDto();

		gratutityCalculationDto.setGratuityamount((double) (Math.round(gratuityAmount * 100) / 100D));
		gratutityCalculationDto.setPastservice(pastservice);
		return ApiResponseDto.created(gratutityCalculationDto);
	}

	@Override
	public ApiResponseDto<GratuityCalculationsDto> RefundCalculation(GratuityCalculationsDto gratitutyCalculationDto) {
		// if no Death or NULL mean need to calcultat bothh current ARD and Future ARD
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(gratitutyCalculationDto.getTmppolicyid()).get();
		// using tmppolicyid get MasterPolicyId
		Long masterPolicyId = renewalPolicyTMPEntity.getMasterPolicyId();
		RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository
				.findByTmpMemberIdandTmpPolicyId(gratitutyCalculationDto.getTmppolicyid(),
						gratitutyCalculationDto.getTmpMemberId());

		Long masterMemberId = renewalPolicyTMPMemberEntity.getPmstMemebrId();

		GratuityCalculationsDto getGratitutyCalculationDto = null;

		System.out.println(gratitutyCalculationDto.getModeOfExit().equals(modeOfExitDeath));
		if (gratitutyCalculationDto.getModeOfExit().equals(modeOfExitDeath)) {
			System.out.println("in death if");

			getGratitutyCalculationDto = futureForDeathRefundCalculation(masterPolicyId, masterMemberId,
					gratitutyCalculationDto.getDateOfExist());
			System.out.println("death1" + getGratitutyCalculationDto.getRefundonGST());
			System.out.println("death2" + getGratitutyCalculationDto.getRefundPremium());

		} else {

			getGratitutyCalculationDto = CurrentRefundCalculation(masterPolicyId, masterMemberId,
					gratitutyCalculationDto.getDateOfExist());
			
		}

		GratuityCalculationsDto setFundCalculationValue = new GratuityCalculationsDto();

		setFundCalculationValue.setRefundPremium(getGratitutyCalculationDto.getRefundPremium());
		setFundCalculationValue.setRefundonGST(getGratitutyCalculationDto.getRefundonGST());
		return ApiResponseDto.created(setFundCalculationValue);
	}

	public GratuityCalculationsDto CurrentRefundCalculation(Long masterPolicyId, Long masterMemberId,
															Date dateOfExist) {

		Long months = 0l;
		// Long changed to float
		Double singleMonthPremium = 0.0;
		Double singleMonthGST = 0.0;
		// long changed to float
		Double refundOnPremium = 0.0;
		Double refundOnGST = 0.0;

		String dateofexitconvert = DateUtils.dateToStringDDMMYYYY(dateOfExist);
		// To Get dateOfExit and add 1year
		LocalDate localDate = dateOfExist.toInstant().atZone(ZonedDateTime.now().getZone()).toLocalDate();
		int year = localDate.getYear();
		int financialYear = year + 1;
		// FinancialYear
		LocalDate financialDate = LocalDate.of(financialYear, Month.SEPTEMBER, 30);
		// CurrentDate
		Date currentDate = new Date();
		LocalDate currDate = currentDate.toInstant().atZone(ZonedDateTime.now().getZone()).toLocalDate();

		Long totalMonths = 12l;

		PolicyServiceEntitiy policyServiceEntitiy2 = null;
		PolicyHistoryEntity newPolicyHistoryEntity = policyHistoryRepository.findBymasterPolicyId(masterPolicyId,
				dateofexitconvert);
		if (newPolicyHistoryEntity != null) {
			new SimpleDateFormat("yyyy-MM-dd");

			Date dateOfPolicyEndDate = newPolicyHistoryEntity.getPolicyEndDate();

			String str_DateofExit = dateOfExist.toString();

			DateTimeFormatter formate1 = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);

			ZonedDateTime zdt1 = ZonedDateTime.parse(str_DateofExit, formate1);

			LocalDate ld1 = zdt1.toLocalDate();
//				try {
//					dateOfAnnualRenewalDate = sdf.parse(dateOfARD.toString());
//				} catch (Exception e) {
			//
//				}
			DateTimeFormatter formate2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s").withLocale(Locale.US);
			String str_DateOfPolicyEndDate = dateOfPolicyEndDate.toString();
			LocalDate doPED = LocalDate.parse(str_DateOfPolicyEndDate, formate2);



			LocalDate startDate = LocalDate.parse(ld1.toString());
			LocalDate endDate = LocalDate.parse(doPED.toString());

			// Calculate the difference between the two dates
			Period period = Period.between(startDate, endDate);

			// Output the result
//				System.out.printf("The difference between %s and %s is %d years, %d months, and %d days.",
//						period.getYears(), period.getMonths(), period.getDays());

			months = Long.valueOf(period.getMonths());

			if (period.getYears() > 0) {
				months = months + Long.valueOf(period.getYears()) * 12;
			}

			System.out.println("mo" + months);
			HistoryMemberEntity getHistoryMemberEntity = historyMemberRepository
					.findBypolicyIdandpmstMemberId(newPolicyHistoryEntity.getId(), masterMemberId);
			if(newPolicyHistoryEntity.getPolicyServiceId()!=null){
				PolicyServiceEntitiy policyServiceEntitiy = policyServiceRepository
						.findById(newPolicyHistoryEntity.getPolicyServiceId()).get();
				StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
				if (policyServiceEntitiy.getServiceType().equals("renewal")
						&& getHistoryMemberEntity.getPmstMemberId() == masterMemberId) {

					// Double to Float
					singleMonthPremium = getHistoryMemberEntity.getLifeCoverPremium().doubleValue() / totalMonths;

					singleMonthGST = (getHistoryMemberEntity.getLifeCoverPremium().doubleValue()
							* Double.parseDouble(standardCodeEntity.getValue()) / 100) / totalMonths.doubleValue();

					refundOnPremium = refundOnPremium + singleMonthPremium * months;
					if (currDate.isBefore(financialDate)) {
						refundOnGST = refundOnGST + singleMonthGST * months;
					}
				}
			}

		}

		List<PolicyHistoryEntity> listPolicyHistoryEntity = policyHistoryRepository
				.findBymasterPolicyIdandDateofExit(masterPolicyId, dateofexitconvert);

		for (PolicyHistoryEntity getPolicyHistoryEntity : listPolicyHistoryEntity) {

			HistoryMemberEntity getHistoryMemberEntity = historyMemberRepository
					.findBypolicyIdandpmstMemberId(getPolicyHistoryEntity.getId(), masterMemberId);
			if (getPolicyHistoryEntity.getPolicyServiceId() != null) {

				policyServiceEntitiy2 = policyServiceRepository.findById(getPolicyHistoryEntity.getPolicyServiceId())
						.get();
			}
			StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
			if (getPolicyHistoryEntity.getPolicyStartDate().compareTo(dateOfExist) >= 0
					&& policyServiceEntitiy2.getServiceType().equals("renewal")
					&& getHistoryMemberEntity.getPmstMemberId() == masterMemberId) {
				// Double to Float
				singleMonthPremium = getHistoryMemberEntity.getLifeCoverPremium().doubleValue();
				singleMonthGST = (getHistoryMemberEntity.getLifeCoverPremium().doubleValue()
						* Double.parseDouble(standardCodeEntity.getValue()) / 100);
				refundOnPremium = refundOnPremium + singleMonthPremium;
				if (currDate.isBefore(financialDate)) {
					refundOnGST = refundOnGST + singleMonthGST;
				}
				System.out.println("2refundOnPremium " + refundOnPremium);
				System.out.println("refundOnGST " + refundOnGST);

			}

		}

//			System.out.println(DateUtils.dateToStringFormatYyyyMmDdHhMmSsSlash(dateOfExist));

		MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findBymasterPolicyId(masterPolicyId,
				dateofexitconvert);
		if (masterPolicyEntity != null) {

			new SimpleDateFormat("yyyy-MM-dd");

			Date dateOfPolicyEndDate = masterPolicyEntity.getPolicyEndDate();

			String str_DateofExit = dateOfExist.toString();

			DateTimeFormatter formate1 = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);

			ZonedDateTime zdt1 = ZonedDateTime.parse(str_DateofExit, formate1);

			LocalDate ld1 = zdt1.toLocalDate();
//				try {
//					dateOfAnnualRenewalDate = sdf.parse(dateOfARD.toString());
//				} catch (Exception e) {
			//
//				}
			String str_DateOfPolicyEndDate = dateOfPolicyEndDate.toString();
			String[] getEndDate = str_DateOfPolicyEndDate.split(" ");
			String getPED = getEndDate[0];
//				ZonedDateTime doPSD = ZonedDateTime.parse(str_DateOfPolicyStartDate, formate1);
//
//				LocalDate ldDateOfPSD = doPSD.toLocalDate();

			LocalDate startDate = LocalDate.parse(ld1.toString());
			LocalDate endDate = LocalDate.parse(getPED);

			// Calculate the difference between the two dates
			Period period = Period.between(startDate, endDate);

			// Output the result
//				System.out.printf("The difference between %s and %s is %d years, %d months, and %d days.",
//						period.getYears(), period.getMonths(), period.getDays());

			// policy end - date exit

			months = Long.valueOf(period.getMonths());

			if (period.getYears() > 0) {
				months = months + Long.valueOf(period.getYears()) * 12;
			}

			System.out.println("mo" + months);
			PolicyMemberEntity getPolicyMemberEntity = policyMemberRepository
					.findBymasterPolicyIDandmemberId(masterPolicyEntity.getId(), masterMemberId);
			StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
			if (getPolicyMemberEntity.getId().equals(masterMemberId)) {

				// Double to Float
				singleMonthPremium = getPolicyMemberEntity.getLifeCoverPremium().doubleValue() / totalMonths;

				singleMonthGST = (getPolicyMemberEntity.getLifeCoverPremium().doubleValue()
						* Double.parseDouble(standardCodeEntity.getValue()) / 100) / totalMonths.doubleValue();

				refundOnPremium = refundOnPremium + singleMonthPremium * months;
				if (currDate.isBefore(financialDate)) {
					refundOnGST = refundOnGST + singleMonthGST * months;
				}
			}

		}
		MasterPolicyEntity masterPolicyCheckSEWithDateofexit = masterPolicyCustomRepository
				.findByGreaterStartDateandExitDate(masterPolicyId, dateofexitconvert);
		if (masterPolicyCheckSEWithDateofexit != null) {
			PolicyMemberEntity masterMemberEntity = policyMemberRepository
					.findBymasterPolicyIDandmemberId(masterPolicyId, masterMemberId);
			StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);

			if (masterPolicyCheckSEWithDateofexit.getPolicyStartDate().compareTo(dateOfExist) >= 0

					&& masterMemberEntity.getId() == masterMemberId) {

				// Double to Float
				singleMonthPremium = masterMemberEntity.getLifeCoverPremium().doubleValue();
				singleMonthGST = (masterMemberEntity.getLifeCoverPremium().doubleValue()
						* Double.parseDouble(standardCodeEntity.getValue()) / 100);
				refundOnPremium = refundOnPremium + singleMonthPremium;
				if (currDate.isBefore(financialDate)) {
					refundOnGST = refundOnGST + singleMonthGST;
				}
				System.out.println("1refundOnPremium " + refundOnPremium);
				System.out.println("refundOnGST " + refundOnGST);
			}
		}
		System.out.println("out of conditionrefundOnPremium " + refundOnPremium);
		System.out.println("out of condition refundOnGST " + refundOnGST);
		GratuityCalculationsDto setFundCalculationValue = new GratuityCalculationsDto();
		setFundCalculationValue.setRefundPremium(refundOnPremium.doubleValue());
		setFundCalculationValue.setRefundonGST((double) (Math.round(refundOnGST * 100) / 100D));
		return setFundCalculationValue;

	}

	public GratuityCalculationsDto futureForDeathRefundCalculation(Long masterPolicyId, Long masterMemberId,
																   Date dateOfExist) {
		Long months = 12l;
		Double singleMonthPremium = 0.0;
		Double singleMonthGST = 0.0;
		Double refundOnPremium = 0.0;
		Double refundOnGST = 0.0;

		String dateofexitconvert = DateUtils.dateToStringDDMMYYYY(dateOfExist);
		// To Get dateOfExit and add 1year
		LocalDate localDate = dateOfExist.toInstant().atZone(ZonedDateTime.now().getZone()).toLocalDate();
		int year = localDate.getYear();
		int financialYear = year + 1;
		// FinancialYear
		LocalDate financialDate = LocalDate.of(financialYear, Month.SEPTEMBER, 30);
		// CurrentDate
		Date currentDate = new Date();
		LocalDate currDate = currentDate.toInstant().atZone(ZonedDateTime.now().getZone()).toLocalDate();

		PolicyServiceEntitiy policyServiceEntitiy2 = null;
		policyHistoryRepository.findBymasterPolicyId(masterPolicyId, dateofexitconvert);

		List<PolicyHistoryEntity> listPolicyHistoryEntity = policyHistoryRepository
				.findBymasterPolicyIdandDateofExit(masterPolicyId, dateofexitconvert);
		if (listPolicyHistoryEntity.size() > 0) {
			for (PolicyHistoryEntity getPolicyHistoryEntity : listPolicyHistoryEntity) {

				HistoryMemberEntity getHistoryMemberEntity = historyMemberRepository
						.findBypolicyIdandpmstMemberId(getPolicyHistoryEntity.getId(), masterMemberId);
				if (getPolicyHistoryEntity.getPolicyServiceId() != null) {

					policyServiceEntitiy2 = policyServiceRepository
							.findById(getPolicyHistoryEntity.getPolicyServiceId()).get();
				}
				StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
				if (getPolicyHistoryEntity.getPolicyStartDate().compareTo(dateOfExist) >= 0
						&& policyServiceEntitiy2.getServiceType().equals("renewal")
						&& getHistoryMemberEntity.getPmstMemberId() == masterMemberId) {

					singleMonthPremium = getHistoryMemberEntity.getLifeCoverPremium().doubleValue() / months;
					singleMonthGST = (getHistoryMemberEntity.getLifeCoverPremium().doubleValue()
							* Double.parseDouble(standardCodeEntity.getValue()) / 100) / months.doubleValue();
					refundOnPremium = refundOnPremium + singleMonthPremium * months;

					if (currDate.isBefore(financialDate)) {
						refundOnGST = refundOnGST + singleMonthGST * months;
					}

					System.out.println("refundOnPremium " + refundOnPremium);
					System.out.println("refundOnGST " + refundOnGST);

				}
			}
		}

		masterPolicyCustomRepository.findBymasterPolicyId(masterPolicyId, dateofexitconvert);

		MasterPolicyEntity masterPolicyCheckSEWithDateofexit = masterPolicyCustomRepository
				.findByGreaterStartDateandExitDate(masterPolicyId, dateofexitconvert);
		if (masterPolicyCheckSEWithDateofexit != null) {
			PolicyMemberEntity masterMemberEntity = policyMemberRepository
					.findBymasterPolicyIDandmemberId(masterPolicyId, masterMemberId);
			StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
			if (masterPolicyCheckSEWithDateofexit.getPolicyStartDate().compareTo(dateOfExist) >= 0
					&& masterMemberEntity.getId().equals(masterMemberId)) {

				singleMonthPremium = masterMemberEntity.getLifeCoverPremium().doubleValue() / months;
				singleMonthGST = (masterMemberEntity.getLifeCoverPremium().doubleValue()
						* Double.parseDouble(standardCodeEntity.getValue()) / 100) / months.doubleValue();
				refundOnPremium = refundOnPremium + singleMonthPremium * months;
				if (currDate.isBefore(financialDate)) {
					refundOnGST = refundOnGST + singleMonthGST * months;
				}

				System.out.println("refundOnPremium " + refundOnPremium);
				System.out.println("refundOnGST " + refundOnGST);
			}
		}

		GratuityCalculationsDto setFundCalculationValue = new GratuityCalculationsDto();
		setFundCalculationValue.setRefundPremium((double) (Math.round(refundOnPremium * 100) / 100D));
		setFundCalculationValue.setRefundonGST((double) (Math.round(refundOnGST * 100) / 100D));
		return setFundCalculationValue;

	}

	@Transactional
	@Override
	public ApiResponseDto<TempPolicyClaimPropsDto> payoutapprove(Long id, String username) {

		Long claimstatusid = 211l;
		Long policystatusid = 123l;
		Long statusid = 222l;
		//Payout Approve store procedure start
	
		
		TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity = tempPolicyClaimPropsRepository.findById(id).get();
		tempPolicyClaimPropsEntity.setClaimStatusId(claimstatusid);
		tempPolicyClaimPropsEntity.setPayoutNo(tempPolicyClaimPropsRepository.getSequence(HttpConstants.PAYOUT));
		tempPolicyClaimPropsEntity.setPayoutDate(new Date());
		tempPolicyClaimPropsRepository.save(tempPolicyClaimPropsEntity);

	
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(tempPolicyClaimPropsEntity.getTmpPolicyId()).get();
		renewalPolicyTMPEntity.setIsActive(false);
		renewalPolicyTMPEntity.setPolicyStatusId(policystatusid);
		renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

		PolicyServiceEntitiy policyServiceEntitiy = policyServiceRepository
				.findById(renewalPolicyTMPEntity.getPolicyServiceId()).get();
		policyServiceEntitiy.setIsActive(false);
		policyServiceRepository.save(policyServiceEntitiy);

		if(isDevEnvironment == false) {
			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery(environment.getProperty("accounting.schema.name") + "."+ environment.getProperty("neft.reject.store.proc.name"));
			setInputParameters(storedProcedureQuery);
			
			storedProcedureQuery.setParameter("PAYOUTNUMBER", tempPolicyClaimPropsEntity.getPayoutNo());
			storedProcedureQuery.setParameter("INTIMATION_NUMBER", tempPolicyClaimPropsEntity.getInitimationNumber());
			storedProcedureQuery.setParameter("PRODUCTCODE", commonModuleService.getProductCode(renewalPolicyTMPEntity.getProductId()));
			storedProcedureQuery.setParameter("VARIANTCODE", commonModuleService.getProductCode(renewalPolicyTMPEntity.getProductId()));
			storedProcedureQuery.execute();
			
//			tempPolicyClaimPropsRepository.callPayoutApproveProcedure(tempPolicyClaimPropsEntity.getPayoutNo(),commonModuleService.getProductCode(renewalPolicyTMPEntity.getProductId()),
//					commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()));
			
//			PayoutSpResponseEntity payoutSpResponseEntity=payoutSpResponseRepository.findBypayoutNumber(tempPolicyClaimPropsEntity.getPayoutNo());
//			
//			if(payoutSpResponseEntity.getStatusCode()=="1") {
//				return ApiResponseDto.errorMessage(null, payoutSpResponseEntity.getStatusCode(), "PayoutApprove");
//			}
			
		}
		
		
		
		List<RenewalPolicyTMPMemberEntity> renewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository
				.findBytmpPolicyId(tempPolicyClaimPropsEntity.getTmpPolicyId());
		for (RenewalPolicyTMPMemberEntity get : renewalPolicyTMPMemberEntity) {
			PolicyMemberEntity policyMemberEntity = policyMemberRepository.findById(get.getPmstMemebrId()).get();
			policyMemberEntity.setStatusId(statusid);
			policyMemberRepository.save(policyMemberEntity);

		}
		ClaimReqDto claimReqDto = new ClaimReqDto();
		claimReqDto.setPolicyId(renewalPolicyTMPEntity.getMasterPolicyId());
		claimReqDto.setTransactionDate(new Date());
		claimReqDto.setDateOfExit(tempPolicyClaimPropsEntity.getDateOfExit());
		claimReqDto.setBatch(false);
		claimReqDto.setAuto(true);
		claimReqDto.setRecalculate(true);
		claimReqDto.setUpdateSubType("Claim");
		claimReqDto.setModule("Claim");
		Double totalClaimAmount = 0.0;
		if (tempPolicyClaimPropsEntity.getModifiedGratuityAmount() != null
				&& tempPolicyClaimPropsEntity.getModifiedGratuityAmount() > 0)
			totalClaimAmount += tempPolicyClaimPropsEntity.getModifiedGratuityAmount();
		else
			totalClaimAmount += tempPolicyClaimPropsEntity.getGratuityAmtOnDateExit();
//		if (tempPolicyClaimPropsEntity.getLcSumAssured() != null && tempPolicyClaimPropsEntity.getLcSumAssured() > 0)
//			totalClaimAmount += tempPolicyClaimPropsEntity.getLcSumAssured();
		claimReqDto.setPurchasePrice(totalClaimAmount);
		claimReqDto.setCommutationAmt(0.0);
	
		fundService.setClaimEntry(claimReqDto);

//		policyMemberRepository.deactivateclaimmember(tempPolicyClaimPropsEntity.getTmpPolicyId(),tempPolicyClaimPropsEntity.getTmpMemberId());
		
	
		return ApiResponseDto.success(
				PolicyClaimHelper.entityToDto(tempPolicyClaimPropsRepository.save(tempPolicyClaimPropsEntity)));

	}

	
	private void setInputParameters(StoredProcedureQuery storedProcedureQuery) {
	
		storedProcedureQuery.registerStoredProcedureParameter("PAYOUTNUMBER", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("INTIMATION_NUMBER", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("PRODUCTCODE", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("VARIANTCODE", String.class, ParameterMode.IN);
	}





	@Transactional
	@Override
	public ApiResponseDto<List<TempPolicyClaimPropsDto>> importClaimData(Long pmstPolicyId, Long batchId,
			String username) {



		List<MemberBulkStgEntity> temps = getQuotationMembers(batchId);

		for (MemberBulkStgEntity temp : temps) {

			MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(pmstPolicyId);

			PolicyMemberEntity policyMemberEntity = policyMemberRepository.findByPolicyIdandMemberId(pmstPolicyId,
					temp.getPmstMemberId());
			logger.info(" pmstPolicyId ");
			logger.info(pmstPolicyId);
			List<PolicyHistoryEntity> getPolicyHistoryEntity = policyHistoryRepository
					.findBymasterPolicyId(pmstPolicyId);
			logger.info("SIZE");
			logger.info(getPolicyHistoryEntity.size());

			TempPolicyClaimPropsEntity getTempPolicyClaimPropsEntity = new TempPolicyClaimPropsEntity();
			if (getPolicyHistoryEntity.size() > 0) {

				for (PolicyHistoryEntity policyHistoryEntities : getPolicyHistoryEntity) {
					HistoryMemberEntity historyMemberEntity = historyMemberRepository.findBypolicyIdandpmstMemberId(
							policyHistoryEntities.getId(), temp.getPmstMemberId());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					new Timestamp(System.currentTimeMillis());
					Date dateOfExitwithdateform = null;
					Date dateEnd = null;
					Date dateStart = null;
					Date dateOfJoin = null;
					Date rcd = null;

					String dateOfExit = temp.getDateOfExit().toString();
					String str_StartDate = policyHistoryEntities.getPolicyStartDate().toString();
					String str_EndDate = policyHistoryEntities.getPolicyEndDate().toString();
					String str_rcd = masterPolicyEntity.getRiskCommencementDate().toString();
					String str_dateOfJoin = policyMemberEntity.getDojToScheme().toString();
					DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
					ZonedDateTime zdt = ZonedDateTime.parse(dateOfExit, f);

					LocalDate ldoe = zdt.toLocalDate();
					String str_AnnualRenewlDate = policyHistoryEntities.getAnnualRenewlDate().toString();
					String[] str_SplitARDDate = str_AnnualRenewlDate.split(" ");
					String[] str_SplitStartDate = str_StartDate.split(" ");
					String[] str_SplitEndDate = str_EndDate.split(" ");
					String[] str_SplitRcd = str_rcd.split(" ");
					String[] str_SplitDateOfJoin = str_dateOfJoin.split(" ");
					try {
						dateOfExitwithdateform = sdf.parse(ldoe.toString());
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

							if (temp.getModeOfExit() == 193) {

								if (dateOfExitwithdateform.compareTo(rcd) >= 0
										&& dateOfExitwithdateform.compareTo(dateEnd) <= 0) {
								} else {
									return ApiResponseDto.errorMessage(null, null,
											"Date Of Exit is not Between Risk Commencement Date & Policy End Date");

								}

							}
							RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
									.copytoHistoryForClaim(policyHistoryEntities);
							renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

							PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();

							policyServiceEntitiy.setServiceType("Claim");
							policyServiceEntitiy.setPmstHisPolicyId(policyHistoryEntities.getId());
							policyServiceEntitiy.setCreatedBy(temp.getCreatedBy());
							policyServiceEntitiy.setCreatedDate(new Date());
							policyServiceEntitiy.setIsActive(true);
							policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

							renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
							renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
							HistoryMPHEntity historyMPHEntity = historyMPHRepository
									.findById(policyHistoryEntities.getMasterpolicyHolder()).get();
							TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper
									.copytoHistoTemp(renewalPolicyTMPEntity.getId(), historyMPHEntity);
							tempMPHRepository.save(tempMPHEntity);

							renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
							renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
							Optional<PolicySchemeRuleHistoryEntity> PolicySchemeRuleHistoryEntity = policySchemeRuleHistoryRepository
									.findBypolicyId(policyHistoryEntities.getId());
							RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
									.copyToHistorySchemeforClaim(PolicySchemeRuleHistoryEntity,
											renewalPolicyTMPEntity.getId());
							renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

							List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
									.findBypmstHisPolicyId(policyHistoryEntities.getId());
							List<MemberCategoryModuleEntity> addMemberCategoryModuleEntity =new ArrayList<MemberCategoryModuleEntity>();
							for(MemberCategoryEntity getmemberCategoryEntities : memberCategoryEntity) {
								MemberCategoryModuleEntity memberCategoryModuleEntity=new MemberCategoryModuleEntity();
								memberCategoryModuleEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
								memberCategoryModuleEntity.setMemberCategoryId(getmemberCategoryEntities.getId());
//								memberCategoryModuleEntity.setPolicyId(getmemberCategoryEntities.getPmstPolicyId());
								memberCategoryModuleEntity.setCreatedBy(getmemberCategoryEntities.getCreatedBy());
								memberCategoryModuleEntity.setCreatedDate(new Date());
								memberCategoryModuleEntity.setIsActive(true);
								addMemberCategoryModuleEntity.add(memberCategoryModuleEntity);
							}
							memberCategoryModuleRepository.saveAll(addMemberCategoryModuleEntity);

//							HistoryMemberEntity historyMemberEntity = historyMemberRepository.findBypolicyIdandpmstMemberId(
//									policyHistoryEntities.getId(), tempPolicyClaimPropsDto.getPmstMemberId());
							RenewalPolicyTMPMemberEntity renewalMemberEntity = PolicyClaimCommonHelper
									.copyToHistoryMemberforClaim(historyMemberEntity, memberCategoryEntity,
											memberCategoryEntity, renewalPolicyTMPEntity.getId());
							renewalPolicyTMPMemberRepository.save(renewalMemberEntity);

							List<HistoryLifeCoverEntity> historyLifeCoverEntity = historyLifeCoverRepository
									.findBypolicyId(policyHistoryEntities.getId());
							List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
									.copyToHistoryTmpLifeCoverforClaimbulk(historyLifeCoverEntity,
											memberCategoryEntity, renewalPolicyTMPEntity.getId());
							renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);

							List<HistoryGratuityBenefitEntity> historyGratuityBenefitEntity = historyGratutiyBenefitRepository
									.findBypolicyId(policyHistoryEntities.getId());
							List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
									.copyToHistoryGratuityforClaimBulk(historyGratuityBenefitEntity,
											memberCategoryEntity, renewalPolicyTMPEntity.getId());
							renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);

							Optional<PolicyValuationHistoryEntity> policyValuationHistoryEntity = policyValuationHistoryRepository
									.findBypolicyId(policyHistoryEntities.getId());
							RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
									.copyToHistoryValuationforClaim(policyValuationHistoryEntity,
											renewalPolicyTMPEntity.getId());
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

							getTempPolicyClaimPropsEntity = PolicyClaimCommonHelper.copytotmpforclaim(
									renewalMemberEntity.getId(), renewalPolicyTMPEntity.getId(), temp);
							getTempPolicyClaimPropsEntity.setOnboardNumber(
									tempPolicyClaimPropsRepository.getSequence(HttpConstants.ONBORDING_MODULE));
							getTempPolicyClaimPropsEntity.setClaimStatusId(onboardedId);
							getTempPolicyClaimPropsEntity.setOnboardingDate(new Date());
							tempPolicyClaimPropsRepository.save(getTempPolicyClaimPropsEntity);

							// return
							// ApiResponseDto.created(PolicyClaimHelper.claimpropstoDto(getTempPolicyClaimPropsEntity));

						} else {
							return ApiResponseDto.errorMessage(null, null,
									"Date Of Exit is not Between Date of Joining to Scheme & Policy End Date");
						}

					}

					else {
						return ApiResponseDto.errorMessage(null,
								"Date Of Exit is not Between Policy Start Date & Policy End Date",
								"Date Of Exit should be between Policy Start Date & Policy End Date");

					}

				}
			
				
			} else {
				

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

				Date dateOfExitwithdateform = null;
				Date dateEnd = null;
				Date dateStart = null;
				Date dateOfJoin = null;
				Date rcd = null;

				String dateOfExit = temp.getDateOfExit().toString();
				String str_StartDate = masterPolicyEntity.getPolicyStartDate().toString();
				String str_EndDate = masterPolicyEntity.getPolicyEndDate().toString();
				String str_rcd = masterPolicyEntity.getRiskCommencementDate().toString();
				String str_dateOfJoin = policyMemberEntity.getDojToScheme().toString();
				System.out.println(str_StartDate);
				System.out.println(str_EndDate);
				System.out.println(str_rcd);
				System.out.println(str_dateOfJoin);

	
				String[] str_SplitDateOfExit = dateOfExit.split(" ");
//				DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
//				ZonedDateTime zdt = ZonedDateTime.parse(dateOfExit, f);
//
//				LocalDate ld = zdt.toLocalDate();
				String str_AnnualRenewlDate = masterPolicyEntity.getAnnualRenewlDate().toString();
				String[] str_SplitARDDate = str_AnnualRenewlDate.split(" ");
				String[] str_SplitStartDate = str_StartDate.split(" ");
				String[] str_SplitEndDate = str_EndDate.split(" ");
				String[] str_SplitRcd = str_rcd.split(" ");
				String[] str_SplitDateOfJoin = str_dateOfJoin.split(" ");
				try {
					dateOfExitwithdateform = sdf.parse(str_SplitDateOfExit[0]);
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

						if (temp.getModeOfExit() == 193) {

							if (dateOfExitwithdateform.compareTo(rcd) >= 0
									&& dateOfExitwithdateform.compareTo(dateEnd) <= 0) {
							} else {
								return ApiResponseDto.errorMessage(null,
										"Date Of Exit is not between Risk Commencement Date & Policy End Date",
										"Date Of Exit should be between Risk Commencement Date & Policy End Date");

							}

						}
						RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
								.copytoTmpForClaim(masterPolicyEntity);
						renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
						PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();

						policyServiceEntitiy.setServiceType("Claim");
						policyServiceEntitiy.setPolicyId(masterPolicyEntity.getId());
						policyServiceEntitiy.setCreatedBy(temp.getCreatedBy());
						policyServiceEntitiy.setCreatedDate(new Date());
						policyServiceEntitiy.setIsActive(true);
						policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

						renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());

						renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

						MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
						TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper
								.copytomastertoTemp(renewalPolicyTMPEntity.getId(), mPHEntity);
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
						List<MemberCategoryModuleEntity> addMemberCategoryModuleEntity =new ArrayList<MemberCategoryModuleEntity>();
						for(MemberCategoryEntity getmemberCategoryEntities : memberCategoryEntity) {
							MemberCategoryModuleEntity memberCategoryModuleEntity=new MemberCategoryModuleEntity();
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
								.copyToTmpLifeCoverforClaimbulk(policyLifeCoverEntity,
										memberCategoryEntity, renewalPolicyTMPEntity.getId());

						renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
						

						List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
								.findBypolicyId(masterPolicyEntity.getId());

						List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
								.copyToTmpGratuityforClaimBulk(policyGratuityBenefitEntity,
										memberCategoryEntity, renewalPolicyTMPEntity.getId());

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
								.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity,
										renewalPolicyTMPEntity.getId());
						renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

						Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
								.findByPolicyId(masterPolicyEntity.getId());
						RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
								.copyToTmpValuationBasicClaim(policyValutationBasicEntity, renewalPolicyTMPEntity.getId());
						renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

						List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
								.findByPolicyId(masterPolicyEntity.getId());
						List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
								.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity,
										renewalPolicyTMPEntity.getId());
						renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);

						RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
								.copyToTmpIndividualMemberClaimBulk(policyMemberEntity,
										memberCategoryEntity, renewalPolicyTMPEntity.getId());
						renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);

						getTempPolicyClaimPropsEntity = PolicyClaimCommonHelper.copytotmpforclaim(
								renewalPolicyTMPMemberEntity.getId(), renewalPolicyTMPEntity.getId(),
								temp);
						getTempPolicyClaimPropsEntity.setOnboardNumber(
								tempPolicyClaimPropsRepository.getSequence(HttpConstants.ONBORDING_MODULE));
						getTempPolicyClaimPropsEntity.setClaimStatusId(onboardedId);
						getTempPolicyClaimPropsEntity.setClaimType("BulkUpload");
						getTempPolicyClaimPropsEntity.setOnboardingDate(new Date());
						tempPolicyClaimPropsRepository.save(getTempPolicyClaimPropsEntity);
						//

						// return
						// ApiResponseDto.created(PolicyClaimHelper.claimpropstoDto(tempPolicyClaimPropsEntity));

					} else {
						return ApiResponseDto.errorMessage(null,
								"Date Of Exit is not Between Doj to scheme & Policy End Date",
								"Date Of Exit should be between DOJ to Scheme & Policy End Date");

					}

				} else {
					return ApiResponseDto.errorMessage(null,
							"Date Of Exit is not Between Policy Start Date & Policy End Date",
							"Date Of Exit should be between Policy Start Date & Policy End Date");

				}
				
			}
		}

		List<TempPolicyClaimPropsDto> getListClaimPropsDto = new ArrayList<TempPolicyClaimPropsDto>();
		for (TempPolicyClaimPropsEntity singleTempPolicyClaimPropsEntity : tempPolicyClaimPropsRepository
				.findBybatchId(batchId)) {
			TempPolicyClaimPropsDto get = PolicyClaimHelper.entityToDto(singleTempPolicyClaimPropsEntity);
			List<Object[]> getTempPolicySearchList = tempPolicyClaimPropsRepository
					.findByOnboardNumber(get.getOnboardNumber());
			if (getTempPolicySearchList.isEmpty()) {
				get.setClaimPropSearchDetailDto(null);
			} else {
				get.setClaimPropSearchDetailDto(PolicyClaimHelper.getObjectToDto(getTempPolicySearchList));
			}
			getListClaimPropsDto.add(get);
		}

		return ApiResponseDto.created(getListClaimPropsDto);
	}
	
	private List<MemberBulkStgEntity> getQuotationMembers(Long batchId) {
		return jdbcTemplate.query(
				"SELECT * FROM QSTG_MEMBER_BULK_STG WHERE RECORD_STATUS='G' AND IS_ACTIVE=1 AND MEMBER_BATCH_ID=?",
				BeanPropertyRowMapper.newInstance(MemberBulkStgEntity.class), batchId);
	}

	@Override
	public ApiResponseDto<ApiValidationResponse> ValidationforOnboardingCondition(
			TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {
//		MasterPolicyEntity masterPolicyEntity = masterPolicyRepository
//				.findById(tempPolicyClaimPropsDto.getPmstPolicyId()).get();
//		PolicyMemberEntity policyMemberEntity = policyMemberRepository.findByPolicyIdandMemberId(
//				tempPolicyClaimPropsDto.getPmstPolicyId(), tempPolicyClaimPropsDto.getPmstMemberId());
//
//		PolicyHistoryEntity policyHistoryEntities = policyHistoryRepository.findBymasterPolicyId(
//				tempPolicyClaimPropsDto.getPmstPolicyId(), tempPolicyClaimPropsDto.getDateOfExit());
//		ApiValidationResponse newApiValidationResponse = new ApiValidationResponse();
//
//		if (policyHistoryEntities != null) {
//
//			HistoryMemberEntity historyMemberEntity = historyMemberRepository.findBypolicyIdandpmstMemberId(
//					policyHistoryEntities.getId(), tempPolicyClaimPropsDto.getPmstMemberId());
//
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
//			Date dateOfExitwithdateform = null;
//			Date dateARD = null;
//			Date dateEnd = null;
//			Date dateStart = null;
//			Date dateOfJoin = null;
//			Date rcd = null;
//
//			String dateOfExit = tempPolicyClaimPropsDto.getDateOfExit().toString();
//			String str_StartDate = policyHistoryEntities.getPolicyStartDate().toString();
//			String str_EndDate = policyHistoryEntities.getPolicyEndDate().toString();
//			String str_rcd = policyHistoryEntities.getRiskCommencementDate().toString();
//			String str_dateOfJoin = historyMemberEntity.getDojToScheme().toString();
//
//			DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
//			ZonedDateTime zdt = ZonedDateTime.parse(dateOfExit, f);
//
//			LocalDate ldoe = zdt.toLocalDate();
//			String str_AnnualRenewlDate = policyHistoryEntities.getAnnualRenewlDate().toString();
//			String[] str_SplitARDDate = str_AnnualRenewlDate.split(" ");
//			String[] str_SplitStartDate = str_StartDate.split(" ");
//			String[] str_SplitEndDate = str_EndDate.split(" ");
//			String[] str_SplitRcd = str_rcd.split(" ");
//			String[] str_SplitDateOfJoin = str_dateOfJoin.split(" ");
//
//			try {
//				dateOfExitwithdateform = sdf.parse(ldoe.toString());
//				dateARD = sdf.parse(str_SplitARDDate[0]);
//				dateStart = sdf.parse(str_SplitStartDate[0]);
//				dateEnd = sdf.parse(str_SplitEndDate[0]);
//				dateOfJoin = sdf.parse(str_SplitDateOfJoin[0]);
//				rcd = sdf.parse(str_SplitRcd[0]);
//
//			} catch (Exception e) {
//			}
//
//			if (dateOfExitwithdateform.compareTo(dateStart) > 0 && dateOfExitwithdateform.compareTo(dateEnd) < 0) {
//
//				if (dateOfExitwithdateform.compareTo(dateOfJoin) > 0 && dateOfExitwithdateform.compareTo(dateEnd) < 0) {
//					if (tempPolicyClaimPropsDto.getModeOfExit() == 193) {
//						// Api response for validation(success error messages)
//						if (dateOfExitwithdateform.compareTo(rcd) > 0
//								&& dateOfExitwithdateform.compareTo(dateEnd) < 0) {
//
//						} else {
//							newApiValidationResponse.setStatus("rcd and policy end date not satisfied");
//							newApiValidationResponse.setMessage("risk managemnet and Policy end date");
//
//						}
//					} else {
//						newApiValidationResponse.setStatus("mode of exist not equal to 193");
//						newApiValidationResponse.setMessage("risk managemnet and Policy end date");
//
//						// return ApiResponseDto.errorMessage(null,"","message");
//					}
//				} else {
//					newApiValidationResponse.setStatus("date of join and policy end date not satisfied");
//					newApiValidationResponse.setMessage("risk managemnet and Policy end date");
//
//					// return ApiResponseDto.errorMessage(null,"","message");
//				}
//			} else {
//				newApiValidationResponse.setStatus("start date and end date not satisfied");
//				newApiValidationResponse.setMessage("risk managemnet and Policy end date");
//			}
//
//			// return ApiResponseDto.errorMessage(null,"","message");
//		}
//
//		else {
//
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////			Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
//			Date dateOfExitwithdateform = null;
//			Date dateARD = null;
//			Date dateEnd = null;
//			Date dateStart = null;
//			Date dateOfJoin = null;
//			Date rcd = null;
//
//			String dateOfExit = tempPolicyClaimPropsDto.getDateOfExit().toString();
//			String str_StartDate = masterPolicyEntity.getPolicyStartDate().toString();
//			String str_EndDate = masterPolicyEntity.getPolicyEndDate().toString();
//			String str_rcd = masterPolicyEntity.getRiskCommencementDate().toString();
//			String str_dateOfJoin = policyMemberEntity.getDojToScheme().toString();
//
////
////			String[] str_SplitDateOfExit = dateOfExit.split(" ");
//			DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
//			ZonedDateTime zdt = ZonedDateTime.parse(dateOfExit, f);
//
//			LocalDate ld = zdt.toLocalDate();
//			String str_AnnualRenewlDate = masterPolicyEntity.getAnnualRenewlDate().toString();
//			String[] str_SplitARDDate = str_AnnualRenewlDate.split(" ");
//			String[] str_SplitStartDate = str_StartDate.split(" ");
//			String[] str_SplitEndDate = str_EndDate.split(" ");
//			String[] str_SplitRcd = str_rcd.split(" ");
//			String[] str_SplitDateOfJoin = str_dateOfJoin.split(" ");
//			try {
//				dateOfExitwithdateform = sdf.parse(ld.toString());
//				dateARD = sdf.parse(str_SplitARDDate[0]);
//				dateStart = sdf.parse(str_SplitStartDate[0]);
//				dateEnd = sdf.parse(str_SplitEndDate[0]);
//				dateOfJoin = sdf.parse(str_SplitDateOfJoin[0]);
//				rcd = sdf.parse(str_SplitRcd[0]);
//
//			} catch (Exception e) {
//			}
//
//			if (dateOfExitwithdateform.compareTo(dateStart) > 0 && dateOfExitwithdateform.compareTo(dateEnd) < 0) {
//
//				if (dateOfExitwithdateform.compareTo(dateOfJoin) > 0 && dateOfExitwithdateform.compareTo(dateEnd) < 0) {
//					if (tempPolicyClaimPropsDto.getModeOfExit() != null) {
//						if (tempPolicyClaimPropsDto.getModeOfExit() == 193) {
//							// Api response for validation(success error messages)
//							if (dateOfExitwithdateform.compareTo(rcd) > 0
//									&& dateOfExitwithdateform.compareTo(dateEnd) < 0) {
//
//							} else {
//								newApiValidationResponse.setStatus("rcd and policy end date not satisfied");
//								newApiValidationResponse.setMessage("risk managemnet and Policy end date");
//
//							}
//						}
//					}
//
//					// return ApiResponseDto.errorMessage(null,"","message");
//				} else {
//					newApiValidationResponse.setStatus("date of join and policy end date not satisfied");
//					newApiValidationResponse.setMessage("risk managemnet and Policy end date");
//
//					// return ApiResponseDto.errorMessage(null,"","message");
//				}
//			} else {
//				newApiValidationResponse.setStatus("start date and end date not satisfied");
//				newApiValidationResponse.setMessage("risk managemnet and Policy end date");
//
//				// return ApiResponseDto.errorMessage(null,"","message");
//			}
//		}
//
		return ApiResponseDto.errorMessage(null, "risk managemnet and Policy end date not satisfied", "message");

	}

	@Override
	public ApiResponseDto<?> ValidateForOnboarding(Long memberid, Long policyid) {
		TempPolicyClaimPropsEntity entity = tempPolicyClaimPropsRepository.getByPolicyAndMemberId(memberid, policyid);
		if (entity != null) {
			return ApiResponseDto.success("Already Exist");
		} else {
			return null;
		}
	}

	@Override
	public ApiResponseDto<?> validationforclaimOnboarding(Long memberid, Long policyid) {
		int entity = tempPolicyClaimPropsRepository.findByMemberIdandPolicyId(memberid, policyid);
		if (entity > 0) {
			return ApiResponseDto.errorMessage(null, null, "already existed");
		} else {
//			Date dateAppointment = policyMemberRepository.getByPolicyIdAndLicId(memberid, policyid);
//			LocalDate appointment = dateAppointment.toInstant().atZone(ZonedDateTime.now().getZone()).toLocalDate();
//			LocalDate currentDate = LocalDate.now();
//			int year = currentDate.getYear();
//			Month month = currentDate.getMonth();
//			int day = currentDate.getDayOfMonth();
//
//			LocalDate specificDate = LocalDate.of(year, month, day);
//			int yearsDifference = (int) ChronoUnit.YEARS.between(appointment, specificDate);
//			int monthsDifference = (int) ChronoUnit.MONTHS.between(appointment, specificDate);
//			int daysDifference = (int) ChronoUnit.DAYS.between(appointment, specificDate);
//
//			PolicySchemeEntity minimumDifference = policySchemeRuleRepository.findByPolicyId(policyid);
//			if (yearsDifference >= minimumDifference.getMinimumGratuityServiceYear()
//					&& monthsDifference >= minimumDifference.getMinimumGratuityServiceMonth()
//					&& daysDifference >= minimumDifference.getMinimumGratuityServiceDay()) {
//				return ApiResponseDto.success(null);
//			}
//			return ApiResponseDto.errorMsg(null, null, "Based on Scheme rule You are not eligible for claims");
			return ApiResponseDto.success(null);
		}
	}

	@Transactional
	@Override
	public ApiResponseDto<RenewalPolicyTMPMemberDto> deleteBeneficiary(Long propsId, Long memberBankId, Long nomineeId,
			Long appointeeId) {
		if (memberBankId != 0) {
			tempMemberBankAccountRepository.deleteByMemberId(memberBankId);
			tempPolicyClaimBeneficiaryRepository.deleteByMemberBankId(propsId, memberBankId);
		} else if (nomineeId != 0) {
			tempMemberRepository.deleteByNominee(nomineeId);
			tempMemberAppointeeRepository.deleteByNomiee(nomineeId);
			tempPolicyClaimBeneficiaryRepository.deleteByNomineeId(propsId, nomineeId);

		} else if (appointeeId != 0) {
			tempPolicyClaimBeneficiaryRepository.deleteByAppointeeId(propsId, appointeeId);
			tempMemberAppointeeRepository.deleteByAppointee(appointeeId);
		}
		Long memberId = tempPolicyClaimPropsRepository.findByClaimId(propsId);
		return ApiResponseDto.success(
				RenewalPolicyTMPMemberHelper.entityToDto(renewalPolicyTMPMemberRepository.findById(memberId).get()));
	}

	@Override
	public ApiResponseDto<TempPolicyClaimPropsDto> getIndividualIntimationNo(String intimationNumber) {
		TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity = tempPolicyClaimPropsRepository
				.findByinitimationNumber(intimationNumber);

		return ApiResponseDto.created(PolicyClaimHelper.claimpropstoDto(tempPolicyClaimPropsEntity));
	}

	@Override
	public ApiResponseDto<CalculateResDto> GetFundSize(Long policyid) {
		CalculationReqDto calculationReqDto = new CalculationReqDto();
		calculationReqDto.setPolicyId(policyid);
		calculationReqDto.setTransactionDate(new Date());
		calculationReqDto.setBatch(false);
		calculationReqDto.setAuto(true);
		calculationReqDto.setRecalculate(true);

		return ApiResponseDto.success(fundService.calculate(calculationReqDto));
	}

	@Override
	public ApiResponseDto<List<TempPolicyClaimPropsDto>> claimFilterSearch(
			TempPolicyClaimPropsSearchDto policyClaimPropsSearchDto) {

		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PolicyTempSearchEntity> createQuery = criteriaBuilder.createQuery(PolicyTempSearchEntity.class);

		Root<PolicyTempSearchEntity> root = createQuery.from(PolicyTempSearchEntity.class);

		Join<PolicyTempSearchEntity, TempPolicyClaimPropsSearchEntity> joinTmpPolicytoProps = root
				.join("tmpPolicySearch");
		Join<PolicyTempSearchEntity, TempMphSearchEntity> joinTmpPolicytoTmpMPH = root.join("policyMPHTmp");

		Join<PolicyTempSearchEntity, TempMemberSearchEntity> joinTmpPolicyIdtoTmpMember = root.join("policySearch");

		// policyTMP start
		if (policyClaimPropsSearchDto.getPolicyNumber() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getPolicyNumber())) {
			predicates
					.add(criteriaBuilder.equal(root.get("policyNumber"), policyClaimPropsSearchDto.getPolicyNumber().trim()));
		}
		// End

		// MPHTMP Start
		if (policyClaimPropsSearchDto.getMphName() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getMphName())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicytoTmpMPH.get("mphName"))),
					policyClaimPropsSearchDto.getMphName().toLowerCase()));
		}
		if (policyClaimPropsSearchDto.getMphPan() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getMphPan())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicytoTmpMPH.get("pan"))),
					policyClaimPropsSearchDto.getMphPan().toLowerCase()));
		}

		// ENd

		// MemberTMP Data start
		if (policyClaimPropsSearchDto.getPanNumber() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getPanNumber())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicyIdtoTmpMember.get("panNumber"))),
					policyClaimPropsSearchDto.getPanNumber().toLowerCase()));
		}

		if (policyClaimPropsSearchDto.getEmployeeName() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getEmployeeName())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicyIdtoTmpMember.get("firstName"))),
					policyClaimPropsSearchDto.getEmployeeName().toLowerCase()));
		}

		if (policyClaimPropsSearchDto.getEmployeeCode() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getEmployeeCode())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicyIdtoTmpMember.get("employeeCode"))),
					policyClaimPropsSearchDto.getEmployeeCode().toLowerCase()));
		}

		if (policyClaimPropsSearchDto.getAadharNumber() != null
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getAadharNumber())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicyIdtoTmpMember.get("aadharNumber"))),
					policyClaimPropsSearchDto.getAadharNumber().toLowerCase()));
		}

		// END

		// Claim props Start
		if (StringUtils.isNotBlank(policyClaimPropsSearchDto.getOnboardNumber())
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getOnboardNumber())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicytoProps.get("onboardNumber"))),
					policyClaimPropsSearchDto.getOnboardNumber().toLowerCase().trim()));
		}
		if (StringUtils.isNotBlank(policyClaimPropsSearchDto.getPayoutNo())
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getPayoutNo())) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicytoProps.get("payoutNo"))),
					policyClaimPropsSearchDto.getPayoutNo().toLowerCase().trim()));
		}

		if ((policyClaimPropsSearchDto.getModeOfExit() != null)) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(criteriaBuilder.lower(joinTmpPolicytoProps.get("modeOfExit"))),
					policyClaimPropsSearchDto.getModeOfExit()));
		}
		if (StringUtils.isNotBlank(policyClaimPropsSearchDto.getInitimationNumber())
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getInitimationNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(joinTmpPolicytoProps.get("initimationNumber")),
					policyClaimPropsSearchDto.getInitimationNumber().toLowerCase().trim()));
		}
		if ((policyClaimPropsSearchDto.getClaimStatusId() != null)
				&& StringUtils.isNotBlank(policyClaimPropsSearchDto.getClaimStatusId().toString())) {
			predicates.add(joinTmpPolicytoProps.get("claimStatusId").in(policyClaimPropsSearchDto.getClaimStatusId()));
		}

		predicates.add(criteriaBuilder.equal(joinTmpPolicytoProps.get("isActive"), Boolean.TRUE));

		// Props end

		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<PolicyTempSearchEntity> entities = entityManager.createQuery(createQuery).getResultList();
		List<TempPolicyClaimPropsDto> getListClaimPropsDto = new ArrayList<TempPolicyClaimPropsDto>();
		List<ClaimPropSearchDetailDto> getClaimPropSearchDetailDto = new ArrayList<ClaimPropSearchDetailDto>();
		System.out.println(entities.size());
		TempPolicyClaimPropsDto tempPolicyClaimPropsDto = new TempPolicyClaimPropsDto();

		if (entities.stream().map(PolicyClaimHelper::entityToDtoFilter).collect(Collectors.toList()).isEmpty()) {
			return ApiResponseDto
					.success(entities.stream().map(PolicyClaimHelper::entityToDtoFilter).collect(Collectors.toList()));
		}

		for (PolicyTempSearchEntity singleTempPolicyClaimPropsEntity : entities) {

			System.out.println(singleTempPolicyClaimPropsEntity.getPolicyMPHTmp().getMphCode());
			System.out.println(singleTempPolicyClaimPropsEntity.getPolicySearch().size());
			System.out.println(singleTempPolicyClaimPropsEntity.getTmpPolicySearch().size());
			for (TempMemberSearchEntity getMember : singleTempPolicyClaimPropsEntity.getPolicySearch()) {
				for (TempPolicyClaimPropsSearchEntity getProps : singleTempPolicyClaimPropsEntity
						.getTmpPolicySearch()) {

					ClaimPropSearchDetailDto claimPropSearchDetailDto = new ClaimPropSearchDetailDto();
					claimPropSearchDetailDto.setOnboardNumber(getProps.getOnboardNumber());
					claimPropSearchDetailDto.setPayoutNo(getProps.getPayoutNo());
					claimPropSearchDetailDto.setInitimationNumber(getProps.getInitimationNumber());
					claimPropSearchDetailDto.setPolicyNumber(singleTempPolicyClaimPropsEntity.getPolicyNumber());
					claimPropSearchDetailDto
							.setMphCode(singleTempPolicyClaimPropsEntity.getPolicyMPHTmp().getMphCode());
					claimPropSearchDetailDto.setLicId(getMember.getLicId());
					claimPropSearchDetailDto.setEmployeeCode(getMember.getEmployeeCode());
					claimPropSearchDetailDto.setClaimType(getProps.getClaimType());
					claimPropSearchDetailDto.setModeOfExitName(
							tempPolicyClaimPropsRepository.getModeofExitName(getProps.getModeOfExit()));
					claimPropSearchDetailDto.setClaimStatusName(
							tempPolicyClaimPropsRepository.getClaimStatusName(getProps.getClaimStatusId()));
					getClaimPropSearchDetailDto.add(claimPropSearchDetailDto);

				}

			}
			tempPolicyClaimPropsDto.setClaimPropSearchDetailDto(getClaimPropSearchDetailDto);

		}
		getListClaimPropsDto.add(tempPolicyClaimPropsDto);
		// end
//		return null;
		return ApiResponseDto.success(getListClaimPropsDto);
	}





	@Override
	public Long getClaimSubStatus(Long claimStatusId) {
		
		Long count=tempPolicyClaimPropsRepository.getCountClaimSubStatus(claimStatusId);
		return count;
	}
	
	
	
	
}