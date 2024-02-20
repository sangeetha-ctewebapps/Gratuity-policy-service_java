package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberTransferApprovalRequest {

	private Long transferRequisitionId;
	private Long transferRequestNumber;
	private String locationType;
	private String role;
	private String userName;
	private String userCode;
	private String unitCode;
	private String transferType;
	private String policyNumberIn;
	private String policyNumberOut;		
	private String productIdOut;
	private String productIdIn;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}

}
