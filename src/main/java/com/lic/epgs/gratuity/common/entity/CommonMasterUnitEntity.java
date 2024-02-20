package com.lic.epgs.gratuity.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "MASTER_UNIT", schema = "schemaname")
public class CommonMasterUnitEntity {
	@Id
	@Column(name = "UNIT_ID")
	private Long id;
	
	@Column(name = "ZONAL_ID")
	private Long zonalId;

	@Column(name = "UNIT_CODE")
	private String code;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "IS_ACTIVE")
	private String isActive;
	
	@Column(name = "IS_DELETED")
	private String isDeleted;
	
	@Column(name = "PINCODE")
	private String pincode;
	
	@Column(name = "CITY_NAME")
	private String cityName;
	
	@Column(name = "DISTRICT_NAME")
	private String districtName;
	
	@Column(name = "STATE_NAME")
	private String stateName;
	
	@Column(name = "ADDRESS1")
	private String address1;
	
	@Column(name = "ADDRESS2")
	private String address2;
	
	@Column(name = "ADDRESS3")
	private String address3;
	
	@Column(name = "ADDRESS4")
	private String address4;
	
	@Column(name = "EMAIL_ID")
	private String emailId;
	
	@Column(name = "TELEPHONE_NO")
	private String telephoneNo;
	
	@Column(name = "TIN")
	private String tin;
	
	@Column(name = "GSTIN")
	private String gstin;
	
	@Column(name = "PAN")
	private String pan;
}
