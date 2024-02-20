package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.io.Serializable;
import java.util.Date;

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
public class TransferSearchWithServiceResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long transferRequisitionId;
	private Long transferRequestNumber;
	private Long licId;
	//private Long membershipId;
	private String memberName;
	private String memberStatus;
	private String unitOut;
	private String unitIn;
	private String policyNumberIn;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy", timezone="IST")
	private Date transferRequestDate;
	private String transferSubStatus;
	private String isBulk;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}

}
