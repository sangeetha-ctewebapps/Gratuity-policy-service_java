package com.lic.epgs.gratuity.mph.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_HIS_MPH_BANK")
public class HistoryMPHBankEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_HIS_MPH_BANK_ID_SEQ")
	@SequenceGenerator(name = "PMST_HIS_MPH_BANK_ID_SEQ", sequenceName = "PMST_HIS_MPH_BANK_ID_SEQ", allocationSize = 1)
	@Column(name = "MPH_BANK_ID")
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="MPH_ID", nullable=false)
    private HistoryMPHEntity masterMph;
	
	@Column(name = "BANK_NAME")
	private String bankName;
	
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;
	
	@Column(name = "ACCOUNT_TYPE")
	private String accountType;
	
	@Column(name = "BANK_ADDRESS")
	private String bankAddress;
	
	@Column(name = "CITY_ID")
	private String cityId;
	
	@Column(name = "TOWN_LOCALITY")
	private String townLocality;
	
	@Column(name = "STATE_ID")
	private String stateId;
	
	@Column(name = "COUNTRY_ID")
	private String countryId;
	
	@Column(name = "DISTRICT_ID")
	private String districtId;

	@Column(name = "BANK_BRANCH")
	private String bankBranch;
	
	
	@Column(name = "IFSC_CODE")
	private String ifscCode;
	
	@Column(name = "COUNTRY_CODE")
	private Long countryCode;
	
	
	
	@Column(name = "EMAIL_ID")
	private String emailId;
	
	@Column(name = "STD_CODE")
	private Long stdCode;
	
	@Column(name = "LANDLINE_NUMBER")
	private Long landlineNumber;

	@Column(name = "IS_ACTIVE")	
	private Boolean isActive;
	
	@Column(name = "CREATED_BY")	
	private String createdBy;
	
	@Column(name = "CREATED_DATE")	
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")	
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")	
	private Date modifiedDate;
}
