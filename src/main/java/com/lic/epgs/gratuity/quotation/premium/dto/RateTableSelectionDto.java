package com.lic.epgs.gratuity.quotation.premium.dto;

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
 public class RateTableSelectionDto {
	
	private Long id;
	private String riskGroup;
	private Long nonSkilledEmpFrom;
	private Long nonSkilledEmpTo;
	private String nonSkilledDesc;
	private String rateTable;
	private Boolean isActive;
	private String createdBy;
	private Date  createdDate;
	private String  modifiedBy;
	private Date  modifiedDate;

}
