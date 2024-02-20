package com.lic.epgs.gratuity.policy.entity;

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
 * @author Vigneshwaran
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "POLICY")
public class PolicyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POLICY_ID_SEQUENCE")
	@SequenceGenerator(name = "POLICY_ID_SEQUENCE", sequenceName = "POLICY_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "POLICY_ID")
	private Long id;
	
	@Column(name = "MASTER_QUOTATION_ID")
	private Long masterQuotationId;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "IS_APPROVAL_ESCALATED_TO_CO")
	private Boolean isEscalatedToCo;
	
	@Column(name = "IS_APPROVAL_ESCALATED_TO_ZO")
	private Boolean isEscalatedToZo;
	
	@Column(name = "IS_MIN_PAYMENT_RECEIVED")
	private Boolean isMinimumPaymentReceived;
	
	@Column(name = "IS_FULL_PAYMENT_RECEIVED")
	private Boolean isFullPaymentReceived;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name = "SUB_STATUS_ID")
	private Long subStatusId;
	
	@Column(name = "TAGGED_STATUS_ID")
	private Long taggedStatusId;
	
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
	
	
	@Column(name = "PAYTO")
	 private  Long payto;
}
