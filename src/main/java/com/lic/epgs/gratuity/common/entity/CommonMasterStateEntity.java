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
@Table(name = "MASTER_STATE", schema = "schemaname")
public class CommonMasterStateEntity {
	@Id
	@Column(name = "STATE_ID")
	private Long id;
	
	@Column(name = "COUNTRY_ID")
	private Long countryId;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "STATE_CODE")
	private String stateCode;
	
	@Column(name = "IS_ACTIVE")
	private String isActive;
	
	@Column(name = "IS_DELETED")
	private String isDeleted;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "GSTIN")
	private String gstin;
	
	@Column(name = "PAN")
	private String pan;
	
	@Column(name = "GSTSTATECODE")
	private String gstStateCode;
}
