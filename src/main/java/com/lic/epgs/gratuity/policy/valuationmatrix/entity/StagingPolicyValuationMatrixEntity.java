package com.lic.epgs.gratuity.policy.valuationmatrix.entity;

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
@Table(name = "PSTG_VALUATIONMATRIX")
public class StagingPolicyValuationMatrixEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PSTG_VALUATION_ID_SEQ")
	@SequenceGenerator(name = "PSTG_VALUATION_ID_SEQ", sequenceName = "PSTG_VALUATION_ID_SEQ", allocationSize = 1)
	@Column(name = "VALUATION_ID ")
	private Long id;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "VALUATION_TYPE_ID")
	private Long valuationTypeId;
	
	@Column(name = "VALUATION_DATE")
	private Date valuationDate;
	
	@Column(name ="TOTAL_SUM_ASSURED")
	private Float totalSumAssured;
	
	@Column(name = "CURRENT_SERVICE_LIABILITY")
	private Float currentServiceLiability;
	
	@Column(name = "PAST_SERVICE_LIABILITY")
	private Float pastServiceLiability;
	
	@Column(name = "FUTURE_SERVICE_LIABILITY")
	private Float futureServiceLiability;
	
	@Column(name = "PREMIUM")
	private Float premium;
	
	@Column(name = "GST")
	private Float gst;
	
	@Column(name = "TOTAL_PREMIUM")
	private Float totalPremium;
	
	@Column(name = "AMOUNT_RECEIVED")
	private Float amountReceived;
	
	@Column(name = "AMOUNT_PAYABLE")
	private Float amountPayable;
	
	@Column(name = "BALANCE_TO_BE_PAID")
	private Float balanceToBePaid;
	
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
