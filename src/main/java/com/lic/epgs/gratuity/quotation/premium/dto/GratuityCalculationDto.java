package com.lic.epgs.gratuity.quotation.premium.dto;

import java.util.Date;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GratuityCalculationDto {
	private Long id;
	 private Long memberId;
	private Long term;
	private Long g2Der;
	private Long dobDerYear;
	private Long dobDerAge;
	private Long dojDerYear;
	private Long pastService;
	private Double pastServiceDeath;
	private Double pastServiceWdl;
	private Double pastServiceRet;
	private Long retDerYear;
	private Long totalService;
	private Long gratuityF1;
	private Double benefitsDeath;
	private Double benefitsWdl;
	private Double benefitsRet;
	private Double currentServiceDeath;
	private Double currentServiceWdl;
	private Double currentServiceRet;
	private Double beneCurrentServiceDeath;
	private Double beneCurrentServiceWith;
	private Double beneCurrentServiceRet;
	private Double pastServiceBenefitDeath;
	private Double pastServiceBenefitWdl;
	private Double pastServiceBenefitRet;
	private Long mvc;
	private Double currentServiceBenefitDeath;
	private Double currentServiceBenefitWdl;
	private Double currentServiceBenefitRet;
	private Double pastServiceBenefit;
	private Double currentServiceBenefit;
	private Double accruedGra;
	private Double accruedGrat;
	private Double totalGra;
	private Double totalGrat;
	private Double lcSumAssured;
	private Double lcPremium;
	private Long isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

	public GratuityCalculationDto entityToDto(GratuityCalculationEntity entity) {
		if (entity == null)
			return null;
		else
			return new ModelMapper().map(entity, GratuityCalculationDto.class);
	}
}
