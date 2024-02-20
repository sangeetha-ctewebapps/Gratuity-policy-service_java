package com.lic.epgs.gratuity.simulation.entity;

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
@Table(name = "TMP_DEPOSIT")
public class DepositEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TMP_DEPOSIT_ID_SEQUENCE")
	@SequenceGenerator(name = "TMP_DEPOSIT_ID_SEQUENCE", sequenceName = "TMP_DEPOSIT_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "TMP_DEPOSIT_ID")
	private Long id;
	
	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "DEPOSIT_AMOUNT")
	private double depositAmount;
	
	@Column(name = "AVAILABLE_AMOUNT")
	private double availableAmount;
	
	@Column(name = "CHEQUE_REALISATION_DATE")
	private Date chequeRealisationDate;
	
	@Column(name = "COLLECTION_DATE")
	private Date collectionDate;
	
	@Column(name = "COLLECTION_NO")
	private String collectionNumber;
	
	@Column(name = "COLLECTION_STATUS")
	private String collectionStatus;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "TRANSACTION_MODE")
	private String transactionMode;
	
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
