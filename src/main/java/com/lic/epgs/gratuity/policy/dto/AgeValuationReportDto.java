package com.lic.epgs.gratuity.policy.dto;

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
public class AgeValuationReportDto {
	
	private Integer category;
	private Integer age;
	private Integer count;
	private Integer pastService;
	private Double salary;
	private Double accruedGratuity;
	private Double totalServiceGratuity;
	private Double lifeCover;
	private Double lifeCoverPremium;

}
