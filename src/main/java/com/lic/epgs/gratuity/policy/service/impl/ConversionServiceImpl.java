package com.lic.epgs.gratuity.policy.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.dto.PmstTmpConversionPropsDto;
import com.lic.epgs.gratuity.policy.entity.PmstTmpConversionPropsEntity;
import com.lic.epgs.gratuity.policy.helper.CommonPolicyServiceHelper;
import com.lic.epgs.gratuity.policy.repository.PmstTmpConversionPropsRepository;
import com.lic.epgs.gratuity.policy.service.ConversionService;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
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
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.repository.RenewalValuationTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationWithdrawalTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationBasicTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationMatrixTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationWithdrawalTMPRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleHistoryRepository;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyMasterValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyMasterValuationRepository;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyValuationHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateRepository;


@Service
public class ConversionServiceImpl implements ConversionService{

	@Autowired
	private PmstTmpConversionPropsRepository pmstTmpConversionPropsRepository;
	
	@Autowired
	private PolicyServiceRepository policyServiceRepository;
	
	@Autowired
    private MasterPolicyCustomRepository masterPolicyCustomRepository;
	
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
	private PolicyMemberRepository policyMemberRepository;




	@Autowired
	private MPHRepository mPHRepository;

	@Autowired
	private TempMPHRepository tempMPHRepository;

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

	@Override
	public ApiResponseDto<PolicyDto> getConversionPolicyDetail(String policyNumber) {
		
		PolicyDto policyDto =null;
		MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findBypolicyNumber(policyNumber);
		if(masterPolicyEntity!= null) {
		String getProductName = masterPolicyEntity.getProductVariant().substring(masterPolicyEntity.getProductVariant().length() - 2);
		System.out.println("get prod"+getProductName);
		
		if(getProductName.equals("V1")  ||getProductName.equals("V2")  ) {
		if(masterPolicyEntity.getPolicyStatusId() == 127 || masterPolicyEntity.getPolicyStatusId()  == 123 ) {
			
			 policyDto =new ModelMapper().map(masterPolicyEntity, PolicyDto.class);
		}
		}
		}
		return ApiResponseDto.created(policyDto);
	}

