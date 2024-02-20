package com.lic.epgs.gratuity.mph.dto;

import java.io.Serializable;
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
public class TempMPHRepresentativeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long Id;
	
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String addressType;
	private String cityLocality;
	private String stateName;
	private String district;
	private String countryCode;
	private String emailId;
	private Long mobileNO;
	private Long landLineNo;
	private Long pincode;
	private String representativeCode;
	private String representativeName;

	private Boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;
	private Date modifiedDate;

	private Long endorsementId;
	private Long pmstId;
	
	
	private String recordType;

}
