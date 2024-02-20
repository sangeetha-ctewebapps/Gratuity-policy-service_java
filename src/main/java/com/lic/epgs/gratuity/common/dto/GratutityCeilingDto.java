package com.lic.epgs.gratuity.common.dto;

import java.util.Date;

import javax.persistence.Column;

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
public class GratutityCeilingDto {
	
	private Long id;
	

	private Double gratuityCeilingAmount;
	
	
	private Date effectiveStartDate;
	

	private Date effectiveEndDate;
	
	private Boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;

}
