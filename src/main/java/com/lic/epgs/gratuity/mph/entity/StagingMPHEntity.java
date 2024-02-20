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

import com.lic.epgs.gratuity.quotation.member.entity.MemberAddressEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberAppointeeEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBankAccount;
import com.lic.epgs.gratuity.quotation.member.entity.MemberNomineeEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PSTG_MPH")
public class StagingMPHEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PSTG_MPH_ID_SEQUENCE")
	@SequenceGenerator(name = "PSTG_MPH_ID_SEQUENCE", sequenceName = "PSTG_MPH_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "MPH_ID")
	private Long id;

		@OneToMany(mappedBy="stagingMph", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	    private Set<StagingMPHAddressEntity> mphAddresses;
	    
	    @OneToMany(mappedBy="stagingMph", cascade = CascadeType.ALL)
	    private Set<StagingMPHBankEntity> mphBank;
	    
	    @OneToMany(mappedBy="stagingMph", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	    private Set<StagingMPHRepresentativesEntity> mphRepresentatives;
	   
	
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
	private long mobileNo;

	@Column(name = "LANDLINE_NO")
	private long landlineNo;

	@Column(name = "FAX")
	private long fax;

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

}
