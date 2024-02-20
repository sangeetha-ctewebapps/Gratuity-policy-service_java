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
public class MPHBankDto {
	private Long id;
	private String bankName;
	private String accountNumber;
	private String accountType;
	private String bankAddress;
	private String cityId;
	private String townLocality;
	private String stateId;
	private String countryId;
	private String districtId;
	private String bankBranch;
	private String ifscCode;
	private long countryCode;
	private String emailId;
	private long stdCode;
	private long landlineNumber;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

}
