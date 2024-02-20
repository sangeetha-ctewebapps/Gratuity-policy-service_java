package com.lic.epgs.gratuity.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationErrorDto {
	
	private Long errorId;
	private String errorCode;
	private String errorMessage;
	
}
