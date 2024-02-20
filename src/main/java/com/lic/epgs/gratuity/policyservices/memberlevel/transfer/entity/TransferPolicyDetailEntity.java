package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity;

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
@Table(name = "TRANSFER_POLICY_DETAIL")

public class TransferPolicyDetailEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONV_TEMP_ID_Sequence")
	@SequenceGenerator(name = "CONV_TEMP_ID_Sequence", sequenceName = "TRANSFER_POLICY_DETAIL_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "TRANSFER_POLICY_DETAIL_ID", unique = true, nullable = false, updatable = false, length = 10)
	private Long transferPolicyDetailId;

	@Column(name = "TRANSFER_REQUISITION_ID")
	private Long transferRequisitionId;

	@Column(name = "POLICY_NUMBER_OUT")
	private Long policyNumberOut;
	
	@Column(name = "POLICY_NUMBER_IN")
	private Long policyNumberIn;

	@Column(name = "MPH_NAME_OUT")
	private String mphNameOut;
	
	@Column(name = "MPH_NAME_IN")
	private String mphNameIn;

	@Column(name = "POLICY_STATUS")
	private String policyStatus;

	@Column(name = "PRODUCT_NAME_OUT")
	private String productNameOut;
	
	@Column(name = "PRODUCT_NAME_IN")
	private String productNameIn;

	@Column(name = "PRODUCT_VARIANT_OUT")
	private String productVariantOut;
	
	@Column(name = "PRODUCT_VARIANT_IN")
	private String productVariantIn;

	@Column(name = "POLICY_START_DATE")
	private Date policyStatDate = new Date();

	@Column(name = "POLICY_END_DATE")
	private Date policyEndDate = new Date();

	@Column(name = "UNIT_IN")
	private String unitIn;
	
	@Column(name = "UNIT_OUT")
	private String unitOut;

	@Column(name = "FREQUENCY")
	private String frequency;

	@Column(name = "FUND_BALANCE_PRIOR_TO_TRANSFER")
	private Long fundBalancePriorToTransfer;

	@Column(name = "ACCRUED_INTEREST_AMOUNT")
	private Long accruedInterestAmount;

	@Column(name = "TOTAL_FUND_VALUE")
	private Long totalFundValue;
	
	@Column(name = "PRODUCT_ID_IN")
	private Long productIdIn;
	
	@Column(name = "PRODUCT_ID_OUT")
	private Long productIdOut;
	
	@Column(name = "TEMP_POLICY_ID")
	private Long tempPolicyId;
	
	@Column(name = "POLICY_ID_OUT")
	private Long policyIdOut;
	
	@Column(name = "POLICY_ID_IN")
	private Long policyIdIn;

		
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
