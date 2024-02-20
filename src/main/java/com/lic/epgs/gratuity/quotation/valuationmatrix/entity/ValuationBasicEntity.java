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
@Table(name = "QSTG_VALUATIONBASIC")
public class ValuationBasicEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QSTG_VALUATIONBASIC_ID_SEQ")
	@SequenceGenerator(name = "QSTG_VALUATIONBASIC_ID_SEQ", sequenceName = "QSTG_VALUATIONBASIC_ID_SEQ", allocationSize = 1)
	@Column(name = "VALUATIONBASIC_ID ")
	private Long id;
	
	@Column(name = "QUOTATION_ID")
	private Long quotationId;
	
	@Column(name = "VALUATION_TYPE_ID")
	private Long valuationTypeId;
	
	@Column(name = "VALUATION_EFFECTIVE_DATE")
	private Date valuationEffectivDate;
	
	@Column(name = "REFERENCE_NUMBER")
	private String referenceNumber;
	
	@Column(name = "MAXIMUM_SERVICE")
	private Long maximumService;
	
	@Column(name = "MINIMUM_SERVICE_FOR_DEATH")
	private Long minimumServiceForDeath;
	
	@Column(name = "MINIMUM_SERVICE_FOR_WITHDRAWAL")
	private Long minimumServiceForWithdrawal;
	
	@Column(name = "MINIMUM_SERVICE_FOR_RETIREMENT")
	private Long minimumServiceForRetirement;
	
	@Column(name = "NO_OF_LIVES")
	private int numberOfLives;
	
	@Column(name = "GRATUITY_AMOUNT_CELLING")
	private Double gratuityAmountCelling;
	
	@Column(name = "RATE_TABLE")
	private String rateTable;
	
	@Column(name = "MAX_LIFE_COVER_SUM_ASSURED")
	private Double maxLifeCoverSumAssured;
	
	@Column(name = "MAX_SALARY")
	private Double maxSalary;
	
	@Column(name = "PAST_SERVICE_DEATH")
	private Double pastServiceDeath;
	
	@Column(name = "PAST_SERVICE_WITHDRAWAL")
	private Double pastServiceWithdrawal;
	
	@Column(name = "PAST_SERVICE_RETIREMENT")
	private Double pastServiceRetirement;
	
	@Column(name = "CURRENT_SERVICE_DEATH")
	private Double currentServiceDeath;
	
	@Column(name = "CURRENT_SERVICE_WITHDRAWAL")
	private Double currentServiceWithdrawal;
	
	@Column(name = "CURRENT_SERVICE_RETIREMENT")
	private Double currentServiceRetirement;
	
	@Column(name = "ACCRUED_GRATUITY")
	private Double accruedGratuity;
	
	@Column(name = "TOTAL_GRATUITY")
	private Double totalGratuity;
	
	@Column(name = "MAXIMUM_SALARY")
	private Double maximumSalary;
	
	@Column(name = "MINIMUM_SALARY")
	private Double minimumSalary;
	
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
