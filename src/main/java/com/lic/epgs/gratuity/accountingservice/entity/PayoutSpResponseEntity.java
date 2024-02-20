package com.lic.epgs.gratuity.accountingservice.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PAYOUT_SP_RESPONSE")
public class PayoutSpResponseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "PAYOUT_SP_ID")
	private Long Id ;

	@Column(name = "PAYOUT_NUMBER")
	private String payoutNumber ;

	@Column(name = "PAYOUT_DATE")
	private Date payOutDate ;

	@Column(name = "JOURNAL_NO")
	private Long journalNo ;

	@Column(name = "DEBIT_ACCOUNT")
	private Long debitAccount ;

	@Column(name = "CREDIT_ACCOUNT")
	private Long creditAccount ;

	@Column(name = "TOTAL_AMOUNT")
	private Long totalAmount ;

	@Column(name = "CREDIT_CODE")
	private String creditCode ;

	@Column(name = "MESSAGE")
	private String message ;
	
	@Column(name = "STATUS")
	private String status ;
	
	@Column(name = "STATUSCODE")
	private String statusCode ;
	
	@Column(name = "SQLCODE")
	private Long sqlCode ;
	
	@Column(name = "SQL_ERROR_MESSAGE")
	private String sqlErrorMessage ;
	
	@Column(name = "IS_ACTIVE")
	private Long isActive ;
	
	@Column(name = "CREATED_BY")
	private String createdBy ;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate ;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy ;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate ;

}
