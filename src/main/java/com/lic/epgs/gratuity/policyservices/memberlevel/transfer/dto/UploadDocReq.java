package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.io.Serializable;
import java.util.List;

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
public class UploadDocReq {
	
	//private static final long serialVersionUID = 1L;
	 Long transferRequisitionId;
	//private String remarks;
	 String createdBy;
	 String picklistItemId;
	 String fileBase64;
	 
	 

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
 
	}
}
