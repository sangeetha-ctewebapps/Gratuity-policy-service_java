package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.io.Serializable;
import java.util.List;

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
public class TransferMemberSearchWithFilterRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long licId;
	private Long policyNumberOut;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private String createdFromDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private String createdToDate;
	private List<String> unitCode;
	private List<String> transferSubStatus;
	private String transferType;
	private String isBulk;
		
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
 
	}

}
