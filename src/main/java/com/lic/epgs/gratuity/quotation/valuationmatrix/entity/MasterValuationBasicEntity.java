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
@Table(name = "QMST_VALUATIONBASIC")
public class MasterValuationBasicEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QMST_VAL_BASIC_ID_SEQ")
	@SequenceGenerator(name = "QMST_VAL_BASIC_ID_SEQ", sequenceName = "QMST_VAL_BASIC_ID_SEQ", allocationSize = 1)
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
	private Integer numberOfLives;
	
	@Column(name = "GRATUITY_AMOUNT_CELLING")
	private Float gratuityAmountCelling;
	
	@Column(name = "RATE_TABLE")
	private String rateTable;
	
	@Column(name = "MAX_LIFE_COVER_SUM_ASSURED")
	private Float maxLifeCoverSumAssured;
	
	@Column(name = "MAX_SALARY")
	private Float maxSalary;
	
	@Column(name = "PAST_SERVICE_DEATH")
	private Float pastServiceDeath;
	
	@Column(name = "PAST_SERVICE_WITHDRAWAL")
	private Float pastServiceWithdrawal;
	
	@Column(name = "PAST_SERVICE_RETIREMENT")
	private Float pastServiceRetirement;
	
	@Column(name = "CURRENT_SERVICE_DEATH")
	private Float currentServiceDeath;
	
	@Column(name = "CURRENT_SERVICE_WITHDRAWAL")
	private Float currentServiceWithdrawal;
	
	@Column(name = "CURRENT_SERVICE_RETIREMENT")
	private Float currentServiceRetirement;
	
	@Column(name = "ACCRUED_GRATUITY")
	private Float accruedGratuity;
	
	@Column(name = "TOTAL_GRATUITY")
	private Float totalGratuity;
	
	@Column(name = "MAXIMUM_SALARY")
	private Float maximumSalary;
	
	@Column(name = "MINIMUM_SALARY")
	private Float minimumSalary;
	
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
