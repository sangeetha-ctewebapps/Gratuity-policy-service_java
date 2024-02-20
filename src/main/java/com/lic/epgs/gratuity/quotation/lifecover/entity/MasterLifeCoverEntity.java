package com.lic.epgs.gratuity.quotation.lifecover.entity;

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
@Table(name = "QMST_LIFE_COVER")
public class MasterLifeCoverEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QMST_LIFE_COVER_ID_SEQ")
	@SequenceGenerator(name = "QMST_LIFE_COVER_ID_SEQ", sequenceName = "QMST_LIFE_COVER_ID_SEQ", allocationSize = 1)
	@Column(name = "LIFE_COVER_ID")
	private Long id;
	
	@Column(name = "QUOTATION_ID")
	private Long quotationId;
	
	@Column(name = "CATEGORY_ID")
	private Long categoryId;
	
	@Column(name = "SUM_ASSURED")
	private Double sumAssured;

	@Column(name ="RETIREMENT_AGE")
	private Integer retirementAge;
	
	@Column(name ="RETENTION_LIMIT")
	private Double retentionLimit;
	
	
	@Column(name = "MINIMUM_SUM_ASSURED")
	private Double minimumSumAssured;	
	
	@Column(name = "MAXIMUM_SUM_ASSURED")
	private Double maximumSumAssured;
	
	@Column(name = "LIFE_COVER_TYPE_ID")
	private Long lifeCoverTypeId;
	
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

