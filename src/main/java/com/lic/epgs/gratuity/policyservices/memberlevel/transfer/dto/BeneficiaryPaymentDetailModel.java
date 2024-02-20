package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeneficiaryPaymentDetailModel {
	
	private String payoutSourceModule;
	private String paymentSourceRefNumber;
	private String beneficiaryName;
	private String beneficiaryBankName;
	private String beneficiaryBankIfsc;
	private String beneficiaryBranchName;
	private String beneficiaryAccountType;
	private String beneficiaryAccountNumber;
	private Double paymentAmount;
	private Date effectiveDateOfPayment;
	private String paymentMode;
	//private String beneficiaryLei; //--optional
	//private String senderLei; //--optional
	private String unitCode;
	private String policyNumber;
	private String memberNumber;
	private String paymentCategory;
	private String paymentSubCategory;
	//private String nroAccount; //--optional
	//private String iban; //--optional
	private String remarks;
	//private String voucherNo; //--optional
	private String beneficiaryPaymentId;
	private String productCode;
	private String variantCode;
	private String operatinUnitType;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
