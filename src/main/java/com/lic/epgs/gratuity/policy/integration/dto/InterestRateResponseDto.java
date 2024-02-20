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
public class InterestRateResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient Object responseData;

	private String message;

	private String code;

	/** private List<String> errors; */

	private transient List<InterestErrorDto> errorData;

	private String status;
	private @Default Integer statusId = 0;

	private String exception;

	private Integer pageNo;
	private Integer perPage;
	private Integer total;
	private String columnSort;
	private String columnOrder;

}
