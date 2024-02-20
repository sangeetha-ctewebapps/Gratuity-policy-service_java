package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferMemberReqModel {

	private String refNo;
	private String mphCode;
	private String policyNo;
	private String lineOfBusiness;
	private String product;
	private String productVariant;
	private int iCodeForLob;
	private int iCodeForProductLine;
	private String iCodeForVariant;
	private int iCodeForBusinessType;
	private int iCodeForParticipatingType;
	private int iCodeForBusinessSegment;
	private int iCodeForInvestmentPortfolio;
	private Boolean PayoutApplicable;
	private String unitCode;
	private String userCode;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
