package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentRequest implements Serializable {

	private static final long serialVersionUID = 1L;
//	private Long transferRequisitionId;
//	private String createdBy;
//	private Date createdDate;
//	private String modifiedBy;
	private String documentIndex;
	private String picklistItemId;
	private String isReceived;
	private String isUploaded;
	private String folderIndexNo;
	//private String remarks;
	

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}

}
