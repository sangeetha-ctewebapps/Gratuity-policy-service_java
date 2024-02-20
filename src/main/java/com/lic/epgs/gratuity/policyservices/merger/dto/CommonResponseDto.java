package com.lic.epgs.gratuity.policyservices.merger.dto;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient List<?> responseList;
	private transient Object responseData;
	//private PolicySearchDto policySearchDto;
	private String proposalId;
	private Long memberId;
	private Long memberStatus;
	private String transactionMessage;
	private String transactionStatus;
	private String message;
	private String code;
	private String other;
	@JsonProperty("Data")
	private transient Object data;
	@JsonProperty("Message")
	private String resMessage;
	private String status;
	private String mphCode;
	private String mphName;
	private String product;
	private String lineOfBusiness;
	private String policyStatus;
	private String date;
	private String financialYear;
	private String quarter;
	private String frequency;
	private String claimOnBoardNumber;
}
