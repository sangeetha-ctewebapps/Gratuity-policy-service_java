package com.lic.epgs.gratuity.policy.integration.dto;

/**
 * 
 */


import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author dhatchanamoorthy M
 * @param <T>
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundResponseDto<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient T responseData;
	/***
	 * private InterestFundResponseDto interestFundResponse;
	 */

	private String message;

	private String code;

	private String status;
	private @Default Integer statusId = 0;

	private String exception;

	private Integer pageNo;
	private Integer perPage;
	private Integer total;
	private String columnSort;
	private String columnOrder;
	private List<String> errors;

}
