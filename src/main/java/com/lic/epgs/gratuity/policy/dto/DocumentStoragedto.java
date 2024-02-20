package com.lic.epgs.gratuity.policy.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentStoragedto {
	private Long id;
	private Long policyid;
	private Long doctypeId;
	private byte[] docBlob;
	private boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedby;
	private Date modifiedDate;

}
