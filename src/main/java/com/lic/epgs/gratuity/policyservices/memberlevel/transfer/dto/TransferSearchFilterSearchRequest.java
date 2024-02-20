package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;
import java.io.Serializable;

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
public class TransferSearchFilterSearchRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long licId;
	private Long PolicyNumber;
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
 
	}
}
