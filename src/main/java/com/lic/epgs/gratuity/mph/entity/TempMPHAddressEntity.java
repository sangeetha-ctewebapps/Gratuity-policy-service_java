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
@Table(name = "PMST_TMP_MPH_ADDRESS")
public class TempMPHAddressEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_MPH_ADDRESS_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_MPH_ADDRESS_ID_SEQ", sequenceName = "PMST_TMP_MPH_ADDRESS_ID_SEQ", allocationSize = 1)
	@Column(name = "MPH_ADDRESS_ID")
	private Long id;

	@ManyToOne
    @JoinColumn(name="MPH_ID", nullable=false)
	private TempMPHEntity masterMph;
	
	
	@Column(name = "ADDRESS_LINE1")	
	private String addressLine1;
	
	@Column(name = "ADDRESS_LINE2")	
	private String addressLine2;
	
	@Column(name = "ADDRESS_LINE3")	
	private String addressLine3;
	
	@Column(name = "ADDRESS_TYPE")	
	private String addressType;
	
	@Column(name = "CITY_ID")	
	private Long cityId;
	
	
	@Column(name = "CITY_LOCALITY")	
	private String cityLocality;
	
	
	@Column(name = "STATE_ID")	
	private Long stateId;
	
	@Column(name = "STATE_NAME")	
	private String stateName;
	
	
	@Column(name = "DISTRICT_ID")	
	private Long districtId;
	
	@Column(name = "DISTRICT")	
	private String district;
	
	@Column(name = "PINCODE")	
	private Long pincode;
	
	@Column(name = "COUNTRY_ID")	
	private Long countryId;
	
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
	
	@Column(name = "PMST_MPH_ADDRESS_ID")
	private Long pmstId;
	
	@Column(name = "RECORD_TYPE")
	private String recordType;
	
	@Column(name = "PMST_HIS_MPH_ADDRESS_ID")
	private Long pmstHisMphAddressId;
	
	
	
}
