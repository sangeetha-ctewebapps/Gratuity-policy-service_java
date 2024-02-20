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
import lombok.Builder;
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
@Builder
@Entity
@Table(name = "QUOTATION_CHARGE")
public class QuotationChargeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUOTATION_CHARGE_ID_SEQ")
	@SequenceGenerator(name = "QUOTATION_CHARGE_ID_SEQ", sequenceName = "QUOTATION_CHARGE_ID_SEQ", allocationSize = 1)
	@Column(name = "QUOTATION_CHARGE_ID")
	private Long id;
	
	@Column(name = "MASTER_QUOTATION_ID")
	private Long masterQuotationId;
	
	@Column(name = "CHARGE_TYPE_ID")
	private Long chargeTypeId;
	
	@Column(name = "AMOUNT_CHARGED")
	private double amountCharged;
	
	@Column(name = "BALANCE_AMOUNT")
	private double balanceAmount;
	
	@Column(name ="TMP_POLICY_ID")
	private Long tmpPolicyId;
	
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
