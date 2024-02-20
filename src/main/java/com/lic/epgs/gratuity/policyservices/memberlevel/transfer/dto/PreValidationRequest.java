package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreValidationRequest {
	
	private String eventName;
	private String transferType;
	private Long transferRequisitionId;
	private String policyNumberOut;
	private String policyNumberIn;
	private String productVariantOut;
	private String productVariantIn;
	private String unitCodeOut;
	private String unitCodeIn;
	private String productIdOut;
	private String productIdIn;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
		
	}

}
