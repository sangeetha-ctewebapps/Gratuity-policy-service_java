package com.lic.epgs.gratuity.quotation.premium.entity;


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

/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "QSTG_PREMIUM")
public class PremiumEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QSTG_PREMIUM_ID_SEQ")
	@SequenceGenerator(name = "QSTG_PREMIUM_ID_SEQ", sequenceName = "QSTG_PREMIUM_ID_SEQ", allocationSize = 1)
	@Column(name = "PREMIUM_ID")
	private Long id;
	
	@Column(name = "QUOTATION_ID")
	private Long quotationId;
	
	@Column(name = "RATE_TABLE_ID")
	private Long rateTableId;
	
	@Column(name = "MODIFIED_RATE_TYPE")
	private String modifiedRateType;
	
	@Column(name = "MODIFIED_RATE_TABLE_ID")
	private Long modifiedRateTypeId;
	
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
