package com.lic.epgs.gratuity.mph.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lic.epgs.gratuity.quotation.member.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PSTG_MPH_ADDRESS")
public class StagingMPHAddressEntity {
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PSTG_MPH_ADDR_ID_SEQ")
	@SequenceGenerator(name = "PSTG_MPH_ADDR_ID_SEQ", sequenceName = "PSTG_MPH_ADDR_ID_SEQ", allocationSize = 1)
	@Column(name = "MPH_ADDRESS_ID")
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
	
}
