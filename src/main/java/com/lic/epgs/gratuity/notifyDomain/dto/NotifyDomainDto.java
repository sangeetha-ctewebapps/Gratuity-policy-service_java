package com.lic.epgs.gratuity.notifyDomain.dto;


import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:24-09-2022
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotifyDomainDto {
	
	private Long notifyDomainId;
	
	private BigInteger demandNo;

	private Integer status;

	private String rejectReason;

	
	private String dateOfCollection;

	private String utrNumber;

	private String licReferenceId;

	private String virtualAccountNumber;

	private double transactionAmount;

	private String remitterName;

	private String remitterIfscCode;

	private String remitterAccountNumber;

	private String remitterToReceiverInformation;

	private String bankTransactionRefNo;

	private String clientCode;

	private String mode;

	private String receiverBankId;

	private String receiverBankIfsc;

	private String receiverAccountNumber;
	
	private String remitterBankName;
	
	private String remitterBankBranch;
	
	private String collectionNo;
	
	private String refNo;

}
