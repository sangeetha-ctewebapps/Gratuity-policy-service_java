package com.lic.epgs.gratuity.policy.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseDto {

	private String unitId;
	private String unitCode;
	private String description;
	private String isActive;
	private String isDeleted;
	private String createdBy;
	private String modifiedBy;
	private String createdOn;
	private String modifiedOn;
	private Object satellite;
	private String pincode;
	private String cityName;
	private String districtName;
	private String stateName;
	private String address1;
	private String address2;
	private String address3;
	private String address4;
	private String emailId;
	private String telephoneNo;
	private String tin;
	private String gstIn;
	private String pan;
	
	private String status;

}
