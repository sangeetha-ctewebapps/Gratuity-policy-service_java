package com.lic.epgs.gratuity.policyservices.unit.transfer.dto;

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

public class SavePolicyUnitTransferDetailRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String role;
	private Long licId;
	private String locationType;
	private String userName;
	private String policyNumber;
	private String mphName;
	private String policyStatus;
	private String productName;
	private String productVariant;
	private Date policyStartDate;
	private Date policyEndDate;
	private String sourceUnit;
	private String destinationUnit;
	private Long policyAccountValue;
	private Long totalFundValue;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}

	

}
