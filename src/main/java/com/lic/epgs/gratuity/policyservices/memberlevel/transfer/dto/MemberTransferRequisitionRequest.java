package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberTransferRequisitionRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String transferRequestNumber;
	private String transferRequestDate;
	private String transferStatus;
	private String transferSubStatus;
	private String licId;
	private String isMemberUpload;
	private String role;	
	private String locationType;
	private String sentTo;
	private String remarks;
	private String uploadType;

}
