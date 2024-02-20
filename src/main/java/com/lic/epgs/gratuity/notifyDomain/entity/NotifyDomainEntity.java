package com.lic.epgs.gratuity.notifyDomain.entity;

/**
 * @author Logesh.D Date:25-05-2022
 *
 */

import java.io.Serializable;
import java.math.BigInteger;
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
 * @author Logesh.D Date:25-05-2022
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "NOTIFY_DOMAIN")
public class NotifyDomainEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFY_DOMAIN_ID_SEQ")
	@SequenceGenerator(name = "NOTIFY_DOMAIN_ID_SEQ", sequenceName = "NOTIFY_DOMAIN_ID_SEQ", allocationSize = 1)
	@Column(name = "NOTIFY_DOMAIN_ID")
	private Integer notifyDomainId;
	
	@Column(name = "DEMAND_NO")
	private BigInteger demandNo;

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "REJECT_REASON")
	private String rejectReason;

	@Column(name = "DATE_OF_COLL")
	private Date dateOfCollection;

	@Column(name = "UTR_NUMBER")
	private String utrNumber;

	@Column(name = "LIC_REF_ID")
	private String licReferenceId;

	@Column(name = "VIRTUAL_ACC_NUMBER")
	private String virtualAccNo;

	@Column(name = "TRANS_AMOUNT")
	private Double transactionAmount;

	@Column(name = "REMITTER_NAME")
	private String remitterName;

	@Column(name = "REM_IFSC_CODE")
	private String remitterIfscCode;

	@Column(name = "REM_ACC_NO")
	private String remitterAccNo;

	@Column(name = "REMITO_RECEV_INFO")
	private String remToReceiverInform;

	@Column(name = "BANK_TRANS_REFNO")
	private String bankTransRefNo;

	@Column(name = "CLIENT_CODE")
	private String clientCode;

	@Column(name = "TXN_MODE")
	private String mode;

	@Column(name = "RECEI_BANK_ID")
	private String receiverBankId;

	@Column(name = "RECEI_BANK_IFSC")
	private String receiverBankIfsc;

	@Column(name = "RECEI_ACC_NO")
	private String receiverAccNo;

	
	@Column(name = "REMITTER_BANK_NAME")
	private String remitterBankName;
	
	@Column(name = "REMITTER_BANK_BRANCH")
	private String remitterBankBranch;
	
	@Column(name = "COLLECTION_NO")
	private String collectionNo;
	
	@Column(name = "REF_NO")
	private String refNo;


}
