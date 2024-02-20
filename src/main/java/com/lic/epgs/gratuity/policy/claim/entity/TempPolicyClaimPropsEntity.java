package com.lic.epgs.gratuity.policy.claim.entity;

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
@Table(name = "PMST_TMP_CLAIM_PROPS")
public class TempPolicyClaimPropsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_CLAIM_PROPS_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_CLAIM_PROPS_ID_SEQ", sequenceName = "PMST_TMP_CLAIM_PROPS_ID_SEQ", allocationSize = 1)
	@Column(name = "CLAIM_PROPS_ID")
	private Long id;
	
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "TMP_MEMBER_ID")
	private Long tmpMemberId;
	
	@Column(name = "CLAIM_TYPE")
	private String claimType;
	
	@Column(name = "MODE_OF_EXIT")
	private Long modeOfExit;
	
	@Column(name = "ONBOARD_NUMBER")
	private String onboardNumber;
	
	@Column(name = "INTIMATION_NUMBER")
	private String initimationNumber;
	
	@Column(name = "CLAIM_STATUS_ID")
	private Long claimStatusId;
	
	@Column(name = "DATE_OF_EXIT")
	private Date dateOfExit;
	
	@Column(name = "DATE_OF_DEATH")
	private Date dateOfDeath;
	
	@Column(name = "REASON_FOR_DEATH_ID")
	private Long reasonForDeathId;
	
	@Column(name = "REASON_FOR_DEATH_OTHER")
	private String reasonForDeathOther;
	
	@Column(name = "PLACE_OF_DEATH")
	private String placeOfDeath;
	
	
	@Column(name = "SALARY_AS_ON_DATE_OF_EXIT")
	private Double salaryAsOnDateOfExit;
	
	@Column(name = "GRATUITY_AMT_ON_DATE_OF_EXIT")
	private Double gratuityAmtOnDateExit;
	
	@Column(name = "MODIFIED_GRATUITY_AMOUNT")
	private Double modifiedGratuityAmount;
	
	@Column(name="REASON_WITHDRAWAL_ID")
	private Long reasonForWithdrawalId;
	
	@Column(name="REASON_SUBEVENT_DEATH_ID")
	private Long reasonForSubeventDeathId;
	
	
	@Column(name = "LC_SUM_ASSURED")
	private Double lcSumAssured;
	
	@Column(name = "IS_PREMIUM_REFUND")
	private Double isPremiumRefund;
	
	@Column(name = "REFUND_PREMIUM_AMOUNT")
	private Double refundPremiumAmount;
	
	@Column(name = "PENAL_AMOUNT")
	private Double penalAmount;
	
	@Column(name = "COURT_AWARD")
	private Double courtAward;
	
	@Column(name = "IS_CLAIM_FORM_RECEIVED")
	private Double isClaimFromReceived;
	
	@Column(name = "IS_DEATH_CERTIFICATE_RECEIVED")
	private Long isDeathCertificateReceived;
	
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
	
	@Column(name = "ONBOARDING_DATE")
	private Date onboardingDate;
	
	@Column(name = "INTIMATION_DATE")
	private Date intimationDate;

	@Column(name = "PAYOUT_NUMBER")
	private String payoutNo;
	
	@Column(name = "BATCH_ID")
	private Long batchId;
	
	@Column(name="REFUND_GST_AMOUNT")
	private Double refundGSTAmount;
	
	@Column(name="PAYOUT_DATE")
	private Date payoutDate;
}
