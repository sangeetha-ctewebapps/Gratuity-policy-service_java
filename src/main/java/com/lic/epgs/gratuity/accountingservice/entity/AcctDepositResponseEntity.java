package com.lic.epgs.gratuity.accountingservice.entity;

import java.util.Date;

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
@Table(name = "ACCT_DEPOSIT_RESPONSE")
public class AcctDepositResponseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCT_RESPONSE_ID_SEQ")
	@SequenceGenerator(name = "ACCT_RESPONSE_ID_SEQ", sequenceName = "ACCT_RESPONSE_ID_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ACCT_RESPONSE_ID", nullable=false)
	private AcctResponseEntity acctResponseEntity;
	
	@Column(name = "ADJUSTMENT_NUMBER")
	private Long adjustmentNumber;
	
	@Column(name = "VOUCHER_NUMBER")
	private String voucherNumber;
	
	@Column(name = "VOUCHER_AMOUNT")
	private Double voucherAmount;
	
	@Column(name = "VOUCHER_EFFECTIVE_DATE")
	private Date voucherEffectiveDate;
	
	@Column(name = "GST_INVOICE_NUMBER")
	private String gstInvoiceNumber;
	
}
