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
@Table(name = "PSTG_MPH_REPRESENTATIVES")
public class StagingMPHRepresentativesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PSTG_MPH_REP_ID_SEQ")
	@SequenceGenerator(name = "PSTG_MPH_REP_ID_SEQ", sequenceName = "PSTG_MPH_REP_ID_SEQ", allocationSize = 1)
	@Column(name = "REP_ID")
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="MPH_ID", nullable=false)
    private StagingMPHEntity stagingMph;
	
	@Column(name = "ADDRESS_LINE1")	
	private String addressLine1;
	
	@Column(name = "ADDRESS_LINE2")	
	private String addressLine2;
	
	@Column(name = "ADDRESS_LINE3")	
	private String addressLine3;
	
	@Column(name = "ADDRESS_TYPE")	
	private String addressType;
	
	@Column(name = "CITY_LOCALITY")	
	private String cityLocality;
	
	@Column(name = "STATE_NAME")	
	private String stateName;
	
	@Column(name = "DISTRICT")	
	private String district;
	
	@Column(name = "COUNTRY_CODE")	
	private String countryCode;
	
	@Column(name = "EMAIL_ID")	
	private String emailId;
	
	@Column(name = "MOBILE_NO")	
	private String mobileNo;
	
	@Column(name = "LANDLINE_NO")	
	private String landlineNo;
	
	@Column(name = "PINCODE")	
	private String pincode;
	
	@Column(name = "REPRESENTATIVE_CODE")	
	private String representativeCode;
	
	@Column(name = "REPRESENTATIVE_NAME")	
	private String representativeName;
	
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
