package com.lic.epgs.gratuity.policyservices.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CommonNotesDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer noteId;
	private String noteContents;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private Long mergeId;
	private Boolean isActive;
	private Long surrenderId;
	private Long transferId;
	private String createdBy;
	private String modifiedBy;
}
