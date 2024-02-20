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
public class AOCMCredibilityFactorDto {
	
	private Long Id;
	private Long fromLives;
	private String variant;
	private Long toLives;
	private Double credibility;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	

}
