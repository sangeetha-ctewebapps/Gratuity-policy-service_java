package com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto;

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
public class AOCMExpenseRateDto {
	private Long fromGroupSize;
	private Long Id;
	private Long toGroupSize;
	private Double premiumRelatedExpense;
	private Double saReleatedExpense;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	

}
