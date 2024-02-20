package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequisitionModel {

	private Long transferRequisitionId;
	private Long transferRequestNumber;
	private String isBulk;
	private String role;
	private String locationType;	
	private Date transferRequestDate;
	private String userName;
	private String transferStatus;
	private String transferSubStatus;	
	private Date createdOn;
	private String createdBy;	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
									
	}
}
