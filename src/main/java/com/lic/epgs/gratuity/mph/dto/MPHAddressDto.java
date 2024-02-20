package com.lic.epgs.gratuity.mph.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHRepresentativesEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MPHAddressDto implements Serializable {
	
	private Long id;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String addressType;
	private long cityId;
	private String cityLocality;
	private long stateId;
	private String stateName;
	private long districtId;
	private String district;
	private long pincode;
	private long countryId;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
}
