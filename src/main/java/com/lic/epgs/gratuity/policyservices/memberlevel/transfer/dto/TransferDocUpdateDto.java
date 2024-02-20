package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.io.Serializable;
import java.util.List;

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
public class TransferDocUpdateDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long transferRequisitionId;
	private String picklistItemId;
	private String isReceived;
	private String isUploaded;
	private String remarks;
	private String modifiedBy;
	private String createdBy;
	private String documentIndex;

	  private List<DocumentRequest> documentRequest;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}

}
