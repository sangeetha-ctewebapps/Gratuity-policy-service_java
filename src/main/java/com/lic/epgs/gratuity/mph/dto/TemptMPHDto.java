package com.lic.epgs.gratuity.mph.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.mph.entity.TempMPHAddressEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemptMPHDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String mphCode;

	private String mphName;

	private String mphType;

	private String cin;

	private String gstIn;

	private String pan;

	private String alternatePan;

	private String countryId;

	private String emailId;

	private Long mobileNo;

	private Long landLineNo;

	private Long fax;

	private Boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;
	private Date modifiedDate;

	private Long endorsementId;
	private Long pmstId;

	private String recordType;

	public List<TempMPHAddressDto> mphAddresses;
	public List<TempMPHBankDto> mphBank;
	public List<TempMPHRepresentativeDto> mphRepresentatives;

}
