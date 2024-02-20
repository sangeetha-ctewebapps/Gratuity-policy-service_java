package com.lic.epgs.gratuity.mph.dto;

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
public class MPHRepresentativeDto {

	private Long id;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String addressType;
	private String cityLocality;
	private String stateName;
	private String district;
	private String countryCode;
	private String emailId;
	private Long mobileNo;
	private Long landlineNo;
	private Long pincode;
	private String representativeCode;
	private String representativeName;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long pmstId;
	private String recordType;
	
	
	
	
	
	
	
	
	
	
	
	
	
}
