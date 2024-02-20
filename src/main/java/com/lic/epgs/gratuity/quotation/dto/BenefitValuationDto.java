package com.lic.epgs.gratuity.quotation.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BenefitValuationDto {
	
	private Long categoryId;
	private String categoryName;
	private Integer retirementAge;
	private Integer numberOfDaysWage;
	private Integer numberOfWorkingDaysPerMonth;
	private Double gratuityCellingAmount;
	private Double lcas;
	private String rateTable;

}
