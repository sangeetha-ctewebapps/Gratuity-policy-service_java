package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MphDetailsResponse {

	private String mphName;
	private String bankName;
	private String accountNumber;
	private String ifscCode;	

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
