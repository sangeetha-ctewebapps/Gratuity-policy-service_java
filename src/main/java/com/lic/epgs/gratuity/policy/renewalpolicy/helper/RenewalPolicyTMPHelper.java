package com.lic.epgs.gratuity.policy.renewalpolicy.helper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalsGratuityBenefitPropsTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.entity.RenewalValuationTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationBasicTMPRepository;
import com.lic.epgs.gratuity.quotation.dto.BenefitValuationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationPDFGenerationDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;
import com.lic.epgs.gratuity.quotation.valuation.entity.MasterValuationEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationBasicEntity;

public class RenewalPolicyTMPHelper {

	public static RenewalPolicyTMPEntity pmsttoTmp(MasterPolicyEntity masterPolicyEntity) {
		
		return new  ModelMapper().map(masterPolicyEntity, RenewalPolicyTMPEntity.class);
	}
	
public static RenewalPolicyTMPEntity histtoTmp(PolicyHistoryEntity policyHistoryEntities) {
		
		return new  ModelMapper().map(policyHistoryEntities, RenewalPolicyTMPEntity.class);
	}

	public static Long nextQuotationNumber(Long maxQuotationNumber) {
		maxQuotationNumber = maxQuotationNumber == null ? 1 : maxQuotationNumber + 1;
		return maxQuotationNumber;
	}

	public static RenewalPolicyTMPDto entityToDto(RenewalPolicyTMPEntity renewalPolicyTMPEntity) {
		return new ModelMapper().map(renewalPolicyTMPEntity, RenewalPolicyTMPDto.class);
	}

	public static List<QuotationPDFGenerationDto> generateRenewalQuotationReport(String[] get, TempMPHEntity mphEntity,
			List<MemberCategoryEntity> memberCategoryEntity, List<RenewalLifeCoverTMPEntity> renewalLifeCoverEntity,
			List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitEntity,
			RenewalValuationBasicTMPEntity getRenewalValuationBasicEntity,
			RenewalValuationTMPEntity getRenewalValuation, RenewalPolicyTMPEntity renewalPolicyTMPEntity) {
		List<QuotationPDFGenerationDto> addQuotationPDFGenerationDto = new ArrayList<QuotationPDFGenerationDto>();
		List<BenefitValuationDto> addbenefitValuationDto = new ArrayList<BenefitValuationDto>();
		QuotationPDFGenerationDto newQuotationPDFGenerationDto = new QuotationPDFGenerationDto();
		
		
		for (MemberCategoryEntity getMemberCategory : memberCategoryEntity) {
			for (RenewalLifeCoverTMPEntity getMasterLifeCoverEntity : renewalLifeCoverEntity) {
				for (RenewalGratuityBenefitTMPEntity getGratuityBenefitEntity : renewalGratuityBenefitEntity) {

					for (RenewalsGratuityBenefitPropsTMPEntity getGratuityBenefitProps : getGratuityBenefitEntity
							.getRenewalgratuityPropsBenefit()) {

						if (getGratuityBenefitEntity.getCategoryId().equals(getMemberCategory.getId())
								&& getMasterLifeCoverEntity.getCategoryId().equals(getMemberCategory.getId())) {

							newQuotationPDFGenerationDto.setTotalMember(Long.parseLong(get[0]));
							newQuotationPDFGenerationDto.setAverageAge(Float.parseFloat(get[1]));
							newQuotationPDFGenerationDto.setAvgMonthlySalary(Float.parseFloat(get[2]));
							newQuotationPDFGenerationDto.setAvgPastService(Float.parseFloat(get[3]));
							newQuotationPDFGenerationDto.setTotalServiceGratuity(get[4]);
							newQuotationPDFGenerationDto.setAccuredGratuity(Long.parseLong(get[5]));
							newQuotationPDFGenerationDto.setLcas(Long.parseLong(get[6]));
							newQuotationPDFGenerationDto.setLcPremium(Long.parseLong(get[7]));
							newQuotationPDFGenerationDto.setGst(Long.parseLong(get[8]));
							newQuotationPDFGenerationDto.setPastServiceBenefit(Long.parseLong(get[9]));
							newQuotationPDFGenerationDto.setCurrentServiceBenefit(Long.parseLong(get[10]));
							newQuotationPDFGenerationDto.setSalaryEscalation(getRenewalValuation.getSalaryEscalation() * 100);
							newQuotationPDFGenerationDto.setMphName(mphEntity.getMphName());
							newQuotationPDFGenerationDto.setMphEmail(mphEntity.getEmailId());
							newQuotationPDFGenerationDto.setMphMobileNo(mphEntity.getMobileNo());
							for (TempMPHAddressEntity getMPHAddress : mphEntity.getMphAddresses()) {
								newQuotationPDFGenerationDto.setMphAddress1(
										getMPHAddress.getAddressLine1() != null ? getMPHAddress.getAddressLine1() : "");
								newQuotationPDFGenerationDto.setMphAddress2(
										getMPHAddress.getAddressLine2() != null ? getMPHAddress.getAddressLine2() : "");
								newQuotationPDFGenerationDto.setMphAddress3(
										getMPHAddress.getAddressLine3() != null ? getMPHAddress.getAddressLine3() : "");
								newQuotationPDFGenerationDto.setMphAddressType(
										getMPHAddress.getAddressType() != null ? getMPHAddress.getAddressType() : "");
							}
							newQuotationPDFGenerationDto.setDateOfApproval(renewalPolicyTMPEntity.getCreatedDate());
//						newQuotationPDFGenerationDto.setProposalNumber(renewalPolicyTMPEntity.getProposalNumber());
							newQuotationPDFGenerationDto
									.setQuotationNumber(renewalPolicyTMPEntity.getQuotationNumber());
							newQuotationPDFGenerationDto.setProposalNumber(renewalPolicyTMPEntity.getPolicyNumber());
//						newQuotationPDFGenerationDto
//								.setDateOfCommencement(renewalPolicyTMPEntity.getDateOfCommencement());

							BenefitValuationDto benefitValuationDto = new BenefitValuationDto();
							benefitValuationDto.setCategoryId(getMemberCategory.getId());
							benefitValuationDto.setCategoryName(getMemberCategory.getName());
							benefitValuationDto.setRetirementAge(getGratuityBenefitProps.getRetirementAge());
							benefitValuationDto.setRateTable(getRenewalValuationBasicEntity.getRateTable());
							benefitValuationDto.setLcas(getMasterLifeCoverEntity.getMaximumSumAssured());
							benefitValuationDto
									.setGratuityCellingAmount(getGratuityBenefitProps.getGratuityCellingAmount());
							benefitValuationDto.setNumberOfDaysWage(getGratuityBenefitProps.getNumberOfDaysWage());
							benefitValuationDto.setNumberOfWorkingDaysPerMonth(
									getGratuityBenefitProps.getNumberOfWorkingDaysPerMonth());
							addbenefitValuationDto.add(benefitValuationDto);
						}
					}
				}
			}
			newQuotationPDFGenerationDto.setBenefitValuation(addbenefitValuationDto);
		}
		addQuotationPDFGenerationDto.add(newQuotationPDFGenerationDto);
		return addQuotationPDFGenerationDto;
	}

	
}
