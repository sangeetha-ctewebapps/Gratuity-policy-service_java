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
@Table(name = "QSTG_VALUATIONMATRIX")
public class ValuationMatrixEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QSTG_VALUATIONMATRIX_ID_SEQ")
	@SequenceGenerator(name = "QSTG_VALUATIONMATRIX_ID_SEQ", sequenceName = "QSTG_VALUATIONMATRIX_ID_SEQ", allocationSize = 1)
	@Column(name = "VALUATIONMATRIX_ID ")
	private Long id;
	
	@Column(name = "QUOTATION_ID")
	private Long quotationId;
	
	@Column(name = "VALUATION_TYPE_ID")
	private Long valuationTypeId;
	
	@Column(name = "VALUATION_DATE")
	private Date valuationDate;
	
	@Column(name ="TOTAL_SUM_ASSURED")
	private Double totalSumAssured;
	
	@Column(name = "CURRENT_SERVICE_LIABILITY")
	private Double currentServiceLiability;
	
	@Column(name = "PAST_SERVICE_LIABILITY")
	private Double pastServiceLiability;
	
	@Column(name = "FUTURE_SERVICE_LIABILITY")
	private Double futureServiceLiability;
	
	@Column(name = "PREMIUM")
	private Double premium;
	
	@Column(name = "GST")
	private Double gst;
	
	@Column(name = "TOTAL_PREMIUM")
	private Double totalPremium;
	
	@Column(name = "AMOUNT_RECEIVED")
	private Double amountReceived;
	
	@Column(name = "AMOUNT_PAYABLE")
	private Double amountPayable;
	
	@Column(name = "BALANCE_TO_BE_PAID")
	private Double balanceToBePaid;
	
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
