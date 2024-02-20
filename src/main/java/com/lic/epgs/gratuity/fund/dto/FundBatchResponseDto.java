package com.lic.epgs.gratuity.fund.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Muruganandam
 * @param <T>
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundBatchResponseDto<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient T responseData;

	private String message;

	private String code;

	private String status;
	private @Default Integer statusId = 0;

	private Integer pageNo;
	private Integer perPage;
	private Integer total;
	private String columnSort;
	private String columnOrder;
	private String batchNumber;
	private List<String> errors;
	private @Default Map<String, String> summary = Collections.emptyMap();
	/**
	 * private Map<PolicyAndMemberDto, String> error = Collections.emptyMap();
	 * private Map<PolicyAndMemberDto, InterestFundResponseDto> success =
	 * Collections.emptyMap();
	 */
}