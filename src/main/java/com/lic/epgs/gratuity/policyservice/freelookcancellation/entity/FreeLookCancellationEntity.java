package com.lic.epgs.gratuity.policyservice.freelookcancellation.entity;

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
@Table(name = "PMST_FREE_LOOK_CANC_PROPS")
public class FreeLookCancellationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FREELOOKCANC_ID_SEQ")
	@SequenceGenerator(name = "FREELOOKCANC_ID_SEQ", sequenceName = "FREELOOKCANC_ID_SEQ", allocationSize = 1)
	@Column(name="FREELOOKCANC_ID")
	private Long id;
	
	@Column(name="POLICY_ID")
	private Long policyId;
	
	@Column(name="FLC_REQUEST_NUMBER")
	private String freeLookCancellationRequestNumber;
	
	@Column(name="FLC_REQ_DT")
	private Date freeLookCancellationRequestDate;
	
	@Column(name="CALCULATE_FLC_PER_HARDCOPY")
	private Long calculateFreeLookCancellationPerHardCopy;
	
	@Column(name="POLICY_DOC_DISPATCH_DATE")
	private Date policyDocumentDispatchDate;
	
	@Column(name="POLICY_DOC_RECEIVED_DATE")
	private Date policyDocumentReceivedDate;
	
	@Column(name="STAMP_DUTY")
	private Long stampDuty;
	
	@Column(name="MEDICAL_TEST_EXPENSES")
	private Double medicalTestExpenses;
	
	@Column(name="LIFE_COVER_PREMIUM")
	private Double lifeCoverPremium;
	
	@Column(name="DEBIT_PREMIUM_FROM_MPH")
	private Double debitPremiumFromMph;
	
	@Column(name="CREDIT_PREMIUM_TO_MPH")
	private Double creditPremiumToMph;
	
	@Column(name="EFFECT_CANCEL_DT")
	private Date effectiveCancelDate;
	
	@Column(name="FREELOOK_STATUS")
	private String freeLookStatus;
	
	@Column(name="REJECTION_REASON_CODE")
	private String rejectionReasonCode;
	
	@Column(name="REJECTION_REMARKS")
	private String rejectionRemarks;
	
	@Column(name="TOTAL_CONTRI")
	private Double totalContribution;
	
	@Column(name="TOT_FUND_VAL")
	private Double totalFundValue;
	
	@Column(name="TOT_INT_ACCR")
	private Double totalAccuredIntrest;
	
	@Column(name="TOT_REFUND_AMT")
	private Double totalRefundAmount;
	
	@Column(name="WORKFLOW_STATUS")
	private String workFlowStatus;
	
	@Column(name="IS_ACTIVE")
	private Long isActive;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="GST")
	private Double gst;
	
	@Column(name="DEBIT_GST_FROM_MPH")
	private Double debitGstFromMph;
	
	@Column(name="CREDIT_GST_TO_MPH")
	private Double creditGstToMph;
	
}
