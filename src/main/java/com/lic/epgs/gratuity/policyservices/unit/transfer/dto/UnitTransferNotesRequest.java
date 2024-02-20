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
public class UnitTransferNotesRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long UnitTransferRequisitionId;
	private String UnitTransferNote;
	private String modifiedBy;
	private Date modifiedOn;
	private String createdBy;
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
 
	}

}
