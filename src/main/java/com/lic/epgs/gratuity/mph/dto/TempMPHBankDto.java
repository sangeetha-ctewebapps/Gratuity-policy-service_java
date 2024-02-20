package com.lic.epgs.gratuity.mph.dto;

import java.io.Serializable;
import java.sql.Date;

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

public class TempMPHBankDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long mphId;
	private String bankName;
	private Long accountNumber;
	private String accountType;
	private String bankAddress;
	private Long cityId;
	private String townLocality;
	private Long stateId;
	private Long countryId;
	private Long districtId;
	private String bankBranch;
	private String ifscCode;
	private Long countryCode;
	private String EmailId;
	private Long stdCode;
	private Long landLineNumber;
	private Boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;
	private Date modifiedDate;

	private Long endorsementId;
	private Long pmstId;
	
	
	private Long recordType;

}
