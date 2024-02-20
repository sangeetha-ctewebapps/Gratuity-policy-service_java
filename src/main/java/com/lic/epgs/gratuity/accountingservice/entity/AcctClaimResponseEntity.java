package com.lic.epgs.gratuity.accountingservice.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ACCT_CLAIM_RESPONSE")
public class AcctClaimResponseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCT_RESPONSE_ID_SEQ")
	@SequenceGenerator(name = "ACCT_RESPONSE_ID_SEQ", sequenceName = "ACCT_RESPONSE_ID_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ACCT_RESPONSE_ID", nullable=false)
	private AcctResponseEntity acctResponseEntity;
	
	@Column(name = "DEBIT_CODE")
	private String debitCode;
	
	@Column(name = "CREDIT_CODE")
	private String creditCode;
	
	@Column(name = "PAYOUT_AMOUNT")
	private Double payoutAmount;
	
	@Column(name = "JOURNAL_NUMBER")
	private String journalNumber;
}
