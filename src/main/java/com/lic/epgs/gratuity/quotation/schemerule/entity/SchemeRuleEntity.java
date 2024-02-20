package com.lic.epgs.gratuity.quotation.schemerule.entity;

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
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "QSTG_SCHEMERULE")
public class SchemeRuleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QSTG_SCHEMERULE_ID_SEQ")
	@SequenceGenerator(name = "QSTG_SCHEMERULE_ID_SEQ", sequenceName = "QSTG_SCHEMERULE_ID_SEQ", allocationSize = 1)
	@Column(name = "SCHEMERULE_ID")
	private Long id;
	
	@Column(name = "QUOTATION_ID")
	private Long quotationId;
	
	
	@Column(name = "FCL_TYPE_ID")
	private Long fclTypeId;
	
	@Column(name = "PREMIUM_ADJUSTMENT_TYPE_ID")
	private Long premiumAadjustmentTypeId;
	
	@Column(name = "MINIMUM_GRATUITY_SERVICE_YEAR")
	private int minimumGratuityServiceYear;
	
	@Column(name = "MINIMUM_GRATUITY_SERVICE_MONTH")
	private int minimumGratuityServiceMonth;
	
	@Column(name = "MINIMUM_GRATUITY_SERVICE_DAY")
	private int minimumGratuityServiceDay;
	
	@Column(name = "FCL")
	private Double fcl;
	
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