	@Override
	public ApiResponseDto<PmstTmpConversionPropsDto> saveConversionservice(
			PmstTmpConversionPropsDto pmstTmpConversionPropsDto) {
	PmstTmpConversionPropsEntity pmstTmpConversionPropsEntity = new PmstTmpConversionPropsEntity();	
		
		try {
			if (policyServiceRepository
					.findByPolicyandType(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId(), pmstTmpConversionPropsDto.getServiceType())
					.size() > 0) {
				return ApiResponseDto.errorMessage(null, null, "already in Progress for this ID");
			}else {
			if (pmstTmpConversionPropsDto.getConverFromPMSTPolicyId() != 0) {
			     MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.
			    		 findById(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());
			     if (masterPolicyEntity != null)  {
			    		RenewalPolicyTMPEntity renewalPolicyTMPEntity = PolicyClaimCommonHelper
								.copytoTmpForClaim(masterPolicyEntity);
						renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
						PolicyServiceEntitiy policyServiceEntitiy = new PolicyServiceEntitiy();

						policyServiceEntitiy.setServiceType("conversion");
						policyServiceEntitiy.setPolicyId(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());
						policyServiceEntitiy.setCreatedBy(pmstTmpConversionPropsDto.getCreatedBy());
						policyServiceEntitiy.setCreatedDate(new Date());
						policyServiceEntitiy.setIsActive(true);
						policyServiceEntitiy = policyServiceRepository.save(policyServiceEntitiy);

						renewalPolicyTMPEntity.setPolicyServiceId(policyServiceEntitiy.getId());
						renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
						
						Optional<PolicySchemeEntity> policySchemeEntity = policySchemeRuleRepository
								.findBypolicyId(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());
						RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity = PolicyClaimCommonHelper
								.copyToTmpSchemeforClaim(policySchemeEntity, renewalPolicyTMPEntity.getId());
						renewalSchemeruleTMPRepository.save(renewalSchemeruleTMPEntity);

						List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository
								.findBypmstPolicyId(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());
						List<MemberCategoryEntity> getmemberCategoryEntity = PolicyClaimCommonHelper
								.copyToTmpMemberforClaim(memberCategoryEntity, renewalPolicyTMPEntity.getId());
						memberCategoryRepository.saveAll(getmemberCategoryEntity);

						List<PolicyLifeCoverEntity> policyLifeCoverEntity = policyLifeCoverRepository
								.findByPolicyId(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());
						List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = PolicyClaimCommonHelper
								.copyToTmpLifeCoverforClaim(policyLifeCoverEntity,
										memberCategoryEntity, renewalPolicyTMPEntity.getId());
						
						renewalLifeCoverTMPRepository.saveAll(renewalLifeCoverTMPEntity);
						policyLifeCoverRepository.updateISActive(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());

						List<PolicyGratuityBenefitEntity> policyGratuityBenefitEntity = policyGratuityBenefitRepository
								.findBypolicyId(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());
						List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = PolicyClaimCommonHelper
								.copyToTmpGratuityforClaim(policyGratuityBenefitEntity, 
										memberCategoryEntity, renewalPolicyTMPEntity.getId());
						
						renewalGratuityBenefitTMPRepository.saveAll(renewalGratuityBenefitTMPEntity);
						policyGratuityBenefitRepository.updateIsActive(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());

						Optional<PolicyMasterValuationEntity> policyValuationEntity = policyMasterValuationRepository
								.findByPolicyId(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());
						RenewalValuationTMPEntity renewalValuationTMPEntity = PolicyClaimCommonHelper
								.copyToTmpValuationforClaim(policyValuationEntity, renewalPolicyTMPEntity.getId());
						renewalValuationTMPRepository.save(renewalValuationTMPEntity);

						Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository
								.findByPolicyId(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());
						RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity = PolicyClaimCommonHelper
								.copyToTmpValuationMatrixforClaim(policyValuationMatrixEntity, renewalPolicyTMPEntity.getId());
						renewalValuationMatrixTMPRepository.save(renewalValuationMatrixTMPEntity);

						Optional<PolicyValutationBasicEntity> policyValutationBasicEntity = policyValuationBasicRepository
								.findByPolicyId(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());
						RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity = PolicyClaimCommonHelper
								.copyToTmpValuationBasicClaim(policyValutationBasicEntity, renewalPolicyTMPEntity.getId());
						renewalValuationBasicTMPRepository.save(renewalValuationBasicTMPEntity);

						List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntity = policyValuationWithdrawalRateRepository
								.findByPolicyId(pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());
						List<RenewalValuationWithdrawalTMPEntity> renewalValuationWithdrawalTMPEntity = PolicyClaimCommonHelper
								.copyToTmpValuationWithdrawlClaim(policyValuationWithdrawalRateEntity,
										renewalPolicyTMPEntity.getId());
						renewalValuationWithdrawalTMPRepository.saveAll(renewalValuationWithdrawalTMPEntity);
						List<PolicyMemberEntity> policyMemberEntityList = policyMemberRepository.findByPolicyId(
								pmstTmpConversionPropsDto.getConverFromPMSTPolicyId());
						for(PolicyMemberEntity policyMemberEntity:policyMemberEntityList) {
						RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = PolicyClaimCommonHelper
								.copyToTmpIndividualMemberClaim(policyMemberEntity, getmemberCategoryEntity,
										memberCategoryEntity, renewalPolicyTMPEntity.getId());
						renewalPolicyTMPMemberRepository.save(renewalPolicyTMPMemberEntity);
						}

						MPHEntity mPHEntity = mPHRepository.findById(renewalPolicyTMPEntity.getMasterpolicyHolder()).get();
						TempMPHEntity tempMPHEntity = PolicyClaimCommonHelper.copytomastertoTemp(renewalPolicyTMPEntity.getId(),
								mPHEntity);
						tempMPHRepository.save(tempMPHEntity);
						pmstTmpConversionPropsEntity.setTmpPolicyID(renewalPolicyTMPEntity.getId());
						
				}
			     
			}
			try {
				pmstTmpConversionPropsEntity.setCreatedBy(pmstTmpConversionPropsDto.getCreatedBy());
				pmstTmpConversionPropsEntity.setCreatedDate(new Date());
				pmstTmpConversionPropsEntity.setConversionRequestDate(pmstTmpConversionPropsDto.getConversionRequestDate());
				pmstTmpConversionPropsEntity.setIsActive(true);
				pmstTmpConversionPropsEntity.setPolicyConversionDate(pmstTmpConversionPropsDto.getPolicyConversionDate());
				pmstTmpConversionPropsEntity.setTotalFundValue(pmstTmpConversionPropsDto.getTotalFundValue());
				pmstTmpConversionPropsEntity.setConversionRequestNumber(RenewalPolicyTMPHelper
						.nextQuotationNumber(pmstTmpConversionPropsRepository.maxConversionRequestNo()));
				pmstTmpConversionPropsRepository.save(pmstTmpConversionPropsEntity);
			} catch (Exception e) {
				System.out.println("Save TmpMergerProps"+e);
			}
			}
			
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		return ApiResponseDto.created(CommonPolicyServiceHelper.entitytoDto(pmstTmpConversionPropsEntity));
	}


	@Override
	public ApiResponseDto<PmstTmpConversionPropsDto> updateconversion(Long id,
			PmstTmpConversionPropsDto pmstTmpConversionPropsDto) {

		PmstTmpConversionPropsEntity pmstTmpConversionPropsEntity = pmstTmpConversionPropsRepository.findById(id).get();
		
		pmstTmpConversionPropsEntity.setConversionRequestDate(pmstTmpConversionPropsDto.getConversionRequestDate());
		pmstTmpConversionPropsEntity.setPolicyConversionDate(pmstTmpConversionPropsDto.getPolicyConversionDate());
		pmstTmpConversionPropsEntity.setTotalFundValue(pmstTmpConversionPropsDto.getTotalFundValue());

		pmstTmpConversionPropsEntity.setStatusID(pmstTmpConversionPropsDto.getStatusID());
		pmstTmpConversionPropsEntity.setModifiedBy(pmstTmpConversionPropsDto.getModifiedBy());
		pmstTmpConversionPropsEntity.setModifiedDate(new Date());
		pmstTmpConversionPropsRepository.save(pmstTmpConversionPropsEntity);
		
		return ApiResponseDto.success(CommonPolicyServiceHelper.entitytoDto(pmstTmpConversionPropsEntity));
	}

	@Override
	public ApiResponseDto<PmstTmpConversionPropsDto> conversionStatusUpdate(
			PmstTmpConversionPropsDto pmstTmpConversionPropsDto) {
		pmstTmpConversionPropsRepository.findById(pmstTmpConversionPropsDto.getId()).get();
		PmstTmpConversionPropsEntity newPmstTmpConversionPropsEntity=new ModelMapper().map(pmstTmpConversionPropsDto, PmstTmpConversionPropsEntity.class);	
		newPmstTmpConversionPropsEntity.setStatusID(pmstTmpConversionPropsDto.getStatusID());
				return ApiResponseDto.success(CommonPolicyServiceHelper.entitytoDto(pmstTmpConversionPropsRepository.save(newPmstTmpConversionPropsEntity)));
	
	}

	// add dto and policyServiceRepository add get policy number,get service type  
	// inprogresssearchfilter
	//pmstTmpConversionPropsRepository add get policy number
	
	@Override
	public ApiResponseDto<List<PmstTmpConversionPropsDto>> inprogresssearchfilter(PmstTmpConversionPropsDto pmstTmpConversionPropsDto) {
		
		PolicyServiceEntitiy policyServiceEntitiy = policyServiceRepository.findcheckserviceIsActive(pmstTmpConversionPropsDto.getPolicyNumber(),pmstTmpConversionPropsDto.getServiceType());
		if(policyServiceEntitiy!=null) {

			List<PmstTmpConversionPropsEntity> pmstTmpConversionPropsEntity = pmstTmpConversionPropsRepository.findBysourceTmpPolicyIDinprogress(pmstTmpConversionPropsDto.getPolicyNumber());
		
			return ApiResponseDto.success(pmstTmpConversionPropsEntity.stream().map(CommonPolicyServiceHelper::entitytoDto).collect(Collectors.toList()));
		}else {
		return ApiResponseDto.success(null);
		}
	}

	// add dto and policyServiceRepository add get policy number,get service type  
	//ExistingSearchFilter
		//pmstTmpConversionPropsRepository add get policy number
	
	@Override
	public ApiResponseDto<List<PmstTmpConversionPropsDto>> getExistingSearchFilter(PmstTmpConversionPropsDto pmstTmpConversionPropsDto) {
		// due to the error, removed the second parameter
//		PolicyServiceEntitiy policyServiceEntitiy = policyServiceRepository.findcheckserviceInActive(pmstTmpConversionPropsDto.getPolicyNumber(),pmstTmpConversionPropsDto.getServiceType());
		PolicyServiceEntitiy policyServiceEntitiy = policyServiceRepository.findcheckserviceInActive(pmstTmpConversionPropsDto.getPolicyNumber());
		if(policyServiceEntitiy!=null) {
			List<PmstTmpConversionPropsEntity> pmstTmpConversionPropsEntity = pmstTmpConversionPropsRepository.findBysourceTmpPolicyIDExisting(pmstTmpConversionPropsDto.getPolicyNumber());
			
			return ApiResponseDto.success(pmstTmpConversionPropsEntity.stream().map(CommonPolicyServiceHelper::entitytoDto).collect(Collectors.toList()));
		}else {
			return ApiResponseDto.success(null);
			}
		}


	@Override
	public ApiResponseDto<PmstTmpConversionPropsDto> getConversionDetailById(Long id) {
		
		PmstTmpConversionPropsEntity pmstTmpConversionPropsEntity = pmstTmpConversionPropsRepository
					.findById(id).get();
		return ApiResponseDto.success(CommonPolicyServiceHelper.entitytoDto(pmstTmpConversionPropsEntity));

	}

}
