package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferMemberDrCrReqModel {
	
	private double debitEquitableOutGo;
	private double debitReFundFirstPremium;
	private double debitReFundOtherFirstPremium;
	private double debitReFundRenewalPremium;
	private double debitGstRefundPremium;
	private double debitSr;	
	
	private double creditCoPaymentContra;
	private double creditEquitableContraTransferOut;
	private double debitEquitableContraTransferIn;
	private double creditEquitableIn;
	private double creditLifePremiumRenewalPremium;
	private double creditOtherFirstLifePremium;
	private double creditGstPremium;
	private double creditSr;
	
	private double debitCoPaymentContra;
	private double creditOsPaymentAtCpc;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}


}
