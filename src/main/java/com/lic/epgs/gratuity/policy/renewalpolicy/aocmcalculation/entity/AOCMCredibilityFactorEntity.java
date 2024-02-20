package com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "AOCM_CREDIBILITY_FACTOR")
public class AOCMCredibilityFactorEntity {
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AOCM_CREDIBILITY_FACTOR_ID_SEQ")
	@SequenceGenerator(name = "AOCM_CREDIBILITY_FACTOR_ID_SEQ", sequenceName = "AOCM_CREDIBILITY_FACTOR_ID_SEQ", allocationSize = 1)
	@Column(name = "AOCM_CREDIBILITY_FACTOR_ID")
	private Long Id;
	
	@Column(name = "FROM_LIVES")
	private Long fromLives;
	
	@Column(name = "VARIANT")
	private String variant;
	
	@Column(name ="TO_LIVES")
	private Long toLives;
	
	@Column(name ="CREDIBILITY")
	private Double credibility;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CREATED_BY", length = 128)
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY", length = 128)
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
}
