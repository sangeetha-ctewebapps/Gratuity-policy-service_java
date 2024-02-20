package com.lic.epgs.gratuity.policy.member.dto;

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
public class TempMemberAddressDto {

	private Long id;
	private Long addressTypeId;
	private String pinCode;
	private String country;
	private String stateName;
	private String district;
	private String city;
	private String address1;
	private String address2;
	private String address3;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long pmstMemberAddressId;
	private Long recordType;
	
}
