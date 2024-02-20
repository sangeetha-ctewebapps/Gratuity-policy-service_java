package com.lic.epgs.gratuity.common.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberWithdrawaldto {
	private Long id;
	private Long ageFrom;
	private Long ageTo;
	private Float rate;
	private Date effectiveFrom;
	private Date effectiveTo;
	private boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
}
