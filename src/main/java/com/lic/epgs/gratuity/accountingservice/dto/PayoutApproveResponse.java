package com.lic.epgs.gratuity.accountingservice.dto;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PayoutApproveResponse {

	
	private Long journalNumber;
	private Long debitAccount;
	private Long creditAccount;
	private BigDecimal amount;
	private String crditIcode;
	private String debitIcode;
	private String outMessage;
	private String outStatus;
	private String statusCode;
	private Integer sqlCode;
	private String sqlErrorMsg;
	private Long payoutId;
	private String benefiaryPaymentId;
	private String paymentIdOld;
	 
	private Blob apiRequest;
	private String apiRequestString;
	private Blob apiResponse;
	private String apiResponseString;
	private Date requestDate;
	private Date responseDate;
	private String type;
	private String apiUrl;
	private String remark;
	private String errorResponse;

}
