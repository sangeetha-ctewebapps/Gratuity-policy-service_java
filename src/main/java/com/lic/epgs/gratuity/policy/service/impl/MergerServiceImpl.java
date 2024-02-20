package com.lic.epgs.gratuity.policy.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.UnlockDepositDetailDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.common.helper.CommonHelper;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
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
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.repository.HistoryMPHRepository;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.dto.GratuityCalculationsDto;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.dto.MasterPolicyMergerDto;
import com.lic.epgs.gratuity.policy.dto.MergerNewSearchFilterDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpMergerPropsDto;
import com.lic.epgs.gratuity.policy.dto.PolicyAdjustmentPDFDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailMergerDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositLockDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpMergerPropsEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.entity.QuotationChargeEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.helper.PolicyGratuityBenefitHelper;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.HistoryGratutiyBenefitRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.helper.CommonPolicyServiceHelper;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
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
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContrySummaryEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContrySummaryRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyDepositRepository;
import com.lic.epgs.gratuity.policy.renewal.repository.PolicyRenewalRemainderRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.helper.RenewalGratuityBenefitTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.helper.RenewalPolicyTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.repository.RenewalLifeCoverTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.helper.RenewalPolicyTMPMemberHelper;
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
import com.lic.epgs.gratuity.policy.repository.MasterPolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PmstTmpMergerPropsRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyAdjustmentDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyHistoryRepository;
import com.lic.epgs.gratuity.policy.repository.QuotationChargeRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.helper.PolicySchemeRuleHelper;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleHistoryRepository;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.service.MergerService;
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
import com.lic.epgs.gratuity.policyservices.common.dto.LeavingMemberDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PremiumGstRefundDto;
import com.lic.epgs.gratuity.policyservices.common.enums.PolicyServiceName;
import com.lic.epgs.gratuity.policyservices.common.service.PolicyServicingCommonService;
import com.lic.epgs.gratuity.policyservices.merger.dto.BeneficiaryPaymentDetailModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.DebitCreditMergePolicyModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.JournalVoucherDetailModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.MergePolicyRequestModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.RegularGstDetailModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.ReversalGstDetailModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.TrnRegisModel;
import com.lic.epgs.gratuity.quotation.dto.AdjustmentVoucherDetailDto;
import com.lic.epgs.gratuity.quotation.member.repository.MemberRepository;
import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;
import com.lic.epgs.gratuity.simulation.dto.DepositAdjustementDto;
import com.lowagie.text.DocumentException;

@Service
public class MergerServiceImpl implements MergerService {
	
	
	@Value("${app.policy.approvedStatudId}")
	private Long approvedStatudId;

	@Value("${app.policy.approvedSubStatudId}")
	private Long approvedSubStatudId;

	@Value("${app.policy.existingTaggedStatusId}")
	private Long existingTaggedStatusId;
	
	@Value("${app.policy.claim.ModeOfExitDeath}")
	private String modeOfExitDeath;
	
	@Value("${app.isDevEnvironmentForRenewals}")
	private Boolean isDevEnvironmentForRenewals;

	@Value("${app.policy.defaultStatusId}")
	private String defaultStatusId;

	@Value("${app.policy.defaultSubStatusId}")
	private String defaultSubStatusId;

	@Value("${app.policy.defaultTaggedStatusId}")
	private String defaultTaggedStatusId;
	
	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	@Autowired
	private PolicyServiceRepository policyServiceRepository;
	
	@Autowired
	private MasterPolicyRepository masterPolicyRepository;
	
	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;

	@Autowired
	private StandardCodeRepository standardCodeRepository;
	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

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
	private MemberRepository memberRepository;

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
	private PolicyMasterValuationRepository policyMasterValuationRepository;

	@Autowired
	private RenewalPolicyTMPMemberRepository renewalPolicyTMPMemberRepository;

	@Autowired
	private HistoryLifeCoverRepository historyLifeCoverRepository;

	@Autowired
	private HistoryGratutiyBenefitRepository historyGratutiyBenefitRepository;

	@Autowired
	private HistoryMemberRepository historyMemberRepository;

	@Autowired
	private PolicyMemberRepository policyMemberRepository;

	@Autowired
	private PolicyHistoryRepository policyHistoryRepository;

	@Autowired
	private MPHRepository mPHRepository;

	@Autowired
	private TempMPHRepository tempMPHRepository;
	@Autowired
	private QuotationChargeRepository quotationChargeRepository;
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
	private GratuityCalculationRepository gratuityCalculationRepository;
	
	@Autowired
	private PmstTmpMergerPropsRepository pmstTmpMergerPropsRepository;
	
	@Autowired
	private HistoryMPHRepository historyMPHRepository;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private TaskAllocationRepository taskAllocationRepository;

	@Autowired
	private CommonModuleService commonModuleService;

	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private CommonMasterStateRepository commonMasterStateRepository;

	@Autowired
	private PolicyContributionDetailRepository policyContributionDetailRepository;

	@Autowired
	private AccountingService accountingService;

	@Autowired
	private PolicyDepositRepository policyDepositRepository;

	@Autowired
	private PolicyRenewalRemainderRepository policyRenewalRemainderRepository;

	@Autowired
	private PolicyContributionRepository policyContributionRepository;

	@Autowired
	private PolicyAdjustmentDetailRepository policyAdjustmentDetailRepository;

	@Autowired
	private MasterPolicyContributionDetailRepository masterPolicyContributionDetailRepository;
	
	@Autowired
	private PolicyContrySummaryRepository policyContrySummaryRepository;
	
	@Autowired
	private TaskProcessRepository taskProcessRepository;
	
	@Autowired
	private PolicyServicingCommonService policyServicingCommonService;
	

	private Long standardcode = 10l;
	
	ModelMapper modelMapper=new ModelMapper();
	
	protected final Logger logger = LogManager.getLogger(getClass());
	
