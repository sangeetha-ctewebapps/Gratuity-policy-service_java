package com.lic.epgs.gratuity.mph.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHRepresentativesEntity;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAddressDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAppointeeDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBankAccountDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberNomineeDto;
import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MPHEntityDto implements Serializable{
	private Long id;
	private String mphCode;
	private String mphName;
	private String mphType;
	private String cin;
	private String pan;
	private String alternatePan;
	private String countryId;
	private String emailId;
	private long mobileNo;
	private long landlineNo;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String gstIn;
	
	public List<MPHAddressDto> mphAddresses;
	public List<MPHBankDto> mphBank;
	public List<MPHRepresentativeDto> mphRepresentatives;

		// TODO Auto-generated method stub
		
	
	
	

}
