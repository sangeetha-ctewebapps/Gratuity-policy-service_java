package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ServiceMemberTransferDetailsResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String role;
	private String locationType;
	private Long memberLicId;
	private Long policyNumberIn;
	private Long policyNumberOut;
	private String mphNameIn;
	private String mphNameOut;
	private String productIn;
	private String productOut;
	private String variantIn;
	private String variantOut;
	private String frequencyIn;
	private String unitCodeIn;
	private String unitCodeOut;
	private Long totalInterestAccrued;
	private String categoryOut;
	private String categoryIn;
	private String memberName;
	private String memberStatus;
	private Double refundPremiumOut; // PREMIUM_AMOUNT
	private Double refundGstOut; // GST_ON_PREMIUN
	private Long accruedGratuityExisting; // ACCRUED_GRATUITY_EXISTING
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy", timezone="IST")
	private Date dateOfBirth;
	private Long panNumber;
	private Long memberId;
	private Long pastSericve; // COMPLETED_YEARS_OF_SERVICE
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy", timezone="IST")
	private Date dateOfJoining;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy", timezone="IST")
	private Date transferEffectiveDate;
	private String userName;
	private Long accruedGratuityNew;
	private String isAccruedGratuityModified;
	private String isPremiumRefund;
	private Long tempPolicyId;	
	private Double accruedGratuityIn;
	private Double premiumAmountIn;
	private Double gstOnPremiumIn;	
	private String toMphRefund;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy", timezone="IST")
	private Date adjustedForDate;
	private String retainLicId;
	private String isLicIdExist;
	private Long policyIdOut;
	private Long policyIdIn;
	private String isBulk;
	private Long productIdIn;
	private Long productIdOut;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}

}
