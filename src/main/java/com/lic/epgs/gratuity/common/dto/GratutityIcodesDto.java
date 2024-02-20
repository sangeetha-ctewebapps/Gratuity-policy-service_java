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
public class GratutityIcodesDto {

	private Long id;
	
	private Long variantName;
	
	private Long icodeBusinessLine;
	
	private Long icodeProductLine;
	
	private Long icodeVarient;
	
	private Long icodeBuinessType;
	
	private Long icodeParticipatingType;
	
	private Long icodeBusinessSegment;
	
	private Long isActive;
	
	private Long createdBy;
	
	private Long createdDate;
	
	private Long modifiedBy;
	
	private Long modifiedDate;
	

}
