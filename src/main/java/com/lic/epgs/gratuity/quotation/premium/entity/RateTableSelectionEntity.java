package com.lic.epgs.gratuity.quotation.premium.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "RATE_TABLE_SELECTION")
public class RateTableSelectionEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RATE_TABLE_SELECTION_ID")
	private Long id;
	
	@Column(name = "RISK_GROUP")
	private String riskGroup;
	
	@Column(name = "NON_SKILLED_EMP_FROM")
	private Long nonSkilledEmpFrom;
	
	@Column(name = "NON_SKILLED_EMP_TO")
	private Long nonSkilledEmpTo;

	@Column(name = "NON_SKILLED_DESC")
	private String nonSkilledDesc;

	
	@Column(name = "RATE_TABLE")
	private String rateTable;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date  createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String  modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date  modifiedDate;
}
