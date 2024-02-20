package com.lic.epgs.gratuity.quotation.lifecover.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vigneshwaran
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LifeCoverDto {
	private Long id;
	private Long quotationId;

	private Long lifeCoverTypeId;
	private Long categoryId;
	private Double sumAssured;
	private Integer retirementAge;
	private Double retentionLimit;
	private Long noOfMonths;
	private Double minimumSumAssured;	
	private Double maximumSumAssured;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
}
