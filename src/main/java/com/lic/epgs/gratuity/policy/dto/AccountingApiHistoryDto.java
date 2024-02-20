package com.lic.epgs.gratuity.policy.dto;

import java.sql.Clob;
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
public class AccountingApiHistoryDto {

	private Long id;
	private Date calledDate;
	private Clob apiUrl;
	private Clob apiRequestBody;
	private Clob apiResponseBody;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

}
