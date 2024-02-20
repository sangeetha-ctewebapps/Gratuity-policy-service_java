package com.lic.epgs.gratuity.quotation.valuationmatrix.entity;

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
@Table(name = "QSTG_VALUATIONWITHDRAWALRATE")
public class ValuationWithdrawalRateEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QSTG_VALWITHDRAWALRATE_ID_SEQ")
	@SequenceGenerator(name = "QSTG_VALWITHDRAWALRATE_ID_SEQ", sequenceName = "QSTG_VALWITHDRAWALRATE_ID_SEQ", allocationSize = 1)
	@Column(name = "VALUATIONWITHDRAWALRATE_ID ")
	private Long id;
	
	@Column(name = "QUOTATION_ID")
	private Long quotationId;
	
	@Column(name = "FROM_AGE_BAND")
	private Long fromAgeBand;
	
	@Column(name = "TO_AGE_BAND")
	private Long toAgeBand;
	
	@Column(name = "RATE")
	private Float rate;
	
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
