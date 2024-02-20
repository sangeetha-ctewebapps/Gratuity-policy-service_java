package com.lic.epgs.gratuity.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "FUND_BATCH_SUMMARY")
public class FundBatchSummaryEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FUND_BATCH_SUMMARY_GEN")
	@SequenceGenerator(name = "FUND_BATCH_SUMMARY_GEN", sequenceName = "FUND_BATCH_SUMMARY_ID_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private BigDecimal Id;

	@Column(name = "BATCH_NO", nullable = false)
	private String batchNo;

	@Column(name = "NO_OF_POLICIES")
	private BigDecimal noOFPolicies;

	@Column(name = "NO_OF_MEMBERS")
	private BigDecimal noOFMembers;

	@Column(name = "NO_OF_SUCCESS_POLICIES")
	private BigDecimal noOFSuccessPolicies;

	@Column(name = "NO_OF_SUCCESS_MEMBERS")
	private BigDecimal noOFSuccessMembers;

	@Column(name = "NO_OF_FAILURE_POLICIES")
	private BigDecimal noOFFailurePolicies;

	@Column(name = "NO_OF_FAILURE_MEMBERS")
	private BigDecimal noOFFailureMembers;

	@Column(name = "BATCH_STATUS")
	private String batchStatus;

	@Column(name = "GENERATED_DATE")
	private String generatedDate;

	@Column(name = "GENERATED_BY")
	private String generatedBy;

}
