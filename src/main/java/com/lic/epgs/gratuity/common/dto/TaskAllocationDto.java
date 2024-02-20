package com.lic.epgs.gratuity.common.dto;

import java.util.Date;

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
public class TaskAllocationDto {
	
	private Long id;
	private Long taskNumber;
	private String makerId;
	private String checkerid;
	private String taskStatus;
	private String requestId;
	private Long officeId;
	private String locationType;
	private Long taskProcessTaskPrId;
	private String taskProcessTaskPrType;
	private Long modulePrimaryId;
	private String financialApproverId;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

}
