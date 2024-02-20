package com.lic.epgs.gratuity.policyservices.unit.transfer.dto;

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

public class UnitDocumentDetailsResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long transferDocumentDetailId;
	private Long transferRequisitionId;
	private String picklistItemId;
	private String isReceived;
	private String isUploaded;
	private String remarks;
	private String documentIndex;
	private String folderIndexNo;
	private String createdBy;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy", timezone="IST")
	private Date createdOn;
	private String modifiedBy;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy", timezone="IST")
	private Date modifiedOn;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}

}
