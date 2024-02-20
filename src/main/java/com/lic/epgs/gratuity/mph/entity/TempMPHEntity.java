package com.lic.epgs.gratuity.mph.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "PMST_TMP_MPH")
public class TempMPHEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_MPH_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_MPH_ID_SEQ", sequenceName = "PMST_TMP_MPH_ID_SEQ", allocationSize = 1)
	@Column(name = "MPH_ID")
	private Long id;
	
	@OneToMany(mappedBy="masterMph", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TempMPHAddressEntity> mphAddresses;
    
    @OneToMany(mappedBy="masterMph", cascade = CascadeType.ALL)
    private Set<TempMPHBankEntity> mphBank;
    
    @OneToMany(mappedBy="masterMph", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TempMPHRepresentativeEntity> mphRepresentatives;
	
	

	@Column(name = "MPH_CODE")
	private String mphCode;

	@Column(name = "MPH_NAME")
	private String mphName;

	@Column(name = "MPH_TYPE")
	private String mphType;

	@Column(name = "CIN")
	private String cin;

	@Column(name = "PAN")
	private String pan;

	@Column(name = "ALTERNATE_PAN")
	private String alternatePan;

	@Column(name = "COUNTRY_ID")
	private String countryId;

	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "MOBILE_NO")
	private Long mobileNo;

	@Column(name = "LANDLINE_NO")
	private Long landlineNo;

	@Column(name = "FAX")
	private Long fax;

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

	@Column(name = "GSTIN")
	private String gstIn;
	
	@Column(name = "PMST_MPH_ID")
	private Long pmstId;
	
	@Column(name = "RECORD_TYPE")
	private String recordType;
	
	@Column(name = "ENDORSEMENT_ID")
	private Long endrosementId;

	@Column(name ="TMP_POLICY_ID")
	private Long tmpPolicyId;

	@Column(name ="PMST_HIS_MPH_ID")
	private Long pmstHisMphId;
	
	
}