	@Override
	public ApiResponseDto<List<PolicyDto>> GetExitingPolicyNoinMaster(MergerNewSearchFilterDto mergerNewSearchFilterDto) {
		if(!mergerNewSearchFilterDto.getUnitCode().equals("null")) {
		List<MasterPolicyEntity> masterPolicyEntity = masterPolicyCustomRepository.findByPolicyNumberwithUnitcode(mergerNewSearchFilterDto.getPolicyNumber(),mergerNewSearchFilterDto.getUnitCode());

		return ApiResponseDto
				.created(masterPolicyEntity.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));
		}else {
			List<MasterPolicyEntity> masterPolicyEntity = masterPolicyCustomRepository.findBypolicyNumberandActive(mergerNewSearchFilterDto.getPolicyNumber());

			return ApiResponseDto
					.created(masterPolicyEntity.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));
		}
	}
	
	@Override
	public ApiResponseDto<PolicyDto> SearchFilterListPolicy(String policyNumber, String servicetype) {

		PolicyDto policyDto = new PolicyDto();
		try {
//		List<MasterPolicyEntity> masterPolicyEntity = masterPolicyRepository.findPolicyDetailSeerch(policyNumber);

			MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findBypolicyNumber(policyNumber);
			if (servicetype.equals("merger")) {
//		for (MasterPolicyEntity getMasterPolicyEntity : masterPolicyEntity) {

				if (masterPolicyEntity.getPolicyStatusId() == 123 || masterPolicyEntity.getPolicyStatusId() == 127) {
					policyDto = new ModelMapper().map(masterPolicyEntity, PolicyDto.class);
				}
//		}
			}
			if (servicetype.equals("conversion")) {
				if (masterPolicyEntity != null) {
					String getProductName = masterPolicyEntity.getProductVariant()
							.substring(masterPolicyEntity.getProductVariant().length() - 2);
					System.out.println("get prod" + getProductName);

					if (getProductName.equals("V1") || getProductName.equals("V2")) {
						if (masterPolicyEntity.getPolicyStatusId() == 127
								|| masterPolicyEntity.getPolicyStatusId() == 123) {

							policyDto = new ModelMapper().map(masterPolicyEntity, PolicyDto.class);
						}
					}
				}
			}
			if (servicetype.equals("policytransfer") ){
				if (masterPolicyEntity != null) {
					if (masterPolicyEntity.getPolicyStatusId() == 127) {
						policyDto = new ModelMapper().map(masterPolicyEntity, PolicyDto.class);
					}
				}
			}
			if (servicetype.equals("transferinout")) {
				if (masterPolicyEntity != null) {

					if (masterPolicyEntity.getPolicyStatusId() == 127) {
						policyDto = new ModelMapper().map(masterPolicyEntity, PolicyDto.class);
					}
				}
			}
			if (servicetype.equals("partialsurrender")) {
				if (masterPolicyEntity != null) {
					String getProductName = masterPolicyEntity.getProductVariant()
							.substring(masterPolicyEntity.getProductVariant().length() - 2);
					System.out.println("get prod" + getProductName);

					if (getProductName.equals("V1")) {
						if (masterPolicyEntity.getPolicyStatusId() == 127) {
							policyDto = new ModelMapper().map(masterPolicyEntity, PolicyDto.class);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.print("get Validation SearchFilterListPolicy" + e);

		}

		return ApiResponseDto.success(policyDto);
	}

	// add dto and policyServiceRepository add get policy number,get service type  
	// inprogresssearchfilter
	//pmstTmpConversionPropsRepository add get policy number
	
	@Override
	public ApiResponseDto<List<PmstTmpMergerPropsDto>> inprogresssearchfilter(PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {

//		RenewalPolicyTMPEntity renewalPolicyTMPEntity=renewalPolicyTMPRepository.findBypolicyNumberandisActive(policyNumber);
	
			PolicyServiceEntitiy policyServiceEntitiy = policyServiceRepository.findcheckserviceIsActive(pmstTmpMergerPropsDto.getPolicyNumber(),pmstTmpMergerPropsDto.getServiceType());
		if(policyServiceEntitiy!=null) {
		List<PmstTmpMergerPropsEntity> pmstTmpMergerPropsEntity = pmstTmpMergerPropsRepository
				.findBysourceTmpPolicyIDinprogress(pmstTmpMergerPropsDto.getPolicyNumber());
		return ApiResponseDto.success(pmstTmpMergerPropsEntity.stream().map(CommonPolicyServiceHelper::entitytoDto)
				.collect(Collectors.toList()));
		}else {
			return ApiResponseDto.success(null);
		}
		
//		 Object[] findPolicyDetailSeerch = pmstTmpMergerPropsRepository.findPolicyDetailSeerch();
//			for (Object object : findPolicyDetailSeerch) {
//
//				 	Object[] ob = (Object[]) object;			
//					System.out.println(ob[4].toString());
//					
//					System.out.println(ob[0].toString());
//					
//					System.out.println(	DateUtils.convertStringToDate(String.valueOf(ob[2])));
//					
//					DateUtils.convertStringToDate(String.valueOf(ob[2]));
//		
//					pmstTmpMergerPropsDto.setId(NumericUtils.convertStringToLong(String.valueOf(ob[0])));
//					
//					pmstTmpMergerPropsDto.setMergerRequestNumber(NumericUtils.convertStringToLong(String.valueOf(ob[1])));
//					
//					pmstTmpMergerPropsDto.setMergerRequestDate(DateUtils.convertStringToDate(String.valueOf(ob[2])));
//					
//					pmstTmpMergerPropsDto.setMergerType(NumericUtils.convertStringToInteger(String.valueOf(ob[3])));
//					
//					pmstTmpMergerPropsDto.setSourceTmpPolicyID(NumericUtils.convertStringToLong(String.valueOf(ob[4])));
//					
//					pmstTmpMergerPropsDto.setDestinationTmpPolicyID(NumericUtils.convertStringToLong(String.valueOf(ob[5])));
//					
//			      
//					pmstTmpMergerPropsDto.setSourcePriorFundValue(NumericUtils.convertStringToInteger(String.valueOf(ob[6])));
//					pmstTmpMergerPropsDto.setDestinationPriorFundValue(NumericUtils.convertStringToInteger(String.valueOf(ob[7])));
//					pmstTmpMergerPropsDto.setSourceAccruedInterest(NumericUtils.convertStringToInteger(String.valueOf(ob[8])));
//					
//					pmstTmpMergerPropsDto.setDestinationPriorFundValue(NumericUtils.convertStringToInteger(String.valueOf(ob[9])));
//					
//					pmstTmpMergerPropsDto.setDestinationPriorTotalValue(NumericUtils.convertStringToInteger(String.valueOf(ob[10])));
//					
//					pmstTmpMergerPropsDto.setStatusID(NumericUtils.convertStringToInteger(String.valueOf(ob[11])));
//					
//				//	pmstTmpMergerPropsDto.setIsActive(String.valueOf(ob[8])));
//					
//					pmstTmpMergerPropsDto.setCreatedDate(DateUtils.convertStringToDate(String.valueOf(ob[13])));
//					
//					
//					pmstTmpMergerPropsDto.setModifiedBy(String.valueOf(ob[14]));
//					
//					pmstTmpMergerPropsDto.setModifiedDate(DateUtils.convertStringToDate(String.valueOf(ob[15])));
//						
//			}

	}

	@Transactional
	@Override
	public ApiResponseDto<PmstTmpMergerPropsDto> SaveMergerinService(PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {
		PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity = new PmstTmpMergerPropsEntity();
		
		TaskProcessEntity taskProcessEntity = taskProcessRepository.findByProcessName("MERGER");

		TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();

		try {
//			if (policyServiceRepository.findByPolicyandType(pmstTmpMergerPropsDto.getSourcemasterPolicyId(),
//					pmstTmpMergerPropsDto.getServiceType()).size() > 0) {
//				return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
//			} else {
//				if (pmstTmpMergerPropsDto.getSourcemasterPolicyId() != null
//						&& pmstTmpMergerPropsDto.getDestiMasterPolicyId() != null) {
//					// Source Policy ID copy to master to Temp then it will be Source TempId
//					MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository
//							.findById(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//					if (masterPolicyEntity != null) {
//						RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
//								.copytoTmpForClaim(masterPolicyEntity);
//						renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
//						PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
//
//						policyServiceEntitiy.setServiceType("MERGER"); 
//						policyServiceEntitiy.setPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//						policyServiceEntitiy.setCreatedBy(pmstTmpMergerPropsDto.getCreatedBy());
//						policyServiceEntitiy.setCreatedDate(new Date());
//						policyServiceEntitiy.setIsActive(true);
//						policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);
//
//						renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
//						renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
//						MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
//						TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper
//								.copytomastertoTemp(renewalPolicyTMPEntity.getId(), mPHEntity);
//						tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
//						renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
//						renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
//						Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
//								.findBypolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//						RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
//								.copyToTmpSchemeforClaim(policySchemeEntity, renewalPolicyTMPEntity.getId());
//						renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);
//
//						List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
//								.findBypmstPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//						List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
//								.copyToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
//						memberCategoryRepository.saveAll(getmemberCategoryEntity);
//
////					policyLifeCoverRepository.updateISActive(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//
//						List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
//								.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//
//						List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
//								.copyToTmpLifeCoverforClaim(policyLifeCoverEntity, memberCategoryEntity,
//										renewalPolicyTMPEntity.getId());
//
//						renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
//						policyLifeCoverRepository.updateISActive(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//
////					policyGratuityBenefitRepository.updateIsActive(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//						List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
//								.findBypolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//						List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
//								.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
//										renewalPolicyTMPEntity.getId());
//
//						renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
//						policyGratuityBenefitRepository.updateIsActive(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//
//						Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
//								.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//						RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
//								.copyToTmpValuationforClaim(policyValuationEntity, renewalPolicyTMPEntity.getId());
//						renewalValuationTMPRepository.save(renewalValuationTMPEntity);
//
//						Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
//								.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//						RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
//								.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity,
//										renewalPolicyTMPEntity.getId());
//						renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);
//
//						Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
//								.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//						RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
//								.copyToTmpValuationBasicClaim(policyValutationBasicEntity,
//										renewalPolicyTMPEntity.getId());
//						renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);
//
//						List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
//								.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//						List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
//								.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity,
//										renewalPolicyTMPEntity.getId());
//						renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);
//						List<PolicyMemberEntity> policyMemberEntityList = policyMemberRepository
//								.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//						for (PolicyMemberEntity policyMemberEntity : policyMemberEntityList) {
//							RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
//									.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity,
//											memberCategoryEntity, renewalPolicyTMPEntity.getId());
//							renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);

//						pmstTmpMergerPropsEntity.setSourceTmpPolicyID(pmstTmpMergerPropsDto.getSourceTmpPolicyID());
//						}


//					}
//				}
//			}
			if (policyServiceRepository.findByPolicyandType(pmstTmpMergerPropsDto.getDestiMasterPolicyId(),
					pmstTmpMergerPropsDto.getServiceType()).size() > 0) {
				return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
			} else {

				MasterPolicyEntity masterPolicyEntitydes = masterPolicyCustomRepository
						.findById(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				if (masterPolicyEntitydes != null) {
					RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
							.copytoTmpForClaim(masterPolicyEntitydes);
					renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
					PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();

					policyServiceEntitiy.setServiceType("merger");
					policyServiceEntitiy.setPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
					policyServiceEntitiy.setCreatedBy(pmstTmpMergerPropsDto.getCreatedBy());
					policyServiceEntitiy.setCreatedDate(new Date());
					policyServiceEntitiy.setIsActive(true);
					policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

					renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
					renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
					MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntitydes.getMasterpolicyHolder()).get();
					TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper
							.copytomastertoTemp(renewalPolicyTMPEntity.getId(), mPHEntity);
					tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
					renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
					renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
					Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
							.findBypolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
					RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
							.copyToTmpSchemeforClaim(policySchemeEntity, renewalPolicyTMPEntity.getId());
					renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

					List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
							.findBypmstPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
					List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
							.copyToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
					memberCategoryRepository.saveAll(getmemberCategoryEntity);

//					policyLifeCoverRepository.updateISActive(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
					List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
							.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
					List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
							.copyToTmpLifeCoverforClaim(policyLifeCoverEntity, memberCategoryEntity,
									renewalPolicyTMPEntity.getId());

					renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
					policyLifeCoverRepository.updateISActive(pmstTmpMergerPropsDto.getDestiMasterPolicyId());

//					policyGratuityBenefitRepository.updateIsActive(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
					List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
							.findBypolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
					List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
							.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
									renewalPolicyTMPEntity.getId());

					renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
					policyGratuityBenefitRepository.updateIsActive(pmstTmpMergerPropsDto.getDestiMasterPolicyId());

					Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
							.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
					RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
							.copyToTmpValuationforClaim(policyValuationEntity, renewalPolicyTMPEntity.getId());
					renewalValuationTMPRepository.save(renewalValuationTMPEntity);

					Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
							.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
					RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
							.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity,
									renewalPolicyTMPEntity.getId());
					renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

					Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
							.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
					RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
							.copyToTmpValuationBasicClaim(policyValutationBasicEntity, renewalPolicyTMPEntity.getId());
					renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

					List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
							.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
					List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
							.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity,
									renewalPolicyTMPEntity.getId());
					renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);
					List<PolicyMemberEntity> policyMemberEntityList = policyMemberRepository
							.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
					for (PolicyMemberEntity policyMemberEntity : policyMemberEntityList) {
						RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
								.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity,
										memberCategoryEntity, renewalPolicyTMPEntity.getId());
						renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);
					}

					//instead of destination policy take the source member and put it in the destination policy id
					pmstTmpMergerPropsEntity.setDestinationTmpPolicyID(renewalPolicyTMPEntity.getId());
				}
			}

			pmstTmpMergerPropsEntity.setCreatedBy(pmstTmpMergerPropsDto.getCreatedBy());
			pmstTmpMergerPropsEntity.setCreatedDate(new Date());
			pmstTmpMergerPropsEntity.setMergerRequestDate(new Date());
			pmstTmpMergerPropsEntity.setIsActive(true);
			pmstTmpMergerPropsEntity.setMergerType(pmstTmpMergerPropsDto.getMergerType());
			pmstTmpMergerPropsEntity.setDestinationPriorFundValue(pmstTmpMergerPropsDto.getDestinationPriorFundValue());
            pmstTmpMergerPropsEntity.setStatusID(250L);			
			pmstTmpMergerPropsEntity.setSourcePriorFundValue(pmstTmpMergerPropsDto.getSourcePriorFundValue());
			pmstTmpMergerPropsEntity.setSourcePriorTotalFund(pmstTmpMergerPropsDto.getSourcePriorTotalFund());
			pmstTmpMergerPropsEntity.setStatusID(pmstTmpMergerPropsDto.getStatusID());
			pmstTmpMergerPropsEntity
					.setDestinationPriorTotalValue(pmstTmpMergerPropsDto.getDestinationPriorTotalValue());
			// pmstTmpMergerPropsEntity.setMergerRequestNumber(
			// RenewalPolicyTMPHelper.nextQuotationNumber(pmstTmpMergerPropsRepository.maxMergerRequestNo()));
			pmstTmpMergerPropsEntity.setMergerRequestNumber(commonService.getSequence(HttpConstants.MERGER));
			pmstTmpMergerPropsEntity.setDestinationPolicyID(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
			pmstTmpMergerPropsEntity.setSourcePolicyID(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
			
			//
			Long statusId = 250L;
			taskAllocationEntity.setTaskStatus(statusId.toString());
			taskAllocationEntity.setRequestId(pmstTmpMergerPropsEntity.getMergerRequestNumber());
			taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//			taskAllocationEntity.setLocationType(quotationRenewalDto.getUnitCode());
			taskAllocationEntity.setCreatedBy(pmstTmpMergerPropsEntity.getCreatedBy());
			taskAllocationEntity.setCreatedDate(new Date());
			taskAllocationEntity.setModulePrimaryId(pmstTmpMergerPropsEntity.getId());
			taskAllocationEntity.setIsActive(true);
			taskAllocationRepository.save(taskAllocationEntity);
			
			//Moving members from source policy to destination policy
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
					.findById(pmstTmpMergerPropsDto.getSourcemasterPolicyId()).get();
			RenewalPolicyTMPEntity desrenewalPolicyTMPEntity = renewalPolicyTMPRepository
					.findById(pmstTmpMergerPropsDto.getDestiMasterPolicyId()).get();
			Long licId = renewalPolicyTMPMemberRepository.maximumLicNumber(desrenewalPolicyTMPEntity.getId());
			List<RenewalPolicyTMPMemberEntity> renewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository
					.findByPolicyId(renewalPolicyTMPEntity.getId());

			List<RenewalPolicyTMPMemberEntity> saveAllFromSource = new ArrayList<>();

			Long temp = 1L;
			
//			List<MemberCategoryEntity> sourceMemCategory = new ArrayList<>();
//			List<MemberCategoryEntity> destinationMemCategory = new ArrayList<>();
			
			List<MemberCategoryEntity> sourceMemCategory = memberCategoryRepository.findBypmstPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//				sourceResult.ifPresent(sourceMemCategory::add);
			List<MemberCategoryEntity> destinationMemCategory = memberCategoryRepository.findBypmstPolicyId(desrenewalPolicyTMPEntity.getId()); 
//			    destinationResult.ifPresent(destinationMemCategory::add);
			    
			for (RenewalPolicyTMPMemberEntity getRenewalPolicyTMPMemberEntity : renewalPolicyTMPMemberEntity) {
				if ((getRenewalPolicyTMPMemberEntity.getTmpPolicyId())
						.equals(pmstTmpMergerPropsDto.getSourceTmpPolicyID())) {
					RenewalPolicyTMPMemberEntity cloneEntity = CommonPolicyServiceHelper
							.cloneEntity(getRenewalPolicyTMPMemberEntity);
//					String licIdFromPolicy = getRenewalPolicyTMPMemberEntity.getLicId();
//					Long temp = Long.valueOf(licIdFromPolicy);
					Long LicinLong = temp + licId;
					cloneEntity.setLicId(LicinLong.toString());
					temp++;
					cloneEntity.setRecordType(null);
					RenewalPolicyTMPMemberEntity newRenewalPolicyTMPEntity = CommonPolicyServiceHelper
							.copytomembersourcetodest(cloneEntity, desrenewalPolicyTMPEntity, sourceMemCategory, destinationMemCategory);
					newRenewalPolicyTMPEntity.setPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());					
					saveAllFromSource.add(newRenewalPolicyTMPEntity);
				}
			}
			renewalPolicyTMPMemberRepository.saveAll(saveAllFromSource);
			pmstTmpMergerPropsEntity.setSourceTmpPolicyID(pmstTmpMergerPropsDto.getSourcemasterPolicyId());//NEED CLARIFICATION 
			pmstTmpMergerPropsRepository.save(pmstTmpMergerPropsEntity);


		} catch (Exception e) {
			e.getStackTrace();
		}

		// destination Policy ID copy to master to Temp then it will be destination
		// TempId

		PmstTmpMergerPropsDto responseDto = CommonPolicyServiceHelper.entitytoDto(pmstTmpMergerPropsEntity);
		responseDto.setDestiMasterPolicyId(pmstTmpMergerPropsEntity.getDestinationPolicyID());
		responseDto.setSourcemasterPolicyId(pmstTmpMergerPropsEntity.getSourcePolicyID());
		return ApiResponseDto.created(responseDto);
	}

	
	// add dto and policyServiceRepository add get policy number,get service type  
	//ExistingSearchFilter
		//pmstTmpConversionPropsRepository add get policy number
	
	@Override
	public ApiResponseDto<List<PmstTmpMergerPropsDto>> getExistingSearchFilter(String policyNumber) {
		PolicyServiceEntitiy policyServiceEntitiy = policyServiceRepository.findcheckserviceInActive(Long.valueOf(policyNumber));
		if (policyServiceEntitiy != null) {
			List<PmstTmpMergerPropsEntity> pmstTmpMergerPropsEntity = pmstTmpMergerPropsRepository
					.findBysourceTmpPolicyIDExisting(Long.valueOf(policyNumber));
			return ApiResponseDto.success(pmstTmpMergerPropsEntity.stream().map(CommonPolicyServiceHelper::entitytoDto)
					.collect(Collectors.toList()));
		} else {
			return ApiResponseDto.notFound(null);
		}
	}

	@Transactional
	@Override
	public ApiResponseDto<PmstTmpMergerPropsDto> updateMergerinService(PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {
		PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity = pmstTmpMergerPropsRepository
				.findById(pmstTmpMergerPropsDto.getId()).get();
		try {

			RenewalPolicyTMPEntity oldrenewalPolicyTMPEntity = renewalPolicyTMPRepository
					.findById(pmstTmpMergerPropsEntity.getDestinationTmpPolicyID()).get();

			if (oldrenewalPolicyTMPEntity.getMasterPolicyId() != pmstTmpMergerPropsDto.getDestiMasterPolicyId()) {
//			RenewalPolicyTMPEntity renewalPolicyTMPEntity=renewalPolicyTMPRepository.findById(pmstTmpMergerPropsEntity.getDestinationTmpPolicyID()).get();

				policyServiceRepository.updateisActivefalse(oldrenewalPolicyTMPEntity.getMasterPolicyId());
				renewalSchemeruleTMPRepository.updateisActivefalse(oldrenewalPolicyTMPEntity.getId());
				renewalLifeCoverTMPRepository.updateisActivefalse(oldrenewalPolicyTMPEntity.getId());
				renewalGratuityBenefitTMPRepository.updateisActivefalse(oldrenewalPolicyTMPEntity.getId());
				renewalValuationTMPRepository.updateisActivefalse(oldrenewalPolicyTMPEntity.getId());
				renewalValuationMatrixTMPRepository.updateisActivefalse(oldrenewalPolicyTMPEntity.getId());
				renewalValuationBasicTMPRepository.updateisActivefalse(oldrenewalPolicyTMPEntity.getId());
				renewalValuationWithdrawalTMPRepository.updateisActivefalse(oldrenewalPolicyTMPEntity.getId());
				renewalPolicyTMPMemberRepository.updateisActivefalse(oldrenewalPolicyTMPEntity.getId());
				oldrenewalPolicyTMPEntity.setIsActive(false);
				renewalPolicyTMPRepository.save(oldrenewalPolicyTMPEntity);
				// if parent table is update as isactice false then child also change or no need

			}
			MasterPolicyEntity masterPolicyEntitydes = masterPolicyCustomRepository
					.findById(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
			if (masterPolicyEntitydes != null) {
				RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
						.copytoTmpForClaim(masterPolicyEntitydes);
				renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
				PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();

				policyServiceEntitiy.setServiceType("merger");
				policyServiceEntitiy.setPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				policyServiceEntitiy.setCreatedBy(pmstTmpMergerPropsDto.getCreatedBy());
				policyServiceEntitiy.setCreatedDate(new Date());
				policyServiceEntitiy.setIsActive(true);
				policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

				renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
				renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

				Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
						.findBypolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
						.copyToTmpSchemeforClaim(policySchemeEntity, renewalPolicyTMPEntity.getId());
				renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

				List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
						.findBypmstPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
						.copyToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
				memberCategoryRepository.saveAll(getmemberCategoryEntity);

				List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
						.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
						.copyToTmpLifeCoverforClaim(policyLifeCoverEntity,
								memberCategoryEntity, renewalPolicyTMPEntity.getId());
				renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
				policyLifeCoverRepository.updateISActive(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				
				List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
						.findBypolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
						.copyToTmpGratuityforClaim(policyGratuityBenefitEntity,
								memberCategoryEntity, renewalPolicyTMPEntity.getId());
				renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
				policyGratuityBenefitRepository.updateIsActive(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				
				Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
						.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
						.copyToTmpValuationforClaim(policyValuationEntity, renewalPolicyTMPEntity.getId());
				renewalValuationTMPRepository.save(renewalValuationTMPEntity);

				Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
						.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
						.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity, renewalPolicyTMPEntity.getId());
				renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

				Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
						.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
						.copyToTmpValuationBasicClaim(policyValutationBasicEntity, renewalPolicyTMPEntity.getId());
				renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

				List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
						.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
						.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity,
								renewalPolicyTMPEntity.getId());
				renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);
				List<PolicyMemberEntity> policyMemberEntityList = policyMemberRepository
						.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				for (PolicyMemberEntity policyMemberEntity : policyMemberEntityList) {
					RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
							.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity,
									memberCategoryEntity, renewalPolicyTMPEntity.getId());
					renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);
				}

				MPHEntity mPHEntity = mPHRepository.findById(renewalPolicyTMPEntity.getMasterpolicyHolder()).get();
				TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytomastertoTemp(renewalPolicyTMPEntity.getId(),
						mPHEntity);
				tempMPHRepository.save(tempMPHEntity);
				pmstTmpMergerPropsEntity.setDestinationTmpPolicyID(renewalPolicyTMPEntity.getId());
			}
			try {
				pmstTmpMergerPropsEntity.setModifiedBy(pmstTmpMergerPropsDto.getModifiedBy());
				pmstTmpMergerPropsEntity.setModifiedDate(new Date());
				pmstTmpMergerPropsEntity.setMergerRequestDate(new Date());
				pmstTmpMergerPropsEntity.setIsActive(true);
				pmstTmpMergerPropsEntity
						.setDestinationPriorFundValue(pmstTmpMergerPropsDto.getDestinationPriorFundValue());
				;
				pmstTmpMergerPropsEntity
						.setDestinationPriorTotalValue(pmstTmpMergerPropsDto.getDestinationPriorTotalValue());
//		pmstTmpMergerPropsEntity.setMergerRequestNumber(RenewalPolicyTMPHelper
//							.nextQuotationNumber(pmstTmpMergerPropsRepository.maxMergerRequestNo()));
				pmstTmpMergerPropsRepository.save(pmstTmpMergerPropsEntity);
			} catch (Exception e) {
				System.out.println("update new destination id" + e);
			}
		} catch (Exception e) {
			System.out.println("Update function" + e);
		}
		return ApiResponseDto.created(CommonPolicyServiceHelper.entitytoDto(pmstTmpMergerPropsEntity));
	}
	
	@Transactional
	@Override
	public ApiResponseDto<PmstTmpMergerPropsDto> mergerStatusUpdate(PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {
		PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity = pmstTmpMergerPropsRepository
				.findById(pmstTmpMergerPropsDto.getId()).get();

		if(pmstTmpMergerPropsDto.getStatusID() == 254) {

			RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
			.findById(pmstTmpMergerPropsEntity.getDestinationTmpPolicyID()).get();

			RenewalPolicyTMPEntity sorrenewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(pmstTmpMergerPropsEntity.getSourceTmpPolicyID()).get();
			
			List<Long> policyServiceIDs = new ArrayList<>();
			
			policyServiceIDs.add(renewalPolicyTMPEntity.getPolicyServiceId());
			policyServiceIDs.add(sorrenewalPolicyTMPEntity.getPolicyServiceId());
			
			policyServiceRepository.inactiveservice(policyServiceIDs);
//					renewalPolicyTMPEntity.getPolicyServiceId(),sorrenewalPolicyTMPEntity.getPolicyServiceId());
		}
		pmstTmpMergerPropsEntity.setStatusID(pmstTmpMergerPropsDto.getStatusID());
		
		return ApiResponseDto.success(
				CommonPolicyServiceHelper.entitytoDto(pmstTmpMergerPropsRepository.save(pmstTmpMergerPropsEntity)));
			}

	@Transactional
	@Override
	public ApiResponseDto<PmstTmpMergerPropsDto> approvemerger(PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {
		
		Long mergerApprove = 253L;
		Long policyMerged = 481L;
		
		PmstTmpMergerPropsEntity mergerPropsEntity = pmstTmpMergerPropsRepository
				.findById(pmstTmpMergerPropsDto.getId()).get();//merger props id
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(mergerPropsEntity.getSourceTmpPolicyID()).get();
		RenewalPolicyTMPEntity desrenewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(mergerPropsEntity.getDestinationTmpPolicyID()).get();

		mergerPropsEntity.setStatusID(mergerApprove);	
		mergerPropsEntity.setModifiedBy(pmstTmpMergerPropsDto.getModifiedBy());
		mergerPropsEntity.setModifiedDate(new Date());
		pmstTmpMergerPropsRepository.save(mergerPropsEntity);
		try {

			Optional<RenewalPolicyTMPEntity> renewalPolicyTMPEntityRenewal = renewalPolicyTMPRepository
					.findById(desrenewalPolicyTMPEntity.getTmpPolicyId());
			RenewalPolicyTMPEntity renewalPolicy = null;
			if (renewalPolicyTMPEntityRenewal.isPresent()) {
				renewalPolicy = renewalPolicyTMPEntityRenewal.get();
				renewalPolicy.setPolicyStatusId(approvedStatudId);
				renewalPolicy.setPolicySubStatusId(approvedSubStatudId);
				renewalPolicy.setPolicytaggedStatusId(existingTaggedStatusId);
				renewalPolicyTMPRepository.save(renewalPolicy);
			}
				
//				TaskAllocationEntity taskallocationentity = taskAllocationRepository
//						.findByRequestId(renewalPolicy.getPolicyNumber());
//				if (taskallocationentity != null) {
//					taskallocationentity.setTaskStatus(approvedSubStatudId.toString());
//					taskAllocationRepository.save(taskallocationentity);
//				}
			MasterPolicyEntity getMasterPolicyEntity = null;
			try {
				getMasterPolicyEntity = policyMastertoPolicyHis(desrenewalPolicyTMPEntity,
						desrenewalPolicyTMPEntity.getCreatedBy());
			} catch (Exception e) {
				System.out.println(e);
			}

				getMasterPolicyEntity.setPolicyStatusId(approvedStatudId);
				getMasterPolicyEntity.setPolicyTaggedStatusId(existingTaggedStatusId);
				masterPolicyRepository.save(getMasterPolicyEntity);
				// New
				String mPHStateType = null;
				String mPHAddress = "";
				String mphStateCode = "";
				String unitStateType ="";
				String unitStateCode = "";
				String unitStateName = "";
				String prodAndVarientCodeSame = "";
				
				if (isDevEnvironment == false) {
					prodAndVarientCodeSame = commonModuleService
							.getProductCode(getMasterPolicyEntity.getProductId());
					unitStateName = commonMasterUnitRepository
							.getStateNameByUnitCode(getMasterPolicyEntity.getUnitCode());
					unitStateType = commonMasterStateRepository.getStateType(unitStateName);
					unitStateCode = commonMasterStateRepository.getGSTStateCode(unitStateName);

					MPHEntity mPHEntity = mPHRepository.findById(getMasterPolicyEntity.getMasterpolicyHolder()).get();
					for (MPHAddressEntity getMPHAddressEntity : mPHEntity.getMphAddresses()) {
						if (getMPHAddressEntity.getStateName() != null
								|| getMPHAddressEntity.getAddressLine1() != null) {
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
					//for souce MasterPolicyContributiondetail pass source master polid findbuymasterpolicyid 
					// MasterPolicyEntity get mph id 
					MergePolicyRequestModel mergePolicyRequestModel = new MergePolicyRequestModel();
					PolicyContributionDetailEntity masterPolicyContributionEntity = null;
					try {
					masterPolicyContributionEntity = policyContributionDetailRepository
							.findByTmpPolicyandType(pmstTmpMergerPropsDto.getDestinationTmpPolicyID(),"MRG");// tempId
					} catch(Exception e) {
						System.out.println("=========================== 1090");
					}
					String toGSTIn = mPHEntity.getGstIn() == null ? "" : mPHEntity.getGstIn();
					HSNCodeDto hSNCodeDto = accountingService.getHSNCode();
					Map<String, Double> gstComponents = accountingService.getGstComponents(unitStateType, mPHStateType,
							hSNCodeDto, masterPolicyContributionEntity.getLifePremium());
					System.out.println("=====================================================================");
					System.out.println("===========================================the isse is in 1093");
					// use the Dto's of Merger here
					mergePolicyRequestModel.setSourcePolicyNo(renewalPolicyTMPEntity.getPolicyNumber());
					mergePolicyRequestModel.setTargetPolicyNo(desrenewalPolicyTMPEntity.getPolicyNumber());
					mergePolicyRequestModel.setSourcePolicyUnitCode(renewalPolicyTMPEntity.getUnitCode());
					mergePolicyRequestModel.setTargetPoicyUnitCode(desrenewalPolicyTMPEntity.getUnitCode());
					mergePolicyRequestModel.setSourceVersion(renewalPolicyTMPEntity.getProductVariant());
					mergePolicyRequestModel.setTargetVersion(desrenewalPolicyTMPEntity.getProductVariant());
					mergePolicyRequestModel.setMphCode(mPHEntity.getMphCode());
					mergePolicyRequestModel.setRefNo(masterPolicyContributionEntity.getId().toString()); //mergerPropsEntity.getMergerRequestNumber()
			
					mergePolicyRequestModel.setIsPayoutApplicable("yes");
					mergePolicyRequestModel.setIsGSTRefundOnPremiumApplicable("yes");

					mergePolicyRequestModel.setGlTransactionModel(accountingService.glTransactionModel(
							getMasterPolicyEntity.getProductId(), getMasterPolicyEntity.getProductVariantId(),
							getMasterPolicyEntity.getUnitCode(), "GratuityMergerPolicyApproval"));

					ResponseDto responseDto = accountingService
							.commonmasterserviceAllUnitCode(getMasterPolicyEntity.getUnitCode());
					System.out.println("=====================================================================");
					System.out.println("==========================================the isse is in 1113");
					
					// 2 using desti policy we need to get master policy data and set it
					RegularGstDetailModel regularGstDetailModel = new RegularGstDetailModel();
					
					regularGstDetailModel.setAmountWithTax(
							masterPolicyContributionEntity.getLifePremium() + masterPolicyContributionEntity.getGst()); 
					regularGstDetailModel.setAmountWithoutTax(masterPolicyContributionEntity.getLifePremium());
					regularGstDetailModel.setCessAmount(0.0);
					regularGstDetailModel.setCgstAmount(gstComponents.get("CGST"));
					regularGstDetailModel.setCgstRate(hSNCodeDto.getCgstRate());
//					regularGstDetailModel.setCreatedBy(username);
//					regularGstDetailModel.setCreatedDate(new Date());
//					regularGstDetailModel.setEffectiveEndDate(""); 
//					regularGstDetailModel.setEffectiveStartDate(new Date());			
					regularGstDetailModel.setEntryType(toGSTIn != null ? "B2B" : "B2C");
					regularGstDetailModel.setFromGstn(responseDto.getGstIn() == null ? "" : responseDto.getGstIn());
					regularGstDetailModel.setFromPan(responseDto.getPan());
					regularGstDetailModel.setFromStateCode(unitStateCode); 

					if (renewalPolicyTMPEntity.getGstApplicableId() == 1l)
						regularGstDetailModel.setGstApplicableType("Taxable");
					else
						regularGstDetailModel.setGstApplicableType("Non-Taxable");

					regularGstDetailModel.setGstInvoiceNo("");
					regularGstDetailModel
							.setGstRate(accountingService.getGSTRate(unitStateType, mPHStateType, hSNCodeDto) * 100);
					regularGstDetailModel.setGstRefNo(renewalPolicyTMPEntity.getPolicyNumber());// ?? they are setting " " 
					regularGstDetailModel.setGstRefTransactionNo(renewalPolicyTMPEntity.getPolicyNumber());
					regularGstDetailModel.setGstTransactionType("DEBIT");
					regularGstDetailModel.setGstType("GST");
					regularGstDetailModel.setHsnCode(hSNCodeDto.getHsnCode());
					regularGstDetailModel.setIgstAmount(gstComponents.get("IGST"));
					regularGstDetailModel.setIgstRate(hSNCodeDto.getIgstRate());
//					regularGstDetailModel.setModifiedBy(0L); 
//					regularGstDetailModel.setModifiedDate(new Date()); 
					regularGstDetailModel.setMphAddress(mPHAddress);
					regularGstDetailModel.setMphName(mPHEntity.getMphName());
					regularGstDetailModel.setNatureOfTransaction("NEFT"); //hSNCodeDto.getDescription()
					regularGstDetailModel.setOldInvoiceDate(new Date()); 
					regularGstDetailModel.setOldInvoiceNo("IN20123QE"); 
					regularGstDetailModel.setProductCode(prodAndVarientCodeSame);
					regularGstDetailModel.setRemarks("MERGER REGULAR GST");
					regularGstDetailModel.setSgstAmount(gstComponents.get("SGST"));
					regularGstDetailModel.setSgstRate(hSNCodeDto.getSgstRate());
					regularGstDetailModel.setToGstIn(mPHEntity.getGstIn() == null ? "" : mPHEntity.getGstIn()); 
																												
					regularGstDetailModel.setToPan(mPHEntity.getPan() == null ? "" : mPHEntity.getPan());
					regularGstDetailModel.setToStateCode(mphStateCode == null ? "" : mphStateCode);
					regularGstDetailModel.setTotalGstAmount(masterPolicyContributionEntity.getGst().doubleValue()); //USING totalRefund method
					regularGstDetailModel.setTransactionDate(new Date());
					regularGstDetailModel.setTransactionSubType('A'); 
					regularGstDetailModel.setTransactionType('C'); 
//					gstDetailModel.setUserCode(username);
					regularGstDetailModel.setUtgstAmount(gstComponents.get("UTGST"));
					regularGstDetailModel.setUtgstRate(hSNCodeDto.getUtgstRate());
					regularGstDetailModel.setVariantCode(prodAndVarientCodeSame);
					regularGstDetailModel.setYear(DateUtils.uniqueNoYYYY());
					regularGstDetailModel.setMonth(DateUtils.currentMonthName());

					System.out.println("=====================================================================");
					System.out.println("=========================================the isse is in 1174");
					mergePolicyRequestModel.setRegularGstDetailModel(regularGstDetailModel);

					JournalVoucherDetailModel journalVoucherDetailModel = new JournalVoucherDetailModel();
					journalVoucherDetailModel.setLineOfBusiness(getMasterPolicyEntity.getLineOfBusiness());
					journalVoucherDetailModel.setProduct(prodAndVarientCodeSame);
					journalVoucherDetailModel.setProductVariant(prodAndVarientCodeSame);
					mergePolicyRequestModel.setJournalVoucherDetailModel(journalVoucherDetailModel);

					// 4
					List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository
							.findBytmpPolicyId(desrenewalPolicyTMPEntity.getTmpPolicyId());
					List<DebitCreditMergePolicyModel> getDdebitCreditMergePolicyModel = new ArrayList<DebitCreditMergePolicyModel>();
					Double premiumAdjustment = masterPolicyContributionEntity.getLifePremium().doubleValue();
					Double gstOnPremiumAdjusted = masterPolicyContributionEntity.getGst().doubleValue();
					Double currentServiceAdjusted = masterPolicyContributionEntity.getCurrentServices().doubleValue();
					Double pastServiceAdjusted = masterPolicyContributionEntity.getPastService().doubleValue();

//			accountingService.consumeDeposit(renewalContributionAdjustRequestModelDto);

					String bankName = "";
					String ifscCode = "";
					String bankBranch = "";
					String accountType = "";
					String accountNo = "";
					for (MPHBankEntity mphBankEntity : mPHEntity.getMphBank()) {
						if(mphBankEntity.getBankName()!=null)
						bankName = mphBankEntity.getBankName();
						
						if(mphBankEntity.getIfscCode()!=null)
						ifscCode = mphBankEntity.getIfscCode();
						
						bankBranch = mphBankEntity.getBankBranch();
						accountType = mphBankEntity.getAccountType();
						accountNo = mphBankEntity.getAccountNumber();
					}

					// 5 get details from mphEntity's BankDetails entity
					
					
					
					String beneficiaryPaymentId = null;
					
					BeneficiaryPaymentDetailModel beneficiaryPaymentDetailModel = new BeneficiaryPaymentDetailModel();
					beneficiaryPaymentDetailModel.setBeneficiaryAccountNumber(accountNo);
					beneficiaryPaymentDetailModel.setBeneficiaryAccountType(accountType); 
					beneficiaryPaymentDetailModel.setBeneficiaryBankIfsc(ifscCode); 
					beneficiaryPaymentDetailModel.setBeneficiaryBankName(bankName); 
					beneficiaryPaymentDetailModel.setBeneficiaryBranchName(bankBranch); 
					beneficiaryPaymentDetailModel.setBeneficiaryLei(null); //ignored in DOMServiceImpl
					beneficiaryPaymentDetailModel.setBeneficiaryName(mPHEntity.getMphName());
					beneficiaryPaymentDetailModel.setBeneficiaryPaymentId(pmstTmpMergerPropsDto.getMergerRequestNumber()); 	// mergerPropsEntity 's service no, we dont have, same in SurrenderServiceImpl
					beneficiaryPaymentDetailModel.setEffectiveDateOfPayment(new Date());
					beneficiaryPaymentDetailModel.setIban(null); // "" in SurrenderSer		//ignored in DOMServiceImpl
					beneficiaryPaymentDetailModel.setMemberNumber(null); // //in surrenderSer		//ignored in DOMServiceImpl
					beneficiaryPaymentDetailModel.setNroAccount(null);	// "" in SurrenderSer		//ignored in DOMServiceImpl
					beneficiaryPaymentDetailModel.setOperatinUnitType("UO"); 
					beneficiaryPaymentDetailModel.setPaymentAmount(null);  // //in SurrenderServiceImpl	  // getTotalRefundMethod
					beneficiaryPaymentDetailModel.setPaymentCategory("PCM002");
					beneficiaryPaymentDetailModel.setPaymentMode('N'); 
					beneficiaryPaymentDetailModel.setPaymentSourceRefNumber("");			// mergerPropsEntity 's service no, we dont have field in entity
					beneficiaryPaymentDetailModel.setPaymentSubCategory("O");
					beneficiaryPaymentDetailModel.setPayoutSourceModule("Gratuity merger");
					beneficiaryPaymentDetailModel.setPolicyNumber(desrenewalPolicyTMPEntity.getPolicyNumber());
					beneficiaryPaymentDetailModel.setProductCode(prodAndVarientCodeSame);
					beneficiaryPaymentDetailModel.setRemarks("MERGER SERVICE approve merger");
					beneficiaryPaymentDetailModel.setSenderLei(null); 						//ignored in DOMServiceImpl & SurrenderServiceImpl
					beneficiaryPaymentDetailModel.setUnitCode(desrenewalPolicyTMPEntity.getUnitCode());  
					beneficiaryPaymentDetailModel.setVariantCode(prodAndVarientCodeSame);
					beneficiaryPaymentDetailModel.setVoucherNo(null);	// "" in SurrenderServie	//ignored in DOMServiceImpl

					mergePolicyRequestModel.setBeneficiaryPaymentDetailModel(beneficiaryPaymentDetailModel);

					//from adjustment_details table we need to get and set data 
					
					// 6
					TrnRegisModel trnRegisModel = new TrnRegisModel();
					
					trnRegisModel.setAmount(null);					//getTotalRefund method  but in SurrenderServiceImpl '1'
					trnRegisModel.setAmountType("9");				
					trnRegisModel.setBankUniqueId("");														
					trnRegisModel.setChallanNo(null);				//new BigInteger("0")
					trnRegisModel.setCreatedBy(null);				// "1"
					trnRegisModel.setPolicyNo(desrenewalPolicyTMPEntity.getPolicyNumber());
					trnRegisModel.setPoolAccountCode(null);			// ""
					trnRegisModel.setProductCode(prodAndVarientCodeSame);
					trnRegisModel.setProposalNo(null);				// ""
					trnRegisModel.setReason("Merger Service");		
					trnRegisModel.setReferenceDate(null);			//"2024-02-01
					trnRegisModel.setReferenceIdType("CRI");		
					trnRegisModel.setStatus(2); 
					trnRegisModel.setValidityUptoDate("2099-03-31"); 
					trnRegisModel.setVan("LICG"+desrenewalPolicyTMPEntity.getPolicyNumber());	
					trnRegisModel.setVariantCode(prodAndVarientCodeSame);

					mergePolicyRequestModel.setTrnRegisModel(trnRegisModel);
					
					System.out.println("=====================================================================");
					System.out.println("===================================================the isse is in 1267");
					MasterPolicyEntity getMasterPolicyEntitySource = masterPolicyRepository.findById(pmstTmpMergerPropsDto.getSourcemasterPolicyId()).get();	
//							policyMastertoPolicyHis(renewalPolicyTMPEntity,renewalPolicyTMPEntity.getCreatedBy());
					if (isDevEnvironment == false) {
						prodAndVarientCodeSame = commonModuleService
								.getProductCode(getMasterPolicyEntitySource.getProductId());
						unitStateName = commonMasterUnitRepository
								.getStateNameByUnitCode(getMasterPolicyEntitySource.getUnitCode());
						unitStateType = commonMasterStateRepository.getStateType(unitStateName);
						unitStateCode = commonMasterStateRepository.getGSTStateCode(unitStateName);

						MPHEntity mPHEntity1 = mPHRepository.findById(getMasterPolicyEntitySource.getMasterpolicyHolder()).get();
						for (MPHAddressEntity getMPHAddressEntity : mPHEntity1.getMphAddresses()) {
							if (getMPHAddressEntity.getStateName() != null
									|| getMPHAddressEntity.getAddressLine1() != null) {
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
								System.out.println("=====================================================================");
								System.out.println("====================================the isse is in 1292");
								break;
							}
						}
					}
					Double premiumRefund = 0D;
					Double gstRefund = 0D;
					
					premiumRefund = mergerPropsEntity.getPremiumRefundAmount();
					gstRefund = mergerPropsEntity.getGstRefundAmount();
					
					//reverslaGst model
					ReversalGstDetailModel reversalGstDetailModel = new ReversalGstDetailModel();

					reversalGstDetailModel.setTransactionDate(new Date());
					reversalGstDetailModel.setGstRefNo(" ");
					reversalGstDetailModel.setGstRefTransactionNo(getMasterPolicyEntity.getPolicyNumber());			//renewalPolicyTMPEntity's policyNo
					reversalGstDetailModel.setGstTransactionType("DEBIT");
					reversalGstDetailModel
							.setAmountWithTax(premiumRefund + gstRefund);
					reversalGstDetailModel
							.setAmountWithoutTax(premiumRefund);
					reversalGstDetailModel.setCessAmount(0.0); 
					reversalGstDetailModel.setTotalGstAmount(gstRefund);
					reversalGstDetailModel.setGstRate(accountingService.getGSTRate(unitStateType, mPHStateType, hSNCodeDto) * 100);
					reversalGstDetailModel.setCgstAmount(gstComponents.get("CGST"));
					reversalGstDetailModel.setCgstRate(hSNCodeDto.getCgstRate());
					reversalGstDetailModel.setIgstAmount(gstComponents.get("IGST"));
					reversalGstDetailModel.setIgstRate(hSNCodeDto.getIgstRate());
					reversalGstDetailModel.setSgstAmount(gstComponents.get("SGST"));
					reversalGstDetailModel.setSgstRate(hSNCodeDto.getSgstRate());
					reversalGstDetailModel.setUtgstAmount(gstComponents.get("UTGST"));
					reversalGstDetailModel.setUtgstRate(hSNCodeDto.getUtgstRate());
					if (getMasterPolicyEntity.getGstApplicableId() == 1l)
						reversalGstDetailModel.setGstApplicableType("Taxable");
					else
						reversalGstDetailModel.setGstApplicableType("Non-Taxable");
					reversalGstDetailModel.setGstType("GST");
					reversalGstDetailModel.setFromStateCode(unitStateCode);
					reversalGstDetailModel.setToStateCode(mphStateCode == null ? "" : mphStateCode);
					// reversalGstDetailModel.setUserCode(userName);
					reversalGstDetailModel.setTransactionType('C');
					reversalGstDetailModel.setTransactionSubType('A');
					reversalGstDetailModel.setGstInvoiceNo("");
					reversalGstDetailModel.setEntryType(toGSTIn != null ? "B2B" : "B2C");
					reversalGstDetailModel.setFromGstn(responseDto.getGstIn() == null ? "" : responseDto.getGstIn());
					reversalGstDetailModel.setFromPan(responseDto.getPan());
					reversalGstDetailModel.setHsnCode(hSNCodeDto.getHsnCode());
					reversalGstDetailModel.setMphAddress(mPHAddress);
					reversalGstDetailModel.setMphName(mPHEntity.getMphName());
					reversalGstDetailModel.setNatureOfTransaction(hSNCodeDto.getDescription());
					reversalGstDetailModel.setOldInvoiceDate(new Date());
					reversalGstDetailModel.setOldInvoiceNo("IN20123QE");
					reversalGstDetailModel.setProductCode(prodAndVarientCodeSame);
					reversalGstDetailModel.setRemarks("MERGER SERVICE RETURNING GST");
					reversalGstDetailModel.setToGstIn(mPHEntity.getGstIn() == null ? "" : mPHEntity.getGstIn());
					reversalGstDetailModel.setToPan(mPHEntity.getPan() == null ? "" : mPHEntity.getPan());
					reversalGstDetailModel.setVariantCode(prodAndVarientCodeSame);
					reversalGstDetailModel.setYear(DateUtils.uniqueNoYYYY());
					reversalGstDetailModel.setMonth(DateUtils.currentMonthName());

					mergePolicyRequestModel.setReveraGstDetailModel(reversalGstDetailModel);

					accountingService.consumeDeposit(mergePolicyRequestModel,getMasterPolicyEntity.getId());

					System.out.println("=====================================================================");
					System.out.println("=========================================the isse is in 1357");
					getMasterPolicyEntitySource.setIsActive(false);
					getMasterPolicyEntitySource.setPolicyStatusId(policyMerged);
					masterPolicyRepository.save(getMasterPolicyEntitySource);	
					policyMemberRepository
							.updateIsActiveFalseForSourcePolicy(pmstTmpMergerPropsDto.getSourcemasterPolicyId());

					policyServiceRepository.updateIsActiveBasedOnPoliyAndType(
							pmstTmpMergerPropsDto.getSourcemasterPolicyId(), pmstTmpMergerPropsDto.getServiceType());
					policyServiceRepository.updateIsActiveBasedOnPoliyAndType(
							pmstTmpMergerPropsDto.getDestiMasterPolicyId(), pmstTmpMergerPropsDto.getServiceType());
					
					TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
							.findByRequestId(mergerPropsEntity.getMergerRequestNumber());
					taskAllocationEntity.setTaskStatus(mergerPropsEntity.getStatusID().toString());
					taskAllocationRepository.save(taskAllocationEntity);
					// 3
				
			} 
		
		} catch (Exception e) {
System.out.println(e);
		}

		return null;
	}

	private MasterPolicyEntity policyMastertoPolicyHis(RenewalPolicyTMPEntity funcrenewalPolicyTMPEntity,
			String username) {
		MasterPolicyEntity newmasterPolicyEntity = null;
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = null;
		PolicyHistoryEntity policyHistoryEntity = null;
		// PolicyMaster to Copy History then Tmp Policy to Master Policy Update
		Optional<MasterPolicyEntity> masterPolicyEntity = masterPolicyRepository
				.findById(funcrenewalPolicyTMPEntity.getMasterPolicyId());
		if (masterPolicyEntity.isPresent()) {
			MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.get().getMasterpolicyHolder()).get();
			// MPH UPDATE from MAster to History update
			HistoryMPHEntity hisMPHEntity = PolicyClaimCommonHelper.copytomastertohis(mPHEntity);
			hisMPHEntity = historyMPHRepository.save(hisMPHEntity);
			// MPH UPDATE from MAster to History End
			renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(funcrenewalPolicyTMPEntity.getId()).get();
			policyHistoryEntity = policyHistoryRepository.save(
					PolicyHelper.policymasterToHisTransfer(masterPolicyEntity.get(), renewalPolicyTMPEntity, username));
 
			policyHistoryEntity.setMasterpolicyHolder(hisMPHEntity.getId());
			policyHistoryRepository.save(policyHistoryEntity);
 
			newmasterPolicyEntity = PolicyHelper.updateTemptoPolicyMaster(renewalPolicyTMPEntity, username);
			// MPH UPDATE from TEMP to MAster start
			TempMPHEntity tempMPHentity = tempMPHRepository.findById(renewalPolicyTMPEntity.getMasterpolicyHolder())
					.get();
			MPHEntity newmPHEntity = mPHRepository.save(PolicyClaimCommonHelper.copytotemptomaster(tempMPHentity));
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
					.updateTempToSchemeMaster(renewalSchemeruleTMPEntity, username);
			policySchemeRuleRepository.save(newPolicySchemeEntity);
		}

		// PolicymasterLifeCoverRule to Copy HistoryLifeCOver then Update TmpLifeCover
		// to PolicymasterLifeCoverRule

		List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
				.findByPolicyId(funcrenewalPolicyTMPEntity.getMasterPolicyId());
		List<PolicyLifeCoverEntity> policyLifeCoverEntities = new ArrayList<PolicyLifeCoverEntity>();
		for (PolicyLifeCoverEntity addPolicyLifeCoverEntity : policyLifeCoverEntity) {

			historyLifeCoverRepository.save(PolicyLifeCoverHelper.policyMastertoHistransfer(addPolicyLifeCoverEntity,
					policyHistoryEntity.getId(), username));
		}

		List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = renewalLifeCoverTMPRepository
				.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
		for (RenewalLifeCoverTMPEntity addrenewalLifeCoverTMPEntity : renewalLifeCoverTMPEntity) {
			PolicyLifeCoverEntity newPolicyLifeCoverEntity = PolicyLifeCoverHelper
					.updateTempToLifecoverMaster(addrenewalLifeCoverTMPEntity, username, newmasterPolicyEntity.getId());
			policyLifeCoverEntities.add(newPolicyLifeCoverEntity);
		}
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
			for (RenewalGratuityBenefitTMPEntity addrenewalGratuityBenefitTMPEntity : renewalGratuityBenefitTMPEntity) {
				PolicyGratuityBenefitEntity newPolicyGratuityBenefitEntity = RenewalGratuityBenefitTMPHelper
						.updateTempToPolicyGratuityBenefitMaster(addrenewalGratuityBenefitTMPEntity, username,
								newmasterPolicyEntity.getId());
				PolicyGratuityBenefitEntities.add(newPolicyGratuityBenefitEntity);
			}
			policyGratuityBenefitRepository.saveAll(PolicyGratuityBenefitEntities);

		}

		// PolicMasterValuation to copy ValuationHistory upodate RenewalvaluationTMP to
		// PolicMasterValuation
		Optional<PolicyMasterValuationEntity> policyMasterValuationEntity = policyMasterValuationRepository
				.findBypolicyId(funcrenewalPolicyTMPEntity.getMasterPolicyId());
		if (policyMasterValuationEntity.isPresent()) {
			policyValuationHistoryRepository.save(PolicyValuationHelper.policyMastertoHistransfer(
					policyMasterValuationEntity.get(), policyHistoryEntity.getId(), username));

			RenewalValuationTMPEntity renewalValuationTMPEntity = renewalValuationTMPRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			PolicyMasterValuationEntity newPolicyMasterValuationEntity = PolicyValuationHelper
					.updateTempToValuationMaster(renewalValuationTMPEntity, username);
			policyMasterValuationRepository.save(newPolicyMasterValuationEntity);
		}

		// Member Renewal Entity start
		try {
			List<RenewalPolicyTMPMemberEntity> tempMemberEntity = renewalPolicyTMPMemberRepository
					.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
			Integer licId = policyMemberRepository.maxLicNumber(renewalPolicyTMPEntity.getMasterPolicyId());
			int LicInc = 1;
			List<PolicyMemberEntity> allMembers = new ArrayList<>();
			for (RenewalPolicyTMPMemberEntity newTempMemberEntity : tempMemberEntity) {
				PolicyMemberEntity policyMemberEntity = null;
				if (newTempMemberEntity.getPmstMemebrId() != null) {
					policyMemberEntity = policyMemberRepository.findById(newTempMemberEntity.getPmstMemebrId()).get();
					historyMemberRepository.save(PolicyMemberHelper.policymastertohistoryupdate(policyMemberEntity,
							policyHistoryEntity.getId(), username));
				}
				if (newTempMemberEntity.getPmstMemebrId() == null) {
					licId = LicInc + licId.intValue();
					Long LicinLong = Long.valueOf(licId);
					newTempMemberEntity.setLicId(LicinLong.toString());
				}

				policyMemberEntity = RenewalPolicyTMPMemberHelper.updateTemptoPolicyMemberMaster(newTempMemberEntity,
						username);
				allMembers.add(policyMemberEntity);
			}
			policyMemberRepository.saveAll(allMembers);
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
					.updateTempToValuationMatrixMaster(renewalValuationMatrixTMPEntity, username);
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
					.updateTempToValuationBasicMaster(renewalValuationBasicTMPEntity, username);
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
			PolicyValuationWithdrawalRateEntity newPolicyValuationWithdrawalRateEntity = PolicyValuationMatrixHelper
					.updateTempToLifecoverMaster(addrenewalValuationWithdrawalTMPEntity, username);
			policyValuationWithdrawalRateEntities.add(newPolicyValuationWithdrawalRateEntity);
		}
		policyValuationWithdrawalRateRepository.saveAll(policyValuationWithdrawalRateEntities);

		// End

		return newmasterPolicyEntity;
	}


	@Transactional
	@Override
	public ApiResponseDto<PmstTmpMergerPropsDto> mergemembesourcetodes(PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {
		//no need to use this method, we need to do this approve
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(pmstTmpMergerPropsDto.getSourceTmpPolicyID()).get();
		RenewalPolicyTMPEntity desrenewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(pmstTmpMergerPropsDto.getDestinationTmpPolicyID()).get();
		Integer licId = renewalPolicyTMPMemberRepository.maxLicNumber(desrenewalPolicyTMPEntity.getId());
		int LicInc = 1;
		List<RenewalPolicyTMPMemberEntity> renewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository
				.findBytmpPolicyId(renewalPolicyTMPEntity.getId());

		List<RenewalPolicyTMPMemberEntity> saveAllFromSource = new ArrayList<>();

		for (RenewalPolicyTMPMemberEntity getRenewalPolicyTMPMemberEntity : renewalPolicyTMPMemberEntity) {
			if ((getRenewalPolicyTMPMemberEntity.getTmpPolicyId())
					.equals(pmstTmpMergerPropsDto.getSourceTmpPolicyID())) {
				RenewalPolicyTMPMemberEntity cloneEntity = CommonPolicyServiceHelper
						.cloneEntity(getRenewalPolicyTMPMemberEntity);
				String licIdFromPolicy = getRenewalPolicyTMPMemberEntity.getLicId();
				Long temp = Long.valueOf(licIdFromPolicy);
				int licIdNew = (int) (temp + licId.intValue());
				Long LicinLong = Long.valueOf(licIdNew);
				cloneEntity.setLicId(LicinLong.toString());
				cloneEntity.setRecordType(null);

//				RenewalPolicyTMPMemberEntity newRenewalPolicyTMPEntity = null;
				RenewalPolicyTMPMemberEntity newRenewalPolicyTMPEntity = CommonPolicyServiceHelper
						.copytomembersourcetodest(cloneEntity, desrenewalPolicyTMPEntity);
				saveAllFromSource.add(newRenewalPolicyTMPEntity);
				getRenewalPolicyTMPMemberEntity.setIsActive(Boolean.FALSE);
				saveAllFromSource.add(getRenewalPolicyTMPMemberEntity);
			}
		}
		renewalPolicyTMPMemberRepository.saveAll(saveAllFromSource);

		// Generate Valuation and Store Procedure Call need to Processing
//		gratuityCalculationRepository.calculateGratuityRenewal1(desrenewalPolicyTMPEntity.getId());
//		gratuityCalculationRepository.calculateGratuityRenewal2(desrenewalPolicyTMPEntity.getId());
//		gratuityCalculationRepository.calculateGratuityRenewaldes3(desrenewalPolicyTMPEntity.getId());

//		RenewalValuationBasicTMPEntity valuationBasicEntitySource = renewalValuationBasicTMPRepository
//				.findBytmpPolicyId(renewalPolicyTMPEntity.getId()).get();
//		Double modfdPremRateCrdbiltyFctr = 0.0;
//		if (valuationBasicEntitySource.getStdPremRateCrdbiltyFctr() != null
//				&& valuationBasicEntitySource.getStdPremRateCrdbiltyFctr() != 0) {
//			modfdPremRateCrdbiltyFctr = valuationBasicEntitySource.getStdPremRateCrdbiltyFctr();
//		} else {
//			modfdPremRateCrdbiltyFctr = valuationBasicEntitySource.getModfdPremRateCrdbiltyFctr();
//		}
//
//		if (valuationBasicEntitySource.getModfdPremRateCrdbiltyFctr() != null) {
//			gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
//					modfdPremRateCrdbiltyFctr.toString());
//			gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
//					modfdPremRateCrdbiltyFctr.toString());
//			gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
//					modfdPremRateCrdbiltyFctr.toString());
//			gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
//					modfdPremRateCrdbiltyFctr.toString());
//			gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "FLAT",
//					modfdPremRateCrdbiltyFctr.toString());
//		} else {
//			gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
//					valuationBasicEntitySource.getRateTable());
//			gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
//					valuationBasicEntitySource.getRateTable());
//			gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
//					valuationBasicEntitySource.getRateTable());
//			gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
//					valuationBasicEntitySource.getRateTable());
//			gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
//					valuationBasicEntitySource.getRateTable());
//		}

		// Start Grat Calculation
//		List<GratuityCalculationEntity> gratuityCalculationEntities = gratuityCalculationRepository
//				.findAllByMergerTmpMemberId(desrenewalPolicyTMPEntity.getId());
//
//		RenewalValuationBasicTMPEntity valuationBasicEntity = renewalValuationBasicTMPRepository
//				.findBytmpPolicyId(desrenewalPolicyTMPEntity.getId()).get();
//
//		Optional<RenewalValuationMatrixTMPEntity> valuationMatrixEntity = renewalValuationMatrixTMPRepository
//				.findBytmpPolicyId(desrenewalPolicyTMPEntity.getId());
//		RenewalValuationMatrixTMPEntity newValuationMatrixEntity = null;
//		if (valuationMatrixEntity.isPresent()) {
//			newValuationMatrixEntity = valuationMatrixEntity.get();
//			newValuationMatrixEntity.setModifiedBy(pmstTmpMergerPropsDto.getCreatedBy());
//			newValuationMatrixEntity.setModifiedDate(new Date());
//		} else {
//			newValuationMatrixEntity = new RenewalValuationMatrixTMPEntity();
//			newValuationMatrixEntity.setCreatedBy(pmstTmpMergerPropsDto.getCreatedBy());
//			newValuationMatrixEntity.setCreatedDate(new Date());
//		}
//
//		// calculate Valuation basic
//		Double accruedGratuity = 0.0D;
//		Double pastServiceDeath = 0.0D;
//		Double pastServiceWithdrawal = 0.0D;
//		Double pastServiceRetirement = 0.0D;
//		Double currentServiceDeath = 0.0D;
//		Double currentServiceWithdrawal = 0.0D;
//		Double currentServiceRetirement = 0.0D;
//		Double totalGratuity = 0.0D;
//
//		List<GratuityCalculationDto> getGratuityCalculationEntity = gratuityCalculationEntities.stream()
//				.map(this::gratuityCalculationEntityToDto).collect(Collectors.toList());
//		for (GratuityCalculationDto gratuityCalculationEntity : getGratuityCalculationEntity) {
//			if (gratuityCalculationEntity.getAccruedGra() != null)
//				accruedGratuity = accruedGratuity + gratuityCalculationEntity.getAccruedGra();
//			if (gratuityCalculationEntity.getPastServiceBenefitDeath() != null)
//				pastServiceDeath = pastServiceDeath + gratuityCalculationEntity.getPastServiceBenefitDeath();
//			if (gratuityCalculationEntity.getPastServiceBenefitWdl() != null)
//				pastServiceWithdrawal = pastServiceWithdrawal + gratuityCalculationEntity.getPastServiceBenefitWdl();
//			if (gratuityCalculationEntity.getPastServiceBenefitRet() != null)
//				pastServiceRetirement = pastServiceRetirement + gratuityCalculationEntity.getPastServiceBenefitRet();
//			if (gratuityCalculationEntity.getCurrentServiceBenefitDeath() != null)
//				currentServiceDeath = currentServiceDeath + gratuityCalculationEntity.getCurrentServiceBenefitDeath();
//			if (gratuityCalculationEntity.getCurrentServiceBenefitWdl() != null)
//				currentServiceWithdrawal = currentServiceWithdrawal
//						+ gratuityCalculationEntity.getCurrentServiceBenefitWdl();
//			if (gratuityCalculationEntity.getCurrentServiceBenefitRet() != null)
//				currentServiceRetirement = currentServiceRetirement
//						+ gratuityCalculationEntity.getCurrentServiceBenefitRet();
//			if (gratuityCalculationEntity.getTotalGra() != null)
//				totalGratuity = totalGratuity + gratuityCalculationEntity.getTotalGra();
//
//		}
//		valuationBasicEntity.setAccruedGratuity(valuationBasicEntity.getAccruedGratuity() + accruedGratuity);
//		valuationBasicEntity.setPastServiceDeath(valuationBasicEntity.getPastServiceDeath() + pastServiceDeath);
//		valuationBasicEntity
//				.setPastServiceWithdrawal(valuationBasicEntity.getPastServiceWithdrawal() + pastServiceWithdrawal);
//		valuationBasicEntity
//				.setPastServiceRetirement(valuationBasicEntity.getPastServiceRetirement() + pastServiceRetirement);
//		valuationBasicEntity
//				.setCurrentServiceDeath(valuationBasicEntity.getCurrentServiceDeath() + currentServiceDeath);
//		valuationBasicEntity.setCurrentServiceWithdrawal(
//				valuationBasicEntity.getCurrentServiceWithdrawal() + currentServiceWithdrawal);
//		valuationBasicEntity.setCurrentServiceRetirement(
//				valuationBasicEntity.getCurrentServiceRetirement() + currentServiceRetirement);
//		valuationBasicEntity.setTotalGratuity(valuationBasicEntity.getTotalGratuity() + totalGratuity);
//		renewalValuationBasicTMPRepository.save(valuationBasicEntity);
//
//		// Renewal Policy Member data Update Grat calculation
//		Double currentServiceLiability = 0.0D;
//		Double pastServiceLiability = 0.0D;
//		Double futureServiceLiability = 0.0D;
//		Double premium = 0.0D;
//		Double totalSumAssured = 0.0D;
//		Double gst = 0.0D;
//		if (renewalPolicyTMPEntity.getGstApplicableId().equals(1L)) {
//			StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
//			gst = Double.parseDouble(standardCodeEntity.getValue()) / 100;
//		}
//		new ArrayList<RenewalPolicyTMPMemberEntity>();
//		for (GratuityCalculationEntity gratuityCalculationEntity : gratuityCalculationEntities) {
//
//			RenewalPolicyTMPMemberEntity getMemberEntity = renewalPolicyTMPMemberRepository
//					.findById(gratuityCalculationEntity.getMemberId()).get();
//			totalSumAssured = totalSumAssured
//					+ (gratuityCalculationEntity.getLcSumAssured() != null ? gratuityCalculationEntity.getLcSumAssured()
//							: 0.0D);
//			premium = premium
//					+ (gratuityCalculationEntity.getLcPremium() != null ? gratuityCalculationEntity.getLcPremium()
//							: 0.0D);
//			currentServiceLiability = currentServiceLiability
//					+ (gratuityCalculationEntity.getCurrentServiceBenefitDeath() != null
//							? gratuityCalculationEntity.getCurrentServiceBenefitDeath()
//							: 0.0D)
//					+ (gratuityCalculationEntity.getCurrentServiceBenefitRet() != null
//							? gratuityCalculationEntity.getCurrentServiceBenefitRet()
//							: 0.0D)
//					+ (gratuityCalculationEntity.getCurrentServiceBenefitWdl() != null
//							? gratuityCalculationEntity.getCurrentServiceBenefitWdl()
//							: 0.0D);
//			pastServiceLiability = pastServiceLiability
//					+ (gratuityCalculationEntity.getPastServiceBenefitDeath() != null
//							? gratuityCalculationEntity.getPastServiceBenefitDeath()
//							: 0.0D)
//					+ (gratuityCalculationEntity.getPastServiceBenefitRet() != null
//							? gratuityCalculationEntity.getPastServiceBenefitRet()
//							: 0.0D)
//					+ (gratuityCalculationEntity.getPastServiceBenefitWdl() != null
//							? gratuityCalculationEntity.getPastServiceBenefitWdl()
//							: 0.0D);
//
//			getMemberEntity.setLifeCoverPremium(gratuityCalculationEntity.getLcPremium());
//			getMemberEntity.setLifeCoverSumAssured(gratuityCalculationEntity.getLcSumAssured());
//			getMemberEntity.setAccruedGratuity(gratuityCalculationEntity.getAccruedGra());
//			getMemberEntity.setTotalGratuity(gratuityCalculationEntity.getTotalGra());
//			getMemberEntity.setGratCalculationId(gratuityCalculationEntity.getId());
//			renewalPolicyTMPMemberRepository.save(getMemberEntity);
//		}
//		newValuationMatrixEntity.setValuationTypeId(1L);
//		newValuationMatrixEntity.setValuationDate(valuationBasicEntity.getValuationEffectivDate());
//		newValuationMatrixEntity.setCurrentServiceLiability(
//				newValuationMatrixEntity.getCurrentServiceLiability() + currentServiceLiability);
//		newValuationMatrixEntity
//				.setPastServiceLiability(newValuationMatrixEntity.getPastServiceLiability() + pastServiceLiability);
//		newValuationMatrixEntity.setFutureServiceLiability(
//				newValuationMatrixEntity.getFutureServiceLiability() + futureServiceLiability);
//		newValuationMatrixEntity.setPremium(newValuationMatrixEntity.getPremium() + premium);
//		newValuationMatrixEntity.setGst(newValuationMatrixEntity.getPremium() * gst);
//		newValuationMatrixEntity
//				.setTotalPremium(newValuationMatrixEntity.getPremium() + (newValuationMatrixEntity.getPremium() * gst));
//		newValuationMatrixEntity.setAmountPayable(newValuationMatrixEntity.getPremium()
//				+ (newValuationMatrixEntity.getPremium() * gst) + newValuationMatrixEntity.getCurrentServiceLiability()
//				+ newValuationMatrixEntity.getPastServiceLiability()
//				+ newValuationMatrixEntity.getFutureServiceLiability());
//		newValuationMatrixEntity.setAmountReceived(0.0D);
//		newValuationMatrixEntity.setBalanceToBePaid(
//				newValuationMatrixEntity.getAmountPayable() - newValuationMatrixEntity.getAmountReceived());
//		newValuationMatrixEntity.setTotalSumAssured(newValuationMatrixEntity.getTotalSumAssured() + totalSumAssured);
//
//		renewalValuationMatrixTMPRepository.save(newValuationMatrixEntity);
//
//		// create payments to be received against master quotation
//		Double oYRGTARenewalPremium = 0D;
//		Double lateFee = 0D;
//		Double lateFeeGSTAmount = 0D;
////				Float currentServiceLiability = 0F;
//		Double pastServiceLiabilty = 0D;
////				Float futureServiceLiability = 0F;
//		// Not Way Correct need to run store Procedure
//		List<GratuityCalculationEntity> gratuityCalculationEntity = gratuityCalculationRepository
//				.findAllByMergerTmpMemberId(desrenewalPolicyTMPEntity.getId());
//		for (GratuityCalculationEntity gratuityCalculationEntry : gratuityCalculationEntity) {
//			oYRGTARenewalPremium = oYRGTARenewalPremium
//					+ (gratuityCalculationEntry.getLcPremium() != null ? gratuityCalculationEntry.getLcPremium()
//							: 0.0D);
//			currentServiceLiability = currentServiceLiability
//					+ (gratuityCalculationEntry.getCurrentServiceBenefitDeath() != null
//							? gratuityCalculationEntry.getCurrentServiceBenefitDeath()
//							: 0.0D)
//					+ (gratuityCalculationEntry.getCurrentServiceBenefitRet() != null
//							? gratuityCalculationEntry.getCurrentServiceBenefitRet()
//							: 0.0D)
//					+ (gratuityCalculationEntry.getCurrentServiceBenefitWdl() != null
//							? gratuityCalculationEntry.getCurrentServiceBenefitWdl()
//							: 0.0D);
//			pastServiceLiabilty = pastServiceLiabilty
//					+ +(gratuityCalculationEntry.getPastServiceBenefitDeath() != null
//							? gratuityCalculationEntry.getPastServiceBenefitDeath()
//							: 0.0D)
//					+ (gratuityCalculationEntry.getPastServiceBenefitRet() != null
//							? gratuityCalculationEntry.getPastServiceBenefitRet()
//							: 0.0D)
//					+ (gratuityCalculationEntry.getPastServiceBenefitWdl() != null
//							? gratuityCalculationEntry.getPastServiceBenefitWdl()
//							: 0.0D);
//
////					futureServiceLiability = futureServiceLiability
////							+ (gratuityCalculationEntity.getTerm() != null ? gratuityCalculationEntity.getTerm() : 0.0F);
//		}
//
//		Double gstAmount = 0.0D;
//		if (renewalPolicyTMPEntity.getGstApplicableId().equals(1L)) {
//			StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
//			gstAmount = oYRGTARenewalPremium * (Float.parseFloat(standardCodeEntity.getValue()) / 100);
//		}
//
//		List<QuotationChargeEntity> quotationChargeEntities = new ArrayList<QuotationChargeEntity>();
//
//		// Need to change ID charge TypeID based on Condition
//		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(desrenewalPolicyTMPEntity.getId())
//				.chargeTypeId(215L).amountCharged(oYRGTARenewalPremium).balanceAmount(0).isActive(true)
//				.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());
//		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(desrenewalPolicyTMPEntity.getId())
//				.chargeTypeId(216L).amountCharged(gstAmount).balanceAmount(0).isActive(true)
//				.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());
//
//		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(desrenewalPolicyTMPEntity.getId())
//				.chargeTypeId(220L).amountCharged(lateFee).balanceAmount(0).isActive(true)
//				.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());
//		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(desrenewalPolicyTMPEntity.getId())
//				.chargeTypeId(217L).amountCharged(lateFeeGSTAmount).balanceAmount(0).isActive(true)
//				.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());
//
//		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(desrenewalPolicyTMPEntity.getId())
//				.chargeTypeId(218L).amountCharged(pastServiceLiabilty).balanceAmount(0).isActive(true)
//				.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());
//		quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(desrenewalPolicyTMPEntity.getId())
//				.chargeTypeId(219L).amountCharged(currentServiceLiability).balanceAmount(0).isActive(true)
//				.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());
//
////				quotationChargeEntities.add(QuotationChargeEntity.builder().masterQuotationId(renewalPolicyTMPEntity.getId())
////						.chargeTypeId(145L).amountCharged(futureServiceLiability).balanceAmount(0).isActive(true)
////						.createdBy(username).createdDate(new Date()).build());
//		quotationChargeRepository.saveAll(quotationChargeEntities);
//
//		RenewalValuationTMPHelper.entityToDtoTemPolicy(renewalPolicyTMPEntity);

		return null;
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
	
	@Value("${temp.pdf.location}")
	private String tempPdfLocation;
	
	@Override
	public String generateReport(Long policyId, String reportType) throws IOException {

		InputStream islicLogo = new ClassPathResource("liclogo.png").getInputStream();
		// Uncomment below line to add watermark to the pdf
		// InputStream islicWatermark = new
		// ClassPathResource("watermark.png").getInputStream();

		byte[] bytesLogo = IOUtils.toByteArray(islicLogo);
		// Uncomment below line to add watermark to the pdf
		// byte[] bytesWatermark = IOUtils.toByteArray(islicWatermark);

		String licLogo = Base64.getEncoder().encodeToString(bytesLogo);
		// Uncomment below line to add watermark to the pdf
		// String licWatermark = Base64.getEncoder().encodeToString(bytesWatermark);

		boolean showLogo = true;
		String reportBody = "";
		String reportStyles = "";
		String htmlContent2 = "</div></body></html>"; // DO NOT DISTRUB THIS LINE

		if (reportType.equals("Adjustmentvoucher")) {
			showLogo = false;
			reportBody = generateAdjustment(reportType,licLogo) + htmlContent2;

		} 


		String completehtmlContent = "<!DOCTYPE  html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>LIC ePGS</title><meta name=\"author\" content=\"LIC PNGS\"/><meta name=\"keywords\" content=\"Customer Communication\"/><meta name=\"description\" content=\"Report/Letter/Contract\"/><style type=\"text/css\"> body{border-width:0px;\r\n"
				+ "border-style:solid;\r\n" + "border-color:#586ec5;} * {margin:0; padding:0; text-indent:0; }"
				+ " .s1 { color: #2E5396; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 16pt; }"
				+ " .s2 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " p { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; }"
				+ " .a { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s3 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s5 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s6 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s7 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s8 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; vertical-align: -2pt; }"
				+ " .s9 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " h1 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s10 { color: #2D3499; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .pt10 { padding-top: 10pt; }" + " .pb10 { padding-bottom: 10pt; }"
				+ " .pb50 { padding-bottom: 50pt; }"
				+ " table, tbody, td {vertical-align: top; overflow: visible; color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; } "
				// Uncomment below line to add watermark to the pdf
				// + " .watermark {\r\n background-image: url(\"data:image/png;base64," +
				// licWatermark + "\");} "
				+ " " + reportStyles + " " + " </style></head><body><div id=\"bg\" class=\"watermark\">"
				+ "<p style=\"padding-left: 5pt;text-indent: 0pt;text-align: left;\"/>";

		if (showLogo) {
			completehtmlContent = completehtmlContent
					+ "<p style=\"text-indent: 0pt;text-align: center;\"><span><table style=\"width:100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr style=\"display:flex;justify-content:space-between;align-items:center\"><td style=\"padding-left:24pt\"><img width=\"100\" height=\"63\" src=\"data:image/jpg;base64,"
					+ licLogo + "\"/></td>" + "</tr></table></span></p>";
		}
		completehtmlContent = completehtmlContent + "<p style=\"text-indent: 0pt;text-align: left;\"><br/></p>"
				+ reportBody + "";

		String htmlFileLoc = tempPdfLocation + "/" + policyId + ".html";

		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();

		FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + policyId + ".pdf");
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(new File(htmlFileLoc));
		renderer.layout();
		try {
			renderer.createPDF(fileOutputStream, false);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		renderer.finishPDF();

		if (new File(htmlFileLoc).exists()) {
			new File(htmlFileLoc).delete();
		}

		return tempPdfLocation + "/" + policyId + ".pdf";
		}

	
	private String generateAdjustment(String reportType, String licLogo) {
		PolicyAdjustmentPDFDto spPdfDto = new PolicyAdjustmentPDFDto();
		String reportBody1 = "<table style=\"width:100%;\">" + "<tr>" + "<td style=\"width:25%;\">" + "</td>"
				+ "<td style=\"width:50%;text-align:center;\">"
				+ "<img width=\"100\" height=\"63\" src=\"data:image/jpg;base64," + licLogo + "\"/>" + "</td>"
				+ "<td style=\"width:25%;vertical-align: bottom;\">"
				+ "<div style=\"padding:3pt;border:1pt;border-style:solid;border-color:#000;text-align:center;\">ADJUSTMENT VOUCHER</div>"
				+ "</td>" + "</tr>" + "</table>" + "<table style=\"width:100%;\">" + "<tr>"
				+ "<td style=\"width:70%;\">P &amp; GS Dept. @PNGSDEPT</td>"
				+ "<td style=\"width:30%;\">Voucher No. @VOUCHERNO</td>" + "</tr>" + "<tr>"
				+ "<td>Divl. Name: @DIVLNAME</td>" + "<td>Date: @VOUCHERDATE</td>" + "</tr>" + "</table>"
				+ "<p class=\"pb10\" style=\"text-align:center;\">To Cash / Banking Section</p>"
				+ "<p class=\"pb10\">Please adjust / Issue Crossed / Order Cheque / M O / Postage Stamps favouring: @FAVOURING</p>"
				+ "<p class=\"pb10\">@AMOUNTINWORDS</p>" + "<table style=\"width:100%;\">" + "<tr>"
				+ "<td style=\"width:50%;padding-bottom:10pt;\">Policy No.: @POLICYNUMBER</td>"
				+ "<td style=\"width:50%;\">Scheme Name: @SCHEMENAME</td>" + "</tr>" + "</table>"
				
				+ "<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\" >"
				+ "<tr>" + "<td style=\"width=25%\">Head of Account</td>" + "<td style=\"width=25%\">Code</td>" + "<td style=\"width=25%\">Debit Amount</td>"
				+ "<td style=\"width=25%\">Credit Amount</td></tr>"
				+ "<tr>" + "<td style=\"width=25%\">Deposit Policy</td>" + "<td style=\"width=25%\"></td>" + "<td style=\"width=25%\"></td>"
				+ "<td style=\"width=25%\"></td></tr>"
				
				+ "<tr>" + "<td style=\"width=25%\">Single Premium</td>" + "<td style=\"width=25%\"></td>" + "<td style=\"width=25%\"></td>"
				+ "<td style=\"width=25%\"></td></tr>"
				
				+ "<tr>" + "<td style=\"width=25%\">Single Premium Test</td>" + "<td style=\"width=25%\"></td>" + "<td style=\"width=25%\"></td>"
				+ "<td style=\"width=25%\"></td></tr>"
				
				+ "<tr>" + "<td style=\"width=50%\" colspan =\"2\">Total</td>" + "<td style=\"width=25%\"></td>"
				+ "<td style=\"width=25%\"></td></tr>"
				+"</table>";

		
//				class=\"tableborder\" callpadding=\"4\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\"
				
		BigDecimal debitTotal = BigDecimal.ZERO;
		BigDecimal creditTotal = BigDecimal.ZERO;
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		String detailTable = "";
		for (AdjustmentVoucherDetailDto adjustmentVoucherDetailDto : spPdfDto.getVoucherDetail()) {
			detailTable += "<tr>" + "<td class=\"rightborder\">" + adjustmentVoucherDetailDto.getHeadOfAccount()
					+ "</td>" + "<td class=\"rightborder\">" + adjustmentVoucherDetailDto.getCode() + "</td>"
					+ "<td class=\"tdrightalign rightborder\">"
					+ formatter.format(adjustmentVoucherDetailDto.getDebitBigdecimal()) + "</td>"
					+ "<td class=\"tdrightalign\">" + formatter.format(adjustmentVoucherDetailDto.getCreditBigDecimal())
					+ "</td>" + "</tr>";
			debitTotal = debitTotal.add(adjustmentVoucherDetailDto.getDebitBigdecimal());
			creditTotal = creditTotal.add(adjustmentVoucherDetailDto.getCreditBigDecimal());
		}

		// to adjust min height
		for (int i = 1; i <= 5; i++) {
			detailTable += "<tr>" + "<td class=\"rightborder\"></td>" + "<td class=\"rightborder\"></td>"
					+ "<td class=\"tdrightalign rightborder\"></td>" + "<td class=\"tdrightalign\"></td>" + "</tr>";
		}
		detailTable += "<tfoot>" + "<tr>" + "<td colspan=\"2\">Total</td>" + "<td class=\"tdrightalign\">"
				+ formatter.format(debitTotal) + "</td>" + "<td class=\"tdrightalign\">" + formatter.format(creditTotal)
				+ "</td>" + "</tr>" + "</tfoot>" ;

		String summaryTable = "<p style=\"padding-top:10pt;\">As per the following details</p>"
				+"<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
				+"<tr>"
				+"<td>"
				+"<table width=\"100%\">"
				
				+"<tr>"
				+"<td colspan=\"10\">"
				+"Regular Deposit Adjustment"
				+"</td>"
				+"</tr>"
				
				+"<tr>"
				+"<td >"
				+"Balance Deposit: .00"
				+"</td>"
				+"</tr>"
				
				+"<tr colspan=\"10\">"
				+"<td width=\"13%\">"
				+"ARD: "
				+"</td>"
				+"<td colspan=\"2\" width=\"13%\">"
				+"01/04/2023"
				+"</td>"
				+"<td colspan=\"2\" width=\"13%\" >"
				+"Mode:"
				+"</td>"
				+"<td colspan=\"2\" width=\"13%\">"
				+"Cash"
				+"</td>"
				+"<td colspan=\"2\" width=\"13%\">"
				+"Due For:"
				+"</td>"
				+"<td colspan=\"2\" width=\"13%\">"
				+"Next Year"
				+"</td>"
				+"</tr>"
				

				+"<tr>"
				+"<td colspan=\"10\">"
				+"Details Adjustment"
				+"</td>"
				+"</tr>"

				+"<tr colspan=\"10\">"
				+"<td width=\"5%\">"
				+"NO"
				+"</td>"
				+"<td width=\"5%\">"
				+""
				+"</td>"
				+"<td width=\"5%\">"
				+"Date"
				+"</td>"
				+"<td width=\"5%\">"
				+""
				+"</td>"
				+"<td width=\"5%\">"
				+"Amount"
				+"</td>"
				+"<td width=\"5%\">"
				+""
				+"</td>"
				+"<td width=\"5%\">"
				+"Adjusted"
				+"</td>"
				+"<td width=\"5%\">"
				+""
				+"</td>"
				+"<td width=\"5%\">"
				+"Balance"
				+"</td>"
				+"<td width=\"5%\">"
				+""
				+"</td>"
				+"</tr>"

				+"</table>"
				+"</td>"
				+"</tr>"
				+"</table>";
//				+ "<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"1\" cellspacing=\"0\" >";
		// to adjust min height
//		for (int i = 1; i <= 1; i++) {
//			summaryTable += "<tr><td></td></tr>";
//		}
//
//		summaryTable += "<tr><td><p class=\"pb10\"> @ADJUSTMENTTYPELABEL</p><p>Balance Deposit: "
//				+ formatter.format(spPdfDto.getBalanceDeposit()) + "</p></td></tr>";
//		summaryTable += "<tr><td><table style=\"width:75%;\"><tr>";
//		summaryTable += "<td>ARD: @ANNUALRENEWALDATE</td>";
//		summaryTable += "<td>Mode: @MODE</td>";
//		summaryTable += "<td>Due For: @DUEFOR</td>";
//		summaryTable += "</tr></table></td></tr>";
//		summaryTable += "<tr><td><p>Details of Adjustment</p></td></tr>";
//		summaryTable += "<table style=\"width:100%;\">";
//		summaryTable += "<tr><td class=\"bottomborder\">No</td><td class=\"bottomborder\">Date</td><td class=\"bottomborder tdrightalign\">Amount</td><td class=\"bottomborder tdrightalign\">Adjusted</td><td class=\"bottomborder tdrightalign\">Balance</td></tr>";

		
		
//		for (SupplementaryAdjustmentDto supplementaryAdjustmentDto : spPdfDto.getSupplymentary()) {
//			summaryTable += "<tr>" + "<td>" + supplementaryAdjustmentDto.getDetailsNo() + "</td>" + "<td>"
//					+ supplementaryAdjustmentDto.getDetailsDate() + "</td>" + "<td class=\"tdrightalign\">"
//					+ formatter.format(supplementaryAdjustmentDto.getDetailsAmount()) + "</td>"
//					+ "<td class=\"tdrightalign\">" + formatter.format(supplementaryAdjustmentDto.getDetailsAdjusted())
//					+ "</td>" + "<td class=\"tdrightalign\">"
//					+ formatter.format(supplementaryAdjustmentDto.getDetailsBalance()) + "</td>" + "</tr>";
//		}
//
//		summaryTable += "</table>";
//		// to adjust min height
//		for (int i = 1; i <= 1; i++) {
//			summaryTable += "<tr><td></td></tr>";
//		}
//		summaryTable += "</table>";

		String footerTable = "<p class=\"pb10\">&nbsp;</p>" + "<table style=\"width:100%;\">" + "<tr>"
				+ "<td style=\"width:50%\">" + "<p class=\"signaturepadding\">Prepared By: " + (spPdfDto.getPreparedBy()!=null ? spPdfDto.getPreparedBy() : "")
				+ "</p>" + "<p class=\"signaturepadding\">Checked By: " + (spPdfDto.getCheckedBy()!=null ? spPdfDto.getCheckedBy() : "")  + "</p>"
				+ "<p class=\"pb10\">Date: " + spPdfDto.getDate() + "</p>" + "<p class=\"pb10\">Cheque No: "
				+ (spPdfDto.getChequeNumber()!=null ? spPdfDto.getChequeNumber() : "") + "</p>" + "<p>Drawn on: " + (spPdfDto.getDrawn()!=null ? spPdfDto.getDrawn() : "") + "</p>" + "</td>"
				
				+ "<td>" + "<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\" >" + "<tr>" + "<td>"
				+ "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\">"
				+ "<tr>" + "<th>" + "<p class=\"pb10\" style=\"text-align:center;\">PAY</p>"
				+ "<p class=\"pb10\">Sign:</p>" + "<p>Date:" + (spPdfDto.getPayDate()!=null ? spPdfDto.getPayDate() : "") + "</p>" + "</th>" + "<th>"
				+ "<p class=\"pb10\" style=\"text-align:center;\">PAID</p>" + "<p class=\"signaturepadding\">Sign:</p>"
				+ "<p>Date:" + (spPdfDto.getPaidDate()!=null ? spPdfDto.getPaidDate() : "") + "</p>" + "</th>" + "</tr>" + "<tr>" + "<th colspan=\"2\">"
				+"<hr style=\"border: 1px solid black;\"></hr>"
				+ "<p class=\"pt10\" style=\"text-align:center;\">Initials of Officers Signing cheques</p>"
				+ "</th>"
				+ "</tr>" + "</table>" + "</td>" + "</tr>" + "</table>" + "</td>" + "</tr>" + "</table>";

		String adjustmenttypeLabel = new String();
		if (reportType.equalsIgnoreCase("regularAdjustment")) { 
			adjustmenttypeLabel = "Regular Deposit Adjustment";
		} else if (reportType.equalsIgnoreCase("subsequentAdjustment")) {
			adjustmenttypeLabel = "Supplementary Deposit Adjustment";
		} else if (reportType.equalsIgnoreCase("NB")) {
			adjustmenttypeLabel = "New Business Deposit Adjustment";
		}

		String mode = null;
		if (spPdfDto.getMode() != null) {
			if ("1".equals(spPdfDto.getMode())) {
				mode = "Monthly";
			} else if ("2".equals(spPdfDto.getMode())) {
				mode = "Quarterly";
			} else if ("4".equals(spPdfDto.getMode())) {
				mode = "Half-Yearly";
			} else if ("3".equals(spPdfDto.getMode())) {
				mode = "Yearly";
			}
		} else {
			mode = "";
		}

		String reportBody = reportBody1 + detailTable + summaryTable + footerTable;
		reportBody = reportBody.replaceAll("@ADJUSTMENTTYPELABEL", adjustmenttypeLabel)
				.replaceAll("@PNGSDEPT", spPdfDto.getPngsDept() != null ? spPdfDto.getPngsDept() : "PNGSDEPT")
				.replaceAll("@VOUCHERNO", spPdfDto.getVoucherNumber() != null ? spPdfDto.getVoucherNumber() : "")
				.replaceAll("@DIVLNAME", spPdfDto.getDivisionalName() != null ? spPdfDto.getDivisionalName() : "")
				.replaceAll("@VOUCHERDATE", spPdfDto.getVoucherDate() != null ? spPdfDto.getVoucherDate() : "")
				.replaceAll("@FAVOURING", spPdfDto.getFavouringName() != null ? spPdfDto.getFavouringName() : "")
				.replaceAll("@AMOUNTINWORDS", spPdfDto.getAmountInWords() != null ? spPdfDto.getAmountInWords() : "")
				.replaceAll("@POLICYNUMBER", spPdfDto.getPolicyNumber() != null ? spPdfDto.getPolicyNumber() : "")
				.replaceAll("@SCHEMENAME", spPdfDto.getSchemeName() != null ? spPdfDto.getSchemeName() : "")
				.replaceAll("@BALANCEDEPOSIT",
						spPdfDto.getBalanceDeposit() != null
								? NumericUtils.convertBigDecimalToString(spPdfDto.getBalanceDeposit())
								: "")
				.replaceAll("@ANNUALRENEWALDATE", spPdfDto.getArd() != null ? spPdfDto.getArd() : "")
				.replaceAll("@MODE", mode != null ? mode : "")
				.replaceAll("@DUEFOR", spPdfDto.getDuedate() != null ? spPdfDto.getDuedate() : "")
				.replaceAll("@VOUCHERNO", spPdfDto.getVoucherNumber() != null ? spPdfDto.getVoucherNumber() : "")
				.replaceAll("@VOUCHERDATE", spPdfDto.getVoucherDate() != null ? spPdfDto.getVoucherDate() : "")
				.replaceAll("@PREPAREDBY", spPdfDto.getPreparedBy() != null ? spPdfDto.getPreparedBy() : "");
		return reportBody.replace("\\", "").replaceAll("\t", "");
		}

	@Override
	public String premiumreceipt(Long policyId) throws IOException {
	
			InputStream islicLogo = new ClassPathResource("liclogo.png").getInputStream();
			// Uncomment below line to add watermark to the pdf
			// InputStream islicWatermark = new
			// ClassPathResource("watermark.png").getInputStream();

			byte[] bytesLogo = IOUtils.toByteArray(islicLogo);
			// Uncomment below line to add watermark to the pdf
			// byte[] bytesWatermark = IOUtils.toByteArray(islicWatermark);

			String licLogo = Base64.getEncoder().encodeToString(bytesLogo);
			// Uncomment below line to add watermark to the pdf
			// String licWatermark = Base64.getEncoder().encodeToString(bytesWatermark);
//			List<AgeValuationReportDto> ageValuationReportDto = new ArrayList<AgeValuationReportDto>();
//			List<Object[]> getAgeReport = policyRepository.getAgeReport(masterpolicyId);

//			ageValuationReportDto = PolicyHelper.valuationObjtoDto(getAgeReport);
			boolean showLogo = true;
			String reportBody = "";
			String reportStyles = "";
			String htmlContent2 = "</div></body></html>";
			
			reportBody = premiumadjustmentvoucher() + htmlContent2;

			String completehtmlContent = "<!DOCTYPE  html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
					+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>LIC ePGS</title><meta name=\"author\" content=\"LIC PNGS\"/><meta name=\"keywords\" content=\"Customer Communication\"/><meta name=\"description\" content=\"Report/Letter/Contract\"/><style type=\"text/css\"> body{border-width:0px;\r\n"
					+ "border-style:solid;\r\n" + "border-color:#586ec5;} * {margin:0; padding:0; text-indent:0; }"
					+ " .s1 { color: #2E5396; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 16pt; }"
					+ " .s2 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
					+ " p { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; }"
					+ " .a { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
					+ " .s3 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
					+ " .s5 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
					+ " .s6 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
					+ " .s7 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
					+ " .s8 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; vertical-align: -2pt; }"
					+ " .s9 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
					+ " h1 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
					+ " .s10 { color: #2D3499; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
					+ " .pt10 { padding-top: 10pt; }" + " .pb10 { padding-bottom: 10pt; }"
					+ " .pb50 { padding-bottom: 50pt; }"
					+ " table, tbody, td {vertical-align: top; overflow: visible; color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; } "
					// Uncomment below line to add watermark to the pdf
					// + " .watermark {\r\n background-image: url(\"data:image/png;base64," +
					// licWatermark + "\");} "
					+ " " + reportStyles + " " + " </style></head><body><div id=\"bg\" class=\"watermark\">"
					+ "<p style=\"padding-left: 5pt;text-indent: 0pt;text-align: left;\"/>";

			if (showLogo) {
				completehtmlContent = completehtmlContent
						+ "<p style=\"text-indent: 0pt;text-align: center;\"><span><table style=\"width:100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr style=\"display:flex;justify-content:space-between;align-items:center\"><td style=\"padding-left:24pt\"><img width=\"100\" height=\"63\" src=\"data:image/jpg;base64,"
						+ licLogo + "\"/></td>" + "</tr></table></span></p>";
			}
			completehtmlContent = completehtmlContent + "<p style=\"text-indent: 0pt;text-align: left;\"><br/></p>"
					+ reportBody + "";

			String htmlFileLoc = tempPdfLocation + "/" + policyId + ".html";
			FileWriter fw = new FileWriter(htmlFileLoc);
			fw.write(completehtmlContent);
			fw.close();

			FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + policyId + ".pdf");
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(new File(htmlFileLoc));
			renderer.layout();
			try {
				renderer.createPDF(fileOutputStream, false);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			renderer.finishPDF();

			if (new File(htmlFileLoc).exists()) {
				new File(htmlFileLoc).delete();
			}

			return tempPdfLocation + "/" + policyId + ".pdf";
		}
			private String premiumadjustmentvoucher() {
		String reportBody =	"<p style=\"text-align:center;\">PENSION AND GROUP SCHEME DEPARTMENT</p>"
				    +"<p>&nbsp;</p>"
				    +"<table style=\"width: 150%;\">\r\n"
				    + "  <tr><td>Date of Adjustment:</td>\r\n"
				    + "      <td class=\"alignRight\">Servicing Unit:</td></tr>\r\n"
				    + "</table>"
				    +"<table style=\"width: 150%;\">\r\n"
				    + "  <tr><td>Adjustment No:</td>\r\n"
				    + "      <td class=\"alignRight\">Address of Unit:</td></tr>\r\n"
				    + "</table>"
				    +"<table style=\"width: 150%;\">\r\n"
				    + "  <tr><td>Unit GST Number:</td>\r\n"
				    + " </tr>"
				    + "</table>"
					+ "<p style=\"text-align:center;\">Final Premium Receipt</p>"
					 +"<table style=\"width: 180%;\">\r\n"
					    + "  <tr><td>MPH Name</td>\r\n"
					    + "      <td class=\"alignRight\">Mode of Collection of Deposit:</td></tr>\r\n"
					    + "</table>"
						+"<p>&nbsp;</p>";
		
					String table="<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
					+"<tr style=\"text-align: center; vertical-align: middle;\">"
					+"<td style=\"width=16.70%\">Policy no.</td>"
					+"<td style=\"width=16.70%\">&lt;max 13 digit &gt;</td>"
					+"<td style=\"width=16.70%\">UIN</td>"
					+"<td style=\"width=16.70%\">&lt;max 13 char/digit &gt;</td>"
					+"<td style=\"width=16.70%\">Name of Plan</td>"
					+"<td style=\"width=16.70%\">23467645</td>"
					+"</tr>"
					+"</table>"
						
					+"<p>&nbsp;</p>";
					
				    String table1="<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
						    +"<tr style=\"text-align: center; vertical-align: middle;\">"
						      +"<td style=\"width=16.70%\">Deposit Nos</td>"
						      +"<td style=\"width=16.70%\">Deposit Date</td>"
						      +"<td style=\"width=16.70%\">Deposit Amount</td>"
						      +"<td style=\"width=16.70%\">Deposit Nos.</td>"
						      +"<td style=\"width=16.70%\">Deposit Date</td>"
						      +"<td style=\"width=16.70%\">Deposit Amount</td>"
						    +"</tr>"
						    +"<tr>"
						      +"<td style=\"width=16.70%\">1.</td>"
						      +"<td style=\"width=16.70%\">dd/mm/yyyy</td>"
						      +"<td style=\"width=16.70%\">&lt;max 14 digit&gt;</td>"
						      +"<td style=\"width=16.70%\">4.</td>"
						      +"<td style=\"width=16.70%\"></td>"
						      +"<td style=\"width=16.70%\"></td>"
						    +"</tr>"
						    +"<tr>"
						      +"<td style=\"width=16.70%\">2.</td>"
						      +"<td style=\"width=16.70%\"></td>"
						      +"<td style=\"width=16.70%\"></td>"
						      +"<td style=\"width=16.70%\">5.</td>"
						      +"<td style=\"width=16.70%\"></td>"
						      +"<td style=\"width=16.70%\"></td>"
						    +"</tr>"
						    +"<tr>"
						      +"<td style=\"width=16.70%\">3.</td>"
						      +"<td style=\"width=16.70%\"></td>"
						      +"<td style=\"width=16.70%\"></td>"
						      +"<td style=\"width=16.70%\">6.</td>"
						      +"<td style=\"width=16.70%\"></td>"
						      +"<td style=\"width=16.70%\"></td>"
						    +"</tr>"
						  +"</table>"	
				   + "<p>&nbsp;</p>";
				    
				    String reportBody1= "<table style=\"width: 120%;\">\r\n"
						    + "  <tr><td>Annual Renewal Date: (dd/mm/yyyy)</td>\r\n"
						    + "      <td class=\"alignRight\">Premium Adjustment For: (dd/mm/yyyy)</td></tr>\r\n"
						    + "</table>"
				    +"<table style=\"width: 120%;\">\r\n"
				    + "  <tr><td>Next Premium Due: (dd/mm/yyyy)</td>\r\n"
				    + "      <td class=\"alignRight\">Premium Mode: &lt;Mode/Fequency&gt;</td></tr>\r\n"
				    + "</table>";
				    
				    
				    String table3="<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
				    +"<tr style=\"text-align: center; vertical-align: middle;\">"
				    +"<td style=\"width=20%\">Description</td>"
				    +"<td style=\"width=20%\">PREMIUM &lt; max. 13 digits with 2 decimal &gt; </td>"
				    +"<td style=\"width=60%\" colspan=\"5\"> GST &lt; max 11digit with 2 decimals &gt; </td>"
				  +"</tr>"
				  +"<tr style=\"text-align: center; vertical-align: middle;\">"
				    +"<td style=\"width=20%\"></td>"
				    +"<td style=\"width=20%\"> </td>"
				    +"<td style=\"width=12%\">CGST</td>"
				    +"<td style=\"width=12%\">SGST</td>"
				    +"<td style=\"width=12%\">UGST</td>"
				    +"<td style=\"width=12%\">IGST</td>"
				    +"<td style=\"width=12%\">TOTAL</td>"
				  +"</tr>"  
				  +"<tr style=\"text-align: center; vertical-align: middle;\">"
				    +"<td style=\"width=20%\">TERM\r\n"
				    + "\r\n"
				    + "INSURANCE</td>"
				    +"<td style=\"width=20%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				  +"</tr>"
				  +"<tr style=\"text-align: center; vertical-align: middle;\">"
				    +"<td style=\"width=20%\">ANNUITY\r\n"
				    + "\r\n"
				    + "PURCHASE</td>"
				    +"<td style=\"width=20%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				  +"</tr>"
				  +"<tr style=\"text-align: center; vertical-align: middle;\">"
				  +"<td style=\"width=20%\">LATE FEE ONPREMIUM</td>"
				    +"<td style=\"width=20%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				  +"</tr>"
				    +"<tr style=\"text-align: center; vertical-align: middle;\">"
				  +"<td style=\"width=20%\">OTHERS\r\n"
				    + "</td>"
				    +"<td style=\"width=20%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				  +"</tr>"
				  +"<tr style=\"text-align: center; vertical-align: middle;\">"
				  +"<td style=\"width=20%\">TOTAL\r\n"
				    + "</td>"
				    +"<td style=\"width=20%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				    +"<td style=\"width=12%\"></td>"
				  +"</tr>"
				+"</table>"
							
				    +"<p>&nbsp;</p>";
				    
				    String body="<p>Address of the policy holder: &lt; &gt; , &lt; &gt; </p>"
				    		
				    		+"<p>&nbsp;</p>"
				    
					+"<p>    GST no. of Customer: </p>"
					 
					+"<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
				    +"<tr>"
				    +"<td style=\"width=30%\">Total Premium Amount</td>"
				    +"<td style=\"width=70%\">Rs. &lt; Numeric &gt; &amp; &lt; In words &gt; </td>"
				    +"</tr>"
				    +"</table>"
				    
				+"<p>This Final premium receipt is electronically generated on &lt; date (dd/mm/yyyy) &gt; and does not </p>"
				+"<p>require signature.</p>"
				+"<p>{If premium amount  Rs. 5000 and other than Mudrank.</p>"
				+"<p>The Physical receipt with revenue stamp will be issued shortly.}will not come in print</p>"
				+"<p>&nbsp;</p>"
				+"<p style=\"text-align:right;\">Mudrank</p>"
				+"<p>&nbsp;</p>"
				+"<p style=\"text-align:right;\">Revenue Stamp</p>"		
				+"<p>&nbsp;</p>";
				    
				    String table4="<table align=\"right\" width=\"5%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
							+"<tr>"
							+"<td > &nbsp;&nbsp; </td>" 
							+"</tr>"
							+"</table>";
			return reportBody+table+table1+reportBody1+table3+body+table4;
		}

			
			
			
			
			@Override
			public ApiResponseDto<PmstTmpMergerPropsDto> gettmpMemberPropsDetails(Long mergerid) {
				PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity = pmstTmpMergerPropsRepository.findById(mergerid).get();
				PmstTmpMergerPropsDto responseDto=CommonPolicyServiceHelper.entitytoDto(pmstTmpMergerPropsEntity);
				return ApiResponseDto.created(responseDto);				
			}



			@Override
			public ApiResponseDto<PmstTmpMergerPropsDto> updateid(PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {
				pmstTmpMergerPropsRepository.findById(pmstTmpMergerPropsDto.getId()).get();
					PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity  = new PmstTmpMergerPropsEntity();
					pmstTmpMergerPropsEntity .setStatusID(pmstTmpMergerPropsDto.getStatusID());
					pmstTmpMergerPropsEntity.setCreatedBy(pmstTmpMergerPropsDto.getCreatedBy());
					pmstTmpMergerPropsEntity.setCreatedDate(new Date());
					pmstTmpMergerPropsEntity.setDestinationPriorFundValue(pmstTmpMergerPropsDto.getDestinationPriorFundValue());
					pmstTmpMergerPropsEntity.setDestinationPriorTotalValue(pmstTmpMergerPropsDto.getDestinationPriorTotalValue());
					pmstTmpMergerPropsEntity.setDestinationTmpPolicyID(pmstTmpMergerPropsDto.getDestinationTmpPolicyID());
					pmstTmpMergerPropsEntity.setIsActive(pmstTmpMergerPropsDto.getIsActive());
					pmstTmpMergerPropsEntity.setMergerRequestDate(new Date());
					pmstTmpMergerPropsEntity.setMergerRequestNumber(pmstTmpMergerPropsDto.getMergerRequestNumber());
					pmstTmpMergerPropsEntity.setMergerType(pmstTmpMergerPropsDto.getMergerType());
					pmstTmpMergerPropsEntity.setModifiedBy(pmstTmpMergerPropsDto.getModifiedBy());		
					pmstTmpMergerPropsEntity.setModifiedDate(new Date());
					pmstTmpMergerPropsEntity.setSourceAccruedInterest(pmstTmpMergerPropsDto.getSourceAccruedInterest());
					pmstTmpMergerPropsEntity.setSourcePriorTotalFund(pmstTmpMergerPropsDto.getSourcePriorTotalFund());
					pmstTmpMergerPropsEntity.setSourceTmpPolicyID(pmstTmpMergerPropsDto.getSourceTmpPolicyID());
					pmstTmpMergerPropsEntity = pmstTmpMergerPropsRepository.save(pmstTmpMergerPropsEntity);

					return ApiResponseDto.created(CommonPolicyServiceHelper.entitytoDto(pmstTmpMergerPropsEntity));
			
			}
			
//general refund calculation
			@Override
			public ApiResponseDto<GratuityCalculationsDto> generalRefundCalculation(GratuityCalculationsDto gratitutyCalculationDto) {			
				MasterPolicyEntity masterPolicyEntity=masterPolicyCustomRepository.findById(gratitutyCalculationDto.getPmstPolicyId());
			
				Long months = 12l;
				Double totalGST=0.0;
				String ard=masterPolicyEntity.getAnnualRenewlDate().toString();
					Double lifeCoverPremium=memberRepository.findByPmstPolicyId(gratitutyCalculationDto.getPmstPolicyId());
					StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(5L).get();
					
					totalGST=lifeCoverPremium*(Double.parseDouble(standardCodeEntity.getValue())/100);
					Double perMonthPremium=lifeCoverPremium/months;
					
					Double perMonthGSt=totalGST/months;
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
					String str_CurrentDate = timeStamp.toString();
					String[] str_SplitCurrentDate = str_CurrentDate.split(" ");
					String[] str_ARD=ard.split(" ");
					try {
					sdf.format(timeStamp);
					sdf.format(ard);
					}catch(Exception e) {
						
					}
					
//					LocalDate ldDateofARD = doard.toLocalDate();

					LocalDate startDate = LocalDate.parse(str_ARD[0]);
				
					LocalDate endDate = LocalDate.parse(str_SplitCurrentDate[0]);
//end will be smaller
					//start will be greater
					// Calculate the difference between the two dates
					Period period = Period.between(endDate,startDate);
					Long noofmonths = Long.valueOf(period.getMonths());

					if (period.getDays() > 0) {
						noofmonths = noofmonths + 1;
					}
		
						Double monthPremiumRefund=0.0;
						Double monthGSTRefund=0.0;
					

						monthPremiumRefund=perMonthPremium*noofmonths;
							
						monthGSTRefund=perMonthGSt * noofmonths;			

				
						GratuityCalculationsDto setFundCalculationValue=new GratuityCalculationsDto();

		setFundCalculationValue.setRefundPremium((double) (Math.round(monthPremiumRefund * 100) / 100D));
		setFundCalculationValue.setRefundonGST((double) (Math.round(monthGSTRefund * 100) / 100D));
		return ApiResponseDto.created(setFundCalculationValue);				
					
		}

			

			@Override
			public ApiResponseDto<PmstTmpMergerPropsDto> mergerApprove(Long mergerPropsId, String username) {
				PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity = pmstTmpMergerPropsRepository.findById(mergerPropsId).get();

				pmstTmpMergerPropsEntity.setStatusID(mergerPropsId);
				
				pmstTmpMergerPropsRepository.save(pmstTmpMergerPropsEntity);
				
				RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(pmstTmpMergerPropsEntity.getSourceTmpPolicyID()).get();

				renewalPolicyTMPEntity.setPolicyStatusId(approvedStatudId);
				renewalPolicyTMPEntity.setPolicySubStatusId(approvedSubStatudId);

				renewalPolicyTMPEntity.setPolicytaggedStatusId(existingTaggedStatusId);

				renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
				
				RenewalPolicyTMPEntity renewalPolicyTMPEntityDestination = renewalPolicyTMPRepository.findById(pmstTmpMergerPropsEntity.getDestinationTmpPolicyID()).get();

				renewalPolicyTMPEntityDestination.setPolicyStatusId(approvedStatudId);
				renewalPolicyTMPEntityDestination.setPolicySubStatusId(approvedSubStatudId);

				renewalPolicyTMPEntityDestination.setPolicytaggedStatusId(existingTaggedStatusId);
				
				renewalPolicyTMPRepository.save(renewalPolicyTMPEntityDestination);
				
				MasterPolicyEntity getMasterPolicyEntity=policyMasterToMergerProps(renewalPolicyTMPEntityDestination,username);
				
				return ApiResponseDto.success(PolicyHelper.entityToDtoMerger(getMasterPolicyEntity));
			}

			private MasterPolicyEntity policyMasterToMergerProps(
					RenewalPolicyTMPEntity renewalPolicyTMPEntityDestination, String username) {

				MasterPolicyEntity newmasterPolicyEntity = null;
				RenewalPolicyTMPEntity renewalPolicyTMPEntity = null;
				PolicyHistoryEntity policyHistoryEntity = null;
				// PolicyMaster to Copy History then Tmp Policy to Master Policy Update
				Optional<MasterPolicyEntity> masterPolicyEntity = masterPolicyRepository
						.findById(renewalPolicyTMPEntityDestination.getMasterPolicyId());

				if (masterPolicyEntity.isPresent()) {
					MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.get().getMasterpolicyHolder())
							.get();
					// MPH UPDATE from MAster to History update
					HistoryMPHEntity hisMPHEntity = PolicyClaimCommonHelper.copytomastertohis(mPHEntity);
					hisMPHEntity = historyMPHRepository.save(hisMPHEntity);
					// MPH UPDATE from MAster to History End
					renewalPolicyTMPEntity = renewalPolicyTMPRepository
							.findById(renewalPolicyTMPEntityDestination.getId()).get();
					policyHistoryEntity = policyHistoryRepository.save(PolicyHelper
							.policymasterToHisTransfer(masterPolicyEntity.get(), renewalPolicyTMPEntity, username));

					policyHistoryEntity.setMasterpolicyHolder(hisMPHEntity.getId());
					policyHistoryRepository.save(policyHistoryEntity);

					newmasterPolicyEntity = PolicyHelper.updateTemptoPolicyMaster(renewalPolicyTMPEntity, username);
					// MPH UPDATE from TEMP to MAster start
					TempMPHEntity tempMPHentity = tempMPHRepository
							.findById(renewalPolicyTMPEntity.getMasterpolicyHolder()).get();
					MPHEntity newmPHEntity = mPHRepository
							.save(PolicyClaimCommonHelper.copytotemptomaster(tempMPHentity));
					newmasterPolicyEntity.setMasterpolicyHolder(newmPHEntity.getId());
					// MPH UPDATE from TEMP to MAster
					newmasterPolicyEntity = masterPolicyRepository.save(newmasterPolicyEntity);
				}

				// PolicymasterSchemeRule to Copy History scheme rule then Update TmpScheme to
				// PolicyScheme rule
				Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
						.findBypolicyId(renewalPolicyTMPEntityDestination.getMasterPolicyId());
				if (policySchemeEntity.isPresent()) {

					policySchemeRuleHistoryRepository
							.save(PolicySchemeRuleHelper.policyMastertoHistransfer(policySchemeEntity.get(),
									policyHistoryEntity.getId(), username));

					RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = renewalSchemeruleTMPRepository
							.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
					PolicySchemeEntity newPolicySchemeEntity = PolicySchemeRuleHelper
							.updateTempToSchemeMaster(renewalSchemeruleTMPEntity, username);
					policySchemeRuleRepository.save(newPolicySchemeEntity);
				}

				// PolicymasterLifeCoverRule to Copy HistoryLifeCOver then Update TmpLifeCover
				// to PolicymasterLifeCoverRule

				List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
						.findByPolicyId(renewalPolicyTMPEntityDestination.getMasterPolicyId());
				List<PolicyLifeCoverEntity> policyLifeCoverEntities = new ArrayList<PolicyLifeCoverEntity>();
				for (PolicyLifeCoverEntity addPolicyLifeCoverEntity : policyLifeCoverEntity) {

					historyLifeCoverRepository
							.save(PolicyLifeCoverHelper.policyMastertoHistransfer(addPolicyLifeCoverEntity,
									policyHistoryEntity.getId(), username));
				}

				List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = renewalLifeCoverTMPRepository
						.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
				for (RenewalLifeCoverTMPEntity addrenewalLifeCoverTMPEntity : renewalLifeCoverTMPEntity) {
					PolicyLifeCoverEntity newPolicyLifeCoverEntity = PolicyLifeCoverHelper
							.updateTempToLifecoverMaster(addrenewalLifeCoverTMPEntity, username,newmasterPolicyEntity.getId());
					policyLifeCoverEntities.add(newPolicyLifeCoverEntity);
				}
				policyLifeCoverRepository.saveAll(policyLifeCoverEntities);

				List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
						.findBypolicyId(renewalPolicyTMPEntityDestination.getMasterPolicyId());
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
					for (RenewalGratuityBenefitTMPEntity addrenewalGratuityBenefitTMPEntity : renewalGratuityBenefitTMPEntity) {
						PolicyGratuityBenefitEntity newPolicyGratuityBenefitEntity = RenewalGratuityBenefitTMPHelper
								.updateTempToPolicyGratuityBenefitMaster(addrenewalGratuityBenefitTMPEntity, username, newmasterPolicyEntity.getId() );
						PolicyGratuityBenefitEntities.add(newPolicyGratuityBenefitEntity);
					}
					policyGratuityBenefitRepository.saveAll(PolicyGratuityBenefitEntities);

				}

				// PolicMasterValuation to copy ValuationHistory upodate RenewalvaluationTMP to
				// PolicMasterValuation
				Optional<PolicyMasterValuationEntity> policyMasterValuationEntity = policyMasterValuationRepository
						.findBypolicyId(renewalPolicyTMPEntityDestination.getMasterPolicyId());
				if (policyMasterValuationEntity.isPresent()) {
					policyValuationHistoryRepository
							.save(PolicyValuationHelper.policyMastertoHistransfer(policyMasterValuationEntity.get(),
									policyHistoryEntity.getId(), username));

					RenewalValuationTMPEntity renewalValuationTMPEntity = renewalValuationTMPRepository
							.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
					PolicyMasterValuationEntity newPolicyMasterValuationEntity = PolicyValuationHelper
							.updateTempToValuationMaster(renewalValuationTMPEntity, username);
					policyMasterValuationRepository.save(newPolicyMasterValuationEntity);
				}

				// Member Renewal Entity start
				try {
					List<RenewalPolicyTMPMemberEntity> tempMemberEntity = renewalPolicyTMPMemberRepository
							.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
					Integer licId = policyMemberRepository.maxLicNumber(renewalPolicyTMPEntity.getMasterPolicyId());
					int LicInc = 1;
					for (RenewalPolicyTMPMemberEntity newTempMemberEntity : tempMemberEntity) {
						PolicyMemberEntity policyMemberEntity = null;
						if (newTempMemberEntity.getPmstMemebrId() != null) {
							policyMemberEntity = policyMemberRepository.findById(newTempMemberEntity.getPmstMemebrId())
									.get();
							historyMemberRepository
									.save(PolicyMemberHelper.policymastertohistoryupdate(policyMemberEntity,policyHistoryEntity.getId(), username));
						}
						if (newTempMemberEntity.getPmstMemebrId() == null) {
							licId = LicInc + licId.intValue();
							Long LicinLong = Long.valueOf(licId);
							newTempMemberEntity.setLicId(LicinLong.toString());
						}

						policyMemberEntity = RenewalPolicyTMPMemberHelper
								.updateTemptoPolicyMemberMaster(newTempMemberEntity, username);
						policyMemberRepository.save(policyMemberEntity);
					}
				} catch (Exception e) {
					e.getStackTrace();
				}

				// PolicyValuationMastxic to copy valuationmatrixHistroy renewalvaluationmatric
				// to PolicyValuationMastxic
				Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
						.findByPolicyId(renewalPolicyTMPEntityDestination.getMasterPolicyId());
				if (policyValuationMatrixEntity.isPresent()) {
					policyValuationMatrixHistoryRepository
							.save(PolicyValuationMatrixHelper.policyMastertoHistransfer(
									policyValuationMatrixEntity.get(), policyHistoryEntity.getId(), username));

					RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
							.findAllBytmpPolicyId(renewalPolicyTMPEntity.getId());
					PolicyValuationMatrixEntity newPolicyValuationMatrixEntity = PolicyValuationMatrixHelper
							.updateTempToValuationMatrixMaster(renewalValuationMatrixTMPEntity, username);
					policyValuationMatrixRepository.save(newPolicyValuationMatrixEntity);
				}

				// PolicValuationBasic to copy PolicValuationBasicHistory upodate
				// RenewalvaluationBasicTMP to PolicValuationBasic

				Optional<PolicyValutationBasicEntity> policyValuationBasicEntity = policyValuationBasicRepository
						.findBypolicyId(renewalPolicyTMPEntityDestination.getMasterPolicyId());
				if (policyValuationBasicEntity.isPresent()) {
					policyValuationBasicHistoryRepository
							.save(PolicyValuationMatrixHelper.policyMastertoHistransfer(
									policyValuationBasicEntity.get(), policyHistoryEntity.getId(), username));

					RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = renewalValuationBasicTMPRepository
							.findAllBytmpPolicyId(renewalPolicyTMPEntity.getId());
					PolicyValutationBasicEntity newPolicyValuationBasicEntity = PolicyValuationMatrixHelper
							.updateTempToValuationBasicMaster(renewalValuationBasicTMPEntity, username);
					policyValuationBasicRepository.save(newPolicyValuationBasicEntity);
				}

				// PolicyWithDraw copy to HistoryWidth renewalvaluationwidthdraw to
				// policywithdrwal
				List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
						.findByPolicyId(renewalPolicyTMPEntityDestination.getMasterPolicyId());
				List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntities = new ArrayList<PolicyValuationWithdrawalRateEntity>();
				for (PolicyValuationWithdrawalRateEntity addPolicyValuationWithdrawalRateEntity : policyValuationWithdrawalRateEntity) {

					policyValuationWithdrawalRateHistoryRepository
							.save(PolicyValuationMatrixHelper.policyMastertoHistransfer(
									addPolicyValuationWithdrawalRateEntity, policyHistoryEntity.getId(), username));
				}

				List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = renewalValuationWithdrawalTMPRepository
						.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
				for (RenewalValuationWithdrawalTMPEntity addrenewalValuationWithdrawalTMPEntity : renewalValuationWithdrawalTMPEntity) {
					PolicyValuationWithdrawalRateEntity newPolicyValuationWithdrawalRateEntity = PolicyValuationMatrixHelper
							.updateTempToLifecoverMaster(addrenewalValuationWithdrawalTMPEntity, username);
					policyValuationWithdrawalRateEntities.add(newPolicyValuationWithdrawalRateEntity);
				}
				policyValuationWithdrawalRateRepository.saveAll(policyValuationWithdrawalRateEntities);

				// End

				return newmasterPolicyEntity;

			}

			@Override
			public ApiResponseDto<RenewalPolicyTMPDto> inprogressSearchByPolicynumber(String policyNumber) {
				MasterPolicyEntity masterPolicyEntity = masterPolicyRepository
						.findByPolicyNumberAndIsActiveTrue(policyNumber);
				RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findByPolicyNumberAndPolicyStatusId(policyNumber);
				if (masterPolicyEntity != null && renewalPolicyTMPEntity != null) {
					List<PmstTmpMergerPropsEntity> tmpMergerPropsList = pmstTmpMergerPropsRepository
							.findBySourcePolicyID(masterPolicyEntity.getId());
					if (tmpMergerPropsList != null && tmpMergerPropsList.size() > 0) {
						RenewalPolicyTMPDto renewalPolicyTMPDto = modelMapper.map(renewalPolicyTMPEntity,
								RenewalPolicyTMPDto.class);
						return ApiResponseDto.success(renewalPolicyTMPDto);
					} else {
						return ApiResponseDto.notFound(null);
					}
				} else {
					return ApiResponseDto.notFound(null);
				}
			}

			@Override
			public ApiResponseDto<List<PmstTmpMergerPropsDto>> mergerPropsDetailsByPolicyNumber(String policyNumber) {
				MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findByPolicyNumberAndIsActiveTrue(policyNumber);
				if(masterPolicyEntity!=null) {
					List<PmstTmpMergerPropsEntity> pmstTmpMergerPropsEntities=pmstTmpMergerPropsRepository.findBySourcePolicyID(masterPolicyEntity.getId()); 
				if(pmstTmpMergerPropsEntities!=null) {
					return ApiResponseDto.success(pmstTmpMergerPropsEntities.stream().map(CommonPolicyServiceHelper::entitytoDto).collect(Collectors.toList()));
				}else {
					return ApiResponseDto.success(null);
				}
				}else {
				return null;
			}


}
			//checker will approve
			@Override
			public ApiResponseDto<PmstTmpMergerPropsDto> forApprove(Long sourceTmpPolicyID, String username) {
				Long policyApproveStatus=253L;
				
				PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity=pmstTmpMergerPropsRepository.findBySourceTmpPolicyID(sourceTmpPolicyID);
				pmstTmpMergerPropsEntity.setStatusID(policyApproveStatus);
				pmstTmpMergerPropsEntity.setModifiedBy(username);
				pmstTmpMergerPropsEntity.setModifiedDate(new Date());
				pmstTmpMergerPropsEntity=pmstTmpMergerPropsRepository.save(pmstTmpMergerPropsEntity);
				//PmstTmpMergerPropsDto pmstTmpMergerPropsDto=modelMapper.map(pmstTmpMergerPropsEntity,PmstTmpMergerPropsDto.class);
				PmstTmpMergerPropsDto responseDto=CommonPolicyServiceHelper.entitytoDto(pmstTmpMergerPropsEntity);
				RenewalPolicyTMPEntity renewalPolicyTMPEntity=renewalPolicyTMPRepository.findById(sourceTmpPolicyID).get();
				
				TaskAllocationEntity taskAllocationEntity=taskAllocationRepository
						.findByRequestId(pmstTmpMergerPropsEntity.getMergerRequestNumber());
				taskAllocationEntity.setTaskStatus(policyApproveStatus.toString());
				taskAllocationRepository.save(taskAllocationEntity);
						
				return ApiResponseDto.success(responseDto);
			}

			@Override
			public ApiResponseDto<PmstTmpMergerPropsDto> forReject(Long sourceTmpPolicyID, String username) {
				Long policyRejectStatus=254L;
				
				PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity=pmstTmpMergerPropsRepository.findBySourceTmpPolicyID(sourceTmpPolicyID);
				pmstTmpMergerPropsEntity.setStatusID(policyRejectStatus);
				pmstTmpMergerPropsEntity.setModifiedBy(username);
				pmstTmpMergerPropsEntity.setModifiedDate(new Date());
				pmstTmpMergerPropsEntity=pmstTmpMergerPropsRepository.save(pmstTmpMergerPropsEntity);
				//PmstTmpMergerPropsDto pmstTmpMergerPropsDto=modelMapper.map(pmstTmpMergerPropsEntity,PmstTmpMergerPropsDto.class);
				PmstTmpMergerPropsDto responseDto=CommonPolicyServiceHelper.entitytoDto(pmstTmpMergerPropsEntity);
//				RenewalPolicyTMPEntity renewalPolicyTMPEntity=renewalPolicyTMPRepository.findById(sourceTmpPolicyID).get();
				
				TaskAllocationEntity taskAllocationEntity=taskAllocationRepository
						.findByRequestId(pmstTmpMergerPropsEntity.getMergerRequestNumber());
				taskAllocationEntity.setTaskStatus(policyRejectStatus.toString());
	            taskAllocationRepository.save(taskAllocationEntity);
				
				return ApiResponseDto.success(responseDto);
			}

			@Override
			public ApiResponseDto<PmstTmpMergerPropsDto> sendBackToMaker(Long sourceTmpPolicyID, String username) {
				Long policySendBackToMakerStatus=252L;
				
//				RenewalPolicyTMPEntity renewalPolicyTMPEntity=renewalPolicyTMPRepository.findById(id).get();
//				renewalPolicyTMPEntity.setModifiedBy(username);
//				renewalPolicyTMPEntity.setModifiedDate(new Date());
//				renewalPolicyTMPEntity.setPolicyStatusId(policySendBackToMakerStatus);
//				renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
				
				PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity=pmstTmpMergerPropsRepository.findBySourceTmpPolicyID(sourceTmpPolicyID);
				pmstTmpMergerPropsEntity.setStatusID(policySendBackToMakerStatus);
				pmstTmpMergerPropsEntity.setModifiedBy(username);
				pmstTmpMergerPropsEntity.setModifiedDate(new Date());
				pmstTmpMergerPropsEntity=pmstTmpMergerPropsRepository.save(pmstTmpMergerPropsEntity);
				//PmstTmpMergerPropsDto pmstTmpMergerPropsDto=modelMapper.map(pmstTmpMergerPropsEntity,PmstTmpMergerPropsDto.class);
                PmstTmpMergerPropsDto responseDto=CommonPolicyServiceHelper.entitytoDto(pmstTmpMergerPropsEntity);
//				RenewalPolicyTMPEntity renewalPolicyTMPEntity=renewalPolicyTMPRepository.findById(sourceTmpPolicyID).get();

				TaskAllocationEntity taskAllocationEntity=taskAllocationRepository
						.findByRequestId(pmstTmpMergerPropsEntity.getMergerRequestNumber());
				taskAllocationEntity.setTaskStatus(policySendBackToMakerStatus.toString());
				taskAllocationRepository.save(taskAllocationEntity);
				
				return ApiResponseDto.success(responseDto);
			}

			@Override
			public ApiResponseDto<PmstTmpMergerPropsDto> sendForApproval(Long sourceTmpPolicyID, String username, Long destinationTmpPolicyId) {
				Long policySendForApprovalStatus = 251L;
				Long masterPolicyId = null;
				try {
					Optional<RenewalPolicyTMPEntity> tempPolicyForSource = renewalPolicyTMPRepository
							.findById(sourceTmpPolicyID);
					if (tempPolicyForSource.isPresent()) {
					masterPolicyId = tempPolicyForSource.get().getMasterPolicyId();
					
					PolicyServiceEntitiy policyServiceEntity = new PolicyServiceEntitiy();
					PolicyServiceEntitiy policyServiceEntityExisting = policyServiceRepository
							.findByPolicyandTypeandActive(masterPolicyId, PolicyServiceName.MERGER.getName());

					if (policyServiceEntityExisting != null) {
						policyServiceEntity.setId(policyServiceEntityExisting.getId());
						policyServiceEntity.setModifiedBy(username);
						policyServiceEntity.setModifiedDate(new Date());
						policyServiceEntity.setServiceType(PolicyServiceName.MERGER.getName());
						policyServiceEntity.setPolicyId(masterPolicyId);
						policyServiceEntity.setIsActive(true);
					} else {
						policyServiceEntity.setServiceType(PolicyServiceName.MERGER.getName());
						policyServiceEntity.setPolicyId(masterPolicyId);
						policyServiceEntity.setCreatedBy(username);
						policyServiceEntity.setCreatedDate(new Date());
						policyServiceEntity.setIsActive(true);
					}
					policyServiceEntityExisting = policyServiceRepository.save(policyServiceEntity);
					RenewalPolicyTMPEntity renewalPolicyTMPEntity = tempPolicyForSource.get();

					renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntityExisting.getId());
					renewalPolicyTMPEntity.setPolicyStatusId(policySendForApprovalStatus);
					renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
					}
					
					Optional<RenewalPolicyTMPEntity> tempPolicyForDestination = renewalPolicyTMPRepository
							.findById(destinationTmpPolicyId);
					if (tempPolicyForDestination.isPresent()) {
					masterPolicyId = tempPolicyForDestination.get().getMasterPolicyId();
					
					PolicyServiceEntitiy policyServiceEntity = new PolicyServiceEntitiy();
					PolicyServiceEntitiy policyServiceEntityExisting = policyServiceRepository
							.findByPolicyandTypeandActive(masterPolicyId, PolicyServiceName.MERGER.getName());

					if (policyServiceEntityExisting != null) {
						policyServiceEntity.setId(policyServiceEntityExisting.getId());
						policyServiceEntity.setModifiedBy(username);
						policyServiceEntity.setModifiedDate(new Date());
						policyServiceEntity.setServiceType(PolicyServiceName.MERGER.getName());
						policyServiceEntity.setPolicyId(masterPolicyId);
						policyServiceEntity.setIsActive(true);
					} else {
						policyServiceEntity.setServiceType(PolicyServiceName.MERGER.getName());
						policyServiceEntity.setPolicyId(masterPolicyId);
						policyServiceEntity.setCreatedBy(username);
						policyServiceEntity.setCreatedDate(new Date());
						policyServiceEntity.setIsActive(true);
					}
					policyServiceEntityExisting = policyServiceRepository.save(policyServiceEntity);
					RenewalPolicyTMPEntity renewalPolicyTMPEntity = tempPolicyForDestination.get();

					renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntityExisting.getId());
					renewalPolicyTMPEntity.setPolicyStatusId(policySendForApprovalStatus);
					renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
					}

					PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity = pmstTmpMergerPropsRepository
							.findBySourceTmpPolicyID(sourceTmpPolicyID);
					pmstTmpMergerPropsEntity.setStatusID(policySendForApprovalStatus);
					pmstTmpMergerPropsEntity.setModifiedBy(username);
					pmstTmpMergerPropsEntity.setModifiedDate(new Date());
					pmstTmpMergerPropsEntity = pmstTmpMergerPropsRepository.save(pmstTmpMergerPropsEntity);

					PmstTmpMergerPropsDto responseDto = CommonPolicyServiceHelper.entitytoDto(pmstTmpMergerPropsEntity);
					TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
							.findByRequestId(pmstTmpMergerPropsEntity.getMergerRequestNumber());
					taskAllocationEntity.setTaskStatus(policySendForApprovalStatus.toString());
					taskAllocationRepository.save(taskAllocationEntity);
					
					return ApiResponseDto.success(responseDto);
				} 
				catch (Exception e) {
					logger.error(e.getStackTrace());
				}
				return null;
			}

			@Override
			@Transactional
			public ApiResponseDto<RenewalPolicyTMPDto> makePaymentAdjustmentForMerger(
					PolicyContributionDetailMergerDto policyContributionDetailDto) {
				Long masterTmpPolicyId = null;
				Double getBalance = null;
				Double adjustementAmount = 0.0;
				int year = 0;
				int month = 0;
				String financialYear = null;
				financialYear = DateUtils.getFinancialYear(new Date());
				Double stampDuty = 0.0;
//				
//				TaskProcessEntity taskProcessEntity =taskProcessRepository.findByProcessName("RENEWAL PROCESSING");

				PolicyContributionDetailEntity policyContributionDetailEntity = new ModelMapper()
						.map(policyContributionDetailDto, PolicyContributionDetailEntity.class);
				policyContributionDetailEntity.setTmpPolicyId(policyContributionDetailDto.getTmpPolicyId());
				policyContributionDetailEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
				policyContributionDetailEntity.setCreatedDate(new Date());
				policyContributionDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
				policyContributionDetailEntity.setEntryType("MRG");
				policyContributionDetailEntity.setActive(true);
				policyContributionDetailEntity.setAdjustmentForDate(new Date());
				policyContributionDetailEntity.setFinancialYear(policyContributionDetailDto.getFinancialYear());
				
				policyContributionDetailEntity = policyContributionDetailRepository.save(policyContributionDetailEntity);

				masterTmpPolicyId = policyContributionDetailEntity.getTmpPolicyId();
				RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findById(masterTmpPolicyId).get();
				;

//				TaskAllocationEntity taskAllocationEntity = taskAllocationRepository
//						.findByRequestId(renewalPolicyTMPEntity.getPolicyNumber());
				

//				PolicyContrySummaryEntity policyContrySummaryEntity = new PolicyContrySummaryEntity();
//				StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(standardcode).get();
				// master contributio detail
//				MasterPolicyContributionDetails getMasterlastyearPCD = masterPolicyContributionDetailRepository
//						.findBymasterPolicyId(renewalPolicyTMPEntity.getMasterPolicyId());

//				Double getFundAmt=renewalPolicyTMPRepository.getFundAmt(renewalPolicyTMPEntity2.getMasterPolicyId());

//				Double getMasterPSCS=(getMasterlastyearPCD.getPastService()+getMasterlastyearPCD.getCurrentServices())-getFundAmt;

				PolicyContributionDetailEntity getGstSC = policyContributionDetailRepository
						.findBymasterTmpPolicyId(masterTmpPolicyId);

				adjustementAmount = getGstSC.getGst() + getGstSC.getLifePremium() + getGstSC.getCurrentServices()
						+ getGstSC.getPastService();

//				policyContributionDetailEntity = policyContributionDetailRepository.save(policyContributionDetailEntity);

				List<ShowDepositLockDto> showDepositLockDto = new ArrayList<ShowDepositLockDto>();
				List<Date> collectionDate = new ArrayList<>();
				String prodAndVarientCodeSame = commonModuleService.getProductCode(renewalPolicyTMPEntity.getProductId());

				for (DepositAdjustementDto getDepositAdjustementDto : policyContributionDetailDto.getDepositAdjustmentDto()) {
					if (adjustementAmount != 0) {
//						masterTmpPolicyId = getDepositAdjustementDto.getTmpPolicyId();

						PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

						//PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();
						getDepositAdjustementDto.setDepositAmount(getDepositAdjustementDto.getBalanceAmount());;
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
						policyDepositEntity.setContributionType("MRG");
						policyDepositEntity.setContributionDetailId(policyContributionDetailEntity.getId());
						
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

						depositLockDto.setChallanNo(Integer.valueOf(policyContributionDetailEntity.getChallanNo()));
						depositLockDto.setRefNo(policyDepositEntity.getId().toString());
						depositLockDto.setCollectionNo(policyDepositEntity.getCollectionNo().toString());
						depositLockDto.setUserCode(policyContributionDetailDto.getCreatedBy());
						depositLockDto.setDueMonth(DateUtils.currentMonth() + "/" + DateUtils.currentDay());
						depositLockDto.setProductCode(prodAndVarientCodeSame);
						depositLockDto.setVariantCode(prodAndVarientCodeSame);

						logger.info("Lock Deposit Params: " + depositLockDto.toString());
						showDepositLockDto.add(depositLockDto);
						
					}
				}

				List<PolicyDepositEntity> policyDepositEntitiesList = policyDepositRepository
						.findBymasterPolicyIdandTypeME(masterTmpPolicyId);

				List<PolicyContributionEntity> policyContributionEntity = new ArrayList<PolicyContributionEntity>();
				List<PolicyAdjustmentDetailEntity> policyAdjustmentDetail = new ArrayList<PolicyAdjustmentDetailEntity>();
				Double depositAmount = 0.0;
				Long depositId = 0l;
				Double currentAmount = 0.0;

//				Long premiumAdjustmentLong = Math.round(policyContributionDetailEntity.getLifePremium());
				Double premiumAdjustmentMerger = policyContributionDetailEntity.getLifePremium(); //premiumAdjustmentLong.doubleValue();

				Double gstOnPremiumAdjusted2 = policyContributionDetailEntity.getGst().doubleValue();

//				Long currentServiceAdjustedLong = Math.round(policyContributionDetailEntity.getCurrentServices());
				Double currentServiceAdjusted2 = policyContributionDetailEntity.getCurrentServices();//currentServiceAdjustedLong.doubleValue();

//				Long pastServiceAdjustedLong = Math.round(policyContributionDetailEntity.getPastService());
				Double pastServiceAdjusted2 = policyContributionDetailEntity.getPastService(); //pastServiceAdjustedLong.doubleValue();
				adjustementAmount = premiumAdjustmentMerger + gstOnPremiumAdjusted2 + currentServiceAdjusted2
						+ pastServiceAdjusted2;
				policyContributionDetailDto
						.setDepositAdjustmentDto(sort(policyContributionDetailDto.getDepositAdjustmentDto()));
				
				List<PolicyDepositEntity> policyDepositEntities = policyDepositRepository.findBymasterPolicyIdandType(masterTmpPolicyId);

				Long versionNumber = policyContributionRepository.getMaxVersion(financialYear,
						policyContributionDetailEntity.getId());
				versionNumber = versionNumber == null ? 01 : versionNumber + 1;
				
				Double openingBalance=0.0;
				Double closingBalance=0.0;

				Double premiumAdjustment = policyContributionDetailEntity.getLifePremium();
				Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();

				Double currentServiceAdjusted = policyContributionDetailEntity.getCurrentServices();
				

				Double pastServiceAdjusted = policyContributionDetailEntity.getPastService();
				adjustementAmount =premiumAdjustment +gstOnPremiumAdjusted + currentServiceAdjusted
				+ pastServiceAdjusted;
				policyContributionDetailDto.setDepositAdjustmentDto(sort(policyContributionDetailDto.getDepositAdjustmentDto()));
			
				versionNumber= versionNumber==null ?01:versionNumber+1;
				for (PolicyDepositEntity policyDepositEntity : policyDepositEntities) {
					if (adjustementAmount > 0) {
						Double currentAmountforContribution=0.0;
						depositAmount = policyDepositEntity.getAdjustmentAmount();
						depositId = policyDepositEntity.getId();
						// LIFE PREMIUM
						if (premiumAdjustment > 0 && depositAmount > 0) {
							if (premiumAdjustment >= depositAmount) {
								currentAmount = depositAmount;

								premiumAdjustment = premiumAdjustment - depositAmount;
								adjustementAmount = adjustementAmount - currentAmount;
								PolicyAdjustmentDetailEntity savePolicyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(this.saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
										"LifePremium", "MRG", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								depositAmount = 0.0;
							} else {
								currentAmount = premiumAdjustment;

								adjustementAmount = adjustementAmount - currentAmount;
								PolicyAdjustmentDetailEntity savePolicyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(this.saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
										"LifePremium", "MRG", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								depositAmount = depositAmount - premiumAdjustment;
								premiumAdjustment = 0.0;
							}
						}

						// GST AMOUNT

						if (premiumAdjustment == 0 && gstOnPremiumAdjusted > 0 && depositAmount > 0) {
							if (gstOnPremiumAdjusted >= depositAmount) {
								currentAmount = depositAmount;

								gstOnPremiumAdjusted = gstOnPremiumAdjusted - depositAmount;
								adjustementAmount = adjustementAmount - currentAmount;
								PolicyAdjustmentDetailEntity savePolicyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(this.saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst",
										"MRG", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								depositAmount = 0.0;
							} else {
								currentAmount = gstOnPremiumAdjusted;

								adjustementAmount = adjustementAmount - currentAmount;
								PolicyAdjustmentDetailEntity savePolicyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(this.saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst",
										"MRG", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								depositAmount = depositAmount - gstOnPremiumAdjusted;
								gstOnPremiumAdjusted = 0.0;
							}
						}

						// SinglePremium

					
						if (premiumAdjustment == 0 && gstOnPremiumAdjusted == 0 
								&& pastServiceAdjusted > 0 && depositAmount > 0) {
							if (pastServiceAdjusted >= depositAmount) {
								currentAmount = depositAmount;

								pastServiceAdjusted = pastServiceAdjusted - depositAmount;
								adjustementAmount = adjustementAmount - currentAmount;
								
								PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity= policyAdjustmentDetailRepository. save(saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
										"SinglePremium", "MRG", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								
//								policyAdjustmentDetail.add(this.saveAdjustmentdata(
//										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//										"SinglePremium", "RE", policyDepositEntity.getChequeRealistionDate(),
//										policyContributionDetailDto.getCreatedBy()));
								currentAmountforContribution+=currentAmount;
								closingBalance=openingBalance+currentAmountforContribution;
								policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
										policyDepositEntity.getContributionDetailId(), "MRG", currentAmount, financialYear,
										versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate(),
										Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
								openingBalance=+closingBalance;
								versionNumber=versionNumber+1;
								depositAmount = 0.0;
							} else {
								currentAmount = pastServiceAdjusted;

								adjustementAmount = adjustementAmount - currentAmount;
								PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity= policyAdjustmentDetailRepository. save(saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
										"SinglePremium", "MRG", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								
//								policyAdjustmentDetail.add(this.saveAdjustmentdata(
//										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//										"SinglePremium", "RE", policyDepositEntity.getChequeRealistionDate(),
//										policyContributionDetailDto.getCreatedBy()));
								depositAmount = depositAmount - pastServiceAdjusted;
								currentAmountforContribution+=currentAmount;
								closingBalance=openingBalance+currentAmountforContribution;
								policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
										policyDepositEntity.getContributionDetailId(), "MRG", currentAmount, financialYear,
										versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate(),
										Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
								versionNumber=versionNumber+1;
								openingBalance=+closingBalance;
								pastServiceAdjusted = 0.0;
							}
						}

						// FirstPremium

						if (premiumAdjustment == 0 && gstOnPremiumAdjusted == 0 && pastServiceAdjusted == 0 &&  currentServiceAdjusted > 0
								&& depositAmount > 0) {
							if (currentServiceAdjusted >= depositAmount) {
								currentAmount = depositAmount;

								currentServiceAdjusted = currentServiceAdjusted - depositAmount;
								adjustementAmount = adjustementAmount - currentAmount;
								
								PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity= policyAdjustmentDetailRepository. save(saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
										"FirstPremium", "MRG", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								
								
//								policyAdjustmentDetail.add(this.saveAdjustmentdata(
//										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//										"FirstPremium", "RE", policyDepositEntity.getChequeRealistionDate(),
//										policyContributionDetailDto.getCreatedBy()));
								currentAmountforContribution+=currentAmount;
								closingBalance=openingBalance+currentAmountforContribution;
								policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
										policyDepositEntity.getContributionDetailId(), "MRG", currentAmount, financialYear,
										versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(),
										policyDepositEntity.getCollectionDate(),Long.toString(policyDepositEntity.getCollectionNo()),
										policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
								versionNumber=versionNumber+1;
								openingBalance=+closingBalance;
								depositAmount = 0.0;
							} else {
								currentAmount = currentServiceAdjusted;

								adjustementAmount = adjustementAmount - currentAmount;
								
								PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity= policyAdjustmentDetailRepository. save(saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
										"FirstPremium", "MRG", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								
								
//								policyAdjustmentDetail.add(this.saveAdjustmentdata(
//										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//										"FirstPremium", "RE", policyDepositEntity.getChequeRealistionDate(),
//										policyContributionDetailDto.getCreatedBy()));
								depositAmount = depositAmount - currentServiceAdjusted;
								currentAmountforContribution+=currentAmount;
								closingBalance=openingBalance+currentAmountforContribution;
								policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
										policyDepositEntity.getContributionDetailId(), "MRG", currentAmount, financialYear,
										versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate(),
										Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
								versionNumber=versionNumber+1;
								openingBalance=+closingBalance;
								currentServiceAdjusted = 0.0;
							}
						}
//						policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
//								policyDepositEntity.getContributionDetailId(), "RE", currentAmountforContribution, financialYear,
//								versionNumber,
//								commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
//								policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate(),Long.toString(policyDepositEntity.getCollectionNo())));
//						versionNumber=versionNumber+1;
					}
				}
				
				policyContributionRepository.saveAll(policyContributionEntity);
				policyAdjustmentDetailRepository.saveAll(policyAdjustmentDetail);

				if (isDevEnvironmentForRenewals == false) {
					accountingService.lockDeposits(showDepositLockDto, policyContributionDetailDto.getCreatedBy());
				}

				Date risk = Collections.max(collectionDate);
				renewalPolicyTMPEntity.setRiskCommencementDate(risk);
				renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

//		        policyServiceRepository.deactivateservicetype(renewalPolicyTMPEntity.getMasterPolicyId());

//				policyRenewalRemainderRepository.updateisActiveFalseforMasterPolicy(renewalPolicyTMPEntity.getMasterPolicyId());

//				if(taskAllocationEntity!=null) {
//				taskAllocationEntity.setRequestId(renewalPolicyTMPEntity.getPolicyNumber());
//				taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//			//	taskAllocationEntity.setLocationType(policyContributionDetailDto.getUnitCode());
//				taskAllocationEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
//				taskAllocationEntity.setCreatedDate(new Date());
//				taskAllocationEntity.setTaskStatus(defaultStatusId);
//				taskAllocationEntity.setModulePrimaryId(renewalPolicyTMPEntity.getId());
//				taskAllocationEntity.setIsActive(true);
		//	
//				taskAllocationRepository.save(taskAllocationEntity);
//				}else {
//					TaskAllocationEntity newTaskAllocationEntity =new TaskAllocationEntity();
//					newTaskAllocationEntity.setRequestId(renewalPolicyTMPEntity.getPolicyNumber());
//					newTaskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//				//	taskAllocationEntity.setLocationType(policyContributionDetailDto.getUnitCode());
//					newTaskAllocationEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
//					newTaskAllocationEntity.setCreatedDate(new Date());
//					newTaskAllocationEntity.setTaskStatus(defaultStatusId);
//					newTaskAllocationEntity.setModulePrimaryId(renewalPolicyTMPEntity.getId());
//					newTaskAllocationEntity.setIsActive(true);
//				
//					taskAllocationRepository.save(newTaskAllocationEntity);
//				}
				return ApiResponseDto.success(RenewalPolicyTMPHelper.entityToDto(renewalPolicyTMPEntity));
			}

			public static List<DepositAdjustementDto> sort(List<DepositAdjustementDto> list) {

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
			
			///Save Contribution Detail
	  private PolicyContributionEntity saveContributionDetail(Long policyId, Long contributionDetailId, String contributionType,
			Double currentAmount, String financialYear, Long maxVersion, String variantCode, Date effectiveDate, String userName, Date date,String collectionnumber,
				Long conAdjId, Double openingBalance, Double closingBalance) {
			PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();

			policyContributionEntity.setAdjConid(conAdjId);

			policyContributionEntity.setTotalContribution(currentAmount);

			policyContributionEntity.setContReferenceNo(collectionnumber);
			policyContributionEntity.setContributionDate(date);
			policyContributionEntity.setCreatedBy(userName);
			policyContributionEntity.setCreateDate(new Date());
			policyContributionEntity.setEmployeeContribution(0.0);
			policyContributionEntity.setEmployerContribution(currentAmount);

			policyContributionEntity.setFinancialYear(financialYear);
			policyContributionEntity.setClosingBlance(closingBalance);
			policyContributionEntity.setOpeningBalance(openingBalance);

			policyContributionEntity.setActive(true);
			policyContributionEntity.setIsDeposit(true);

			if (contributionType.toLowerCase().equals("MRG")) {
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
			// for now default Q0

			return policyContributionEntity;
		}
			
			@Override
			@Transactional
			public ApiResponseDto<RenewalPolicyTMPDto> updatePaymentAdjustmentForMerger(
					PolicyContributionDetailMergerDto policyContributionDetailDto) {
				Long masterTmpPolicyId = null;
				Double getBalance = null;
				String financialYear =null;
				Double adjustementAmount = 0.0;
				Double stampDuty=0.0;
				int year = 0;
				int month = 0;
				financialYear = DateUtils.getFinancialYear(new Date());
				masterTmpPolicyId = policyContributionDetailDto.getTmpPolicyId();
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
						depositLockDto.setProductCode(prodAndVarientCodeSame);
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
				policyContributionDetailEntity.setChallanNo(commonService.getSequence(HttpConstants.CHALLEN_NO));
				policyContributionDetailEntity.setLifePremium(policyContributionDetailDto.getLifePremium());
				policyContributionDetailEntity.setPastService(policyContributionDetailDto.getPastService());
				policyContributionDetailEntity.setGst(policyContributionDetailDto.getGst());
				policyContributionDetailEntity.setCurrentServices(policyContributionDetailDto.getCurrentServices());
				policyContributionDetailEntity.setModifiedBy(policyContributionDetailDto.getModifiedBy());
				policyContributionDetailEntity.setModifiedDate(new Date());
				policyContributionDetailEntity.setEntryType("MRG");			
				policyContributionDetailEntity.setFinancialYear(policyContributionDetailDto.getFinancialYear());
				policyContributionDetailEntity.setAdjustmentForDate(policyContributionDetailDto.getAdjustmentForDate());
				
				policyContributionDetailEntity=policyContributionDetailRepository.save(policyContributionDetailEntity);

				List<PolicyAdjustmentDetailEntity>PolicyAdjustmentDetail= policyAdjustmentDetailRepository
						.deleteBycontributionDetailId(policyContributionDetailEntity.getId());
				List<PolicyDepositEntity> oldDepositList = policyDepositRepository.deleteBytmpPolicyId(masterTmpPolicyId);
				List<PolicyContributionEntity> oldPolicyContribution = policyContributionRepository
						.deleteBytmpPolicyId(masterTmpPolicyId);
				List<PolicyContrySummaryEntity> oldPolicyContriSummary = policyContrySummaryRepository
						.deleteBytmpPolicyId(masterTmpPolicyId); //no need
				
			
//				PolicyContrySummaryEntity policyContrySummaryEntity = new PolicyContrySummaryEntity();
//				StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(standardcode).get();
				PolicyContributionDetailEntity getGstSC = policyContributionDetailRepository
						.findBymasterTmpPolicyId(masterTmpPolicyId);
//				MasterPolicyContributionDetails getMasterlastyearPCD = masterPolicyContributionDetailRepository
//						.findBymasterPolicyId(renewalPolicyTMPEntity.getMasterPolicyId());
//				
//				Double getFundAmt=renewalPolicyTMPRepository.getFundAmt(renewalPolicyTMPEntity.getMasterPolicyId());
//				
//				Double getMasterPSCS=(getMasterlastyearPCD.getPastService()+getMasterlastyearPCD.getCurrentServices())-getFundAmt;
				
				

				adjustementAmount = getGstSC.getGst() + getGstSC.getLifePremium() + getGstSC.getCurrentServices()
						+ getGstSC.getPastService();
//				Double getCPLCSum = getGstSC.getCurrentServices() + getGstSC.getPastService()
//				+  getGstSC.getLifePremium();
//				RenewalValuationMatrixTMPEntity getRenewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
//						.findBytmpPolicyId(masterTmpPolicyId).get();
//				Double getPaise = Double.parseDouble(standardCodeEntity.getValue()) / 100;
//				Double getCPLCSum = getGstSC.getCurrentServices() + getGstSC.getPastService()
//						+ (getRenewalValuationMatrixTMPEntity.getTotalSumAssured().isNaN() ? 0L
//								: getRenewalValuationMatrixTMPEntity.getTotalSumAssured().longValue());
//				logger.info("Adding Total= CurrentService+PastService+totalsumAssured");
//				getCPLCSum=getCPLCSum+getMasterPSCS;
//				if (getCPLCSum > 0) {
//					Double valuesDividedByThousandQuotient = getCPLCSum / 1000;
//					Double valuesDividedByThousandremainder = getCPLCSum % 1000;
//				
//					if (valuesDividedByThousandremainder == 0) {
//						stampDuty=( valuesDividedByThousandQuotient) * getPaise;
//					} else {
//						stampDuty= (valuesDividedByThousandQuotient + 1) * getPaise;
//					}
//				}
//				policyContributionDetailEntity.setStampDuty(stampDuty);			
//				policyContributionDetailEntity=policyContributionDetailRepository.save(policyContributionDetailEntity);

				List<ShowDepositLockDto> showDepositLockDto=new ArrayList<ShowDepositLockDto>();
				for (DepositAdjustementDto getDepositAdjustementDto : policyContributionDetailDto
						.getDepositAdjustmentDto()) {
					if (adjustementAmount != 0) {
						masterTmpPolicyId = getDepositAdjustementDto.getTmpPolicyId();

						PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

						getDepositAdjustementDto.setDepositAmount(getDepositAdjustementDto.getBalanceAmount());
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
						Random r = new Random(System.currentTimeMillis());
						int getCollectionNumber = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
						policyDepositEntity.setChallanNo(String.valueOf(getCollectionNumber));
						policyDepositEntity.setCollectionDate(new Date());
						policyDepositEntity.setCreateDate(new Date());
						policyDepositEntity.setZeroId(null);
						policyDepositEntity.setTransactionMode(getDepositAdjustementDto.getCollectionMode());
						policyDepositEntity.setStatus(null);
						policyDepositEntity.setRegConId(null);
						policyDepositEntity.setTmpPolicyId(masterTmpPolicyId);
						policyDepositEntity.setContributionType("MRG");
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
						
//						policyContributionEntity.setContributionDetailId(policyContributionDetailEntity.getId());
//						policyContributionEntity.setAdjConid(null);
//						policyContributionEntity.setOpeningBalance(0.0);
//						policyContributionEntity
//								.setTotalContribution(getGstSC.getCurrentServices() + getGstSC.getPastService());
//						policyContributionEntity.setClosingBlance(policyContributionEntity.getOpeningBalance()
//								+ policyContributionEntity.getTotalContribution());
//						policyContributionEntity
//								.setContReferenceNo(Long.toString(policyDepositEntity.getCollectionNo()));
//						policyContributionEntity.setContributionDate(policyDepositEntity.getCollectionDate());
//						policyContributionEntity.setContributionType(null);
//						policyContributionEntity.setCreatedBy(getDepositAdjustementDto.getCreatedBy());
//						policyContributionEntity.setCreateDate(new Date());
//						policyContributionEntity.setEmployeeContribution(0.0);
//						policyContributionEntity
//								.setEmployerContribution(getGstSC.getCurrentServices() + getGstSC.getPastService());
//						year = Calendar.getInstance().get(Calendar.YEAR);
//						month = Calendar.getInstance().get(Calendar.MONTH) + 1;
//						System.out.println("Financial month : " + month);
//						if (month < 3) {
//							policyContributionEntity.setFinancialYear((year - 1) + "-" + year);
//						} else {
//							policyContributionEntity.setFinancialYear(year + "-" + (year + 1));
//						}
//						policyContributionEntity.setActive(true);
//						policyContributionEntity.setIsDeposit(true);
//						policyContributionEntity.setOpeningBalance(0.0);
////						policyContributionEntity.setPolicyId(savedStagingPolicyEntity.getId());
//						policyContributionEntity.setTmpPolicyId(masterTmpPolicyId);
//						policyContributionEntity.setRegConId(null);
//						policyContributionEntity.setTxnEntryStatus(0l);
//						policyContributionEntity.setVersionNo(1l);
//						policyContributionEntity.setVoluntaryContribution(0.0);
//						policyContributionEntity.setZeroAccountEntries(0l);
//						policyContributionRepository.save(policyContributionEntity);
						
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
//				year = Calendar.getInstance().get(Calendar.YEAR);
//
//				month = Calendar.getInstance().get(Calendar.MONTH) + 1;
//				System.out.println("Financial month : " + month);
//			
//				if (month < 3) {
//					financialYear =((year - 1) + "-" + year);
//				} else {
//					financialYear=(year + "-" + (year + 1));
//				}
				List<PolicyContributionEntity> policyContributionEntity = new ArrayList<PolicyContributionEntity>();
				List<PolicyDepositEntity> policyDepositEntitieList = policyDepositRepository.findBymasterPolicyIdandTypeME(masterTmpPolicyId);
//				getGstSC.getGst() + getGstSC.getLifePremium() + getGstSC.getCurrentServices()
//				+ getGstSC.getPastService();
			List<PolicyAdjustmentDetailEntity> policyAdjustmentDetail =new ArrayList<PolicyAdjustmentDetailEntity>();
				Double depositAmount=0.0;
				Long depositId=0l;
				Double currentAmount=0.0;
				Double closingBalance=0.0;
				Double openingBalance=0.0;
//				Long premiumAdjustmentLong=Math.round(policyContributionDetailEntity.getLifePremium());
				Double premiumAdjustment = policyContributionDetailEntity.getLifePremium(); // premiumAdjustmentLong.doubleValue();
				
				Double gstOnPremiumAdjusted = policyContributionDetailEntity.getGst().doubleValue();
				
//				Long currentServiceAdjustedLong=Math.round(policyContributionDetailEntity.getCurrentServices());
				Double currentServiceAdjusted = policyContributionDetailEntity.getCurrentServices(); // policyContributionDetailEntity.getCurrentServices(); // currentServiceAdjustedLong.doubleValue();
				
//				Long pastServiceAdjustedLong=Math.round(policyContributionDetailEntity.getPastService());
				Double pastServiceAdjusted = policyContributionDetailEntity.getPastService(); // pastServiceAdjustedLong.doubleValue();
				adjustementAmount =premiumAdjustment +gstOnPremiumAdjusted + currentServiceAdjusted
						+ pastServiceAdjusted;
				policyContributionDetailDto.setDepositAdjustmentDto(sort(policyContributionDetailDto.getDepositAdjustmentDto()));
				Long versionNumber=policyContributionRepository.getMaxVersion(financialYear,
						policyContributionDetailEntity.getId());
				versionNumber= versionNumber==null ?01:versionNumber+1;
				for (PolicyDepositEntity policyDepositEntity : policyDepositEntitieList) {

					if (adjustementAmount > 0) {
						Double currentAmountforContribution=0.0;
						depositAmount = policyDepositEntity.getAdjustmentAmount();
						depositId = policyDepositEntity.getId();
						// LIFE PREMIUM
						if (premiumAdjustment > 0 && depositAmount > 0) {
							if (premiumAdjustment >= depositAmount) {
								currentAmount = depositAmount;

								premiumAdjustment = premiumAdjustment - depositAmount;
								adjustementAmount = adjustementAmount - currentAmount;
								PolicyAdjustmentDetailEntity savePolicyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(this.saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
										"LifePremium", "RE", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								depositAmount = 0.0;
							} else {
								currentAmount = premiumAdjustment;

								adjustementAmount = adjustementAmount - currentAmount;
								PolicyAdjustmentDetailEntity savePolicyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(this.saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "LifePremium",
										"LifePremium", "RE", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								depositAmount = depositAmount - premiumAdjustment;
								premiumAdjustment = 0.0;
							}
						}

						// GST AMOUNT

						if (premiumAdjustment == 0 && gstOnPremiumAdjusted > 0 && depositAmount > 0) {
							if (gstOnPremiumAdjusted >= depositAmount) {
								currentAmount = depositAmount;

								gstOnPremiumAdjusted = gstOnPremiumAdjusted - depositAmount;
								adjustementAmount = adjustementAmount - currentAmount;
								PolicyAdjustmentDetailEntity savePolicyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(this.saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst",
										"RE", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								depositAmount = 0.0;
							} else {
								currentAmount = gstOnPremiumAdjusted;

								adjustementAmount = adjustementAmount - currentAmount;
								PolicyAdjustmentDetailEntity savePolicyAdjustmentDetailEntity=policyAdjustmentDetailRepository.save(this.saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Gst", "Gst",
										"RE", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								depositAmount = depositAmount - gstOnPremiumAdjusted;
								gstOnPremiumAdjusted = 0.0;
							}
						}

						// SinglePremium

						if (premiumAdjustment == 0 && gstOnPremiumAdjusted == 0 
								&& pastServiceAdjusted > 0 && depositAmount > 0) {
							if (pastServiceAdjusted >= depositAmount) {
								currentAmount = depositAmount;

								pastServiceAdjusted = pastServiceAdjusted - depositAmount;
								adjustementAmount = adjustementAmount - currentAmount;
								
								PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity= policyAdjustmentDetailRepository. save(saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
										"SinglePremium", "RE", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								
//								policyAdjustmentDetail.add(this.saveAdjustmentdata(
//										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//										"SinglePremium", "RE", policyDepositEntity.getChequeRealistionDate(),
//										policyContributionDetailDto.getCreatedBy()));
								currentAmountforContribution+=currentAmount;
								closingBalance=openingBalance+currentAmountforContribution;
								policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
										policyDepositEntity.getContributionDetailId(), "RE", currentAmount, financialYear,
										versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate(),
										Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
								openingBalance=+closingBalance;
								versionNumber=versionNumber+1;
								depositAmount = 0.0;
							} else {
								currentAmount = pastServiceAdjusted;

								adjustementAmount = adjustementAmount - currentAmount;
								PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity= policyAdjustmentDetailRepository. save(saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
										"SinglePremium", "RE", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								
//								policyAdjustmentDetail.add(this.saveAdjustmentdata(
//										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//										"SinglePremium", "RE", policyDepositEntity.getChequeRealistionDate(),
//										policyContributionDetailDto.getCreatedBy()));
								depositAmount = depositAmount - pastServiceAdjusted;
								currentAmountforContribution+=currentAmount;
								closingBalance=openingBalance+currentAmountforContribution;
								policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
										policyDepositEntity.getContributionDetailId(), "RE", currentAmount, financialYear,
										versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate(),
										Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
								versionNumber=versionNumber+1;
								openingBalance=+closingBalance;
								pastServiceAdjusted = 0.0;
							}
						}

						// FirstPremium

						if (premiumAdjustment == 0 && gstOnPremiumAdjusted == 0 && pastServiceAdjusted == 0 &&  currentServiceAdjusted > 0
								&& depositAmount > 0) {
							if (currentServiceAdjusted >= depositAmount) {
								currentAmount = depositAmount;

								currentServiceAdjusted = currentServiceAdjusted - depositAmount;
								adjustementAmount = adjustementAmount - currentAmount;
								
								PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity= policyAdjustmentDetailRepository. save(saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
										"FirstPremium", "RE", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								
								
//								policyAdjustmentDetail.add(this.saveAdjustmentdata(
//										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//										"FirstPremium", "RE", policyDepositEntity.getChequeRealistionDate(),
//										policyContributionDetailDto.getCreatedBy()));
								currentAmountforContribution+=currentAmount;
								closingBalance=openingBalance+currentAmountforContribution;
								policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
										policyDepositEntity.getContributionDetailId(), "RE", currentAmount, financialYear,
										versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(),
										policyDepositEntity.getCollectionDate(),Long.toString(policyDepositEntity.getCollectionNo()),
										policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
								versionNumber=versionNumber+1;
								openingBalance=+closingBalance;
								depositAmount = 0.0;
							} else {
								currentAmount = currentServiceAdjusted;

								adjustementAmount = adjustementAmount - currentAmount;
								
								PolicyAdjustmentDetailEntity policyAdjustmentDetailEntity= policyAdjustmentDetailRepository. save(saveAdjustmentdata(
										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
										"FirstPremium", "RE", policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy()));
								
								
//								policyAdjustmentDetail.add(this.saveAdjustmentdata(
//										policyDepositEntity.getContributionDetailId(), depositId, currentAmount, "Fund",
//										"FirstPremium", "RE", policyDepositEntity.getChequeRealistionDate(),
//										policyContributionDetailDto.getCreatedBy()));
								depositAmount = depositAmount - currentServiceAdjusted;
								currentAmountforContribution+=currentAmount;
								closingBalance=openingBalance+currentAmountforContribution;
								policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
										policyDepositEntity.getContributionDetailId(), "RE", currentAmount, financialYear,
										versionNumber,
										commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
										policyDepositEntity.getChequeRealistionDate(),
										policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate(),
										Long.toString(policyDepositEntity.getCollectionNo()),policyAdjustmentDetailEntity.getId(),openingBalance,closingBalance));
								versionNumber=versionNumber+1;
								openingBalance=+closingBalance;
								currentServiceAdjusted = 0.0;
							}
						}
//						policyContributionEntity.add(this.saveContributionDetail(masterTmpPolicyId,
//								policyDepositEntity.getContributionDetailId(), "RE", currentAmountforContribution, financialYear,
//								versionNumber,
//								commonModuleService.getVariantCode(renewalPolicyTMPEntity.getProductVariantId()),
//								policyDepositEntity.getChequeRealistionDate(),
//								policyContributionDetailDto.getCreatedBy(),policyDepositEntity.getCollectionDate(),Long.toString(policyDepositEntity.getCollectionNo())));
//						versionNumber=versionNumber+1;
					}
				
				}
				policyContributionRepository.saveAll(policyContributionEntity);
				policyAdjustmentDetailRepository.saveAll(policyAdjustmentDetail);
					if (isDevEnvironmentForRenewals == false) {
						accountingService.lockDeposits(showDepositLockDto,policyContributionDetailDto.getCreatedBy());
					}

				
//				Double getCSPPSP = getGstSC.getCurrentServices() + getGstSC.getPastService();
//				policyContrySummaryEntity.setContributionDetailId(policyContributionDetailEntity.getId());
//				policyContrySummaryEntity.setClosingBalance(getCSPPSP);
//				policyContrySummaryEntity.setTotalContribution(getCSPPSP);
//				policyContrySummaryEntity.setTotalEmployerContribution(getCSPPSP);
//				policyContrySummaryEntity.setActive(true);
//				policyContrySummaryEntity.setCreatedDate(new Date());
//				policyContrySummaryEntity.setCreatedBy(policyContributionDetailDto.getCreatedBy());
//				System.out.println("Financial month : " + month);
//				if (month < 3) {
//					policyContrySummaryEntity.setFinancialYear((year - 1) + "-" + year);
//				} else {
//					policyContrySummaryEntity.setFinancialYear(year + "-" + (year + 1));
//				}
//				policyContrySummaryEntity.setOpeningBalance(0.0);
////				policyContrySummaryEntity.setPolicyId(savedStagingPolicyEntity.getId());
//				policyContrySummaryEntity.setTmpPolicyId(masterTmpPolicyId);
//				policyContrySummaryEntity.setTotalEmployeeContribution(0.0);
//				policyContrySummaryEntity.setTotalVoluntaryContribution(0.0);
//				Double getPaise = Double.parseDouble(standardCodeEntity.getValue()) / 100;
		//
//				policyContrySummaryEntity.setStampDuty(
//						(policyContrySummaryEntity.getTotalContribution().doubleValue() / 1000) * getPaise);
//				logger.info("StamDuty =(TotalContribution /1000) * getPaise"
//						+ policyContrySummaryEntity.getStampDuty());
//				policyContrySummaryRepository.save(policyContrySummaryEntity);
////				StagingPolicyValuationMatrixEntity getStagingPolicyValuationMatrixEntity=stagingPolicyValuationMatrixRepository.findByPolicyId(savedStagingPolicyEntity.getId()).get();
//				RenewalValuationMatrixTMPEntity getRenewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
//						.findBytmpPolicyId(masterTmpPolicyId).get();
		//
//				Double getCPLCSum = getGstSC.getCurrentServices() + getGstSC.getPastService()
//						+ (getRenewalValuationMatrixTMPEntity.getTotalSumAssured().isNaN() ? 0L
//								: getRenewalValuationMatrixTMPEntity.getTotalSumAssured().longValue());
//				logger.info("Adding Total= CurrentService+PastService+totalsumAssured");
//				if (getCPLCSum > 0) {
//					Double valuesDividedByThousandQuotient = getCPLCSum / 1000;
//					Double valuesDividedByThousandremainder = getCPLCSum % 1000;
//					if (valuesDividedByThousandremainder == 0) {
//						policyContrySummaryEntity.setTotalContribution(
//								getCPLCSum + (valuesDividedByThousandQuotient * getPaise.longValue()));
//					} else {
//						policyContrySummaryEntity.setTotalContribution(
//								getCPLCSum + ((valuesDividedByThousandQuotient + 1) * getPaise.longValue()));
//					}
//				}
//				policyContrySummaryEntity
//						.setStampDuty((policyContrySummaryEntity.getTotalContribution() / 1000) * getPaise);
//				logger.info("StamDuty =(TotalContribution /1000) * getPaise"
//						+ policyContrySummaryEntity.getStampDuty());
//				policyContrySummaryRepository.save(policyContrySummaryEntity);

				return ApiResponseDto.success(RenewalPolicyTMPHelper.entityToDto(renewalPolicyTMPEntity));
			}

			@Override
			@Transactional
			public ApiResponseDto<ValuationMatrixDto> generateValuationForMerger(PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {
				
				ValuationMatrixDto valuationMatrixDto = null;
				
				RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
						.findById(pmstTmpMergerPropsDto.getDestinationTmpPolicyID()).get();
				RenewalValuationBasicTMPEntity valuationBasicEntitySource = renewalValuationBasicTMPRepository
						.findBytmpPolicyId(renewalPolicyTMPEntity.getId()).get();
				RenewalPolicyTMPEntity renewalPolicyTMPEntityForSource = renewalPolicyTMPRepository
						.findById(pmstTmpMergerPropsDto.getSourceTmpPolicyID()).get();
				RenewalValuationBasicTMPEntity valuationBasicEntitySourceForSource = renewalValuationBasicTMPRepository
						.findBytmpPolicyId(renewalPolicyTMPEntityForSource.getId()).get();
				Double modfdPremRateCrdbiltyFctr = 0.0;
				if (valuationBasicEntitySource.getStdPremRateCrdbiltyFctr() != null
						&& valuationBasicEntitySource.getStdPremRateCrdbiltyFctr() != 0) {
					modfdPremRateCrdbiltyFctr = valuationBasicEntitySource.getStdPremRateCrdbiltyFctr();
				} else {
					modfdPremRateCrdbiltyFctr = valuationBasicEntitySource.getModfdPremRateCrdbiltyFctr();
				}

				if (valuationBasicEntitySource.getModfdPremRateCrdbiltyFctr() != null) {
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
				} else {
					gratuityCalculationRepository.calculateGratuity11(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
							valuationBasicEntitySource.getRateTable());
					gratuityCalculationRepository.calculateGratuity21(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
							valuationBasicEntitySource.getRateTable());
					gratuityCalculationRepository.calculateGratuity31(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
							valuationBasicEntitySource.getRateTable());
					gratuityCalculationRepository.calculateGratuity41(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
							valuationBasicEntitySource.getRateTable());
					gratuityCalculationRepository.calculateGratuity51(renewalPolicyTMPEntity.getId(), "POLICY", "TABULAR",
							valuationBasicEntitySource.getRateTable());
				}
			
				// Start Grat Calculation
				List<GratuityCalculationEntity> gratuityCalculationEntities = gratuityCalculationRepository
						.findAllByMergerTmpMemberId(renewalPolicyTMPEntity.getId());

				RenewalValuationBasicTMPEntity valuationBasicEntity = renewalValuationBasicTMPRepository
						.findBytmpPolicyId(renewalPolicyTMPEntity.getId()).get();

				Optional<RenewalValuationMatrixTMPEntity> valuationMatrixEntity = renewalValuationMatrixTMPRepository
						.findBytmpPolicyId(renewalPolicyTMPEntity.getId());
				RenewalValuationMatrixTMPEntity newValuationMatrixEntity = null;
				if (valuationMatrixEntity.isPresent()) {
					newValuationMatrixEntity = valuationMatrixEntity.get();
					newValuationMatrixEntity.setModifiedBy(pmstTmpMergerPropsDto.getCreatedBy());
					newValuationMatrixEntity.setModifiedDate(new Date());
				} else {
					newValuationMatrixEntity = new RenewalValuationMatrixTMPEntity();
					newValuationMatrixEntity.setCreatedBy(pmstTmpMergerPropsDto.getCreatedBy());
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
						pastServiceWithdrawal = pastServiceWithdrawal + gratuityCalculationEntity.getPastServiceBenefitWdl();
					if (gratuityCalculationEntity.getPastServiceBenefitRet() != null)
						pastServiceRetirement = pastServiceRetirement + gratuityCalculationEntity.getPastServiceBenefitRet();
					if (gratuityCalculationEntity.getCurrentServiceBenefitDeath() != null)
						currentServiceDeath = currentServiceDeath + gratuityCalculationEntity.getCurrentServiceBenefitDeath();
					if (gratuityCalculationEntity.getCurrentServiceBenefitWdl() != null)
						currentServiceWithdrawal = currentServiceWithdrawal
								+ gratuityCalculationEntity.getCurrentServiceBenefitWdl();
					if (gratuityCalculationEntity.getCurrentServiceBenefitRet() != null)
						currentServiceRetirement = currentServiceRetirement
								+ gratuityCalculationEntity.getCurrentServiceBenefitRet();
					if (gratuityCalculationEntity.getTotalGra() != null)
						totalGratuity = totalGratuity + gratuityCalculationEntity.getTotalGra();

				}
				valuationBasicEntity.setAccruedGratuity(valuationBasicEntity.getAccruedGratuity() + accruedGratuity);
				valuationBasicEntity.setPastServiceDeath(valuationBasicEntity.getPastServiceDeath() + pastServiceDeath);
				valuationBasicEntity
						.setPastServiceWithdrawal(valuationBasicEntity.getPastServiceWithdrawal() + pastServiceWithdrawal);
				valuationBasicEntity
						.setPastServiceRetirement(valuationBasicEntity.getPastServiceRetirement() + pastServiceRetirement);
				valuationBasicEntity
						.setCurrentServiceDeath(valuationBasicEntity.getCurrentServiceDeath() + currentServiceDeath);
				valuationBasicEntity.setCurrentServiceWithdrawal(
						valuationBasicEntity.getCurrentServiceWithdrawal() + currentServiceWithdrawal);
				valuationBasicEntity.setCurrentServiceRetirement(
						valuationBasicEntity.getCurrentServiceRetirement() + currentServiceRetirement);
				valuationBasicEntity.setTotalGratuity(valuationBasicEntity.getTotalGratuity() + totalGratuity);
				valuationBasicEntity.setNumberOfLives(valuationBasicEntity.getNumberOfLives()+valuationBasicEntitySourceForSource.getNumberOfLives());
				renewalValuationBasicTMPRepository.save(valuationBasicEntity);

				// Renewal Policy Member data Update Grat calculation
				Double currentServiceLiability = 0.0D;
				Double pastServiceLiability = 0.0D;
				Double futureServiceLiability = 0.0D;
				Double premium = 0.0D;
				Double totalSumAssured = 0.0D;
				Double gst = 0.0D;
				Double result = null;
				
				if (renewalPolicyTMPEntity.getGstApplicableId().equals(1L)) {
					StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
					gst = Double.parseDouble(standardCodeEntity.getValue()) / 100;
				}
				new ArrayList<RenewalPolicyTMPMemberEntity>();
				for (GratuityCalculationEntity gratuityCalculationEntity : gratuityCalculationEntities) {

					RenewalPolicyTMPMemberEntity getMemberEntity = renewalPolicyTMPMemberRepository
							.findById(gratuityCalculationEntity.getMemberId()).get();
					
					// calculate the premium
					RenewalPolicyTMPEntity findByTmpPolicyId = renewalPolicyTMPRepository
							.findByTmpPolicyId(pmstTmpMergerPropsDto.getDestinationTmpPolicyID());

					Long memberId = getMemberEntity.getId();
//					Date dateOfJoining = getMemberEntity.getDojToScheme();
					Date dateOfJoining = new Date();

					Date policyEndDate = findByTmpPolicyId.getPolicyEndDate();
					gratuityCalculationEntity = gratuityCalculationRepository.findByMemberId(memberId);
					gratuityCalculationEntity.getId();
					Double lcPremium = gratuityCalculationEntity.getLcPremium();

					Calendar startDate = new GregorianCalendar();
					startDate.setTime(dateOfJoining);

					Calendar endDate = new GregorianCalendar();
					endDate.setTime(policyEndDate);

					int diffInYear = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
					int diffInMonths = diffInYear * 12
							+ (endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH) + 1);

					result = (lcPremium / 12) * diffInMonths;
					
					totalSumAssured = totalSumAssured
							+ (gratuityCalculationEntity.getLcSumAssured() != null ? gratuityCalculationEntity.getLcSumAssured()
									: 0.0D);
					premium = premium + (gratuityCalculationEntity.getLcPremium() != null ? result : 0.0D);
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

					getMemberEntity.setLifeCoverPremium(result); //where is gst? below diff entity
					getMemberEntity.setLifeCoverSumAssured(gratuityCalculationEntity.getLcSumAssured());
					getMemberEntity.setAccruedGratuity(gratuityCalculationEntity.getAccruedGra());
					getMemberEntity.setTotalGratuity(gratuityCalculationEntity.getTotalGra());
					getMemberEntity.setGratCalculationId(gratuityCalculationEntity.getId());
					renewalPolicyTMPMemberRepository.save(getMemberEntity);
				}
				newValuationMatrixEntity.setValuationTypeId(1L);
				newValuationMatrixEntity.setValuationDate(valuationBasicEntity.getValuationEffectivDate());
				newValuationMatrixEntity.setCurrentServiceLiability(
						newValuationMatrixEntity.getCurrentServiceLiability() + currentServiceLiability);
				newValuationMatrixEntity
						.setPastServiceLiability(newValuationMatrixEntity.getPastServiceLiability() + pastServiceLiability);
				newValuationMatrixEntity.setFutureServiceLiability(
						newValuationMatrixEntity.getFutureServiceLiability() + futureServiceLiability);
				//get the destination premium and add it to source's premium, its become total premium
				
				newValuationMatrixEntity.setPremium(newValuationMatrixEntity.getPremium() + premium);
				newValuationMatrixEntity.setGst(newValuationMatrixEntity.getPremium() * gst);
				newValuationMatrixEntity
						.setTotalPremium(newValuationMatrixEntity.getPremium() + (newValuationMatrixEntity.getPremium() * gst));
				newValuationMatrixEntity.setAmountPayable(newValuationMatrixEntity.getPremium()
						+ (newValuationMatrixEntity.getPremium() * gst) + newValuationMatrixEntity.getCurrentServiceLiability()
						+ newValuationMatrixEntity.getPastServiceLiability()
						+ newValuationMatrixEntity.getFutureServiceLiability());
				newValuationMatrixEntity.setAmountReceived(0.0D);
				newValuationMatrixEntity.setBalanceToBePaid(
						newValuationMatrixEntity.getAmountPayable() - newValuationMatrixEntity.getAmountReceived());
				newValuationMatrixEntity.setTotalSumAssured(newValuationMatrixEntity.getTotalSumAssured() + totalSumAssured);

				RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = renewalValuationMatrixTMPRepository
						.save(newValuationMatrixEntity);
				valuationMatrixDto = modelMapper.map(renewalValuationMatrixTMPEntity, ValuationMatrixDto.class);
				
				// create payments to be received against master quotation
				Double oYRGTARenewalPremium = 0D;
				Double lateFee = 0D;
				Double lateFeeGSTAmount = 0D;
//						Float currentServiceLiability = 0F;
				Double pastServiceLiabilty = 0D;
//						Float futureServiceLiability = 0F;
				// Not Way Correct need to run store Procedure
				List<GratuityCalculationEntity> gratuityCalculationEntity = gratuityCalculationRepository
						.findAllByMergerTmpMemberId(renewalPolicyTMPEntity.getId());
				for (GratuityCalculationEntity gratuityCalculationEntry : gratuityCalculationEntity) {
					oYRGTARenewalPremium = oYRGTARenewalPremium
							+ (gratuityCalculationEntry.getLcPremium() != null ? gratuityCalculationEntry.getLcPremium()
									: 0.0D);
					currentServiceLiability = currentServiceLiability
							+ (gratuityCalculationEntry.getCurrentServiceBenefitDeath() != null
									? gratuityCalculationEntry.getCurrentServiceBenefitDeath()
									: 0.0D)
							+ (gratuityCalculationEntry.getCurrentServiceBenefitRet() != null
									? gratuityCalculationEntry.getCurrentServiceBenefitRet()
									: 0.0D)
							+ (gratuityCalculationEntry.getCurrentServiceBenefitWdl() != null
									? gratuityCalculationEntry.getCurrentServiceBenefitWdl()
									: 0.0D);
					pastServiceLiabilty = pastServiceLiabilty
							+ +(gratuityCalculationEntry.getPastServiceBenefitDeath() != null
									? gratuityCalculationEntry.getPastServiceBenefitDeath()
									: 0.0D)
							+ (gratuityCalculationEntry.getPastServiceBenefitRet() != null
									? gratuityCalculationEntry.getPastServiceBenefitRet()
									: 0.0D)
							+ (gratuityCalculationEntry.getPastServiceBenefitWdl() != null
									? gratuityCalculationEntry.getPastServiceBenefitWdl()
									: 0.0D);

//							futureServiceLiability = futureServiceLiability
//									+ (gratuityCalculationEntity.getTerm() != null ? gratuityCalculationEntity.getTerm() : 0.0F);
				}

				Double gstAmount = 0.0D;
				if (renewalPolicyTMPEntity.getGstApplicableId().equals(1L)) {
					StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
					gstAmount = oYRGTARenewalPremium * (Float.parseFloat(standardCodeEntity.getValue()) / 100);
				}

				List<QuotationChargeEntity> quotationChargeEntities = new ArrayList<QuotationChargeEntity>();

				// Need to change ID charge TypeID based on Condition
				quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
						.chargeTypeId(215L).amountCharged(oYRGTARenewalPremium).balanceAmount(0).isActive(true)
						.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());
				quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
						.chargeTypeId(216L).amountCharged(gstAmount).balanceAmount(0).isActive(true)
						.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());

				quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
						.chargeTypeId(220L).amountCharged(lateFee).balanceAmount(0).isActive(true)
						.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());
				quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
						.chargeTypeId(217L).amountCharged(lateFeeGSTAmount).balanceAmount(0).isActive(true)
						.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());

				quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
						.chargeTypeId(218L).amountCharged(pastServiceLiabilty).balanceAmount(0).isActive(true)
						.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());
				quotationChargeEntities.add(QuotationChargeEntity.builder().tmpPolicyId(renewalPolicyTMPEntity.getId())
						.chargeTypeId(219L).amountCharged(currentServiceLiability).balanceAmount(0).isActive(true)
						.createdBy(pmstTmpMergerPropsDto.getCreatedBy()).createdDate(new Date()).build());

//						quotationChargeEntities.add(QuotationChargeEntity.builder().masterQuotationId(renewalPolicyTMPEntity.getId())
//								.chargeTypeId(145L).amountCharged(futureServiceLiability).balanceAmount(0).isActive(true)
//								.createdBy(username).createdDate(new Date()).build());
				quotationChargeRepository.saveAll(quotationChargeEntities);

				RenewalValuationTMPHelper.entityToDtoTemPolicy(renewalPolicyTMPEntity);
				
				return ApiResponseDto.success(valuationMatrixDto);	
			}
			
			@Transactional
			@Override
			public ApiResponseDto<PmstTmpMergerPropsDto> saveMerger(PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {
				PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity = new PmstTmpMergerPropsEntity();
				
				TaskProcessEntity taskProcessEntity = taskProcessRepository.findByProcessName("MERGER");
				TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();
				try {
					if (policyServiceRepository.findByPolicyandType(pmstTmpMergerPropsDto.getSourcemasterPolicyId(),
							pmstTmpMergerPropsDto.getServiceType()).size() > 0) {
						return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
					} else {
						if (pmstTmpMergerPropsDto.getSourcemasterPolicyId() != null
								&& pmstTmpMergerPropsDto.getDestiMasterPolicyId() != null) {
							MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository
									.findById(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
							if (masterPolicyEntity != null) {
								RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
										.copytoTmpForClaim(masterPolicyEntity);
								renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
//								PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
//
//								policyServiceEntitiy.setServiceType("MERGER");
//								policyServiceEntitiy.setPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//								policyServiceEntitiy.setCreatedBy(pmstTmpMergerPropsDto.getCreatedBy());
//								policyServiceEntitiy.setCreatedDate(new Date());
//								policyServiceEntitiy.setIsActive(true);
//								policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);
//
//								renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
								renewalPolicyTMPEntity.setPolicyStatusId(250L);
								renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
								MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder())
										.get();
								TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper
										.copytomastertoTemp(renewalPolicyTMPEntity.getId(), mPHEntity);
								tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
								renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
								renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
								Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
										.findBypolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
								RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
										.copyToTmpSchemeforClaim(policySchemeEntity, renewalPolicyTMPEntity.getId());
								renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

								List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
										.findBypmstPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
								List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
										.copyToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
								memberCategoryRepository.saveAll(getmemberCategoryEntity);

								policyLifeCoverRepository
										.updateISActive(pmstTmpMergerPropsDto.getSourcemasterPolicyId());

								List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
										.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());

								List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
										.copyToTmpLifeCoverforClaim(policyLifeCoverEntity, memberCategoryEntity,
												renewalPolicyTMPEntity.getId());

								renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
//								policyLifeCoverRepository
//										.updateISActive(pmstTmpMergerPropsDto.getSourcemasterPolicyId());

								policyGratuityBenefitRepository
										.updateIsActive(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
								List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
										.findBypolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
								List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
										.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
												renewalPolicyTMPEntity.getId());

								renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
//								policyGratuityBenefitRepository
//										.updateIsActive(pmstTmpMergerPropsDto.getSourcemasterPolicyId());

								Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
										.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
								RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
										.copyToTmpValuationforClaim(policyValuationEntity,
												renewalPolicyTMPEntity.getId());
								renewalValuationTMPRepository.save(renewalValuationTMPEntity);

								Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
										.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
								RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
										.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity,
												renewalPolicyTMPEntity.getId());
								renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

								Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
										.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
								RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
										.copyToTmpValuationBasicClaim(policyValutationBasicEntity,
												renewalPolicyTMPEntity.getId());
								renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

								List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
										.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
								List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
										.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity,
												renewalPolicyTMPEntity.getId());
								renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);
								List<PolicyMemberEntity> policyMemberEntityList = policyMemberRepository
										.findByPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
								for (PolicyMemberEntity policyMemberEntity : policyMemberEntityList) {
									RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
											.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity,
													memberCategoryEntity, renewalPolicyTMPEntity.getId());
									renewalPolicyTMPMemberEntity.setPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
									
									renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);
								}
								
								for (PolicyMemberEntity policyMemberEntity : policyMemberEntityList) {
									RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
											.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity,
													memberCategoryEntity, renewalPolicyTMPEntity.getId());									
									renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);
								}
								pmstTmpMergerPropsEntity.setSourceTmpPolicyID(renewalPolicyTMPEntity.getId());
								
								Double premiumRefund = 0.0;
								Double gstRefund = 0.0;
								
								if ("Y".equals(pmstTmpMergerPropsDto.getIsPremiumRefundRequired())) {
									List<PolicyMemberEntity> memberEntity = policyMemberRepository
											.findByPolicyId(masterPolicyEntity.getId());

									PremiumGstRefundDto premiumGstRefundDto = null;
									for (PolicyMemberEntity member : memberEntity) {
										LeavingMemberDto leavingMemberDto = new LeavingMemberDto();
										leavingMemberDto.setMasterMemberId(member.getId());
										leavingMemberDto.setDateOfLeaving(new Date());
										premiumGstRefundDto = policyServicingCommonService.getRefundablePremiumAndGST(
												masterPolicyEntity.getId(), leavingMemberDto);
										gstRefund += premiumGstRefundDto.getGstRefundable();
										premiumRefund += premiumGstRefundDto.getPremiumRefunable();
									}
									pmstTmpMergerPropsEntity.setPremiumRefundAmount(roundDecimals(premiumRefund));
									pmstTmpMergerPropsEntity.setGstRefundAmount(roundDecimals(gstRefund));
									pmstTmpMergerPropsEntity.setIsPremiumRefundRequired(
											pmstTmpMergerPropsDto.getIsPremiumRefundRequired());
								}
								else {
									pmstTmpMergerPropsEntity.setPremiumRefundAmount(premiumRefund);
									pmstTmpMergerPropsEntity.setGstRefundAmount(gstRefund);
									pmstTmpMergerPropsEntity.setIsPremiumRefundRequired(pmstTmpMergerPropsDto.getIsPremiumRefundRequired());
								}
							}
						}
					}

					if (policyServiceRepository.findByPolicyandType(pmstTmpMergerPropsDto.getDestiMasterPolicyId(),
						pmstTmpMergerPropsDto.getServiceType()).size() > 0) {
					return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
				} else {
					MasterPolicyEntity masterPolicyEntitydes = masterPolicyCustomRepository
							.findById(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
					if (masterPolicyEntitydes != null) {
						RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
								.copytoTmpForClaim(masterPolicyEntitydes);
						renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);//first time
//						PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();
//
//						policyServiceEntitiy.setServiceType("MERGER");
//						policyServiceEntitiy.setPolicyId(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
//						policyServiceEntitiy.setCreatedBy(pmstTmpMergerPropsDto.getCreatedBy());
//						policyServiceEntitiy.setCreatedDate(new Date());
//						policyServiceEntitiy.setIsActive(true);
//						policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

//						renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
						renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
						MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntitydes.getMasterpolicyHolder()).get();
						TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper
								.copytomastertoTemp(renewalPolicyTMPEntity.getId(), mPHEntity);
						tempMPHEntity = tempMPHRepository.save(tempMPHEntity);
						renewalPolicyTMPEntity.setMasterpolicyHolder(tempMPHEntity.getId());
						renewalPolicyTMPEntity.setPolicyStatusId(250L);
						renewalPolicyTMPEntity = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity); //second time
						Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
								.findBypolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
								.copyToTmpSchemeforClaim(policySchemeEntity, renewalPolicyTMPEntity.getId());
						renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

						List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
								.findBypmstPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
								.copyToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
						memberCategoryRepository.saveAll(getmemberCategoryEntity);

//						policyLifeCoverRepository.updateISActive(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
								.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
								.copyToTmpLifeCoverforClaim(policyLifeCoverEntity, memberCategoryEntity,
										renewalPolicyTMPEntity.getId());

						renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
						policyLifeCoverRepository.updateISActive(pmstTmpMergerPropsDto.getDestiMasterPolicyId());

//						policyGratuityBenefitRepository.updateIsActive(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
								.findBypolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
								.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, memberCategoryEntity,
										renewalPolicyTMPEntity.getId());

						renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
						policyGratuityBenefitRepository.updateIsActive(pmstTmpMergerPropsDto.getDestiMasterPolicyId());

						Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
								.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
								.copyToTmpValuationforClaim(policyValuationEntity, renewalPolicyTMPEntity.getId());
						renewalValuationTMPRepository.save(renewalValuationTMPEntity);

						Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
								.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
								.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity,
										renewalPolicyTMPEntity.getId());
						renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

						Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
								.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
								.copyToTmpValuationBasicClaim(policyValutationBasicEntity, renewalPolicyTMPEntity.getId());
						renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

						List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
								.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
								.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity,
										renewalPolicyTMPEntity.getId());
						renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);
						List<PolicyMemberEntity> policyMemberEntityList = policyMemberRepository
								.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());

						List<MemberCategoryEntity> getSourceMemberList = memberCategoryRepository
								.findBypmstPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						
//						for (PolicyMemberEntity policyMemberEntity : policyMemberEntityList) {
//							RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
//									.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity,
//											getSourceMemberList, renewalPolicyTMPEntity.getId());
//							renewalPolicyTMPMemberEntity.setPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
//							renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);
//						List<RenewalPolicyTMPMemberEntity> sourceTmpMemberList = renewalPolicyTMPMemberRepository.findByPolicyId(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
						renewalPolicyTMPMemberRepository.updateSourceTmpPolicyIdAsDesti(pmstTmpMergerPropsDto.getDestiMasterPolicyId(), renewalPolicyTMPEntity.getId());
//						}
						
						pmstTmpMergerPropsEntity.setDestinationTmpPolicyID(renewalPolicyTMPEntity.getId());
					}
				}

				Long statusIdForMerger = 250L;	
					
				pmstTmpMergerPropsEntity.setCreatedBy(pmstTmpMergerPropsDto.getCreatedBy());
				pmstTmpMergerPropsEntity.setCreatedDate(new Date());
				pmstTmpMergerPropsEntity.setMergerRequestDate(new Date());
				pmstTmpMergerPropsEntity.setIsActive(true);
				pmstTmpMergerPropsEntity.setMergerType(pmstTmpMergerPropsDto.getMergerType());
				pmstTmpMergerPropsEntity.setDestinationPriorFundValue(pmstTmpMergerPropsDto.getDestinationPriorFundValue());
	            pmstTmpMergerPropsEntity.setStatusID(statusIdForMerger);			
				pmstTmpMergerPropsEntity.setSourcePriorFundValue(pmstTmpMergerPropsDto.getSourcePriorFundValue());
				pmstTmpMergerPropsEntity.setSourcePriorTotalFund(pmstTmpMergerPropsDto.getSourcePriorTotalFund());
				pmstTmpMergerPropsEntity.setDestinationPriorTotalValue(pmstTmpMergerPropsDto.getDestinationPriorTotalValue());
				// pmstTmpMergerPropsEntity.setMergerRequestNumber(
				// RenewalPolicyTMPHelper.nextQuotationNumber(pmstTmpMergerPropsRepository.maxMergerRequestNo()));
				pmstTmpMergerPropsEntity.setMergerRequestNumber(commonService.getSequence(HttpConstants.MERGER));
				pmstTmpMergerPropsEntity.setDestinationPolicyID(pmstTmpMergerPropsDto.getDestiMasterPolicyId());
				pmstTmpMergerPropsEntity.setSourcePolicyID(pmstTmpMergerPropsDto.getSourcemasterPolicyId());
				pmstTmpMergerPropsRepository.save(pmstTmpMergerPropsEntity);
				
				//
				
				taskAllocationEntity.setTaskStatus(statusIdForMerger.toString());
				taskAllocationEntity.setRequestId(pmstTmpMergerPropsEntity.getMergerRequestNumber());
				taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
//				taskAllocationEntity.setLocationType(quotationRenewalDto.getUnitCode());
				taskAllocationEntity.setCreatedBy(pmstTmpMergerPropsEntity.getCreatedBy());
				taskAllocationEntity.setCreatedDate(new Date());
				taskAllocationEntity.setModulePrimaryId(pmstTmpMergerPropsEntity.getId());
				taskAllocationEntity.setIsActive(true);
				taskAllocationRepository.save(taskAllocationEntity);
				

			} catch (Exception e) {
				e.getStackTrace();
			}

			PmstTmpMergerPropsDto responseDto = CommonPolicyServiceHelper.entitytoDto(pmstTmpMergerPropsEntity);
			responseDto.setDestiMasterPolicyId(pmstTmpMergerPropsEntity.getDestinationPolicyID());
			responseDto.setSourcemasterPolicyId(pmstTmpMergerPropsEntity.getSourcePolicyID());
			return ApiResponseDto.created(responseDto);
		}
			
			
			@Override
			public ApiResponseDto<List<MemberCategoryDto>> memberCategoryDetailsByPolicynumber(String policyNumber) {
				// TODO Auto-generated method stub
				MasterPolicyEntity masterPolicyEntity=masterPolicyRepository.findByPolicyNumberAndIsActiveTrue(policyNumber);
				if(masterPolicyEntity !=null) {
					List<MemberCategoryEntity> memberCategoryEntities=memberCategoryRepository.findBypmstPolicy(masterPolicyEntity.getId());
					if(memberCategoryEntities !=null && memberCategoryEntities.size()>0) {	
						return ApiResponseDto.success(memberCategoryEntities.stream().map(CommonHelper::entityToDto).collect(Collectors.toList()));
					}else {
						return ApiResponseDto.notFound(null);
			}
			}else {
				return ApiResponseDto.notFound(null);
			}
			}

			@Override
			public ApiResponseDto<ValuationMatrixDto> getValuationMatrixForMerger(Long tempPolicyId) {

				try {
					RenewalValuationMatrixTMPEntity valuationMatrixTmpEntity = renewalValuationMatrixTMPRepository
							.findAllBytmpPolicyId(tempPolicyId);
					if (valuationMatrixTmpEntity != null) {
						return ApiResponseDto
								.success(modelMapper.map(valuationMatrixTmpEntity, ValuationMatrixDto.class));
					}
				} catch (Exception e) {
				}
				return ApiResponseDto.notFound(null);
			}

			@Override
			public ApiResponseDto<PremiumGstRefundDto> getOverViewOfGstAndPremium(String policyNumber) {
				try {
					MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findByPolicyNumber(policyNumber);
					if (masterPolicyEntity == null) {
						return ApiResponseDto.notFound(null);
					}
					List<PolicyMemberEntity> memberEntity = policyMemberRepository
							.findByPolicyId(masterPolicyEntity.getId());
					if (memberEntity == null) {
						return ApiResponseDto.notFound(null);
					}
					
					Double premiumRefund = 0.0;
					Double gstRefund = 0.0;
					PremiumGstRefundDto premiumGstRefundDto = null;

					for (PolicyMemberEntity member : memberEntity) {
						LeavingMemberDto leavingMemberDto = new LeavingMemberDto();
						leavingMemberDto.setMasterMemberId(member.getId());
						leavingMemberDto.setDateOfLeaving(new Date());
						premiumGstRefundDto = policyServicingCommonService
								.getRefundablePremiumAndGST(masterPolicyEntity.getId(), leavingMemberDto);
						gstRefund += premiumGstRefundDto.getGstRefundable();
						premiumRefund += premiumGstRefundDto.getPremiumRefunable();
					}
					premiumGstRefundDto.setPremiumRefunable(roundDecimals(premiumRefund));
					premiumGstRefundDto.setGstRefundable(roundDecimals(gstRefund));
					return ApiResponseDto.success(premiumGstRefundDto);
				} catch (Exception e) {
				}
				return ApiResponseDto.notFound(null);
			}
			
			public static double roundDecimals(double value) {
				double scalingFactor = Math.pow(10, 2);
				return Math.round((value * scalingFactor) / scalingFactor);
			}
	}

		
			



	
	
