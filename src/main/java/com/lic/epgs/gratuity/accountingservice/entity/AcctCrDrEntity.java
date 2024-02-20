package com.lic.epgs.gratuity.accountingservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ACCT_CR_DR_RESPONSE")
public class AcctCrDrEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCT_RESPONSE_ID_SEQ")
	@SequenceGenerator(name = "ACCT_RESPONSE_ID_SEQ", sequenceName = "ACCT_RESPONSE_ID_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="ACCT_RESPONSE_ID", nullable=false)
	private AcctResponseEntity acctResponseEntity;
	
	@Column(name = "DEBIT_CODE")
	private String debitCode;
	
	@Column(name = "DEBIT_AMOUNT")
	private Double debitAmount;
	
	@Column(name = "DEBIT_CODE_DESCRIPTION")
	private String debitCodeDescription;
	
	@Column(name = "CREDIT_CODE")
	private String creditCode;
	
	@Column(name = "CREDIT_AMOUNT")
	private Double creditAmount;
	
	@Column(name = "CREDIT_CODE_DESCRIPTION")
	private String creditCodeDescription;
	
}
