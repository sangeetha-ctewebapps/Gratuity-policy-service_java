package com.lic.epgs.gratuity.policy.surrender.dto;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SurrenderDocUpdateDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long surrenderId;
	private String picklistItemId;
	private String isReceived;
	private String isUploaded;
	private String remarks;
	private String modifiedBy;
	private String documentIndex;

	  private List<DocumentRequest> documentRequest;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}

}
