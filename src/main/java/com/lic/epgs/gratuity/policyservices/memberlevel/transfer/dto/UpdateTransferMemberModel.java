package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UpdateTransferMemberModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//private Long transferRequisitionId;
	private String role;
	private String locationType;
	private Long memberLicId;
	private String policyNumberIn;
	private String policyNumberOut;
	private String mphNameIn;
	private String mphNameOut;
	private String productIdIn;
	private String productIdOut;
	private String productNameIn;
	private String productNameOut;
	private String productVariantIn;
	private String productVariantOut;
	private String frequencyIn;
	private String unitIn;
	private String unitOut;
	private Long totalInterestAccrued;
	private String categoryOut;
	private String categoryIn;
	private String memberName;
	private String memberStatus;
	private Double refundPremiumOut; // PREMIUM_AMOUNT
	private Double refundGstOut; // GST_ON_PREMIUN
	private String isAccruedGratuityModified;
	private Long accruedGratuityExisting;
	private Long accruedGratuityNew;
	private String dateOfBirth;
	private String panNumber;
	private Long memberId;
	private Long pastSericve; // COMPLETED_YEARS_OF_SERVICE
	private String dateOfJoining;
	private String transferEffectiveDate;
	private String userName;
	private String isPremiumRefund;
	private String retainLicId;
	private Long policyIdOut;
	private Long policyIdIn;


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}

}
