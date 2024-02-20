package com.lic.epgs.gratuity.mph.dto;

import java.io.Serializable;

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

public class TempMPHAddressDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	
	private String addressType;;
	private Long cityId;
	private String cityLocality;
	private Long stateId;
	private String stateName;
	private Long districtId;
	private String district;
	private Long pincode;
	private Long countryId;
	public Boolean isActive;
	private String createdBy;

	private String createdDate;

	private String modifiedBy;
	private String modifiedDate;

	private Long endorsementId;

	private Long pmstId;

	private String recordType;

}
