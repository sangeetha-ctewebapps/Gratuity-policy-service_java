package com.lic.epgs.gratuity.policyservices.unit.transfer.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nandini.R Date:12-08-2023
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "UNIT_TRANSFER_POLICY_DETAIL")

public class UnitTransferPolicyDetailEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UNIT_TRANSFER_POLICY_DETAIL_ID_SEQUENCE")
	@SequenceGenerator(name = "UNIT_TRANSFER_POLICY_DETAIL_ID_SEQUENCE", sequenceName = "UNIT_TRANSFER_POLICY_DETAIL_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "UNIT_TRANSFER_POLICY_DETAIL_ID", unique = true, nullable = false, updatable = false, length = 10)
	private Long unitTransferPolicyDetailId;

	@Column(name = "UNIT_TRANSFER_REQUISITION_ID")
	private Long unitTransferRequisitionId;

	@Column(name = "POLICY_NUMBER")
	private Long policyNumber;

	@Column(name = "MPH_NAME")
	private String mphName;

	@Column(name = "POLICY_STATUS")
	private String policyStatus;

	@Column(name = "PRODUCT_NAME")
	private String productName;

	@Column(name = "PRODUCT_VARIANT")
	private String productVariant;

	@Column(name = "POLICY_START_DATE")
	private Date policyStartDate = new Date();

	@Column(name = "POLICY_END_DATE")
	private Date policyEndDate = new Date();

	@Column(name = "SOURCE_UNIT")
	private String sourceUnit;

	@Column(name = "UNIT_OUT")
	private String unitOut;

	@Column(name = "DESTINATION_UNIT")
	private String destinationUnit;

	@Column(name = "POLICY_ACCOUNT_VALUE")
	private Long policyAccountValue;

	@Column(name = "TOTAL_FUND_VALUE")
	private Long totalFundValue;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

}
