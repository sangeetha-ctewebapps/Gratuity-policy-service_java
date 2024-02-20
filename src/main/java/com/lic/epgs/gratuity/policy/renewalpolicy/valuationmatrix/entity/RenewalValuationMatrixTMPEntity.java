package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity;

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
@Table(name = "PMST_TMP_VALUATIONMATRIX")
public class RenewalValuationMatrixTMPEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_VALUATION_ID_SEQ")
	@SequenceGenerator(name = "PMST_VALUATION_ID_SEQ", sequenceName = "PMST_VALUATION_ID_SEQ", allocationSize = 1)
	@Column(name = "VALUATIONMATRIX_ID ")
	private Long id;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
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
	
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	
	@Column(name = "PMST_VALUATIONMATRIX_ID")
	private Long pmstValuationMatrixId;

	
	@Column(name = "PMST_HIS_VALUATIONMATRIX_ID")
	private Long pmstHisValuationMatrixId;
	
	
	
}
