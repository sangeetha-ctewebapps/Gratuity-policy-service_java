package com.lic.epgs.gratuity.quotation.member.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberBatchDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long batchId;
	private Long quotationId;
	private String quotationNo;
	private Long successCount;
	private Long failedCount;
	private Long totalCount;
	private String fileName;
	private String modifiedBy;
	private String createdBy;
	private Date modifiedOn;
	private Date createdOn;
	private String memberImportCount;
}
