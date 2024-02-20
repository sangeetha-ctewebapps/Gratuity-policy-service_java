package com.lic.epgs.gratuity.policy.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "PSTG_ADJUSTMENT_DETAIL")
public class PolicyAdjustmentDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ADJUSTMENT_DETAIL_ID")
	private Long id;

	@Column(name = "CONTRIBUTION_DETAIL_ID")
	private Long contributionDetailId;

	@Column(name = "DEPOSIT_ID")
	private Long depositId;

	@Column(name = "ADJUSTED_AMOUNT")
	private Double adjustedAmount;

	@Column(name = "ADJUSTED_FOR")
	private String adjustedFor;

	@Column(name = "SUB_TYPE")
	private String subType;

	@Column(name = "ENTRY_TYPE")
	private String entryType;

	@Column(name = "DEPOSIT_EFFECTIVE_DATE")
	private Date depositEffectiveDate;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
}
