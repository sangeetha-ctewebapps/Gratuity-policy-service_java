package com.lic.epgs.gratuity.quotation.valuationmatrix.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValuationBasicDto {

	private Long id;
	private Long quotationId;
	private Long valuationTypeId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date valuationEffectivDate;
	private String referenceNumber;
	private Long maximumService;
	private Long minimumServiceForDeath;
	private Long minimumServiceForWithdrawal;
	private Long minimumServiceForRetirement;
	private Integer numberOfLives;
	private Float gratuityAmountCelling;
	private String rateTable;
	private Float maxLifeCoverSumAssured;
	private Float maxSalary;
	private Float	  pastServiceDeath;
	private Float pastServiceWithdrawal;
	private Float pastServiceRetirement;
	private Float currentServiceDeath;
	private Float currentServiceWithdrawal;
	private Float currentServiceRetirement;
	private Float accruedGratuity;
	private Float totalGratuity;
	private Float maximumSalary;
	private Float minimumSalary;
	private Boolean isActive;
	private String createdBy;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	
}
