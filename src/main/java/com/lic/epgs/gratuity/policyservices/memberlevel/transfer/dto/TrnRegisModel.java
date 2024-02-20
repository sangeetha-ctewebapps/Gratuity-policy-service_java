package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.math.BigInteger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrnRegisModel {
	
	private String referenceIdType;
	private String van;
	//private BigInteger challanNo; //--optional
	private String policyNo;
	//private String proposalNo; //--optional
	//private String referenceDate; //--optional
	private String amount;
	private String amountType;
	private String productCode;
	private String variantCode;
	private String validityUptoDate;
	private int status;
	//private String createdBy; //--optional
	//private String poolAccountCode; //--optional
	private String bankUniqueId;
	private String reason;

	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
